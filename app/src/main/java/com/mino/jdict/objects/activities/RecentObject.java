package com.mino.jdict.objects.activities;

import android.database.Cursor;

import com.mino.jdict.JDictApplication;
import com.mino.jdict.interfaces.IFactory;
import com.mino.jdict.models.basic.SearchValues;

import java.util.ArrayList;

import utils.grammar.InputUtils;

/**
 * Created by Mino on 2014-07-28.
 */
public class RecentObject extends ARecentObject {

    public RecentObject() {
    }

    public RecentObject(int inDataType) {
        DataType.set(inDataType);
    }

    public RecentObject(boolean inIsGetNewResults) {
        IsGetNewResults.set(inIsGetNewResults);
    }

    @Override
    public void init(String inReb, String inKeb, int inID, String inHeaderText, SearchValues inSearchValues) {

        mRomajiMode = JDictApplication.getInstance().getGlobalSetting("KanaAsRomaji").contentEquals("True");
        this.reb = inReb;
        this.keb = inKeb;
        ID.set(inID);
        Header.set(inHeaderText);

        mRomajiText = InputUtils.getRomaji(reb, true);
        mRomajiOnlyText = InputUtils.getRomaji(keb, true);
    }

    @Override
    public void bind(ASearchObject inASearchObject, SearchValues inSearchValues) {

        mRomajiMode = JDictApplication.getInstance().getGlobalSetting("KanaAsRomaji").contentEquals("True");

        this.keb = inASearchObject.getSecond();
        this.reb = inASearchObject.getFirst();
        mRomajiText = InputUtils.getRomaji(reb, true);
        mRomajiOnlyText = InputUtils.getRomaji(keb, true);

        ID.set(inASearchObject.ID.get());
    }


    @Override
    public void getItems(int offset, ArrayList<ARecentObject> result, SearchValues inSearchValues) {
        Cursor cursor = JDictApplication.getDatabaseHelper().getWord().getRecentWords(offset, DataType.get());
        mRomajiMode = JDictApplication.getInstance().getGlobalSetting("KanaAsRomaji").contentEquals("True");

        JDictApplication.getDatabaseHelper().getWord().getDataFromCursorRecent((ArrayList<ARecentObject>) (ArrayList<?>) result, cursor, inSearchValues, new RecentObjectFactory());

    }

    @Override
    public String toString() {
        return "";
    }

    public static class RecentObjectFactory implements IFactory<ARecentObject> {

        public RecentObjectFactory() {
        }

        public RecentObject factory() {
            return new RecentObject(0);
        }

    }
}
