package com.maan.eway.common.req;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.eway.bean.EserviceCustomerDetails;

import lombok.Data;

@Data
public class NonMotorSaveReq {

	//Common Details
	@JsonProperty("PolicyDetails")
    private NonMotorPolicyReq     nonMotorPolicyReq ;
	//BrokerDetails
	@JsonProperty("BrokerDetails")
    private NonMotorBrokerReq     nonMotorBrokerReq ;
	//EndtFields
    @JsonProperty("EndorsementDetails") 
    private NonMotEndtReq     nonMotEndtReq ;   
	// Location Based
	@JsonProperty("LocationList")
	private List<NonMotorLocationReq> locationList;
	
	@JsonProperty("CustomerDetails")
	private EserviceCustomerDetails customerDetails;
	
	@JsonProperty("CustomerId")
	private String customerId;

	
	
}
