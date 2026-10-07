package com.maan.eway.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DMVICTokenGenerationRes {
	
	@JsonProperty("token")
	private String token;
	
	@JsonProperty("loginUserId")
	private String loginUserId;
	
	@JsonProperty("issueAt")
	private String issueAt;
	
	@JsonProperty("expires")
	private String expires;
	
	@JsonProperty("code")
	private String code;
	
	@JsonProperty("LoginHistoryId")
	private String loginHistoryId;
	
	@JsonProperty("firstName")
	private String firstName;
	
	@JsonProperty("lastName")
	private String lastName;
	
	@JsonProperty("loggedinEntityId")
	private String loggedinEntityId;
	
	@JsonProperty("ApimSubscriptionKey")
	private String apimSubscriptionKey;
	
	@JsonProperty("IndustryTypeId")
	private String industryTypeId;
	
	
	

}
