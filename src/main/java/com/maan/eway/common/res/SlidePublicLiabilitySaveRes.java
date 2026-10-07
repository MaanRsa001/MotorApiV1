package com.maan.eway.common.res;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SlidePublicLiabilitySaveRes {

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
	
//	@JsonProperty("AooSumInsured")
//    private String aooSumInsured ;
	
//	@JsonProperty("AggSumInsured")
//    private String aggSumInsured ;
	
	@JsonProperty("ProductTurnoverSi")
    private String productTurnoverSi    ;
	
	@JsonProperty("Category")
    private String category;
	
//	@JsonProperty("AnyAccidentSi")
//	private String anyAccidentSi;
	
//	@JsonProperty("AnyAcidentSiLc")
//	private String anyAccidentSiLc;
	
	@JsonProperty("InsurancePeriodSi")
	private String insurancePeriodSi;
	
//	@JsonProperty("InsurancePeriodSiLc")
//	private String insurancePeriodSiLc;
	
	@JsonProperty("SumInsured")
	private String sumInsured;
}
