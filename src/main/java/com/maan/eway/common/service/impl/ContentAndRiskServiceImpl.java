package com.maan.eway.common.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.eway.bean.AdditionalInformation;
import com.maan.eway.bean.BuildingRiskDetails;
import com.maan.eway.bean.CommonDataDetails;
import com.maan.eway.bean.CompanyProductMaster;
import com.maan.eway.bean.ContentAndRisk;
import com.maan.eway.bean.EserviceBuildingDetails;
import com.maan.eway.bean.EserviceSectionDetails;
import com.maan.eway.bean.HomePositionMaster;
import com.maan.eway.bean.ListItemValue;
import com.maan.eway.bean.MotorDataDetails;
import com.maan.eway.common.req.AdditionalInfoGetReq;
import com.maan.eway.common.req.AdditionalInformationRequest;
import com.maan.eway.common.req.ContentAndRiskSaveReq;
import com.maan.eway.common.req.ContentItem;
import com.maan.eway.common.req.ContentRiskDetailsListReq;
import com.maan.eway.common.req.ContentRiskGetAllReq;
import com.maan.eway.common.req.ContentRiskGetReq;
import com.maan.eway.common.req.RiskDuplicateCheckReq;
import com.maan.eway.common.res.AdditionalInformationResponse;
import com.maan.eway.common.res.ContentItemResponse;
import com.maan.eway.common.res.ContentRiskDetailsRes;
import com.maan.eway.common.res.ContentRiskGetRes;
import com.maan.eway.common.res.ContentRiskGetallRes;
import com.maan.eway.common.service.ContentAndRiskService;
import com.maan.eway.repository.AdditionalInformationRepository;
import com.maan.eway.repository.BuildingRiskDetailsRepository;
import com.maan.eway.repository.CommonDataDetailsRepository;
import com.maan.eway.repository.CompanyProductMasterRepository;
import com.maan.eway.repository.ContentAndRiskRepository;
import com.maan.eway.repository.EServiceBuildingDetailsRepository;
import com.maan.eway.repository.EServiceSectionDetailsRepository;
import com.maan.eway.repository.HomePositionMasterRepository;
import com.maan.eway.repository.ListItemValueRepository;
import com.maan.eway.repository.MotorDataDetailsRepository;
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
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ContentAndRiskServiceImpl implements ContentAndRiskService {

	@Autowired
	private ContentAndRiskRepository repo;

	@Autowired
	private ListItemValueRepository listrepo;

	@Autowired
	private EServiceSectionDetailsRepository sectionrepo;
	
	@Autowired
	private EServiceBuildingDetailsRepository eserBuildingRepo ;

	@Autowired
	private MotorDataDetailsRepository motorRepo; 
	
	@Autowired
    private AdditionalInformationRepository additionalInformationrepo;
	
	@Autowired
	private HomePositionMasterRepository homeRepo ;
	
	@Autowired
	private BuildingRiskDetailsRepository buildingRiskRepo;
	
	@Autowired
	private CompanyProductMasterRepository companyrepo;

	@Autowired
	private CommonDataDetailsRepository commonDataRepo;
	
	@PersistenceContext
	private EntityManager em;
	
	private Logger log = LogManager.getLogger(ContentAndRiskServiceImpl.class);

	@Override
	public List<String>validatecontentrisk(ContentAndRiskSaveReq req) {
		List<String> error = new ArrayList<String>();

		try {
			Long row = 0L ;
			BigDecimal sumInsured = BigDecimal.ZERO ;
			String quoteNo = "" ;
			String Req_NO="";
			String productId =req.getProductid();
			String companyId =req.getCompanyid();
			String branchCode = "" ;
			List<EserviceBuildingDetails> buidingData = new ArrayList<EserviceBuildingDetails>();
			

			if (StringUtils.isBlank(req.getSectionId())) {
				error.add("1154");
//				error.add(new Error("01", "SectionId", "Please Select Section Id"));
			}
			 if (StringUtils.isBlank(req.getQuoteNo())) { 
				 error.add("1155"); 
//				 error.add(new Error("03", "QuoteNo", "Please Select QuoteNo")); 
			 }
			 
			 
			if(StringUtils.isBlank(req.getRequestReferenceNo()))
			{
				error.add("2277");
			}
			else if( StringUtils.isNotBlank(req.getSectionId()) && StringUtils.isNotBlank(req.getQuoteNo())){
				quoteNo = req.getQuoteNo() ;
				Req_NO=req.getRequestReferenceNo();
				//List<MotorDataDetails> motList = motorRepo.findByQuoteNoAndStatusNotOrderByVehicleIdAsc(req.getQuoteNo(),"D");
				List<MotorDataDetails> motList = motorRepo.findByRequestReferenceNoAndStatusNotOrderByVehicleIdAsc(req.getRequestReferenceNo(),"D");
	         	//buidingData = eserBuildingRepo.findByQuoteNoAndSectionId(quoteNo ,req.getSectionId());
				buidingData = eserBuildingRepo.findByRequestReferenceNoAndSectionId(Req_NO ,req.getSectionId());
				
				
	         	if(buidingData==null||buidingData.isEmpty()) {
					productId =  motList.size()==0 ? "5" : motList.get(0).getProductId().toString();
					companyId =  motList.size()==0 ? "" : motList.get(0).getCompanyId().toString();
					branchCode =  motList.size()==0 ? "" : motList.get(0).getBranchCode().toString();
				} else {
					productId =  buidingData.get(0).getProductId();
					companyId =  buidingData.get(0).getCompanyId();
					branchCode =   buidingData.get(0).getBranchCode();
				}
				
			}
			if (StringUtils.isBlank(req.getType())) {
				error.add("1156");
//				error.add(new Error("04", "Type", "Please Select Type"));
			}
			
			List<RiskDuplicateCheckReq> contAndRisks = new ArrayList<RiskDuplicateCheckReq>(); 
			ListItemValue type = new ListItemValue();
			
			if("42".equalsIgnoreCase(productId ) ) {
				              
				type = listrepo.findByItemTypeAndItemCodeAndCompanyId("CYBER_INSURANCE_TYPE",buidingData.get(0).getOccupationType(),companyId);
			} else {
				type = listrepo.findByItemTypeAndItemCodeAndCompanyId("CONTENT_RISK",req.getType(),req.getCompanyid());
			}

			if(req.getContentriskdetails().isEmpty()) {
				error.add("3312");
			}
			//ALL
			if(req.getContentriskdetails().size()>0 && !req.getType().equals("EA")) {
				List<ListItemValue> itemList =  getListItemDrodown(companyId ,branchCode,  type.getItemType());
				List<ListItemValue> filterAll =  itemList.stream().filter(o-> o.getItemValue().equalsIgnoreCase("All") )
						.collect(Collectors.toList());
				List<String> riskIds = req.getContentriskdetails().stream().map(ContentRiskDetailsListReq :: getRiskId  ).collect(Collectors.toList());
				 riskIds = riskIds.stream()
			                .filter(java.util.Objects::nonNull)
			                .collect(Collectors.toList());				
			
				riskIds = riskIds.stream().distinct().collect(Collectors.toList());
				if(riskIds.size()>0 ) {
					for (String id : riskIds ) {
						if(id!=null) {
							if(filterAll.size() > 0 ) {
								String itemCode = filterAll.get(0).getItemCode() ;
								List<ContentRiskDetailsListReq> list = req.getContentriskdetails().stream().filter(o->  (o.getRiskId()!=null && o.getRiskId().equalsIgnoreCase(id))  &&
										o.getItemId().equalsIgnoreCase(itemCode) )
										.collect(Collectors.toList());	
								List<ContentRiskDetailsListReq> riskCOntents = req.getContentriskdetails().stream().filter(o->  (o.getRiskId()!=null && o.getRiskId().equalsIgnoreCase(id))   )
										.collect(Collectors.toList());	
								if(list.size()>0 &&  riskCOntents.size()>1 )
									error.add("1157"+","+type.getItemValue());
//									error.add(new Error("07", "Duplicate",type.getItemValue() + " - " + "ALL"  + " Should not Allow Other Item Type  in same Location  "  ));
							
							}
						}
					}
				}
				
			}
			List<String> itemId=new ArrayList<>();
			for(ContentRiskDetailsListReq req1 : req.getContentriskdetails()) {
				row = row + 1 ;
				if (StringUtils.isBlank(req1.getRiskId())) {
					error.add("1158"+","+ row);
//					error.add(new Error("02", "RiskId", "Please Select RiskId In Row No:" + row ));
				}
				if (StringUtils.isBlank(req1.getLocationId())) {
					error.add("1201"+","+ row);
//					error.add(new Error("02", "Location Id", "Please Select Location Id In Row No:" + row ));
				}
				
				if (StringUtils.isBlank(req1.getLocationName())) {
					error.add("1192"+","+ row);
//					error.add(new Error("02", "Location Id", "Please Select Location Id In Row No:" + row ));
				}
				
				if (StringUtils.isBlank(req1.getItemId())) {
					error.add("1159"+","+ row);
//					error.add(new Error("05", "ItemId", "Please Select ItemType In Row No:" + row ));
				}else  if (!req1.getItemId().matches("[0-9.]+")) {
					error.add("1160"+","+ row);
//					error.add(new Error("06", "ItemId", "Please Enter Valid Number In ItemId"));
				}
				if (StringUtils.isBlank(req1.getItemValue())) {
					error.add("2273"+","+ row);
//					error.add(new Error("05", "ItemId", "Please Select ItemValue In Row No:" + row ));
				}
//				else if (!req1.getItemValue().matches("[0-9-a-zA-Z]")) {
//					error.add("2275"+","+ row);
////					error.add(new Error("05", "ItemId", "Serial No AlphaNumeric Only Allowed In Row No:" + row));
//				}
				if ((StringUtils.isNotBlank(req.getType()))&& req.getType().equalsIgnoreCase("C")) {
				if (StringUtils.isBlank(req1.getContentRiskDesc())) {
					error.add("2268"+","+ row);
				}else if(req1.getContentRiskDesc().length() > 500 ) {
					error.add("1160"+","+ row);
//					error.add(new Error("05", "ItemId", "Description Must be Under 500 Charecter Only Allowed In Row No:" + row ));
				}
		
				if (StringUtils.isBlank(req1.getSerialNoDesc())) {
					error.add("1161"+","+ row);
//					error.add(new Error("08", "SerialNoDesc", "Please Enter Serial No In Row No:" + row ));
				}else if (req1.getSerialNoDesc().length() > 500 ) {
					error.add("1162"+","+ row);
//					error.add(new Error("05", "Serial No Desc", "Serial No  Must be Under 500 Character Only Allowed In Row No:" + row ));
				} 
//				else if (!req1.getSerialNoDesc().matches("[0-9-a-zA-Z]")) {
//					error.add("2274"+","+ row);
////					error.add(new Error("05", "ItemId", "Serial No AlphaNumeric Only Allowed In Row No:" + row));
//				}
				}
				
				// Content DUplicate CHecking
				if (StringUtils.isNotBlank(req1.getRiskId()) && StringUtils.isNotBlank(req1.getItemId())  ) {
					//ListItemValue itemvalue = new ListItemValue();
					List<ListItemValue> itemvalue= new ArrayList<>();
					if("42".equalsIgnoreCase(productId ) ) {
						itemvalue = listrepo.findByItemTypeAndItemCodeAndCompanyIdOrderByAmendIdDesc("CYBER_INSURANCE_TYPE",buidingData.get(0).getOccupationType(),companyId) ;
					} else {
						if(type!=null)
						{
						itemvalue = listrepo.findByItemTypeAndItemCodeAndCompanyIdOrderByAmendIdDesc(type.getItemValue() ,req1.getItemId(),req.getCompanyid());
						}
						type = listrepo.findByItemTypeAndItemCodeAndCompanyId("CONTENT_RISK",req1.getItemId(),req.getCompanyid());
						
					
					}
					
//					if(itemvalue.getItemValue().equalsIgnoreCase("All") ) {
//											
//						List<RiskDuplicateCheckReq> filterContAndRisks = contAndRisks.stream().filter( 
//								o ->  (o.getRiskId().equalsIgnoreCase(req1.getRiskId())) ).collect(Collectors.toList());
//						
//
//						if( filterContAndRisks.size() > 0 ) { 
//							error.add(new Error("07", "Duplicate",type.getItemValue() + " -" + itemvalue.getItemValue()  + " Should not Allow Other Item Type  for same Location" ));
//							
//						}else {
//							contAndRisks.add(new RiskDuplicateCheckReq(req1.getRiskId(),req1.getItemId(),req1.getSerialNoDesc()));
//							
//						}
//						
//					} else {
					if(  !itemvalue.isEmpty()&&!itemvalue.get(0).getItemValue().equalsIgnoreCase("All") ) {
						
						if(productId.equalsIgnoreCase("25") ) {
							
							List<RiskDuplicateCheckReq> filterContAndRisks = contAndRisks.stream().filter( 
									o ->  (o.getRiskId().equalsIgnoreCase(req1.getRiskId()))  && (o.getValue().equalsIgnoreCase(req1.getItemId()))    ).collect(Collectors.toList());
							if( filterContAndRisks.size() > 0 ) { 
								error.add("1163"+","+ row);
//								error.add(new Error("07", "Duplicate", type.getItemValue() + " - " + itemvalue.getItemValue()  + "  Duplicate for same Location In Row No:" + row ));
								
							} else {
								contAndRisks.add(new RiskDuplicateCheckReq(req1.getRiskId(),req1.getItemId(),req1.getSerialNoDesc()));
								
							}
						} 
						
						else {
							List<RiskDuplicateCheckReq> filterContAndRisks = contAndRisks.stream().filter( 
									o ->  (o.getRiskId().equalsIgnoreCase(req1.getRiskId()))  && (o.getValue().equalsIgnoreCase(req1.getItemId())) && (o.getSerialNoDesc().equals(req1.getSerialNoDesc()) )  ).collect(Collectors.toList());
							if( filterContAndRisks.size() > 0 ) { 
								error.add("1164"+","+ row);
//								error.add(new Error("07", "Duplicate", type.getItemValue() + " - " + itemvalue.getItemValue()  + "  Duplicate for same Location and Serial No In Row No:" + row ));
								
							} else {
								contAndRisks.add(new RiskDuplicateCheckReq(req1.getRiskId(),req1.getItemId(),req1.getSerialNoDesc()));
								
							}
				
						}
					}
					
				}
				
				if (StringUtils.isBlank(req1.getMakeAndModel())) {
					error.add("1165"+","+ row);
//					error.add(new Error("07", "MakeAndModel", "Please Enter MakeAndModel In Row No:" + row ));
				}
//				if (StringUtils.isBlank(req1.getSerialNo())) {
//					error.add(new Error("08", "SerialNo", "Please Enter SerialNo In Row No:" + row ));
//				}
				if(  productId.equalsIgnoreCase("42")  ) {
					SimpleDateFormat yf = new SimpleDateFormat("yyyy"); 
					int year = Integer.valueOf(yf.format( new Date()));
					if (StringUtils.isBlank(req1.getManufactureYear())) {
						error.add("1166"+","+ row);
//						error.add(new Error("10", "ManufactureYear", "Please Enter Year Of Manufacture In Row No:" + row ));
						
					}else if (!req1.getManufactureYear().matches("[0-9]+") || Integer.valueOf(req1.getManufactureYear()) > year ) {
						error.add("1167"+","+ row);
//						error.add(new Error("09", "ManufactureYear", "Please Enter Valid Year Of Manufacture In Row No:" + row ));
					}
					
				} else {
					if (StringUtils.isBlank(req1.getSumInsured())) {
						error.add("1168"+","+ row);
//						error.add(new Error("09", "SumInsured", "Please Enter SumInsured In Row No:" + row ));
					}
					else if (!req1.getSumInsured().matches("[0-9.]+") || Double.valueOf(req1.getSumInsured()) <=0) {
						error.add("1169"+","+ row);
//						error.add(new Error("09", "SumInsured", "Please Enter Valid Number In SumInsured In Row No:" + row ));
					}
					else if (req1.getSumInsured().equalsIgnoreCase("0") ) {
						error.add("1170"+","+ row);
//						error.add(new Error("09", "SumInsured", "Please Enter SumInsured above 0 In Row No:" + row ));
					}
//					else {
//						sumInsured = sumInsured.add(new BigDecimal(req1.getSumInsured()));
//					}
					
					if ((StringUtils.isNotBlank(req.getType()))&& req.getType().equalsIgnoreCase("E")) {
						int year = Year.now().getValue();

						if (StringUtils.isBlank(req1.getPurchaseYear())) {
							error.add("1171"+","+ row);
//							error.add(new Error("10", "Purchase Year", "Please Enter Year Of Purchase In Row No:" + row ));
							
						}else if (!req1.getPurchaseYear().matches("[0-9]+") || Integer.valueOf(req1.getPurchaseYear()) > year ) {
							error.add("1172"+","+ row);
//							error.add(new Error("09", "Purchase Year", "Please Enter Valid Purchase Year In Row No:" + row ));
						}
						
						if (StringUtils.isBlank(req1.getPurchaseMonth())) {
							error.add("1173"+","+ row);
//							error.add(new Error("11", "PurchaseMonth", "Please Enter PurchaseMonth In Row No:" + row ));
						}
					}
				}
				
				
				
		/*		if(req.getSectionId().equalsIgnoreCase("19")  || productId.equalsIgnoreCase("39") || req.getSectionId().equalsIgnoreCase("41")  ) {
					if (StringUtils.isBlank(req1.getBrand())) {
						error.add("1174"+","+ row);
//						error.add(new Error("07", "Brand", "Please Enter Brand In Row No:" + row ));
					}
					
					if (StringUtils.isBlank(req1.getName())) {
						error.add("1175"+","+ row);
//						error.add(new Error("07", "Name", "Please Enter Name In Row No:" + row ));
					} else if(! req1.getName().matches("^[a-zA-Z ]+$")) { 
						error.add("1176"+","+ row);
//						error.add(new Error("07", "Name", "Please Enter Name Alphabet only In Row No:" + row ));
					}
							
				}*/
				
			}
			System.out.println("Entering Duplicate Validation");
			List<ContentRiskDetailsListReq> contentReq = req.getContentriskdetails();
			List<String> findlocationId = contentReq.stream().map(ContentRiskDetailsListReq::getLocationId).distinct()
					.collect(Collectors.toList());
			if (error.isEmpty()) {
				for (String l : findlocationId) {
					System.out.println("Location Id : " + l);
					if ((StringUtils.isNotBlank(req.getType()))
							&& (req.getType().equalsIgnoreCase("C") || req.getType().equalsIgnoreCase("E"))) {
						List<ContentRiskDetailsListReq> filterContAndRisks = contentReq.stream()
								.filter(o -> o.getLocationId().equalsIgnoreCase(l)).collect(Collectors.toList());
						System.out.println("Entering Duplicate Validation for  Item Id");
						for (ContentRiskDetailsListReq sum : filterContAndRisks) {
							sumInsured = sumInsured.add(new BigDecimal(sum.getSumInsured()));
//							if(StringUtils.isNotBlank(sum.getItemId())) {
//								List<String> itemIdDuplicate =  itemId.stream().filter( o -> o.equalsIgnoreCase(sum.getItemId()) ).collect(Collectors.toList()); 
//								if(itemIdDuplicate.size()>0  ) {
//									error.add("2276" + "," + row);
//									System.out.println("Duplicate Item Id Available in Row No : " + row );
//									//errors.add(new Error("12", "Item Id", "Duplicate Item Id Available in Row No : " + row ));
//								} else {
//									itemId.add(sum.getItemId());
//								}
//								
//							}
						}
						System.out.println("Entering Validation for Sum insured");
						System.out.println("Total SumInsured from Request : " + sumInsured);

						Double contentSumInsured = buidingData.stream()
								.filter(o -> o.getSectionId().equalsIgnoreCase(req.getSectionId())
										&& o.getLocationId().equals(Integer.valueOf(l)))
								.mapToDouble(o -> o.getSumInsured().doubleValue()).sum();
						System.out.println("Total SumInsured from table : " + contentSumInsured);
						if (contentSumInsured != null && contentSumInsured > 0) {

							if (new BigDecimal(contentSumInsured).compareTo(sumInsured) < 0) {
								error.add("1177" + "," + row);
								System.out.println("Total SumInsured Greater Than Actual SumInsured In Row No:" + row );
//									error.add(new Error("03", "SumInsured", " Total SumInsured Greater Than Actual SumInsured In Row No:" + row  ));
							}
						}
					}
				}
			}
			
			
			
			

//			if((StringUtils.isNotBlank(req.getType()))&&req.getType().equalsIgnoreCase("C")) {
//			if(StringUtils.isNotBlank(quoteNo)  ) {
//				  if(buidingData.getContentSuminsured() == null || buidingData.getContentSuminsured().compareTo(BigDecimal.ZERO) ==0 ) {
//				    	BigDecimal Si1 = buidingData.getEquipmentSi() == null? BigDecimal.ZERO :buidingData.getEquipmentSi();
//				    	BigDecimal Si2 = buidingData.getJewellerySi() == null? BigDecimal.ZERO :buidingData.getJewellerySi();
//				    	BigDecimal Si3 = buidingData.getPaitingsSi() == null? BigDecimal.ZERO :buidingData.getPaitingsSi();
//				    	BigDecimal Si4 = buidingData.getCarpetsSi() == null? BigDecimal.ZERO :buidingData.getCarpetsSi();
//				    	
//				    	BigDecimal totalContentSi = Si1.add(Si2).add(Si3).add(Si4);
//				    	if( sumInsured.compareTo(totalContentSi) > 0 ) {
//				    		error.add("1177"+","+ row);
////							error.add(new Error("03", "SumInsured", " Total SumInsured Greater Than Actual SumInsured In Row No:" + row  ));
//						}
//				    } else if(buidingData.getContentSuminsured()!=null && sumInsured.compareTo(buidingData.getContentSuminsured()) > 0 ) {
//				    	error.add("1177"+","+ row);
////				    	error.add(new Error("03", "SumINsured", " Total SumInsured Greater Than Actual SumInsured In Row No:" + row  ));
//				    }
//			}
//			}
			
//			if((StringUtils.isNotBlank(req.getType()))&&req.getType().equalsIgnoreCase("A")) {
//				if(StringUtils.isNotBlank(quoteNo)  ) {
//					
//					if(req.getSectionId().equalsIgnoreCase("41")  || productId.equalsIgnoreCase("39") ) {
//						
//						// Machinery Suminsured
//						Double ElecMachinesSi = buidingData.getElecMachinesSi() == null?0D :Double.valueOf(buidingData.getElecMachinesSi().toPlainString());
//						Double BoilerPlantsSi = buidingData.getBoilerPlantsSi() == null?0D :Double.valueOf(buidingData.getBoilerPlantsSi().toPlainString()) ;
//						Double EquipmentSi = buidingData.getEquipmentSi() == null?0D :Double.valueOf(buidingData.getEquipmentSi().toPlainString()) ;
//						Double GeneralMachineSi = buidingData.getGeneralMachineSi() == null?0D :Double.valueOf(buidingData.getGeneralMachineSi().toPlainString()) ;
//						Double MachineEquipSi = buidingData.getMachineEquipSi() == null?0D :Double.valueOf(buidingData.getMachineEquipSi().toPlainString()) ;
//						Double ManuUnitsSi = buidingData.getManuUnitsSi() == null?0D :Double.valueOf(buidingData.getManuUnitsSi().toPlainString()) ;
//						Double plantSi = buidingData.getPowerPlantSi() == null?0D :Double.valueOf(buidingData.getPowerPlantSi().toPlainString()) ;
//						Double machinerySi = ElecMachinesSi + BoilerPlantsSi + EquipmentSi + GeneralMachineSi + MachineEquipSi + ManuUnitsSi + plantSi ;
//								
//						
//						if(machinerySi!=null && sumInsured.compareTo(new BigDecimal(machinerySi)) > 0 ) {
//							error.add("1178"+","+ row);
////							error.add(new Error("03", "SumINsured", " Total SumInsured Greater Than Actual Machinery Suminsured In Row No:" + row  ));
//						}
//						
//						
//					}else if(!productId.equalsIgnoreCase("24") && !productId.equalsIgnoreCase("19") && !productId.equalsIgnoreCase("26") && ! productId.equals("59") &&  ( req.getSectionId().equalsIgnoreCase("3")  || productId.equalsIgnoreCase("21") ) ) {
//						
//						Double MiningPlantSi = buidingData.getMiningPlantSi() == null?0D :Double.valueOf(buidingData.getMiningPlantSi().toPlainString()) ;
//						Double NonminingPlantSi = buidingData.getNonminingPlantSi() == null?0D :Double.valueOf(buidingData.getNonminingPlantSi().toPlainString()) ;
//						Double GensetsSi = buidingData.getGensetsSi() == null?0D :Double.valueOf(buidingData.getGensetsSi().toPlainString()) ;
//						Double plantAllRiskSi = MiningPlantSi +NonminingPlantSi  + GensetsSi ;	
//						
//						if(plantAllRiskSi!=null && sumInsured.compareTo(new BigDecimal(plantAllRiskSi)) > 0 ) {
//							error.add("1179"+","+ row);
////							error.add(new Error("03", "SumINsured", " Total SumInsured Greater Than Actual Plant All Risk Suminsured In Row No:" + row  ));
//						}
//						
//					} else if(productId.equalsIgnoreCase("26") ) {
//						if(buidingData.getEquipmentSi()!=null && sumInsured.compareTo(buidingData.getEquipmentSi()) > 0 ) {
//							error.add("1180"+","+ row);
////							error.add(new Error("03", "SumINsured", " Total SumInsured Greater Than Actual Business AllriskSuminsured In Row No:" + row  ));
//						}
//					} else if( ! productId.equalsIgnoreCase("42")  ) { 
//						if(! productId.equalsIgnoreCase("19")) {
//						
//							if(buidingData.getAllriskSuminsured()!=null && sumInsured.compareTo(buidingData.getAllriskSuminsured()) > 0 ) {
//								error.add("1181"+","+ row);
////								error.add(new Error("03", "SumINsured", " Total SumInsured Greater Than Actual AllriskSuminsured In Row No:" + row  ));
//							}
//						} else if ((productId.equalsIgnoreCase("19") && req.getSectionId().equalsIgnoreCase("3") )) {
//							if(buidingData.getAllriskSuminsured()!=null && sumInsured.compareTo(buidingData.getAllriskSuminsured()) > 0 ) {
//								error.add("1181"+","+ row);
////								error.add(new Error("03", "SumINsured", " Total SumInsured Greater Than Actual AllriskSuminsured In Row No:" + row  ));
//							}
//						}
//					}
//					
//				}
//				}

//			if((StringUtils.isNotBlank(req.getType()))&&req.getType().equalsIgnoreCase("E")) {
//				if(StringUtils.isNotBlank(quoteNo)  ) {
//					if(buidingData.getElecEquipSuminsured()!=null && sumInsured.compareTo(buidingData.getElecEquipSuminsured()) > 0 ) {
//						error.add("1182"+","+ row);
////						error.add(new Error("03", "SumINsured", " Total SumInsured Greater Than Actual ElecEquipSuminsured In Row No:" + row  ));
//					}
//				}
//				}
			
			
			
			 
//			if(productId.equalsIgnoreCase("42") ) {
//				Integer occupationType  = Integer.valueOf(buidingData.getOccupationType()) ;
//				Integer occupationCount = 1 == occupationType  ? 5 : 2 == occupationType  ? 8 : 3 == occupationType  ? 10 : 1 ;
//				String  occupationDesc  = buidingData.getOccupationTypeDesc();
//				
//				if( req.getContentriskdetails()== null || req.getContentriskdetails().size() <=0 || req.getContentriskdetails().size() > occupationCount ) {
//					error.add("1183"+","+occupationCount);
////					error.add(new Error("03", "Content Occupation", "More Than " + occupationCount + " Equipments Not Allowed For " + occupationDesc +  " Occupation "));
//				}
//			}
			/*
		
			// Content Type Validation
			List<String> risk = new ArrayList<String>();
			List<String> risk1 = new ArrayList<String>();
			
			List<String> item = new ArrayList<String>();
			List<String> item1 = new ArrayList<String>();

			Integer row=0;
			for (ContentRiskDetailsListReq req2 : req.getContentriskdetails()) {

				risk1 = risk.stream().filter( 
							o ->  o
							.equalsIgnoreCase(req2.getRiskId()))
							.collect(Collectors.toList());			
				
				item1 = item.stream().filter( 
						o ->  o
						.equalsIgnoreCase(req2.getItemId()))
						.collect(Collectors.toList());			
				row++;
				
				if(risk1.size()>0&&item1.size()>0) {
					error.add(new Error("36", "Item Id", "Duplicate Item Id Not Allowed in "+row));

				}
				else {

				
				risk.add(req2.getRiskId());
				item.add(req2.getItemId());
				}
			}
		
*/
		
			
			if((StringUtils.isNotBlank(req.getType()))&&req.getType().equalsIgnoreCase("EA")) {
				// List<MotorDataDetails> motList = motorRepo.findByQuoteNoAndStatusNotOrderByVehicleIdAsc(req.getQuoteNo(),"D");
				List<MotorDataDetails> motList = motorRepo.findByRequestReferenceNoAndStatusNotOrderByVehicleIdAsc(req.getRequestReferenceNo(),"D");
				
				
				 List<ContentRiskDetailsListReq> contentList =   req.getContentriskdetails();
				 
				 for (MotorDataDetails mot : motList ) {
					 Double accSuminsured = 0D;	 
					 List<ContentRiskDetailsListReq> filterContents = contentList.stream().filter( o -> o.getRiskId().equalsIgnoreCase(mot.getVehicleId().toString()) )
							 .collect(Collectors.toList());
					 for(ContentRiskDetailsListReq con :filterContents  ) {
						 accSuminsured = accSuminsured +  (StringUtils.isBlank(con.getSumInsured()) ? 0D : Double.valueOf(con.getSumInsured()))  ; 
					 }
					 
					 if(mot.getAcccessoriesSumInsured()!=null && accSuminsured > mot.getAcccessoriesSumInsured() ) {
						error.add("1184"+","+ mot.getAcccessoriesSumInsured());
//					    error.add(new Error("03", "ChasissiNo", "Chassis No " + mot.getChassisNumber() + " Given Suminsured " +accSuminsured +  " Is Greater Then " +
						 		     // " Actual Suminsured  "  +  mot.getAcccessoriesSumInsured()  ));
					 }
				 }
				 
			}
		}
		 catch (Exception e) {

				log.error(e);
				e.printStackTrace();
				error.add("1185");
//				error.add(new Error("03", "Common Error", e.getMessage()));
			}
			return error;
		}

	public List<ListItemValue>  getListItem(String insuranceId, String branchCode, String itemType, String itemCode) {
		List<ListItemValue> list = new ArrayList<ListItemValue>();
		try {
			Date today = new Date();
			Calendar cal = new GregorianCalendar();
			cal.setTime(today);
			today = cal.getTime();
			Date todayEnd = cal.getTime();

			// Criteria
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<ListItemValue> query = cb.createQuery(ListItemValue.class);
			// Find All
			Root<ListItemValue> c = query.from(ListItemValue.class);

			// Select
			query.select(c);
			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(c.get("branchCode")));

			// Effective Date Start Max Filter
			Subquery<Date> effectiveDate = query.subquery(Date.class);
			Root<ListItemValue> ocpm1 = effectiveDate.from(ListItemValue.class);
			effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart").as(Date.class)));
			Predicate a1 = cb.equal(c.get("itemId"), ocpm1.get("itemId"));
			Predicate a9 = cb.equal(c.get("itemType"), ocpm1.get("itemType"));
			Predicate a10 = cb.equal(c.get("itemCode"), ocpm1.get("itemCode"));
			Predicate a5 = cb.equal(c.get("branchCode"), ocpm1.get("branchCode"));
			Predicate a6 = cb.equal(c.get("companyId"), ocpm1.get("companyId"));
			Predicate a2 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
			effectiveDate.where(a1, a2,a5,a6,a9,a10);
			// Effective Date End Max Filter
			Subquery<Date> effectiveDate2 = query.subquery(Date.class);
			Root<ListItemValue> ocpm2 = effectiveDate2.from(ListItemValue.class);
			effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd").as(Date.class)));
			Predicate a11 = cb.equal(c.get("itemType"), ocpm2.get("itemType"));
			Predicate a12 = cb.equal(c.get("itemCode"), ocpm2.get("itemCode"));
			Predicate a3 = cb.equal(c.get("itemId"), ocpm2.get("itemId"));
			Predicate a7 = cb.equal(c.get("companyId"), ocpm2.get("companyId"));
			Predicate a8 = cb.equal(c.get("branchCode"), ocpm2.get("branchCode"));
			Predicate a4 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
			effectiveDate2.where(a3, a4,a7,a8,a11,a12);

			// Where
			Predicate n1 = cb.equal(c.get("status"),"Y");
			Predicate n12 = cb.equal(c.get("status"),"R");
			Predicate n13 = cb.or(n1,n12);
			Predicate n2 = cb.equal(c.get("effectiveDateStart"), effectiveDate);
			Predicate n3 = cb.equal(c.get("effectiveDateEnd"), effectiveDate2);
			Predicate n4 = cb.equal(c.get("companyId"), insuranceId);
			Predicate n6 = cb.equal(c.get("branchCode"), branchCode);
			Predicate n7 = cb.equal(c.get("branchCode"), "99999");
			Predicate n9 = cb.or(n6, n7);
			Predicate n10 = cb.equal(c.get("itemType"), itemType);
			Predicate n11 = cb.equal(c.get("itemCode"), itemCode);
			
			query.where(n13, n2, n3, n4, n9, n10, n11).orderBy(orderList);
			
			
			// Get Result
			TypedQuery<ListItemValue> result = em.createQuery(query);
			list = result.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			return null;
		}
		return list;
	}
	                                         
	                                            
	public synchronized List<ListItemValue> getListItemDrodown(String insuranceId, String branchCode, String itemType ) {
		List<ListItemValue> list = new ArrayList<ListItemValue>();
		try {
			Date today = new Date();
			Calendar cal = new GregorianCalendar();
			cal.setTime(today);
			today = cal.getTime();
			Date todayEnd = cal.getTime();

			// Criteria
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<ListItemValue> query = cb.createQuery(ListItemValue.class);
			// Find All
			Root<ListItemValue> c = query.from(ListItemValue.class);

			// Select
			query.select(c);
			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(c.get("branchCode")));

			// Effective Date Start Max Filter
			Subquery<Date> effectiveDate = query.subquery(Date.class);
			Root<ListItemValue> ocpm1 = effectiveDate.from(ListItemValue.class);
			effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart").as(Date.class)));
			Predicate a1 = cb.equal(c.get("itemId"), ocpm1.get("itemId"));
			Predicate a9 = cb.equal(c.get("itemType"), ocpm1.get("itemType"));
			Predicate a10 = cb.equal(c.get("itemCode"), ocpm1.get("itemCode"));
			Predicate a5 = cb.equal(c.get("branchCode"), ocpm1.get("branchCode"));
			Predicate a6 = cb.equal(c.get("companyId"), ocpm1.get("companyId"));
			Predicate a2 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
			effectiveDate.where(a1, a2,a5,a6,a9,a10);
			// Effective Date End Max Filter
			Subquery<Date> effectiveDate2 = query.subquery(Date.class);
			Root<ListItemValue> ocpm2 = effectiveDate2.from(ListItemValue.class);
			effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd").as(Date.class)));
			Predicate a11 = cb.equal(c.get("itemType"), ocpm2.get("itemType"));
			Predicate a12 = cb.equal(c.get("itemCode"), ocpm2.get("itemCode"));
			Predicate a3 = cb.equal(c.get("itemId"), ocpm2.get("itemId"));
			Predicate a7 = cb.equal(c.get("companyId"), ocpm2.get("companyId"));
			Predicate a8 = cb.equal(c.get("branchCode"), ocpm2.get("branchCode"));
			Predicate a4 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
			effectiveDate2.where(a3, a4,a7,a8,a11,a12);


			// Where
			Predicate n1 = cb.equal(c.get("status"),"Y");
			Predicate n12 = cb.equal(c.get("status"),"R");
			Predicate n13 = cb.or(n1,n12);
			Predicate n2 = cb.equal(c.get("effectiveDateStart"), effectiveDate);
			Predicate n3 = cb.equal(c.get("effectiveDateEnd"), effectiveDate2);
			Predicate n4 = cb.equal(c.get("companyId"), insuranceId);
			Predicate n6 = cb.equal(c.get("branchCode"), branchCode);
			Predicate n7 = cb.equal(c.get("branchCode"), "99999");
			Predicate n9 = cb.or(n6, n7);
			Predicate n10 = cb.equal(c.get("itemType"), itemType);
			
			query.where(n13, n2, n3, n4, n9, n10).orderBy(orderList);
			
			
			// Get Result
			TypedQuery<ListItemValue> result = em.createQuery(query);
			list = result.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			return null;
		}
		return list;
	}
	
	@Override
	public SuccessRes savecontentrisk(ContentAndRiskSaveReq req) {
		SuccessRes res = new SuccessRes();
		DozerBeanMapper dozermapper = new DozerBeanMapper();
		
		try {
			Integer locationId=Integer.valueOf(req.getContentriskdetails().get(0).getLocationId());
			//List<ContentAndRisk> count = repo.findByQuoteNoAndType(req.getQuoteNo(),req.getType() );
			List<ContentAndRisk> count = repo.findByRequestReferenceNoAndSectionIdAndTypeAndLocationId(req.getRequestReferenceNo(),req.getSectionId(),req.getType(),locationId);
			count.removeIf(Objects::isNull);
			if (!count.isEmpty()) {
		        repo.deleteAll(count);
		    }
			
	
			ContentAndRisk saveData = new ContentAndRisk();
			Date entryDate = new Date();

			List<EserviceBuildingDetails> buidingData = new ArrayList<EserviceBuildingDetails>(); 
			//List<MotorDataDetails> motList = motorRepo.findByQuoteNoAndStatusNotOrderByVehicleIdAsc(req.getQuoteNo(),"D");
			List<MotorDataDetails> motList = motorRepo.findByRequestReferenceNoAndStatusNotOrderByVehicleIdAsc(req.getRequestReferenceNo(),"D");
			
            //buidingData = eserBuildingRepo.findByQuoteNoAndSectionId(req.getQuoteNo() , req.getSectionId());
            buidingData = eserBuildingRepo.findByRequestReferenceNoAndSectionIdAndRiskId(req.getRequestReferenceNo(),req.getSectionId(),1);
        	
            String productId = req.getProductid();
			String companyId = req.getCompanyid() ;
			String brancode=req.getBranchcode();
			
			if(buidingData==null||buidingData.isEmpty()) {
				productId =  motList.size()==0 ? "5" : motList.get(0).getProductId().toString();
				companyId =  motList.size()==0 ? "" : motList.get(0).getCompanyId().toString();
				brancode =  motList.size()==0 ? "" : motList.get(0).getBranchCode().toString();
			} else {
				productId =  buidingData.get(0).getProductId();
				companyId =  buidingData.get(0).getCompanyId();
				brancode =   buidingData.get(0).getBranchCode();
			}
			/*
			 * if(buidingData==null || buidingData.isEmpty()) { //productId =
			 * motList.size()==0 ? "5" : motList.get(0).getProductId().toString();
			 * //ompanyId = motList.size()==0 ? "" :
			 * motList.get(0).getCompanyId().toString(); brancode="99999"; } else {
			 * //productId = buidingData.get(0).getProductId(); //companyId =
			 * buidingData.get(0).getCompanyId();
			 * brancode=buidingData.get(0).getBranchCode(); }
			 */
          // buidingData = eserBuildingRepo.findByQuoteNoAndSectionId(req.getQuoteNo(), req.getSectionId());

            List<EserviceSectionDetails> section = sectionrepo.findByRequestReferenceNoAndSectionId(req.getRequestReferenceNo() , req.getSectionId());
			if(section==null || section.isEmpty()) {
				section = sectionrepo.findByRequestReferenceNoOrderByRiskIdAsc(req.getRequestReferenceNo());
			
			}
			
			ListItemValue type = listrepo.findByItemTypeAndItemCodeAndCompanyIdOrderByAmendIdDesc("CONTENT_RISK",req.getType(), req.getCompanyid()  ).get(0);
			
			saveData=dozermapper.map(req, ContentAndRisk.class);
			saveData.setEntryDate(entryDate);
			saveData.setCreatedBy(req.getCreatedBy());
			saveData.setStatus("Y");
			saveData.setQuoteNo(req.getQuoteNo());	
			saveData.setSectionDesc(section.isEmpty()?" ":section.get(0).getSectionName());
		//	saveData.setCompanyId(companyId);
			saveData.setTypeDesc(type.getItemValue());
			
			
			List<ContentRiskDetailsListReq> reqList =  req.getContentriskdetails()  ;
			
			Map<String,	List<ContentRiskDetailsListReq>> groupByRiskId = reqList.stream().collect(Collectors.groupingBy(ContentRiskDetailsListReq :: getRiskId))  ;
			
		//	EserviceBuildingDetails eserBuildingData = eserBuildingRepo.findByQuoteNoAndSectionId(req.getQuoteNo() , req.getSectionId());
//			EserviceBuildingDetails eserBuildingData = eserBuildingRepo.findByRequestReferenceNoAndSectionId(req.getRequestReferenceNo() , req.getSectionId());
		//	HomePositionMaster home = homeRepo.findByQuoteNo(req.getQuoteNo());
			//HomePositionMaster home = homeRepo.findByRequestReferenceNo(req.getRequestReferenceNo());
//			EserviceBuildingDetails home = eserBuildingRepo.findByRequestReferenceNoAndSectionId(req.getRequestReferenceNo(),req.getSectionId());

			for (String riskId : groupByRiskId.keySet() ) {
				List<ContentRiskDetailsListReq> filterData =  groupByRiskId.get(riskId) ;
				BigDecimal sno = BigDecimal.ZERO ;
				for(ContentRiskDetailsListReq reqdata :filterData) {
					Long no=0l;
//					List<ContentAndRisk> list=new ArrayList<ContentAndRisk>();
//				    // Obtain a CriteriaBuilder instance
//			        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
//			        
//			        // Create a CriteriaQuery instance
//			        CriteriaQuery<ContentAndRisk> criteriaQuery = criteriaBuilder.createQuery(ContentAndRisk.class);
//			        
//			        // Define the root of the query
//			        Root<ContentAndRisk> root = criteriaQuery.from(ContentAndRisk.class);
//			        
//			        // Define the query (select all)
//			        criteriaQuery.select(root);
//			        
//			        // Create and execute the query
//			    	TypedQuery<ContentAndRisk> result = em.createQuery(criteriaQuery);
//			    	list = result.getResultList();
//			    	list = list.stream().filter(distinctByKey(o -> Arrays.asList(o.getSno())))
//							.collect(Collectors.toList());
//			    	list.sort(Comparator.comparing(ContentAndRisk :: getSno ).reversed());				
////					List<ContentAndRisk> snoCount = repo.findAll();
//					if(list!=null && list.size()>0) {
//						no=Long.valueOf(list.get(0).getSno())+1;
//					}
					sno = sno.add(BigDecimal.ONE);
					List<ListItemValue> itemvalues = new ArrayList<ListItemValue>();
					if("42".equalsIgnoreCase(productId ) ) {
						itemvalues = getListItem( companyId,brancode,  "CYBER_INSURANCE_CONTENT",reqdata.getItemId() );
					} else {
						itemvalues = getListItem(companyId ,brancode, type.getItemType(),type.getItemCode());
					} 
					ListItemValue itemvalue =  itemvalues.size() > 0 ? itemvalues.get(0) : null ;
//					saveData.setSno(no);
					saveData.setItemId(Integer.valueOf(reqdata.getItemId()));
					saveData.setItemValue(new BigDecimal(reqdata.getItemId()));
//					saveData.setItemDesc(itemvalue ==null ? "" : itemvalue.getItemValue());
					saveData.setItemDesc(reqdata.getItemValue());
					saveData.setSumInsured(StringUtils.isBlank(reqdata.getSumInsured()) ? BigDecimal.ZERO : new BigDecimal(reqdata.getSumInsured()));
					saveData.setSerialNo(reqdata.getSerialNo());
					saveData.setLocationName(reqdata.getLocationName());
					saveData.setMakeAndModel(reqdata.getMakeAndModel());
					saveData.setRiskId(Integer.valueOf(reqdata.getRiskId()));
					saveData.setPurchaseYear((StringUtils.isBlank(reqdata.getPurchaseYear()))?"": reqdata.getPurchaseYear());
					saveData.setPurchaseMonth((StringUtils.isBlank(reqdata.getPurchaseMonth()))?"": reqdata.getPurchaseMonth());
					saveData.setContentRiskDesc(StringUtils.isBlank(reqdata.getContentRiskDesc())?"": reqdata.getContentRiskDesc());
					saveData.setSerialNoDesc(StringUtils.isBlank(reqdata.getSerialNoDesc())?"": reqdata.getSerialNoDesc());
					saveData.setManufactureYear(StringUtils.isBlank(reqdata.getManufactureYear())?"": reqdata.getManufactureYear());
					saveData.setBrand(reqdata.getBrand());
					saveData.setName(reqdata.getName());
					saveData.setLocationId(Integer.valueOf(reqdata.getLocationId()));
					
					if (buidingData.size()>0 &&buidingData!=null) {
						if (StringUtils.isNotBlank(buidingData.get(0).getExchangeRate().toString())) {
							BigDecimal exRate = buidingData.get(0).getExchangeRate();
							if (StringUtils.isNotBlank(reqdata.getSumInsured())) {
								saveData.setSumInsuredLc(new BigDecimal(reqdata.getSumInsured()).multiply(exRate));
							} else {
								saveData.setSumInsuredLc(BigDecimal.ZERO);
							}
						}
					}
					
					repo.saveAndFlush(saveData);
					res.setSuccessId(req.getRequestReferenceNo());
					res.setResponse("Saved Successful");

				}
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Log Details" + e.getMessage());
			return null;
		}

		return res;
	}


	@Override
	public ContentRiskGetRes getcontentrisk(ContentRiskGetReq req) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		ContentRiskGetRes res = new ContentRiskGetRes();
		DozerBeanMapper dozermapper = new DozerBeanMapper();
		try {
			
			ContentAndRisk data = repo.findByQuoteNoAndRiskIdAndSectionIdAndItemId(req.getQuoteNo(),Integer.valueOf(req.getRiskId()),req.getSectionId(),Integer.valueOf(req.getItemId()));
			if(data!=null) {
			res = dozermapper.map(data, ContentRiskGetRes.class);
			res.setRiskId(data.getRiskId().toString());
			res.setItemId(data.getItemId().toString());
			res.setItemValue(data.getItemValue().toString());
			res.setSumInsured(data.getSumInsured().toString());
			res.setSerialNo(data.getSerialNo().toString());
			res.setEntryDate(data.getEntryDate());
			res.setUpdatedDate(data.getUpdatedDate());
			res.setPurchaseYear(data.getPurchaseYear()==null?"":data.getPurchaseYear());
			res.setPurchaseMonth(data.getPurchaseMonth()==null?"":data.getPurchaseMonth());			
			}
		else {
			return res;
		}
		} 
		
		catch (Exception e) {
		e.printStackTrace();
		log.info("Log Details" + e.getMessage());
		return null;
	}
	

	return res;
	}

	@Override
	public ContentRiskGetallRes getallcontentrisk(ContentRiskGetAllReq req) {
		ContentRiskGetallRes res = new ContentRiskGetallRes();
		DozerBeanMapper dozermapper  = new DozerBeanMapper();
		try {
			List<ContentAndRisk> datas = null;
			if(StringUtils.isNotBlank(req.getLocationId())){
				datas = repo.findByQuoteNoAndSectionIdAndLocationId(req.getQuoteNo(),req.getSectionId(),Integer.valueOf(req.getLocationId()));
			}else{
				datas = repo.findByQuoteNoAndSectionId(req.getQuoteNo(),req.getSectionId());
			}
			if(datas!=null  && datas.size()>0 ) {

			res = dozermapper.map(datas.get(0),ContentRiskGetallRes.class);			
			res.setQuoteNo(datas.get(0).getQuoteNo());
			res.setRequestReferenceNo(datas.get(0).getRequestReferenceNo());
			res.setSectionId(datas.get(0).getSectionId());				
			res.setSectionDesc(datas.get(0).getSectionDesc());
			res.setType(datas.get(0).getType());
			res.setTypeDesc(datas.get(0).getTypeDesc());
			
			List<ContentRiskDetailsRes> resList1 = new ArrayList<ContentRiskDetailsRes>();;
		
			for(ContentAndRisk data : datas) {
				ContentRiskDetailsRes res1 = new ContentRiskDetailsRes();
				res1=dozermapper.map(data, ContentRiskDetailsRes.class);
				res1.setSumInsured(data.getSumInsured().toString());
				res1.setItemId(data.getItemId().toString());
				res1.setItemValue(data.getItemValue().toString());
				res1.setRiskId(data.getRiskId().toString()  );
				res1.setPurchaseYear(data.getPurchaseYear()==null?"":data.getPurchaseYear());
				res1.setPurchaseMonth(data.getPurchaseMonth()==null?"":data.getPurchaseMonth());			
				res1.setContentRiskDesc(data.getContentRiskDesc()==null?"":data.getContentRiskDesc());
				res1.setManufactureYear(data.getManufactureYear()==null?"":data.getManufactureYear());
				res1.setSerialNoDesc(data.getSerialNoDesc()==null?"":data.getSerialNoDesc());
				res1.setLocationName(data.getLocationName()==null?"":data.getLocationName());
				res1.setLocationId(data.getLocationId()==null?"":data.getLocationId().toString());
				resList1.add(res1);
				
			}
			resList1.sort( Comparator.comparing(ContentRiskDetailsRes :: getRiskId ));
			res.setContentriskdetails(resList1);
		}
			else {
				return res;
			
		}
		}
		catch (Exception e) {
		e.printStackTrace();
		log.info("Log Details" + e.getMessage());
		return null;
	}

	return res;
	}		
		
	private static <T> java.util.function.Predicate<T> distinctByKey(
			java.util.function.Function<? super T, ?> keyExtractor) {
		Map<Object, Boolean> seen = new ConcurrentHashMap<>();
		return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}
	
	
	@Override
	public AdditionalInformationResponse saveOrUpdate(AdditionalInformationRequest req) {

	    HomePositionMaster home = homeRepo.findByQuoteNo(req.getQuoteNo());
	    if (home == null) {
	        throw new RuntimeException("No Home record found for QuoteNo: " + req.getQuoteNo());
	    }

	    List<AdditionalInformation> allEntities = new ArrayList<>();

	    for (ContentItem item : req.getContentItems()) {

	        String sectionId = item.getSectionId(); 
	        String coverId = item.getCoverId();     

	        additionalInformationrepo.deleteByQuoteNoAndRequestReferenceNoAndCompanyIdAndProductIdAndSectionIdAndCoverId(
	                req.getQuoteNo(), home.getRequestReferenceNo(), home.getCompanyId(),
	                String.valueOf(home.getProductId()), sectionId, coverId);

	        AdditionalInformation entity = AdditionalInformation.builder()
	                .quoteNo(req.getQuoteNo())
	                .requestReferenceNo(home.getRequestReferenceNo())
	                .companyId(home.getCompanyId())
	                .productId(String.valueOf(home.getProductId()))
	                .sectionId(sectionId)
	                .coverId(coverId)
	                .param1(item.getParam1())
	                .value(item.getValue())
	                .param3(item.getParam3())
	                .param4(item.getParam4())
	                .param5(item.getParam5())
	                .param6(item.getParam6())
	                .param7(item.getParam7())
	                .param8(item.getParam8())
	                .param9(item.getParam9())
	                .param10(item.getParam10())
	                .build();

	        allEntities.add(entity);
	    }

	    // 6️⃣ Save All
	    additionalInformationrepo.saveAll(allEntities);

	    // 7️⃣ Prepare Response
	    List<ContentItemResponse> contentItems = allEntities.stream()
	            .map(e -> ContentItemResponse.builder()
	                    .contentId(e.getContentId())
	                    .sectionId(e.getSectionId())
	                    .coverId(e.getCoverId())
	                    .param1(e.getParam1())
	                    .value(e.getValue())
	                    .param3(e.getParam3())
	                    .param4(e.getParam4())
	                    .param5(e.getParam5())
	                    .param6(e.getParam6())
	                    .param7(e.getParam7())
	                    .param8(e.getParam8())
	                    .param9(e.getParam9())
	                    .param10(e.getParam10())
	                    .build())
	            .collect(Collectors.toList());

	    return AdditionalInformationResponse.builder()
	            .response("Success")
	            .quoteNo(req.getQuoteNo())
	            .requestReferenceNo(home.getRequestReferenceNo())
	            .companyId(home.getCompanyId())
	            .productId(String.valueOf(home.getProductId()))
	            .contentItems(contentItems)
	            .build();
	}

	
	@Override
	public AdditionalInformationResponse getByQuoteAndRef(AdditionalInfoGetReq req) {

	    List<AdditionalInformation> list =
	            additionalInformationrepo.findByQuoteNoAndRequestReferenceNo(
	                    req.getQuoteNo(), req.getRequestReferenceNo());

	    if (list == null || list.isEmpty()) {
	        return AdditionalInformationResponse.builder()
	                .response("No Data")
	                .quoteNo(req.getQuoteNo())
	                .requestReferenceNo(req.getRequestReferenceNo())
	                .companyId(null)
	                .productId(null)
	                .contentItems(Collections.emptyList())
	                .build();
	    }

	    AdditionalInformation first = list.get(0);

	    List<ContentItemResponse> contentItems = list.stream()
	            .map(e -> ContentItemResponse.builder()
	                    .contentId(e.getContentId())
	                    .sectionId(e.getSectionId())
	                    .coverId(e.getCoverId())
	                    .param1(e.getParam1())
	                    .value(e.getValue())
	                    .param3(e.getParam3())
	                    .param4(e.getParam4())
	                    .param5(e.getParam5())
	                    .param6(e.getParam6())
	                    .param7(e.getParam7())
	                    .param8(e.getParam8())
	                    .param9(e.getParam9())
	                    .param10(e.getParam10())
	                    .build())
	            .collect(Collectors.toList());

	    return AdditionalInformationResponse.builder()
	            .response("Success")
	            .quoteNo(req.getQuoteNo())
	            .requestReferenceNo(req.getRequestReferenceNo())
	            .companyId(first.getCompanyId())
	            .productId(first.getProductId())
	            .contentItems(contentItems)
	            .build();
	}

	
	@Override
	public List<String> validateAdditionalInformation(AdditionalInformationRequest req) {
	    List<String> error = new ArrayList<>();

	    // 1️⃣ Basic validation
	    if (req.getQuoteNo() == null || req.getQuoteNo().isBlank()) {
	        error.add("1755"); // Missing QuoteNo
	        return error;
	    }

	    HomePositionMaster home = homeRepo.findByQuoteNo(req.getQuoteNo());
	    if (home == null) {
	        error.add("1756"); // Home record not found
	        return error;
	    }

	    // 2️⃣ Get product type
	    List<CompanyProductMaster> companyProductList =
	            companyrepo.findByCompanyIdAndProductIdOrderByAmendIdDesc(
	                    home.getCompanyId(), home.getProductId());

	    if (companyProductList.isEmpty()) {
	        error.add("1759"); // Company product not configured
	        return error;
	    }

	    CompanyProductMaster companyProduct = companyProductList.get(0);
	    String motorYN = companyProduct.getMotorYn(); // A / H

	    // 3️⃣ Group ContentItems by (sectionId, coverId)
	    Map<String, List<ContentItem>> groupedBySecCover = req.getContentItems() == null
	            ? Collections.emptyMap()
	            : req.getContentItems().stream()
	                .filter(Objects::nonNull)
	                .collect(Collectors.groupingBy(ci -> (ci.getSectionId() + "|" + ci.getCoverId())));

	    // Flag to ensure we add "1757" only once overall
	    boolean sumMismatchAdded = false;

	    for (Map.Entry<String, List<ContentItem>> entry : groupedBySecCover.entrySet()) {

	        List<ContentItem> groupItems = entry.getValue();
	        if (groupItems == null || groupItems.isEmpty()) {
	            continue;
	        }

	        String sectionId = groupItems.get(0).getSectionId();
	        String coverId   = groupItems.get(0).getCoverId();

	        if (sectionId == null || coverId == null) {
	            error.add("1760");
	            continue;
	        }

	        BigDecimal declaredSumInsured = BigDecimal.ZERO;

	        // 4️⃣ Fetch declaredSumInsured ONCE per sectionId/coverId based on product type
	        if ("A".equalsIgnoreCase(motorYN)) {
	            // 🔹 BuildingRiskDetails
	            List<BuildingRiskDetails> buildingList = buildingRiskRepo
	                    .findByQuoteNoAndCompanyIdAndProductIdAndSectionIdAndCoverId(
	                            home.getQuoteNo(),
	                            home.getCompanyId(),
	                            String.valueOf(home.getProductId()),
	                            sectionId,
	                            Integer.valueOf(coverId)
	                    );

	            if (buildingList == null || buildingList.isEmpty()) {
	                error.add("1758"); // No building data
	                continue;
	            }

	            declaredSumInsured = buildingList.get(0).getSumInsured() == null
	                    ? BigDecimal.ZERO
	                    : buildingList.get(0).getSumInsured();

	        } else if ("H".equalsIgnoreCase(motorYN)) {
	            // 🔹 CommonDataDetails
	            List<CommonDataDetails> commonList = commonDataRepo
	                    .findByQuoteNoAndCompanyIdAndProductIdAndSectionIdAndCoverId(
	                            home.getQuoteNo(),
	                            home.getCompanyId(),
	                            String.valueOf(home.getProductId()),
	                            sectionId,
	                            Integer.valueOf(coverId)
	                    );

	            if (commonList == null || commonList.isEmpty()) {
	                error.add("1758"); // No common data
	                continue;
	            }

	            declaredSumInsured = commonList.get(0).getSumInsured() == null
	                    ? BigDecimal.ZERO
	                    : commonList.get(0).getSumInsured();
	        } else {
	            error.add("1761"); // Product type not supported
	            continue;
	        }

	     // 5️⃣ Calculate TOTAL from request for this sectionId/coverId group
	     BigDecimal totalFromItems = BigDecimal.ZERO;
	     boolean invalidNumber = false;

	     for (ContentItem ci : groupItems) {

	         if (ci == null) {
	             continue;
	         }

	         String value = ci.getValue();

	         if (value == null || value.isBlank()) {
	             continue;
	         }

	         try {
	             BigDecimal itemValue = new BigDecimal(value.trim());

	             // Individual value should not exceed declared Sum Insured
	             if (itemValue.compareTo(declaredSumInsured) > 0) {
	                 if (!sumMismatchAdded) {
	                     error.add("1757"); // Sum insured mismatch
	                     sumMismatchAdded = true;
	                 }
	                 invalidNumber = true;
	                 break;
	             }

	             totalFromItems = totalFromItems.add(itemValue);

	         } catch (NumberFormatException e) {

	             error.add("1762"); // Invalid numeric value
	             invalidNumber = true;
	             break;
	         }
	     }

	     if (invalidNumber) {
	         continue;
	     }

	     // 6️⃣ Final TOTAL must EXACTLY match declared Sum Insured
	     if (declaredSumInsured.compareTo(totalFromItems) != 0) {

	         if (!sumMismatchAdded) {
	             error.add("1757"); // Sum insured mismatch
	             sumMismatchAdded = true;
	         }
	     }
	    }

	    // 7️⃣ Optional: remove duplicate error codes so each code appears only once overall
	    List<String> deduped = new ArrayList<>(new LinkedHashSet<>(error));

	    return deduped;
	}




}
