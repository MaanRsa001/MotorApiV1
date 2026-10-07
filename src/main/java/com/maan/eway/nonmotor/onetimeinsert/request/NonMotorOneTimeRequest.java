package com.maan.eway.nonmotor.onetimeinsert.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class NonMotorOneTimeRequest {

	@JsonProperty("InsuranceId")
	private String insuranceId;

	@JsonProperty("ProductId")
	private String productId;

	@JsonProperty("CreatedBy")
	private String CreatedBy;

	@JsonProperty("RequestReferenceNo")
	private String requestReferenceNo;

	@JsonProperty("OrginalPolicyNo")
	private String orginalPolicyNo;

	@JsonProperty("ExchangeRate")
	private String exchangeRate;

	@JsonProperty("PolicyEndDate")
	private String policyEndDate;

	@JsonProperty("PolicyStartDate")
	private String policyStartDate;

	@JsonProperty("AgencyCode")
	private String agencyCode;

	@JsonProperty("SubUsertype")
	private String subUsertype;

	@JsonProperty("BdmCode")
	private String bdmCode;

	@JsonProperty("BranchCode")
	private String branchCode;

	@JsonProperty("Currency")
	private String currency;
	
	@JsonProperty("CustomerReferenceNo")
	private String customerReferenceNo;

	@JsonProperty("BrokerCode")
	private String brokerCode;

	@JsonProperty("BrokerBranchCode")
	private String brokerBranchCode;

	@JsonProperty("Havepromocode")
	private String havepromocode;
	
	@JsonProperty("PromoCode")
	private String promoCode;

	@JsonProperty("BuildingOwnerYn")
	private String buildingOwnerYn;

	@JsonProperty("CustomerName")
	private String customerName;

	@JsonProperty("EndorsementType")
	private String endorsementType;
	
	@JsonProperty("HavePromoCode")
	private String havePromoCode;
	
	@JsonProperty("CategoryId")
	private String categoryId;
	
	@JsonProperty("SourceTypeId")
	private String sourceTypeId;
	
	@JsonProperty("CustomerCode")
	private String customerCode;
	
	@JsonProperty("Status")
	private String status;
	
	@JsonProperty("EndorsementRequest")
	private EndorsementRequest endormentRequest;

	@JsonProperty("FireRiskDetailsRequest")
	private List<FireRiskRequest> frdRequest;

	@JsonProperty("PersonalAccidentRequest")
	private List<PersonalAccidentRequest> paRequest;

}
