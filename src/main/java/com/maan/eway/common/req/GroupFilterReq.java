package com.maan.eway.common.req;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GroupFilterReq {
		
	
	@JsonFormat(pattern="dd/MM/yyyy")
	@JsonProperty("Dob")
	private Date dob;
		
	@JsonProperty("PassportNo")
	private String passportNo;
	
	@JsonProperty("From")
	private Long from ;
	
	@JsonProperty("To")
	private Long to ;
	
	@JsonProperty("GroupId")
	private Integer groupId ;
	
	@JsonProperty("GroupDesc")
	private String groupDesc ;
	
}
