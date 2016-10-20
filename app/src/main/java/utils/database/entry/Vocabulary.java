package utils.database.entry;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import com.mino.jdict.objects.activities.VocabularyElementObject;

import utils.database.JMDictHelper;

/**
 * Created by Dominik on 7/20/2015.
 */
public class Vocabulary {
    JMDictHelper dbHelper;

    public enum ElementType { Kanji, Sentence, Entry }

    public Vocabulary(JMDictHelper inHelper) {
        dbHelper = inHelper;
    }

    public Cursor getEntries(int ID, ElementType inType) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "select ID_VocabListElement, character_seq, sentence_seq, ent_seq, date(CreationDate) as CreationDate, LastGuess, LastShown from VocabListelement where ID_VocabList = " + String.valueOf(ID) + " AND ";

        switch (inType) {
            case Entry:
                sql += " ent_seq IS NOT NULL ";
                break;
            case Sentence:
                sql += " sentence_seq IS NOT NULL ";
                break;
            case Kanji:
                sql += " character_seq IS NOT NULL ";
                break;
        }

        sql += " order by datetime(CreationDate) desc";
        Cursor c = db.rawQuery(sql, null);

        c.moveToFirst();
        return c;
    }

    public Cursor getNotesCount() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(" SELECT count(*) as Count FROM Note ", null);

        c.moveToFirst();
        return c;
    }

    public Cursor getNotes(int limit, int offset) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("select ID_Note, character_seq, sentence_seq, ent_seq, date(Lasteditdate) as Lasteditdate, Note from Note order by datetime(LastEditDate) desc LIMIT " + String.valueOf(limit) + " OFFSET " + String.valueOf(offset), null);

        c.moveToFirst();
        return c;
    }

    public Cursor getFolders() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("select V.*, count(distinct VLE.ID_VocabListElement) as EntryCount, count(distinct V2.ID_VocabList) as SublistCount from VocabList V \n" +
                "left join VocabListElement VLE on (V.ID_VocabList = VLE.ID_VocabList) \n" +
                "left join VocabList V2 on (V2.IDVocabList = V.ID_VocabList)\n" +
                "where V.IDVocabList is NULL group by V.ID_VocabList ", null);


        c.moveToFirst();
        return c;
    }

    public Cursor getVocabListElements(int id, ElementType idType) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String seqString = "";

        switch (idType) {
            case Sentence:
                seqString = "sentence_seq = " + String.valueOf(id);
                break;

            case Entry:
                seqString = "ent_seq = " + String.valueOf(id);
                break;

            case Kanji:
                seqString = "character_seq = " + String.valueOf(id);
                break;
        }

        Cursor c = db.rawQuery("select * from VocabListElement where " + seqString, null);

        c.moveToFirst();
        return c;
    }

    public Cursor getAllFoldersCount() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("select count(*) as Count from VocabList ", null);

        c.moveToFirst();
        return c;
    }

    public Cursor getAllFolders(int id, ElementType idType) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String seqString = "";

        switch (idType) {
            case Sentence:
                seqString = "VLE.sentence_seq = " + String.valueOf(id);
                break;

            case Entry:
                seqString = "VLE.ent_seq = " + String.valueOf(id);
                break;

            case Kanji:
                seqString = "VLE.character_seq = " + String.valueOf(id);
                break;
        }
        Cursor c;

        // wsparcie with recursive od 3.8
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            c = db.rawQuery("\n" +
                    "        with recursive\n" +
                    "        under_fav(Name, ParentName, ID_VocabList, IDVocabList, level, IsLearnGroup, CreationDate, CurrentState) AS (\n" +
                    "        select Name, NULL as ParentName,  V.ID_VocabList, NULL as IDVocabList, 0 as level,  IsLearnGroup, V.CreationDate, max(ID_VocabListElement) as CurrentState " +

                    " FROM VocabList V left join VocabListElement VLE ON (V.ID_vocabList = VLE.ID_VocabList AND " + seqString + ") " +
                    " where V.IDVocabList is null " +
                    " group by V.ID_VocabList " +
                    "                UNION ALL\n" +
                    "                SELECT vocabList.Name as Name, under_fav.Name as ParentName, vocabList.ID_VocabList, vocabList.IDVocabList, under_fav.level + 1, vocabList.IsLearnGroup, vocabList.CreationDate, max(ID_VocabListElement) as CurrentState\n" +
                    "                FROM vocabList\n" +
                    "                LEFT JOIN VocabListElement VLE on (vocabList.ID_VocabList = VLE.ID_VocabList AND " + seqString + ")\n" +
                    "                join under_fav on vocablist.IDVocabList = under_fav.ID_VocabList\n" +
                    "                group by vocabList.ID_VocabList\n" +
                    "                order by 5 desc\n" +
                    "        ) select ID_VocabList, IDVocabList, substr('..........',1,level*3) || Name as Name, CreationDate, IsLearnGroup, ParentName, CurrentState, level from under_fav;\n", null);
        } else {
            c = db.rawQuery("\t\tselect ID_VocabList, IDVocabList, Name as Name, CreationDate, IsLearnGroup, ParentName, CurrentState, 0 as level from (\n" +
                    "\t\t  SELECT vocabList.Name as Name, NULL as ParentName, vocabList.ID_VocabList, vocabList.IDVocabList, 0, vocabList.IsLearnGroup, vocabList.CreationDate, max(ID_VocabListElement) as CurrentState\n" +
                    "                FROM vocabList\n" +
                    "                LEFT JOIN VocabListElement VLE on (vocabList.ID_VocabList = VLE.ID_VocabList AND " + seqString + ")\n" +
                    "                group by vocabList.ID_VocabList\n" +
                    "                order by 5 desc) s", null);
        }

        c.moveToFirst();
        return c;
    }

    public Cursor getSubFolders(int ID) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("select V.*, count(distinct VLE.ID_VocabListElement) as EntryCount, count(distinct V2.ID_VocabList) as SublistCount from VocabList V \n" +
                "left join VocabListElement VLE on (V.ID_VocabList = VLE.ID_VocabList) \n" +
                "left join VocabList V2 on (V2.IDVocabList = V.ID_VocabList)\n" +
                " where V.IDVocabList = " + String.valueOf(ID) + " group by V.ID_VocabList ", null);


        c.moveToFirst();
        return c;
    }

    public Cursor getNote(int id, ElementType idType) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = null;
        switch (idType) {
            case Kanji:
                c = db.rawQuery("SELECT * FROM Note where character_seq = " + String.valueOf(id) + " ", null);
                break;

            case Sentence:
                c = db.rawQuery("SELECT * FROM Note where sentence_seq = " + String.valueOf(id) + " ", null);
                break;

            case Entry:
                c = db.rawQuery("SELECT * FROM Note where ent_seq = " + String.valueOf(id) + " ", null);
                break;
        }

        c.moveToFirst();
        return c;
    }

    public void insertNote(int id, ElementType idType, String text) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String query = "";

        switch (idType) {
            case Kanji:
                query = "INSERT INTO Note (character_seq, sentence_seq, ent_seq, Lasteditdate, Note) VALUES (" + String.valueOf(id) + ", null, null, datetime('now'), '" + text + "')";
                break;

            case Sentence:
                query = "INSERT INTO Note (sentence_seq, character_seq, ent_seq, Lasteditdate, Note) VALUES (" + String.valueOf(id) + ", null, null, datetime('now'), '" + text + "')";
                break;

            case Entry:
                query = "INSERT INTO Note (ent_seq, sentence_seq, character_seq, Lasteditdate, Note) VALUES (" + String.valueOf(id) + ", null, null, datetime('now'), '" + text + "')";
                break;
        }

        db.execSQL(query);
    }

    public void updateNote(int id, ElementType idType, String text) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String query = "";

        switch (idType) {
            case Kanji:
                query = "UPDATE Note SET Note = '" + text + "', Lasteditdate = datetime('now') WHERE character_seq = " + String.valueOf(id);
                break;

            case Sentence:
                query = "UPDATE Note SET Note = '" + text + "', Lasteditdate = datetime('now') WHERE sentence_seq = " + String.valueOf(id);
                break;

            case Entry:
                query = "UPDATE Note SET Note = '" + text + "', Lasteditdate = datetime('now') WHERE ent_seq = " + String.valueOf(id);
                break;
        }

        db.execSQL(query);
    }

    public void removeFolder(int id) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String query1 = "DELETE FROM VocabListElement WHERE ID_VocabList = " + String.valueOf(id);
        db.execSQL(query1);

        String query2 = "DELETE FROM VocabList WHERE ID_VocabList = " + String.valueOf(id);
        db.execSQL(query2);

        removeSubFolders(id);
    }

    public void removeSubFolders(int id) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT ID_VocabList from VocabList where IDVocabList = " + String.valueOf(id) + " ", null);

        c.moveToFirst();

        if (c.getCount() > 0) {

            do {
                removeFolder(c.getInt(0));

            } while (c.moveToNext());
        }

        c.close();
    }

    public void insertEntry(int id, ElementType idType, int idVocabList) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String query = "";

        switch (idType) {
            case Kanji:
                query = "INSERT INTO VocabListElement (character_seq, sentence_seq, ent_seq, CreationDate, ID_VocabList) VALUES (" + String.valueOf(id) + ", null, null, datetime('now'), '" + String.valueOf(idVocabList) + "')";
                break;

            case Sentence:
                query = "INSERT INTO VocabListElement (sentence_seq, character_seq, ent_seq, CreationDate, ID_VocabList) VALUES (" + String.valueOf(id) + ", null, null, datetime('now'), '" + String.valueOf(idVocabList) + "')";
                break;

            case Entry:
                query = "INSERT INTO VocabListElement (ent_seq, sentence_seq, character_seq, CreationDate, ID_VocabList) VALUES (" + String.valueOf(id) + ", null, null, datetime('now'), '" + String.valueOf(idVocabList) + "')";
                break;
        }

        db.execSQL(query);
    }

    public void removeEntryFromList(int id, ElementType idType, int idVocabList) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String query = "DELETE FROM VocabListElement WHERE ID_VocabList = " + String.valueOf(idVocabList) + " AND ";

        switch (idType) {
            case Kanji:
                query += " character_seq = " + String.valueOf(id);
                break;

            case Sentence:
                query += " sentence_seq = " + String.valueOf(id);
                break;

            case Entry:
                query += " ent_seq = " + String.valueOf(id);
                break;
        }

        db.execSQL(query);
    }

    public void removeEntryFromList(int id, VocabularyElementObject obj) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String query = "DELETE FROM VocabListElement WHERE ID_VocabList = " + String.valueOf(id) + " AND ";

        if (obj.ListObject.get() != null) {
            query += " ent_seq = " + obj.EntSeq.get();
        } else if (obj.ExampleObject.get() != null) {
            query += " sentence_seq = " + obj.SentenceSeq.get();
        } else if (obj.KanjiObject.get() != null) {
            query += " character_seq = " + obj.CharacterSeq.get();
        }

        db.execSQL(query);
    }

    public long insertFolder(String name) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Name", name);
        return db.insert("VocabList", null, values);
        //db.close();
    }

    public long insertFolder(String name, int IDVocabList) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("IDVocabList", IDVocabList);
        values.put("Name", name);
        return db.insert("VocabList", null, values);
        //db.close();
    }

    public void deleteNote(int id, ElementType idType) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String query = "";

        switch (idType) {
            case Kanji:
                query = "DELETE FROM Note WHERE character_seq = " + String.valueOf(id);
                break;

            case Sentence:
                query = "DELETE FROM Note WHERE sentence_seq = " + String.valueOf(id);
                break;

            case Entry:
                query = "DELETE FROM Note WHERE ent_seq = " + String.valueOf(id);
                break;
        }

        db.execSQL(query);
    }
}
