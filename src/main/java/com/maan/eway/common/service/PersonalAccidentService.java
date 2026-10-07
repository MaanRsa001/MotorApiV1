package com.maan.eway.common.service;

import java.util.List;

import com.maan.eway.common.req.PersonalAccidentGetAllReq;
import com.maan.eway.common.req.PersonalAccidentGetReq;
import com.maan.eway.common.req.PersonalAccidentSaveReq;
import com.maan.eway.common.res.PersonalAccidentGetAllRes;
import com.maan.eway.common.res.PersonalAccidentGetRes;
import com.maan.eway.error.Error;
import com.maan.eway.res.SuccessRes;

public interface PersonalAccidentService {

	List<Error> validatepersonalaccident(PersonalAccidentSaveReq req);

	SuccessRes savepersonalaccident(PersonalAccidentSaveReq req);

	PersonalAccidentGetRes getpersonalaccident(PersonalAccidentGetReq req);

	PersonalAccidentGetAllRes getallpersonalaccident(PersonalAccidentGetAllReq req);

	SuccessRes savepersonalaccidentInfo(PersonalAccidentSaveReq req);

	

}
