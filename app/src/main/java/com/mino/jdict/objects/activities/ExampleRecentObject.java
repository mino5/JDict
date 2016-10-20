package com.mino.jdict.objects.activities;

import android.database.Cursor;

import com.mino.jdict.JDictApplication;
import com.mino.jdict.interfaces.IFactory;
import com.mino.jdict.models.basic.SearchValues;

import java.util.ArrayList;

public class ExampleRecentObject extends ARecentObject {

    private SearchValues mSearchValues = null;

    public ExampleRecentObject(int inDataType) {
        DataType.set(inDataType);
    }

    public ExampleRecentObject(boolean inIsGetNewResults) {
        DataType.set(1);
        IsGetNewResults.set(inIsGetNewResults);
    }

    @Override
    public void init(String inReb, String inKeb, int inEntSeq, String inHeaderText, SearchValues inSearchValues) {

        this.reb = inReb;
        this.keb = inKeb;
        this.ID.set(inEntSeq);
        this.Header.set(inHeaderText);
    }

    @Override
    public void bind(ASearchObject inASearchObject, SearchValues inSearchValues) {

        mSearchValues = inSearchValues;

        this.keb = inASearchObject.getSecond();
        this.reb = inASearchObject.getFirst();
        this.ID.set(inASearchObject.ID.get());
    }

    @Override
    public void getItems(int offset, ArrayList<ARecentObject> result, SearchValues inSearchValues) {
        Cursor cursor = JDictApplication.getDatabaseHelper().getWord().getRecentWords(offset, DataType.get());
        JDictApplication.getDatabaseHelper().getWord().getDataFromCursorRecent((ArrayList<ARecentObject>) (ArrayList<?>) result, cursor, inSearchValues, new ExampleRecentObjectFactory());
    }

    @Override
    public String getReb() {
        return reb;
    }

    @Override
    public String getKeb() {
        return keb;
    }


    @Override
    public String toString() {
        return "";
    }

    public static class ExampleRecentObjectFactory implements IFactory<ARecentObject> {

        public ExampleRecentObjectFactory() {
        }

        public ExampleRecentObject factory() {
            return new ExampleRecentObject(1);
        }
    }
}
