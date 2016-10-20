package com.mino.jdict.objects.activities;

import com.mino.jdict.JDictApplication;
import com.mino.jdict.interfaces.ICountable;
import com.mino.jdict.interfaces.IFactory;
import com.mino.jdict.objects.basic.CountObject;
import com.mino.jdict.objects.basic.SenseObject;

import java.io.Serializable;
import java.util.ArrayList;

import utils.grammar.InputUtils;
import utils.other.properties.BoolProperty;
import utils.other.properties.IntProperty;
import utils.other.properties.Property;
import utils.other.properties.StringProperty;

/**
 * Created by Mino on 2014-07-28.
 */
public class ListObject extends ASearchObject implements ICountable, Serializable {

    private String keb, reb;
    private String rebOnly = "";
    private String mRomajiText = "";
    private String mRomajiOnlyText = "";
    private ArrayList<String> mKebList;
    private ArrayList<String> mRebList;
    private Boolean mRomajiMode = false;
    private int mSelectedItemSpinner;
    private ArrayList<SenseObject> mSenseList;

    public final BoolProperty IsPreloaded = new BoolProperty();
    public final IntProperty Length = new IntProperty();
    public final StringProperty Misc = new StringProperty("");
    public final StringProperty Query = new StringProperty();
    public final IntProperty JLPTLevel = new IntProperty();
    public final Property<InputUtils.WildCardMode> WildCard = new Property<InputUtils.WildCardMode>(InputUtils.WildCardMode.NoWildCard);
    public final BoolProperty IsCertified = new BoolProperty();
    public final BoolProperty IsCommon = new BoolProperty();
    public final IntProperty SenseID = new IntProperty();
    public final StringProperty CompoundsSectionCharacter = new StringProperty();
    public final BoolProperty IsConjugated = new BoolProperty();
    public final BoolProperty ShowArrow = new BoolProperty();
    public final StringProperty GrammarChain = new StringProperty("");
    public final StringProperty WordType = new StringProperty("");
    public final StringProperty Gloss = new StringProperty("");
    public final Property<CountObject> CountObject = new Property<CountObject>();
    public final StringProperty AddHeader = new StringProperty("");

    public ListObject() {
    }

    public ListObject(boolean inIsGetNewResults) {
        IsGetNewItems.set(inIsGetNewResults);
    }

    public ListObject(boolean inIsGetNewResults, int inCount, int inShown, String inShowString) {
        IsGetNewItems.set(inIsGetNewResults);
        CountObject.set(new CountObject(inShown, inCount, inShowString));
    }

    public ListObject(int inEntSeq, boolean isConjugated, int jlptLevel, String inWordType, String inMisc, int inLength, boolean inIsCommon, String inGrammarChain, boolean inShowArrow) {

        super();

        ID.set(inEntSeq);
        IsConjugated.set(isConjugated);
        JLPTLevel.set(jlptLevel);
        Misc.set(inMisc);
        WordType.set(inWordType);
        IsCommon.set(inIsCommon);
        this.mRomajiMode = JDictApplication.getInstance().getGlobalSetting("KanaAsRomaji").contentEquals("True");
        ShowArrow.set(inShowArrow);
        Length.set(inLength);
        GrammarChain.set(inGrammarChain);
    }

    public void setRebKebSense(ArrayList<String> rebList, ArrayList<String> kebList, ArrayList<SenseObject> senseList) {

        keb = "";
        reb = "";
        rebOnly = "";
        Gloss.set("");

        if (kebList == null) kebList = new ArrayList<String>();
        if (rebList == null) rebList = new ArrayList<String>();
        if (senseList == null) senseList = new ArrayList<SenseObject>();

        this.mKebList = kebList;
        this.mRebList = rebList;
        this.mSenseList = senseList;

        boolean atLeastOne = false;

        for (String item : kebList) {
            keb += item;
            keb += " , ";
            atLeastOne = true;
        }

        if (atLeastOne) {
            keb = keb.substring(0, keb.length() - 3);
            atLeastOne = false;
        }

        for (String item : rebList) {
            reb += item;
            reb += " , ";
            atLeastOne = true;
        }

        if (atLeastOne) {
            rebOnly = reb.substring(0, reb.length() - 3);
            reb = "「" + rebOnly + "」";
            atLeastOne = false;
        }


        for (SenseObject item : senseList) {
            Gloss.set(Gloss.get() + item.getGloss());
            atLeastOne = Gloss.get().length() > 0;
            Gloss.set(Gloss.get() + " , ");
            // atLeastOne = true;
        }

        if (atLeastOne) {
            Gloss.set(Gloss.get().substring(0, Gloss.get().length() - 3));
        }

        mRomajiText = InputUtils.getRomaji(reb, true);
        mRomajiOnlyText = InputUtils.getRomaji(rebOnly, true);
    }


    public CountObject getCountObject() {
        return CountObject.get();
    }

    @Override
    public void setSearchSetting(int i, Object value) {

        switch (i) {
            case 0:
                if (value instanceof Boolean) {
                } else if (value instanceof Integer) {
                    mSelectedItemSpinner = (Integer) value;
                }

                break;
        }
    }

    public int getKebListLen() {
        return mKebList.size();
    }

    public int getRebListLen() {
        return mRebList.size();
    }

    public String getFromKebList(int pos) {
        return mKebList.get(pos);
    }

    public String getFromRebList(int pos) {
        return mRebList.get(pos);
    }

    public String getKeb() {
        if (keb != null && !keb.equals("")) return keb;
        else if (rebOnly != null) {
            if (!mRomajiMode)
                return rebOnly;
            else return mRomajiOnlyText;
        } else return "";
    }

    public String getReb() {
        if (reb != null && keb != null && !keb.equals("")) {
            if (!mRomajiMode) {
                return reb;
            } else {
                return mRomajiText;
            }
        } else return "";
    }

    @Override
    public String getSecond() {
        if (keb != null && !keb.equals("")) return keb;
        else if (rebOnly != null) {
            return rebOnly;
        } else return "";
    }

    @Override
    public String getFirst() {
        if (reb != null && keb != null && !keb.equals("")) {
            return reb;
        } else return "";
    }

    public ArrayList<SenseObject> getSenseList() {
        return mSenseList;
    }

    public String getWordTypeFirst() {
        if (WordType.get().contains(";")) {
            String[] wordTypes = WordType.get().split(";");

            for (int i = 0; i < wordTypes.length; i++) {
                if (wordTypes[i].startsWith("v") || wordTypes[i].startsWith("adj")) {
                    return wordTypes[i];
                }
            }

            return WordType.get().substring(0, WordType.get().indexOf(';'));
        } else return WordType.get();
    }


    @Override
    public String toString() {
        return "";
    }

    public static class ListObjectFactory implements IFactory<ASearchObject> {
        public ListObject factory() {
            return new ListObject();
        }
    }
}
