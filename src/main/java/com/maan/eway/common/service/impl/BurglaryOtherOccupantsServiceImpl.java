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

import com.maan.eway.bean.BurglaryOtherOccupants;
import com.maan.eway.common.req.BurglaryOtherOccupantsGetReq;
import com.maan.eway.common.req.BurglaryOtherOccupantsSaveReq;
import com.maan.eway.common.res.BurglaryOtherOccupantsGetRes;
import com.maan.eway.common.res.BurglaryOtherOccupantsListRes;
import com.maan.eway.common.service.BurglaryOtherOccupantsService;
import com.maan.eway.error.Error;
import com.maan.eway.repository.BurglaryOtherOccupantsRepository;
import com.maan.eway.res.SuccessRes;

@Service
@Transactional
public class BurglaryOtherOccupantsServiceImpl implements BurglaryOtherOccupantsService {

	@Autowired
	private BurglaryOtherOccupantsRepository repo;

	private Logger log = LogManager.getLogger(BurglaryOtherOccupantsServiceImpl.class);

	@Override
	public List<Error> validateBurglaryOtherOccupants(List<BurglaryOtherOccupantsSaveReq> reqList) {
		List<Error> error = new ArrayList<Error>();

		try {
			Long row =0L;
			for(BurglaryOtherOccupantsSaveReq req : reqList) {
				row = row+1;
				if (StringUtils.isBlank(req.getQuoteNo())) {
					error.add(new Error("01", "QuoteNo", "Please Enter QuoteNo"+row));
				}
				if (StringUtils.isBlank(req.getBranchCode())) {
					error.add(new Error("02", "BranchCode", "Please Enter BranchCode"+row));
				}
				if (StringUtils.isBlank(req.getCompanyId())) {
					error.add(new Error("03", "CompanyId", "Please Enter CompanyId"+row));
				}
				if (StringUtils.isBlank(req.getHowSecured())) {
					error.add(new Error("04", "HowSecured", "Please Enter HowSecured"+row));
				}
				if (StringUtils.isBlank(req.getName())) {
					error.add(new Error("05", "Name", "Please Enter Name"+row));
				}
				else if ((StringUtils.isNotBlank(req.getName()))&&!req.getName().matches("[A-Z a-z]+")) {
					error.add(new Error("05", "Name", "Please Enter Valid Name In Name In Row No:" + row ));
				}

				if (StringUtils.isBlank(req.getOccupation())) {
					error.add(new Error("06", "Occupation", "Please Enter Occupation"+row));
				}
				else if ((StringUtils.isNotBlank(req.getOccupation()))&&!req.getOccupation().matches("[A-Z a-z]+")) {
					error.add(new Error("06", "Occupation", "Please Enter Valid Occupation In Occupation In Row No:" + row ));
				}
				if (StringUtils.isBlank(req.getProductId())) {
					error.add(new Error("07", "ProductId", "Please Enter ProductId"+row));
				}
				if (StringUtils.isBlank(req.getRequestReferenceNo())) {
					error.add(new Error("08", "RequestReferenceNo", "Please Enter RequestReferenceNo"+row));
				}
				if (StringUtils.isBlank(req.getRiskId())) {
					error.add(new Error("09", "RiskId", "Please Enter RiskId"+row));
				}
				if (StringUtils.isBlank(req.getSectionId())) {
					error.add(new Error("10", "SectionId", "Please Enter SectionId"+row));
				}
				if (StringUtils.isBlank(req.getCreatedBy())) {
					error.add(new Error("11", "CreatedBy", "Please Enter CreatedBy"+row));
				}
			}
		
	} catch (Exception e) {

		log.error(e);
		e.printStackTrace();
	}
	return error;
}

	@Override
	public SuccessRes saveBurglaryOtherOccupants(List<BurglaryOtherOccupantsSaveReq> reqList) {
		// TODO Auto-generated method stub
		SuccessRes res = new SuccessRes();
		DozerBeanMapper dozermapper = new DozerBeanMapper();

		try {
			
			String quoteNo = reqList.get(0).getQuoteNo() ;
			
			BurglaryOtherOccupants saveData = new BurglaryOtherOccupants();
			Date entryDate = new Date();
			Integer riskid=0;
			// Drop Downs
			List<BurglaryOtherOccupants> data = repo.findByQuoteNo(reqList.get(0).getQuoteNo());
			if (data != null) {
				repo.deleteAll(data);
			}
			Long count = repo.count()+1;
			
			for(BurglaryOtherOccupantsSaveReq req : reqList) {
				Integer id = count.intValue();
				saveData = dozermapper.map(req,BurglaryOtherOccupants.class);
				saveData.setId(id);
				saveData.setEntryDate(new Date());
				saveData.setStatus("Y");
				saveData.setCreatedBy(req.getCreatedBy());
				repo.save(saveData);
				count++;
			}
			res.setSuccessId(quoteNo);
			res.setResponse("Successfully Added");
			}
		
		catch (Exception e) {

			log.error(e);
			e.printStackTrace();
		}
		
		return res;
	}

	@Override
	public BurglaryOtherOccupantsGetRes getBurglaryQuoteNo(BurglaryOtherOccupantsGetReq req) {
		// TODO Auto-generated method stub
		DozerBeanMapper dozermapper = new DozerBeanMapper();
		BurglaryOtherOccupantsGetRes res = new BurglaryOtherOccupantsGetRes();

		try {
		
		List<BurglaryOtherOccupants> datas = repo.findByQuoteNoAndRiskIdOrderByIdAsc(
											req.getQuoteNo(),req.getRiskId());
		
		if(datas.size()>0 && datas!=null) {
			List<BurglaryOtherOccupantsListRes> resList = new ArrayList<BurglaryOtherOccupantsListRes>();
			for(BurglaryOtherOccupants data : datas) {
				BurglaryOtherOccupantsListRes res1 = new BurglaryOtherOccupantsListRes();
				res=dozermapper.map(data,BurglaryOtherOccupantsGetRes.class);
				res1.setName(data.getName()==null?"":data.getName());
				res1.setOccupation(data.getOccupation()==null?"":data.getOccupation());
				res1.setHowSecured(data.getHowSecured()==null?"":data.getHowSecured());
				res1.setId(data.getId().toString()==null?"":data.getId().toString());
				resList.add(res1);
			}
			res.setBurglaryOtherOccupants(resList);
		}
		
	}
	catch (Exception e) {

		log.error(e);
		e.printStackTrace();
	}
	return res;
	}

}
