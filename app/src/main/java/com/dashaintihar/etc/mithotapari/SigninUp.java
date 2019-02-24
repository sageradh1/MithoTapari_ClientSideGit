package com.dashaintihar.etc.mithotapari;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dashaintihar.etc.mithotapari.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ru.dimorinny.floatingtextbutton.FloatingTextButton;

public class SigninUp extends AppCompatActivity {
    EditText usernamesignup,userpasswordsignup,phonenumbersignup;
    FloatingTextButton action_button2signup;

    //Init Firebase


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_up);
        usernamesignup = (EditText)findViewById(R.id.usernamesignup);
        userpasswordsignup = (EditText)findViewById(R.id.userpasswordsignup);
        phonenumbersignup=(EditText)findViewById(R.id.phonenumbersignup);

        action_button2signup = (FloatingTextButton)findViewById(R.id.action_button2signup);

        //Init database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        action_button2signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog mDialogue = new ProgressDialog(SigninUp.this);
                mDialogue.setMessage("Adding username in database....");
                mDialogue.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(phonenumbersignup.getText().toString()).exists()){
                            mDialogue.dismiss();
                            Toast.makeText(SigninUp.this,"Sorry Phone number already registered",Toast.LENGTH_SHORT);
                        }
                        else {
                            mDialogue.dismiss();
                            User user = new User(usernamesignup.getText().toString(),userpasswordsignup.getText().toString());
                            table_user.child(phonenumbersignup.getText().toString()).setValue(user);
                            Toast.makeText(SigninUp.this,"New id saved",Toast.LENGTH_SHORT);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }

}
