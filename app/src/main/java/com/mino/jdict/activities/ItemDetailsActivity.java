package com.mino.jdict.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;

import com.mino.jdict.R;
import com.mino.jdict.logics.activities.ItemDetailsLogic;
import com.mino.jdict.logics.basic.Logic;

import com.mino.jdict.models.basic.SimpleLogicModel;

/**
 * Created by Mino on 2014-09-07.
 */

public class ItemDetailsActivity extends BaseActivity {


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        assert ((ItemDetailsLogic) Logic.get(this)) != null;
        ((ItemDetailsLogic) Logic.get(this)).onCreateOptionsMenu(menu);

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("ItemDetailsActivity", "onCreateView. Saved state? "
                + (savedInstanceState != null));

        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mContentView = inflater.inflate(R.layout.activity_itemdetails, null, false);
        mDrawerLayout.addView(mContentView, 0);

        new ItemDetailsLogic().init(new SimpleLogicModel(this, savedInstanceState));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        assert ((ItemDetailsLogic) Logic.get(this)) != null;
        ((ItemDetailsLogic) Logic.get(this)).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        assert ((ItemDetailsLogic) Logic.get(this)) != null;
        ((ItemDetailsLogic) Logic.get(this)).onNewIntent(intent);
    }

    @Override
    public void onResume() {

        Logic.resumeActivity(this);
        super.onResume();
      // //  overridePendingTransition(0, 0);
    }

    @Override
    public void onPause() {
        super.onPause();
      // //  overridePendingTransition(0, 0);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        assert ((ItemDetailsLogic) Logic.get(this)) != null;
        ((ItemDetailsLogic) Logic.get(this)).onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        assert ((ItemDetailsLogic) Logic.get(this)) != null;
        ((ItemDetailsLogic) Logic.get(this)).onRestoreInstanceState(savedInstanceState);
    }
}

