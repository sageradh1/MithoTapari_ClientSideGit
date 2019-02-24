package com.dashaintihar.etc.mithotapari;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dashaintihar.etc.mithotapari.Model.Food;
import com.dashaintihar.etc.mithotapari.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class innerFoodList extends AppCompatActivity {

    RecyclerView recycler_inner_foodlist;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference fooditems;

    String categoryID ="";
    FirebaseRecyclerAdapter<Food,FoodViewHolder> fadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inner_food_list);

        //Firebase
        database = FirebaseDatabase.getInstance();
        fooditems = database.getReference("Food");

        recycler_inner_foodlist = (RecyclerView)findViewById(R.id.recycler_inner_foodlist);
        recycler_inner_foodlist.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_inner_foodlist.setLayoutManager(layoutManager);

        if (getIntent() != null) {
            categoryID = getIntent().getStringExtra("CategoryID");
        }
        if (!categoryID.isEmpty() && categoryID !=null){
            loadFoodlist(categoryID);
        }



    }

    private void loadFoodlist(String categoryID) {
        FirebaseRecyclerOptions<Food> options =
                new FirebaseRecyclerOptions.Builder<Food>()
                        .setQuery(fooditems, Food.class)
                        .build();


        fadapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(options) {

            @Override
            public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.innerfood_list, parent, false);

                return new FoodViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder holder, int position, @NonNull Food model) {
                holder.inner_foodlist_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(holder.inner_foodlist_image);

            }
        };
        recycler_inner_foodlist.setAdapter(fadapter);
        fadapter.startListening();

    }

}
