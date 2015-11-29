package com.example.sean.compickmat;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by Seán on 01/11/2015.
 */
public class MainList extends AppCompatActivity {

	FragmentManager manager = getFragmentManager();
	
	Stack<Integer> stack = new Stack<Integer>();
	Spinner spin;
	DBManager db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_list);

		db = new DBManager(this);
		db.open();
		
		this.spin = (Spinner) findViewById(R.id.spinner1);

		String[] spinnerOptions = {"CPU","CPU Cooler","Motherboard","Memory","Storage","Video Card","Case","Power Supply"};
		ArrayList<String> aList = new ArrayList<String>();
		
		for(int i = 0; i < spinnerOptions.length; i++) {
			aList.add(spinnerOptions[i]);
		}
		ArrayAdapter<String> ad = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, aList);
		
		spin.setAdapter(ad);
		
		FragmentTransaction transaction = manager.beginTransaction();
        transaction.addToBackStack(null);
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
		
		spin.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub

				stack.add(position);

				FragmentTransaction transaction = manager.beginTransaction();

				ArrayList<Part> list;

				list = getList(spin.getSelectedItem().toString());
				transaction.replace(R.id.partContainer, new detailFragment(list));
		        transaction.commit();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
		
		Button partsListButton = (Button) findViewById(com.example.sean.compickmat.R.id.partsListButton);
		partsListButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), partsListActivity.class);
				startActivity(intent);
				
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		if(stack.size() > 1)
		{
			stack.pop();
			//manager.popBackStack();
			Spinner spin = (Spinner) findViewById(com.example.sean.compickmat.R.id.spinner1);
			spin.setSelection(stack.lastElement());
		} else {
			finish();
		}
	}

	public ArrayList<Part> getList(String category) {
		ArrayList<Part> list = new ArrayList<Part>();
		Cursor cursor = db.fetchParts(category);
		do {
			String name = cursor.getString(cursor.getColumnIndex("name"));
			double cost = Double.parseDouble(cursor.getString(cursor.getColumnIndex("cost")));
			String desc = cursor.getString(cursor.getColumnIndex("description"));
			String img = cursor.getString(cursor.getColumnIndex("image"));
			String cat = cursor.getString(cursor.getColumnIndex("category"));
			list.add(new Part(name, cost, desc, img, cat));
		} while (cursor.moveToNext());
		return list;
	}
}
