package com.maan.eway.res;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DoubleInsuranceCallObj {
	
	@JsonProperty("DoubleInsurance")
	private List<DoubleInsurance> doubleInsurance;

}
