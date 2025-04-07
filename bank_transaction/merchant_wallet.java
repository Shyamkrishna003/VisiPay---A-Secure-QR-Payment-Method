package com.example.bank_transaction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class merchant_wallet extends AppCompatActivity implements JsonResponse {
    TextView t1; // TextView to display balance
    SharedPreferences sh;
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); // will hide the title
        getSupportActionBar().hide(); // hide the title bar

        setContentView(R.layout.activity_merchant_wallet);

        // Initialize TextView and Button
        t1 = findViewById(R.id.textView18);
        b1 = findViewById(R.id.button4);

        // Get the login ID from SharedPreferences
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String log_id = sh.getString("log_id", "");

        // Fetch the balance when the activity starts
        fetchBalance(log_id);

        // Set up button click to navigate to merchant_withdraw_money
        b1.setOnClickListener(v -> {
            Intent intent = new Intent(merchant_wallet.this, merchant_withdraw_money.class);
            startActivity(intent);
        });
    }

    private void fetchBalance(String log_id) {
        // Create a request to fetch the balance
        JsonReq jsonRequest = new JsonReq();
        jsonRequest.json_response = merchant_wallet.this;
        String query = "/merchant_wallet?login_id=" + log_id + "&amount=0"; // Pass 0 to just fetch balance
        query = query.replace(" ", "%20");
        jsonRequest.execute(query);
    }

    @Override
    public void response(JSONObject jo) {
        try {
            // Log the full API response
            System.out.println("API Response: " + jo.toString());

            // Check status
            if (jo.has("status") && jo.getString("status").equalsIgnoreCase("Added")) {
                JSONArray data = jo.optJSONArray("data");

                if (data != null && data.length() > 0) {
                    JSONObject walletInfo = data.getJSONObject(0);
                    String balance = walletInfo.getString("amount");

                    t1.setText("₹" + balance); // Display the balance
                } else {
                    t1.setText("Balance fetched, but no data returned.");
                }
            } else {
                String status = jo.optString("status", "unknown");
                t1.setText("Failed to fetch balance. Status: " + status);
            }
        } catch (Exception e) {
            e.printStackTrace();
            t1.setText("Error processing response. Check logs.");
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

