package com.maan.eway.common.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class EwayStateReq {

	@JsonProperty("CountryId")
    private String  countryId;
	
	@JsonProperty("CityId")
	private Integer cityId;
	
	@JsonProperty("StateId")
	private Integer StateId;
	
	
}
