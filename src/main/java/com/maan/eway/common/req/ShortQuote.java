package com.maan.eway.common.req;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShortQuote implements Serializable {
		
		@JsonProperty("CustomerName")
	    @NotEmpty(message = "CustomerName is mandatory")
		private String customerName;
		@JsonProperty("Gender")
		@NotEmpty(message = "Gender is mandatory")
	    private String gender;
		@JsonFormat(pattern="dd/MM/yyyy")
		@JsonProperty("DateOfBirth")
		@NotNull(message = "DateOfBirth is mandatory")
	    private Date dateOfBirth;
		@JsonProperty("PolicyTerm")
		@NotEmpty(message = "PolicyTerm is mandatory")
	    private String policyTerm;
		@JsonProperty("PayingTerm")
	    private Long payingTerm;
		
		@JsonProperty("SumInsured")
		@NotNull(message = "SumInsured is mandatory")
	    private BigDecimal sumInsured;
		
		@JsonProperty("EmailId")
		@NotNull(message = "EmailId is mandatory")
	    private String emailId;
		
		@JsonProperty("MobileNo")
		@NotNull(message = "MobileNo is mandatory")
	    private String mobileNo;
		
		@JsonProperty("MobileCode")
		@NotNull(message = "MobileCode is mandatory")
	    private String mobileCode;
		
		@JsonProperty("PaymentMode")		
	    private String paymentMode;
		
		@JsonProperty("SourceType")
	    private String sourceType;
		
		@JsonProperty("BrokerCode")
	    private String brokerCode;
		
		@JsonProperty("BrokerBranchCode")
	    private String brokerBranchCode;
		
		@JsonProperty("BDMCode")
	    private String bdmCode;
		@JsonProperty("BranchCode")
	    private String branchCode;
		@JsonProperty("AgencyCode")
	    private String agencyCode;
		@JsonProperty("InsuranceId")
	    private String insuranceID;
		@JsonProperty("ProductId")
		private Integer productID;
		@JsonProperty("LoginId")
	    private String loginID;
		@JsonProperty("ApplicationId")
	    private String applicationID;
		@JsonProperty("RequestReferenceNo")
	    private String requestReferenceNo;
		
		
		@JsonProperty("BusinessType")
		private String businessType;
		@JsonProperty("BusinessTypeDesc")
		private String businessTypeDesc;
		@JsonProperty("CityCode")
		private String cityCode;
		@JsonProperty("CityName")
		private String cityName;
		@JsonProperty("CreatedBy")
		private String createdBy;
		@JsonProperty("GenderDesc")
		private String genderDesc;
		@JsonProperty("IdNumber")
		private String idNumber;
		@JsonProperty("IdType")
		private String idType;
		@JsonProperty("IdTypeDesc")
		private String idTypeDesc;
		@JsonProperty("isTaxExempted")
		private String isTaxExempted;
		@JsonProperty("Nationality")
		private String nationality;
		@JsonProperty("Occupation")
		private String occupation;
		@JsonProperty("OccupationDesc")
		private String occupationDesc;
		@JsonProperty("PlaceOfBirth")
		private String placeOfBirth;
		@JsonProperty("PolicyHolderType")
		private String policyHolderType;
		@JsonProperty("PolicyHolderTypeid")
		private String policyHolderTypeid;
		@JsonProperty("RegionCode")
		private String regionCode;
		@JsonProperty("StateCode")
		private String stateCode;
		@JsonProperty("StateName")
		private String stateName;
		@JsonProperty("taxExemptedId") 
		private String taxExemptedId;
		
		@JsonProperty("Currency")
		private String currency ;
		@JsonProperty("ExchangeRate")
	    private Double exchangeRate ;
		@JsonProperty("Havepromocode")
	    private String havepromocode ;
		@JsonProperty("PeriodOfInsurance")
	    private String periodOfInsurance ;
		@JsonProperty("Promocode")
	    private String promocode ;
		
		
		@JsonProperty("SumInsuredLc")
		private BigDecimal sumInsuredLc;
		@JsonProperty("UwLoading")
		private Double uwLoading;
		
}
