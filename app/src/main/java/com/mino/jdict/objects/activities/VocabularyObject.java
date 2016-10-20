package com.mino.jdict.objects.activities;

import utils.other.properties.BoolProperty;
import utils.other.properties.IntProperty;
import utils.other.properties.Property;
import utils.other.properties.StringProperty;

import static com.mino.jdict.objects.activities.VocabularyObject.EditType.NonEditable;

/**
 * Created by Mino on 2015-04-25.
 */
public class VocabularyObject {

    public final StringProperty Name = new StringProperty("");
    public final BoolProperty IsGetNewResults = new BoolProperty();
    public final BoolProperty IsLearnGroup = new BoolProperty();
    public final BoolProperty IsSubLists = new BoolProperty();
    public final Property<EditType> EditTypeProp = new Property<EditType>(NonEditable);
    public final IntProperty ID_Vocablist = new IntProperty();
    public final IntProperty IDVocablist = new IntProperty();
    public final IntProperty EntryCount = new IntProperty();
    public final IntProperty SublistCount = new IntProperty();
    public final StringProperty CreationDate = new StringProperty();
    public final StringProperty HeaderText = new StringProperty("");
    public final Property<VocabularyObject> Parent = new Property<VocabularyObject>();
    public final Property<VocabularyElementObject> Element = new Property<VocabularyElementObject>();


    public VocabularyObject() {
    }

    public VocabularyObject(boolean inIsGetNewResults) {
        IsGetNewResults.set(inIsGetNewResults);
    }

    public VocabularyObject(String inHeaderText) {
        HeaderText.set(inHeaderText);
    }

    public VocabularyObject(boolean inIsGetNewResults, int inCount, int inShown, String inShowString) {
        IsGetNewResults.set(inIsGetNewResults);
        //CountObject mCountObject = new CountObject(inShown, inCount, inShowString);
    }

    public VocabularyObject(String inText, EditType inEditType) {
        EditTypeProp.set(inEditType);
        Name.set(inText);
    }

    public VocabularyObject(int ID_vocablist, int IDvocablist, String name, String creationDate, Boolean isLearnGroup, int entryCount, int sublistCount, EditType editType) {

        Name.set(name);
        ID_Vocablist.set(ID_vocablist);
        IDVocablist.set(IDvocablist);
        CreationDate.set(creationDate);
        IsLearnGroup.set(isLearnGroup);
        EntryCount.set(entryCount);
        SublistCount.set(sublistCount);
        EditTypeProp.set(editType);
    }

    public VocabularyObject(int character_seq, int sentence_seq, int ent_seq, String creationDate, int lastGuess, String lastShown, EditType editType) {

        Element.set(new VocabularyElementObject(character_seq, sentence_seq, ent_seq, creationDate, lastGuess, lastShown));
        EditTypeProp.set(editType);
    }

    public enum EditType {NonEditable, Remove, Add}
}
