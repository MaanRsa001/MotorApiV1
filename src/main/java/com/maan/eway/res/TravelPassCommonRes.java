package com.maan.eway.res;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TravelPassCommonRes {

	@JsonProperty("QuoteNo")
    private String     quoteNo ;
    
   
    @JsonProperty("RequestReferenceNo")
	 private String     requestReferenceNo ;
   
    @JsonProperty("CustomerReferenceNo")
    private String     customerReferenceNo ;
	@JsonProperty("InsuranceId")
    private String     companyId    ;
	@JsonProperty("BranchCode")
    private String     branchCode   ;
	@JsonProperty("ProductId")
    private Integer    productId    ;
	@JsonProperty("SectionId")
    private Integer    sectionId    ;
	
	@JsonProperty("ActualPremiumLc")
	private String actualPremiumLc;
	
	@JsonProperty("AcctualPremiumFc")
	private String actualPremiumFc ;
	
	@JsonProperty("OverallPremiumLc")
	private String overallPremiumLc ;
	
	@JsonProperty("OverallPremiumFc")
	private String    overallPremiumFc ;
	
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
//	@JsonProperty("City")
//    private String     city         ;
	@JsonProperty("CompanyName")
    private String     companyName  ;
	@JsonProperty("ProductName")
    private String     productName  ;
	@JsonProperty("SectionName")
    private String     sectionName  ;
	@JsonProperty("Currency")
    private String     currency     ;
	@JsonProperty("ExchangeRate")
    private Double     exchangeRate ;
	@JsonProperty("TravelCoverDesc")
    private String     travelCoverDesc ;
	@JsonProperty("SourceCountry")
    private String     sourceCountry ;
	@JsonProperty("SourceCountryDesc")
    private String     sourceCountryDesc ;
	@JsonProperty("DestinationCountry")
    private String     destinationCountry ;
	@JsonProperty("DestinationCountryDesc")
    private String     destinationCountryDesc ;
	@JsonProperty("SportsCoverYn")
    private String     sportsCoverYn ;
	@JsonProperty("TerrorismCoverYn")
    private String     terrorismCoverYn ;
	@JsonProperty("PlanTypeId")
    private Integer    planTypeId   ;
	@JsonProperty("PlanTypeDesc")
    private String     planTypeDesc ;
	@JsonFormat(pattern="dd/MM/yyyy")
	@JsonProperty("TravelStartDate")
    private Date       travelStartDate ;
	@JsonFormat(pattern="dd/MM/yyyy")
	@JsonProperty("TravelEnddDate")
    private Date       travelEndDate ;
	@JsonProperty("TravelCoverDuration")
    private Integer    travelCoverDuration ;
	@JsonProperty("TotalPassengers")
    private Integer    totalPassengers ;
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
	@JsonProperty("HavePromoCode")
    private String     havepromocode ;
	@JsonProperty("PromoCode")
    private String     promocode    ;
	@JsonProperty("CovidCoverYn")
    private String     covidCoverYn ;
	@JsonProperty("AcExecutiveId")
    private Integer    acExecutiveId ;
	@JsonProperty("ApplicationId")
    private String     applicationId ;
	@JsonProperty("BrokerCode")
    private String     brokerCode   ;
	@JsonProperty("SubUserType")
    private String     subUserType  ;
	@JsonProperty("CustomerId")
    private String     customerId   ;
	@JsonProperty("AdminLoginId")
    private String     adminLoginId ;
	@JsonProperty("AdminRemarks")
    private String     adminRemarks ;
	@JsonProperty("RejectReason")
    private String     rejectReason ;
	@JsonProperty("ReferalRemarks")
    private String     referalRemarks ;
	@JsonProperty("BdmCode")
    private String     bdmCode      ;
	@JsonProperty("SourceType")
    private String     sourceType   ;
	@JsonProperty("CustomerCode")
    private String     customerCode ;
	@JsonProperty("BrokerBranchCode")
    private String     brokerBranchCode ;
	@JsonProperty("BrokerBranchName")
    private String     brokerBranchName ;
	@JsonProperty("CommissionType")
    private String     commissionType ;
	@JsonProperty("CommissionTypeDesc")
    private String     commissionTypeDesc ;
//	@JsonProperty("StateCode")
//    private String     stateCode;
//	@JsonProperty("StateName")
//    private String     stateName     ;
	
	@JsonProperty("TravelPassengersList")
	private List<TravelIndividualPassRes> travelPassengersList ; 
	

}
