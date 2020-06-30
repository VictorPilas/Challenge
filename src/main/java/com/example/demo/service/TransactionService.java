package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.dto.ResultDto;
import com.example.demo.entity.Transaction;

public interface TransactionService {

	/***
	 * This method add a transaction if the balance is positive or 0
	 */
	ResultDto setTransaction(Transaction transaction);

	/***
	 * This method return all the transactions
	 */
	List<Transaction> getTransactions(Optional<String> order);

	/***
	 * This method return all the transactions with the iban selected
	 */
	List<Transaction> getTransactionsByIban(String iban, Optional<String> order);

	Optional<Transaction> getTransactionById(String reference);
}
