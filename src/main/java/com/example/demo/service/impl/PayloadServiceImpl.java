package com.example.demo.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.PayloadDto;
import com.example.demo.entity.Transaction;
import com.example.demo.enumeration.Channel;
import com.example.demo.enumeration.Status;
import com.example.demo.service.PayloadService;
import com.example.demo.service.TransactionService;

@Service
public class PayloadServiceImpl implements PayloadService {

	@Autowired
	private TransactionService transactionService;

	public HashMap<String, Object> processPayload(PayloadDto payload) {

		HashMap<String, Object> map = new HashMap<>();
		Optional<Transaction> transaction = transactionService.getTransactionById(payload.getReference());
		Date today = new Date();
		map.put("reference", payload.getReference());
		if (!transaction.isPresent()) {
			map.put("status", Status.INVALID);
		} else {
			if (payload.getChannel().equals(Channel.CLIENT) || payload.getChannel().equals(Channel.ATM)) {

				if (transaction.get().getDate().before(today)) {
					map.put("status", Status.SETTLED);
				}
				if (DateUtils.isSameDay(transaction.get().getDate(), today)) {
					map.put("status", Status.PENDING);
				}
				if (payload.getChannel().equals(Channel.CLIENT) && transaction.get().getDate().after(today)) {
					map.put("status", Status.FUTURE);
				}
				if (payload.getChannel().equals(Channel.ATM) && transaction.get().getDate().after(today)) {
					map.put("status", Status.PENDING);
				}
				map.put("amount", transaction.get().getAmount().subtract(transaction.get().getFee()));
			}
			if (payload.getChannel().equals(Channel.INTERNAL)) {
				if (transaction.get().getDate().before(today)) {
					map.put("status", Status.SETTLED);
				}
				if (DateUtils.isSameDay(transaction.get().getDate(), today)) {
					map.put("status", Status.PENDING);
				}
				if (transaction.get().getDate().after(today)) {
					map.put("status", Status.FUTURE);
				}
				map.put("amount", transaction.get().getAmount());
				map.put("fee", transaction.get().getFee());
			}

		}
		return map;
	}
}
