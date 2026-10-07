package com.maan.eway.req.fleet;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.eway.req.push.CoverNoteAddons;
import com.maan.eway.req.push.MotorDtl;
import com.maan.eway.req.push.RisksCovered;
import com.maan.eway.req.push.SubjectMattersCovered;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Jacksonized
@Builder
@XmlAccessorType(XmlAccessType.FIELD)
public class FleetDtl {
	@JsonProperty("FleetEntry") 
	@XmlElement(name = "FleetEntry")
	public Integer fleetEntry;
	
	@JsonProperty("CoverNoteNumber") 
	@XmlElement(name = "CoverNoteNumber")
	private String coverNoteNumber;
	

	@JsonProperty("PrevCoverNoteReferenceNumber") 
	@XmlElement(name = "PrevCoverNoteReferenceNumber")
	private String prevCoverNoteReferenceNumber;
	
	@JsonProperty("CoverNoteDesc") 
	@XmlElement(name = "CoverNoteDesc")
	private String coverNoteDesc;


	@JsonProperty("OperativeClause") 
	@XmlElement(name = "OperativeClause")
	private String operativeClause;

	@JsonProperty("EndorsementType") 
	@XmlElement(name = "EndorsementType")
	private String endorsementType;
	
	@XmlElement(name = "EndorsementReason")
	@JsonProperty("EndorsementReason") 
	private String endorsementReason;
	
	@JsonProperty("EndorsementPremiumEarned") 
	@XmlElement(name = "EndorsementPremiumEarned")
	private String endorsementPremiumEarned;
	
	@JsonProperty("RisksCovered") 
	@XmlElement(name = "RisksCovered")
	private RisksCovered risksCoveredBean ;

	@JsonProperty("SubjectMattersCovered") 
	@XmlElement(name = "SubjectMattersCovered")
	private  SubjectMattersCovered subjectMattersCoveredBean ;
	
	@JsonProperty("CoverNoteAddons") 
	@XmlElement(name = "CoverNoteAddons")
	private CoverNoteAddons coverNoteAddonsBean ;
	
	@JsonProperty("MotorDtl") 
	@XmlElement(name = "MotorDtl")
	private MotorDtl motorDtlBean ;
}
