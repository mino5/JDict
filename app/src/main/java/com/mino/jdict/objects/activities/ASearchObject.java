package com.mino.jdict.objects.activities;

import com.mino.jdict.models.basic.SearchPart;
import com.mino.jdict.models.basic.SearchValues;

import utils.other.properties.BoolProperty;
import utils.other.properties.IntProperty;
import utils.other.properties.Property;
import utils.other.properties.StringProperty;

/**
 * Created by Dominik on 10/15/2016.
 */

public abstract class ASearchObject {

    public final StringProperty Header = new StringProperty("", (new Runnable() {
        public void run() {
            ID.set(0);
        }
    }));
    public final StringProperty HeaderGetMore = new StringProperty("");
    public final BoolProperty IsGetNewItems = new BoolProperty();
    public final Property<SearchPart> SearchPart = new Property<SearchPart>();
    public final Property<com.mino.jdict.models.basic.SearchValues> SearchValues = new Property<SearchValues>();
    public final IntProperty ID = new IntProperty();
    public final IntProperty SelectedItemSpinner = new IntProperty();

    public void setSearchSetting(int i, Object value) {
    }

    public String getSecond() {
        return null;
    }

    public String getFirst() {
        return null;
    }
}
