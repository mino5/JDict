package com.mino.jdict.logics.activities;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;

import com.mino.jdict.JDictApplication;
import com.mino.jdict.R;
import com.mino.jdict.activities.KanjiActivity;
import com.mino.jdict.activities.KanjiDetailsActivity;
import com.mino.jdict.adapters.KanjiByComponentAdapter;
import com.mino.jdict.interfaces.IClearable;
import com.mino.jdict.logics.basic.Logic;
import com.mino.jdict.models.basic.SearchValues;
import com.mino.jdict.objects.activities.KanjiBaseObject;
import com.mino.jdict.objects.activities.KanjiDetailObject;
import com.mino.jdict.objects.activities.KanjiObject;
import com.mino.jdict.objects.activities.KanjiRecentObject;
import com.mino.jdict.objects.basic.KanjiContainerObject;
import com.viewpagerindicator.LinePageIndicator;

import java.util.ArrayList;
import java.util.HashMap;

import utils.grammar.InputUtils;
import utils.other.FontHelper;
import utils.other.NonScrollableListView;
import utils.other.StaticObjectContainer;

/**
 * Created by Dominik on 7/26/2015.
 */
public class KanjiByComponentLogic extends Logic {

    private static final int componentPages = 3;
    private static final int rowSize = 5;
    private static final int buttonsCount = 8;
    private final int pageSize = 20;
    private ArrayList<KanjiContainerObject> componentLists[] = new ArrayList[componentPages];
    private ArrayList<KanjiContainerObject> kanjiList = new ArrayList<KanjiContainerObject>();
    private KanjiByComponentAdapter adapterKanji;
    private KanjiByComponentAdapter[] adapterComponents = new KanjiByComponentAdapter[componentPages];
    private ArrayList<String> mSelectedStrings = new ArrayList<String>();
    private ArrayList<KanjiDetailObject> mSelectedComponents = new ArrayList<KanjiDetailObject>();
    private ActionBar mActionBar;
    private SearchView mSearchView;
    private int mCurrentStrokeCount = 0;
    private HashMap<Integer, Boolean> mKanjiAvailabilityLoaded = new HashMap<Integer, Boolean>();
    private HashMap<Integer, ArrayList<KanjiDetailObject>> mComponentLists = new HashMap<Integer, ArrayList<KanjiDetailObject>>();
    private ImageView mCloseButton;
    private ViewPager pager;
    private ComponentAdapter componentAdapter;
    private Button buttons[] = new Button[buttonsCount];
    private String mCurrentQuery = "";
    private LinePageIndicator titleIndicator;
    private LayoutInflater inflater;

    private class State {

        private HashMap<Integer, ArrayList<KanjiDetailObject>> mComponentLists;
        private ArrayList<KanjiDetailObject> mSelectedComponents;
        private ArrayList<String> mSelectedStrings;
        private int mCurrentStrokeCount;
        private String mCurrentQuery;
        private ArrayList<KanjiContainerObject>[] mComponentListArray;

        public State(ArrayList<KanjiContainerObject>[] inComponentListArray, HashMap<Integer, ArrayList<KanjiDetailObject>> inComponentLists, ArrayList<KanjiDetailObject> inSelectedComponents, ArrayList<String> inSelectedStrings, int inCurrentStrokeCount, String inCurrentQuery) {

            mSelectedComponents = inSelectedComponents;
            mCurrentQuery = inCurrentQuery;
            mSelectedStrings = inSelectedStrings;
            mCurrentStrokeCount = inCurrentStrokeCount;
            mComponentListArray = inComponentListArray;
            mComponentLists = inComponentLists;
        }

        public ArrayList<KanjiContainerObject>[] getComponentListArray() { return mComponentListArray; }
        public HashMap<Integer, ArrayList<KanjiDetailObject>> getComponentLists() { return mComponentLists; }
        public ArrayList<KanjiDetailObject> getSelectedComponents() { return mSelectedComponents;}
        public ArrayList<String> getSelectedStrings() { return mSelectedStrings; }
        public String getCurrentQuery() {
            return mCurrentQuery;
        }
        public int getCurrentStrokeCount() { return mCurrentStrokeCount; }
    }

