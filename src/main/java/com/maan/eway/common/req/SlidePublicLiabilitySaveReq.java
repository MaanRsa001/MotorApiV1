package com.maan.eway.common.req;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SlidePublicLiabilitySaveReq {

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
	
	@JsonProperty("LiabilitySi")
    private String liabilitySi    ;
	
	@JsonProperty("ProductTurnoverSi")
    private String productTurnoverSi    ;
	
	@JsonProperty("AooSumInsured")
    private String aooSumInsured ;
	
	@JsonProperty("AggSumInsured")
    private String aggSumInsured ;

	@JsonProperty("Category")
    private String category;
	
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
	private BigDecimal endtCount;
	@JsonProperty("EndtStatus") // EndtStatus
	private String endtStatus;
	@JsonProperty("IsFinanceEndt") // IsFinanceEndt
	private String isFinaceYn;
	@JsonProperty("EndtCategoryDesc") // EndtCategoryDesc
	private String endtCategDesc;
	@JsonProperty("EndorsementType") // EndorsementType
	private Integer endorsementType;

	@JsonProperty("EndorsementTypeDesc") // EndorsementTypeDesc
	private String endorsementTypeDesc;
	
	@JsonProperty("Status")
	private String status;
	
	@JsonProperty("AnyAccidentSi")
	private String anyAccidentSi;
	
	@JsonProperty("AnyAcidentSiLc")
	private String anyAccidentSiLc;
	
//	@JsonProperty("InsurancePeriodSi")
//	private String insurancePeriodSi;
	
	@JsonProperty("InsurancePeriodSiLc")
	private String insurancePeriodSiLc;
	
	@JsonProperty("SumInsured")
	private BigDecimal sumInsured;

}
