package com.mino.jdict.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mino.jdict.R;
import com.mino.jdict.activities.ExampleActivity;
import com.mino.jdict.interfaces.IAdapterFactory;
import com.mino.jdict.logics.basic.Logic;
import com.mino.jdict.logics.basic.SearchLogic;
import com.mino.jdict.objects.activities.ASearchObject;
import com.mino.jdict.objects.activities.ExampleObject;

import java.util.ArrayList;

public class ExampleAdapter extends ArrayAdapter<ASearchObject> {

    // declaring our ArrayList of items
    private ArrayList<ASearchObject> objects;

    private Context context;

    /* here we must override the constructor for ArrayAdapter
    * the only variable we care about now is ArrayList<Item> objects,
    * because it is the list of objects we want to display.
    */
    public ExampleAdapter(Context context, int textViewResourceId, ArrayList<ASearchObject> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.objects = objects;
    }

    /*
     * we are overriding the getView method here - this is what defines how each
     * list item will look.
     */
    public View getView(int position, View convertView, ViewGroup parent) {

        ExampleObject i = (ExampleObject) objects.get(position);

        // assign the view we are converting to a local variable
        View v = convertView;

        if (i != null) {

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (i.Detail.get() != null && i.Detail.get().getReb(true) != null && i.Detail.get().getKeb(true) != null) {

                v = inflater.inflate(R.layout.example_item, null);

                TextView jp = (TextView) v.findViewById(R.id.exampleJP);
                TextView eng = (TextView) v.findViewById(R.id.exampleEN);


                if (jp != null) {
                    jp.setText(i.Detail.get().getReb(true));
                }

                if (eng != null) {
                    eng.setText(i.Detail.get().getKeb(true));
                }

            } else if (i.IsGetNewItems.get()) {
                v = inflater.inflate(R.layout.header_item4, null);
                TextView moreResultsText = (TextView) v.findViewById(R.id.moreResults);
                final int pos = position;

                moreResultsText.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        assert ((SearchLogic) Logic.get(((ExampleActivity) context))) != null;
                        ((SearchLogic) Logic.get(((ExampleActivity) context))).getMoreAdditionalResults(pos);
                    }
                });

            }
            if (!i.Header.get().isEmpty()) {
                v = inflater.inflate(R.layout.header_item, null);

                TextView header = (TextView) v.findViewById(R.id.foundCountText);

                if (header != null) {
                    header.setText(i.Header.get());
                }

                // sticky Header - problem gdy zescrolluje się do góry wtedy przez chwile widać kawałek headera pod spodem
                if (position == 0)
                    v.setVisibility(View.INVISIBLE);

            } else if (!i.HeaderGetMore.get().isEmpty()) {
                v = inflater.inflate(R.layout.header_item2, null);

                TextView additionalHeader = (TextView) v.findViewById(R.id.additionalFoundText);

                if (additionalHeader != null) {
                    additionalHeader.setText(i.HeaderGetMore.get());
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

    public static class ExampleAdapterFactory implements IAdapterFactory<ArrayAdapter<ASearchObject>> {
        public ArrayAdapter<ASearchObject> factory(Context context, int textViewResourceId, ArrayList<?> objects) {
            return new ExampleAdapter(context, textViewResourceId, (ArrayList<ASearchObject>) objects);
        }
    }


}