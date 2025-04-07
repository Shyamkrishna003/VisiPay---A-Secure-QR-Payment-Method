package com.example.bank_transaction;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class merchant_search_user extends AppCompatActivity implements JsonResponse, AdapterView.OnItemClickListener {

    ListView l1;
    EditText e1;
    SharedPreferences sh;
    String num;
    String[] fname, lname, number, acno, value, customer_id;
    public static String account_number, cus_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // will hide the title
        getSupportActionBar().hide(); // hide the title bar

        setContentView(R.layout.activity_merchant_search_user);

        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        l1 = findViewById(R.id.mobilelist);
        l1.setOnItemClickListener(this);
        e1 = findViewById(R.id.editTextTextPersonName7);

        e1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                num = e1.getText().toString();
                JsonReq JR = new JsonReq();
                JR.json_response = (JsonResponse) merchant_search_user.this;
                String q = "/searchuser?num=" + num + "&lid=" + sh.getString("log_id", "");
                q = q.replace(" ", "%20");
                JR.execute(q);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    @Override
    public void response(JSONObject jo) {
        try {
            String method = jo.getString("method");
            Log.d("pearl", method);

            if (method.equalsIgnoreCase("searchuser")) {
                String status = jo.getString("status");
                Log.d("pearl", status);

                JSONArray ja1 = (JSONArray) jo.getJSONArray("data");

                // Initialize arrays
                value = new String[ja1.length()];
                fname = new String[ja1.length()];
                lname = new String[ja1.length()];
                number = new String[ja1.length()];
//                acno = new String[ja1.length()];
                customer_id = new String[ja1.length()]; // Ensure initialization here

                if (status.equalsIgnoreCase("success")) {
                    for (int i = 0; i < ja1.length(); i++) {
                        customer_id[i] = ja1.getJSONObject(i).getString("customer_id");
                        number[i] = ja1.getJSONObject(i).getString("phone");
//                        acno[i] = ja1.getJSONObject(i).getString("account_number");
                        fname[i] = ja1.getJSONObject(i).getString("f_name");
                        lname[i] = ja1.getJSONObject(i).getString("l_name");
                        value[i] = "Name: " + fname[i] + " " + lname[i] + "\nNumber: " + number[i];
                    }
                    ArrayAdapter<String> ar = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, value);
                    l1.setAdapter(ar);
                } else if (status.equalsIgnoreCase("failed")) {
                    Toast.makeText(getApplicationContext(), "Checking..., Not found!", Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < ja1.length(); i++) {
                        customer_id[i] = ja1.getJSONObject(i).getString("customer_id");
                        number[i] = ja1.getJSONObject(i).getString("phone");
//                        acno[i] = ja1.getJSONObject(i).getString("account_number");
                        fname[i] = ja1.getJSONObject(i).getString("f_name");
                        lname[i] = ja1.getJSONObject(i).getString("l_name");
                        value[i] = "";
                    }
                    ArrayAdapter<String> ar = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, value);
                    l1.setAdapter(ar);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        // Validate customer_id
        if (customer_id == null || customer_id.length == 0) {
            Toast.makeText(this, "Customer ID not available", Toast.LENGTH_SHORT).show();
            return;
        }

//        account_number = acno[i];
        final CharSequence[] items = {"Request Money","Request Money through qr code", "Payment info", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(merchant_search_user.this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Request Money")) {
                    SharedPreferences.Editor e = sh.edit();
                    e.putString("customer_id", customer_id[i]); // Save specific customer_id
                    e.apply();
                    startActivity(new Intent(getApplicationContext(), merchant_request_money.class));
                }
                else if (items[item].equals("Request Money through qr code")) {
                    SharedPreferences.Editor e = sh.edit();
                    e.putString("customer_id", customer_id[i]); // Save specific customer_id
                    e.apply();
                    startActivity(new Intent(getApplicationContext(), generateqrcode.class));
                } else if (items[item].equals("Payment info")) {
                    SharedPreferences.Editor e = sh.edit();
                    e.putString("customer_id", customer_id[i]); // Save specific customer_id
                    e.apply(); // Use apply for asynchronous saving
                    startActivity(new Intent(getApplicationContext(), merchant_view_payment.class));
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void onBackPressed()
    {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent b=new Intent(getApplicationContext(),merchanthome.class);
        startActivity(b);
    }
}
