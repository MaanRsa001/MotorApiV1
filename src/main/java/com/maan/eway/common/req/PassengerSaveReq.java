package com.maan.eway.common.req;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PassengerSaveReq {

	@JsonProperty("QuoteNo")
	private String quoteNo;

	@JsonProperty("CreatedBy")
	private String createdBy;
	
	@JsonProperty("PassengerList")
	private List<PassengerListSaveReq> passengerList;
}
