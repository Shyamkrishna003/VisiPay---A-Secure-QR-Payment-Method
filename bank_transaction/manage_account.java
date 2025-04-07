package com.example.bank_transaction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class manage_account extends AppCompatActivity implements JsonResponse {

    private EditText accountNumberEditText, ifscEditText;
    private Button submitButton;
    private SharedPreferences sharedPreferences;
    private boolean isAccountPresent = false; // Flag to check if account exists

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); // will hide the title
        getSupportActionBar().hide(); // hide the title bar

        setContentView(R.layout.activity_manage_account); // Replace with your XML layout name

        // Initialize views
        accountNumberEditText = findViewById(R.id.editTextNumber2);
        ifscEditText = findViewById(R.id.editTextTextPersonName8);
        submitButton = findViewById(R.id.button18);

        // SharedPreferences to get user ID
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String customer_id = sharedPreferences.getString("log_id", "");

        // Fetch existing account details if any
        fetchAccountDetails(customer_id);

        // Set OnClickListener for the submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String accountNumber = accountNumberEditText.getText().toString().trim();
                String ifscCode = ifscEditText.getText().toString().trim();

                // Validate input fields
                if (!validateAccountNumber(accountNumber)) {
                    Toast.makeText(manage_account.this, "Invalid account number", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!validateIFSCCode(ifscCode)) {
                    Toast.makeText(manage_account.this, "Invalid IFSC code", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (isAccountPresent) {
                    // Update account details
                    updateAccountDetails(customer_id, accountNumber, ifscCode);
                } else {
                    // Add new account details
                    addAccountDetails(customer_id, accountNumber, ifscCode);
                }
            }
        });
    }

    private void fetchAccountDetails(String customer_id) {
        JsonReq jsonReq = new JsonReq();
        jsonReq.json_response = this;

        String query = "/fetch_account_details?customer_id=" + customer_id;
        query = query.replace(" ", "%20");
        jsonReq.execute(query);
    }

    private void addAccountDetails(String customer_id, String accountNumber, String ifscCode) {
        JsonReq jsonReq = new JsonReq();
        jsonReq.json_response = this;

        String query = "/add_account_details?customer_id=" + customer_id + "&account_number=" + accountNumber + "&ifsc=" + ifscCode;
        query = query.replace(" ", "%20");
        jsonReq.execute(query);
    }

    private void updateAccountDetails(String customer_id, String accountNumber, String ifscCode) {
        JsonReq jsonReq = new JsonReq();
        jsonReq.json_response = this;

        String query = "/update_account_details?customer_id=" + customer_id + "&account_number=" + accountNumber + "&ifsc=" + ifscCode;
        query = query.replace(" ", "%20");
        jsonReq.execute(query);
    }

    @Override
    public void response(JSONObject jo) {
        try {
            String status = jo.getString("status");
            Log.d("Response Status", status);

            if (status.equalsIgnoreCase("success")) {
                if (jo.has("account_details")) {
                    // Display fetched account details
                    JSONArray accountDetails = jo.getJSONArray("account_details");
                    JSONObject details = accountDetails.getJSONObject(0);
                    accountNumberEditText.setText(details.getString("account_number"));
                    ifscEditText.setText(details.getString("ifsc"));
                    isAccountPresent = true; // Mark that account is already present
                } else {
                    Toast.makeText(this, "Operation successful", Toast.LENGTH_SHORT).show();
                    finish(); // Close activity after successful operation
                }
            } else {
                String message = jo.optString("message", "Add Bank Account");
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("Response Error", e.toString());
            Toast.makeText(this, "Error processing response", Toast.LENGTH_SHORT).show();
        }
    }

    // Validation for account number: ensure it is numeric and of appropriate length
    private boolean validateAccountNumber(String accountNumber) {
        return accountNumber.matches("\\d{9,18}"); // Account numbers typically have 9 to 18 digits
    }

    // Validation for IFSC code: 4 alphabetic characters followed by a digit and 6 alphanumeric characters
    private boolean validateIFSCCode(String ifscCode) {
        String ifscPattern = "^[A-Z]{4}0[A-Z0-9]{6}$"; // Standard IFSC code pattern
        return Pattern.compile(ifscPattern).matcher(ifscCode).matches();
    }
}
