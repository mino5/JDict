package utils.grammar;

import android.content.Context;
import android.util.Pair;

import com.mino.jdict.JDictApplication;
import com.mino.jdict.interfaces.IRebKebObject;
import com.mino.jdict.objects.basic.FuriganaObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import utils.database.JMDictHelper;

import static utils.grammar.InputUtils.isKana;

/**
 * Created by Mino on 2014-10-05.
 */
public class FuriganaHelper {

    private Queue<FuriganaObject> mQueue;
    private Map<String, List<Pair<String, String>>> mCharacterReadingsMap;
    private FuriganaObject mLongestMatchObject;
    private String lastPartKanjiChecked = "";

    public FuriganaHelper() {
    }

    public static Map<String, Map<String, IRebKebObject>> furiganaCacheMap = new HashMap<String, Map<String, IRebKebObject>>();

    public boolean matchFurigana(IRebKebObject MainObject, String keb, String currentRebForKeb) {

        if (keb.length() > currentRebForKeb.length() && !InputUtils.isStringAllKana(keb)) {
            MainObject.addRebKeb(currentRebForKeb, keb);
            return true;
        }

        // cache
        if (furiganaCacheMap.containsKey(keb)) {

            Map<String, IRebKebObject> currentMap = furiganaCacheMap.get(keb);

            if (currentMap.containsKey(currentRebForKeb)) {
                for (Pair<String, String> rebKeb : currentMap.get(currentRebForKeb).getRebKebList()) {
                    MainObject.addRebKeb(rebKeb.first, rebKeb.second);
                }

                return true;
            }
        }

        boolean isAllKana = InputUtils.isStringAllKana(keb);

        mQueue = new LinkedBlockingQueue<FuriganaObject>();
        mQueue.add(mLongestMatchObject = new FuriganaObject("", "", "", "", null, 0, false, 0));

        if (!isAllKana) {
            mCharacterReadingsMap = getStringPriorityQueueMap(keb);
        }

        boolean res =  matchFuriganaAux(MainObject, keb, currentRebForKeb, isAllKana);
        if (res) {

            if (furiganaCacheMap.size() > 10000) return res;

            if (furiganaCacheMap.containsKey(keb)) {
                Map<String, IRebKebObject> currentMap = furiganaCacheMap.get(keb);
                currentMap.put(currentRebForKeb, MainObject);
            } else {
                Map<String, IRebKebObject> currentMap = new HashMap<String, IRebKebObject>();
                currentMap.put(currentRebForKeb, MainObject);
                furiganaCacheMap.put(keb, currentMap);
            }
        }

        return res;
    }

