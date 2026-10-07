package com.maan.eway.req.life;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LifeDetail {
	@JsonProperty("PolicyTerm") 
	@NotEmpty(message = "PolicyTerm is mandatory")
    public String policyTerm;
    @JsonProperty("PremiumPayingTerm")
    @NotEmpty(message = "PremiumPayingTerm is mandatory")
    public String premiumPayingTerm;
    @JsonProperty("PaymentTerm") 
    @NotNull(message = "PaymentTerm is mandatory")
    public Long paymentTerm;
    @JsonProperty("SumInsured") 
    @NotNull(message = "SumInsured is mandatory")
    public BigDecimal sumInsured;
}
