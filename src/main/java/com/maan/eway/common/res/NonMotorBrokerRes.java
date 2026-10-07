package com.maan.eway.common.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class NonMotorBrokerRes {

	@JsonProperty("ApplicationId")
	private String applicationId;
	@JsonProperty("BrokerCode")
	private String brokerCode;
	@JsonProperty("SubUsertype")
	private String subUserType;
	@JsonProperty("LoginId")
	private String loginId;
	@JsonProperty("AgencyCode")
	private String agencyCode;
	@JsonProperty("UserType")
	private String userType;
	@JsonProperty("BankCode")
	private String bankCode;
	@JsonProperty("BrokerBranchCode")
	private String brokerBranchCode;
	@JsonProperty("SourceTypeId")
	private String sourceTypeId;
	@JsonProperty("SourceType")
	private String sourceType;
	@JsonProperty("CustomerCode")
	private String customerCode;
	@JsonProperty("BdmCode")
	private String bdmCode;
	@JsonProperty("CommissionType")
	private String commissionType;

	@JsonProperty("BusinessSourceId")
	private String businessSourceId;

	@JsonProperty("BusinessSource")
	private String businessSource;
	
	@JsonProperty("CustomerName")
	private String customerName;
	
	@JsonProperty("BdmName")
	private String bdmName;
	
	@JsonProperty("SalePointCode")
	private String salePointCode;
	
	@JsonProperty("TiraCode")
	private String tiraCode;
	
	@JsonProperty("AcCode")
	private String acCode;
	
	@JsonProperty("AcCoreAppCode")
	private String acCoreAppCode;
	
	@JsonProperty("AcName")
	private String acName;
	
	
	
	

}
