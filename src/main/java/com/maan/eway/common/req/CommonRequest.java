package com.maan.eway.common.req;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class CommonRequest {

	@JsonProperty("RequestReferenceNo")
	private String requestReferenceNo;
	@JsonProperty("BrokerCode")
	private String BrokerCode;
	
	@JsonProperty("UserType")
	private String UserType;
	@JsonProperty("TiraCoverNoteNo")
    private String     tiraCoverNoteNo ;
	@JsonProperty("AcexecutiveId")
    private String    acExecutiveId ;
	
	@JsonProperty("CommissionType")
    private String    commissionType;
	
	@JsonProperty("BankCode")
    private String    bankCode;
	@JsonProperty("ProductId")
	private String productId;

	@JsonProperty("InsuranceId")
	private String insuranceId;

	@JsonProperty("CreatedBy")
	private String createdBy;	
	
	@JsonProperty("CustomerReferenceNo")
    private String     customerReferenceNo ;

	@JsonProperty("Currency")
    private String     currency ;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("PolicyStartDate")
    private Date       policyStartDate ;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("PolicyEndDate")
    private Date       policyEndDate ;
	
	@JsonProperty("CustomerCode")
    private String     customerCode;
	
	@JsonProperty("SubUsertype")
    private String     subUserType  ;
	
	@JsonProperty("BrokerBranchCode")
    private String     BrokerbranchCode   ;

	
	@JsonProperty("BranchCode")
    private String     branchCode   ;
	
	@JsonProperty("CustomerName")
    private String     customerName;
	
	@JsonProperty("ExchangeRate")
    private String     exchangeRate ;
	
	
	@JsonProperty("Havepromocode")
    private String     havepromocode;
	
	@JsonProperty("BuildingOwnerYn")
	private String     buildingOwnerYn;
	
	
	@JsonProperty("ApplicationId")
	private String   ApplicationId;
	
	
	@JsonProperty("Usertype")
	private String   subUsertype;
	
	
	@JsonProperty("SourceTypeId")
	private String sourceTypeId;
	
	@JsonProperty("SourceType")
	private String sourceType;

	@JsonProperty("ProductType")
	private String   productType;
	
	@JsonProperty("AgencyCode")
	private String   agencyCode;
	
	@JsonProperty("Promocode")
	private String   promoCode;
	
	@JsonProperty("EndorsementType")
	private String   endorsementType;

	@JsonProperty("EndorsementTypeDesc")
	private String   EndorsementTypeDesc;
	
	  @JsonProperty("EndorsementDate") 
	  private String EndorsementDate;
	 
	  
	  @JsonFormat(pattern = "dd/MM/yyyy")
	  private Date endorsementDate;
	  
	  @JsonProperty("EndorsementRemarks") 
	  private String endorsementRemarks;
	  
	  @JsonProperty("EndorsementEffectiveDate") //
	  private String EndorsementEffectiveDate;
	  
	  @JsonFormat(pattern = "dd/MM/yyyy")
	  private Date endorsementEffdate;
	  
	  @JsonProperty("OrginalPolicyNo") // OrginalPolicyNo 
	  private String originalPolicyNo;
	  
	  @JsonProperty("EndtPrevPolicyNo") // EndtPrevPolicyNo
	  private String endtPrevPolicyNo;
	  
	  @JsonProperty("EndtPrevQuoteNo") // EndtPrevQuoteNo 
	  private String endtPrevQuoteNo;
	  
	  @JsonProperty("EndtCount") // EndtCount
	  private BigDecimal endtCount;
	  
	  @JsonProperty("EndtStatus") // EndtStatus 
	  private String endtStatus;
	  
	  @JsonProperty("IsFinanceEndt") // IsFinanceEndt
	  private String isFinaceYn;
	  
	  @JsonProperty("EndtCategoryDesc") // EndtCategoryDesc 
	  private String endtCategDesc;
	  

	  @JsonProperty("BdmCode") // EndtCategoryDesc 
	  private String BdmCode;
	  
	  @JsonProperty("Status")
	     private String    status;
	
	  
	  @JsonProperty("FireDetails") 
	  private List<FireReqData> firedel;
	  
	 
	  
}
