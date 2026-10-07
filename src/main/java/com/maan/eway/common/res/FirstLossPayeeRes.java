package com.maan.eway.common.res;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
public class FirstLossPayeeRes {

	@JsonProperty("RequestReferenceNo")
	private String requestReferenceNo;
	@JsonProperty("FirstLossPayeeId")
	private String     firstLossPayeeId ;
	@JsonProperty("FirstLossPayeeDesc")
	private String     firstLossPayeeDesc ;
	@JsonProperty("SectionId")
	private String sectionId;
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty( "LocationId")
	private String locationId;
	@JsonProperty("LocationName")
	private String locationName;
	@JsonProperty("CompanyId")
	private String companyId;
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EntryDate")
	private Date entryDate;
	@JsonProperty("CreatedBy")
	private String createdBy;
	@JsonProperty("Status")
	private String status;
	@JsonProperty("RiskId")
	private String riskId;
	
	@JsonProperty("BorrowerTypeId")
	private String borrowerTypeId;
	@JsonProperty("BorrowerTypeDesc")
	private String borrowerTypeDesc;
	@JsonProperty("CollateralId")
	private String collateralId;
	@JsonProperty("CollateralName")
	private String collateralName;

}
