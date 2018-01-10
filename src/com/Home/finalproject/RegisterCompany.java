package com.Home.finalproject;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Activity;
import android.app.ProgressDialog;

public class RegisterCompany extends Activity {

	Button new_comp;
	EditText email, password, cpassword, feild, caddress, name;
	ProgressDialog mypro;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registeractivity);
		setTitle("New Company");

		new_comp = (Button) findViewById(R.id.CompReg_btn_register);
		name = (EditText) findViewById(R.id.CompReg_edt_name);
		email = (EditText) findViewById(R.id.CompReg_edt_email);
		password = (EditText) findViewById(R.id.CompReg_edt_password);
		cpassword = (EditText) findViewById(R.id.CompReg_edt_confpass);
		feild = (EditText) findViewById(R.id.CompReg_edt_feild);
		caddress = (EditText) findViewById(R.id.CompReg_edt_address);

		new_comp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				String comname = name.getText().toString();
				String comemail = email.getText().toString();
				String compassword = password.getText().toString();
				String confpass = cpassword.getText().toString();
				String comfeild = feild.getText().toString();
				String comaddress = caddress.getText().toString();

				if (comname.equals("") || comemail.equals("")
						|| compassword.equals("") || confpass.equals("")
						|| comfeild.equals("") || comaddress.equals("")) {
					Toast.makeText(RegisterCompany.this,
							"Complete all Data Required", Toast.LENGTH_SHORT)
							.show();

				} else if (isValidEmail(comemail) == false) {
					Toast.makeText(RegisterCompany.this, "Not a valid e-mail ",
							Toast.LENGTH_SHORT).show();

				} else if (compassword.equals(confpass) == false) {

					Toast.makeText(RegisterCompany.this,
							"Password didn't matches \n Enter password again ",
							Toast.LENGTH_SHORT).show();
					password.getText().clear();
					cpassword.getText().clear();

				} else {
					String Url = "http://www.amit-learning.com/parkForMe/index.php";
					StringEntity entity = null;
					AddCompany u = new AddCompany();
					u.setEmail(comemail);
					u.setPassword(compassword);
					u.setAddress(comaddress);
					u.setCompanyfield(comfeild);

					try {
						JSONObject jsonparm = new JSONObject();
						jsonparm.put("name", comname);
						jsonparm.put("email", u.getEmail());
						jsonparm.put("password", u.getPassword());
						jsonparm.put("companyfield", u.getCompanyfield());
						jsonparm.put("address", u.getAddress());
						entity = new StringEntity(jsonparm.toString());

					} catch (JSONException e) {
					} catch (UnsupportedEncodingException e) {
					}

					AsyncHttpClient client = new AsyncHttpClient();
					client.post(getApplicationContext(), Url, entity,
							"application/json", new AsyncHttpResponseHandler() {
								public void onStart() {

									mypro = new ProgressDialog(
											RegisterCompany.this);
									mypro.setTitle("Park 4 Me");
									mypro.setMessage("Please wait a few seconds");
									mypro.show();
								};

								public void onSuccess(int arg0, String arg1) {

									Toast.makeText(
											RegisterCompany.this,
											"Congratulations, now you can login to the system",
											Toast.LENGTH_SHORT).show();

								};

								public void onFailure(Throwable arg0) {

									Toast.makeText(RegisterCompany.this,
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

	// method to check the mail is valid or not
	public final static boolean isValidEmail(String target) {
		if (TextUtils.isEmpty(target)) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
					.matches();
		}
	}

}
