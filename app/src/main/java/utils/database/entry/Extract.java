package utils.database.entry;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.util.TypedValue;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mino.jdict.adapters.AdapterHelper;
import com.mino.jdict.objects.activities.ExampleDetailObject;
import com.mino.jdict.objects.activities.ExampleDetailObjects;
import com.mino.jdict.objects.activities.ExtractObject;
import com.mino.jdict.objects.activities.ListObject;
import com.mino.jdict.objects.basic.GrammarObject;

import org.acra.ACRA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import utils.database.JMDictHelper;
import utils.database.WordSearchState;
import utils.grammar.InputUtils;
import utils.other.ExtractHelper;
import utils.other.StartupStaticData;

/**
 * Created by Dominik on 10/15/2015.
 */
public class Extract {

    JMDictHelper dbHelper;
    ExtractHelper mExtractHelper = new ExtractHelper();

    public Extract(JMDictHelper inHelper) {
        dbHelper = inHelper;
    }

    private static HashMap<String, ArrayList<GrammarObject>> simpleFormsCache = new HashMap<String, ArrayList<GrammarObject>>();

    private int getFirstKanjiPos(String inSearchString) {
        for (int i = inSearchString.length() - 1; i >= 0; i--) {

            if (InputUtils.isCharKanji(inSearchString.charAt(i))) {
                return i;
            }
        }

        return -1;
    }

