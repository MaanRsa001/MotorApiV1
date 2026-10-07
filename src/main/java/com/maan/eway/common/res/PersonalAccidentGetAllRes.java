package com.maan.eway.common.res;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PersonalAccidentGetAllRes {

	@JsonProperty("RequestReferenceNo")
	private String requestReferenceNo;
	@JsonProperty("SectionId")
	private String sectionId;
	@JsonProperty("SerialNo")
	private String serialNo;
	@JsonProperty("SectionDesc")
	private String sectionDesc;
	
	@JsonProperty("QuoteNo")
	private String quoteNo;
	@JsonProperty("Type")
	private String type;

	@JsonProperty("TypeDesc")
	private String typeDesc;
	
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

	@JsonProperty("PersonalDetails")
	private List<PersonalDetailsGetallRes> personalDeatils;
	
	
}
