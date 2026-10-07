package com.maan.eway.document.res;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DocumentImageRes {

	@JsonProperty("car_details")
	private CarDetailsRes carDetails;

	@JsonProperty("damaged_parts")
	private List<DamagedPartRes> damagedPartRes;

	@JsonProperty("total_repair_cost_usd")
	private String totalRepairCostUsd;

	@JsonProperty("total_repair_cost_inr_min")
	private int totalRepairCostInrMin;

	@JsonProperty("total_repair_cost_inr_max")
	private int totalRepairCostInrMax;

	@JsonProperty("total_repair_cost")
	private TotalRepairCostRes totalRepairCost;

	@JsonProperty("analysis_scope")
	private String analysisScope;

}
