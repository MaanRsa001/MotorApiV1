package com.maan.eway.bean;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fire_risk_info")
@IdClass(FireRiskInfoId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FireRiskInfo {

	@Id
	@NotBlank(message = "RequestReferenceNo is required")
	@Column(name = "Request_Reference_No", length = 100, nullable = false)
	private String requestReferenceNo;

	@Column(name = "Quote_No", length = 100)
	private String quoteNo;

	@Id
	@NotNull(message = "ProductId is required")
	@Column(name = "Product_Id")
	private Integer productId;

	@Id
	@NotNull(message = "Section is required")
	@Column(name = "Section_Id")
	private Integer sectionId;

	@NotNull(message = "CompanyId is required")
	@Column(name = "Company_Id")
	private Integer companyId;

	@Id
	@NotNull(message = "LocationId is required")
	@Column(name = "Location_id", nullable = false)
	private Integer locationId;

	@Column(name = "Location_Name", length = 100)
	private String locationName;

	@Column(name = "Type_of_Industry")
	private Integer typeOfIndustry;

	@Column(name = "Type_of_Industry_Desc", length = 100)
	private String typeOfIndustryDesc;

	@Column(name = "Cladding_Type")
	private Integer claddingType;

	@Column(name = "Cladding_Type_Desc", length = 100)
	private String claddingTypeDesc;

	@Column(name = "Construction_Type", nullable = false)
	private Integer constructionType;

	@Column(name = "Construction_Type_Desc", length = 100)
	private String constructionTypeDesc;

	@Column(name = "Occupancy_Type")
	private Integer occupancyType;

	@Column(name = "Occupancy_Type_Desc", length = 100)
	private String occupancyTypeDesc;

	@Column(name = "No_of_Floors", length = 100)
	private String noOfFloors;

	@Column(name = "Age_of_Building", length = 100)
	private String ageOfBuilding;

	@Column(name = "Sprinkler_YN", length = 1)
	private String sprinklerYn;

	@Column(name = "Extinguisher_YN", length = 1)
	private String extinguisherYn;

	@Column(name = "Hydrant_YN", length = 1)
	private String hydrantYn;

	@Column(name = "Watchman_YN", length = 1)
	private String watchmanYn;

	@Column(name = "Surveyed_YN", length = 1)
	private String surveyedYn;

	@Column(name = "Risk_Longitude", length = 100)
	private String riskLongitude;

	@Column(name = "Risk_Latitude", length = 100)
	private String riskLatitude;

	@Column(name = "GPS_Location", length = 100)
	private String gpsLocation;

	@Column(name = "Risk_Full_address", length = 100)
	private String riskFullAddress;

	@Column(name = "PARAM1", length = 100)
	private String param1;

	@Column(name = "PARAM2", length = 100)
	private String param2;

	@Column(name = "PARAM3", length = 100)
	private String param3;

	@Column(name = "PARAM4", length = 100)
	private String param4;

	@Column(name = "PARAM5", length = 100)
	private String param5;

	@Column(name = "Entry_Date")
	private LocalDate entryDate;

	@Column(name = "Start_Date")
	private LocalDate startDate;

	@Column(name = "End_Date")
	private LocalDate endDate;

	@Column(name = "Status")
	private String status;
}
