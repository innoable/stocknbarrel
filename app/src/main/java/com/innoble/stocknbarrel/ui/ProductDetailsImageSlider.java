package com.innoble.stocknbarrel.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.innoble.stocknbarrel.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailsImageSlider extends AppCompatActivity {

    private ViewPager mPager;
    private String[] imageUris;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details_slider);

        // Instantiate a ViewPager and a PagerAdapter.

        imageUris = getIntent().getStringArrayExtra("imageUris");
        mPager = (ViewPager) findViewById(R.id.details_gallerySlider);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
    }


    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private List <ProductDetailsGalleryFragment> fragments;

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
            fragments = new ArrayList<>();
            ProductDetailsGalleryFragment fragment;

            for(int i = 0 ; i < imageUris.length; i ++){
                fragment = new ProductDetailsGalleryFragment();
                fragment.setImageUri(imageUris[i]);
                fragments.add(fragment);
            }

        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return imageUris.length;
        }
    }











    public static class ProductDetailsGalleryFragment extends Fragment{


        public void setImageUri(String imageUri) {
            Bundle bundle = new Bundle();
            bundle.putString("imageUri",imageUri);
            setArguments(bundle);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            ViewGroup rootView = (ViewGroup) inflater.inflate(
                    R.layout.fragment_product_details_slider, container, false);

            ImageView image = (ImageView)rootView.findViewById(R.id.details_gallerySlider_image);
            Picasso.with(getContext())
                    .load(getArguments().getString("imageUri"))
                    .error(R.drawable.empty_photo)
                    .placeholder(R.drawable.progress_animation)
                    .fit()
                    .into(image);

            return rootView;
        }

    }


}
