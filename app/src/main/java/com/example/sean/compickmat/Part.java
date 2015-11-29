package com.example.sean.compickmat;

import android.graphics.drawable.Drawable;

/**
 * Created by Seán on 01/11/2015.
 */
public class Part {
	private String name;
	private double cost;
	private String description;
	private String imageName;
	private int id;
	private int quantity;
	private Boolean selected;
	private String category;
	private Drawable image;

	public Part(String name, double cost, String description, String imageName, String category) {
		this.setName(name);
		this.setCost(cost);
		this.setDescription(description);
		this.setImageName(imageName);
		this.setQuantity(0);
		this.setSelected(false);
		this.setCategory(category);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public Drawable getImage() {
		return image;
	}

	public void setImage(Drawable drawable) {
		this.image = drawable;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public Boolean getSelected() {
		return selected;
	}
	public void setSelected(Boolean selected) {
		this.selected = selected;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
}
