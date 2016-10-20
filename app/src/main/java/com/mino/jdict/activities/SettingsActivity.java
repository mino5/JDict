package com.mino.jdict.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;

import com.mino.jdict.R;
import com.mino.jdict.interfaces.IActivitySection;
import com.mino.jdict.logics.activities.AboutLogic;
import com.mino.jdict.logics.activities.SettingsLogic;
import com.mino.jdict.logics.basic.Logic;
import com.mino.jdict.models.basic.SimpleLogicModel;

/**
 * Created by Dominik on 11/10/2016.
 */
public class SettingsActivity extends BaseActivity implements IActivitySection {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = inflater.inflate(R.layout.activity_settings, null, false);

        mDrawerLayout.addView(mContentView, 0);
        new SettingsLogic().init(new SimpleLogicModel(this));
    }

    @Override
    public void onResume() {

        Logic.resumeActivity(this);
        super.onResume();
        // //  overridePendingTransition(0, 0);
    }

    @Override
    public int getSectionNumber() {
        return 15;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        assert ((SettingsLogic) Logic.get(this)) != null;
        ((SettingsLogic) Logic.get(this)).onCreateOptionsMenu(menu);

        return true;
    }
}
