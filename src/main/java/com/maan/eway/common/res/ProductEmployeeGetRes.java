package com.maan.eway.common.res;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ProductEmployeeGetRes {

//	@JsonProperty("RequestReferenceNo")
//    private String     requestReferenceNo ;
//	
	@JsonProperty("RiskId")
    private Integer    riskId   ;
	
	@JsonProperty("LocationId")
    private Integer    locationId   ;
	@JsonProperty("LocationName")
	private String locationName;
//	
//	@JsonProperty("QuoteNo")
//    private String  quoteNo;
//	
//	@JsonProperty("Createdby")
//    private String     createdBy    ;
//	
//	@JsonProperty("InsuranceId")
//    private String     insuranceId;
//	
//	@JsonFormat(pattern = "dd/MM/yyyy")
//	@JsonProperty("EntryDate")
//    private Date entryDate;
//	
	@JsonProperty("EmployeeId")
    private Integer    employeeId ;
	
	@JsonProperty("EmployeeName")
    private String    employeeName  ;
	
	@JsonProperty("OccupationId")
    private String     occupationId ;
	
	@JsonProperty("OccupationDesc")
    private String     occupationDesc ;
	
	@JsonProperty("Salary")
    private String salary;
	
	@JsonProperty("ProductId")
    private String    productId   ;
	
	@JsonProperty("ProductDesc")
    private String    productDesc ;
	
	@JsonProperty("Address")
    private String address;
	
	@JsonProperty("NationalityId")
    private String    nationalityId   ;
	
	@JsonProperty("DateOfJoiningYear")
    private String     dateOfJoiningYear ;

	@JsonProperty("DateOfJoiningMonth")
    private String     dateOfJoiningMonth;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("DateOfBirth")
    private Date dateOfBirth;
	
	 @JsonProperty("SectionId")
	 private String     sectionId ;
//	 
//		@JsonFormat(pattern = "dd/MM/yyyy")
//		@JsonProperty("PolicyStartDate")
//	    private Date policyStartDate;
//		
//		@JsonFormat(pattern = "dd/MM/yyyy")
//		@JsonProperty("PolicyEndDate")
//	    private Date policyEndDate;
//		
		@JsonProperty("Rate")
	    private String    rate ;
		
		@JsonProperty("PremiumFc")
	    private String premiumFc;
		
		@JsonProperty("PremiumLc")
	    private String    premiumLc   ;
		
		@JsonProperty("HighestQualificationHeld")
	    private String   highestQualificationHeld;
		
		@JsonProperty("IssuingAuthority")
	    private String     issuingAuthority;

		
//		@JsonProperty("CurrencyCode")
//	    private String     currencyCode ;
//
//		@JsonProperty("ExchangeRate")
//	    private String     exchangeRate;
	
}
