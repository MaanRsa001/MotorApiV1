package com.maan.eway.common.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.eway.bean.BurglaryPolicyInfo;
import com.maan.eway.bean.ListItemValue;
import com.maan.eway.common.req.BurglaryPolicyInfoGetReq;
import com.maan.eway.common.req.BurglaryPolicyInfoSaveReq;
import com.maan.eway.common.res.BurglaryPolicyInfoGetRes;
import com.maan.eway.common.service.BurglaryPolicyInfoService;
import com.maan.eway.error.Error;
import com.maan.eway.repository.BurglaryPolicyInfoRepository;
import com.maan.eway.repository.ListItemValueRepository;
import com.maan.eway.res.SuccessRes;

@Service
@Transactional
public class BurglaryPolicyInfoServiceImpl implements BurglaryPolicyInfoService {

	@Autowired
	private BurglaryPolicyInfoRepository repo;

	@Autowired
	private ListItemValueRepository listrepo;

	private Logger log = LogManager.getLogger(BurglaryPolicyInfoServiceImpl.class);

	@Override
	public List<Error> validateBurglaryPolicyInfo(BurglaryPolicyInfoSaveReq req) {
		List<Error> error = new ArrayList<Error>();

		try {
			if (StringUtils.isBlank(req.getQuoteNo())) {
					error.add(new Error("01", "QuoteNo", "Please Enter QuoteNo"));
				}
				if (StringUtils.isBlank(req.getBranchCode())) {
					error.add(new Error("02", "BranchCode", "Please Enter BranchCode"));
				}
				if (StringUtils.isBlank(req.getCompanyId())) {
					error.add(new Error("03", "CompanyId", "Please Enter CompanyId"));
				}
				if (StringUtils.isBlank(req.getProductId())) {
					error.add(new Error("07", "ProductId", "Please Enter ProductId"));
				}
				if (StringUtils.isBlank(req.getRequestReferenceNo())) {
					error.add(new Error("08", "RequestReferenceNo", "Please Enter RequestReferenceNo"));
				}
				if (StringUtils.isBlank(req.getRiskId())) {
					error.add(new Error("09", "RiskId", "Please Enter RiskId"));
				}
				if (StringUtils.isBlank(req.getSectionId())) {
					error.add(new Error("10", "SectionId", "Please Enter SectionId"));
				}
				if (StringUtils.isBlank(req.getCreatedBy())) {
					error.add(new Error("11", "CreatedBy", "Please Enter CreatedBy"));
				}
				
				if (StringUtils.isBlank(req.getLeftUninhabited())) {
					error.add(new Error("12", "LeftUninhabited", "Please Select LeftUninhabited"));
				}
				else if((StringUtils.isNotBlank(req.getLeftUninhabited()))&& req.getLeftUninhabited().equalsIgnoreCase("Y")) {
				
					if (StringUtils.isBlank(req.getUninhabitedPerYear())) {
						error.add(new Error("13", "UninhabitedPerYear", "Please Enter UninhabitedPerYear"));
					}
					
					if (StringUtils.isBlank(req.getUninhabitedDays())) {
						error.add(new Error("14", "UninhabitedDays", "Please Enter UninhabitedDays"));
					}
				}
			
				if (StringUtils.isBlank(req.getPremisesOccupied())) {
					error.add(new Error("15", "PremisesOccupied", "Please Select PremisesOccupied"));
				}
				else if((StringUtils.isNotBlank(req.getPremisesOccupied()))&& req.getPremisesOccupied().equalsIgnoreCase("Y")) {
				
					if (StringUtils.isBlank(req.getExtentOfLoss())) {
						error.add(new Error("16", "ExtentOfLoss", "Please Enter ExtentOfLoss"));
					}
					
					if (StringUtils.isBlank(req.getPrecautionTaken())) {
						error.add(new Error("17", "PrecautionTaken", "Please Enter PrecautionTaken"));
					}
				}
				
		
				if (StringUtils.isBlank(req.getStockBookMaintained())) {
					error.add(new Error("18", "StockBookMaintained", "Please Select Stock Book Maintained"));
				}
				
				if (StringUtils.isBlank(req.getStockBookLoss())) {
					error.add(new Error("19", "StockBookLoss", "Please Select Stock Book Loss"));
				}
				
				if (StringUtils.isBlank(req.getLastStockTaken())) {
					error.add(new Error("20", "LastStockTaken", "Please Enter Last Stock Taken"));
				}
				
				if (StringUtils.isBlank(req.getSafetyLockerName())) {
					error.add(new Error("21", "SafetyLockerName", "Please Enter Safety Locker Name"));
				}
				if (StringUtils.isBlank(req.getBurglarResistingSafe())) {
					error.add(new Error("22", "BurglarResistingSafe", "Please Enter Burglar Resisting Safe"));
				}
				if (StringUtils.isBlank(req.getSafetyLockerHeight())) {
					error.add(new Error("22", "SafetyLockerHeight", "Please Enter Safety Locker Height"));
				}
				if (StringUtils.isBlank(req.getSafetyLockerWidth())) {
					error.add(new Error("23", "SafetyLockerWidth", "Please Enter Safety Locker Width"));
				}
				if (StringUtils.isBlank(req.getSafetyLockerDepth())) {
					error.add(new Error("24", "SafetyLockerDepth", "Please Enter Safety Locker Depth"));
				}
				if (StringUtils.isBlank(req.getSafetyLockerStructure())) {
					error.add(new Error("25", "SafetyLockerStructure", "Please Enter Safety Locker Structure"));
				}
				if (StringUtils.isBlank(req.getNumberOfKeys())) {
					error.add(new Error("26", "NumberOfKeys", "Please Enter Number Of Keys"));
				}
				if (StringUtils.isBlank(req.getOpenOptionId())) {
					error.add(new Error("27", "OpenOptionId", "Please Enter Open Option Id"));
				}
				
	} catch (Exception e) {

		log.error(e);
		e.printStackTrace();
	}
	return error;
}

