package com.maan.eway.common.res;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.eway.common.req.VehicleDetailsReq;

import lombok.Data;
@Data
public class GetOtherVehicleInfoRes {

	@JsonProperty("CompanyId")
	private String companyId;
	
	@JsonProperty("ProductId")
	private Integer productId;
	
	@JsonProperty("SectionId")
	private Integer sectionId;
	
	@JsonProperty("QuoteNo")
	private String quoteNo;
	
	@JsonProperty("RequestReferenceNo")
	private String requestReferenceNo;
	
	@JsonProperty("VehicleDetails")
	private List<VehicleDetailsReq> VehicleDetails;
}
