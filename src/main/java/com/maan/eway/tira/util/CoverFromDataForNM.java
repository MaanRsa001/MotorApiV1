package com.maan.eway.tira.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.function.Function;

import com.maan.eway.bean.PolicyCoverData;
import com.maan.eway.req.push.DiscountOffered;
import com.maan.eway.req.push.DiscountsOffered;
import com.maan.eway.req.push.RiskCovered;
import com.maan.eway.req.push.TaxCharged;
import com.maan.eway.req.push.TaxesCharged;

public class CoverFromDataForNM implements Function<PolicyCoverData,RiskCovered> {
	
	private String filterBy;
	
	private List<DiscountOffered> discounts;
	private List<DiscountOffered> loadings;
	private List<TaxCharged> taxes;
	
	public CoverFromDataForNM(String filterBy, List<DiscountOffered> discounts, List<DiscountOffered> loadings, List<TaxCharged> taxes) {
		super();
		this.filterBy = filterBy;
		this.discounts=discounts;
		this.loadings=loadings;
		this.taxes=taxes;
		
	}
	@Override
	public RiskCovered apply(PolicyCoverData t) {
		if(((t.getCoverageType().equalsIgnoreCase(filterBy)) || t.getCoverageType().equalsIgnoreCase("O")) && t.getPremiumAfterDiscountFc().doubleValue()>0D) {
			String pattern =  "#####0.#####" ;
			DecimalFormat decimalFormat = new DecimalFormat(pattern);
			double loading =(loadings!=null && loadings.size()>0)?loadings.stream().mapToDouble(e-> Double.parseDouble(e.getDiscountAmount())).sum():0D;
			RiskCovered r=RiskCovered.builder()
						.discountsOfferedBean(DiscountsOffered.builder().discountOfferedBeanList(discounts).build())
						//.isMulti(null)
						//.premiumAfterDiscount("Y".equals(t.getMinimumPremiumYn())?t.getPremiumExcludedTaxLc().toPlainString(): t.getPremiumAfterDiscountFc().toPlainString())
						.premiumAfterDiscount("Y".equals(t.getMinimumPremiumYn())? t.getPremiumExcludedTaxLc().abs().toPlainString(): t.getPremiumAfterDiscountFc().abs().toPlainString())
					//	.premiumBeforeDiscount("Y".equals(t.getMinimumPremiumYn())?t.getPremiumExcludedTaxLc().abs().toPlainString():t.getPremiumBeforeDiscountFc().abs().add(new BigDecimal(loading)).toPlainString())
						.premiumBeforeDiscount("Y".equals(t.getMinimumPremiumYn())
								? t.getPremiumExcludedTaxLc().abs().toPlainString()
								: t.getPremiumBeforeDiscountFc().abs()
										.add(BigDecimal.valueOf(loading))
										.setScale(2, RoundingMode.HALF_UP)
										.toPlainString())
						.premiumIncludingTax(t.getPremiumIncludedTaxFc().toPlainString())
						//.premiumRate(t.getRegulatoryRate()==null?String.valueOf(t.getRate()/100) : t.getRegulatoryRate().divide(new BigDecimal("100"),3, RoundingMode.HALF_UP).toPlainString())
						.premiumRate("A".equals(t.getCalcType())?"0":decimalFormat.format((Double) t.getActualRate().doubleValue()/100))
						.riskCode(t.getRegulatoryCode())
						.sumInsured(t.getRegulatorySuminsured()==null?t.getSumInsured().toPlainString():t.getRegulatorySuminsured().toPlainString())
						.sumInsuredEquivalent(t.getRegulatorySuminsured().toPlainString())
						.taxesChargedBean(TaxesCharged.builder().taxChargedBean(taxes).build())
						//.taxChargedBean(taxes)
						.premiumExcludingTaxEquivalent(t.getPremiumExcludedTaxLc().toPlainString())
						.build();
			
			return r;
		}
		return null;
	}

}
