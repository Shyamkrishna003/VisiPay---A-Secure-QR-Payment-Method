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
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class user_view_transaction extends AppCompatActivity implements JsonResponse {

    ListView l1;
    SharedPreferences sh;
    String[] value,to_ac,type,amount,date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar

        setContentView(R.layout.activity_user_view_transaction
        );

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        l1=findViewById(R.id.transactions);
        String log_id = sh.getString("log_id", "");
        JsonReq JR = new JsonReq();
        JR.json_response = (JsonResponse) user_view_transaction.this;
        String q = "/user_view_transactions?login_id="+log_id;
        q = q.replace(" ", "%20");
        JR.execute(q);
    }
    @Override
    public void response(JSONObject jo) {
        try {
            String method = jo.getString("method");
            if (method.equalsIgnoreCase("view_transactions")) {
                String status = jo.getString("status");

                if (status.equalsIgnoreCase("success")) {
                    JSONArray ja1 = jo.getJSONArray("data");
                    String[] direction = new String[ja1.length()];
                    String[] otherAccount = new String[ja1.length()];
                    amount = new String[ja1.length()];
                    type = new String[ja1.length()];
                    date = new String[ja1.length()];
                    value = new String[ja1.length()];

                    for (int i = 0; i < ja1.length(); i++) {
                        JSONObject transaction = ja1.getJSONObject(i);

                        direction[i] = transaction.getString("transaction_direction");
                        otherAccount[i] = transaction.getString("other_account");
                        amount[i] = transaction.getString("amount");
                        type[i] = transaction.getString("type");
                        date[i] = transaction.getString("date_time");

                        value[i] = "Other Account: " + otherAccount[i] + "\nAmount: " + amount[i] +
                                " (" + direction[i] + ")" + "\nDate: " + date[i] + "    Type: " + type[i];
                    }

                    ArrayAdapter<String> ar = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, value);
                    l1.setAdapter(ar);
                } else if (status.equalsIgnoreCase("failed")) {
                    Toast.makeText(getApplicationContext(), "No History found!", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }



    public void onBackPressed()
    {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent b=new Intent(getApplicationContext(),UserHome.class);
        startActivity(b);
    }
}