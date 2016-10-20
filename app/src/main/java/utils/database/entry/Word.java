package utils.database.entry;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Pair;

import com.mino.jdict.interfaces.IFactory;
import com.mino.jdict.models.basic.SearchValues;
import com.mino.jdict.objects.activities.ARecentObject;
import com.mino.jdict.objects.activities.ListObject;
import com.mino.jdict.objects.activities.RecentObject;
import com.mino.jdict.objects.basic.GrammarObject;
import com.mino.jdict.objects.basic.SenseObject;

import org.acra.ACRA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import utils.database.JMDictHelper;
import utils.grammar.InputUtils;
import utils.other.StringUtils;

/**
 * Created by Dominik on 7/20/2015.
 */
public class Word {
    JMDictHelper dbHelper;

    public Word(JMDictHelper inHelper) {
        dbHelper = inHelper;
    }


    public Map<Integer, ListObject> getBasicDetails2(ArrayList<Integer> inSelectedObjectEntSeq, boolean inShowArrow) {
        //ArrayList<ListObject> result;
        ArrayList<ListObject> result = new ArrayList<ListObject>();
        Map<Integer, ListObject> resultMap = new HashMap<Integer, ListObject>();

        Cursor cursor = getEntries(inSelectedObjectEntSeq);
        try {
            if (cursor != null) {

                getDataFromCursor(result, cursor, false, inShowArrow, false, false);

                if (!result.isEmpty()) {
                    for (ListObject obj : result) {
                        resultMap.put(obj.ID.get(), obj);
                    }

                    return resultMap;
                } else return null;
            } else return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public ListObject getBasicDetails(int inSelectedObjectEntSeq, boolean inShowArrow) {
        ArrayList<ListObject> result;
        result = new ArrayList<ListObject>();

        Cursor cursor = getEntry(inSelectedObjectEntSeq);
        try {
            if (cursor != null) {
                getDataFromCursor(result, cursor, false, inShowArrow, true, false);

                if (!result.isEmpty()) {
                    return result.get(0);
                } else return null;
            } else return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public Cursor getEntry(String searchString, boolean commonWordsOnly, int inSpinnerSelectedItem) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // additional
        if (searchString.contains("*") || searchString.contains("＊")) return null;

        searchString = searchString.replace("'", "$");

        boolean rebRomajiMode;
        boolean allKanaMode = false;

        String romajiSearchString = "";

        if ((rebRomajiMode = InputUtils.isStringAllRomaji(searchString)) || (allKanaMode = InputUtils.isStringAllKana(searchString))) {

            romajiSearchString = InputUtils.getRomaji(searchString, false);

            if (romajiSearchString == null || romajiSearchString.equals("")) {
                romajiSearchString = searchString;
            }
        }

        romajiSearchString = InputUtils.prepareQueryForSearchRomaji(romajiSearchString);
        ArrayList<GrammarObject> simpleForms = InputUtils.getPossibleSimpleForms(searchString);

        String query = "SELECT DISTINCT _id, conjugated, JLPT_level, Pos, IsCommon, grammarChain FROM ( ";

        if (rebRomajiMode) {
            query += "SELECT DISTINCT _id, conjugated, JLPT_level, Pos, IsCommon, grammarChain FROM (" +
                    "SELECT DISTINCT E.ent_seq as _id, 0 as conjugated, JLPT_level, E.Pos, IsCommon, '' as grammarChain FROM entry E " +
                    "INNER JOIN r_ele_fts R ON E.ent_seq = R.ent_seq  " +
                    "INNER JOIN pos P ON P.ent_seq = E.ent_seq " +
                    "LEFT JOIN misc M ON M.ent_seq = E.ent_seq " +
                    "WHERE reb_romaji match '^" + romajiSearchString + "' and length(reb_romaji) = " + romajiSearchString.length() + "  " + InputUtils.getVocabularySpinnerItem(inSpinnerSelectedItem) +
                    " UNION ALL " +
                    "SELECT DISTINCT E.ent_seq as _id, 0 as conjugated, JLPT_level, E.Pos, IsCommon, '' as grammarChain FROM entry E " +
                    "INNER JOIN gloss_fts G ON G.ent_seq = E.ent_seq  " +
                    " join pos P ON (G.ent_seq = P.ent_seq) LEFT JOIN misc M ON G.ent_seq = M.ent_seq  " +
                    "WHERE gloss match '^" + searchString + "' and length(gloss) = " + searchString.length() + " " + InputUtils.getVocabularySpinnerItem(inSpinnerSelectedItem) + " collate nocase)  group by _id, conjugated, JLPT_level, Pos, IsCommon, grammarChain ";
        } else if (allKanaMode) {

            query += "SELECT DISTINCT _id, conjugated, JLPT_level, Pos, IsCommon, grammarChain FROM (" +
                    "SELECT DISTINCT E.ent_seq as _id, 0 as conjugated, JLPT_level, E.Pos, IsCommon, '' as grammarChain FROM entry E " +
                    "INNER JOIN r_ele_fts R ON E.ent_seq = R.ent_seq  " +
                    "INNER JOIN pos P ON P.ent_seq = E.ent_seq " +
                    "LEFT JOIN misc M ON M.ent_seq = E.ent_seq " +
                    "WHERE reb_romaji match '^" + romajiSearchString + "' and length(reb_romaji) = " + romajiSearchString.length() + " "
                    + InputUtils.getVocabularySpinnerItem(inSpinnerSelectedItem) + " )  group by _id, conjugated, JLPT_level, Pos, IsCommon, grammarChain ";
        } else {
            query +=
                    "SELECT DISTINCT _id, conjugated, JLPT_level, Pos, IsCommon, grammarChain FROM (" +
                            "SELECT DISTINCT E.ent_seq as _id, 0 as conjugated, JLPT_level, E.Pos, IsCommon,  '' as grammarChain FROM entry E " +
                            "INNER JOIN k_ele_fts K ON E.ent_seq = K.ent_seq " +
                            "INNER JOIN pos P ON P.ent_seq = E.ent_seq " +
                            "LEFT JOIN misc M ON M.ent_seq = E.ent_seq " +
                            "WHERE keb match '^" + searchString + "' and length(keb) = " + searchString.length() + " " + InputUtils.getVocabularySpinnerItem(inSpinnerSelectedItem) + " )  group by _id, conjugated, JLPT_level, Pos, IsCommon, grammarChain  ";
        }

        if (simpleForms != null && simpleForms.size() > 0) {

            for (GrammarObject simpleForm : simpleForms) {
                if (!simpleForm.getWordType().isEmpty()) {
                    String romaji = InputUtils.getRomaji(simpleForm.getSimpleForm(), false);
                    int lengthRomaji = romaji.length();

                    query += " UNION ALL SELECT DISTINCT E.ent_seq as _id, 1 as conjugated, JLPT_level, P.[pos], IsCommon, '"
                            + simpleForm.getGrammarChain() + "' as grammarChain FROM entry E INNER JOIN r_ele_fts R ON E.ent_seq = R.ent_seq "
                            + " LEFT JOIN misc M ON M.ent_seq = E.ent_seq INNER JOIN pos P ON E.ent_seq = P.ent_seq WHERE "
                            //  + " 1 = 1 "
                            + " (P.pos = '" + simpleForm.getWordType() + "' OR P.pos = 'exp') "
                            + InputUtils.getVocabularySpinnerItem(inSpinnerSelectedItem)
                            + " AND reb_romaji match '^" + romaji + "' and length(reb_romaji) = "
                            + lengthRomaji + "  group by _id, conjugated, JLPT_level, P.Pos, IsCommon, grammarChain " +
                            " UNION ALL SELECT DISTINCT E.ent_seq as _id, 1 as conjugated, JLPT_level, P.[pos], IsCommon, '"
                            + simpleForm.getGrammarChain() + "' as grammarChain FROM entry E INNER JOIN k_ele_fts K ON E.ent_seq = K.ent_seq "
                            + " LEFT JOIN misc M ON M.ent_seq = E.ent_seq INNER JOIN pos P ON E.ent_seq = P.ent_seq WHERE "
                            //  " 1 = 1 "
                            + " (P.pos =  '" + simpleForm.getWordType() + "' OR P.pos = 'exp') "
                            + InputUtils.getVocabularySpinnerItem(inSpinnerSelectedItem)
                            + " AND keb match '^" + simpleForm.getSimpleForm() + "' and length(keb) = "
                            + simpleForm.getSimpleForm().length() + " group by _id, conjugated, JLPT_level, P.Pos, IsCommon, grammarChain";
                } else {
                    query += " UNION ALL SELECT DISTINCT E.ent_seq as _id, 1 as conjugated, JLPT_level, P.[pos], IsCommon, '"
                            + simpleForm.getGrammarChain() + "' as grammarChain FROM entry E INNER JOIN r_ele_fts R ON E.ent_seq = R.ent_seq "
                            + " LEFT JOIN misc M ON M.ent_seq = E.ent_seq INNER JOIN pos P ON E.ent_seq = P.ent_seq WHERE reb_romaji match '^"
                            + simpleForm.getSimpleForm() + "' and length(reb_romaji) = " + simpleForm.getSimpleForm().length() + " "
                            + InputUtils.getVocabularySpinnerItem(inSpinnerSelectedItem) + "  group by _id, conjugated, JLPT_level, P.Pos, IsCommon, grammarChain " +
                            " UNION ALL SELECT DISTINCT E.ent_seq as _id, 1 as conjugated, JLPT_level, P.[pos], IsCommon, '"
                            + simpleForm.getGrammarChain() + "' as grammarChain FROM entry E INNER JOIN k_ele_fts K ON E.ent_seq = K.ent_seq "
                            + "LEFT JOIN misc M ON M.ent_seq = E.ent_seq INNER JOIN pos P ON E.ent_seq = P.ent_seq WHERE keb match '^"
                            + simpleForm.getSimpleForm() + "' and length(keb) = " + simpleForm.getSimpleForm().length() + " "
                            + InputUtils.getVocabularySpinnerItem(inSpinnerSelectedItem) + "  group by _id, conjugated, JLPT_level, P.Pos, IsCommon, grammarChain ";
                }

            }

        }
        query += " ) where _id is not null " + (commonWordsOnly ? "and IsCommon = 1 " : "")
                + " ORDER BY JLPT_level desc, IsCommon desc ";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        return c;
    }


    public void insertRecentWordsV2(ARecentObject recentObj) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String pureReb = recentObj.getPureReb();
        if (pureReb != null && !pureReb.isEmpty()) {
            pureReb = pureReb.replace("'", "''");
        } else {
            pureReb = "";
        }

        String pureKeb = recentObj.getPureKeb();
        if (pureKeb != null && !pureKeb.isEmpty()) {
            pureKeb = pureKeb.replace("'", "''");
        } else {
            pureKeb = "";
        }

        String query = "INSERT INTO RecentWords (ent_seq, reb, keb, data_type, addedDate) VALUES (" + recentObj.ID.get() + ",'" + pureReb + "','" + pureKeb + "', " + String.valueOf(recentObj.DataType.get()) + ",date('now'))";

        db.execSQL(query);
    }

    public void insertRecentWords(ARecentObject recentObj) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String pureReb = recentObj.getPureReb();
        if (pureReb != null && !pureReb.isEmpty()) {
            pureReb = pureReb.replace("'", "''");
        } else {
            pureReb = "";
        }

        String pureKeb = recentObj.getPureKeb();
        if (pureKeb != null && !pureKeb.isEmpty()) {
            pureKeb = pureKeb.replace("'", "''");
        } else {
            pureKeb = "";
        }

        String query = "INSERT INTO RecentWords (ent_seq, reb, keb, data_type, addedDate) VALUES (" + recentObj.ID.get() + ",'" + pureReb + "','" + pureKeb + "', " + String.valueOf(recentObj.DataType.get()) + ",date('now'))";

        db.execSQL(query);
    }


    //todo zmiana na r_ele_fts
    public Cursor getKebAlternativeSpecialReading(int ent_seq, String keb) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "select reb, re_nokanji from r_ele_fts R left join re_restr RR on (R.rowid = RR.ID_r_ele) \n" +
                "join (\n" +
                "select ent_seq, keb from k_ele_fts K\n" +
                "where K.keb match '^" + keb + "' and length(K.keb) = " + keb.length() + ") K on (K.ent_seq = R.ent_seq) \n" +
                "where R.ent_seq match " + String.valueOf(ent_seq) + " \n" +
                " and (re_restr = K.keb OR re_restr IS NULL) order by re_nokanji";

        Cursor c = db.rawQuery(query, null);

        c.moveToFirst();
        return c;
    }

