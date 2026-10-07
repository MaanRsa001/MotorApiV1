package com.maan.eway.common.req;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class updateDateReq {

	@JsonProperty("RequestReferenceNo")
	private String requestReferenceNo;
	@JsonProperty("QuoteNo")
	private String quoteNo;
	@JsonProperty("StartDate")
	private String startDate;
	@JsonProperty("EndDate")
	private String endDate;
}
