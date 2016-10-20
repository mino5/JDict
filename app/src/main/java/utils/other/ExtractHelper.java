package utils.other;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Pair;

import com.mino.jdict.objects.activities.ExampleDetailObject;
import com.mino.jdict.objects.activities.ExampleDetailObjects;
import com.mino.jdict.objects.activities.ExtractObject;
import com.mino.jdict.objects.activities.ListObject;
import com.mino.jdict.objects.basic.GrammarObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import utils.database.JMDictHelper;
import utils.database.WordSearchState;
import utils.grammar.FuriganaHelper;
import utils.grammar.GrammarDictionaries;
import utils.grammar.InputUtils;

/**
 * Created by Dominik on 10/12/2015.
 */
public class ExtractHelper {

    public ExtractObject addSentenceWithFurigana(String inSearchString, ArrayList<ListObject> inResult, JMDictHelper dbHelper) {

        ExampleDetailObjects objects = new ExampleDetailObjects();
        FuriganaHelper furiganaHelper = new FuriganaHelper();
        String partNotInObj = "";

        String inSearchStringTrimmed = "";
        for (char c : inSearchString.toCharArray()) {
            if (c != ' ') inSearchStringTrimmed += c;
        }

        for (ListObject lObj : inResult) {

            ExampleDetailObject exampleObj = null;

            while (exampleObj == null) {

                if (lObj.Query.get() != null && inSearchStringTrimmed.length() >= lObj.Query.get().length()) {

                    if (InputUtils.isStringAllKana(inSearchStringTrimmed.substring(0, lObj.Query.get().length()))) {

                        // reb mode
                        if (InputUtils.isStringAllKana(lObj.Query.get())) {

                            partNotInObj = checkAddPartNotInObj(objects, partNotInObj);

                            exampleObj = new ExampleDetailObject(lObj.ID.get());
                            furiganaHelper.matchFurigana(exampleObj, lObj.Query.get(), lObj.Query.get());

                            exampleObj.ListObj.set(lObj);
                            objects.ExampleDetailObjectList.add(exampleObj);
                            inSearchStringTrimmed = inSearchStringTrimmed.substring(lObj.Query.get().length());
                        }
                    } else if (InputUtils.isStringAllKanaOrKanji(inSearchStringTrimmed.substring(0, lObj.Query.get().length()))) {

                        // keb mode
                        if (!InputUtils.isStringAllKana(lObj.Query.get())) {

                            partNotInObj = checkAddPartNotInObj(objects, partNotInObj);
                            String reb = lObj.getFromRebList(0);
                            exampleObj = new ExampleDetailObject(lObj.ID.get());

                            if (!lObj.IsConjugated.get()) {
                                furiganaHelper.matchFurigana(exampleObj, lObj.Query.get(), reb);
                            } else {

                                String newReb = createFormFromGrammar(lObj, reb, lObj.Query.get());
                                furiganaHelper.matchFurigana(exampleObj, lObj.Query.get(), newReb);
                            }

                            exampleObj.ListObj.set(lObj);
                            objects.ExampleDetailObjectList.add(exampleObj);
                            inSearchStringTrimmed = inSearchStringTrimmed.substring(lObj.Query.get().length());
                        }
                    }
                } else if (lObj.Query.get() == null) {

                    if (lObj.getKeb() != null && lObj.getKeb().length() > 0 && inSearchStringTrimmed.startsWith(lObj.getKeb())) {
                        partNotInObj += lObj.getKeb();
                        inSearchStringTrimmed = inSearchStringTrimmed.substring(lObj.getKeb().length());
                        break;
                    } else if (lObj.getReb() != null && lObj.getReb().length() > 0 && inSearchStringTrimmed.startsWith(lObj.getReb())) {
                        partNotInObj += lObj.getReb();
                        inSearchStringTrimmed = inSearchStringTrimmed.substring(lObj.getReb().length());
                        break;
                    }
                }

                if (exampleObj != null) continue;

                if (inSearchStringTrimmed.length() > 0) {

                    if (!inSearchStringTrimmed.substring(0, 1).equals("\n")) {
                        partNotInObj += inSearchStringTrimmed.substring(0, 1);
                    }

                    inSearchStringTrimmed = inSearchStringTrimmed.substring(1);

                } else {
                    break;
                }

                /*
                if (lObj.getQuery() == null) {
                    break;
                }
                */

            }
        }

        if (partNotInObj.length() > 0) {
            ExampleDetailObject notAWordObj = new ExampleDetailObject();
            notAWordObj.addRebKeb(partNotInObj, partNotInObj);
            objects.ExampleDetailObjectList.add(notAWordObj);
        }

        if (inSearchStringTrimmed.length() > 0)

        {
            ExampleDetailObject notAWordObj = new ExampleDetailObject();
            notAWordObj.addRebKeb(inSearchStringTrimmed, inSearchStringTrimmed);
            objects.ExampleDetailObjectList.add(notAWordObj);
        }

        if (objects.ExampleDetailObjectList.get().isEmpty()) {
            return null;
        }

        return new ExtractObject(objects);
    }

