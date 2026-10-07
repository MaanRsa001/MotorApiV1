package com.maan.eway.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class DMVICRequest {
	
	@JsonProperty("policystartdate")
	private String policystartdate;
	
	@JsonProperty("policyenddate")
	private String policyenddate;
	
	@JsonProperty("vehicleregistrationnumber")
	private String vehicleregistrationnumber;
	
	@JsonProperty("chassisnumber")
	private String chassisnumber;

	@JsonProperty("CompanyId")
	private String companyId;
}
