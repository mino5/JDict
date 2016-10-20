package com.mino.jdict.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;

import com.mino.jdict.R;
import com.mino.jdict.logics.activities.AddNoteLogic;
import com.mino.jdict.logics.basic.Logic;
import com.mino.jdict.models.basic.SimpleLogicModel;

/**
 * Created by Mino on 2015-04-21.
 */
public class AddNoteActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = inflater.inflate(R.layout.activity_addnote, null, false);

        mDrawerLayout.addView(mContentView, 0);
        Bundle extras = getIntent().getExtras();

        new AddNoteLogic().init(new SimpleLogicModel(this, extras));
    }

    @Override
    public void onResume() {

        Logic.resumeActivity(this);
        super.onResume();
       //  overridePendingTransition(0, 0);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        assert ((AddNoteLogic) Logic.get(this)) != null;
        ((AddNoteLogic) Logic.get(this)).onCreateOptionsMenu(menu);

        return true;
    }
}
