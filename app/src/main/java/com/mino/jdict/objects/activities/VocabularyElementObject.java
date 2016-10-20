package com.mino.jdict.objects.activities;

import utils.other.properties.IntProperty;
import utils.other.properties.Property;
import utils.other.properties.StringProperty;

/**
 * Created by Mino on 2015-05-06.
 */
public class VocabularyElementObject {

    public IntProperty CharacterSeq = new IntProperty();
    public IntProperty SentenceSeq = new IntProperty();
    public IntProperty EntSeq = new IntProperty();
    public IntProperty LastGuess = new IntProperty();
    public StringProperty CreationDate = new StringProperty();
    public StringProperty LastShown = new StringProperty();
    public Property<ListObject> ListObject = new Property<ListObject>();
    public Property<KanjiBaseObject> KanjiObject = new Property<KanjiBaseObject>();
    public Property<ExampleObject> ExampleObject = new Property<ExampleObject>();

    public VocabularyElementObject(int character_seq, int sentence_seq, int ent_seq, String creationDate, int lastGuess, String lastShown) {

        CharacterSeq.set(character_seq);
        SentenceSeq.set(sentence_seq);
        EntSeq.set(ent_seq);
        CreationDate.set(creationDate);
        LastGuess.set(lastGuess);
        LastShown.set(lastShown);
    }
}
