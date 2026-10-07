package com.maan.eway.common.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SlideSectionGetReq {

	@JsonProperty("RequestReferenceNo")
    private String     requestReferenceNo ;
	@JsonProperty("RiskId")
    private String    riskId   ;
	@JsonProperty("SectionId")
    private String    sectionId   ;
	
	@JsonProperty("InsuranceId")
    private String    insuranceId   ;
	
	
	
}
