package com.example.demo.controller;

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

import com.example.demo.service.TransactionService;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	TransactionService transactionService;

	@Test
	public void getTransactionsWithoutOrderTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/transactions")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void getTransactionsWithOrderTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/transactions/desc"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void getTransactionsWithIbanTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/iban/ES9820385778983000760236"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void getTransactionsWithIbanAndOrderTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/iban/ES9820385778983000760236/desc"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void setTransactionMissingBodyTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/transaction"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	public void setTransactionOKTest() throws Exception {
		/**
		 * Given
		 */
		JSONObject jsonRequest = new JSONObject();
		jsonRequest.accumulate("reference", "12345");
		jsonRequest.accumulate("iban", "ES9820385778983000760236");
		jsonRequest.accumulate("date", null);
		jsonRequest.accumulate("amount", -45.31);
		jsonRequest.accumulate("fee", 1000);
		jsonRequest.accumulate("description", "Test AT 345");

		/**
		 * When
		 */
		ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/transaction")
				.contentType(MediaType.APPLICATION_JSON).content(jsonRequest.toString()).characterEncoding("utf-8"));

		/**
		 * Then
		 */
		result.andExpect(status().isOk());
	}

}
