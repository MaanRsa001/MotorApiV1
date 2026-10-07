package com.maan.eway.common.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maan.eway.bean.CompanyProductMaster;
import com.maan.eway.bean.EserviceBuildingDetails;
import com.maan.eway.bean.EserviceCommonDetails;
import com.maan.eway.common.req.NonMotorLocationReq;
import com.maan.eway.common.req.NonMotorSaveReq;
import com.maan.eway.common.req.NonMotorSectionReq;
import com.maan.eway.repository.BuildingRiskDetailsRepository;
import com.maan.eway.repository.CommonDataDetailsRepository;
import com.maan.eway.repository.EServiceBuildingDetailsRepository;
import com.maan.eway.repository.EServiceSectionDetailsRepository;
import com.maan.eway.repository.EserviceCommonDetailsRepository;
import com.maan.eway.repository.FactorRateRequestDetailsRepository;
import com.maan.eway.repository.HomePositionMasterRepository;
import com.maan.eway.repository.MsAssetDetailsRepository;
import com.maan.eway.repository.MsCommonDetailsRepository;
import com.maan.eway.repository.MsHumanDetailsRepository;
import com.maan.eway.repository.PolicyCoverDataRepository;
import com.maan.eway.repository.SectionDataDetailsRepository;

@Service
public class NonMotorServiceImpl {
	
	@Autowired
	private EserviceCommonDetailsRepository humanRepo;
	
	@Autowired
	private HomePositionMasterRepository homeRepo;
	
	@Autowired
	private MsAssetDetailsRepository msAssetRepo;
	@Autowired
	private MsHumanDetailsRepository mshumanRepo;
	
	@Autowired
	private PolicyCoverDataRepository policyRepo;

	@Autowired
	private MsCommonDetailsRepository msCommon;
	
	@Autowired
	private FactorRateRequestDetailsRepository factRepo;

	@Autowired
	private SectionDataDetailsRepository sddRepo;
	
	@Autowired
	private CommonDataDetailsRepository maincommonRepo;
	
	@Autowired
	private BuildingRiskDetailsRepository mainBuildingRepo;
	
	@Autowired
	private EServiceSectionDetailsRepository secRepo;

	@Autowired
	private EServiceBuildingDetailsRepository buildingRepo;
	
	private Logger log = LogManager.getLogger(NonMotorServiceImpl.class);


