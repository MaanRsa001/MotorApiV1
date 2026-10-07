package com.maan.eway.common.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GoodsIntransitSaveReq {
	
	
	@JsonProperty("TransportedBy")
	private String transportedBy;

	@JsonProperty("ModeOfTransport")
	private String modeOfTransport;

	@JsonProperty("SingleRoadSiFc")
	private Integer singleRoadSiFc; 
	
	@JsonProperty("SingleRoadSiLc")
	private Integer singleRoadSiLc; 

	@JsonProperty("GeographicalCoverage")
	private String geographicalCoverage;

	@JsonProperty("EstAnnualCarriesSiFc")
	private Integer estAnnualCarriesSiFc; 

	@JsonProperty("EstAnnualCarriesSiLc")
	private Integer estAnnualCarriesSiLc; 

	@JsonProperty("SectionId")
	private String sectionId;

	@JsonProperty("Status")
	private String status;

}
