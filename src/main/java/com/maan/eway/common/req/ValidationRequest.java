package com.maan.eway.common.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ValidationRequest {
	
	
	@JsonProperty("Type")
	private String type;
	
	
	@JsonProperty("LoginId")
	private String loginId;
	
	@JsonProperty("IsDataInclude")
	private Boolean isDataIncluded;
	
	@JsonProperty("ValidationField")
	private String validationField;
	
	@JsonProperty("SeatingCapacity")
	private String seatingCapacity;

	@JsonProperty("InsuranceId")
	private String insuranceId;

	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("BodyType")
	private String bodyType;
}
