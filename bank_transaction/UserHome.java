package com.example.bank_transaction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class UserHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar

        setContentView(R.layout.activity_user_home);

//        Button b1 = findViewById(R.id.button2);
//        Button b4 = findViewById(R.id.button4);
//        Button b6 = findViewById(R.id.button6);
//        Button b7 = findViewById(R.id.button7);
//        Button b8 = findViewById(R.id.button8);
        Button b9 = findViewById(R.id.button9);
        Button b2 = findViewById(R.id.button1);
        Button b5 = findViewById(R.id.button5);
        Button b10 = findViewById(R.id.button10);
        Button b11 = findViewById(R.id.button11);
        Button b13 = findViewById(R.id.button13);
        Button b14 = findViewById(R.id.button14);
        Button b15 = findViewById(R.id.button15);
        Button b16 = findViewById(R.id.button17);
        Button b17 = findViewById(R.id.button20);
        Button b18 = findViewById(R.id.button21);
        Button b19 = findViewById(R.id.button24);
        Button b20 = findViewById(R.id.button_view_qr);

//        b1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Toast.makeText(getApplicationContext(),"Registration Succesfull",Toast.LENGTH_LONG).show();
//                startActivity(new Intent(getApplicationContext(),ViewMyAccountDetails.class));
//            }
//        });



//        b4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Toast.makeText(getApplicationContext(),"Registration Succesfull",Toast.LENGTH_LONG).show();
//                startActivity(new Intent(getApplicationContext(),SearchWithMobile.class));
//            }
//        });
//
//        b6.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Toast.makeText(getApplicationContext(),"Registration Succesfull",Toast.LENGTH_LONG).show();
//                startActivity(new Intent(getApplicationContext(),AvailableLoans.class));
//            }
//        });
//
//        b7.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Toast.makeText(getApplicationContext(),"Registration Succesfull",Toast.LENGTH_LONG).show();
//                startActivity(new Intent(getApplicationContext(),MyLoan.class));
//            }
//        });
//
//        b8.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Toast.makeText(getApplicationContext(),"Registration Succesfull",Toast.LENGTH_LONG).show();
//                startActivity(new Intent(getApplicationContext(),ViewEmployees.class));
//            }
//        });

        b9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(),"Registration Succesfull",Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),Complaints.class));
            }
        });

        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(),"Registration Succesfull",Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),user_upload_image.class));
            }
        });


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(),"Registration Succesfull",Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),IpSettings.class));
            }
        });

        b10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(),"Registration Succesfull",Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),user_view_transaction.class));
            }
        });
        b11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(),"Registration Succesfull",Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),user_view_notification.class));
            }
        });
        b13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(),"Registration Succesfull",Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),user_wallet.class));
            }
        });
        b14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(),"Registration Succesfull",Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),user_search.class));
            }
        });

        b15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(),"Registration Succesfull",Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),user_generate_qrcode.class));
            }
        });
        b16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(),"Registration Succesfull",Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),user_pay_request.class));
            }
        });
        b17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(),"Registration Succesfull",Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),manage_account.class));
            }
        });
        b18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(),"Registration Succesfull",Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),bank_transactions.class));
            }
        });
        b19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(),"Registration Succesfull",Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),wallet_transactions.class));
            }
        });
        b20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(),"Registration Succesfull",Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), user_local_image.class));
            }
        });


    }

    public void onBackPressed()
    {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent b=new Intent(getApplicationContext(),Login.class);
        startActivity(b);
    }
}