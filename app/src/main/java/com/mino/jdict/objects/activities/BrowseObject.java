package com.mino.jdict.objects.activities;

import utils.other.properties.IntProperty;
import utils.other.properties.Property;
import utils.other.properties.StringProperty;

/**
 * Created by Dominik on 8/23/2015.
 */
public class BrowseObject {

    public final StringProperty Name = new StringProperty();
    public final StringProperty Description = new StringProperty();
    public final StringProperty Count = new StringProperty();
    public final IntProperty Pos = new IntProperty();
    public final StringProperty IconText = new StringProperty();
    public final Property<KanjiBaseObject> KanjiObject = new Property<KanjiBaseObject>();

    public BrowseObject(int inPos, String inName, String inDescription, String inCount, String inIcon) {

        Pos.set(inPos);
        Name.set(inName);
        Description.set(inDescription);
        Count.set(inCount);
        IconText.set(inIcon);
    }

    public BrowseObject(KanjiBaseObject inKanjiObject) {

        KanjiObject.set(inKanjiObject);
    }
}
