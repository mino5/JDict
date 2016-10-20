package com.mino.jdict.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mino.jdict.R;
import com.mino.jdict.objects.activities.BrowseObject;

import java.util.ArrayList;

public class BrowseAdapter extends ArrayAdapter<BrowseObject> {

    // declaring our ArrayList of items
    private ArrayList<BrowseObject> objects;

    private Context context;

    /* here we must override the constructor for ArrayAdapter
    * the only variable we care about now is ArrayList<Item> objects,
    * because it is the list of objects we want to display.
    */
    public BrowseAdapter(Context context, int textViewResourceId, ArrayList<BrowseObject> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.objects = objects;
    }

    /*
     * we are overriding the getView method here - this is what defines how each
     * list item will look.
     */
    public View getView(int position, View convertView, ViewGroup parent) {

        BrowseObject i = objects.get(position);

        // assign the view we are converting to a local variable
        View v = convertView;

        if (i != null) {

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (i.Name.get() != null) {

                v = inflater.inflate(R.layout.browse_list_item, null);

                TextView nameText = (TextView) v.findViewById(R.id.itemName);

                if (nameText != null) {
                    nameText.setText(i.Name.get());
                }

                TextView descriptionText = (TextView) v.findViewById(R.id.itemDescription);

                if (descriptionText != null) {
                    descriptionText.setText(i.Description.get());
                }

                TextView countText = (TextView) v.findViewById(R.id.counter);

                if (countText != null) {
                    countText.setText(i.Count.get());
                }

                TextView image = (TextView) v.findViewById(R.id.itemImage);
                if (image != null && i.IconText.get() != null) {
                    image.setText(i.IconText.get());
                }
            } else if (i.KanjiObject.get() != null) {
                v = inflater.inflate(R.layout.kanji_decomposition_item, null);
                TextView kanji = (TextView) v.findViewById(R.id.kanji);
                TextView kanjiMeaning = (TextView) v.findViewById(R.id.kanjiMeaning);
                TextView kanjiReading = (TextView) v.findViewById(R.id.kanjiReading);
                View showKanji = (View) v.findViewById(R.id.showKanji);

                AdapterHelper.DecompositionItem(i.KanjiObject.get(), inflater, kanji, kanjiMeaning, kanjiReading, showKanji, v);
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