package com.mino.jdict.objects.activities;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Pair;

import com.mino.jdict.JDictApplication;
import com.mino.jdict.interfaces.ICountable;
import com.mino.jdict.interfaces.IKanjiObject;
import com.mino.jdict.interfaces.IRebKebObject;
import com.mino.jdict.objects.basic.CountObject;
import com.mino.jdict.objects.basic.SenseObject;

import java.util.List;

import utils.grammar.InputUtils;
import utils.other.properties.ArrayListPairProperty;
import utils.other.properties.BoolProperty;
import utils.other.properties.IntProperty;
import utils.other.properties.Property;
import utils.other.properties.StringProperty;

/**
 * Created by Mino on 2014-07-28.
 */
public class ItemDetailObject extends ANoteObject implements Parcelable, IKanjiObject, IRebKebObject, ICountable {

    private String reb;
    private String keb;

    public final StringProperty ItemType = new StringProperty("");
    public final StringProperty Gloss = new StringProperty("");
    public final StringProperty GrammarChain = new StringProperty("");
    public final StringProperty SectionHeader = new StringProperty("");
    public final StringProperty ConjugatedForm = new StringProperty("");
    public final StringProperty ConjugatedName = new StringProperty("");
    public final StringProperty ConjugatedQuery = new StringProperty("");
    public final StringProperty Stagk = new StringProperty();
    public final StringProperty Stagr = new StringProperty();
    public final StringProperty SInf = new StringProperty();

    public final BoolProperty IsCommon = new BoolProperty();
    public final BoolProperty IsCurrentGrammarForm = new BoolProperty();
    public final BoolProperty IsAlternative = new BoolProperty();
    public final BoolProperty IsMain = new BoolProperty();
    public final BoolProperty IsExample = new BoolProperty();
    public final BoolProperty ShowArrow = new BoolProperty();
    public final BoolProperty IsSenseSeparator = new BoolProperty();
    public final BoolProperty IsGetNewResults = new BoolProperty();
    public final BoolProperty IsColored = new BoolProperty();
    public final BoolProperty IsNewResultsForSelectedSense = new BoolProperty();

    public final IntProperty AlternativeNumber = new IntProperty();
    public final IntProperty AlternativeReading = new IntProperty();
    public final IntProperty Language = new IntProperty();
    public final IntProperty JLPTLevel = new IntProperty();
    public final IntProperty JLPTLevelReb = new IntProperty();
    public final IntProperty SentenceSeq = new IntProperty();

    public final Property<KanjiBaseObject> KanjiBaseObject = new Property<KanjiBaseObject>();
    public final ArrayListPairProperty<String, String> RebKebList = new ArrayListPairProperty<String, String>();
    public final Property<Pair<String, Integer>> Xref = new Property<Pair<String, Integer>>();
    public final Property<Pair<String, String>> Lsource = new Property<Pair<String, String>>();
    public final Property<Pair<String, Integer>> Ant = new Property<Pair<String, Integer>>();
    public final Property<CountObject> CountObject = new Property<com.mino.jdict.objects.basic.CountObject>();
    public final Property<SenseObject> SenseObject = new Property<com.mino.jdict.objects.basic.SenseObject>();

    //private List<Pair<String, String>> mRebKebList = new ArrayList<Pair<String, String>>();

    public static final Parcelable.Creator<ItemDetailObject> CREATOR = new Creator<ItemDetailObject>() {
        public ItemDetailObject createFromParcel(Parcel source) {
            ItemDetailObject mItemDetailObject = new ItemDetailObject();

            int characterID = source.readInt();
            String kunyomi = source.readString();
            String onyomi = source.readString();
            String nanori = source.readString();
            String meaning = source.readString();
            String character = source.readString();

            mItemDetailObject.KanjiBaseObject.set(new KanjiBaseObject(characterID, kunyomi, onyomi, nanori, meaning, character));
            return mItemDetailObject;
        }

        @Override
        public ItemDetailObject[] newArray(int i) {
            return new ItemDetailObject[i];
        }
    };


    public ItemDetailObject() {
    }

    public ItemDetailObject(boolean inIsGetNewResults, int inExamplesCount, int inExamplesShown) {
        IsGetNewResults.set(inIsGetNewResults);
        CountObject.set(new CountObject(inExamplesShown, inExamplesCount, "examples"));
    }

    public ItemDetailObject(String inReb, String inKeb, Boolean inShowArrow) {

        ShowArrow.set(inShowArrow);
        this.reb = inReb;
        this.keb = inKeb;

        RebKebList.get().clear();
        RebKebList.add(new Pair<String, String>(reb, keb));
    }

    public ItemDetailObject(String wordType) {
        ItemType.set(InputUtils.getWordTypeString(wordType));
    }

    public ItemDetailObject(String gloss, int language) {
        this.Gloss.set(gloss);
        this.Language.set(language);
    }

    public ItemDetailObject(Boolean isCommon) {
        IsCommon.set(isCommon);
    }

    public ItemDetailObject(int inJLPTLevel, int inJLPTLevelReb) {
        this.JLPTLevel.set(inJLPTLevel);
        this.JLPTLevelReb.set(inJLPTLevelReb);
    }

    public ItemDetailObject(KanjiBaseObject inKanjiBaseObject) {
        this.KanjiBaseObject.set(inKanjiBaseObject);
    }

    public void addRebKeb(String inReb, String inKeb) {
        RebKebList.add(new Pair<String, String>(inReb, inKeb));
    }


    public String getReb(boolean inIgnoreRomaji) {
        if (!inIgnoreRomaji && JDictApplication.getInstance().getGlobalSetting("KanaAsRomaji").contentEquals("True"))
            return InputUtils.getRomaji(reb, true);
        else return reb;
    }

    public String getKeb(boolean inIgnoreRomaji) {
        if (!inIgnoreRomaji && JDictApplication.getInstance().getGlobalSetting("KanaAsRomaji").contentEquals("True"))
            return InputUtils.getRomaji(keb, true);
        else return keb;
    }


    public boolean getIsKanjiObject() {
        return KanjiBaseObject.get() != null;
    }

    public CountObject getCountObject() {
        return CountObject.get();
    }

    public KanjiBaseObject getKanjiObject() {
        return KanjiBaseObject.get();
    }

    public void setExample(int sentence_seq) {
        SentenceSeq.set(sentence_seq);
        IsExample.set(true);
    }

    public List<Pair<String, String>> getRebKebList() {
        return RebKebList.get();
    }


    public void setConjugationItem(String inConjugationName, String inConjugatedForm, boolean inIsCurrentGrammarForm) {
        ConjugatedForm.set(inConjugatedForm);
        ConjugatedName.set(inConjugationName);
        IsCurrentGrammarForm.set(inIsCurrentGrammarForm);
    }

    public void setIsAlternative(Boolean isAlternative, int alternativeNumber, int alternativeReading) {
        IsAlternative.set(isAlternative);
        AlternativeNumber.set(alternativeNumber);
        AlternativeReading.set(alternativeReading);
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(getKanjiObject().CharacterID.get());
        parcel.writeString(getKanjiObject().getKunyomi());
        parcel.writeString(getKanjiObject().getOnyomi());
        parcel.writeString(getKanjiObject().getNanori());
        parcel.writeString(getKanjiObject().Meaning.get());
        parcel.writeString(getKanjiObject().Character.get());
    }

    public int describeContents() {
        return 0;
    }
}
