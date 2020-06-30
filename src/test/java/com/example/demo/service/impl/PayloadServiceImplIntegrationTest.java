package com.example.demo.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.ChallengeApplication;
import com.example.demo.dto.PayloadDto;
import com.example.demo.entity.Transaction;
import com.example.demo.enumeration.Channel;
import com.example.demo.enumeration.Status;
import com.example.demo.repository.TransactionRepository;

@SpringBootTest(classes = ChallengeApplication.class)
public class PayloadServiceImplIntegrationTest {

	@Autowired
	private PayloadServiceImpl payloadServiceImpl;
	
	@Autowired
	TransactionRepository transactionRepository;
	
	@Test
	public void processPayloadWrongReferenceTest() {
		//Given: A transaction that is not stored in our system
		PayloadDto payload = new PayloadDto();
		payload.setReference("1"); 
		payload.setChannel(Channel.ATM);
		//When: I check the status from any channel
		HashMap<String, Object> found = payloadServiceImpl.processPayload(payload);
		//Then: The system returns the status 'INVALID'
		assertEquals(found.get("reference"), payload.getReference());
		assertEquals(found.get("status"), Status.INVALID);
	}
	
	@Test
	public void processPayloadTestCorrectReferenceChannelClientTest() {
		//Given: A transaction that is stored in our system
		PayloadDto payload = new PayloadDto();
		payload.setReference("12345B"); 
		payload.setChannel(Channel.CLIENT);
		//When: I check the status from CLIENT or ATM channel And the transaction date is before today
		HashMap<String, Object> found = payloadServiceImpl.processPayload(payload);
		//Then: The system returns the status 'SETTLED'	And the amount substracting the fee
		assertEquals(found.get("reference"), payload.getReference());
		assertEquals(found.get("status"), Status.SETTLED);
		assertEquals(found.get("amount"), BigDecimal.valueOf(255.31));
	}
	
	@Test
	public void processPayloadTestCorrectReferenceChannelInternalTest() {
		//Given: A transaction that is stored in our system
		PayloadDto payload = new PayloadDto();
		payload.setReference("12345B"); 
		payload.setChannel(Channel.INTERNAL);
		//When: I check the status from INTERNAL channel And the transaction date is before today
		HashMap<String, Object> found = payloadServiceImpl.processPayload(payload);
		//Then: The system returns the status 'SETTLED'	And the amount and the fee
		assertEquals(found.get("reference"), payload.getReference());
		assertEquals(found.get("status"), Status.SETTLED);
		assertEquals(found.get("amount"), BigDecimal.valueOf(256.31));
		assertNotNull(found.get("fee"));
	}
	
	@Test
	public void processPayloadTestCorrectReferenceChannelATMTodayTest() {
		//Given: A transaction that is stored in our system
		Transaction t = new Transaction();
		t.setReference("12");
		t.setIban("1");
		t.setDate(new Date());
		t.setAmount(BigDecimal.valueOf(255.31));
		t.setFee(BigDecimal.ZERO);
		transactionRepository.save(t);
		PayloadDto payload = new PayloadDto();
		payload.setReference("12"); 
		payload.setChannel(Channel.ATM);
		//When: I check the status from CLIENT or ATM channel And the transaction date is equals to today
		HashMap<String, Object> found = payloadServiceImpl.processPayload(payload);
		//The system returns the status 'PENDING' And the amount substracting the fee
		assertEquals(found.get("reference"), payload.getReference());
		assertEquals(found.get("status"), Status.PENDING);
		assertEquals(found.get("amount"), BigDecimal.valueOf(255.31));
		//finally
		transactionRepository.deleteById("12");
	}
	
	@Test
	public void processPayloadTestCorrectReferenceChannelInternalTodayTest() {
		//Given: A transaction that is stored in our system
		Transaction t = new Transaction();
		t.setReference("12");
		t.setIban("1");
		t.setDate(new Date());
		t.setAmount(BigDecimal.valueOf(255.31));
		t.setFee(BigDecimal.ZERO);
		transactionRepository.save(t);
		PayloadDto payload = new PayloadDto();
		payload.setReference("12"); 
		payload.setChannel(Channel.INTERNAL);
		//When: I check the status from INTERNAL channel And the transaction date is equals to today
		HashMap<String, Object> found = payloadServiceImpl.processPayload(payload);
		//The system returns the status 'PENDING' And the amount and the fee
		assertEquals(found.get("reference"), payload.getReference());
		assertEquals(found.get("status"), Status.PENDING);
		assertEquals(found.get("amount"), BigDecimal.valueOf(255.31));
		assertNotNull(found.get("fee"));
		//finally
		transactionRepository.deleteById("12");
	}
	
	@Test
	public void processPayloadTestCorrectReferenceChannelClientTFutureTest() {
		//Given: A transaction that is stored in our system
		PayloadDto payload = new PayloadDto();
		payload.setReference("132"); 
		payload.setChannel(Channel.CLIENT);
		//When: I check the status from CLIENT channel And the transaction date is greater than today
		HashMap<String, Object> found = payloadServiceImpl.processPayload(payload);
		//The system returns the status 'FUTURE' And the amount substracting the fee 
		assertEquals(found.get("reference"), payload.getReference());
		assertEquals(found.get("status"), Status.FUTURE);
		assertNotNull(found.get("amount"));

	}
	
	@Test
	public void processPayloadTestCorrectReferenceChannelAtmTFutureTest() {
		//Given: A transaction that is stored in our system
		PayloadDto payload = new PayloadDto();
		payload.setReference("132"); 
		payload.setChannel(Channel.ATM);
		//When: I check the status from CLIENT channel And the transaction date is greater than today
		HashMap<String, Object> found = payloadServiceImpl.processPayload(payload);
		//The system returns the status 'FUTURE' And the amount substracting the fee 
		assertEquals(found.get("reference"), payload.getReference());
		assertEquals(found.get("status"), Status.PENDING);
		assertNotNull(found.get("amount"));

	}
	
	@Test
	public void processPayloadTestCorrectReferenceChannelInternalTFutureTest() {
		//Given: A transaction that is stored in our system
		PayloadDto payload = new PayloadDto();
		payload.setReference("132"); 
		payload.setChannel(Channel.INTERNAL);
		//When: I check the status from CLIENT channel And the transaction date is greater than today
		HashMap<String, Object> found = payloadServiceImpl.processPayload(payload);
		//The system returns the status 'FUTURE' And the amount substracting the fee 
		assertEquals(found.get("reference"), payload.getReference());
		assertEquals(found.get("status"), Status.FUTURE);
		assertNotNull(found.get("amount"));
		assertNotNull(found.get("fee"));

	}
}
