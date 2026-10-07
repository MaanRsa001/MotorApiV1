package com.maan.eway.common.req;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
 
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BurglaryOtherOccupantsSaveReq {

	@JsonProperty("Id")
    private String id   ;
	@JsonProperty("InsuranceId")
    private String companyId   ;
	@JsonProperty("BranchCode")
    private String branchCode ;
	@JsonProperty("ProductId")
    private String productId   ;
	@JsonProperty("SectionId")
    private String sectionId    ;
	@JsonProperty("RiskId")
    private String    riskId   ;
	@JsonProperty("RequestReferenceNo")
    private String     requestReferenceNo ;	
	@JsonProperty("QuoteNo")
    private String  quoteNo;
	@JsonProperty("Name")
    private String   name  ;
	@JsonProperty("Occupation")
    private String  occupation;
	@JsonProperty("HowSecured")
    private String howSecured;
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EntryDate")
    private Date  entryDate ;
	@JsonProperty("Createdby")
    private String     createdBy    ;
	@JsonProperty("Status")
    private String   status  ;
	
	
}
