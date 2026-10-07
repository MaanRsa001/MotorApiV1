package com.maan.eway.common.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BuildignDetailsDeleteReq {

	 @JsonProperty("QuoteNo")
	 private String     quoteNo ;
	 
	 @JsonProperty("LocationId")
	 private String    locationId;
}