    public Cursor getEntryForExtract(String searchString, WordSearchState inSearchState) {

        Cursor c = null;

        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            searchString = searchString.replace("'", "$");
            boolean allKanaMode = InputUtils.isStringAllKana(searchString);
            String romajiString = "";

            if (allKanaMode) {
                romajiString = InputUtils.getRomaji(searchString, false);
            }

            ArrayList<GrammarObject> simpleForms = null;
            //simpleForms = new ArrayList<GrammarObject>();
            simpleForms = getSimpleFormsIfNeeded(searchString, simpleForms);

            String query = "SELECT DISTINCT _id, conjugated, JLPT_level, Pos, IsCommon, grammarChain, ifnull(misc_g,'-') as misc_nn, kebList FROM ( ";


            if (allKanaMode) {

                query += "SELECT DISTINCT _id, conjugated, JLPT_level, Pos, IsCommon, grammarChain, misc_g, '' as kebList from ( " +
                        "SELECT DISTINCT _id, conjugated, JLPT_level, Pos, IsCommon, grammarChain, group_concat(ifnull(distinct misc,'-'),',') as misc_g FROM (" +
                        "SELECT DISTINCT E.ent_seq as _id, 0 as conjugated, JLPT_level, E.Pos, IsCommon, '' as grammarChain, M.misc, S.ID_sense FROM entry E " +
                        "INNER JOIN r_ele_fts R ON E.ent_seq = R.ent_seq  " +
                        " LEFT JOIN re_inf RINF on (RINF.ID_r_ele = R.rowid) " +
                        "INNER JOIN pos P ON P.ent_seq = E.ent_seq " +
                        " left join sense S on (E.ent_seq = S.ent_seq) " +
                        "LEFT JOIN misc M ON (M.ent_seq = E.ent_seq  and M.ID_sense = S.ID_sense) " +
                        "WHERE reb_romaji match '^" + romajiString + "' and length(reb_romaji) = " + romajiString.length() + " "
                        + " AND (re_inf is null or re_inf <> 'ok') " + " )  group by _id, conjugated, JLPT_level, Pos, IsCommon, grammarChain "
                        + "  ) D " +
                        " group by _id ";
            } else {
                query +=
                        "SELECT DISTINCT _id, conjugated, JLPT_level, Pos, IsCommon, grammarChain, misc_g, group_concat(ifnull(distinct keb, '-'), ',') as kebList  from ( " +
                                "SELECT DISTINCT _id, conjugated, JLPT_level, Pos, IsCommon, grammarChain, group_concat(ifnull(distinct misc,'-'),',') as misc_g FROM (" +
                                "SELECT DISTINCT E.ent_seq as _id, 0 as conjugated, JLPT_level, E.Pos, IsCommon,  '' as grammarChain, M.misc, S.ID_sense FROM entry E " +
                                "INNER JOIN k_ele_fts K ON E.ent_seq = K.ent_seq " +
                                "INNER JOIN pos P ON P.ent_seq = E.ent_seq " +
                                " left join sense S on (E.ent_seq = S.ent_seq) " +
                                "LEFT JOIN misc M ON (M.ent_seq = E.ent_seq  and M.ID_sense = S.ID_sense) " +
                                "WHERE keb match '^" + searchString + "' and length(keb) = " + searchString.length() + " "
                                + " )  group by _id, conjugated, JLPT_level, Pos, IsCommon, grammarChain  "
                                + "  ) D join k_ele_fts K on (K.ent_seq match D._id) " +
                                " group by _id ";
            }


            if (simpleForms != null && simpleForms.size() > 0) {

                for (GrammarObject simpleForm : simpleForms) {
                    if (!simpleForm.getWordType().isEmpty()) {
                        String romaji = InputUtils.getRomaji(simpleForm.getSimpleForm(), false);
                        int lengthRomaji = romaji.length();

                        query += " UNION ALL ";

                        query += " SELECT DISTINCT E.ent_seq as _id, 1 as conjugated, JLPT_level, E.[pos], IsCommon, '" + simpleForm.getGrammarChain() +
                                "' as grammarChain, group_concat(ifnull(distinct misc,'-'),',') as misc_g, '' as kebList FROM entry E INNER JOIN r_ele_fts R ON E.ent_seq = R.ent_seq "
                                + " left join sense S on (E.ent_seq = S.ent_seq) "
                                + " LEFT JOIN misc M ON (M.ent_seq = E.ent_seq and M.ID_sense = S.ID_sense) INNER JOIN pos P ON E.ent_seq = P.ent_seq WHERE  "
                                //" 1 = 1 "
                                + " (P.pos = '" + simpleForm.getWordType() + "' OR P.pos = 'exp') "
                                + " AND reb_romaji match '^" + romaji + "' and length(reb_romaji) = "
                                + lengthRomaji + "  group by _id, conjugated, JLPT_level, P.Pos, IsCommon, grammarChain " +
                                " UNION ALL " +
                                " SELECT DISTINCT _id, conjugated, JLPT_level, Pos, IsCommon, grammarChain, misc_g, group_concat(ifnull(distinct keb, '-'), ',') as kebList from ( " +
                                " SELECT DISTINCT E.ent_seq as _id, 1 as conjugated, JLPT_level, E.[pos], IsCommon, '"
                                + simpleForm.getGrammarChain() + "' as grammarChain, group_concat(ifnull(distinct misc,'-'),',') as misc_g FROM entry E INNER JOIN k_ele_fts K ON E.ent_seq = K.ent_seq "
                                + " left join sense S on (E.ent_seq = S.ent_seq) "
                                + "LEFT JOIN misc M ON (M.ent_seq = E.ent_seq and M.ID_sense = S.ID_sense) INNER JOIN pos P ON E.ent_seq = P.ent_seq WHERE "
                                //+ " 1 = 1 "
                                + "  (P.pos = '" + simpleForm.getWordType() + "' OR P.pos = 'exp') "
                                + " AND keb match '^"
                                + simpleForm.getSimpleForm() + "' and length(keb) = "
                                + simpleForm.getSimpleForm().length() + " group by _id, conjugated, JLPT_level, P.Pos, IsCommon, grammarChain"
                                + " ) D join k_ele_fts K on (K.ent_seq match D._id)" +
                                " group by _id";
                    } else {

                        query += " UNION ALL ";

                        query += " SELECT DISTINCT E.ent_seq as _id, 1 as conjugated, JLPT_level, E.[pos], IsCommon, '"
                                + simpleForm.getGrammarChain() + "' as grammarChain, group_concat(ifnull(distinct misc,'-'),',') as misc_g, '' as kebList FROM entry E INNER JOIN r_ele_fts R ON E.ent_seq = R.ent_seq "
                                + " left join sense S on (E.ent_seq = S.ent_seq) "
                                + " LEFT JOIN misc M ON (M.ent_seq = E.ent_seq and M.ID_sense = S.ID_sense) INNER JOIN pos P ON E.ent_seq = P.ent_seq WHERE reb_romaji match '^"
                                + simpleForm.getSimpleForm() + "' and length(reb_romaji) = "
                                + simpleForm.getSimpleForm().length() + " "
                                + "  group by _id, conjugated, JLPT_level, P.Pos, IsCommon, grammarChain " +
                                " UNION ALL " +
                                " SELECT DISTINCT _id, conjugated, JLPT_level, Pos, IsCommon, grammarChain, misc_g, group_concat(ifnull(distinct keb, '-'), ',') as kebList from ( " +
                                " SELECT DISTINCT E.ent_seq as _id, 1 as conjugated, JLPT_level, E.[pos], IsCommon, '"
                                + simpleForm.getGrammarChain() + "' as grammarChain, group_concat(ifnull(distinct misc,'-'),',') as misc_g FROM entry E INNER JOIN k_ele_fts K ON E.ent_seq = K.ent_seq "
                                + " left join sense S on (E.ent_seq = S.ent_seq) "
                                + " LEFT JOIN misc M ON (M.ent_seq = E.ent_seq and M.ID_sense = S.ID_sense) INNER JOIN pos P ON E.ent_seq = P.ent_seq WHERE keb match '^"
                                + simpleForm.getSimpleForm() + "' and length(keb) = "
                                + simpleForm.getSimpleForm().length()
                                + " group by _id, conjugated, JLPT_level, P.Pos, IsCommon, grammarChain "
                                + " ) D join k_ele_fts K on (K.ent_seq match D._id)" +
                                " group by _id";
                    }
                }
            }


            String bestMatchOrderString = getBestMatchOrderString(searchString, inSearchState);
            query += " ) where _id is not null " + bestMatchOrderString + " LIMIT 1 ";

            c = db.rawQuery(query, null);
            c.moveToFirst();

        } catch (Exception ex) {
            String message = ex.getMessage();
            ACRA.getErrorReporter().handleSilentException(ex);
        }

