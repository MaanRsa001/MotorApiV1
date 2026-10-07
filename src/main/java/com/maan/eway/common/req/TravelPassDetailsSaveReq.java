package com.maan.eway.common.req;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TravelPassDetailsSaveReq {

	

	
	@JsonProperty("TravelPassDetailsSave")
	private List<TravelPassDetailsSaveListReq> travelPassDetailsSaveListReq;
	

}
