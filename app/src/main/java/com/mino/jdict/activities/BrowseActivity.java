package com.mino.jdict.activities;

import android.os.Bundle;

import com.mino.jdict.logics.activities.BrowseLogic;
import com.mino.jdict.logics.basic.Logic;

/**
 * Created by Mino on 2015-04-29.
 */
public class BrowseActivity extends BaseActivity  {

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        assert ((BrowseLogic) Logic.get(this)) != null;
        ((BrowseLogic) Logic.get(this)).onSaveInstanceState(savedInstanceState);
    }
}
