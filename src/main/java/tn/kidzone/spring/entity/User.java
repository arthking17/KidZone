package tn.kidzone.spring.entity;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Date;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;
	@Column
	private String firstName;
	@Column
	private String lastName;
	@Column
	private String email;
	@Column
	private String password;
	@Column
	private String profil;
	@Column
	private String address;
	@Column
	@Temporal(TemporalType.DATE)
	private Date birthdayDate;
	@Column
	@Enumerated(EnumType.STRING)
	private Role role;
	@Column
	@Temporal(TemporalType.DATE)
	private Date createdDate;

	public enum Role {
		PARENT, ADMIN, VISITOR, DIRECTOR, STUDENT, DOCTOR, TEACHER, DRIVER
	}

	public User() {
		super();
	}

	public User(String firstName, String lastName, String email, String password, String profil, String address,
			Date birthdayDate, Role role, Date createdDate) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.profil = profil;
		this.address = address;
		this.birthdayDate = birthdayDate;
		this.role = role;
		this.createdDate = createdDate;
	}

	public User(String id, String firstName, String lastName, String email, String password, String profil,
			String address, Date birthdayDate, Role role, Date createdDate) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.profil = profil;
		this.address = address;
		this.birthdayDate = birthdayDate;
		this.role = role;
		this.createdDate = createdDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[16];
		random.nextBytes(salt);
		/*MessageDigest md = MessageDigest.getInstance("SHA-512");
		md.update(salt);
		byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));*/
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		byte[] hashedPassword = factory.generateSecret(spec).getEncoded();

		System.out.println(hashedPassword);
		this.password = String.valueOf(hashedPassword);
	}

	public String getPassword() {
		return password;
	}

	public String getProfil() {
		return profil;
	}

	public void setProfil(String profil) {
		this.profil = profil;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getBirthdayDate() {
		return birthdayDate;
	}

	public void setBirthdayDate(Date birthdayDate) {
		this.birthdayDate = birthdayDate;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", password=" + password + ", profil=" + profil + ", address=" + address + ", birthdayDate="
				+ birthdayDate + ", role=" + role + ", createdDate=" + createdDate + "]";
	}

}
