package com.example.bank_transaction;


import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;


import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.regex.Pattern;

public class bank_transactions extends AppCompatActivity implements JsonResponse {

    Button submitButton;
    EditText accountNumberEditText, ifscEditText, amountEditText;
    public static String accno, ifsc, amount;
    SharedPreferences sharedPreferences;

    private final String CHANNEL_ID = "transaction_notification";
    private final int NOTIFICATION_ID = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar

        setContentView(R.layout.activity_bank_transactions);

        // Initialize views
        accountNumberEditText = findViewById(R.id.editTextNumber2);
        ifscEditText = findViewById(R.id.editTextTextPersonName8);
        amountEditText = findViewById(R.id.editTextNumber4);
        submitButton = findViewById(R.id.button18);

        // OnClickListener for Submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accno = accountNumberEditText.getText().toString().trim();
                ifsc = ifscEditText.getText().toString().trim();
                amount = amountEditText.getText().toString().trim();

                // Validate account number
                if (accno.isEmpty()) {
                    accountNumberEditText.setError("Enter the account number");
                    return;
                } else if (!Pattern.matches("\\d{9,18}", accno)) {
                    accountNumberEditText.setError("Enter a valid account number");
                    return;
                }

                // Validate IFSC code
                if (ifsc.isEmpty()) {
                    ifscEditText.setError("Enter the IFSC code");
                    return;
                } else if (!Pattern.matches("^[A-Z]{4}0[A-Z0-9]{6}$", ifsc)) {
                    ifscEditText.setError("Enter a valid IFSC code");
                    return;
                }

                // Validate amount
                if (amount.isEmpty()) {
                    amountEditText.setError("Enter the amount");
                    return;
                } else if (Double.parseDouble(amount) <= 0) {
                    amountEditText.setError("Enter a valid amount");
                    return;
                }

                // Call backend to transfer money
                transferMoney(accno, ifsc, amount);
            }
        });
        createNotificationChannel();
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Transaction Notifications";
            String description = "Notifications for successful transactions";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }
    private void showNotification(String amount) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.bank) // Use an existing drawable for the icon
                .setContentTitle("Transaction Successful")
                .setContentText("Amount of ₹" + amount + " has been sent successfully.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void transferMoney(String accountNumber, String ifscCode, String amount) {
        JsonReq jsonReq = new JsonReq();
        jsonReq.json_response = this;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String login_id = sharedPreferences.getString("log_id", "");

        // Query to transfer money
        String query = "/transfer_money?account_number=" + accountNumber + "&ifsc=" + ifscCode + "&amount=" + amount + "&login_id=" + login_id;
        query = query.replace(" ", "%20");
        jsonReq.execute(query);
    }

    @Override
    public void response(JSONObject jo) {
        try {
            String status = jo.getString("status");

            if (status.equalsIgnoreCase("success")) {
                Toast.makeText(this, "Money transferred successfully!", Toast.LENGTH_SHORT).show();
                showNotification(amount);
                finish(); // Close activity after successful transfer
            } else if (status.equalsIgnoreCase("insufficient_balance")) {
                Toast.makeText(this, "Insufficient balance for the transaction", Toast.LENGTH_SHORT).show();
            } else {
                String message = jo.optString("message", "Transfer failed");
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error processing response", Toast.LENGTH_SHORT).show();
        }
    }
}
