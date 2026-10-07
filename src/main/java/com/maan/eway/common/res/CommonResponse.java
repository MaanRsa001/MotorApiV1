package com.maan.eway.common.res;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.maan.eway.error.Error;

import lombok.Data;

@Data
public class CommonResponse {
	
	
	@SerializedName("Message")
	@JsonProperty("Message")
	private String message;
	
	@SerializedName("error")
	@JsonProperty("error")	
	private String error;
	
	@SerializedName("IsError")
	@JsonProperty("IsError")	
	private Boolean isError;
	
	@SerializedName("ErrorMessage")
	@JsonProperty("ErrorMessage")
	private List<Error> errorMessage;

	//Dynamic
	@SerializedName("Result")
	@JsonProperty("Result")
	private List<Object> commonResponse;
	
	@SerializedName("ErroCode")
	@JsonProperty("ErroCode")
	private String erroCode;


}
