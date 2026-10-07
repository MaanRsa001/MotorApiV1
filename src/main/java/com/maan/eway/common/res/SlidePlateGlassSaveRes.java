package com.maan.eway.common.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SlidePlateGlassSaveRes {


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
	
	@JsonProperty("PlateGlassSi")
    private String plateGlassSi    ;
	
	@JsonProperty("PlateGlassType")
    private String plateGlassType    ;
}