	@Override
	public SuccessRes saveBurglaryPolicyInfo(BurglaryPolicyInfoSaveReq req) {
		// TODO Auto-generated method stub
		SuccessRes res = new SuccessRes();
		DozerBeanMapper dozermapper = new DozerBeanMapper();

		try {
			
			BurglaryPolicyInfo saveData = new BurglaryPolicyInfo();
			Date entryDate = new Date();
			// Drop Downs
			List<BurglaryPolicyInfo> data = repo.findByQuoteNo(req.getQuoteNo());
	        ListItemValue key = listrepo.findByItemTypeAndItemCodeAndCompanyId("KEYS_REQUIRED_TO_OPEN",req.getOpenOptionId(), req.getCompanyId());

			if (data.size()>0 && data!= null) {
				saveData = dozermapper.map(req,BurglaryPolicyInfo.class);
				saveData.setEntryDate(data.get(0).getEntryDate());
				saveData.setId(Integer.valueOf(data.get(0).getId()));
				saveData.setUpdatedDate(new Date());
				saveData.setUpdatedBy(req.getCreatedBy());
				saveData.setOpenOptionId(Integer.valueOf(req.getOpenOptionId()));
				saveData.setOpenOptionDesc(key.getItemValue());
				saveData.setStatus(data.get(0).getStatus());			
				repo.save(saveData);
				res.setSuccessId(data.get(0).getId().toString());
				res.setResponse("Updated Successfully ");
					
			}
			else {
			Long count = repo.count()+1;
			
				Integer id = count.intValue();
				saveData = dozermapper.map(req,BurglaryPolicyInfo.class);
				saveData.setId(id);
				saveData.setEntryDate(entryDate);
				saveData.setStatus("Y");
				saveData.setCreatedBy(req.getCreatedBy());
				saveData.setUpdatedDate(entryDate);
				saveData.setUpdatedBy(req.getCreatedBy());
				saveData.setOpenOptionId(Integer.valueOf(req.getOpenOptionId()));
				saveData.setOpenOptionDesc(key.getItemValue());
				repo.save(saveData);
				res.setSuccessId(id.toString());
				res.setResponse("Inserted Successfully ");
			
			}
			}
		
		catch (Exception e) {

			log.error(e);
			e.printStackTrace();
		}
		
		return res;
	}

	@Override
	public BurglaryPolicyInfoGetRes getBurglaryPolicyInfo(BurglaryPolicyInfoGetReq req) {
		// TODO Auto-generated method stub
		DozerBeanMapper dozermapper = new DozerBeanMapper();
		BurglaryPolicyInfoGetRes res = new BurglaryPolicyInfoGetRes();

		try {
		
		List<BurglaryPolicyInfo> datas = repo.findByQuoteNoAndRiskIdOrderByIdAsc(
											req.getQuoteNo(),Integer.valueOf(req.getRiskId()));
		
		if(datas.size()>0 && datas!=null) {
			
			for(BurglaryPolicyInfo data : datas) {
				res=dozermapper.map(data,BurglaryPolicyInfoGetRes.class);
				}
		}
		
	}
	catch (Exception e) {

		log.error(e);
		e.printStackTrace();
	}
	return res;
	}

}
