package com.maan.eway.document.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class TotalRepairCostInrRes {
	
	@JsonProperty("min")
	private Long min;
	
	@JsonProperty("max")
	private Long max;

}
