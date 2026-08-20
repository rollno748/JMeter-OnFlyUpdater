package io.perfwise.utils;

public class Credentials {
	private static String keyPass;
	public Credentials(String key) {
		Credentials.keyPass = key;
	}

	public static boolean validate(String pass) {
		if (keyPass == null || pass == null) {
			return false;
		}
		return keyPass.equals(pass);
	}

}
