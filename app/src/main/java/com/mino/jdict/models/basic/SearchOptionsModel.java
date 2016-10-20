package com.mino.jdict.models.basic;

/**
 * Created by Mino on 2014-12-17.
 */
public class SearchOptionsModel {

    private int mSearchOptionsLayoutID;
    private OptionContainer[] mOptionContainers;
    private int mOKButtonID;
    private int mCancelButtonID;

    public SearchOptionsModel(int inSearchOptionsLayoutID, OptionContainer[] inOptionContainers, int inOKButtonID, int inCancelButtonID) {
        this.mSearchOptionsLayoutID = inSearchOptionsLayoutID;
        this.mOptionContainers = inOptionContainers;
        this.mOKButtonID = inOKButtonID;
        this.mCancelButtonID = inCancelButtonID;
    }


    public int getSearchOptionsLayoutID() {
        return mSearchOptionsLayoutID;
    }

    public OptionContainer[] getOptionContainers() {
        return mOptionContainers;
    }

    public int getOKButtonID() {
        return mOKButtonID;
    }

    public int getCancelButtonID() {
        return mCancelButtonID;
    }
}
