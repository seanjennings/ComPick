package com.example.sean.compickmat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Seán on 01/11/2015.
 */
public class partListAdapter extends ArrayAdapter<Part>{

	private Context context;
	private ArrayList<Part> list;
	static String currencySymbol;
	static double conversionRate;

	public partListAdapter(Context context, ArrayList<Part> list) {
		super(context,R.layout.form_layout ,list);
		this.context = context;
		this.list = list;
	}

	@SuppressLint("ViewHolder")
	public View getView(final int position, View convertView, ViewGroup parent){

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View rowView;

		//find part in list and increment
		Boolean found = false;
		int qty = 0;
		for(Part p : ((User) getContext().getApplicationContext()).getList().getParts()) {
			if(p.getName().equals(list.get(position).getName())) {
				found = true;
				qty = p.getQuantity();
			}
		}

		if(list.get(position).getSelected()) {
			rowView = inflater.inflate(R.layout.part_row_view_selected, parent, false);
		} else if(found) {
			rowView = inflater.inflate(R.layout.part_row_view_onlist, parent, false);
		} else {
			rowView = inflater.inflate(R.layout.part_row_view, parent, false);
		}

		final TextView partTitle = (TextView) rowView.findViewById(R.id.partTitle);
		TextView partDesc = (TextView) rowView.findViewById(R.id.partDescription);
		TextView partPrice = (TextView) rowView.findViewById(R.id.partPrice);

		ImageView imageView = (ImageView) rowView.findViewById(R.id.partImage);

		//Referenced from http://stackoverflow.com/questions/21856260/how-can-i-convert-string-to-drawable
		list.get(position).setId(context.getResources().getIdentifier(list.get(position).getImageName(), "drawable", context.getPackageName()));
		list.get(position).setImage(context.getResources().getDrawable(list.get(position).getId()));
		// End reference

		switch(MainActivity.country) {
			case "Ireland": 		currencySymbol = "€";
									conversionRate = 1.0;
									break;
			case "United Kingdom": 	currencySymbol = "£";
									conversionRate = Rates.gbp;
									break;
			case "United States":	currencySymbol = "$";
									conversionRate = Rates.usd;
									break;
			default: 				currencySymbol = "€";
									conversionRate = 1.0;
									break;
		}
		//System.out.println("//" + MainActivity.country + "//" + currencySymbol + "//" + conversionRate);

		//set row data
		partTitle.setText(list.get(position).getName());
		partDesc.setText(list.get(position).getDescription());
		partPrice.setText(String.format(currencySymbol + "%.2f", list.get(position).getCost() * conversionRate));
		imageView.setImageResource(list.get(position).getId());

		// +/- button functionality
		if(list.get(position).getSelected()) {
			TextView selectedQty = (TextView) rowView.findViewById(R.id.selectedQty);
			selectedQty.setText(Integer.toString(qty));

			TextView plusQty = (TextView) rowView.findViewById(R.id.plusQty);
			TextView minusQty = (TextView) rowView.findViewById(R.id.minusQty);

			plusQty.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					((User) getContext().getApplicationContext()).addPart(list.get(position));
					notifyDataSetChanged();

				}
			});

			minusQty.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					((User) getContext().getApplicationContext()).removePart(list.get(position));
					notifyDataSetChanged();
				}
			});
		}
		return rowView;
	}
}