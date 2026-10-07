package com.maan.eway.common.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BuildingSecSaveReq {
	
	@JsonProperty("SectionId")
	private String sectionId;

	@JsonProperty("BuildingSumInsured")
	private String buildingSumInsured;

	@JsonProperty("WallType")
	private String wallType;

	@JsonProperty("RoofType")
	private String roofType;

	@JsonProperty("BuildingUsageId")
	private String buildingUsageId;

	@JsonProperty("BuildingOwnerYn")
	private String buildingOwnerYn;

	@JsonProperty("BuildingBuildYear")
	private String buildingBuildYear;

	@JsonProperty("Status")
	private String status;

	@JsonProperty("WaterTankSi")
	private String waterTankSi;

	@JsonProperty("ArchitectsSi")
	private String architectsSi;

	@JsonProperty("LossOfRentSi")
	private String lossOfRentSi;

	@JsonProperty("TypeOfProperty")
	private String typeOfProperty;

	@JsonProperty("GroundUndergroundSi")
	private String groundUndergroundSi;
	
	@JsonProperty ("RiskId")
	private String riskId ;
	
	@JsonProperty("LocationName")
    private String     locationName;
	
	@JsonProperty("BuildingAddress")
    private String   buildingAddress;
	
	//Fire And Allied Perills
	
	@JsonProperty("IndustryType")
    private String   industryType;
	
	@JsonProperty("OccupationId")
    private String   occupationId;
	
	@JsonProperty("CoveringDetails")
    private String  CoveringDetails ;
	
	
	@JsonProperty("DescriptionOfRisk")
    private String  descriptionOfRisk ;
	

	@JsonProperty("RegionName")
    private String  regionName ;
	
	@JsonProperty("DistrictName")
    private String  districtName ;
	
	@JsonProperty("FirstLossPayee")
    private String  firstLossPayee ;
	

}
