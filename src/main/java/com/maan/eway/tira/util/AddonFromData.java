package com.maan.eway.tira.util;

import java.util.List;
import java.util.function.Function;

import com.maan.eway.bean.PolicyCoverData;
import com.maan.eway.req.push.CoverNoteAddon;
import com.maan.eway.req.push.TaxCharged;
import com.maan.eway.req.push.TaxesCharged;

public class AddonFromData  implements Function<PolicyCoverData,CoverNoteAddon>{
	
	private List<TaxCharged> taxes;
	

	public AddonFromData(List<TaxCharged> taxes) {
		super();
		this.taxes = taxes;
	}


	@Override
	public CoverNoteAddon apply(PolicyCoverData t) {
			try {
				if(t.getCoverageType().equalsIgnoreCase("O") && t.getPremiumAfterDiscountFc().doubleValue()>0D) {
				CoverNoteAddon a=CoverNoteAddon.builder()
						.addonAmount(t.getPremiumExcludedTaxFc().toString())
						.addonDesc(t.getCoverDesc())
						.addonPremiumRate("A".equals(t.getCalcType())?"0":t.getRate().toString())
						.addonReference(null)
						.premiumExcludingTax(t.getPremiumExcludedTaxFc().toString())
						.premiumExcludingTaxEquivalent(t.getPremiumExcludedTaxLc().toString())
						.premiumIncludingTax(t.getPremiumIncludedTaxFc().toString())
						.taxesChargedBean(TaxesCharged.builder().taxChargedBean(taxes).build())						
						.build();
				return a;
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
 		return null;
	}

}
