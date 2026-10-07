package com.maan.eway.common.res;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AccidentDamageSaveResponse {

	// Primary Key 
	@JsonProperty("RequestReferenceNo")
    private String     requestReferenceNo ;
	
	@JsonProperty("RiskId")
    private String    riskId   ;
	
	@JsonProperty("SectionId")
    private String sectionId    ;
	
	@JsonProperty("InsuranceId")
    private String insuranceId;
	
	@JsonProperty("ProductId")
    private String productId;
	
	@JsonProperty("CreatedBy")
	private String createdBy;
	
	
	// Input Fields 	
	@JsonProperty("AccDamageSi")
	private String accDamageSi;
	

}
