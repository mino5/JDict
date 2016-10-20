package com.mino.jdict.models.activities.vocabulary;

import android.database.Cursor;
import android.widget.ProgressBar;

import com.mino.jdict.JDictApplication;
import com.mino.jdict.R;
import com.mino.jdict.models.basic.SearchPart;
import com.mino.jdict.models.basic.SearchValues;
import com.mino.jdict.objects.activities.ListObject;
import com.mino.jdict.objects.activities.ASearchObject;

import java.util.ArrayList;

/**
 * Created by Dominik on 9/6/2015.
 */
public class VocabularyAdditionalSearchPart extends SearchPart {

    public VocabularyAdditionalSearchPart() {
        super(R.string.additional_words, 10, 2);
    }

    @Override
    public int getItems(String inSearchString, ArrayList<ASearchObject> result, SearchValues inSearchValues, ProgressBar inProgress) {

        Cursor cursor = JDictApplication.getDatabaseHelper().getWord().getEntryAlike(inSearchString, getLimit() + 1, getOffset(), mFirstCheck, mSelectedItemSpinner);
        int size = cursor.getCount();

        JDictApplication.getDatabaseHelper().getWord().getDataFromCursor((ArrayList<ListObject>) (ArrayList<?>) result, cursor, JDictApplication.getInstance().getGlobalSetting("KanaAsRomaji").contentEquals("True"), false, false, false);

        return size;
    }
}
