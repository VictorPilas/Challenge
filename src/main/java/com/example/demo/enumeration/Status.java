package com.example.demo.enumeration;

/***
 * Enum to indicate type of status<br>
 */
public enum Status {
	PENDING("PENDING"), SETTLED("SETTLED"), FUTURE("FUTURE"), INVALID("INVALID");

	private final String status;

	Status(String status) {
		this.status = status;
	}

	public String getStatus() {
		return this.status;
	}

}
