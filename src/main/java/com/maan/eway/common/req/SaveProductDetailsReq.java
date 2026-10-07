package com.maan.eway.common.req;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SaveProductDetailsReq {
	
	@JsonProperty("ProductEmployeeSaveReq")
    private List<ProductEmployeeSaveReq>     productEmployeeSaveReq ;
	
	@JsonProperty("QuoteNo")
    private String  quoteNo;
	
	@JsonProperty("SectionId")
    private String  sectionId;
	
	@JsonProperty("Createdby")
    private String     createdBy    ;
		
	@JsonProperty("InsuranceId")
    private String     insuranceId;
	
	@JsonProperty("ProductId")
    private String     productId;
	
	@JsonProperty("SaveOrSubmit")
    private String     saveOrSubmit;
}
