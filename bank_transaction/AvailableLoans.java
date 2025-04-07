package com.example.bank_transaction;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class AvailableLoans extends AppCompatActivity implements JsonResponse, AdapterView.OnItemClickListener {

    ListView l1;
    SharedPreferences sh;
    String[] value,loan,details,intrest_name,max_amount,loanid;
    public static String loan_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full sc
        setContentView(R.layout.activity_available_loans);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());



        l1=findViewById(R.id.history);
        l1.setOnItemClickListener(this);

        JsonReq JR = new JsonReq();
        JR.json_response = (JsonResponse) AvailableLoans.this;
        String q = "/viewavailableLoans?lid="+sh.getString("log_id","");
        q = q.replace(" ", "%20");
        JR.execute(q);
    }

    @Override
    public void response(JSONObject jo) {
        try {

            String method = jo.getString("method");
            Log.d("pearl", method);

            if (method.equalsIgnoreCase("viewavailableLoans")) {
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
                    loanid= new String[ja1.length()];


                    for (int i = 0; i < ja1.length(); i++) {
                        loan[i] = ja1.getJSONObject(i).getString("loan_name");
                        details[i] = ja1.getJSONObject(i).getString("details");
                        intrest_name[i] = ja1.getJSONObject(i).getString("interest_name");
                        max_amount[i] = ja1.getJSONObject(i).getString("max_amount");
                        loanid[i] = ja1.getJSONObject(i).getString("loan_id");



                        value[i] = "Loan: "+loan[i]+"\nDetails: "+details[i]+"\nInterest name: "+intrest_name[i]+ "\nMax Amount: "+max_amount[i];
                    }
                    ArrayAdapter<String> ar = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, value);
                    l1.setAdapter(ar);

//                    CustomUser a = new CustomUser(this, name, num);
//                    l1.setAdapter(a);
                } else  if (status.equalsIgnoreCase("failed")) {
                    Toast.makeText(getApplicationContext(), "No Data, found!", Toast.LENGTH_SHORT).show();
                }
            }

            if (method.equalsIgnoreCase("requestloan")) {
                String status = jo.getString("status");
                if (status.equalsIgnoreCase("success")) {

                    Toast.makeText(getApplicationContext(), "Request Send Successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), UserHome.class));
                }else    if (status.equalsIgnoreCase("already")) {
                    Toast.makeText(getApplicationContext(),"Can't Request Same Loan more than one time!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),UserHome.class));
                }else {
                    Toast.makeText(getApplicationContext(), "Something Went Wrong!", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        loan_id=loanid[i];
        final CharSequence[] items = {"Request","Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(AvailableLoans.this);
        // builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {


                if (items[item].equals("Request")) {

                    JsonReq JR = new JsonReq();
                    JR.json_response = (JsonResponse) AvailableLoans.this;
                    String q = "/requestloan?lid="+sh.getString("log_id","")+"&loan_id="+loan_id;
                    q = q.replace(" ", "%20");
                    JR.execute(q);


                }
                else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }

            }

        });
        builder.show();
    }
}