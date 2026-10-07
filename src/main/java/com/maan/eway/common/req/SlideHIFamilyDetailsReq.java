package com.maan.eway.common.req;
import java.math.BigDecimal;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;


@Data

public class SlideHIFamilyDetailsReq {
	
	
	@JsonProperty("RiskId")
    private String     riskId ;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("DateOfBirth")
    private Date dateOfBirth;
	
	@JsonProperty("RelationType")
    private String    relationType;
	
	@JsonProperty("FirstName")
	private String firstName;
	
	@JsonProperty("LastName")
	private String lastName;
	
	
	
	@JsonProperty("NationalityId")
    private String    nationalityId;
	
	@JsonProperty("EmployeeId")
    private String    employeeId ;
	
	
	/*
	 * 
	 * @JsonProperty("Salary")
    private String salary;
    
    
	@JsonProperty("LocationId")
    private String     locationId ;
	
	@JsonProperty("LocationName")
    private String     locationName ;
	
	@JsonProperty("EmployeeId")
    private String    employeeId ;
	
	@JsonProperty("EmployeeName")
    private String    employeeName  ;
	
	
	
	@JsonProperty("OccupationId")
    private String     occupationId ;
	
	@JsonProperty("OccupationDesc")
    private String     occupationDesc ;
	
	@JsonProperty("Salary")
    private String salary;
	
	@JsonProperty("Address")
    private String address;
	
	@JsonProperty("DateOfJoiningYear")
    private String     dateOfJoiningYear ;
	
	
	
	@JsonProperty("DateOfJoiningMonth")
    private String     dateOfJoiningMonth;

	@JsonProperty("HighestQualificationHeld")
    private String   highestQualificationHeld;
	
	@JsonProperty("IssuingAuthority")
    private String     issuingAuthority;

*/
}
