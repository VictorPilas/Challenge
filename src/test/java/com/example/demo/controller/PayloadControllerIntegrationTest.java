package com.example.demo.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.demo.service.PayloadService;

@SpringBootTest
@AutoConfigureMockMvc
public class PayloadControllerIntegrationTest {		
	
	@Autowired
    private MockMvc mockMvc;
    
	@Autowired	
    private PayloadService payloadService;	
	
	@Test
    public void requestMissingBodyTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/payload")).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
	
	@Test
	public void requestEmptyBodyChannelTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/payload").contentType(MediaType.APPLICATION_JSON).content("{\"reference\":\"12345B\", \"channel\":\"\"}")).andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
	@Test
	public void requestEmptyBodyReferenceTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/payload").contentType(MediaType.APPLICATION_JSON).content("{\"reference\":\", \"channel\":\"CLIENT\"}")).andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
	
	
	@Test
	public void processPayloadResponseOKTest() throws Exception {
		/**
		 * Given
		 */
		JSONObject jsonRequest = new JSONObject();
		jsonRequest.accumulate("reference", "12345");
		jsonRequest.accumulate("channel", "CLIENT");
		/**
		 * When
		 */
		ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/payload").contentType(MediaType.APPLICATION_JSON).content(jsonRequest.toString()).characterEncoding("utf-8"));

		/**
		 * Then
		 */		
		result.andExpect(status().isOk()).andExpect(content().json("{\"reference\":\"12345\",\"status\":\"INVALID\"}"));
	}
	
	
	
}
