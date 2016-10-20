package com.mino.jdict.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;

import com.mino.jdict.R;
import com.mino.jdict.interfaces.IActivitySection;
import com.mino.jdict.logics.activities.VocabularyLogic;
import com.mino.jdict.logics.basic.Logic;
import com.mino.jdict.models.basic.SimpleLogicModel;

/**
 * Created by Mino on 2015-04-29.
 */
public class VocabularyActivity extends BaseActivity implements IActivitySection {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = inflater.inflate(R.layout.activity_vocabulary, null, false);

        mDrawerLayout.addView(mContentView, 0);
        new VocabularyLogic().init(new SimpleLogicModel(this));
    }

    @Override
    public void onResume() {

        Logic.resumeActivity(this);
        super.onResume();
        //  overridePendingTransition(0, 0);
    }

    @Override
    public int getSectionNumber() {
        return 13;
    }

    @Override
    public void onBackPressed() {

        assert ((VocabularyLogic) Logic.get(this)) != null;
        Boolean backPress = ((VocabularyLogic) Logic.get(this)).onBackPressed();

        if (backPress) {
            super.onBackPressed();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        assert ((VocabularyLogic) Logic.get(this)) != null;
        ((VocabularyLogic) Logic.get(this)).onSaveInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        assert ((VocabularyLogic) Logic.get(this)) != null;
        ((VocabularyLogic) Logic.get(this)).onCreateOptionsMenu(menu);

        return true;
    }
}
