package com.allbib.entity;

import java.io.Serializable;
import java.time.LocalDate;


public class Request implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer idRequest;
	private Book book;
	private String username;
	private LocalDate requestDate;

	public Request() {
	}

	public Request(Book book, String username, LocalDate requestDate) {
		this.book = book;
		this.username = username;
		this.requestDate = requestDate;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Integer getIdRequest() {
		return idRequest;
	}

	public void setIdRequest(Integer idRequest) {
		this.idRequest = idRequest;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public LocalDate getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(LocalDate requestDate) {
		this.requestDate = requestDate;
	}

	@Override
	public String toString() {
		return "Request{" +
				"idRequest=" + idRequest +
				", book=" + book +
				", username='" + username + '\'' +
				", requestDate='" + requestDate + '\'' +
				'}';
	}
}