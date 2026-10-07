package com.maan.eway.common.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class NonMotorReqForDD {
	
	 @JsonProperty("ProductId") 
	 private String productId;

	 @JsonProperty("SectionId") 
	 private String sectionId;
	 
	 @JsonProperty("CompanyId") 
	 private String companyId;
	 
}
