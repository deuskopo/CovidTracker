package com.deuscorsiga.covidtracker;

public class Question {
	private String text, defaultValue;
	private int type;
	private boolean isScreen, isOptional;
	private long min, max;
	private Error error;

	Question() {
		this(true, true, 1, 0, 0, "", "");
	}

	Question(boolean isScreen, boolean isOptional, int type, long min, long max, String text, String defaultValue) {
		this.isScreen = isScreen;
		this.isOptional = isOptional;
		this.type = type;
		this.text = text;
		this.defaultValue = defaultValue;
		setError();
		if (min > max) {
			final long temp = min;
			min = max;
			max = temp;
		}
		this.min = min;
		this.max = max;
	}

	private void setError() {
		for (Error err : Error.values()) {
			if (type == err.getId()) {
				error = err;
			}
		}
	}

	public Error getError() {
		return error;
	}

	public boolean isScreen() {
		return isScreen;
	}

	public boolean isOptional() {
		return isOptional;
	}

	public int getType() {
		return type;
	}

	public long getMin() {
		return min;
	}

	public long getMax() {
		return max;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	@Override
	public String toString() {
		return text;
	}
}
