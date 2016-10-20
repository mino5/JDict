package com.mino.jdict.logics.basic;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mino.jdict.JDictApplication;
import com.mino.jdict.R;
import com.mino.jdict.activities.ExtractActivity;
import com.mino.jdict.interfaces.ICacheableAdapter;
import com.mino.jdict.interfaces.IClearable;
import com.mino.jdict.interfaces.IDetails;
import com.mino.jdict.models.basic.LogicModel;
import com.mino.jdict.models.basic.OptionContainer;
import com.mino.jdict.models.basic.SearchLogicModel;
import com.mino.jdict.models.basic.SearchPart;
import com.mino.jdict.models.basic.SearchValues;
import com.mino.jdict.objects.activities.ARecentObject;
import com.mino.jdict.objects.activities.ASearchObject;
import com.mino.jdict.objects.activities.RecentObject;

import org.acra.ACRA;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;

import utils.database.JMDictHelper;
import utils.database.entry.Extract;
import utils.grammar.InputUtils;
import utils.other.StaticObjectContainer;

/**
 * Created by Mino on 2014-12-14.
 */
public class SearchLogic extends Logic implements IClearable {

    //region VARIABLES

    // wielkości offsetów dla recentWords
    private static final int RECENT_OFFSET_SIZE = 20;

    // obecnie wyszukiwany ciąg znaków
    private String mCurrentQuery;

    // offset recentWords
    private int mCurrentOffsetRecentWords = 0;

    private boolean mEmptyLocker;

    private ArrayAdapter<ASearchObject> mAdapter;
    private ArrayAdapter<ARecentObject> mAdapterRecent;

    private ArrayList<ASearchObject> objectList = new ArrayList<ASearchObject>();
    private ArrayList<ARecentObject> recentObjectList = new ArrayList<ARecentObject>();

    private ActionBar mActionBar;
    private ArrayList<Pair<OptionContainer, CheckBox>> mCheckBoxes;
    private ArrayList<Pair<OptionContainer, Spinner>> mSpinners;
    private Button mOKButton, mCancelButton;
    private ListView mListView;
    private TextView mHeader;
    private RelativeLayout mRecentView;
    private RelativeLayout mSearchOptionsLayout;

    private boolean mRecentWordsToRefresh = false;
    private boolean mSearchOptionsVisible = false;

    private LoadRecentWordsData mLoadRecentData;
    private ArrayList<LoadData> mLoadData = new ArrayList<LoadData>();

    private SearchValues mSearchValues;
    private SearchView mSearchView;
    private TextView mTextKana;
    private boolean mRecentMode = true;
    private boolean mGlobalOptionsMode = false;
    private ProgressBar mListHorizontalProgressBar = null;
    private RelativeLayout mListHorizontalProgressBarLayout = null;


    private SearchLogicModel mModel;
    //endregion

    //region INIT

    public SearchLogic() {
    }

    public void init(LogicModel inModel) {
        super.init(inModel);

        this.mModel = (SearchLogicModel) inModel;

        mAdapter = mModel.getAdapterFactory().factory(mModel.getContentView().getContext(), mModel.getAdapterResourceItem(), objectList);

        if (mModel.getAdapterRecentFactory() != null) {
            mAdapterRecent = mModel.getAdapterRecentFactory().factory(mModel.getContentView().getContext(), mModel.getAdapterRecentResourceItem(), recentObjectList);
            mRecentMode = true;

        } else {
            mRecentView = (RelativeLayout) mModel.getContentView().findViewById(mModel.getRecentView());
            mRecentMode = false;
        }

        mListView = (ListView) mModel.getContentView().findViewById(mModel.getList());
        mHeader = (TextView) mModel.getContentView().findViewById(mModel.getHeader());
        mSearchOptionsLayout = (RelativeLayout) mModel.getContentView().findViewById(mModel.getSearchModel().getSearchOptionsLayoutID());
        mOKButton = (Button) mModel.getContentView().findViewById(mModel.getSearchModel().getOKButtonID());
        mCancelButton = (Button) mModel.getContentView().findViewById(mModel.getSearchModel().getCancelButtonID());
        mListHorizontalProgressBar = (ProgressBar) mModel.getContentView().findViewById(R.id.ListHorizontalProgressBar);
        mListHorizontalProgressBarLayout = (RelativeLayout) mModel.getContentView().findViewById(R.id.ListHorizontalProgressBarLayout);

        if (mListHorizontalProgressBar != null) {
            mListHorizontalProgressBar.setProgress(100);
        }

        mSearchValues = new SearchValues();

        mGlobalOptionsMode = JDictApplication.getInstance().getGlobalSetting("SearchSettingsGlobal").contentEquals("True");

        if (!mGlobalOptionsMode) {
            mModel.clearValues();
        }

        ArrayList<Boolean> mCheckBoxValues = mModel.getCheckBoxValues(); // new ArrayList<Boolean>();
        ArrayList<Integer> mSpinnerValues = mModel.getSpinnerValues(); // new ArrayList<Integer>();

        mCheckBoxes = new ArrayList<Pair<OptionContainer, CheckBox>>();
        mSpinners = new ArrayList<Pair<OptionContainer, Spinner>>();

        for (int i = 0; i < mModel.getSearchModel().getOptionContainers().length; i++) {

            // checkboxy
            if (mModel.getSearchModel().getOptionContainers()[i].getControlType().isAssignableFrom(CheckBox.class)) {
                mCheckBoxes.add(new Pair<OptionContainer, CheckBox>(mModel.getSearchModel().getOptionContainers()[i], (CheckBox) mModel.getContentView().findViewById(mModel.getSearchModel().getOptionContainers()[i].getControlID())));

                if (!mModel.isInitialized()) {
                    mCheckBoxValues.add(false);
                }
            }

            // spinnery
            if (mModel.getSearchModel().getOptionContainers()[i].getControlType().isAssignableFrom(Spinner.class)) {
                mSpinners.add(new Pair<OptionContainer, Spinner>(mModel.getSearchModel().getOptionContainers()[i], (Spinner) mModel.getContentView().findViewById(mModel.getSearchModel().getOptionContainers()[i].getControlID())));
                if (!mModel.isInitialized()) {
                    mSpinnerValues.add(0);
                }
            }
        }

        mSearchValues.setCheckBoxValues(mCheckBoxValues);
        mSearchValues.setSpinnerValues(mSpinnerValues);
        setSearchOptions(true);
        mModel.setInitialized();

        // dla każdego regionu osobny Task
        for (int i = 0; i < mModel.getRegionsCount(); i++) {
            mLoadData.add(i, new LoadData(i, mHeader));
        }

        initialize();
    }

