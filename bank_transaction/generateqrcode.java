package com.example.bank_transaction;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Main activity class for QR code generation and visual cryptography
 * Handles QR code creation, encryption using visual cryptography, and API communication
 */
public class generateqrcode extends AppCompatActivity implements JsonResponse {
    // UI Elements and data storage
    private EditText e1;                  // Amount input field
    private Button b1, b2, decryptButton; // Generation and decryption buttons
    private ImageView i1, share1Image, share2Image, decryptedImage; // Display images
    private Bitmap qrCodeBitmap, share1Bitmap, share2Bitmap;        // Image storage
    private SharedPreferences sh;         // App preferences
    private String customerId;            // User identification
    byte[] byteArray, byteArray1;        // Image data for API

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_generateqrcode);

        // Initialize views
        e1 = findViewById(R.id.inputqrcode);
        b1 = findViewById(R.id.buttonqrcode);
        b2 = findViewById(R.id.buttonqrcode2);
        i1 = findViewById(R.id.qrcode);
        share1Image = findViewById(R.id.share1);
        share2Image = findViewById(R.id.share2);
        decryptedImage = findViewById(R.id.decrypted_image);
        decryptButton = findViewById(R.id.button_decrypt);

        // Retrieve customer_id from SharedPreferences
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        customerId = sh.getString("customer_id", null);

        if (customerId == null) {
            Toast.makeText(this, "No customer selected", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Button for primary QR code generation and upload
        b1.setOnClickListener(v -> {
            String transactionDetails = e1.getText().toString().trim();
            if (validateTransactionDetails(transactionDetails)) {
                String qrCodeData = "customer_id=" + sh.getString("log_ids", "") + "&Amount=" + transactionDetails;
                generateQRCodeAndShares(qrCodeData, "http://" + sh.getString("ip", "") + "/api/uploadfile");
            }
        });

        // Button for alternative upload endpoint
        b2.setOnClickListener(v -> {
            String transactionDetails = e1.getText().toString().trim();
            if (validateTransactionDetails(transactionDetails)) {
                String qrCodeData = "customer_id=" + sh.getString("log_ids", "") + "&Amount=" + transactionDetails;
                generateQRCodeAndShares(qrCodeData, "http://" + sh.getString("ip", "") + "/api/uploadfile2");
            }
        });

        // Button for reconstructing original QR from shares
        decryptButton.setOnClickListener(v -> {
            if (share1Bitmap != null && share2Bitmap != null) {
                Bitmap decryptedBitmap = VisualCryptography.decryptShares(share1Bitmap, share2Bitmap);
                i1.setImageBitmap(qrCodeBitmap);
                decryptedImage.setImageBitmap(decryptedBitmap);
                Toast.makeText(this, "Decryption Complete", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Generate QR code first!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Validates transaction details input
     * @param transactionDetails The input string
     * @return True if valid, false otherwise
     */
    private boolean validateTransactionDetails(String transactionDetails) {
        if (transactionDetails.isEmpty()) {
            e1.setError("Enter the Amount");
            return false;
        } else if (!Pattern.matches("^[0-9]{1,10}$", transactionDetails)) {
            e1.setError("Enter a Valid Amount");
            return false;
        }
        return true;
    }

    /**
     * Creates QR code from text and splits it into encrypted shares
     */
    private void generateQRCodeAndShares(String text, String apiUrl) {
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            qrCodeBitmap = barcodeEncoder.encodeBitmap(text, BarcodeFormat.QR_CODE, 800, 800);
            i1.setImageBitmap(qrCodeBitmap);

            Bitmap[] shares = VisualCryptography.generateShares(qrCodeBitmap);
            share1Bitmap = shares[0];
            share2Bitmap = shares[1];

            share1Image.setImageBitmap(share1Bitmap);
            share2Image.setImageBitmap(share2Bitmap);

            byteArray = bitmapToByteArray(share1Bitmap);
            byteArray1 = bitmapToByteArray(share2Bitmap);

            try {
                Map<String, byte[]> data = new HashMap<>();
                data.put("lid", customerId.getBytes());
                data.put("image", byteArray);
                data.put("image1", byteArray1);

                FileUploadAsync fua = new FileUploadAsync(apiUrl);
                fua.json_response = this;
                fua.execute(data);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Exception upload : " + e, Toast.LENGTH_SHORT).show();
            }

            Toast.makeText(this, "QR Code and shares generated successfully", Toast.LENGTH_SHORT).show();
        } catch (WriterException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error generating QR code: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Unexpected error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Sends encrypted image shares to server using multipart form
     */
    private void sendEncryptedImagesToApi(String apiUrl) {
        if (customerId == null || customerId.isEmpty()) {
            Toast.makeText(this, "Customer ID is missing", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();

            try {
                String logId = sh.getString("log_id", "");
                String ip = sh.getString("ip", "");

                if (logId.isEmpty() || ip.isEmpty()) {
                    runOnUiThread(() -> Toast.makeText(this, "Missing configuration", Toast.LENGTH_SHORT).show());
                    return;
                }

                File share1File = bitmapToFile(share1Bitmap, "share1.png");
                File share2File = bitmapToFile(share2Bitmap, "share2.png");

                if (share1File == null || share2File == null) {
                    runOnUiThread(() -> Toast.makeText(this, "Error creating files for images", Toast.LENGTH_SHORT).show());
                    return;
                }

                MultipartBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("customer_id", customerId)
                        .addFormDataPart("log_id", logId)
                        .addFormDataPart("share1", share1File.getName(), RequestBody.create(MediaType.parse("image/png"), share1File))
                        .addFormDataPart("share2", share2File.getName(), RequestBody.create(MediaType.parse("image/png"), share2File))
                        .build();

                Request request = new Request.Builder()
                        .url(apiUrl)
                        .post(requestBody)
                        .build();

                Response response = client.newCall(request).execute();
                String result;
                if (response.isSuccessful()) {
                    result = "Upload success: " + response.body().string();
                } else {
                    result = "Upload failed: HTTP " + response.code() + " " + response.message();
                }

                String finalResult = result;
                runOnUiThread(() -> Toast.makeText(generateqrcode.this, finalResult, Toast.LENGTH_SHORT).show());
            } catch (Exception e) {
                String error = "Error preparing API request: " + e.getMessage();
                Log.e("API_ERROR", error, e);
                runOnUiThread(() -> Toast.makeText(generateqrcode.this, error, Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    // Utility methods
    private File bitmapToFile(Bitmap bitmap, String fileName) throws IOException {
        File file = new File(getExternalCacheDir(), fileName);
        try (FileOutputStream out = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        }
        return file;
    }

    @Override
    public void response(JSONObject jo) {
        try {
            String status = jo.getString("status");
            if ("success".equals(status)) {
                Toast.makeText(this, "Images uploaded successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to upload images", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("API Response", "Error processing response", e);
            Toast.makeText(this, "Error processing response: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}