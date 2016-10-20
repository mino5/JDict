package com.mino.jdict.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mino.jdict.R;
import com.mino.jdict.objects.activities.VocabularyObject;

import java.util.ArrayList;

public class VocabularyAdapter extends ArrayAdapter<VocabularyObject> {

    // declaring our ArrayList of items
    private ArrayList<VocabularyObject> objects;

    private Context context;

    /* here we must override the constructor for ArrayAdapter
    * the only variable we care about now is ArrayList<Item> objects,
    * because it is the list of objects we want to display.
    */
    public VocabularyAdapter(Context context, int textViewResourceId, ArrayList<VocabularyObject> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.objects = objects;
    }

    /*
     * we are overriding the getView method here - this is what defines how each
     * list item will look.
     */
    public View getView(int position, View convertView, ViewGroup parent) {

        VocabularyObject i = objects.get(position);

        // assign the view we are converting to a local variable
        View v = convertView;

        if (i != null) {

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (!i.HeaderText.get().isEmpty()) {

                v = inflater.inflate(R.layout.header_item, null);

                TextView header = (TextView) v.findViewById(R.id.foundCountText);

                if (header != null) {
                    header.setText(i.HeaderText.get());
                }

                if (i.IsSubLists.get()) {
                    header.setTypeface(null, Typeface.BOLD);
                }
            } else if (i.Element.get() != null) {
                if (i.Element.get().CharacterSeq.get() > 0 && i.Element.get().KanjiObject.get() != null) {
                    v = inflater.inflate(R.layout.vocabulary_kanji_item, null);
                    TextView kanji = (TextView) v.findViewById(R.id.vKanji);
                    TextView kanjiMeaning = (TextView) v.findViewById(R.id.vKanjiMeaning);
                    TextView kanjiReading = (TextView) v.findViewById(R.id.vKanjiReading);
                    View showKanji = (View) v.findViewById(R.id.vShowKanji);

                    AdapterHelper.DecompositionItem(i.Element.get().KanjiObject.get(), inflater, kanji, kanjiMeaning, kanjiReading, showKanji, v);

                    ImageView plusMinus = (ImageView) v.findViewById(R.id.kanjiMinus);
                    if (i.EditTypeProp.get() == VocabularyObject.EditType.Remove) {
                        plusMinus.setVisibility(View.VISIBLE);
                    } else {
                        plusMinus.setVisibility(View.GONE);
                    }

                } else if (i.Element.get().EntSeq.get() > 0 && i.Element.get().ListObject.get() != null) {
                    v = inflater.inflate(R.layout.vocabulary_word_item, null);

                    View layout = v.findViewById(R.id.vocab_list_item);
                    AdapterHelper.AddEntryToView(i.Element.get().ListObject.get(), layout);

                    ImageView plusMinus = (ImageView) v.findViewById(R.id.wordMinus);
                    if (i.EditTypeProp.get() == VocabularyObject.EditType.Remove) {
                        plusMinus.setVisibility(View.VISIBLE);
                    } else {
                        plusMinus.setVisibility(View.GONE);
                    }

                } else if (i.Element.get().SentenceSeq.get() > 0) {
                    v = inflater.inflate(R.layout.vocabulary_example_item, null);

                    TextView jp = (TextView) v.findViewById(R.id.vExampleJP);
                    TextView eng = (TextView) v.findViewById(R.id.vExampleEN);


                    if (jp != null) {
                        jp.setText(i.Element.get().ExampleObject.get().Detail.get().getReb(true));
                    }

                    if (eng != null) {
                        eng.setText(i.Element.get().ExampleObject.get().Detail.get().getKeb(true));
                    }

                    ImageView plusMinus = (ImageView) v.findViewById(R.id.exampleMinus);
                    if (i.EditTypeProp.get() == VocabularyObject.EditType.Remove) {
                        plusMinus.setVisibility(View.VISIBLE);
                    } else {
                        plusMinus.setVisibility(View.GONE);
                    }

                }
            } else {
                v = inflater.inflate(R.layout.vocabulary_list_item, null);


                ImageView folderView = (ImageView) v.findViewById(R.id.folderView);
                ImageView plusMinus = (ImageView) v.findViewById(R.id.minus);

                TextView folderName = (TextView) v.findViewById(R.id.folderName);
                TextView folderCounter = (TextView) v.findViewById(R.id.folderCounter);


                if (folderName != null) {
                    folderName.setText(i.Name.get());
                }

                VocabularyObject.EditType mode = i.EditTypeProp.get();

                switch (mode) {
                    case Add:
                        folderView.setVisibility(View.GONE);
                        plusMinus.setVisibility(View.VISIBLE);
                        plusMinus.setImageResource(R.drawable.plus);
                        folderCounter.setVisibility(View.GONE);
                        break;

                    case Remove:
                        plusMinus.setVisibility(View.VISIBLE);
                        folderCounter.setVisibility(View.GONE);
                        plusMinus.setImageResource(R.drawable.minus);
                        break;

                    case NonEditable:

                        if (folderCounter != null) {
                            folderCounter.setText(" " + i.SublistCount.get() + " | " + i.EntryCount.get() + " ");
                        }

                        break;
                }

            }
        }


		/*
         * Recall that the variable position is sent in as an argument to this method.
		 * The variable simply refers to the position of the current object in the list. (The ArrayAdapter
		 * iterates through the list we sent it)
		 *
		 * Therefore, i refers to the current Item object.
		 */

        // the view must be returned to our activity
        return v;

    }
}