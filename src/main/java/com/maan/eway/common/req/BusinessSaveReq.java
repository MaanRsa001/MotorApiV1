package com.maan.eway.common.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BusinessSaveReq {

	@JsonProperty("SectionId")
	private String sectionId;

	@JsonProperty("GrossProfitSi")
	private Integer grossProfitSi;

	@JsonProperty("IndemnityPeriodSi")
	private Integer indemnityPeriodSi;

	@JsonProperty("Status")
	private String status;

}
