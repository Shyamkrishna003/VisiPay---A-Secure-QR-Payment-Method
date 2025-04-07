package com.example.bank_transaction;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class user_scan_qrcode extends Activity {
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    public static String contents;
    SharedPreferences sh;
    String logid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_scan_qrcode);

        // Retrieve login_id from SharedPreferences
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        logid = sh.getString("log_id", "");
    }

    // Function to initiate QR code scan
    public void scanQR(View v) {
        try {
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException anfe) {
            showDialog(user_scan_qrcode.this, "No Scanner Found",
                    "Download a scanner code activity?", "Yes", "No").show();
        }
    }

    // Display dialog to prompt user to download a scanner
    private static AlertDialog showDialog(final Activity act, CharSequence title,
                                          CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {
                    // Do nothing
                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, null);
        return downloadDialog.show();
    }

    // Handle QR code scan results
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                // Retrieve QR code content
                contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");

                // Log and display the raw QR content
                Log.d("QRContent", "Scanned Content: " + contents);
//                Toast.makeText(getApplicationContext(),
//                        "Content: " + contents + "\nFormat: " + format, Toast.LENGTH_LONG).show();

                // Parse QR code content to extract login_id and Amount
                try {
                    String loginId = null, amount = null;

                    // Assume content format: login_id=USER123&Amount=1500
                    String[] params = contents.split("&");
                    for (String param : params) {
                        if (param.startsWith("login_id=")) {
                            loginId = param.split("=")[1];
                        } else if (param.startsWith("Amount=")) {
                            amount = param.split("=")[1];
                        }
                    }

                    if (loginId != null && amount != null) {
                        // Display extracted values
//                        Toast.makeText(this, "Login ID: " + loginId + "\nAmount: " + amount, Toast.LENGTH_LONG).show();

//                        // Store values in SharedPreferences (optional)
//                        SharedPreferences.Editor editor = sh.edit();
//                        editor.putString("login_id", loginId);
//                        editor.putString("amount", amount);
//                        editor.apply();

                        // Redirect to another activity with extracted data
                        Intent nextActivity = new Intent(getApplicationContext(), user_qr_pay.class);
                        nextActivity.putExtra("login_id", loginId);
                        nextActivity.putExtra("amount", amount);
                        startActivity(nextActivity);
                    } else {
                        Toast.makeText(this, "Invalid QR Code Data", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    // Handle parsing errors
                    Toast.makeText(this, "Error Parsing QR Code: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
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
