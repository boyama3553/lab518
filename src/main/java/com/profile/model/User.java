package com.profile.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity // This tells Hibernate to make a table out of this class
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(nullable = false)
	private String username;

	@Column(nullable = false)
	private String password;

	@Column
	private String firstName;

	@Column
	private String lastName;

	@Column
	private String biography;

	@Column
	private String picture;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getFirstName() {
		if (firstName == null || firstName.isBlank()) {
			return "First name here";
		}
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		if (lastName == null || lastName.isBlank()) {
			return "Last name here";
		}
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getBiography() {
		if (biography == null || biography.isBlank()) {
			return "Biography here";
		}
		return biography;
	}

	public void setBiography(String biography) {
		this.biography = biography;
	}

	public String getPicture() {
		if (picture == null || picture.isBlank()) {
			return "https://profilepics518.s3.us-east-2.amazonaws.com/blank-profile-picture.png";
		}
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}
}
