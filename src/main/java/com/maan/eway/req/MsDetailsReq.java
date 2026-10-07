package com.maan.eway.req;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MsDetailsReq {

	@JsonProperty("MotorVerificationReq")
	private MotorVerificationReq motorVerificationReq ;
	
	@JsonProperty("MsgSignature")
	private String msgSignature ;

}
