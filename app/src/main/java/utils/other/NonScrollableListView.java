package utils.other;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by Mino on 2015-04-13.
 */
public class NonScrollableListView extends ListView {

    public NonScrollableListView(Context context) {
        super(context);
    }

    public NonScrollableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public NonScrollableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return ev.getAction() == MotionEvent.ACTION_MOVE || super.dispatchTouchEvent(ev);
    }
}
