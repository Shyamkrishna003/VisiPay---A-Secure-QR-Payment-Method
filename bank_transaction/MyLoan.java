package com.example.bank_transaction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class MyLoan extends AppCompatActivity implements JsonResponse {

    ListView l1;
    SharedPreferences sh;
    String[] value,loan,details,intrest_name,max_amount,date,sts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full sc
        setContentView(R.layout.activity_my_loan);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());



        l1=findViewById(R.id.myloans);


        JsonReq JR = new JsonReq();
        JR.json_response = (JsonResponse) MyLoan.this;
        String q = "/myloandetails?lid="+sh.getString("log_id","");
        q = q.replace(" ", "%20");
        JR.execute(q);
    }

    @Override
    public void response(JSONObject jo) {

        try {

            String method = jo.getString("method");
            Log.d("pearl", method);

            if (method.equalsIgnoreCase("myloandetails")) {
                String status = jo.getString("status");
                if (status.equalsIgnoreCase("success")) {
//                    Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_LONG).show();

                    JSONArray ja1 = (JSONArray) jo.getJSONArray("data");
                    loan = new String[ja1.length()];
                    details= new String[ja1.length()];
                    intrest_name= new String[ja1.length()];
                    max_amount= new String[ja1.length()];
//                    lid = new String[ja1.length()];
                    value = new String[ja1.length()];
                    date = new String[ja1.length()];
                    sts = new String[ja1.length()];


                    for (int i = 0; i < ja1.length(); i++) {
                        loan[i] = ja1.getJSONObject(i).getString("loan_name");
                        details[i] = ja1.getJSONObject(i).getString("details");
                        intrest_name[i] = ja1.getJSONObject(i).getString("interest_name");
                        max_amount[i] = ja1.getJSONObject(i).getString("max_amount");
                        date[i] = ja1.getJSONObject(i).getString("date_time");
                        sts[i] = ja1.getJSONObject(i).getString("status");



                        value[i] = "Loan: "+loan[i]+"\nDetails: "+details[i]+"\nInterest name: "+intrest_name[i]+ "\nMax Amount: "+max_amount[i]+"\nStatus: "+sts[i];
                    }
                    ArrayAdapter<String> ar = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, value);
                    l1.setAdapter(ar);

//                    CustomUser a = new CustomUser(this, name, num);
//                    l1.setAdapter(a);
                } else  if (status.equalsIgnoreCase("failed")) {
                    Toast.makeText(getApplicationContext(), "No Data, found!", Toast.LENGTH_SHORT).show();
                }
            }



        }
        catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
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