    public boolean matchFuriganaAux(IRebKebObject MainObject, String keb, String currentRebForKeb, boolean isAllKana) {

        boolean isReverse = false;

        while (!mQueue.isEmpty()) {

            FuriganaObject mNewObject = null;

            FuriganaObject currentObject = mQueue.poll();
            if (mLongestMatchObject.WordPart.get().length() < currentObject.WordPart.get().length()
                    || (mLongestMatchObject.WordPart.get().length() > 0
                    && mLongestMatchObject.WordPart.get().length() == currentObject.WordPart.get().length()
                    && mLongestMatchObject.getCharacterReadingCut() > currentObject.getCharacterReadingCut())
                    ) {
                mLongestMatchObject = currentObject;
            }

            String character = currentObject.getNextCharacter(keb, isReverse);
            int nextCharPos = isReverse ? currentObject.NextCharacterPos.get() - 1 : currentObject.NextCharacterPos.get() + 1;

            if (character != null) {
                if (isKana(character)) {
                    if (!isReverse) {
                        mNewObject = new FuriganaObject(character, character, currentObject.WordPart.get() + character, currentObject.getWordPartReading() + character, currentObject, nextCharPos, false, 0);

                    } else {
                        mNewObject = new FuriganaObject(character, character, character + currentObject.WordPart.get(), character + currentObject.getWordPartReading(), currentObject, nextCharPos, true, 0);
                    }

                    if (isAllKana || mNewObject.isValid(keb, currentRebForKeb, isReverse)) {

                        if (!isReverse) {

                            // sha, nya itp -> wtedy łączymy obiekty
                            if (isKana(currentObject.Character.get()) &&
                                    !InputUtils.getRomaji(currentObject.Character.get() + character, false).contentEquals(InputUtils.getRomaji(currentObject.Character.get(), false) + InputUtils.getRomaji(character, false))
                                    && !currentObject.Character.get().contentEquals("っ") && !currentObject.Character.get().contentEquals("ッ")
                                    && !character.contentEquals("っ") && !character.contentEquals("ッ")) {
                                currentObject.Character.set(currentObject.Character.get() + character);
                                currentObject.CharacterReading.set(currentObject.CharacterReading.get() + character);
                                currentObject.WordPart.set(currentObject.WordPart.get() + character);
                                currentObject.setWordPartReading(currentObject.getWordPartReading() + character);
                                currentObject.NextCharacterPos.set(nextCharPos);

                                mQueue.add(currentObject);

                            } else {
                                mQueue.add(mNewObject);
                            }
                        } else {
                            if (isKana(currentObject.Character.get()) &&
                                    !InputUtils.getRomaji(character + currentObject.Character.get(), false).contentEquals(InputUtils.getRomaji(character, false) + InputUtils.getRomaji(currentObject.Character.get(), false))
                                    && !currentObject.Character.get().contentEquals("っ") && !currentObject.Character.get().contentEquals("ッ")
                                    && !character.contentEquals("っ") && !character.contentEquals("ッ")) {

                                currentObject.Character.set(character + currentObject.Character.get());
                                currentObject.CharacterReading.set(character + currentObject.CharacterReading.get());
                                currentObject.WordPart.set(character + currentObject.WordPart.get());
                                currentObject.setWordPartReading(character + currentObject.getWordPartReading());
                                currentObject.NextCharacterPos.set(nextCharPos);

                                mQueue.add(currentObject);

                            } else {
                                mQueue.add(mNewObject);
                            }
                        }

                    } else

                        // first kana in word - ケ月 kagetsu problem
                        if (keb.startsWith("ケ") && currentRebForKeb.startsWith("か")) {

                            if (!isReverse) {
                                mNewObject = new FuriganaObject(character, currentRebForKeb.substring(currentRebForKeb.length() - 1, currentRebForKeb.length()), currentObject.WordPart.get() + character, currentObject.getWordPartReading() + currentRebForKeb.substring(currentRebForKeb.length() - 1, currentRebForKeb.length()), currentObject, nextCharPos, false, 0);
                            } else {
                                mNewObject = new FuriganaObject(character, currentRebForKeb.substring(0, 1), character + currentObject.WordPart.get(), currentRebForKeb.substring(0, 1) + currentObject.getWordPartReading(), currentObject, nextCharPos, true, 0);
                            }

                            mQueue.add(mNewObject);
                        }

                } else {

                    // pobieramy wszystkie czytania znaku
                    List<Pair<String, String>> characterReadings = mCharacterReadingsMap.get(character);

                    String wordPart = currentObject.WordPart.get();
                    wordPart = isReverse ? character + wordPart : wordPart + character;
                    String wordPartReading = currentObject.getWordPartReading();
                    String newWordPartReading = wordPartReading;

                    if (character.contentEquals("々") && !isReverse) {

                        newWordPartReading = wordPartReading + currentObject.CharacterReading.get();

                        if (FuriganaObject.isValid(keb, currentRebForKeb, wordPart, newWordPartReading, isReverse)) {
                            mNewObject = new FuriganaObject(character, currentObject.CharacterReading.get(), wordPart, newWordPartReading, currentObject, nextCharPos, isReverse, 0);
                            mQueue.add(checkIfKanaNeedsToBeAddedToPartialAssignment(mNewObject, keb, currentRebForKeb, isReverse));
                        } else if (wordPartReading.length() > 0) {

                            String part = wordPartReading.substring(0, 1);
                            if (GrammarDictionaries.NIG_KANA.containsKey(part)) {

                                for (String nig : GrammarDictionaries.NIG_KANA.get(part)) {

                                    String newCharReading = nig + wordPartReading.substring(1, wordPartReading.length());
                                    newWordPartReading = currentObject.CharacterReading.get() + newCharReading;

                                    if (FuriganaObject.isValid(keb, currentRebForKeb, wordPart, newWordPartReading, isReverse)) {
                                        mNewObject = new FuriganaObject(character, newCharReading, wordPart, newWordPartReading, currentObject, nextCharPos, isReverse, 0);
                                        mQueue.add(checkIfKanaNeedsToBeAddedToPartialAssignment(mNewObject, keb, currentRebForKeb, isReverse));

                                        break;
                                    }
                                }
                            }
                        }

                    } else if (!characterReadings.isEmpty()) {

                        int longestReading = characterReadings.get(0).first.length();
                        int cut = 0;

                        while (cut < longestReading) {

                            HashSet<String> cuts = new HashSet<String>();

                            for (Pair<String, String> readType : characterReadings) {

                                String read = readType.first;
                                String type = readType.second;

                                final int len = read.length();

                                if (cut > 0 && !type.equals("kun")) continue;

                                if (cut > 0 && len - cut > 0) {
                                    read = read.substring(0, len - cut);

                                    if (cuts.contains(read)) continue;
                                }

                                if (cut > 0 && len - cut <= 0) {
                                    cut++;
                                    break;
                                }

                                newWordPartReading = isReverse ? read + wordPartReading : wordPartReading + read;

                                if (FuriganaObject.isValid(keb, currentRebForKeb, wordPart, newWordPartReading, isReverse)) {

                                    cuts.add(read);
                                    mNewObject = new FuriganaObject(character, read, wordPart, newWordPartReading, currentObject, nextCharPos, isReverse, cut);
                                    mQueue.add(checkIfKanaNeedsToBeAddedToPartialAssignment(mNewObject, keb, currentRebForKeb, isReverse));
                                    //break; // znalezlismy dopasowanie - wiecej nie szukamy. niekoniecznie dobrze.
                                }
                            }

                            cut++;
                            //if (mNewObject != null) break; // jak znalezlismy dopasowanie bez ucinania to juz nie ucinamy. do sprawdzenia.
                        }
                    }
                }

                // ateji -> znajdujemy najdłuższe dopasowanie. Z przodu już mamy to pora teraz wyszukać dopasowania od tyłu
                if (mQueue.isEmpty() && !isReverse) {
                    isReverse = true;
                    mQueue.add(new FuriganaObject("", "", "", "", null, keb.length(), true, 0));

                } else if (mQueue.isEmpty()) // ateji
                {
                    addNotFoundMatch(MainObject, keb, currentRebForKeb, mLongestMatchObject);
                }
            } else { // udało się znaleźć dopasowanie

                addFoundMatch(MainObject, currentRebForKeb, currentObject);

                // could assign all string
                return true;
            }

        }

        return false; // couldn't assign anything
    }

