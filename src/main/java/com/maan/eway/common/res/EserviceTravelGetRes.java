package com.maan.eway.common.res;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.eway.common.req.TravelGroupGetRes;
import com.maan.eway.common.req.TravelGroupInsertReq;

import lombok.Data;

@Data
public class EserviceTravelGetRes {

		@JsonProperty("CustomerReferenceNo")
	    private String   customerReferenceNo ;
	    
	    @JsonProperty("RequestReferenceNo")
	    private String   requestReferenceNo ;
	    @JsonProperty("BrokerBranchCode")
	    private String     brokerBranchCode ;
	    @JsonProperty("TravelId")
	    private String    travelId     ;
		@JsonProperty("InsuranceId")
	    private String     companyId    ;
		@JsonProperty("BranchCode")
	    private String     branchCode   ;
		@JsonProperty("ProductId")
	    private String     productId    ;
		@JsonProperty("ProductName")
	    private String     productName    ;
		
		
		@JsonProperty("SectionId")
	    private String     sectionId    ;
		@JsonProperty("SectionName")
	    private String     sectionName    ;
		@JsonProperty("CompanyName")
	    private String    companyName    ;

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
		
		@JsonProperty("PolicyNo")
	    private String    policyNo;
		
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
		
		@JsonFormat(pattern="dd/MM/yyyy")
		@JsonProperty("EntryDate")
	    private Date       entryDate ;
		
		@JsonFormat(pattern="dd/MM/yyyy")
		@JsonProperty("UpdatedDate")
	    private Date       updatedDate ;
		
		
		@JsonProperty("UpdatedBy")
	    private String updatedBy;
		
		@JsonProperty("QuoteNo")
	    private String quoteNo;
		
		@JsonProperty("CustomerId")
	    private String customerId;
		
		@JsonProperty("CommissionType")
		private String commissionType;
		
		@JsonProperty("SourceCountryDesc")
		private String sourceCountryDesc;
		
		@JsonProperty("DestinationCountryDesc")
		private String desctinationCountryDesc;

		@JsonProperty("BankCode")
	    private String    bankCode;
		
		@JsonProperty("GroupDetails")
		private List<TravelGroupGetRes> groupDetails;
		
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
	    @JsonProperty("TiraCoverNoteNo")
	    private String     tiraCoverNoteNo ;
	    
		@JsonProperty("CustomerName")
	    private String     customerName;
		
		@JsonProperty("PlanTypeDesc")
	    private String    planTypeDesc;
		
		@JsonProperty("RatingRelationId")
		private String ratingRelationId;
		
		@JsonProperty("RatingRelationDesc")
		private String ratingRelationDesc;
}
