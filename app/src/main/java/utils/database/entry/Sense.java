package utils.database.entry;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;

import com.mino.jdict.objects.activities.ItemDetailObject;
import com.mino.jdict.objects.basic.SenseObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import utils.database.JMDictHelper;

/**
 * Created by Dominik on 7/19/2015.
 */
public class Sense {

    JMDictHelper dbHelper;

    public Sense(JMDictHelper inHelper) {
        dbHelper = inHelper;
    }

    public Map<Integer, ArrayList<SenseObject>> getSenseObjectForMultipleEntries(ArrayList<Integer> entries) {

        int lastSense = -1;
        int lastEntry = -1;
        SenseObject currentObj = null;
        ArrayList<SenseObject> currentList = new ArrayList<SenseObject>();
        Map<Integer, ArrayList<SenseObject>> list = new HashMap<Integer, ArrayList<SenseObject>>();

        if (entries.size() == 0) return list;

        Cursor cursor = getSenseGlosses(entries);

        try {
            if (cursor != null) {

                int glossIndex = cursor.getColumnIndex("gloss");
                int entIndex = cursor.getColumnIndex("ent_seq");
                int senseIndex = cursor.getColumnIndex("ID_sense");

                if (cursor.isFirst()) {

                    do {

                        String glossText = cursor.getString(glossIndex);
                        int entseq = cursor.getInt(entIndex);
                        int senseID = cursor.getInt(senseIndex);

                        if (lastEntry != entseq) {
                            if (lastEntry > 0) {

                                list.put(lastEntry, currentList);
                                currentList = new ArrayList<SenseObject>();
                            }

                            lastEntry = entseq;
                        }

                        if (lastSense != senseID) {
                            lastSense = senseID;

                            currentList.add(currentObj = new SenseObject(senseID));
                            currentObj.addGloss(glossText);

                        } else if (currentObj != null) {
                            currentObj.addGloss(glossText);
                        }

                    } while (cursor.moveToNext());

                    if (lastEntry > 0) {
                        list.put(lastEntry, currentList);
                    }
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return list;
    }

    public ArrayList<SenseObject> getSenseObjects(Boolean inGetSenseRelatedInfo, ArrayList<SenseObject> sense, int entryID) {
        Cursor cursor = getSenseGloss(entryID);
        int lastSense = -1;
        SenseObject currentObj = null;
        ArrayList<Integer> senseIds = new ArrayList<Integer>();
        Map<Integer, SenseObject> map = new HashMap<Integer, SenseObject>();

        try {
            if (cursor != null) {

                if (sense == null) {
                    sense = new ArrayList<SenseObject>();
                } else {
                    sense.clear();
                }

                int glossIndex = cursor.getColumnIndex("gloss");
                // int idIndex = cursor.getColumnIndex("_id");
                int senseIndex = cursor.getColumnIndex("ID_sense");

                if (cursor.isFirst()) {

                    do {

                        String glossText = cursor.getString(glossIndex);
                        // int glossID = cursor.getInt(idIndex);
                        int senseID = cursor.getInt(senseIndex);

                        if (lastSense != senseID) {
                            lastSense = senseID;
                            senseIds.add(lastSense);

                            sense.add(currentObj = new SenseObject(senseID));
                            map.put(senseID, currentObj);
                            currentObj.addGloss(glossText);

                        } else if (currentObj != null) {
                            currentObj.addGloss(glossText);
                        }

                    } while (cursor.moveToNext());
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        if (inGetSenseRelatedInfo) {

            getRelatedInfo(senseIds, map);
        }

        return sense;

    }

    private void getRelatedInfo(ArrayList<Integer> senseIds, Map<Integer, SenseObject> map) {
        getXref(senseIds, map);
        getAnt(senseIds, map);
        getStagr(senseIds, map);
        getStagk(senseIds, map);
        getPos(senseIds, map);
        getField(senseIds, map);
        getMisc(senseIds, map);
        getDial(senseIds, map);
        getSinf(senseIds, map);
        getLsource(senseIds, map);
        getExample(senseIds, map);
    }

    public ArrayList<ItemDetailObject> getMoreExamples(SenseObject senseObj, int inIDsense) {

        Cursor exampleCursor = getMoreExamplesCursor(senseObj);
        ArrayList<ItemDetailObject> list = new ArrayList<ItemDetailObject>();

        boolean isColored = senseObj.getSenseID() == inIDsense;
        int limit = senseObj.getOffsetSize();

        try {
            if (exampleCursor != null && exampleCursor.getCount() > 0) {

                int sentenceIndex = exampleCursor.getColumnIndex("sentence_seq");
                int jpIndex = exampleCursor.getColumnIndex("sentence_jp_a");
                int engIndex = exampleCursor.getColumnIndex("sentence_eng");

                do {

                    if (limit == 0) {
                        ItemDetailObject objSearchForMore = new ItemDetailObject();
                        objSearchForMore.IsNewResultsForSelectedSense.set(true);
                        objSearchForMore.SenseObject.set(senseObj);
                        objSearchForMore.IsColored.set(isColored);
                        list.add(objSearchForMore);
                    } else {
                        int sentence_seq = exampleCursor.getInt(sentenceIndex);
                        String sentence_jp_a = exampleCursor.getString(jpIndex);
                        String sentence_eng = exampleCursor.getString(engIndex);

                        ItemDetailObject obj = new ItemDetailObject(sentence_jp_a, sentence_eng, true);
                        obj.setExample(sentence_seq);
                        obj.IsColored.set(isColored);
                        senseObj.addExample(obj);
                        list.add(obj);
                    }

                    limit--;

                    // map.get(id_sense).addExample(obj);
                } while (exampleCursor.moveToNext());
            }
        } finally {
            if (exampleCursor != null) {
                exampleCursor.close();
            }
        }

        return list;
    }

    private void getExample(ArrayList<Integer> senseIds, Map<Integer, SenseObject> map) {
        Cursor exampleCursor = getExample(senseIds);

        try {
            if (exampleCursor != null && exampleCursor.getCount() > 0) {

                int sentenceIndex = exampleCursor.getColumnIndex("sentence_seq");
                int jpIndex = exampleCursor.getColumnIndex("sentence_jp_a");
                int engIndex = exampleCursor.getColumnIndex("sentence_eng");
                int idSenseIndex = exampleCursor.getColumnIndex("ID_sense");

                do {

                    int sentence_seq = exampleCursor.getInt(sentenceIndex);
                    String sentence_jp_a = exampleCursor.getString(jpIndex);
                    String sentence_eng = exampleCursor.getString(engIndex);
                    Integer id_sense = exampleCursor.getInt(idSenseIndex);

                    ItemDetailObject obj = new ItemDetailObject(sentence_jp_a, sentence_eng, true);
                    obj.setExample(sentence_seq);

                    map.get(id_sense).addExample(obj);
                } while (exampleCursor.moveToNext());
            }
        } finally {
            if (exampleCursor != null) {
                exampleCursor.close();
            }
        }
    }

    private void getLsource(ArrayList<Integer> senseIds, Map<Integer, SenseObject> map) {

        Cursor lsourceCursor = getLsource(senseIds);

        try {
            if (lsourceCursor != null && lsourceCursor.getCount() > 0) {

                int lsourceIndex = lsourceCursor.getColumnIndex("lsource");
                int langIndex = lsourceCursor.getColumnIndex("lang");
                int idSenseIndex = lsourceCursor.getColumnIndex("ID_sense");

                do {

                    String lsource = lsourceCursor.getString(lsourceIndex);
                    String lang = lsourceCursor.getString(langIndex);
                    Integer id_sense = lsourceCursor.getInt(idSenseIndex);

                    map.get(id_sense).addLSource(new Pair<String, String>(lsource, lang));
                } while (lsourceCursor.moveToNext());
            }
        } finally {
            if (lsourceCursor != null) {
                lsourceCursor.close();
            }
        }

    }

    private void getXref(ArrayList<Integer> senseIds, Map<Integer, SenseObject> map) {
        Cursor xrefCursor = getXref(senseIds);

        try {
            if (xrefCursor != null && xrefCursor.getCount() > 0) {

                int xrefIndex = xrefCursor.getColumnIndex("xref");
                int entseqIndex = xrefCursor.getColumnIndex("ent_seq");
                int idSenseIndex = xrefCursor.getColumnIndex("ID_sense");

                do {

                    String xref = xrefCursor.getString(xrefIndex);
                    Integer ent_seq = xrefCursor.getInt(entseqIndex);
                    Integer id_sense = xrefCursor.getInt(idSenseIndex);

                    if ((ent_seq == null || ent_seq == 0) && xref.contains("・")) {
                        ent_seq = getEntSeqFromKeb(xref.split("・")[0], ent_seq);
                    }

                    if ((ent_seq == null || ent_seq == 0) && xref.contains("・")) {
                        ent_seq = getEntSeqFromReb(xref.split("・")[0], ent_seq);
                    }

                    map.get(id_sense).addXref(new Pair<String, Integer>(xref, ent_seq));

                } while (xrefCursor.moveToNext());
            }
        } finally {
            if (xrefCursor != null) {
                xrefCursor.close();
            }
        }
    }

    private void getStagr(ArrayList<Integer> senseIds, Map<Integer, SenseObject> map) {

        Cursor stagrCursor = getStagr(senseIds);
        try {
            if (stagrCursor != null && stagrCursor.getCount() > 0) {

                int stagrIndex = stagrCursor.getColumnIndex("stagr");
                int idSenseIndex = stagrCursor.getColumnIndex("ID_sense");

                do {

                    String stagr = stagrCursor.getString(stagrIndex);
                    Integer id_sense = stagrCursor.getInt(idSenseIndex);
                    map.get(id_sense).addStagr(stagr);

                } while (stagrCursor.moveToNext());

            }
        } finally {
            if (stagrCursor != null) {
                stagrCursor.close();
            }
        }
    }

    private void getStagk(ArrayList<Integer> senseIds, Map<Integer, SenseObject> map) {

        Cursor stagkCursor = getStagk(senseIds);

        try {
            if (stagkCursor != null && stagkCursor.getCount() > 0) {

                int stagkIndex = stagkCursor.getColumnIndex("stagk");
                int idSenseIndex = stagkCursor.getColumnIndex("ID_sense");

                do {

                    String stagr = stagkCursor.getString(stagkIndex);
                    Integer id_sense = stagkCursor.getInt(idSenseIndex);
                    map.get(id_sense).addStagr(stagr);

                } while (stagkCursor.moveToNext());


            }
        } finally {
            if (stagkCursor != null) {
                stagkCursor.close();
            }
        }
    }

    private void getSinf(ArrayList<Integer> senseIds, Map<Integer, SenseObject> map) {

        Cursor sInfCursor = getSinf(senseIds);

        try {
            if (sInfCursor != null && sInfCursor.getCount() > 0) {

                int sInf = sInfCursor.getColumnIndex("s_inf");
                int idSenseIndex = sInfCursor.getColumnIndex("ID_sense");

                do {

                    String sinf = sInfCursor.getString(sInf);
                    Integer id_sense = sInfCursor.getInt(idSenseIndex);
                    map.get(id_sense).addSInf(sinf);

                } while (sInfCursor.moveToNext());


            }
        } finally {
            if (sInfCursor != null) {
                sInfCursor.close();
            }
        }
    }

    private void getDial(ArrayList<Integer> senseIds, Map<Integer, SenseObject> map) {

        Cursor dialCursor = getDial(senseIds);

        try {

            if (dialCursor != null && dialCursor.getCount() > 0) {

                int dialIndex = dialCursor.getColumnIndex("dial");
                int idSenseIndex = dialCursor.getColumnIndex("ID_sense");

                do {

                    String dial = dialCursor.getString(dialIndex);
                    Integer id_sense = dialCursor.getInt(idSenseIndex);
                    map.get(id_sense).addDial(dial);

                } while (dialCursor.moveToNext());
            }
        } finally {
            if (dialCursor != null) {
                dialCursor.close();
            }
        }
    }

    private void getMisc(ArrayList<Integer> senseIds, Map<Integer, SenseObject> map) {

        Cursor fieldCursor = getMisc(senseIds);

        try {
            if (fieldCursor != null && fieldCursor.getCount() > 0) {

                int miscIndex = fieldCursor.getColumnIndex("misc");
                int idSenseIndex = fieldCursor.getColumnIndex("ID_sense");

                do {

                    String misc = fieldCursor.getString(miscIndex);
                    Integer id_sense = fieldCursor.getInt(idSenseIndex);
                    map.get(id_sense).addMisc(misc);

                } while (fieldCursor.moveToNext());
            }
        } finally {
            if (fieldCursor != null) {
                fieldCursor.close();
            }
        }
    }

    private void getField(ArrayList<Integer> senseIds, Map<Integer, SenseObject> map) {

        Cursor fieldCursor = getField(senseIds);

        try {
            if (fieldCursor != null && fieldCursor.getCount() > 0) {

                int stagkIndex = fieldCursor.getColumnIndex("field");
                int idSenseIndex = fieldCursor.getColumnIndex("ID_sense");

                do {

                    String stagr = fieldCursor.getString(stagkIndex);
                    Integer id_sense = fieldCursor.getInt(idSenseIndex);
                    map.get(id_sense).addField(stagr);

                } while (fieldCursor.moveToNext());
            }
        } finally {
            if (fieldCursor != null) {
                fieldCursor.close();
            }
        }
    }


    private void getPos(ArrayList<Integer> senseIds, Map<Integer, SenseObject> map) {

        Cursor posCursor = getPos(senseIds);

        try {
            if (posCursor != null && posCursor.getCount() > 0) {

                int posIndex = posCursor.getColumnIndex("pos");
                int idSenseIndex = posCursor.getColumnIndex("ID_sense");

                do {

                    String pos = posCursor.getString(posIndex);
                    Integer id_sense = posCursor.getInt(idSenseIndex);
                    map.get(id_sense).addPos(pos);

                } while (posCursor.moveToNext());
            }
        } finally {
            if (posCursor != null) {
                posCursor.close();
            }
        }
    }


    private Integer getEntSeqFromKeb(String s, Integer ent_seq) {

        if (s.length() > 0) {
            Cursor entseqCursor = dbHelper.getWord().getKeleEntSeq(s);

            try {
                if (entseqCursor != null && entseqCursor.getCount() > 0) {

                    int entseqIndexKeb = entseqCursor.getColumnIndex("ent_seq");
                    ent_seq = entseqCursor.getInt(entseqIndexKeb);
                }
            } finally {
                if (entseqCursor != null) {
                    entseqCursor.close();
                }
            }
        }
        return ent_seq;
    }

    private Integer getEntSeqFromReb(String s, Integer ent_seq) {
        if (s.length() > 0) {

            return dbHelper.getWord().getReleEntSeq(s);
        }

        return ent_seq;
    }

    private void getAnt(ArrayList<Integer> senseIds, Map<Integer, SenseObject> map) {

        Cursor antCursor = getAnt(senseIds);
        try {
            if (antCursor != null && antCursor.getCount() > 0) {

                int antIndex = antCursor.getColumnIndex("ant");
                int entseqIndex = antCursor.getColumnIndex("ent_seq");
                int idSenseIndex = antCursor.getColumnIndex("ID_sense");

                do {

                    String ant = antCursor.getString(antIndex);
                    Integer ent_seq = antCursor.getInt(entseqIndex);
                    Integer id_sense = antCursor.getInt(idSenseIndex);

                    if ((ent_seq == null || ent_seq == 0) && ant.contains("・")) {
                        ent_seq = getEntSeqFromKeb(ant.split("・")[0], ent_seq);
                    }

                    if ((ent_seq == null || ent_seq == 0) && ant.contains("・")) {
                        ent_seq = getEntSeqFromReb(ant.split("・")[0], ent_seq);
                    }

                    map.get(id_sense).addAnt(new Pair<String, Integer>(ant, ent_seq));

                } while (antCursor.moveToNext());
            }
        } finally {
            if (antCursor != null) {
                antCursor.close();
            }
        }
    }


    public Cursor getSenseGloss(int ent_seq) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("select ID_sense, gloss from gloss_fts where ent_seq match " + ent_seq, null);
        c.moveToFirst();

        return c;
    }

    public Cursor getSenseGlosses(ArrayList<Integer> ent_seq) {

        if (ent_seq.isEmpty()) return null;

        String sqlConst = " select ID_sense, ent_seq, gloss from gloss_fts where ent_seq match ";
        String sql = "";

        for (int i : ent_seq) {
            sql += sqlConst;
            sql += String.valueOf(i);
            sql += " union ";
        }

        sql = sql.substring(0, sql.length() - 6);
        sql += " order by ent_seq, ID_sense";

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);

        c.moveToFirst();
        return c;
    }

    public Cursor getAnt(ArrayList<Integer> senseIds) {

        if (senseIds.isEmpty()) return null;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String stringSenseIDs = getSenseIDStr(senseIds);

        Cursor c = db.rawQuery("select ant, ent_seq, ID_sense from (\n" +
                "select antonym as ant, ent_seq, ID_sense from (\n" +
                "\n" +
                "                select keb as antonym, K.ent_seq as ent_seq, X.ID_sense from k_ele_fts K join ant X on (K.keb match X.ant and ID_sense in " + stringSenseIDs + ") \n" +
                "                union select reb as antonym, R.ent_seq as ent_seq, X.ID_sense from r_ele_fts R join ant X on (R.reb match X.ant and ID_sense in " + stringSenseIDs + " ) " +
                " ) X group by antonym " +
                "\tunion select ant, null as ent_seq, ID_sense from ant where ID_sense in " + stringSenseIDs + ") Z group by ant ", null);

        c.moveToFirst();
        return c;
    }

    private String getSenseIDStr(ArrayList<Integer> senseIds) {

        String stringSenseIDs = "(";
        for (int i : senseIds) {
            stringSenseIDs += String.valueOf(i);
            stringSenseIDs += ",";
        }

        stringSenseIDs = stringSenseIDs.substring(0, stringSenseIDs.length() - 1);
        stringSenseIDs += ")";
        return stringSenseIDs;
    }

    public Cursor getPos(ArrayList<Integer> senseIds) {

        if (senseIds.isEmpty()) return null;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String stringSenseIDs = getSenseIDStr(senseIds);

        Cursor c = db.rawQuery("select ID_sense, pos from pos where ID_sense in " + stringSenseIDs + "", null);

        c.moveToFirst();
        return c;
    }

    private Cursor getMoreExamplesCursor(SenseObject senseObj) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        int senseId = senseObj.getSenseID();
        int limit = senseObj.getOffsetSize() + 1;
        int offset = senseObj.getExampleCount() - 1;
        String senseStr = String.valueOf(senseId);

        String rawQuery = "select sentence_seq, sentence_jp_a, sentence_eng, ID_sense, ID_k_ele,certified from\n" +
                "(\n" +
                "select sentence_seq, sentence_jp_a, sentence_eng, ID_sense, ID_k_ele,certified from\n" +
                " (select distinct S.sentence_seq, sentence_jp_a, F.sentence_eng, SW.ID_sense, SW.ID_k_ele,certified  from Sentences S join sentences_fts F on (F.sentence_seq match S.sentence_seq) \n" +
                "   join sentenceword SW ON \n" +
                "\t\t\t(s.sentence_seq = SW.sentence_seq and  ID_sense = " + senseStr + "))) limit " + String.valueOf(limit) + " offset " + String.valueOf(offset);


        Cursor c = db.rawQuery(rawQuery, null);
        c.moveToFirst();

        return c;
    }


    public Cursor getExample(ArrayList<Integer> senseIds) {

        if (senseIds.isEmpty()) return null;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String rawQuery = "";

        for (int senseID : senseIds) {
            String senseStr = String.valueOf(senseID);

            rawQuery += "select sentence_seq, sentence_jp_a, sentence_eng, ID_sense, ID_k_ele,certified from\n" +
                    "(\n" +
                    "select sentence_seq, sentence_jp_a, sentence_eng, ID_sense, ID_k_ele,certified from\n" +
                    " (select distinct S.sentence_seq, sentence_jp_a, F.sentence_eng, SW.ID_sense, SW.ID_k_ele,certified  from Sentences S join Sentences_fts F on (F.sentence_seq match S.sentence_seq) \n" +
                    "   join sentenceword SW ON \n" +
                    "\t\t\t(s.sentence_seq = SW.sentence_seq and  ID_sense = " + senseStr + ") limit 2)) ";


            rawQuery += " UNION ALL ";
        }

        rawQuery = rawQuery.substring(0, rawQuery.length() - 11);
        Cursor c = db.rawQuery(rawQuery, null);

        c.moveToFirst();
        return c;
    }

    public Cursor getLsource(ArrayList<Integer> senseIds) {

        if (senseIds.isEmpty()) return null;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String stringSenseIDs = getSenseIDStr(senseIds);

        Cursor c = db.rawQuery("select ID_sense, lsource, lang from lsource where ID_sense in " + stringSenseIDs + "", null);

        c.moveToFirst();
        return c;
    }

    public Cursor getStagr(ArrayList<Integer> senseIds) {

        if (senseIds.isEmpty()) return null;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String stringSenseIDs = getSenseIDStr(senseIds);

        Cursor c = db.rawQuery("select ID_sense, stagr from stagr where ID_sense in " + stringSenseIDs + "", null);

        c.moveToFirst();
        return c;
    }

    public Cursor getMisc(ArrayList<Integer> senseIds) {

        if (senseIds.isEmpty()) return null;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String stringSenseIDs = getSenseIDStr(senseIds);

        Cursor c = db.rawQuery("select ID_sense, misc from misc where ID_sense in " + stringSenseIDs + "", null);

        c.moveToFirst();
        return c;
    }

    public Cursor getDial(ArrayList<Integer> senseIds) {

        if (senseIds.isEmpty()) return null;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String stringSenseIDs = getSenseIDStr(senseIds);

        Cursor c = db.rawQuery("select ID_sense, dial from dial where ID_sense in " + stringSenseIDs + "", null);

        c.moveToFirst();
        return c;
    }

    public Cursor getField(ArrayList<Integer> senseIds) {

        if (senseIds.isEmpty()) return null;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String stringSenseIDs = getSenseIDStr(senseIds);

        Cursor c = db.rawQuery("select ID_sense, field from field where ID_sense in " + stringSenseIDs + "", null);

        c.moveToFirst();
        return c;
    }

    public Cursor getStagk(ArrayList<Integer> senseIds) {

        if (senseIds.isEmpty()) return null;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String stringSenseIDs = getSenseIDStr(senseIds);

        Cursor c = db.rawQuery("select ID_sense, stagk from stagk where ID_sense in " + stringSenseIDs + "", null);

        c.moveToFirst();
        return c;
    }

    public Cursor getSinf(ArrayList<Integer> senseIds) {

        if (senseIds.isEmpty()) return null;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String stringSenseIDs = getSenseIDStr(senseIds);

        Cursor c = db.rawQuery("select ID_sense, s_inf from s_inf where ID_sense in " + stringSenseIDs + "", null);

        c.moveToFirst();
        return c;
    }

    public Cursor getXref(ArrayList<Integer> senseIds) {

        if (senseIds.isEmpty()) return null;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String stringSenseIDs = getSenseIDStr(senseIds);

        // todo: pomyśleć o analizie xref-a z kropką
        Cursor c = db.rawQuery("\n" +
                "select xref, ent_seq, ID_sense from (select reference as xref, ent_seq, ID_sense from (\n" +
                "select keb as reference, K.ent_seq as ent_seq, X.ID_sense from k_ele_fts K join xref X on (K.keb match X.xref and ID_sense in " + stringSenseIDs + ") \n" +
                "union select reb as reference, R.ent_seq as ent_seq, X.ID_sense from r_ele_fts R join xref X on (R.reb match X.xref and ID_sense in " + stringSenseIDs + ")  " +
                " ) X group by reference " +
                "union select xref, null as ent_seq, ID_sense from xref where ID_sense in " + stringSenseIDs + " ) Z group by xref", null);

        c.moveToFirst();
        return c;
    }
}
