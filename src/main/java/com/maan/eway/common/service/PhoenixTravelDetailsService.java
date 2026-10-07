package com.maan.eway.common.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.maan.eway.bean.BranchMaster;
import com.maan.eway.bean.BrokerCommissionDetails;
import com.maan.eway.bean.CompanyProductMaster;
import com.maan.eway.bean.CountryMaster;
import com.maan.eway.bean.EserviceSectionDetails;
import com.maan.eway.bean.EserviceTravelDetails;
import com.maan.eway.bean.EserviceTravelGroupDetails;
import com.maan.eway.bean.InsuranceCompanyMaster;
import com.maan.eway.bean.ListItemValue;
import com.maan.eway.bean.LoginBranchMaster;
import com.maan.eway.bean.LoginMaster;
import com.maan.eway.bean.LoginUserInfo;
import com.maan.eway.bean.ProductGroupMaster;
import com.maan.eway.bean.ProductSectionMaster;
import com.maan.eway.bean.TravelPassengerDetails;
import com.maan.eway.common.req.EserviceSaveRes;
import com.maan.eway.common.req.EserviceTravelDeleteReq;
import com.maan.eway.common.req.EserviceTravelGetAllReq;
import com.maan.eway.common.req.EserviceTravelGetReq;
import com.maan.eway.common.req.EserviceTravelSaveReq;
import com.maan.eway.common.req.SequenceGenerateReq;
import com.maan.eway.common.req.TravelGroupGetRes;
import com.maan.eway.common.req.TravelGroupInsertReq;
import com.maan.eway.common.res.EserviceTravelGetRes;
import com.maan.eway.common.res.PremiaTiraReq;
import com.maan.eway.common.res.PremiaTiraRes;
import com.maan.eway.common.res.SuccessRes;
import com.maan.eway.common.service.impl.EserviceMotorDetailsServiceImpl;
import com.maan.eway.common.service.impl.EserviceTravelDetailsServiceImpl;
import com.maan.eway.common.service.impl.FetchErrorDescServiceImpl;
import com.maan.eway.common.service.impl.GenerateSeqNoServiceImpl;
import com.maan.eway.common.service.impl.PremiaBrokerServiceImpl;
import com.maan.eway.common.service.impl.TrackingDetailsServiceImpl;
import com.maan.eway.repository.EServiceSectionDetailsRepository;
import com.maan.eway.repository.EserviceTravelDetailsRepository;
import com.maan.eway.repository.EserviceTravelGroupDetailsRepository;
import com.maan.eway.repository.InsuranceCompanyMasterRepository;
import com.maan.eway.repository.LoginBranchMasterRepository;
import com.maan.eway.repository.LoginMasterRepository;
import com.maan.eway.repository.LoginUserInfoRepository;
import com.maan.eway.repository.ProductMasterRepository;
import com.maan.eway.repository.SectionMasterRepository;
import com.maan.eway.repository.TravelPassengerDetailsRepository;
import com.maan.eway.req.OneTimeTableReq;
import com.maan.eway.res.OneTimeTableRes;
import com.maan.eway.service.OneTimeService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

public interface PhoenixTravelDetailsService {


	EserviceTravelDetails create(EserviceTravelDetails d);
	EserviceTravelDetails update(EserviceTravelDetails d);
	//EserviceTravelDetails getOne(long id) ;
	 List<EserviceTravelDetails> getAll();
	long getTotal();
	//boolean delete(long id);
	List<String> validateTravelDetails(EserviceTravelSaveReq req);
	EserviceSaveRes saveTravelDetails(EserviceTravelSaveReq req);
	EserviceTravelGetRes getTravelDetails(EserviceTravelGetReq req);
	SuccessRes deleteTravelDetails(EserviceTravelDeleteReq req);
	List<EserviceTravelGetRes> getallTravelDetails(EserviceTravelGetAllReq req);

}
