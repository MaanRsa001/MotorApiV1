package com.maan.eway.common.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class EmpLiabilitySecSaveReq {
	
	@JsonProperty("SectionId")
	private String sectionId;

	@JsonProperty("EmpLiabilitySi")
	private String empLiabilitySi;

	@JsonProperty("LiabilityOccupationId")
	private String liabilityOccupationId;

	@JsonProperty("TotalNoOfEmployees")
	private String totalNoOfEmployees;

	@JsonProperty("OtherOccupation")
	private String otherOccupation;
	
//	@JsonProperty("RiskId")
//	private String riskId;

}
