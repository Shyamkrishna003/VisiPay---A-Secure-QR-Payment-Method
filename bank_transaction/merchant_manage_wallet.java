package com.example.bank_transaction;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class merchant_manage_wallet extends AppCompatActivity implements JsonResponse {
    EditText usernameInput, amountInput;
    Button addButton, backButton;
    ListView categoryListView;
    String[] wallet_id, username, amount, displayValues;
    public static String selectedWalletId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); // will hide the title
        getSupportActionBar().hide(); // hide the title bar

        setContentView(R.layout.activity_merchant_manage_wallet);

        // Initializing views
        usernameInput = findViewById(R.id.editTextTextPersonName5);
        amountInput = findViewById(R.id.editTextTextPersonName6);
        addButton = findViewById(R.id.button5);
        backButton = findViewById(R.id.button8);
        categoryListView = findViewById(R.id.category_view);

        // Back Button Click Listener
        backButton.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), merchanthome.class));
        });

        // Add Button Click Listener
        addButton.setOnClickListener(view -> {
            String username = usernameInput.getText().toString().trim();
            String amount = amountInput.getText().toString().trim();

            if (username.isEmpty()) {
                usernameInput.setError("Enter a valid username");
                usernameInput.requestFocus();
            } else if (amount.isEmpty()) {
                amountInput.setError("Enter a valid amount");
                amountInput.requestFocus();

            } else {
                JsonReq jsonRequest = new JsonReq();
                jsonRequest.json_response = merchant_manage_wallet.this;
                String query = "/merchant_insert_wallet?username=" + username + "&amount=" + amount;
                query = query.replace(" ", "%20");
                jsonRequest.execute(query);
            }
        });

        // ListView Item Click Listener
        categoryListView.setOnItemClickListener((parent, view, position, id) -> {
            selectedWalletId = wallet_id[position]; // Use wallet_id instead of category_id
            final CharSequence[] options = {"Update", "Delete", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(merchant_manage_wallet.this);
            builder.setItems(options, (dialog, which) -> {
                if (options[which].equals("Update")) {
                    showUpdateDialog(position);
                } else if (options[which].equals("Delete")) {
                    JsonReq jsonRequest = new JsonReq();
                    jsonRequest.json_response = merchant_manage_wallet.this;
                    String query = "/delete_wallet?wallet_id=" + merchant_manage_wallet.selectedWalletId; // Updated query
                    query = query.replace(" ", "%20");
                    jsonRequest.execute(query);
                    startActivity(new Intent(getApplicationContext(),merchant_manage_wallet.class));
                    Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_LONG).show();
                } else if (options[which].equals("Cancel")) {
                    dialog.dismiss();
                }
            });
            builder.show();
        });

        // Fetch and display wallet details
        JsonReq jsonRequest = new JsonReq();
        jsonRequest.json_response = merchant_manage_wallet.this;
        String query = "/view_wallet"; // Assuming API endpoint is view_category
        query = query.replace(" ", "%20");
        jsonRequest.execute(query);
    }

    private void showUpdateDialog(int position) {
        // Create a dialog to update wallet details
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Wallet");

        View updateView = getLayoutInflater().inflate(R.layout.dialog_update_wallet, null);
        builder.setView(updateView);

        EditText updateAmountInput = updateView.findViewById(R.id.updateAmountInput);

        // Pre-fill current amount for the selected wallet
        updateAmountInput.setText(amount[position]);

        builder.setPositiveButton("Update", (dialog, which) -> {
            String updatedAmount = updateAmountInput.getText().toString().trim();
            if (updatedAmount.isEmpty()) {
                Toast.makeText(this, "Amount cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            // Send update request to the server
            JsonReq jsonRequest = new JsonReq();
            jsonRequest.json_response = this;
            String query = "/update_wallet?wallet_id=" + wallet_id[position] + "&amount=" + updatedAmount; // Use wallet_id
            query = query.replace(" ", "%20");
            jsonRequest.execute(query);
            startActivity(new Intent(getApplicationContext(),merchant_manage_wallet.class));
            Toast.makeText(this, "Wallet Updated Successfully", Toast.LENGTH_LONG).show();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    @Override
    public void response(JSONObject response) {
        try {
            String method = response.getString("method");

            if (method.equalsIgnoreCase("category")) { // Assuming the API name is "category"
                String status = response.getString("status");
                if (status.equalsIgnoreCase("Added")) {
                    Toast.makeText(getApplicationContext(), "Category Added Successfully", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), merchant_manage_wallet.class));
                }
            } else if (method.equalsIgnoreCase("view_category")) { // Assuming API endpoint is view_category
                String status = response.getString("status");
                if (status.equalsIgnoreCase("Success")) {
                    JSONArray data = response.getJSONArray("data");

                    wallet_id = new String[data.length()];
                    username = new String[data.length()];
                    amount = new String[data.length()];
                    displayValues = new String[data.length()];

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject wallet = data.getJSONObject(i);
                        wallet_id[i] = wallet.getString("wallet_id"); // Using wallet_id
                        username[i] = wallet.getString("username");
                        amount[i] = wallet.getString("amount");
                        displayValues[i] = "Username: " + username[i] + "\nAmount: " + amount[i];
                    }

                    // Update the adapter
                    categoryListView.setAdapter(new ArrayAdapter<>(merchant_manage_wallet.this, android.R.layout.simple_list_item_1, displayValues));
                } else {
                    Toast.makeText(this, "No data available", Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), merchanthome.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