	@Transactional
	public String nonpackageDelete(String requestReferenceNo, CompanyProductMaster product,List<NonMotorLocationReq> locationdata ) 
	{
		List<EserviceCommonDetails> findHumans;
		List<EserviceBuildingDetails> findBuildings;
		String customerId = null;
		StringBuilder msg = new StringBuilder();
		if ("N".equalsIgnoreCase(product.getPackageYn())) {
			try {
				Set<Integer> locId = locationdata.stream().map(NonMotorLocationReq::getLocationId).map(Integer::parseInt).collect(Collectors.toSet());
				findHumans = humanRepo.findByRequestReferenceNoAndLocationIdIn(requestReferenceNo, locId);
				if (findHumans != null && findHumans.size() > 0) {
					customerId = findHumans.get(0).getCustomerId();
					msg.append("ECommon:" + findHumans.size() + ", ");
					humanRepo.deleteAll(findHumans);
				}
				findBuildings = buildingRepo.findByRequestReferenceNoAndLocationIdIn(requestReferenceNo, locId);
				if (findBuildings != null && findBuildings.size() > 0) {
					customerId = findBuildings.get(0).getCustomerId();
					msg.append("EBUILDING  :" + findBuildings.size() + ", ");
					buildingRepo.deleteAll(findBuildings);
				}
				long deletedSection = secRepo.deleteByRequestReferenceNoAndLocationIdIn(requestReferenceNo, locId);
				if (deletedSection > 0)
					msg.append("ESECTION :" + deletedSection + ", ");
				long deletedAsset = msAssetRepo.deleteByRequestReferenceNoAndLocationIdIn(requestReferenceNo, locId);
				if (deletedAsset > 0)
					msg.append("Asset :" + deletedAsset + ", ");
				long deletedMsHuman = mshumanRepo.deleteByRequestReferenceNoAndLocationIdIn(requestReferenceNo,locId);
				if (deletedMsHuman > 0)
					msg.append("MsHuman :" + deletedMsHuman + ", ");
				long deletedMsCommon = msCommon.deleteByRequestreferencenoAndLocationIdIn(requestReferenceNo, locId);
				if (deletedMsCommon > 0)
					msg.append("MsCommon :" + deletedMsCommon + ", ");
				
				long fact = factRepo.deleteByRequestReferenceNoAndLocationIdIn(requestReferenceNo,locId);
		        if (fact > 0) msg.append("FactTable :"+fact+", ");
		        
//		        long home = homeRepo.deleteByRequestReferenceNo(requestReferenceNo);
//		        if (home > 0) msg.append("HomePoisitionMaster :"+home+", ");
		        
		        if(fact > 0)
		        {
			        long home = homeRepo.deleteByRequestReferenceNo(requestReferenceNo);
			        if (home > 0) msg.append("HomePoisitionMaster :"+home+", ");
		        	
		        	 long policy = policyRepo.deleteByRequestReferenceNoAndLocationIdIn(requestReferenceNo,locId);
			 	     if (policy > 0) msg.append("PolicyCoverdata :"+policy+", ");
			 	     
			 	    long mBuilding = mainBuildingRepo.deleteByRequestReferenceNoAndLocationIdIn(requestReferenceNo,locId);
			        if (mBuilding > 0) msg.append("MBuilding :"+mBuilding+", ");
			        
			        long mCommon = maincommonRepo.deleteByRequestReferenceNoAndLocationIdIn(requestReferenceNo,locId);
			        if (mCommon > 0) msg.append("MCommon :"+mCommon+", ");
			        
			       long mSection= sddRepo.deleteByRequestReferenceNoAndLocationIdIn(requestReferenceNo,locId);
			        if (mSection > 0) msg.append("mSection :"+mSection+", ");
		        }
				
				System.out.println("Full section is Deleted Successfully" + msg);
			} catch (Exception e) {
				e.printStackTrace();
				log.info("Exception Is nonpackageDelete---> " + e.getMessage());

			}
		}
		return customerId;
	}

