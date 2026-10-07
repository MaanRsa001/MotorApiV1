package com.maan.eway.common.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ProductEmployeeDeleteAllReq {
	@JsonProperty("QuoteNo")
	 private String     quoteNo ;
	

	 @JsonProperty("SectionId")
	 private String    sectionId ;
}
