package com.example.bank_transaction;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.regex.Pattern;

public class merchant_register extends AppCompatActivity implements JsonResponse{
    EditText t1,t2,t3,t4,t5,t6,t7,t8,t9,t10;
    Button b1;
    public static String name,address,phone,username,password,email;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar


        setContentView(R.layout.activity_merchant_register);
        t1=(EditText) findViewById(R.id.username4);
        t2=(EditText) findViewById(R.id.username5);
        t3=(EditText) findViewById(R.id.username6);
        t4=(EditText) findViewById(R.id.username7);
        t5=(EditText) findViewById(R.id.username3);
        t6=(EditText) findViewById(R.id.password3);

        b1=(Button) findViewById(R.id.loginbtn3);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name=t1.getText().toString();
                phone=t2.getText().toString();
                email=t3.getText().toString();
                address=t4.getText().toString();
                username=t5.getText().toString();
                password=t6.getText().toString();

                if(name.isEmpty())
                {
                    t1.setError("Enter Your Name");
                    t1.setFocusable(true);
                }
                else if(!Pattern.matches("^[a-zA-Z ]+$",name))
                {
                    t1.setError("Enter a Valid Name:Alphabets Only");
                    t1.setFocusable(true);
                }
                else if(phone.isEmpty()) {
                    t2.setError("Enter Your  Number");
                    t2.setFocusable(true);
                }
                else if(!Pattern.matches("^[0-9]{10}$",phone))
                {
                    t2.setError("Enter a Valid Number");
                    t2.setFocusable(true);
                }
                else if(email.isEmpty()) {
                    t3.setError("Enter Your email");
                    t3.setFocusable(true);
                }
                else if(!Pattern.matches("^[a-z0-9]+@[a-z.]+$",email))
                {
                    t3.setError("enter a Valid mail id");
                    t3.setFocusable(true);
                }
                else if(address.isEmpty()) {
                    t4.setError("Enter Your Address");
                    t4.setFocusable(true);
                }

                else if(username.isEmpty()) {
                    t5.setError("Enter Your Username");
                    t5.setFocusable(true);
                }

                else if(password.isEmpty()) {
                    t6.setError("Enter Your Password");
                    t6.setFocusable(true);
                }
                else if(!Pattern.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@!#$%^&*()_+=~`]).{8,}$",password))
                {
                    t6.setError("enter a valid Password min 8 letter Mandatory-Data-Types(Aa1@)");
                    t6.setFocusable(true);
                }
                else {
                    JsonReq JR=new JsonReq();
                    JR.json_response=(JsonResponse) merchant_register.this;
                    String a = "/merchant_register?name=" + name +"&address=" + address + "&phone=" + phone + "&email=" + email + "&username=" + username + "&password=" + password;
                    a=a.replace(" ","%20");
                    JR.execute(a);
//                startActivity(new Intent(getApplicationContext(), login.class));
                }}
        });
    }
    @Override
    public void response(JSONObject jo) {
        try {
            String status = jo.getString("status");
            Log.d("pearl", status);
            if (status.equalsIgnoreCase("success")) {
                Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
            else if(status.equalsIgnoreCase("exist")){
                Toast.makeText(getApplicationContext(), "Username already exist", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "Registration failed: " + jo.getString("message"), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "An error occurred: " + e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}