package com.maan.eway.common.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MoneySaveReq {
	
	
	@JsonProperty("SectionId")
	private String sectionId;

	@JsonProperty("MoneySafeLimit")
	private Integer moneySafeLimit; 

	@JsonProperty("MoneyOutofSafe")
	private String moneyOutofSafe;

	@JsonProperty("MoneyDirectorResidence")
	private Integer moneyDirectorResidence;

	@JsonProperty("MoneyCollector")
	private String moneyCollector;

	@JsonProperty("MoneyAnnualEstimate")
	private Integer moneyAnnualEstimate; 

	@JsonProperty("MoneyMajorLoss")
	private Integer moneyMajorLoss; 

	@JsonProperty("StrongroomSi")
	private Integer strongroomSi; 

	@JsonProperty("Status")
	private String status;

}
