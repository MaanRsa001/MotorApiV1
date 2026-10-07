package com.maan.eway.common.res;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ContentRiskGetallRes {

	
	@JsonProperty("RequestReferenceNo")
	private String requestReferenceNo;
	@JsonProperty("SectionId")
	private String sectionId;
	@JsonProperty("SectionDesc")
	private String sectionDesc;
	@JsonProperty("Type")
	private String type;

	@JsonProperty("TypeDesc")
	private String typeDesc;

	@JsonProperty("QuoteNo")
	private String quoteNo;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EntryDate")
	private Date entryDate;

	@JsonProperty("Createdby")
	private String createdBy;

	@JsonProperty("Status")
	private String status;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("UpdatedDate")
	private Date updatedDate;

	@JsonProperty("Updatedby")
	private String updatedBy;

	
	@JsonProperty("ContentRiskDetails")
	private List<ContentRiskDetailsRes> contentriskdetails;
	

}
