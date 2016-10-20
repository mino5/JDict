package utils.database.entry;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mino.jdict.JDictApplication;

import java.util.HashMap;
import java.util.Map;

import utils.database.JMDictHelper;

/**
 * Created by Dominik on 10/9/2016.
 */

public class Setting {
    JMDictHelper dbHelper;

    public Setting(JMDictHelper inHelper) {

        dbHelper = inHelper;
    }



    public Map<String, String> getGlobalSettings() {

        Map<String, String> list = new HashMap<String, String>();
        String sql = " select ID_Setting, SettingKey, SettingValue from Setting ";

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);

        c.moveToFirst();

        try {
            if (c != null) {
                do {

                    int settingKeyIndex = c.getColumnIndex("SettingKey");
                    int settingValueIndex = c.getColumnIndex("SettingValue");

                    String key = c.getString(settingKeyIndex);
                    String val = c.getString(settingValueIndex);

                    if (!list.containsKey(key)) {
                        list.put(key, val);
                    }

                } while (c.moveToNext());

            }
        } finally {
            if (c != null) {
                c.close();
            }
        }

        return list;
    }

    public void updateSetting(String key, String value) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String query = "UPDATE Setting SET SettingValue = '" + value + "' WHERE SettingKey = '" + key + "'";

        db.execSQL(query);

        JDictApplication.getInstance().setGlobalSetting(key, value);
    }

}
