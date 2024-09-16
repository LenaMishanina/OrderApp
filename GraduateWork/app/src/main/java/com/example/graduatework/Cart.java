package com.example.graduatework;

import static com.google.android.gms.common.util.CollectionUtils.listOf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graduatework.Common.Common;
import com.example.graduatework.adapter.MyAdapterCart;
import com.example.graduatework.database.Database;
import com.example.graduatework.database.Order;
import com.example.graduatework.database.Request;
import com.google.android.gms.common.api.Status;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
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
    TextInputEditText edtName, edtPhone, edtComment;
    AppCompatAutoCompleteTextView edtAddress;
    RadioGroup radioGroup;
    RadioButton radioBtnCash, radioBtnCashless;

//    Place shippingAddress;


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

                if (cart.size() > 0) {
                    try {
                        showAlertDialog();
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                }
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

    private void showAlertDialog() throws PackageManager.NameNotFoundException {

//        String apiKey = "AIzaSyADW2XTigd2fimOqd3VFX2QXJ-ihLLhE0E";
//        if (!Places.isInitialized()) {
//            Places.initialize(getApplicationContext(), apiKey);
//        }


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("Оформление заказа");


        //activity_set_order
        LayoutInflater inflater = this.getLayoutInflater();
        @SuppressLint("InflateParams") View SetOrder = inflater.inflate(R.layout.set_order,null);
        edtName = (TextInputEditText) SetOrder.findViewById(R.id.edtNameSO);
        edtPhone = (TextInputEditText) SetOrder.findViewById(R.id.edtPhoneSO);
        edtAddress = (AppCompatAutoCompleteTextView) SetOrder.findViewById(R.id.edtAddressSO);
        edtComment = (TextInputEditText) SetOrder.findViewById(R.id.edtCommentSO);
        radioGroup = (RadioGroup) SetOrder.findViewById(R.id.radio_group);
        radioBtnCash = (RadioButton) SetOrder.findViewById(R.id.radio_cash);
        radioBtnCashless = (RadioButton) SetOrder.findViewById(R.id.radio_cashless);


        //ENTER ADDRESS
        String[] addressList =getResources().getStringArray(R.array.strArrayAddress);
        ArrayAdapter<String> adapterArray =new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, addressList);
        edtAddress.setAdapter(adapterArray);



        //ADDRESS PLACE ADI
//        PlaceAutocompleteFragment edtAddress = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

//        AutocompleteSupportFragment edtAddress = (AutocompleteSupportFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.place_autocomplete_fragment);

//        edtAddress.getView().findViewById(com.google.android.gms.location.places.R.id.place_autocomplete_search_button).setVisibility(View.GONE);
//        ((EditText) edtAddress.getView().findViewById(com.google.android.gms.location.places.R.id.place_autocomplete_search_input)).setHint("Введите адрес");
//        ((EditText) edtAddress.getView().findViewById(com.google.android.gms.location.places.R.id.place_autocomplete_search_input)).setTextSize(20);
//        //get address from place
//        edtAddress.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(Place place) {
//                shippingAddress=place;
//            }
//
//            @Override
//            public void onError(Status status) {
//                Log.v("ERROR", "error in place api");
////                Log.e("ERROR", status.getStatusMessage().toString());
//            }
//        });
//

//        java.util.List<com.google.android.libraries.places.api.model.Place.Field> placeFields = new java.util.List<com.google.android.libraries.places.api.model.Place.Field>;
//        placeFields.add(0,Place.Field.NAME);
//        placeFields.add(1,Place.Field.ADDRESS);
//        placeFields.add(2,Place.Field.PHONE_NUMBER);
//        placeFields.add(3,Place.Field.LAT_LNG);
//        placeFields.add(4,Place.Field.OPENING_HOURS);
//        placeFields.add(5,Place.Field.RATING);
//        placeFields.add(6,Place.Field.USER_RATINGS_TOTAL);


        // Specify the types of place data to return.
//        assert edtAddress != null;
//        edtAddress.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        // Set up a PlaceSelectionListener to handle the response.
//        edtAddress.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onError(@NonNull Status status) {
//                 TODO: Handle the error.
//                Log.i("ERROR", "An error occurred: " + status);
//            }

//            @Override
//            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
//                Log.i("PLACE", "Place: " + place.getName() + ", " + place.getId());
//
//            }
//        });


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
//                    Log.v("SELECTED RADIO BTN", radioButton.getText().toString());
//                    Toast.makeText(Cart.this, radioButton.getText(), Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getApplicationContext(), "Обязательное поле", Toast.LENGTH_SHORT).show();
                    Log.v("Required", "Обязательное поле");

                } else {
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
//                            String.format("%s","%s",shippingAddress.getLatLng().latitude,shippingAddress.getLatLng().longitude),
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

//                getFragmentManager().beginTransaction()
//                        .remove(getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment))
//                        .commit();
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