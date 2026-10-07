package com.maan.eway.common.service.impl;



import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.maan.eway.bean.EwayVehicleMakeModelMaster;
import com.maan.eway.bean.ListItemValue;
import com.maan.eway.common.req.EwayVehicleMakeModelGetReq;
import com.maan.eway.common.req.EwayVehicleMakeModelSaveReq;
import com.maan.eway.common.res.EwayVehicleMakeDropdownRes;
import com.maan.eway.common.res.EwayVehicleMakeModelGetRes;
import com.maan.eway.common.service.EwayVehicleMakemodelMasterDetailsService;
import com.maan.eway.error.Error;
import com.maan.eway.repository.EwayVehicleMakeModelDetailsRepository;
import com.maan.eway.res.SuccessRes;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;




@Service 
@Transactional
public class EwayVehicleMakeModelMasterServiceImpl  implements EwayVehicleMakemodelMasterDetailsService{
	
	private Logger log=LogManager.getLogger(EwayVehicleMakeModelMasterServiceImpl.class);
	
	@PersistenceContext
	private EntityManager em;

	Gson json = new Gson();
	
	@Autowired
	private EwayVehicleMakeModelDetailsRepository repo ;
	ModelMapper mapper = new ModelMapper();
	
	//Model
	@Override
	public List<EwayVehicleMakeModelGetRes> getVehicleModel(EwayVehicleMakeModelGetReq req) {
		List<EwayVehicleMakeModelGetRes> resList = new ArrayList<EwayVehicleMakeModelGetRes>();
		try {
//			List<EwayVehicleMakeModelMaster> list=repo.findByMakeOrderByMakeDesc(req.getMake());
			Date today = new Date();
			Calendar cal = new GregorianCalendar();
			cal.setTime(today);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 1);
			today = cal.getTime();
			cal.set(Calendar.HOUR_OF_DAY, 1);
			cal.set(Calendar.MINUTE, 1);
			Date todayEnd = cal.getTime();
			
			// Criteria
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<EwayVehicleMakeModelMaster> query = cb.createQuery(EwayVehicleMakeModelMaster.class);
			List<EwayVehicleMakeModelMaster> list = new ArrayList<EwayVehicleMakeModelMaster>();

			// Find All
			Root<EwayVehicleMakeModelMaster> c = query.from(EwayVehicleMakeModelMaster.class);

			// Select
			query.select(c);

			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(c.get("modelGroup")));

			// Effective Date Start Max Filter
			Subquery<Date> effectiveDate = query.subquery(Date.class);
			Root<EwayVehicleMakeModelMaster> ocpm1 = effectiveDate.from(EwayVehicleMakeModelMaster.class);
			effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart").as(Date.class)));
			jakarta.persistence.criteria.Predicate a1 = cb.equal(c.get("vehicleid"), ocpm1.get("vehicleid"));
			jakarta.persistence.criteria.Predicate a2 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
			effectiveDate.where(a1, a2);
			
			// Effective Date End Max Filter
			Subquery<Date> effectiveDate2 = query.subquery(Date.class);
			Root<EwayVehicleMakeModelMaster> ocpm2 = effectiveDate2.from(EwayVehicleMakeModelMaster.class);
			effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd").as(Date.class)));
			jakarta.persistence.criteria.Predicate a3 = cb.equal(c.get("vehicleid"), ocpm2.get("vehicleid"));
			jakarta.persistence.criteria.Predicate a4 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
			effectiveDate2.where(a3,a4);

			// Where
			jakarta.persistence.criteria.Predicate n1 = cb.equal(c.get("status"), "Y");
			jakarta.persistence.criteria.Predicate n2 = cb.equal(c.get("effectiveDateStart"), effectiveDate);
			jakarta.persistence.criteria.Predicate n3 = cb.equal(c.get("effectiveDateEnd"), effectiveDate2);
			jakarta.persistence.criteria.Predicate n4 = cb.equal(c.get("companyId"), req.getCompanyId());
			jakarta.persistence.criteria.Predicate n5 = cb.equal(c.get("productId"), req.getProductId());
			jakarta.persistence.criteria.Predicate n6 = cb.equal(c.get("makeId"), req.getMakeId());
			query.where(n1, n2,n3,n4,n5,n6).orderBy(orderList);

			// Get Result
			TypedQuery<EwayVehicleMakeModelMaster> result = em.createQuery(query);
			list = result.getResultList();
			list = list.stream().filter(distinctByKey(o -> Arrays.asList(o.getModel()))).collect(Collectors.toList());
			list.sort(Comparator.comparing(EwayVehicleMakeModelMaster :: getModel ));
			for (EwayVehicleMakeModelMaster data : list) {
				// Response
				EwayVehicleMakeModelGetRes res = new EwayVehicleMakeModelGetRes();
				res = mapper.map(data, EwayVehicleMakeModelGetRes.class);
//				res.setCode(data.getVehicleid().toString());
//				res.setCodeDesc(data.getModel());
				resList.add(res);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			return null;
		}
		return resList;
	}

	
