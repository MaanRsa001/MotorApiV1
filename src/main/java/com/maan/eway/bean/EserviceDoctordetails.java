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
@Table(name = "eservice_doctor_details")
public class EserviceDoctordetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "reg_no")
	private String regNo;

	@Column(name = "sub_specialty")
	private String subSpecialty;

	@Column(name = "qualifications")
	private String qualifications;

	@Column(name = "specialty")
	private String specialty;

	@Column(name = "gender")
	private String gender;

	@Column(name = "cadre")
	private String cadre;

	@Column(name = "full_names")
	private String fullName;

	@Column(name = "email")
	private String email;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "entry_date")
	private Date entryDate;
}
