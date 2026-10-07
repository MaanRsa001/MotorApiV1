package com.maan.eway.common.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DriverSaveRes {

	@JsonProperty("Response")
	private String response ;
	
	
}
