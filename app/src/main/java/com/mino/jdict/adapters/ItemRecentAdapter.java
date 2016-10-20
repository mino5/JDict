package com.mino.jdict.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mino.jdict.R;
import com.mino.jdict.activities.MainActivity;
import com.mino.jdict.interfaces.IAdapterFactory;
import com.mino.jdict.logics.basic.Logic;
import com.mino.jdict.logics.basic.SearchLogic;
import com.mino.jdict.objects.activities.ARecentObject;
import com.mino.jdict.objects.activities.ASearchObject;
import com.mino.jdict.objects.activities.ListObject;
import com.mino.jdict.objects.activities.RecentObject;

import java.util.ArrayList;

public class ItemRecentAdapter extends ArrayAdapter<ARecentObject> {

    // declaring our ArrayList of items
    private ArrayList<ARecentObject> objects;

    private Context context;

    /* here we must override the constructor for ArrayAdapter
    * the only variable we care about now is ArrayList<Item> objects,
    * because it is the list of objects we want to display.
    */
    public ItemRecentAdapter(Context context, int textViewResourceId, ArrayList<ARecentObject> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.objects = objects;
    }

    /*
     * we are overriding the getView method here - this is what defines how each
     * list item will look.
     */
    public View getView(int position, View convertView, ViewGroup parent) {

        RecentObject i = (RecentObject) objects.get(position);

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

                // sticky Header - problem gdy zescrolluje się do góry wtedy przez chwile widać kawałek headera pod spodem
                if (position == 0)
                    v.setVisibility(View.INVISIBLE);

            } else if (i.IsGetNewResults.get()) {
                v = inflater.inflate(R.layout.header_item4, null);
                TextView moreResultsText = (TextView) v.findViewById(R.id.moreResults);
                moreResultsText.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        assert ((SearchLogic) Logic.get(((MainActivity) context))) != null;
                        ((SearchLogic) Logic.get(((MainActivity) context))).getMoreAdditionalRecentWords();
                    }
                });

            } else {

                v = inflater.inflate(R.layout.list_recent_item, null);

                TextView text = (TextView) v.findViewById(R.id.text);
                TextView textadd = (TextView) v.findViewById(R.id.textadd);

                if (text != null) {
                    text.setText(i.getKeb());
                }

                if (textadd != null) {
                    textadd.setText(i.getReb());
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

    public static class ItemRecentFactory implements IAdapterFactory<ArrayAdapter<ARecentObject>> {
        public ArrayAdapter<ARecentObject> factory(Context context, int textViewResourceId, ArrayList<?> objects) {
            return new ItemRecentAdapter(context, textViewResourceId, (ArrayList<ARecentObject>) objects);
        }
    }

}