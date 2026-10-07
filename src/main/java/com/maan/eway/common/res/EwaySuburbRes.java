package com.maan.eway.common.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class EwaySuburbRes {

	@JsonProperty("Code")
	 private Integer suburbId;

	@JsonProperty("CodeDesc")
	 private String suburb;
}
