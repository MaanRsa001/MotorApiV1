package com.maan.eway.common.res;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.eway.common.req.NonMotorSectionReq;

import lombok.Data;

@Data
public class NonMotorLocationRes {

	
	@JsonProperty("LocationId")
	private Integer locationId;
	@JsonProperty("LocationName")
	private String locationName;
	
	@JsonProperty("AssesListDetails")
	private List<NonMotorAssestRes> Assest;
	
	@JsonProperty("HumanListDetails")
	private List<NonMotorHumanRes> Human;
}
