package com.maan.eway.common.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class OccupationResClass {

	
	@JsonProperty("OccupationType")
    private String    occupationType;
	
	@JsonProperty("SumInsuredTotal")
    private String    sumInsuredTotal;

	@JsonProperty("Count")
    private String    count;
	
}
