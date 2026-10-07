package com.maan.eway.document.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class TotalRepairCostUsdRes {

	@JsonProperty("min")
	private Long min;
	
	@JsonProperty("max")
	private Long max;
	
	@JsonProperty("formatted")
	private String formatted;

}
