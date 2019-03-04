package com.dashaintihar.etc.mithotapari.ViewHolder;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.dashaintihar.etc.mithotapari.Interface.ItemClickListener;
import com.dashaintihar.etc.mithotapari.Model.Order;
import com.dashaintihar.etc.mithotapari.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView cart_item_name,cart_price;
    public ImageView cart_item_image;

    private ItemClickListener itemClickListener;

    public void setCart_item_name(TextView cart_item_name) {
        this.cart_item_name = cart_item_name;
    }

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        cart_item_name = (TextView)itemView.findViewById(R.id.cart_item_name);
        cart_item_image = (ImageView) itemView.findViewById(R.id.cart_item_image);
        cart_price = (TextView) itemView.findViewById(R.id.cart_price);

    }

    @Override
    public void onClick(View view) {

    }
}

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {
    private List<Order> listData = new ArrayList<>();
    private Context context;

    public CartAdapter(List<Order> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemview = inflater.inflate(R.layout.card_layout,viewGroup,false);
        return new CartViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i) {
        TextDrawable drawable = TextDrawable.builder().buildRound(""+listData.get(i).getQuantity(), Color.RED);
        cartViewHolder.cart_item_image.setImageDrawable(drawable);

        Locale locale = new Locale("en","NP");
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);

        int price = ((Integer.parseInt(listData.get(i).getPrice()))-(Integer.parseInt(listData.get(i).getDiscount())))*(Integer.parseInt(listData.get(i).getQuantity()));
        cartViewHolder.cart_price.setText(format.format(price));
        cartViewHolder.cart_item_name.setText(listData.get(i).getProductName());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
