package com.maan.eway.common.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ElectronicEquipmentSaveReq {
	
	@JsonProperty("SectionId")
	private String sectionId;

	@JsonProperty("ElecEquipSuminsured")
	private int elecEquipSuminsured;

	@JsonProperty("Status")
	private String status;
	
	@JsonProperty("RiskId")
	private String riskid;
	
	
	
	
}
