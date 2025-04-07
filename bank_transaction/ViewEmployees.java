package com.example.bank_transaction;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

public class ViewEmployees extends AppCompatActivity implements JsonResponse, AdapterView.OnItemClickListener {

    SharedPreferences sh;
    ListView l1;
    String[] fname,lname,place,value,uid,lid,bank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full sc
        setContentView(R.layout.activity_view_employees);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        l1 = findViewById(R.id.lvusers);
        l1.setOnItemClickListener(this);


        JsonReq JR = new JsonReq();
        JR.json_response = (JsonResponse) ViewEmployees.this;
        String q = "/viewemployee?log_id=" + sh.getString("log_id","");
        q = q.replace(" ", "%20");
        JR.execute(q);
    }

    @Override
    public void response(JSONObject jo) {

        try {

            String method = jo.getString("method");
            Log.d("pearl", method);

            if (method.equalsIgnoreCase("viewemployee")) {
                String status = jo.getString("status");
                if (status.equalsIgnoreCase("success")) {
//                    Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_LONG).show();

                    JSONArray ja1 = (JSONArray) jo.getJSONArray("data");
                    fname = new String[ja1.length()];
                    lname = new String[ja1.length()];
                    uid = new String[ja1.length()];
                    place = new String[ja1.length()];
                    value = new String[ja1.length()];
                    lid = new String[ja1.length()];
                    bank = new String[ja1.length()];


                    for (int i = 0; i < ja1.length(); i++) {
                        fname[i] = ja1.getJSONObject(i).getString("first_name");
                        lname[i] = ja1.getJSONObject(i).getString("last_name");
                        uid[i] = ja1.getJSONObject(i).getString("employee_id");
                        place[i] = ja1.getJSONObject(i).getString("place");
                        lid[i] = ja1.getJSONObject(i).getString("login_id");
                        bank[i] = ja1.getJSONObject(i).getString("bank_name");

//                        Toast.makeText(getApplicationContext(), name[i]+" "+num[i], Toast.LENGTH_LONG).show();


                        value[i] = "Name : " + fname[i] + lname[i]+"\nBank Name: "+bank[i]+ "\nPlace : " + place[i];
                    }
                    ArrayAdapter<String> ar = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, value);
                    l1.setAdapter(ar);

//                    CustomUser a = new CustomUser(this, name, num);
//                    l1.setAdapter(a);
                } else  if (status.equalsIgnoreCase("failed")) {
                    Toast.makeText(getApplicationContext(), "something went Wrong", Toast.LENGTH_SHORT).show();
                }
            }

        }
        catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        SharedPreferences.Editor e=sh.edit();
        e.putString("receiver_id",lid[i]);
        e.putString("name",fname[i]+lname[i]);
        e.commit();



        final CharSequence[] items = {"Chat", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(ViewEmployees.this);
        // builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Chat")) {

                    startActivity(new Intent(getApplicationContext(), ChatHere.class));

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }


            }

        });
        builder.show();
    }

    public void onBackPressed()
    {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent b=new Intent(getApplicationContext(),UserHome.class);
        startActivity(b);
    }
}