        return c;
    }

    private ArrayList<GrammarObject> getSimpleFormsIfNeeded(String searchString, ArrayList<GrammarObject> simpleForms) {
        int firstKanjiPos;

        firstKanjiPos = getFirstKanjiPos(searchString);

        if (firstKanjiPos < 0) {

            simpleForms = simpleFormsCache.get(searchString);

            if (simpleForms == null) {
                simpleForms = InputUtils.getPossibleSimpleForms(searchString);

                if (simpleFormsCache.size() > CACHE_SIZE) {
                    simpleFormsCache.clear();
                }

                simpleFormsCache.put(searchString, simpleForms);
            }

        } else {

            String firstPart = searchString.substring(0, firstKanjiPos);
            String lastPart = searchString.substring(firstKanjiPos, searchString.length());
            ArrayList<GrammarObject> temp = simpleFormsCache.get(lastPart);

            if (temp != null) {

                simpleForms = getSimpleFormFromCacheKanji(temp, firstPart);

            } else {

                temp = InputUtils.getPossibleSimpleForms(lastPart);

                if (simpleFormsCache.size() > CACHE_SIZE) {
                    simpleFormsCache.clear();
                }

                simpleFormsCache.put(lastPart, temp);
                simpleForms = getSimpleFormFromCacheKanji(temp, firstPart);
            }
        }

        return simpleForms;
    }


    @NonNull
    private ArrayList<GrammarObject> getSimpleFormFromCacheKanji(ArrayList<GrammarObject> temp, String firstPart) {
        ArrayList<GrammarObject> simpleForms;
        simpleForms = new ArrayList<GrammarObject>();

        for (GrammarObject obj : temp) {
            GrammarObject newObj = new GrammarObject(obj.getWordType(), obj.getGrammarWordType(), firstPart + obj.getChangedForm(), firstPart + obj.getSimpleForm(), obj.getGrammar(), obj.getGrammarChain());
            simpleForms.add(newObj);
        }
        return simpleForms;
    }

    @NonNull
    private String getBestMatchOrderString(String searchString, WordSearchState inSearchState) {

        boolean isStringAllKana = InputUtils.isStringAllKana((searchString));

        String bestMatchOrderString = " ORDER BY misc_nn = 'arch', ";

        if (inSearchState.isPreviousNoun()) {
            bestMatchOrderString += " Pos = 'n', ";
        }

        if (inSearchState.isPreviousAdv()) {
            bestMatchOrderString += " Pos = 'adv', ";
        }

        boolean prevCounter = inSearchState.isPreviousCounter();

        if (prevCounter || !inSearchState.isPreviousDigit()) {
            bestMatchOrderString += " Pos = 'ctr', ";
        } else if (inSearchState.isPreviousDigit()) {
            bestMatchOrderString += " Pos like '%ctr%' desc, ";
        }

        bestMatchOrderString += " IsCommon desc, ";

        if (isStringAllKana && (!inSearchState.isPrefix() || searchString.length() <= 2)) {
            bestMatchOrderString += " Pos like '%prt%' desc, ";
        }

        bestMatchOrderString += " Pos like '%ok%',  ";

        if (inSearchState.getWordTypeForbidden() != null && !inSearchState.getWordTypeForbidden().isEmpty()) {
            bestMatchOrderString += " Pos like '" + inSearchState.getWordTypeForbidden() + "%', ";
        }

        // kanji -> nie chcemy tych co mają usually written with kana alone
        if (!isStringAllKana) {
            bestMatchOrderString += " CASE WHEN misc_nn like 'uk%' THEN 2 ELSE 1 END, ";
        } else {

            bestMatchOrderString += " CASE WHEN misc_nn like 'uk%' then 0 ELSE 1 END,  ";
        }

        boolean hasPref = true;

        if (inSearchState.getWordTypePreference() != null && !inSearchState.getWordTypePreference().isEmpty()) {
            bestMatchOrderString += " Pos like '%" + inSearchState.getWordTypePreference() + "%' desc, ";
        } else {

            hasPref = false;
        }

        if (InputUtils.isStringAllKanji(searchString.substring(0, 1))) {
            bestMatchOrderString += " CASE WHEN ('" + searchString.substring(0, 1) + "' like kebList||'%' OR kebList like '" + searchString.substring(0, 1) + "%') THEN 1 ELSE 2 END, ";
        }

        boolean isSuffix = inSearchState.isSuffix();

        if (isSuffix) {
            bestMatchOrderString += " Pos like '%suf%' desc, ";
        } else {
            bestMatchOrderString += " Pos like 'suf%', ";
        }

        if (!hasPref) {

            bestMatchOrderString += " Pos like '%prt%' desc, Pos like '%n-t%' desc, Pos like '%int%' desc, Pos like '%cop-da%' desc, Pos like '%aux%' desc, " +
                    "  Pos like '%vk%' desc, Pos like '%v5-aru%' desc, Pos like '%v5r-i%' desc,  Pos like '%v5k-s' desc, " +
                    "  Pos like '%vs-s%' desc, Pos like '%vs-i%' desc, Pos like '%num%', ";
        }


        if (inSearchState.isPrefix()) {
            bestMatchOrderString += " Pos like 'pref%' desc, Pos like '%pref%' desc, Pos like '%ctr%', Pos like '%suf%' ,";
        } else {
            bestMatchOrderString += " Pos like '%pref%', ";
        }

        /*
        if (inSearchState.isSuffix()) {
            bestMatchOrderString += " Pos like '%suf%' desc, ";
        }*/


        bestMatchOrderString += " Pos like '%exp%' desc, conjugated, Pos like '%vs%' desc, ";

        if (!isSuffix) {
            bestMatchOrderString += " Pos like '%suf%', ";
        }

        if (prevCounter) {
            bestMatchOrderString += " Pos like 'n%' desc, ";
        }

        bestMatchOrderString += " JLPT_level desc, length(misc_nn) desc ";

        return bestMatchOrderString;
    }

    public Pair<ArrayList<ExtractObject>, Integer> getWordsBySearchString(String inSearchString, Boolean inRomaji, double inProgressWeight, ProgressBar inProgressBar) {

        // Debug.startMethodTracing("getWordsNew");
        Interrupt = false;
        ArrayList<ListObject> result = new ArrayList<ListObject>();

        Pair<String, HashMap<Character, InputUtils.CharType>> stringHashMap = InputUtils.getStringDescription(inSearchString);
        //String cleanString = stringHashMap.first;
        HashMap<Character, InputUtils.CharType> typeMap = stringHashMap.second;

        String currentString = "";
        ArrayList<String> listToProcess = new ArrayList<String>();

        for (int i = 0; i < inSearchString.length(); i++) {

            if (Interrupt) {
                Interrupt = false;
                return null;
            }

            // wiemy co to za char
            if (typeMap.containsKey(inSearchString.charAt(i))) {

                // tniemy
                if (canCut(inSearchString, typeMap, i)) {

                    if (currentString.length() > 0) {
                        listToProcess.add(currentString);
                        currentString = "";
                    }

                    if (typeMap.get(inSearchString.charAt(i)) == InputUtils.CharType.Katakana
                            || typeMap.get(inSearchString.charAt(i)) == InputUtils.CharType.Hiragana
                            || typeMap.get(inSearchString.charAt(i)) == InputUtils.CharType.Kanji) {
                        currentString += String.valueOf(inSearchString.charAt(i));
                    }

                } else {
                    currentString += String.valueOf(inSearchString.charAt(i));
                }
            }
        }

        if (currentString.length() > 0) {
            listToProcess.add(currentString);
        }

        if (listToProcess.size() > 0) {

            double progressStep = inProgressWeight / (double) listToProcess.size();
            double progress = progressStep;
            double previousProgress = 0;

            for (String processString : listToProcess) {

                if (Interrupt) {
                    Interrupt = false;
                    return null;
                }

                if (wordsBySearchResults.containsKey(processString)) {
                    result.addAll(wordsBySearchResults.get(processString));

                } else {
                    ArrayList<ListObject> toSave = getWordsBySearchStringAux(inRomaji, result, processString, typeMap, 0, processString.length(), true);
                    saveToCache(toSave, processString);
                }

                progress += progressStep;
                if (((int)progress) > previousProgress) {
                    inProgressBar.setProgress((int)progress);
                    previousProgress = (int)progress;
                }
            }
        }

        // Debug.stopMethodTracing();

        ArrayList<ExtractObject> extractObjects = new ArrayList<ExtractObject>();

        ExtractObject furiganaObj = mExtractHelper.addSentenceWithFurigana(inSearchString, result, dbHelper);
        divideFuriganaObject(extractObjects, furiganaObj);
        //extractObjects.add(furiganaObj);

        ExtractObject separator = new ExtractObject();
        separator.IsSeparator.set(true);
        extractObjects.add(separator);

        int divideParts = extractObjects.size();

        for (ListObject obj : result) {
            extractObjects.add(new ExtractObject(obj));
        }

        return new Pair<ArrayList<ExtractObject>, Integer>(extractObjects, divideParts);
    }

    public static boolean Interrupt = false;

    private void divideFuriganaObject(ArrayList<ExtractObject> extractObjects, ExtractObject furiganaObj) {
        if (furiganaObj != null) {

            final float densityMultiplier = dbHelper.getContext().getResources().getDisplayMetrics().density;

            DisplayMetrics displayMetrics = new DisplayMetrics();
            WindowManager windowmanager = (WindowManager) dbHelper.getContext().getSystemService(Context.WINDOW_SERVICE);
            windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
            //int deviceWidth = displayMetrics.widthPixels;

            final float scaledPx = displayMetrics.widthPixels; //350 * densityMultiplier;

            TextView tvKanji = new TextView(dbHelper.getContext());
            tvKanji.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);

            TextView tvRest = new TextView(dbHelper.getContext());
            tvRest.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);

            Paint textPaintKanji = tvKanji.getPaint();
            Paint textPaintRest = tvRest.getPaint();

            float size = (int) (12 * densityMultiplier);
            int j = 0;

            for (int i = 0; i < furiganaObj.Example.get().ExampleDetailObjectList.get().size(); i++) {

                ExampleDetailObject currentObj = furiganaObj.Example.get().ExampleDetailObjectList.get().get(i);
                float currentMax = 0;

                for (int l = 0; l < currentObj.getRebKebList().size(); l++) {

                    Pair<String, String> rebKebPair = currentObj.getRebKebList().get(l);

                    String romaji = AdapterHelper.getRomajiFromRebKebList(currentObj, l);

                    float widthKanji = textPaintKanji.measureText(rebKebPair.second.trim());
                    float widthDown = textPaintRest.measureText(rebKebPair.first);
                    float widthRomaji = textPaintRest.measureText(romaji);

                    currentMax += Math.max(widthRomaji, Math.max(widthKanji, widthDown));
                }

                size += currentMax;

                if (size >= scaledPx) {

                    addExampleDetailObjects(extractObjects, furiganaObj, j, i);

                    j = i;
                    size = currentMax + ((int) (12 * densityMultiplier));
                }

                size += (int) (12 * densityMultiplier + 0.5f);
            }

            if (j < furiganaObj.Example.get().ExampleDetailObjectList.get().size()) {
                addExampleDetailObjects(extractObjects, furiganaObj, j, furiganaObj.Example.get().ExampleDetailObjectList.get().size());
            }

            ExtractObject separator = new ExtractObject();
            separator.IsSeparator.set(true);
            extractObjects.add(separator);
        }
    }


    private void addExampleDetailObjects(ArrayList<ExtractObject> extractObjects, ExtractObject furiganaObj, int j, int i) {
        ExampleDetailObjects objects = new ExampleDetailObjects();

        for (int k = j; k < i; k++) {
            objects.ExampleDetailObjectList.add(furiganaObj.Example.get().ExampleDetailObjectList.get().get(k));
        }

        ExtractObject newObj = new ExtractObject(objects);
        extractObjects.add(newObj);
    }

    private boolean canCut(String inSearchString, HashMap<Character, InputUtils.CharType> typeMap, int i) {
        boolean canCut = false;

        canCut |= (typeMap.get(inSearchString.charAt(i)) == InputUtils.CharType.Punctuation || typeMap.get(inSearchString.charAt(i)) == InputUtils.CharType.Other) && inSearchString.charAt(i) != '々';
        if (i > 0) {
            //canCut |= typeMap.get(inSearchString.charAt(i - 1)) == InputUtils.CharType.Katakana && typeMap.get(inSearchString.charAt(i)) == InputUtils.CharType.Hiragana;
            canCut |= typeMap.get(inSearchString.charAt(i - 1)) == InputUtils.CharType.Hiragana && typeMap.get(inSearchString.charAt(i)) == InputUtils.CharType.Katakana;

            canCut |= (inSearchString.charAt(i - 1) == 'は'
                    || inSearchString.charAt(i - 1) == 'が'
                    //|| inSearchString.charAt(i - 1) == 'に'
                    || inSearchString.charAt(i - 1) == 'を'
                    || inSearchString.charAt(i - 1) == 'へ'
                    || inSearchString.charAt(i - 1) == 'な'
                    //|| inSearchString.charAt(i - 1) == 'の'
                    || inSearchString.charAt(i - 1) == 'で')
                    && typeMap.get(inSearchString.charAt(i)) == InputUtils.CharType.Kanji;
        }

        return canCut;
    }

    private String getPreference(ListObject obj) {
        if (obj.WordType.get().contentEquals("prt") && StartupStaticData.PARTICLE_WORDTYPE_PREFERENCES.containsKey(obj.Query.get())) {
            return StartupStaticData.PARTICLE_WORDTYPE_PREFERENCES.get(obj.Query.get());
        }

        return null;
    }

    private String getForbidden(ListObject obj) {
        if (obj.WordType.get().contentEquals("prt") && StartupStaticData.PARTICLE_WORDTYPE_FORBIDDEN.containsKey(obj.Query.get())) {
            return StartupStaticData.PARTICLE_WORDTYPE_FORBIDDEN.get(obj.Query.get());
        }

        return null;
    }

    private static HashMap<String, ArrayList<ListObject>> wordsBySearchResults = new HashMap<String, ArrayList<ListObject>>();
    private final int CACHE_SIZE = 1000;

    private static HashMap<String, ArrayList<ListObject>> wordsByFindBetterMatch = new HashMap<String, ArrayList<ListObject>>();

    public ArrayList<ListObject> getWordsBySearchStringAux(Boolean inRomaji, ArrayList<ListObject> result, String cleanString, HashMap<Character, InputUtils.CharType> inTypeMap, int inOriginalStart, int inOriginalEnd, boolean isStart) {


        ArrayList<ListObject> toSave = new ArrayList<ListObject>();
        boolean isPreviousDigit = false;
        int start = 0;
        int end = cleanString.length();
        if (end > 37) end = 37; //max (reb,keb) len

        while (start != cleanString.length()) {

            if (start >= end) break;
            if (end > cleanString.length()) break;

            String currentSubString = cleanString.substring(start, end);

            String previousWordType = null;
            String previousConjugation = null;
            String wordTypePreference = null;
            String wordTypeForbidden = null;

            if (toSave.size() > 0) {
                ListObject prevObj = toSave.get(toSave.size() - 1);
                previousWordType = prevObj.WordType.get();
                previousConjugation = prevObj.GrammarChain.get();
                wordTypePreference = getPreference(prevObj);
                wordTypeForbidden = getForbidden(prevObj);
                isPreviousDigit = InputUtils.isNumeric(prevObj.getKeb());
            } else if (result.size() > 0) { //  && !isStart) {
                ListObject prevObj = result.get(result.size() - 1);
                previousWordType = prevObj.WordType.get();
                previousConjugation = prevObj.GrammarChain.get();
                wordTypePreference = getPreference(prevObj);
                wordTypeForbidden = getForbidden(prevObj);
                isPreviousDigit = InputUtils.isNumeric(prevObj.getKeb());
            }

            ListObject newEntry;
            WordSearchState state = new WordSearchState(start, end, inOriginalStart, inOriginalEnd, cleanString, previousWordType, previousConjugation, wordTypePreference, wordTypeForbidden, isPreviousDigit);

            if ((newEntry = ExtractHelper.checkCache(state, inRomaji)) != null) {

                isStart = false;
                newEntry.Query.set(currentSubString);

            } else {

                Cursor c = getEntryForExtract(currentSubString, state);

                // znaleźliśmy jakieś słowo
                if (c != null && c.getCount() > 0) {

                    ArrayList<ListObject> newEntryList = new ArrayList<ListObject>();
                    dbHelper.getWord().getDataFromCursor(newEntryList, c, inRomaji, false, false, false);

                    // powinien być 1
                    for (ListObject obj : newEntryList) {
                        obj.Query.set(currentSubString);
                    }

                    newEntry = newEntryList.get(0);

                    if (StartupStaticData.DoubleCheckPreloadedStrings.contains(currentSubString)) {
                        newEntry.IsPreloaded.set(true);
                        StartupStaticData.DOUBLE_CHECK_PRELOADED_OBJECTS.put(currentSubString, newEntry);
                    }
                }
            }

            if (newEntry != null) {
                ExtractHelper.DoubleCheckResult doubleCheckLength;


                boolean isAllKana = InputUtils.isStringAllKana(newEntry.Query.get());
                // być może potrzebny inny podział -> dostaliśmy słowo rzadkie zaczynające się od czegoś co chcemy sprawdzić (partykuła itp)
                if (
                        (end - start != cleanString.length()
                                || (!newEntry.IsCommon.get() || isAllKana)
                        //        || (newEntry.getMisc().contains("uk") && !isAllKana)
                        )
                                //&& (!newEntry.getWordType().contains("prt") || !newEntry.getIsCommon())
                                && (isAllKana || splitesNonToSplit(cleanString, start, end))
                                && (!newEntry.WordType.get().contains("exp") || !newEntry.IsCommon.get())
                                && (!newEntry.WordType.get().contains("adj-na") || cleanString.length() < end + 1 || !cleanString.substring(end, end + 1).equals("な"))
                                && (end - start >= 2)
                                && (!(newEntry.IsConjugated.get() && newEntry.IsCommon.get() && newEntry.Length.get() >= 3 && !newEntry.GrammarChain.get().contains("-i form")))
                                && ((doubleCheckLength = mExtractHelper.toDoubleCheck(newEntry, start, end, inOriginalStart, inOriginalEnd, cleanString)) != null)) {


                    toSave.addAll(mExtractHelper.findBetterMatch(inRomaji, cleanString, inTypeMap, start, currentSubString.length() + start, inOriginalStart, inOriginalEnd, newEntry, doubleCheckLength, dbHelper, previousWordType, previousConjugation, wordTypePreference, wordTypeForbidden, isPreviousDigit));
                    result.addAll(toSave);
                    return toSave;
                } else {

                    checkCacheSize();

                    if (newEntry != null) {
                        StartupStaticData.EXTRACT_SAVED_OBJECTS.put(state, newEntry);
                    }

                    toSave.add(newEntry);
                }

                start = end;
                end = cleanString.length();

            } else {

                /*
                boolean cantSplit = false;
                for (String notToSplit : StartupStaticData.NotToSplitStrings) {
                    if (currentSubString.endsWith(notToSplit) && !currentSubString.equals(notToSplit)) {
                        end -= notToSplit.length();
                        cantSplit = true;
                        break;
                    }
                }

                if (!cantSplit) {
                    end--;
                }
                */
                end--;

                if (start == end && start != cleanString.length()) {
                    ListObject obj = new ListObject(0, false, 0, "", "", 0, false, "", false);
                    ArrayList<String> keb = new ArrayList<String>();
                    keb.add(cleanString.substring(start, start + 1));

                    obj.setRebKebSense(null, keb, null);
                    toSave.add(obj);
                    start++;
                    end = cleanString.length();
                }
            }
        }

        result.addAll(toSave);
        return toSave;
    }

    private void checkCacheSize() {

        if (StartupStaticData.EXTRACT_SAVED_OBJECTS.size() > CACHE_SIZE) {

            int j = 0;
            Iterator<WordSearchState> i = StartupStaticData.EXTRACT_SAVED_OBJECTS.keySet().iterator();
            while (i.hasNext() && j < (CACHE_SIZE / 10)) {
                i.next(); // must be called before you can call i.remove()
                i.remove();
                j++;
            }
        }
    }

    private boolean splitesNonToSplit(String wholeString, int start, int end) {

        String cutStr = wholeString.substring(start);
        for (String notToSplit : StartupStaticData.NotToSplitStrings) {
            int foundPlace = cutStr.indexOf(notToSplit);
            if (foundPlace > 0 && foundPlace < end - start) {
                return true;
            }
        }

        return false;
    }

    private void saveToCache(ArrayList<ListObject> toSave, String cleanString) {
        if (wordsBySearchResults.size() > CACHE_SIZE) {

            int j = 0;
            Iterator<String> i = wordsBySearchResults.keySet().iterator();
            while (i.hasNext() && j < CACHE_SIZE - 200) {
                i.next(); // must be called before you can call i.remove()
                i.remove();
                j++;
            }
        }

        wordsBySearchResults.put(cleanString, toSave);
    }

}
