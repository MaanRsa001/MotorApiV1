package com.maan.eway.common.req;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddRiskInfoReq {
	
	@JsonProperty("PrevQuoteNo")
	private String prevQuoteNo;
	
	@JsonProperty("EndtTypeId")
	private String endtTypeId;
	
	@JsonProperty("QuoteNo")
	private String quoteNo;
	
	@JsonProperty("OriginalPolicyNo")
	private String originalPolicyNo;
	
	@JsonProperty("EndtPolicyNo")
	private String endtPolicyNo;
	
	@JsonProperty("EndtReqRefNo")
	private String endtReqRefNo;
	
	@JsonProperty("AdditionalInfoList")
	private List<AdditionalInformationReq> addRiskList;
	
	@JsonProperty("EndorsementDetails")
	private NonMotEndtReq nonMotEndtReq;
	

}
