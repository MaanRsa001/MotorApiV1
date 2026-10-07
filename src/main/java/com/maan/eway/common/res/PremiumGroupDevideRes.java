package com.maan.eway.common.res;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class PremiumGroupDevideRes {

    //--- ENTITY PRIMARY KEY 
    private String     quoteNo ;

   private String     requestReferenceNo ;

   private Integer    vehicleId ;

    private Integer    productId ;

    private Integer    individualId ;
  
    private Integer    sectionId ;

    private Integer    coverId ;

    private String     subCoverYn ;

    private Integer    subCoverId ;

    private Integer    discLoadId ;

    private Integer    taxId ;
    
    private Integer    discountCoverId;
     
    private BigDecimal     endtCount ;

    private String     cdRefno ;

    private String     vdRefno ;

    private String     msRefno ;

    private String     companyId ;

      private String     coverName ;

    private String     coverDesc ;

    private String     subCoverName ;

    private String     subCoverDesc ;

    private String     calcType ;

    private BigDecimal     minimumPremium ;

    private BigDecimal     sumInsured ;

    private BigDecimal     rate ;

    private String     currency ;

    private BigDecimal     exchangeRate ;

    private BigDecimal     premiumBeforeDiscountFc ;

    private BigDecimal     premiumBeforeDiscountLc ;

    private BigDecimal     premiumAfterDiscountFc ;

    private BigDecimal     premiumAfterDiscountLc ;

    private BigDecimal     premiumExcludedTaxFc ;

    private BigDecimal     premiumExcludedTaxLc ;

    private BigDecimal     premiumIncludedTaxFc ;

    private BigDecimal     premiumIncludedTaxLc ;

    private BigDecimal     factorTypeId ;

    private String     dependentCoverYn ;

    private Integer    dependentCoverId ;

    private String     coverageType ;

    private String     isSelected ;

    private Date       entryDate ;

    private String     status ;

    private String     createdBy ;

    private BigDecimal     taxRate ;

    private BigDecimal     taxAmount ;

    private String     taxDesc ;

    private String     taxCalcType ;

    private String     isTaxExtempted ;

    private String     taxExemptType ;

    private String     taxExemptCode ;

    private BigDecimal     maxLodingAmount ;

    private String     isReferral ;

    private String     referralDescription ;

    private String     regulatoryCode ;


    private BigDecimal     excessAmount ;
    
    private BigDecimal     excessPercent ;
    
    private String     excessDesc ;
    
    private BigDecimal     actualRate ;
    
    
    private BigDecimal     regulSumInsured ;
    //--- ENTITY LINKS ( RELATIONSHIP )


    private String     coverBasedOn ;

    private String       multiSelectYn;

    private String    minimumPremiumYn ;
  
    private String    proRataYn ;
    
    private BigDecimal    proRataPercent ;
    
    private Date       coverPeriodFrom;
    
    private Date       coverPeriodTo;
    
    private BigDecimal    noOfDays;
 
    private String    policyNo ;
    //--- ENTITY LINKS ( RELATIONSHIP )

    private BigDecimal     diffPremiumIncludedTaxLc ;

    private BigDecimal     diffPremiumIncludedTaxFc ;
    
    private BigDecimal    regulatoryRate ;
    
    private BigDecimal    regulatorySuminsured ;

    private BigDecimal     coverageLimit ;
    
    private String originalPolicyNo ;

}
