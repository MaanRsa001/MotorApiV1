package com.maan.eway.common.req;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DriverDetailsSaveReq {

	@JsonProperty("DriverName")
	private String driverName;
	
	@JsonFormat(pattern="dd/MM/yyyy")
	@JsonProperty("DriverDob")
	private Date driverDob;
	
	@JsonProperty("DriverType")
	private String driverType;
	
	@JsonProperty("LicenseNo")
	private String licenseNo;
	
	
}
