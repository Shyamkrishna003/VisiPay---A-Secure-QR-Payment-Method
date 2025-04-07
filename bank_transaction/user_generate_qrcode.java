package com.example.bank_transaction;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.regex.Pattern;

public class user_generate_qrcode extends AppCompatActivity {

    private EditText e1;
    private Button b1;
    private ImageView i1;
    SharedPreferences sh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_user_generate_qrcode);

        // Initialize views
        e1 = findViewById(R.id.inputqrcode);
        b1 = findViewById(R.id.buttonqrcode);
        i1 = findViewById(R.id.qrcode);

        // Example: Fetch merchant ID (replace with actual logic)
        String merchantId = fetchMerchantId(); // Replace this with actual fetch logic

        // Set button click listener
        b1.setOnClickListener(v -> {
            String transactionDetails = e1.getText().toString().trim();

            if (transactionDetails.isEmpty()) {
                e1.setError("Enter the Amount");
            }
            else if (!Pattern.matches("^[0-9]{1,10}$",transactionDetails)) {
                e1.setError("Enter a Valid Amount");
            }


            else {
                // Combine merchant ID with transaction details
                sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String log_id = sh.getString("log_id", "");
                String qrCodeData = "login_id=" + log_id + "&Amount=" + transactionDetails;
                generateQRCode(qrCodeData);
            }
        });
    }

    private String fetchMerchantId() {
        // Simulate fetching merchant ID (e.g., from SharedPreferences, API, or database)
        return "MERCHANT12345"; // Replace with real merchant ID fetch logic
    }

    private void generateQRCode(String text) {
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(text, BarcodeFormat.QR_CODE, 400, 400);
            i1.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error generating QR code: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Unexpected error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
