package com.maan.eway.common.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DriverLicenceReq {


	@JsonProperty("RiskId")
	private String riskId;
	
	@JsonProperty("LicenseNo")
	private String licenseNo;
}
