package com.dfa.vinatrip;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProvinceAdapter extends RecyclerView.Adapter<ProvinceAdapter.ProvinceViewHolder> {
    private List<Province> provinceList;
    private LayoutInflater layoutInflater;
    private Context context;
    private SwipeRefreshLayout srlReload;

    public ProvinceAdapter(Context context, List<Province> provinceList, SwipeRefreshLayout srlReload) {
        this.provinceList = provinceList;
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.srlReload = srlReload;
    }

    @Override
    public ProvinceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_province2, parent, false);
        return new ProvinceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProvinceAdapter.ProvinceViewHolder holder, int position) {
        //get song in mSong via position
        Province province = provinceList.get(position);

        //bind data to viewholder
        holder.tvName.setText(province.getName());
        holder.tvTitle.setText(province.getTitle());
        // tỉ lệ ảnh trên mọi screen sẽ giống nhau
        holder.ivAvatar.setScaleType(ImageView.ScaleType.FIT_XY);
        Picasso.with(context).load(province.getLinkPhoto()).transform(new RoundedTransformation(20, 4)).into(holder.ivAvatar,
                new Callback() {
                    @Override
                    public void onSuccess() {
                        // Tắt icon waiting khi load xong
                        srlReload.setRefreshing(false);
                    }

                    @Override
                    public void onError() {
                    }
                });
    }

    @Override
    public int getItemCount() {
        return provinceList.size();
    }

    public static class ProvinceViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName, tvTitle;
        private ImageView ivAvatar;

        public ProvinceViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            ivAvatar = (ImageView) itemView.findViewById(R.id.ivAvatar);
        }
    }
}
