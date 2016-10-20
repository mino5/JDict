package com.mino.jdict.models.basic;

import android.content.Context;
import android.view.View;

import com.mino.jdict.JDictApplication;
import com.mino.jdict.activities.BaseActivity;

/**
 * Created by Mino on 2014-12-19.
 */
public class LogicModel {

    protected BaseActivity mActivity;
    protected View mContentView;

    public BaseActivity getActivity() {
        return mActivity;
    }


    public Context getBaseContext() {
        return mActivity.getBaseContext();
    }

    public android.view.MenuInflater getMenuInflater() {
        return mActivity.getMenuInflater();
    }

    public View getContentView() {
        return mContentView;
    }
}
