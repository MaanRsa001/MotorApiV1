package com.maan.eway.service.impl;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TiraFrameReqCall {

	@JsonProperty("QuoteNo")
	private String quoteNo ;
}
