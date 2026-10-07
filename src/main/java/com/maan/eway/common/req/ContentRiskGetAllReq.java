package com.maan.eway.common.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ContentRiskGetAllReq {

	 @JsonProperty("RequestReferenceNO")
	 private String     requestreferenceno ;
	 @JsonProperty("QuoteNo")
	 private String     quoteNo ;
	 
	 @JsonProperty("SectionId")
	 private String     sectionId;
	 
	 @JsonProperty("LocationId")
	 private String     locationId;
	 
	 
}
