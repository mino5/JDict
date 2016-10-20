package com.mino.jdict.logics.activities;

import android.database.Cursor;
import android.os.AsyncTask;

import com.mino.jdict.JDictApplication;
import com.mino.jdict.R;
import com.mino.jdict.adapters.AdapterHelper;
import com.mino.jdict.objects.activities.BrowseObject;
import com.mino.jdict.objects.activities.KanjiBaseObject;

import java.util.ArrayList;

import utils.database.JMDictHelper;

/**
 * Created by Dominik on 8/23/2015.
 */
public class JLPTLogic extends BrowseLogic {

    public JLPTLogic() {
        mCurrentActionBarTitle = "JLPT";
        mType = BrowseType.JLPT;
    }

    protected void initResources() {

        mNameArray = mActivity.getResources().getStringArray(R.array.JLPTItems);
        mDescriptionArray = mActivity.getResources().getStringArray(R.array.JLPTDescriptions);
        mCounts = mActivity.getResources().getStringArray(R.array.JLPTCounts);
        mIconTexts = mActivity.getResources().getStringArray(R.array.JLPTIconTexts);
        mActionBarTitles = mActivity.getResources().getStringArray(R.array.JLPTActionBarHeaders);
    }

    protected void loadFolders() {

        initResources();

        for (int i = 0; i < 5; i++) {
            String name = mNameArray[i];
            String description = mDescriptionArray[i];
            String count = mCounts[i];
            String iconText = mIconTexts[i];
            BrowseObject obj = new BrowseObject(i, name, description, count, iconText);
            mObjectList.add(obj);
        }

        mAdapter.notifyDataSetChanged();
    }

    protected void getDetails(int pos) {

        mActionBarHeader.setText(mActionBarTitles[pos]);
        (new LoadKanji()).execute(pos);
    }


    private class LoadKanji extends AsyncTask<Integer, Void, ArrayList<BrowseObject>> {

        JMDictHelper db;

        public LoadKanji() {
            super();
        }

        @Override
        protected ArrayList<BrowseObject> doInBackground(Integer... params) {

            db = JDictApplication.getDatabaseHelper();
            final ArrayList<BrowseObject> resultArray = new ArrayList<BrowseObject>();
            int jlpt = params[0];

            Cursor cursorKanji = db.getKanji().getKanjiDetailJLPT(jlpt);

            try {
                if (cursorKanji.getCount() > 0) {

                    AdapterHelper.GetKanjiBasicInfo(cursorKanji, new AdapterHelper.KanjiBasicInfoInterface() {
                        @Override
                        public void onGotData(KanjiBaseObject result) {

                            result.ShowArrow.set(true);
                            resultArray.add(new BrowseObject(result));
                        }
                    }, null);

                }

            } finally {

                if (cursorKanji != null) {
                    cursorKanji.close();
                }
            }

            return resultArray;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mObjectList.clear();
        }

        protected void onPostExecute(ArrayList<BrowseObject> result) {

            for (BrowseObject obj : result) {
                mObjectList.add(obj);
            }

            mAdapter.notifyDataSetChanged();
        }
    }

}
