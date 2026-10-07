package com.maan.eway.agri.service;

import java.util.List;

import com.maan.eway.agri.req.AgricultureMasterReq;
import com.maan.eway.agri.req.agriGetreq;
import com.maan.eway.agri.req.cropDropDownReq;
import com.maan.eway.agri.res.AgricultureMasterRes;
import com.maan.eway.agri.res.CropDropdownRes;
import com.maan.eway.agri.res.SuccessResponse;
import com.maan.eway.agri.res.aezResponse;
import com.maan.eway.agri.res.cropDropRes;

public interface AgricultureMasterService {

	SuccessResponse saveorupdate(AgricultureMasterReq req);

	List<AgricultureMasterRes> getAll(Integer companyId, Integer productId);

	AgricultureMasterRes getById(agriGetreq req);

	List<aezResponse> getAllAez(Integer aez);

	List<CropDropdownRes> getAllUniqueCropIdAndDesc();

	List<cropDropRes> getCropDrop(cropDropDownReq req);

	

}
