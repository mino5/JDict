package com.mino.jdict.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import com.mino.jdict.R;
import com.mino.jdict.adapters.ExtractAdapter;
import com.mino.jdict.interfaces.IActivitySection;
import com.mino.jdict.logics.basic.Logic;
import com.mino.jdict.logics.basic.SearchLogic;
import com.mino.jdict.models.activities.extract.ExtractKanjiSearchPart;
import com.mino.jdict.models.activities.extract.ExtractVocabularySearchPart;
import com.mino.jdict.models.basic.OptionContainer;
import com.mino.jdict.models.basic.SearchLogicModel;
import com.mino.jdict.models.basic.SearchOptionsModel;
import com.mino.jdict.models.basic.SearchPart;
import com.mino.jdict.models.basic.SearchRegion;
import com.mino.jdict.objects.activities.ExtractObject;
import com.mino.jdict.objects.activities.ListObject;

import utils.other.FontHelper;
import utils.other.ShowUtils;

public class ExtractActivity extends BaseActivity implements IActivitySection {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = inflater.inflate(R.layout.activity_extract, null, false);
        mDrawerLayout.addView(mContentView, 0);

        (new SearchLogic()).init(new SearchLogicModel(
                new ExtractObject.ExtractObjectFactory(),
                new ExtractAdapter.ExtractAdapterFactory(),
                R.id.recentView,
                R.layout.list_item,
                R.layout.list_item,
                R.id.listExtract,
                R.id.listHeader,
                ExtractActivity.class,
                ItemDetailsActivity.class,
                mContentView,
                this,
                mNavigationDrawerFragment,
                new SearchOptionsModel(R.id.searchOptionsLayoutExample,
                        new OptionContainer[]
                                {
                                        //new OptionContainer(R.id.kanaButton, false, CheckBox.class),
                                },
                        R.id.OKbuttonExample, R.id.CancelButtonExample),
                "",
                new SearchRegion[]
                        {
                                new SearchRegion<ExtractObject>(getString(R.string.vocabulary), true, new SearchPart[]{new ExtractVocabularySearchPart()}, 98),
                                new SearchRegion<ExtractObject>(getString(R.string.kanji), true, new SearchPart[]{new ExtractKanjiSearchPart()}, 2)
                        }
        ));

        FontHelper.getInstance().Initialize(mContentView.getContext().getAssets());
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        assert (Logic.get(this)) != null;
        ((SearchLogic) Logic.get(this)).onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        assert (Logic.get(this)) != null;
        ((SearchLogic) Logic.get(this)).onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        //overridePendingTransition(0, 0);
    }

    @Override
    public void onResume() {

        Logic.resumeActivity(this);
        super.onResume();
        //  overridePendingTransition(0, 0);
    }

    @Override
    public void onBackPressed() {

        assert (Logic.get(this)) != null;
        Boolean backPress = ((SearchLogic) Logic.get(this)).onBackPressed();

        if (backPress) {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // run logic createoptionsmenu

        assert (Logic.get(this)) != null;
        ((SearchLogic) Logic.get(this)).onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    public void goToItemDetails(ListObject inListObj) {

        if (inListObj != null) {
            ShowUtils.showEntry(inListObj, getBaseContext());
        }
    }


    @Override
    public int getSectionNumber() {
        return 7;
    }
}
