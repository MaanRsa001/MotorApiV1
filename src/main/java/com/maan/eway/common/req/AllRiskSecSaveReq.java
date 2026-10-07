package com.maan.eway.common.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AllRiskSecSaveReq {
	
	@JsonProperty("SectionId")
	private String sectionId;

	@JsonProperty("Status")
	private String status;

	@JsonProperty("AllriskSumInsured")
	private String allriskSuminsured;

	@JsonProperty("MiningPlantSi")
	private String miningPlantSi;

	@JsonProperty("NonminingPlantSi")
	private String nonminingPlantSi;

	@JsonProperty("GensetsSi")
	private String gensetsSi;

	@JsonProperty("EquipmentSi")
	private String equipmentSi;
	
//	@JsonProperty("RiskId")
//	private String riskId;

}
