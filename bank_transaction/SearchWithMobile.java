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

public class SearchWithMobile extends AppCompatActivity implements JsonResponse, AdapterView.OnItemClickListener {

    ListView l1;
    EditText e1;
    SharedPreferences sh;
    String num;
    String[] fname,lname,number,acno,value;
    public static String account_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full sc
        setContentView(R.layout.activity_search_with_mobile);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        l1 = findViewById(R.id.mobile);
        l1.setOnItemClickListener(this);
        e1 = findViewById(R.id.editTextTextPersonName5);

        e1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                num = e1.getText().toString();
                JsonReq JR = new JsonReq();
                JR.json_response = (JsonResponse) SearchWithMobile.this;
                String q = "/searchnumbers?num=" + num+"&lid="+sh.getString("log_id","") ;
                q = q.replace(" ", "%20");
                JR.execute(q);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void response(JSONObject jo) {
        try {

            String method = jo.getString("method");
            Log.d("pearl", method);

            if (method.equalsIgnoreCase("searchnumbers")) {
                String status = jo.getString("status");
                if (status.equalsIgnoreCase("success")) {
//                    Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_LONG).show();

                    JSONArray ja1 = (JSONArray) jo.getJSONArray("data");

                    value = new String[ja1.length()];
                    fname = new String[ja1.length()];
                    lname= new String[ja1.length()];
                    number= new String[ja1.length()];
                    acno= new String[ja1.length()];



                    for (int i = 0; i < ja1.length(); i++) {
                        number[i] = ja1.getJSONObject(i).getString("phone");
                        acno[i] = ja1.getJSONObject(i).getString("account_number");
                        fname[i] = ja1.getJSONObject(i).getString("f_name");
                        lname[i] = ja1.getJSONObject(i).getString("l_name");



                        value[i] = "Name : " + fname[i]+lname[i] + "\nNumber : " + number[i];

                    }
                    ArrayAdapter<String> ar = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, value);
                    l1.setAdapter(ar);

//                    CustomUser a = new CustomUser(this, name, num);
//                    l1.setAdapter(a);
                } else  if (status.equalsIgnoreCase("failed")) {


                    Toast.makeText(getApplicationContext(), "Checking..., Not found!", Toast.LENGTH_SHORT).show();
                    JSONArray ja1 = (JSONArray) jo.getJSONArray("data");

                    value = new String[ja1.length()];
                    fname = new String[ja1.length()];
                    lname= new String[ja1.length()];
                    number= new String[ja1.length()];
                    acno= new String[ja1.length()];



                    for (int i = 0; i < ja1.length(); i++) {
                        number[i] = ja1.getJSONObject(i).getString("phone");
                        acno[i] = ja1.getJSONObject(i).getString("account_number");
                        fname[i] = ja1.getJSONObject(i).getString("f_name");
                        lname[i] = ja1.getJSONObject(i).getString("l_name");



                            value[i] = "";

                    }
                    ArrayAdapter<String> ar = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, value);
                    l1.setAdapter(ar);
                }
            }

        }
        catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        account_number=acno[i];
        final CharSequence[] items = {"Transfer Money", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(SearchWithMobile.this);
        // builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Transfer Money")) {

                    startActivity(new Intent(getApplicationContext(), SelectAccountForNumber.class));

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }


            }

        });
        builder.show();
    }
}