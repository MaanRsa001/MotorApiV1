package com.maan.eway.common.res;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TravelPolicyTypeCoverRes {

	
	@JsonProperty("CoverId")
    private String    coverId ;
	
	@JsonProperty("CoverDesc")
    private String    coverDesc;
	
	@JsonProperty("CoverStatus")
    private String    coverStatus;

	@JsonProperty("SubCoverDetails")
    private List<TravelPolicyTypeSubCoverRes> travelSubCover;

}

