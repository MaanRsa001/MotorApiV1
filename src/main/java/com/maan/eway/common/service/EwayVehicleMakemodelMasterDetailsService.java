package com.maan.eway.common.service;

import java.util.List;

import com.maan.eway.common.req.AdditionalValidationReq;
import com.maan.eway.common.req.BuildingDetailsGetAllReq;
import com.maan.eway.common.req.BuildingDetailsGetReq;
import com.maan.eway.common.req.BuildingDetailsSaveReq;
import com.maan.eway.common.req.EwayVehicleMakeModelGetReq;
import com.maan.eway.common.req.EwayVehicleMakeModelSaveReq;
import com.maan.eway.common.res.BuildingDetailsGetRes;
import com.maan.eway.common.res.EwayVehicleMakeDropdownRes;
import com.maan.eway.common.res.EwayVehicleMakeModelGetRes;
import com.maan.eway.error.Error;
import com.maan.eway.res.SuccessRes;

public interface EwayVehicleMakemodelMasterDetailsService {


	List<EwayVehicleMakeModelGetRes> getVehicleModel(EwayVehicleMakeModelGetReq req);

	List<EwayVehicleMakeModelGetRes> getVehicleTrim(EwayVehicleMakeModelGetReq req);

	List<EwayVehicleMakeDropdownRes> getVehicleMake(EwayVehicleMakeModelGetReq req);

	List<Error> validateVehicleMakeModel(EwayVehicleMakeModelSaveReq req);

	SuccessRes saveVehicleMakeModel(EwayVehicleMakeModelSaveReq req);

	EwayVehicleMakeModelGetRes getByVehicleId(EwayVehicleMakeModelGetReq req);

	List<EwayVehicleMakeModelGetRes> getallMotorMakeModelDetails(EwayVehicleMakeModelGetReq req);



	

}
