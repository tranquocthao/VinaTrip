package com.dfa.vinatrip;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class LoadProvinceFromFirebase extends AsyncTask<Void, Province, Void> {

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private List<Province> provinceList;
    private ProvinceAdapter provinceAdapter;
    private Context context;
    private SwipeRefreshLayout srlReload;

    public LoadProvinceFromFirebase(Context context, List<Province> provinceList, ProvinceAdapter provinceAdapter, SwipeRefreshLayout srlReload) {
        this.provinceList = provinceList;
        this.provinceAdapter = provinceAdapter;
        this.context = context;
        this.srlReload = srlReload;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        // nếu không có mạng thì sẽ auto không chạy vào hàm này
        databaseReference.child("Province").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String name, title, linkPhoto;
                name = dataSnapshot.child("name").getValue().toString();
                title = dataSnapshot.child("title").getValue().toString();
                linkPhoto = dataSnapshot.child("linkPhoto").getValue().toString();
                Province province = new Province(name, title, linkPhoto);
                publishProgress(province);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // quá trình tải về rất nhanh, thời gian chờ có thể là tg tải ảnh của Picasso
        srlReload.setRefreshing(true);
    }

    @Override
    protected void onProgressUpdate(Province... values) {
        super.onProgressUpdate(values);
        provinceList.add(values[0]);
        provinceAdapter.notifyDataSetChanged();
    }
}
