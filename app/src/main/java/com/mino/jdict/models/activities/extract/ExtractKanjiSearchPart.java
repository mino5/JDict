package com.mino.jdict.models.activities.extract;

import android.database.Cursor;
import android.widget.ProgressBar;

import com.mino.jdict.JDictApplication;
import com.mino.jdict.R;
import com.mino.jdict.adapters.AdapterHelper;
import com.mino.jdict.models.basic.SearchPart;
import com.mino.jdict.models.basic.SearchValues;
import com.mino.jdict.objects.activities.ExtractObject;
import com.mino.jdict.objects.activities.KanjiBaseObject;
import com.mino.jdict.objects.activities.KanjiObject;
import com.mino.jdict.objects.activities.ASearchObject;

import java.util.ArrayList;

/**
 * Created by Dominik on 9/6/2015.
 */
public class ExtractKanjiSearchPart extends SearchPart {

    public ExtractKanjiSearchPart() {
        super(R.string.kanji);
    }

    @Override
    public int getItems(String inSearchString, ArrayList<ASearchObject> result, SearchValues inSearchValues, ProgressBar inProgress) {

        int size = 0;
        inProgress.setProgress(98);

        // KANJI
        Cursor cursor = JDictApplication.getDatabaseHelper().getKanji().getKanjiBySearchString(inSearchString, false, mSelectedItemSpinner, "'ja_kun'",
                getHasLimit() ? (" LIMIT " + String.valueOf(getLimit() + 1) + " OFFSET " + String.valueOf(getOffset()) + " ") : "", true);

        inProgress.setProgress(99);

        if (cursor != null) {

            final ArrayList<KanjiObject> kanjiResult = new ArrayList<KanjiObject>();
            final SearchPart part = this;

            AdapterHelper.GetKanjiBasicInfo(cursor, new AdapterHelper.KanjiBasicInfoInterface() {
                @Override
                public void onGotData(KanjiBaseObject res) {

                    //KanjiObject obj = new KanjiObject(res, mFirstCheck);
                    KanjiObject obj = new KanjiObject(res);
                    obj.SearchPart.set(part);
                    kanjiResult.add(obj);
                }
            }, null);

            for (KanjiObject obj : kanjiResult) {
                result.add(new ExtractObject(obj));
            }
            size = kanjiResult.size();
            inProgress.setProgress(100);
        }

        return size;
    }
}
