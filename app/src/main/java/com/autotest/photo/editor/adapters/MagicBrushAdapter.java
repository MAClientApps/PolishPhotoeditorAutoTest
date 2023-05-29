package com.autotest.photo.editor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.autotest.photo.editor.R;
import com.autotest.photo.editor.listener.BrushMagicListener;
import com.autotest.photo.editor.draw.DrawModel;

import java.util.ArrayList;
import java.util.List;


public class MagicBrushAdapter extends RecyclerView.Adapter<MagicBrushAdapter.ViewHolder> {
    public static List<DrawModel> drawBitmapModels = new ArrayList();

    public BrushMagicListener brushMagicListener;
    private Context context;

    public int selectedColorIndex;

    public MagicBrushAdapter(Context context2, BrushMagicListener brushMagicListener2) {
        this.context = context2;
        this.brushMagicListener = brushMagicListener2;
        drawBitmapModels = lstDrawBitmapModel(context2);
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_brush, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.magicBrush.setImageResource(drawBitmapModels.get(i).getMainIcon());
        if (this.selectedColorIndex == i) {
            viewHolder.viewSelected.setVisibility(View.VISIBLE);
            return;
        }
        viewHolder.viewSelected.setVisibility(View.GONE);
    }

    public void setSelectedColorIndex(int i) {
        this.selectedColorIndex = i;
    }

