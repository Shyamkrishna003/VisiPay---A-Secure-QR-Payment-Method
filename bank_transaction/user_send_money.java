package com.example.bank_transaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

public class user_send_money extends AppCompatActivity implements JsonResponse {

    SharedPreferences sh;
    private TextView amountTextView;
    private TextView recipientNameTextView; // New TextView for recipient name
    public static String amount, transfer_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_send_money);

        // Initialize views
        amountTextView = findViewById(R.id.textView22); // Rs. TextView
        recipientNameTextView = findViewById(R.id.textViewRecipientName); // New TextView for recipient name

        // Retrieve amount and transfer_id from previous activity
        amount = user_pay_request.amt;
        transfer_id = user_pay_request.transferid;

        // Check if amount is available and display it
        if (amount != null && !amount.isEmpty()) {
            amountTextView.setText("Rs. " + user_pay_request.amt);
        } else {
            Toast.makeText(this, "Amount not found!", Toast.LENGTH_SHORT).show();
        }

        // Fetch recipient information
        fetchRecipientInfo(transfer_id);

        // Send Money Button Click Listener
        Button b1 = findViewById(R.id.button16);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String log_id = sh.getString("log_id", "");
                JsonReq JR = new JsonReq();
                JR.json_response = user_send_money.this;
                String q = "/user_send_money?amount=" + amount + "&transfer_id=" + transfer_id;
                q = q.replace(" ", "%20");
                JR.execute(q);
            }
        });
    }

    // New method to fetch recipient information
    private void fetchRecipientInfo(String transferId) {
        JsonReq JR = new JsonReq();
        JR.json_response = new JsonResponse() {
            @Override
            public void response(JSONObject jo) {
                try {
                    String status = jo.getString("status");
                    if (status.equals("success")) {
                        String recipientName = jo.getString("recipient_name");
                        recipientNameTextView.setText("To: " + recipientName);
                    } else {
                        recipientNameTextView.setText("To: Unknown Recipient");
                        Toast.makeText(getApplicationContext(), "Could not fetch recipient details", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("Error", e.toString());
                    recipientNameTextView.setText("To: Unknown Recipient");
                }
            }
        };
        String q = "/get_recipient_info?transfer_id=" + transferId;
        q = q.replace(" ", "%20");
        JR.execute(q);
    }

    @Override
    public void response(JSONObject jo) {
        try {
            // Extract status from the response
            String status = jo.getString("status");
            Log.d("pearl", status);

            // Handle different server responses
            switch (status.toLowerCase()) {
                case "success":
                    Toast.makeText(getApplicationContext(), "Payment Completed", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), user_pay_request.class));
                    break;

                case "lowmoney":
                    Toast.makeText(getApplicationContext(), "Insufficient Balance", Toast.LENGTH_LONG).show();
                    break;

                case "already paid":
                    Toast.makeText(getApplicationContext(), "Payment Already Completed", Toast.LENGTH_LONG).show();
                    break;

                default:
                    Toast.makeText(getApplicationContext(), "Payment failed: " + status, Toast.LENGTH_LONG).show();
                    break;
            }
        } catch (Exception e) {
            // Handle unexpected errors
            Log.e("Error", e.toString());
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}