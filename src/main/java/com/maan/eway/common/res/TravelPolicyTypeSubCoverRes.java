package com.maan.eway.common.res;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TravelPolicyTypeSubCoverRes {

	
	
	@JsonProperty("SubCoverId")
    private String    subCoverId ;
	
	@JsonProperty("SubCoverDesc")
    private String    subCoverDesc;

	@JsonProperty("Currency")
    private String    currency;

	@JsonProperty("SumInsured")
    private String   sumInsured;

	@JsonProperty("ExcessAmt")
    private String  excessAmt;

	@JsonFormat(pattern="dd/MM/yyyy")
	@JsonProperty("EntryDate")
    private Date  entryDate;
	
	@JsonProperty("Status")
    private String  status;




}