    @NonNull
    private String checkAddPartNotInObj(ExampleDetailObjects objects, String partNotInObj) {
        if (partNotInObj.length() > 0) {
            ExampleDetailObject notAWordObj = new ExampleDetailObject();
            notAWordObj.addRebKeb(partNotInObj, partNotInObj);
            objects.ExampleDetailObjectList.add(notAWordObj);
        }

        partNotInObj = "";
        return partNotInObj;
    }

    private static String createFormFromGrammar(ListObject lObj, String rebSimple, String item) {

        String specialReading = checkSpecialReading(lObj);
        if (specialReading != null) return specialReading;

        String currentWordType = lObj.getWordTypeFirst();
        String rebToReturn = rebSimple;

        ArrayList<String> grammarList = GrammarObject.convertGrammarChainToList(lObj.GrammarChain.get());

        // budujemy reb-a
        for (int i = 0; i < grammarList.size(); i++) {

            String grammarName = grammarList.get(i);
            List<GrammarObject> grammar = GrammarDictionaries.GRAMMAR_MAP.get(grammarName);

            if (grammar != null) {

                for (GrammarObject grammarObj : grammar) {

                    if ((grammarObj.getWordType().equals(currentWordType) || (currentWordType.equals("exp") && rebToReturn.endsWith(grammarObj.getSimpleForm())))
                            && InputUtils.isStringAllKana(grammarObj.getChangedForm())
                            && ((grammarObj.getWordType() != "aux"
                            && grammarObj.getWordType() != "vs-i"
                            //&& (grammarObj.getWordType() != "v1" || grammarName.equals("-i form"))
                            && grammarObj.getWordType() != "vs"
                            && !grammarName.equals("negative")
                            && !grammarName.equals("progressive")) // tteru - tteiru
                            || (rebToReturn.endsWith(grammarObj.getSimpleForm())))) {

                        rebToReturn = rebToReturn.substring(0, rebToReturn.length() - grammarObj.getSimpleForm().length()) + grammarObj.getChangedForm();
                        String newWordType = InputUtils.getGrammarWordType(grammarName);

                        if (newWordType != null) {
                            currentWordType = newWordType;
                        }

                        break;
                    }
                }
            }
        }

        StringBuilder newReb = new StringBuilder();

        int i = rebToReturn.length() - 1;
        int j = item.length() - 1;
        while (i >= 0 && j >= 0) {

            String s1 = item.substring(j, j + 1);
            String s2 = rebToReturn.substring(i, i + 1);

            if (s2.equals(s1)) {
                newReb.append(s2);
            } else if (InputUtils.isStringAllKanji(s1)) {
                newReb.append(s2);
                i--;
                j--;
                break;
            } else if (i > 0 && rebToReturn.substring(i - 1, i).equals(s1)) {
                i--;
                continue;
            } else {
                return rebToReturn;
            }

            i--;
            j--;
        }

        while (i >= 0) {
            newReb.append(rebToReturn.substring(i, i + 1));
            i--;
        }

        return newReb.reverse().toString();
    }