    public int getItemCount() {
        return drawBitmapModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView magicBrush;
        View viewSelected;
        ViewHolder(View view) {
            super(view);
            this.viewSelected = view.findViewById(R.id.view_selected);
            this.magicBrush = view.findViewById(R.id.round_image_view_brush);
            this.magicBrush.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    MagicBrushAdapter.this.selectedColorIndex = ViewHolder.this.getLayoutPosition();
                    MagicBrushAdapter.this.brushMagicListener.onMagicChanged(MagicBrushAdapter.drawBitmapModels.get(MagicBrushAdapter.this.selectedColorIndex));
                    MagicBrushAdapter.this.notifyDataSetChanged();
                }
            });
        }
    }

    public static List<DrawModel> lstDrawBitmapModel(Context context2) {
        if (drawBitmapModels != null && !drawBitmapModels.isEmpty()) {
            return drawBitmapModels;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(Integer.valueOf(R.drawable.bg1));
        arrayList.add(Integer.valueOf(R.drawable.bg2));
        arrayList.add(Integer.valueOf(R.drawable.bg3));
        arrayList.add(Integer.valueOf(R.drawable.bg4));
        drawBitmapModels.add(new DrawModel(R.drawable.bg_icon, arrayList, false, context2));

        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(Integer.valueOf(R.drawable.bt1));
        arrayList2.add(Integer.valueOf(R.drawable.bt2));
        arrayList2.add(Integer.valueOf(R.drawable.bt3));
        arrayList2.add(Integer.valueOf(R.drawable.bt4));
        arrayList2.add(Integer.valueOf(R.drawable.bt5));
        arrayList2.add(Integer.valueOf(R.drawable.bt6));
        arrayList2.add(Integer.valueOf(R.drawable.bt7));
        arrayList2.add(Integer.valueOf(R.drawable.bt8));
        arrayList2.add(Integer.valueOf(R.drawable.bt9));
        drawBitmapModels.add(new DrawModel(R.drawable.bt_icon, arrayList2, true, context2));

        ArrayList arrayList3 = new ArrayList();
        arrayList3.add(Integer.valueOf(R.drawable.bb1));
        arrayList3.add(Integer.valueOf(R.drawable.bb2));
        arrayList3.add(Integer.valueOf(R.drawable.bb3));
        arrayList3.add(Integer.valueOf(R.drawable.bb4));
        drawBitmapModels.add(new DrawModel(R.drawable.bb_icon, arrayList3, true, context2));

        ArrayList arrayList11 = new ArrayList();
        arrayList11.add(Integer.valueOf(R.drawable.ba1));
        arrayList11.add(Integer.valueOf(R.drawable.ba2));
        arrayList11.add(Integer.valueOf(R.drawable.ba3));
        arrayList11.add(Integer.valueOf(R.drawable.ba4));
        arrayList11.add(Integer.valueOf(R.drawable.ba5));
        drawBitmapModels.add(new DrawModel(R.drawable.ba_icon, arrayList11, true, context2));

        ArrayList arrayList12 = new ArrayList();
        arrayList12.add(Integer.valueOf(R.drawable.b21));
        arrayList12.add(Integer.valueOf(R.drawable.b22));
        arrayList12.add(Integer.valueOf(R.drawable.b23));
        arrayList12.add(Integer.valueOf(R.drawable.b24));
        drawBitmapModels.add(new DrawModel(R.drawable.b26, arrayList12, true, context2));

        ArrayList arrayList4 = new ArrayList();
        arrayList4.add(Integer.valueOf(R.drawable.bw1));
        arrayList4.add(Integer.valueOf(R.drawable.bw2));
        arrayList4.add(Integer.valueOf(R.drawable.bw3));
        arrayList4.add(Integer.valueOf(R.drawable.bw4));
        arrayList4.add(Integer.valueOf(R.drawable.bw5));
        arrayList4.add(Integer.valueOf(R.drawable.bw6));
        arrayList4.add(Integer.valueOf(R.drawable.bw7));
        arrayList4.add(Integer.valueOf(R.drawable.bw8));
        arrayList4.add(Integer.valueOf(R.drawable.bw9));
        arrayList4.add(Integer.valueOf(R.drawable.bw10));
        arrayList4.add(Integer.valueOf(R.drawable.bw11));
        arrayList4.add(Integer.valueOf(R.drawable.bw12));
        arrayList4.add(Integer.valueOf(R.drawable.bw13));
        arrayList4.add(Integer.valueOf(R.drawable.bw14));
        arrayList4.add(Integer.valueOf(R.drawable.bw15));
        arrayList4.add(Integer.valueOf(R.drawable.bw16));
        arrayList4.add(Integer.valueOf(R.drawable.bw17));
        arrayList4.add(Integer.valueOf(R.drawable.bw18));
        arrayList4.add(Integer.valueOf(R.drawable.bw19));
        drawBitmapModels.add(new DrawModel(R.drawable.bw_icon, arrayList4, true, context2));

        ArrayList arrayList5 = new ArrayList();
        arrayList5.add(Integer.valueOf(R.drawable.bc1));
        arrayList5.add(Integer.valueOf(R.drawable.bc2));
        arrayList5.add(Integer.valueOf(R.drawable.bc3));
        arrayList5.add(Integer.valueOf(R.drawable.bc4));
        drawBitmapModels.add(new DrawModel(R.drawable.bc_icon, arrayList5, true, context2));

        ArrayList arrayList6 = new ArrayList();
        arrayList6.add(Integer.valueOf(R.drawable.be1));
        arrayList6.add(Integer.valueOf(R.drawable.be2));
        arrayList6.add(Integer.valueOf(R.drawable.be3));
        arrayList6.add(Integer.valueOf(R.drawable.be4));
        arrayList6.add(Integer.valueOf(R.drawable.be5));
        arrayList6.add(Integer.valueOf(R.drawable.be6));
        arrayList6.add(Integer.valueOf(R.drawable.be7));
        arrayList6.add(Integer.valueOf(R.drawable.be8));
        drawBitmapModels.add(new DrawModel(R.drawable.be_icon, arrayList6, true, context2));

        ArrayList arrayList7 = new ArrayList();
        arrayList7.add(Integer.valueOf(R.drawable.bh1));
        arrayList7.add(Integer.valueOf(R.drawable.bh2));
        arrayList7.add(Integer.valueOf(R.drawable.bh3));
        arrayList7.add(Integer.valueOf(R.drawable.bh4));
        arrayList7.add(Integer.valueOf(R.drawable.bh5));
        arrayList7.add(Integer.valueOf(R.drawable.bh6));
        arrayList7.add(Integer.valueOf(R.drawable.bh7));
        arrayList7.add(Integer.valueOf(R.drawable.bh8));
        drawBitmapModels.add(new DrawModel(R.drawable.bh_icon, arrayList7, true, context2));

        ArrayList arrayList8 = new ArrayList();
        arrayList8.add(Integer.valueOf(R.drawable.bd1));
        arrayList8.add(Integer.valueOf(R.drawable.bd2));
        arrayList8.add(Integer.valueOf(R.drawable.bd3));
        arrayList8.add(Integer.valueOf(R.drawable.bd4));
        arrayList8.add(Integer.valueOf(R.drawable.bd5));
        drawBitmapModels.add(new DrawModel(R.drawable.bd_icon, arrayList8, true, context2));

        ArrayList arrayList9 = new ArrayList();
        arrayList9.add(Integer.valueOf(R.drawable.bf1));
        arrayList9.add(Integer.valueOf(R.drawable.bf2));
        arrayList9.add(Integer.valueOf(R.drawable.bf3));
        arrayList9.add(Integer.valueOf(R.drawable.bf4));
        drawBitmapModels.add(new DrawModel(R.drawable.bf_icon, arrayList9, true, context2));

        ArrayList arrayList10 = new ArrayList();
        arrayList10.add(Integer.valueOf(R.drawable.bi1));
        arrayList10.add(Integer.valueOf(R.drawable.bi2));
        arrayList10.add(Integer.valueOf(R.drawable.bi3));
        arrayList10.add(Integer.valueOf(R.drawable.bi4));
        arrayList10.add(Integer.valueOf(R.drawable.bi5));
        arrayList10.add(Integer.valueOf(R.drawable.bi6));
        drawBitmapModels.add(new DrawModel(R.drawable.bi_icon, arrayList10, true, context2));

        return drawBitmapModels;
    }
}
