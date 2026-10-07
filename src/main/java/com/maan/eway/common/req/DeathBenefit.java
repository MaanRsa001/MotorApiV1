package com.maan.eway.common.req;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
@Builder
public class DeathBenefit {
	@JsonProperty("SumAssured")
	@SerializedName("SumAssured")
	private BigDecimal sumAssured;
	@JsonProperty("RevisionaryBonus")
	@SerializedName("RevisionaryBonus")
	private BigDecimal revisionaryBonus;
	@JsonProperty("Total")
	@SerializedName("Total")
	private BigDecimal total;
}
