package com.mino.jdict.activities;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;

import com.mino.jdict.R;
import com.mino.jdict.logics.activities.ExampleDetailsLogic;
import com.mino.jdict.logics.basic.Logic;
import com.mino.jdict.models.basic.SimpleLogicModel;
import com.mino.jdict.objects.activities.ListObject;

/**
 * Created by Mino on 2014-11-08.
 */

public class ExampleDetailsActivity extends BaseActivity implements
        ActionBar.OnNavigationListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = inflater.inflate(R.layout.activity_itemdetails, null, false);
        mDrawerLayout.addView(mContentView, 0);

        new ExampleDetailsLogic().init(new SimpleLogicModel(this, savedInstanceState));
    }

    public void goToItemDetails(ListObject inListObj) {
        assert ((ExampleDetailsLogic) Logic.get(this)) != null;
        ((ExampleDetailsLogic) Logic.get(this)).goToItemDetails(inListObj);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        assert ((ExampleDetailsLogic) Logic.get(this)) != null;
        ((ExampleDetailsLogic) Logic.get(this)).onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        assert ((ExampleDetailsLogic) Logic.get(this)) != null;
        ((ExampleDetailsLogic) Logic.get(this)).onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        assert ((ExampleDetailsLogic) Logic.get(this)) != null;
        ((ExampleDetailsLogic) Logic.get(this)).onNewIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        assert ((ExampleDetailsLogic) Logic.get(this)) != null;
        ((ExampleDetailsLogic) Logic.get(this)).onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        assert ((ExampleDetailsLogic) Logic.get(this)) != null;
        ((ExampleDetailsLogic) Logic.get(this)).onPause();
        //  overridePendingTransition(0, 0);
    }

    @Override
    public void onResume() {

        Logic.resumeActivity(this);
        super.onResume();
        //  overridePendingTransition(0, 0);
    }


    @Override
    public void onBackPressed() {

        assert ((ExampleDetailsLogic) Logic.get(this)) != null;
        ((ExampleDetailsLogic) Logic.get(this)).onBackPressed();
        super.onBackPressed();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        assert ((ExampleDetailsLogic) Logic.get(this)) != null;
        ((ExampleDetailsLogic) Logic.get(this)).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onNavigationItemSelected(int i, long l) {
        return false;
    }


}
