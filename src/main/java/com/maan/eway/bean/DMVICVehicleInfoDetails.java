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
@Table(name = "dmvic_vehicle_info_details")
public class DMVICVehicleInfoDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "registration_number")
	private String registrationNumber;

	@Column(name = "chassis_number")
	private String chassisNumber;

	@Column(name = "engine_number")
	private String engineNumber;

	@Column(name = "make")
	private String make;

	@Column(name = "model")
	private String model;

	@Column(name = "type_of_body")
	private String typeOfBody;

	@Column(name = "year_of_manufacture")
	private String yearOfManufacture;

	@Column(name = "carrying_capacity")
	private Integer carryingCapacity;

	@Column(name = "policy_number")
	private String policyNumber;

	@Column(name = "type_of_cover")
	private String typeOfCover;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "cover_start_date")
	private Date coverStartDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "cover_end_date")
	private Date coverEndDate;

	@Column(name = "member_company")
	private String memberCompany;

}
