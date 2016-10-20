package utils.database.entry;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mino.jdict.objects.activities.ExampleObject;
import com.mino.jdict.objects.activities.ItemDetailObject;

import java.util.ArrayList;

import utils.database.JMDictHelper;
import utils.grammar.InputUtils;

/**
 * Created by Dominik on 7/20/2015.
 */
public class Example {
    private JMDictHelper dbHelper;

    public Example(JMDictHelper inHelper) {
        dbHelper = inHelper;
    }

    public ItemDetailObject getBasicExampleDetails(int inSentenceSeq) {

        Cursor cursor = getExampleDetail(inSentenceSeq);
        ItemDetailObject obj = null;

        try {
            if (cursor != null && cursor.getCount() > 0) {

                int jp_a_index = cursor.getColumnIndex("sentence_jp_a");
                int eng_index = cursor.getColumnIndex("sentence_eng");

                do {
                    obj = new ItemDetailObject(cursor.getString(jp_a_index), cursor.getString(eng_index), false);
                    obj.setExample(inSentenceSeq);

                } while (cursor.moveToNext());
            }

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return obj;
    }


    public void getExampleDataFromCursor(ArrayList<ExampleObject> m_parts, Cursor cursor) {

        try {
            if (cursor != null && cursor.getCount() > 0) {

                int jp_a_index = cursor.getColumnIndex("sentence_jp_a");
                int eng_index = cursor.getColumnIndex("sentence_eng");
                int sentence_seq_index = cursor.getColumnIndex("sentence_seq");

                do {

                    ItemDetailObject obj = new ItemDetailObject(cursor.getString(jp_a_index), cursor.getString(eng_index), false);
                    obj.setExample(cursor.getInt(sentence_seq_index));

                    m_parts.add(new ExampleObject(obj));


                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public Cursor getExamplesByMeaning(String searchString, int offset, boolean inCertifiedOnly, int inLimit) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String romajiString = searchString.replace("'", "$");
        if (InputUtils.isStringAllKana(searchString)) {
            romajiString = InputUtils.getRomaji(searchString, false);
        }

        // by meaning - certified sprawdza tylko, czy jakiekolwiek słowo w zdaniu jest Certified, inaczej jest w by reading - tam patrzymy na konkretne słowo bo mamy powiązanie certified->sentenceword
        Cursor c;
        String sql;

        if (!inCertifiedOnly) {
            sql = " SELECT distinct Sentences.sentence_seq as sentence_seq, Sentences.sentence_jp_a as sentence_jp_a, Sentences_fts.sentence_eng as sentence_eng FROM Sentences, Sentences_fts \n" +
                    "  WHERE Sentences.sentence_seq=Sentences_fts.sentence_seq \n" +
                    "    AND  Sentences_fts.sentence_eng match '" + romajiString + "*' " + " limit " + String.valueOf(inLimit) + " offset " + String.valueOf(offset);
        } else {
            sql = " SELECT distinct Sentences.sentence_seq as sentence_seq, Sentences.sentence_jp_a as sentence_jp_a, Sentences_fts.sentence_eng as sentence_eng FROM Sentences, Sentences_fts  \n" +
                    "  join sentenceword SW on (Sentences.sentence_seq = SW.sentence_seq) WHERE Sentences.sentence_seq=Sentences_fts.sentence_seq  \n" +
                    "    AND  Sentences_fts.sentence_eng match '" + romajiString + "*' " + " and SW.certified glob 1 limit " + String.valueOf(inLimit) + " offset " + String.valueOf(offset);
        }

        c = db.rawQuery(sql, null);
        c.moveToFirst();
        return c;
    }

    public Cursor getExamplesByReading(String searchString, int offset, boolean inCertifiedOnly, int inLimit) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String hiraganaString = searchString;
        String katakanaString = searchString;

        String romajiString = searchString.replace("'", "$");

        if ((InputUtils.isStringAllRomaji(searchString)) || (InputUtils.isStringAllKana(searchString))) {

            romajiString = InputUtils.getRomaji(searchString, false);

            if (romajiString == null || romajiString.equals("")) {
                romajiString = searchString;
            }
        }

        if (InputUtils.isStringAllRomaji(searchString)) {
            hiraganaString = InputUtils.getKana(romajiString, InputUtils.KanaType.Hiragana);
            katakanaString = InputUtils.getKana(romajiString, InputUtils.KanaType.Katakana);
        }

        searchString = searchString.replace("'", "");
        romajiString = InputUtils.prepareQueryForSearchRomaji(romajiString);

        String sql = "select distinct S.sentence_seq, sentence_jp_a, sentence_eng from Sentences S join sentences_fts F on (F.sentence_seq match S.sentence_seq) \n" +
                "where S.sentence_seq in (select sentence_seq from ( \n" +
                "select sentence_seq, certified > 0 as c from sentenceword SW where " + (inCertifiedOnly ? " c = 1 and " : "")  + "\n" +
                "(\n" +
                "(SW.ID_r_ele in  (SELECT rowid from r_ele_fts where reb_romaji match '^" + romajiString + "*'  )   )\n" +
                "or (SW.ID_k_ele in (SELECT rowid from k_ele_fts where keb match  '^" + searchString + "*' )  )\n" +
                (katakanaString == null ? "" : ("or ( readingchangedform glob '" + katakanaString + "*' )\n")) +
                (hiraganaString == null ? "" : ("or ( readingchangedform glob '" + hiraganaString + "*' )\n")) +
                ") \n" + " limit " + String.valueOf(inLimit) + " offset " + String.valueOf(offset) +
                ")) ";

        Cursor c = db.rawQuery(sql, null);
        c.moveToFirst();
        return c;
    }

    public Cursor getExamplesCount(int ent_seq) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c;

        c = db.rawQuery("select distinct count(sentence_seq) as Count from " +

                " (select SW.sentence_seq from sentenceword SW where length(ID_sense) is null and \n" +
                "                (SW.ID_k_ele in (select rowid from k_ele_fts K where K.ent_seq match " + ent_seq + ") OR  " +
                "                (SW.ID_r_ele in (select rowid from r_ele_fts R where R.ent_seq match " + ent_seq + " )  ) )" +
                " limit 1000 )  S", null);

        c.moveToFirst();
        return c;
    }


    public Cursor getExampleDetail(int sentence_seq) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // re_restr zawiera jedynie takie czytania które są unikatowe dla danego keb. To nie znaczy, że takie unikatowe czytanie jest porządane.
        // zamiast brania po re_restr, bierzemy pierwsze z góry czytanie (po ID_r_ele)

        Cursor c = db.rawQuery("select orderInSentence, IFNULL(keb, R.reb) as word, RR.reb as rebSimple, IFNULL(K.ent_seq,R.ent_seq) as entry_seq, readingChangedForm, sentence_jp_a, sentence_eng, certified, ID_sense from sentences_fts F join sentences S on (F.sentence_seq match S.sentence_seq) join sentenceword ss ON (F.sentence_seq = SS.sentence_seq) " +
                "left join r_ele_fts R on (ss.ID_r_ele = R.rowid) " +
                "left join k_ele_fts K on (ss.ID_k_ele = K.rowid) " +
                "join r_ele_fts RR on (RR.ent_seq match entry_seq) " +
                "where S.sentence_seq = " + String.valueOf(sentence_seq) + " " +
                "group by orderInSentence, word, entry_seq, ID_sentenceword, rebSimple " +
                "order by orderInSentence, (word = rebSimple) desc, RR.rowid ", null);
        c.moveToFirst();
        return c;
    }

    public Cursor getExamples(int offset, int ent_seq) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c;

        // match na joinie ?
        String query = "select distinct S.sentence_seq, sentence_jp_a, sentence_eng from Sentences S join sentences_fts F on (F.sentence_seq match S.sentence_seq) join sentenceword SW ON (length(ID_sense) is null and s.sentence_seq = SW.sentence_seq) where " +
                " (SW.ID_k_ele in (select rowid from k_ele_fts K where K.ent_seq match " + String.valueOf(ent_seq) + "  ) OR  " +
                " (SW.ID_r_ele in (select rowid from r_ele_fts R where R.ent_seq match " + String.valueOf(ent_seq) + " ) ))" +
                " limit 6 offset " + String.valueOf(offset);

        c = db.rawQuery(query, null);

        c.moveToFirst();
        return c;
    }
}
