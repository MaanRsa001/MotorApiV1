package com.maan.eway.common.service.impl;

import java.util.Map;

import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CalcAsyncService {

    @Async("calctaskExecuter")
	public void callCalcAsync(Map<String, Object> requestMap, String token, String url) {

		String threadName = Thread.currentThread().getName();
		System.out.println("Thread running: " + threadName);

		try {
			ObjectMapper mapper = new ObjectMapper();
			String body = mapper.writeValueAsString(requestMap);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", token);

			HttpEntity<String> entity = new HttpEntity<>(body, headers);

			RestTemplate restTemplate = new RestTemplate();

			// 🔥 Fire & forget (non-blocking at caller side)
			
			restTemplate.postForEntity(url, entity, Void.class);

			
			
			System.out.println("CALC async triggered | ReqRefNo=" + requestMap.get("RequestReferenceNo") + " | Thread="
					+ threadName);

		} catch (Exception e) {
			System.err.println("❌ Async CALC failed | Thread=" + threadName + " | " + e.getMessage());
		}
	}
}
