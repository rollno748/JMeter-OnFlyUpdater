package io.perfwise.utils;

public class Credentials {

	private static String keyPass;

	public Credentials(String key) {
		Credentials.keyPass = key;

	}

	public static boolean validate(String pass) {
		return keyPass.equals(pass) ? true : false;
	}

}
