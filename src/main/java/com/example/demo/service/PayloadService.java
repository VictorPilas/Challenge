package com.example.demo.service;

import java.util.HashMap;

import com.example.demo.dto.PayloadDto;

public interface PayloadService {

	HashMap<String, Object> processPayload(PayloadDto payload);
}
