package com.mino.jdict.models.basic;

import android.widget.ProgressBar;

import com.mino.jdict.objects.activities.ASearchObject;

import java.util.ArrayList;

/**
 * Created by Dominik on 9/5/2015.
 */
public class SearchPart {

    // tekst na headerze dla konkretnego partu (np. KUN readings)
    private int mResText;

    // czy ma ustawiony limit, czy raczej pokazuje od razu wszystko
    private boolean mHasLimit = false;

    // jaki limit ma ustawiony
    private int mLimit;

    // obecny offset
    private int mOffset = 0;

    // brak wyników
    private boolean mEmpty;

    // od jakiej długości ConjugatedQuery pokazujemy od razu pierwsze Limit itemów
    private int mStartsOnQueryLength;

    // powiązane z StartsOnQueryLength -> po kliknęciu nie zmieniamy offseta bo jeszcze nic nie pobraliśmy
    private boolean mPreventOffsetChange;

    // parent
    private SearchRegion mRegion;

    // opcje
    protected int mSelectedItemSpinner = 0;
    protected Boolean mFirstCheck = false;
    protected Boolean mSecondCheck = false;

    public SearchPart(int inResText) {
        mResText = inResText;
    }

    public SearchPart(int inResText, int inLimit, int inStartsOnQueryLength) {

        this(inResText);

        mHasLimit = true;
        mLimit = inLimit;
        mStartsOnQueryLength = inStartsOnQueryLength;
    }

    public void setRegion(SearchRegion inRegion) {
        mRegion = inRegion;
    }

    public SearchRegion getRegion() {
        return mRegion;
    }

    public Integer getLimit() {
        return mLimit;
    }

    public int getResText() {
        return mResText;
    }

    public Boolean getHasLimit() {
        return mHasLimit;
    }

    public Integer getOffset() {
        return mOffset;
    }

    public void setOffset(int inOffset) {
        mOffset = inOffset;
    }

    public void setPreventOffsetChange(boolean inPrevent) {
        mPreventOffsetChange = inPrevent;
    }

    public Boolean getPreventOffsetChange() {
        return mPreventOffsetChange;
    }

    public Boolean getIsEmpty() {
        return mEmpty;
    }

    public void setEmpty(boolean inEmpty) {
        mEmpty = inEmpty;
    }

    public int getStartsOnQueryLength() {
        return mStartsOnQueryLength;
    }

    public int getItems(String inSearchString, ArrayList<ASearchObject> result, SearchValues inSearchValues, ProgressBar inProgress) {
        return 0;
    }

    public void setSearchSetting(int i, Object value) {

        switch (i) {
            case 0:

                if (value instanceof Boolean) {
                    mFirstCheck = (Boolean) value;

                } else if (value instanceof Integer) {
                    mSelectedItemSpinner = (Integer) value;
                }

                break;

            case 1:

                if (value instanceof Boolean) {
                    mSecondCheck = (Boolean) value;
                }
                break;
        }
    }
}
