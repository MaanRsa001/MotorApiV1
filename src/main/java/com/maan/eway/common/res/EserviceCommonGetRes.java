package com.maan.eway.common.res;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class EserviceCommonGetRes {

	@JsonProperty("RequestReferenceNo")
    private String     requestReferenceNo ;
	@JsonProperty("RiskId")
    private String    riskId   ;
	
	@JsonProperty("CustomerReferenceNo")
    private String     customerReferenceNo ;
	@JsonProperty("ProductId")
    private String    productId    ;
	@JsonProperty("InsuranceId")
    private String     companyId    ;
	@JsonProperty("BranchCode")
    private String     branchCode   ;
	@JsonProperty("SalaryPerAnnum")
    private String     salaryPerAnnum ;
	
	@JsonProperty("BenefitCoverMonth")
    private String     benefitCoverMonth;
	@JsonProperty("SectionId")
    private String sectionId    ;
	@JsonProperty("AdminRemarks")
    private String     adminRemarks;
	
	@JsonProperty("ReferralRemarks")
    private String     referralRemarks;
	
	@JsonProperty("SectionDesc")
    private String sectionDesc   ;
	@JsonProperty("SumInsured")
    private String     sumInsured ;
	
	@JsonProperty("PolicyPeriod")
    private String     policyPeriod   ;
	
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

	@JsonProperty("OccupationTypeDesc")
    private String    occupationTypeDesc;

	@JsonProperty("CategoryId")
    private String    categoryId;

	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("Dob")
    private Date    dob;
	
	@JsonProperty("JobJoiningMonth")
    private String    jobJoiningMonth;
	
	@JsonProperty("BetweenDiscontinued")
    private String    betweenDiscontinued;
	
	@JsonProperty("EthicalWorkInvolved")
    private String    ethicalWorkInvolved;
	
	@JsonProperty("Status")
    private String   status;

	@JsonFormat(pattern="dd/MM/yyyy")
	@JsonProperty("UpdatedDate")
    private Date updatedDate;

	@JsonProperty("UpdatedBy")
    private String  updatedBy;

	@JsonProperty("SavedFrom")
    private String  savedFrom;

	@JsonProperty("ActualPremiumLc")
	private String actualPremiumLc;
	
	@JsonProperty("AcctualPremiumFc")
	private String actualPremiumFc ;
	
	@JsonProperty("OverallPremiumLc")
	private String overallPremiumLc ;
	
	@JsonProperty("OverallPremiumFc")
	private String    overallPremiumFc ;

	@JsonProperty("QuoteNo")
	private String quoteNo;
	  
	@JsonProperty("CustomerId")
	private String customerId;
	
	@JsonProperty("BdmCode")
	private String bdmCode;
	
	@JsonProperty("SourceTypeId")
	private String sourceTypeId;
	
	@JsonProperty("SourceType")
	private String sourceType;
	
	@JsonProperty("CustomerCode")
	private String customerCode;
	
	@JsonProperty("BankCode")
    private String    bankCode;
	

    @JsonProperty("EndorsementDate") //EndorsementDate
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date       endorsementDate ;
    @JsonProperty("EndorsementRemarks") // EndorsementRemarks
    private String     endorsementRemarks ;    
    @JsonProperty("EndorsementEffectiveDate") // EndorsementEffectiveDate
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date       endorsementEffdate ;
    @JsonProperty("OrginalPolicyNo") // OrginalPolicyNo
    private String     originalPolicyNo ;
    @JsonProperty("EndtPrevPolicyNo") // EndtPrevPolicyNo
    private String     endtPrevPolicyNo ;
    @JsonProperty("EndtPrevQuoteNo") // EndtPrevQuoteNo
    private String     endtPrevQuoteNo ;
    @JsonProperty("EndtCount")  // EndtCount
    private String endtCount ;
    @JsonProperty("EndtStatus") //EndtStatus
    private String     endtStatus ;   
    @JsonProperty("IsFinanceEndt") //IsFinanceEndt
    private String     isFinaceYn ;  
    @JsonProperty("EndtCategoryDesc") //EndtCategoryDesc
    private String     endtCategDesc ;
    

    @JsonProperty("EndorsementType") //EndorsementType
    private String    endorsementType ;

    @JsonProperty("EndorsementTypeDesc") // EndorsementTypeDesc
    private String     endorsementTypeDesc ;
    
    @JsonProperty("IndustryName")
	private String       industryName;
	
	@JsonProperty("NatureOfBusinessId")
	private String       natureOfBusinessId ;
	    
	@JsonProperty("NatureOfBusinessDesc")
	private String       natureOfBusinessDesc;
	    
	@JsonProperty("TotalNoOfEmployees")
	private String       totalNoOfEmployees;
	    
	@JsonProperty("TotalExcludedEmployees")
	private String       totalExcludedEmployees ;
	    
	@JsonProperty("TotalRejoinedEmployees")
	private String       totalRejoinedEmployees ;
	    
	@JsonProperty("AccountOutstandingEmployees")
	private String       accountOutstandingEmployees;
	    
	@JsonProperty("AccountAuditentType")
	private String       accountAuditentType ;
	    
	@JsonProperty("AuditentTypeDesc")
	private String       auditentTypeDesc ;


	@JsonProperty("TotalOutstandingAmount")
	private String       totalOutstandingAmount;
	
	@JsonProperty("TiraCoverNoteNo")
    private String     tiraCoverNoteNo ;
	
	@JsonProperty("CustomerName")
    private String     customerName;
}
