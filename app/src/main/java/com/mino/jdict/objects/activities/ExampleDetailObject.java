package com.mino.jdict.objects.activities;

import android.util.Pair;

import com.mino.jdict.interfaces.IRebKebObject;

import java.util.List;

import utils.other.properties.ArrayListPairProperty;
import utils.other.properties.BoolProperty;
import utils.other.properties.IntProperty;
import utils.other.properties.Property;
import utils.other.properties.StringProperty;

/**
 * Created by Mino on 2014-07-28.
 */
public class ExampleDetailObject implements IRebKebObject {

    public final StringProperty Reb = new StringProperty();
    public final StringProperty Keb = new StringProperty();
    public final StringProperty ChangedKeb = new StringProperty();
    public final StringProperty Translation = new StringProperty();
    public final IntProperty EntSeq = new IntProperty();
    public final IntProperty IDSense = new IntProperty();
    public final Property<ListObject> ListObj = new Property<ListObject>();
    public final BoolProperty IsMain = new BoolProperty();
    public final BoolProperty IsCertified = new BoolProperty();
    public final ArrayListPairProperty<String, String> RebKebList = new ArrayListPairProperty<String, String>();

    public ExampleDetailObject() {
    }

    public ExampleDetailObject(int inEntSeq) {

        EntSeq.set(inEntSeq);
    }

    public ExampleDetailObject(String inTranslation) {

        Translation.set(inTranslation);
    }

    public ExampleDetailObject(int inEntSeq, String inReb, String inKeb, Boolean inCertified, int inIDSense) {
        EntSeq.set(inEntSeq);
        Reb.set(inReb);
        Keb.set(inKeb);
        IsCertified.set(inCertified);
        IDSense.set(inIDSense);
    }

    public ExampleDetailObject(int inEntSeq, String inReb, String inKeb, String inChangedKeb, Boolean inCertified, int inIDSense) {
        EntSeq.set(inEntSeq);
        Reb.set(inReb);
        Keb.set(inKeb);
        ChangedKeb.set(inChangedKeb);
        IsCertified.set(inCertified);
        IDSense.set(inIDSense);
    }

    public void addRebKeb(String inReb, String inKeb) {
        RebKebList.add(inReb, inKeb);
    }

    public List<Pair<String, String>> getRebKebList() {

        return RebKebList.get();
    }

}
