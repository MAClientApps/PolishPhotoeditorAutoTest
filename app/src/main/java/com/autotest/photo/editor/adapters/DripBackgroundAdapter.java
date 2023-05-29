package com.autotest.photo.editor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.autotest.photo.editor.R;
import com.autotest.photo.editor.layout.DripLayout;
import com.autotest.photo.editor.listener.BackgroundItemListener;

import java.util.ArrayList;

public class DripBackgroundAdapter extends RecyclerView.Adapter<DripBackgroundAdapter.ViewHolder> {
    public BackgroundItemListener clickListener;
    public int selectedPos = 0;
    private ArrayList<String> dripItemList = new ArrayList<>();
    Context mContext ;

    public DripBackgroundAdapter(Context context) {
        mContext = context;
    }

    public void addData(ArrayList<String> arrayList) {
        this.dripItemList.clear();
        this.dripItemList.addAll(arrayList);
        notifyDataSetChanged();
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_neon, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.mSelectedBorder.setVisibility(position == selectedPos ? View.VISIBLE : View.GONE);
        String sb2 = "file:///android_asset/drip/background/" + dripItemList.get(position) +".webp";
        Glide.with(mContext)
                .load(sb2)
                .fitCenter()
                .into(holder.imageViewItem);
    }

    public int getItemCount() {
        return this.dripItemList.size();
    }

    public void setClickListener(DripLayout clickListener) {
        this.clickListener = clickListener;
    }

    public ArrayList<String> getItemList() {
        return dripItemList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        View mSelectedBorder , lockPro;
        ImageView imageViewItem;

        ViewHolder(View view) {
            super(view);
            imageViewItem = view.findViewById(R.id.imageViewItem1);
            mSelectedBorder = view.findViewById(R.id.selectedBorder);
            view.setTag(view);
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            int p = selectedPos;
            selectedPos = getAdapterPosition();
            notifyItemChanged(p);
            notifyItemChanged(selectedPos);
            clickListener.onBackgroundListClick(view, getAdapterPosition());
        }
    }
}
