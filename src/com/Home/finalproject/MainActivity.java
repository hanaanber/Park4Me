package com.Home.finalproject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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

public class MainActivity extends Activity {
	Button btn_log, btn_Reg, btn_RegComp;
	EditText edt_email, edt_pass;
	ProgressDialog mypro;

	ReturnedUser avali_user = new ReturnedUser();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		edt_email = (EditText) findViewById(R.id.Log_edt_email);
		edt_pass = (EditText) findViewById(R.id.Log_edt_password);
		btn_log = (Button) findViewById(R.id.Log_btn_login);
		btn_Reg = (Button) findViewById(R.id.Log_btn_Register);
		btn_RegComp = (Button) findViewById(R.id.Log_btn_CompRegister);

		btn_Reg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this,
						RegisterUser.class);
				startActivityForResult(intent, 0);

			}
		});
		btn_log.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				String pass = edt_pass.getText().toString();
				String email = edt_email.getText().toString();

				if (pass.equals("") || email.equals("")) {
					Toast.makeText(MainActivity.this,
							"Complete all Data Required", Toast.LENGTH_SHORT)
							.show();
				} else {

					String Url = "http://www.amit-learning.com/parkForMe/index.php";
					StringEntity entity = null;
					Add u = new Add();

					u.setEmail(edt_email.getText().toString());
					u.setPassword(edt_pass.getText().toString());

					try {
						JSONObject jsonparm = new JSONObject();
						jsonparm.put("email", u.getEmail());
						jsonparm.put("password", u.getPassword());

						entity = new StringEntity(jsonparm.toString());

					} catch (JSONException e) {
					} catch (UnsupportedEncodingException e) {
					}

					AsyncHttpClient client = new AsyncHttpClient();
					client.post(getApplicationContext(), Url, entity,
							"application/json", new AsyncHttpResponseHandler() {
								public void onStart() {

									mypro = new ProgressDialog(
											MainActivity.this);
									mypro.setTitle("Park 4 Me");
									mypro.setMessage("Please wait a few seconds");
									mypro.show();
								};

								public void onSuccess(int arg0, String arg1) {
									Gson g = new Gson();
									Type t = new TypeToken<ReturnedUser>() {
									}.getType();

									avali_user = g.fromJson(arg1, t);

									if (avali_user.data.size() == 0) {
										Toast.makeText(MainActivity.this,
												"Check your data or Register",
												Toast.LENGTH_SHORT).show();

									} else if (avali_user.data.get(0).type
											.equals("0")) {

										Intent intent = new Intent(
												MainActivity.this,
												CompLogin.class);
										startActivity(intent);

									} else {

										Intent intent = new Intent(
												MainActivity.this,
												Locations.class);
										startActivity(intent);
									}
									edt_pass.getText().clear();

								};

								public void onFailure(Throwable arg0) {

									Toast.makeText(MainActivity.this,
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
		btn_RegComp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this,
						RegisterCompany.class);
				startActivity(intent);
			}
		});

	}

	public void onBackPressed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Park 4 Me");
		builder.setMessage("Do you want to Exit?");
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				dialog.cancel();
			}
		});
		builder.create().show();

	}
}
