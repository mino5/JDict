package com.mino.jdict.adapters;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mino.jdict.R;
import com.mino.jdict.activities.KanjiBaseActivity;
import com.mino.jdict.objects.activities.KanjiDetailObject;

import java.util.ArrayList;

import utils.grammar.InputUtils;
import utils.other.ExTextView;

public class KanjiDetailsAdapter extends ArrayAdapter<KanjiDetailObject> {

    // declaring our ArrayList of items
    private ArrayList<KanjiDetailObject> objects;

    private Context context;

    /* here we must override the constructor for ArrayAdapter
    * the only variable we care about now is ArrayList<Item> objects,
    * because it is the list of objects we want to display.
    */
    public KanjiDetailsAdapter(Context context, int textViewResourceId, ArrayList<KanjiDetailObject> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.objects = objects;
    }

    /*
     * we are overriding the getView method here - this is what defines how each
     * list item will look.
     */
    public View getView(int position, View convertView, ViewGroup parent) {

        KanjiDetailObject i = objects.get(position);
        // assign the view we are converting to a local variable
        View v = convertView;

        if (i != null) {

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            v = inflater.inflate(R.layout.kanjidetail_item, null);

            if (i.IsNote.get()) {
                v = inflater.inflate(R.layout.note_item, null);
                TextView note = (TextView) v.findViewById(R.id.note);
                if (i.Note.get() != null) {
                    note.setText(i.Note.get());
                } else {
                    note.setText(context.getText(R.string.addnote));
                }

            } else if (i.getIsKanjiObject()) {
                v = inflater.inflate(R.layout.kanjidetail_item, null);

                TextView kanji = (TextView) v.findViewById(R.id.kanjiDetailItem);
                String character = i.getKanjiObject().Character.get();

                kanji.setText(character);
                //ShowUtils.showKanji(kanji, character);

            } else if (i.IsComponentKanji.get()) {
                v = inflater.inflate(R.layout.kanji_decomposition_item, null);
                TextView kanji = (TextView) v.findViewById(R.id.kanji);
                TextView kanjiMeaning = (TextView) v.findViewById(R.id.kanjiMeaning);
                TextView kanjiReading = (TextView) v.findViewById(R.id.kanjiReading);
                View showKanji = (View) v.findViewById(R.id.showKanji);

                AdapterHelper.DecompositionItem(i.getKanjiObject(), inflater, kanji, kanjiMeaning, kanjiReading, showKanji, v);
            } else if (i.ListObject.get() != null) {
                if (i.ListObject.get().IsGetNewItems.get()) {

                    if (i.ListObject.get().getCountObject() != null) {
                        v = inflater.inflate(R.layout.header_item3, null);
                        TextView moreResultsText = (TextView) v.findViewById(R.id.moreResultsText);
                        moreResultsText.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                ((KanjiBaseActivity) context).getMore();
                            }
                        });

                        TextView moreResultsCount = (TextView) v.findViewById(R.id.moreResultsCount);
                        moreResultsCount.setText(i.ListObject.get().getCountObject().getText());
                        moreResultsCount.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                ((KanjiBaseActivity) context).getMore();
                            }
                        });
                    }
                } else if (i.ListObject.get().AddHeader.get() != null && !i.ListObject.get().AddHeader.get().isEmpty()) {
                    if (i.ListObject.get().CompoundsSectionCharacter.get() != null) {

                        v = inflater.inflate(R.layout.detail_sectiontype_item_compounds, null);

                        final ExTextView buttonLeft = (ExTextView) v.findViewById(R.id.buttonLeft);
                        final ExTextView buttonRight = (ExTextView) v.findViewById(R.id.buttonRight);
                        final ExTextView buttonBoth = (ExTextView) v.findViewById(R.id.buttonBoth);
                        final ExTextView buttonAll = (ExTextView) v.findViewById(R.id.buttonAll);

                        buttonLeft.setText("_" + i.ListObject.get().CompoundsSectionCharacter.get());
                        buttonRight.setText(i.ListObject.get().CompoundsSectionCharacter.get() + "_");
                        buttonBoth.setText("_" + i.ListObject.get().CompoundsSectionCharacter.get() + "_");

                        switch (i.ListObject.get().WildCard.get()) {

                            case NoWildCard:
                                setWildCardAll(buttonLeft, buttonRight, buttonAll, buttonBoth);
                                break;

                            case Right:
                                setWildCardRight(buttonLeft, buttonRight, buttonAll, buttonBoth);
                                break;

                            case Left:
                                setWildCardLeft(buttonLeft, buttonRight, buttonAll, buttonBoth);
                                break;

                            case RightLeft:
                                setWildCardBoth(buttonLeft, buttonRight, buttonAll, buttonBoth);
                                break;
                        }

                        buttonLeft.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                //setWildCardLeft(buttonLeft, buttonRight, buttonAll, buttonBoth);
                                ((KanjiBaseActivity) context).getCompounds(InputUtils.WildCardMode.Left);
                            }
                        });

                        buttonRight.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                //setWildCardRight(buttonLeft, buttonRight, buttonAll, buttonBoth);
                                ((KanjiBaseActivity) context).getCompounds(InputUtils.WildCardMode.Right);
                            }
                        });

                        buttonBoth.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                //setWildCardBoth(buttonLeft, buttonRight, buttonAll, buttonBoth);
                                ((KanjiBaseActivity) context).getCompounds(InputUtils.WildCardMode.RightLeft);
                            }
                        });

                        buttonAll.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                //setWildCardAll(buttonLeft, buttonRight, buttonAll, buttonBoth);
                                ((KanjiBaseActivity) context).getCompounds(InputUtils.WildCardMode.NoWildCard);
                            }
                        });

                    } else {
                        v = inflater.inflate(R.layout.detail_sectiontype_item, null);
                        TextView header = (TextView) v.findViewById(R.id.detailSectionHeader);
                        header.setText(i.ListObject.get().AddHeader.get());
                    }
                } else {

                    v = inflater.inflate(R.layout.list_item, null);
                    AdapterHelper.AddEntryToView(i.ListObject.get(), v);
                }

            } else if (i.IsComponent.get()) {
                v = inflater.inflate(R.layout.kanji_decomposition_item, null);
                TextView kanji = (TextView) v.findViewById(R.id.kanji);
                TextView kanjiMeaning = (TextView) v.findViewById(R.id.kanjiMeaning);
                TextView kanjiReading = (TextView) v.findViewById(R.id.kanjiReading);
                View showKanji = (View) v.findViewById(R.id.showKanji);

                AdapterHelper.DecompositionItem(i.getKanjiObject(), inflater, kanji, kanjiMeaning, kanjiReading, showKanji, v);
            }

            if (!i.SectionHeader.get().isEmpty()) {

                v = inflater.inflate(R.layout.detail_sectiontype_item, null);
                TextView header = (TextView) v.findViewById(R.id.detailSectionHeader);
                header.setText(i.SectionHeader.get());

            } else if (i.IsKunyomi.get()) {
                v = inflater.inflate(R.layout.kanji_reading_item2, null);

                if (!i.getKanjiObject().getKunyomi().isEmpty()) {

                    TextView kanjiReadingType = (TextView) v.findViewById(R.id.kanjiReadingType2);
                    kanjiReadingType.setText("Kun-yomi");

                    TextView kanjiReadingText = (TextView) v.findViewById(R.id.kanjiReadingText2);

                    SpannableStringBuilder WordtoSpan;
                    WordtoSpan = new SpannableStringBuilder(i.getKanjiObject().getKunyomi());

                    ColorReading(v, WordtoSpan);
                    kanjiReadingText.setText(WordtoSpan);
                }
            } else if (i.IsOnyomi.get()) {
                v = inflater.inflate(R.layout.kanji_reading_item2, null);

                if (!i.getKanjiObject().getOnyomi().isEmpty()) {

                    TextView kanjiReadingType = (TextView) v.findViewById(R.id.kanjiReadingType2);
                    kanjiReadingType.setText("On-yomi");

                    TextView kanjiReadingText = (TextView) v.findViewById(R.id.kanjiReadingText2);

                    SpannableStringBuilder WordtoSpan;
                    WordtoSpan = new SpannableStringBuilder(i.getKanjiObject().getOnyomi());

                    ColorReading(v, WordtoSpan);
                    kanjiReadingText.setText(WordtoSpan);
                }
            } else if (i.IsNanori.get()) {
                v = inflater.inflate(R.layout.kanji_reading_item2, null);

                if (!i.getKanjiObject().getNanori().isEmpty()) {

                    TextView kanjiReadingType = (TextView) v.findViewById(R.id.kanjiReadingType2);
                    kanjiReadingType.setText("IsNanori");

                    TextView kanjiReadingText = (TextView) v.findViewById(R.id.kanjiReadingText2);

                    SpannableStringBuilder WordtoSpan;
                    WordtoSpan = new SpannableStringBuilder(i.getKanjiObject().getNanori());

                    ColorReading(v, WordtoSpan);
                    kanjiReadingText.setText(WordtoSpan);
                }
            } else if (i.Meaning.get() != null && !i.Meaning.get().isEmpty()) {

                v = inflater.inflate(R.layout.detail_meaning_item, null);

                TextView header = (TextView) v.findViewById(R.id.meaningText);
                header.setText(i.Meaning.get());

                ImageView flag = (ImageView) v.findViewById(R.id.languageFlag);
                flag.setImageResource(i.Language.get());
            } else if (i.StrokeCount.get() > 0) {

                v = inflater.inflate(R.layout.kanji_reading_item, null);

                TextView kanjiReadingType = (TextView) v.findViewById(R.id.kanjiReadingType);

                kanjiReadingType.setText("Stroke Count");

                TextView kanjiReadingText = (TextView) v.findViewById(R.id.kanjiReadingText);
                View showDetails = (View) v.findViewById(R.id.showDetails);

                kanjiReadingText.setText(String.valueOf(i.StrokeCount.get()));
                kanjiReadingText.setTextColor(context.getResources().getColor(R.color.graydark));

                final String svg = i.StrokeSvg.get();

                if (svg != null && svg.length() > 0) {

                    showDetails.setVisibility(View.VISIBLE);
                }
            } else if (!i.CharacterSetType.get().isEmpty()) {

                v = inflater.inflate(R.layout.kanji_reading_item2, null);

                TextView kanjiReadingType = (TextView) v.findViewById(R.id.kanjiReadingType2);
                kanjiReadingType.setText(i.CharacterSetType.get().equals("ucs") ? "Unicode 4.0" : "JIS X 208");

                TextView kanjiReadingText = (TextView) v.findViewById(R.id.kanjiReadingText2);
                kanjiReadingText.setText(i.CharacterSetValue.get());
                kanjiReadingText.setTextColor(context.getResources().getColor(R.color.graydark));
            } else if (i.JLPT.get() > 0) {

                v = inflater.inflate(R.layout.kanji_reading_item2, null);

                TextView kanjiReadingType = (TextView) v.findViewById(R.id.kanjiReadingType2);
                kanjiReadingType.setText("JLPT level");

                TextView kanjiReadingText = (TextView) v.findViewById(R.id.kanjiReadingText2);
                kanjiReadingText.setText(String.valueOf(i.JLPT.get()));
                kanjiReadingText.setTextColor(context.getResources().getColor(R.color.graydark));
            } else if (i.Freq.get() > 0) {

                v = inflater.inflate(R.layout.kanji_reading_item2, null);

                TextView kanjiReadingType = (TextView) v.findViewById(R.id.kanjiReadingType2);
                kanjiReadingType.setText("Frequency");

                TextView kanjiReadingText = (TextView) v.findViewById(R.id.kanjiReadingText2);
                kanjiReadingText.setText(String.valueOf(i.Freq.get()));
                kanjiReadingText.setTextColor(context.getResources().getColor(R.color.graydark));
            } else if (i.Grade.get() > 0) {

                v = inflater.inflate(R.layout.kanji_reading_item2, null);

                TextView kanjiReadingType = (TextView) v.findViewById(R.id.kanjiReadingType2);
                kanjiReadingType.setText("Grade");

                TextView kanjiReadingText = (TextView) v.findViewById(R.id.kanjiReadingText2);
                kanjiReadingText.setText(String.valueOf(i.Grade.get()));
                kanjiReadingText.setTextColor(context.getResources().getColor(R.color.graydark));
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

    private void setWildCardAll(ExTextView buttonLeft, ExTextView buttonRight, ExTextView buttonAll, ExTextView buttonBoth) {
        buttonLeft.setBackgroundResource(R.color.transparent);
        buttonRight.setBackgroundResource(R.color.transparent);
        buttonBoth.setBackgroundResource(R.color.transparent);
        buttonAll.setBackgroundResource(R.color.graydark);

        buttonLeft.setTextColor(context.getResources().getColor(R.color.text_section));
        buttonRight.setTextColor(context.getResources().getColor(R.color.text_section));
        buttonBoth.setTextColor(context.getResources().getColor(R.color.text_section));
        buttonAll.setTextColor(context.getResources().getColor(R.color.gray_foreground));
    }

    private void setWildCardBoth(ExTextView buttonLeft, ExTextView buttonRight, ExTextView buttonAll, ExTextView buttonBoth) {
        buttonLeft.setBackgroundResource(R.color.transparent);
        buttonRight.setBackgroundResource(R.color.transparent);
        buttonAll.setBackgroundResource(R.color.transparent);
        buttonBoth.setBackgroundResource(R.color.graydark);

        buttonLeft.setTextColor(context.getResources().getColor(R.color.text_section));
        buttonRight.setTextColor(context.getResources().getColor(R.color.text_section));
        buttonAll.setTextColor(context.getResources().getColor(R.color.text_section));
        buttonBoth.setTextColor(context.getResources().getColor(R.color.gray_foreground));
    }

    private void setWildCardRight(ExTextView buttonLeft, ExTextView buttonRight, ExTextView buttonAll, ExTextView buttonBoth) {
        buttonLeft.setBackgroundResource(R.color.transparent);
        buttonBoth.setBackgroundResource(R.color.transparent);
        buttonAll.setBackgroundResource(R.color.transparent);
        buttonRight.setBackgroundResource(R.color.graydark);

        buttonLeft.setTextColor(context.getResources().getColor(R.color.text_section));
        buttonBoth.setTextColor(context.getResources().getColor(R.color.text_section));
        buttonAll.setTextColor(context.getResources().getColor(R.color.text_section));
        buttonRight.setTextColor(context.getResources().getColor(R.color.gray_foreground));
    }

    private void setWildCardLeft(ExTextView buttonLeft, ExTextView buttonRight, ExTextView buttonAll, ExTextView buttonBoth) {
        buttonRight.setBackgroundResource(R.color.transparent);
        buttonBoth.setBackgroundResource(R.color.transparent);
        buttonAll.setBackgroundResource(R.color.transparent);
        buttonLeft.setBackgroundResource(R.color.graydark);

        buttonRight.setTextColor(context.getResources().getColor(R.color.text_section));
        buttonBoth.setTextColor(context.getResources().getColor(R.color.text_section));
        buttonAll.setTextColor(context.getResources().getColor(R.color.text_section));
        buttonLeft.setTextColor(context.getResources().getColor(R.color.gray_foreground));
    }

    private void ColorReading(View v, SpannableStringBuilder wordtoSpan) {
        Boolean isDot = false;

        int len = wordtoSpan.length();

        for (int j = 0; j < len; j++) {

            if (wordtoSpan.charAt(j) == '.') {
                isDot = true;
                wordtoSpan.delete(j, j + 1);
                wordtoSpan.setSpan(new ForegroundColorSpan(v.getResources().getColor(R.color.wordending)), j, j + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                len--;
            } else if (wordtoSpan.charAt(j) != ',' && wordtoSpan.charAt(j) != '/') {

                if (isDot) {
                    wordtoSpan.setSpan(new ForegroundColorSpan(v.getResources().getColor(R.color.wordending)), j, j + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                } else {
                    wordtoSpan.setSpan(new ForegroundColorSpan(v.getResources().getColor(R.color.imired_light)), j, j + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            } else {
                isDot = false;
            }
        }
    }

}