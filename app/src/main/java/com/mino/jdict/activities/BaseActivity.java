package com.mino.jdict.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.mino.jdict.NavigationDrawerFragment;
import com.mino.jdict.R;
import com.mino.jdict.interfaces.IActivitySection;

import utils.other.StaticObjectContainer;

/**
 * Created by Mino on 2014-10-08.
 */


public class BaseActivity extends FragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private static CharSequence mTitle;
    NavigationDrawerFragment mNavigationDrawerFragment;
    DrawerLayout mDrawerLayout;
    private boolean mIsCreating;
    private final boolean devMode = false;
    protected View mContentView;

    public CharSequence getSearchTitle() {
        return mTitle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (devMode)
        {
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }


/*
        Log.i("BaseActivity", "onCreateView. Saved state? "
                + (savedInstanceState != null));
*/

        super.onCreate(savedInstanceState);
        mIsCreating = true;
        setContentView(R.layout.activity_base);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                mDrawerLayout);
    }

    @Override
    protected void onResume() {

        if (this instanceof IActivitySection) {
            mNavigationDrawerFragment.Resume(((IActivitySection) this).getSectionNumber());

            SetTitles(((IActivitySection) this).getSectionNumber());
        }

        super.onResume();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position, boolean fromSavedInstanceState) {
        // update the main content by replacing fragments

        if (mIsCreating) {
            mIsCreating = false;
        } else {
            if (!fromSavedInstanceState) {
                this.onSectionAttached(position + 1);
            }
        }
    }

    public void hideKeyboard() {
        InputMethodManager mgr = (InputMethodManager) (mContentView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE));
        mgr.hideSoftInputFromWindow(mContentView.getWindowToken(), 0);
    }

    private void onSectionAttached(int number) {

        SetTitles(number);

        switch (number) {
            case 1: //Header
                break;

            case 2:

                if (!(this instanceof MainActivity)) {
                    Intent i = new Intent(getBaseContext(), MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(i);
                }

                break;
            case 3:

                if (!(this instanceof KanjiActivity)) {
                    Intent i = new Intent(getBaseContext(), KanjiActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(i);
                }
                break;

            case 4:

                if (!(this instanceof KanjiByComponentActivity)) {
                    Intent i = new Intent(getBaseContext(), KanjiByComponentActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(i);
                }
                break;

            case 5:

                if (!(this instanceof ExampleActivity)) {
                    Intent i = new Intent(getBaseContext(), ExampleActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(i);
                }
                break;

            case 6: //Header
                break;

            case 7:
                if (!(this instanceof ExtractActivity)) {
                    Intent i = new Intent(getBaseContext(), ExtractActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(i);
                }
                break;

            case 8: // Header
                break;

            case 9:
                if (!(this instanceof JLPTActivity)) {
                    StaticObjectContainer.StaticObject = null;
                    Intent i = new Intent(getBaseContext(), JLPTActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(i);
                }
                break;

            case 10:
                if (!(this instanceof SchoolGradesActivity)) {
                    StaticObjectContainer.StaticObject = null;
                    Intent i = new Intent(getBaseContext(), SchoolGradesActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(i);
                }
                break;

            case 11: //Header
                break;

            case 12:

                if (!(this instanceof NoteActivity)) {
                    Intent i = new Intent(getBaseContext(), NoteActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(i);
                }
                break;

            case 13:

                if (!(this instanceof VocabularyActivity)) {
                    Intent i = new Intent(getBaseContext(), VocabularyActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(i);
                }
                break;

            case 14: //Header
                break;
            case 15:
                if (!(this instanceof SettingsActivity)) {
                    Intent i = new Intent(getBaseContext(), SettingsActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(i);
                }
                break;
            case 16:
                if (!(this instanceof AboutActivity)) {
                    Intent i = new Intent(getBaseContext(), AboutActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(i);
                }
                break;

        }
    }

    public void SetTitles(int number) {
        switch (number) {
            case 2:
                mTitle = getString(R.string.search_hint);
                break;
            case 3:
                mTitle = getString(R.string.search_hint_kanji);
                break;
            case 4:
                mTitle = getString(R.string.search_hint_kanji_by_components);
                break;
            case 5:
                mTitle = getString(R.string.search_hint_examples);
                break;
            case 7:
                mTitle = getString(R.string.search_hint_extract);
                break;
            default:
                break;
        }
    }
}
