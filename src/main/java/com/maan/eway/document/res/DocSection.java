package com.maan.eway.document.res;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.eway.bean.DamagePartsDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocSection {
	
	@JsonProperty("SectionId")
	private String sectionId;
	
	@JsonProperty("DocumentId")
	private String documentId;
	
	@JsonProperty("DocumentIdName")
	private String documentIdName;
	
	@JsonProperty("DocumentType")
	private String DocumentType;
	
	@JsonProperty("DocumentTypeName")
	private String documentTypeName;
	

	@JsonProperty("DocumentName")
	private String documentName;

	@JsonProperty("DamagePartsDetails")
	private List<DamagePartsDetails> damagePartsDetailsList;

}
