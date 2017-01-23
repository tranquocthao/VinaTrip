package com.dfa.vinatrip;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
    private ProgressDialog pdWaiting;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // phải tạo ra view trước
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        rvProvinces = (RecyclerView) view.findViewById(R.id.rvProvince);
        provinceList = new ArrayList<>();

        // truyền nó vào Asyntask để show, Adapter để tắt khi Picasso load ảnh hoàn tất
        pdWaiting = new ProgressDialog(getActivity());

        provinceAdapter = new ProvinceAdapter(getActivity(), provinceList, pdWaiting);
        rvProvinces.setAdapter(provinceAdapter);

        LoadProvinceFromFirebase loadProvinceFromFirebase = new LoadProvinceFromFirebase(getActivity(), provinceList, provinceAdapter, pdWaiting);
        loadProvinceFromFirebase.execute();

//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
//        rvProvinces.setLayoutManager(linearLayoutManager);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvProvinces.setLayoutManager(staggeredGridLayoutManager);

        return view;
    }
}
