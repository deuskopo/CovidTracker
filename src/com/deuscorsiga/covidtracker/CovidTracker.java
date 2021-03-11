package com.deuscorsiga.covidtracker;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class CovidTracker {

	public static void main(String[] args) throws IOException {
		Scanner scanner = new Scanner(System.in);
		ArrayList<Question> questions = importQuestions("questions.csv");
		ArrayList<String[]> guidelines = importGuidelines();
		ArrayList<User> allUsers = new ArrayList<User>();
		// TODO import users

		CovidTrackerApp covidApp = new CovidTrackerApp(scanner, questions, guidelines, allUsers);
		covidApp.start();

		allUsers = covidApp.getUsers();

		System.out.println("3...\n2...\n1...\nProgam Ended Successfully");
	}

	private static ArrayList<String[]> importGuidelines() {
		ArrayList<String[]> guidelines = new ArrayList<String[]>();

		try (BufferedReader br = new BufferedReader(new FileReader("guidelines.csv"))) {
			String readLine;

			while ((readLine = br.readLine()) != null) {
				final ArrayList<String> parsed = parseCSVline(readLine);
				final String[] parsedToString = parsed.toArray(new String[parsed.size()]);
				guidelines.add(parsedToString);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return guidelines;
	}

	private static ArrayList<Question> importQuestions(String fileName) {
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			ArrayList<Question> questions = new ArrayList<Question>();
			String readLine = br.readLine(); // throw first line
			while ((readLine = br.readLine()) != null) {
				final ArrayList<String> parsed = parseCSVline(readLine);

				// process parse
				final boolean isScreen = Boolean.parseBoolean(parsed.get(0));
				final boolean isOptional = Boolean.parseBoolean(parsed.get(1));
				final int type = Integer.parseInt(parsed.get(2));
				final long min = Long.parseLong(parsed.get(3));
				final long max = Long.parseLong(parsed.get(4));
				final String text = parsed.get(5).trim();
				final String defaultValue = parsed.get(6).trim();
				final Question q = new Question(isScreen, isOptional, type, min, max, text, defaultValue);

				questions.add(q);
			}

			return questions;
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return new ArrayList<Question>();

	}

	private static ArrayList<String> parseCSVline(String str) {
		StringBuilder sb = new StringBuilder(str);
		char newDelimiter = '$';

		for (int i = 32; i < 127; i++) {
			if (str.indexOf((char) i) == -1) {
				newDelimiter = (char) i;
				break;
			}
		}

		boolean insideQuote = false;
		for (int i = 0; i < sb.length(); i++) {
			if (str.charAt(i) == '"') {
				insideQuote = !insideQuote;
				sb.setCharAt(i, '\0');
			} else if (str.charAt(i) == ',' && !insideQuote) {
				sb.setCharAt(i, newDelimiter);
			}
		}

		final String[] newStr = sb.toString().split(String.valueOf(newDelimiter), -1);
		final ArrayList<String> ret = new ArrayList<String>(Arrays.asList(newStr));

		return ret;

	}
}
