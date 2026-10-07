package com.maan.eway.common.req;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AddRemovedPassengerReq {

	
	@JsonProperty("QuoteNo")
	private String quoteNo;

	@JsonProperty("TravelId")
	private String travelId;
	
	@JsonProperty("PassengerIds")
	private List<Integer> passengerIds;
}
