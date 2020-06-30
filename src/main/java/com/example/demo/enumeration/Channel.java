package com.example.demo.enumeration;

/***
 * Enum to indicate type of channels<br>
 * <strong>C</strong> for client<br>
 * <strong>A</strong> for atm<br>
 * <strong>I</strong> for internal
 */
public enum Channel {
	CLIENT("CLIENT"), ATM("ATM"), INTERNAL("INTERNAL");

	private final String channel;

	Channel(String channel) {
		this.channel = channel;
	}

	public String getChannel() {
		return this.channel;
	}

}
