package sample.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the user database table.
 * 
 */
@Entity
@Table(name="user")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_user") //annotation for map
	private int idUser;

	private String username;

	private String password;

	public User() {
	}

	public User(String username, String password ) {
		this.username = username;
		this.password = password;
	}

	public int getIdUser() { return idUser;	}

	public void setIdUser(int idUser) {	this.idUser = idUser; }

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "User{" +
				"idUser=" + idUser +
				", password='" + password + '\'' +
				", username='" + username + '\'' +
				'}';
	}
}