package com.example.sean.compickmat;

import android.annotation.SuppressLint;
import android.app.Application;
import android.widget.Toast;

/**
 * Created by Seán on 01/11/2015.
 */
// Making object global learnt from http://stackoverflow.com/questions/1944656/android-global-variable
public class User extends Application{
	private String name;
	private String email;
	private double budget;
	private ChosenParts list;
	private String country;
	
	public User()
	{
		this.list = new ChosenParts();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public double getBudget() {
		return budget;
	}

	public void setBudget(double budget) {
		this.budget = budget;
		list.setBudget(budget);
	}

	public ChosenParts getList() {
		return list;
	}

	public void setList(ChosenParts list) {
		this.list = list;
	}
	
	@SuppressLint("ShowToast")
	public void addPart(Part part){
		
		if(list.getBudget() - list.getTotal() >= part.getCost()) {
			this.list.addPart(part);
		} else {
			Toast.makeText(getApplicationContext(), "You have reached your budget limit.", Toast.LENGTH_LONG).show();
		}
	}
	
	@SuppressLint("ShowToast")
	public void removePart(Part part){
		this.list.removePart(part);
	}
}
