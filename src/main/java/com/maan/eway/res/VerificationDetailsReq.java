package com.maan.eway.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class VerificationDetailsReq {


		@JsonProperty("MotorCategory")
		private String motorCategory ;
		
		@JsonProperty("MotorRegistrationNumber")
		private String motorRegistrationNumber ;
		
		@JsonProperty("MotorChassisNumber")
		private String motorChassisNumber ;

}
