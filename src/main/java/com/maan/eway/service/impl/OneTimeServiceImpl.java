package com.maan.eway.service.impl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.maan.eway.bean.CompanyProductMaster;
import com.maan.eway.bean.EserviceBuildingDetails;
import com.maan.eway.bean.EserviceCommonDetails;
import com.maan.eway.bean.EserviceCustomerDetails;
import com.maan.eway.bean.EserviceDriverDetails;
import com.maan.eway.bean.EserviceMotorDetails;
import com.maan.eway.bean.EserviceTravelDetails;
import com.maan.eway.bean.EserviceTravelGroupDetails;
import com.maan.eway.bean.MsCommonDetails;
import com.maan.eway.bean.MsCustomerDetails;
import com.maan.eway.bean.MsLifeDetails;
import com.maan.eway.bean.MsVehicleDetails;
import com.maan.eway.bean.ProductSectionMaster;
import com.maan.eway.bean.SeqOnetimetable;
import com.maan.eway.common.req.ShortQuote;
import com.maan.eway.common.res.CommonRes;
import com.maan.eway.config.onetime.Thread_OneTime;
import com.maan.eway.config.thread.MyTaskList;
import com.maan.eway.repository.EServiceBuildingDetailsRepository;
import com.maan.eway.repository.EServiceDriverDetailsRepository;
import com.maan.eway.repository.EServiceMotorDetailsRepository;
import com.maan.eway.repository.EServiceSectionDetailsRepository;
import com.maan.eway.repository.EserviceCommonDetailsRepository;
import com.maan.eway.repository.EserviceCustomerDetailsRepository;
import com.maan.eway.repository.EserviceTravelDetailsRepository;
import com.maan.eway.repository.EserviceTravelGroupDetailsRepository;
import com.maan.eway.repository.MsAssetDetailsRepository;
import com.maan.eway.repository.MsCommonDetailsRepository;
import com.maan.eway.repository.MsCustomerDetailsRepository;
import com.maan.eway.repository.MsDriverDetailsRepository;
import com.maan.eway.repository.MsHumanDetailsRepository;
import com.maan.eway.repository.MsLifeDetailsRepository;
import com.maan.eway.repository.MsVehicleDetailsRepository;
import com.maan.eway.repository.SeqOnetimetableRepository;
import com.maan.eway.req.OneTimeTableReq;
import com.maan.eway.res.MsCommonDetailsRes;
import com.maan.eway.res.MsCustomerDetailsRes;
import com.maan.eway.res.MsDetailsRes;
import com.maan.eway.res.MsVehicleDetailsRes;
import com.maan.eway.res.OneTimeTableRes;
import com.maan.eway.res.OneTimeVehicleRes;
import com.maan.eway.service.OneTimeService;
import com.maan.eway.upgrade.criteria.CriteriaService;
import com.maan.eway.upgrade.criteria.SpecCriteria;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

@Service
@Transactional
public class OneTimeServiceImpl implements OneTimeService {

	//@Lazy
	@Autowired
	private OneTimeService otSer;
	
	private ForkJoinPool forkjoin = new ForkJoinPool(2);
	
	@Autowired
	private EserviceCustomerDetailsRepository eserviceCustomerRepo ;
	
	@Autowired
	private MsCustomerDetailsRepository msCustomerRepo ;
	
	@Autowired
	private EServiceMotorDetailsRepository eserviceMotorRepo ;
	
	@Autowired
	private EServiceDriverDetailsRepository esDriverRepo;
	
	@Autowired
	private MsVehicleDetailsRepository msVehicleRepo ;
	
	@Autowired
	private MsCommonDetailsRepository msCommonRepo ;
	
	@Autowired
	private MsLifeDetailsRepository msLifeRepo;
	
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private EserviceTravelDetailsRepository eserTravelRepo ;
	@Autowired
	private MsHumanDetailsRepository msHumanRepo ;
	@Autowired
	private EserviceTravelGroupDetailsRepository eserGroupRepo ;
	@Autowired
	private EServiceBuildingDetailsRepository eserBuildRepo ;
	

	@Autowired
	private MsAssetDetailsRepository msAssetRepo ;
	
	@Autowired
	private EserviceCommonDetailsRepository eserCommonRepo ;
	
	@Autowired
	private SeqOnetimetableRepository oneNoRepo ;
	
	@Autowired
	private MsDriverDetailsRepository msDriverRepo ;
	
	@Value(value = "${travel.productId}")
	private String travelProductId;
	
	
	@Autowired
	private DozerBeanMapper dozerMapper;
	
	
	private Logger log = LogManager.getLogger(OneTimeTableServiceImpl.class);
	
	private EserviceMotorDetails motorDatas=null ;
	private EserviceDriverDetails motorDriverDatas=null ;
	private EserviceTravelDetails travelData=null ;
	private EserviceCustomerDetails custData = null ;
	List<EserviceTravelGroupDetails> groupDatas = null;
	private List<EserviceBuildingDetails> buildingDatas=null ;
	private List<EserviceCommonDetails> esercommonDatas =null ;
	
