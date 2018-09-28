package com.social_network.model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class User {

	private int id;
	private String firstName;
	private String surname;
	private int age;
	private String gender;
	private List<Integer> friends;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public List<Integer> getFriends() {
		return friends;
	}

	public void setFriends(List<Integer> friends) {
		this.friends = friends;
	}

	public User(int id, String firstName, String surname, int age, String gender, List<Integer> friends) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.surname = surname;
		this.age = age;
		this.gender = gender;
		this.friends = friends;
	}

	public User(int id, String firstName, String surname, int age, String gender) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.surname = surname;
		this.age = age;
		this.gender = gender;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", surname=" + surname + ", age=" + age + ", gender="
				+ gender + ", friends=" + friends + "]";
	}

}
