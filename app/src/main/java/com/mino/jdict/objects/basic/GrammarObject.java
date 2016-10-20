package com.mino.jdict.objects.basic;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Mino on 2014-08-20.
 */
public class GrammarObject {

    String mWordType;
    String mGrammarWordType;
    String mSimpleForm;
    String mChangedForm;
    String mGrammar;
    String mGrammarChain;
    Boolean mIsVisibleInDetails;

    public GrammarObject(String inWordType, String inChangedForm, String inSimpleForm, String inGrammar, boolean isVisibleInDetails) {
        this.mWordType = inWordType;
        this.mSimpleForm = inSimpleForm;
        this.mChangedForm = inChangedForm;
        this.mGrammar = inGrammar;
        this.mGrammarChain = inGrammar;
        this.mIsVisibleInDetails = isVisibleInDetails;
    }

    public GrammarObject(String inWordType, String inGrammarWordType, String inChangedForm, String inSimpleForm, String inGrammar, String inGrammarChain) {
        this.mWordType = inWordType;
        this.mGrammarWordType = inGrammarWordType;
        this.mSimpleForm = inSimpleForm;
        this.mChangedForm = inChangedForm;
        this.mGrammar = inGrammar;
        this.mGrammarChain = inGrammarChain;
    }


    public Boolean getIsVisibleInDetails() {
        return mIsVisibleInDetails;
    }

    public String  getWordType() {
        return mWordType;
    }

    public String getGrammarWordType() {
        return mGrammarWordType;
    }

    public String getSimpleForm() {
        return mSimpleForm;
    }

    public String getChangedForm() {
        return mChangedForm;
    }

    public String getGrammar() {
        return mGrammar;
    }

    public String getGrammarChain() {
        return mGrammarChain;
    }

    public void addToGrammarChain(String inGrammar) {
        if (mGrammarChain.contains(inGrammar)) return;

        if (mGrammarChain.isEmpty()) {
            mGrammarChain = inGrammar;
        } else {
            mGrammarChain = inGrammar + " < " + mGrammarChain;
        }
    }

    public static ArrayList<String> convertGrammarChainToList(String inGrammarChain) {
        ArrayList<String> grammarChainList = new ArrayList<String>();

        String[] split = inGrammarChain.split(" < ");
        Collections.addAll(grammarChainList, split);

        return grammarChainList;
    }


    public Boolean isVerb() {
        return mWordType.startsWith("v") || mWordType.equals("aux");
    }

    public Boolean isAdj() {
        return mWordType.startsWith("adj") || mWordType.equals("aux-adj");
    }
}
