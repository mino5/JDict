package com.mino.jdict.objects.basic;

import utils.grammar.InputUtils;
import utils.other.properties.IntProperty;
import utils.other.properties.StringProperty;

/**
 * Created by Mino on 2014-09-23.
 */
public class FuriganaObject {

    public final StringProperty Character = new StringProperty();
    public final StringProperty CharacterReading = new StringProperty();
    public final StringProperty WordPart = new StringProperty();
    public final IntProperty NextCharacterPos = new IntProperty();

    private String mWordPartReading;
    private String mWordPartReadingRomaji;
    private int mCharacterReadingCut;
    private boolean mIsReversed;
    private FuriganaObject mParent;


    public FuriganaObject(String inCharacter, String inCharacterReading, String inWordPart, String inWordPartReading, FuriganaObject inParent, int inNextCharacterPos, boolean isReversed, int inCharacterReadingCut) {

        Character.set(inCharacter);
        CharacterReading.set(inCharacterReading);
        NextCharacterPos.set(inNextCharacterPos);
        WordPart.set(inWordPart);

        this.mCharacterReadingCut = inCharacterReadingCut;
        this.mWordPartReading = inWordPartReading;
        this.mParent = inParent;
        this.mIsReversed = isReversed;
        this.mWordPartReadingRomaji = InputUtils.getRomaji(inWordPartReading, false);
    }

    public static boolean isValid(String fullWord, String fullWordReading, String wordPart, String wordPartReading, boolean isReverse) {
        if (!isReverse) {
            return fullWord.startsWith(wordPart) && fullWordReading.startsWith(wordPartReading) &&
                    (!fullWord.contentEquals(wordPart) || fullWordReading.contentEquals(wordPartReading)) &&
                    (fullWord.contentEquals(wordPart) || !fullWordReading.contentEquals(wordPartReading)) &&
                    (!wordPartReading.endsWith("っ") || fullWord.length() == wordPart.length() || !fullWord.substring(wordPart.length(), wordPart.length() + 1).endsWith("っ"));
        } else {
            return fullWord.endsWith(wordPart) && fullWordReading.endsWith(wordPartReading) &&
                    (!fullWord.contentEquals(wordPart) || fullWordReading.contentEquals(wordPartReading)) &&
                    (fullWord.contentEquals(wordPart) || !fullWordReading.contentEquals(wordPartReading)) &&
                    (!wordPartReading.startsWith("っ") || fullWord.length() == wordPart.length() || !fullWord.substring(fullWord.length() - wordPart.length(), fullWord.length() - wordPart.length() - 1).startsWith("っ"));
        }
    }

    public boolean isValid(String fullWord, String fullWordReading, boolean isReverse) {

        String fullWordReadingRomaji = InputUtils.getRomaji(fullWordReading, false);

        if (!isReverse) {
            return fullWord.startsWith(WordPart.get()) && fullWordReadingRomaji.startsWith(mWordPartReadingRomaji) &&
                    (!fullWord.contentEquals(WordPart.get()) || fullWordReadingRomaji.contentEquals(mWordPartReadingRomaji)) &&
                    (fullWord.contentEquals(WordPart.get()) || !fullWordReadingRomaji.contentEquals(mWordPartReadingRomaji));
        } else {
            return fullWord.endsWith(WordPart.get()) && fullWordReadingRomaji.endsWith(mWordPartReadingRomaji) &&
                    (!fullWord.contentEquals(WordPart.get()) || fullWordReadingRomaji.contentEquals(mWordPartReadingRomaji)) &&
                    (fullWord.contentEquals(WordPart.get()) || !fullWordReadingRomaji.contentEquals(mWordPartReadingRomaji));
        }
    }

    public String getNextCharacter(String keb, boolean isReverse) {

        if (!isReverse && NextCharacterPos.get() < keb.length()) {
            return keb.substring(NextCharacterPos.get(), NextCharacterPos.get() + 1);
        } else if (isReverse && NextCharacterPos.get() > 0) {
            return keb.substring(NextCharacterPos.get() - 1, NextCharacterPos.get());
        } else return null;
    }


    public int getCharacterReadingCut() {
        return mCharacterReadingCut;
    }

    public Boolean getIsReversed() {
        return mIsReversed;
    }

    public String getWordPartReading() {
        return mWordPartReading;
    }

    public void setWordPartReading(String wordPartReading) {
        this.mWordPartReading = wordPartReading;
        this.mWordPartReadingRomaji = InputUtils.getRomaji(mWordPartReading, false);
    }

    public FuriganaObject getParent() {
        return mParent;
    }

}
