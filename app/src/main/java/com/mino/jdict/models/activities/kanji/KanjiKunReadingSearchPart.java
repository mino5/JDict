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
public class KanjiKunReadingSearchPart extends SearchPart {

    public KanjiKunReadingSearchPart() {
        super(R.string.kunyomi, 10, 0);
    }

    @Override
    public int getItems(String inSearchString, ArrayList<ASearchObject> result, SearchValues inSearchValues, ProgressBar inProgress) {

        int size = 0;

        // KUN-READING
        Cursor cursor = JDictApplication.getDatabaseHelper().getKanji().getKanjiBySearchString(inSearchString, false, mSelectedItemSpinner, "'ja_kun'",
                getHasLimit() ? (" LIMIT " + String.valueOf(getLimit() + 1) + " OFFSET " + String.valueOf(getOffset()) + " ") : "", false);
        if (cursor != null) {

            final ArrayList<ASearchObject> kanjiResultKun = new ArrayList<ASearchObject>();
            final SearchPart part = this;

            AdapterHelper.GetKanjiBasicInfo(cursor, new AdapterHelper.KanjiBasicInfoInterface() {
                @Override
                public void onGotData(KanjiBaseObject res) {

                    KanjiObject obj = new KanjiObject(res);
                    obj.SearchPart.set(part);
                    kanjiResultKun.add(obj);
                }
            }, null);

            result.addAll(kanjiResultKun);
            size = kanjiResultKun.size();

        }

        return size;
    }
}
