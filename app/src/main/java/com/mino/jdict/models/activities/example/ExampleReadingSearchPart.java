package com.mino.jdict.models.activities.example;

import android.database.Cursor;
import android.widget.ProgressBar;

import com.mino.jdict.JDictApplication;
import com.mino.jdict.R;
import com.mino.jdict.models.basic.SearchPart;
import com.mino.jdict.models.basic.SearchValues;
import com.mino.jdict.objects.activities.ASearchObject;
import com.mino.jdict.objects.activities.ExampleObject;

import java.util.ArrayList;

/**
 * Created by Dominik on 9/6/2015.
 */
public class ExampleReadingSearchPart extends SearchPart {

    public ExampleReadingSearchPart() {
        super(R.string.reading, 10, 0);
    }

    @Override
    public int getItems(String inSearchString, ArrayList<ASearchObject> result, SearchValues inSearchValues, ProgressBar inProgress) {

        Cursor cursor = JDictApplication.getDatabaseHelper().getExample().getExamplesByReading(inSearchString, getOffset(), mFirstCheck, getLimit() + 1);
        int size = cursor.getCount();

        JDictApplication.getDatabaseHelper().getExample().getExampleDataFromCursor((ArrayList<ExampleObject>) (ArrayList<?>) result, cursor);

        return size;
    }
}
