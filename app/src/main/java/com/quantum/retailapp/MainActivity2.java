package com.quantum.retailapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;


public class MainActivity2 extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, ActionBar.TabListener {

    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(MainActivity2.this, RetailElementViewer.class);
            RetailElement.BuildingType tmp = ((RetailElementsManager.RetailElementsAdapter) parent.getAdapter()).getBuidlingType();
            intent.putExtra(RetailElementViewer.ADAPTER_TYPE, tmp.ordinal());
            intent.putExtra(RetailElementViewer.SELECTED_ELEMENT_ID, position);
            startActivity(intent);
        }
    };

    public static final String SHARED_PREF_NAME = "AmlakApp";

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link android.support.v4.view.ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = "املاک";

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                try {
                    getSupportActionBar().setSelectedNavigationItem(position);
                } catch (Exception ex) {

                }
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }

        refreshData();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        if (position == 2) {//update data button
            new UpdateAsyncTask().execute(ServiceHandler.ServerAddress);
        }
        // update the main content by replacing fragments
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction()
//                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
//                .commit();
    }

    public void onSectionAttached(int number) {
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main_activity2, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    public void refreshData() {
        SharedPreferences settings = getSharedPreferences(SHARED_PREF_NAME, 0);
        String fileName = settings.getString("FileName", "");
        String key1 = settings.getString("AES1", "");
        String key2 = settings.getString("AES2", "");

        RetailElementsManager.getDefaultInstance().RefreshListFromFile(fileName,
                key1.getBytes(Charset.forName("US-ASCII")),
                key2.getBytes(Charset.forName("US-ASCII")));
    }

    private class UpdateAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return ServiceHandler.GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject responseObj = new JSONObject(result);
                int resType = responseObj.getInt("ResponseType");
                String resValue = responseObj.getString("ResponseValue");
                JSONArray resAdditionalData = responseObj.getJSONArray("AdditionalData");

                SharedPreferences settings = getSharedPreferences(SHARED_PREF_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("FileName", resValue);
                editor.putString("AES1", resAdditionalData.get(0).toString());
                editor.putString("AES2", resAdditionalData.get(1).toString());

                editor.commit();
                if (resType == 0) {
                    new DownloadAmlakFile().execute(ServiceHandler.FilePath + resValue, resValue);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        private class DownloadAmlakFile extends AsyncTask<String, Void, Void> {
            @Override
            protected Void doInBackground(String... sUrl) {

                byte[] bytes = ServiceHandler.GETBytes(sUrl[0]);
                File root = Environment.getExternalStorageDirectory();

                File fileDir = new File(root.getAbsolutePath());
                fileDir.mkdirs();

                File file = new File(fileDir, sUrl[1]);
                FileOutputStream output = null;
                try {
                    output = new FileOutputStream(file);
                    output.write(bytes, 0, bytes.length);

                } catch (FileNotFoundException e) {
                    Log.d("Creating File", e.getStackTrace().toString());
                } catch (IOException e) {
                    Log.d("Creating File", e.getStackTrace().toString());
                } finally {
                    if (output != null)
                        try {
                            output.close();
                        } catch (IOException e) {
                        }
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                refreshData();
            }

        }
    }


    /**
     * A {@link android.support.v4.app.FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(RetailElement.BuildingType.values()[position]);
        }

        @Override
        public int getCount() {
            return RetailElement.BuldingTypes.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return RetailElement.BuldingTypes[position];
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */

        public PlaceholderFragment() {

        }

        public static PlaceholderFragment newInstance(RetailElement.BuildingType sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber.ordinal());
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(android.R.layout.list_content, container, false);
            final ListView lst = (ListView) rootView.findViewById(android.R.id.list);

            lst.setAdapter(RetailElementsManager.getDefaultInstance().new RetailElementsAdapter(getActivity(),
                    RetailElement.BuildingType.values()[getArguments().getInt(ARG_SECTION_NUMBER)]));

            lst.setOnItemClickListener(((MainActivity2) getActivity()).itemClickListener);
            return rootView;
        }
    }


}
