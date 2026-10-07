package com.maan.eway.common.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BuildingDetailsGetAllReq {

	 @JsonProperty("QuoteNo")
	 private String     quoteNo ;
	 
	 
}
