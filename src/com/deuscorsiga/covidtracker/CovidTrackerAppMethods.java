package com.deuscorsiga.covidtracker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import org.apache.commons.text.WordUtils;

public abstract class CovidTrackerAppMethods {

	final static int MAX_LENGTH = 90;
	final static char BORDER_CHAR = '-';
	final static String PROMPT_CHAR = "\n>>";

	public static String generateBorder() {
		StringBuilder border = new StringBuilder();
		for (int i = 0; i < MAX_LENGTH; i++) {
			border.append(BORDER_CHAR);
		}
		return border.toString();
	}

	public static String toCenter(String str) {
		StringBuilder returnString = new StringBuilder();

		if (str.length() > MAX_LENGTH) {
			final String newStr[] = toWrap(str).split("\n");
			for (String s : newStr) {
				returnString.append(toCenter(s) + "\n");
			}
			return returnString.toString();
		}

		for (int i = 0; i * 2 < MAX_LENGTH - str.length(); i++) {
			returnString.append(" ");
		}
		returnString.append(str);

		return returnString.toString();
	}

	public static void printPrompt() {
		System.out.print(PROMPT_CHAR + " ");
	}

	public static void printHeader(String title) {
		System.out.println("\n" + generateBorder());
		System.out.println(toCenter(title));
		System.out.println(generateBorder() + "\n");
	}

	public static String toWrap(String str) {
		return WordUtils.wrap(str, MAX_LENGTH, "\n", false);
	}

	public static boolean matches(String input, String compareTo, boolean partialMatch) {

		if (input.isBlank() && compareTo.length() != 0) {
			return false;
		}

		input = input.toLowerCase();
		compareTo = compareTo.toLowerCase();

		if (partialMatch && input.length() <= compareTo.length()) {
			for (int i = 0; i < input.length(); i++) {
				if (input.charAt(i) != compareTo.charAt(i)) {
					return false;
				}
			}
			return true;
		}
		return input.equals(compareTo);
	}

	public static boolean matchesPartially(String input, String compareTo) {
		return matches(input, compareTo, true);
	}

	public static boolean matchesPartially(String input, String[] compareTo) {
		for (String compare : compareTo) {
			if (matchesPartially(input, compare)) {
				return true;
			}
		}
		return false;
	}

	public static String whichMatchesPartially(String input, String[] compareTo) {
		String ret = "";
		for (String compare : compareTo) {
			if (matchesPartially(input, compare)) {
				return compare;
			}
		}
		return ret;
	}

	public static boolean isNumeric(String input) {
		if (input.isEmpty()) {
			return false;
		}
		for (int i = 0; i < input.length(); i++) {
			if (i == 0 && input.charAt(i) == '-') {
				if (input.length() == 1) {
					return false;
				} else {
					continue;
				}
			}
			if (Character.digit(input.charAt(i), 10) < 0) {
				return false;
			}
		}
		return true;
	}

	public static boolean isNumericBetween(String input, long min, long max) {
		if (isNumeric(input)) {
			final long num = Long.parseLong(input);
			if (min > max) {
				final long temp = min;
				min = max;
				max = temp;
			}
			return num >= min && num <= max;
		}
		return false;
	}

	public String getAnswer(Question question, Scanner scanner) {
		return getAnswers(new ArrayList<Question>(Arrays.asList(question)), scanner).get(0);
	}

	public ArrayList<String> getAnswers(ArrayList<Question> questions, Scanner scanner) {
		ArrayList<String> returnAnswers = new ArrayList<String>();
		final String[] yesOrNo = { "yes", "no" };
		int questionNo = 1;
		for (Question question : questions) {
			System.out.println(toCenter(questionNo++ + ") " + question.toString()));
			final int type = question.getType();
			final boolean isOptional = question.isOptional();
			boolean done = false;
			do {
				printPrompt();
				final String userInput = scanner.nextLine().trim();
				final boolean isEmpty = userInput.isEmpty();
				final boolean isBelowMaxCharacters = userInput.length() <= question.getMax();
				switch (type) {
				case 0: // string
					if (isOptional && isEmpty) {
						returnAnswers.add(question.getDefaultValue());
						done = true;
					} else if (!isEmpty && isBelowMaxCharacters) {
						returnAnswers.add(userInput);
						done = true;
					} else if (!isEmpty && !isBelowMaxCharacters) {
						Error.DEFAULT.print(question.getMax());
					} else if (!isOptional && isEmpty) {
						Error.DEFAULT.print();
					}
					break;
				case 2: // yes or no
					if (isOptional) {
						Error.CANNOT_BE_EMPTY.print();
					} else if (matchesPartially(userInput, yesOrNo)) {
						returnAnswers.add(whichMatchesPartially(userInput, yesOrNo));
						done = true;
					} else {
						Error.NOT_YESORNO.print();
					}
					break;
				case 5: // age
				case 10: // phone
					final long min = question.getMin();
					final long max = question.getMax();
					final boolean isBetweenMinAndMax = isNumericBetween(userInput, min, max);
					if (isOptional && isEmpty || !isBetweenMinAndMax) {
						switch (type) {
						case 5:
							Error.INVALID_AGE.print();
							break;
						case 10:
							Error.INVALID_PHONE.print();
							break;
						}
					} else if ((!isOptional || !isEmpty) && isBetweenMinAndMax) {
						returnAnswers.add(userInput);
						done = true;
					}
					break;
				}
			} while (!done);
		}

		return returnAnswers;
	}

	public void updateAllUsers(ArrayList<User> allUsers, User user, int index) {
		allUsers.set(index, user);
	}

}
