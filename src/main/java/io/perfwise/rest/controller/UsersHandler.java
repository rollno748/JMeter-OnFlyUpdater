package io.perfwise.rest.controller;

public class UsersHandler {

	public static Object handle(User user) {
		
		if (user.getType().equalsIgnoreCase("add")) {
			addUsers(user);
		}else if (user.getType().equalsIgnoreCase("remove")) {
			removeUsers(user);
			
		} else {
			return "{\"message\": \"request body error\"}";
		}
		return user;
	}
	
	
	
	
	public static Object addUsers(User user) {
		
		return null;
	}
	
	
	
	public static Object removeUsers(User user) {

		return null;
	}
	
	

}
