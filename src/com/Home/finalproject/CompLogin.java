package com.Home.finalproject;

import java.io.UnsupportedEncodingException;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

public class CompLogin extends Activity {
	Button add;
	EditText edt_lat, edt_long, edt_address, edt_no;
	ProgressDialog mypro;
	String ID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comp_login);
		setTitle("New Location");

		Intent intp = getIntent();

		ID = intp.getStringExtra("ID");

		add = (Button) findViewById(R.id.CompLog_btn_Add);
		edt_lat = (EditText) findViewById(R.id.CompLog_edt_lat);
		edt_long = (EditText) findViewById(R.id.CompLog_edt_long);
		edt_address = (EditText) findViewById(R.id.CompLog_edt_address);
		edt_no = (EditText) findViewById(R.id.CompLog_edt_no);

		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				String lat = edt_lat.getText().toString();
				String longt = edt_long.getText().toString();
				String addr = edt_address.getText().toString();
				String num = edt_no.getText().toString();

				if (lat.equals("") || longt.equals("")) {
					Toast.makeText(CompLogin.this, "Complete Data Required",
							Toast.LENGTH_SHORT).show();
				} else if (-180 > Double.parseDouble(longt)
						|| Double.parseDouble(longt) > 180) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							CompLogin.this);
					builder.setTitle("Wrong data");
					builder.setMessage("longitude Values:\nlongitude must be between +180 and -180 ");
					builder.setPositiveButton("ok",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
								}
							});
					builder.create().show();
					edt_long.getText().clear();

				} else if (-90 > Double.parseDouble(lat)
						|| Double.parseDouble(lat) > 90) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							CompLogin.this);
					builder.setTitle("Wrong data");
					builder.setMessage("Latitude Values:\n Latitude must be between +90 and -90 ");
					builder.setPositiveButton("ok",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
								}
							});
					builder.create().show();
					edt_lat.getText().clear();

				} else {

					String Url = "http://www.amit-learning.com/parkForMe/index.php";
					StringEntity entity = null;
					AddNewLocation u = new AddNewLocation();

					u.setLang(longt);
					u.setLat(lat);
					u.setAddress(addr);
					u.setUsernumbers(num);
					u.setCompanyID(ID);

					try {
						JSONObject jsonparm = new JSONObject();
						jsonparm.put("lang", u.getLang());
						jsonparm.put("lat", u.getLat());
						jsonparm.put("usernumbers", u.getUsernumbers());
						jsonparm.put("address", u.getAddress());
						jsonparm.put("companyID", u.getCompanyID());

						entity = new StringEntity(jsonparm.toString());

					} catch (JSONException e) {
					} catch (UnsupportedEncodingException e) {
					}

					AsyncHttpClient client = new AsyncHttpClient();
					client.post(getApplicationContext(), Url, entity,
							"application/json", new AsyncHttpResponseHandler() {
								public void onStart() {

									mypro = new ProgressDialog(CompLogin.this);
									mypro.setTitle("Park 4 Me");
									mypro.setMessage("Please wait a few seconds");
									mypro.show();
								};

								public void onSuccess(int arg0, String arg1) {
									Toast.makeText(CompLogin.this,
											"Data Added", Toast.LENGTH_SHORT)
											.show();
									edt_address.getText().clear();
									edt_lat.getText().clear();
									edt_long.getText().clear();
									edt_no.getText().clear();

								};

								public void onFailure(Throwable arg0) {

									Toast.makeText(CompLogin.this,
											"Network connection failure ",
											Toast.LENGTH_SHORT).show();
								};

								public void onFinish() {
									mypro.dismiss();

								};

							});

				}
			}
		});

	}
}
