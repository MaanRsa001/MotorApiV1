package com.maan.eway.common.req;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PassengerListSaveReq {

	@JsonProperty("PassengerId")
	private String passengerId;
	
//	@JsonProperty("GroupId")
//	private Integer groupId;
		
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

	@JsonProperty("Nationality")
	private String nationality;
	
	@JsonProperty("PassportNo")
	private String passportNo;
	
	@JsonProperty("PassportType")
	private String passportType;
	
	@JsonProperty("PlaceOfBirth")
	private String placeOfBirth;
	
	@JsonProperty("PlaceOfIssue")
	private String placeOfIssue;
	
	@JsonFormat(pattern="dd/MM/yyyy")
	@JsonProperty("PassportIssueDate")
	private Date passportIssueDate;
	
	@JsonFormat(pattern="dd/MM/yyyy")
	@JsonProperty("PassportExpiryDate")
	private Date passportExpiryDate;
	
	@JsonProperty("AlertYN")
	private String alertYN;
	
	
}
