package com.maan.eway.common.res;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BurglaryOtherOccupantsGetRes {

	@JsonProperty("InsuranceId")
    private String companyId  ;
	@JsonProperty("BranchCode")
    private String  branchCode;
	@JsonProperty("ProductId")
    private String  productId;
	@JsonProperty("SectionId")
    private String sectionId;
	@JsonProperty("RequestReferenceNo")
    private String requestReferenceNo;
	@JsonProperty("QuoteNo")
    private String quoteNo;
	@JsonFormat(pattern="dd/MM/yyyy")
	@JsonProperty("EntryDate")
    private String entryDate;
	@JsonProperty("Status")
    private String status;
	@JsonProperty("RiskId")
    private String riskId;

	@JsonProperty("BurglaryOtherOccupants")
	private List<BurglaryOtherOccupantsListRes> BurglaryOtherOccupants;
	
}
