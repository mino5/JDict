package com.mino.jdict.models.activities.kanji;

import android.database.Cursor;
import android.widget.ProgressBar;

import com.mino.jdict.JDictApplication;
import com.mino.jdict.R;
import com.mino.jdict.adapters.AdapterHelper;
import com.mino.jdict.models.basic.SearchPart;
import com.mino.jdict.models.basic.SearchValues;
import com.mino.jdict.objects.activities.KanjiBaseObject;
import com.mino.jdict.objects.activities.KanjiObject;
import com.mino.jdict.objects.activities.ASearchObject;

import java.util.ArrayList;

/**
 * Created by Dominik on 9/6/2015.
 */
public class KanjiOnReadingSearchPart extends SearchPart {

    public KanjiOnReadingSearchPart() {
        super(R.string.onyomi, 10, 0);
    }

    @Override
    public int getItems(String inSearchString, ArrayList<ASearchObject> result, SearchValues inSearchValues, ProgressBar inProgress) {

        // OPTIONS
        /*
        if (inSearchValues != null && inSearchValues.getCheckBoxValues().size() > 0) {
            mFirstCheck = (Boolean) inSearchValues.getCheckBoxValues().get(0);
        }

        if (inSearchValues != null && inSearchValues.getSpinnerValues().size() > 0) {
            mSelectedItemSpinner = (Integer) inSearchValues.getSpinnerValues().get(0);
        }
        */

        int size = 0;

        // ON-READING
        Cursor cursor = JDictApplication.getDatabaseHelper().getKanji().getKanjiBySearchString(inSearchString, false, mSelectedItemSpinner, "'ja_on'",
                getHasLimit() ? (" LIMIT " + String.valueOf(getLimit() + 1) + " OFFSET " + String.valueOf(getOffset()) + " ") : "", false);

        if (cursor != null) {

            final ArrayList<ASearchObject> kanjiResult = new ArrayList<ASearchObject>();
            final SearchPart part = this;

            AdapterHelper.GetKanjiBasicInfo(cursor, new AdapterHelper.KanjiBasicInfoInterface() {
                @Override
                public void onGotData(KanjiBaseObject res) {
                    KanjiObject obj = new KanjiObject(res);
                    obj.SearchPart.set(part);
                    kanjiResult.add(obj);
                }
            }, null);

            size = kanjiResult.size();
            result.addAll(kanjiResult);
        }

        return size;
    }
}
