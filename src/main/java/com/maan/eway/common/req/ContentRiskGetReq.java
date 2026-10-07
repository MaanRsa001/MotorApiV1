package com.maan.eway.common.req;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
 
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ContentRiskGetReq {

	@JsonProperty("QuoteNo")
	private String quoteNo;
	@JsonProperty("RiskId")
	private String riskId;
	@JsonProperty("ItemId")
	private String itemId;
	@JsonProperty("SectionId")
	private String sectionId;

}
