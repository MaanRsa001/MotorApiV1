package com.maan.eway.common.service;

import java.util.List;


import com.maan.eway.common.req.AdditionalValidationReq;
import com.maan.eway.common.req.BuildingDetailsGetAllReq;
import com.maan.eway.common.req.BuildingDetailsGetReq;
import com.maan.eway.common.req.BuildingDetailsSaveReq;
import com.maan.eway.common.res.BuildingDetailsGetRes;
import com.maan.eway.error.Error;
import com.maan.eway.res.SuccessRes;
import com.maan.eway.res.SuccessRes1;

public interface BuildingDetailsService {



	SuccessRes1 savebuildingDetails(List<BuildingDetailsSaveReq> req);

	List<BuildingDetailsGetRes> getallbuildingDetails(BuildingDetailsGetAllReq req);

	List<BuildingDetailsGetRes> getbuildingDetails(BuildingDetailsGetReq req);

	SuccessRes deleteBuildingDetails(BuildingDetailsGetReq req);

	List<String> additionalInfoVali(AdditionalValidationReq req);

	List<String> validatebuildingDetails(List<BuildingDetailsSaveReq> req);

}
