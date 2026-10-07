package com.maan.eway.common.service;

import java.util.List;

import com.maan.eway.common.req.AddRemovedPassengerReq;
import com.maan.eway.common.req.PassengerSaveReq;
import com.maan.eway.common.req.TravelPassDetailsGetAllReq;
import com.maan.eway.common.req.TravelPassDetailsGetReq;
import com.maan.eway.common.req.TravelPassDetailsSaveListReq;
import com.maan.eway.common.req.TravelPassDetailsSaveReq;
import com.maan.eway.common.req.TravelPassValidateReq;
import com.maan.eway.common.res.TravelPassDetailsRes;
import com.maan.eway.common.res.TravelPassHistoryRes;
import com.maan.eway.error.Error;
import com.maan.eway.res.SuccessRes;
import com.maan.eway.res.TravelPassCommonRes;

public interface TravelPassengerDetailsService {

	List<Error> validatepassdetails(List<TravelPassDetailsSaveListReq> reqList);

	SuccessRes savepassdetails(List<TravelPassDetailsSaveListReq> reqList);

	TravelPassDetailsRes getpassdetails(TravelPassDetailsGetReq req);

	SuccessRes deletepassdetails(TravelPassDetailsGetReq req);

	List<TravelPassDetailsRes> getallpassdetails(TravelPassDetailsGetAllReq req);

	List<Error> validatepassListDetails(TravelPassValidateReq req);

	TravelPassCommonRes getpassdetails(TravelPassValidateReq req);

	List<TravelPassHistoryRes> getallpasshistorydetails(TravelPassDetailsGetAllReq req);

	List<Error> validatepassergerList(PassengerSaveReq reqList);

	SuccessRes savepassengerlist(PassengerSaveReq reqList);

	List<TravelPassDetailsRes> getActivePassengers(TravelPassDetailsGetAllReq req);

	List<TravelPassDetailsRes> getRemovedPassengers(TravelPassDetailsGetAllReq req);

	List<Error> validateaddpassergerList(AddRemovedPassengerReq req);

	SuccessRes addRemovedPassengersAgain(AddRemovedPassengerReq req);

	SuccessRes deleteallpassdetails(TravelPassDetailsGetAllReq req);

	List<String> mergepassergerValidation(PassengerSaveReq reqList);

	SuccessRes mergepassengerlist(PassengerSaveReq reqList);

	List<String> addnewpassergerListValidate(PassengerSaveReq reqList);

	SuccessRes addpassengerlist(PassengerSaveReq reqList);

}
