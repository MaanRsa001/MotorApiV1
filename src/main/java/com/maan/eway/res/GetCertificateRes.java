package com.maan.eway.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetCertificateRes {

	@JsonProperty("downloadURL")
	private String downloadURL;

	@JsonProperty("ErrorMessage")
	private String errorMessage;
	
	@JsonProperty("Message")
	private String massage;
	
	@JsonProperty("Success")
	private Boolean success;
}
