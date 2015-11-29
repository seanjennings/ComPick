package com.example.sean.compickmat;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by Seán on 01/11/2015.
 */
public class partsListActivity extends Activity {

	DBManager db = new DBManager(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.parts_list_view);
		
		Double total = (((User) getApplication()).getList().getTotal())* partListAdapter.conversionRate;
		//Double vat = ((User) getApplication()).getList().getTotal()*0.21;
		Double budget = ((User) getApplication()).getList().getBudget();
		
		if((total) > budget)
		{
			setContentView(com.example.sean.compickmat.R.layout.parts_list_view_over_budget);
			TextView warning = (TextView) findViewById(com.example.sean.compickmat.R.id.warning);
			
			warning.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finish();
				}
			});
		} else {
			setContentView(com.example.sean.compickmat.R.layout.parts_list_view);
		}
		
		TextView finalParts = (TextView) findViewById(com.example.sean.compickmat.R.id.finalPartList);
		TextView finalPrices = (TextView) findViewById(com.example.sean.compickmat.R.id.finalPriceList);
		TextView finalQty = (TextView) findViewById(com.example.sean.compickmat.R.id.finalQty);
		TextView finalTotals = (TextView) findViewById(com.example.sean.compickmat.R.id.finalTotals);
		TextView finalDate = (TextView) findViewById(com.example.sean.compickmat.R.id.finalDate);
		TextView finalCurrency = (TextView) findViewById(com.example.sean.compickmat.R.id.finalCurrency);
		
		String finalPartsStr = new String();
		String finalPricesStr = new String();
		String finalQtyStr = new String();

		db.open();
		db.resetBuild(((User) getApplication()).getEmail());
		db.close();
		
		for(int i = 0; i < ((User) getApplication()).getList().getParts().size(); i++) {

			finalPartsStr += ((User) getApplication()).getList().getParts().get(i).getName();
			finalPartsStr += "\n";
			finalQtyStr += ((User) getApplication()).getList().getParts().get(i).getQuantity();
			finalQtyStr += "\n";
			finalPricesStr += String.format("%.2f",(((User) getApplication()).getList().getParts().get(i).getCost()* partListAdapter.conversionRate) * ((User) getApplication()).getList().getParts().get(i).getQuantity());
			finalPricesStr += "\n";

			db.open();
			db.updateBuildPart(((User) getApplication()).getEmail(), ((User) getApplication()).getList().getParts().get(i).getName(), ((User) getApplication()).getList().getParts().get(i).getQuantity());
			db.close();
		}
				
		finalParts.setText(finalPartsStr);
		finalQty.setText(finalQtyStr);
		finalPrices.setText(finalPricesStr);
		finalCurrency.setText(partListAdapter.currencySymbol);
		
		String finalTotalsStr = new String();

		finalTotalsStr += String.format("%.2f",total);
		finalTotalsStr += "\n";
		finalTotalsStr += String.format("%.2f",budget);
		finalTotalsStr += "\n";
		finalTotalsStr += String.format("%.2f",budget - (total));
				
		finalTotals.setText(finalTotalsStr);
		
		// REFERENCED (learned) FROM http://stackoverflow.com/questions/5369682/get-current-time-and-date-on-android
		Time today = new Time(Time.getCurrentTimezone());
		today.setToNow();
		String date = new String();
		
		date += today.monthDay + "/" + today.month + "/" + today.year + "\t" + today.hour + ":" + today.minute;
		// END REFERENCE
		
		finalDate.setText(date);
		//finalDate.setText(System.currentTimeMillis());
	}
}
