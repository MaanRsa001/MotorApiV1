package com.maan.eway.common.req;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class NonMotorCommonRequest {

	@JsonProperty("Title")
	private String title;
	
	@JsonProperty("ClientName")
	private String clientName;
	
	@JsonProperty("Gender")
	private String gender;
	
	@JsonProperty("IdNumber")
	private String idNumber;
	
	@JsonProperty("IdType")
	private String idType;
	
	@JsonProperty("DobOrRegDate")
	private String dobOrRegDate;
	
	@JsonProperty("MobileCode")
	private String mobileCode;
	
	@JsonProperty("MobileNo")
	private String mobileNo;
	
	@JsonProperty("Nationality")
	private String nationality;
	
	@JsonProperty("Country")
	private String country;
	
	@JsonProperty("CountryName")
	private String countryName;
	
	@JsonProperty("Occupation")
	private String occupation;
	
	@JsonProperty("Street")
	private String street;
	
	@JsonProperty("Address")
	private String address;
	
	@JsonProperty("CityName")
	private String cityName;
	
	@JsonProperty("RegionCode")
	private String regionCode;
	
	@JsonProperty("StateCode")
	private String stateCode;
	
	@JsonProperty("BrokerBranchCode")
	private String brokerBranchCode;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("LoginId")
	private String loginId;
	
	@JsonProperty("PolicyHolderType")
	private String policyHolderType;

	@JsonProperty("PolicyDetails")
	private CommonPolicyDetails policyDetails;

	@JsonProperty("LocationList")
	private List<CommonLocation> locationList;
	
}
