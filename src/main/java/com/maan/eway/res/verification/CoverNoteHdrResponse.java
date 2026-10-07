package com.maan.eway.res.verification;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CoverNoteHdrResponse {

	@JsonProperty("ResponseId")
	private String responseId;
	@JsonProperty("RequestId")
	private String requestId;
	@JsonProperty("ResponseStatusCode")
	private String responseStatusCode;
	@JsonProperty("ResponseStatusDesc")
	private String responseStatusDesc;
	
}
