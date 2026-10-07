package com.maan.eway.agri.serviceImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.eway.agri.entity.AgricultureMaster;
import com.maan.eway.agri.repo.AgricultureMasterRepository;
import com.maan.eway.agri.req.AgricultureMasterReq;
import com.maan.eway.agri.req.agriGetreq;
import com.maan.eway.agri.req.cropDropDownReq;
import com.maan.eway.agri.res.AgricultureMasterRes;
import com.maan.eway.agri.res.CropDropdownRes;
import com.maan.eway.agri.res.SuccessResponse;
import com.maan.eway.agri.res.aezResponse;
import com.maan.eway.agri.res.cropDropRes;
import com.maan.eway.agri.service.AgricultureMasterService;
import com.maan.eway.repository.ListItemValueRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AgricultureMasterServiceImpl implements AgricultureMasterService {

	@Autowired
	private AgricultureMasterRepository repository;
	
	@Autowired
	private ListItemValueRepository listRepo;

	private final ModelMapper mapper = new ModelMapper();

	@Override
	public SuccessResponse saveorupdate(AgricultureMasterReq req) {
	    Integer sno = req.getSno();

	  
	    if (sno == null || sno == 0) {
	        Integer maxSno = repository.findMaxSnoByCompanyIdAndProductId(req.getCompanyId(), req.getProductId());
	        sno = (maxSno == null) ? 1 : maxSno + 1;
	    }


	    List<AgricultureMaster> history = repository.findByCompanyIdAndProductIdAndSnoOrderByAmendIdDesc(
	            req.getCompanyId(), req.getProductId(), sno);

	    int newAmendId = 0;
	    AgricultureMaster entity = mapper.map(req, AgricultureMaster.class);
	    entity.setCompanyId(req.getCompanyId());
	    entity.setProductId(req.getProductId());
	    entity.setSno(sno);
	    entity.setEntryDate(new Date());

	    if (!history.isEmpty()) {
	        AgricultureMaster latest = history.get(0);
	        if (req.getEffectiveDateStart().after(latest.getEffectiveDateStart())) {
	            latest.setEffectiveDateEnd(new Date(req.getEffectiveDateStart().getTime() - 86400000));
	            repository.save(latest);
	            newAmendId = latest.getAmendId() + 1;
	        } else {
	            newAmendId = latest.getAmendId();
	        }
	    }

	    entity.setAmendId(newAmendId);

	 
	    if (req.getEffectiveDateEnd() != null) {
	        entity.setEffectiveDateEnd(req.getEffectiveDateEnd());
	    } else {
	        entity.setEffectiveDateEnd(new GregorianCalendar(2050, Calendar.DECEMBER, 31).getTime());
	    }
	    entity.setYieldPercentage(req.getYieldPercentage());
	    repository.save(entity);
	    return history.isEmpty() ? new SuccessResponse("Saved Successfully") : new SuccessResponse("Updated Successfully");
	}

	@Override
	public List<AgricultureMasterRes> getAll(Integer companyId, Integer productId) {
		 List<AgricultureMaster> list = repository.findByCompanyIdAndProductIdOrderByAmendIdDesc(companyId, productId);
		 List<AgricultureMasterRes> result=new ArrayList<>();
		 Map<String, AgricultureMaster> latestMap=new HashMap<>();
		 
		 for(AgricultureMaster master:list) {
			 String key=master.getSno()+"-"+master.getCompanyId()+"-"+master.getProductId();
			 if (!latestMap.containsKey(key)) {
	                latestMap.put(key, master);
	            }
		 }
		 for(AgricultureMaster master:latestMap.values()) {
			 result.add(mapper.map(master, AgricultureMasterRes.class));
		 }
		 return result;
	     
	}

	@Override
	public AgricultureMasterRes getById(agriGetreq req) {
		List<AgricultureMaster> list=repository.findByCompanyIdAndProductIdOrderByAmendIdDesc(
				req.getCompanyId(), req.getProductId());
		return list.stream()
				.filter(e->e.getSno().equals(req.getSNo()))
				.findFirst()
				.map(e->mapper.map(e, AgricultureMasterRes.class))
				.orElse(null);
	}

	@Override
	public List<aezResponse> getAllAez(Integer aez) {
		List<AgricultureMaster> list=repository.findByAez(aez);
		return list.stream()
		.map(e-> new aezResponse(e.getPerHaCost(),e.getCropDesc()))
		.collect(Collectors.toList());
	}
	
	@Override
	public List<CropDropdownRes> getAllUniqueCropIdAndDesc() {
	    return repository.findDistinctCropIdAndDesc().stream()
	            .map(arr -> new CropDropdownRes(String.valueOf(arr[0]), (String) arr[1]))
	            .collect(Collectors.toList());
	}

	@Override
	public List<cropDropRes> getCropDrop(cropDropDownReq req) {
		return listRepo.findByCompanyIdAndItemType(req.getCompanyId(),req.getItemType()).stream()
				.map(e->new cropDropRes(e.getItemCode(),e.getItemValue()))
				.collect(Collectors.toList());
	}



}