    public ArrayList<Pair<String, Boolean>> getRebForKeb(String keb, ListObject mSelectedObject) {

        ArrayList<Pair<String, Boolean>> readings = new ArrayList<Pair<String, Boolean>>();

        // szukamy po relacji restr
        Cursor c = dbHelper.getWord().getKebAlternativeSpecialReading(mSelectedObject.ID.get(), keb);
        if (c != null && c.getCount() > 0) {

            int rebIndex = c.getColumnIndex("reb");
            int reNoKanjiIndex = c.getColumnIndex("re_nokanji");

            do {

                readings.add(new Pair<String, Boolean>(c.getString(rebIndex), c.getShort(reNoKanjiIndex) == 1));

            } while (c.moveToNext());

            c.close();

        } else {
            // jak nie ma relacji robimy LCS-a
            String romajiKeb = InputUtils.getRomaji(keb, false);

            int bestMatchSize = 0;
            List<Integer> bestMatchList = new ArrayList<Integer>();

            for (int j = 0; j < mSelectedObject.getRebListLen(); j++) {

                String romaji = InputUtils.getRomaji(mSelectedObject.getFromRebList(j), false);
                int matchSize = StringUtils.longestSubstring(romaji, romajiKeb).length();
                if (matchSize > bestMatchSize) {

                    bestMatchSize = matchSize;

                    bestMatchList.clear();
                    bestMatchList.add(j);
                } else if (matchSize == bestMatchSize) {
                    bestMatchList.add(j);
                }
            }

            for (int match : bestMatchList) {
                readings.add(new Pair<String, Boolean>(mSelectedObject.getFromRebList(match), false));
            }
        }

        return readings;
    }


