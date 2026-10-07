package com.maan.eway.common.req;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ProductEmployeeSaveReq {

	
	@JsonProperty("RiskId")
    private String     riskId ;
	
	@JsonProperty("LocationId")
    private String     locationId ;
	
	@JsonProperty("LocationName")
    private String     locationName ;
	
	@JsonProperty("EmployeeId")
    private String    employeeId ;
	
	@JsonProperty("EmployeeName")
    private String    employeeName  ;
	
	@JsonProperty("NationalityId")
    private String    nationalityId;
	
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
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("DateOfBirth")
    private Date dateOfBirth;
	
	@JsonProperty("DateOfJoiningMonth")
    private String     dateOfJoiningMonth;

	@JsonProperty("HighestQualificationHeld")
    private String   highestQualificationHeld;
	
	@JsonProperty("IssuingAuthority")
    private String     issuingAuthority;

}
