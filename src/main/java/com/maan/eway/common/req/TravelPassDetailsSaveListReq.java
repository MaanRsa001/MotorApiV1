package com.maan.eway.common.req;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TravelPassDetailsSaveListReq {

	
	@JsonProperty("PassengerId")
	private String passengerId;
	
	@JsonProperty("GroupId")
	private Integer groupId;
	
	@JsonProperty("QuoteNo")
	private String quoteNo;

	@JsonProperty("CreatedBy")
	private String createdBy;
	
	@JsonProperty("PassengerFirstName")
	private String passengerFirstName;
	
	@JsonProperty("PassengerLastName")
	private String passengerLastName;
	
	@JsonFormat(pattern="dd/MM/yyyy")
	@JsonProperty("Dob")
	private Date dob;
	
	@JsonProperty("GenderId")
	private String genderId;
	
	@JsonProperty("Age")
	private String age;

	@JsonProperty("RelationId")
	private String relationId;

	
	@JsonProperty("Natinality")
	private String nationality;
	

	
	@JsonProperty("PassportNo")
	private String passportNo;
	
	@JsonProperty("CivilId")
	private String civilId;


}
