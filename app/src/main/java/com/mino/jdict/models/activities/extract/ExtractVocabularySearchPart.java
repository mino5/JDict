package com.mino.jdict.models.activities.extract;

import android.util.Pair;
import android.widget.ProgressBar;

import com.mino.jdict.JDictApplication;
import com.mino.jdict.R;
import com.mino.jdict.models.basic.SearchPart;
import com.mino.jdict.models.basic.SearchValues;
import com.mino.jdict.objects.activities.ASearchObject;
import com.mino.jdict.objects.activities.ExtractObject;

import java.util.ArrayList;

/**
 * Created by Dominik on 9/6/2015.
 */
public class ExtractVocabularySearchPart extends SearchPart {

    public ExtractVocabularySearchPart() {
        super(R.string.vocabulary);
    }

    @Override
    public int getItems(String inSearchString, ArrayList<ASearchObject> result, SearchValues inSearchValues, ProgressBar inProgress) {

        if (inSearchString.length() < 6) {
            inProgress.setProgress(100);
        } else {
            inProgress.setProgress(0);
        }

        // VOCABULARY
        Pair<ArrayList<ExtractObject>, Integer> res = JDictApplication.getDatabaseHelper().getExtract().getWordsBySearchString(inSearchString, mFirstCheck, getRegion().getPartPercentage(), inProgress);

        if (res != null) {
            result.addAll(res.first);

            if (res.first.size() > 0)
                return res.first.size() - res.second;
            else return 0;
        } else return 0;
    }
}
