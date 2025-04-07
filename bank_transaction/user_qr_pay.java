package com.example.bank_transaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;




import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

public class user_qr_pay extends AppCompatActivity implements JsonResponse {

    SharedPreferences sh;
    private TextView amountTextView;
    private final String CHANNEL_ID = "payment_notification";
    private final int NOTIFICATION_ID = 102;
    private String amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_qr_pay);

        // Initialize views
        amountTextView = findViewById(R.id.textView22); // Rs. TextView

        amount = getIntent().getStringExtra("amount");


        // Retrieve data from Intent
        String amount = getIntent().getStringExtra("amount");
        String receiver_id = getIntent().getStringExtra("login_id");

        // Validate Intent extras
        if (amount == null || amount.isEmpty() || receiver_id == null || receiver_id.isEmpty()) {
            Toast.makeText(this, "Invalid payment data!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Display amount
        amountTextView.setText("Rs. " + amount);
        createNotificationChannel();

        Button b1 = findViewById(R.id.button16);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve sender ID from SharedPreferences
                sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String sender_id = sh.getString("log_id", "");

                if (sender_id == null || sender_id.isEmpty()) {
                    Toast.makeText(user_qr_pay.this, "Sender ID not found. Please login again.", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    // Validate amount format
                    double amt = Double.parseDouble(amount);
                    if (amt <= 0) {
                        Toast.makeText(user_qr_pay.this, "Invalid amount!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Create JSON request
                    JsonReq JR = new JsonReq();
                    JR.json_response = (JsonResponse) user_qr_pay.this;

                    String q = "/user_qr_pay?amount=" + amt + "&receiver_id=" + receiver_id + "&sender_id=" + sender_id;
                    q = q.replace(" ", "%20");
                    JR.execute(q);
                } catch (NumberFormatException e) {
                    Toast.makeText(user_qr_pay.this, "Amount must be a valid number!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Payment Notifications";
            String description = "Notifications for successful payments";
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
                .setContentTitle("Payment Successful")
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


    @Override
    public void response(JSONObject jo) {
        try {
            String status = jo.getString("status");
            Log.d("Response Status", status);

            if (status.equalsIgnoreCase("success")) {
                Toast.makeText(getApplicationContext(), "Payment Completed", Toast.LENGTH_LONG).show();
                showNotification(amount);
                startActivity(new Intent(getApplicationContext(), UserHome.class));

            } else {
                String message = jo.optString("message", "Payment failed");
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e("Error", e.toString());
            Toast.makeText(getApplicationContext(), "Error processing response: " + e.toString(), Toast.LENGTH_LONG).show();
        }
    }
    public void onBackPressed()
    {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent b=new Intent(getApplicationContext(),UserHome.class);
        startActivity(b);
    }

}
