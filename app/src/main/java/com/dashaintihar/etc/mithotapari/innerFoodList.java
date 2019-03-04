package com.dashaintihar.etc.mithotapari;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dashaintihar.etc.mithotapari.Interface.ItemClickListener;
import com.dashaintihar.etc.mithotapari.Model.Category;
import com.dashaintihar.etc.mithotapari.Model.Food;
import com.dashaintihar.etc.mithotapari.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class innerFoodList extends AppCompatActivity {

    RecyclerView recycler_inner_foodlist;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference fooditems;

    String categoryID ="";

    //total view adapter
    FirebaseRecyclerAdapter<Food,FoodViewHolder> fadapter;

    //Search functionality adapter
    FirebaseRecyclerAdapter<Food, FoodViewHolder> searchAdapter;
    List<String> suggestList =new ArrayList<>();

    MaterialSearchBar materialSearchBar ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inner_food_list);

        //Firebase
        database = FirebaseDatabase.getInstance();
        fooditems = database.getReference("Food");

        recycler_inner_foodlist = findViewById(R.id.recycler_inner_foodlist);
        recycler_inner_foodlist.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_inner_foodlist.setLayoutManager(layoutManager);

        if (getIntent() != null) {
            categoryID = getIntent().getStringExtra("CategoryID");
        }
        if (!categoryID.isEmpty() && categoryID !=null){
            loadFoodlist(categoryID);
        }


        materialSearchBar = findViewById(R.id.searchBar);
        materialSearchBar.setHint("Enter a food item to search");

        loadsuggestions();
        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);

        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //while the user types new name
                List<String> suggest = new ArrayList<String>();
                for (String search:suggestList)
                {
                    if (search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                    {
                        suggest.add(search);
                    }
                }
                materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                //when the seach bar is close, restore orignal view
                if (!enabled){
                    recycler_inner_foodlist.setAdapter(fadapter);
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                //when search is complete, show the result of search adapter
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

    }

    private void startSearch(CharSequence text) {
        FirebaseRecyclerOptions<Food> options =
                new FirebaseRecyclerOptions.Builder<Food>()
                        .setQuery(fooditems.orderByChild("Name").equalTo(text.toString()), Food.class)
                        .build();


        searchAdapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(options) {

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
                final Food clickedItem = model;
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(innerFoodList.this,"Directing towards "+clickedItem.getName(),Toast.LENGTH_SHORT).show();
                        Intent direct =new Intent(innerFoodList.this,Fooddetails.class);
                        direct.putExtra("FoodID",searchAdapter.getRef(position).getKey());
                        startActivity(direct);
                    }
                });

            }
        };
        recycler_inner_foodlist.setAdapter(searchAdapter);
        searchAdapter.startListening();
    }

    private void loadsuggestions() {
        fooditems.orderByChild("MenuID").equalTo(categoryID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                            Food item = postSnapshot.getValue(Food.class);
                            suggestList.add(item.getName());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void loadFoodlist(String categoryID) {
        FirebaseRecyclerOptions<Food> options =
                new FirebaseRecyclerOptions.Builder<Food>()
                        .setQuery(fooditems.orderByChild("MenuID").equalTo(categoryID), Food.class)
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
                final Food clickedItem = model;
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(innerFoodList.this,"Directing towards "+clickedItem.getName(),Toast.LENGTH_SHORT).show();
                        Intent direct =new Intent(innerFoodList.this,Fooddetails.class);
                        direct.putExtra("FoodID",fadapter.getRef(position).getKey());
                        startActivity(direct);
                    }
                });

            }
        };
        recycler_inner_foodlist.setAdapter(fadapter);
        fadapter.startListening();

    }

}