    // 有難う / ありがとう -> mamy czytanie drugiego znaku GA ale nie mamy GATO. Z kontekstu jednak wynika jednoznacznie, że musimy temu znakowi przypisać GATO
    // w przypadku arigatou biore pierwsze kolejne 'u' z reb co niekoniecznie zawsze może być poprawne - wystarczy żeby zamiast arigatou było arigatouu.
    private FuriganaObject checkIfKanaNeedsToBeAddedToPartialAssignment(FuriganaObject inObject, String inKeb, String inReb, boolean inReverse) {

        String nextCharacter = inObject.getNextCharacter(inKeb, inReverse);
        boolean worked = false;

        if (nextCharacter != null && !isKana(inObject.Character.get()) && isKana(nextCharacter)) {
            if (inReverse) { // todo?

            } else {

                String partAfterAssignment = inReb.substring(inObject.getWordPartReading().length(), inReb.length());
                String next = inObject.getWordPartReading() + nextCharacter;

                if (!inReb.startsWith(next)) {

                    String toAdd = "";

                    final int len = partAfterAssignment.length();
                    for (int i = 0; i < len; i++) {

                        char c = partAfterAssignment.charAt(i);
                        if (c == nextCharacter.charAt(0)) {
                            worked = true;
                            break;
                        } else {
                            toAdd += c;
                        }
                    }

                    if (!worked) return inObject;
                    String newAssignment = inObject.getWordPartReading() + toAdd;

                    if (inReb.startsWith(newAssignment)) {
                        inObject.setWordPartReading(newAssignment);
                        inObject.CharacterReading.set(inObject.CharacterReading.get() + toAdd);
                    }

                }
            }
        }

        return inObject;
    }

