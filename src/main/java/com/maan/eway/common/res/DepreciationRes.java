package com.maan.eway.common.res;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class DepreciationRes {
	
	
	private BigDecimal inflationActualSumInsured;
	
	private BigDecimal inflationPercentage;
	
	private Date purchaseDate;
	
	private BigDecimal inflationSiWithPercentage;
	
	private Integer yearsPassed;
	
	private BigDecimal inflationSumInsured;

}
