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

public class merchant_view_payment extends AppCompatActivity implements JsonResponse {

    ListView l1;
    SharedPreferences sh;
    String[] value,amount,date,sendertype,receivertype,status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar

        setContentView(R.layout.activity_merchant_view_payment);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String customer_id = sh.getString("customer_id", "Guest"); // Default: "Guest"

        l1=findViewById(R.id.payment);
        String log_id = sh.getString("log_id", "");
        JsonReq JR = new JsonReq();
        JR.json_response = (JsonResponse) merchant_view_payment.this;
        String q = "/view_payment?login_id="+log_id+"&customer_id="+customer_id;
        q = q.replace(" ", "%20");
        JR.execute(q);
    }

    @Override
    public void response(JSONObject jo) {
        try {

            String method = jo.getString("method");
            Log.d("pearl", method);

            if (method.equalsIgnoreCase("view_payment")) {
                String stat = jo.getString("stat");
                if (stat.equalsIgnoreCase("success")) {
//                    Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_LONG).show();

                    JSONArray ja1 = (JSONArray) jo.getJSONArray("data");
                    amount = new String[ja1.length()];
                    date= new String[ja1.length()];
                    sendertype= new String[ja1.length()];
                    receivertype= new String[ja1.length()];
                    status=new String[ja1.length()];
//                    lid = new String[ja1.length()];
                    value = new String[ja1.length()];


                    for (int i = 0; i < ja1.length(); i++) {
                        amount[i] = ja1.getJSONObject(i).getString("amount");
                        date[i] = ja1.getJSONObject(i).getString("date");
                        sendertype[i] = ja1.getJSONObject(i).getString("sendertype");
                        receivertype[i] = ja1.getJSONObject(i).getString("receivertype");
                        status[i] = ja1.getJSONObject(i).getString("status");
//                        "\nsendertype: "+sendertype[i]+ "\nreceivertype: "+receivertype[i]+
                        value[i] = "Amount: "+amount[i]+ "\nDate: "+date[i]+  "     Status: "+status[i];
                    }
                    ArrayAdapter<String> ar = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, value);
                    l1.setAdapter(ar);

//                    CustomUser a = new CustomUser(this, name, num);
//                    l1.setAdapter(a);
                } else  if (stat.equalsIgnoreCase("Failed")) {
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
        Intent b=new Intent(getApplicationContext(),merchanthome.class);
        startActivity(b);
    }
}