package com.mino.jdict.models.basic;

/**
 * Created by Mino on 2015-01-16.
 */
public class OptionContainer {

    private boolean mRefreshOnSearch;
    private int mControlID;
    private java.lang.Class<?> mControlType;


    public OptionContainer(int inControlID, boolean inRefreshOnSearch, java.lang.Class<?> inControlType) {

        this.mControlID = inControlID;
        this.mRefreshOnSearch = inRefreshOnSearch;
        this.mControlType = inControlType;
    }

    public int getControlID() {
        return mControlID;
    }

    public boolean getRefreshOnSearch() {
        return mRefreshOnSearch;
    }

    public java.lang.Class<?> getControlType() {
        return mControlType;
    }
}
