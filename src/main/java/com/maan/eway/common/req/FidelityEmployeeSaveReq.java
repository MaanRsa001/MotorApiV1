package com.maan.eway.common.req;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class FidelityEmployeeSaveReq {


	@JsonProperty("RequestReferenceNo")
    private String     requestReferenceNo ;
	
	@JsonProperty("InsuranceId")
    private String     insuranceId;
	
	@JsonProperty("ProductId")
    private String     productId;
	
	@JsonProperty("QuoteNo")
    private String  quoteNo;
	
	@JsonProperty("Createdby")
    private String     createdBy    ;

	@JsonProperty("RiskId")
    private String     riskId ;
	
	@JsonProperty("EmployeeName")
    private String    employeeName  ;
	
	@JsonProperty("Address")
    private String address ;
	
	@JsonProperty("OccupationId")
    private String     occupationId ;
	
	@JsonProperty("OccupationDesc")
    private String     occupationDesc ;
	
	@JsonProperty("Salary")
    private String salary;
	
}
