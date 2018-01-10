package com.Home.finalproject;

import java.lang.reflect.Type;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

public class Locations extends Activity {
	GoogleMap map;
	ProgressDialog mypro;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location);
		if (map == null) {
			map = ((MapFragment) getFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			map.setMyLocationEnabled(true);

		}

		String Url = "http://www.amit-learning.com/parkForMe/index.php";

		AsyncHttpClient client = new AsyncHttpClient();
		client.get(Url, new AsyncHttpResponseHandler() {
			public void onStart() {

				mypro = new ProgressDialog(Locations.this);
				mypro.setTitle("Park 4 Me");
				mypro.setMessage("Please wait a few seconds");
				mypro.show();
			};

			public void onSuccess(int arg0, String arg1) {
				Gson g = new Gson();
				Type t = new TypeToken<ReturnedFromLocations>() {
				}.getType();
				ReturnedFromLocations loc_data = new ReturnedFromLocations();

				loc_data = g.fromJson(arg1, t);

				Double lat;
				Double lang;
				String address;

				for (int i = 0; i < loc_data.data.size(); i++) {

					if (loc_data.data.get(i).latitude.equals("") == false
							|| loc_data.data.get(i).langtitude.equals("") == false) {
						lat = Double.parseDouble(loc_data.data.get(i).latitude);
						lang = Double.parseDouble(loc_data.data.get(i).langtitude);
						address = loc_data.data.get(i).address;
						LatLng point = new LatLng(lat, lang);
						map.addMarker(new MarkerOptions().position(point)
								.title(address));
					}

				}

			};

			public void onFailure(Throwable arg0) {

				Toast.makeText(Locations.this, "Network connection failure ",
						Toast.LENGTH_SHORT).show();
			};

			public void onFinish() {
				mypro.dismiss();

			};

		});

	}
}
