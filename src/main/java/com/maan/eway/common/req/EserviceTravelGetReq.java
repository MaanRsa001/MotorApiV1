package com.maan.eway.common.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class EserviceTravelGetReq {

	@JsonProperty("RequestReferenceNo")
	private String     requestReferenceNo ;
	@JsonProperty("TravelId")
	private String    travelId    ;
}
