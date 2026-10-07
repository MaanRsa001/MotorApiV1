package com.maan.eway.common.res;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetAllBuldingDetailsRes {

	@JsonProperty("RequestReferenceNo")
    private String     requestReferenceNo ;
	@JsonProperty("CustomerReferenceNo")
    private String     customerReferenceNo ;

  
	@JsonProperty("ProductId")
    private String    productId    ;

	@JsonProperty("InsuranceId")
    private String     companyId    ;
	@JsonProperty("BranchCode")
    private String     branchCode   ;
	
	@JsonProperty("Createdby")
    private String     createdBy    ;
	@JsonProperty("AdminRemarks")
    private String     adminRemarks;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EntryDate")
    private Date       entryDate ;
	
	@JsonProperty("QuoteNo")
    private String     quoteNo;
	
	@JsonProperty("Status")
    private String    status;
	
	@JsonProperty("BankCode")
    private String    bankCode;
	
	
	@JsonProperty("CustomerId")
    private String     customerId;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("UpdatedDate")
	private Date updatedDate;

	@JsonProperty("UpdatedBy")
	private String updatedBy;
	
// Money Related
    
    @JsonProperty("CashInHandDirectors")
    private String cashInHandDirectors;
    
    @JsonProperty("CashInTransit")
    private String cashInTransit;
    
    @JsonProperty("CashInHandEmployees")
    private String cashInHandEmployees;
    
    @JsonProperty("CashInSafe")
    private String cashInSafe;
    
    @JsonProperty("CashInPremises")
    private String cashInPremises;
    
    @JsonProperty("RevenueFromStamps")
    private String revenueFromStamps;
    
    @JsonProperty("MoneyInSafeBusiness")
    private String moneyInSafeBusiness;
    
    @JsonProperty("MoneyOutSafeBusiness")
    private String moneyOutSafeBusiness;
    
    @JsonProperty("MoneyInPremises")
    private String moneyInPremises;
    
    @JsonProperty("MoneyInLocker")
    private String moneyInLocker;
}