    private static String checkSpecialReading(ListObject item) {
        if (item.Query.get().startsWith("出来") && (item.WordType.get().equals("vs-i") || item.WordType.get().equals("v1"))) {
            return "でき" + item.Query.get().substring(2, item.Query.get().length());
        }

        return null;
    }

    public enum WordPosition {Prefix, Suffix, SuffixExcess}


    public class DoubleCheckResult {
        private WordPosition mPosition;
        private int mCut;
        private int mLength;

        public DoubleCheckResult(WordPosition inPosition, int inCut, int inLength) {

            mPosition = inPosition;
            mCut = inCut;
            mLength = inLength;
        }

        public WordPosition getWordPosition() {
            return mPosition;
        }

        public int getCut() {
            return mCut;
        }

        public int getLength() {
            return mLength;
        }

    }

    public static ListObject checkCache(String inStr, boolean inRomaji) {

        // wartości z podziału -> niezależne od WordSearchState
        if (StartupStaticData.DOUBLE_CHECK_PRELOADED_OBJECTS.containsKey(inStr)) {
            ListObject obj = StartupStaticData.DOUBLE_CHECK_PRELOADED_OBJECTS.get(inStr);
            return obj;
        }

        return null;
    }

    public static ListObject checkCache(WordSearchState inState, boolean inRomaji) {

        // wartości z podziału -> niezależne od WordSearchState
        if (StartupStaticData.DOUBLE_CHECK_PRELOADED_OBJECTS.containsKey(inState.getString())) {
            ListObject obj = StartupStaticData.DOUBLE_CHECK_PRELOADED_OBJECTS.get(inState.getString());
            return obj;
        }

        // WordSearchState ma swój zaimplementowany equals
        if (StartupStaticData.EXTRACT_SAVED_OBJECTS.containsKey(inState)) {
            ListObject obj = StartupStaticData.EXTRACT_SAVED_OBJECTS.get(inState);
            return obj;
        }

        return null;

    }

    public DoubleCheckResult toDoubleCheck(ListObject lObj, int start, int end, int inOriginalStart, int inOriginalEnd, String inClearString) {

        //boolean isPrefix = inOriginalStart == 0;
        //boolean isSuffix = inOriginalEnd == end && inOriginalStart > 0;

        String[] cantBePrefix = new String[]{"へ", "を", "か"};
        //ArrayList<String> cantBePrefixList = new ArrayList<String>(Arrays.asList(cantBePrefix));

        // query -> to co wpisaliśmy, typ zapisu może się różnić od tego z bazy
        boolean stringAllKana = InputUtils.isStringAllKana(lObj.Query.get());
        String clearStringCut = inClearString.substring(start, end);

        if (StartupStaticData.NotToSplitStrings.contains(clearStringCut)) return null;

        // special classes
        if (lObj.IsCommon.get() && !lObj.IsConjugated.get() && (lObj.WordType.get().contains("vs-s") || lObj.WordType.get().contains("vs-i") || lObj.WordType.get().contains("vk")))
            return null;

        for (String s : StartupStaticData.DoubleCheckPreloadedStrings) {

            if (clearStringCut.length() <= s.length()) continue;
            //if (isPrefix && cantBePrefixList.contains(s)) continue;

            /*
            if (StartupStaticData.DOUBLE_CHECK_PRELOADED_OBJECTS.containsKey(s)) {
                if (lObj.getEntSeq() == StartupStaticData.DOUBLE_CHECK_PRELOADED_OBJECTS.get(s).getEntSeq())
                    continue;
            }
            */

            // sama kana -> proste porównanie
            if (stringAllKana) {
                if (clearStringCut.startsWith(s)) {
                    return new DoubleCheckResult(WordPosition.Prefix, s.length(), s.length());
                }
            }
            // kanji -> musimy zobaczyć czy czytania tego słowa nie możemy jakoś podzielić
            else {
                for (int i = 0; i < lObj.getKebListLen(); i++) {
                    if (lObj.getFromKebList(i).length() > 0) {

                        // if (isPrefix && !prefixesList.contains(s)) continue;

                        if (lObj.getFromKebList(i).startsWith(s) &&
                                (!InputUtils.isStringAllKana(s) || InputUtils.isStringAllKana(clearStringCut.substring(s.length(), s.length() + 1)))) {
                            return new DoubleCheckResult(WordPosition.Prefix, s.length(), s.length());
                        }
                    }
                }
            }

            if (clearStringCut.length() > s.length()) {

                if (stringAllKana) {
                    if (clearStringCut.endsWith(s)) {
                        return new DoubleCheckResult(WordPosition.Suffix, clearStringCut.length() - s.length(), s.length());
                    }
                } else {
                    for (int i = 0; i < lObj.getKebListLen(); i++) {
                        if (lObj.getFromKebList(i).length() > 0 && inClearString.contains(lObj.getFromKebList(i))) {

                            if (lObj.getFromKebList(i).endsWith(s)
                                //&& !InputUtils.isStringAllKanji(lObj.getFromKebList(i).substring(lObj.getFromKebList(i).length() - s.length() - 1, lObj.getFromKebList(i).length() - s.length()))
                                    )
                                return new DoubleCheckResult(WordPosition.Suffix, lObj.getFromKebList(i).length() - s.length(), s.length());
                        }
                    }
                }
            }

            // musi się zacząć w (start, end) ale wyjść poza ten przedział
            if (s.length() > 1) {

                String suffix = inClearString.substring(start, Math.min(inClearString.length(), end + s.length() - 1));
                boolean suffixFound = false;

                while (suffix.contains(s)) {
                    suffix = suffix.substring(0, suffix.length() - 1);
                    suffixFound = true;
                }

                if (suffixFound && suffix.length() > end - start - 1) {

                    int cut = suffix.length() - s.length() + 1;
                    if (cut > 0 &&
                            !InputUtils.isStringAllKanji(suffix.substring(suffix.length() - 1, suffix.length()))) {

                        return new DoubleCheckResult(WordPosition.SuffixExcess, cut, s.length());
                    }
                }

            }
        }

        return null;
    }

