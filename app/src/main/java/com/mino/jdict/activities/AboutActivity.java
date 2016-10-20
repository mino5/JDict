package com.mino.jdict.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;

import com.mino.jdict.R;
import com.mino.jdict.interfaces.IActivitySection;
import com.mino.jdict.logics.activities.AboutLogic;
import com.mino.jdict.logics.basic.Logic;
import com.mino.jdict.models.basic.SimpleLogicModel;

/**
 * Created by Dominik on 8/23/2015.
 */
public class AboutActivity extends BaseActivity implements IActivitySection {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = inflater.inflate(R.layout.activity_about, null, false);

        mDrawerLayout.addView(mContentView, 0);
        new AboutLogic().init(new SimpleLogicModel(this));
    }

    @Override
    public void onResume() {

        Logic.resumeActivity(this);
        super.onResume();
       // //  overridePendingTransition(0, 0);
    }

    @Override
    public int getSectionNumber() {
        return 16;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        assert ((AboutLogic) Logic.get(this)) != null;
        ((AboutLogic) Logic.get(this)).onCreateOptionsMenu(menu);

        return true;
    }
}
