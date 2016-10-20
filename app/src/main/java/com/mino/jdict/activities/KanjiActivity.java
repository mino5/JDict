package com.mino.jdict.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;

import com.mino.jdict.R;
import com.mino.jdict.adapters.KanjiAdapter;
import com.mino.jdict.adapters.KanjiRecentAdapter;
import com.mino.jdict.interfaces.IActivitySection;
import com.mino.jdict.logics.basic.Logic;
import com.mino.jdict.logics.basic.SearchLogic;
import com.mino.jdict.models.activities.kanji.KanjiKanjiSearchPart;
import com.mino.jdict.models.activities.kanji.KanjiKunReadingSearchPart;
import com.mino.jdict.models.activities.kanji.KanjiMeaningSearchPart;
import com.mino.jdict.models.activities.kanji.KanjiNanoriSearchPart;
import com.mino.jdict.models.activities.kanji.KanjiOnReadingSearchPart;
import com.mino.jdict.models.basic.OptionContainer;
import com.mino.jdict.models.basic.SearchLogicModel;
import com.mino.jdict.models.basic.SearchOptionsModel;
import com.mino.jdict.models.basic.SearchPart;
import com.mino.jdict.models.basic.SearchRegion;
import com.mino.jdict.objects.activities.KanjiObject;
import com.mino.jdict.objects.activities.KanjiRecentObject;

import utils.other.FontHelper;

public class KanjiActivity extends BaseActivity implements IActivitySection {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = inflater.inflate(R.layout.activity_kanji, null, false);
        mDrawerLayout.addView(mContentView, 0);

        try {
            (new SearchLogic()).init(new SearchLogicModel(
                    new KanjiObject.KanjiObjectFactory(),
                    new KanjiRecentObject.KanjiRecentObjectFactory(),
                    new KanjiAdapter.KanjiAdapterFactory(),
                    new KanjiRecentAdapter.KanjiRecentFactory(),
                    R.layout.kanji_decomposition_item,
                    R.layout.kanji_decomposition_item,
                    R.id.listKanji,
                    R.id.listHeader,
                    KanjiActivity.class,
                    KanjiDetailsActivity.class,
                    mContentView,
                    this,
                    mNavigationDrawerFragment,
                    new SearchOptionsModel(R.id.searchOptionsLayoutKanji,
                            new OptionContainer[]
                                    {
                                            //new OptionContainer(R.id.checkBoxKanaAsRomaji, false, CheckBox.class),
                                            new OptionContainer(R.id.kanji_spinner, true, Spinner.class)
                                    },
                            R.id.OKbuttonKanji, R.id.CancelButtonKanji),
                    getString(R.string.recent_kanji),
                    new SearchRegion[]
                            {
                                    new SearchRegion<KanjiObject>(getString(R.string.reading), true, new SearchPart[]{new KanjiOnReadingSearchPart(), new KanjiKunReadingSearchPart(), new KanjiKanjiSearchPart()}, 1),
                                    new SearchRegion<KanjiObject>(getString(R.string.meaning), true, new SearchPart[]{new KanjiMeaningSearchPart()}, 1),
                                    new SearchRegion<KanjiObject>(getString(R.string.nanori), false, new SearchPart[]{new KanjiNanoriSearchPart()}, 1)
                            }
            ));
        } catch (Exception ex) {

        }

        FontHelper.getInstance().Initialize(mContentView.getContext().getAssets());

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
        return 3;
    }
}
