package com.example.sean.compickmat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Seán on 01/11/2015.
 */
public class MainActivity extends AppCompatActivity {
	DBManager db = new DBManager(this);
	EditText editName;
	EditText editEmail;
	EditText budgetAmount;
	EditText editEmailLogin;
	public static String country;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//Run Fixer.io API queries
		Rates rates = new Rates();

		Button regButton = (Button)findViewById(R.id.button);
		Button logButton = (Button)findViewById(R.id.button4);

		//Button Listener for registering a new user
		regButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//read in EditText data
				editName = (EditText) findViewById(R.id.editName);
				editEmail = (EditText) findViewById(R.id.editEmail);
				budgetAmount = (EditText) findViewById(R.id.budgetAmount);

				//Check validity of EditText data.
				if (isEmailValid(editEmail.getText().toString()) && !editName.getText().toString().isEmpty() && !budgetAmount.getText().toString().isEmpty()) {
					//Parse budget amount
					String budgetText = budgetAmount.getText().toString();
					double budget = Double.parseDouble(budgetText);

					//Set current App User data
					((User) getApplication()).setName(editName.getText().toString());
					((User) getApplication()).setEmail(editEmail.getText().toString());
					((User) getApplication()).setList(new ChosenParts());
					((User) getApplication()).setBudget(budget);

					//Log the user's data in the db and start the list activity
					try {
						db.open();
						//Update if user exists or insert new user
						if(db.userExists(editName.getText().toString())) {
							db.updateUser(editName.getText().toString(), editEmail.getText().toString(), budget);
						} else {
							db.insertUser(editName.getText().toString(), editEmail.getText().toString(), budget);
						}
						db.close();

						Intent intent = new Intent(getApplicationContext(), MainList.class);
						startActivity(intent);
					} catch (Exception ex) {
						Context context = getApplicationContext();
						CharSequence text = "Error opening database";
						int duration = Toast.LENGTH_LONG;

						Toast toast = Toast.makeText(context, text, duration);
						toast.show();
					}
				} else {
					Toast toast = Toast.makeText(getApplicationContext(),"Please enter valid data.",Toast.LENGTH_LONG);
					toast.show();
				}
			}
		});

		//Button listener for logging in an existing user and loading their data
		logButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Receive user email
				editEmailLogin = (EditText) findViewById(R.id.editText);

				if (isEmailValid(editEmailLogin.getText().toString())) {
					db.open();

					//Check if user exists before trying to load data
					if(db.userExists(editEmailLogin.getText().toString())) {
						//Set current user data for the app
						((User) getApplication()).setEmail(editEmailLogin.getText().toString());

						//Get the user's previous builds and their budget so they can resume using the app
						((User) getApplication()).setList(db.loadExistingUser(editEmailLogin.getText().toString()));
						((User) getApplication()).setBudget(db.loadExistingBudget(editEmailLogin.getText().toString()));
						db.close();

						//Start the MainList activity
						Intent intent = new Intent(getApplicationContext(), MainList.class);
						startActivity(intent);
					} else {
						Toast toast = Toast.makeText(getApplicationContext(),"User does not exist.",Toast.LENGTH_LONG);
						toast.show();
					}
				} else {
					Toast toast = Toast.makeText(getApplicationContext(),"Please enter a valid email.",Toast.LENGTH_LONG);
					toast.show();
				}
			}
		});

		//Get the user's location and use the Reverse Geocoding function to determine their country
		//This will be used to automatically change the prices in the app for users in any country
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Location l = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		//System.out.print("###"+l.getLongitude() + " " +l.getLatitude());
		getMyLocationAddress(l);
	}

	//REFERENCED FROM http://stackoverflow.com/questions/6119722/how-to-check-edittexts-text-is-email-address-or-not
	public boolean isEmailValid(CharSequence email){
		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}
	//END REFERENCE

	//Method that uses reverse geocoding to get the user's country from their location data.
	public void getMyLocationAddress(Location location) {
		if(location != null) { //cover case for no LastKnownLocation and set default in else
			Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);

			List<Address> addresses = null;
			try {
				addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
				//American location test case
				//addresses = geocoder.getFromLocation(40.9246946,-95.2891164, 1);
			} catch (IOException e) {
				e.printStackTrace();
			}

			Address fetchedAddress = addresses.get(0);
			System.out.println("I am in: " + fetchedAddress.getCountryName());
			country = fetchedAddress.getCountryName();
		} else {
			country = "Ireland";
		}
	}
}