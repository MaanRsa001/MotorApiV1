package com.maan.eway.common.res;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PersonalDetailsGetallRes {

	
	@JsonProperty("RiskId")
	private String riskId;

	@JsonProperty("PersonId")
	private String personId;
	@JsonProperty("PersonName")
	private String personName;
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("Dob")
	private Date dob;
	@JsonProperty("Age")
	private String age;
	@JsonProperty("Height")
	private String height;
	@JsonProperty("Weight")
	private String weight;
	@JsonProperty("Description")
	private String description;


	@JsonProperty("OccupationId")
	private String occupationId;

	@JsonProperty("CategoryId")
	private String categoryId;

	@JsonProperty("OccupationDesc")
	private String occupationDesc;
	@JsonProperty("Salary")
	private String salary;

	@JsonProperty("NationalityId")
	private String nationalityId;

}
