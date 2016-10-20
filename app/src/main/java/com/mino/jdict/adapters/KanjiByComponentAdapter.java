package com.mino.jdict.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mino.jdict.R;
import com.mino.jdict.activities.KanjiByComponentActivity;
import com.mino.jdict.objects.basic.KanjiContainerObject;
import com.mino.jdict.objects.activities.KanjiDetailObject;

import java.util.ArrayList;

import utils.other.ExTextView;
import utils.other.FontHelper;

public class KanjiByComponentAdapter extends ArrayAdapter<KanjiContainerObject> {

    // declaring our ArrayList of items
    private ArrayList<KanjiContainerObject> objects;

    private Context context;

    /* here we must override the constructor for ArrayAdapter
    * the only variable we care about now is ArrayList<Item> objects,
    * because it is the list of objects we want to display.
    */
    public KanjiByComponentAdapter(Context context, int textViewResourceId, ArrayList<KanjiContainerObject> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.objects = objects;
    }

    /*
     * we are overriding the getView method here - this is what defines how each
     * list item will look.
     */
    public View getView(int position, View convertView, ViewGroup parent) {

        KanjiContainerObject i = objects.get(position);
        // assign the view we are converting to a local variable
        View v = convertView;

        if (i != null) {

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.kanjibydecomposition_item, null);

            final KanjiDetailObject[] list = i.getKanjiList();

            for (int j = 1; j <= 5; j++) {

                KanjiDetailObject obj = list[j - 1];
                if (obj != null) {

                    TextView kanji = null;

                    switch (j) {
                        case 1:
                            kanji = (TextView) v.findViewById(R.id.kanji1);
                            kanji.setVisibility(View.VISIBLE);

                            kanji.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    final KanjiDetailObject kdo = list[0];
                                    view.setBackgroundColor(Color.BLUE);

                                    if (kdo.IsComponent.get()) {
                                        ((KanjiByComponentActivity) context).GetKanjiByComponent(kdo);
                                    } else {
                                        ((KanjiByComponentActivity) context).GetKanjiDetails(kdo);
                                    }

                                    view.setBackgroundColor(Color.TRANSPARENT);

                                }
                            });

                            break;

                        case 2:
                            kanji = (TextView) v.findViewById(R.id.kanji2);
                            kanji.setVisibility(View.VISIBLE);
                            kanji.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    final KanjiDetailObject kdo = list[1];
                                    if (kdo.IsComponent.get()) {
                                        ((KanjiByComponentActivity) context).GetKanjiByComponent(kdo);
                                    } else {
                                        ((KanjiByComponentActivity) context).GetKanjiDetails(kdo);
                                    }
                                }
                            });

                            break;

                        case 3:
                            kanji = (TextView) v.findViewById(R.id.kanji3);
                            kanji.setVisibility(View.VISIBLE);
                            kanji.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    final KanjiDetailObject kdo = list[2];
                                    if (kdo.IsComponent.get()) {
                                        ((KanjiByComponentActivity) context).GetKanjiByComponent(kdo);
                                    } else {
                                        ((KanjiByComponentActivity) context).GetKanjiDetails(kdo);
                                    }
                                }
                            });

                            break;

                        case 4:
                            kanji = (TextView) v.findViewById(R.id.kanji4);
                            kanji.setVisibility(View.VISIBLE);
                            kanji.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    final KanjiDetailObject kdo = list[3];
                                    if (kdo.IsComponent.get()) {
                                        ((KanjiByComponentActivity) context).GetKanjiByComponent(kdo);
                                    } else {
                                        ((KanjiByComponentActivity) context).GetKanjiDetails(kdo);
                                    }
                                }
                            });

                            break;

                        case 5:
                            kanji = (TextView) v.findViewById(R.id.kanji5);
                            kanji.setVisibility(View.VISIBLE);
                            kanji.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    final KanjiDetailObject kdo = list[4];
                                    if (kdo.IsComponent.get()) {
                                        ((KanjiByComponentActivity) context).GetKanjiByComponent(kdo);
                                    } else {
                                        ((KanjiByComponentActivity) context).GetKanjiDetails(kdo);
                                    }
                                }
                            });

                            break;
                    }


                    if (obj.IsComponent.get()) {

                        String character = obj.getKanjiObject().Character.get();

                        if (character.isEmpty()) {
                            character = obj.getKanjiObject().getKunyomi();
                            ((ExTextView)kanji).setSpecialFont();
                            assert kanji != null;
                        }

                        assert kanji != null;
                        kanji.setText(character);


                        if (obj.IsSelected.get()) {
                            kanji.setTextColor(v.getResources().getColor(R.color.imired_light));
                        }

                        if (obj.IsEmpty.get()) {
                            kanji.setTextColor(Color.GRAY);
                        }

                    } else if (obj.IsComponentKanji.get()) {

                        assert kanji != null;
                        //ShowUtils.showKanji(kanji, obj.getKanjiObject().getCharacter());
                        kanji.setText(obj.getKanjiObject().Character.get());

                        //if (obj.getKanjiObject().getCharacterID() == 0) {

                            //((ExTextView)kanji).setSpecialFont();
                            //kanji.setText(obj.getKanjiObject().getCharacter());
                        //}
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