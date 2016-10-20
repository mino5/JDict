package com.mino.jdict.models.basic;

import android.app.ActionBar;
import android.view.View;
import android.widget.ArrayAdapter;

import com.mino.jdict.NavigationDrawerFragment;
import com.mino.jdict.activities.BaseActivity;
import com.mino.jdict.interfaces.IAdapterFactory;
import com.mino.jdict.interfaces.IFactory;
import com.mino.jdict.objects.activities.ARecentObject;
import com.mino.jdict.objects.activities.ASearchObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mino on 2014-12-17.
 */
public class SearchLogicModel extends LogicModel {

    private IFactory<ASearchObject> mOTypeFactory;
    private IFactory<ARecentObject> mRTypeFactory;
    private IAdapterFactory<ArrayAdapter<ASearchObject>> mAdapterClass;
    private IAdapterFactory<ArrayAdapter<ARecentObject>> mAdapterRecentClass;
    private java.lang.Class<?> mActivityClass;
    private java.lang.Class<?> mDetailsActivityClass;
    private NavigationDrawerFragment mNavigationDrawerFragment;

    private int mAdapterResourceItem;
    private int mAdapterRecentResourceItem;
    private int mList;
    private int mHeader;
    private int mRecentView;
    private String mRecentText;
    private SearchOptionsModel mSearchOptionsModel;

    private SearchRegion[] mSearchRegions;
    private static Map<java.lang.Class<?>, ArrayList<Boolean>> mCheckBoxValues = new HashMap<java.lang.Class<?>, ArrayList<Boolean>>();
    private static Map<java.lang.Class<?>, ArrayList<Integer>> mSpinnerValues = new HashMap<java.lang.Class<?>, ArrayList<Integer>>();
    private static ArrayList<java.lang.Class<?>> mClasses = new ArrayList<java.lang.Class<?>>();

    public Boolean isInitialized() { return mClasses.contains(mActivityClass); }

    public void setInitialized() { if (!isInitialized()) mClasses.add(mActivityClass); }

    public void clearValues() {
        mClasses.remove(mActivityClass);
        mCheckBoxValues.remove(mActivityClass);
        mSpinnerValues.remove(mActivityClass);
    }

    public ArrayList<Boolean> getCheckBoxValues() {

        if (!mCheckBoxValues.containsKey(mActivityClass)) {
            mCheckBoxValues.put(mActivityClass, new ArrayList<Boolean>());
        }

        return mCheckBoxValues.get(mActivityClass);
    }
    public ArrayList<Integer> getSpinnerValues() {

        if (!mSpinnerValues.containsKey(mActivityClass)) {
            mSpinnerValues.put(mActivityClass, new ArrayList<Integer>());
        }

        return mSpinnerValues.get(mActivityClass);
    }

    public SearchLogicModel(IFactory<ASearchObject> inOTypeFactory, IFactory<ARecentObject> inRTypeFactory, IAdapterFactory<ArrayAdapter<ASearchObject>> inAdapterClass,
                            IAdapterFactory<ArrayAdapter<ARecentObject>> inAdapterRecentClass, int inAdapterResourceItem, int inAdapterRecentResourceItem,
                            int inList, int inHeader, java.lang.Class<?> inActivityClass, java.lang.Class<?> inDetailsActivityClass, View inContentView, BaseActivity inActivity,
                            NavigationDrawerFragment inNavigationDrawerFragment, SearchOptionsModel inSearchModel, String inRecentText, SearchRegion[] inSearchRegions) {

        this.mOTypeFactory = inOTypeFactory;
        this.mRTypeFactory = inRTypeFactory;
        this.mAdapterClass = inAdapterClass;
        this.mAdapterRecentClass = inAdapterRecentClass;
        this.mDetailsActivityClass = inDetailsActivityClass;
        this.mContentView = inContentView;
        this.mNavigationDrawerFragment = inNavigationDrawerFragment;
        this.mAdapterResourceItem = inAdapterResourceItem;
        this.mAdapterRecentResourceItem = inAdapterRecentResourceItem;
        this.mList = inList;
        this.mHeader = inHeader;
        this.mActivity = inActivity;
        this.mSearchOptionsModel = inSearchModel;
        this.mRecentText = inRecentText;
        this.mSearchRegions = inSearchRegions;
        this.mActivityClass = inActivityClass;
    }

    public SearchLogicModel(IFactory<ASearchObject> inOTypeFactory, IAdapterFactory<ArrayAdapter<ASearchObject>> inAdapterClass, int inRecentView, int inAdapterResourceItem, int inAdapterRecentResourceItem,
                            int inList, int inHeader, java.lang.Class<?> inActivityClass, java.lang.Class<?> inDetailsActivityClass, View inContentView, BaseActivity inActivity,
                            NavigationDrawerFragment inNavigationDrawerFragment, SearchOptionsModel inSearchModel, String inRecentText, SearchRegion[] inSearchRegions) {

        this.mOTypeFactory = inOTypeFactory;
        this.mRTypeFactory = null;
        this.mAdapterClass = inAdapterClass;
        this.mAdapterRecentClass = null;
        this.mRecentView = inRecentView;
        this.mDetailsActivityClass = inDetailsActivityClass;
        this.mContentView = inContentView;
        this.mNavigationDrawerFragment = inNavigationDrawerFragment;
        this.mAdapterResourceItem = inAdapterResourceItem;
        this.mAdapterRecentResourceItem = inAdapterRecentResourceItem;
        this.mList = inList;
        this.mHeader = inHeader;
        this.mActivity = inActivity;
        this.mSearchOptionsModel = inSearchModel;
        this.mRecentText = inRecentText;
        this.mSearchRegions = inSearchRegions;
        this.mActivityClass = inActivityClass;
    }

    public SearchRegion getRegion(int regionNumber) {
        if (mSearchRegions.length > regionNumber)
            return mSearchRegions[regionNumber];
        else return null;
    }

    public int getRegionsCount() {
        return mSearchRegions.length;
    }

    public IFactory<ASearchObject> getOTypeFactory() {
        return mOTypeFactory;
    }

    public IFactory<ARecentObject> getRTypeFactory() {
        return mRTypeFactory;
    }

    public IAdapterFactory<ArrayAdapter<ASearchObject>> getAdapterFactory() {
        return mAdapterClass;
    }

    public IAdapterFactory<ArrayAdapter<ARecentObject>> getAdapterRecentFactory() {
        return mAdapterRecentClass;
    }

    public java.lang.Class<?> getDetailsActivityClass() {
        return mDetailsActivityClass;
    }

    public NavigationDrawerFragment getNavigationDrawerFragment() {
        return mNavigationDrawerFragment;
    }

    public int getAdapterResourceItem() {
        return mAdapterResourceItem;
    }

    public int getAdapterRecentResourceItem() {
        return mAdapterRecentResourceItem;
    }

    public int getList() {
        return mList;
    }

    public int getHeader() {
        return mHeader;
    }

    public int getRecentView() { return mRecentView; }

    public String getRecentText() {
        return mRecentText;
    }

    public ActionBar getActionBar() {
        return mActivity.getActionBar();
    }

    public CharSequence getTitle() {
        return mActivity.getSearchTitle();
    }

    public SearchOptionsModel getSearchModel() {
        return mSearchOptionsModel;
    }
}
