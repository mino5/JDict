package com.mino.jdict.objects.basic;

/**
 * Created by Mino on 2014-08-20.
 */
public class KanaObject {

    String mHiragana;
    String mKatakana;
    String mRomaji;

    public KanaObject(String inHiragana, String inKatakana, String inRomaji) {
        this.mHiragana = inHiragana;
        this.mKatakana = inKatakana;
        this.mRomaji = inRomaji;
    }


    public String getHiragana() {
        return mHiragana;
    }

    public String getKatakana() {
        return mKatakana;
    }

    public String getRomaji() {
        return mRomaji;
    }
}
