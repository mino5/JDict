package com.mino.jdict.logics.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import com.mino.jdict.JDictApplication;
import com.mino.jdict.R;
import com.mino.jdict.adapters.AdapterHelper;
import com.mino.jdict.interfaces.IKanjiObject;
import com.mino.jdict.objects.activities.KanjiBaseObject;
import com.mino.jdict.objects.activities.KanjiDetailObject;
import com.mino.jdict.objects.activities.ListObject;

import java.util.ArrayList;

import utils.database.JMDictHelper;
import utils.other.ClassObject;
import utils.other.StaticObjectContainer;

/**
 * Created by Dominik on 7/26/2015.
 */
public class KanjiComponentDetailsLogic extends KanjiBaseLogic {

    private int allCount = 0;
    private LoadKanjiWithComponent mLoadKanjiWithComponent = null;

    public void initialize() {

        mBaseObject = ((IKanjiObject) mActivity.getIntent().getParcelableExtra("PAR")).getKanjiObject();
        selectedCharacterSeq = mBaseObject.CharacterID.get();
        StaticObjectContainer.SetClassObject(this.hashCode(), new ClassObject(mBaseObject));
        fillDetails(false, false);
    }
    public void resume() {

        ClassObject mObj = StaticObjectContainer.GetClassObject(this.hashCode());

        if (mObj != null && mObj.getObject1() != null && mObj.getObject1() instanceof KanjiBaseObject) {
            mBaseObject = (KanjiBaseObject) mObj.getObject1();
            selectedCharacterSeq = mBaseObject.CharacterID.get();
        }

        if (mToRefresh) {
            mToRefresh = false;

            if (mLoadKanjiWithComponent == null || mLoadKanjiWithComponent.getStatus() != AsyncTask.Status.RUNNING) {
                fillDetails(false, false);
            }
        }

        super.resume();
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {

        selectedCharacterSeq = savedInstanceState.getInt("kanjiSeq");

        getBasicDetails();

        if (mListView == null || mListView.getCount() == 0) {
            fillDetails(false, false);
        }
    }

    public void onNewIntent(Intent intent) {

        mBaseObject = ((IKanjiObject) intent.getParcelableExtra("PAR")).getKanjiObject();
        selectedCharacterSeq = mBaseObject.CharacterID.get();

        fillDetails(false, false);
    }

    @Override
    public void getMore() {

        getMoreComponentKanjis();
    }

    @Override
    protected void getAdditionalData() {

        (mLoadKanjiWithComponent = new LoadKanjiWithComponent(mActivity)).execute();
    }

    private void getMoreComponentKanjis() {
        mCurrentOffset += 5;
        getAdditionalData();
    }

    private class LoadKanjiWithComponent extends AsyncTask<Void, Void, ArrayList<KanjiDetailObject>> {

        public static final int limit = 5;
        JMDictHelper db;
        Context mContext;

        public LoadKanjiWithComponent(Context inContext) {
            super();
            mContext = inContext;
        }

        @Override
        protected ArrayList<KanjiDetailObject> doInBackground(Void... params) {

            db = JDictApplication.getDatabaseHelper();
            final ArrayList<KanjiDetailObject> resultArray = new ArrayList<KanjiDetailObject>();

            Cursor cursor, cursorCount;

            String component = mBaseObject.Character.get();
            cursor = db.getKanji().getComponentKanji(component, limit + 1, mCurrentOffset);

            try {
                cursorCount = db.getKanji().getComponentKanjiCount(component);

                if (allCount == 0) {

                    if (cursorCount != null && cursorCount.getCount() > 0) {

                        int countIndex = cursorCount.getColumnIndex("Count");
                        allCount += cursorCount.getInt(countIndex);
                    }
                }

                if (cursorCount != null) {
                    cursorCount.close();
                }

                if (mCurrentOffset == 0 && cursor != null && cursor.getCount() > 0) {
                    ListObject meaningHeader = new ListObject();
                    meaningHeader.AddHeader.set(mContext.getResources().getString(R.string.component_kanjis));
                    resultArray.add(new KanjiDetailObject(meaningHeader));
                }

                AdapterHelper.GetKanjiBasicInfo(cursor, new AdapterHelper.KanjiBasicInfoInterface() {

                    int i = 1;

                    @Override
                    public void onGotData(KanjiBaseObject result) {

                        if (i <= limit) {
                            result.ShowArrow.set(true);
                            resultArray.add(new KanjiDetailObject(result, false, true));

                            i++;
                        }
                    }
                }, null);

                if (mCurrentOffset + limit < allCount) {
                    resultArray.add(new KanjiDetailObject(new ListObject(true, allCount, limit + mCurrentOffset, "kanji"))); //?
                }
            } finally {

                if (cursor != null) {
                    cursor.close();
                }
            }

            return resultArray;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected void onPostExecute(ArrayList<KanjiDetailObject> result) {

            if (mCurrentOffset > 0) {
                mAdapter.remove(mAdapter.getItem(mAdapter.getCount() - 1));
            }

            for (KanjiDetailObject obj : result) {
                mAdapter.add(obj);
            }

            mAdapter.notifyDataSetChanged();

            if (mCurrentOffset > 0)
                mListView.post(new Runnable() {
                    @Override
                    public void run() {
                        mListView.smoothScrollToPosition(mListView.getCount());
                    }
                });

        }
    }

}
