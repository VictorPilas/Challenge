package com.example.demo.dto;

import java.io.Serializable;

import com.example.demo.entity.Transaction;

public class ResultDto implements Serializable {

	private static final long serialVersionUID = 4513741196180970122L;

	String result;
	Transaction transaction;

	public ResultDto() {

	}

	public ResultDto(String result, Transaction transaction) {
		this.result = result;
		this.transaction = transaction;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

}
