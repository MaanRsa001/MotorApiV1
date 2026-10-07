package com.maan.eway.common.req;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class FireReqData {
	@JsonProperty("RiskId")
	private String riskId;
	
	
	@JsonProperty("BuildingSumInsured")
	private BigDecimal buildingSumInsured;
	
	@JsonProperty("LocationName")
    private String     locationName;
	
	
	@JsonProperty("IndustryType")
    private String   industryType;
	
	@JsonProperty("IndustryTypeDesc")
    private String   industrytypedesc;
	
	@JsonProperty("OccupationId")
    private String   occupationId;
	
	@JsonProperty("OccupationDesc")
    private String   occupationDesc;
	
	@JsonProperty("CoveringDetails")
    private String  CoveringDetails ;
	
	
	@JsonProperty("DescriptionOfRisk")
    private String  descriptionOfRisk ;
	

	/*
	 * @JsonProperty("RegionName") private String regionName ;
	 * 
	 * @JsonProperty("DistrictName") private String districtName ;
	 */
	
	@JsonProperty("RegionCode")
    private String  regionCode ;
	
	@JsonProperty("DistrictCode")
    private String  districtCode ;
	
	
	
	@JsonProperty("SectionId")
	private String   sectionId;
	
	
	
	@JsonProperty("Business_Interruption")
	private String  businessInterruption;
	
}
