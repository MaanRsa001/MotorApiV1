package com.maan.eway.common.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class OccupationReqClass {

	
	@JsonProperty("OccupationType")
    private String    occupationType;
	
	@JsonProperty("SumInsuredTotal")
    private String    sumInsuredTotal;

	@JsonProperty("Count")
    private String    count;
	
}