    private Map<String, List<Pair<String, String>>> getStringPriorityQueueMap(String keb) {
        Map<String, List<Pair<String, String>>> characterReadingsMap = new HashMap<String, List<Pair<String, String>>>();

        for (int i = 0; i < keb.length(); i++) {

            String character = keb.substring(i, i + 1);
            if (!InputUtils.isKana(character) && !characterReadingsMap.containsKey(character)) {
                characterReadingsMap.put(character, JDictApplication.getDatabaseHelper().getKanji().getKanjiReadings(character, i == 0));
            }
        }
        return characterReadingsMap;
    }

    private void addNotFoundMatch(IRebKebObject mainObject, String keb, String currentRebForKeb, FuriganaObject longestMatchObject) {
        // oddzielamy kanę

        boolean previousKana = false;
        boolean isReverse = longestMatchObject.getIsReversed();

        String partKanji = "";
        String partReb = "";
        String wholeReb = "";


        if (!isReverse) {
            addFoundMatch(mainObject, longestMatchObject.getWordPartReading(), longestMatchObject);
            keb = keb.substring(longestMatchObject.WordPart.get().length(), keb.length());
            currentRebForKeb = currentRebForKeb.substring(longestMatchObject.getWordPartReading().length(), currentRebForKeb.length());

        } else {

            // we take what's left to assign from the left
            keb = keb.substring(0, keb.length() - longestMatchObject.WordPart.get().length());
            currentRebForKeb = currentRebForKeb.substring(0, currentRebForKeb.length() - longestMatchObject.getWordPartReading().length());
        }

        int startPos = 0;
        int endPos = keb.length();

        // trying to assign from start
        for (int i = startPos; i < endPos; i++) {
            String currentCharacter = keb.substring(i, i + 1);

            if (isKana(currentCharacter)) {
                if (previousKana) {

                    partReb += currentCharacter;

                    if (i == endPos - 1) {
                        mainObject.addRebKeb("", partReb);
                        partReb = "";
                    }
                } else {

                    if (!partKanji.isEmpty()) {
                        String partString = currentRebForKeb.substring(wholeReb.length(), currentRebForKeb.length());

                        int index = partString.indexOf(currentCharacter);
                        if (index >= 0) {
                            partString = partString.substring(0, index);
                            wholeReb += partString;

                            addKanjiReadingPart(mainObject, partKanji, partString);
                        }
                    }

                    partReb = currentCharacter;
                    partKanji = "";

                    previousKana = true;
                }

                wholeReb += currentCharacter;
            } else { // Kanji

                if (previousKana) {

                    mainObject.addRebKeb("", partReb);
                    partReb = "";

                    partKanji = currentCharacter;

                    if (i == endPos - 1) {

                        addKanjiReadingPart(mainObject, partKanji, currentRebForKeb.substring(wholeReb.length(), currentRebForKeb.length()));
                    }

                } else {

                    // 躊躇う -> problem gdy drugi znak w ogóle nie ma zadnego czytania powiazanego z czytaniem słowa, ale pierwszy znak potrafi prawie cale czytanie wziąść na siebie.
                    // wtedy scalamy oba znaki i dajemy im wspólne czytanie
                    if (i + 1 == endPos && currentRebForKeb.contentEquals("ためら") && keb.contentEquals("躊躇")) {

                        mainObject.addRebKeb("ためら", "躊躇");
                        continue;
                    }

                    partKanji += currentCharacter;

                    if (i == endPos - 1) {

                        addKanjiReadingPart(mainObject, partKanji, currentRebForKeb.substring(wholeReb.length(), currentRebForKeb.length()));
                    }
                }

                previousKana = false;
            }
        }

        if (isReverse) {

            addFoundMatch(mainObject, longestMatchObject.getWordPartReading(), longestMatchObject);
        } else if (partReb.length() > 0) {
            mainObject.addRebKeb("", partReb);
        }
    }

