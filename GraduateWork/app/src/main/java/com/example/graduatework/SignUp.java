package com.example.graduatework;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graduatework.database.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity {
    TextInputEditText edtPhone, edtName, edtPassword;
    TextView txtAlreadyLogin;
    Button btnSingUp;
    TextInputLayout txtInpLayoutPhoneReg,txtInpLayoutNameReg,txtInpLayoutPwdReg;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtPhone = (TextInputEditText) findViewById(R.id.edtPhone);
        edtName = (TextInputEditText) findViewById(R.id.edtName);
        edtPassword = (TextInputEditText) findViewById(R.id.edtPassword);
        btnSingUp = (Button) findViewById(R.id.btnSignUp);
        txtAlreadyLogin = (TextView) findViewById(R.id.txtAlreadyLogin);
        txtInpLayoutPhoneReg = (TextInputLayout) findViewById(R.id.txtInpLayoutPhoneReg);
        txtInpLayoutNameReg = (TextInputLayout) findViewById(R.id.txtInpLayoutNameReg);
        txtInpLayoutPwdReg = (TextInputLayout) findViewById(R.id.txtInpLayoutPwdReg);

        //init firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        txtAlreadyLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, SignIn.class);
                startActivity(intent);
            }
        });

        btnSingUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(edtPhone.getText())) {

                    if (!TextUtils.isEmpty(edtName.getText())) {

                        if (!TextUtils.isEmpty(edtPassword.getText())) {

                            final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                            mDialog.setMessage("Пожалуйста, подождите...");
                            mDialog.show();

                            table_user.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    //check if already exist
                                    if (snapshot.child(edtPhone.getText().toString()).exists()) {
                                        mDialog.dismiss();
                                        Toast.makeText(SignUp.this, "phone number already register", Toast.LENGTH_SHORT).show();
                                    } else {
                                        mDialog.dismiss();
                                        User user = new User(edtName.getText().toString(), edtPassword.getText().toString());
                                        table_user.child(edtPhone.getText().toString()).setValue(user);//create new user
                                        Toast.makeText(SignUp.this, "Sign Up succeed", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        } else {
                            txtInpLayoutPwdReg.setHelperText("Обязательное поле*");
                        }

                    } else {
                        txtInpLayoutNameReg.setHelperText("Обязательное поле*");
                    }
                }else {
                    txtInpLayoutPhoneReg.setHelperText("Обязательное поле*");
                }
            }
        });
    }
}