	@Transactional
	public NonMotorSaveReq endtNonpackageDelete(String requestReferenceNo,NonMotorSaveReq req) {
		try {
			String customerId = null;
			StringBuilder msg = new StringBuilder();
		//	Set<String> locString = req.getLocationList().stream().map(NonMotorLocationReq::getLocationId).collect(Collectors.toSet());
			Set<Integer> locId = req.getLocationList().stream().map(NonMotorLocationReq::getLocationId).map(Integer::parseInt).collect(Collectors.toSet());
			List<EserviceBuildingDetails> findBuildings = buildingRepo.findByRequestReferenceNoAndLocationIdIn(requestReferenceNo,locId);

			if (findBuildings != null && !findBuildings.isEmpty()) {

				List<EserviceBuildingDetails> buildingList = findBuildings.stream().filter(t -> "Y".equalsIgnoreCase(t.getEndtoptd())).collect(Collectors.toList());

				Set<String> buildingKeys = buildingList.stream().map(b -> b.getLocationId() + "-" + b.getSectionId() + "-" + b.getCoverId()).collect(Collectors.toSet());
				setEndtOpdt(req, buildingKeys);
				customerId = findBuildings.get(0).getCustomerId();
				req.setCustomerId(customerId);
				msg.append("EBUILDING :" + findBuildings.size() + ", ");
				buildingRepo.deleteAll(findBuildings);
			}
			// ========================================
			// COMMON DETAILS
			// ========================================
			List<EserviceCommonDetails> findHumans = humanRepo.findByRequestReferenceNoAndLocationIdIn(requestReferenceNo,locId);
			if (findHumans != null && !findHumans.isEmpty()) {

				List<EserviceCommonDetails> humanList = findHumans.stream().filter(t -> "Y".equalsIgnoreCase(t.getEndtoptd())).collect(Collectors.toList());

				Set<String> humanKeys = humanList.stream().map(h -> h.getLocationId() + "-" + h.getSectionId() + "-" + h.getCoverId())
						.collect(Collectors.toSet());
				setEndtOpdt(req, humanKeys);
				customerId = findHumans.get(0).getCustomerId();
				req.setCustomerId(customerId);
				msg.append("ECommon :" + findHumans.size() + ", ");
				humanRepo.deleteAll(findHumans);
			}
			
			long deletedSection = secRepo.deleteByRequestReferenceNoAndLocationIdIn(requestReferenceNo, locId);
			if (deletedSection > 0)
				msg.append("ESECTION :" + deletedSection + ", ");
			long deletedAsset = msAssetRepo.deleteByRequestReferenceNoAndLocationIdIn(requestReferenceNo, locId);
			if (deletedAsset > 0)
				msg.append("Asset :" + deletedAsset + ", ");
			long deletedMsHuman = mshumanRepo.deleteByRequestReferenceNoAndLocationIdIn(requestReferenceNo,locId);
			if (deletedMsHuman > 0)
				msg.append("MsHuman :" + deletedMsHuman + ", ");
			long deletedMsCommon = msCommon.deleteByRequestreferencenoAndLocationIdIn(requestReferenceNo, locId);
			if (deletedMsCommon > 0)
				msg.append("MsCommon :" + deletedMsCommon + ", ");
			
			long fact = factRepo.deleteByRequestReferenceNoAndLocationIdIn(requestReferenceNo,locId);
	        if (fact > 0) msg.append("FactTable :"+fact+", ");
	        if(fact > 0)
	        {
		        long home = homeRepo.deleteByRequestReferenceNo(requestReferenceNo);
		        if (home > 0) msg.append("HomePoisitionMaster :"+home+", ");
	        	
	        	 long policy = policyRepo.deleteByRequestReferenceNoAndLocationIdIn(requestReferenceNo,locId);
		 	     if (policy > 0) msg.append("PolicyCoverdata :"+policy+", ");
		 	     
		 	    long mBuilding = mainBuildingRepo.deleteByRequestReferenceNoAndLocationIdIn(requestReferenceNo,locId);
		        if (mBuilding > 0) msg.append("MBuilding :"+mBuilding+", ");
		        
		        long mCommon = maincommonRepo.deleteByRequestReferenceNoAndLocationIdIn(requestReferenceNo,locId);
		        if (mCommon > 0) msg.append("MCommon :"+mCommon+", ");
		        
		       long mSection= sddRepo.deleteByRequestReferenceNoAndLocationIdIn(requestReferenceNo,locId);
		        if (mSection > 0) msg.append("mSection :"+mSection+", ");
	        }
			
			System.out.println("Full section is Deleted Successfully" + msg);

		} catch (Exception e) {

			e.printStackTrace();
			log.info("Exception Is nonpackageDelete---> " + e.getMessage());
		}

		return req;
	}
	
	private void setEndtOpdt(NonMotorSaveReq req, Set<String> keys) {

		if (req.getLocationList() != null) {

			for (NonMotorLocationReq locReq : req.getLocationList()) {

				String locationId = locReq.getLocationId();

				if (locReq.getSectionList() != null) {

					for (NonMotorSectionReq sectionReq : locReq.getSectionList()) {

						String key = locationId + "-" + sectionReq.getSectionId() + "-" + sectionReq.getCoverId();

						if (keys.contains(key)) {

							sectionReq.setEndtOpdt("Y");
						}
					}
				}
			}
		}
	}

