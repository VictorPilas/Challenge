package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
	
	List<Transaction> findByIban(String iban);
	List<Transaction> findByIban(String iban, Sort sort);
}
