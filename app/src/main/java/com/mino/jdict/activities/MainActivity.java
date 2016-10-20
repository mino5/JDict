package com.mino.jdict.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.mino.jdict.R;
import com.mino.jdict.adapters.ItemAdapter;
import com.mino.jdict.adapters.ItemRecentAdapter;
import com.mino.jdict.interfaces.IActivitySection;
import com.mino.jdict.logics.basic.Logic;
import com.mino.jdict.logics.basic.SearchLogic;
import com.mino.jdict.models.activities.vocabulary.VocabularyAdditionalSearchPart;
import com.mino.jdict.models.activities.vocabulary.VocabularySearchPart;
import com.mino.jdict.models.basic.OptionContainer;
import com.mino.jdict.models.basic.SearchLogicModel;
import com.mino.jdict.models.basic.SearchOptionsModel;
import com.mino.jdict.models.basic.SearchPart;
import com.mino.jdict.models.basic.SearchRegion;
import com.mino.jdict.objects.activities.ListObject;
import com.mino.jdict.objects.activities.RecentObject;

import utils.other.FontHelper;

public class MainActivity extends BaseActivity implements IActivitySection {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = inflater.inflate(R.layout.activity_main, null, false);
        mDrawerLayout.addView(mContentView, 0);

        (new SearchLogic()).init(new SearchLogicModel(
                new ListObject.ListObjectFactory(),
                new RecentObject.RecentObjectFactory(),
                new ItemAdapter.ItemAdapterFactory(),
                new ItemRecentAdapter.ItemRecentFactory(),
                R.layout.list_item,
                R.layout.list_item,
                R.id.list,
                R.id.listHeader,
                MainActivity.class,
                ItemDetailsActivity.class,
                mContentView,
                this,
                mNavigationDrawerFragment,
                new SearchOptionsModel(R.id.searchOptionsLayout,
                        new OptionContainer[]{
                                //new OptionContainer(R.id.kanaButton, false, CheckBox.class),
                                new OptionContainer(R.id.checkBoxCommon, true, CheckBox.class),
                                new OptionContainer(R.id.vocabulary_spinner, true, Spinner.class)
                        }, R.id.OKbutton, R.id.CancelButton),
                getString(R.string.recent_words),
                new SearchRegion[]
                        {
                                new SearchRegion<ListObject>(getString(R.string.exact_words), true, new SearchPart[]{new VocabularySearchPart()}, 1),
                                new SearchRegion<ListObject>(getString(R.string.additional_words), false, new SearchPart[]{new VocabularyAdditionalSearchPart()}, 1)
                        }
        ));

        FontHelper.getInstance().Initialize(mContentView.getContext().getAssets());

    }

    @Override
    public void onPause() {

        super.onPause();
        //  overridePendingTransition(0, 0);

    }

    @Override
    public void onResume() {

        super.onResume();
        Logic.resumeActivity(this);
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
        return 2;
    }
}