	@Transactional
	public String packagedeleteSection(Integer locId, String requestReferenceNo, CompanyProductMaster product,Set<String> distinctSectionIds, Set<Integer> sectionin){
		List<EserviceCommonDetails> findHumans;
		List<EserviceBuildingDetails> findBuildings;
		StringBuilder msg = new StringBuilder();
		String customerId = null;
		if (!("N".equalsIgnoreCase(product.getPackageYn()))) {
			try {
				findHumans = humanRepo.findByRequestReferenceNoAndSectionIdInAndLocationId(requestReferenceNo,
						distinctSectionIds, locId);
				findBuildings = buildingRepo.findByRequestReferenceNoAndSectionIdInAndLocationId(requestReferenceNo,
						distinctSectionIds, locId);
				if (findHumans != null && findHumans.size() > 0) {
					customerId = findHumans.get(0).getCustomerId();
					msg.append("ECommon  :" + findHumans.size() + ", ");
					humanRepo.deleteAll(findHumans);
				}
				if (findBuildings != null && findBuildings.size() > 0) {
					customerId = findBuildings.get(0).getCustomerId();
					msg.append("EBUILDING :" + findBuildings.size() + ", ");
					buildingRepo.deleteAll(findBuildings);
				}
				long deletedSection  = secRepo.deleteByRequestReferenceNoAndSectionIdInAndLocationId(requestReferenceNo,distinctSectionIds, locId);
				if (deletedSection > 0) msg.append("ESECTION :" +deletedSection+", ");
				long deletedAsset = msAssetRepo.deleteByRequestReferenceNoAndLocationIdAndSectionIdIn(requestReferenceNo, locId,sectionin);
				long deletedMsHuman  = mshumanRepo.deleteByRequestReferenceNoAndLocationIdAndSectionidIn(requestReferenceNo, locId,sectionin);
				long deletedMsCommon  = msCommon.deleteByRequestreferencenoAndLocationIdAndSectionIdIn(requestReferenceNo, locId,sectionin);
				// Delete Old Records
				if (deletedMsHuman > 0) msg.append("MsHuman :"+deletedMsHuman+", ");
				if (deletedMsCommon > 0) msg.append("MsCommon :"+deletedMsCommon+", ");
				if (deletedAsset > 0) msg.append("Asset :"+deletedAsset+", ");
				
				long fact = factRepo.deleteByRequestReferenceNoAndLocationIdAndSectionIdIn(requestReferenceNo,locId,sectionin);
		        if (fact > 0) msg.append("FactTable :"+fact+", ");
		        
		        if(fact > 0)
		        {
			        long home = homeRepo.deleteByRequestReferenceNo(requestReferenceNo);
			        if (home > 0) msg.append("HomePoisitionMaster :"+home+", ");
		        	
		        	 long policy = policyRepo.deleteByRequestReferenceNoAndLocationIdAndSectionIdIn(requestReferenceNo,locId,sectionin);
			 	     if (policy > 0) msg.append("PolicyCoverdata :"+policy+", ");
			 	     
			 	    long mBuilding = mainBuildingRepo.deleteByRequestReferenceNoAndLocationIdAndSectionIdIn(requestReferenceNo,locId,distinctSectionIds);
			        if (mBuilding > 0) msg.append("MBuilding :"+mBuilding+", ");
			        
			        long mCommon = maincommonRepo.deleteByRequestReferenceNoAndLocationIdAndSectionIdIn(requestReferenceNo,locId,distinctSectionIds);
			        if (mCommon > 0) msg.append("MCommon :"+mCommon+", ");
			        
			       long mSection= sddRepo.deleteByRequestReferenceNoAndLocationIdAndSectionIdIn(requestReferenceNo,locId,distinctSectionIds);
			        if (mSection > 0) msg.append("mSection :"+mSection+", ");
		        }
				System.out.println("Deleted Successfully" + msg);
			} catch (Exception e) {
				e.printStackTrace();
				log.info("Exception Is packagedeleteSection ---> " + e.getMessage());

			}
		}
		return customerId;
	}

