package utils.other;

import android.content.Context;
import android.content.Intent;

import com.mino.jdict.JDictApplication;
import com.mino.jdict.activities.ItemDetailsActivity;
import com.mino.jdict.activities.MainActivity;
import com.mino.jdict.interfaces.IClearable;
import com.mino.jdict.logics.basic.Logic;
import com.mino.jdict.models.basic.SearchValues;
import com.mino.jdict.objects.activities.ListObject;
import com.mino.jdict.objects.activities.RecentObject;

import java.util.ArrayList;

/**
 * Created by Dominik on 7/14/2015.
 */
public class ShowUtils {

/*
    public static void showKanji(TextView textView, String character) {

        textView.setText("一");
        textView.measure(0, 0);       //must call measure!
        int smallestMeasure = textView.getMeasuredWidth();  //get width

        textView.setText(character);
        textView.measure(0, 0);

        if (textView.getMeasuredWidth() < smallestMeasure) {

            Typeface typeFace = FontHelper.getInstance().getDroidSansJapanese();
            textView.setTypeface(typeFace);
        }
    }
    */

    public static void showEntry(ListObject lObj, Context context) {

        if (lObj != null) {

            RecentObject recentObj = new RecentObject.RecentObjectFactory().factory();

            SearchValues values = new SearchValues();
            ArrayList<Boolean> cbValues = new ArrayList<Boolean>();
            cbValues.add(false);
            values.setCheckBoxValues(cbValues);
            recentObj.init(lObj.getFirst(), lObj.getSecond(), lObj.ID.get(), "", values);

            if (!recentObj.getPureKeb().isEmpty() || !recentObj.getPureReb().isEmpty()) {
                JDictApplication.getDatabaseHelper().getWord().insertRecentWords(recentObj);
                Logic.refreshActivity(MainActivity.class, IClearable.ClearanceLevel.Soft);

                Intent i = new Intent(context, ItemDetailsActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("SelectedObjectEntSeq", lObj.ID.get());

                StaticObjectContainer.StaticObject = lObj;

                context.startActivity(i);
            }
        }
    }

    public static void showEntry(int ent_seq, Context context) {

        ListObject lObj = JDictApplication.getDatabaseHelper().getWord().getBasicDetails(ent_seq, true);
        showEntry(lObj, context);
    }
}
