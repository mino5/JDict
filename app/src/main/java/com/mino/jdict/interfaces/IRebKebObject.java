package com.mino.jdict.interfaces;

import android.util.Pair;

import java.util.List;

import utils.other.properties.ArrayListPairProperty;

/**
 * Created by Mino on 2014-11-11.
 */
public interface IRebKebObject {

    void addRebKeb(String inReb, String inKeb);

    List<Pair<String, String>> getRebKebList();
}
