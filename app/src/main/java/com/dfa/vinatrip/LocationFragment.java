package com.dfa.vinatrip;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class LocationFragment extends Fragment {

    private RecyclerView rvProvinces;
    private ProvinceAdapter provinceAdapter;
    private List<Province> provinceList;
    private SwipeRefreshLayout srlReload;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // phải tạo ra view trước
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        rvProvinces = (RecyclerView) view.findViewById(R.id.rvProvince);
        srlReload = (SwipeRefreshLayout) view.findViewById(R.id.srlReload);
        srlReload.setColorSchemeResources(R.color.colorIconWaiting1, R.color.colorIconWaiting2);

        provinceList = new ArrayList<>();
        provinceAdapter = new ProvinceAdapter(getActivity(), provinceList, srlReload);
        rvProvinces.setAdapter(provinceAdapter);

        if (isNetworkConnected()) {
            LoadProvinceFromFirebase loadProvinceFromFirebase = new LoadProvinceFromFirebase(getActivity(), provinceList, provinceAdapter, srlReload);
            loadProvinceFromFirebase.execute();
        }

        srlReload.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isNetworkConnected()) {
                    provinceList.clear();
                    LoadProvinceFromFirebase loadProvinceFromFirebase = new LoadProvinceFromFirebase(getActivity(), provinceList, provinceAdapter, srlReload);
                    loadProvinceFromFirebase.execute();
                } else {
                    srlReload.setRefreshing(false);
                }
            }
        });
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
