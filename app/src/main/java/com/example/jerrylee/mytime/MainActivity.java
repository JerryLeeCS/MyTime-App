package com.example.jerrylee.mytime;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.PersistableBundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import database.TimeDatabaseHelper;
import layout.ChronometerFragment;
import layout.ChartFragment;
import layout.ListFragment;
import listener.onDataChangedListener;

public class MainActivity extends AppCompatActivity  implements onDataChangedListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private static final String TAG = MainActivity.class.getSimpleName();
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private TabLayout tabLayout;

    Fragment[] fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG,"onCreate...");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolBar.setTitle("");
        toolBar.setSubtitle("");
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_play_circle_filled_black_48dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_reorder_black_48dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_assessment_black_48dp);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.v(TAG,"onSaveInstanceState...");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.v(TAG,"onCreateOptionMenu...");
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.v(TAG,"onOptionsItemSelected...");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v(TAG,"onActivityResult...");
        if(requestCode == ListFragment.REQUEST_FOR_EDIT){
            Log.v(TAG,"requestCode is REQUEST_FOR_EDIT");
        }
    }

    @Override
    public void onDataInserted() {
        ListFragment listFragment = (ListFragment) mSectionsPagerAdapter.getItem(1);
        Log.v(TAG,"onDataInserted...");
        if(listFragment != null){
            listFragment.refreshAdapter();
        }
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            Log.v(TAG,"on setting new fragments.....");
            fragments = new Fragment[getCount()];
            fragments[0] = ChronometerFragment.newInstance("1");
            fragments[1] = ListFragment.newInstance("2");
            fragments[2] = ChartFragment.newInstance("3");
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return null;
                case 1:
                    return null;
                case 2:
                    return null;
            }
            return null;
        }
    }
}
