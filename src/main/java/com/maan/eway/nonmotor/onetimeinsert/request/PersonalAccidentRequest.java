package com.maan.eway.nonmotor.onetimeinsert.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PersonalAccidentRequest {

	@JsonProperty("Dob")
	private String dob;
	
	@JsonProperty("Height")
	private String height;
	
	@JsonProperty("OccupationId")
	private String occupationId;
	
	@JsonProperty("PersonName")
	private String personName;
	
	@JsonProperty("NationalityId")
	private String nationalityId;
	
	@JsonProperty("Salary")
	private String salary;
	
	@JsonProperty("Weight")
	private String weight;
	
	@JsonProperty("RiskId")
	private String riskId;
	
	@JsonProperty("LocationName")
	private String locationName;
	
	@JsonProperty("SerialNo")
	private String serialNo;
	
	@JsonProperty("SectionId")
	private String sectionId;
	
	@JsonProperty("RegionCode")
	private String regionCode;
	
	@JsonProperty("DistrictCode")
	private String districtCode;
	
	
}
