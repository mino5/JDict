package com.mino.jdict.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mino.jdict.R;
import com.mino.jdict.objects.basic.NavigationDrawerObject;

import java.util.ArrayList;

public class NavigationDrawerAdapter extends ArrayAdapter<NavigationDrawerObject> {

    // declaring our ArrayList of items
    private ArrayList<NavigationDrawerObject> objects;

    private Context context;

    /* here we must override the constructor for ArrayAdapter
    * the only variable we care about now is ArrayList<Item> objects,
    * because it is the list of objects we want to display.
    */
    public NavigationDrawerAdapter(Context context, int textViewResourceId, ArrayList<NavigationDrawerObject> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.objects = objects;
    }

    /*
     * we are overriding the getView method here - this is what defines how each
     * list item will look.
     */
    public View getView(int position, View convertView, ViewGroup parent) {

        NavigationDrawerObject i = objects.get(position);

        // assign the view we are converting to a local variable
        View v = convertView;

        if (i != null) {

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (i.IsHeader.get()) {
                v = inflater.inflate(R.layout.navigation_header_item, null);

                TextView header = (TextView) v.findViewById(R.id.navigationText);

                if (header != null) {
                    header.setText(i.getDrawerStr());
                }

            } else {
                v = inflater.inflate(R.layout.navigation_activity_item, null);

                TextView text1 = (TextView) v.findViewById(R.id.navigationActivityText);

                if (text1 != null) {
                    text1.setText(i.getDrawerStr());
                }

                if (i.getIcon() > 0) {
                    ImageView iv = (ImageView) v.findViewById(R.id.imageViewNavigation);
                    iv.setImageResource(i.getIcon());
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