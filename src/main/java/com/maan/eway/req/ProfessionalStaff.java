package com.maan.eway.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ProfessionalStaff {
	
	

	@JsonProperty("SectionId")
	private String sectionId;

//	@JsonProperty("ProfessionalType")
//	private String professionaltype;

	@JsonProperty("EmployeeCount")
	private String employeecount;

}
