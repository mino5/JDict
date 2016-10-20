package com.mino.jdict.objects.activities;

import utils.other.properties.ArrayListProperty;
import utils.other.properties.BoolProperty;
import utils.other.properties.ListObjectProperty;
import utils.other.properties.StringProperty;

/**
 * Created by Mino on 2014-07-28.
 */
public class ExampleDetailObjects extends ANoteObject {

    public final BoolProperty IsMain = new BoolProperty();

    public final StringProperty Translation = new StringProperty();
    public final StringProperty SectionHeader = new StringProperty("");
    public final ListObjectProperty ListObject = new ListObjectProperty();
    public final ArrayListProperty<ExampleDetailObject> ExampleDetailObjectList = new ArrayListProperty<ExampleDetailObject>();

    public ExampleDetailObjects() {
    }

    public ExampleDetailObjects(String inTranslation) {
        Translation.set(inTranslation);
    }

    public ExampleDetailObjects(com.mino.jdict.objects.activities.ListObject inObj) {
        ListObject.set(inObj);
    }
}
