package com.maan.eway.document.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TotalRepairCostRes {

	@JsonProperty("usd")
	private TotalRepairCostUsdRes totalRepairCostUsdRes;

	@JsonProperty("inr")
	private TotalRepairCostInrRes TotalRepairCostInrRes;

}