    private Pair<Integer, Integer> calculatePenalties(WordSearchState state, ListObject newEntry, ArrayList<ListObject> resultProposition1, ArrayList<ListObject> resultProposition2) {

        int penalty1 = 0, penalty2 = 0;

        /*
        if (state.getWordTypeForbidden() != null && !state.getWordTypeForbidden().isEmpty()) {
            if (newEntry.getWordType().startsWith(state.getWordTypeForbidden())) {
                penalty2 += 4;
            }

            if (resultProposition1.get(0).getWordType().startsWith(state.getWordTypeForbidden())) {
                penalty1 += 4;
            }
        }
        */

        if (state.isPreviousVerbThatCantConnect()) {

            if (!newEntry.WordType.get().equals("vs-i") && WordSearchState.isVerb(newEntry.WordType.get())) {
                penalty2 += 3;
            }

            if (!resultProposition1.get(0).WordType.get().equals("vs-i") && WordSearchState.isVerb(resultProposition1.get(0).WordType.get())) {
                penalty1 += 3;
            }
        }

        if (resultProposition1.size() > 1) {
            for (int i = 1; i < resultProposition1.size(); i++) {
                if (WordSearchState.isVerbThatCantConnect(resultProposition1.get(i - 1).WordType.get(), resultProposition1.get(i - 1).GrammarChain.get()) &&
                        WordSearchState.isVerb(resultProposition1.get(i).WordType.get())) {
                    penalty1 += 3;
                }
            }
        }

        if (resultProposition2.size() > 1) {
            for (int i = 1; i < resultProposition2.size(); i++) {
                if (WordSearchState.isVerbThatCantConnect(resultProposition2.get(i - 1).WordType.get(), resultProposition2.get(i - 1).GrammarChain.get()) &&
                        WordSearchState.isVerb(resultProposition2.get(i).WordType.get())) {
                    penalty2 += 3;
                }
            }
        }

        if (state.isPreviousAdjThatCantConnect())

        {
            if (WordSearchState.isAdj(newEntry.WordType.get())) {
                penalty2 += 2;
            }

            if (WordSearchState.isAdj(resultProposition1.get(0).WordType.get())) {
                penalty1 += 2;
            }
        }

        if (state.isPreviousNoun())
        {

            if (state.isPreviousNotSuruVerb()) {
                if (WordSearchState.isSuru(newEntry.WordType.get())) {
                    penalty2 += 2;
                }

                if (WordSearchState.isSuru(resultProposition1.get(0).WordType.get())) {
                    penalty1 += 2;
                }
            }

            if (WordSearchState.isNoun(newEntry.WordType.get()) || WordSearchState.isAdj(newEntry.WordType.get())) {
                penalty2 += 2;
            }

            if (WordSearchState.isNoun(resultProposition1.get(0).WordType.get()) || WordSearchState.isAdj(resultProposition1.get(0).WordType.get())) {
                penalty1 += 2;
            }
        }
        /*
        else

        {
            if (resultProposition1.size() > 1) {
                for (int i = 1; i < resultProposition1.size(); i++) {
                    if (WordSearchState.isNoun(resultProposition1.get(i - 1).getWordType()) &&
                            WordSearchState.isNoun(resultProposition1.get(i).getWordType()) || WordSearchState.isAdj(resultProposition1.get(i).getWordType())) {
                        penalty1 += 3;
                    }
                }
            }

            if (resultProposition2.size() > 1) {
                for (int i = 1; i < resultProposition2.size(); i++) {
                    if (WordSearchState.isNoun(resultProposition2.get(i - 1).getWordType()) &&
                            WordSearchState.isNoun(resultProposition2.get(i).getWordType()) || WordSearchState.isAdj(resultProposition2.get(i).getWordType())) {
                        penalty2 += 3;
                    }
                }
            }
        }
        */

        if (state.isPreviousAdv())
        {

            if (WordSearchState.isAdv(newEntry.WordType.get())) {
                penalty2 += 2;
            }

            if (WordSearchState.isAdv(resultProposition1.get(0).WordType.get())) {
                penalty1 += 2;
            }
        } else
        {
            if (resultProposition1.size() > 1) {
                for (int i = 1; i < resultProposition1.size(); i++) {
                    if (WordSearchState.isAdv(resultProposition1.get(i - 1).WordType.get()) &&
                            WordSearchState.isAdv(resultProposition1.get(i).WordType.get())) {
                        penalty1 += 2;
                    }
                }
            }

            if (resultProposition2.size() > 1) {
                for (int i = 1; i < resultProposition2.size(); i++) {
                    if (WordSearchState.isAdv(resultProposition2.get(i - 1).WordType.get()) &&
                            WordSearchState.isAdv(resultProposition2.get(i).WordType.get())) {
                        penalty2 += 2;
                    }
                }
            }
        }

        for (int i = 0; i < resultProposition1.size(); i++)
        {
            if (WordSearchState.isUsuallyWrittenWithKanaAlone(resultProposition1.get(i).Misc.get())) {

                if (!InputUtils.isStringAllKana(resultProposition1.get(i).Query.get())) {
                    penalty1 += 1;
                }
            }
            /*
            else {
                if (InputUtils.isStringAllKana(resultProposition1.get(i).getQuery())) {
                    penalty1 += 0.5;
                }
            } */
        }

        for (int i = 0; i < resultProposition2.size(); i++)
        {
            if (WordSearchState.isUsuallyWrittenWithKanaAlone(resultProposition2.get(i).Misc.get())) {

                if (!InputUtils.isStringAllKana(resultProposition2.get(i).Query.get())) {
                    penalty2 += 1;
                }

            }
            /*
            else {
                if (InputUtils.isStringAllKana(resultProposition2.get(i).getQuery())) {
                    penalty2 += 0.5;
                }
            } */
        }

        /*
        if (penalty1 == penalty2 && resultProposition1.get(0).

                getEntSeq()

                == resultProposition2.get(0).

                getEntSeq()

                )

        {
            return new Pair<Integer, Integer>(2, 0);
        }
        */

        return new Pair<Integer, Integer>(penalty1, penalty2);
    }

