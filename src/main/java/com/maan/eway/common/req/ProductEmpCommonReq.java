package com.maan.eway.common.req;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ProductEmpCommonReq {

	@JsonProperty("RequestReferenceNo")
    private String     requestReferenceNo ;
	
	@JsonProperty("RiskId")
    private String    riskId   ;
	
	@JsonProperty("QuoteNo")
    private String  quoteNo;
	
	@JsonProperty("Createdby")
    private String     createdBy    ;
	
	@JsonProperty("FidelityEmployeeDetails")
    private List<FidelityEmployeeSaveReq> fidelityEmployeeDetails;
	
}
