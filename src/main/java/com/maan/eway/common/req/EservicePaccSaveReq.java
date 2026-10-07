package com.maan.eway.common.req;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class EservicePaccSaveReq {

	@JsonProperty("RequestReferenceNo")
    private String     requestReferenceNo ;
	@JsonProperty("RiskId")
    private String    riskId ;
	@JsonProperty("CustomerReferenceNo")
    private String     customerReferenceNo ;

    //----------------------------------------------------------------------
    // ENTITY DATA FIELDS 
    //----------------------------------------------------------------------    
	@JsonProperty("ProductId")
    private String    productId    ;
	@JsonProperty("SectionId")
    private String sectionId    ;
	@JsonProperty("InsuranceId")
    private String     companyId    ;
	@JsonProperty("BranchCode")
    private String     branchCode   ;
	
	@JsonProperty("PolicyPeriod")
    private String     policyPeriod   ;

	@JsonProperty("SumInsured")
    private String     sumInsured ;
	
	@JsonProperty("SalaryPerAnnum")
    private String     salaryPerAnnum ;
	
	@JsonProperty("BenefitCoverMonth")
    private String     benefitCoverMonth;
	
	
	@JsonProperty("CreatedBy")
    private String     createdBy    ;
	
	@JsonProperty("AcExecutiveId")
    private String    acExecutiveId ;
	@JsonProperty("ApplicationId")
    private String     applicationId ;
	@JsonProperty("BrokerCode")
    private String     brokerCode   ;
	@JsonProperty("SubUsertype")
    private String     subUserType  ;
	@JsonProperty("LoginId")
    private String     loginId      ;
	@JsonProperty("AgencyCode")
    private String     agencyCode   ;
	
	@JsonProperty("UserType")
    private String     userType;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("PolicyStartDate")
    private Date       policyStartDate ;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("PolicyEndDate")
    private Date       policyEndDate ;
	
	@JsonProperty("Currency")
    private String     currency     ;
	@JsonProperty("ExchangeRate")
    private String     exchangeRate ;
	@JsonProperty("BrokerBranchCode")
    private String     brokerBranchCode  ;
	
	@JsonProperty("Havepromocode")
    private String     havepromocode;
	
	@JsonProperty("Promocode")
    private String     promocode;
	
	@JsonProperty("OccupationType")
    private String    occupationType;
	
	@JsonProperty("CustomerName")
    private String    customerName;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("Dob")
    private Date    dob;
	
	@JsonProperty("BankCode")
    private String    bankCode;
	
}
