package com.mino.jdict.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;

import com.mino.jdict.R;
import com.mino.jdict.logics.activities.KanjiDetailsLogic;
import com.mino.jdict.logics.basic.Logic;
import com.mino.jdict.models.basic.SimpleLogicModel;

/**
 * Created by Mino on 2014-10-07.
 */
public class KanjiDetailsActivity extends KanjiBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = inflater.inflate(R.layout.activity_itemdetails, null, false);
        mDrawerLayout.addView(mContentView, 0);

        new KanjiDetailsLogic().init(new SimpleLogicModel(this, savedInstanceState));
    }

    @Override
    public void onPause() {
        super.onPause();
        assert ((KanjiDetailsLogic) Logic.get(this)) != null;
        ((KanjiDetailsLogic) Logic.get(this)).onPause();

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
        assert ((KanjiDetailsLogic) Logic.get(this)) != null;
        ((KanjiDetailsLogic) Logic.get(this)).onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        assert ((KanjiDetailsLogic) Logic.get(this)) != null;
        ((KanjiDetailsLogic) Logic.get(this)).onRestoreInstanceState(savedInstanceState);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        assert ((KanjiDetailsLogic) Logic.get(this)) != null;
        ((KanjiDetailsLogic) Logic.get(this)).onNewIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        assert ((KanjiDetailsLogic) Logic.get(this)) != null;
        ((KanjiDetailsLogic) Logic.get(this)).onCreateOptionsMenu(menu);
        return true;
    }
}
