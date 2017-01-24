package com.dfa.vinatrip;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class LocationFragment extends Fragment {

    CustomPagerAdapter customPagerAdapter;
    int[] mResources = {
            R.drawable.background1,
            R.drawable.background2,
            R.drawable.background3,
            R.drawable.background4
    };
    private RecyclerView rvProvinces;
    private ProvinceAdapter provinceAdapter;
    private List<Province> provinceList;
    private SwipeRefreshLayout srlReload;
    private ViewPager vpPhotoSlideShow;
    private TextView[] tvDots;
    private LinearLayout llDots;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // phải tạo ra view trước
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        findViewByIds(view);


        customPagerAdapter = new CustomPagerAdapter(getActivity());
        vpPhotoSlideShow.setAdapter(customPagerAdapter);
        addBottomDots();

        srlReload.setColorSchemeResources(R.color.colorIconWaiting1, R.color.colorIconWaiting2);

        provinceList = new ArrayList<>();
        provinceAdapter = new ProvinceAdapter(getActivity(), provinceList, srlReload);
        rvProvinces.setAdapter(provinceAdapter);

        if (isNetworkConnected()) {
            loadData();
        }

        srlReload.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isNetworkConnected()) {
                    provinceList.clear();
                    loadData();
                } else {
                    srlReload.setRefreshing(false);
                }
            }
        });

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvProvinces.setLayoutManager(staggeredGridLayoutManager);

        rvProvinces.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), rvProvinces, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getActivity(), "abc", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        return view;
    }

    public void addBottomDots() {
        tvDots = new TextView[7];
        llDots.removeAllViews();
        for (int i = 0; i < 7; i++) {
            tvDots[i] = new TextView(getActivity());
            // ký tự bullet
            tvDots[i].setText("\u2022");
            tvDots[i].setTextSize(35);
            tvDots[i].setTextColor(Color.WHITE);
            llDots.addView(tvDots[i]);
        }
    }

    public void findViewByIds(View view) {
        rvProvinces = (RecyclerView) view.findViewById(R.id.rvProvince);
        srlReload = (SwipeRefreshLayout) view.findViewById(R.id.srlReload);
        vpPhotoSlideShow = (ViewPager) view.findViewById(R.id.vpPhotoSlideShow);
        llDots = (LinearLayout) view.findViewById(R.id.llDots);
    }

    public void loadData() {
        LoadProvinceFromFirebase loadProvinceFromFirebase = new LoadProvinceFromFirebase(getActivity(), provinceList, provinceAdapter, srlReload);
        loadProvinceFromFirebase.execute();
    }

    public boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null;
    }

    public class CustomPagerAdapter extends PagerAdapter {
        Context context;
        LayoutInflater layoutInflater;

        public CustomPagerAdapter(Context context) {
            this.context = context;
            this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mResources.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = layoutInflater.inflate(R.layout.item_photo_slide_show, container, false);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.ivPhotoSlideShow);
            imageView.setImageResource(mResources[position]);
            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }
}
