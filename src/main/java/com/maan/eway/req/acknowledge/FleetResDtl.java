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
@XmlRootElement(name="FleetResDtl")
@XmlAccessorType(XmlAccessType.FIELD)
public class FleetResDtl {
	
	@JsonProperty("CoverNoteNumber")
	@XmlElement(name = "CoverNoteNumber")
	private String coverNoteNumber;
	
	@JsonProperty("CoverNoteReferenceNumber")
	@XmlElement(name = "CoverNoteReferenceNumber")
    private String coverNoteReferenceNumber;
	
	@JsonProperty("ResponseStatusDesc")
	@XmlElement(name = "ResponseStatusDesc")
    private String responseStatusDesc;
	
	@JsonProperty("StickerNumber")
	@XmlElement(name = "StickerNumber")
    private String stickerNumber;
	
	@JsonProperty("ResponseStatusCode")
	@XmlElement(name = "ResponseStatusCode")
    private String responseStatusCode;
	
	@JsonProperty("FleetEntry")
	@XmlElement(name = "FleetEntry")
    private Integer fleetEntry;
}
