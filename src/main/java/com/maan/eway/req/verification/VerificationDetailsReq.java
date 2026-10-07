package com.maan.eway.req.verification;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class VerificationDetailsReq {
/*
 * <CoverNoteReferenceNumber>42424-246767-65768</CoverNoteReferenceNumber>
         <StickerNumber>13143-14145-12412</StickerNumber>
         <MotorRegistrationNumber>T233SQA</MotorRegistrationNumber>
         <MotorChassisNumber>4353646</MotorChassisNumber>
 */

	@JsonProperty("CoverNoteReferenceNumber")
	@XmlElement(name = "CoverNoteReferenceNumber")
	private String coverNoteReferenceNumber ;
	
	@JsonProperty("StickerNumber")
	@XmlElement(name = "StickerNumber")
	private String stickerNumber ;
	
	@JsonProperty("MotorRegistrationNumber")
	@XmlElement(name = "MotorRegistrationNumber")
	private String motorRegistrationNumber ;
	
	@JsonProperty("MotorChassisNumber")
	@XmlElement(name = "MotorChassisNumber")
	private String motorChassisNumber ;
}
