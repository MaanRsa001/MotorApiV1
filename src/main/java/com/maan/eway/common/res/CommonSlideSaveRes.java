package com.maan.eway.common.res;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CommonSlideSaveRes {


	@JsonProperty("RequestReferenceNo")
	private String requestReferenceNo;
	
	@JsonProperty("CustomerReferenceNo")
	private String customerReferenceNo;
	
	@JsonProperty("RiskId")
	private String riskId;
	
	
	@JsonProperty("Response")
	private String response;
	
	@JsonProperty("ProductId")
	private String productId;

	@JsonProperty("SectionIds")
	private List<String> sectionIds;


	
	@JsonProperty("InsuranceId")
	private String companyId;

	@JsonProperty("CreatedBy")
	private String createdBy;

}
