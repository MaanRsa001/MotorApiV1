package com.maan.eway.service.impl;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PremiaResponse {

	@JsonProperty("Response")
	private String response;
}
