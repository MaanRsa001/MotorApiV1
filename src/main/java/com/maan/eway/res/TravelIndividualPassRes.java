package com.maan.eway.res;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TravelIndividualPassRes {

	    
	    @JsonProperty("PassengerId")
	    private String     passengerId;    
	   
		@JsonProperty("GenderId")
	    private String    genderId     ;
		
		@JsonProperty("NameTitleDesc")
	    private String     nameTitleDesc ;
		@JsonProperty("NameTitleId")
	    private Integer    nameTitleId  ;
		@JsonProperty("PassengerName")
	    private String     passengerName ;
		
		@JsonProperty("PassengerFirstName")
	    private String     passengerFirstName ;
		@JsonProperty("PassengerLastName")
	    private String     passengerLastName ;
		

		@JsonProperty("ActualPremiumLc")
		private String actualPremiumLc;
		
		@JsonProperty("AcctualPremiumFc")
		private String actualPremiumFc ;
		
		@JsonProperty("OverallPremiumLc")
		private String overallPremiumLc ;
		
		@JsonProperty("OverallPremiumFc")
		private String    overallPremiumFc ;
		
		@JsonFormat(pattern = "dd/MM/yyyy")
		@JsonProperty("Dob")
	    private Date       dob          ;
		@JsonProperty("GenderDesc")
	    private String     genderDesc   ;
		@JsonProperty("Age")
	    private Integer    age          ;
		@JsonProperty("RelationId")
	    private Integer    relationId   ;
		@JsonProperty("RelationDesc")
	    private String     relationDesc ;
		@JsonProperty("Nationality")
	    private String     nationality  ;
		@JsonProperty("NationalityDesc")
	    private String     nationalityDesc  ;
		@JsonProperty("CoverType")
	    private String     coverType    ;
		@JsonProperty("PassportNo")
	    private String     passportNo   ;
		@JsonProperty("CivilId")
	    private String     civilId      ;
		@JsonProperty("TotalPremium")
	    private Double     totalPremium ;
		@JsonProperty("LoginId")
	    private String     loginId      ;
		@JsonFormat(pattern = "dd/MM/yyyy")
		@JsonProperty("EntryDate")
	    private Date       entryDate    ;
		@JsonProperty("Status")
	    private String     status       ;
		@JsonProperty("Address1")
	    private String     address1     ;
		@JsonProperty("Address2")
	    private String     address2     ;

		@JsonFormat(pattern="dd/MM/yyyy")
		@JsonProperty("EffectiveDate")
	    private Date       effectiveDate ;
		@JsonProperty("CreatedBy")
	    private String     createdBy    ;
		@JsonFormat(pattern="dd/MM/yyyy")
		@JsonProperty("UpdatedDate")
	    private Date       updatedDate  ;
		@JsonProperty("UpdatedBy")
	    private String     updatedBy    ;
		@JsonProperty("Remarks")
	    private String     remarks      ;
				

		@JsonProperty("GroupId")
	    private String   groupId;
}
