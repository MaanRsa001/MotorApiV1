package com.maan.eway.common.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PremiaTiraRes {

	@JsonProperty("TiraCode")
	private String tiraCode;
	
	@JsonProperty("TiraDesc")
	private String tiraDesc;
	
	@JsonProperty("SalePoint")
	private String salePoint;
	
	@JsonProperty("SalePointDesc")
	private String salePointDesc;
	
	@JsonProperty("MappingType")
	private String mappingType;
	
	
}
