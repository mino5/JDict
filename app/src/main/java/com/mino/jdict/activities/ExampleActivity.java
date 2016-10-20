package com.mino.jdict.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;

import com.mino.jdict.R;
import com.mino.jdict.adapters.ExampleAdapter;
import com.mino.jdict.adapters.ExampleRecentAdapter;
import com.mino.jdict.interfaces.IActivitySection;
import com.mino.jdict.logics.basic.Logic;
import com.mino.jdict.logics.basic.SearchLogic;
import com.mino.jdict.models.activities.example.ExampleMeaningSearchPart;
import com.mino.jdict.models.activities.example.ExampleReadingSearchPart;
import com.mino.jdict.models.basic.OptionContainer;
import com.mino.jdict.models.basic.SearchLogicModel;
import com.mino.jdict.models.basic.SearchOptionsModel;
import com.mino.jdict.models.basic.SearchPart;
import com.mino.jdict.models.basic.SearchRegion;
import com.mino.jdict.objects.activities.ExampleObject;
import com.mino.jdict.objects.activities.ExampleRecentObject;

import utils.other.FontHelper;

public class ExampleActivity extends BaseActivity implements IActivitySection {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = inflater.inflate(R.layout.activity_example, null, false);
        mDrawerLayout.addView(mContentView, 0);

        (new SearchLogic()).init(new SearchLogicModel(
                new ExampleObject.ExampleObjectFactory(),
                new ExampleRecentObject.ExampleRecentObjectFactory(),
                new ExampleAdapter.ExampleAdapterFactory(),
                new ExampleRecentAdapter.ExampleRecentFactory(),
                R.layout.list_item,
                R.layout.list_item,
                R.id.listExample,
                R.id.listHeader,
                ExampleActivity.class,
                ExampleDetailsActivity.class,
                mContentView,
                this,
                mNavigationDrawerFragment,
                new SearchOptionsModel(R.id.searchOptionsLayoutExample,
                        new OptionContainer[]
                                {
                                        new OptionContainer(R.id.checkBoxCertified, true, CheckBox.class)
                                },
                        R.id.OKbuttonExample, R.id.CancelButtonExample),
                getString(R.string.recent_examples),
                new SearchRegion[]
                        {
                                new SearchRegion<ExampleObject>(getString(R.string.reading), true, new SearchPart[]{new ExampleReadingSearchPart()}, 1),
                                new SearchRegion<ExampleObject>(getString(R.string.meaning), true, new SearchPart[]{new ExampleMeaningSearchPart()}, 1)
                        }
        ));

        FontHelper.getInstance().Initialize(mContentView.getContext().getAssets());
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


    @Override
    public int getSectionNumber() {
        return 5;
    }
}
