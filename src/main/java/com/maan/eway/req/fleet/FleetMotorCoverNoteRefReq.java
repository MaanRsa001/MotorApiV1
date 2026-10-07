package com.maan.eway.req.fleet;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.eway.req.push.CoverNoteHdr;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
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
//@XmlSeeAlso({FleetMotorCoverNoteRefReq.class})
public class FleetMotorCoverNoteRefReq implements Serializable  {

	@JsonProperty("CoverNoteHdr") 
	@XmlElement(name = "CoverNoteHdr")
	private   CoverNoteHdr coverNoteHdrBean ;
	
	@JsonProperty("CoverNoteDtl") 
	@XmlElement(name = "CoverNoteDtl")
	private FleetCoverNoteDtl coverNoteDtlBean;
}
