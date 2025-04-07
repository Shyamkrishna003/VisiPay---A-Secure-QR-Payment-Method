package com.example.bank_transaction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureActivity;

public class qr_code_scanner extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Start the QR code scanner when the activity is launched
        new IntentIntegrator(this)
                .setCaptureActivity(CaptureActivity.class) // Use the default CaptureActivity
                .setOrientationLocked(true) // Lock orientation to portrait
                .initiateScan(); // Start scanning
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handle the result of the QR code scan
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                String scannedData = result.getContents();
                Toast.makeText(this, "Scanned: " + scannedData, Toast.LENGTH_LONG).show();

                // Parse the scanned QR code content to extract login_id and amount
                try {
                    String loginId = null, amount = null;

                    // Assume the QR content format: login_id=USER123&Amount=1500
                    String[] params = scannedData.split("&");
                    for (String param : params) {
                        if (param.startsWith("login_id=")) {
                            loginId = param.split("=")[1];
                        } else if (param.startsWith("Amount=")) {
                            amount = param.split("=")[1];
                        }
                    }

                    if (loginId != null && amount != null) {
                        // Redirect to the next activity and pass the data
                        Intent nextPage = new Intent(getApplicationContext(), user_qr_pay.class);
                        nextPage.putExtra("login_id", loginId);
                        nextPage.putExtra("amount", amount);
                        startActivity(nextPage);
                    } else {
                        Toast.makeText(this, "Invalid QR Code Data", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    // Handle parsing errors
                    Toast.makeText(this, "Error Parsing QR Code: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else {
                // User canceled scanning
                Toast.makeText(this, "Scan Cancelled", Toast.LENGTH_LONG).show();
            }
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
