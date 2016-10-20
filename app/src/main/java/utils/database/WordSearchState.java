package utils.database;

/**
 * Created by Dominik on 10/28/2015.
 */
public class WordSearchState {

    // obcięty być może z wywołania rekurencyjnego
    private int mStart;
    private int mEnd;

    // przed wywołaniem rekurencyjnym
    private int mOriginalStart;
    private int mOriginalEnd;

    private String mCleanString;
    private String mPreviousWordType;
    private String mPreviousConjugation;
    private String mWordTypePreference;
    private String mWordTypeForbidden;
    private boolean mIsPreviousDigit;

    public WordSearchState(int inStart, int inEnd, int inOriginalStart, int inOriginalEnd, String inCleanString, String inPreviousWordType, String inPreviousConjugation, String inWordTypePreference, String inWordTypeForbidden, Boolean inIsPreviousDigit) {
        mStart = inStart;
        mEnd = inEnd;
        mOriginalStart = inOriginalStart;
        mOriginalEnd = inOriginalEnd;
        mCleanString = inCleanString;
        mPreviousWordType = inPreviousWordType;
        mPreviousConjugation = inPreviousConjugation;
        mWordTypePreference = inWordTypePreference;
        mWordTypeForbidden = inWordTypeForbidden;
        mIsPreviousDigit = inIsPreviousDigit;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (mCleanString == null ? 0 : getString().hashCode());

        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof WordSearchState)) return false;
        WordSearchState otherWordSearchState = (WordSearchState) other;

        if (otherWordSearchState.isPrefix() != isPrefix()) return false;
        if (otherWordSearchState.isSuffix() != isSuffix()) return false;
        if (!mCleanString.substring(mStart, mEnd).equals(otherWordSearchState.mCleanString.substring(otherWordSearchState.mStart, otherWordSearchState.mEnd)))
            return false;

        if (isPreviousNoun() != otherWordSearchState.isPreviousNoun()) return false;
        if (isPreviousVerbThatCantConnect() != otherWordSearchState.isPreviousVerbThatCantConnect())
            return false;
        if (isPreviousAdjThatCantConnect() != otherWordSearchState.isPreviousAdjThatCantConnect())
            return false;
        if (isPreviousAdv() != otherWordSearchState.isPreviousAdv()) return false;
        if (isPreviousParticle() != otherWordSearchState.isPreviousParticle()) return false;
        if (isPreviousCounter() != otherWordSearchState.isPreviousCounter()) return false;
        if (isPreviousDigit() != otherWordSearchState.isPreviousDigit()) return false;

        if ((mWordTypePreference == null && otherWordSearchState.mWordTypePreference != null)
                || (mWordTypePreference != null && otherWordSearchState.mWordTypePreference == null)
                || (mWordTypePreference != null && otherWordSearchState.mWordTypePreference != null && !mWordTypePreference.contentEquals(otherWordSearchState.mWordTypePreference))) {

            return false;
        }

        if ((mWordTypeForbidden == null && otherWordSearchState.mWordTypeForbidden != null)
                || (mWordTypeForbidden != null && otherWordSearchState.mWordTypeForbidden == null)
                || (mWordTypeForbidden != null && otherWordSearchState.mWordTypeForbidden != null && !mWordTypeForbidden.contentEquals(otherWordSearchState.mWordTypeForbidden))) {

            return false;
        }

        return true;
    }

    public String getString() {
        return mCleanString.substring(mStart, mEnd);
    }

    public boolean isPrefix() {
        return mStart == 0;
    }

    public boolean isSuffix() {
        return (mStart > 0 && mOriginalEnd == mCleanString.length() && mEnd == mOriginalEnd && mOriginalEnd > 1) || isPreviousDigit();
    }


    public boolean isPreviousNoun() {
        return isNoun(mPreviousWordType);
    }

    public boolean isPreviousNotSuruVerb() {
        return isNotSuruVerb(mPreviousWordType);
    }

    public boolean isPreviousVerbThatCantConnect() {
        return mPreviousWordType != null && mPreviousWordType.startsWith("v") && !mPreviousConjugation.contains("-te form") && !mPreviousConjugation.contains("-i form");
    }

    public static boolean isVerbThatCantConnect(String mWordType, String mConjugation) {
        return mWordType != null && mWordType.startsWith("v") && !mConjugation.contains("-te form") && !mConjugation.contains("-i form");
    }

    public boolean isPreviousAdjThatCantConnect() {
        return  mPreviousWordType != null && mPreviousWordType.startsWith("adj") && !mPreviousConjugation.contains("-te form");
    }

    public boolean isPreviousDigit() {
        return mIsPreviousDigit || findString(mPreviousWordType, "num", false, false, ";");
    }

    public boolean isPreviousAdv() {
        return isAdv(mPreviousWordType);
    }

    public boolean isPreviousParticle() {
        return isParticle(mPreviousWordType);
    }

    public String getWordTypePreference() {
        return mWordTypePreference;
    }

    public String getWordTypeForbidden() {
        return mWordTypeForbidden;
    }

    public boolean isPreviousCounter() {
        return isCounter(mPreviousWordType);
    }

    private static boolean findString(String inInput, String inSearchString, boolean inStrict, boolean inStartsWith, String inSeparator) {

        if (inInput == null) return false;
        if (inStrict) return inInput.equals(inSearchString);

        String[] wordTypes = inInput.split(inSeparator);

        for (int i = 0; i < wordTypes.length; i++) {
            if (inStartsWith) {
                if (wordTypes[i].startsWith(inSearchString)) return true;
            } else {
                if (wordTypes[i].equals(inSearchString)) return true;
            }
        }

        return false;
    }

    public static boolean isUsuallyWrittenWithKanaAlone(String inMisc) {
        return findString(inMisc, "uk", false, false, ",");
    }

    public static boolean isNoun(String mWordType) {

        return (findString(mWordType, "n", false, false, ";")
                || findString(mWordType, "n-pref", false, false, ";"))
                && !findString(mWordType, "prt", false, false, ";")
                && !findString(mWordType, "v", false, false, ";")
                && !findString(mWordType, "n-adv", false, false, ";");
    }

    public static boolean isNotSuruVerb(String mWordType) {

        return !findString(mWordType, "vs", false, false, ";");
    }

    public static boolean isAdj(String mWordType) {
        return findString(mWordType, "adj", true, false, ";") || findString(mWordType, "aux-adj", true, false, ";");
    }

    public static boolean isAdv(String mWordType) {
        return findString(mWordType, "adv", true, false, ";") || findString(mWordType, "n-adv", true, false, ";");
    }

    public static boolean isVerb(String mWordType) {
        return findString(mWordType, "v", false, true, ";") && !findString(mWordType, "aux-v", false, true, ";") && !isNoun(mWordType) && !findString(mWordType, "exp", false, true, ";");
    }

    public static boolean isParticle(String mWordType) {
        return findString(mWordType, "prt", false, true, ";");
    }

    public static boolean isCounter(String mWordType) {
        return findString(mWordType, "ctr", false, true, ";");
    }


    public static boolean isSuru(String mWordType) {
        return mWordType.startsWith("vs-i");
    }
}