package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ResultDto;
import com.example.demo.entity.Transaction;
import com.example.demo.service.TransactionService;

@RestController
@RequestMapping()
public class TransactionController {

	@Autowired
	private TransactionService transactionService;

	@GetMapping(value = { "/transactions", "/transactions/{order}" })
	@ResponseBody
	public ResponseEntity<List<Transaction>> getTransactions(@PathVariable("order") Optional<String> order) {
		final List<Transaction> response = transactionService.getTransactions(order);
		if (response != null) {
			if(!response.isEmpty()) {
				return ResponseEntity.ok(response);			}
			
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.noContent().build();
	}

	@GetMapping(value = { "/iban/{iban}", "/iban/{iban}/{order}" })
	@ResponseBody
	public ResponseEntity<List<Transaction>> getTransactionsById(@PathVariable("iban") String iban,
			@PathVariable("order") Optional<String> order) {
		final List<Transaction> response = transactionService.getTransactionsByIban(iban, order);
		if (response != null) {
			if(!response.isEmpty()) {
				return ResponseEntity.ok(response);			}
			
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.noContent().build();

	}

	@PostMapping("/transaction")
	@ResponseBody
	public ResponseEntity<ResultDto> setTransactions(@RequestBody Transaction json) {
		final ResultDto response = transactionService.setTransaction(json);
		if (response != null) {
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.noContent().build();
	}

}
