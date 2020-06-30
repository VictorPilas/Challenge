package com.example.demo.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.PayloadDto;
import com.example.demo.service.PayloadService;

@RestController
@RequestMapping()
public class PayloadController {

	@Autowired
	private PayloadService payloadService;

	@GetMapping("/payload")
	public ResponseEntity<HashMap<String, Object>> getTransactions(@RequestBody PayloadDto json) {
		final HashMap<String, Object> response = payloadService.processPayload(json);		
		if (response != null) {
			if(!response.isEmpty()) {
				return ResponseEntity.ok(response);
			}			
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.noContent().build();
	}
}
