package com.example.bank_transaction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class Complaints extends AppCompatActivity implements JsonResponse {

    ListView l1;
    String complaint;
    SharedPreferences sh;
    String[] comp,reply,date,value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar

        setContentView(R.layout.activity_complaints);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        l1=findViewById(R.id.lvcomplaints);
        EditText e1=findViewById(R.id.complaint);
        Button b1=findViewById(R.id.button6);

        JsonReq JR = new JsonReq();
        JR.json_response = (JsonResponse) Complaints.this;
        String q = "/viewcomplaint?lid=" + sh.getString("log_id","");
        q = q.replace(" ", "%20");
        JR.execute(q);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                complaint = e1.getText().toString();

                if (complaint.equalsIgnoreCase("")) {
                    e1.setError("Enter complaint to submit");
                    e1.setFocusable(true);
                }else {

                    JsonReq JR = new JsonReq();
                    JR.json_response = (JsonResponse) Complaints.this;
                    String q = "/addcomplaint?complaint=" + complaint+"&lid=" + sh.getString("log_id","");
                    q = q.replace(" ", "%20");
                    JR.execute(q);
                }
            }
        });

    }

    @Override
    public void response(JSONObject jo) {
        try {

            String method = jo.getString("method");
            Log.d("pearl", method);

            if (method.equalsIgnoreCase("viewcomplaint")) {
                String status = jo.getString("status");
                if (status.equalsIgnoreCase("success")) {
//                    Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_LONG).show();

                    JSONArray ja1 = (JSONArray) jo.getJSONArray("data");
                    comp = new String[ja1.length()];
                    reply= new String[ja1.length()];
                    date= new String[ja1.length()];
                    value = new String[ja1.length()];


                    for (int i = 0; i < ja1.length(); i++) {

                        comp[i] = ja1.getJSONObject(i).getString("complaint");
                        reply[i] = ja1.getJSONObject(i).getString("replay");
                        date[i] = ja1.getJSONObject(i).getString("date");

//                        Toast.makeText(getApplicationContext(), name[i]+" "+num[i], Toast.LENGTH_LONG).show();


                        value[i] = "Complaint : " + comp[i] + "\nReply : " + reply[i] + "\nDate : " + date[i];
                    }
                    ArrayAdapter<String> ar = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, value);
                    l1.setAdapter(ar);

//                    CustomUser a = new CustomUser(this, name, num);
//                    l1.setAdapter(a);
                }
            }

            if (method.equalsIgnoreCase("addcomplaint")) {

                String status = jo.getString("status");
                Log.d("pearl", status);

                if (status.equalsIgnoreCase("success")) {

                    Toast.makeText(getApplicationContext(), "Complaint send Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), Complaints.class));

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

        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String userType = sh.getString("usertype", ""); // Default to empty string if not found

        Intent intent;
        switch (userType) {
            case "merchant":
                Intent b=new Intent(getApplicationContext(),merchanthome.class);
                startActivity(b);
                break;
            case "customer":
                Intent c=new Intent(getApplicationContext(),UserHome.class);
                startActivity(c);
                break;
            default:
                Intent d=new Intent(getApplicationContext(),Login.class);
                startActivity(d);
                break;
        }
    }
}