package com.example.graduatework;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.util.LocaleData;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graduatework.Common.Common;
import com.example.graduatework.database.User;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class SignIn extends AppCompatActivity {
    TextInputEditText edtPhone, edtPassword;
    TextView txtToSignUp;
    Button btnSignIn;
    CheckBox checkBox;

    TextInputLayout txtInpLayoutPhone, txtInpLayoutPwd;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        edtPhone = (TextInputEditText) findViewById(R.id.edtPhone);
        edtPassword = (TextInputEditText) findViewById(R.id.edtPassword);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        checkBox = (CheckBox) findViewById(R.id.cb_rem_me);
        txtToSignUp = (TextView) findViewById(R.id.txtToRegistration);

        txtInpLayoutPhone = (TextInputLayout) findViewById(R.id.txtInpLayoutPhone);
        txtInpLayoutPwd = (TextInputLayout) findViewById(R.id.txtInpLayoutPwd);

        //Init paper

        Paper.init(this);

        //Init Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        txtToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignIn.this, SignUp.class);
                startActivity(intent);
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //save user
                if (checkBox.isChecked()){
                    Paper.book().write(Common.USER_KEY, edtPhone.getText().toString());
                    Paper.book().write(Common.PASSWD_KEY, edtPassword.getText().toString());
                }

                if (!TextUtils.isEmpty(edtPhone.getText())) {

                    if (!TextUtils.isEmpty(edtPassword.getText())) {

                        final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                        mDialog.setMessage("Пожалуйста, подождите...");
                        mDialog.show();

                        table_user.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                                    mDialog.dismiss();
                                    //Get user info
                                    User user = dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);//get user
                                    user.setPhone(edtPhone.getText().toString());
                                    if (user.getPassword().equals(edtPassword.getText().toString())) {

                                        Intent home = new Intent(SignIn.this, Home.class);
                                        Common.currentUser = user;
                                        startActivity(home);
                                        finish();


                                    } else {
                                        Toast.makeText(SignIn.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    mDialog.dismiss();
                                    Toast.makeText(SignIn.this, "User not exist", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    } else{
                        txtInpLayoutPwd.setHelperText("Обязательное поле*");
                    }

                }else {
                    txtInpLayoutPhone.setHelperText("Обязательное поле*");
                }

            }
        });
    }
}