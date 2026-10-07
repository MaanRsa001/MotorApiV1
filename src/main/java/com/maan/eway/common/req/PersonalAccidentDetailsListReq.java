package com.maan.eway.common.req;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PersonalAccidentDetailsListReq {

	
	@JsonProperty("PersonName")
	private String personName;
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("Dob")
	private Date dob;
	@JsonProperty("Height")
	private String height;
	@JsonProperty("Weight")
	private String weight;
	@JsonProperty("OccupationId")
	private String occupationId;
	@JsonProperty("Salary")
	private String salary;
	@JsonProperty("SerialNo")
	private String serialNo;
	@JsonProperty("RiskId")
	private String riskId;
	@JsonProperty("LocationId")
	private String LocationId;
	@JsonProperty("LocationName")
	private String locationName;
	@JsonProperty("NationalityId")
	private String nationalityId;

}
