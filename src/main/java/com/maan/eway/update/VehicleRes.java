package com.maan.eway.update;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class VehicleRes {

	@JsonProperty("RequestReferenceNo")
	private String requestReferenceNo;

	@JsonProperty("InsuranceId")
	private String insuranceId;

	@JsonProperty("VehicleId")
	private String vehicleId;

	@JsonProperty("LocationId")
	private String locationId;
}
