package com.dashaintihar.etc.mithotapari.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dashaintihar.etc.mithotapari.Interface.ItemClickListener;
import com.dashaintihar.etc.mithotapari.R;

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView inner_foodlist_name;
    public ImageView inner_foodlist_image;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    public FoodViewHolder(@NonNull View itemView) {
        super(itemView);

        inner_foodlist_name = (TextView)itemView.findViewById(R.id.inner_foodlist_name);
        inner_foodlist_image = (ImageView) itemView.findViewById(R.id.inner_foodlist_image);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }
}
