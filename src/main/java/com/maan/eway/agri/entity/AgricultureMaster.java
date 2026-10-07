package com.maan.eway.agri.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "agriculture_master")
@IdClass(AgricultureMasterId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgricultureMaster {

    @Id
    @Column(name = "SNO")
    private Integer sno;

    @Id
    @Column(name = "COMPANY_ID")
    private Integer companyId;

    @Id
    @Column(name = "PRODUCT_ID")
    private Integer productId;

    @Id
    @Column(name = "AMEND_ID")
    private Integer amendId;

    @Column(name = "PROVINCE_ID")
    private Integer provinceId;

    @Column(name = "PROVINCE_DESC")
    private String provinceDesc;

    @Column(name = "DISTRICT_ID")
    private Integer districtId;

    @Column(name = "DISTRICT_DESC")
    private String districtDesc;

    @Column(name = "AEZ")
    private Integer aez;

    @Column(name = "CROP_ID")
    private Integer cropId;

    @Column(name = "CROP_DESC")
    private String cropDesc;

    @Column(name = "PER_HA_COST")
    private Double perHaCost;

    @Column(name = "SECTION_ID")
    private Integer sectionId;

    @Column(name = "CORE_APP_CODE")
    private String coreAppCode;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "ENTRY_DATE")
    private Date entryDate;

    @Column(name = "EFFECTIVE_DATE_START")
    private Date effectiveDateStart;

    @Column(name = "EFFECTIVE_DATE_END")
    private Date effectiveDateEnd;
    
    @Column(name="REMARKS")
    private String remarks;
    
    @Column(name="YIELD_PRECENTAGE")
    private Integer yieldPercentage;
}