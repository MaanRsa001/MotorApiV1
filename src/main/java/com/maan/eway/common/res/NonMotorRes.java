package com.maan.eway.common.res;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.eway.common.req.NonMotorLocationReq;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class NonMotorRes {

	
	//Common Details
	@JsonProperty("RequestReferenceNo")
    private String     requestReferenceNo  ;
	@JsonProperty("CustomerReferenceNo")
    private String     customerReferenceNo ;
	@JsonProperty("ProductId")
    private String    productId    ;
	@JsonProperty("InsuranceId")
    private String     companyId    ;
	@JsonProperty("Status")
    private String     status;
	@JsonProperty("BranchCode")
    private String     branchCode   ;
	
	@JsonProperty("Createdby")
    private String     createdBy    ;
	@JsonProperty("AcexecutiveId")
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
	@JsonProperty("BankCode")
    private String    bankCode;
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("PolicyStartDate")
    private Date       policyStartDate ;
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("PolicyEndDate")
    private Date       policyEndDate ;
	@JsonProperty("Currency")
    private String     currency     ;
	@JsonProperty("ExchangeRate")
	  private BigDecimal     exchangeRate ;
	@JsonProperty("BrokerBranchCode")
    private String     brokerBranchCode  ;
	@JsonProperty("Havepromocode")
    private String     havepromocode;
	@JsonProperty("Promocode")
    private String     promocode;
	@JsonProperty("SourceTypeId")
	private String sourceTypeId;
	@JsonProperty("SourceType")
	private String sourceType;
	@JsonProperty("CustomerCode")
    private String     customerCode;
	@JsonProperty("BdmCode")
    private String     bdmCode   ;
	@JsonProperty("CommissionType")
    private String    commissionType;
	@JsonProperty("IndustryId")
	private Integer    industryId ;
	
	

	//EndtFields
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
    private BigDecimal endtCount ;
    @JsonProperty("EndtStatus") //EndtStatus
    private String     endtStatus ;   
    @JsonProperty("IsFinanceEndt") //IsFinanceEndt
    private String     isFinaceYn ;  
    @JsonProperty("EndtCategoryDesc") //EndtCategoryDesc
    private String     endtCategDesc ;
    @JsonProperty("EndorsementType") //EndorsementType
    private Integer    endorsementType ;
    @JsonProperty("EndorsementTypeDesc") // EndorsementTypeDesc
    private String     endorsementTypeDesc ;
    @JsonProperty("PolicyNo")
    private String policyNo;
	@JsonProperty("TiraCoverNoteNo")
    private String     tiraCoverNoteNo ;
	@JsonProperty("CustomerName")
    private String     customerName;
	@JsonProperty("IndustryDesc")
	private String industryDesc;
	
	// Location Based
		@JsonProperty("LocationList")
		private List<NonMotorLocationRes> locationList;
	
}