	@Transactional
	public NonMotorSaveReq endtPackageDelete(String requestReferenceNo, NonMotorSaveReq req, Integer locId,
			Set<Integer> sectionin, Set<String> distinctSectionIds) {
		try
		{
			String customerId = null;
			StringBuilder msg = new StringBuilder();
			List<EserviceCommonDetails> findHumans;
			List<EserviceBuildingDetails> findBuildings;
			findHumans = humanRepo.findByRequestReferenceNoAndSectionIdInAndLocationId(requestReferenceNo,
					distinctSectionIds, locId);
			findBuildings = buildingRepo.findByRequestReferenceNoAndSectionIdInAndLocationId(requestReferenceNo,
					distinctSectionIds, locId);
		
			if (findBuildings != null && findBuildings.size() > 0) {

				List<EserviceBuildingDetails> buildingList = findBuildings.stream().filter(t -> "Y".equalsIgnoreCase(t.getEndtoptd())).collect(Collectors.toList());

				Set<String> buildingKeys = buildingList.stream().map(b -> b.getLocationId() + "-" + b.getSectionId() + "-" + b.getCoverId()).collect(Collectors.toSet());
				setEndtOpdt(req, buildingKeys);
				customerId = findBuildings.get(0).getCustomerId();
				req.setCustomerId(customerId);
				msg.append("EBUILDING :" + findBuildings.size() + ", ");
				buildingRepo.deleteAll(findBuildings);
			}
			if (findHumans != null && findHumans.size() > 0) {

				List<EserviceCommonDetails> humanList = findHumans.stream().filter(t -> "Y".equalsIgnoreCase(t.getEndtoptd())).collect(Collectors.toList());

				Set<String> humanKeys = humanList.stream().map(h -> h.getLocationId() + "-" + h.getSectionId() + "-" + h.getCoverId())
						.collect(Collectors.toSet());
				setEndtOpdt(req, humanKeys);
				customerId = findHumans.get(0).getCustomerId();
				req.setCustomerId(customerId);
				msg.append("ECommon :" + findHumans.size() + ", ");
				humanRepo.deleteAll(findHumans);
			}
			long deletedSection  = secRepo.deleteByRequestReferenceNoAndSectionIdInAndLocationId(requestReferenceNo,distinctSectionIds, locId);
			if (deletedSection > 0) msg.append("ESECTION :" +deletedSection+", ");
			long deletedAsset = msAssetRepo.deleteByRequestReferenceNoAndLocationIdAndSectionIdIn(requestReferenceNo, locId,sectionin);
			long deletedMsHuman  = mshumanRepo.deleteByRequestReferenceNoAndLocationIdAndSectionidIn(requestReferenceNo, locId,sectionin);
			long deletedMsCommon  = msCommon.deleteByRequestreferencenoAndLocationIdAndSectionIdIn(requestReferenceNo, locId,sectionin);
			// Delete Old Records
			if (deletedMsHuman > 0) msg.append("MsHuman :"+deletedMsHuman+", ");
			if (deletedMsCommon > 0) msg.append("MsCommon :"+deletedMsCommon+", ");
			if (deletedAsset > 0) msg.append("Asset :"+deletedAsset+", ");
			
			long fact = factRepo.deleteByRequestReferenceNoAndLocationIdAndSectionIdIn(requestReferenceNo,locId,sectionin);
	        if (fact > 0) msg.append("FactTable :"+fact+", ");
	        
	        if(fact > 0)
	        {
		        long home = homeRepo.deleteByRequestReferenceNo(requestReferenceNo);
		        if (home > 0) msg.append("HomePoisitionMaster :"+home+", ");
	        	
	        	 long policy = policyRepo.deleteByRequestReferenceNoAndLocationIdAndSectionIdIn(requestReferenceNo,locId,sectionin);
		 	     if (policy > 0) msg.append("PolicyCoverdata :"+policy+", ");
		 	     
		 	    long mBuilding = mainBuildingRepo.deleteByRequestReferenceNoAndLocationIdAndSectionIdIn(requestReferenceNo,locId,distinctSectionIds);
		        if (mBuilding > 0) msg.append("MBuilding :"+mBuilding+", ");
		        
		        long mCommon = maincommonRepo.deleteByRequestReferenceNoAndLocationIdAndSectionIdIn(requestReferenceNo,locId,distinctSectionIds);
		        if (mCommon > 0) msg.append("MCommon :"+mCommon+", ");
		        
		       long mSection= sddRepo.deleteByRequestReferenceNoAndLocationIdAndSectionIdIn(requestReferenceNo,locId,distinctSectionIds);
		        if (mSection > 0) msg.append("mSection :"+mSection+", ");
	        }
			System.out.println("Deleted Successfully" + msg);
			
		}catch (Exception e) {
			e.printStackTrace();
			log.info("Exception Is packagedeleteSection ---> " + e.getMessage());

		}
		return req;
	}

}