    public void initialize() {

        inflater = mActivity.getLayoutInflater();

        for (int i = 0; i <= 2; i++) {
            componentLists[i] = new ArrayList<KanjiContainerObject>();
        }

        int strokeCount = 1;

        if (StaticObjectContainer.StaticObject != null && StaticObjectContainer.StaticObject instanceof State) {
            State state = (State) StaticObjectContainer.StaticObject;
            mSelectedComponents = state.getSelectedComponents();
            mComponentLists = state.getComponentLists();
            mSelectedStrings = state.getSelectedStrings();
            strokeCount = state.getCurrentStrokeCount();
            mCurrentQuery = state.getCurrentQuery();
            componentLists = state.getComponentListArray();

            StaticObjectContainer.StaticObject = null;
        }

        init(strokeCount);
        hideKeyboard();
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {

        StaticObjectContainer.StaticObject = new State(componentLists, mComponentLists, mSelectedComponents, mSelectedStrings, mCurrentStrokeCount, mCurrentQuery);
    }

    public void resume() {

        if (mSearchView != null && mSearchView.getQuery().length() == 0) {

            mCloseButton.setVisibility(View.GONE);
        }

        hideKeyboard();
    }

    public void onCreateOptionsMenu(Menu menu) {
        // run logic createoptionsmenu

        restoreActionBar();
        final View actionBarCustomView = mActionBar.getCustomView();

        mSearchView =
                (SearchView) actionBarCustomView.findViewById(R.id.search);

        mSearchView.setQueryHint(mActivity.getSearchTitle());

        ImageView mOptionsButton =
                (ImageView) actionBarCustomView.findViewById(R.id.optionsButton);

        ImageView mBackSpaceButton =
                (ImageView) actionBarCustomView.findViewById(R.id.backspaceButton);

        mOptionsButton.setVisibility(View.INVISIBLE);
        mBackSpaceButton.setVisibility(View.VISIBLE);


        int closeId = mSearchView.getContext().getResources().getIdentifier("android:id/search_close_btn", null, null);


        mCloseButton = (ImageView) mSearchView.findViewById(closeId);
        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mSearchView.getQuery().length() > 0) {
                    mCloseButton.setVisibility(View.GONE);
                    mSearchView.setQuery("", false);
                }
            }
        });

        mBackSpaceButton.setClickable(true);
        mBackSpaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mSearchView.getQuery().length() > 0) {
                    KanjiDetailObject kdo = mSelectedComponents.get(mSelectedComponents.size() - 1);

                    kdo.IsSelected.set(false);

                    if (mSelectedStrings.contains(kdo.getKanjiObject().getKunyomi())) {
                        mSelectedStrings.remove(kdo.getKanjiObject().getKunyomi());
                        mSelectedComponents.remove(kdo);

                    }

                    for (int i = 0; i < buttonsCount; i++) {
                        mKanjiAvailabilityLoaded.put(i, false);
                    }

                    getComponents(mCurrentStrokeCount);
                    getKanjiByComponent();
                    mSearchView.setQuery(getTextForSearch(), false);
                }
            }
        });

        mSearchView.setIconified(false);
        mSearchView.setEnabled(false);
        mSearchView.clearFocus();
        mSearchView.setFocusable(false);
        mCloseButton.setVisibility(View.GONE);

        int searchPlateId = mSearchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText searchPlate = (EditText) mSearchView.findViewById(searchPlateId);
        searchPlate.setClickable(false);
        searchPlate.setFocusable(false);

        // jako, że część komponentów się nie wyświetla w normalnym foncie, góra będzie wyświetlana w foncie DroidSans
        // problem zostaje, że co najmniej jeden komopnent inaczej wygląda w obu fontach
        searchPlate.setTypeface(FontHelper.getInstance().getDroidSansJapanese());


        SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                mSearchView.clearFocus();
                hideKeyboard();

                if (query.isEmpty()) {

                    mCurrentQuery = "";
                    mCloseButton.setVisibility(View.GONE);
                    mSelectedStrings.clear();

                    clearSelected();

                    getComponents(mCurrentStrokeCount);
                    getKanjiByComponent();
                } else {

                    mCurrentQuery = query;
                    mCloseButton.setVisibility(View.VISIBLE);
                }

                if (mCurrentStrokeCount == 0) {
                    mCurrentStrokeCount = 1;
                }
                return true;
            }

        };

        mSearchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if (mSearchView.getQuery().length() == 0) {
                    mCloseButton.setVisibility(View.GONE);
                }

                if (b) {
                    mSearchView.clearFocus();
                }


            }
        });

        mSearchView.setOnQueryTextListener(listener);

        if (!mCurrentQuery.isEmpty()) {
            mSearchView.setQuery(getTextForSearch(), true);
        }

        hideKeyboard();
    }

    private void init(int strokeCount) {

        for (int i = 1; i <= buttonsCount; i++) {
            mKanjiAvailabilityLoaded.put(i, true);
        }

        setAdapter();
        getComponents(strokeCount);

        if (mSelectedStrings.size() > 0) {
            getKanjiByComponent();

            if (mCurrentStrokeCount > 0) {
                setButtonBackgroundColors(mCurrentStrokeCount - 1);
            }
        }
    }

    private void getComponents(int strokeCount) {

        if (mCurrentStrokeCount == strokeCount) {
            for (int i = 0; i < componentPages; i++) {
                adapterComponents[i].notifyDataSetChanged();
            }
            return;
        }

        int pageLimit = 1;

        mCurrentStrokeCount = strokeCount;

        for (int i = 0; i < componentPages; i++) {
            componentLists[i].clear();
        }

        if (mComponentLists.containsKey(mCurrentStrokeCount)) {

            ArrayList<KanjiDetailObject> list = mComponentLists.get(mCurrentStrokeCount);
            pageLimit = (list.size() / (pageSize + 1)) + 1;

            KanjiContainerObject containerObject = new KanjiContainerObject();
            int j = 1;
            int k = 0;

            for (KanjiDetailObject obj : list) {

                containerObject.add(j, obj);
                j++;
                k++;

                if (j == rowSize + 1) {

                    addToComponentList(containerObject, k);

                    containerObject = new KanjiContainerObject();
                    j = 1;
                }
            }

            if (j > 1) {

                addToComponentList(containerObject, k);
            }

        } else {
            ArrayList<Pair<String, String>> list = InputUtils.getComponentCharacterEntSeq(strokeCount);
            ArrayList<KanjiDetailObject> listToAdd = new ArrayList<KanjiDetailObject>();

            for (Pair<String, String> pair : list) {

                Boolean selected = mSelectedStrings.contains(pair.second);
                KanjiDetailObject obj = new KanjiDetailObject(new KanjiBaseObject(0, pair.second, "", "", "", pair.first), true, false);

                obj.IsSelected.set(selected);

                listToAdd.add(obj);
            }

            pageLimit = (listToAdd.size() / (pageSize + 1)) + 1;

            mComponentLists.put(mCurrentStrokeCount, listToAdd);
            KanjiContainerObject containerObject = new KanjiContainerObject();
            int j = 1;
            int k = 0;

            for (KanjiDetailObject obj : listToAdd) {

                containerObject.add(j, obj);
                j++;
                k++;

                if (j == rowSize + 1) {

                    addToComponentList(containerObject, k);

                    containerObject = new KanjiContainerObject();
                    j = 1;
                }
            }

            if (j > 1) {
                addToComponentList(containerObject, k);
            }
        }

        for (int i = 0; i < componentPages; i++) {
            adapterComponents[i].notifyDataSetChanged();
        }

        if (!mKanjiAvailabilityLoaded.get(mCurrentStrokeCount)) {

            getKanjiNonAvailable();
            mKanjiAvailabilityLoaded.put(mCurrentStrokeCount, true);
        }

        componentAdapter.setCount(pageLimit);

        titleIndicator.notifyDataSetChanged();
        titleIndicator.forceLayout();
        pager.setCurrentItem(0);

        componentAdapter.setCount(pageLimit);


    }

    private void addToComponentList(KanjiContainerObject containerObject, int j) {

        componentLists[j / (pageSize + 1)].add(containerObject);
    }

    private void getKanjiByComponent() {

        getKanjiNonAvailable();

        for (int i = 1; i <= buttonsCount; i++) {
            mKanjiAvailabilityLoaded.put(i, i == mCurrentStrokeCount);
        }

        kanjiList.clear();

        if (mSelectedStrings.size() > 0) {
            Cursor cursor = JDictApplication.getDatabaseHelper().getKanji().getKanjiByComponent(mSelectedStrings);

            if (cursor != null && cursor.getCount() > 0) {

                int cIndex = cursor.getColumnIndex("character_seq");
                int litIndex = cursor.getColumnIndex("literal");
                int decompIndex = cursor.getColumnIndex("decomposition");
                int meaningIndex = cursor.getColumnIndex("meaning");

                int i = 1;
                KanjiContainerObject obj = new KanjiContainerObject();

                do {

                    obj.add(i, new KanjiDetailObject(new KanjiBaseObject(cursor.getInt(cIndex), "", "", "", cursor.getString(meaningIndex), cursor.getString(litIndex)), false, true));
                    i++;

                    if (i == rowSize + 1) {
                        kanjiList.add(obj);
                        i = 1;
                        obj = new KanjiContainerObject();
                    }

                } while (cursor.moveToNext());

                if (i > 1) {
                    kanjiList.add(obj);
                }

                cursor.close();
            }
        }

        adapterKanji.notifyDataSetChanged();
    }

    private void getKanjiNonAvailable() {

        for (int i = 0; i < componentPages; i++) {
            setEmpty(componentLists[i], true);
        }

        if (mSelectedStrings.size() > 0) {

            ArrayList<String> availabilityList = JDictApplication.getDatabaseHelper().getKanji().getKanjiNonAvailableByComponent(mSelectedStrings, mCurrentStrokeCount);

            for (String str : availabilityList) {

                for (int i = 0; i < componentPages; i++) {
                    checkForNonEmpty(str, componentLists[i]);
                }
            }

            for (int i = 0; i < componentPages; i++) {
                checkInSelectedStrings(componentLists[i]);
            }

        } else {

            for (int i = 0; i < componentPages; i++) {
                setEmpty(componentLists[i], false);
            }
        }
    }

    private void checkInSelectedStrings(ArrayList<KanjiContainerObject> cList) {
        for (KanjiContainerObject kco : cList) {
            for (KanjiDetailObject kdo : kco) {
                if (mSelectedStrings.contains(kdo.getKanjiObject().getKunyomi())) {
                    kdo.IsEmpty.set(false);
                }
            }
        }
    }

    private void setEmpty(ArrayList<KanjiContainerObject> cList, boolean empty) {
        for (KanjiContainerObject kco : cList) {
            for (KanjiDetailObject kdo : kco) {
                kdo.IsEmpty.set(empty);
            }
        }
    }

    private void checkForNonEmpty(String str, ArrayList<KanjiContainerObject> cList) {
        for (KanjiContainerObject kco : cList) {
            for (KanjiDetailObject kdo : kco) {

                if (kdo.getKanjiObject().getKunyomi().equals(str)) {
                    kdo.IsEmpty.set(false);
                    break;
                }
            }
        }
    }

    private String getTextForSearch() {

        String text = "";

        for (String s : mSelectedStrings) {
            text += s;
            text += ";";
        }

        if (!text.isEmpty()) {
            text = text.substring(0, text.length() - 1);
        }

        return text;
    }


    private void clearSelected() {

        for (int i = 0; i < componentPages; i++) {
            setEmpty(componentLists[i], false);
        }

        for (ArrayList<KanjiDetailObject> list : mComponentLists.values()) {

            for (KanjiDetailObject obj : list) {
                obj.IsSelected.set(false);
            }
        }
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = mActivity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void restoreActionBar() {
        mActionBar = mActivity.getActionBar();
        assert mActionBar != null;
        mActionBar.setTitle("");
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);
        mActionBar.setDisplayShowHomeEnabled(true);
        mActionBar.setCustomView(R.layout.actionbar_layout);
        mActionBar.setDisplayShowCustomEnabled(true);
    }


    private void setAdapter() {

        for (int i = 0; i < componentPages; i++) {

            componentLists[i].clear();
        }

        kanjiList.clear();

        for (int i = 0; i < componentPages; i++) {
            if (adapterComponents[i] != null) {
                adapterComponents[i].clear();
                adapterComponents[i].notifyDataSetChanged();
            }
        }

        if (adapterKanji != null) {
            adapterKanji.clear();
            adapterKanji.notifyDataSetChanged();
        }

        for (int i = 0; i < componentPages; i++) {
            adapterComponents[i] = new KanjiByComponentAdapter(mActivity, R.layout.kanjidetail_item, componentLists[i]);
        }

        adapterKanji = new KanjiByComponentAdapter(mActivity, R.layout.kanjidetail_item, kanjiList);

        ListView mListViewKanji = (ListView) mActivity.findViewById(R.id.ListViewUP);
        mListViewKanji.setAdapter(adapterKanji);

        componentAdapter = new ComponentAdapter(inflater);
        pager = (ViewPager) mActivity.findViewById(R.id.pager);
        pager.setAdapter(componentAdapter);


        //bind the title indicator to the adapter
        titleIndicator = (LinePageIndicator) mActivity.findViewById(R.id.indicator);
        //titleIndicator.setFades(false);
        titleIndicator.setViewPager(pager);

        componentAdapter.setCount(1);

        buttons[0] = (Button) mActivity.findViewById(R.id.button);
        buttons[1] = (Button) mActivity.findViewById(R.id.button2);
        buttons[2] = (Button) mActivity.findViewById(R.id.button3);
        buttons[3] = (Button) mActivity.findViewById(R.id.button4);
        buttons[4] = (Button) mActivity.findViewById(R.id.button5);
        buttons[5] = (Button) mActivity.findViewById(R.id.button6);
        buttons[6] = (Button) mActivity.findViewById(R.id.button7);
        buttons[7] = (Button) mActivity.findViewById(R.id.button8);

        for (int i = 0; i < buttonsCount; i++) {

            final int currentComponent = i;
            buttons[i].setClickable(true);
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    setButtonBackgroundColors(currentComponent);
                    getComponents(currentComponent + 1);
                }
            });
        }
    }

    private void setButtonBackgroundColors(int currentComponent) {
        buttons[currentComponent].setBackgroundColor(mActivity.getResources().getColor(R.color.gray));

        for (int i = 0; i < buttonsCount; i++) {
            if (i != currentComponent) {
                buttons[i].setBackgroundResource(R.drawable.component_button);
            }
        }
    }

    public void getKanjiDetails(KanjiDetailObject o) {
        KanjiBaseObject obj = ((KanjiDetailObject) o).getKanjiObject();

        if (obj != null) {
            Intent i = new Intent(mActivity.getBaseContext(), KanjiDetailsActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
            Bundle mBundle = new Bundle();
            mBundle.putParcelable("PAR", ((KanjiDetailObject) o));
            i.putExtras(mBundle);

            KanjiRecentObject recentObj = new KanjiRecentObject.KanjiRecentObjectFactory().factory();
            KanjiObject kanjiObject = new KanjiObject(obj);

            SearchValues values = new SearchValues();
            ArrayList<Boolean> cbValues = new ArrayList<Boolean>();
            cbValues.add(false);
            values.setCheckBoxValues(cbValues);

            recentObj.bind(kanjiObject, values);

            JDictApplication.getDatabaseHelper().getWord().insertRecentWords(recentObj);
            Logic.refreshActivity(KanjiActivity.class, IClearable.ClearanceLevel.Soft);

            mActivity.startActivity(i);
        }
    }

    public void getKanjiByComponent(KanjiDetailObject kdo) {

        if (!kdo.IsEmpty.get()) {
            kdo.IsSelected.set(!kdo.IsSelected.get());

            if (mSelectedStrings.contains(kdo.getKanjiObject().getKunyomi())) {
                mSelectedStrings.remove(kdo.getKanjiObject().getKunyomi());
                mSelectedComponents.remove(kdo);

            } else {
                mSelectedStrings.add(kdo.getKanjiObject().getKunyomi());
                mSelectedComponents.add(kdo);
            }

            getKanjiByComponent();

            for (int i = 0; i < componentPages; i++) {
                adapterComponents[i].notifyDataSetChanged();
            }

            mSearchView.setQuery(getTextForSearch(), false);
        }
    }

    public class ComponentAdapter extends PagerAdapter {

        private int count = -1;
        private LayoutInflater mInflator;

        public ComponentAdapter(LayoutInflater inInflator) {
            mInflator = inInflator;
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        public Object instantiateItem(ViewGroup container, int position) {

            if (position > getCount()) return null;

            View child = mInflator.inflate(R.layout.decomposition_page_item, null, false);

            NonScrollableListView mListViewComponent = (NonScrollableListView) child.findViewById(R.id.ListViewDOWN);
            mListViewComponent.setAdapter(adapterComponents[position]);

            container.addView(child, 0);
            return child;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((LinearLayout) object);
        }

        @Override
        public int getCount() {
            return count;
        }

        public void setCount(int inCount) {
            this.count = inCount;
            notifyDataSetChanged();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == ((LinearLayout) arg1);
        }
    }
}
