package com.maan.eway.common.req;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DepreciationReq {
	
	@JsonProperty("InflationSumInsured")
	private BigDecimal inflationSumInsured;

	@JsonProperty("InflationPercentage")
	private BigDecimal inflationPercentage;

	@JsonProperty("PurchaseDate")
	private Date purchaseDate;
	
	@JsonProperty("RequestReferenceNo")
	private String requestReferenceNo;
	
	@JsonProperty("VehicleId")
	private Integer riskId;
	
	
	@JsonProperty("VdRefNo")
	private String vdRefNo;
	
	@JsonProperty("ExchangeRate")
	private BigDecimal exchangeRate;

}
