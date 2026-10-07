package com.maan.eway.nonmotor.onetimeinsert.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class EndorsementRequest {
	
	@JsonProperty("EndorsementDate")
	private String endorsementDate;
	
	@JsonProperty("EndorsementEffectiveDate")
	private String endorsementEffectiveDate;
	
	@JsonProperty("EndorsementRemarks")
	private String endorsementRemarks;
	
	@JsonProperty("EndorsementTypeDesc")
	private String endorsementTypeDesc;
	
	@JsonProperty("EndtCategoryDesc")
	private String endtCategoryDesc;
	
	@JsonProperty("EndtCount")
	private String endtCount;
	
	@JsonProperty("EndtPrevPolicyNo")
	private String endtPrevPolicyNo;
	
	@JsonProperty("EndtPrevQuoteNo")
	private String endtPrevQuoteNo;
	
	@JsonProperty("EndtStatus")
	private String endtStatus;
	
	@JsonProperty("IsFinanceEndt")
	private String isFinanceEndt;
	


}


