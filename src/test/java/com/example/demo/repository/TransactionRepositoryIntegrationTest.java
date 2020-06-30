package com.example.demo.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;

import com.example.demo.ChallengeApplication;
import com.example.demo.entity.Transaction;

@SpringBootTest(classes = ChallengeApplication.class)
public class TransactionRepositoryIntegrationTest {

	
	@Autowired
    private TransactionRepository transactionRepository;	
	
	@Test
	public void whenFindByIbanTest(){
	    // given
		String iban = "ES9820385778983000760235";
		String reference_expected = "132";
	    // when
	    List<Transaction> found = transactionRepository.findByIban(iban);

	    // then
	    assertNotNull(found);	
	    assertEquals(reference_expected,found.get(0).getReference());	
	}
	
	@Test
	public void whenFindByIbanSortAscTest(){
		// given
		String iban = "ES9820385778983000760236";
		Sort sort = Sort.by(Sort.Direction.DESC, "amount");		
	 
	    // when
	    List<Transaction> found = transactionRepository.findByIban(iban,sort);

	    // then	
	    Assertions.assertThat(found)
        .extracting(Transaction::getReference)
        .containsExactly("12345B", "12345A");	
	}
	
	@Test
	public void whenFindByIbanSortDescTest(){
		// given
		String iban = "ES9820385778983000760236";
		Sort sort = Sort.by(Sort.Direction.ASC, "amount");		
	 
	    // when
	    List<Transaction> found = transactionRepository.findByIban(iban,sort);

	    // then	
	    Assertions.assertThat(found)
        .extracting(Transaction::getReference)
        .containsExactly("12345A", "12345B");	
	}
}
