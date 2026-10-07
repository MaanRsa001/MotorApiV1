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
@Table(name = "dmvic_trancation_log")
public class DMVICTrancationLog {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "dmvic_url")
	private String dmvicUrl;

	@Column(name = "request")
	private String request;

	@Column(name = "response")
	private String response;
	
	@Column(name = "KMPDC_regno")
	private String regNo;
	
	@Column(name = "policy_no")
	private String policyNo;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "Entry_Date")
	private Date entryDate;
	
	

}
