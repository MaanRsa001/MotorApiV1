package com.maan.eway.common.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ContentRiskDetailsListReq {

	
	@JsonProperty("ItemId")
	private String itemId;
	@JsonProperty("ItemValue")
	private String itemValue;
	
	@JsonProperty("SumInsured")
	private String sumInsured;
	@JsonProperty("SerialNo")
	private String serialNo;
	@JsonProperty("MakeAndModel")
	private String makeAndModel;
	@JsonProperty("RiskId")
	private String riskId;
	@JsonProperty("PurchaseYear")
	private String purchaseYear;
	@JsonProperty("PurchaseMonth")
	private String purchaseMonth;
	
	@JsonProperty("ManufactureYear")
	private String manufactureYear;
	
	@JsonProperty("ContentRiskDesc")
	private String contentRiskDesc ;  //machinary desc
	
	@JsonProperty("SerialNoDesc")
	private String serialNoDesc ;
	
	@JsonProperty("Brand")
	private String brand;
	
	@JsonProperty("Name")
	private String name;
	
	//machinary breakdown
//	@JsonProperty("Name")
//	private String name ;
//	@JsonProperty("Brand")
//	private String brand ;
	@JsonProperty("LocationId")
	private String locationId ;
	@JsonProperty("LocationName")
	private String locationName ;
	
	}
