package com.maan.eway.common.req;

import java.math.BigDecimal;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Builder
public class BenefitIllustration {
	@JsonProperty("PolicyYear")
	@SerializedName("PolicyYear")
	private Integer policyYear;
	@JsonProperty("Age")
	@SerializedName("Age")
	private Integer age;
	
	@JsonProperty("Premium")
	@SerializedName("Premium")
	private BigDecimal premium;
	
	@JsonProperty("AdditionalBenefit")
	@SerializedName("AdditionalBenefit")
	private Map<String,BigDecimal> additionalBenefit;
	
	@JsonProperty("SurvivalBenefit")
	@SerializedName("SurvivalBenefit")
	private BigDecimal survivalBenefit;
	@JsonProperty("DeathBenefit")
	@SerializedName("DeathBenefit")
	private DeathBenefit deathBenefit;
	@JsonProperty("Surrender")
	@SerializedName("Surrender")
	private BigDecimal surrender;
	@JsonProperty("MaturityBenefit")
	@SerializedName("MaturityBenefit")
	private BigDecimal maturityBenefit;
	
}