package com.maan.eway.common.req;

import java.math.BigDecimal;

import org.apache.poi.hpsf.Decimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BondRes {
	
	@JsonProperty("RequestReferenceNo")
    private String     requestReferenceNo ;
	
    @JsonProperty("InsuranceId")
    private String insuranceId;
	
	@JsonProperty("ProductId")
    private String productId;
	
	@JsonProperty("CreatedBy")
	private String createdBy;
	
    @JsonProperty("RiskId")
	private Integer riskId;
	
	@JsonProperty("BondType")
	private String bondType;
	
	@JsonProperty("BondYear")
	private String bondYear;
		
	@JsonProperty("BondSuminsured")
	private BigDecimal bondSumInsured;
}
