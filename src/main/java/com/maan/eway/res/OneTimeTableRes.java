package com.maan.eway.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.eway.bean.MsVehicleDetails;

import lombok.Data;

@Data
public class OneTimeTableRes {
	
	@JsonProperty("RequestReferenceNo")
	private String requestReferenceNo ;
	
	@JsonProperty("CdRefNo")
	private String cdRefNo;

	@JsonProperty("VehicleId")
	private String vehicleId;
	
	@JsonProperty("CoverId")
	private String coverid;
	
	@JsonProperty("LocationId")
	private String locationId;
	
	@JsonProperty("VdRefNo")
	private String vdRefNo;
	
	@JsonProperty("MsRefNo")
	private String msRefNo;
	
	@JsonProperty("SectionId")
	private String sectionId;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("CompanyId")
	private String companyId;
	
	@JsonProperty("MsVehicleDetails")
	private MsVehicleDetails msVehicleDetails ;
	
	@JsonProperty("FinalizeYn")
	private String finalizeYn;
	@JsonProperty("DdRefNo")
	private String ddRefNo;
	
	
}
