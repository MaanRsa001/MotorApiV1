package com.maan.eway.common.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class EwayCityRes {
	
	@JsonProperty("Code")
	private Integer cityId;
	@JsonProperty("CodeDesc")
    private String city;

}
