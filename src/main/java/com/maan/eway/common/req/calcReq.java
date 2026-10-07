package com.maan.eway.common.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class calcReq {
	
	@JsonProperty("InsuranceId") 
	private String insuranceId;
	
	@JsonProperty("LocationId") 
	private String locationId;
	
	 @JsonProperty("SectionId") 
	 private String sectionId;
	 
	 @JsonProperty("RequestReferenceNo")
	 private String requestReferenceNo;
	 
	 @JsonProperty("ProductId") 
	 private String productId;
	 

}
