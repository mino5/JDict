package utils.other;

import android.content.Context;
import android.database.Cursor;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.mino.jdict.JDictApplication;
import com.mino.jdict.R;
import com.mino.jdict.activities.VocabularyActivity;
import com.mino.jdict.interfaces.IClearable;
import com.mino.jdict.logics.basic.Logic;

import java.util.HashMap;

import utils.database.JMDictHelper;
import utils.database.entry.Vocabulary;

/**
 * Created by Dominik on 7/5/2015.
 */
public class VocabularyPopup {

    Context mContext;
    ImageView mFavButton;
    Vocabulary.ElementType mType;
    JMDictHelper DictHelper;
    HashMap<MenuItem, Integer> folderIds = new HashMap<MenuItem, Integer>();

    public VocabularyPopup(Context inContext, ImageView inButton, Vocabulary.ElementType inType) {
        mContext = inContext;
        mFavButton = inButton;
        mType = inType;
        DictHelper = JDictApplication.getDatabaseHelper();
    }

    public void checkIfInVocabList(int selectedObjectID) {

        if (DictHelper == null) {
            DictHelper = new JMDictHelper(mContext);
        }

        if (mFavButton != null) {
            Cursor cursor = DictHelper.getVocabulary().getVocabListElements(selectedObjectID, mType);

            if (cursor != null && cursor.getCount() > 0) {
                mFavButton.setImageResource(android.R.drawable.btn_star_big_on);
            } else {
                mFavButton.setImageResource(android.R.drawable.btn_star_big_off);
            }

            assert cursor != null;
            cursor.close();
        }
    }

    public void setMenuItemTag(MenuItem item, Integer tag) {
        folderIds.put(item, tag);
    }

    // returns null if tag has not been set(or was set to null)
    public Integer getMenuItemTag(MenuItem item) {
        return folderIds.get(item);
    }

    public void showPopup(int selectedObjectID) {
        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(mContext, mFavButton);
        //Inflating the Popup using xml file
        //    popup.getMenuInflater()
        //          .inflate(R.menu.favourites, popup.getMenu());

        final int selectedObjectID_ = selectedObjectID;

        folderIds.clear();

        Cursor countCursor = DictHelper.getVocabulary().getAllFoldersCount();

        if (countCursor.getCount() == 0) {
            Toast.makeText(mContext, mContext.getString(R.string.no_folders), Toast.LENGTH_SHORT).show();
        }

        if (countCursor.getCount() > 0) {

            boolean simpleMode = countCursor.getInt(0) == 1;

            Cursor cursor = DictHelper.getVocabulary().getAllFolders(selectedObjectID, mType);

            if (cursor != null && cursor.getCount() > 0) {

                int idIndex = cursor.getColumnIndex("ID_VocabList");
                int idVocabIndex = cursor.getColumnIndex("IDVocabList");
                int nameIndex = cursor.getColumnIndex("Name");
                int parentNameIndex = cursor.getColumnIndex("ParentName");
                int stateIndex = cursor.getColumnIndex("CurrentState");

                do {

                    Integer state = cursor.getInt(stateIndex);
                    boolean checked = state > 0;
                    Integer index = cursor.getInt(idIndex);

                    if (simpleMode) {

                        addRemoveEntry(!checked, index, selectedObjectID);
                    } else {
                        String parentName = cursor.getString(parentNameIndex);
                        MenuItem item = null;

                        //if (parentName == null || parentName.isEmpty()) {
                            item = popup.getMenu().add(cursor.getString(nameIndex));
                        //} else {
                       //     item = popup.getMenu().add(parentName + " / " + cursor.getString(nameIndex));
                        //}

                        item.setCheckable(true);
                        item.setChecked(checked);

                        setMenuItemTag(item, index);
                    }
                } while (cursor.moveToNext());

            }

            if (cursor != null) {
                cursor.close();
            }

            if (!simpleMode) {
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()

                                                 {
                                                     public boolean onMenuItemClick(MenuItem item) {

                                                         int id = getMenuItemTag(item);

                                                         if (id > 0) {
                                                             addRemoveEntry(!item.isChecked(), id, selectedObjectID_);
                                                         }

                                                         return true;
                                                     }
                                                 }

                );

                popup.show(); //showing popup menu
            }
        }

        if (countCursor != null) {
            countCursor.close();
        }
    }

    private void addRemoveEntry(boolean add, int idVocabList, int idObject) {

        if (add) {
            DictHelper.getVocabulary().insertEntry(idObject, mType, idVocabList);
            Toast.makeText(mContext, mContext.getString(R.string.entry_added), Toast.LENGTH_SHORT).show();
        } else {
            DictHelper.getVocabulary().removeEntryFromList(idObject, mType, idVocabList);
            Toast.makeText(mContext, mContext.getString(R.string.entry_deleted), Toast.LENGTH_SHORT).show();
        }

        checkIfInVocabList(idObject);
        Logic.refreshActivity(VocabularyActivity.class, IClearable.ClearanceLevel.Soft);
    }

}
