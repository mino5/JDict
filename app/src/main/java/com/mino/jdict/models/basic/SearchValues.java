package com.mino.jdict.models.basic;

import java.util.ArrayList;

/**
 * Created by Mino on 2015-01-16.
 */
public class SearchValues {

    private ArrayList<Boolean> mCheckBoxValues;
    private ArrayList<Integer> mSpinnerValues;

    public ArrayList<Boolean> getCheckBoxValues() {
        return mCheckBoxValues;
    }

    public void setCheckBoxValues(ArrayList<Boolean> inCheckBoxValues) {
        this.mCheckBoxValues = inCheckBoxValues;
    }

    public ArrayList<Integer> getSpinnerValues() {
        return mSpinnerValues;
    }

    public void setSpinnerValues(ArrayList<Integer> inSpinnerValues) {
        this.mSpinnerValues = inSpinnerValues;
    }
}
