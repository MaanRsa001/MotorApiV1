package com.maan.eway.bean;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.*;
import jakarta.persistence.*;
import jakarta.persistence.*;
import jakarta.persistence.*;
import jakarta.persistence.*;
import jakarta.persistence.*;
import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(MedMalLiabilityMasterId.class)
@Table(name = "medmal_liability_master")
public class MedMalLiabilityMaster implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// --- ENTITY PRIMARY KEY

	@Id
	@Column(name = "S_NO", nullable = false)
	private Integer sNo;
	
	@Id
	@Column(name = "AOO_ID", nullable = false)
	private Integer aooId;

	@Id
	@Column(name = "AMEND_ID", nullable = false)
	private Integer amendId;

	@Id
	@Column(name = "COMPANY_ID", nullable = false, length = 20)
	private String companyId;
	
	// --- ENTITY DATA FIELDS
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EFFECTIVE_DATE_START", nullable = false)
	private Date effectiveDateStart;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EFFECTIVE_DATE_END", nullable = false)
	private Date effectiveDateEnd;

	@Column(name = "AOO")
	private Integer aoo;

	@Column(name = "AGG")
	private Integer agg;
	
	@Column(name = "STATUS", length = 10)
	private String status;

//	@Temporal(TemporalType.TIMESTAMP)
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale = "en-IN", timezone = "Asia/Calcutta")
//	@Column(name = "ENTRY_DATE")
//	private Date entryDate;
//
//	@Column(name = "REMARKS", length = 100)
//	private String remarks;
//
//
//	@Column(name = "UPDATED_BY", length = 100)
//	private String updatedBy;
//	
//	@Column(name = "CREATED_BY", length = 100)
//	private String createdBy;
//	
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "UPDATED_DATE")
//	private Date updatedDate;
//
//    @Column(name="REGULATORY_CODE",  length=20)
//    private String     regulatoryCode ;
	
   
	
}