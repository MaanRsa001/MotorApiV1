package com.maan.eway.common.res;

import java.math.BigDecimal;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.eway.common.req.NonMotEndtReq;

import lombok.Data;

@Data
public class NonMotorComRes {

	@JsonProperty("RequestReferenceNo")
	private String requestReferenceNo;

	@JsonProperty("EndorsementDetails")
	private NonMotEndtReq nonMotEndtReq;
	
	@JsonProperty("AdditionalInfoYn")
	private String additionalInfoYn;
	
	@JsonProperty("SumInsuredEndt") 
	 private Map<String, BigDecimal> sumInsuredByRiskId;
	
	   @JsonProperty("countByRiskId") 
	   private Map<String, Long> countByRiskId;
	   
	   @JsonProperty("ReqColumn")
		private String reqColumn;
}
