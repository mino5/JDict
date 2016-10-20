package utils.other;

import android.content.res.AssetManager;
import android.graphics.Typeface;

/**
 * Created by Mino on 2014-10-24.
 */
public class FontHelper {
    private static FontHelper ourInstance = new FontHelper();

    private Typeface DroidSansJapanese = null;

    private FontHelper() {
    }

    public static FontHelper getInstance() {
        return ourInstance;
    }

    public Typeface getDroidSansJapanese() {
        return DroidSansJapanese;
    }

    public void Initialize(AssetManager mgr) {
        if (DroidSansJapanese == null) {
            DroidSansJapanese = Typeface.createFromAsset(mgr, "fonts/DroidSansJapaneseMod.ttf");
        }
    }
}
