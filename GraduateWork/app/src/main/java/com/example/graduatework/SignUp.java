package com.example.graduatework;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
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

import java.util.Objects;

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


        edtPhone.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode != KeyEvent.KEYCODE_DEL) {

                    edtPhone.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @SuppressLint("SetTextI18n")
                        @Override
                        public void afterTextChanged(Editable editable) {

                            setMaskPhone(edtPhone);

                        }
                    });

                }
                else{
                    edtPhone.setSelection(edtPhone.getText().length());
                    if (edtPhone.getText().length() == 16 || edtPhone.getText().length() == 13)
                        edtPhone.setText(edtPhone.getText().subSequence(0, edtPhone.getText().length()-2));
                    else if (edtPhone.getText().length() == 9)
                        edtPhone.setText(edtPhone.getText().subSequence(0, edtPhone.getText().length()-3));
                    else if (edtPhone.getText().length() == 4 || edtPhone.getText().length() == 3)
                        edtPhone.getText().clear();
                }

                edtPhone.setSelection(edtPhone.getText().length());
                return false;
            }
        });


    }

    private void setMaskPhone(TextInputEditText edt) {

        if ( edt.getText().length() == 1 && !Objects.equals(edt.getText().toString(), "8") && !Objects.equals(edt.getText().toString(), "+") && !TextUtils.isEmpty(edt.getText())  ){
            edt.getText().clear();
        } else if (edt.getText().length() == 2 && !Objects.equals(edt.getText().toString(), "+7") && !TextUtils.isEmpty(edt.getText())){
            edt.getText().clear();
        } else {

            if (Objects.equals(edt.getText().toString(), "8")) {
                edt.setText("+7 (");
                edt.setSelection(edt.getText().length());
            }
            if (Objects.equals(edt.getText().toString(), "+7")) {
                edt.setText(edt.getText().toString() + " (");
                edt.setSelection(edt.getText().length());
            }
            if (edt.getText().length() >= 4 && Objects.equals(edt.getText().subSequence(0, 4).toString(), "+7 (")) {
                if (edt.getText().length() == 7) {
                    edt.setText(edt.getText().toString() + ") ");
                    edt.setSelection(edt.getText().length());
                }
                if (edt.getText().length() == 12) {
                    edt.setText(edt.getText().toString() + "-");
                    edt.setSelection(edt.getText().length());
                }
                if (edt.getText().length() == 15) {
                    edt.setText(edt.getText().toString() + "-");
                    edt.setSelection(edt.getText().length());
                }
            }
        }


    }



}