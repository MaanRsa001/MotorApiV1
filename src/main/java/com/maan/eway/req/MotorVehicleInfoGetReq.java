package com.maan.eway.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data

public class MotorVehicleInfoGetReq {

	@JsonProperty("ReqRegNumber")
	private String reqRegNumber ;
	
	@JsonProperty("ReqChassisNumber")
	private String reqChassisNumber ;
	@JsonProperty("SavedFrom")
	private String savedFrom ;
	
	
	@JsonProperty("InsuranceId")
	private String insuranceId ;
	@JsonProperty("BranchCode")
	private String branchCode ;
	@JsonProperty("BrokerBranchCode")
	private String brokerBranchCode ;
	@JsonProperty("ProductId")
	private String productId ;
	@JsonProperty("CreatedBy")
	private String createdBy ;
	
}
