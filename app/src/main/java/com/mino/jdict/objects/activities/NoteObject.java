package com.mino.jdict.objects.activities;

import utils.other.properties.BoolProperty;
import utils.other.properties.IntProperty;
import utils.other.properties.Property;
import utils.other.properties.StringProperty;

/**
 * Created by Mino on 2015-04-25.
 */
public class NoteObject extends ANoteObject {

    public final StringProperty Header = new StringProperty("");
    public final BoolProperty IsGetNewResults = new BoolProperty();
    public final StringProperty LastEditDate = new StringProperty();
    public final Property<com.mino.jdict.objects.activities.ListObject> ListObject = new Property<com.mino.jdict.objects.activities.ListObject>();
    public final Property<KanjiBaseObject> KanjiObject = new Property<KanjiBaseObject>();
    public final Property<com.mino.jdict.objects.activities.ExampleObject> ExampleObject = new Property<com.mino.jdict.objects.activities.ExampleObject>();
    public final IntProperty EntSeq = new IntProperty();
    public final IntProperty SentenceSeq = new IntProperty();
    public final IntProperty CharacterSeq = new IntProperty();

    public NoteObject() {
    }

    public NoteObject(boolean inIsGetNewResults, int inCount, int inShown, String inShowString) {
        IsGetNewResults.set(inIsGetNewResults);
        //CountObject mCountObject = new CountObject(inShown, inCount, inShowString);
    }

    public NoteObject(int character_seq, int sentence_seq, int ent_seq, String lastEditDate, String note) {
        Note.set(note);
        CharacterSeq.set(character_seq);
        SentenceSeq.set(sentence_seq);
        EntSeq.set(ent_seq);
        LastEditDate.set(lastEditDate);
    }

}
