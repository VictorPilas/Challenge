package com.example.demo.dto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.example.demo.enumeration.Channel;

public class PayloadDto {

	private String reference;

	@Enumerated(EnumType.STRING)
	public Channel channel;

	public PayloadDto() {

	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	};

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}
}
