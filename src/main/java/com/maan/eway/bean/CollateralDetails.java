package com.maan.eway.bean;

//import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity

@Table(name = "collateral_details")
public class CollateralDetails {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name="SNO")
	    private Long sno;

	    @Column(name="COMPANY_ID")
	    private String companyId;

	    @Column(name="REQUEST_REFERENCE_NO" )
	    private String requestReferenceNo;

	    @Column(name="QUOTE_NO" )
	    private String quoteNo;

	    @Column(name="PRODUCT_ID" )
	    private String productId;

	    @Column(name="PRODUCT_DESC" )
	    private String productDesc;

	    @Column(name="SECTION_ID" )
	    private String sectionId;

	    @Column(name="SECTION_DESC" )
	    private String sectionDesc;

	    @Column(name="COLLATERAL_TYPE_ID" )
	    private String collateralTypeId;

	    @Column(name="COLLATERAL_DESC" )
	    private String collateralDesc;

	    @Column(name="PARAM1" )
	    private String param1;

	    @Column(name="PARAM2" )
	    private String param2;

	    @Column(name="PARAM3" )
	    private String param3;

	    @Column(name="PARAM4" )
	    private String param4;

	    @Column(name="PARAM5" )
	    private String param5;

	    @Column(name="COLLATERL_VALUE")
	    private String collaterlValue;

	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="ENTRY_DATE")
	    private Date entryDate;
	    
	    @Column(name="LOCATION_ID")
	    private String locationId;

	    @Column(name="STATUS" )
	    private String status;

	    @Column(name="REMARKS" )
	    private String remarks;
}
