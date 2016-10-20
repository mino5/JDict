package com.mino.jdict.models.basic;

import android.os.Bundle;

import com.mino.jdict.activities.BaseActivity;

/**
 * Created by Dominik on 7/24/2015.
 */
public class SimpleLogicModel extends LogicModel {

    private Bundle mSavedInstanceState;

    public SimpleLogicModel(BaseActivity inActivity) {
        mActivity = inActivity;
    }

    public SimpleLogicModel(BaseActivity inActivity, Bundle savedInstanceState) {
        mActivity = inActivity;
        mSavedInstanceState = savedInstanceState;
    }

    public Bundle getBundle() { return mSavedInstanceState; }
}
