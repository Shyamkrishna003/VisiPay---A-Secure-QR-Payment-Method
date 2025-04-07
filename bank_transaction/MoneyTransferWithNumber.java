package com.example.bank_transaction;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Random;

public class MoneyTransferWithNumber extends AppCompatActivity implements JsonResponse {

    String myacno,mybalance,toacnum,toamount,otpval;
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar

        setContentView(R.layout.activity_money_transfer_with_number);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        EditText e1 = findViewById(R.id.editTextTextPersonName);
        EditText e2 = findViewById(R.id.editTextTextPersonName2);
        TextView t1 = findViewById(R.id.textView5);

        EditText e4 = findViewById(R.id.editTextTextPersonName4);
        Button b1 = findViewById(R.id.button3);

        e1.setText("My Account: "+SelectAccountForNumber.myacno);
        e2.setText("My Balance: "+SelectAccountForNumber.blnce);

        EditText e8 = findViewById(R.id.otp);
        TextView e9 = findViewById(R.id.otpmsg);

        e8.setVisibility(View.GONE);
        e9.setVisibility(View.GONE);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                toamount = e4.getText().toString();

                if (toamount.equalsIgnoreCase("")) {
                    e4.setError("Enter Amount");
                    e4.setFocusable(true);
                } else {

                    if (Integer.parseInt(toamount) > Integer.parseInt(SelectAccountForNumber.blnce)){

                        Toast.makeText(getApplicationContext(), "Insufficient Balance", Toast.LENGTH_SHORT).show();

                    }else {


                        String OTP = String.format("%06d", new Random().nextInt(1000000));

                        String phoneNumber = Login.phone;
                        String message = "Your OTP is: " + OTP;

                        SmsManager sms=SmsManager.getDefault();
                        sms.sendTextMessage(phoneNumber, null, message, null,null);
//                        Toast.makeText(getApplicationContext(),phoneNumber+" "+message,Toast.LENGTH_LONG).show();

                        e8.setVisibility(View.VISIBLE);
                        e9.setVisibility(View.VISIBLE);
                        e1.setVisibility(View.GONE);
                        e2.setVisibility(View.GONE);
                        e4.setVisibility(View.GONE);
                        b1.setVisibility(View.GONE);
                        t1.setVisibility(View.GONE);



                        e8.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                otpval = e8.getText().toString();

                                if(otpval.length() == 6){

                                    if (Integer.parseInt(otpval) == Integer.parseInt(OTP)){


                                        JsonReq JR = new JsonReq();
                                        JR.json_response = (JsonResponse) MoneyTransferWithNumber.this;
                                        String q = "/moneytransferwithnnum?toaccount=" + SearchWithMobile.account_number + "&amount=" + toamount+"&myaccount="+SelectAccountForNumber.myacno;
                                        q = q.replace(" ", "%20");
                                        JR.execute(q);
                                    }else {

                                        Toast.makeText(getApplicationContext(),"OTP Mismatch!!! ",Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(),UserHome.class));
                                    }

                                }





                            }

                            @Override
                            public void afterTextChanged(Editable editable) {

                            }
                        });


                    }

                }

            }
        });
    }

    @Override
    public void response(JSONObject jo) {
        try {
            String status = jo.getString("status");
            Log.d("pearl", status);

            if (status.equalsIgnoreCase("success")) {

                Toast.makeText(getApplicationContext(),"Successful",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),UserHome.class));



            }
            else if (status.equalsIgnoreCase("noaccount")) {

                Toast.makeText(getApplicationContext(),"No Valid Account with this Number",Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(getApplicationContext(),Login.class));



            }
        }
        catch (Exception e) {
            // TODO: handle exception

            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}