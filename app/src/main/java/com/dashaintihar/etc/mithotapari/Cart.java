package com.dashaintihar.etc.mithotapari;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dashaintihar.etc.mithotapari.Common.Common;
import com.dashaintihar.etc.mithotapari.Database.Database;
import com.dashaintihar.etc.mithotapari.Model.Order;
import com.dashaintihar.etc.mithotapari.Model.Request;
import com.dashaintihar.etc.mithotapari.ViewHolder.CartAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ru.dimorinny.floatingtextbutton.FloatingTextButton;

public class Cart extends AppCompatActivity {

    RecyclerView listcart;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    TextView total;
    FloatingTextButton btnPlaceOrder;

    List<Order> cart = new ArrayList<>();

    CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        listcart =(RecyclerView)findViewById(R.id.listcart);
        listcart.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(this);
        listcart.setLayoutManager(layoutManager);

        total =(TextView)findViewById(R.id.total);
        btnPlaceOrder =(FloatingTextButton)findViewById(R.id.btnPlaceOrder);

        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert();
            }
        });

        loadlist();
    }

    private void showAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("One More Step");
        alertDialog.setMessage("Enter the delivery address : ");

        final EditText edtAddress = new EditText(Cart.this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        edtAddress.setLayoutParams(layoutParams);
        alertDialog.setView(edtAddress);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_24dp);

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Create new request
                Request request = new Request(
                        Common.currentUser.getPhone(),
                        Common.currentUser.getName(),
                        edtAddress.getText().toString(),
                        total.getText().toString(),
                        cart
                );

                //Submitting to Firebase
                //Using current time in millisec as unique key
                requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);

                new Database(getBaseContext()).cleanCart();
                Toast.makeText(Cart.this, "Thank you !! Your order has been placed", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }

    private void loadlist() {
        cart = new Database(this).getCarts();
        cartAdapter = new CartAdapter(cart, this);
        listcart.setAdapter(cartAdapter);

        //calculate total price
        int totalvalue=0;
        for (Order order:cart){
            totalvalue += ((Integer.parseInt(order.getPrice()))-(Integer.parseInt(order.getDiscount())))*(Integer.parseInt(order.getQuantity()));
        }

        Locale locale = new Locale("en","NP");
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);

        total.setText(format.format(totalvalue));

    }
}
