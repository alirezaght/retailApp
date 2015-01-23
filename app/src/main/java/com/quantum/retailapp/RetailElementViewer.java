package com.quantum.retailapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class RetailElementViewer extends FragmentActivity {
    public static final String ADAPTER_TYPE = "adapter_type";
    public static final String SELECTED_ELEMENT_ID = "selected_id";


    private ViewPager pager;
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retail_element_viewer);

        RetailElement.BuildingType optionType = RetailElement.BuildingType.values()[getIntent().getExtras().getInt(ADAPTER_TYPE)];
        pager = (ViewPager) findViewById(R.id.pager);
        RetailElementsManager.RetailElementsAdapter elementsAdapter = RetailElementsManager.getDefaultInstance().new RetailElementsAdapter(this, optionType);
        ArrayList tmp = elementsAdapter.getAllItems();

        pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), elementsAdapter);
        pager.setAdapter(pagerAdapter);

        pager.setPageTransformer(true, new DepthPageTransformer());


        int selectedElement = getIntent().getExtras().getInt(SELECTED_ELEMENT_ID);
        pager.setCurrentItem(selectedElement);

    }


    /**
     * A {@link android.support.v4.app.FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        RetailElementsManager.RetailElementsAdapter _adapter;

        public SectionsPagerAdapter(FragmentManager fm,
                                    RetailElementsManager.RetailElementsAdapter adapter) {
            super(fm);
            _adapter = adapter;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(((RetailElement) _adapter.getItem(position)).getId());
        }

        @Override
        public int getCount() {
            return _adapter.getCount();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return _adapter.getBuidlingType().toString();
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
        public static PlaceholderFragment newInstance(int id) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, id);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.element_viewer_fragment, container, false);
            TextView txt1 = (TextView) rootView.findViewById(R.id.textView1);
            TextView txt2= (TextView) rootView.findViewById(R.id.textView2);
            TextView txt3 = (TextView) rootView.findViewById(R.id.textView3);

            int id = getArguments().getInt(ARG_SECTION_NUMBER);
            RetailElement element = RetailElementsManager.getDefaultInstance().getElementById(id);
            txt1.setText(RetailElement.BuldingTypes[element.getBuildingType().ordinal()]);
            txt2.setText(element.getTitleString());
            txt3.setText(element.getOtherSpecsString());
            return rootView;
        }
    }

    public class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }


}
