package com.maan.eway.common.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class VehicleExchangeRateScenario {

	@JsonProperty("OldSumInsured")
	private String oldSumInsured ;
	
	@JsonProperty("OldExchangeRate")
	private String oldExchangeRate;
	
	@JsonProperty("OldAcccessoriesSumInsured")
    private String     oldAcccessoriesSumInsured ;
	
	@JsonProperty("OldWindScreenSumInsured")
    private String     oldWindScreenSumInsured ;
	
	@JsonProperty("OldTppdIncreaeLimit")
    private String     oldTppdIncreaeLimit ;
	
}
