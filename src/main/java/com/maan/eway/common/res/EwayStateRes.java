package com.maan.eway.common.res;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class EwayStateRes {

	@JsonProperty("Code")
	private Integer stateId;

	@JsonProperty("CodeDesc")
	private String stateName;

	@JsonIgnore
	private Integer cityId;

	@JsonIgnore
	private String city;
	
	@JsonProperty("CityList")
	 private List<EwayCityRes> cities;

}