    //public SearchValues getSearchValues() { return mSearchValues; }

    public void initialize() {

        if (mRecentMode) {
            setRecentAdapter();
        } else {
            setRecentView();
        }

        mListView.setClickable(true);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                Object o = mListView.getItemAtPosition(position);

                if (o instanceof ASearchObject && ((ASearchObject) o).ID.get() > 0) {

                    ASearchObject listObj = (ASearchObject) o;

                    addNewRecentObject(listObj);

                    java.lang.Class<?> activityToGo = mModel.getDetailsActivityClass();

                    if (listObj instanceof IDetails && ((IDetails) listObj).getDetailsActivityClass() != null) {
                        activityToGo = ((IDetails) listObj).getDetailsActivityClass();
                    }

                    Intent i = new Intent(mModel.getBaseContext(), activityToGo);

                    i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    i.putExtra("SelectedObjectEntSeq", listObj.ID.get());
                    i.putExtra("ConjugatedQuery", mCurrentQuery);

                    if (listObj instanceof IDetails && ((IDetails) listObj).getObjectForContainer() != null) {
                        StaticObjectContainer.StaticObject = ((IDetails) listObj).getObjectForContainer();
                    } else {
                        StaticObjectContainer.StaticObject = listObj;
                    }

                    //Logic.refreshActivity(mModel.getActivity().getClass());
                    mModel.getContentView().getContext().startActivity(i);

                    hideKeyboard();

                } else if (o instanceof ARecentObject && ((ARecentObject) o).ID.get() > 0) {

                    ARecentObject recentObj = (ARecentObject) o;

                    Intent i = new Intent(mModel.getBaseContext(), mModel.getDetailsActivityClass());
                    i.putExtra("SelectedObjectEntSeq", recentObj.ID.get());
                    i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    mCurrentQuery = "";
                    StaticObjectContainer.StaticObject = recentObj;
                    mModel.getContentView().getContext().startActivity(i);

                    hideKeyboard();
                }
            }
        });

             /* Handle list View scroll events */
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                for (int i = 0; i < mLoadData.size(); i++) {
                    if (mLoadData.get(i).getStatus() == AsyncTask.Status.RUNNING) {
                        return;
                    }
                }

                setHeaderText(firstVisibleItem);
            }
        });


        View.OnClickListener okClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                hideOptionsMenu(R.anim.slideup);

                setSearchOptions(false);
            }
        };

        mOKButton.setOnClickListener(okClickListener);

        View.OnClickListener cancelClickListener = new View.OnClickListener() {
            public void onClick(View v) {

                hideOptionsMenu(R.anim.slideup);
                resetOptionsValues();
            }
        };

        mCancelButton.setOnClickListener(cancelClickListener);
        mHeaders.clear();
        mHeader.setText(mModel.getRecentText());

        initSearch();
    }

    private void setSearchOptions(boolean fromSearchValues) {
        for (int i = 0; i < mSearchValues.getCheckBoxValues().size(); i++) {

            if (mSearchValues.getCheckBoxValues().get(i) != mCheckBoxes.get(i).second.isChecked()) {

                if (fromSearchValues) {
                    mCheckBoxes.get(i).second.setChecked(mSearchValues.getCheckBoxValues().get(i));
                } else {
                    mSearchValues.getCheckBoxValues().set(i, mCheckBoxes.get(i).second.isChecked());
                }

                setNewOptionsValues(i, mSearchValues.getCheckBoxValues().get(i), mCheckBoxes.get(i).first);
            }
        }

        for (int i = 0; i < mSearchValues.getSpinnerValues().size(); i++) {

            if (mSearchValues.getSpinnerValues().get(i) != mSpinners.get(i).second.getSelectedItemPosition()) {

                if (fromSearchValues) {
                    mSpinners.get(i).second.setSelection(mSearchValues.getSpinnerValues().get(i));
                } else {
                    mSearchValues.getSpinnerValues().set(i, mSpinners.get(i).second.getSelectedItemPosition());
                }

                setNewOptionsValues(i, mSearchValues.getSpinnerValues().get(i), mSpinners.get(i).first);
            }
        }
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putString("currentQuery", mCurrentQuery);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {

        mCurrentQuery = savedInstanceState.getString("currentQuery");
        search(mCurrentQuery);
    }

    public JMDictHelper getDictHelper() {
        return JDictApplication.getDatabaseHelper();
    }

    private void setRecentView() {
        mListView.setVisibility(View.GONE);
        mRecentView.setVisibility(View.VISIBLE);
    }

    private void setHeaderText(int firstVisibleItem) {
        if (mHeaders.size() > 0) {


            for (int i = 0; i < mHeaders.size(); i++) {
                if (firstVisibleItem < mHeaders.get(i).first) {

                    if (i > 0) i--;

                    if (mHeaders.get(i).second != null && mHeaders.get(i).second.length() > 0) {
                        mHeader.setText(mHeaders.get(i).second);
                    }

                    return;
                }

                if (mHeaders.get(i).second != null && mHeaders.get(i).second.length() > 0) {
                    mHeader.setText(mHeaders.get(i).second);
                }
            }
        }
    }

    private void initSearch() {

        // nie załadowana baza
       /* if (mDictHelper == null) {
            createAndRunDatabaseTask();
        } else */
        if (mRecentMode) {

            // pokazujemy recenta o ile nie jest w trakcie pracy
            if (!(getRecentWordsData() != null && getRecentWordsData().getStatus() == AsyncTask.Status.RUNNING)) {

                mCurrentQuery = null;
                mCurrentOffsetRecentWords = 0;
                mAdapterRecent.clear();
                setRecentAdapter();
                createAndRunLoadRecentWordsData(true);
            }
        } else {
            if (mCurrentQuery != null && mCurrentQuery.length() > 0) {
                search(mCurrentQuery);
            }
        }
    }

    private void restoreActionBar() {
        mActionBar = mModel.getActionBar();
        mActionBar.setTitle("");
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);
        mActionBar.setDisplayShowHomeEnabled(true);
        mActionBar.setCustomView(R.layout.actionbar_layout);
        mActionBar.setDisplayShowCustomEnabled(true);
    }

    private void hideKeyboard() {
        InputMethodManager mgr = (InputMethodManager) (mModel.getContentView().getContext().getSystemService(Context.INPUT_METHOD_SERVICE));
        mgr.hideSoftInputFromWindow(mModel.getContentView().getWindowToken(), 0);
    }

    private void setNewOptionsValues(int i, Object setting, OptionContainer optionContainer) {


        for (Object anObjectList : objectList) {

            ((ASearchObject) anObjectList).setSearchSetting(i, setting);
        }

        for (int j = 0; j < mModel.getRegionsCount(); j++) {
            for (int k = 0; k < mModel.getRegion(j).getPartsCount(); k++) {
                mModel.getRegion(j).getPart(k).setSearchSetting(i, setting);
            }
        }

        mAdapter.notifyDataSetChanged();

        if (mRecentMode) {
            for (Object obj : recentObjectList) {

                if (obj instanceof RecentObject) {
                    ((RecentObject) obj).setSearchSetting(i, setting);
                }
            }

            mAdapterRecent.notifyDataSetChanged();
        }

        if (optionContainer.getRefreshOnSearch()) {

            if (mCurrentQuery != null && !mCurrentQuery.isEmpty()) {
                search(mCurrentQuery);
            }
        }
    }

    public void resume() {

        if (mSearchView != null) {
            mSearchView.setQueryHint(mModel.getTitle());
            mSearchView.setIconified(false);
        }

        super.resume();
    }

    public void clear(IClearable.ClearanceLevel inClearanceLevel) {

        mCurrentOffsetRecentWords = 0;
        killLoadDataTasks();

        if (mRecentMode) {
            mAdapterRecent.clear();
            mAdapterRecent.notifyDataSetChanged();
        }

        if (mAdapter instanceof ICacheableAdapter) {
            ((ICacheableAdapter) mAdapter).clearCache();
        }

        // dla każdego regionu osobny Task
        for (int i = 0; i < mModel.getRegionsCount(); i++) {
            mLoadData.add(i, new LoadData(i, mHeader));
        }

        if (inClearanceLevel == ClearanceLevel.Hard && mCurrentQuery != null && mCurrentQuery.length() > 0) {
            mAdapter.clear();
            mAdapter.notifyDataSetChanged();
            search(mCurrentQuery);
        }

        mRecentWordsToRefresh = true;
    }

    private void resetOptionsValues() {
        for (int i = 0; i < mSearchValues.getCheckBoxValues().size(); i++) {
            mCheckBoxes.get(i).second.setChecked(mSearchValues.getCheckBoxValues().get(i));
        }

        for (int i = 0; i < mSearchValues.getSpinnerValues().size(); i++) {
            mSpinners.get(i).second.setSelection(mSearchValues.getSpinnerValues().get(i));
        }
    }

    public boolean onBackPressed() {
        if (mSearchOptionsVisible) {

            hideOptionsMenu(R.anim.slideup);
            return false;
        }

        killLoadDataTasks();
        return true;
    }

    private void showOptionsMenu() {

        resetOptionsValues();
        Animation slide = AnimationUtils.loadAnimation(JDictApplication.getContext(), R.anim.slidedown);

        slide.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // Called when the Animation starts
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Called when the Animation ended
                // Since we are fading a View out we set the visibility
                // to GONE once the Animation is finished
                mSearchOptionsLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // This is called each time the Animation repeats
            }
        });

        mSearchOptionsLayout.startAnimation(slide);
        mSearchOptionsVisible = true;
    }

    private void hideOptionsMenu(int animRes) {
        Animation slide = AnimationUtils.loadAnimation(JDictApplication.getContext(), animRes);
        slide.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // Called when the Animation starts
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Called when the Animation ended
                // Since we are fading a View out we set the visibility
                // to GONE once the Animation is finished

                mSearchOptionsLayout.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // This is called each time the Animation repeats
            }
        });

        mSearchOptionsLayout.startAnimation(slide);
        mSearchOptionsVisible = false;
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        if (!mModel.getNavigationDrawerFragment().isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            mModel.getMenuInflater().inflate(R.menu.main, menu);
        }

        restoreActionBar();
        final View actionBarCustomView = mActionBar.getCustomView();

        mSearchView =
                (SearchView) actionBarCustomView.findViewById(R.id.search);

        ImageView mOptionsButton = (ImageView) actionBarCustomView.findViewById(R.id.optionsButton);


        View.OnClickListener searchOptionsListener = new View.OnClickListener() {
            public void onClick(View v) {

                if (!mSearchOptionsVisible) {
                    showOptionsMenu();
                } else {
                    hideOptionsMenu(R.anim.slideup);
                    resetOptionsValues();
                }
            }

        };

        mOptionsButton.setOnClickListener(searchOptionsListener);

        if (!mEmptyLocker && mCurrentQuery != null) {
            mSearchView.setQuery(mCurrentQuery, false);
        }

        mSearchView.setQueryHint(mModel.getTitle());

        View.OnClickListener clickListener = new View.OnClickListener() {
            public void onClick(View v) {

                if (mCurrentQuery == null || mCurrentQuery.isEmpty() && mRecentMode) {
                    setRecentAdapter();
                }

            }
        };

        SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mCurrentQuery = query;
                if (query.length() == 0) {
                    toggleIndeterminateSpinner(View.GONE);
                }
                search(query);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                mCurrentQuery = query;
                if (query.length() == 0) {
                    toggleIndeterminateSpinner(View.GONE);
                }
                search(query);

                return true;
            }
        };

        mSearchView.setOnQueryTextListener(listener);
        mSearchView.setOnSearchClickListener(clickListener);

        if (isActivityVisible()) {
            mSearchView.setIconified(false);
        }

        mTextKana = (TextView) mModel.getActivity().findViewById(R.id.textViewKana);

        if (mCurrentQuery != null) {
            setKanaText(mCurrentQuery);
        }

        if (mCurrentQuery != null && mCurrentQuery.length() > 0 && (mHeaders == null || mHeaders.size() == 0)) {
            toggleIndeterminateSpinner(View.VISIBLE);
        }

        return true;
    }

    protected boolean isActivityVisible() {
        if (this.mModel.getActivity() != null) {
            Class klass = this.mModel.getActivity().getClass();
            while (klass != null) {
                try {
                    Field field = klass.getDeclaredField("mResumed");
                    field.setAccessible(true);
                    Object obj = field.get(this.mModel.getActivity());
                    return (Boolean) obj;
                } catch (NoSuchFieldException exception1) {
//                Log.e(TAG, exception1.toString());
                } catch (IllegalAccessException exception2) {
//                Log.e(TAG, exception2.toString());
                }
                klass = klass.getSuperclass();
            }
        }
        return false;
    }
    //endregion

    //region DATABASEINIT

    /*
    public void createAndRunDatabaseTask() {
        new LoadDatabaseTask(mModel.getContentView().getContext()).execute(mModel.getContentView().getContext());
    }

    private class LoadDatabaseTask extends AsyncTask<Context, Void, JMDictHelper> {
        Context mContext;
        ProgressDialog mDialog;

        // Provide a constructor so we can get a Context to use to create
        // the ProgressDialog.
        LoadDatabaseTask(Context context) {
            super();
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(mContext);
            mDialog.setMessage(mModel.getContentView().getContext().getString(R.string.loadingdatabase));

            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.show();
        }

        @Override
        protected JMDictHelper doInBackground(Context... contexts) {
            // Copy database.
            JMDictHelper helper = new JMDictHelper(contexts[0]);
            helper.getReadableDatabase();
            return helper;
        }

        @Override
        protected void onPostExecute(JMDictHelper result) {
            super.onPostExecute(result);
            mDictHelper = result;

            try {
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            } catch (Exception ex) {
                ACRA.getErrorReporter().handleSilentException(ex);
            }

            clearRecentOffset();
            clearCurrentOffset();

            if (mRecentMode && (mLoadRecentData == null || mLoadRecentData.getStatus() != AsyncTask.Status.RUNNING)) {
                (mLoadRecentData = new LoadRecentWordsData(mContext, true)).execute();
            }
        }

    } */
    //endregion

    //region KANA
    private void setKanaText(String query) {
        if (mTextKana != null) {

            if (InputUtils.isStringAllRomaji(query)) {
                mTextKana.setText(InputUtils.getKana(query, InputUtils.KanaType.Hiragana));
            } else if (InputUtils.isStringAllKana(query)) {
                mTextKana.setText(InputUtils.getRomaji(query, true));

            } else {
                mTextKana.setText("");
            }

            Paint textPaint = mTextKana.getPaint();
            float width = textPaint.measureText(query + mTextKana.getText());

            final float densityMultiplier = mModel.getContentView().getResources().getDisplayMetrics().density;
            final float scaledPx = 230 * densityMultiplier;

            if (width > scaledPx) {
                mTextKana.setText("");
            }

        }
    }
    //endregion

    //region UTILS

    private void clearCurrentOffset() {

        for (int i = 0; i < mModel.getRegionsCount(); i++) {
            for (int j = 0; j < mModel.getRegion(i).getPartsCount(); j++) {
                mModel.getRegion(i).getPart(j).setOffset(0);
            }
        }
    }

    private void clearRecentOffset() {
        mCurrentOffsetRecentWords = 0;
    }

    private void toggleIndeterminateSpinner(int state) {
        // Indeterminate Progress Spinner
        ProgressBar progressSpinner = (ProgressBar) mModel.getActivity().findViewById(R.id.progress_spinner);

        if (progressSpinner != null) {
            switch (state) {
                case View.VISIBLE:
                    progressSpinner.setVisibility(View.VISIBLE);

                    if (mListHorizontalProgressBarLayout != null) {
                        //mListHorizontalProgressBar.setProgress(0);
                        mListHorizontalProgressBarLayout.setVisibility(View.VISIBLE);
                    }

                    break;
                case View.GONE:
                    progressSpinner.setVisibility(View.GONE);

                    if (mListHorizontalProgressBarLayout != null) {
                        //mListHorizontalProgressBar.setProgress(0);
                        mListHorizontalProgressBarLayout.setVisibility(View.GONE);
                    }

                    break;
            }
        }
    }

    private void killLoadDataTasks() {
        for (LoadData data : mLoadData) {
            killTask(data);
        }

        if (mModel.getActivity() instanceof ExtractActivity) {
            Extract.Interrupt = true;
        }
        //toggleIndeterminateSpinner(View.GONE);
    }

    private void killTask(AsyncTask task) {
        if (task != null && task.getStatus() == AsyncTask.Status.RUNNING) {
            try {
                task.cancel(true);
            } catch (Exception ex) {
                ACRA.getErrorReporter().handleSilentException(ex);
            }

            //toggleIndeterminateSpinner(View.GONE);
        }
    }

    private void getLoadDataTasks() {
        for (LoadData data : mLoadData) {
            getTask(data);
        }
    }

    private void getTask(AsyncTask task) {
        if (task != null && task.getStatus() == AsyncTask.Status.RUNNING) {
            try {
                task.get();
            } catch (Exception ex) {
                ACRA.getErrorReporter().handleSilentException(ex);
            }

            //toggleIndeterminateSpinner(View.GONE);
        }
    }
    //endregion

    //region RECENT
    public LoadRecentWordsData getRecentWordsData() {
        return mLoadRecentData;
    }

    public void createAndRunLoadRecentWordsData(boolean inMode) {

        (mLoadRecentData = new LoadRecentWordsData(mModel.getContentView().getContext(), inMode)).execute();
    }

    private void setRecentAdapter() {

        if (!mRecentMode) return;

        if (mRecentWordsToRefresh) {

            mRecentWordsToRefresh = false;

            if (getRecentWordsData() == null || getRecentWordsData().getStatus() != AsyncTask.Status.RUNNING) {

                mCurrentOffsetRecentWords = 0;
                mAdapterRecent.clear();
                mAdapterRecent.notifyDataSetChanged();

                createAndRunLoadRecentWordsData(false);
            }
        }

        mListView.setAdapter(mAdapterRecent);
    }

    public void addNewRecentObject(ASearchObject listObj) {

        ARecentObject recentObj;

        if (listObj instanceof IDetails) {
            recentObj = (ARecentObject) ((IDetails) listObj).getRTypeFactory().factory();
        } else {
            recentObj = mModel.getRTypeFactory().factory();
        }
        recentObj.bind(listObj, mSearchValues);

        if (!recentObj.getPureKeb().isEmpty() || !recentObj.getPureReb().isEmpty()) {

            Iterator itr = recentObjectList.iterator();
            while (itr.hasNext()) {

                int currentEntSeq = ((ARecentObject) itr.next()).ID.get();
                if (currentEntSeq == recentObj.ID.get()) {
                    itr.remove();
                }
            }

            if (recentObjectList != null && recentObjectList.size() > 0) {
                recentObjectList.add(1, recentObj);
            }

            new AddRecentWord().execute(recentObj);

            if (mRecentMode) {
                mAdapterRecent.notifyDataSetChanged();
            }
        }
    }

    public void getMoreAdditionalRecentWords() {

        if ((mLoadRecentData == null || mLoadRecentData.getStatus() != AsyncTask.Status.RUNNING)) {

            mCurrentOffsetRecentWords += RECENT_OFFSET_SIZE;
            mAdapterRecent.remove(mAdapterRecent.getItem(mAdapterRecent.getCount() - 1));
            mAdapterRecent.notifyDataSetChanged();

            createAndRunLoadRecentWordsData(false);
        }
    }

    private class AddRecentWord extends AsyncTask<ARecentObject, Void, Boolean> {

        @Override
        protected Boolean doInBackground(ARecentObject... params) {

            //if (mDictHelper != null) {
            ARecentObject obj = params[0];

            //obj.InsertToDatabase(mDictHelper);
            obj.InsertToDatabase(JDictApplication.getDatabaseHelper());
            //}

            return true;
        }
    }

    public class LoadRecentWordsData extends AsyncTask<Void, Void, ArrayList<ARecentObject>> {
        JMDictHelper db;
        ArrayList<ARecentObject> result;
        ARecentObject mGetNewItems;
        ProgressDialog mDialog;
        Context mContext;
        Boolean mProgressInform;
        Boolean mMoreItemsAvailable = false;

        public LoadRecentWordsData(Context inContext, Boolean inProgressInform) {
            super();
            mContext = inContext;
            mProgressInform = inProgressInform;
        }

        @Override
        protected ArrayList<ARecentObject> doInBackground(Void... params) {

            db = JDictApplication.getDatabaseHelper();
            result = new ArrayList<ARecentObject>();
            mGetNewItems = mModel.getRTypeFactory().factory();
            mGetNewItems.IsGetNewResults.set(true);

            mModel.getRTypeFactory().factory().getItems(mCurrentOffsetRecentWords, result, mSearchValues);

            if ((mCurrentOffsetRecentWords == 0 && result.size() == RECENT_OFFSET_SIZE + 1) || (mCurrentOffsetRecentWords > 0 && result.size() == RECENT_OFFSET_SIZE + 1)) {
                result.remove(result.size() - 1);
                mMoreItemsAvailable = true;
            }

            if (mCurrentOffsetRecentWords == 0) {

                ARecentObject rObj = mModel.getRTypeFactory().factory();
                rObj.Header.set(mModel.getRecentText()); //, mSearchValues);
                result.add(0, rObj);
            }

            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            killLoadDataTasks();

            toggleIndeterminateSpinner(View.VISIBLE);

            if (mProgressInform) {
                mDialog = new ProgressDialog(mContext);
                mDialog.setMessage(mModel.getContentView().getContext().getString(R.string.loadingadddata));
                mDialog.show();
            }

            if (mAdapter.getCount() > 0) {
                mAdapter.clear();
                mAdapterRecent.clear();
                mAdapterRecent.notifyDataSetChanged();
                mCurrentOffsetRecentWords = 0;
            }
        }

        protected void onPostExecute(ArrayList<ARecentObject> result) {

            if (mMoreItemsAvailable) {
                result.add(mGetNewItems);
            }

            mAdapterRecent.addAll(result);
            mAdapterRecent.notifyDataSetChanged();

            if (mProgressInform) {
                try {

                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                } catch (Exception ex) {
                    ACRA.getErrorReporter().handleSilentException(ex);
                }
            }

            // resume
            if (mCurrentQuery != null && !mCurrentQuery.isEmpty()) {
                searchData(mCurrentQuery, false);
            } else if (mCurrentOffsetRecentWords > 0) {
                mListView.post(new Runnable() {
                    @Override
                    public void run() {
                        mListView.smoothScrollToPosition(mListView.getCount());
                    }
                });

            }

            toggleIndeterminateSpinner(View.GONE);
        }
    }

    //endregion

    //region SEARCH
    private void search(String query) {

        getTask(mLoadRecentData);
        killLoadDataTasks();

        if (query.isEmpty()) {
            mEmptyLocker = true;

            if (mTextKana != null) {
                mTextKana.setText("");
            }

            mAdapter.clear();
            mAdapter.notifyDataSetChanged();

            if (mRecentMode && mListView.getAdapter() != mAdapterRecent) {
                setRecentAdapter();
            } else if (!mRecentMode) {
                setRecentView();
            }

            mHeaders.clear();
            mHeader.setText(mModel.getRecentText());

            if (mAdapter instanceof ICacheableAdapter) {
                ((ICacheableAdapter) mAdapter).clearCache();
            }


        } else {
            if (mListView.getAdapter() != mAdapter) {
                mListView.setAdapter(mAdapter);
            }

            if (!mRecentMode) {
                mListView.setVisibility(View.VISIBLE);
                mRecentView.setVisibility(View.GONE);
            }

            mEmptyLocker = false;
            setKanaText(query);

            if (mAdapter instanceof ICacheableAdapter) {
                ((ICacheableAdapter) mAdapter).clearCache();
            }

            searchData(query, true);
        }
    }

    private void fillItems(ArrayList<ASearchObject> result, boolean clear) {
        if (!mEmptyLocker) {

            if (clear) {
                mAdapter.clear();
            }

            mAdapter.addAll(result);

            if (mAdapter instanceof ICacheableAdapter) {
                ((ICacheableAdapter) mAdapter).clearCache();
            }

            mAdapter.notifyDataSetChanged();
        }
    }

    private void fillItemsAtPosition(ArrayList<ASearchObject> result, int position) {
        if (!mEmptyLocker && mAdapter.getCount() >= position) {

            for (ASearchObject item : result) {
                mAdapter.insert(item, position++);
            }

            mAdapter.notifyDataSetChanged();
        }
    }


    public void searchData(String query, boolean checkIfRecentDataIsLoading) {

        clearCurrentOffset();

        if (checkIfRecentDataIsLoading) {
            getTask(mLoadRecentData);
        }

        killLoadDataTasks();
        mLoadData.set(0, new LoadData(0, mHeader));

        killLoadDataTasks();
        mLoadData.get(0).execute(query);
    }

    public void getMoreAdditionalResults(int position) {

        getLoadDataTasks();
        ASearchObject item = mAdapter.getItem(position);
        SearchPart inPart = item.SearchPart.get();

        if (!inPart.getPreventOffsetChange()) {
            inPart.setOffset(inPart.getOffset() + inPart.getLimit());
        } else {
            inPart.setPreventOffsetChange(false);
        }

        mAdapter.remove(item);

        LoadData addMore = new LoadData(inPart, position, mHeader);
        addMore.execute(mCurrentQuery);
    }

    private enum LoadMode {Normal, AddMore}

    public class LoadData extends AsyncTask<String, Void, ArrayList<ASearchObject>> {
        String mSearchstring;
        ASearchObject mHeader, mGetNewItems;
        ArrayList<String> reb, keb, gloss;
        ArrayList<ASearchObject> result;

        JMDictHelper db;
        Resources mRes;
        int mPos, mGetMorePosition;
        LoadMode mMode;
        SearchPart mSearchPart;
        ASearchObject mEmptyHeader;
        TextView mMainHeader;
        String mHeaderTextToSet = null;

        public LoadData(int inPos, TextView inHeader) {
            mPos = inPos;
            mMode = LoadMode.Normal;
            mMainHeader = inHeader;
        }

        public LoadData(SearchPart inPart, int inGetMorePosition, TextView inHeader) {
            mSearchPart = inPart;
            mMode = LoadMode.AddMore;
            mGetMorePosition = inGetMorePosition;
            mPos = 0;
            mMainHeader = inHeader;
        }

        @Override
        protected void onPreExecute() {

            toggleIndeterminateSpinner(View.VISIBLE);
            mRes = mModel.getContentView().getResources();
        }


        @Override
        protected ArrayList<ASearchObject> doInBackground(String... params) {

            mSearchstring = params[0];
            db = JDictApplication.getDatabaseHelper();

            result = new ArrayList<ASearchObject>();

            if (db != null) {

                // wartości wspólne dla całego regionu -> co gdy wszystkie party są puste (czy wyświetlamy headera jakiegoś, a jeśli tak to jakiego)
                String textEmpty = mModel.getRegion(mPos).getTextEmpty();
                Boolean visibleWhenEmpty = mModel.getRegion(mPos).getVisibleWhenEmpty();
                Boolean allEmpty = true;

                // pusty string
                if (mSearchstring.isEmpty()) {
                    return result;
                }

                if (mMode == LoadMode.Normal) {

                    if (mModel.getRegion(mPos).getEmptyHeader() != null) {
                        mEmptyHeader = (ASearchObject) mModel.getRegion(mPos).getEmptyHeader();
                        mModel.getRegion(mPos).setEmptyHeader(null);
                    }

                    for (int i = 0; i < mModel.getRegion(mPos).getPartsCount(); i++) {

                        SearchPart part = mModel.getRegion(mPos).getPart(i);
                        int size = getItems(part);

                        allEmpty &= size == 0;
                    }

                    // wszystkie empty - jak pokazujemy Header to ma specjalny text
                    if (allEmpty && visibleWhenEmpty) {

                        mHeader = mModel.getOTypeFactory().factory();
                        mModel.getRegion(mPos).setEmptyHeader(mHeader);
                        result.add(0, mHeader);
                        mHeader.Header.set(Integer.toString(0) + " " + textEmpty + " " + mRes.getQuantityString(R.plurals.matches, 0, 0)); //, mSearchValues);
                        mHeaderTextToSet = mHeader.Header.get();
                    }
                } else if (mMode == LoadMode.AddMore) {
                    getItems(mSearchPart);
                }

            }

            return result;
        }

        private int getItems(SearchPart part) {

            if (part == null) return 0;

            // wartości dla konkretnego parta - limit, offset, text na headerze
            int offset = part.getOffset();
            boolean hasLimit = part.getHasLimit();
            int limit = part.getLimit();
            String headerText = mModel.getActivity().getString(part.getResText());
            int queryLength = mSearchstring.length();
            int size = 0;

            reb = new ArrayList<String>();
            keb = new ArrayList<String>();
            gloss = new ArrayList<String>();

            // wyświetlamy tylko getMore za pierwszym razem jeśli jest ustawione StartsOnQueryLength i nie przekraczamy tej wartości
            if (mMode == LoadMode.Normal && part.getStartsOnQueryLength() > queryLength) {
                part.setPreventOffsetChange(true);
                addGetNewItems(part);
            } else {
                part.setPreventOffsetChange(false);
                boolean headerAdded = false;

                // pierwsze zaczytanie -> dodajemy headera (lub getMore ale startujemy od 0)
                if (mMode == LoadMode.Normal || (part.getStartsOnQueryLength() > queryLength && part.getOffset() == 0)) {
                    mHeader = mModel.getOTypeFactory().factory();
                    result.add(mHeader);
                    headerAdded = true;
                }

                part.setOffset(offset);
                int sizeFromGet = part.getItems(mSearchstring, (ArrayList<ASearchObject>) result, mSearchValues, mListHorizontalProgressBar);
                size = sizeFromGet;

                // każdy part powinien obsłużyć limit + 1 żeby móc stwierdzić czy coś jeszcze jest
                if (hasLimit && sizeFromGet > limit) {
                    size--;
                    result.remove(result.size() - 1);
                    addGetNewItems(part);
                }

                size = size + part.getOffset();
                String quantityString = mRes.getQuantityString(R.plurals.matches, size, size);

                if (headerAdded) {
                    if (size > 0) {
                        //part.getRegion().setEmptyHeader(null);
                        mHeader.Header.set(Integer.toString(size) + " " + headerText + " " + quantityString);//, mSearchValues);
                        //mHeaderTextToSet = mHeader.getHeader();
                    } else {
                        result.remove(mHeader);
                    }
                } else if (size > 0) { // getMore - uaktualniamy Header
                    //part.getRegion().setEmptyHeader(null);

                    if (mGetMorePosition - part.getOffset() - 1 >= 0) {

                        if (mAdapter.getCount() > mGetMorePosition - part.getOffset() - 1) {
                            mHeader = mAdapter.getItem(mGetMorePosition - part.getOffset() - 1);
                            mHeader.Header.set(Integer.toString(size) + " " + headerText + " " + quantityString); //, mSearchValues);
                            mHeaderTextToSet = mHeader.Header.get();
                        }
                    }
                }
            }

            return size;
        }

        private void addGetNewItems(SearchPart part) {
            mGetNewItems = mModel.getOTypeFactory().factory();
            mGetNewItems.IsGetNewItems.set(true);
            mGetNewItems.SearchPart.set(part);
            result.add(mGetNewItems);
        }

        protected void onPostExecute(ArrayList<ASearchObject> result) {

            if (mPos > 0) {
                for (int i = 0; i < mPos; i++) {
                    if (mLoadData.size() > i) {
                        if (mLoadData.get(i).getStatus() == Status.RUNNING) {
                            return;
                        }
                    } else break;
                }
            }

            if (mMode == LoadMode.Normal) {

                if (mEmptyHeader != null) {
                    mAdapter.remove(mEmptyHeader);
                }

                fillItems(result, mPos == 0);

                // następny region
                if (mPos < mModel.getRegionsCount() - 1) {

                    mLoadData.set(mPos + 1, new LoadData(mPos + 1, mMainHeader));
                    mLoadData.get(mPos + 1).execute(mCurrentQuery);
                } else {
                    toggleIndeterminateSpinner(View.GONE);
                }

            } else {
                final int size = result.size();
                fillItemsAtPosition(result, mGetMorePosition);
                mListView.post(new Runnable() {
                    @Override
                    public void run() {
                        mListView.smoothScrollToPosition(mGetMorePosition + size);
                    }
                });

                toggleIndeterminateSpinner(View.GONE);
            }

            if (mHeaderTextToSet != null) {
                mMainHeader.setText(mHeaderTextToSet);
            }

            mHeaders.clear();

            for (int i = 0; i < mAdapter.getCount(); i++) {
                if (mAdapter.getItem(i).Header.get() != null && mAdapter.getItem(i).Header.get().length() > 0) {
                    mHeaders.add(new Pair<Integer, String>(i, mAdapter.getItem(i).Header.get()));
                }
            }

            setHeaderText(mListView.getFirstVisiblePosition());
        }
    }

    private ArrayList<Pair<Integer, String>> mHeaders = new ArrayList<Pair<Integer, String>>();

//endregion
}