    public ArrayList<ListObject> findBetterMatch(Boolean inRomaji, String cleanString, HashMap<Character, InputUtils.CharType> inTypeMap, int start, int end, int inOriginalStart, int inOriginalEnd, ListObject newEntry, ExtractHelper.DoubleCheckResult doubleCheckLength, JMDictHelper dbHelper, String inPreviousWordType, String inPreviousConjugation, String inWordTypePreference, String inWordTypeForbidden, Boolean inIsPreviousDigit) {

        ArrayList<ListObject> resultProposition1 = new ArrayList<ListObject>();
        ArrayList<ListObject> resultProposition2 = new ArrayList<ListObject>();

        WordSearchState state = new WordSearchState(start, end, inOriginalStart, inOriginalEnd, cleanString, inPreviousWordType, inPreviousConjugation, inWordTypePreference, inWordTypeForbidden, inIsPreviousDigit);

        // prop 1

        // ucięty fragment [...] + reszta [......] (rekurencyjnie)
        if (doubleCheckLength.getWordPosition() == WordPosition.Prefix) {

            String str = cleanString.substring(start, start + doubleCheckLength.getCut());
            ListObject cacheObj;

            if ((cacheObj = ExtractHelper.checkCache(str, inRomaji)) != null) {
                resultProposition1.add(cacheObj);
            } else {

                Cursor c = dbHelper.getExtract().getEntryForExtract(str, state);

                dbHelper.getWord().getDataFromCursor(resultProposition1, c, inRomaji, false, false, false);

                if (resultProposition1.get(0).Length.get() == str.length()) {
                    resultProposition1.get(0).IsPreloaded.set(true);
                    //StartupStaticData.DOUBLE_CHECK_PRELOADED_OBJECTS.put(str, resultProposition1.get(0));
                }
            }

            for (ListObject obj : resultProposition1) {
                obj.Query.set(cleanString.substring(start, start + doubleCheckLength.getCut()));
            }

            if (start + doubleCheckLength.getCut() <= cleanString.length()) {
                dbHelper.getExtract().getWordsBySearchStringAux(inRomaji, resultProposition1, cleanString.substring(start + doubleCheckLength.getCut(), cleanString.length()), inTypeMap, inOriginalStart, inOriginalEnd, false);
            }

        } else {
            dbHelper.getExtract().getWordsBySearchStringAux(inRomaji, resultProposition1, cleanString.substring(start, start + doubleCheckLength.getCut()), inTypeMap, inOriginalStart, inOriginalEnd, false);
            if (start + doubleCheckLength.getCut() <= cleanString.length()) {

                dbHelper.getExtract().getWordsBySearchStringAux(inRomaji, resultProposition1, cleanString.substring(start + doubleCheckLength.getCut(), cleanString.length()), inTypeMap, inOriginalStart, inOriginalEnd, false);
            }
        }

        // prop 2
        resultProposition2.add(newEntry);
        dbHelper.getExtract().getWordsBySearchStringAux(inRomaji, resultProposition2, cleanString.substring(end, cleanString.length()), inTypeMap, inOriginalStart, inOriginalEnd, false);

        // penalties
        int penalty1 = 0, penalty2 = 0;
        Pair<Integer, Integer> penalties = calculatePenalties(state, newEntry, resultProposition1, resultProposition2);

        penalty1 = penalties.first;
        penalty2 = penalties.second;

        if (resultProposition2.size() < resultProposition1.size() && resultProposition1.size() + penalty1 > resultProposition2.size() + penalty2 + 1) {
            return resultProposition2;
        } else if ((resultProposition1.size() == resultProposition2.size()) && resultProposition1.size() + penalty1 > resultProposition2.size() + penalty2) {
            return resultProposition2;
        } else if (resultProposition2.size() + penalty2 > resultProposition1.size() + penalty1) {
            return resultProposition1;
        } else {

            // pierwszy od doubleCheck
            double prop1Points = getPropPoints(resultProposition1);
            double prop2Points = getPropPoints(resultProposition2);
            double res1 = prop1Points - ((double) penalty1 / resultProposition1.size());
            double res2 = prop2Points - ((double) penalty2 / resultProposition2.size());

            if (res1 >= res2) {
                if (res1 == res2 && resultProposition1.size() > resultProposition2.size() && resultProposition1.size() > 2) {
                    return resultProposition2;
                } else return resultProposition1;
            } else {
                return resultProposition2;
            }
        }
    }

