package com.maan.eway.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ExternalTokenReq {

	
	@JsonProperty("username")
	private String username ;
	
	@JsonProperty("password")
	private String password ;
}
