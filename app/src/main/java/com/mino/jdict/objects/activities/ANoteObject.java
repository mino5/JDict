package com.mino.jdict.objects.activities;
import utils.other.properties.BoolProperty;
import utils.other.properties.StringProperty;

/**
 * Created by Dominik on 10/15/2016.
 */

public abstract class ANoteObject {
    public final StringProperty Note = new StringProperty();
    public final BoolProperty IsNote = new BoolProperty();
    public final BoolProperty IsInDatabase = new BoolProperty();

}
