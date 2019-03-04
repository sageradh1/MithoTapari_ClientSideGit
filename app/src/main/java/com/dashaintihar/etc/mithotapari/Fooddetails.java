package com.dashaintihar.etc.mithotapari;

import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.dashaintihar.etc.mithotapari.Database.Database;
import com.dashaintihar.etc.mithotapari.Model.Food;
import com.dashaintihar.etc.mithotapari.Model.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Fooddetails extends AppCompatActivity {

    TextView food_name,food_description,food_price;
    ImageView img_food;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;

    String foodID="";

    FirebaseDatabase database;
    DatabaseReference foods;

    Food currentFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fooddetails);

        //Firebase
        database = FirebaseDatabase.getInstance();
        foods = database.getReference("Food");

        //Init view
        numberButton = (ElegantNumberButton) findViewById(R.id.number_button);
        btnCart = (FloatingActionButton)findViewById(R.id.btnCart);
        food_name =(TextView)findViewById(R.id.food_name);
        food_price =(TextView) findViewById(R.id.food_price);
        food_description=(TextView)findViewById(R.id.food_description);

        img_food =(ImageView)findViewById(R.id.img_food);

        collapsingToolbarLayout =(CollapsingToolbarLayout)findViewById(R.id.collapsing);

        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Database(getBaseContext()).addToCart(new Order(
                        foodID,
                        currentFood.getName(),
                        numberButton.getNumber(),
                        currentFood.getPrice(),
                        currentFood.getDiscount()
                ));
                Toast.makeText(Fooddetails.this,"Added to the cart",Toast.LENGTH_SHORT);
            }
        });

        if (getIntent() != null){
            foodID = getIntent().getStringExtra("FoodID");
        }
        if(!foodID.isEmpty()){
            getFoodDetails(foodID);
        }


    }

    private void getFoodDetails(final String foodID) {
        foods.child(foodID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentFood= dataSnapshot.getValue(Food.class);

                //Set Image
                Picasso.with(getBaseContext()).load(currentFood.getImage()).into(img_food);

                collapsingToolbarLayout.setTitle(currentFood.getName());
                food_price.setText(currentFood.getPrice());
                food_name.setText(currentFood.getName());
                food_description.setText(currentFood.getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
