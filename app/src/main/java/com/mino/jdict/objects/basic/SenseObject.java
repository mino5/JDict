package com.mino.jdict.objects.basic;

import android.util.Pair;

import com.mino.jdict.R;
import com.mino.jdict.objects.activities.ItemDetailObject;

import java.util.ArrayList;

/**
 * Created by Dominik on 7/7/2015.
 */
public class SenseObject {

    int mIDSense;

    // sense restriction
    ArrayList<String> mStagrList = new ArrayList<String>();
    ArrayList<String> mStagkList = new ArrayList<String>();

    // cross-reference
    ArrayList<Pair<String, Integer>> mXRefList = new ArrayList<Pair<String, Integer>>();

    // antonym
    ArrayList<Pair<String, Integer>> mAntList = new ArrayList<Pair<String, Integer>>();

    // part of speech
    ArrayList<String> mPosList = new ArrayList<String>();

    // translation
    ArrayList<String> mGlossList = new ArrayList<String>();

    // field of application
    ArrayList<String> mFieldList = new ArrayList<String>();

    // misc
    ArrayList<String> mMiscList = new ArrayList<String>();

    // source language
    ArrayList<Pair<String, String>> mSourceLanguage = new ArrayList<Pair<String, String>>();

    // regional dialects
    ArrayList<String> mDial = new ArrayList<String>();

    // s-inf additional info
    ArrayList<String> mSInf = new ArrayList<String>();

    // first example
    ArrayList<ItemDetailObject> mExamples = new ArrayList<ItemDetailObject>();

    private int mCount = 0;
    private int mOffsetSize = 0;

    public void setOffsetSize(int offsetSize) {
        mOffsetSize = offsetSize;
    }

    public int getOffsetSize() { return mOffsetSize; }

    public void addItemDetailObjects(ArrayList<ItemDetailObject> objectList, Boolean inIsColored) {
        // pos - type of word
        for (String wordType : this.getPosList()) {
            ItemDetailObject obj = new ItemDetailObject(wordType);
            obj.IsColored.set(inIsColored);
            objectList.add(obj);
        }

        // field - field of application
        for (String wordType : this.getFieldList()) {
            ItemDetailObject obj = new ItemDetailObject(wordType);
            obj.IsColored.set(inIsColored);
            objectList.add(obj);
        }

        // misc
        for (String wordType : this.getMiscList()) {
            ItemDetailObject obj = new ItemDetailObject(wordType);
            obj.IsColored.set(inIsColored);
            objectList.add(obj);
        }

        // dialect
        for (String wordType : this.getDialList()) {
            ItemDetailObject obj = new ItemDetailObject(wordType);
            obj.IsColored.set(inIsColored);
            objectList.add(obj);
        }

        // s_inf - additional info
        for (String sInf : this.getSInfList()) {
            ItemDetailObject objRef = new ItemDetailObject();
            objRef.IsColored.set(inIsColored);
            objRef.SInf.set(sInf);
            objectList.add(objRef);
        }

        // lsource - language source
        for (Pair<String, String> lsource : this.getLSourceList()) {
            ItemDetailObject objRef = new ItemDetailObject();
            objRef.Lsource.set(lsource);
            objRef.IsColored.set(inIsColored);
            objectList.add(objRef);
        }

        // references
        for (Pair<String, Integer> xref : this.getXRefList()) {

            ItemDetailObject objRef = new ItemDetailObject();
            objRef.Xref.set(xref);
            objRef.IsColored.set(inIsColored);
            objectList.add(objRef);
        }

        // antonyms
        for (Pair<String, Integer> ant : this.getAntList()) {

            ItemDetailObject objRef = new ItemDetailObject();
            objRef.Ant.set(ant);
            objRef.IsColored.set(inIsColored);
            objectList.add(objRef);
        }

        // stagr - sense only for selected reb
        for (String stagr : this.getStagrList()) {

            ItemDetailObject objRef = new ItemDetailObject();
            objRef.Stagr.set(stagr);
            objRef.IsColored.set(inIsColored);
            objectList.add(objRef);
        }

        // stagk - sense only for selected keb
        for (String stagk : this.getStagkList()) {

            ItemDetailObject objRef = new ItemDetailObject();
            objRef.Stagk.set(stagk);
            objRef.IsColored.set(inIsColored);
            objectList.add(objRef);
        }

        String gloss = this.getGloss();

        ItemDetailObject obj = new ItemDetailObject(gloss, R.drawable.unitedkingdomflag);
        obj.IsColored.set(inIsColored);
        objectList.add(obj);

        // first example
        if (this.getExamples() != null && this.getExampleCount() > 0) {

            ItemDetailObject objExample = this.getExamples().get(0);
            objExample.IsColored.set(inIsColored);
            objectList.add(objExample);

            if (this.getExampleCount() > 1) {
                ItemDetailObject objSearchForMore = new ItemDetailObject();
                objSearchForMore.IsNewResultsForSelectedSense.set(true);
                objSearchForMore.IsColored.set(inIsColored);
                objSearchForMore.SenseObject.set(this);
                objectList.add(objSearchForMore);
            }
        }
    }

    public SenseObject(int idSense) {
        this.mIDSense = idSense;
    }

    public void addStagr(String inStagr) {
        mStagrList.add(inStagr);
    }

    public void addStagk(String inStagk) {
        mStagkList.add(inStagk);
    }

    public void addXref(Pair<String, Integer> inRef) {
        mXRefList.add(inRef);
    }

    public void addAnt(Pair<String, Integer> inAnt) {
        mAntList.add(inAnt);
    }

    public void addPos(String inPos) {
        mPosList.add(inPos);
    }

    public void addGloss(String inGloss) {
        mGlossList.add(inGloss);
    }

    public void addField(String inField) {
        mFieldList.add(inField);
    }

    public void addMisc(String inMisc) {
        mMiscList.add(inMisc);
    }

    public void addLSource(Pair<String, String> inLSource) {
        mSourceLanguage.add(inLSource);
    }

    public void addExample(ItemDetailObject inObject) {
        mExamples.add(inObject);
        mCount++;
    }

    public int getExampleCount() {
        return mCount;
    }

    public void addDial(String inDial) {
        mDial.add(inDial);
    }

    public void addSInf(String inInf) {
        mSInf.add(inInf);
    }


    public ArrayList<Pair<String, Integer>> getXRefList() {
        return mXRefList;
    }

    public ArrayList<Pair<String, Integer>> getAntList() {
        return mAntList;
    }

    public ArrayList<String> getStagrList() {
        return mStagrList;
    }

    public ArrayList<String> getStagkList() {
        return mStagkList;
    }

    public ArrayList<String> getPosList() {
        return mPosList;
    }

    public ArrayList<String> getFieldList() {
        return mFieldList;
    }

    public ArrayList<String> getMiscList() {
        return mMiscList;
    }

    public ArrayList<Pair<String, String>> getLSourceList() {
        return mSourceLanguage;
    }

    public ArrayList<String> getDialList() {
        return mDial;
    }

    public ArrayList<String> getSInfList() {
        return mSInf;
    }

    public ArrayList<ItemDetailObject> getExamples() {
        return mExamples;
    }

    public int getSenseID() {
        return mIDSense;
    }

    public String getGloss() {
        String gloss = "";
        Boolean atLeastOne = false;
        for (String item : mGlossList) {
            gloss += item;
            gloss += " , ";
            atLeastOne = true;
        }
        if (atLeastOne) {
            gloss = gloss.substring(0, gloss.length() - 3);
        }

        return gloss;
    }
}
