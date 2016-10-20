package utils.other;

import android.view.View;
import android.widget.Adapter;

/**
 * Created by Mino on 2014-11-06.
 */
public class ViewAdapter {

    private View mView;
    private Adapter mAdapter;

    public ViewAdapter(View inView, Adapter inAdapter) {

        this.mView = inView;
        this.mAdapter = inAdapter;
    }

    public View getView() {
        return mView;
    }

    public Adapter getAdapter() {
        return mAdapter;
    }
}
