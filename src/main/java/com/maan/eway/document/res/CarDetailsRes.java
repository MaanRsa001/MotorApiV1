package com.maan.eway.document.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class CarDetailsRes {

	@JsonProperty("make")
	private String make;
	
	@JsonProperty("model")
	private String model;
	
	@JsonProperty("color")
	private String color;

}
