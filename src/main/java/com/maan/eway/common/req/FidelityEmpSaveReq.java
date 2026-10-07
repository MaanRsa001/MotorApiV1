package com.maan.eway.common.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class FidelityEmpSaveReq {
	
	@JsonProperty("LiabilityOccupationId")
	private String liabilityOccupationId;

	@JsonProperty("FidEmpCount")
	private Integer fidEmpCount; 

	@JsonProperty("FidEmpSi")
	private Integer fidEmpSi; 

	@JsonProperty("OtherOccupation")
	private String otherOccupation;

	@JsonProperty("Status")
	private String status;

	@JsonProperty("SectionId")
	private String sectionId;

}
