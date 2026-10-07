package com.maan.eway.req.acknowledge;


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
@XmlRootElement(name="CoverNoteRefRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class CoverNoteRefRes {
	
	@JsonProperty("ResponseId")
	@XmlElement(name = "ResponseId")
	private String responseId;
		
	@JsonProperty("RequestId")
	@XmlElement(name = "RequestId")
	private String requestId;
	
	@JsonProperty("CoverNoteReferenceNumber")
	@XmlElement(name = "CoverNoteReferenceNumber")
	private String coverNoteReferenceNumber;
	
/*	@JsonProperty("StickerNumber")
	@XmlElement(name = "StickerNumber")
	private String stickerNumber;
	*/
	@JsonProperty("ResponseStatusCode")
	@XmlElement(name = "ResponseStatusCode")
	private String responseStatusCode;
	
	@JsonProperty("ResponseStatusDesc")
	@XmlElement(name = "ResponseStatusDesc")
	private String responseStatusDesc;
	
	
}
