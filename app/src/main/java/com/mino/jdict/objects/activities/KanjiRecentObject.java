package com.mino.jdict.objects.activities;

import android.database.Cursor;

import com.mino.jdict.JDictApplication;
import com.mino.jdict.interfaces.IFactory;
import com.mino.jdict.models.basic.SearchValues;

import java.util.ArrayList;

public class KanjiRecentObject extends ARecentObject {

    public KanjiRecentObject() {
        DataType.set(1);
    }

    public KanjiRecentObject(int inDataType) {
        DataType.set(inDataType);
    }

    public KanjiRecentObject(boolean inIsGetNewResults) {
        DataType.set(1);
        IsGetNewResults.set(inIsGetNewResults);
    }

    @Override
    public void init(String inReb, String inKeb, int inEntSeq, String inHeaderText, SearchValues inSearchValues) {

        this.reb = inReb;
        this.keb = inKeb;
        ID.set(inEntSeq);
        Header.set(inHeaderText);

        // nie ma co romanizować póki co
        mRomajiText = reb;
        mRomajiOnlyText = keb;
    }

    @Override
    public void bind(ASearchObject inASearchObject, SearchValues inSearchValues) {

        mRomajiMode = JDictApplication.getInstance().getGlobalSetting("KanaAsRomaji").contentEquals("True");

        this.keb = inASearchObject.getSecond();
        this.reb = inASearchObject.getFirst();
        this.ID.set(inASearchObject.ID.get());
    }

    @Override
    public void getItems(int offset, ArrayList<ARecentObject> result, SearchValues inSearchValues) {
        Cursor cursor = JDictApplication.getDatabaseHelper().getWord().getRecentWords(offset, DataType.get());
        mRomajiMode = JDictApplication.getInstance().getGlobalSetting("KanaAsRomaji").contentEquals("True");

        JDictApplication.getDatabaseHelper().getWord().getDataFromCursorRecent((ArrayList<ARecentObject>) (ArrayList<?>) result, cursor, inSearchValues, new KanjiRecentObjectFactory());
    }

    @Override
    public String toString() {
        return "";
    }

    public static class KanjiRecentObjectFactory implements IFactory<ARecentObject> {

        public KanjiRecentObjectFactory() {
        }

        public KanjiRecentObject factory() {
            return new KanjiRecentObject(2);
        }
    }
}
