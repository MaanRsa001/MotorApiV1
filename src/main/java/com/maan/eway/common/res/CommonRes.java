package com.maan.eway.common.res;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.maan.eway.common.req.NonMotorLocationReq;
import com.maan.eway.error.Error;
import lombok.Data;

@Data
public class CommonRes {
	@SerializedName("Message")
	@JsonProperty("Message")
	private String message;
	@SerializedName("IsError")
	@JsonProperty("IsError")	
	private Boolean isError;
	@SerializedName("ErrorMessage")
	@JsonProperty("ErrorMessage")
	private List<Error> errorMessage;

	@JsonProperty("LocationList")
	private List<NonMotorLocationReq> locationList;
	
	//Dynamic
	@SerializedName("Result")
	@JsonProperty("Result")
	private Object commonResponse;
	
	@JsonProperty("CalcResponse")
	private Object calcResponse;
	
	
	
	@SerializedName("ErroCode")
	@JsonProperty("ErroCode")
	private int erroCode;

	

/*	@JsonProperty("AdditionalData")
	private DefaultAllResponse defaultValue; */
}
