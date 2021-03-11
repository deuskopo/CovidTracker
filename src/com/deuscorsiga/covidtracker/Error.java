package com.deuscorsiga.covidtracker;

public enum Error {
	DEFAULT(0, "Please try again."), CANNOT_BE_EMPTY(1, "Cannot be blank. Please try again."),
	NOT_YESORNO(2, "Please answer with a \"yes\" or \"no\"."),
	FIRST_TIME(100,
			"Note: It is possible to type just the first few letters of an option.\n"
					+ "(e.g. \"e\", \"exam\", \"examp\", etc. are valid inputs for \"example\".)"),
	INVALID_AGE(5, "Please input a valid age (1-120)."),
	INVALID_PHONE(10, "Please input a 10-digit phone number. Do not include space, dash, and parenthesis.")

	;

	private final String message;
	private int id;

	private Error(int id, String message) {
		this.id = id;
		this.message = message;
	}

	public void print() {
		System.out.println(this);
	}

	public void print(long max) {
		if (id == 0) {
			System.out.println(this + " " + max + " max characters.");
		}
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return message;
	}
}
