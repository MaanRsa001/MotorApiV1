package com.maan.eway.common.req;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ProductLevelReq {
	@JsonProperty("InsuranceId")
    private String    insuranceId;
	
	@JsonProperty("ProductId")
    private String  productId;
	
	
	@JsonProperty("SectionLevelReq")
	private List<SectionLevelReq> sectionLevelReq;
}
