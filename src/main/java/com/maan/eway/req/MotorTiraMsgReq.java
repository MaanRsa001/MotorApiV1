package com.maan.eway.req;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MotorTiraMsgReq {

	@JsonProperty("MotorVerificationReq")
	private MotorVerificationReq motorVerificationReq ;
	
	@JsonProperty("MsgSignature")
	private String msgSignature ;

}
