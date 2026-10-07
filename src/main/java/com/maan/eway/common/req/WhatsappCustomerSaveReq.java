package com.maan.eway.common.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class WhatsappCustomerSaveReq {
	
	
	@JsonProperty("WhatsappDesc")
    private String     whatsappDesc;
	
	@JsonProperty("WhatsappNo")
    private String     whatsappNo;
	
	@JsonProperty("ProductId")
    private String     productId;
	
	@JsonProperty("ClientName")
    private String     clientName;
	
	@JsonProperty("IdType")
    private String     idType;
	
	@JsonProperty("IdNumber")
    private String     idNumber;
	
	@JsonProperty("Title")
    private String     title;
		

}
