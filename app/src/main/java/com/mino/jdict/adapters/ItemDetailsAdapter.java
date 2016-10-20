package com.mino.jdict.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mino.jdict.R;
import com.mino.jdict.objects.activities.ItemDetailObject;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ItemDetailsAdapter extends ArrayAdapter<ItemDetailObject> {

    // declaring our ArrayList of items
    private ArrayList<ItemDetailObject> objects;

    private Context context;

    /* here we must override the constructor for ArrayAdapter
    * the only variable we care about now is ArrayList<Item> objects,
    * because it is the list of objects we want to display.
    */
    public ItemDetailsAdapter(Context context, int textViewResourceId, ArrayList<ItemDetailObject> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.objects = objects;
    }

    /*
     * we are overriding the getView method here - this is what defines how each
     * list item will look.
     */
    public View getView(int position, View convertView, ViewGroup parent) {

        ItemDetailObject i = objects.get(position);
        // assign the view we are converting to a local variable
        View v = convertView;

        if (i != null) {

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            v = inflater.inflate(R.layout.detail_itemtype_item, null);

            if (!i.SectionHeader.get().isEmpty()) {

                v = inflater.inflate(R.layout.detail_sectiontype_item, null);
                TextView header = (TextView) v.findViewById(R.id.detailSectionHeader);
                header.setText(i.SectionHeader.get());
            } else if (i.Xref.get() != null) {
                if (i.Xref.get().second > 0) {
                    v = inflater.inflate(R.layout.detail_itemtype_clickable_item, null);
                    TextView header = (TextView) v.findViewById(R.id.itemTypeTextClickable);
                    header.setText(context.getString(R.string.xref) + " " + i.Xref.get().first);
                } else {
                    v = inflater.inflate(R.layout.detail_itemtype_item, null);
                    TextView header = (TextView) v.findViewById(R.id.itemTypeText);
                    header.setText(context.getString(R.string.xref) + " " + i.Xref.get().first);
                }

            } else if (i.Ant.get() != null) {
                if (i.Ant.get().second > 0) {
                    v = inflater.inflate(R.layout.detail_itemtype_clickable_item, null);
                    TextView header = (TextView) v.findViewById(R.id.itemTypeTextClickable);
                    header.setText(context.getString(R.string.ant) + " " + i.Ant.get().first);
                } else {
                    v = inflater.inflate(R.layout.detail_itemtype_item, null);
                    TextView header = (TextView) v.findViewById(R.id.itemTypeText);
                    header.setText(context.getString(R.string.ant) + " " + i.Ant.get().first);
                }
            }
            else if (i.IsNewResultsForSelectedSense.get() != null && i.IsNewResultsForSelectedSense.get()) {
                v = inflater.inflate(R.layout.header_item5, null);

            }
            else if(i.IsSenseSeparator.get()) {
                v = inflater.inflate(R.layout.sense_separator, null);
            }
         else if (i.SInf.get() != null) {

            v = inflater.inflate(R.layout.detail_itemtype_item, null);
            TextView header = (TextView) v.findViewById(R.id.itemTypeText);
            header.setText(i.SInf.get());

        }
        else if (i.Stagk.get() != null) {

                v = inflater.inflate(R.layout.detail_itemtype_item, null);
                TextView header = (TextView) v.findViewById(R.id.itemTypeText);
                header.setText(context.getString(R.string.stag) + " " + i.Stagk.get());

            } else if (i.Stagr.get() != null) {

                v = inflater.inflate(R.layout.detail_itemtype_item, null);
                TextView header = (TextView) v.findViewById(R.id.itemTypeText);
                header.setText(context.getString(R.string.stag) + " " + i.Stagr.get());

            } else if (i.Lsource.get() != null) {

                v = inflater.inflate(R.layout.detail_itemtype_item, null);
                TextView header = (TextView) v.findViewById(R.id.itemTypeText);

                if (i.Lsource.get().second != null) {
                    Class c = null;
                    try {
                        c = Class.forName("com.mino.jdict.R$string");

                        String langStr = "";
                        if (i.Lsource.get().second.contentEquals("")) {
                            langStr = context.getString(R.string.eng);
                        } else {
                            Field field = c.getDeclaredField(i.Lsource.get().second);
                            int resId = (int) field.getInt(null);
                            langStr = context.getResources().getString(resId);
                        }

                        header.setText(context.getString(R.string.from) + " " + langStr + " \"" + i.Lsource.get().first + "\"");

                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                } else
                {
                    header.setText(context.getString(R.string.from) + " " + context.getString(R.string.eng) + " \"" + i.Lsource.get().first + "\"");
                }


            } else
            if (i.IsNote.get()) {
                v = inflater.inflate(R.layout.note_item, null);
                TextView note = (TextView) v.findViewById(R.id.note);
                if (i.Note.get() != null) {
                    note.setText(i.Note.get());
                } else {
                    note.setText(context.getText(R.string.addnote));
                }

            } else if (i.IsGetNewResults.get()) {
                v = inflater.inflate(R.layout.header_item3, null);
                TextView moreResultsText = (TextView) v.findViewById(R.id.moreResultsText);
                moreResultsText.setClickable(false);

                TextView moreResultsCount = (TextView) v.findViewById(R.id.moreResultsCount);
                moreResultsCount.setText(i.getCountObject().getText());
                moreResultsCount.setClickable(false);

            } else if (i.IsExample.get()) {
                v = inflater.inflate(R.layout.example_item, null);

                TextView jp = (TextView) v.findViewById(R.id.exampleJP);
                TextView eng = (TextView) v.findViewById(R.id.exampleEN);
                View showArrow = (View) v.findViewById(R.id.showArrowExample);

                if (i.ShowArrow.get()) {
                    showArrow.setVisibility(View.VISIBLE);
                }

                if (jp != null) {
                    jp.setText(i.getReb(true));
                }

                if (eng != null) {
                    eng.setText(i.getKeb(true));
                }
            }
            if (i.IsMain.get()) {
                v = inflater.inflate(R.layout.detail_word_item, null);
                LinearLayout ll = (LinearLayout) v.findViewById(R.id.wordItemLinearLayout);

                boolean kanjiEmpty = AdapterHelper.CheckKebEmpty(i);

                for (int j = 0; j < i.getRebKebList().size(); j++) {

                    View v2 = inflater.inflate(R.layout.detail_word_item_kanji_reading, null);
                    TextView readingKanji = (TextView) v2.findViewById(R.id.readingKanji);
                    TextView readingKanjiRomaji = (TextView) v2.findViewById(R.id.readingKanjiRomaji);

                    Pair<String, String> rebKebPair = i.getRebKebList().get(j);

                    if (kanjiEmpty) {
                        readingKanji.setVisibility(View.GONE);
                    }

                    readingKanji.setText(rebKebPair.first);
                    String romaji = AdapterHelper.getRomajiFromRebKebList(i, j);

                    readingKanjiRomaji.setText(" " + romaji + " ");

                    TextView kanji = (TextView) v2.findViewById(R.id.kanji);
                    kanji.setText(rebKebPair.second);

                    ll.addView(v2);
                }
            } else if (i.IsAlternative.get()) {
                v = inflater.inflate(R.layout.list_recent_item, null);
                TextView text = (TextView) v.findViewById(R.id.text);
                TextView textadd = (TextView) v.findViewById(R.id.textadd);
                View showArrow = (View) v.findViewById(R.id.showArrow);

                if (i.ShowArrow.get()) {
                    showArrow.setVisibility(View.VISIBLE);
                }

                if (text != null) {
                    text.setText(i.getKeb(true));
                }

                if (textadd != null) {
                    textadd.setText(i.getReb(false));
                }
            } else if (i.getIsKanjiObject()) {
                v = inflater.inflate(R.layout.kanji_decomposition_item, null);
                TextView kanji = (TextView) v.findViewById(R.id.kanji);
                TextView kanjiMeaning = (TextView) v.findViewById(R.id.kanjiMeaning);
                TextView kanjiReading = (TextView) v.findViewById(R.id.kanjiReading);
                View showKanji = (View) v.findViewById(R.id.showKanji);

                AdapterHelper.DecompositionItem(i.getKanjiObject(), inflater, kanji, kanjiMeaning, kanjiReading, showKanji, v);
            } else if (!i.ConjugatedName.get().isEmpty()) {
                v = inflater.inflate(R.layout.detail_conjugation_item, null);
                TextView conjugationName = (TextView) v.findViewById(R.id.textConjugationName);
                conjugationName.setText(i.ConjugatedName.get());
                TextView conjugatedForm = (TextView) v.findViewById(R.id.textConjugatedForm);
                conjugatedForm.setText(i.ConjugatedForm.get());

                if (i.IsCurrentGrammarForm.get()) {
                    conjugationName.setTextSize(20);
                    conjugatedForm.setTextSize(20);
                    conjugationName.setTypeface(null, Typeface.BOLD);
                    conjugatedForm.setTypeface(null, Typeface.BOLD);
                }
            }
            if (i.IsCommon.get()) {

                TextView header = (TextView) v.findViewById(R.id.itemTypeText);
                header.setText("Common word");
                header.setTextColor(context.getResources().getColor(R.color.commonblue));
            } else if (i.JLPTLevel.get() > 0) {

                TextView header = (TextView) v.findViewById(R.id.itemTypeText);
                String jlptText = "JLPT N" + String.valueOf(i.JLPTLevel.get());

                if (i.JLPTLevelReb.get() > 0 && i.JLPTLevelReb.get() > i.JLPTLevel.get()) {
                    jlptText += " (N" + String.valueOf(i.JLPTLevelReb.get()) + " - word written in kana) ";
                }
                header.setText(jlptText);

                header.setTextColor(context.getResources().getColor(R.color.imired_light));
            }
            else if (i.JLPTLevelReb.get() > 0)
            {
                TextView header = (TextView) v.findViewById(R.id.itemTypeText);
                String jlptText = "JLPT N" + String.valueOf(i.JLPTLevelReb.get()) + " - word written in kana ";
                header.setText(jlptText);

                header.setTextColor(context.getResources().getColor(R.color.imired_light));
            }
            else if (!i.GrammarChain.get().isEmpty() && i.ConjugatedQuery.get() != null) {
                TextView header = (TextView) v.findViewById(R.id.itemTypeText);
                header.setText("ConjugatedQuery '" + i.ConjugatedQuery.get() + "' is a conjugated form (" + i.GrammarChain.get() + ")");
            } else if (!i.ItemType.get().isEmpty()) {

                TextView header = (TextView) v.findViewById(R.id.itemTypeText);
                header.setText(i.ItemType.get());
            } else if (!i.Gloss.get().isEmpty()) {

                v = inflater.inflate(R.layout.detail_meaning_item, null);

                TextView header = (TextView) v.findViewById(R.id.meaningText);
                header.setText(i.Gloss.get());

                ImageView flag = (ImageView) v.findViewById(R.id.languageFlag);
                flag.setImageResource(i.Language.get());
/*
                if (i.getIsColored() != null && i.getIsColored()) {
                    v.setBackgroundColor(v.getResources().getColor(R.color.gray));
                }
                */
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