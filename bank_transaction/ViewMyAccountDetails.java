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

public class ViewMyAccountDetails extends AppCompatActivity implements JsonResponse, AdapterView.OnItemClickListener {

    ListView l1;
    SharedPreferences sh;
    String[] bank,value,acno,balance,acid;
    public  static  String ac_id,blnce,myacno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar

        setContentView(R.layout.activity_view_my_account_details);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        l1=findViewById(R.id.lvac);
        l1.setOnItemClickListener(this);


        JsonReq JR = new JsonReq();
        JR.json_response = (JsonResponse) ViewMyAccountDetails.this;
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
        final CharSequence[] items = {"Add Money","Money transfer","history","Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(ViewMyAccountDetails.this);
        // builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {


                if (items[item].equals("Money transfer")) {

                    startActivity(new Intent(getApplicationContext(),MoneyTransfer.class));


                } else if (items[item].equals("history")) {

                    startActivity(new Intent(getApplicationContext(),TransactionHistory.class));

                }

                else if (items[item].equals("Add Money")) {

                    startActivity(new Intent(getApplicationContext(),AddMoney.class));

                }



                else if (items[item].equals("Cancel")) {
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
        Intent b=new Intent(getApplicationContext(),UserHome.class);
        startActivity(b);
    }
}