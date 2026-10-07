package com.maan.eway.common.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class WhatsappPremiumCalcReq	 {

	
	@JsonProperty("CustomerSaveReq")
    private WhatsappCustomerSaveReq     CustomerSaveReq;
	
	@JsonProperty("MotorSaveReq")
    private WhatsappMotorSaveReq    MotorSaveReq;
	
}