    public Cursor getEntries(ArrayList<Integer> list) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String entries = " ( ";

        for (int ent_seq : list) {
            entries += String.valueOf(ent_seq);
            entries += ",";
        }

        entries = entries.substring(0, entries.length() - 1);
        entries += " ) ";

        String query =
                "SELECT DISTINCT E.ent_seq as _id, 0 as conjugated, JLPT_level, Pos, IsCommon, '' as grammarChain FROM entry E inner JOIN r_ele_fts R ON R.ent_seq match E.ent_seq "
                        + " WHERE _id in " + entries;

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        return c;
    }

    public Cursor getEntry(int ent_seq) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query =
                "SELECT DISTINCT E.ent_seq as _id, 0 as conjugated, JLPT_level, Pos, IsCommon, '' as grammarChain FROM entry E inner JOIN r_ele_fts R ON R.ent_seq match E.ent_seq "
                        + " WHERE _id = " + String.valueOf(ent_seq);

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        return c;
    }

    public Cursor getRecentWords(int offset, int data_type) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String offsetString = "";

        if (offset > 0) {
            offsetString = " OFFSET " + Integer.toString(offset);
        }

        String query = "SELECT ent_seq, reb, keb FROM RecentWords WHERE data_type = " + String.valueOf(data_type) + " order by ID_RecentWords desc LIMIT 21 " + offsetString;

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        return c;

    }

    public Cursor getGlossNotEng(int ent_seq) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT group_concat(gloss.gloss, ' ,') as glossgroup FROM gloss_fts where Ent_seq = " + String.valueOf(ent_seq) + " ", null);

        c.moveToFirst();
        return c;
    }

    public void getDataFromCursor(ArrayList<ListObject> m_parts, Cursor cursor, Boolean romaji, Boolean inShowArrow, Boolean inGetGlossDetail, Boolean inHasLength) {

        Map<Integer, ArrayList<String>> reb;
        Map<Integer, ArrayList<String>> keb;
        Map<Integer, ArrayList<SenseObject>> sense = new HashMap<Integer, ArrayList<SenseObject>>();
        ArrayList<Integer> entries = new ArrayList<Integer>();
        int prevLength = -1;

        if (cursor != null) {

            try {
                int entryIDIndex = cursor.getColumnIndex("_id");

                //Boolean hasConjugated = cursor.getColumnCount() > 3;
                Boolean isConjugated;

                /* Check if at least one Result was returned. */
                if (cursor.isFirst()) {
                    int i = 0;
                    /* Loop through all Results */
                    do {
                        i++;

                        int length = 0;
                        if (inHasLength && cursor.getColumnCount() > 8) {

                            length = cursor.getInt(8);

                            if (length == prevLength) {
                                continue;
                            } else {
                                prevLength = length;
                            }
                        }

                        int entryID = cursor.getInt(entryIDIndex);
                        int jlptLevel;

                        isConjugated = cursor.getInt(1) == 1;
                        jlptLevel = cursor.getInt(2);
                        String pos = cursor.getString(3);
                        String distinct_pos = pos;

                        if (!pos.isEmpty()) {

                            distinct_pos = "";
                            String[] pos_split = pos.split(";");


                            for (String s : pos_split) {

                                if (!distinct_pos.contains(s)) {
                                    distinct_pos += s;
                                    distinct_pos += ";";
                                }
                            }

                            if (!distinct_pos.isEmpty()) {
                                distinct_pos = distinct_pos.substring(0, distinct_pos.length() - 1);
                            }
                        }

                        int isCommon = cursor.getShort(4);
                        String grammarChain = "";

                        if (isConjugated) {
                            grammarChain = cursor.getString(5);
                        }

                        String misc = "";

                        if (cursor.getColumnCount() > 6) {
                            misc = cursor.getString(6);
                        }

                        entries.add(entryID);

                        if (inGetGlossDetail) {
                            sense.put(entryID, dbHelper.getSense().getSenseObjects(inGetGlossDetail, null, entryID));
                        }

                        m_parts.add(new ListObject(entryID, isConjugated, jlptLevel, distinct_pos, misc, length, isCommon == 1, grammarChain, inShowArrow));

                    } while (cursor.moveToNext());
                }

            } finally {
                cursor.close();
            }

            reb = getReb(entries);
            keb = getKeb(entries);

            if (!inGetGlossDetail) {
                sense = dbHelper.getSense().getSenseObjectForMultipleEntries(entries);
            }


            for (ListObject obj : m_parts) {

                ArrayList<String> currentReb = reb.get(obj.ID.get());
                ArrayList<String> currentKeb = keb.get(obj.ID.get());
                ArrayList<SenseObject> currentSense = sense.get(obj.ID.get());

                obj.setRebKebSense(currentReb, currentKeb, currentSense);
            }
        }
    }

    private Map<Integer, ArrayList<String>> getKeb(ArrayList<Integer> entryIDs) {

        Map<Integer, ArrayList<String>> res = new HashMap<Integer, ArrayList<String>>();
        if (entryIDs.size() == 0) return res;

        Cursor cursor2 = getKele(entryIDs);

        ArrayList<String> current = new ArrayList<String>();

        if (cursor2 != null) {

            try {
                int kebIndex = cursor2.getColumnIndex("keb");
                int entIndex = cursor2.getColumnIndex("ent_seq");

                if (cursor2.isFirst()) {
                    int currentEntSeq = 0;

                    do {

                        int ent_seq = cursor2.getInt(entIndex);

                        if (currentEntSeq != ent_seq) {
                            if (currentEntSeq > 0) {
                                res.put(currentEntSeq, current);
                                current = new ArrayList<String>();
                            }

                            currentEntSeq = ent_seq;
                        }

                        String keb = cursor2.getString(kebIndex);
                        current.add(keb);

                    } while (cursor2.moveToNext());

                    if (currentEntSeq > 0) {
                        res.put(currentEntSeq, current);
                    }

                }
            } finally {
                cursor2.close();
            }
        }
        return res;
    }


    private Map<Integer, ArrayList<String>> getReb(ArrayList<Integer> entryIDs) {

        Map<Integer, ArrayList<String>> res = new HashMap<Integer, ArrayList<String>>();

        if (entryIDs.size() == 0) return res;

        Cursor cursor2 = getRele(entryIDs);

        ArrayList<String> current = new ArrayList<String>();

        if (cursor2 != null) {

            try {
                int rebIndex = cursor2.getColumnIndex("reb");
                int entIndex = cursor2.getColumnIndex("ent_seq");

                if (cursor2.isFirst()) {
                    int currentEntSeq = 0;

                    do {

                        int ent_seq = cursor2.getInt(entIndex);

                        if (currentEntSeq != ent_seq) {
                            if (currentEntSeq > 0) {
                                res.put(currentEntSeq, current);
                                current = new ArrayList<String>();
                            }

                            currentEntSeq = ent_seq;
                        }

                        String reb = cursor2.getString(rebIndex);
                        current.add(reb);

                    } while (cursor2.moveToNext());

                    if (currentEntSeq > 0) {
                        res.put(currentEntSeq, current);
                    }
                }
            } finally {
                cursor2.close();
            }
        }
        return res;
    }

    public Cursor getRele(ArrayList<Integer> ent_seq) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String matchSql = "SELECT reb, ent_seq FROM r_ele_fts WHERE ent_seq match ";
        String sql = "";

        for (int i : ent_seq) {
            sql += matchSql;
            sql += i;
            sql += " UNION ALL ";
        }

        if (!ent_seq.isEmpty()) {
            sql = sql.substring(0, sql.length() - 10);
        }

        Cursor c = db.rawQuery(sql, null);
        c.moveToFirst();
        return c;
    }

    public Cursor getKele(ArrayList<Integer> ent_seq) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // wyjątkowa sytuacja -> czytanie jest dodane jako alternatywa, bo ma inny JLPT level od słowa zapisanego w kanji
        String matchSql = "SELECT keb, ent_seq FROM k_ele_fts WHERE ent_seq match ";
        String sql = "";

        for (int i : ent_seq) {
            sql += matchSql;
            sql += i;
            sql += " UNION ALL ";
        }

        if (!ent_seq.isEmpty()) {
            sql = sql.substring(0, sql.length() - 10);
        }

        Cursor c = db.rawQuery(sql, null);
        c.moveToFirst();
        return c;
    }

    public Cursor getKeleEntSeq(String keb) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT ent_seq FROM k_ele_fts WHERE keb match '^" + keb + "' and length(keb) = " + String.valueOf(keb.length()), null);

        c.moveToFirst();
        return c;
    }

    public Integer getReleEntSeq(String reb) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        if (reb == null || reb.length() == 0) return null;

        Cursor cReb = db.rawQuery("select ent_seq from r_ele_fts where reb match '" + reb + "'", null);
        String matchQuery = "select ent_seq, keb from k_ele_fts where ent_seq match ";
        String query = "";

        HashMap<Integer, Boolean> entSeqs = new HashMap<Integer, Boolean>();

        if (cReb != null && cReb.getCount() > 0 && cReb.getColumnCount() > 0) {
            try {

                cReb.moveToFirst();
                int entSeqIndex = 0;

                do {

                    int entSeq = cReb.getInt(entSeqIndex);
                    entSeqs.put(entSeq, true);

                    query += matchQuery;
                    query += entSeq;
                    query += " UNION ALL ";

                } while (cReb.moveToNext());

                if (query.length() > 10) {
                    query = query.substring(0, query.length() - 10);
                }

            } catch (Exception ex) {
                ACRA.getErrorReporter().handleSilentException(ex);
            } finally {
                cReb.close();
            }

            if (query.length() > 0) {

                Cursor cMatchCursor = db.rawQuery(query, null);

                try {

                    int entSeqIndex = 0;
                    int kebIndex = 1;

                    if (cMatchCursor != null && cMatchCursor.getCount() > 0) {

                        cMatchCursor.moveToFirst();

                        do {

                            int entSeq = cMatchCursor.getInt(entSeqIndex);
                            String keb = cMatchCursor.getString(kebIndex);

                            if (keb != null && !keb.isEmpty()) {
                                entSeqs.put(entSeq, false);
                            }

                        } while (cMatchCursor.moveToNext());
                    }

                } finally {
                    cMatchCursor.close();
                }
            }

            Iterator it = entSeqs.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();

                if ((Boolean) pair.getValue()) {
                    return (Integer) pair.getKey();
                }
            }

            if (!entSeqs.isEmpty()) {
                return (Integer) entSeqs.keySet().toArray()[0];
            }
        }

        return null;
    }

    public Cursor getKeInf(String keb, int ent_seq) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        //String query = "SELECT ke_inf FROM ke_inf KINF JOIN k_ele K on (K.ID_k_ele = KINF.ID_k_ele) join k_ele_fts F ON (F.ent_seq match K.ent_seq) WHERE F.keb match '^" + keb + "' AND length(F.keb) = " + String.valueOf(keb.length()) + " AND K.ent_seq = " + String.valueOf(ent_seq);
        String query = " \n" +
                " SELECT ke_inf FROM ke_inf KINF JOIN \n" +
                " k_ele_fts K on (KINF.ID_k_ele = K.rowid) where keb match '^" + keb + "' AND length(keb) = " + keb.length() + " and K.ent_seq = " + String.valueOf(ent_seq) + " ";

        Cursor c = db.rawQuery(query, null);

        c.moveToFirst();
        return c;
    }

    public Cursor getReInf(String reb, int ent_seq) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String rebRomaji = InputUtils.getRomaji(reb, false);

        String query = " \n" +
                " SELECT re_inf FROM re_inf RINF JOIN \n" +
                " r_ele_fts R on (RINF.ID_r_ele = R.rowid) where reb_romaji match '^" + rebRomaji + "' AND length(reb_romaji) = " + rebRomaji.length() + " and R.ent_seq = " + String.valueOf(ent_seq) + " ";

        //Cursor c = db.rawQuery("SELECT re_inf FROM re_inf RINF JOIN r_ele R  ON R.ID_r_ele = RINF.ID_r_ele WHERE R.reb = '" + reb + "' AND R.ent_seq = " + String.valueOf(ent_seq), null);

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        return c;
    }

    public Cursor getEntryAlike(String searchString, int limit, int offset, boolean commonWordsOnly, int inSpinnerSelectedItem) {

        //long startTime = System.currentTimeMillis();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        searchString = searchString.replace("'", "$");
        String offsetString = "";

        if (offset > 0) {
            offsetString = "OFFSET " + Integer.toString(offset);
        }
        boolean rebRomajiMode = false;
        boolean allKanaMode = false;

        String romajiSearchString = "";

        String searchStringWithoutStars = searchString.replace("*", "").replace("＊", "");

        if ((rebRomajiMode = InputUtils.isStringAllRomaji(searchStringWithoutStars)) || (allKanaMode = InputUtils.isStringAllKana(searchStringWithoutStars))) {

            romajiSearchString = InputUtils.getRomaji(searchString, false);

            if (romajiSearchString == null || romajiSearchString.equals("")) {
                romajiSearchString = searchString;
            }
        }

        romajiSearchString = InputUtils.prepareQueryForSearchRomaji(romajiSearchString);

        String query = "SELECT DISTINCT E.ent_seq as _id, 0 as conjugated, JLPT_level, Pos, IsCommon FROM Entry E where _id in ( ";

        if (searchString.contains("*") || searchString.contains("＊")) {

            searchString = searchString.replace("＊", "*");
            romajiSearchString = romajiSearchString.replace("＊", "*");

            query = prepareWildCardQuery(searchString, inSpinnerSelectedItem, rebRomajiMode, allKanaMode, romajiSearchString, searchStringWithoutStars, query, commonWordsOnly, limit, offsetString);

        } else {

            romajiSearchString = InputUtils.getRomaji(searchString, false);
            romajiSearchString = InputUtils.prepareQueryForSearchRomaji(romajiSearchString);

            query = prepareNormalQuery(searchString, inSpinnerSelectedItem, rebRomajiMode, allKanaMode, romajiSearchString, query, commonWordsOnly, limit, offsetString);
        }

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        //long difference = System.currentTimeMillis() - startTime;

        return c;
    }

    @NonNull
    private String prepareNormalQuery(String searchString, int inSpinnerSelectedItem, boolean rebRomajiMode, boolean allKanaMode, String romajiSearchString, String query, boolean commonOnly, int limit, String offsetString) {

        if (rebRomajiMode && romajiSearchString.length() <= 2 && inSpinnerSelectedItem == 0) {
            // od JLPT N5 i common words
            for (int i = 5; i >= 0; i--) {
                for (int j = 1; j >= (commonOnly ? 1 : 0); j--) {

                    query +=
                            ((rebRomajiMode || allKanaMode) ? (

                                    // brak znaku kanji szukamy po czytaniu w r_ele i po znaczeniu w gloss_fts
                                    " select distinct E.ent_seq as _id from entry E " +
                                            " LEFT JOIN misc M ON M.ent_seq = E.ent_seq \n" +
                                            " join POS P on (E.ent_seq = P.ent_seq)\n" +
                                            " where E.ent_seq in " +
                                            "(select ent_seq from r_ele_fts R where reb_romaji match '" + romajiSearchString + "*' and length(reb_romaji) > " + String.valueOf(romajiSearchString.length()) + " collate nocase ) " +
                                            " and JLPT_level = " + String.valueOf(i) + " and IsCommon = " + String.valueOf(j) + " " +
                                            " " + InputUtils.getVocabularySpinnerItem(inSpinnerSelectedItem) + " " +
                                            "UNION ALL\n " +
                                            " select distinct G.ent_seq as _id from gloss_fts G " +
                                            " join Entry E on (G.ent_seq = E.ent_seq and JLPT_level = " + String.valueOf(i) + " and IsCommon = " + String.valueOf(j) + " ) " +
                                            " join pos P ON (G.ent_seq = P.ent_seq) " +
                                            " LEFT JOIN misc M ON G.ent_seq = M.ent_seq  " +
                                            " where gloss match '" + searchString + "*' " + InputUtils.getVocabularySpinnerItem(inSpinnerSelectedItem) +
                                            " and length(gloss) > " + searchString.length() +
                                            " collate nocase ")
                                    :
                                    // wpisaliśmy jakiś znak kanji szukamy po keb
                                    "SELECT DISTINCT E.ent_seq as _id FROM entry E join pos P ON (E.ent_seq = P.ent_seq) LEFT JOIN misc M ON (E.ent_seq = M.ent_seq) \n" +
                                            "where JLPT_level = " + String.valueOf(i) + " and IsCommon = " + String.valueOf(j) + " and  _id in (\n" +
                                            "sELECT K.ent_seq FROM k_ele_fts K WHERE keb match '" + searchString + "*' " +
                                            " AND length(keb) > " + String.valueOf(searchString.length()) + " ) " + InputUtils.getVocabularySpinnerItem(inSpinnerSelectedItem) +
                                            " collate nocase ");

                    query += " UNION ALL ";
                }

            }

            query = query.substring(0, query.length() - " UNION ALL ".length());
            query += " LIMIT " + String.valueOf(limit) + " " + offsetString + " ) ";

            return query;

        } else {

            query +=
                    ((rebRomajiMode || allKanaMode) ? ("select distinct P.ent_seq as _id from (\n" +
                            " select  R.ent_seq from r_ele_fts R \n" +
                            " where reb_romaji match '" + romajiSearchString + "*' and length(reb_romaji) > " + String.valueOf(romajiSearchString.length()) + " collate nocase ) RR\n" +
                            " LEFT JOIN misc M ON M.ent_seq = rr.ent_seq \n" +
                            " join POS P on (RR.ent_seq = P.ent_seq)\n" +
                            " where 1 = 1  " + InputUtils.getVocabularySpinnerItem(inSpinnerSelectedItem) + " " +
                            "UNION ALL " +
                            "\n" +
                            " select G.ent_seq from gloss_fts G join pos P ON (G.ent_seq = P.ent_seq) LEFT JOIN misc M ON G.ent_seq = M.ent_seq  where gloss match '" + searchString + "*' "
                            + InputUtils.getVocabularySpinnerItem(inSpinnerSelectedItem) + " and length(gloss) > " + searchString.length() + " collate nocase " +
                            "UNION ALL  ") : "") + " " +
                            "SELECT DISTINCT E.ent_seq as _id FROM entry E join pos P ON (E.ent_seq = P.ent_seq) LEFT JOIN misc M ON (E.ent_seq = M.ent_seq) \n" +
                            "where _id in (\n" +
                            "sELECT K.ent_seq FROM k_ele_fts K WHERE keb match '" + searchString + "*' AND length(keb) > " + String.valueOf(searchString.length()) + ")  "
                            + InputUtils.getVocabularySpinnerItem(inSpinnerSelectedItem);

            query += " ) " + (commonOnly ? " and IsCommon = 1 " : "") + " order by JLPT_level desc, IsCommon desc ";
            query += " LIMIT " + String.valueOf(limit) + " " + offsetString + " ";

            return query;
        }
    }


    @NonNull
    private String prepareWildCardQuery(String searchString, int inSpinnerSelectedItem, boolean rebRomajiMode, boolean allKanaMode, String romajiSearchString, String searchStringWithoutStars, String query, boolean commonOnly, int limit, String offsetString) {

        if (rebRomajiMode) {

            query += " SELECT DISTINCT E.ent_seq as _id FROM entry E join pos P ON (E.ent_seq = P.ent_seq) LEFT JOIN misc M ON (E.ent_seq = M.ent_seq) \n" +
                    "where _id in (\n";

            query += InputUtils.getWildCardString("r_ele_fts", "reb_romaji", romajiSearchString, searchString, 1, false);
            query += " ) " + InputUtils.getVocabularySpinnerItem(inSpinnerSelectedItem) + "  ";
            query += " UNION ALL select E.ent_seq from entry E join pos P ON (E.ent_seq = P.ent_seq) LEFT JOIN misc M ON E.ent_seq = M.ent_seq where E.ent_seq in ( ";

            query += InputUtils.getWildCardString("gloss_fts", "gloss", romajiSearchString, searchString, 2, false);
            query += " ) " + InputUtils.getVocabularySpinnerItem(inSpinnerSelectedItem) + " ";

        } else {
            if (allKanaMode) {

                query += " SELECT DISTINCT E.ent_seq as _id FROM entry E join pos P ON (E.ent_seq = P.ent_seq) LEFT JOIN misc M ON (E.ent_seq = M.ent_seq) \n" +
                        "where _id in (\n";

                query += InputUtils.getWildCardString("r_ele_fts", "reb", searchStringWithoutStars, searchString, 2, false);
                query += " ) " + InputUtils.getVocabularySpinnerItem(inSpinnerSelectedItem) + "  ";


            } else {

                query += " SELECT DISTINCT E.ent_seq as _id FROM entry E join pos P ON (E.ent_seq = P.ent_seq) LEFT JOIN misc M ON (E.ent_seq = M.ent_seq) \n" +
                        "where _id in (\n";

                query += InputUtils.getWildCardString("k_ele_fts", "keb", searchString, searchString, 1, false);
                query += " ) " + InputUtils.getVocabularySpinnerItem(inSpinnerSelectedItem) + "  ";
            }
        }

        query += " ) " + (commonOnly ? " and IsCommon = 1 " : "") + " order by JLPT_level desc, IsCommon desc ";
        query += " LIMIT " + String.valueOf(limit) + " " + offsetString + " ";

        return query;
    }

    public Cursor getJLPTLevel(String reb, String keb, int ent_seq) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c;

        String query = "";

        if (keb != null && reb != null) {
            query = "SELECT JLPT_level FROM JLPT J join k_ele_fts K on (J.ID_k_ele = K.rowid) WHERE keb match '^" + keb + "' and length(keb) = " + keb.length() + " and ent_seq = " + String.valueOf(ent_seq);

        } else {
            String romaji = InputUtils.getRomaji(reb, false);
            query = "SELECT JLPT_level FROM JLPT J join r_ele_fts R on (J.ID_k_ele = R.rowid) WHERE reb_romaji match '^" + romaji + "' and length(reb_romaji) = " + romaji.length() + " and ent_seq = " + String.valueOf(ent_seq) + " ";
        }

        c = db.rawQuery(query, null);
        c.moveToFirst();
        return c;

    }

    public Cursor getAllWordsToFurigana() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("select keb, reb, K.ent_seq from entry E join k_ele K on (E.ent_seq = K.ent_seq) join r_ele R on (R.ent_seq = K.ent_seq) and length(keb) > 1 limit 10 ", null);

        c.moveToFirst();
        return c;
    }

    public void getDataFromCursorRecent(ArrayList<ARecentObject> m_parts, Cursor cursor, SearchValues inSearchValues, IFactory<ARecentObject> factory) {
        try {
    /* Check if our result was valid. */
            if (cursor != null && cursor.getCount() > 0) {

                int entSeqIndex = cursor.getColumnIndex("ent_seq");
                int rebIndex = cursor.getColumnIndex("reb");
                int kebIndex = cursor.getColumnIndex("keb");

                do {
                    ARecentObject recentObject = factory.factory();
                    recentObject.init(cursor.getString(rebIndex), cursor.getString(kebIndex), cursor.getInt(entSeqIndex), "", inSearchValues);
                    m_parts.add(recentObject);

                } while (cursor.moveToNext());

            }

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

}
