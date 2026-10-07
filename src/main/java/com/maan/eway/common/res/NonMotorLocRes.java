package com.maan.eway.common.res;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class NonMotorLocRes {
	@JsonProperty("LocationId")
	private String locationId;
	@JsonProperty("LocationName")
	private String locationName;
	@JsonProperty("CoversRequiredYn")
	private String coversRequiredYn;
	@JsonProperty("Address")
	private String address;
	@JsonProperty("BuildingOwnerYn")
	private String buildingOwnerYn;
	@JsonProperty("EndtLocationYn")
	private String endtLocationYn;
	// Section Based
	@JsonProperty("SectionList")
	private List<NonMotorSectionRes> sectionList;
}
