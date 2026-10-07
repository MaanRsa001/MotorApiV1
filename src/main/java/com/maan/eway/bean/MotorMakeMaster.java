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
@IdClass(MotorMakeMasterId.class)
@Table(name = "eway_motor_make_master")
public class MotorMakeMaster implements Serializable {
	
	private static final long serialVersionUID = 1L;
	// --- ENTITY PRIMARY KEY

	@Id
	@Column(name = "MAKE_ID", nullable = false)
	private Integer makeId;
	
    @Id
    @Column(name="COMPANY_ID", nullable=false, length=100)
    private String     companyId ;

    @Id
    @Column(name="BRANCH_CODE", nullable=false, length=100)
    private String     branchCode ;

	
    @Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EFFECTIVE_DATE_START", nullable = false)
	private Date effectiveDateStart;

	@Id
	@Column(name = "AMEND_ID", nullable = false)
	private Integer amendId;

	
    @Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EFFECTIVE_DATE_END", nullable = false)
	private Date effectiveDateEnd;

	@Column(name = "MAKE_NAME_EN", length = 100)
	private String makeNameEn;
	
	@Column(name = "MAKE_NAME_LOCAL", length = 100)
	private String makeNameLocal;

	@Column(name = "COLOR_DESC", length = 100)
	private String colorDesc;

	
	@Column(name = "STATUS", length = 10)
	private String status;

    @Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ENTRY_DATE")
	private Date entryDate;

	@Column(name = "REMARKS")
	private String remarks;
	
    @Column(name="CREATED_BY", length=20)
    private String     createdBy ;

    @Column(name="UPDATED_BY", length=20)
    private String     updatedBy ;

    @Column(name="REGULATORY_CODE",  length=20)
    private String     regulatoryCode ;
    
    @Column(name="CORE_APP_CODE",  length=20)
    private String     coreAppCode ;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="UPDATED_DATE")
    private Date       updatedDate ;

}