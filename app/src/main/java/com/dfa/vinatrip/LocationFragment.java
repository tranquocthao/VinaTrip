package com.dfa.vinatrip;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

public class LocationFragment extends Fragment {

    private RecyclerView rvProvinces;
    private ProvinceAdapter provinceAdapter;
    private List<Province> provinceList;
    private ProgressBar pbWaiting;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // phải tạo ra view trước
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        rvProvinces = (RecyclerView) view.findViewById(R.id.rvProvince);

        if (!isNetworkConnected()) {
            return view;
        }

        provinceList = new ArrayList<>();

        // truyền nó vào Asyntask để show, Adapter để tắt khi Picasso load ảnh hoàn tất
        pbWaiting = (ProgressBar) view.findViewById(R.id.pbWaiting);

        provinceAdapter = new ProvinceAdapter(getActivity(), provinceList, pbWaiting);
        rvProvinces.setAdapter(provinceAdapter);

        LoadProvinceFromFirebase loadProvinceFromFirebase = new LoadProvinceFromFirebase(getActivity(), provinceList, provinceAdapter, pbWaiting);
        loadProvinceFromFirebase.execute();

//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
//        rvProvinces.setLayoutManager(linearLayoutManager);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvProvinces.setLayoutManager(staggeredGridLayoutManager);

        return view;
    }

    public boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null;
    }
}
