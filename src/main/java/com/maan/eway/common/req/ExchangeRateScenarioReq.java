package com.maan.eway.common.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ExchangeRateScenarioReq {
	
	@JsonProperty("OldCurrency")
    private String  currency;
	
	@JsonProperty("OldExchangeRate")
    private String  exchangeRate;

	@JsonProperty("OldWindScreenSumInsured")
    private String    windScreenSumInsured ;
	
	@JsonProperty("OldAcccessoriesSumInsured")
    private String     acccessoriesSumInsured ;

	@JsonProperty("OldSumInsured")
    private String     sumInsured   ;

	@JsonProperty("OldTppdIncreaeLimit")
    private String     tppdIncreaeLimit ;
}
