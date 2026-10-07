package com.maan.eway.req;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Principal {

	@JsonProperty("SectionId")
	private String sectionId;

//	@JsonProperty("ProfessionalType")
//	private String professionaltype;

	@JsonProperty("EmployeeCount")
	private String employeecount;

	@JsonProperty("IndemnityType")
	private String indemnitytype;

	@JsonProperty("IndemnitySi")
	private String IndemnitySi;

	@JsonProperty("GrossIncome")
	private String grossincome;

}
