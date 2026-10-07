package com.maan.eway.tira.bean;

import java.util.List;

import com.maan.eway.bean.CompanyProductMaster;
import com.maan.eway.bean.EndtTypeMaster;
import com.maan.eway.bean.HomePositionMaster;
import com.maan.eway.bean.InsuranceCompanyMaster;
import com.maan.eway.bean.ListItemValue;
import com.maan.eway.bean.LoginMaster;
import com.maan.eway.bean.LoginUserInfo;
import com.maan.eway.bean.MotorDataDetails;
import com.maan.eway.bean.MotorVehicleInfo;
import com.maan.eway.bean.PersonalInfo;
import com.maan.eway.bean.PolicyCoverData;
import com.maan.eway.bean.ProductSectionMaster;
import com.maan.eway.bean.SectionDataDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class MaansarovarToTira {

	private InsuranceCompanyMaster company;
	private CompanyProductMaster product;
	private LoginMaster login;
	private LoginUserInfo broker;
	private LoginUserInfo uw;
	private HomePositionMaster policy;
	private List<PolicyCoverData> covers;
	private ListItemValue ltPayment;
	private PersonalInfo customerInfo;
	private MotorVehicleInfo vehicleInfo;
	private ProductSectionMaster section;
	private EndtTypeMaster endt;
	private MotorDataDetails mdd;
	private List<SectionDataDetails> sectionData;
}
