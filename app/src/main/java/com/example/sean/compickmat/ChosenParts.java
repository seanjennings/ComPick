package com.example.sean.compickmat;

import java.util.ArrayList;

/**
 * Created by Seán on 01/11/2015.
 */
public class ChosenParts {
	private ArrayList<Part> parts;
	private double total;
	private double budget;

	public ChosenParts() {
		this.parts = new ArrayList<Part>();
		this.total = 0;
	}

	public ArrayList<Part> getParts() {
		return parts;
	}

	//Add a part to the ChosenParts list
	public void addPart(Part part){
		//Search to see if the part already exists in the user's build list
		Boolean found = false;
		for (Part p : parts) {
			if(p.getName().equals(part.getName()))
			{
				//increment quantity if the part is already present.
				p.setQuantity(p.getQuantity() + 1);
				found = true;
			}
		}

		//Add part if not present
		if(!found)
		{
			part.setQuantity(1);
			this.parts.add(part);
		}

		//Update list total
		this.total += part.getCost();
	}

	//Remove a part from the list
	public void removePart(Part part)
	{
		for (Part p : parts) {
			if(p.getName().equals(part.getName())) {
				//Remove part or decrement quantity
				if(p.getQuantity() == 1) {
					this.parts.remove(p);
				} else {
					p.setQuantity(p.getQuantity() - 1);
				}
				break;
			}
		}

		//Update the list total
		this.total -= part.getCost();
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public double getBudget() {
		return budget;
	}

	public void setBudget(double budget) {
		this.budget = budget;
	}
}
