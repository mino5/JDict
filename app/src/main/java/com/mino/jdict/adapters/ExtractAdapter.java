package com.mino.jdict.adapters;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mino.jdict.R;
import com.mino.jdict.activities.ExtractActivity;
import com.mino.jdict.interfaces.IAdapterFactory;
import com.mino.jdict.interfaces.ICacheableAdapter;
import com.mino.jdict.objects.activities.ASearchObject;
import com.mino.jdict.objects.activities.ExampleDetailObject;
import com.mino.jdict.objects.activities.ExampleDetailObjects;
import com.mino.jdict.objects.activities.ExtractObject;
import com.mino.jdict.objects.activities.ListObject;

import java.util.ArrayList;
import java.util.HashMap;

import utils.database.WordSearchState;
import utils.grammar.InputUtils;

public class ExtractAdapter extends ArrayAdapter<ASearchObject> implements ICacheableAdapter {

    // declaring our ArrayList of items
    private ArrayList<ASearchObject> objects;

    private Context context;
    private boolean firstExample = true;

    private static HashMap<Integer, View> Views = new HashMap<Integer, View>();

    /* here we must override the constructor for ArrayAdapter
    * the only variable we care about now is ArrayList<Item> objects,
    * because it is the list of objects we want to display.
    */
    public ExtractAdapter(Context context, int textViewResourceId, ArrayList<ASearchObject> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.objects = objects;
    }

    public void clearCache() {
        Views.clear();
    }

    /*
     * we are overriding the getView method here - this is what defines how each
     * list item will look.
     */
    public View getView(int position, View convertView, ViewGroup parent) {

        if (Views.containsKey(position)) {
            return Views.get(position);
        }

        ExtractObject i = (ExtractObject) objects.get(position);

        // assign the view we are converting to a local variable
        View v = convertView;

        int previousEntSeq = 0;

        if (position == 0) {
            firstExample = true;
        }

        if (i != null) {

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (i.Kanji.get() != null && i.Kanji.get().Detail.get() != null) {

                v = inflater.inflate(R.layout.kanji_decomposition_item, null);
                ImageView separatorTextView = (ImageView) v.findViewById(R.id.separatorTextView);
                separatorTextView.setVisibility(View.VISIBLE);

                TextView kanji = (TextView) v.findViewById(R.id.kanji);
                TextView kanjiMeaning = (TextView) v.findViewById(R.id.kanjiMeaning);
                TextView kanjiReading = (TextView) v.findViewById(R.id.kanjiReading);
                View showKanji = (View) v.findViewById(R.id.showKanji);

                AdapterHelper.DecompositionItem(i.Kanji.get().Detail.get(), inflater, kanji, kanjiMeaning, kanjiReading, showKanji, v);


            } else if (i.Word.get() != null) {
                v = inflater.inflate(R.layout.list_item, null);
                ImageView separatorTextView = (ImageView) v.findViewById(R.id.separatorTextView);
                separatorTextView.setVisibility(View.VISIBLE);

                AdapterHelper.AddEntryToView(i.Word.get(), v);

            } else if (i.IsSeparator.get()) {
                v = inflater.inflate(R.layout.extract_separator, null);
            } else if (i.Example.get() != null) {


                ExampleDetailObjects exampleObj = i.Example.get();

                v = inflater.inflate(R.layout.extract_detail_item, null);

                //FlowLayout.LayoutParams flowLP = new FlowLayout.LayoutParams(5, 5);
                LinearLayout ll = (LinearLayout) v.findViewById(R.id.extractDetailLayout);

                for (ExampleDetailObject example : exampleObj.ExampleDetailObjectList.get()) {

                    View v2 = inflater.inflate(R.layout.example_detail_item_word_container, null);
                    LinearLayout wordLL = (LinearLayout) v2.findViewById(R.id.exampleDetailLayoutWordContainer);

                    View dot = (View) v2.findViewById(R.id.exampleDotLine);
                    dot.setVisibility(View.VISIBLE);

                    if (example == null || example.getRebKebList() == null) continue;

                    for (int j = 0; j < example.getRebKebList().size(); j++) {

                        View v3 = inflater.inflate(R.layout.example_detail_item_kanji_reading, null);
                        //LinearLayout exampleLL = (LinearLayout) v3.findViewById(R.id.exampleLinearLayout);
                        TextView readingKanji = (TextView) v3.findViewById(R.id.exampleReadingKanji);
                        TextView readingKanjiRomaji = (TextView) v3.findViewById(R.id.exampleReadingRomaji);
                        Pair<String, String> rebKebPair = example.getRebKebList().get(j);

                        if (InputUtils.isStringAllKana(rebKebPair.first)) {
                            readingKanji.setText(rebKebPair.first);
                        } else {
                            readingKanji.setText("");
                        }

                        String romaji = AdapterHelper.getRomajiFromRebKebList(example, j);

                        readingKanjiRomaji.setText(romaji);

                        TextView kanji = (TextView) v3.findViewById(R.id.exampleKanji);
                        kanji.setText(rebKebPair.second.trim());

                        if (example.ListObj.get() != null && WordSearchState.isParticle(example.ListObj.get().WordType.get())) {
                            kanji.setTextColor(v.getResources().getColor(R.color.imired_light));
                        }

                        if (example.EntSeq.get() > 0) {

                            if (previousEntSeq != example.EntSeq.get() && previousEntSeq != 0) {

                                float scale = context.getResources().getDisplayMetrics().density;
                                int dpAsPixels = (int) (12 * scale + 0.5f);
                                v2.setPadding(dpAsPixels, 0, 0, 0);
                            }

                            previousEntSeq = example.EntSeq.get();
                            final ListObject lObj = example.ListObj.get();

                            v3.setClickable(true);
                            v3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    ((ExtractActivity) context).goToItemDetails(lObj);
                                }
                            });
                        } else {

                            previousEntSeq = -1;
                            readingKanji.setText("");

                            /*
                            if (firstExample) {

                                ll.setPadding(0, 0, 0, 5);
                            }
                            */

                            dot.setVisibility(View.GONE);
                        }

                        wordLL.addView(v3);
                    }

                    firstExample = false;

                    ll.addView(v2);

                }

                Views.put(position, v);

            } else if (!i.Header.get().isEmpty()) {
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

    public static class ExtractAdapterFactory implements IAdapterFactory<ArrayAdapter<ASearchObject>> {
        public ArrayAdapter<ASearchObject> factory(Context context, int textViewResourceId, ArrayList<?> objects) {
            return new ExtractAdapter(context, textViewResourceId, (ArrayList<ASearchObject>) objects);
        }
    }


}