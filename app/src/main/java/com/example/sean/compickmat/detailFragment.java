package com.example.sean.compickmat;

import android.annotation.SuppressLint;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Seán on 01/11/2015.
 */
@SuppressLint("ValidFragment")
public class detailFragment extends ListFragment {

	ArrayList<Part> list;
	partListAdapter adapter;

	public detailFragment( ArrayList<Part> list) {
		this.list = null;
		this.list = list;
	}

	@SuppressLint("ShowToast")
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Part selection = (Part) l.getItemAtPosition(position);
		
		for(Part p : list) {
			if(p.getName().equals(selection.getName())) {
				p.setSelected(!p.getSelected());
			}
		}
		adapter.notifyDataSetChanged();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		adapter = new partListAdapter(getActivity(),list);
		setListAdapter(adapter);
	   
	    return inflater.inflate(com.example.sean.compickmat.R.layout.part_list_fragment_view, container, false);
    }
}