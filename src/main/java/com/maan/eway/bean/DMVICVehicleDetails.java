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
@Table(name = "DMVIC_Vehicle_details")
public class DMVICVehicleDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "policy_start_date")
	private Date policyStartDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "policy_end_date")
	private Date policyEndDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "cover_end_date")
	private Date coverEndDate;

	@Column(name = "insurance_certificate_no")
	private String insuranceCertificateNo;

	@Column(name = "member_company_name")
	private String memberCompanyName;

	@Column(name = "registration_number")
	private String registrationNumber;

	@Column(name = "chassis_number")
	private String chassisNumber;

}

