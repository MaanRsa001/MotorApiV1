package com.maan.eway.common.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MachinaryBreakdownSaveReq {

	@JsonProperty("SectionId")
	private String sectionId;

	@JsonProperty("MachinerySi")
	private Integer machinerySi; 
	
	@JsonProperty("Status")
	private String status;

}
