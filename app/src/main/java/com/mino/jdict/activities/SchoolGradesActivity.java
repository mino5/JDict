package com.mino.jdict.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;

import com.mino.jdict.R;
import com.mino.jdict.interfaces.IActivitySection;
import com.mino.jdict.logics.basic.Logic;
import com.mino.jdict.logics.activities.SchoolGradesLogic;
import com.mino.jdict.models.basic.SimpleLogicModel;

/**
 * Created by Dominik on 8/23/2015.
 */
public class SchoolGradesActivity extends BrowseActivity implements IActivitySection {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = inflater.inflate(R.layout.activity_browse, null, false);

        mDrawerLayout.addView(mContentView, 0);
        new SchoolGradesLogic().init(new SimpleLogicModel(this));
    }

    @Override
    public void onResume() {

        Logic.resumeActivity(this);
        super.onResume();
       //  overridePendingTransition(0, 0);
    }

    @Override
    public int getSectionNumber() { return 10; }

    @Override
    public void onBackPressed() {

        assert ((SchoolGradesLogic) Logic.get(this)) != null;
        Boolean backPress = ((SchoolGradesLogic) Logic.get(this)).onBackPressed();

        if (backPress) {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        assert ((SchoolGradesLogic) Logic.get(this)) != null;
        ((SchoolGradesLogic) Logic.get(this)).onCreateOptionsMenu(menu);

        return true;
    }
}
