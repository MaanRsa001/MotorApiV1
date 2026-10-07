package com.maan.eway.common.req;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Illustration {

	@JsonProperty("MsRefNo")
	private Long msRefNo;
	@JsonProperty("CdRefNo")
	private Long cdRefNo;
	@JsonProperty("VdRefNo")
	private Long vdRefNo;
	
	@JsonProperty("OptedCovers")
	private List<Integer> optedCovers;
	
	@JsonProperty("LoginId")
	private String loginId;
	
	@JsonProperty("ApplicationId")
	private String applicationId;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
}
