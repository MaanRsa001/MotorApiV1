package com.maan.eway.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class VerificationDetailsReq {


		@JsonProperty("MotorCategory")
		@XmlElement(name = "MotorCategory")
		private String motorCategory ;
		
		@JsonProperty("MotorRegistrationNumber")
		@XmlElement(name = "MotorRegistrationNumber")
		private String motorRegistrationNumber ;
		
		@JsonProperty("MotorChassisNumber")
		@XmlElement(name = "MotorChassisNumber")
		private String motorChassisNumber ;

}
