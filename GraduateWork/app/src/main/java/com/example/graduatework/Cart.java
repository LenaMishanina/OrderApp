package com.example.graduatework;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graduatework.Common.Common;
import com.example.graduatework.adapter.MyAdapterCart;
import com.example.graduatework.database.Database;
import com.example.graduatework.database.Order;
import com.example.graduatework.database.Request;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference database;

    public TextView txtTotalPrice;
    Button btnPlaceOrder;

    List<Order> cart = new ArrayList<>();
    MyAdapterCart adapter;

    //activity_set_order
    TextInputEditText edtName, edtPhone, edtAddress, edtComment;
    RadioGroup radioGroup;
    RadioButton radioBtnCash, radioBtnCashless;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        database = FirebaseDatabase.getInstance().getReference("Requests");

        txtTotalPrice =(TextView) findViewById(R.id.total);
        btnPlaceOrder=(Button)findViewById(R.id.btnPlaceOrder);


        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cart.size() > 0)
                    showAlertDialog();
                else
                    Toast.makeText(Cart.this, "Корзина пуста", Toast.LENGTH_SHORT).show();
            }


        });

        recyclerView=(RecyclerView) findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadListFood();

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

    @SuppressLint("NonConstantResourceId")
    public void checkRadioBtn(View v){
//        int radioId=radioGroup.getCheckedRadioButtonId();
//        radioButton=findViewById(radioId);

        boolean checked = ((RadioButton) v).isChecked();
        String payment_opt="";
        switch (v.getId()){
            case R.id.radio_cash:
                if (checked)
                    payment_opt = "Наличные";
                break;
            case R.id.radio_cashless:
                if (checked)
                    payment_opt = "Безналичные";
                break;
        }
        Toast.makeText(getApplicationContext(), payment_opt, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("CutPasteId")
    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("Оформление заказа");


        //activity_set_order
        LayoutInflater inflater = this.getLayoutInflater();
        @SuppressLint("InflateParams") View SetOrder = inflater.inflate(R.layout.set_order,null);
        edtName = (TextInputEditText) SetOrder.findViewById(R.id.edtNameSO);
        edtPhone = (TextInputEditText) SetOrder.findViewById(R.id.edtPhoneSO);
        edtAddress = (TextInputEditText) SetOrder.findViewById(R.id.edtAddressSO);
        edtComment = (TextInputEditText) SetOrder.findViewById(R.id.edtCommentSO);
        radioGroup = (RadioGroup) SetOrder.findViewById(R.id.radio_group);
        radioBtnCash = (RadioButton) SetOrder.findViewById(R.id.radio_cash);
        radioBtnCashless = (RadioButton) SetOrder.findViewById(R.id.radio_cashless);


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




        edtName.setText(Common.currentUser.getName());
        edtPhone.setText(Common.currentUser.getPhone());

        alertDialog.setView(SetOrder);
        alertDialog.setPositiveButton("Оформить заказ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (TextUtils.isEmpty(edtName.getText().toString()) || TextUtils.isEmpty(edtPhone.getText().toString()) || TextUtils.isEmpty(edtAddress.getText().toString())){
//                    //radio get selected
//                    int radioId = radioGroup.getCheckedRadioButtonId();
//                    radioButton = findViewById(radioId);
//                    Log.v("SELECTED RADIO BTN", radioButton.getText().toString());
//                    Toast.makeText(Cart.this, radioButton.getText(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "Обязательное поле", Toast.LENGTH_SHORT).show();

                } else {
//                    //radio get selected
//                    int radioId = radioGroup.getCheckedRadioButtonId();
//                    radioButton = findViewById(radioId);
                    String selectedRadioBtn = "";
                    if (radioBtnCash.isChecked())
                        selectedRadioBtn = radioBtnCash.getText().toString();
                    else if (radioBtnCashless.isChecked())
                        selectedRadioBtn = radioBtnCashless.getText().toString();

                    Request request = new Request(
                            Common.currentUser.getPhone(),
                            Common.currentUser.getName(),
                            edtAddress.getText().toString(),
                            txtTotalPrice.getText().toString(),
                            "0", //status
                            edtComment.getText().toString(),
                            selectedRadioBtn,
                            cart
                    );

                    //submit to firebase
                    database.child(String.valueOf(System.currentTimeMillis())).setValue(request);

                    //delete cart
                    new Database(getBaseContext()).cleanCart();
                    finish();
                }
            }

        });

        alertDialog.setNegativeButton("Назад", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();


    }

    private void loadListFood(){
        cart = new Database(this).getCarts();
        adapter=new MyAdapterCart(cart,this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        //total price
        int total = 0;
        for (Order order : cart) {
            total += ( (Integer.parseInt(order.getPrice())) * (Integer.parseInt(order.getQuantity())) );
        }

        txtTotalPrice.setText(String.valueOf(total));
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getTitle().equals(Common.DELETE))
            deleteCart(item.getOrder());
        return true;
    }

    private void deleteCart(int position){
        cart.remove(position);
        new Database(this).cleanCart();
        for (Order item : cart)
            new Database(this).addToCart(item);
        loadListFood();
    }
}