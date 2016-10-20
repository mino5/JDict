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
public class KanjiNanoriSearchPart extends SearchPart {

    public KanjiNanoriSearchPart() {
        super(R.string.nanori);
    }

    @Override
    public int getItems(String inSearchString, ArrayList<ASearchObject> result, SearchValues inSearchValues, ProgressBar inProgress) {

        // NANORI
        Cursor cursor = JDictApplication.getDatabaseHelper().getKanji().getKanjiBySearchStringNanori(inSearchString, mSelectedItemSpinner);

        final ArrayList<ASearchObject> kanjiResult2 = new ArrayList<ASearchObject>();
        final SearchPart part = this;

        AdapterHelper.GetKanjiBasicInfo(cursor, new AdapterHelper.KanjiBasicInfoInterface() {
            @Override
            public void onGotData(KanjiBaseObject res) {

                KanjiObject obj = new KanjiObject(res);
                obj.SearchPart.set(part);
                kanjiResult2.add(obj);
            }
        }, null);


        result.addAll(kanjiResult2);
        int size = kanjiResult2.size();

        return size;
    }
}