//Trim
	@Override
	public List<EwayVehicleMakeModelGetRes> getVehicleTrim(EwayVehicleMakeModelGetReq req) {
		List<EwayVehicleMakeModelGetRes> resList = new ArrayList<EwayVehicleMakeModelGetRes>();
		try {
//			List<EwayVehicleMakeModelMaster> list = repo.findByMakeAndModelGroupOrderByMakeDesc(req.getMake(),req.getModel());
			Date today = new Date();
			Calendar cal = new GregorianCalendar();
			cal.setTime(today);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 1);
			today = cal.getTime();
			cal.set(Calendar.HOUR_OF_DAY, 1);
			cal.set(Calendar.MINUTE, 1);
			Date todayEnd = cal.getTime();
			
			// Criteria
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<EwayVehicleMakeModelMaster> query = cb.createQuery(EwayVehicleMakeModelMaster.class);
			List<EwayVehicleMakeModelMaster> list = new ArrayList<EwayVehicleMakeModelMaster>();

			// Find All
			Root<EwayVehicleMakeModelMaster> c = query.from(EwayVehicleMakeModelMaster.class);

			// Select
			query.select(c);

			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(c.get("makemodel")));

			// Effective Date Start Max Filter
			Subquery<Date> effectiveDate = query.subquery(Date.class);
			Root<EwayVehicleMakeModelMaster> ocpm1 = effectiveDate.from(EwayVehicleMakeModelMaster.class);
			effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart").as(Date.class)));
			jakarta.persistence.criteria.Predicate a1 = cb.equal(c.get("vehicleid"), ocpm1.get("vehicleid"));
			jakarta.persistence.criteria.Predicate a2 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
			effectiveDate.where(a1, a2);
			
			// Effective Date End Max Filter
			Subquery<Date> effectiveDate2 = query.subquery(Date.class);
			Root<EwayVehicleMakeModelMaster> ocpm2 = effectiveDate2.from(EwayVehicleMakeModelMaster.class);
			effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd").as(Date.class)));
			jakarta.persistence.criteria.Predicate a3 = cb.equal(c.get("vehicleid"), ocpm2.get("vehicleid"));
			jakarta.persistence.criteria.Predicate a4 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
			effectiveDate2.where(a3,a4);

			// Where
			jakarta.persistence.criteria.Predicate n1 = cb.equal(c.get("status"), "Y");
			jakarta.persistence.criteria.Predicate n2 = cb.equal(c.get("effectiveDateStart"), effectiveDate);
			jakarta.persistence.criteria.Predicate n3 = cb.equal(c.get("effectiveDateEnd"), effectiveDate2);
			jakarta.persistence.criteria.Predicate n4 = cb.equal(c.get("companyId"), req.getCompanyId());
			jakarta.persistence.criteria.Predicate n5 = cb.equal(c.get("productId"), req.getProductId());
			jakarta.persistence.criteria.Predicate n6 = cb.equal(c.get("makeId"), req.getMakeId());
			jakarta.persistence.criteria.Predicate n7 = cb.equal(c.get("modelgroupId"), req.getModelId());
			query.where(n1, n2,n3,n4,n5,n6,n7).orderBy(orderList);

			// Get Result
			TypedQuery<EwayVehicleMakeModelMaster> result = em.createQuery(query);
			list = result.getResultList();

			list = list.stream().filter(distinctByKey(o -> Arrays.asList(o.getMakemodel()))).collect(Collectors.toList());
			list.sort(Comparator.comparing(EwayVehicleMakeModelMaster :: getMakemodel ));
			for (EwayVehicleMakeModelMaster data : list) {
				// Response
				EwayVehicleMakeModelGetRes res = new EwayVehicleMakeModelGetRes();
				res = mapper.map(data, EwayVehicleMakeModelGetRes.class);
//				res.setCode(data.getVehicleid());
//				res.setCodeDesc(data.getMakemodel());
				resList.add(res);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			return null;
		}
		return resList;
	}

	//Make
	@Override
	public List<EwayVehicleMakeDropdownRes> getVehicleMake(EwayVehicleMakeModelGetReq req) {
		List<EwayVehicleMakeDropdownRes> resList = new ArrayList<EwayVehicleMakeDropdownRes>();
		try {
//			List<EwayVehicleMakeModelMaster> list = repo.OrderByMakeDesc();
			Date today = new Date();
			Calendar cal = new GregorianCalendar();
			cal.setTime(today);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 1);
			today = cal.getTime();
			cal.set(Calendar.HOUR_OF_DAY, 1);
			cal.set(Calendar.MINUTE, 1);
			Date todayEnd = cal.getTime();
			
			// Criteria
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<EwayVehicleMakeModelMaster> query = cb.createQuery(EwayVehicleMakeModelMaster.class);
			List<EwayVehicleMakeModelMaster> list = new ArrayList<EwayVehicleMakeModelMaster>();

			// Find All
			Root<EwayVehicleMakeModelMaster> c = query.from(EwayVehicleMakeModelMaster.class);

			// Select
			query.select(c);

			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(c.get("make")));

			// Effective Date Start Max Filter
			Subquery<Date> effectiveDate = query.subquery(Date.class);
			Root<EwayVehicleMakeModelMaster> ocpm1 = effectiveDate.from(EwayVehicleMakeModelMaster.class);
			effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart").as(Date.class)));
			jakarta.persistence.criteria.Predicate a1 = cb.equal(c.get("vehicleid"), ocpm1.get("vehicleid"));
			jakarta.persistence.criteria.Predicate a2 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
			effectiveDate.where(a1, a2);
			
			// Effective Date End Max Filter
			Subquery<Date> effectiveDate2 = query.subquery(Date.class);
			Root<EwayVehicleMakeModelMaster> ocpm2 = effectiveDate2.from(EwayVehicleMakeModelMaster.class);
			effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd").as(Date.class)));
			jakarta.persistence.criteria.Predicate a3 = cb.equal(c.get("vehicleid"), ocpm2.get("vehicleid"));
			jakarta.persistence.criteria.Predicate a4 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
			effectiveDate2.where(a3,a4);

			// Where
			jakarta.persistence.criteria.Predicate n1 = cb.equal(c.get("status"), "Y");
			jakarta.persistence.criteria.Predicate n2 = cb.equal(c.get("effectiveDateStart"), effectiveDate);
			jakarta.persistence.criteria.Predicate n3 = cb.equal(c.get("effectiveDateEnd"), effectiveDate2);
			jakarta.persistence.criteria.Predicate n4 = cb.equal(c.get("companyId"), req.getCompanyId());
			jakarta.persistence.criteria.Predicate n5 = cb.equal(c.get("productId"), req.getProductId());
			query.where(n1, n2,n3,n4,n5).orderBy(orderList);

			// Get Result
			TypedQuery<EwayVehicleMakeModelMaster> result = em.createQuery(query);
			list = result.getResultList();
			list = list.stream().filter(distinctByKey(o -> Arrays.asList(o.getMake()))).collect(Collectors.toList());
			list.sort(Comparator.comparing(EwayVehicleMakeModelMaster :: getMake ));
			for (EwayVehicleMakeModelMaster data : list) {
				// Response
				EwayVehicleMakeDropdownRes res = new EwayVehicleMakeDropdownRes();
//				res = mapper.map(data, EwayVehicleMakeModelGetRes.class);
				res.setCode(data.getMakeId().toString());
				res.setCodeDesc(data.getMake());
				resList.add(res);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			return null;
		}
		return resList;
	}
	private static <T> java.util.function.Predicate<T> distinctByKey(java.util.function.Function<? super T, ?> keyExtractor) {
	    Map<Object, Boolean> seen = new ConcurrentHashMap<>();
	    return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}
	
	@Override
	public List<Error> validateVehicleMakeModel(EwayVehicleMakeModelSaveReq req) {

		List<Error> errorList = new ArrayList<Error>();

		try {

			if (StringUtils.isBlank(req.getMake())) {
				errorList.add(new Error("01", "Make", "Please Enter Make"));
			}
//			else if (req.getMake().length()>500) {
//				errorList.add(new Error("01", "Make", "Please Enter Make within 500 Characters "));
//			}else if (StringUtils.isBlank(req.getMake()) &&  StringUtils.isNotBlank(req.getCompanyId()) ) {
//				List<EwayVehicleMakeModelMaster> makeList = getMakeNameExistDetails(req.getMake() , req.getCompanyId() );
//				if (makeList.size()>0 ) {
//					errorList.add(new Error("01", "Make", "This Make Already Exist "));
//				}
//			}else if (StringUtils.isNotBlank(req.getMake()) &&  StringUtils.isNotBlank(req.getCompanyId())) {
//				List<EwayVehicleMakeModelMaster> makeList = getMakeNameExistDetails(req.getMake() , req.getCompanyId());
//				
//				if (makeList.size()>0 &&  (! req.getMake().equalsIgnoreCase(makeList.get(0).getBodyId().toString())) ) {
//					errorList.add(new Error("01", "Make", "This Body Name Already Exist "));
//				}
//			}	
			
			// Date Validation
			Calendar cal = new GregorianCalendar();
			Date today = new Date();
			cal.setTime(today);
			cal.add(Calendar.DAY_OF_MONTH, -1);
			today = cal.getTime();
			if (req.getEffectiveDateStart() == null || StringUtils.isBlank(req.getEffectiveDateStart().toString())) {
				errorList.add(new Error("02", "EffectiveDateStart", "Please Enter Effective Date Start"));

			} else if (req.getEffectiveDateStart().before(today)) {
				errorList
						.add(new Error("02", "EffectiveDateStart", "Please Enter Effective Date Start as Future Date"));
			}
			// Status Validation
			if (StringUtils.isBlank(req.getStatus())) {
				errorList.add(new Error("05", "Status", "Please Enter Status"));
			} else if (req.getStatus().length() > 1) {
				errorList.add(new Error("05", "Status", "Enter Status in One Character Only"));
			} else if(!("Y".equalsIgnoreCase(req.getStatus())||"N".equalsIgnoreCase(req.getStatus())||"R".equalsIgnoreCase(req.getStatus())|| "P".equalsIgnoreCase(req.getStatus()))) {
				errorList.add(new Error("05", "Status", "Please Select Valid Status - Active or Deactive or Pending or Referral "));
			}
			if (StringUtils.isBlank(req.getCompanyId())) {
				errorList.add(new Error("04", "CompanyId", "Please Enter CompanyId"));
			} else if (req.getCompanyId().length() > 20) {
				errorList.add(new Error("04", "CompanyId", "CompanyId 20 Character Only"));
			}
			if (StringUtils.isBlank(req.getEnginesizeCc())) {
				errorList.add(new Error("05", "EnginesizeCc", "Please Enter EnginesizeCc "));
			} else if (!req.getEnginesizeCc().matches("[0-9.]+")) {
				errorList.add(new Error("05", "EnginesizeCc", "Please Enter Valid EnginesizeCc"));
			}else if (Integer.valueOf(req.getEnginesizeCc())<0) {
				errorList.add(new Error("05", "EnginesizeCc", "Please Enter EnginesizeCc correct Value"));
			}
			if (StringUtils.isBlank(req.getFueltype())) {
				errorList.add(new Error("06", "Fueltype", "Please Enter Fueltype"));
			} else if (!req.getFueltype().matches("[0-9.]+")) {
				errorList.add(new Error("06", "Fueltype", "Fueltype"));
			}else if (Integer.valueOf(req.getFueltype())<0) {
				errorList.add(new Error("05", "Fueltype", "Please Enter  Fueltype correct Value"));
			}
			if (StringUtils.isBlank(req.getPowerKw())) {
				errorList.add(new Error("07", "PowerKw", "Please Enter PowerKw"));
			} else if (!req.getPowerKw().matches("[0-9.]+")) {
				errorList.add(new Error("06", "PowerKw", "PowerKw"));
			}else if (Integer.valueOf(req.getPowerKw())<0) {
				errorList.add(new Error("05", "Tonnage", "Please Enter  PowerKw correct Value"));
			}
			
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return errorList;
	}

		public List<EwayVehicleMakeModelMaster> getMakeNameExistDetails(String name , String InsuranceId) {
			List<EwayVehicleMakeModelMaster> list = new ArrayList<EwayVehicleMakeModelMaster>();
			try {
				Date today = new Date();
				// Find Latest Record
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<EwayVehicleMakeModelMaster> query = cb.createQuery(EwayVehicleMakeModelMaster.class);

				// Find All
				Root<EwayVehicleMakeModelMaster> b = query.from(EwayVehicleMakeModelMaster.class);

				// Select
				query.select(b);

				// Effective Date Max Filter
				Subquery<Long> amendId = query.subquery(Long.class);
				Root<EwayVehicleMakeModelMaster> ocpm1 = amendId.from(EwayVehicleMakeModelMaster.class);
				amendId.select(cb.max(ocpm1.get("amendId")));
				Predicate a1 = cb.equal(ocpm1.get("makeId"), b.get("makeId"));
				Predicate a2 = cb.equal(ocpm1.get("companyId"), b.get("companyId"));
				Predicate a3 = cb.equal(ocpm1.get("branchCode"), b.get("branchCode"));
				Predicate a4 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
				Predicate a5 = cb.greaterThanOrEqualTo(ocpm1.get("effectiveDateEnd"), today);
				amendId.where(a1,a2,a3,a4,a5);

				Predicate n1 = cb.equal(b.get("amendId"), amendId);
				Predicate n2 = cb.equal(cb.lower( b.get("make")), name.toLowerCase());
				Predicate n3 = cb.equal(b.get("companyId"),InsuranceId);
				query.where(n1,n2,n3);
				
				// Get Result
				TypedQuery<EwayVehicleMakeModelMaster> result = em.createQuery(query);
				list = result.getResultList();		
			
			} catch (Exception e) {
				e.printStackTrace();
				log.info(e.getMessage());

			}
			return list;
		}

	
		
	@Override
	public SuccessRes saveVehicleMakeModel(EwayVehicleMakeModelSaveReq req){
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");
		SuccessRes res = new SuccessRes();
		EwayVehicleMakeModelMaster saveData = new EwayVehicleMakeModelMaster();
		List<EwayVehicleMakeModelMaster> list = new ArrayList<EwayVehicleMakeModelMaster>();
		DozerBeanMapper dozerMapper = new DozerBeanMapper();

		try {
			Integer amendId=0;
			Date startDate = req.getEffectiveDateStart() ;
			String  end = "31/12/2050";
			Date endDate = sdf.parse(end);
			long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;
			Date oldEndDate = new Date(req.getEffectiveDateStart().getTime() - MILLIS_IN_A_DAY);
			Date entryDate = null ;
			String createdBy = "" ;
			Integer makeId ;
			Integer modelId;
			Integer modelGroupId ;
			Integer serviceVehicleId;
			if (StringUtils.isBlank(req.getMakeId()) || req.getMakeId()==null) {
				// Save
				// Long totalCount = repo.count();
				Integer totalCount1 = getMaKeCount( req.getCompanyId());
				makeId = totalCount1 + 1;
				Integer totalCount2 = getModelCount( req.getCompanyId());
				modelId=totalCount2 + 1;
				Integer totalCount3 = getModelGroupCount( req.getCompanyId());
				modelGroupId=totalCount3 + 1;
				Integer totalCount4 = getServiceVehicleIdCount( req.getCompanyId());
				serviceVehicleId=totalCount4 + 1;
				
				entryDate = new Date();
				createdBy = req.getCreatedBy();
				res.setResponse("Saved Successfully ");
				res.setSuccessId(serviceVehicleId.toString());

			} else {
				// Update
				// Get Less than Equal Today Record
				// Criteria
				makeId = Integer.valueOf(req.getMakeId());
				modelId =Integer.valueOf(req.getModelId());
				modelGroupId =Integer.valueOf(req.getModelgroupId());
				serviceVehicleId= Integer.valueOf(req.getSourcevehicleid());	
				
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<EwayVehicleMakeModelMaster> query = cb.createQuery(EwayVehicleMakeModelMaster.class);

				// Find All
				Root<EwayVehicleMakeModelMaster> b = query.from(EwayVehicleMakeModelMaster.class);

				// Select
				query.select(b);

				// Order By
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.desc(b.get("effectiveDateStart")));
				
				
				// Where
				Predicate n1 = cb.equal(b.get("companyId"), req.getCompanyId());
				Predicate n3 = cb.equal(b.get("makeId"), req.getMakeId());
				Predicate n4 = cb.equal(b.get("sectionId"), req.getSectionId());
				query.where(n1, n3 , n4).orderBy(orderList);;
				
				// Get Result
				TypedQuery<EwayVehicleMakeModelMaster> result = em.createQuery(query);
				int limit = 0 , offset = 2 ;
				result.setFirstResult(limit * offset);
				result.setMaxResults(offset);
				list = result.getResultList();

				if(list.size()>0) {
					Date beforeOneDay = new Date(new Date().getTime() - MILLIS_IN_A_DAY);
				
					if ( list.get(0).getEffectiveDateStart().before(beforeOneDay)  ) {
						amendId = list.get(0).getAmendId() + 1 ;
						entryDate = new Date() ;
						createdBy = req.getCreatedBy();
						EwayVehicleMakeModelMaster lastRecord = list.get(0);
							lastRecord.setEffectiveDateEnd(oldEndDate);
							repo.saveAndFlush(lastRecord);
						
					} else {
						amendId = list.get(0).getAmendId() ;
						entryDate = list.get(0).getEntryDate() ;
						createdBy = list.get(0).getCreatedBy();
						saveData = list.get(0) ;
						if (list.size()>1 ) {
							EwayVehicleMakeModelMaster lastRecord = list.get(1);
							lastRecord.setEffectiveDateEnd(oldEndDate);
							repo.saveAndFlush(lastRecord);
						}
					
				    }
				}
			
				res.setResponse("Updated Successfully ");
				res.setSuccessId(serviceVehicleId.toString());

			}

			dozerMapper.map(req, saveData);
			saveData.setMakeId(makeId);
			saveData.setMake(req.getMake());
			saveData.setModel(req.getModel());
			saveData.setModelId(modelId);
			saveData.setModelgroupId(modelGroupId);
			saveData.setModelGroup(req.getModelGroup());
			saveData.setSourcevehicleid(serviceVehicleId.toString());
			saveData.setEffectiveDateStart(startDate);
			saveData.setEffectiveDateEnd(endDate);
			saveData.setCreatedBy(createdBy);
			saveData.setStatus(req.getStatus());
			saveData.setCompanyId(req.getCompanyId());
			saveData.setEntryDate(entryDate);
			saveData.setAmendId(amendId);
			saveData.setUpdatedDate(new Date());
			saveData.setSectionId(StringUtils.isBlank(req.getSectionId())? 10 :Integer.valueOf(req.getSectionId()));
			saveData.setUpdatedBy(req.getCreatedBy());
			saveData.setIsselectable("1");
			// Fuel Id
			if(StringUtils.isNotBlank(req.getFueltype() )) {
				String fuelTypeDesc =getListItemCode(req.getCompanyId() , "99999" ,"FUEL_TYPE",req.getFueltype());  
				saveData.setFueltype(fuelTypeDesc);	
			} 
			saveData.setBodyId(17);
			repo.saveAndFlush(saveData);
			log.info("Saved Details is ---> " + json.toJson(saveData));

		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is --->" + e.getMessage());
			return null;
		}
		return res;
	}
	
	public synchronized String getListItemCode(String insuranceId , String branchCode, String itemType, String itemCode) {
		String itemDesc = "" ;
		List<ListItemValue> list = new ArrayList<ListItemValue>();
		try {
			Date today = new Date();
			Calendar cal = new GregorianCalendar();
			cal.setTime(today);
			today = cal.getTime();
			Date todayEnd = cal.getTime();
			
			// Criteria
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<ListItemValue> query=  cb.createQuery(ListItemValue.class);
			// Find All
			Root<ListItemValue> c = query.from(ListItemValue.class);
			
			//Select
			query.select(c);
			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(c.get("branchCode")));
			
			
			// Effective Date Start Max Filter
			Subquery<Date> effectiveDate = query.subquery(Date.class);
			Root<ListItemValue> ocpm1 = effectiveDate.from(ListItemValue.class);
			effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart").as(Date.class)));
			Predicate a1 = cb.equal(c.get("itemId"),ocpm1.get("itemId"));
			Predicate b3 = cb.equal(c.get("branchCode"),ocpm1.get("branchCode"));
			Predicate b4 = cb.equal(c.get("companyId"),ocpm1.get("companyId"));
			Predicate a2 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
			effectiveDate.where(a1,a2,b3,b4);
			// Effective Date End Max Filter
			Subquery<Date> effectiveDate2 = query.subquery(Date.class);
			Root<ListItemValue> ocpm2 = effectiveDate2.from(ListItemValue.class);
			effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd").as(Date.class)));
			Predicate a3 = cb.equal(c.get("itemId"),ocpm2.get("itemId"));
			Predicate b1 = cb.equal(c.get("branchCode"),ocpm2.get("branchCode"));
			Predicate b2 = cb.equal(c.get("companyId"),ocpm2.get("companyId"));
			Predicate a4 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
			effectiveDate2.where(a3,a4,b1,b2);
						
			// Where
			Predicate n1 = cb.equal(c.get("status"),"Y");
			Predicate n12 = cb.equal(c.get("status"),"R");
			Predicate n13 = cb.or(n1,n12);
			Predicate n2 = cb.equal(c.get("effectiveDateStart"),effectiveDate);
			Predicate n3 = cb.equal(c.get("effectiveDateEnd"),effectiveDate2);	
			Predicate n4 = cb.equal(c.get("companyId"), insuranceId);
			Predicate n6 = cb.equal(c.get("branchCode"), branchCode);
			Predicate n7 = cb.equal(c.get("branchCode"), "99999");
			Predicate n9 = cb.or(n6,n7);
			Predicate n10 = cb.equal(c.get("itemType"),itemType );
			Predicate n11 = cb.equal(c.get("itemCode"), itemCode);
			query.where(n13,n2,n3,n4,n9,n10,n11).orderBy(orderList);
			
		
			// Get Result
			TypedQuery<ListItemValue> result = em.createQuery(query);
			list = result.getResultList();
			
			itemDesc = list.size() > 0 ? list.get(0).getItemValue() : "" ; 
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			return null;
		}
		return itemDesc ;
	}



	public Integer getMaKeCount(String companyId) {

		Integer data = 0;
		try {

			List<EwayVehicleMakeModelMaster> list = new ArrayList<EwayVehicleMakeModelMaster>();
			// Find Latest Record
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<EwayVehicleMakeModelMaster> query = cb.createQuery(EwayVehicleMakeModelMaster.class);

			// Find All
			Root<EwayVehicleMakeModelMaster> b = query.from(EwayVehicleMakeModelMaster.class);

			// 
			query.select(b);
//			query.multiselect(cb.count(b));

			// Effective Date Max Filter
			Subquery<Date> effectiveDate = query.subquery(Date.class);
			Root<EwayVehicleMakeModelMaster> ocpm1 = effectiveDate.from(EwayVehicleMakeModelMaster.class);
			effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart").as(Date.class)));
			Predicate a1 = cb.equal(ocpm1.get("vehicleid"), b.get("vehicleid"));
			Predicate a2 = cb.equal(ocpm1.get("companyId"), b.get("companyId"));
			effectiveDate.where(a1,a2);
			
			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.desc(b.get("makeId")));
		

			Predicate n1 = cb.equal(b.get("effectiveDateStart"), effectiveDate);
			Predicate n2 = cb.equal(b.get("companyId"), companyId);
			query.where(n1,n2).orderBy(orderList);
			
			// Get Result
			TypedQuery<EwayVehicleMakeModelMaster> result = em.createQuery(query);
			int limit = 0 , offset = 1 ;
			result.setFirstResult(limit * offset);
			result.setMaxResults(offset);
			list = result.getResultList();
			data = list.get(0).getMakeId();

		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());

		}
		return data;
	}
	public Integer getModelCount(String companyId) {

		Integer data = 0;
		try {

			List<EwayVehicleMakeModelMaster> list = new ArrayList<EwayVehicleMakeModelMaster>();
			// Find Latest Record
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<EwayVehicleMakeModelMaster> query = cb.createQuery(EwayVehicleMakeModelMaster.class);

			// Find All
			Root<EwayVehicleMakeModelMaster> b = query.from(EwayVehicleMakeModelMaster.class);

			// Select
			query.select(b);

			// Effective Date Max Filter
			Subquery<Date> effectiveDate = query.subquery(Date.class);
			Root<EwayVehicleMakeModelMaster> ocpm1 = effectiveDate.from(EwayVehicleMakeModelMaster.class);
			effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart").as(Date.class)));
			Predicate a1 = cb.equal(ocpm1.get("vehicleid"), b.get("vehicleid"));
			Predicate a2 = cb.equal(ocpm1.get("companyId"), b.get("companyId"));
			effectiveDate.where(a1,a2);
			
			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.desc(b.get("modelId")));
		

			Predicate n1 = cb.equal(b.get("effectiveDateStart"), effectiveDate);
			Predicate n2 = cb.equal(b.get("companyId"), companyId);
			query.where(n1,n2).orderBy(orderList);
			
			// Get Result
			TypedQuery<EwayVehicleMakeModelMaster> result = em.createQuery(query);
			int limit = 0 , offset = 1 ;
			result.setFirstResult(limit * offset);
			result.setMaxResults(offset);
			list = result.getResultList();
			data = Integer.valueOf(list.get(0).getModelId());

		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());

		}
		return data;
	}
	
	public Integer getModelGroupCount(String companyId) {

		Integer data = 0;
		try {

			List<EwayVehicleMakeModelMaster> list = new ArrayList<EwayVehicleMakeModelMaster>();
			// Find Latest Record
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<EwayVehicleMakeModelMaster> query = cb.createQuery(EwayVehicleMakeModelMaster.class);

			// Find All
			Root<EwayVehicleMakeModelMaster> b = query.from(EwayVehicleMakeModelMaster.class);

			// Select
			query.select(b);
//			query.multiselect(cb.count(b));

			// Effective Date Max Filter
			Subquery<Date> effectiveDate = query.subquery(Date.class);
			Root<EwayVehicleMakeModelMaster> ocpm1 = effectiveDate.from(EwayVehicleMakeModelMaster.class);
			effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart").as(Date.class)));
			Predicate a1 = cb.equal(ocpm1.get("vehicleid"), b.get("vehicleid"));
			Predicate a2 = cb.equal(ocpm1.get("companyId"), b.get("companyId"));
			effectiveDate.where(a1,a2);
			
			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.desc(b.get("modelId")));
		

			Predicate n1 = cb.equal(b.get("effectiveDateStart"), effectiveDate);
			Predicate n2 = cb.equal(b.get("companyId"), companyId);
			query.where(n1,n2).orderBy(orderList);
			
			// Get Result
			TypedQuery<EwayVehicleMakeModelMaster> result = em.createQuery(query);
			int limit = 0 , offset = 1 ;
			result.setFirstResult(limit * offset);
			result.setMaxResults(offset);
			list = result.getResultList();
			data = Integer.valueOf(list.get(0).getModelgroupId());

		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());

		}
		return data;
	}
	
	public Integer getServiceVehicleIdCount(String companyId) {

		Integer data = 0;
		try {

			List<EwayVehicleMakeModelMaster> list = new ArrayList<EwayVehicleMakeModelMaster>();
			// Find Latest Record
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<EwayVehicleMakeModelMaster> query = cb.createQuery(EwayVehicleMakeModelMaster.class);

			// Find All
			Root<EwayVehicleMakeModelMaster> b = query.from(EwayVehicleMakeModelMaster.class);

			// Select
			query.select(b);
//			query.multiselect(cb.max(b.get("sourcevehicleid")).alias("sourcevehicleid"));

			// Effective Date Max Filter
			Subquery<Date> effectiveDate = query.subquery(Date.class);
			Root<EwayVehicleMakeModelMaster> ocpm1 = effectiveDate.from(EwayVehicleMakeModelMaster.class);
			effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart").as(Date.class)));
			Predicate a1 = cb.equal(ocpm1.get("vehicleid"), b.get("vehicleid"));
			Predicate a2 = cb.equal(ocpm1.get("companyId"), b.get("companyId"));
			effectiveDate.where(a1,a2);
			
			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.desc(b.get("sourcevehicleid")));
		

			Predicate n1 = cb.equal(b.get("effectiveDateStart"), effectiveDate);
			Predicate n2 = cb.equal(b.get("companyId"), companyId);
			query.where(n1,n2).orderBy(orderList);
			
			// Get Result
			TypedQuery<EwayVehicleMakeModelMaster> result = em.createQuery(query);
			int limit = 0 , offset = 1 ;
			result.setFirstResult(limit * offset);
			result.setMaxResults(offset);
			list = result.getResultList();
			data = Integer.valueOf(list.get(0).getSourcevehicleid());

		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());

		}
		return data;
	}

	//Get
	@Override
	public EwayVehicleMakeModelGetRes getByVehicleId(EwayVehicleMakeModelGetReq req) {
		EwayVehicleMakeModelGetRes res = new EwayVehicleMakeModelGetRes();
		ModelMapper mapper = new ModelMapper();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		try {
			// Criteria
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<EwayVehicleMakeModelMaster> query = cb.createQuery(EwayVehicleMakeModelMaster.class);
			List<EwayVehicleMakeModelMaster> list = new ArrayList<EwayVehicleMakeModelMaster>();

			// Find All
			Root<EwayVehicleMakeModelMaster> c = query.from(EwayVehicleMakeModelMaster.class);

			// Select
			query.select(c);

			// AmendId Max Filter
			Subquery<Long> amendId = query.subquery(Long.class);
			Root<EwayVehicleMakeModelMaster> ocpm1 = amendId.from(EwayVehicleMakeModelMaster.class);
			amendId.select(cb.max(ocpm1.get("amendId")));
			Predicate a1 = cb.equal(ocpm1.get("vehicleid"), c.get("vehicleid"));
			Predicate a2 = cb.equal(ocpm1.get("companyId"), c.get("companyId"));
			amendId.where(a1, a2);

			// Where
			Predicate n6 = cb.equal(c.get("amendId"), amendId);
			Predicate n1 = cb.equal(c.get("companyId"), req.getCompanyId());
			Predicate n3 = cb.equal(c.get("vehicleid"), req.getVehicleId());
			query.where(n1,n3,n6);
			
			// Get Result
			TypedQuery<EwayVehicleMakeModelMaster> result = em.createQuery(query);
			list = result.getResultList();
			list = list.stream().filter(distinctByKey(o -> Arrays.asList(o.getMakeId()))).collect(Collectors.toList());
			list.sort(Comparator.comparing(EwayVehicleMakeModelMaster :: getVehicleid ));
			
			res = mapper.map(list.get(0), EwayVehicleMakeModelGetRes.class);
			res.setMakeId(list.get(0).getMakeId());
			res.setEntryDate(list.get(0).getEntryDate());
			res.setEffectiveDateStart(list.get(0).getEffectiveDateStart());
			res.setEffectiveDateEnd(list.get(0).getEffectiveDateEnd());
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			return null;
		}
		return res;
	}


	@Override
	public List<EwayVehicleMakeModelGetRes> getallMotorMakeModelDetails(EwayVehicleMakeModelGetReq req) {
		List<EwayVehicleMakeModelGetRes> resList = new ArrayList<EwayVehicleMakeModelGetRes>();
		DozerBeanMapper mapper = new DozerBeanMapper();
		try {
			List<EwayVehicleMakeModelMaster> list = new ArrayList<EwayVehicleMakeModelMaster>();
			
			// Find Latest Record
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<EwayVehicleMakeModelMaster> query = cb.createQuery(EwayVehicleMakeModelMaster.class);

			// Find All
			Root<EwayVehicleMakeModelMaster> b = query.from(EwayVehicleMakeModelMaster.class);

			// Select
			query.select(b);

			// AmendId Max Filter
			Subquery<Long> amendId = query.subquery(Long.class);
			Root<EwayVehicleMakeModelMaster> ocpm1 = amendId.from(EwayVehicleMakeModelMaster.class);
			amendId.select(cb.max(ocpm1.get("amendId")));
			Predicate a1 = cb.equal(ocpm1.get("vehicleid"), b.get("vehicleid"));
			Predicate a2 = cb.equal(ocpm1.get("companyId"), b.get("companyId"));
			Predicate a3 = cb.equal(ocpm1.get("branchCode"), b.get("branchCode"));
			amendId.where(a1, a2, a3);
			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(b.get("branchCode")));

			Predicate n6 = cb.equal(b.get("amendId"), amendId);
			Predicate n1 = cb.equal(b.get("companyId"), req.getCompanyId());
			query.where(n1,n6).orderBy(orderList);

			// Get Result
			TypedQuery<EwayVehicleMakeModelMaster> result = em.createQuery(query);
			list = result.getResultList();
			list = list.stream().filter(distinctByKey(o -> Arrays.asList(o.getMakeId()))).collect(Collectors.toList());
			list.sort(Comparator.comparing(EwayVehicleMakeModelMaster :: getVehicleid ));


			// Map
			for (EwayVehicleMakeModelMaster data : list) {
				EwayVehicleMakeModelGetRes res = new EwayVehicleMakeModelGetRes();

				res = mapper.map(data, EwayVehicleMakeModelGetRes.class);
				res.setMakeId(data.getMakeId());
				resList.add(res);
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			return null;

		}
		return resList;
	}
	
}