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

public class user_search extends AppCompatActivity implements JsonResponse, AdapterView.OnItemClickListener {

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

        setContentView(R.layout.activity_user_search);

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
                JR.json_response = (JsonResponse) user_search.this;
                String q = "/user_search?num=" + num + "&lid=" + sh.getString("log_id", "");
                q = q.replace(" ", "%20");
                JR.execute(q);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    String[] user_type; // New array to hold user type

    @Override
    public void response(JSONObject jo) {
        try {
            String method = jo.getString("method");
            if (method.equalsIgnoreCase("user_search")) {
                String status = jo.getString("status");
                JSONArray ja1 = jo.getJSONArray("data");

                // Initialize arrays
                value = new String[ja1.length()];
                fname = new String[ja1.length()];
                lname = new String[ja1.length()];
                number = new String[ja1.length()];
                customer_id = new String[ja1.length()];
                user_type = new String[ja1.length()]; // Initialize user_type array

                if (status.equalsIgnoreCase("success")) {
                    for (int i = 0; i < ja1.length(); i++) {
                        customer_id[i] = ja1.getJSONObject(i).getString("user_id");
                        number[i] = ja1.getJSONObject(i).getString("phone");
                        fname[i] = ja1.getJSONObject(i).getString("f_name");
                        lname[i] = ja1.getJSONObject(i).getString("l_name");
                        user_type[i] = ja1.getJSONObject(i).getString("user_type"); // Get user type
                        value[i] = "Name: " + fname[i] + " " + lname[i] + "\nNumber: " + number[i];
                    }
                    ArrayAdapter<String> ar = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, value);
                    l1.setAdapter(ar);
                } else {
                    Toast.makeText(getApplicationContext(), "No users found!", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        // Validate customer_id and user_type
        if (user_type[i].equalsIgnoreCase("customer")) {
            final CharSequence[] items = {"Request Money", "Payment info", "Cancel"};

            AlertDialog.Builder builder = new AlertDialog.Builder(user_search.this);
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (items[item].equals("Request Money")) {
                        SharedPreferences.Editor e = sh.edit();
                        e.putString("customer_id", customer_id[i]); // Save specific customer_id
                        e.apply();
                        startActivity(new Intent(getApplicationContext(), user_request_money.class));
                    } else if (items[item].equals("Payment info")) {
                        SharedPreferences.Editor e = sh.edit();
                        e.putString("customer_id", customer_id[i]); // Save specific customer_id
                        e.apply();
                        startActivity(new Intent(getApplicationContext(), user_view_payment.class));
                    } else if (items[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        } else {
            final CharSequence[] items = {"Payment info", "Cancel"};

            AlertDialog.Builder builder = new AlertDialog.Builder(user_search.this);
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (items[item].equals("Payment info")) {
                        SharedPreferences.Editor e = sh.edit();
                        e.putString("customer_id", customer_id[i]); // Save specific customer_id
                        e.apply();
                        startActivity(new Intent(getApplicationContext(), user_view_payment.class));
                    } else if (items[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();        }
    }

    public void onBackPressed()
    {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent b=new Intent(getApplicationContext(),UserHome.class);
        startActivity(b);
    }
}
