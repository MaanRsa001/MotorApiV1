package com.maan.eway.common.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BuildDetailsGetByLocIdReq {

	   @JsonProperty("RequestReferenceNo")
	    private String     requestReferenceNo ;
		@JsonProperty("LocationId")
	    private String    locationId    ;
}
