package com.mino.jdict.models.basic;

/**
 * Created by Dominik on 9/4/2015.
 */
public class SearchRegion<OType> {

    // tekst na headerze, jak nie ma nic w całym regionie
    private String mTextEmpty;

    // czy ma być w ogóle jakiś Header widoczny jak nic nie ma
    private Boolean mHeaderVisibleWhenEmpty;

    // wszystkie party składające się na region wyszukiwań (np. KUN i ON readings - razem readings)
    private SearchPart[] mParts;

    // wszystkie składowe są puste -> wyświetlany aktualnie jest teksty z mTextEmpty
    private OType mEmptyHeader = null;

    // ciężar dla progressBara
    private int mRegionWeightPercent = 1;

    public SearchRegion(String inTextEmpty, Boolean inHeaderVisibleWhenEmpty, SearchPart[] inParts, int inWeight) {

        mTextEmpty = inTextEmpty;
        mHeaderVisibleWhenEmpty = inHeaderVisibleWhenEmpty;
        mParts = inParts;
        mRegionWeightPercent = inWeight;

        for (SearchPart part : mParts) {
            part.setRegion(this);
        }
    }

    public double getPartPercentage() {
        return (double)getPercentageWeight() / (double) getPartsCount();
    }

    public int getPercentageWeight() {
        return mRegionWeightPercent;
    }

    public OType getEmptyHeader() {
        return mEmptyHeader;
    }

    public void setEmptyHeader(OType inEmptyHeader) {
        mEmptyHeader = inEmptyHeader;
    }

    public String getTextEmpty() {
        return mTextEmpty;
    }

    public Boolean getVisibleWhenEmpty() {
        return mHeaderVisibleWhenEmpty;
    }

    public int getPartsCount() {
        return mParts.length;
    }

    public SearchPart getPart(int partNumber) {

        if (mParts.length > partNumber)
            return mParts[partNumber];
        else
            return null;
    }

}