	@Autowired
	private EServiceSectionDetailsRepository eserSecRepo ;

	
	@Transactional
	@Override
	public List<OneTimeTableRes> call_OT_Insert(OneTimeTableReq req) {
		List<OneTimeTableRes> resList = new ArrayList<OneTimeTableRes>();
	//	SimpleDateFormat sdf = new SimpleDateFormat("yyMMddhhmmssSS");
	//	DozerBeanMapper dozerMapper = new DozerBeanMapper();
		try {
			String custRefNo = "" ;
			List<Callable<Object>> queue = new ArrayList<Callable<Object>>();

			MyTaskList taskList = new MyTaskList(queue);
			CompanyProductMaster product = new CompanyProductMaster();
			if(req.isY())
			{
				product = req.getProduct();
			}
			else
			{
				product =  getCompanyProductMasterDropdown(req.getInsuranceId() , req.getProductId().toString());
			}
			

			// Product Id Based Insert
			if (product.getMotorYn().equalsIgnoreCase("H") && product.getProductId().equals(Integer.valueOf(travelProductId))) {

				travelData = eserTravelRepo.findByRequestReferenceNoAndRiskIdAndSectionId(req.getRequestReferenceNo(), req.getId(),req.getSectionId());
				Thread_OneTime msTravel = new Thread_OneTime("MSTravel", req, otSer, em, custData, msVehicleRepo,eserviceMotorRepo, msCustomerRepo, eserviceCustomerRepo, motorDatas, eserTravelRepo,
						msHumanRepo, travelData, groupDatas, eserGroupRepo, eserBuildRepo, msAssetRepo, buildingDatas,eserSecRepo, oneNoRepo, esercommonDatas, eserCommonRepo,esDriverRepo,motorDriverDatas,msDriverRepo,dozerMapper,req.isY());
				queue.add(msTravel);
				custRefNo = travelData.getCustomerReferenceNo();
				custData = eserviceCustomerRepo.findByCustomerReferenceNo(custRefNo);

			} else if (product.getMotorYn().equalsIgnoreCase("M")) { 
				//Eservice Motor Details
				if (req.getMotorDetails() != null) {
					motorDatas = req.getMotorDetails();
				} else {
					motorDatas = eserviceMotorRepo.findByRequestReferenceNoAndRiskId(req.getRequestReferenceNo(),Integer.valueOf(req.getVehicleId()));
				}
				custRefNo = motorDatas.getCustomerReferenceNo();
				custData = eserviceCustomerRepo.findByCustomerReferenceNo(custRefNo);
				//Section Based Insert
				for(String section:req.getSectionIds()) {
					OneTimeTableReq newreq=new OneTimeTableReq();
					//newreq=req;
					dozerMapper.map(req, newreq);
					newreq.setSectionId(Integer.valueOf(section));
					Thread_OneTime msVehicle = new Thread_OneTime("MSVehicle", newreq, otSer, em, custData, msVehicleRepo,eserviceMotorRepo, msCustomerRepo, eserviceCustomerRepo, motorDatas, eserTravelRepo,
							msHumanRepo, travelData, groupDatas, eserGroupRepo, eserBuildRepo, msAssetRepo, buildingDatas,eserSecRepo, oneNoRepo, esercommonDatas, eserCommonRepo,esDriverRepo,motorDriverDatas,msDriverRepo,dozerMapper,req.isY());
					queue.add(msVehicle);
				}
				
				//Eservice Driver Details
				if (req.getMotorDriverDetails() != null) {
					motorDriverDatas = req.getMotorDriverDetails();
				} else {
					motorDriverDatas = esDriverRepo.findByRequestReferenceNoAndRiskId(req.getRequestReferenceNo(),Integer.valueOf(req.getVehicleId()));
				}
				if(motorDriverDatas!=null) {
						Thread_OneTime msDriver = new Thread_OneTime("MsDriver", req, otSer, em, custData, msVehicleRepo,eserviceMotorRepo, msCustomerRepo, eserviceCustomerRepo, motorDatas, eserTravelRepo,
						msHumanRepo, travelData, groupDatas, eserGroupRepo, eserBuildRepo, msAssetRepo, buildingDatas,eserSecRepo, oneNoRepo, esercommonDatas, eserCommonRepo,esDriverRepo,motorDriverDatas,msDriverRepo,dozerMapper,req.isY());
						queue.add(msDriver);
				}
			} else {

				if (req.getEserviceBuilding() != null || req.getEserviceCommon() != null) {
					buildingDatas = req.getEserviceBuilding();
					esercommonDatas = req.getEserviceCommon();
				} else {
					buildingDatas = eserBuildRepo.findByRequestReferenceNo(req.getRequestReferenceNo());
					esercommonDatas = eserCommonRepo.findByRequestReferenceNo(req.getRequestReferenceNo());
				}

				Thread_OneTime msTravel = new Thread_OneTime("MSAssetOrMsHuman", req, otSer, em, custData,
						msVehicleRepo, eserviceMotorRepo, msCustomerRepo, eserviceCustomerRepo, motorDatas,
						eserTravelRepo, msHumanRepo, travelData, groupDatas, eserGroupRepo, eserBuildRepo, msAssetRepo,
						buildingDatas, eserSecRepo, oneNoRepo, esercommonDatas, eserCommonRepo, esDriverRepo,
						motorDriverDatas, msDriverRepo, dozerMapper,req.isY());
				queue.add(msTravel);
				if (buildingDatas != null && buildingDatas.size() > 0) {
					custRefNo = buildingDatas.get(0).getCustomerReferenceNo();
				} else if (esercommonDatas != null && esercommonDatas.size() > 0) {
					custRefNo = esercommonDatas.get(0).getCustomerReferenceNo();
				}
				if (req.getEcustomer() == null) {
					custData = eserviceCustomerRepo.findByCustomerReferenceNo(custRefNo);
				} else {
					custData = req.getEcustomer();
				}

			}
//			else  {
//				esercommonDatas = new ArrayList<>();
//				esercommonDatas.add(eserCommonRepo.findByRequestReferenceNoAndRiskId(req.getRequestReferenceNo(),req.getVehicleId()));
//				
//				Thread_OneTime msPersonalAcc = new Thread_OneTime("MsHuman", req, otSer, em,custData, msVehicleRepo, eserviceMotorRepo, msCustomerRepo, eserviceCustomerRepo,motorDatas,eserTravelRepo,msHumanRepo
//							,travelData,groupDatas,eserGroupRepo,eserBuildRepo,msAssetRepo,buildingDatas,eserSecRepo,oneNoRepo,esercommonDatas ,eserCommonRepo);
//					queue.add(msPersonalAcc);	
//					custRefNo = esercommonDatas.get(0).getCustomerReferenceNo() ;
//			}
			
			
			
 			Thread_OneTime msCustomer = new Thread_OneTime("MSCustomer", req, otSer, em,custData, msVehicleRepo, eserviceMotorRepo, msCustomerRepo, eserviceCustomerRepo,motorDatas,eserTravelRepo,msHumanRepo
					,travelData,groupDatas,eserGroupRepo,eserBuildRepo,msAssetRepo,buildingDatas,eserSecRepo,oneNoRepo,esercommonDatas ,eserCommonRepo,esDriverRepo,motorDriverDatas,msDriverRepo,dozerMapper,req.isY());
			queue.add(msCustomer);

			ConcurrentLinkedQueue<Future<Object>> invoke = (ConcurrentLinkedQueue<Future<Object>>) forkjoin
					.invoke(taskList);

			int success = 0;

			List<OneTimeVehicleRes> oneTimeResList = new ArrayList<OneTimeVehicleRes>();
			
			String cdRefno = "";
			String msCust=null;
			String msVehDet = null;
			String ddRefno="0";
			for (Future<Object> callable : invoke) {

				log.info(callable.getClass() + "," + callable.isDone());

				if (callable.isDone()) {
					Map<String, Object> map = (Map<String, Object>) callable.get();

					for (Entry<String, Object> future : map.entrySet()) {
						if ("MSVehicle".equalsIgnoreCase(future.getKey())) {

							List<OneTimeVehicleRes> response= (List<OneTimeVehicleRes>) future.getValue();
							 
							 oneTimeResList.addAll(response);
						}else if ("MsDriver".equalsIgnoreCase(future.getKey())) {

							//cdRefno = (String) future.getValue();
							ddRefno = (String) future.getValue();

						} else if ("MSCustomer".equalsIgnoreCase(future.getKey())) {

							//cdRefno = (String) future.getValue();
							cdRefno = (String) future.getValue();

						} else if ("MSTravel".equalsIgnoreCase(future.getKey())) {

							//cdRefno = (String) future.getValue();
							oneTimeResList = (List<OneTimeVehicleRes>) future.getValue();

						}
						else if ("MSAsset".equalsIgnoreCase(future.getKey())) {

							//cdRefno = (String) future.getValue();
							oneTimeResList = (List<OneTimeVehicleRes>) future.getValue();

						}
						else if ("MsHuman".equalsIgnoreCase(future.getKey())) {

							//cdRefno = (String) future.getValue();
							oneTimeResList = (List<OneTimeVehicleRes>) future.getValue();

						}
						else if ("MsDriver".equalsIgnoreCase(future.getKey())) {

							//cdRefno = (String) future.getValue();
							oneTimeResList = (List<OneTimeVehicleRes>) future.getValue();

						}
					}

					success++;
				}
			}
			/*	resList.add(vdRefNo);
			resList.add(sectionId);
			resList.add(agencyCode);
			resList.add(branchCode);
			resList.add(productId);
			resList.add(companyId); */
			
			
			for (OneTimeVehicleRes oneTimeRes : oneTimeResList) {
				OneTimeTableRes response = new OneTimeTableRes();
				
				String vdRefno = oneTimeRes.getVdRefNo() ;
				// = oneTimeRes.getDdRefNo() ;
				String sectionId = oneTimeRes.getSectionId() ;
				String agencyCode = oneTimeRes.getAgencyCode() ;
				String branchCode = oneTimeRes.getBranchCode() ;
				String productId = oneTimeRes.getProductId() ;
				String companyId = oneTimeRes.getCompanyId() ;
				
				String msRefNo = "" ;
				String complete = (queue.size() == success) ? "yes" : "No";
				log.info("call_OTInsert--> complete: " + complete);
				
				// Save Common Details
				MsCommonDetails findData =null;
				if(!req.isY())
				{
					findData = msCommonRepo.findByCdRefnoAndVdRefnoAndRequestreferencenoAndAgencyCodeAndBranchCodeAndInsuranceIdAndProductIdAndSectionIdAndStatusAndDdRefno(Long.valueOf(cdRefno) ,Long.valueOf(vdRefno) , req.getRequestReferenceNo(),req.getAgencyCode(),req.getBranchCode(),companyId,Integer.valueOf(productId),Integer.valueOf(sectionId),"Y",Long.parseLong(ddRefno)) ;
				}
				
				if( findData !=null) {
					msRefNo = String.valueOf(findData.getMsRefno());
				} else {
					 
				///	Random rand = new Random();
		        //     int random=rand.nextInt(90)+10; 
					msRefNo = genOneTimeTableRefNo();//sdf.format(new Date()) + random ; ; 
					MsCommonDetails saveData = new MsCommonDetails();
					saveData.setAgencyCode(agencyCode);
					saveData.setBranchCode(branchCode);
					saveData.setInsuranceId(companyId);
					saveData.setProductId(Integer.valueOf(productId)) ;
					saveData.setRequestreferenceno(req.getRequestReferenceNo());
					saveData.setCdRefno(Long.valueOf(cdRefno));
					saveData.setEntryDate(new Date());
					saveData.setVdRefno(Long.valueOf(vdRefno));
					saveData.setMsRefno(Long.valueOf(msRefNo));
					saveData.setDdRefno(Long.valueOf(ddRefno));
					saveData.setStatus("Y");
					saveData.setSectionId(Integer.valueOf(sectionId));
					saveData.setLocationId(Integer.valueOf(oneTimeRes.getLocationId()));					
					saveData.setParam1(StringUtils.isBlank(oneTimeRes.getParam1())?"":oneTimeRes.getParam1());
					saveData.setParam2(StringUtils.isBlank(oneTimeRes.getParam2())?"":oneTimeRes.getParam2());
					saveData.setParam3(StringUtils.isBlank(oneTimeRes.getParam3())?"":oneTimeRes.getParam3());
					saveData.setParam4(StringUtils.isBlank(oneTimeRes.getParam4())?"":oneTimeRes.getParam4());
					saveData.setParam5(StringUtils.isBlank(oneTimeRes.getParam5())?"":oneTimeRes.getParam5());
					saveData.setParam6(StringUtils.isBlank(oneTimeRes.getParam6())?"":oneTimeRes.getParam6());
					saveData.setParam7(StringUtils.isBlank(oneTimeRes.getParam7())?"":oneTimeRes.getParam7());
					saveData.setParam8(StringUtils.isBlank(oneTimeRes.getParam8())?"":oneTimeRes.getParam8());
					saveData.setParam9(StringUtils.isBlank(oneTimeRes.getParam9())?"":oneTimeRes.getParam9());
					saveData.setParam10(StringUtils.isBlank(oneTimeRes.getParam10())?"":oneTimeRes.getParam10());
					if(req.isY())
					{
						req.setMsCommon(saveData);
					}else
					{
						msCommonRepo.saveAndFlush(saveData);
					}
                 }
				if (product.getMotorYn().equalsIgnoreCase("H") && product.getProductId().equals(Integer.valueOf(travelProductId))) {

					EserviceTravelDetails eserTravel =  travelData ; 
					eserTravel.setVdRefno(Long.valueOf(vdRefno));
					eserTravel.setCdRefno(Long.valueOf(cdRefno));
					eserTravel.setMsRefno(Long.valueOf(msRefNo));
					eserTravelRepo.saveAndFlush(eserTravel);
					
				} else if (product.getMotorYn().equalsIgnoreCase("M")) {
					EserviceMotorDetails	motorData = new EserviceMotorDetails(); 
					if (req.getMotorDetails() != null) {
					   motorData = req.getMotorDetails();
					} else {
					   motorData = motorDatas ;
					}
					motorData.setVdRefno(Long.valueOf(vdRefno));
					motorData.setCdRefno(Long.valueOf(cdRefno));
					motorData.setMsRefno(Long.valueOf(msRefNo));
					eserviceMotorRepo.saveAndFlush(motorData);
					//Eservice Driver Details
					EserviceDriverDetails	motorDriverData = new EserviceDriverDetails();
					if (req.getMotorDriverDetails() != null) {
						motorDriverData = req.getMotorDriverDetails();
					} else {
						motorDriverData = motorDriverDatas;
					}
					if(motorDriverData!=null) {
					motorDriverData.setVdRefno(Long.valueOf(vdRefno)); 
					motorDriverData.setCdRefno(Long.valueOf(cdRefno));
					motorDriverData.setMsRefno(Long.valueOf(msRefNo));
					motorDriverData.setDdRefno(Long.valueOf(ddRefno));
					esDriverRepo.saveAndFlush(motorDriverData);
					}
					

				} else {
					List<ProductSectionMaster> sectionList = new ArrayList<ProductSectionMaster>();
					if(req.getPsm()!=null && !req.getPsm().isEmpty())
					{
						sectionList=req.getPsm();
					}
					else
					{
						sectionList= getProductSectionDropdown(req.getInsuranceId(), req.getProductId().toString());
					}
				//	List<ProductSectionMaster> sectionList = getProductSectionDropdown(req.getInsuranceId(), req.getProductId().toString());
					List<ProductSectionMaster> filterSecList = sectionList.stream().filter( o -> o.getSectionId().equals(Integer.valueOf(oneTimeRes.getSectionId()) ) )
							.collect(Collectors.toList());
							
					if(filterSecList.size() > 0 ) {
						ProductSectionMaster secData = filterSecList.get(0) ;
						
//						if (secData.getMotorYn().equalsIgnoreCase("H")) {
//							List<EserviceCommonDetails> filterCommons = esercommonDatas.stream().filter( o -> o.getRiskId().equals(Integer.valueOf(oneTimeRes.getVehicleId())) 
//									&& o.getSectionId().equalsIgnoreCase(oneTimeRes.getSectionId())
//									&& String.valueOf(o.getCoverId()).equals(oneTimeRes.getCoverid())
//									&& o.getLocationId().equals(Integer.valueOf(oneTimeRes.getLocationId()))).collect(Collectors.toList());								
//							if(filterCommons.size() > 0 ) {
//								EserviceCommonDetails com = filterCommons.get(0);
//								com.setVdRefno(Long.valueOf(vdRefno));
//								com.setCdRefno(Long.valueOf(cdRefno));
//								com.setMsRefno(Long.valueOf(msRefNo));
//								eserCommonRepo.saveAndFlush(com);
//							}
//							
//							
//						} 
						if (secData.getMotorYn().equalsIgnoreCase("H")) {

							for (EserviceCommonDetails com : esercommonDatas) {

								if (com.getRiskId().equals(Integer.valueOf(oneTimeRes.getVehicleId()))
										&& com.getSectionId().equalsIgnoreCase(oneTimeRes.getSectionId())
										&& String.valueOf(com.getCoverId()).equals(oneTimeRes.getCoverid())
										&& com.getLocationId().equals(Integer.valueOf(oneTimeRes.getLocationId()))) {

									com.setVdRefno(Long.valueOf(vdRefno));

									com.setCdRefno(Long.valueOf(cdRefno));

									com.setMsRefno(Long.valueOf(msRefNo));

									break;

								}

							}

						}

						 else {
							//List<EserviceBuildingDetails> filterBuilding = buildingDatas.stream().filter( o -> o.getRiskId().equals(Integer.valueOf(oneTimeRes.getVehicleId())) 
							//		&& o.getSectionId().equalsIgnoreCase(oneTimeRes.getSectionId())
							//		&& String.valueOf(o.getCoverId()).equals(oneTimeRes.getCoverid())
							//		&& o.getLocationId().equals(Integer.valueOf(oneTimeRes.getLocationId()))
							//		).collect(Collectors.toList());		
							//if(filterBuilding.size() > 0 ) {
							//	EserviceBuildingDetails com = filterBuilding.get(0);
							//	com.setVdRefno(Long.valueOf(vdRefno));
							//	com.setCdRefno(Long.valueOf(cdRefno));
							//	com.setMsRefno(Long.valueOf(msRefNo));
							//	eserBuildRepo.saveAndFlush(com);
							//}
							for (EserviceBuildingDetails com : buildingDatas) {
							    if (com.getRiskId().equals(Integer.valueOf(oneTimeRes.getVehicleId()))
							&& com.getSectionId().equalsIgnoreCase(oneTimeRes.getSectionId())
							&& String.valueOf(com.getCoverId()).equals(oneTimeRes.getCoverid())
							&& com.getLocationId().equals(Integer.valueOf(oneTimeRes.getLocationId()))) {
							 
							        com.setVdRefno(Long.valueOf(vdRefno));
							        com.setCdRefno(Long.valueOf(cdRefno));
							        com.setMsRefno(Long.valueOf(msRefNo));
							        break;
							    }
							}
						}
						
					}

				}
				//Response 
				response.setCdRefNo(cdRefno);
				response.setVdRefNo(vdRefno);
				response.setMsRefNo(msRefNo);
				response.setRequestReferenceNo(req.getRequestReferenceNo());
				response.setVehicleId(oneTimeRes.getVehicleId()==null?"" : oneTimeRes.getVehicleId());
				response.setProductId(productId);
				response.setSectionId(sectionId);
				response.setProductId(productId);
				response.setCompanyId(companyId);
				response.setDdRefNo(ddRefno);
				response.setLocationId(oneTimeRes.getLocationId()==null?"1" : oneTimeRes.getLocationId());
				resList.add(response);
			}
			
			
			
			return resList;
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is --> " +  e.getMessage());
			return null;	
		}
		
	}
	
