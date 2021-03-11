package com.deuscorsiga.covidtracker;

public abstract class Person {
	private String first, last, phone, email;
	private int age;

	Person() {
		this("", "", 0, "", "");
	}

	Person(String first, String last, int age, String phone, String email) {
		this.setFirst(first);
		this.setLast(last);
		this.setAge(age);
		this.setPhone(phone);
		this.setEmail(email);
	}

	public String getFirst() {
		return first;
	}

	public void setFirst(String first) {
		this.first = first;
	}

	public String getLast() {
		return last;
	}

	public void setLast(String last) {
		this.last = last;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
}
