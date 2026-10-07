package com.maan.eway.res;

import com.fasterxml.jackson.annotation.JsonProperty;

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
@XmlRootElement(name="MotorVerificationRes")

@XmlAccessorType(XmlAccessType.FIELD)
public class MotorVerificationRes {

	@JsonProperty("VerificationHdr")
	@XmlElement(name = "VerificationHdr")
	private VerificationHdrRes verificationHdr ;
	
	@JsonProperty("VerificationDtl")
	@XmlElement(name = "VerificationDtl")
	private VerificationHdrDtlsRes VerificationDtl ;
}
