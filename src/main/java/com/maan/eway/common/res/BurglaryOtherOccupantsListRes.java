package com.maan.eway.common.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BurglaryOtherOccupantsListRes {

	@JsonProperty("Id")
    private String Id   ;
	@JsonProperty("Name")
    private String   name  ;
	@JsonProperty("Occupation")
    private String  occupation;
	@JsonProperty("HowSecured")
    private String howSecured;

}
