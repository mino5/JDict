package com.mino.jdict.objects.basic;

import utils.other.properties.BoolProperty;

/**
 * Created by Dominik on 8/18/2015.
 */
public class NavigationDrawerObject {

    private String mDrawerStr;
    private int mIcon = 0;
    public final BoolProperty IsHeader = new BoolProperty();


    public NavigationDrawerObject(String inDrawerStr, int inIcon) {
        mDrawerStr = inDrawerStr;
        mIcon = inIcon;
    }

    public NavigationDrawerObject(String inDrawerStr) {
        mDrawerStr = inDrawerStr;
    }

    public String getDrawerStr() {
        return mDrawerStr;
    }

    public int getIcon() {
        return mIcon;
    }
}
