package com.example.bank_transaction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class TransactionHistory extends AppCompatActivity implements JsonResponse {

    ListView l1;
    SharedPreferences sh;
    String[] value,ac1,ac2,amount,date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar

        setContentView(R.layout.activity_transaction_history);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());



        l1=findViewById(R.id.history);

        JsonReq JR = new JsonReq();
        JR.json_response = (JsonResponse) TransactionHistory.this;
        String q = "/viewmyhistory?myac="+ViewMyAccountDetails.myacno;
        q = q.replace(" ", "%20");
        JR.execute(q);
    }

    @Override
    public void response(JSONObject jo) {
        try {

            String method = jo.getString("method");
            Log.d("pearl", method);

            if (method.equalsIgnoreCase("viewmyhistory")) {
                String status = jo.getString("status");
                if (status.equalsIgnoreCase("success")) {
//                    Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_LONG).show();

                    JSONArray ja1 = (JSONArray) jo.getJSONArray("data");
                    ac1 = new String[ja1.length()];
                    ac2= new String[ja1.length()];
                    amount= new String[ja1.length()];
                    date= new String[ja1.length()];
//                    lid = new String[ja1.length()];
                    value = new String[ja1.length()];


                    for (int i = 0; i < ja1.length(); i++) {
                        ac1[i] = ja1.getJSONObject(i).getString("from_ac");
                        ac2[i] = ja1.getJSONObject(i).getString("to_ac");
                        amount[i] = ja1.getJSONObject(i).getString("amount");
                        date[i] = ja1.getJSONObject(i).getString("date_time");



                        value[i] = "Transaction With "+ac1[i]+" & "+ac2[i]+" of Rs"+amount[i]+ " on "+date[i];
                    }
                    ArrayAdapter<String> ar = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, value);
                    l1.setAdapter(ar);

//                    CustomUser a = new CustomUser(this, name, num);
//                    l1.setAdapter(a);
                } else  if (status.equalsIgnoreCase("failed")) {
                    Toast.makeText(getApplicationContext(), "No History, found!", Toast.LENGTH_SHORT).show();
                }
            }

        }
        catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void onBackPressed()
    {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent b=new Intent(getApplicationContext(),ViewMyAccountDetails.class);
        startActivity(b);
    }
}