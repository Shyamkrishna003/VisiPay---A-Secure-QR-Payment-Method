package com.example.bank_transaction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

public class merchant_withdraw_money extends AppCompatActivity implements JsonResponse {
    EditText amountInput; // To get the entered amount
    Button withdrawButton; // To initiate the withdraw action
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_merchant_withdraw_money);

        // Initialize Views
        amountInput = findViewById(R.id.editTextTextPersonName9);
        withdrawButton = findViewById(R.id.button7);

        // Get SharedPreferences to retrieve login ID
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String log_id = sh.getString("log_id", "");

        // Set up button click listener
        withdrawButton.setOnClickListener(v -> {
            String amount = amountInput.getText().toString().trim();

            // Validate amount
            if (amount.isEmpty() || Double.parseDouble(amount) <= 0) {
                Toast.makeText(merchant_withdraw_money.this, "Enter a valid amount", Toast.LENGTH_SHORT).show();
                return;
            }

            // Call method to withdraw money
            withdrawMoney(log_id, amount);
        });
    }

    private void withdrawMoney(String log_id, String amount) {
        // Create a JSON request
        JsonReq jsonRequest = new JsonReq();
        jsonRequest.json_response = merchant_withdraw_money.this;

        // Construct the API query
        String query = "/merchant_withdraw_money?login_id=" + log_id + "&amount=" + amount;
        query = query.replace(" ", "%20");

        // Execute the request
        jsonRequest.execute(query);
    }

    @Override
    public void response(JSONObject jo) {
        try {
            // Log the full API response
            System.out.println("API Response: " + jo.toString());

            // Check API response status
            if (jo.has("status") && jo.getString("status").equalsIgnoreCase("success")) {
                Toast.makeText(this, "Withdrawal successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),merchant_wallet.class));
            } else {
                String message = jo.optString("message", "Failed to withdraw money.");
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error processing response. Check logs.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onBackPressed()
    {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent b=new Intent(getApplicationContext(),merchant_wallet.class);
        startActivity(b);
    }
}


