package com.Home.finalproject;

import java.io.UnsupportedEncodingException;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterUser extends Activity {
	Button btn_reg;
	EditText edt_usrname, edt_pass, edt_confpass, edt_email;
	ProgressDialog mypro;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTitle("New User");
		setContentView(R.layout.register);
		super.onCreate(savedInstanceState);

		edt_usrname = (EditText) findViewById(R.id.Reg_edt_usrname);
		edt_pass = (EditText) findViewById(R.id.Reg_edt_password);
		edt_confpass = (EditText) findViewById(R.id.Reg_edt_confpass);
		edt_email = (EditText) findViewById(R.id.Reg_edt_email);

		btn_reg = (Button) findViewById(R.id.Reg_btn_register);

		btn_reg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				final String username = edt_usrname.getText().toString();
				String password = edt_pass.getText().toString();
				String confpass = edt_confpass.getText().toString();
				String email = edt_email.getText().toString();

				if (username.equals("") || password.equals("")
						|| confpass.equals("") || email.equals("")) {
					Toast.makeText(RegisterUser.this,
							"Complete all Data Required", Toast.LENGTH_SHORT)
							.show();

				} else if (isValidEmail(email) == false) {
					Toast.makeText(RegisterUser.this, "Not a valid e-mail ",
							Toast.LENGTH_SHORT).show();

				} else if (password.equals(confpass) == false) {

					Toast.makeText(RegisterUser.this,
							"Password didn't matches \n Enter password again ",
							Toast.LENGTH_SHORT).show();
					edt_pass.getText().clear();
					edt_confpass.getText().clear();

				} else {
					String Url = "http://www.amit-learning.com/parkForMe/index.php";
					StringEntity entity = null;
					Add u = new Add();
					u.setName(username);
					u.setPassword(password);
					u.setEmail(email);

					try {
						JSONObject jsonparm = new JSONObject();
						jsonparm.put("name", u.getName());
						jsonparm.put("password", u.getPassword());
						jsonparm.put("email", u.getEmail());
						entity = new StringEntity(jsonparm.toString());

					} catch (JSONException e) {
					} catch (UnsupportedEncodingException e) {
					}

					AsyncHttpClient client = new AsyncHttpClient();
					client.post(getApplicationContext(), Url, entity,
							"application/json", new AsyncHttpResponseHandler() {
								public void onStart() {

									mypro = new ProgressDialog(
											RegisterUser.this);
									mypro.setTitle("Park 4 Me");
									mypro.setMessage("Please wait a few seconds");
									mypro.show();
								};

								public void onSuccess(int arg0, String arg1) {

									Toast.makeText(
											RegisterUser.this,
											"Congratulations, "
													+ username
													+ " \n now you can login to the system",
											Toast.LENGTH_SHORT).show();
								};

								public void onFailure(Throwable arg0) {

									Toast.makeText(RegisterUser.this,
											"Network connection failure ",
											Toast.LENGTH_SHORT).show();
								};

								public void onFinish() {
									mypro.dismiss();

									finishActivity(0);

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
