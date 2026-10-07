package com.maan.eway.common.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maan.eway.bean.TravelPolicyType;
import com.maan.eway.common.req.TravelPolicyTypeGetReq;
import com.maan.eway.common.res.TravelPolicyTypeCoverRes;
import com.maan.eway.common.res.TravelPolicyTypeRes;
import com.maan.eway.common.res.TravelPolicyTypeSubCoverRes;
import com.maan.eway.common.service.TravelPolicyTypeService;
import com.maan.eway.repository.ListItemValueRepository;
import com.maan.eway.repository.TravelPolicyTypeRepository;

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
public class TravelPolicyTypeServiceImpl  implements TravelPolicyTypeService{
	
	@Autowired
	private TravelPolicyTypeRepository repo;

	private Logger log=LogManager.getLogger(TravelPolicyTypeServiceImpl.class);

	@Autowired
	private ListItemValueRepository listRepo;
	
	@PersistenceContext
	private EntityManager em;


//	@Override
//	public List<TravelPolicyTypeRes> getTravelPolicyType(TravelPolicyTypeGetReq req) { //status
//		List<TravelPolicyTypeRes> resList = new ArrayList<TravelPolicyTypeRes>();
//		DozerBeanMapper dozermapper = new DozerBeanMapper();
//		List<TravelPolicyType> datas1 =new ArrayList<TravelPolicyType>();
//		List<TravelPolicyType> datas = new ArrayList<TravelPolicyType>();
//		SimpleDateFormat sdformat = new SimpleDateFormat("dd/MM/YYYY");
//		try {
//			TravelPolicyTypeRes res = new TravelPolicyTypeRes(); 
//			
////			if(StringUtils.isNotBlank(req.getStatus()))
////				 datas1 =  repo.findByPolicyTypeIdAndPlanTypeIdAndStatusAndCoverStatus(Integer.valueOf(req.getPolicyTypeId()),Integer.valueOf( req.getPlanTypeId()), req.getStatus(),req.getStatus() );
////			else
////				 datas1 =  repo.findByPolicyTypeIdAndPlanTypeId(Integer.valueOf(req.getPolicyTypeId()),Integer.valueOf( req.getPlanTypeId()) );
////		
////			if(datas1.size()>0) {
////			
////				//Amendid
////				Optional<Integer> datasfilter = datas1.stream().map(TravelPolicyType :: getAmendId) .max(Comparator.naturalOrder());
////				
////				if(StringUtils.isNotBlank(req.getStatus()))
////					datas =  repo.findByPolicyTypeIdAndPlanTypeIdAndAmendIdAndStatusAndCoverStatus(Integer.valueOf(req.getPolicyTypeId()),Integer.valueOf( req.getPlanTypeId()), datasfilter.get(),req.getStatus(),req.getStatus() );
////				else
////					datas =  repo.findByPolicyTypeIdAndPlanTypeIdAndAmendId(Integer.valueOf(req.getPolicyTypeId()),Integer.valueOf( req.getPlanTypeId()), datasfilter.get());
////			
////			}
//			
//		
//				 datas1 =  repo.findByPolicyTypeIdAndPlanTypeIdAndStatusAndCoverStatus(Integer.valueOf(req.getPolicyTypeId()),Integer.valueOf( req.getPlanTypeId()), "Y", "Y" );
//		
//				
//		
//			if(datas1.size()>0) {
//			
//				//Amendid
//				Optional<Integer> datasfilter = datas1.stream().map(TravelPolicyType :: getAmendId) .max(Comparator.naturalOrder());
//			
//					datas =  repo.findByPolicyTypeIdAndPlanTypeIdAndAmendIdAndStatusAndCoverStatus(Integer.valueOf(req.getPolicyTypeId()),Integer.valueOf( req.getPlanTypeId()), datasfilter.get(), "Y", "Y" );
//				
//			}
//			
//			datas = datas.stream().filter(distinctByKey(o -> Arrays.asList(o.getCoverId()))).collect(Collectors.toList());
//			 
//			datas = datas.stream().filter(o -> o.getCoverDesc()!=null).collect(Collectors.toList());
//			
//			
//			List<TravelPolicyTypeCoverRes> coverList = new ArrayList<TravelPolicyTypeCoverRes>();
//			TravelPolicyTypeCoverRes coverRes = new TravelPolicyTypeCoverRes();
//		
//			if(datas.size()>0) {
//			for(TravelPolicyType data : datas) {
//	
//				coverRes = dozermapper.map(data,TravelPolicyTypeCoverRes.class );
//				coverRes.setCoverId(data.getCoverId().toString());
//				coverRes.setCoverDesc(data.getCoverDesc());
//				coverRes.setCoverStatus(data.getCoverStatus());	
//				List<TravelPolicyType> covers = repo.findByPolicyTypeIdAndPlanTypeIdAndCoverId(Integer.valueOf(req.getPolicyTypeId()), Integer.valueOf(req.getPlanTypeId()),Integer.valueOf(data.getSubCoverId()));
//			
//				Optional<Integer> datasfilter = covers.stream().map(TravelPolicyType :: getAmendId) .max(Comparator.naturalOrder());
//				List<TravelPolicyType> coversfilter  = repo.findByPolicyTypeIdAndPlanTypeIdAndCoverIdAndAmendId(Integer.valueOf(req.getPolicyTypeId()), Integer.valueOf(req.getPlanTypeId()),Integer.valueOf(data.getSubCoverId()), datasfilter.get());
//				coversfilter = coversfilter.stream().filter(distinctByKey(o -> Arrays.asList(o.getSubCoverId()))).collect(Collectors.toList());
//				coversfilter = coversfilter.stream().filter(o -> o.getSubCoverDesc()!=null).collect(Collectors.toList());
//				
//				List<TravelPolicyTypeSubCoverRes> subCoverList = new ArrayList<TravelPolicyTypeSubCoverRes>();				
//				for(TravelPolicyType cover : coversfilter) {
//				TravelPolicyTypeSubCoverRes subCoverRes = new TravelPolicyTypeSubCoverRes();		
//				subCoverRes = dozermapper.map(cover,TravelPolicyTypeSubCoverRes.class);
//		
//				if(cover.getSubCoverId() != 0)
//					subCoverList.add(subCoverRes);
//				}
//				coverRes.setTravelSubCover(subCoverList);
//				coverList.add(coverRes);
//				res.setTravelCover(coverList);
//
//		//	}
//			}
//			res.setRemarks(datas.get(0).getRemarks()==null?"":datas.get(0).getRemarks().toString());
//			res.setEffectiveDateStart(datas.get(0).getEffectiveStartdate()==null?null:sdformat.format(datas.get(0).getEffectiveStartdate()));
//			res.setEffectiveDateEnd(datas.get(0).getEffectiveEnddate()==null?null:sdformat.format(datas.get(0).getEffectiveEnddate()));
//			res.setPlanTypeId(datas.get(0).getPlanTypeId()==null?null:datas.get(0).getPlanTypeId().toString());
//			res.setPlanTypeDesc(datas.get(0).getPlanTypeDesc()==null?null:datas.get(0).getPlanTypeDesc());
//			res.setPolicyTypeId(datas.get(0).getPolicyTypeId()==null?null:datas.get(0).getPolicyTypeId().toString());
//			res.setPolicyTypeDesc(datas.get(0).getPolicyTypeDesc()==null?null:datas.get(0).getPolicyTypeDesc());			
//			resList.add(res);
//			}
//			
//			if(datas.size()<0){
//				res.setPlanTypeDesc(null);
//				res.setPlanTypeId(null);
//				res.setPolicyTypeDesc(null);
//				res.setPolicyTypeId(null);
//				res.setTravelCover(null);
//				resList.add(res);
//			}
//		}
//		catch(Exception e) {
//			e.printStackTrace();
//			log.info("Log Details"+e.getMessage());
//			return null;
//		}
//		return resList;
//	}
	
