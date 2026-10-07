package com.maan.eway.req;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.eway.bean.CompanyProductMaster;
import com.maan.eway.bean.EserviceBuildingDetails;
import com.maan.eway.bean.EserviceCommonDetails;
import com.maan.eway.bean.EserviceCustomerDetails;
import com.maan.eway.bean.EserviceDriverDetails;
import com.maan.eway.bean.EserviceMotorDetails;
import com.maan.eway.bean.MsAssetDetails;
import com.maan.eway.bean.MsCommonDetails;
import com.maan.eway.bean.MsCustomerDetails;
import com.maan.eway.bean.MsHumanDetails;
import com.maan.eway.bean.ProductSectionMaster;
import com.maan.eway.common.req.TravelGroupInsertReq;
import com.maan.eway.common.res.BuildingSectionRes;

import lombok.Data;

@Data

public class OneTimeTableReq {

	@JsonProperty("RequestReferenceNo")
	private String requestReferenceNo ;
	
	@JsonProperty("VehicleId")
	 private Integer    vehicleId ;
	
	@JsonProperty("LocationId")
	 private Integer    locationId ;
	
	@JsonProperty("RiskId")
	private String riskId;
	
	@JsonProperty("Id")
	 private Integer    id ;
	
	@JsonProperty("CoverId")
	 private Integer    coverId ;
	
	@JsonProperty("MotorDetails")
	private EserviceMotorDetails motorDetails ;
	@JsonProperty("MotorDriverDetails")
	private EserviceDriverDetails motorDriverDetails ;
	
	@JsonProperty("AgencyCode")
	private String agencyCode ;
	@JsonProperty("BranchCode")
	private String branchCode ;
	@JsonProperty("InsuranceId")
	private String insuranceId ;
	@JsonProperty("ProductId")
	private Integer productId ;
	@JsonProperty("SectionId")
	private Integer sectionId ; 
	
	@JsonProperty("SectionIds")
	private List<String> sectionIds ; 
	
	@JsonProperty("GroupDetails")
	private List<TravelGroupInsertReq> groupDetails;
	
	@JsonProperty("SectionList")
	private List<BuildingSectionRes> sectionList ;
	
	@JsonProperty("BuildingDetails")
	private List<EserviceBuildingDetails> eserviceBuilding;
	
	@JsonProperty("CommonDetails")
	private List<EserviceCommonDetails> eserviceCommon;
	
	@JsonProperty("MsAssert")
	private MsAssetDetails  msAssertlist;
	
	@JsonProperty("msHuman")
	private MsHumanDetails  msHuman;
	
	@JsonProperty("msHuman")
	private MsCommonDetails  msCommon;
	
	@JsonProperty("Ecustomer")
	private EserviceCustomerDetails  Ecustomer;
	
	@JsonProperty("ProductSectionMaster")
	private List<ProductSectionMaster> psm;
	
	@JsonProperty("flag")
	private boolean  y = false;
	
	@JsonProperty("companyProduct")
	private CompanyProductMaster  product;
	
	@JsonProperty("MsCustomer")
	private MsCustomerDetails  MsCustomer;
	
	@JsonProperty("OTMSCustomer")
	private Optional<MsCustomerDetails> OTCustome;
	
	
	
	
}
