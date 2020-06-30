package com.example.demo.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ResultDto;
import com.example.demo.entity.Transaction;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

	private static final String BALANCE_BELOW = "Account balance negative";
	private static final String BALANCE_ABOVE = "Account balance positive or 0 ";
	private static final String AMOUNT_MANDATORY = "Amount is missing ";
	
	@Autowired
	private TransactionRepository repository;

	public List<Transaction> getTransactions(Optional<String> order) {
		List<Transaction> returned = null;

		if (order.isPresent()) {
			returned = repository.findAll(Sort.by(Sort.Direction.ASC, "amount"));
			if (order.get().equals("desc")) {
				returned = repository.findAll(Sort.by(Sort.Direction.DESC, "amount"));
			}
		} else {
			returned = repository.findAll();
		}
		return returned;
	}

	public List<Transaction> getTransactionsByIban(String iban, Optional<String> order) {
		List<Transaction> returned = null;
		if (order.isPresent()) {
			returned = repository.findByIban(iban);
			if (order.get().equals("desc")) {
				returned = repository.findByIban(iban, Sort.by(Sort.Direction.DESC, "amount"));
			}
		} else {
			returned = repository.findByIban(iban);
		}
		return returned;
	}

	//add to database a transaction
	public ResultDto setTransaction(Transaction transaction) {
		String message = BALANCE_BELOW;
		transaction.setReference(getId(transaction.getReference()));
		if (transaction.getDate() == null) {
			transaction.setDate(new Date());
		}
		if (transaction.getFee() == null) {
			transaction.setFee(BigDecimal.ZERO);
		}
		if (transaction.getAmount() == null) {
			message = AMOUNT_MANDATORY;
		} else {
			if (checkBalance(transaction)) {
				repository.save(transaction);
				message = BALANCE_ABOVE;
			}
		}
		return new ResultDto(message, transaction);
	}

	public Optional<Transaction> getTransactionById(String reference) {
		return repository.findById(reference);
	}

	
	// Get reference (alphabetic + numeric, base 36)
	public String getId(String reference) {
		String returned = reference;
		
		List<Transaction> list = repository.findAll();
		
		//empty table id = "1;"
		if (list.isEmpty()) {
			returned = "1";
		}else {
			//table transactions with values
			if (StringUtils.equals(null,reference)) {
				reference = "";
			}	
			//If exists, create new one
			if(repository.existsById(reference) || reference.isEmpty()) {
				Integer valueInt = getBiggestId(list) + 1;
				returned = Integer.toString(valueInt, 36).toUpperCase();
			}			
		}
		return returned;
	}
	
	//return the biggest reference in base 36
	public Integer getBiggestId(List<Transaction> list) {
		Integer returned = 1;
		for(Transaction t : list) {
			if(Integer.parseInt(t.getReference(), 36) > returned) {				
				returned = Integer.parseInt(t.getReference(), 36);
			}
		}
		return returned;
	}

	public Boolean checkBalance(Transaction transaction) {
		Boolean returned = false;
		BigDecimal amount = BigDecimal.ZERO;
		if (StringUtils.equals(null,transaction.getIban())) {
			transaction.setIban("");
		}	
		List<Transaction> transactions = repository.findByIban(transaction.getIban());
		transactions.add(transaction);
		for (Transaction checked : transactions) {
			amount = amount.add(checked.getAmount().subtract(checked.getFee()));
		}
		if (amount.signum() != -1) {
			returned = true;
		}

		return returned;
	}
	
	
}
