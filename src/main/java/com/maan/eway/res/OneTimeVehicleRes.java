package com.maan.eway.res;

import com.maan.eway.bean.MsAssetDetails;
import com.maan.eway.bean.MsVehicleDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OneTimeVehicleRes {

	private String vdRefNo ;
	private String sectionId ;
	private String agencyCode  ;
	private String branchCode ;
	private String productId ;
	private String companyId ;
	private String vehicleId ;
	private String ddRefNo ;
	private String locationId ;
	private String coverid;
	private String param1;
	private String param2;
	private String param3;
	private String param4;
	private String param5;
	private String param6;
	private String param7;
	private String param8;
	private String param9;
	private String param10;
	

	private MsVehicleDetails msVehicleDetails ;
}
