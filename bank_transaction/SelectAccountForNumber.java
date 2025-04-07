package com.example.bank_transaction;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class SelectAccountForNumber extends AppCompatActivity implements JsonResponse, AdapterView.OnItemClickListener {

    ListView l1;
    SharedPreferences sh;
    String[] bank,value,acno,balance,acid;
    public  static  String ac_id,blnce,myacno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full sc
        setContentView(R.layout.activity_select_account_for_number);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        l1=findViewById(R.id.lvac);
        l1.setOnItemClickListener(this);


        JsonReq JR = new JsonReq();
        JR.json_response = (JsonResponse) SelectAccountForNumber.this;
        String q = "/viewmyac?lid="+sh.getString("log_id","");
        q = q.replace(" ", "%20");
        JR.execute(q);
    }

    @Override
    public void response(JSONObject jo) {
        try {

            String method = jo.getString("method");
            Log.d("pearl", method);

            if (method.equalsIgnoreCase("viewmyac")) {
                String status = jo.getString("status");
                if (status.equalsIgnoreCase("success")) {
//                    Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_LONG).show();

                    JSONArray ja1 = (JSONArray) jo.getJSONArray("data");
                    acid = new String[ja1.length()];
                    bank = new String[ja1.length()];
                    balance= new String[ja1.length()];
                    acno= new String[ja1.length()];

                    value = new String[ja1.length()];




                    for (int i = 0; i < ja1.length(); i++) {
                        acid[i] = ja1.getJSONObject(i).getString("account_id");
                        bank[i] = ja1.getJSONObject(i).getString("bank_name");
                        balance[i] = ja1.getJSONObject(i).getString("balance");
                        acno[i] = ja1.getJSONObject(i).getString("account_number");




                        value[i] = "Bank : " + bank[i] + "\nAccount no : " + acno[i] + "\nBalance : " + balance[i];
                    }
                    ArrayAdapter<String> ar = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, value);
                    l1.setAdapter(ar);


                } else  if (status.equalsIgnoreCase("failed")) {
                    Toast.makeText(getApplicationContext(), "No data, found!", Toast.LENGTH_SHORT).show();
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
        myacno=acno[i];
        ac_id=acid[i];
        blnce=balance[i];
        final CharSequence[] items = {"Proceed","Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(SelectAccountForNumber.this);
        // builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {


                if (items[item].equals("Proceed")) {

                    startActivity(new Intent(getApplicationContext(),MoneyTransferWithNumber.class));


                }



                else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }

            }

        });
        builder.show();
    }
}