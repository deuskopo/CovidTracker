package com.deuscorsiga.covidtracker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Prompt {

	private Scanner scanner;
	private ArrayList<String> requiredAnswers;
	private String input;

	Prompt() {
		scanner = new Scanner(System.in);
		requiredAnswers = new ArrayList<String>();
		input = "";
	}

	public void appendAnswer(String answer) {
		requiredAnswers.add(answer);
	}

	public void addAnswers(String[] answers) {
		requiredAnswers.addAll(Arrays.asList(answers));
	}

	public void setAnswersAsYesOrNo() {
		flushAnswers();
		appendAnswer("yes");
		appendAnswer("no");
	}

	public void flushAnswers() {
		requiredAnswers.clear();
	}

	public void askInput(boolean caseSensitive) {
		input = scanner.nextLine().trim();
		if (!caseSensitive) {
			input = input.toLowerCase();
		}
	}

	public void askInput() {
		askInput(false);
	}

	public String getInput() {
		return input;
	}

	public boolean inputFound() {
		return requiredAnswers.isEmpty() || requiredAnswers.contains(input);
	}

	public boolean inputEmpty() {
		return input.isEmpty();
	}

	public boolean inputIsInt() {
		if (inputEmpty()) {
			return false;
		}
		for (int i = 0; i < input.length(); i++) {
			if (!Character.isDigit(input.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public boolean inputIsIntBetween(int min, int max) {
		if (min > max) {
			int temp = min;
			min = max;
			max = temp;
		}
		return inputIsInt() && Integer.valueOf(input) >= min && Integer.valueOf(input) <= max;
	}

	public boolean inputIsLongBetween(long min, long max) {
		if (min > max) {
			long temp = min;
			min = max;
			max = temp;
		}
		return inputIsInt() && Long.valueOf(input) >= min && Long.valueOf(input) <= max;

	}
}
