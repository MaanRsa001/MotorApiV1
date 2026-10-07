package com.maan.eway.common.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.eway.bean.BuildingDetails;
import com.maan.eway.bean.BuildingRiskDetails;
import com.maan.eway.bean.ContentAndRisk;
import com.maan.eway.bean.EserviceBuildingDetails;
import com.maan.eway.bean.EserviceSectionDetails;
import com.maan.eway.bean.ListItemValue;
import com.maan.eway.bean.ProductEmployeeDetails;
import com.maan.eway.bean.ProductEmployeeDetailsArch;
import com.maan.eway.bean.SectionDataDetails;
import com.maan.eway.common.req.AdditionalValidationReq;
import com.maan.eway.common.req.BuildingDetailsGetAllReq;
import com.maan.eway.common.req.BuildingDetailsGetReq;
import com.maan.eway.common.req.BuildingDetailsSaveReq;
import com.maan.eway.common.res.BuildingDetailsGetRes;
import com.maan.eway.common.service.BuildingDetailsService;
import com.maan.eway.repository.BuildingDetailsRepository;
import com.maan.eway.repository.BuildingRiskDetailsRepository;
import com.maan.eway.repository.ContentAndRiskRepository;
import com.maan.eway.repository.DocumentTransactionDetailsRepository;
import com.maan.eway.repository.EServiceBuildingDetailsRepository;
import com.maan.eway.repository.EServiceSectionDetailsRepository;
import com.maan.eway.repository.ListItemValueRepository;
import com.maan.eway.repository.ProductEmployeeDetailsArchRepository;
import com.maan.eway.repository.ProductEmployeesDetailsRepository;
import com.maan.eway.repository.SectionDataDetailsRepository;
import com.maan.eway.repository.SectionMasterRepository;
import com.maan.eway.res.SuccessRes;
import com.maan.eway.res.SuccessRes1;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class BuildingDetailsServiceImpl implements BuildingDetailsService {

	@Autowired
	private BuildingDetailsRepository repo;

	@Autowired
	private ListItemValueRepository listrepo;

	@Autowired
	private SectionMasterRepository sectionrepo;
	
	@Autowired
	private EServiceBuildingDetailsRepository eserBuildingRepo ;
	
	@Autowired
	private EServiceSectionDetailsRepository eserSecRepo ;
	
	@Autowired
	private ContentAndRiskRepository contentRepo ;
	
	@Autowired
	private ProductEmployeesDetailsRepository paccRepo ;
	
	@Autowired
	private DocumentTransactionDetailsRepository docTransRepo;
	
	@Autowired
	private ProductEmployeesDetailsRepository productRepo;
	
	@Autowired
	private GenerateSeqNoServiceImpl genSeqNoService;
	
	@Autowired
	private ProductEmployeeDetailsArchRepository empArchRepo;
	
	@Autowired
	private ProductEmployeesDetailsRepository empRepo;
	
	@Autowired
	private SectionDataDetailsRepository secRepo;
	
	@Autowired
	private BuildingRiskDetailsRepository buildRiskRepo ;

	private Logger log = LogManager.getLogger(EserviceBuildingDetailsServiceImpl.class);

	@Override
	public List<String> validatebuildingDetails(List<BuildingDetailsSaveReq> reqList) {		
		List<String> error = new ArrayList<String>();

		try {
			
			Long row = 0L ;
			BigDecimal sumInsured = BigDecimal.ZERO ;
			String quoteNo = "" ;
			String sectionId = reqList.size() > 0 ? reqList.get(0).getSectionId() : "1";
			List<String> locations = new ArrayList<String>();
			List<EserviceBuildingDetails> buidingData = new ArrayList<EserviceBuildingDetails>();
			List<EserviceSectionDetails> domesticSection =  new ArrayList<EserviceSectionDetails>();
			List<EserviceSectionDetails> parSection =  new ArrayList<EserviceSectionDetails>();
			
			if(reqList!=null && reqList.size()> 0 && StringUtils.isNotBlank(reqList.get(0).getQuoteNo() )  ) {
				//buidingData = eserBuildingRepo.findByQuoteNoAndSectionId(reqList.get(0).getQuoteNo() ,sectionId );
				buidingData = eserBuildingRepo.findByRequestReferenceNoAndSectionId(reqList.get(0).getRequestReferenceNo(), sectionId) ;          
				List<EserviceSectionDetails> sectionList =  eserSecRepo.findByRequestReferenceNoOrderByRiskIdAsc(buidingData.get(0).getRequestReferenceNo() );
				
				domesticSection = sectionList.stream().filter( o -> o.getSectionId().equalsIgnoreCase("1") ).collect(Collectors.toList()) ; 
				parSection = sectionList.stream().filter( o -> o.getSectionId().equalsIgnoreCase("40") ).collect(Collectors.toList()) ; 
				
			}
			
			for(BuildingDetailsSaveReq req : reqList) {
				row = row +1;
				if (StringUtils.isBlank(req.getRequestReferenceNo())) {
					error.add("1186"+","+row);
//					error.add(new Error("01", "QuoteNo", "Please Select QuoteNo in Row : " + row));
				}else {
					quoteNo = req.getQuoteNo() ;
					//buidingData = eserBuildingRepo.findByQuoteNoAndSectionId(quoteNo,sectionId);
//					buidingData = eserBuildingRepo.findByRiskIdAndRequestReferenceNoAndSectionId(Integer.valueOf(req.getRiskId()),req.getRequestReferenceNo(),sectionId);
				}
				
				if (StringUtils.isBlank(req.getSectionId())) {
					error.add("1187"+","+row);
//					error.add(new Error("01", "SectionId", "Please Select Section Id In Row No : " + row ));
				}
				
				if(domesticSection.size() > 0 ||parSection.size() > 0    ) {
					
					
					EserviceSectionDetails details = domesticSection.size() > 0 ? domesticSection.get(0) : parSection.size() > 0 ? parSection.get(0) : null;
					String companyId = "";
					String productId = "";
					
					if(details != null) {
						
					 companyId = StringUtils.isNotBlank(details.getCompanyId()) ? details.getCompanyId() : "" ;
				     productId = StringUtils.isNotBlank(details.getProductId() ) ? details.getProductId() : "";
					}
					
					if (StringUtils.isBlank(req.getBuildingSuminsured())) {
						error.add("1188"+","+row);
//						error.add(new Error("08", "BuildingSuminsured", "Please Enter BuildingSuminsured In Row No : " + row));
					} else if (!req.getBuildingSuminsured().matches("[0-9.]+")  || Double.valueOf(req.getBuildingSuminsured()) <=0 
							&& !companyId.equals("100004") && !productId.equals("6") ) {
						error.add("1189"+","+row);
//						error.add(new Error("09", "BuildingSuminsured", "Please Enter Valid Number In BuildingSuminsured In Row No : " + row));
					} else {
						sumInsured = sumInsured.add(new BigDecimal(req.getBuildingSuminsured()));
					}
				}
				
				if (StringUtils.isBlank(req.getBuildingAddress())) {
					error.add("1190"+","+row);
//					error.add(new Error("03", "BuildingAddress", "Please Enter BuildingAddress In Row No : " + row));
				} else if (StringUtils.isNotBlank(req.getBuildingAddress())  &&req.getBuildingAddress().length()>1000 ) {
					error.add("1191"+","+row);
//					error.add(new Error("03", "BuildingAddress", "Please Enter BuildingAddress within 1000 Characters : " + row));
				}
				
				if (StringUtils.isBlank(req.getLocationName()) ) {
					error.add("1192"+","+row);
//					error.add(new Error("03", "LocationName", "Please Enter LocationName In Row No : " + row));
				}
				else if ((StringUtils.isNotBlank(req.getLocationName()))&&!req.getLocationName().matches("[A-Z a-z0-9]+")) {
					error.add("1193"+","+row);
//					error.add(new Error("03", "LocationName", "Please Enter Valid Name In Location Name In Row No:" + row ));
				}

				else {
					List<String> filterLocations =  locations.stream().filter( o -> o.equals(req.getLocationName().trim().toLowerCase()) ).collect(Collectors.toList());
					if(filterLocations.size()> 0) {
						error.add("1194"+","+row);
//						error.add(new Error("03", "LocationName", "LocationName Duplicate In Row No : " + row));
					} else {
						locations.add(req.getLocationName().trim().toLowerCase());
					}
				}
			}
			
//			if(StringUtils.isNotBlank(quoteNo)  ) {
//				if(buidingData.getBuildingSuminsured()!=null && sumInsured.compareTo(buidingData.getBuildingSuminsured()) > 0 && ( domesticSection.size() > 0 ||parSection.size() > 0    )) {
//					error.add("1195"+","+row);
////					error.add(new Error("03", "BuildingSumINsured", "Locations Total SumInsured Greater Than Actual SumInsured" ));
//				}
//			}
		
	} catch (Exception e) {

		log.error(e);
		e.printStackTrace();
	}
	return error;
}

	@Override
	@Transactional
	public SuccessRes1 savebuildingDetails(List<BuildingDetailsSaveReq> reqList) {
		// TODO Auto-generated method stub
		SuccessRes1 res = new SuccessRes1();
		DozerBeanMapper dozermapper = new DozerBeanMapper();
		try {
			String requestno=reqList.get(0).getRequestReferenceNo();
			String quoteNo = reqList.get(0).getQuoteNo() ;
			BuildingDetails saveData = new BuildingDetails();
			String sectionId = reqList.get(0).getSectionId();
			//EserviceBuildingDetails eserBuildingData = eserBuildingRepo.findByQuoteNoAndSectionId(quoteNo,sectionId);
			EserviceBuildingDetails eserBuildingData = eserBuildingRepo.findByRiskIdAndRequestReferenceNoAndSectionId(Integer.valueOf(reqList.get(0).getRiskId()),requestno,sectionId);
			EserviceSectionDetails parSection =  new EserviceSectionDetails();
			Date entryDate = new Date();
			Integer riskid=Integer.valueOf(reqList.get(0).getRiskId());
			// Drop Downs
		//	List<BuildingDetails> data = repo.findByQuoteNo(reqList.get(0).getQuoteNo());
			BuildingDetails data = repo.findByRequestReferenceNoAndRiskId(requestno,riskid);
			parSection=eserSecRepo.findByRequestReferenceNoAndSectionIdAndRiskId(requestno, sectionId, riskid);
			if (data != null) {
				repo.delete(data);
			}
			ListItemValue inbuildconstruct  = new ListItemValue (); 
				for(BuildingDetailsSaveReq req : reqList) {
					if(eserBuildingData!=null) {
					 inbuildconstruct = listrepo.findByItemTypeAndItemCodeAndCompanyId("CONSTRUCT_TYPE", eserBuildingData.getInbuildConstructType(), eserBuildingData.getCompanyId());
						saveData.setBuildingFloors(eserBuildingData.getBuildingFloors());
						saveData.setBuildingUsageId(eserBuildingData.getBuildingUsageId());
						saveData.setBuildingUsageDesc(eserBuildingData.getBuildingUsageDesc());
						saveData.setBuildingAge(eserBuildingData.getBuildingAge());
						saveData.setInbuildConstructType(eserBuildingData.getInbuildConstructType() );
						
					}
					
//					List<SectionMaster> sectionName = sectionrepo.findBySectionIdOrderByAmendIdDesc(Integer.valueOf(req.getSectionId())); 	
					riskid = StringUtils.isBlank(req.getRiskId()) ? riskid + 1 : Integer.valueOf(req.getRiskId())  ;
				saveData=dozermapper.map(req, BuildingDetails.class);
				saveData.setUpdatedDate(new Date());
				saveData.setUpdatedBy(req.getCreatedBy());
				saveData.setLocationName(req.getLocationName().trim() );
				saveData.setEntryDate(entryDate);
				saveData.setCreatedBy(req.getCreatedBy());
				saveData.setStatus("Y");
				saveData.setRequestReferenceNo(req.getRequestReferenceNo());
				//saveData.setBuildingPurpose(buidingpurpose.getItemValue());
				saveData.setBuildingSuminsured(req.getBuildingSuminsured()==null?null:new BigDecimal(req.getBuildingSuminsured()));
				
				
				if (eserBuildingData!=null &&  StringUtils.isNotBlank(eserBuildingData.getExchangeRate().toString())) {
					BigDecimal exRate=eserBuildingData.getExchangeRate();
					if (StringUtils.isNotBlank(req.getBuildingSuminsured())) {
						saveData.setBuildingSumInsuredLC(new BigDecimal(req.getBuildingSuminsured()).multiply(exRate));
					} else {
						saveData.setBuildingSumInsuredLC(BigDecimal.ZERO);
					}
				}
				
				
				saveData.setBuildingAreaSqm(req.getBuildingAreaSqm()==null?null:Double.valueOf(req.getBuildingAreaSqm()));
				saveData.setInbuildConstructTypeDesc(inbuildconstruct!=null? inbuildconstruct.getItemValue():"" );
				saveData.setWithoutInhabitantDays(req.getWithoutInhabitantDays()==null?null:Integer.valueOf(req.getWithoutInhabitantDays()));
			//sectionName.get(0).getSectionName());
				// Building Age Calculation
				saveData.setRiskId(riskid);
				saveData.setQuoteNo(null);
				saveData.setSectionId(sectionId);
			//	riskid++;
				if(parSection!=null) {saveData.setSectionDesc(parSection.getSectionName());}
				
				repo.saveAndFlush(saveData);
				res.setSuccessId(req.getRequestReferenceNo());
				res.setResponse("Updted Successful");
				
				}
				//List<BuildingDetails> loc = repo.findByQuoteNo(quoteNo);
				List<BuildingDetails> loc = repo.findByRequestReferenceNo(requestno);
				List<Integer> locIds = loc.stream().map(BuildingDetails :: getRiskId).collect(Collectors.toList());
				locIds.add(99999);
				
				// Delete Non Opted Loc Document
				//Long nonOptedLocDocsCount = docTransRepo.countByQuoteNoAndLocationIdNotIn(quoteNo ,locIds );
				Long nonOptedLocDocsCount = docTransRepo.countByRequestReferenceNoAndLocationIdNotIn(requestno ,locIds );
				if(nonOptedLocDocsCount > 0 ) {
					//docTransRepo.deleteByQuoteNoAndLocationIdNotIn(quoteNo ,locIds );	
					docTransRepo.deleteByRequestReferenceNoAndLocationIdNotIn(requestno ,locIds );	
				}
				
				// Delete Non Opted Loc Contents
				//Long conCount = contentRepo.countByQuoteNoAndRiskIdNotIn(quoteNo ,locIds );
				Long conCount = contentRepo.countByRequestReferenceNoAndRiskIdNotIn( requestno,locIds );
				if(conCount > 0 ) {
					//contentRepo.deleteByQuoteNoAndRiskIdNotIn(quoteNo ,locIds );
					contentRepo.deleteByRequestReferenceNoAndRiskIdNotIn(requestno ,locIds );
				}
				
				// Delete Non Opted Loc Employees
				//List<ProductEmployeeDetails> empList = productRepo.findByQuoteNoAndLocationIdNotIn(quoteNo,locIds );
				List<ProductEmployeeDetails> empList = productRepo.findByRequestReferenceNoAndLocationIdNotIn(requestno,locIds );
				if(empList.size() > 0 ) {
					//arch table save
					List<ProductEmployeeDetailsArch> savearchList = new ArrayList<ProductEmployeeDetailsArch>();
					String archId = "AI-" +  genSeqNoService.generateArchId();
					for(ProductEmployeeDetails data1 : empList) {
						ProductEmployeeDetailsArch savearch = new ProductEmployeeDetailsArch();
						savearch.setArchId(archId); 
						dozermapper.map(data1, savearch);
						savearchList.add(savearch);
					}
					empArchRepo.saveAllAndFlush(savearchList);
					productRepo.deleteByQuoteNoAndLocationIdNotIn(quoteNo ,locIds );
					productRepo.deleteByRequestReferenceNoAndLocationIdNotIn(requestno ,locIds);
				}
				
				
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Log Details" + e.getMessage());
			return null;
		}

		return res;
	}

	@Override
	public List<BuildingDetailsGetRes> getbuildingDetails(BuildingDetailsGetReq req) {
		// TODO Auto-generated method stub
		List<BuildingDetailsGetRes> res1 = new ArrayList<>();
		BuildingDetailsGetRes res = new BuildingDetailsGetRes();
		DozerBeanMapper dozermapper = new DozerBeanMapper();
		try {
			//BuildingDetails data = repo.findByQuoteNoAndRiskId(req.getQuoteNo(),Integer.valueOf(req.getRiskId()));
			List<BuildingDetails> data1 = repo.findByRequestReferenceNo(req.getRequestreferenceno());
				if(data1!=null || !data1.isEmpty()) {
					for(BuildingDetails data: data1) {
					res = dozermapper.map(data, BuildingDetailsGetRes.class);
					res.setBuildingAreaSqm(data.getBuildingAreaSqm()==null?"":data.getBuildingAreaSqm().toString());
					res.setBuildingSuminsured(data.getBuildingSuminsured()==null?"":data.getBuildingSuminsured().toString());
					res.setBuildingAge(data.getBuildingAge()==null?"":data.getBuildingAge().toString());			
					res.setBuildingFloors(data.getBuildingFloors()==null?"":data.getBuildingFloors().toString());		
					res.setBuildingBuildYear(data.getBuildingBuildYear()==null?"":data.getBuildingBuildYear().toString());		
					res.setWithoutInhabitantDays(data.getWithoutInhabitantDays()==null?"":data.getWithoutInhabitantDays().toString());
					res.setRiskId(data.getRiskId()==null?"":data.getRiskId().toString());
					res1.add(res);
					}
					}
				else {
				  return res1;
				}
				
				
		}
		catch(Exception e) {
			e.printStackTrace();
			log.info("Log Details"+e.getMessage());
			return null;
		}
		return res1;
	}
	
	
	
	@Override
	public List<BuildingDetailsGetRes> getallbuildingDetails(BuildingDetailsGetAllReq req) {
		// TODO Auto-generated method stub
		List<BuildingDetailsGetRes> resList = new ArrayList<BuildingDetailsGetRes>();
		DozerBeanMapper dozermapper = new DozerBeanMapper();
		try {
			List<BuildingDetails> datas = repo.findByQuoteNo(req.getQuoteNo());

			if(datas!=null) {
			for(BuildingDetails data : datas) {
					BuildingDetailsGetRes res = new BuildingDetailsGetRes();
					res = dozermapper.map(data, BuildingDetailsGetRes.class);
					res.setBuildingAreaSqm(data.getBuildingAreaSqm()==null?"":data.getBuildingAreaSqm().toString());
					res.setBuildingSuminsured(data.getBuildingSuminsured()==null?"":data.getBuildingSuminsured().toString());
					res.setBuildingAge(data.getBuildingAge()==null?"":data.getBuildingAge().toString());			
					res.setBuildingFloors(data.getBuildingFloors()==null?"":data.getBuildingFloors().toString());		
					res.setBuildingBuildYear(data.getBuildingBuildYear()==null?"":data.getBuildingBuildYear().toString());		
					res.setWithoutInhabitantDays(data.getWithoutInhabitantDays()==null?"":data.getWithoutInhabitantDays().toString());
					res.setRiskId(data.getRiskId()==null?"":data.getRiskId().toString());
					resList.add(res);
			}
			}
			else {
				return resList;
			}
				
		}
		catch(Exception e) {
			e.printStackTrace();
			log.info("Log Details"+e.getMessage());
			return null;
		}
		return resList;
	}

	@Override
	public SuccessRes deleteBuildingDetails(BuildingDetailsGetReq req) {
			SuccessRes res = new SuccessRes();
			try {
				// Building
				Long buildingCount = repo.countByQuoteNoAndRiskId(req.getQuoteNo() ,Integer.valueOf(req.getRiskId()));
				if(buildingCount > 0 ) {
					repo.deleteByQuoteNoAndRiskId(req.getQuoteNo() ,Integer.valueOf(req.getRiskId()));
				}
				
				// Content And All Risk
				Long contentCount = contentRepo.countByQuoteNoAndRiskId(req.getQuoteNo() ,Integer.valueOf(req.getRiskId()));
				if(contentCount > 0 ) {
					contentRepo.deleteByQuoteNoAndRiskId(req.getQuoteNo() ,Integer.valueOf(req.getRiskId()));
				}
				
				// Personal Accident
				Long paccCount = paccRepo.countByQuoteNoAndRiskId(req.getQuoteNo(), Integer.valueOf(req.getRiskId()));
				if(paccCount > 0 ) {
					paccRepo.deleteByQuoteNoAndRiskId(req.getQuoteNo() ,Integer.valueOf(req.getRiskId()));
				}
				
				res.setResponse("Deleted Successfully") ;
				res.setSuccessId(req.getQuoteNo()) ;;
					
			}
			catch(Exception e) {
				e.printStackTrace();
				log.info("Log Details"+e.getMessage());
				return null;
			}
			return res;
		}

	@Override
	public List<String> additionalInfoVali(AdditionalValidationReq req) { //for screen skip issue (Domestic&corporate)
		List<String> error = new ArrayList<String>();
	
		try {
			
			//List<SectionDataDetails> secList = secRepo.findByQuoteNo(req.getQuoteNo());
			List<SectionDataDetails> secList = secRepo.findByRequestReferenceNo(req.getRequestreferenceno());
			
			if(secList.size()>0) {
				
				
				for(SectionDataDetails sec : secList) {
					
					if(sec.getSectionId().equalsIgnoreCase("1")  && ! (sec.getProductId().equalsIgnoreCase("29") || sec.getProductId().equalsIgnoreCase("5") ) ) { //building
						
						List<BuildingDetails> build = repo.findByQuoteNoAndSectionId(req.getQuoteNo(),"0");
						
						if(! (build.size()>0) ) {
							error.add("1250"+","+sec.getSectionDesc());
//							error.add(new Error("01", sec.getSectionDesc(), "Please Enter Atleast One Data in " + sec.getSectionDesc() +" Section"));
						}
						
					} else if(sec.getSectionId().equalsIgnoreCase("47")) { //Content and risk
						//BuildingRiskDetails build = buildRiskRepo.findByQuoteNoAndSectionIdAndStatusNot(req.getQuoteNo(),sec.getSectionId() , "D");
						BuildingRiskDetails build = buildRiskRepo.findByRequestReferenceNoAndSectionIdAndStatusNot(req.getRequestreferenceno(),sec.getSectionId() , "D");
                         //List<ContentAndRisk> con = contentRepo.findByQuoteNoAndSectionId(req.getQuoteNo(),sec.getSectionId());
						List<ContentAndRisk> con = contentRepo.findByRequestReferenceNoAndSectionId(req.getRequestreferenceno(),sec.getSectionId());

						if(! (con.size()>0) && build!=null  ) {
							error.add("1250"+","+sec.getSectionDesc());
//							error.add(new Error("01", sec.getSectionDesc(), "Please Enter Atleast One Data in " + sec.getSectionDesc() +" Section"));
						}	
					
					}	else if(sec.getSectionId().equalsIgnoreCase("35")) { //Personal Accident
						
						//List<ProductEmployeeDetails> acci = empRepo.findByQuoteNoAndSectionIdAndStatusNot(req.getQuoteNo(),sec.getSectionId(),"D");
						List<ProductEmployeeDetails> acci = empRepo.findByRequestReferenceNoAndSectionIdAndStatusNot(req.getRequestreferenceno(),sec.getSectionId(),"D");
						if(! (acci.size()>0)   ) {
							error.add("1250"+","+sec.getSectionDesc());
//							error.add(new Error("01", sec.getSectionDesc(), "Please Enter Atleast One Data in " + sec.getSectionDesc() +" Section"));
						}	
				
					}	else if(sec.getSectionId().equalsIgnoreCase("3") && ! (sec.getProductId().equalsIgnoreCase("21") ||sec.getProductId().equalsIgnoreCase("29") || sec.getProductId().equalsIgnoreCase("5") )  ) { //All Risk
						//BuildingRiskDetails build = buildRiskRepo.findByQuoteNoAndSectionIdAndStatusNot(req.getQuoteNo(),sec.getSectionId() , "D");
						BuildingRiskDetails build = buildRiskRepo.findByRequestReferenceNoAndSectionIdAndStatusNot(req.getRequestreferenceno(),sec.getSectionId() , "D");
						//List<ContentAndRisk> con = contentRepo.findByQuoteNoAndSectionId(req.getQuoteNo(),sec.getSectionId());
						List<ContentAndRisk> con = contentRepo.findByRequestReferenceNoAndSectionId(req.getRequestreferenceno(),sec.getSectionId());
						
						if(! (con.size()>0) && build!=null ) {
							error.add("1250"+","+sec.getSectionDesc());
//							error.add(new Error("01", sec.getSectionDesc(), "Please Enter Atleast One Data in " + sec.getSectionDesc() +" Section"));
						}	
				
					}	else if(sec.getSectionId().equalsIgnoreCase("36")) { //Personal Indemenity
						
						//List<ProductEmployeeDetails> acci = empRepo.findByQuoteNoAndSectionIdAndStatusNot(req.getQuoteNo(),sec.getSectionId(),"D");
						List<ProductEmployeeDetails> acci = empRepo.findByRequestReferenceNoAndSectionIdAndStatusNot(req.getRequestreferenceno(),sec.getSectionId(),"D");
						if(! (acci.size()>0) ) {
							error.add("1250"+","+sec.getSectionDesc());
//							error.add(new Error("01", sec.getSectionDesc(), "Please Enter Atleast One Data in " + sec.getSectionDesc() +" Section"));
						}	
				
					} else if(sec.getSectionId().equalsIgnoreCase("45")) { //Employer's Liabilty
						
						//List<ProductEmployeeDetails> acci = empRepo.findByQuoteNoAndSectionIdAndStatusNot(req.getQuoteNo(),sec.getSectionId(),"D");
						List<ProductEmployeeDetails> acci = empRepo.findByRequestReferenceNoAndSectionIdAndStatusNot(req.getRequestreferenceno(),sec.getSectionId(),"D");
						if(! (acci.size()>0) ) {
							error.add("1250"+","+sec.getSectionDesc());
//							error.add(new Error("01", sec.getSectionDesc(), "Please Enter Atleast One Data in " + sec.getSectionDesc() +" Section"));
						}	
				
					}	else if(sec.getSectionId().equalsIgnoreCase("43")) { //Fidelity Details
						
						//List<ProductEmployeeDetails> acci = empRepo.findByQuoteNoAndSectionIdAndStatusNot(req.getQuoteNo(),sec.getSectionId(),"D");
						List<ProductEmployeeDetails> acci = empRepo.findByRequestReferenceNoAndSectionIdAndStatusNot(req.getRequestreferenceno(),sec.getSectionId(),"D");
						if(! (acci.size()>0) ) {
							error.add("1250"+","+sec.getSectionDesc());
//							error.add(new Error("01", sec.getSectionDesc(), "Please Enter Atleast One Data in " + sec.getSectionDesc() +" Section"));
						}	
				
					}		
					
			}			
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			log.info("Log Details"+e.getMessage());
			return null;
		}
		return error;
	}
	

}
