package com.mino.jdict.objects.activities;

import com.mino.jdict.JDictApplication;

import utils.grammar.InputUtils;
import utils.other.properties.BoolProperty;
import utils.other.properties.IntProperty;
import utils.other.properties.StringProperty;

/**
 * Created by Mino on 2014-10-05.
 */
public class KanjiBaseObject {

    private String mRomajiKunyomiText = "";
    private String mRomajiOnyomiText = "";
    private String mRomajiNanoriText = "";
    private String mKunyomi = "";
    private String mOnyomi = "";
    private String mNanori = "";

    public final IntProperty CharacterID = new IntProperty();
    public final StringProperty Meaning = new StringProperty("");
    public final StringProperty Character = new StringProperty("");
    public final BoolProperty ShowArrow = new BoolProperty();

    public KanjiBaseObject() {
    }

    public KanjiBaseObject(int inCharacterID, String inKunyomi, String inOnyomi, String inNanori, String inMeaning, String inCharacter) {

        mKunyomi = inKunyomi;
        mNanori = inNanori;
        mOnyomi = inOnyomi;

        Character.set(inCharacter);
        CharacterID.set(inCharacterID);
        Meaning.set(inMeaning);

        if (mKunyomi != null) {
            mRomajiKunyomiText = InputUtils.getRomaji(mKunyomi, true);
        }

        if (mOnyomi != null) {
            mRomajiOnyomiText = InputUtils.getRomaji(mOnyomi, true);
        }

        if (mNanori != null) {
            mRomajiNanoriText = InputUtils.getRomaji(mNanori, true);
        }
    }


    public String getKunyomi() {
        if (JDictApplication.getInstance().getGlobalSetting("KanaAsRomaji").contentEquals("True")) return mRomajiKunyomiText;
        else return mKunyomi;
    }

    public void setKunyomi(String kunyomi) {
        mKunyomi = kunyomi;
        if (mKunyomi != null) {
            mRomajiKunyomiText = InputUtils.getRomaji(mKunyomi, true);
        }
    }

    public String getOnyomi() {
        if (JDictApplication.getInstance().getGlobalSetting("KanaAsRomaji").contentEquals("True")) return mRomajiOnyomiText;
        else return mOnyomi;
    }

    public void setOnyomi(String onyomi) {
        this.mOnyomi = onyomi;
        if (mOnyomi != null) {
            mRomajiOnyomiText = InputUtils.getRomaji(mOnyomi, true);
        }
    }

    public String getNanori() {
        if (JDictApplication.getInstance().getGlobalSetting("KanaAsRomaji").contentEquals("True")) return mRomajiNanoriText;
        else return mNanori;
    }

    public void setNanori(String nanori) {
        this.mNanori = nanori;
        if (mNanori != null) {
            mRomajiNanoriText = InputUtils.getRomaji(mNanori, true);
        }
    }
}