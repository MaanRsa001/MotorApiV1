package com.maan.eway.common.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SectionSaveReq {
	
	@JsonProperty("SectionId")
	private String sectionId;
	
	@JsonProperty("RequestReferenceNo")
	private String requestRefNo;
	
	@JsonProperty("ProductId")
	private String productId;
	
	
	@JsonProperty("LocationId")
	private String locationId;
	
	
	

}
