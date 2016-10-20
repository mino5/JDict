package com.mino.jdict.objects.activities;

import android.os.Parcel;
import android.os.Parcelable;

import com.mino.jdict.interfaces.IKanjiObject;

import utils.other.properties.BoolProperty;
import utils.other.properties.IntProperty;
import utils.other.properties.Property;
import utils.other.properties.StringProperty;

/**
 * Created by Mino on 2014-10-05.
 */
public class KanjiDetailObject extends ANoteObject implements Parcelable, IKanjiObject {


    public static final Parcelable.Creator<KanjiDetailObject> CREATOR = new Creator<KanjiDetailObject>() {
        public KanjiDetailObject createFromParcel(Parcel source) {
            KanjiDetailObject mKanjiDetailObject = new KanjiDetailObject();

            int characterID = source.readInt();
            String kunyomi = source.readString();
            String onyomi = source.readString();
            String nanori = source.readString();
            String meaning = source.readString();
            String character = source.readString();

            mKanjiDetailObject.KanjiBaseObject.set(new KanjiBaseObject(characterID, kunyomi, onyomi, nanori, meaning, character));
            return mKanjiDetailObject;
        }

        @Override
        public KanjiDetailObject[] newArray(int i) {
            return new KanjiDetailObject[i];
        }
    };

    public final Property<KanjiBaseObject> KanjiBaseObject = new Property<com.mino.jdict.objects.activities.KanjiBaseObject>();
    public final Property<ListObject> ListObject = new Property<ListObject>();

    public final BoolProperty IsKunyomi = new BoolProperty();
    public final BoolProperty IsOnyomi = new BoolProperty();
    public final BoolProperty IsNanori = new BoolProperty();
    public final BoolProperty IsComponent = new BoolProperty();
    public final BoolProperty IsComponentKanji = new BoolProperty();
    public final BoolProperty IsSelected = new BoolProperty();
    public final BoolProperty IsEmpty = new BoolProperty();

    public final StringProperty SectionHeader = new StringProperty("");
    public final StringProperty Meaning = new StringProperty("");
    public final StringProperty CharacterSetType = new StringProperty("");
    public final StringProperty CharacterSetValue = new StringProperty("");
    public final StringProperty StrokeSvg = new StringProperty("");

    public final IntProperty Language = new IntProperty();
    public final IntProperty StrokeCount = new IntProperty();
    public final IntProperty JLPT = new IntProperty();
    public final IntProperty Grade = new IntProperty();
    public final IntProperty Freq = new IntProperty();


    public KanjiDetailObject(KanjiBaseObject inKanjiObj, Boolean isComponent, Boolean isComponentKanji) {

        KanjiBaseObject.set(inKanjiObj);
        IsComponent.set(isComponent);
        IsComponentKanji.set(isComponentKanji);
    }

    public KanjiDetailObject(String inMeaning, int language) {
        Meaning.set(inMeaning);
        Language.set(language);
    }

    public KanjiDetailObject() {
    }


    public KanjiDetailObject(ListObject inObj) {
        ListObject.set(inObj);
    }


    public Boolean getIsKanjiObject() {
        return KanjiBaseObject.get() != null && !IsComponent.get() && !IsComponentKanji.get();
    }

    public void setCharacterSet(String type, String value) {
        CharacterSetType.set(type);
        CharacterSetValue.set(value);
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(getKanjiObject().CharacterID.get());
        parcel.writeString(getKanjiObject().getKunyomi());
        parcel.writeString(getKanjiObject().getOnyomi());
        parcel.writeString(getKanjiObject().getNanori());
        parcel.writeString(getKanjiObject().Meaning.get());
        parcel.writeString(getKanjiObject().Character.get());
    }

    public KanjiBaseObject getKanjiObject() {
        return KanjiBaseObject.get();
    }

    public int describeContents() {
        return 0;
    }
}