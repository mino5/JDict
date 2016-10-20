package com.mino.jdict.objects.basic;

/**
 * Created by Mino on 2015-03-05.
 */
public class CountObject {

    private int mShown, mCount;
    private String mType;

    public CountObject(int inShown, int inCount, String inType) {
        mShown = inShown;
        mCount = inCount;
        mType = inType;
    }

    public int getShown() {
        return mShown;
    }

    public int getCount() {
        return mCount;
    }

    public String getText() {
        String count = getCount() >= 1000 ? "1000+" : String.valueOf(getCount());
        return "Showing " + String.valueOf(getShown()) + " out of " + count + " " + mType;
    }
}
