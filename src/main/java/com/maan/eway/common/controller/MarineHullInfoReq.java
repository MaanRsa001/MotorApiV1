package com.maan.eway.common.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class MarineHullInfoReq {
	
	@JsonProperty("RequestReferenceNo")
	private String requestRefNo;

}
