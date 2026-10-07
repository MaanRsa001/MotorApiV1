package com.maan.eway.common.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ContentSecSaveReq {
	
	@JsonProperty("SectionId")
	private String sectionId;

	@JsonProperty("Status")
	private String status;

	@JsonProperty("ContentSuminsured")
	private String contentSuminsured;

	@JsonProperty("JewellerySi")
	private String jewellerySi;

	@JsonProperty("PaitingsSi")
	private String paitingsSi;

	@JsonProperty("CarpetsSi")
	private String carpetsSi;

	@JsonProperty("EquipmentSi")
	private String equipmentSi;

	@JsonProperty("AllriskSumInsured")
	private String allriskSuminsured;
	
//	@JsonProperty("RiskId")
//	private String riskId;
}
