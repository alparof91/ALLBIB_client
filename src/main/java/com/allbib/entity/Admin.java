package com.allbib.entity;

import java.io.Serializable;


public class Admin implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer idAdmin;
	private String firstName;
	private String secondName;
	private String address;
	private String email;
	private String phone;
	private User user;
	public Admin() {
	}

	public Admin(String firstName, String secondName, String address, String email, String phone, User user) {
		this.firstName = firstName;
		this.secondName = secondName;
		this.address = address;
		this.email = email;
		this.phone = phone;
		this.user = user;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Integer getIdAdmin() {
		return idAdmin;
	}

	public void setIdAdmin(Integer idAdmin) {
		this.idAdmin = idAdmin;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Reader{" +
				"idReader=" + idAdmin +
				", firstName='" + firstName + '\'' +
				", secondName='" + secondName + '\'' +
				", address='" + address + '\'' +
				", email='" + email + '\'' +
				", phone='" + phone + '\'' +
				", user=" + user +
				'}';
	}
}