    private double getPropPoints(ArrayList<ListObject> resultProposition) {
        double propPoints = 0.0;
        boolean pointsForNotSplitGiven = false;
        String prevWordType = "";
        Set<Integer> setToFindDuplicates = new HashSet();

        for (int i = 0; i < resultProposition.size(); i++) {
            ListObject lObj = resultProposition.get(i);

            if (!setToFindDuplicates.add(lObj.ID.get())) {
                if (lObj.WordType.get().startsWith("prt")) {
                    propPoints--;
                }
            }

            if (lObj.IsPreloaded.get() && lObj.Query.get().length() > 1) propPoints += 1;

            boolean isCommonWord = false;

            if (isCommon(lObj)) {
                propPoints++;
                isCommonWord = true;
            } else if (lObj.JLPTLevel.get() > 0) {
                propPoints += 0.5;
                isCommonWord = true;
            }

            if (!pointsForNotSplitGiven && lObj.Query.get() != null) {

                for (String notToSplit : StartupStaticData.NotToSplitStrings) {
                    if (lObj.Query.get().contains(notToSplit)) {
                        propPoints += 2;
                        pointsForNotSplitGiven = true;
                        break;
                    }
                }
            }

            if (lObj.IsCommon.get() && lObj.GrammarChain.get().contains("progressive")) {
                propPoints += 1;
            }

            boolean iFormFound = false;

            if (lObj.GrammarChain.get().contains("-i form")) {
                iFormFound = true;
            }

            // iru/oru i-form
            if ((lObj.ID.get() == 1577980 || lObj.ID.get() == 1577985) && iFormFound) {
                propPoints -= 3;
            }

            if (iFormFound) {
                propPoints -= 1;
            }

            if (WordSearchState.isNoun(lObj.WordType.get()) && !lObj.IsCommon.get()) {
                propPoints -= 0.5;
            }

            if (lObj.ID.get() == 1628500) propPoints += 1; // desu
            if (lObj.ID.get() == 1340450) propPoints += 2; //dekiru > kuru
            //if (lObj.getEntSeq() == 1577980) propPoints += 1; // iru
            if (lObj.ID.get() == 1157170) {

                if (lObj.GrammarChain.get().startsWith("potential")) {
                    propPoints += 2; // dekiru z suru
                } else propPoints += 1;
            }

            if (lObj.ID.get() == 2028920) propPoints += 1; // ha particle

            if ((lObj.WordType.get().startsWith("prt")) ||
                    (//isCommon(lObj) &&
                            lObj.WordType.get().startsWith("exp"))
                    //|| lObj.getWordType().startsWith("vs-i")
                    || (isCommonWord && lObj.WordType.get().startsWith("conj"))
                    || (isCommonWord && lObj.WordType.get().startsWith("aux"))) {

                if ((!prevWordType.isEmpty() && lObj.WordType.get().contains(prevWordType)) || (!lObj.WordType.get().isEmpty() && prevWordType.contains(lObj.WordType.get()))) {
                    propPoints--;
                } else
                //if (lObj.getIsCommon())
                {
                    if (lObj.Query.get().length() > 1) {
                        propPoints++;
                    } else {
                        propPoints += 0.5;
                    }
                }
            }

            if (lObj.WordType.get().startsWith("n") && lObj.Query.get().length() == 1) {
                propPoints -= 1;
            }

            prevWordType = lObj.WordType.get();
        }

        propPoints = propPoints / (double) resultProposition.size();
        return propPoints;
    }

    // coś co powinno być common, a nie jest w bazie
    private static boolean isCommon(ListObject lObj) {
        return lObj.IsCommon.get() || (lObj.Query.get() != null && lObj.Query.get().equals("なの"));
    }


}
