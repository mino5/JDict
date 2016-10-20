package com.mino.jdict.adapters;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mino.jdict.R;
import com.mino.jdict.activities.ExampleDetailsActivity;
import com.mino.jdict.objects.activities.ExampleDetailObject;
import com.mino.jdict.objects.activities.ExampleDetailObjects;
import com.mino.jdict.objects.activities.ListObject;

import java.util.ArrayList;

import utils.grammar.InputUtils;
import utils.other.FlowLayout;

public class ExampleDetailsAdapter extends ArrayAdapter<ExampleDetailObjects>  {


    /*
     * we are overriding the getView method here - this is what defines how each
     * list item will look.
     */
    // declaring our ArrayList of items
    private ArrayList<ExampleDetailObjects> objects;
    private Context context;

    /* here we must override the constructor for ArrayAdapter
    * the only variable we care about now is ArrayList<Item> objects,
    * because it is the list of objects we want to display.
    */
    public ExampleDetailsAdapter(Context context, ArrayList<ExampleDetailObjects> objects) {
        super(context, R.layout.detail_itemtype_item, objects);
        this.context = context;
        this.objects = objects;
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        ExampleDetailObjects i = objects.get(position);
        // assign the view we are converting to a local variable
        View v = convertView;
        int previousEntSeq = 0;
/*
        if (progress != null) {
            progress.setVisibility(View.GONE);
        }
*/
        if (i != null) {

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (i.IsNote.get()) {
                v = inflater.inflate(R.layout.note_item, null);
                TextView note = (TextView) v.findViewById(R.id.note);
                if (i.Note.get() != null) {
                    note.setText(i.Note.get());
                } else {
                    note.setText(context.getText(R.string.addnote));
                }

            }
            else if (i.SectionHeader.notEmpty()) {

                v = inflater.inflate(R.layout.detail_sectiontype_item, null);
                TextView header = (TextView) v.findViewById(R.id.detailSectionHeader);
                header.setText(i.SectionHeader.get());
            }
            else
            if (i.Translation.notEmpty()) {
                v = inflater.inflate(R.layout.detail_meaning_item, null);

                TextView header = (TextView) v.findViewById(R.id.meaningText);
                header.setText(i.Translation.get());

                //ImageView flag = (ImageView) v.findViewById(R.id.languageFlag);
                //flag.setImageResource(0);
            } else if (i.ListObject.notNull()) {
                if (i.ListObject.get().AddHeader.get() != null && !i.ListObject.get().AddHeader.get().isEmpty()) {

                    v = inflater.inflate(R.layout.detail_sectiontype_item, null);
                    TextView header = (TextView) v.findViewById(R.id.detailSectionHeader);
                    header.setText(i.ListObject.get().AddHeader.get());
/*
                    progress = (RelativeLayout) v.findViewById(R.id.sectionProgressLayout);
                    progress.setVisibility(View.VISIBLE);

                    */
                } else {
                    final ListObject listObj = i.ListObject.get();

                    v = inflater.inflate(R.layout.list_item, null);
                    AdapterHelper.AddEntryToView(i.ListObject.get(), v);
                }
            } else if (i.IsMain.get()) {
                v = inflater.inflate(R.layout.example_detail_item, null);

                FlowLayout.LayoutParams flowLP = new FlowLayout.LayoutParams(5, 5);
                FlowLayout ll = (FlowLayout) v.findViewById(R.id.exampleDetailLayout);

                boolean firstExample = true;
                for (ExampleDetailObject example : i.ExampleDetailObjectList.get()) {

                    View v2 = inflater.inflate(R.layout.example_detail_item_word_container, null);
                    LinearLayout wordLL = (LinearLayout) v2.findViewById(R.id.exampleDetailLayoutWordContainer);

                    View dot = (View) v2.findViewById(R.id.exampleDotLine);
                    dot.setVisibility(View.VISIBLE);

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
                        //String romaji = example.getRomajiForRebKeb(j);

                        readingKanjiRomaji.setText(romaji);

                        TextView kanji = (TextView) v3.findViewById(R.id.exampleKanji);
                        kanji.setText(rebKebPair.second.trim());

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

                                    ((ExampleDetailsActivity) context).goToItemDetails(lObj);
                                }
                            });
                        } else {

                            previousEntSeq = -1;
                            readingKanji.setText("");

                            if (firstExample) {

                                ll.setPadding(0, 0, 0, 5);
                            }

                            dot.setVisibility(View.GONE);
                        }

                        wordLL.addView(v3);
                    }

                    firstExample = false;

                    ll.addView(v2);

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