    private void addFoundMatch(IRebKebObject mainObject, String currentRebForKeb, FuriganaObject currentObject) {
        String characterReading = currentObject.CharacterReading.get();

        // irregular ending
        for (int i = currentObject.getWordPartReading().length(); i < currentRebForKeb.length(); i++) {

            characterReading += currentRebForKeb.substring(i, i + 1);
        }

        currentObject.CharacterReading.set(characterReading);

        LinkedList<Pair<String, String>> rebKebPairList = new LinkedList<Pair<String, String>>();

        while (currentObject != null) {

            if (currentObject.getIsReversed()) {
                rebKebPairList.addLast(new Pair<String, String>(currentObject.CharacterReading.get(), currentObject.Character.get()));
            } else {
                rebKebPairList.addFirst(new Pair<String, String>(currentObject.CharacterReading.get(), currentObject.Character.get()));
            }

            currentObject = currentObject.getParent();
        }

        while (!rebKebPairList.isEmpty()) {

            Pair<String, String> currentPair = rebKebPairList.pop();

            if (InputUtils.isStringAllKana(currentPair.second) && currentPair.second.equals(currentPair.first)) {
                if (!currentPair.second.contentEquals("")) {
                    mainObject.addRebKeb("", currentPair.second);
                }
            } else {
                if (!currentPair.first.contentEquals("") || !currentPair.second.contentEquals("")) {
                    mainObject.addRebKeb(currentPair.first, currentPair.second);
                }
            }
        }
    }

    private void addKanjiReadingPart(IRebKebObject mainObject, String partKanji, String partString) {

        // czytanie jest oczywiste bo jest tyle hiragany ile kanji
        if (partString.length() == partKanji.length()) {

            for (int j = 0; j < partString.length(); j++) {
                mainObject.addRebKeb(partString.substring(j, j + 1), partKanji.substring(j, j + 1));
            }
        } else { // szukamy rekurencyjnie dopasowań z lewa i z prawa do podciągu złożonego z kanji

            if (!lastPartKanjiChecked.contentEquals(partKanji)) {
                lastPartKanjiChecked = partKanji;

                mQueue.add(mLongestMatchObject = new FuriganaObject("", "", "", "", null, 0, false, 0));
                //boolean found =

                boolean isAllKana = InputUtils.isStringAllKana(partKanji);

                matchFuriganaAux(mainObject, partKanji, partString, isAllKana);

//                if (!found && !lastPartKanjiChecked.equals("")) {
                //                  mainObject.addRebKeb(partString, partKanji);

                if (!lastPartKanjiChecked.equals(""))
                    lastPartKanjiChecked = "";
                //            }

            } else if (!lastPartKanjiChecked.equals("")) {
                mainObject.addRebKeb(partString, partKanji);
                lastPartKanjiChecked = "";
            }
        }

    }
}
