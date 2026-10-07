package com.maan.eway.document.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DamagedPartRes {
	
	@JsonProperty("name")
    private String name;
	
	@JsonProperty("material_type")
    private String materialType;
	
	@JsonProperty("damage_type")
    private String damageType;
	
	@JsonProperty("damage_percentage")
    private int damagePercentage;
	
	@JsonProperty("recommendation")
    private String recommendation;
	
	@JsonProperty("repair_cost_usd")
    private String repairCostUsd;
	
	@JsonProperty("repair_cost_inr_min")
    private Long repairCostInrMin;
	
	@JsonProperty("repair_cost_inr_max")
    private Long repairCostInrMax;


}
