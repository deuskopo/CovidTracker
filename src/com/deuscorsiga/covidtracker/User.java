package com.deuscorsiga.covidtracker;

public class User extends Person {

	private String ID;
	private boolean isEmployee;
	private boolean ProfileIsSet;
	private Boolean[] screenAnswers;

	public User() {
		super();
		this.setID("");
		this.setEmployee(false);
		ProfileIsSet = false;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public boolean isEmployee() {
		return isEmployee;
	}

	public String getStatus() {
		if (isEmployee) {
			return "Employee";
		}
		return "Client";
	}

	public void setEmployee(boolean isEmployee) {
		this.isEmployee = isEmployee;
	}

	public String status() {
		if (isEmployee) {
			return "Employee";
		}
		return "Client";
	}

	public boolean isProfileSet() {
		return ProfileIsSet;
	}

	public void ProfileIsSet() {
		ProfileIsSet = true;
	}

	public void initializeScreenAnswers(int size) {
		screenAnswers = new Boolean[size];
		for (int i = 0; i < size; i++) {
			screenAnswers[i] = false;
		}
	}

	public void setScreenAnswers(int index, Boolean b) {
		screenAnswers[index] = b;
	}

	public Boolean[] getScreenAnswers() {
		return screenAnswers;
	}

	public Boolean answeredYes() {
		for (int i = 0; i < screenAnswers.length; i++) {
			if (screenAnswers[i]) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return CovidTrackerAppMethods.toWrap(" Status: " + getStatus()) + "\n"
				+ CovidTrackerAppMethods.toWrap(" First:  " + getFirst()) + "\n"
				+ CovidTrackerAppMethods.toWrap(" Last:   " + getLast()) + "\n"
				+ CovidTrackerAppMethods.toWrap(" Age:    " + getAge()) + "\n"
				+ CovidTrackerAppMethods.toWrap(" Phone:  " + getPhone()) + "\n"
				+ CovidTrackerAppMethods.toWrap(" Email:  " + getEmail()) + "\n";
	}

}
