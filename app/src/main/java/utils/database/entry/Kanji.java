package utils.database.entry;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;

import com.mino.jdict.adapters.AdapterHelper;
import com.mino.jdict.objects.activities.KanjiBaseObject;
import com.mino.jdict.objects.basic.GrammarObject;

import org.acra.ACRA;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import utils.database.JMDictHelper;
import utils.grammar.GrammarDictionaries;
import utils.grammar.InputUtils;

/**
 * Created by Dominik on 7/19/2015.
 */
public class Kanji {
    JMDictHelper dbHelper;

    public Kanji(JMDictHelper inHelper) {
        dbHelper = inHelper;
    }

    public KanjiBaseObject getBasicKanjiDetails(int inCharacterSeq) {

        final KanjiBaseObject obj = new KanjiBaseObject();
        Cursor cursor = getKanjiDetail(inCharacterSeq);
        return getKanjiBaseObject(obj, cursor);
    }

    private KanjiBaseObject getKanjiBaseObject(final KanjiBaseObject obj, Cursor cursor) {
        try {
            AdapterHelper.GetKanjiBasicInfo(cursor, new AdapterHelper.KanjiBasicInfoInterface() {
                @Override
                public void onGotData(KanjiBaseObject result) {
                    obj.CharacterID.set(result.CharacterID.get());
                    obj.setKunyomi(result.getKunyomi());
                    obj.setOnyomi(result.getOnyomi());
                    obj.setNanori(result.getNanori());
                    obj.Meaning.set(result.Meaning.get());
                    obj.Character.set(result.Character.get());
                }
            }, null);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return obj;
    }


    public Cursor getKanjiDetailJLPT(int inSpinnerSelectedItem) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery("select C.character_seq as character_seq, literal, read, r_type, group_concat(mean, ', ')  as meaning from character C " +
                " join misckanji MK on (C.character_seq = MK.character_seq) join reading R on (C.character_seq = R.character_seq) " +
                " left join meaning M on (M.character_seq = C.character_seq) where r_type in ('ja_on', 'ja_kun')  " + InputUtils.getKanjiJLPTItem(inSpinnerSelectedItem) + " group by C.character_seq, r_type, read order by C.character_seq, r_type ", null);

        c.moveToFirst();
        return c;
    }

    public Cursor getKanjiDetailSchoolGrade(int inSpinnerSelectedItem) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery("select C.character_seq as character_seq, literal, read, r_type, group_concat(mean, ', ')  as meaning from character C " +
                " join misckanji MK on (C.character_seq = MK.character_seq) join reading R on (C.character_seq = R.character_seq) " +
                " left join meaning M on (M.character_seq = C.character_seq) where r_type in ('ja_on', 'ja_kun')  " + InputUtils.getKanjiSchoolGradesItem(inSpinnerSelectedItem) + " group by C.character_seq, r_type, read order by C.character_seq, r_type", null);

