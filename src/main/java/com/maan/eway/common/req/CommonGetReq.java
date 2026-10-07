package com.maan.eway.common.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CommonGetReq {

	@JsonProperty("RequestReferenceNo")
    private String     requestReferenceNo ;
	@JsonProperty("RiskId")
    private String    riskId   ;
	@JsonProperty("InsuranceId")
    private String    insuranceId;
	@JsonProperty("ProductId")
    private String    productId;
}
