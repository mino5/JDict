package com.mino.jdict.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.mino.jdict.R;
import com.mino.jdict.logics.activities.KanjiComponentDetailsLogic;
import com.mino.jdict.logics.basic.Logic;
import com.mino.jdict.models.basic.SimpleLogicModel;

/**
 * Created by Mino on 2014-10-07.
 */
public class KanjiComponentDetailsActivity extends KanjiBaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = inflater.inflate(R.layout.activity_itemdetails, null, false);
        mDrawerLayout.addView(mContentView, 0);

        new KanjiComponentDetailsLogic().init(new SimpleLogicModel(this, savedInstanceState));
    }

    @Override
    public void onPause() {
        super.onPause();

        assert ((KanjiComponentDetailsLogic) Logic.get(this)) != null;
        ((KanjiComponentDetailsLogic) Logic.get(this)).onPause();
       //  overridePendingTransition(0, 0);
    }

    @Override
    public void onResume() {

        Logic.resumeActivity(this);
        super.onResume();
       //  overridePendingTransition(0, 0);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        assert ((KanjiComponentDetailsLogic) Logic.get(this)) != null;
        ((KanjiComponentDetailsLogic) Logic.get(this)).onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        assert ((KanjiComponentDetailsLogic) Logic.get(this)) != null;
        ((KanjiComponentDetailsLogic) Logic.get(this)).onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        assert ((KanjiComponentDetailsLogic) Logic.get(this)) != null;
        ((KanjiComponentDetailsLogic) Logic.get(this)).onNewIntent(intent);
    }


}
