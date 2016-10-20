package com.mino.jdict.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;

import com.mino.jdict.R;
import com.mino.jdict.interfaces.IActivitySection;
import com.mino.jdict.logics.activities.KanjiByComponentLogic;
import com.mino.jdict.logics.basic.Logic;
import com.mino.jdict.models.basic.SimpleLogicModel;
import com.mino.jdict.objects.activities.KanjiDetailObject;

//import com.larvalabs.svgandroid.SVG;
//import com.larvalabs.svgandroid.SVGParser;

/**
 * Created by Mino on 2014-10-07.
 */
public class KanjiByComponentActivity extends BaseActivity implements IActivitySection {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = inflater.inflate(R.layout.kanjibycomponent_layout, null, false);

        mDrawerLayout.addView(mContentView, 0);

        new KanjiByComponentLogic().init(new SimpleLogicModel(this, savedInstanceState));
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        assert ((KanjiByComponentLogic) Logic.get(this)) != null;
        ((KanjiByComponentLogic) Logic.get(this)).onSaveInstanceState(savedInstanceState);
    }

    public void GetKanjiByComponent(KanjiDetailObject kdo) {
        assert ((KanjiByComponentLogic) Logic.get(this)) != null;
        ((KanjiByComponentLogic) Logic.get(this)).getKanjiByComponent(kdo);
    }

    public void GetKanjiDetails(KanjiDetailObject o) {
        assert ((KanjiByComponentLogic) Logic.get(this)) != null;
        ((KanjiByComponentLogic) Logic.get(this)).getKanjiDetails(o);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        assert ((KanjiByComponentLogic) Logic.get(this)) != null;
        ((KanjiByComponentLogic) Logic.get(this)).onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public void onPause() {

        super.onPause();
       //  overridePendingTransition(0, 0);
    }

    @Override
    public void onResume() {

        Logic.resumeActivity(this);
        super.onResume();
       //  overridePendingTransition(0, 0);
    }

    @Override
    public int getSectionNumber() {
        return 4;
    }

}
