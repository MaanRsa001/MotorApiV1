package com.maan.eway.common.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class CreditLimitReq {
	
	@JsonProperty("AgencyCode")
	private String agencyCode;
	
	@JsonProperty("PremiunAmount")
	private String premiunAmount;
	

	@JsonProperty("CompanyId")
	private String companyId;

}
