package com.dashaintihar.etc.mithotapari;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dashaintihar.etc.mithotapari.Common.Common;
import com.dashaintihar.etc.mithotapari.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ru.dimorinny.floatingtextbutton.FloatingTextButton;

public class MainActivity extends AppCompatActivity {

    EditText phonenumber,userpassword;
    FloatingTextButton btnsignin,btnsignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phonenumber = (EditText) findViewById(R.id.phonenumber);
        userpassword =(EditText)findViewById(R.id.userpassword);

        btnsignin =(FloatingTextButton)findViewById(R.id.action_button1);
        btnsignup = (FloatingTextButton)findViewById(R.id.action_button2);

        //Init Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog mDialogue = new ProgressDialog(MainActivity.this);
                mDialogue.setMessage("Signing in.....");
                mDialogue.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(phonenumber.getText().toString()).exists()){
                            //stop waiting sign
                            mDialogue.dismiss();

                            User user = dataSnapshot.child(phonenumber.getText().toString()).getValue(User.class);
                            user.setPhone(phonenumber.getText().toString());

                            if (user.getPassword().equals(userpassword.getText().toString())){
                                Toast.makeText(MainActivity.this, "Signed in Successfully", Toast.LENGTH_SHORT).show();
                                Intent homeintent =new Intent(MainActivity.this,navhome.class);
                                Common.currentUser = user;
                                startActivity(homeintent);
                                finish();

                            }
                            else{
                                Toast.makeText(MainActivity.this,"Username or Password didnt match",Toast.LENGTH_SHORT).show();
                            }

                        }
                        else{
                            mDialogue.dismiss();
                            Toast.makeText(MainActivity.this, "Sorry the user doesnot exit", Toast.LENGTH_SHORT).show();

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signup = new Intent(MainActivity.this,SigninUp.class);
                startActivity(signup);
            }
        });
    }
}
