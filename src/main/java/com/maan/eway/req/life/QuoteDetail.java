package com.maan.eway.req.life;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
public class QuoteDetail {
	 	@JsonProperty("ApplicationId")
        @NotEmpty(message = "ApplicationId is mandatory")

	    private String applicationId;
	    @JsonProperty("QuoteNo")	 
	    private String quoteNo;
	    
	    @JsonProperty("PolicyNo") 
	    private String policyNo;
	    @NotEmpty(message = "BranchCode is mandatory")
	    @JsonProperty("BranchCode") 
	    private String branchCode;
	    @JsonProperty("SourceType")
	    @NotEmpty(message = "SourceType is mandatory")
	    private String sourceType;
	    @JsonProperty("BDMCode") 
	    @NotEmpty(message = "BDMCode is mandatory")
	    private String bDMCode;
	    @JsonProperty("LoginId")
	    @NotEmpty(message = "LoginId is mandatory")
	    private String loginId;
	    @JsonProperty("BrokerBranch")
	    @NotEmpty(message = "BrokerBranch is mandatory")
	    private String brokerBranch;
	    @JsonFormat(pattern="dd/MM/yyyy")
	    @JsonProperty("PolicyStartDate")
	    @NotNull(message = "PolicyStartDate is mandatory")
	    private Date policyStartDate;
	    @JsonFormat(pattern="dd/MM/yyyy")
	    @JsonProperty("PolicyEndDate") 
	    @NotNull(message = "PolicyEndDate is mandatory")
	    private Date policyEndDate;
	    @JsonProperty("PolicyPeriod")
	    @NotEmpty(message = "PolicyPeriod is mandatory")
	    private String policyPeriod;
	    @JsonProperty("HavePromoCode")
	    @NotEmpty(message = "HavePromoCode is mandatory")
	    private String havePromoCode;
	    @JsonProperty("PromoCode") 
	    private String promoCode;
	    @JsonProperty("Currency")
	    @NotEmpty(message = "Currency is mandatory")
	    private String currency;
	    @JsonProperty("ExchangeRate") 
	    @NotEmpty(message = "ExchangeRate is mandatory")
	    private Double exchangeRate;
	    
	    @JsonProperty("CompanyId") 
	    @NotEmpty(message = "CompanyId is mandatory")
	    private String companyId;
	    @JsonProperty("ProductId") 
	    @NotEmpty(message = "ProductId is mandatory")
	    private Integer productId;
	    @JsonProperty("SectionId") 
	    @NotEmpty(message = "SectionId is mandatory")
	    private String sectionId; 
	    
	    @JsonProperty("RequestReferenceNo") 
	    @NotEmpty(message = "RequestReferenceNo is mandatory")
	    private String requestReferenceNo;
	    
	    @JsonProperty("SubUserType") 
	    @NotEmpty(message = "SubUserType is mandatory")
	    private String subUserType;
	    
}
