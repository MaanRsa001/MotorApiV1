package com.maan.eway.res;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MotorSaveRes {

	@JsonProperty("Response")
	private String response;
	@JsonProperty("RequestReferenceNo")
	private String requestReferenceNo;
//	@JsonProperty("Error")
//	private List<String> error;
	@JsonProperty("Error")
	private String error;
	@JsonProperty("ShowVehicleInfo")
	private MotorVehicleInfoRes showVehicleInfo;
}
