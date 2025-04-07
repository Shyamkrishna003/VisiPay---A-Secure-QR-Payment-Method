package com.example.bank_transaction;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class AndroidBarcodeQrExample extends Activity implements JsonResponse
{
	/** Called when the activity is first created. */
	String method="getslotidandlocid";
	String soapaction="http://tempuri.org/getslotidandlocid";
	static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
	public static String contents;
	SharedPreferences sh;
	String logid;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		 logid=sh.getString("log_id", "");

	}

	public void scanBar(View v) {
		try {
			Intent intent = new Intent(ACTION_SCAN);
			intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
			startActivityForResult(intent, 0);
		} catch (ActivityNotFoundException anfe) {
			showDialog(AndroidBarcodeQrExample.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
		}
	}

	public void scanQR(View v) {
		try {
			Intent intent = new Intent(ACTION_SCAN);
			intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
			startActivityForResult(intent, 0);
		} catch (ActivityNotFoundException anfe) {
			showDialog(AndroidBarcodeQrExample.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
		}
	}

	private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
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

				}
			}
		});
		downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int i)
			{
			}
		});
		return downloadDialog.show();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				contents = intent.getStringExtra("SCAN_RESULT");
				String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
				Toast.makeText(getApplicationContext(),contents , Toast.LENGTH_LONG).show();
				Toast toast = Toast.makeText(this, "Content:" + contents + " Format:" + format, Toast.LENGTH_LONG);
				toast.show();
				startActivity(new Intent(getApplicationContext(),SelectBankQR.class));

//				JsonReq jr= new JsonReq();
//				jr.json_response=(JsonResponse) AndroidBarcodeQrExample.this;
//				String q="/scan_qr_code/?logid="+logid+"&qrid="+contents;
//				q.replace("", "%20");
//				jr.execute(q);


			}
		}
	}

	@Override
	public void response(JSONObject jo) {
		// TODO Auto-generated method stub



		try {

			String method=jo.getString("method");

		 if(method.equalsIgnoreCase("scan_qr_code"))
			{
				String status=jo.getString("status");
				Toast.makeText(getApplicationContext(), status, Toast.LENGTH_LONG).show();
				if(status.equalsIgnoreCase("success"))
				{
					Toast.makeText(getApplicationContext(), "Scan Successful.\n", Toast.LENGTH_LONG).show();
					JSONArray ja = (JSONArray) jo.getJSONArray("data");

	                String labourid = ja.getJSONObject(0).getString("labourid");
	                Editor e=sh.edit();
	                e.putString("labourid", "");
	                e.commit();
				startActivity(new Intent(getApplicationContext(),SelectBankQR.class));
				}

				else
				{
					Toast.makeText(getApplicationContext(), "Not Successfull!!!! \n Try Again......", Toast.LENGTH_LONG).show();
				}
			}

	}catch (Exception e) {
	// TODO: handle exception

	  Toast.makeText(getApplicationContext(),e.toString(), Toast.LENGTH_LONG).show();
	}

	}
}