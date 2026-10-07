package com.maan.eway.req.push;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Jacksonized
@XmlRootElement(name="MotorCoverNoteRefReq")

@XmlAccessorType(XmlAccessType.FIELD)
public class MotorCoverNoteRefReq {
 
	@JsonProperty("CoverNoteHdr") 
	@XmlElement(name = "CoverNoteHdr")
	private   CoverNoteHdr coverNoteHdrBean ;
	@JsonProperty("CoverNoteDtl") 
	@XmlElement(name = "CoverNoteDtl")
    private CoverNoteDtl coverNoteDtlBean ;
 
}