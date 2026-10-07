package com.maan.eway.common.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class FleetDetailsRes {


	@JsonProperty("RequestReferenceNo")
	private String requestReferenceNo;
	
	
	@JsonProperty("PdRefNo")
	private String pdrefno;
	
	@JsonProperty("NoOfVehicles")
	private Integer noOfVehicles;
	
	@JsonProperty("InsuranceId")
	private String insuranceId;
	
	@JsonProperty("VdRefNo")
	private String vdRefNo;
	
	@JsonProperty("PDRefNo")
	private String pdrefno2;
	
	@JsonProperty("CreatedBy")
	private String createdBy;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("SectionId")
	private String sectionId;
	
	@JsonProperty("VehicleId")
	private String vehicleId;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("AgencyCode")
	private String agencyCode;
	

	@JsonProperty("CdRefNo")
	private String cdRefNo;


	@JsonProperty("MSRefNo")
	private String mSRefNo;
	
	
	
//	{
//	    "InsuranceId": "100019",
//	    "BranchCode": "59",
//	    "AgencyCode": "12689",
//	    "SectionId": "99999",
//	    "ProductId": "5",
//	    "MSRefNo": "96106",
//	    "VehicleId": "99999",
//	    "CdRefNo": "96104",
//	    "VdRefNo": "96105",
//	    "CreatedBy": "Ugandabroker",
//	    "productId": "5",
//	    "sectionId": "99999",
//	    "RequestReferenceNo": "AGI-MOT-05215",
//	    "EffectiveDate": "17/02/2024",
//	    "PolicyEndDate": "16/02/2025",
//	    "CoverModification": "N",
//	    "PDrefno":""
//	}
}
