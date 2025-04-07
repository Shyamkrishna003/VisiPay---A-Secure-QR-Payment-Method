package com.example.bank_transaction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class user_wallet extends AppCompatActivity implements JsonResponse {
    TextView t1;
    EditText e1;
    SharedPreferences sh;
    Button b1, b2; // Buttons for "Add Money" and "Withdraw Money"
    public static String amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_user_wallet);

        // Initialize views
        t1 = (TextView) findViewById(R.id.textView18);
        e1 = (EditText) findViewById(R.id.editTextNumber);
        b1 = (Button) findViewById(R.id.button12); // Add Money button
        b2 = (Button) findViewById(R.id.button25); // Withdraw Money button

        // Get the login ID from SharedPreferences
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String log_id = sh.getString("log_id", "");

        // Fetch the balance when the activity starts
        fetchBalance(log_id);

        // Add Money button click listener
        b1.setOnClickListener(view -> {
            amount = e1.getText().toString().trim();
            if (validateAmount(amount)) {
                updateWallet(log_id, amount, "add");
            }
        });

        // Withdraw Money button click listener
        b2.setOnClickListener(view -> {
            amount = e1.getText().toString().trim();
            if (validateAmount(amount)) {
                updateWallet(log_id, amount, "withdraw");
            }
        });
    }

    private boolean validateAmount(String amount) {
        if (amount.isEmpty()) {
            e1.setError("Enter a valid amount");
            e1.requestFocus();
            return false;
        }

        try {
            int amountValue = Integer.parseInt(amount);
            if (amountValue <= 0) {
                e1.setError("Amount must be greater than 0");
                e1.requestFocus();
                return false;
            } else if (amountValue > 1000000) {
                e1.setError("Maximum limit is ₹10,00,000");
                e1.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            e1.setError("Invalid amount format");
            e1.requestFocus();
            return false;
        }

        return true;
    }

    private void updateWallet(String log_id, String amount, String action) {
        // Create API query based on action (add or withdraw)
        String query = "/user_wallet?login_id=" + log_id + "&amount=" + amount + "&action=" + action;
        query = query.replace(" ", "%20");

        // Execute API request
        JsonReq jsonRequest = new JsonReq();
        jsonRequest.json_response = user_wallet.this;
        jsonRequest.execute(query);
    }

    private void fetchBalance(String log_id) {
        // Create a request to fetch the balance
        String query = "/user_wallet?login_id=" + log_id + "&amount=0&action=fetch"; // Use action=fetch for balance
        query = query.replace(" ", "%20");

        JsonReq jsonRequest = new JsonReq();
        jsonRequest.json_response = user_wallet.this;
        jsonRequest.execute(query);
    }

    @Override
    public void response(JSONObject jo) {
        try {
            // Log the full API response
            System.out.println("API Response: " + jo.toString());

            // Check status
            String status = jo.optString("status", "error");
            if (status.equalsIgnoreCase("Added") || status.equalsIgnoreCase("Withdrawn")) {
                Toast.makeText(this, status + " successfully!", Toast.LENGTH_SHORT).show();
                fetchBalance(sh.getString("log_id", ""));
            } else if (status.equalsIgnoreCase("insufficient balance")) {
                Toast.makeText(this, "Insufficient balance!", Toast.LENGTH_SHORT).show();
            } else if (status.equalsIgnoreCase("fetch")) {
                JSONArray data = jo.optJSONArray("data");
                if (data != null && data.length() > 0) {
                    JSONObject walletInfo = data.getJSONObject(0);
                    String balance = walletInfo.getString("amount");
                    t1.setText("₹" + balance);
                }
            } else {
                Toast.makeText(this, "Error: " + status, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error processing response. Check logs.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onBackPressed() {
        // Navigate back to UserHome
        super.onBackPressed();
        Intent b = new Intent(getApplicationContext(), UserHome.class);
        startActivity(b);
    }
}
