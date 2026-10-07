package com.maan.eway.common.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class EservicePersonalAccGetById {

		@JsonProperty("RequestReferenceNo")
	    private String     requestReferenceNo ;
		@JsonProperty("RiskId")
	    private String    riskId    ;
}
