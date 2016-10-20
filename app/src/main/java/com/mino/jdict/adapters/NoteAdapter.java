package com.mino.jdict.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mino.jdict.R;
import com.mino.jdict.activities.NoteActivity;
import com.mino.jdict.objects.activities.NoteObject;

import java.util.ArrayList;

public class NoteAdapter extends ArrayAdapter<NoteObject> {

    // declaring our ArrayList of items
    private ArrayList<NoteObject> objects;

    private Context context;

    /* here we must override the constructor for ArrayAdapter
    * the only variable we care about now is ArrayList<Item> objects,
    * because it is the list of objects we want to display.
    */
    public NoteAdapter(Context context, int textViewResourceId, ArrayList<NoteObject> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.objects = objects;
    }

    /*
     * we are overriding the getView method here - this is what defines how each
     * list item will look.
     */
    public View getView(int position, View convertView, ViewGroup parent) {

        NoteObject i = objects.get(position);

        // assign the view we are converting to a local variable
        View v = convertView;

        if (i != null) {

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (!i.Header.get().isEmpty()) {
                v = inflater.inflate(R.layout.header_item, null);

                TextView header = (TextView) v.findViewById(R.id.foundCountText);

                if (header != null) {
                    header.setText(i.Header.get());
                }
            } else if (i.IsGetNewResults.get()) {
                v = inflater.inflate(R.layout.header_item4, null);
                TextView moreResultsText = (TextView) v.findViewById(R.id.moreResults);
                moreResultsText.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        ((NoteActivity) context).getMoreNotes();
                    }
                });

            } else {
                v = inflater.inflate(R.layout.list_note_item, null);

                TextView header = (TextView) v.findViewById(R.id.noteText);

                if (header != null) {
                    header.setText(i.Note.get());
                }

                if (i.CharacterSeq.get() > 0) {
                    LinearLayout kanjiLayout = (LinearLayout) v.findViewById(R.id.note_kanji_layout);
                    kanjiLayout.setVisibility(View.VISIBLE);

                    TextView text = (TextView) v.findViewById(R.id.textKanji);
                    TextView textadd = (TextView) v.findViewById(R.id.textKanjiAdd);

                    if (text != null) {
                        text.setText(i.KanjiObject.get().Character.get());
                    }

                    if (textadd != null) {
                        textadd.setText(i.KanjiObject.get().Meaning.get());
                    }


                } else if (i.EntSeq.get() > 0) {
                    LinearLayout entryLayout = (LinearLayout) v.findViewById(R.id.note_item_layout);
                    entryLayout.setVisibility(View.VISIBLE);

                    TextView text = (TextView) v.findViewById(R.id.text);
                    TextView textadd = (TextView) v.findViewById(R.id.textadd);

                    if (text != null) {
                        text.setText(i.ListObject.get().getKeb());
                    }

                    if (textadd != null) {
                        textadd.setText(i.ListObject.get().getReb());
                    }
                } else if (i.SentenceSeq.get() > 0) {
                    RelativeLayout exampleLayout = (RelativeLayout) v.findViewById(R.id.note_example_layout);
                    exampleLayout.setVisibility(View.VISIBLE);

                    TextView text = (TextView) v.findViewById(R.id.textExample);

                    if (text != null) {
                        text.setText(i.ExampleObject.get().Detail.get().getReb(true));
                    }

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