package com.maan.eway.res;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.eway.error.Error;
import lombok.Data;

@Data
public class MsDetailsRes {

	@JsonProperty("MsCustomerDetails")
	private MsCustomerDetailsRes msCustomerDetails ;

	@JsonProperty("MsCommonDetails")	
	private MsCommonDetailsRes msCommonDetails;
	
	@JsonProperty("MsVehicleDetails")
	private MsVehicleDetailsRes msVehicleDetails;
}
