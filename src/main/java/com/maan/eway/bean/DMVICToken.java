package com.maan.eway.bean;

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
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "DMVIC_Token")
public class DMVICToken {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "Company_Id")
	private String companyId;

	@Column(name = "Status")
	private String status;

	@Column(name = "Token")
	private String token;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "Entry_Date")
	private Date entryDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "Expire_Date")
	private Date expireDate;

}
