package com.eli.coupons.beans;

import java.util.Date;

import com.eli.coupons.enums.Category;
import com.eli.coupons.utils.DateUtils;

public class Coupon {

//  Properties

	private long id;
	private long companyId;
	private Category category;
	private String title;
	private String description;
	private Date startDate;
	private Date endDate;
	private int amount;
	private double price;
	private String image;

//  constructor

	public Coupon(long companyId, Category category, String title, String description, Date startDate, Date endDate,
			int amount, double price, String image) {
		this.companyId = companyId;
		this.category = category;
		this.title = title;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.amount = amount;
		this.price = price;
		this.image = image;
	}

//	Default constructor
	public Coupon() {
	}

//  Setters & Getters

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

//  methods  

	@Override
	public String toString() {
		return "Coupon ID: " + getId() + ", Company Id: " + getCompanyId() + ", Category: " + getCategory()
				+ "\n Title: " + getTitle() + ", Description: " + getDescription() + "\n Amount: " + getAmount()
				+ ", Price: " + getPrice() + ", Image: " + getImage() + "\n Start Date: "
				+ DateUtils.stringifyDate(getStartDate()) + ", End Date: " + DateUtils.stringifyDate(getEndDate())
				+ "\n\n";
	}
}
