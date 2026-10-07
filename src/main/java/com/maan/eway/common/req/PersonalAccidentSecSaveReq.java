package com.maan.eway.common.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PersonalAccidentSecSaveReq {
	
	@JsonProperty("SectionId")
	private String sectionId;

	@JsonProperty("TotalNoOfPersons")
	private String totalNoOfPersons;

	@JsonProperty("SumInsured")
	private String sumInsured;

	@JsonProperty("OccupationType")
	private String occupationType;

	@JsonProperty("OtherOccupation")
	private String otherOccupation;

	@JsonProperty("TTDSumInsured")
	private Integer ttdSumInsured;

	@JsonProperty("MESumInsured")
	private Integer meSumInsured;

	@JsonProperty("FESumInsured")
	private Integer feSumInsured;

	@JsonProperty("PTDSumInsured")
	private Integer ptdSumInsured;
	
//	@JsonProperty("RiskId")
//	private String riskId;

}
