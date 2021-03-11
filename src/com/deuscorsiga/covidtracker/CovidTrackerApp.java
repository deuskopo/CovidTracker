package com.deuscorsiga.covidtracker;

import java.util.ArrayList;
import java.util.Scanner;

public class CovidTrackerApp extends CovidTrackerAppMethods {
	private Scanner scanner;
	private User loggedUser;
	private int loggedUserIndex;
	private ArrayList<User> allUsers;
	private ArrayList<Question> profileQuestions;
	private ArrayList<Question> screenQuestions;
	private ArrayList<String[]> guidelines;

	private int menu;
	private final int EXIT = 0;
	private final int MAIN_MENU = 1;
	private final int EDIT_PROFILE_MENU = 2;
	private final int VIEW_PROFILE_MENU = 3;
	private final int SCREENING_MENU = 4;
	private final int GUIDELINE_MENU = 5;
	private final int END_MENU = 6;

	private boolean printFirstError;

	CovidTrackerApp(Scanner scanner, ArrayList<Question> questions, ArrayList<String[]> guidelines,
			ArrayList<User> allUsers) {
		this.scanner = scanner;
		this.guidelines = guidelines;
		this.allUsers = allUsers;

		profileQuestions = new ArrayList<Question>();
		screenQuestions = new ArrayList<Question>();

		for (Question q : questions) {
			if (q.isScreen()) {
				screenQuestions.add(q);
			} else {
				profileQuestions.add(q);
			}
		}

		menu = MAIN_MENU;
		printFirstError = false;
	}

	public void start() {

		// TODO Administrator console

		while (menu != EXIT) {
			switch (menu) {
			case MAIN_MENU:
				mainMenu();
				break;
			case EDIT_PROFILE_MENU:
				editProfile();
				break;
			case VIEW_PROFILE_MENU:
				viewProfile();
				break;
			case SCREENING_MENU:
				screenQuestions();
				break;
			case GUIDELINE_MENU:
				guidelinesMenu();
				break;
			case END_MENU:
				endMenu();
				break;
			}
		}
		// TODO update Array list

		// TODO cleanup code

	}

	private void mainMenu() {
		printHeader("Global Health Impact (GHI) / Covid Screening Tool");

		System.out.println(toCenter("Welcome!\n"));
		System.out.println(toCenter("Please enter your 6-digit Employee / Client ID"));
		System.out.println(toCenter("If you do not have one, enter \"register\""));

		boolean done = false;
		boolean isExit = false;
		do {
			printPrompt();
			final String userInput = scanner.nextLine().trim().toLowerCase();
			// TODO check first for existing IDs here
			if (matches(userInput, "exit", false)) {
				menu = EXIT;
				isExit = true;
				done = true;
			} else if (matchesPartially(userInput, "register")) {
				done = true;
				loggedUser = new User();
				allUsers.add(loggedUser);
				loggedUserIndex = allUsers.size() - 1;
			} else {
				Error.DEFAULT.print();
				if (!printFirstError && !userInput.isEmpty()) {
					Error.FIRST_TIME.print();
					printFirstError = true;
				}
			}
		} while (!done);

		if (!isExit) {
			menu = EDIT_PROFILE_MENU;
		}
	}

	private void editProfile() {
		printHeader("Edit Profile"); // TODO change to Register if new user

		final String[] employeeOrClient = { "employee", "client" };
		boolean done = false;
		if (!loggedUser.isProfileSet()) {
			System.out.println(toCenter("Are you an \"employee\" or a \"client?\" Note that the answer to this can not be changed."));

			do {
				printPrompt();
				final String userInput = scanner.nextLine().trim().toLowerCase();
				if (matchesPartially(userInput, employeeOrClient)) {
					if (whichMatchesPartially(userInput, employeeOrClient).equals("employee")) {
						loggedUser.setEmployee(true);
					}
					done = true;
					break;
				}
				if (!done) {
					Error.DEFAULT.print();
					if (!printFirstError && !userInput.isEmpty()) {
						Error.FIRST_TIME.print();
						printFirstError = true;
					}
				}
			} while (!done);
		}
		// Get user information
		System.out.println("\n" + toCenter("Please provide the following information...") + "\n");

		ArrayList<String> profileAnswers = getAnswers(profileQuestions, scanner);

		loggedUser.setFirst(profileAnswers.get(0));
		loggedUser.setLast(profileAnswers.get(1));
		loggedUser.setAge(Integer.parseInt(profileAnswers.get(2)));
		loggedUser.setPhone(profileAnswers.get(3));
		loggedUser.setEmail(profileAnswers.get(4));
		loggedUser.ProfileIsSet();
		loggedUser.initializeScreenAnswers(screenQuestions.size());

		updateAllUsers(allUsers, loggedUser, loggedUserIndex);

		menu = VIEW_PROFILE_MENU;

	}

	private void viewProfile() {
		printHeader("View Profile");

		System.out.println(loggedUser);

		System.out.println(toCenter("If you wish to edit your information, type \"edit\"."));
		System.out.println(toCenter("Alternatively, hit <ENTER> to confirm and proceed to the screening questions..."));
		boolean done = false;
		do {
			printPrompt();
			final String userInput = scanner.nextLine().trim().toLowerCase();
			if (userInput.isEmpty()) {
				menu = SCREENING_MENU;
				done = true;
			} else if (matchesPartially(userInput, "edit")) {
				menu = EDIT_PROFILE_MENU;
				done = true;
			} else {
				Error.DEFAULT.print();
				if (!printFirstError && !userInput.isEmpty()) {
					Error.FIRST_TIME.print();
					printFirstError = true;
				}
			}
		} while (!done);
	}

	private void screenQuestions() {
		printHeader("Screening Questions");

		System.out.println(toCenter("Please answer the following questions with a \"yes\" or \"no\"") + "\n");

		int questionNo = 0;
		for (Question question : screenQuestions) {
			final String userInput = getAnswer(question, scanner);
			if (userInput.equals("yes")) {
				loggedUser.setScreenAnswers(questionNo, true);
				break;
			}
			questionNo++;
		}
		if (loggedUser.answeredYes()) {
			menu = GUIDELINE_MENU;
		} else {
			menu = END_MENU;
		}

		updateAllUsers(allUsers, loggedUser, loggedUserIndex);

	}

	private void guidelinesMenu() {
		printHeader("Guidelines");

		final Boolean[] screenAnswers = loggedUser.getScreenAnswers();
		int whichGuideline = -1;
		for (int i = 0; i < screenAnswers.length; i++) {
			if (screenAnswers[i]) {
				whichGuideline = i;
				break;
			}
		}
		final String guideline[] = guidelines.get(whichGuideline);
		System.out.println(toCenter("PLEASE READ CAREFULLY"));
		System.out.println(toCenter(
				"FOR EVERYONE'S SAFETY, please read and follow the following guidelines mandated by the Department of Health."));
		for (int i = 1; i <= guideline.length; i++) {
			System.out.println(toCenter(i + ") " + guideline[i - 1]));
		}
		System.out.println(toCenter("Please hit <ENTER> to restart the process..."));
		scanner.nextLine();

		menu = MAIN_MENU;
	}

	private void endMenu() {
		printHeader("Thank you");
		System.out.println(toCenter("Thank you very much for your responses! You may now proceed."));
		System.out.println(toCenter("Please hit <ENTER> to restart the process..."));
		scanner.nextLine();

		menu = MAIN_MENU;
	}

	// return allUsers for export
	public ArrayList<User> getUsers() {
		return allUsers;
	}

}
