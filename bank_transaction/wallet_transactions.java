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

public class wallet_transactions extends AppCompatActivity implements JsonResponse {

    ListView l1;
    SharedPreferences sh;
    String[] value, amount, date, transactionDirection, otherPartyName, status, transfer_id;
    public static String transferid, amt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // Hide the title
        getSupportActionBar().hide(); // Hide the title bar

        setContentView(R.layout.activity_wallet_transactions);

        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        l1 = findViewById(R.id.payment);
        String log_id = sh.getString("log_id", "");
        JsonReq JR = new JsonReq();
        JR.json_response = (JsonResponse) wallet_transactions.this;
        String q = "/wallet_transactions?login_id=" + log_id;
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

                    JSONArray ja1 = jo.getJSONArray("data");
                    transfer_id = new String[ja1.length()];
                    amount = new String[ja1.length()];
                    date = new String[ja1.length()];
                    transactionDirection = new String[ja1.length()];
                    otherPartyName = new String[ja1.length()];
                    status = new String[ja1.length()];
                    value = new String[ja1.length()];

                    for (int i = 0; i < ja1.length(); i++) {
                        JSONObject transaction = ja1.getJSONObject(i);

                        transfer_id[i] = transaction.getString("transfer_id");
                        amount[i] = transaction.getString("amount");
                        date[i] = transaction.getString("date");
                        transactionDirection[i] = transaction.getString("transaction_direction"); // "credited" or "debited"
                        otherPartyName[i] = transaction.getString("other_party_name"); // Name of other party
                        status[i] = transaction.getString("status");

                        // Create a value string to display
                        value[i] = "Name : " + otherPartyName[i] +
                                "\nAmount: " + amount[i] + " (" + transactionDirection[i] + ")" +
                                "\nDate: " + date[i] +
                                "\nStatus: " + status[i];
                    }

                    // Bind the data to the ListView
                    ArrayAdapter<String> ar = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, value);
                    l1.setAdapter(ar);

                } else if (stat.equalsIgnoreCase("Failed")) {
                    Toast.makeText(getApplicationContext(), "No History Found!", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String userType = sh.getString("usertype", ""); // Default to empty string if not found

        Intent intent;
        switch (userType) {
            case "merchant":
                intent = new Intent(getApplicationContext(), merchanthome.class);
                startActivity(intent);
                break;
            case "customer":
                intent = new Intent(getApplicationContext(), UserHome.class);
                startActivity(intent);
                break;
            default:
                intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                break;
        }
    }
}