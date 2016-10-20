package com.mino.jdict.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;

import com.mino.jdict.R;
import com.mino.jdict.interfaces.IActivitySection;
import com.mino.jdict.logics.basic.Logic;
import com.mino.jdict.logics.activities.NoteLogic;
import com.mino.jdict.models.basic.SimpleLogicModel;

/**
 * Created by Mino on 2015-04-24.
 */
public class NoteActivity extends BaseActivity implements IActivitySection {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = inflater.inflate(R.layout.activity_note, null, false);

        mDrawerLayout.addView(mContentView, 0);
        new NoteLogic().init(new SimpleLogicModel(this));
    }

    public void getMoreNotes() {
        assert ((NoteLogic) Logic.get(this)) != null;
        ((NoteLogic) Logic.get(this)).getMoreNotes();
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

        assert ((NoteLogic) Logic.get(this)) != null;
        ((NoteLogic) Logic.get(this)).onCreateOptionsMenu(menu);

        return true;
    }


    @Override
    public int getSectionNumber() {
        return 12;
    }

}
