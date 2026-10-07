package com.maan.eway.common.req;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class EserviceTravelSaveReq {


    @JsonProperty("CustomerReferenceNo")
    private String   customerReferenceNo ;
    
    @JsonProperty("RequestReferenceNo")
    private String   requestReferenceNo ;
  
    @JsonProperty("TravelId")
    private String    travelId     ;
	@JsonProperty("InsuranceId")
    private String     companyId    ;
	@JsonProperty("BranchCode")
    private String     branchCode   ;
	
	@JsonProperty("BrokerBranchCode")
    private String     brokerBranchCode   ;
	@JsonProperty("ProductId")
    private String     productId    ;
	@JsonProperty("SectionId")
    private String     sectionId    ;
	
	@JsonProperty("SavedFrom")
	private String savedFrom;

	@JsonProperty("TravelCoverId")
    private String    travelCoverId ;
	@JsonProperty("SourceCountry")
    private String     sourceCountry ;
	@JsonProperty("DestinationCountry")
    private String     destinationCountry ;
	@JsonProperty("SportsCoverYn")
    private String     sportsCoverYn ;
	@JsonProperty("TerrorismCoverYn")
    private String     terrorismCoverYn ;
	@JsonProperty("PlanTypeId")
    private String    planTypeId   ;
	
	@JsonFormat(pattern="dd/MM/yyyy")
	@JsonProperty("TravelStartDate")
    private Date       travelStartDate ;
	
	@JsonFormat(pattern="dd/MM/yyyy")
	@JsonProperty("TravelEndDate")
    private Date       travelEndDate ;
	
	@JsonProperty("TravelCoverDuration")
    private String    travelCoverDuration ;
	@JsonProperty("TotalPassengers")
    private String    totalPassengers ;
	
	@JsonProperty("Age")
    private String    age          ;
	
	@JsonProperty("CreatedBy")
    private String     createdBy    ;
	@JsonProperty("Status")
    private String     status       ;
	
	@JsonProperty("Remarks")
    private String     remarks      ;
	
	@JsonProperty("HavePromoCode")
    private String     havepromocode ;
	
	@JsonProperty("PromoCode")
    private String     promocode    ;
	@JsonProperty("CovidCoverYn")
    private String     covidCoverYn ;
	
	@JsonProperty("AdminLoginId")
    private String     adminLoginId ;
	@JsonProperty("AdminRemarks")
    private String     adminRemarks ;
	@JsonProperty("RejectReason")
    private String     rejectReason ;
	
	@JsonProperty("ReferalRemarks")
    private String     referalRemarks ;
	@JsonProperty("BrokerCode")
	private String brokerCode;
	
	@JsonProperty("LoginId")
	private String loginId;
	
	@JsonProperty("AcExecutiveId")
	private String acExecutiveId;
	
	@JsonProperty("UserType")
	private String userType;
	
	@JsonProperty("SubUserType")
	private String subUserType;
	
	@JsonProperty("Currency")
	private String currency;
	
	@JsonProperty("ExchangeRate")
	private String exchangeRate;
	
	@JsonProperty("ApplicationId")
	private String applicationId;
	
	@JsonProperty("BdmCode")
	private String bdmCode;
	
	@JsonProperty("SourceTypeId")
	private String sourceTypeId;
	
	@JsonProperty("SourceType")
	private String sourceType;
	
	@JsonProperty("CustomerCode")
	private String customerCode;
	
	@JsonProperty("CommissionType")
	private String commissionType;
	
	@JsonProperty("BankCode")
    private String    bankCode;
	
	@JsonProperty("PolicyNo")
    private String    policyNo;

	@JsonProperty("CustomerName")
    private String     customerName;
	
	@JsonProperty("GroupDetails")
	private List<TravelGroupInsertReq> groupDetails;
	
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
	@JsonProperty("TiraCoverNoteNo")
    private String     tiraCoverNoteNo ;	
	
	@JsonProperty("FinalizeYn")
	private String finalizeYn;
	
	@JsonProperty("LocationId")
	private String locationId;
	
	
	@JsonProperty("MobileNumber")
	private String mobileNo;
	@JsonProperty("MobileCode")
	private String mobileCode;
	
	@JsonProperty("Occupation")
	private String occupation;
	
	@JsonProperty("Email1")
	private String email1;
	
	@JsonProperty("FirstName")
	private String firstName;
	
	@JsonProperty("LastName")
	private String lastName;
	
}
