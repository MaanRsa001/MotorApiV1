package com.maan.eway.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DoubleInsurance {
	
	@JsonProperty("CoverEndDate")
	private String coverEndDate;
	
	@JsonProperty("InsuranceCertificateNo")
	private String insuranceCertificateNo;
	
	@JsonProperty("MemberCompanyName")
	private String memberCompanyName;
	
	@JsonProperty("RegistrationNumber")
	private String registrationNumber;
	
	@JsonProperty("ChassisNumber")
	private String chassisNumber;

}
