package com.maan.eway.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class GetCertificateReq {
	
	@JsonProperty("CertificateNumber")
	private String certificateNumber;
	
	@JsonProperty("PolicyNo")
	private String policyNo;

}
