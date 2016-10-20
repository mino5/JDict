package utils.other;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Dominik on 9/30/2015.
 */
public class ExTextView extends TextView {

    public ExTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ExTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExTextView(Context context) {
        super(context);
    }

    public void setSpecialFont() {
        setTypeface(FontHelper.getInstance().getDroidSansJapanese());
    }

}