	public List<ProductSectionMaster> getProductSectionDropdown(String companyId, String productId) {
		List<ProductSectionMaster> sectionList = new ArrayList<ProductSectionMaster>();
		try {
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
			CriteriaQuery<ProductSectionMaster> query = cb.createQuery(ProductSectionMaster.class);
		
			// Find All
			Root<ProductSectionMaster> c = query.from(ProductSectionMaster.class);

			// Select
			query.select(c);

			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(c.get("sectionName")));

			// Effective Date Max Filter
			Subquery<Date> effectiveDate = query.subquery(Date.class);
			Root<ProductSectionMaster> ocpm1 = effectiveDate.from(ProductSectionMaster.class);
			effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart").as(Date.class)));
			Predicate a1 = cb.equal(c.get("sectionId"), ocpm1.get("sectionId"));
			Predicate a2 = cb.equal(c.get("companyId"), ocpm1.get("companyId"));
			Predicate a3 = cb.equal(c.get("productId"), ocpm1.get("productId"));
			Predicate a4 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
			effectiveDate.where(a1, a2, a3, a4);

			// Effective Date End
			Subquery<Date> effectiveDate2 = query.subquery(Date.class);
			Root<ProductSectionMaster> ocpm2 = effectiveDate2.from(ProductSectionMaster.class);
			effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd").as(Date.class)));
			jakarta.persistence.criteria.Predicate a5 = cb.equal(c.get("sectionId"), ocpm2.get("sectionId"));
			Predicate a7 = cb.equal(c.get("companyId"), ocpm2.get("companyId"));
			Predicate a8 = cb.equal(c.get("productId"), ocpm2.get("productId"));

			jakarta.persistence.criteria.Predicate a6 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
			effectiveDate2.where(a5, a6, a7, a8);

			// Where
			jakarta.persistence.criteria.Predicate n1 = cb.equal(c.get("status"), "Y");
			jakarta.persistence.criteria.Predicate n2 = cb.equal(c.get("effectiveDateStart"), effectiveDate);
			jakarta.persistence.criteria.Predicate n3 = cb.equal(c.get("effectiveDateEnd"), effectiveDate2);
			jakarta.persistence.criteria.Predicate n4 = cb.equal(c.get("companyId"), companyId);
			jakarta.persistence.criteria.Predicate n5 = cb.equal(c.get("productId"), productId);
		//	Predicate n6 = cb.equal(c.get("sectionId"), sectionId);
		//	query.where(n1, n2, n3, n4, n5, n6).orderBy(orderList);
			query.where(n1, n2, n3, n4, n5).orderBy(orderList);

			// Get Result
			TypedQuery<ProductSectionMaster> result = em.createQuery(query);
			sectionList = result.getResultList();
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			return null;
		}
		return sectionList;
	}
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	 public  String genOneTimeTableRefNo() {
	       try {
	    	   SeqOnetimetable entity;
	            entity = oneNoRepo.save(new SeqOnetimetable());          
	            return String.format("%05d",entity.getReferenceNo()) ;
	        } catch (Exception e) {
				e.printStackTrace();
				log.info( "Exception is ---> " + e.getMessage());
	            return null;
	        }
	       
	 }
	public MsDetailsRes msDetails (Long msrefno) {
	MsDetailsRes res = new MsDetailsRes();
	DozerBeanMapper mapper = new DozerBeanMapper();
	try {
	MsCommonDetails data = msCommonRepo.findByMsRefno(msrefno);
	MsCustomerDetails cusdetails =  msCustomerRepo.findByCdRefno(Long.valueOf(data.getCdRefno()));
	MsVehicleDetails  msvehdetails = msVehicleRepo.findByVdRefno(Long.valueOf(data.getVdRefno()));
	res.setMsCommonDetails(mapper.map(data,MsCommonDetailsRes.class));
	res.setMsCustomerDetails(mapper.map(cusdetails,MsCustomerDetailsRes.class));
	res.setMsVehicleDetails(mapper.map(msvehdetails, MsVehicleDetailsRes.class));	
	
	}
	catch (Exception e) {
	 	e.printStackTrace();
		log.error("Exception is ---> " + e.getMessage());
		return null ;
 }

 return res;
}
	
	
	public synchronized CompanyProductMaster getCompanyProductMasterDropdown(String companyId, String productId) {
		CompanyProductMaster product = new CompanyProductMaster();
		try {
			Date today = new Date();
			Calendar cal = new GregorianCalendar();
			cal.setTime(today);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			;
			cal.set(Calendar.MINUTE, 1);
			today = cal.getTime();
			cal.set(Calendar.HOUR_OF_DAY, 1);
			cal.set(Calendar.MINUTE, 1);
			Date todayEnd = cal.getTime();

			// Criteria
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<CompanyProductMaster> query = cb.createQuery(CompanyProductMaster.class);
			List<CompanyProductMaster> list = new ArrayList<CompanyProductMaster>();
			// Find All
			Root<CompanyProductMaster> c = query.from(CompanyProductMaster.class);
			// Select
			query.select(c);
			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(c.get("productName")));

			// Effective Date Start Max Filter
			Subquery<Date> effectiveDate = query.subquery(Date.class);
			Root<CompanyProductMaster> ocpm1 = effectiveDate.from(CompanyProductMaster.class);
			effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart").as(Date.class)));
			Predicate a1 = cb.equal(c.get("productId"), ocpm1.get("productId"));
			Predicate a2 = cb.equal(c.get("companyId"), ocpm1.get("companyId"));
			Predicate a3 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
			effectiveDate.where(a1, a2, a3);
			// Effective Date End Max Filter
			Subquery<Date> effectiveDate2 = query.subquery(Date.class);
			Root<CompanyProductMaster> ocpm2 = effectiveDate2.from(CompanyProductMaster.class);
			effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd").as(Date.class)));
			Predicate a4 = cb.equal(c.get("productId"), ocpm2.get("productId"));
			Predicate a5 = cb.equal(c.get("companyId"), ocpm2.get("companyId"));
			Predicate a6 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
			effectiveDate2.where(a4, a5, a6);

			// Where
			Predicate n1 = cb.equal(c.get("status"), "Y");
			Predicate n2 = cb.equal(c.get("effectiveDateStart"), effectiveDate);
			Predicate n3 = cb.equal(c.get("effectiveDateEnd"), effectiveDate2);
			Predicate n4 = cb.equal(c.get("companyId"), companyId);
			Predicate n5 = cb.equal(c.get("productId"), productId);
			query.where(n1, n2, n3, n4, n5).orderBy(orderList);
			// Get Result
			TypedQuery<CompanyProductMaster> result = em.createQuery(query);
			list = result.getResultList();
			product = list.size() > 0 ? list.get(0) :null;
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is --->" + e.getMessage());
			return null;
		}
		return product;
	}

	@Autowired
	protected CriteriaService crservice;
	
	public CommonRes saveLifeTables(ShortQuote request) {
		// TODO Auto-generated method stub
		try {
			
			Date today = new Date();			
			Date dateOfBirth = request.getDateOfBirth();
			int age = today.getYear() - dateOfBirth.getYear();

		
			String search="policyHolderTypeid:"+ (StringUtils.isBlank(request.getPolicyHolderTypeid())?"99999":request.getPolicyHolderTypeid())
						 +";policyHolderType:"+ (StringUtils.isBlank(request.getPolicyHolderType())?"99999":request.getPolicyHolderType())
						 +";age:"+String.valueOf(age)
						 +";gender:"+request.getGender()
						 +";occupation:"+(StringUtils.isBlank(request.getOccupation())?"99999":request.getOccupation())
						 +";regionCode:"+(StringUtils.isBlank(request.getRegionCode())?"99999":request.getRegionCode())
						 +";taxExemptedId:"+ (StringUtils.isBlank(request.getTaxExemptedId())?"N":request.getTaxExemptedId())
						 +";status:"+"Y"
						 +";idNumber:"+(StringUtils.isBlank(request.getIdNumber())?"99999":request.getIdNumber())
						 +";";
						 
			
			SpecCriteria criteria = crservice.createCriteria(MsCustomerDetails.class, search, "cdRefno"); 
			List<Long> count = crservice.getCount(criteria,0,0);
			String cdRefNo;
			if(!count.isEmpty() && count.get(0)>0L) {
				List<Tuple> result = crservice.getResult(criteria, 0, 1);
				cdRefNo=result.get(0).get("cdRefno").toString();
			}else {
				cdRefNo=genOneTimeTableRefNo(); 
				MsCustomerDetails customer=MsCustomerDetails.builder()
						.age(age)
						.businessType(StringUtils.isBlank(request.getBusinessType())?null:request.getBusinessType())
						.businessTypeDesc(StringUtils.isBlank(request.getBusinessTypeDesc())?null:request.getBusinessTypeDesc())
						.cdRefno(Long.parseLong(cdRefNo))
						.cityCode(StringUtils.isBlank(request.getCityCode())?null:request.getCityCode())
						.cityName(StringUtils.isBlank(request.getCityName())?null:request.getCityName())
						.createdBy(StringUtils.isBlank(request.getCreatedBy())?null:request.getCreatedBy())
						.dobOrRegDate(dateOfBirth)
						.entryDate(new Date())
						.gender(request.getGender())
						.genderDesc(StringUtils.isBlank(request.getGenderDesc())?null:request.getGenderDesc())
						.idNumber(StringUtils.isBlank(request.getIdNumber())?"99999":request.getIdNumber())
						.idType(StringUtils.isBlank(request.getIdType())?"99999":request.getIdType())
						.idTypeDesc(StringUtils.isBlank(request.getIdTypeDesc())?"99999":request.getIdTypeDesc())
						.isTaxExempted(StringUtils.isBlank(request.getIsTaxExempted())?"N":request.getIsTaxExempted())
						.nationality(StringUtils.isBlank(request.getNationality())?null:request.getNationality())
						.occupation(StringUtils.isBlank(request.getOccupation())?"99999":request.getOccupation())
						.occupationDesc(StringUtils.isBlank(request.getOccupationDesc())?null:request.getOccupationDesc())
						.placeOfBirth(StringUtils.isBlank(request.getPlaceOfBirth())?null:request.getPlaceOfBirth())
						.policyHolderType(StringUtils.isBlank(request.getPolicyHolderType())?"99999":request.getPolicyHolderType())
						.policyHolderTypeid(StringUtils.isBlank(request.getPolicyHolderTypeid())?"99999":request.getPolicyHolderTypeid())
						.regionCode(StringUtils.isBlank(request.getRegionCode())?"99999":request.getRegionCode())
						.stateCode(StringUtils.isBlank(request.getStateCode())?null:request.getStateCode())
						.stateName(StringUtils.isBlank(request.getStateName())?null:request.getStateName())
						.status("Y")	
						.mobileCode(StringUtils.isBlank(request.getMobileCode())?"":request.getMobileCode())
						.mobileNo(StringUtils.isBlank(request.getMobileNo())?"":request.getMobileNo())
						.email(StringUtils.isBlank(request.getEmailId())?"":request.getEmailId())
						.createdBy("1".equals(request.getApplicationID())?request.getLoginID():request.getApplicationID())
						.taxExemptedId(StringUtils.isBlank(request.getTaxExemptedId())?"N":request.getTaxExemptedId())
				.build();
				msCustomerRepo.save(customer);
			}

			search="companyId:"+request.getInsuranceID()
					+";branchCode:"+request.getBranchCode()
					+";currency:"+(StringUtils.isBlank(request.getCurrency())?"1":request.getCurrency())
					+";exchangeRate:"+(request.getExchangeRate()==null?1D:request.getExchangeRate())
					+";groupCount:"+"1"
					+";havepromocode:"+(StringUtils.isBlank(request.getHavepromocode())?"N":request.getHavepromocode())
					+";periodOfInsurance:"+request.getPolicyTerm()
					+";productId:"+request.getProductID()
					+";promocode:"+(StringUtils.isBlank(request.getPromocode())?"":request.getPromocode())
					+";riskId:"+"1"
					+";sectionId:"+"0"
					+";status:"+"Y"
					+";sumInsured:"+request.getSumInsured()
					+";requestReferenceNo:"+request.getRequestReferenceNo()
					+";uwLoading:"+(request.getUwLoading()==null?0D:request.getUwLoading())
					+";policyTerm:"+(StringUtils.isBlank(request.getPolicyTerm())?0:Integer.parseInt(request.getPolicyTerm()))
					+";premiumPayingTerm:"+(request.getPayingTerm()==null?0:request.getPayingTerm().intValue())
					+";paymentMode:"+(StringUtils.isBlank(request.getPaymentMode())?"0":request.getPaymentMode())
					;
					
			 criteria = crservice.createCriteria(MsLifeDetails.class, search, "vdRefno"); 
			 count = crservice.getCount(criteria,0,0);
			 String vdRefNo;
			if(!count.isEmpty() && count.get(0)>0L) {
				List<Tuple> result = crservice.getResult(criteria, 0, 1);
				vdRefNo=result.get(0).get("vdRefno").toString();
			}else {
				vdRefNo=genOneTimeTableRefNo();
				MsLifeDetails life=MsLifeDetails.builder()
						.branchCode(request.getBranchCode())
						.companyId(request.getInsuranceID())						
						.currency(StringUtils.isBlank(request.getCurrency())?"1":request.getCurrency())
						.endtCategoryId("0")
						.endtTypeId(0)
						.entryDate(today)
						.exchangeRate(request.getExchangeRate()==null?BigDecimal.ONE:new BigDecimal(request.getExchangeRate()))
						.groupCount(1)
						.havepromocode(StringUtils.isBlank(request.getHavepromocode())?"N":request.getHavepromocode())
						.periodOfInsurance(request.getPolicyTerm())
						.productId(request.getProductID())
						.promocode(StringUtils.isBlank(request.getPromocode())?"":request.getPromocode())
						.requestReferenceNo(request.getRequestReferenceNo())
						.riskId(1)
						.sectionId(0)
						.status("Y")
						.sumInsured(request.getSumInsured()==null?null:request.getSumInsured())
						.sumInsuredLc((request.getSumInsured()==null?BigDecimal.ZERO:request.getSumInsured()).multiply((request.getExchangeRate()==null?BigDecimal.ONE:new BigDecimal(request.getExchangeRate())), MathContext.DECIMAL32))
						.uwLoading(request.getUwLoading()==null?BigDecimal.ZERO:new BigDecimal(request.getUwLoading()))
						.vdRefno(Long.parseLong(vdRefNo))	
						.createdBy("1".equals(request.getApplicationID())?request.getLoginID():request.getApplicationID())
						.policyTerm(StringUtils.isBlank(request.getPolicyTerm())?0:Integer.parseInt(request.getPolicyTerm()))
						.premiumPayingTerm(request.getPayingTerm()==null?0:request.getPayingTerm().intValue())
						.paymentMode(StringUtils.isBlank(request.getPaymentMode())?"0":request.getPaymentMode())
						.build();
				msLifeRepo.save(life);
			}
			
			search="insuranceId:"+request.getInsuranceID()
					+";branchCode:"+request.getBranchCode()
					+";agencyCode:"+request.getAgencyCode()
					+";sectionId:"+"0"
					+";productId:"+request.getProductID()
					+";cdRefno:"+cdRefNo					
					+";vdRefno:"+vdRefNo
					+";status:"+"Y";
					
					
					
	 criteria = crservice.createCriteria(MsCommonDetails.class, search, "msRefno"); 
	 count = crservice.getCount(criteria,0,0);
	 String msRefNo;
	if(!count.isEmpty() && count.get(0)>0L) {
		List<Tuple> result = crservice.getResult(criteria, 0, 1);
		msRefNo=result.get(0).get("msRefno").toString();
	}else {
		msRefNo=genOneTimeTableRefNo();
		MsCommonDetails ms=MsCommonDetails.builder()
				.agencyCode(request.getAgencyCode())
				.branchCode(request.getBranchCode())
				.cdRefno(Long.parseLong(cdRefNo))
				.entryDate(new Date())
				.insuranceId(request.getInsuranceID())
				.msRefno(Long.parseLong(msRefNo))
				.productId(request.getProductID())
				.requestreferenceno(request.getRequestReferenceNo())
				.sectionId(0)
				.status("Y") 
				.vdRefno(Long.parseLong(vdRefNo))				
				.build();
		msCommonRepo.save(ms);
	}
					
					
					
			CommonRes c=new CommonRes();
			Map<String,String> res=new HashMap<String, String>();
			res.put("cdRefNo", cdRefNo);
			res.put("vdRefNo", vdRefNo);
			res.put("msRefNo", msRefNo);
			c.setCommonResponse(res);
			return c;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
}