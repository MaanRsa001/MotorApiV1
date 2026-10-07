package com.maan.eway.common.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CommonSection {
	
	@JsonProperty("SectionId")
	private String sectionId;

	@JsonProperty("SectionName")
	private String sectionName;
	
	@JsonProperty("CoveringDetails")
	private String coveringDetails;
	
	@JsonProperty("DescriptionOfRisk")
	private String descriptionOfRisk;
	
	@JsonProperty("CategoryId")
	private String categoryId;
	
	@JsonProperty("ContentId")
	private String contentId;
	
	@JsonProperty("SumInsured")
	private String sumInsured;
	
	@JsonProperty("BusinessInterruption")
	private String businessInterruption;
	
	@JsonProperty("IndustryId")
	private String industryId;
	
	@JsonProperty("CoverId")
	private Integer coverId;
	
	@JsonProperty("RiskId")
	private String riskId;
}
