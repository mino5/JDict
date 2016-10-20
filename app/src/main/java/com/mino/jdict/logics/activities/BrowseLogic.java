package com.mino.jdict.logics.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.mino.jdict.JDictApplication;
import com.mino.jdict.R;
import com.mino.jdict.activities.KanjiActivity;
import com.mino.jdict.activities.KanjiDetailsActivity;
import com.mino.jdict.adapters.BrowseAdapter;
import com.mino.jdict.interfaces.IClearable;
import com.mino.jdict.logics.basic.Logic;
import com.mino.jdict.models.basic.SearchValues;
import com.mino.jdict.objects.activities.BrowseObject;
import com.mino.jdict.objects.activities.ItemDetailObject;
import com.mino.jdict.objects.activities.KanjiObject;
import com.mino.jdict.objects.activities.KanjiRecentObject;

import java.util.ArrayList;

import utils.other.StaticObjectContainer;

/**
 * Created by Dominik on 8/23/2015.
 */
public class BrowseLogic extends Logic implements IClearable {
    protected ArrayList<BrowseObject> mObjectList = new ArrayList<BrowseObject>();
    protected ListView mListView;
    protected BrowseAdapter mAdapter;
    protected Boolean mToRefresh = false;
    private boolean mChooseMode = true;
    private ActionBar mActionBar;
    private int mPos;
    protected BrowseType mType;

    protected TextView mActionBarHeader;
    protected String[] mNameArray;
    protected String[] mDescriptionArray;
    protected String[] mCounts;
    protected String[] mActionBarTitles;
    protected String[] mIconTexts;
    protected String mCurrentActionBarTitle = "";

    protected enum BrowseType { JLPT, School }

    private class State {

        private boolean mChooseMode;
        private int mPos;
        private BrowseType mType;

        public State(boolean inChooseMode, int inPos, BrowseType inType) {

            mChooseMode = inChooseMode;
            mPos = inPos;
            mType = inType;
        }

        public boolean getChooseMode() { return mChooseMode; }
        public int getPos() { return mPos; }
        public BrowseType getType() { return mType; }

    }


    public BrowseLogic() { }

    public void initialize() {
        mObjectList.clear();

        if (StaticObjectContainer.StaticObject != null && StaticObjectContainer.StaticObject instanceof State) {
            State state = (State) StaticObjectContainer.StaticObject;

            if (mType == state.getType()) {
                mChooseMode = state.getChooseMode();
                mPos = state.getPos();
            }
        }

        StaticObjectContainer.StaticObject = null;
        setAdapter();

        if (mChooseMode) {
            loadFolders();
        } else {
            initResources();
        }
    }

    protected void initResources() { }

    protected void loadFolders() { }

    private void restoreActionBar() {
        mActionBar = mActivity.getActionBar();
        mActionBar.setTitle(mCurrentActionBarTitle);
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);
        mActionBar.setDisplayShowHomeEnabled(true);
        mActionBar.setCustomView(R.layout.simple_actionbar_layout);
        mActionBar.setDisplayShowCustomEnabled(true);
    }

    public void onCreateOptionsMenu(Menu menu) {

        restoreActionBar();
        final View actionBarCustomView = mActionBar.getCustomView();

        mActionBarHeader =
                (TextView) actionBarCustomView.findViewById(R.id.actionBarHeader);

        mActionBarHeader.setText(mCurrentActionBarTitle);

        if (!mChooseMode) {
            getDetails(mPos);
        }
    }

    public void clear(IClearable.ClearanceLevel inClearanceLevel) {

        mObjectList.clear();
        mAdapter.clear();
        mAdapter.notifyDataSetChanged();
        mToRefresh = true;
    }

    public void resume() {

        if (mToRefresh) {
            mToRefresh = false;

            if (mChooseMode) {
                loadFolders();
            } else {
                getDetails(mPos);
            }
        }

        super.resume();
    }


    public void onSaveInstanceState(Bundle savedInstanceState) {

        StaticObjectContainer.StaticObject = new State(mChooseMode, mPos, mType);
    }

    private void setAdapter() {

        mAdapter = new BrowseAdapter(mActivity, R.layout.browse_list_item, mObjectList);
        mListView = (ListView) mActivity.findViewById(R.id.listBrowse);
        mListView.setAdapter(mAdapter);

        mListView.setClickable(true);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                Object o = mListView.getItemAtPosition(position);

                if (o instanceof BrowseObject) {

                    if (((BrowseObject) o).KanjiObject.get() != null) {

                        ItemDetailObject idObj = new ItemDetailObject(((BrowseObject) o).KanjiObject.get());

                        Intent i = new Intent(mActivity.getBaseContext(), KanjiDetailsActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        Bundle mBundle = new Bundle();
                        mBundle.putParcelable("PAR", idObj);
                        i.putExtras(mBundle);

                        KanjiRecentObject recentObj = new KanjiRecentObject.KanjiRecentObjectFactory().factory();
                        KanjiObject kanjiObj = new KanjiObject(idObj.getKanjiObject());

                        SearchValues values = new SearchValues();
                        ArrayList<Boolean> cbValues = new ArrayList<Boolean>();
                        cbValues.add(false);
                        values.setCheckBoxValues(cbValues);

                        recentObj.bind(kanjiObj, values);

                        JDictApplication.getDatabaseHelper().getWord().insertRecentWords(recentObj);
                        Logic.refreshActivity(KanjiActivity.class, IClearable.ClearanceLevel.Soft);

                        mActivity.startActivity(i);
                    } else {

                        mChooseMode = false;
                        mPos = ((BrowseObject) o).Pos.get();
                        mObjectList.clear();
                        mAdapter.notifyDataSetChanged();
                        getDetails(mPos);
                    }
                }
            }

        });
    }

    public boolean onBackPressed() {

        if (!mChooseMode) {

            mActionBarHeader.setText(mCurrentActionBarTitle);
            mChooseMode = true;
            mObjectList.clear();
            loadFolders();

            return false;
        }

        return true;
    }

    protected void getDetails(int pos) {
    }

}
