package com.allbib.entity;

import java.io.Serializable;


public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	private int idUser;
	private String username;
	private String password;
	private String privilege;

	public User() {
	}

	public User(String username, String password, String privilege) {
		this.username = username;
		this.password = password;
		this.privilege = privilege;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public int getIdUser() {
		return idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPrivilege() {
		return privilege;
	}

	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}

	@Override
	public String toString() {
		return "User{" +
				"idUser=" + idUser +
				", username='" + username + '\'' +
				", password='" + password + '\'' +
				", privilege='" + privilege + '\'' +
				'}';
	}
}