package com.maan.eway.common.res;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GoodInTransitRes {
	
	@JsonProperty("RequestReferenceNo")
    private String     requestReferenceNo ;
	
	@JsonProperty("RiskId")
    private String    riskId   ;
	
	@JsonProperty("ProductId")
    private String    productId    ;
	
	@JsonProperty("SectionId")
    private String sectionId    ;
	
	@JsonProperty("InsuranceId")
    private String insuranceId    ;
	
	@JsonProperty("CreatedBy")
    private String createdBy    ;
	
	@JsonProperty("TransportedBy")
	private String transportedBy; 
	

	@JsonProperty("ModeOfTransport")
	private String modeOfTransport; 

	@JsonProperty("GeographicalCoverage")
	private String geographicalCoverage; 

	@JsonProperty("SingleRoadSiLc")
	private String singleRoadSiLc; 

	@JsonProperty("SingleRoadSiFc")
	private String singleRoadSiFc; 

	@JsonProperty("EstAnnualCarriesSiLc")
	private String estAnnualCarriesSiLc;
	
	@JsonProperty("EstAnnualCarriesSiFc")
	private String estAnnualCarriesSiFc;
	
	@JsonProperty("EndorsementDate") // EndorsementDate
	@JsonFormat(pattern = "dd/MM/yyyy")
	
	private Date endorsementDate;
	@JsonProperty("EndorsementRemarks") // EndorsementRemarks
	private String endorsementRemarks;
	
	@JsonProperty("EndorsementEffectiveDate") // EndorsementEffectiveDate
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date endorsementEffdate;
	
	@JsonProperty("OrginalPolicyNo") // OrginalPolicyNo
	private String originalPolicyNo;
	
	@JsonProperty("EndtPrevPolicyNo") // EndtPrevPolicyNo
	private String endtPrevPolicyNo;
	
	@JsonProperty("EndtPrevQuoteNo") // EndtPrevQuoteNo
	private String endtPrevQuoteNo;
	
	@JsonProperty("EndtCount") // EndtCount
	private String endtCount;
	
	@JsonProperty("EndtStatus") // EndtStatus
	private String endtStatus;
	
	@JsonProperty("IsFinanceEndt") // IsFinanceEndt
	private String isFinaceYn;
	
	@JsonProperty("EndtCategoryDesc") // EndtCategoryDesc
	private String endtCategDesc;
	
	@JsonProperty("EndorsementType") // EndorsementType
	private String endorsementType;

	@JsonProperty("EndorsementTypeDesc") // EndorsementTypeDesc
	private String endorsementTypeDesc;
	
   @JsonProperty("Status")
	private String status;

}
