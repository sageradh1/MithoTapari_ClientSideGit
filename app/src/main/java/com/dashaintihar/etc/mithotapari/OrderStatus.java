package com.dashaintihar.etc.mithotapari;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dashaintihar.etc.mithotapari.Common.Common;
import com.dashaintihar.etc.mithotapari.Interface.ItemClickListener;
import com.dashaintihar.etc.mithotapari.Model.Food;
import com.dashaintihar.etc.mithotapari.Model.Request;
import com.dashaintihar.etc.mithotapari.ViewHolder.FoodViewHolder;
import com.dashaintihar.etc.mithotapari.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class OrderStatus extends AppCompatActivity {

    RecyclerView recycler_orderStatus;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    FirebaseRecyclerAdapter<Request, OrderViewHolder> oadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        recycler_orderStatus = (RecyclerView)findViewById(R.id.recycler_orderStatus);
        recycler_orderStatus.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_orderStatus.setLayoutManager(layoutManager);

        loadOrders(Common.currentUser.getPhone());

    }

    private void loadOrders(String phone) {
        FirebaseRecyclerOptions<Request> options =
                new FirebaseRecyclerOptions.Builder<Request>()
                        .setQuery(requests.orderByChild("phone").equalTo(phone), Request.class)
                        .build();


        oadapter = new FirebaseRecyclerAdapter<Request,OrderViewHolder>(options) {
            @Override
            public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.order_layout, parent, false);

                return new OrderViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull Request model) {
                holder.order_id.setText(model.getName());
                holder.order_address.setText(model.getAddress());
                holder.order_phone.setText(model.getPhone());
                holder.order_status.setText(convertIntToStatus(model.getStatus()));

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(OrderStatus.this,"Haha Clicked ",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        recycler_orderStatus.setAdapter(oadapter);
        oadapter.startListening();
    }

    private String convertIntToStatus(String status) {
        String realstatus = "";
        if (status.equals("0")) {
            realstatus = "Status: Order has been Placed";
        } else if (status.equals("1")) {
            realstatus = "Status: On The Way";
        } else if (status.equals("2")) {
            realstatus = "Status: Delivered";
        }
        return realstatus;
    }

}
