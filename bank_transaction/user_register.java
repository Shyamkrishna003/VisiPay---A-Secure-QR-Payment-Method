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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.regex.Pattern;

public class user_register extends AppCompatActivity implements JsonResponse{
    EditText t1,t2,t3,t4,t5,t6,t7,t8,t9,t10;
    Button b1;
    RadioButton r1,r2;
    public static String fname,lname,address,phone,username,password,email,gender,housename,pincode;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar

        setContentView(R.layout.activity_user_register);
        t1=(EditText) findViewById(R.id.fname);
        t2=(EditText) findViewById(R.id.lname);
        t3=(EditText) findViewById(R.id.housename);
        t4=(EditText) findViewById(R.id.place);
        t5=(EditText) findViewById(R.id.phone);
        t6=(EditText) findViewById(R.id.email);
        t7=(EditText) findViewById(R.id.pincode);
        t8=(EditText) findViewById(R.id.username33);
        t9=(EditText) findViewById(R.id.password33);
        r1=(RadioButton) findViewById(R.id.radioButton3);
        r2=(RadioButton) findViewById(R.id.radioButton4);
        b1=(Button) findViewById(R.id.register);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fname=t1.getText().toString();
                lname=t2.getText().toString();
                housename=t3.getText().toString();
                phone=t5.getText().toString();
                email=t6.getText().toString();
                address=t4.getText().toString();
                pincode=t7.getText().toString();
                username=t8.getText().toString();
                password=t9.getText().toString();
                gender="";
                if(r1.isChecked())
                {
                    gender=r1.getText().toString();
                }
                else if(r2.isChecked())
                {
                    gender=r2.getText().toString();
                }
                if(fname.isEmpty())
                {
                    t1.setError("Enter Your First Name");
                    t1.setFocusable(true);
                }
                else if(!Pattern.matches("^[a-zA-Z ]+$",fname))
                {
                    t1.setError("Enter a Valid Name:Alphabets Only");
                    t1.setFocusable(true);
                }
                else if(lname.isEmpty())
                {
                    t2.setError("Enter Your Last Name");
                    t2.setFocusable(true);
                }
                else if(!Pattern.matches("^[a-zA-Z ]+$",lname))
                {
                    t2.setError("Enter a Valid Name:Alphabets Only");
                    t2.setFocusable(true);
                }
                else  if(gender.isEmpty())
                {
                    Toast.makeText(user_register.this,"Select Your Gender ",Toast.LENGTH_LONG).show();
                }
                else if(housename.isEmpty()) {
                    t3.setError("Enter Your  House Name");
                    t3.setFocusable(true);
                }
                else if(address.isEmpty()) {
                    t4.setError("Enter Your Address");
                    t4.setFocusable(true);
                }
                else if(phone.isEmpty()) {
                    t5.setError("Enter Your  Number");
                    t5.setFocusable(true);
                }
                else if(!Pattern.matches("^[0-9]{10}$",phone))
                {
                    t5.setError("Enter a Valid Number");
                    t5.setFocusable(true);
                }
                else if(email.isEmpty()) {
                    t6.setError("Enter Your email");
                    t6.setFocusable(true);
                }
                else if(!Pattern.matches("^[a-z0-9]+@[a-z.]+$",email))
                {
                    t6.setError("enter a Valid mail id");
                    t6.setFocusable(true);
                }

                else if(pincode.isEmpty()) {
                    t7.setError("Enter Your Pincode");
                    t7.setFocusable(true);
                }
                else if(!Pattern.matches("^[0-9]{6}$",pincode))
                {
                    t7.setError("Enter a Valid Pincode: 6 digit,only numbers");
                    t7.setFocusable(true);
                }
                else if(username.isEmpty()) {
                    t8.setError("Enter Your Username");
                    t8.setFocusable(true);
                }

                else if(password.isEmpty()) {
                    t9.setError("Enter Your Password");
                    t9.setFocusable(true);
                }
                else if(!Pattern.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@!#$%^&*()_+=~`]).{8,}$",password))
                {
                    t9.setError("enter a valid Password min 8 letter Mandatory-Data-Types(Aa1@)");
                    t9.setFocusable(true);
                }
                else {
                    JsonReq JR=new JsonReq();
                    JR.json_response=(JsonResponse) user_register.this;
                    String a = "/user_register?fname=" + fname +"&lname=" + lname +"&address=" + address + "&phone=" + phone + "&email=" + email + "&housename="+housename+ "&pincode="+pincode+"&gender="+gender+"&username=" + username + "&password=" + password;
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