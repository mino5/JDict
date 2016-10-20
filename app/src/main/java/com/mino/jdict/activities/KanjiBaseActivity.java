package com.mino.jdict.activities;

import android.content.Intent;

import com.mino.jdict.logics.activities.KanjiBaseLogic;
import com.mino.jdict.logics.basic.Logic;

import utils.grammar.InputUtils;

/**
 * Created by Mino on 2014-10-07.
 */
public class KanjiBaseActivity extends BaseActivity {

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        assert ((KanjiBaseLogic) Logic.get(this)) != null;
        ((KanjiBaseLogic) Logic.get(this)).onActivityResult(requestCode, resultCode, data);
    }

    public void getMore() {
        assert ((KanjiBaseLogic) Logic.get(this)) != null;
        ((KanjiBaseLogic) Logic.get(this)).getMore();
    }

    public void getCompounds(InputUtils.WildCardMode inMode) {
        assert ((KanjiBaseLogic) Logic.get(this)) != null;
        ((KanjiBaseLogic) Logic.get(this)).getCompounds(inMode);
    }

}
