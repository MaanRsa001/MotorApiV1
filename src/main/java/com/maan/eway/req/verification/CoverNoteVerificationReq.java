package com.maan.eway.req.verification;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.eway.req.VerificationHdrReq;

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
@XmlRootElement(name="CoverNoteVerificationReq")

@XmlAccessorType(XmlAccessType.FIELD)
public class CoverNoteVerificationReq {

	@JsonProperty("VerificationHdr")
	@XmlElement(name = "VerificationHdr")
	private VerificationHdrReq verificationHdr ;
	
	@JsonProperty("VerificationDtl")
	@XmlElement(name = "VerificationDtl")
	private VerificationDetailsReq verificationDtl ;
}
