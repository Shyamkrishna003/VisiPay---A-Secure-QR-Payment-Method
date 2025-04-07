package com.example.bank_transaction;

import androidx.appcompat.app.AppCompatActivity;

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

import org.json.JSONObject;

public class AddMoney extends AppCompatActivity implements JsonResponse {

    EditText e1;
    Button b1;
    SharedPreferences sh;
    String amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full sc
        setContentView(R.layout.activity_add_money);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        e1=(EditText) findViewById(R.id.wallet);
        b1=(Button) findViewById(R.id.add);

        TextView t1 =findViewById(R.id.textView11);
        TextView t2 =findViewById(R.id.balance);
        t1.setText("Account No : "+ViewMyAccountDetails.myacno);
        t2.setText("Balance : "+ViewMyAccountDetails.blnce);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                amount=e1.getText().toString();

                JsonReq JR=new JsonReq();
                JR.json_response=(JsonResponse) AddMoney.this;
                String q="/accountbalanceadd?account="+ViewMyAccountDetails.myacno+"&amount="+amount;
                q = q.replace(" ", "%20");
                JR.execute(q);

            }
        });
    }

    @Override
    public void response(JSONObject jo) {
        try {
            String status = jo.getString("status");
            Log.d("pearl", status);

            if (status.equalsIgnoreCase("success")) {

                Toast.makeText(getApplicationContext(), "Amount credited!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), ViewMyAccountDetails.class));

            }
        }
        catch (Exception e) {
            // TODO: handle exception

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