package com.mino.jdict.activities;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.mino.jdict.R;
import com.mino.jdict.logics.activities.StrokeOrderLogic;
import com.mino.jdict.logics.activities.StrokeOrderLogicForNewerSDK;
import com.mino.jdict.logics.basic.Logic;
import com.mino.jdict.models.basic.SimpleLogicModel;

//import com.larvalabs.svgandroid.SVG;
//import com.larvalabs.svgandroid.SVGParser;

/**
 * Created by Mino on 2014-10-07.
 */
public class StrokeOrderActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = inflater.inflate(R.layout.activity_strokeorder, null, false);

        mDrawerLayout.addView(mContentView, 0);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            new StrokeOrderLogicForNewerSDK().init(new SimpleLogicModel(this, savedInstanceState));
        } else {
            new StrokeOrderLogic().init(new SimpleLogicModel(this, savedInstanceState));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            assert ((StrokeOrderLogicForNewerSDK) Logic.get(this)) != null;
            ((StrokeOrderLogicForNewerSDK) Logic.get(this)).onSaveInstanceState(savedInstanceState);
        } else {

            assert ((StrokeOrderLogic) Logic.get(this)) != null;
            ((StrokeOrderLogic) Logic.get(this)).onSaveInstanceState(savedInstanceState);
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {

            assert ((StrokeOrderLogicForNewerSDK) Logic.get(this)) != null;
            ((StrokeOrderLogicForNewerSDK) Logic.get(this)).onRestoreInstanceState(savedInstanceState);
        } else {

            assert ((StrokeOrderLogic) Logic.get(this)) != null;
            ((StrokeOrderLogic) Logic.get(this)).onRestoreInstanceState(savedInstanceState);
        }

    }

    @Override
    public void onPause() {

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {

            assert ((StrokeOrderLogicForNewerSDK) Logic.get(this)) != null;
            ((StrokeOrderLogicForNewerSDK) Logic.get(this)).onPause();
        } else {

            assert ((StrokeOrderLogic) Logic.get(this)) != null;
            ((StrokeOrderLogic) Logic.get(this)).onPause();
        }

        super.onPause();
        //  overridePendingTransition(0, 0);
    }

    @Override
    public void onBackPressed() {

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {

            assert ((StrokeOrderLogicForNewerSDK) Logic.get(this)) != null;
            ((StrokeOrderLogicForNewerSDK) Logic.get(this)).onBackPressed();
        } else {

            assert ((StrokeOrderLogic) Logic.get(this)) != null;
            ((StrokeOrderLogic) Logic.get(this)).onBackPressed();
        }

        super.onBackPressed();
    }

    @Override
    public void onResume() {

        Logic.resumeActivity(this);
        super.onResume();
        //  overridePendingTransition(0, 0);
    }
}