        c.moveToFirst();
        return c;
    }


    public Cursor getKanjiDetail(int character_seq) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery("select C.character_seq as character_seq, literal, read, r_type, group_concat(mean, ', ')  as meaning from character C join reading R on (C.character_seq = R.character_seq) left join meaning M on (M.character_seq = C.character_seq) where C.character_seq = " + String.valueOf(character_seq) + " and r_type in ('ja_on', 'ja_kun')  group by C.character_seq, r_type, read " +
                "union select C.character_seq as character_seq, C.literal as literal, Name as read, 'nanori' as r_type, '' as meaning from character C join nanori N on (c.character_seq = N.character_seq) where N.character_seq = " + String.valueOf(character_seq) + " order by r_type", null);
        if (c.getCount() == 0) {
            c.close();

            c = db.rawQuery("select C.character_seq as character_seq, literal, '' as read, '' as r_type, group_concat(mean, ', ')  as meaning from character C left join meaning M on (M.character_seq = C.character_seq) where C.character_seq = " + String.valueOf(character_seq) + " group by C.character_seq, r_type, read ", null);
        }

        c.moveToFirst();
        return c;
    }

    public Cursor getKanjiSvg(int character_seq) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery("select stroke_svg from character where character_seq = " + String.valueOf(character_seq), null);
        c.moveToFirst();
        return c;
    }

    public Cursor getComponentKanji(String character, int limit, int offset) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Cursor c = db.rawQuery("select * from character where decomposition like '%" + character + "%' LIMIT " + String.valueOf(limit) + " OFFSET " + String.valueOf(offset), null);

        Cursor c = db.rawQuery(" select character_seq, literal, ifnull(read,'') as read,  ifnull(r_type,'') as r_type, ifnull(meaning,'') as meaning from ( " +
                "select C.character_seq as character_seq, literal, read, r_type, group_concat(mean, ', ')  as meaning from character C  " +
                " join (select C.character_seq from character C join misckanji MK on (C.character_seq = MK.character_seq) where decomposition like '%" + character + "%'  order by jlpt desc, freq limit " + String.valueOf(limit) + " offset " + String.valueOf(offset) + " ) C2 on (C2.character_seq = C.character_seq) " +
                " left join reading R on (C.character_seq = R.character_seq and r_type in ('ja_on', 'ja_kun')) left join meaning M on (M.character_seq = C.character_seq) where C.decomposition like '%" + character + "%'  group by C.character_seq, r_type, read " +
                " union select C.character_seq as character_seq, C.literal as literal, Name as read, 'nanori' as r_type, '' as meaning from character C " +
                " join (select C.character_seq from character C join misckanji MK on (C.character_seq = MK.character_seq) where decomposition like '%" + character + "%'  order by jlpt desc, freq limit " + String.valueOf(limit) + " offset " + String.valueOf(offset) + " ) C2 on (C2.character_seq = C.character_seq) " +
                " join nanori N on (c.character_seq = N.character_seq) where C.decomposition like '%" + character + "%'  order by r_type ) order by character_seq ", null);


        c.moveToFirst();
        return c;

    }

    public Cursor getAllComponents(int strokeCount) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("select distinct C.character_seq, literal, decomposition from character C join misckanji M on (C.character_seq = M.character_seq) where \n" +
                " decomposition like '%'||C.literal||'%' and stroke_count " + (strokeCount < 8 ? " = " : " >= ") + String.valueOf(strokeCount) +
                " group by decomposition", null);


        c.moveToFirst();
        return c;

    }

    public ArrayList<String> getKanjiNonAvailableByComponent(ArrayList<String> components, int strokeOrder) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<String> availabilityList = new ArrayList<String>();

        String componentString = "";
        for (String component : components) {

            componentString += " '" + component + "',";
        }

        componentString = componentString.substring(0, componentString.length() - 1);
        ArrayList<Pair<String, String>> componentList = InputUtils.getComponentCharacterEntSeq(strokeOrder);

        String sql = "";
        for (Pair<String, String> pair : componentList) {

            //sql += " SELECT  * FROM (select 1 as KanjiAvailable, '" + pair.second + "' as Component from character where " + likeString + " and decomposition like '%" + pair.second + "%' limit 1) union";
            if (!componentString.contains(pair.second)) {
                sql += " '" + pair.second + "',";
            }
        }

        sql = sql.substring(0, sql.length() - 1);
        //sql = "select D.literal as Component from character C join character D on (D.literal in ( " + sql + ")) where " + componentString + " and C.decomposition like  '%'||D.literal||'%' group by D.literal ";

        sql =
                " select distinct component from characterComponents where character_seq in (select character_seq from characterComponents CC where component in ("
                        + componentString + ") group by CC.character_seq having count(*) = " + String.valueOf(components.size()) + ") and component in (" + sql + ")";

        Cursor cursor = db.rawQuery(sql, null);

        try {
            cursor.moveToFirst();

            if (cursor != null && cursor.getCount() > 0) {

                //int KanjiAvailableIndex = cursor.getColumnIndex("KanjiAvailable");
                int ComponentIndex = cursor.getColumnIndex("component");

                do {

                    availabilityList.add(cursor.getString(ComponentIndex));

                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return availabilityList;
    }

    public Cursor getKanjiByComponent(ArrayList<String> components) {

        String likeString = "";
        for (String component : components) {

            if (!likeString.isEmpty()) likeString += " AND ";

            likeString += " decomposition glob '*" + component + "*' ";
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("select distinct C.character_seq, literal, decomposition, group_concat(mean, ', ')  as meaning from character C left join meaning M on (C.character_seq = M.character_seq) left join misckanji MK on (C.character_seq = MK.character_seq) where " + likeString + " group by C.character_seq order by IFNULL(MK.freq, 9999)", null);

        c.moveToFirst();
        return c;
    }

    public Cursor getComponentKanjiCount(String character) {

        Cursor c = null;

        try {

            SQLiteDatabase db = dbHelper.getReadableDatabase();
            c = db.rawQuery(" select count(*) as Count from character where decomposition like '%" + character + "%'", null);
            c.moveToFirst();
            return c;

        } catch (Exception ex) {

            ACRA.getErrorReporter().handleSilentException(ex);
            return null;
        }
    }

    public Cursor getKanjiBasicInfo(String character) {

        Cursor c = null;

        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            c = db.rawQuery("select C.character_seq as character_seq, literal, read, r_type, group_concat(mean, ', ')  as meaning from character C join reading R on (C.character_seq = R.character_seq) join meaning M on (M.character_seq = R.character_seq) where C.literal = '" + character + "' and r_type in ('ja_on', 'ja_kun') group by C.character_seq, r_type, read " +
                    " union select C.character_seq as character_seq, C.literal as literal, Name as read, 'nanori' as r_type, '' as meaning from character C join nanori N on (c.character_seq = N.character_seq) where C.literal = '" + character + "'  order by r_type", null);

            c.moveToFirst();
            return c;
        } catch (Exception ex) {

            ACRA.getErrorReporter().handleSilentException(ex);
            if (c != null) {
                c.close();
            }

            return null;
        }
    }

    public Cursor getCompounds(String searchString, int limit, int offset, InputUtils.WildCardMode mode) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        /*
        String query = "select distinct(E.ent_seq) as _id,  0 as conjugated, E.JLPT_level, Pos,  E.IsCommon from Entry E\n" +
                "join\n" +
                " (\n" +
                "select ent_seq as ent_seq, keb as keb from k_ele_fts where keb_with_spaces match \n" +
                " '" + searchString + "' \n" +
                " ) T on (T.ent_seq = E.ent_seq)\n" +
                "order by E.JLPT_level desc, E.IsCommon desc, length(keb) limit " + String.valueOf(limit) + " offset " + String.valueOf(offset);
                */

        String query = "select distinct(E.ent_seq) as _id,  0 as conjugated, E.JLPT_level, Pos,  E.IsCommon from Entry E\n" +
                "join\n" +
                " (\n" +
                InputUtils.getWildCardString("k_ele_fts", "keb", searchString, searchString, 1, true, mode) +
                " ) T on (T.ent_seq = E.ent_seq)\n" +
                "order by E.JLPT_level desc, E.IsCommon desc, length(keb) limit " + String.valueOf(limit) + " offset " + String.valueOf(offset);

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        return c;
    }

    public Cursor getCompoundsCount(String searchString, InputUtils.WildCardMode mode) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "select count(distinct (ent_seq)) as Count from \n" +
                "( " +
                InputUtils.getWildCardString("k_ele_fts", "keb", searchString, searchString, 1, false, mode) +
                ") T";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        return c;
    }

    public List<Pair<String, String>> getKanjiReadings(String inCharacter, boolean inGetSimpleFormsOnly) {

        Cursor c = null;

        try {

            // dłuższe dopasowania mają wyższy priorytet
            //PriorityQueue<String> reading = new PriorityQueue<String>(10, new utils.other.StringLengthComparator());

            HashSet<Pair<String, String>> hashSet = new HashSet<Pair<String, String>>();
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            // KUN-YOMI + ON-YOMI (skonwertowane na hiraganę) + NANORI
            c = db.rawQuery("select distinct read as 'readings', read_romaji, 'kun' as type from reading R join character C on (R.character_seq = C.character_seq) where r_type = 'ja_kun' AND C.literal = '" + inCharacter + "'" +
                    " UNION select distinct on_read_hiragana as 'readings', read_romaji, 'on' as type from reading R join character C on (R.character_seq = C.character_seq) where r_type = 'ja_on' AND C.literal = '" + inCharacter + "' " +
                    " UNION select distinct Name as 'readings', name_romaji as read_romaji, 'nanori' as type from nanori N join character C on (N.character_seq = C.character_seq) where C.literal = '" + inCharacter + "' "
                    , null);

            c.moveToFirst();

            if (c != null) {

                int readIndex = c.getColumnIndex("readings");
                int readRomajiIndex = c.getColumnIndex("read_romaji");
                int typeIndex = c.getColumnIndex("type");

                if (c.isFirst()) {

                    do {
                        String read = c.getString(readIndex);
                        String type = c.getString(typeIndex);
                        read = read.replace("-", "");

                        // KUN-YOMI
                        if (read.contains(".")) {
                            read = read.replace(".", "");
                        }

                        hashSet.add(new Pair<String, String>(read, type));

                        //  - sprawdzamy np. し.まり dopasowania shimari, potem shima i na koniec shi (od największego dopasowania)
                    /*
                    for (int i = read.length(); i >= 1; i--) {

                        hashSet.add(read.substring(0, i));
                    }
                    */


                        // I-FORM (sprawdza się przy irregular okurigana np. kurinuku)
                        for (GrammarObject gObj : GrammarDictionaries.I_FORM_LIST) {
                            if (type.equals("kun") && read.endsWith(gObj.getSimpleForm())) {
                                hashSet.add(new Pair<String, String>(read.substring(0, read.length() - gObj.getSimpleForm().length()) + gObj.getChangedForm(), type));
                            }
                        }

                        //if (read.endsWith("つ"))
                        if (read.length() > 1) {
                            hashSet.add(new Pair<String, String>(read.substring(0, read.length() - 1) + "っ", type));
                        }

                        if (!inGetSimpleFormsOnly) {

                            // nigoryzacja
                            for (Map.Entry<String, List<String>> entry : GrammarDictionaries.NIG_KANA.entrySet()) {
                                String kana = entry.getKey();
                                List<String> nigKana = entry.getValue();

                                for (String nig : nigKana) {
                                    addChangedForm(hashSet, read, kana, nig, type);
                                }
                            }
                        }

                    } while (c.moveToNext());

                    c.close();
                }
            }

            List<Pair<String, String>> list = new ArrayList<Pair<String, String>>(hashSet);
            Collections.sort(list, new utils.other.StringLengthComparator());

            return list;
        } catch (Exception ex) {

            ACRA.getErrorReporter().handleSilentException(ex);
            return new ArrayList<Pair<String, String>>();
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }


    /*
        public Cursor getKanjiMeaningRest(int character_seq) {

            SQLiteDatabase db = getReadableDatabase();

            Cursor c = db.rawQuery("select group_concat (mean, ', ') as meanGroup, m_lang from meaning where character_seq = " + character_seq + " and m_lang is not null group by m_lang ", null);

            c.moveToFirst();
            return c;

        }
    */
    public Cursor getKanjiMeaningEng(int character_seq) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("select group_concat (mean, ', ') as meanGroup from meaning where character_seq = " + character_seq + "  EXCEPT select NULL", null);

        c.moveToFirst();
        return c;

    }

    public Cursor getKanjiBySearchStringNanori(String searchString, int inSpinnerSelectedItem) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = null;
        searchString = searchString.replace("'", "$");

        if (InputUtils.isStringAllKana(searchString) || InputUtils.isStringAllRomaji(searchString)) {
            String stringRomaji = InputUtils.getRomaji(searchString, false);
            stringRomaji = InputUtils.prepareQueryForSearchRomaji(stringRomaji);

            c = db.rawQuery("select C.character_seq as character_seq, literal, read, r_type, replace(group_concat(distinct ifnull(mean, '')),',', ', ') as meaning from character C join reading R on (C.character_seq = R.character_seq) join meaning M on (M.character_seq = R.character_seq) " +
                    "where C.character_seq in (" +
                    "select distinct C.character_seq  from character C " +
                    "join nanori N on (C.character_seq = N.character_seq) " +
                    "join misckanji MK ON (MK.character_seq = C.character_seq) " +
                    "where (name_romaji = '" + stringRomaji + "' ) " + InputUtils.getKanjiSpinnerItem(inSpinnerSelectedItem) + " " +
                    ")" +
                    " and r_type in ('ja_on', 'ja_kun') group by C.character_seq, r_type, read " +
                    "union select C.character_seq as character_seq, C.literal as literal, Name as read, 'nanori' as r_type, '' as meaning from character C join nanori N on (c.character_seq = N.character_seq) " +
                    "where N.character_seq in (" +
                    "select distinct C.character_seq  from character C " +
                    "join nanori N on (C.character_seq = N.character_seq) " +
                    "join misckanji MK ON (MK.character_seq = C.character_seq) " +
                    "where (name_romaji = '" + stringRomaji + "' ) " + InputUtils.getKanjiSpinnerItem(inSpinnerSelectedItem) + " " +
                    ") " +
                    "order by character_seq, r_type" +
                    "" +
                    "", null);
        } else if (searchString.length() == 1) {
            c = db.rawQuery("select C.character_seq as character_seq, literal, read, r_type, replace(group_concat(distinct ifnull(mean, '')),',', ', ') as meaning from character C join reading R on (C.character_seq = R.character_seq) join meaning M on (M.character_seq = R.character_seq) " +
                    "where C.character_seq in (" +
                    "select distinct C.character_seq  from character C " +
                    "join nanori N on (C.character_seq = N.character_seq) " +
                    "join misckanji MK ON (MK.character_seq = C.character_seq) " +
                    "where (name_romaji = '" + searchString + "' )  " + InputUtils.getKanjiSpinnerItem(inSpinnerSelectedItem) + " " +
                    ")" +
                    " and r_type in ('ja_on', 'ja_kun') group by C.character_seq, r_type, read " +
                    "union select C.character_seq as character_seq, C.literal as literal, Name as read, 'nanori' as r_type, '' as meaning from character C join nanori N on (c.character_seq = N.character_seq) " +
                    "where N.character_seq in (" +
                    "select distinct C.character_seq  from character C " +
                    "join nanori N on (C.character_seq = N.character_seq) " +
                    "join misckanji MK ON (MK.character_seq = C.character_seq) " +
                    "where (name_romaji = '" + searchString + "' )  " + InputUtils.getKanjiSpinnerItem(inSpinnerSelectedItem) + " " +
                    ") " +
                    "order by character_seq, r_type" +
                    "" +
                    "", null);
        } else return null;

        c.moveToFirst();
        return c;
    }

    public Cursor getKanjiBySearchString(String searchString, boolean searchMeaning, int inSpinnerSelectedItem, String r_type, String offset, boolean inSearchKanji) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = null;

        searchString = searchString.replace("'", "$");

        if (!inSearchKanji && (InputUtils.isStringAllKana(searchString) || InputUtils.isStringAllRomaji(searchString))) {
            String stringRomaji = InputUtils.getRomaji(searchString, false);
            stringRomaji = InputUtils.prepareQueryForSearchRomaji(stringRomaji);

            if (!searchMeaning) {

                c = db.rawQuery("select C.character_seq as character_seq, literal, ifnull(read,'') as read, r_type, replace(group_concat(distinct ifnull(mean, '')),',', ', ') as meaning from character C join reading R on (C.character_seq = R.character_seq) left join meaning M on (M.character_seq = R.character_seq) " +
                        "where C.character_seq in (" +
                        "select C.character_seq  from character C " +
                        "join reading R on (C.character_seq = R.character_seq) " +
                        "join meaning M on (C.character_seq = M.character_seq) " +
                        "left join misckanji MK ON (MK.character_seq = C.character_seq) " +
                        "left join rad_name RN ON (RN.character_seq = C.character_seq) " +
                        "where ( " + "read_romaji = '" + stringRomaji + "'" + ")  " + InputUtils.getKanjiSpinnerItem(inSpinnerSelectedItem) + "  " +
                        ")" +
                        " and r_type = " + r_type + " and read_romaji = '" + stringRomaji + "' group by C.character_seq, r_type, read " +
                        "order by character_seq, r_type " + offset +
                        "" +
                        "", null);
            } else if (InputUtils.isStringAllRomaji(searchString)){
                c = db.rawQuery("select C.character_seq as character_seq, literal, ifnull(read,'') as read, ifnull(r_type,'') as r_type, replace(group_concat(distinct ifnull(mean, '')),',', ', ') as meaning from character C " +
                        " join meaning M on (M.character_seq = C.character_seq) left join reading R on (C.character_seq = R.character_seq) " +
                        "where C.character_seq in (" +
                        "select C.character_seq  from character C " +
                        "join meaning M on (C.character_seq = M.character_seq) " +
                        "join reading R on (C.character_seq = R.character_seq) " +
                        "left join misckanji MK ON (MK.character_seq = C.character_seq) " +
                        "left join rad_name RN ON (RN.character_seq = C.character_seq) " +
                        "where (M.mean glob '" + searchString + "*'" + ")  " + InputUtils.getKanjiSpinnerItem(inSpinnerSelectedItem) + "  " +
                        ")" +
                        " " + (r_type == null ? "" : (" and r_type = " + r_type + " ")) + " group by C.character_seq " +


                        "order by character_seq, r_type " + offset + //(searchMeaning ? (" limit 11 offset " + String.valueOf(offset) + " ") : "") +
                        "" +
                        "", null);
            }
        } else if (inSearchKanji && !searchMeaning && !InputUtils.isStringAllKana(searchString) && !InputUtils.isStringAllRomaji(searchString)) {
            c = db.rawQuery("select C.character_seq as character_seq, literal, ifnull(read,'') as read, r_type, replace(group_concat(distinct ifnull(mean, '')),',', ', ') as meaning from character C join reading R on (C.character_seq = R.character_seq) left join meaning M on (M.character_seq = R.character_seq) " +
                    "where C.character_seq in (" +
                    "select C.character_seq  from character C " +
                    "left join reading R on (C.character_seq = R.character_seq)" +
                    "left join misckanji MK ON (MK.character_seq = C.character_seq) " +
                    "left join rad_name RN ON (RN.character_seq = C.character_seq) " +
                    "where ('" + searchString + "' like '%'||C.literal||'%')  " + InputUtils.getKanjiSpinnerItem(inSpinnerSelectedItem) + " " +
                    ")" +
                    " group by C.character_seq, r_type, read " +
                    "union select C.character_seq as character_seq, C.literal as literal, ifnull(Name,'') as read, 'nanori' as r_type, '' as meaning from character C join nanori N on (c.character_seq = N.character_seq) " +
                    "where N.character_seq in (" +
                    "select C.character_seq  from character C " +
                    "left join reading R on (C.character_seq = R.character_seq)" +
                    "left join misckanji MK ON (MK.character_seq = C.character_seq) " +
                    "left join rad_name RN ON (RN.character_seq = C.character_seq) " +
                    "where ('" + searchString + "' like '%'||C.literal||'%') " + InputUtils.getKanjiSpinnerItem(inSpinnerSelectedItem) + " " +
                    ") " +
                    "order by character_seq, r_type " +
                    "" +
                    "", null);
        } else return null;

        if (c != null) {
            c.moveToFirst();
        }

        return c;
    }


    public Cursor getCharacterSets(int character_seq) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("select cp_type, cp_value from codepoint where character_seq = " + character_seq, null);

        c.moveToFirst();
        return c;

    }

    public Cursor getKanjiDecompositionLiterals(int character_seq) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("select decomposition from character where character_seq = '" + character_seq + "'", null);

        c.moveToFirst();
        return c;
    }


    public Cursor getKanjiMisc(int character_seq) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("select grade, stroke_count, freq, jlpt from misckanji where character_seq = " + character_seq, null);

        c.moveToFirst();
        return c;

    }


    private void addChangedForm(HashSet<Pair<String, String>> reading, String read, String original, String changed, String type) {

        //if (read.startsWith(original)) // 37.3%
        if (read.charAt(0) == original.charAt(0)) {
            final int len = read.length();

            if (len > 1) {

                String readNew = changed;

                for (int i = 1; i < read.length(); i++) {

                    readNew += String.valueOf(read.charAt(i));
                    //reading.add(changed + read.substring(1, i));
                    reading.add(new Pair<String, String>(readNew, type));
                }
            } else {
                reading.add(new Pair<String, String>(changed, type));
            }

        }
    }
}
