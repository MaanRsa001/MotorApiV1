package com.maan.eway.common.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ContentRiskDetailsRes {

	
	
	@JsonProperty("RiskId")
	private String riskId;
	@JsonProperty("ItemId")
	private String itemId;
	@JsonProperty("ItemDesc")
	private String itemDesc;
	@JsonProperty("ItemValue")
	private String itemValue;
	@JsonProperty("SumInsured")
	private String sumInsured;
	@JsonProperty("SerialNo")
	private String serialNo;
	@JsonProperty("MakeAndModel")
	private String makeAndModel;
	@JsonProperty("PurchaseYear")
	private String purchaseYear;
	@JsonProperty("PurchaseMonth")
	private String purchaseMonth;

	@JsonProperty("ContentRiskDesc")
	private String contentRiskDesc ;
	
	@JsonProperty("SerialNoDesc")
	private String serialNoDesc ;
	@JsonProperty("ManufactureYear")
	private String manufactureYear;
	@JsonProperty("Brand")
	private String brand;
	
	@JsonProperty("Name")
	private String name;
	@JsonProperty("LocationId")
	private String locationId;
	
	@JsonProperty("LocationName")
	private String locationName;
}
