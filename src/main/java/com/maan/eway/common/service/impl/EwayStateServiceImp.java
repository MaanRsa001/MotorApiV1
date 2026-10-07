package com.maan.eway.common.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.maan.eway.bean.EwayStateMaster;
import com.maan.eway.common.req.EwayStateReq;
import com.maan.eway.common.res.CommonRes;
import com.maan.eway.common.res.EwayCityRes;
import com.maan.eway.common.res.EwayStateRes;
import com.maan.eway.common.res.EwaySuburbRes;
import com.maan.eway.common.service.EwayStateService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
@Service
public class EwayStateServiceImp implements EwayStateService {

	@PersistenceContext
	private EntityManager em;
	@Override
	public CommonRes getStateAndRegionList(EwayStateReq req) {
		CommonRes data = new CommonRes();
		try {
			List<EwayStateRes> stateres = getStateList(req);
			if (stateres != null && !stateres.isEmpty()) {
				data.setCommonResponse(stateres);
				data.setErroCode(0);
				data.setMessage("Sucess");
				data.setIsError(false);
			} else {
				data.setCommonResponse(stateres);
				data.setErroCode(1);
				data.setMessage("failed");
				data.setIsError(true);
			}
			return data;
		} catch (Exception e) {
			System.err.print("Exception in getStateAndRegionList"
					+ " " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	
	}

	private List<EwayStateRes> getStateList(EwayStateReq req) {
		
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Tuple> cq = cb.createTupleQuery();
			Root<EwayStateMaster> root = cq.from(EwayStateMaster.class);

			cq.multiselect(root.get("stateId").alias("stateId"), root.get("stateName").alias("stateName"),
					root.get("cityId").alias("cityId"), root.get("city").alias("city")

			).distinct(true);

			List<Predicate> predicates = new ArrayList<>();
			predicates.add(cb.equal(root.get("countryId"), req.getCountryId()));

			cq.where(predicates.toArray(new Predicate[0]));
			cq.orderBy(cb.asc(root.get("stateName")));

			List<Tuple> resultList = em.createQuery(cq).getResultList();

			Map<Integer, EwayStateRes> stateMap = new LinkedHashMap<>();

			for (Tuple t : resultList) {
				Integer stateId = t.get("stateId", Integer.class);
				EwayStateRes stateRes = stateMap.get(stateId);

				if (stateRes == null) {
					stateRes = new EwayStateRes();
					stateRes.setStateId(stateId);
					stateRes.setStateName(t.get("stateName", String.class));
					stateRes.setCities(new ArrayList<>());
					stateMap.put(stateId, stateRes);
				}

				EwayCityRes cityRes = new EwayCityRes();
				cityRes.setCityId(t.get("cityId", Integer.class));
				cityRes.setCity(t.get("city", String.class));
				stateRes.getCities().add(cityRes);
			}
			return new ArrayList<>(stateMap.values());
		} catch (Exception e) {
			System.err.print("Exception in getStateList :" + e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}

}

	@Override
	public CommonRes getsuburbListfromCityId(EwayStateReq req) {
		CommonRes data = new CommonRes();
		try
		{
			List<EwaySuburbRes> res= getDisList(req);
			if (res != null && !res.isEmpty()) {
				data.setCommonResponse(res);
				data.setErroCode(0);
				data.setMessage("Sucess");
				data.setIsError(false);
			} else {
				data.setCommonResponse(res);
				data.setErroCode(1);
				data.setMessage("failed");
				data.setIsError(true);
			}

			return data;
		}
		catch(Exception e)
		{
			System.err.print("Exception in getsuburbListfromCityId " + e.getMessage());
			e.printStackTrace();
			return null;
		}
		
	}

	private List<EwaySuburbRes> getDisList(EwayStateReq req) {
		List<EwaySuburbRes> subList= new ArrayList<>();
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Tuple> cq = cb.createTupleQuery();
			Root<EwayStateMaster> root = cq.from(EwayStateMaster.class);
			
			cq.multiselect(root.get("suburbId").alias("suburbId"), root.get("suburb").alias("suburb")
			).distinct(true);
			
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(cb.equal(root.get("countryId"), req.getCountryId()));
			predicates.add(cb.equal(root.get("stateId"), req.getStateId()));
			predicates.add(cb.equal(root.get("cityId"), req.getCityId()));
			cq.where(predicates.toArray(new Predicate[0]));
			cq.orderBy(cb.asc(root.get("suburb")));
			
			List<Tuple> resultList = em.createQuery(cq).getResultList();
			
			for (Tuple t : resultList) {
				EwaySuburbRes res= new EwaySuburbRes();
				res.setSuburb(t.get("suburb",String.class));
				res.setSuburbId(t.get("suburbId",Integer.class));
				subList.add(res);
			}
			return subList ;
			
		}catch(Exception e)
		{
			System.err.print("Exception in getDisList " + e.getMessage());
			e.printStackTrace();
			return Collections.emptyList();
		}
		
	}

}
