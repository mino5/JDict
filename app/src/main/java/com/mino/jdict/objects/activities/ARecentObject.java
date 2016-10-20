package com.mino.jdict.objects.activities;

import com.mino.jdict.JDictApplication;
import com.mino.jdict.models.basic.SearchValues;

import java.util.ArrayList;

import utils.database.JMDictHelper;
import utils.other.properties.BoolProperty;
import utils.other.properties.IntProperty;
import utils.other.properties.StringProperty;

/**
 * Created by Dominik on 10/16/2016.
 */

public abstract class ARecentObject {
    public final IntProperty ID = new IntProperty();
    public final StringProperty Header = new StringProperty("", (new Runnable() {
        public void run() {
            mRomajiMode = JDictApplication.getInstance().getGlobalSetting("KanaAsRomaji").contentEquals("True");
            //ID.set(0);
        }
    }));

    public final BoolProperty IsGetNewResults = new BoolProperty();
    public final IntProperty DataType = new IntProperty();

    protected String reb, keb;
    protected Boolean mRomajiMode = false;
    protected String mRomajiText = "";
    protected String mRomajiOnlyText = "";

    public void setSearchSetting(int i, Object value) { }
    public void getItems(int offset, ArrayList<ARecentObject> result, SearchValues inSearchValues) { }
    public void bind(ASearchObject inASearchObject, SearchValues inSearchValues) { }
    public String getPureReb() {
        return reb;
    }
    public String getPureKeb() {
        return keb;
    }
    public void init(String inReb, String inKeb, int inID, String inHeaderText, SearchValues inSearchValues) { }

    public String getReb() {
        if (mRomajiMode) return mRomajiText;
        else return reb;
    }

    public String getKeb() {
        if (mRomajiMode && reb.isEmpty()) return mRomajiOnlyText;
        else return keb;
    }

    public void InsertToDatabase(JMDictHelper inDictHelper) {

        inDictHelper.getWord().insertRecentWordsV2(this);
    }
}
