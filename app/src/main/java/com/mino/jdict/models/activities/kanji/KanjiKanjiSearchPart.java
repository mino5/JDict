package com.mino.jdict.models.activities.kanji;

import android.database.Cursor;
import android.widget.ProgressBar;

import com.mino.jdict.JDictApplication;
import com.mino.jdict.R;
import com.mino.jdict.adapters.AdapterHelper;
import com.mino.jdict.models.basic.SearchPart;
import com.mino.jdict.models.basic.SearchValues;
import com.mino.jdict.objects.activities.ASearchObject;
import com.mino.jdict.objects.activities.KanjiBaseObject;
import com.mino.jdict.objects.activities.KanjiObject;

import java.util.ArrayList;

/**
 * Created by Dominik on 9/6/2015.
 */
public class KanjiKanjiSearchPart extends SearchPart {

    public KanjiKanjiSearchPart() {
        super(R.string.kanji);
    }

    @Override
    public int getItems(String inSearchString, ArrayList<ASearchObject> result, SearchValues inSearchValues, ProgressBar inProgress) {

        int size = 0;

        // KANJI
        Cursor cursor = JDictApplication.getDatabaseHelper().getKanji().getKanjiBySearchString(inSearchString, false, mSelectedItemSpinner, "'ja_kun'",
                getHasLimit() ? (" LIMIT " + String.valueOf(getLimit() + 1) + " OFFSET " + String.valueOf(getOffset()) + " ") : "", true);

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

            result.addAll(kanjiResult);
            size = kanjiResult.size();

        }

        return size;
    }
}
