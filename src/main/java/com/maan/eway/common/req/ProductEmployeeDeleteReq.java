package com.maan.eway.common.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ProductEmployeeDeleteReq {

	@JsonProperty("QuoteNo")
	 private String     quoteNo ;
	
	 @JsonProperty("EmployeeId")
	 private String    employeeId ;
	 
	 @JsonProperty("SectionId")
	 private String    sectionId ;
}
