package com.example.bank_transaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

public class merchant_request_money extends AppCompatActivity implements JsonResponse {

    EditText e1,e2,e3;
    Button b1,b2,b3;
    TextView t1;
    public static String amount,bbb,logid,usertype,phone;
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full sc
        setContentView(R.layout.activity_merchant_request_money);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String customer_id = sh.getString("customer_id", ""); // Default: "Guest"
        String log_id = sh.getString("log_id", "");
        e1=(EditText) findViewById(R.id.amount1);
        b1=(Button) findViewById(R.id.loginbtn);

        t1=(TextView) findViewById(R.id.textView3);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                amount = e1.getText().toString();

                if (amount.equalsIgnoreCase("")) {
                    e1.setError("Enter the Amount");
                    e1.setFocusable(true);
                } else {

                    JsonReq JR = new JsonReq();
                    JR.json_response = (JsonResponse) merchant_request_money.this;
                    String q = "/merchant_request_money?amount=" + amount+"&customer_id="+customer_id+"&log_id="+log_id ;
                    q = q.replace(" ", "%20");
                    JR.execute(q);
                }
            }
        });
    }
    @Override
    public void response(JSONObject jo) {
        try {
            String status = jo.getString("status");
            Log.d("pearl", status);

            if (status.equalsIgnoreCase("success")) {

                Toast.makeText(getApplicationContext(), "Request Success", Toast.LENGTH_LONG).show();

                startActivity(new Intent(getApplicationContext(), merchant_search_user.class));
            }
            else {
                Toast.makeText(getApplicationContext(), "Request failed", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), merchant_request_money.class));
            }
        }
        catch (Exception e) {
            // TODO: handle exception

            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void onBackPressed()
    {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent b=new Intent(getApplicationContext(),merchant_search_user.class);
        startActivity(b);
    }
}