	@Override
	public TravelPolicyTypeRes getTravelPolicyType(TravelPolicyTypeGetReq req) { //status
		TravelPolicyTypeRes res = new TravelPolicyTypeRes();
		DozerBeanMapper dozermapper = new DozerBeanMapper();
		try {
			
			Date today  = new Date();
			Calendar cal = Calendar.getInstance();

			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			Date todayStart = cal.getTime();

			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			cal.set(Calendar.MILLISECOND, 999);
			Date todayEnd = cal.getTime();

			
			
			//get all Active cover list 
			List<TravelPolicyType> list = new ArrayList<TravelPolicyType>();

			//Find Latest Record
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<TravelPolicyType> query = cb.createQuery(TravelPolicyType.class);

			//Find All
			Root<TravelPolicyType> b = query.from(TravelPolicyType.class);

			//Select
			query.select(b);

//			Subquery<Long> maxAmendId = query.subquery(Long.class);
//			Root<TravelPolicyType> ocpm1 = maxAmendId.from(TravelPolicyType.class);
//			maxAmendId.select(cb.max(ocpm1.get("amendId")));
//			Predicate a1 = cb.equal(ocpm1.get("companyId"), b.get("companyId"));
//			Predicate a2 = cb.equal(ocpm1.get("productId"), b.get("productId"));
//			Predicate a3 = cb.equal(ocpm1.get("policyTypeId"), b.get("policyTypeId"));
//			Predicate a4 = cb.equal(ocpm1.get("planTypeId"), b.get("planTypeId"));
//			Predicate a5 = cb.equal(ocpm1.get("branchCode"), b.get("branchCode"));
//			Predicate a6 = cb.equal(ocpm1.get("coverId"), b.get("coverId"));
//			Predicate a7 = cb.equal(ocpm1.get("subCoverId"), "0");
//			Predicate a8 = cb.equal(ocpm1.get("coverStatus"), "Y");
//			maxAmendId.where(a1,a2,a3,a4,a5,a6, a7, a8);
			
			// Effective Date Max Filter
			Subquery<Date> effectiveDate = query.subquery(Date.class);
			Root<TravelPolicyType> ocpm1 = effectiveDate.from(TravelPolicyType.class);
			effectiveDate.select(cb.greatest(ocpm1.get("effectiveStartDate").as(Date.class)));
			Predicate a1 = cb.equal(ocpm1.get("companyId"), b.get("companyId"));
			Predicate a2 = cb.equal(ocpm1.get("productId"), b.get("productId"));
			Predicate a3 = cb.equal(ocpm1.get("policyTypeId"), b.get("policyTypeId"));
			Predicate a4 = cb.equal(ocpm1.get("planTypeId"), b.get("planTypeId"));
			Predicate a5 = cb.equal(ocpm1.get("branchCode"), b.get("branchCode"));
			Predicate a6 = cb.equal(ocpm1.get("coverId"), b.get("coverId"));
			//Predicate a7 = cb.equal(ocpm1.get("subCoverId"), 1);
			Predicate a8 = cb.equal(ocpm1.get("coverStatus"), "Y");
			Predicate a9 = cb.lessThanOrEqualTo(ocpm1.get("effectiveStartDate"), todayStart);
			effectiveDate.where(a1,a2,a3,a4,a5,a6,a8,a9);
			
			// Effective Date Max Filter
			Subquery<Date> effectiveDate2 = query.subquery(Date.class);
			Root<TravelPolicyType> ocpm2 = effectiveDate2.from(TravelPolicyType.class);
			effectiveDate2.select(cb.greatest(ocpm2.get("effectiveEndDate").as(Date.class)));
			Predicate b1 = cb.equal(ocpm2.get("companyId"), b.get("companyId"));
			Predicate b2 = cb.equal(ocpm2.get("productId"), b.get("productId"));
			Predicate b3 = cb.equal(ocpm2.get("policyTypeId"), b.get("policyTypeId"));
			Predicate b4 = cb.equal(ocpm2.get("planTypeId"), b.get("planTypeId"));
			Predicate b5 = cb.equal(ocpm2.get("branchCode"), b.get("branchCode"));
			Predicate b6 = cb.equal(ocpm2.get("coverId"), b.get("coverId"));
			//Predicate b7 = cb.equal(ocpm2.get("subCoverId"), 1);
			//Predicate b8 = cb.equal(ocpm2.get("coverStatus"), "Y");
			Predicate b9 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveEndDate"), todayEnd);
			effectiveDate2.where(b1, b2, b3, b4,b5,b6,  b9);
		

			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.desc(b.get("coverId")));
			orderList.add(cb.desc(b.get("subCoverId")));


			Predicate n1 = cb.equal(b.get("companyId"), req.getCompanyId());
			Predicate n2 = cb.equal(b.get("productId"), req.getProductId());
			Predicate n3 = cb.equal(b.get("policyTypeId"), req.getPolicyTypeId());
			Predicate n4 = cb.equal(b.get("planTypeId"), req.getPlanTypeId());
			Predicate n5 = cb.equal(b.get("branchCode"), "99999");
			Predicate n6 = b.get("effectiveStartDate").in(effectiveDate);
			Predicate n8 = b.get("effectiveEndDate").in(effectiveDate2);
			Predicate n7 = cb.equal(b.get("coverStatus"),"Y");
			//Predicate n10 = cb.equal(b.get("subCoverId"), "0");

			query.where(n1, n2,n3,n4,n6, n5,n7, n8).orderBy(orderList) ;

			TypedQuery<TravelPolicyType> result = em.createQuery(query);
			list = result.getResultList();
			list = list.stream().filter(distinctByKey(o -> Arrays.asList(o.getCoverId()))).collect(Collectors.toList());
			list = list.stream().filter(o -> o.getCoverDesc()!=null).collect(Collectors.toList());

			List<TravelPolicyTypeCoverRes> coverList = new ArrayList<TravelPolicyTypeCoverRes>();
			
			if(list.size()>0) {
			for(TravelPolicyType data : list) {
				TravelPolicyTypeCoverRes travelCover = new TravelPolicyTypeCoverRes();
				
				
				res = dozermapper.map(data,TravelPolicyTypeRes.class );
				res.setEffectiveDateEnd(data.getEffectiveEndDate()==null?null:data.getEffectiveEndDate());
				res.setEffectiveDateStart(data.getEffectiveStartDate()==null?null:data.getEffectiveStartDate());
			
				travelCover.setCoverId(data.getCoverId().toString());
				travelCover.setCoverDesc(data.getCoverDesc());
				travelCover.setCoverStatus(data.getCoverStatus());
				
				//based on cover Id get all Active sub cover list
				List<TravelPolicyTypeSubCoverRes> subCoverList = new ArrayList<TravelPolicyTypeSubCoverRes>();
				List<TravelPolicyType> subcovers = getAllActiveSubCoversList(req,data.getCoverId() );
				if(subcovers.size()>0) {
					for(TravelPolicyType subcover : subcovers) {
						TravelPolicyTypeSubCoverRes subCover = new TravelPolicyTypeSubCoverRes();
						subCover = dozermapper.map(subcover,TravelPolicyTypeSubCoverRes.class );
						
						subCoverList.add(subCover);
					}
				}
				
				travelCover.setTravelSubCover(subCoverList);
				coverList.add(travelCover);
				
				res.setTravelCover(coverList);
				
				}
			}
		
		}
		catch(Exception e) {
			e.printStackTrace();
			log.info("Log Details"+e.getMessage());
			return null;
		}
		return res;
	}


	private List<TravelPolicyType> getAllActiveSubCoversList(TravelPolicyTypeGetReq req, Integer coverId) {
		List<TravelPolicyType> list = new ArrayList<TravelPolicyType>();
		try {
			
			Date today  = new Date();
			Calendar cal = new GregorianCalendar(); 
			cal.setTime(today);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 1);
			today   = cal.getTime();
			cal.setTime(today);
			cal.set(Calendar.HOUR_OF_DAY, 1);
			cal.set(Calendar.MINUTE, 1);
			Date todayEnd   = cal.getTime();
			
			// Find Latest Record
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<TravelPolicyType> query = cb.createQuery(TravelPolicyType.class);

			// Find All
			Root<TravelPolicyType> b = query.from(TravelPolicyType.class);

			// Select
			query.select(b);

			// Amend ID Max Filter
//			Subquery<Long> amendId = query.subquery(Long.class);
//			Root<TravelPolicyType> ocpm1 = amendId.from(TravelPolicyType.class);
//			amendId.select(cb.max(ocpm1.get("amendId")));
//			Predicate a1 = cb.equal(ocpm1.get("productId"), b.get("productId"));
//			Predicate a2 = cb.equal(ocpm1.get("companyId"), b.get("companyId"));
//			Predicate a3 = cb.equal(ocpm1.get("policyTypeId"),b.get("policyTypeId"));
//			Predicate a4 = cb.equal(ocpm1.get("planTypeId"),b.get("planTypeId"));
//			Predicate a5 = cb.equal(ocpm1.get("coverId"),b.get("coverId"));
//			Predicate a7 = cb.equal(ocpm1.get("branchCode"), b.get("branchCode"));
//			Predicate a9 = cb.equal(ocpm1.get("coverId"),  b.get("coverId"));
//			Predicate a10 = cb.notEqual(ocpm1.get("subCoverId"), "0");
//			Predicate a11 = cb.equal(ocpm1.get("status"), "Y");
//	
//			amendId.where(a1,a2,a3,a4,a5,a7,a9,a10, a11);
			
			// Effective Date Max Filter
			Subquery<Date> effectiveDate = query.subquery(Date.class);
			Root<TravelPolicyType> ocpm1 = effectiveDate.from(TravelPolicyType.class);
			effectiveDate.select(cb.greatest(ocpm1.get("effectiveStartDate").as(Date.class)));
			Predicate a1 = cb.equal(ocpm1.get("productId"), b.get("productId"));
			Predicate a2 = cb.equal(ocpm1.get("companyId"), b.get("companyId"));
			Predicate a3 = cb.equal(ocpm1.get("policyTypeId"),b.get("policyTypeId"));
			Predicate a4 = cb.equal(ocpm1.get("planTypeId"),b.get("planTypeId"));
			Predicate a5 = cb.equal(ocpm1.get("coverId"),b.get("coverId"));
			Predicate a6 = cb.equal(ocpm1.get("branchCode"), b.get("branchCode"));
			Predicate a7 = cb.equal(ocpm1.get("coverId"),  b.get("coverId"));
			Predicate a8 = cb.notEqual(ocpm1.get("subCoverId"), "0");
			Predicate a10 = cb.equal(ocpm1.get("status"), "Y");
			Predicate a9 = cb.lessThanOrEqualTo(ocpm1.get("effectiveStartDate"), today);
			effectiveDate.where(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10);

			// Effective Date Max Filter
			Subquery<Date> effectiveDate2 = query.subquery(Date.class);
			Root<TravelPolicyType> ocpm2 = effectiveDate2.from(TravelPolicyType.class);
			effectiveDate2.select(cb.greatest(ocpm2.get("effectiveEndDate").as(Date.class)));
			Predicate b1 = cb.equal(ocpm2.get("productId"), b.get("productId"));
			Predicate b2 = cb.equal(ocpm2.get("companyId"), b.get("companyId"));
			Predicate b3 = cb.equal(ocpm2.get("policyTypeId"),b.get("policyTypeId"));
			Predicate b4 = cb.equal(ocpm2.get("planTypeId"),b.get("planTypeId"));
			Predicate b5 = cb.equal(ocpm2.get("coverId"),b.get("coverId"));
			Predicate b6 = cb.equal(ocpm2.get("branchCode"), b.get("branchCode"));
			Predicate b7 = cb.equal(ocpm2.get("coverId"),  b.get("coverId"));
			Predicate b8 = cb.notEqual(ocpm2.get("subCoverId"), "0");
			Predicate b9 = cb.equal(ocpm2.get("status"), "Y");
			Predicate b10 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveEndDate"), todayEnd);
			effectiveDate2.where(b1, b2, b3, b4, b5, b6, b7, b8, b9, b10);

			Predicate n1 = cb.equal(b.get("effectiveStartDate"),effectiveDate);
			Predicate n8 = cb.equal(b.get("effectiveEndDate"),effectiveDate2);
			Predicate n2 = cb.equal(b.get("productId"), req.getProductId() );	
			Predicate n3 = cb.equal(b.get("companyId"), req.getCompanyId() );		
			Predicate n4 = cb.equal(b.get("branchCode"), "99999");
			Predicate n5 = cb.equal(b.get("coverId"), coverId);
			Predicate n6 =  cb.equal(b.get("policyTypeId"), req.getPolicyTypeId()); 
			Predicate n7 =  cb.equal(b.get("planTypeId"), req.getPlanTypeId()); 
			Predicate n11 = cb.notEqual(b.get("subCoverId"), "0");
			Predicate n12 = cb.equal(b.get("status"),"Y");
			
		
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.desc(b.get("subCoverId")));
			query.where(n1, n2,n3,n4, n5, n6, n7,n8, n11, n12).orderBy(orderList);
			
			TypedQuery<TravelPolicyType> result = em.createQuery(query);
			list = result.getResultList();
			
			list = list.stream().filter(distinctByKey(o -> Arrays.asList(o.getSubCoverId()))).collect(Collectors.toList());
			list = list.stream().filter(o -> o.getSubCoverDesc()!=null).collect(Collectors.toList());

		}catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}


	private static <T> java.util.function.Predicate<T> distinctByKey(java.util.function.Function<? super T, ?> keyExtractor) {
	    Map<Object, Boolean> seen = new ConcurrentHashMap<>();
	    return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}


}
