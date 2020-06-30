package com.example.demo.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.ChallengeApplication;
import com.example.demo.dto.ResultDto;
import com.example.demo.entity.Transaction;

@SpringBootTest(classes = ChallengeApplication.class)
public class TransactionServiceImplIntegrationTest {
	
	private static final String BALANCE_BELOW = "Account balance negative";
	private static final String BALANCE_ABOVE = "Account balance positive or 0 ";
	private static final String AMOUNT_MANDATORY = "Amount is missing ";
	
	@Autowired
	TransactionServiceImpl transactionServiceImpl;

	@Test
	public void getTransactionsNullValueTest() {
		// given
		Optional<String> order = Optional.ofNullable(null);		
		// when
		List<Transaction> found = transactionServiceImpl.getTransactions(order);		
		// then
	    assertEquals(found.size(),3);
		
	}
	
	@Test
	public void getTransactionsEmptyValueTest() {
		//given
		Optional<String> order = Optional.empty();		
		// when
		List<Transaction> found = transactionServiceImpl.getTransactions(order);
		assertEquals(found.size(),4);
		
	}
	
	@Test
	public void getTransactionsRandomValueTest() {
		// given
		Optional<String> order = Optional.of("foo");
		// when
		List<Transaction> found = transactionServiceImpl.getTransactions(order);		
		// then	
	    Assertions.assertThat(found)
	    .extracting(Transaction::getReference)
        .containsExactly("132","12345A", "12345B");	
		
	}	
	
	@Test
	public void getTransactionsOrderValueTest() {
		// given
		Optional<String> order = Optional.of("desc");
		// when
		List<Transaction> found = transactionServiceImpl.getTransactions(order);		
		// then	
	    Assertions.assertThat(found)
	    .extracting(Transaction::getReference)
        .containsExactly("12345B","12345A", "132");			
	}	
	
	
	@Test
	public void getTransactionsByIbanNullValueTest() {
		// given
		String iban = "ES9820385778983000760236";
		Optional<String> order = Optional.ofNullable(null);		
		// when
		List<Transaction> found = transactionServiceImpl.getTransactionsByIban(iban, order);		
		// then
	    assertEquals(found.size(),2);
		
	}
	
	@Test
	public void getTransactionsByIbanEmptyValueTest() {
		// given
		String iban = "ES9820385778983000760236";
		Optional<String> order = Optional.empty();		
		// when
		List<Transaction> found = transactionServiceImpl.getTransactionsByIban(iban, order);		
		// then
	    assertEquals(found.size(),2);
		
	}
	
	@Test
	public void getTransactionsByIbanRandomValueTest() {
		// given
		String iban = "ES9820385778983000760236";
		Optional<String> order = Optional.of("foo");		
		// when
		List<Transaction> found = transactionServiceImpl.getTransactionsByIban(iban, order);		
		// then
		Assertions.assertThat(found)
	    .extracting(Transaction::getReference)
        .containsExactly("12345A","12345B");	
		
	}
	
	@Test
	public void getTransactionsByIbanOrderValueTest() {
		// given
		String iban = "ES9820385778983000760236";
		Optional<String> order = Optional.of("desc");		
		// when
		List<Transaction> found = transactionServiceImpl.getTransactionsByIban(iban, order);		
		// then
		Assertions.assertThat(found)
	    .extracting(Transaction::getReference)
        .containsExactly("12345B","12345A");		
	}
	
	@Test
	public void setTransactionsTest() {
		// given
		Transaction transaction = new Transaction();	
		transaction.setReference("1456");
		transaction.setAmount(BigDecimal.ZERO);
		transaction.setIban("ES9820385778983000760234");
		// when
		ResultDto found = transactionServiceImpl.setTransaction(transaction);		
		// then
		assertEquals(found.getResult(), BALANCE_ABOVE);	
	}
	
	
	@Test
	public void setTransactionsNullAmountTest() {
		// given
		Transaction transaction = new Transaction();	
		transaction.setReference("1456");
		transaction.setIban("ES9820385778983000760234");
		// when
		ResultDto found = transactionServiceImpl.setTransaction(transaction);		
		// then
		assertEquals(found.getResult(), AMOUNT_MANDATORY);	
	}
	
	@Test
	public void setTransactionsBalanceTest() {
		// given
		Transaction transaction = new Transaction();	
		transaction.setReference("1456");
		transaction.setAmount(BigDecimal.ZERO);
		transaction.setIban("ES9820385778983000760234");
		transaction.setFee(BigDecimal.TEN);
		
		// when
		ResultDto found = transactionServiceImpl.setTransaction(transaction);		
		// then
		assertEquals(found.getResult(), BALANCE_BELOW);	
	}
	
	@Test
	public void setTransactionsNullDateTest() {
		// given
		Transaction transaction = new Transaction();	
		transaction.setReference("1456");
		transaction.setAmount(BigDecimal.ZERO);
		transaction.setIban("ES9820385778983000760234");
		transaction.setFee(BigDecimal.TEN);
		
		// when
		ResultDto found = transactionServiceImpl.setTransaction(transaction);		
		// then
		assertNotNull(found.getTransaction().getDate());	
	}
	
	@Test
	public void setTransactionsNullFeeTest() {
		// given
		Transaction transaction = new Transaction();	
		transaction.setReference("1456");
		transaction.setAmount(BigDecimal.ZERO);
		transaction.setIban("ES9820385778983000760234");
		
		// when
		ResultDto found = transactionServiceImpl.setTransaction(transaction);		
		// then
		assertNotNull(found.getTransaction().getFee());	
	}
	
	
	@Test
	public void getIdNullReferenceTest() {
		// given
		String reference = null;		
		// when
		String found = transactionServiceImpl.getId(reference);		
		// then
		assertEquals(found, "12345C");	
	}
	
	@Test
	public void getIdCorrectReferenceTest() {
		// given
		String reference = "12345D";		
		// when
		String found = transactionServiceImpl.getId(reference);		
		// then
		assertEquals(found, "12345D");	
	}
	
	@Test
	public void getIdWrongReferenceTest() {
		// given
		String reference = "12345B";		
		// when
		String found = transactionServiceImpl.getId(reference);		
		// then
		assertEquals(found, "12345C");	
	}
	
	@Test
	public void getBiggestidTest() {
		//given 
		List<Transaction> list = transactionServiceImpl.getTransactions(Optional.ofNullable(null));
		Integer expected = Integer.parseInt("12345B", 36);
		//when
		Integer found = transactionServiceImpl.getBiggestId(list);
		//then
		assertEquals(found,expected);
	
	}
	
	@Test
	public void checkBalanceTrueTest() {
		//given
		Transaction reference = new Transaction();
		reference.setReference("ES9820385778983000760235");
		reference.setAmount(BigDecimal.valueOf(32.0));
		reference.setFee(BigDecimal.ZERO);
		//when
		Boolean found = transactionServiceImpl.checkBalance(reference);
		//then
		assertTrue(found);
		
		
	}
	
	@Test
	public void checkBalanceFalseTest() {
		//given
		Transaction reference = new Transaction();
		reference.setReference("ES9820385778983000760235");
		reference.setAmount(BigDecimal.valueOf(32.0));
		reference.setFee(BigDecimal.valueOf(50.0));
		//when
		Boolean found = transactionServiceImpl.checkBalance(reference);
		//then
		assertFalse(found);
		
		
	}
	
}
