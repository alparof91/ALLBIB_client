package com.allbib.entity;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Review implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer idReview;
	private String review;
	private Book book;
	private String user;
	private String date;

	public Review() {
	}

	public Review(String review, Book book, String user, String date) {
		this.review = review;
		this.book = book;
		this.user = user;
		this.date = date;
	}

	public Integer getIdReview() {
		return idReview;
	}

	public void setIdReview(Integer idReview) {
		this.idReview = idReview;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Date getDate() {
		Date formattedDate = null;
		try {
			formattedDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return formattedDate;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "[" + date + "] " + user;
	}
}