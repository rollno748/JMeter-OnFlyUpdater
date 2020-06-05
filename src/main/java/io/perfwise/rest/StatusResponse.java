package io.perfwise.rest;

public enum StatusResponse {

	SUCCESS("Success"), ERROR("Error"), AUTHERROR("Authentication Error");

	private String status;

	private StatusResponse(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}
	
}
