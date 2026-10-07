package com.maan.eway.common.req;
 
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ChangeOfCurrencyReq {

	@JsonProperty("InsuranceId")
	private String insuranceId;
	
	@JsonProperty("Currency")
	private String currency;
	
	@JsonProperty("ExchangeRate")
	private String exchangeRate;
	
	@JsonProperty("RequestReferenceNo")
	private String requestReferenceNo;
	
	@JsonProperty("ProductId")
	private String productId;
	
}
