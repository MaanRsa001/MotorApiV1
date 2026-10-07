package com.maan.eway.common.service.impl;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.eway.bean.CommonDataDetails;
import com.maan.eway.bean.CurrencyMaster;
import com.maan.eway.bean.DocumentTransactionDetails;
import com.maan.eway.bean.EserviceBuildingDetails;
import com.maan.eway.bean.EserviceCommonDetails;
import com.maan.eway.bean.EserviceSectionDetails;
import com.maan.eway.bean.HomePositionMaster;
import com.maan.eway.bean.OccupationMaster;
import com.maan.eway.bean.PolicyCoverData;
import com.maan.eway.bean.PolicyCoverDataIndividuals;
import com.maan.eway.bean.ProductEmployeeDetails;
import com.maan.eway.bean.ProductEmployeeDetailsArch;
import com.maan.eway.bean.SectionDataDetails;
import com.maan.eway.common.req.OldDocumentsCopyReq;
import com.maan.eway.common.req.PersonalAccidentDetailsListReq;
import com.maan.eway.common.req.PersonalAccidentGetAllReq;
import com.maan.eway.common.req.PersonalAccidentGetReq;
import com.maan.eway.common.req.PersonalAccidentSaveReq;
import com.maan.eway.common.req.ProductEmployeeSaveReq;
import com.maan.eway.common.res.PersonalAccidentGetAllRes;
import com.maan.eway.common.res.PersonalAccidentGetRes;
import com.maan.eway.common.res.PersonalDetailsGetallRes;
import com.maan.eway.common.res.PremiumGroupDevideRes;
import com.maan.eway.common.service.DocumentCopyService;
import com.maan.eway.common.service.PersonalAccidentService;
import com.maan.eway.error.Error;
import com.maan.eway.repository.CommonDataDetailsRepository;
import com.maan.eway.repository.DocumentTransactionDetailsRepository;
import com.maan.eway.repository.EServiceBuildingDetailsRepository;
import com.maan.eway.repository.EServiceSectionDetailsRepository;
import com.maan.eway.repository.EserviceCommonDetailsRepository;
import com.maan.eway.repository.HomePositionMasterRepository;
import com.maan.eway.repository.ListItemValueRepository;
import com.maan.eway.repository.OccupationMasterRepository;
import com.maan.eway.repository.PolicyCoverDataIndividualsRepository;
import com.maan.eway.repository.PolicyCoverDataRepository;
import com.maan.eway.repository.ProductEmployeeDetailsArchRepository;
import com.maan.eway.repository.ProductEmployeesDetailsRepository;
import com.maan.eway.repository.SectionDataDetailsRepository;
import com.maan.eway.req.EmployeeIdDuplicaionReq;
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
public class PersonalAccidentServiceImpl implements PersonalAccidentService {

	@Autowired
	private ProductEmployeesDetailsRepository repo;

	@Autowired
	private ListItemValueRepository listrepo;

	@Autowired
	private SectionDataDetailsRepository sectionrepo;

	@Autowired
	private OccupationMasterRepository occupationrepo;

	@Autowired
	private EServiceBuildingDetailsRepository eserBuildingRepo ;

	@Autowired
	private EserviceCommonDetailsRepository commonRepo;
	
	@Autowired
	private GenerateSeqNoServiceImpl genSeqNoService;
	
	@Autowired
	private ProductEmployeeDetailsArchRepository empArchRepo ;
	
	@Autowired
	private PolicyCoverDataRepository coverRepo ;
	
	@Autowired
	private CommonDataDetailsRepository commonDataRepo;
	

	@Autowired
	private DocumentCopyService  docService ;
	
	@Autowired
	private SectionDataDetailsRepository secRepo;
	
	
	@Autowired
	private DocumentTransactionDetailsRepository docTransRepo;
	
	@Autowired
	private PolicyCoverDataIndividualsRepository indiCoverRepo ;
	
	@Autowired
	private HomePositionMasterRepository homeRepo ;
	
	@Autowired
	private EServiceSectionDetailsRepository secrepo;
	
	@PersistenceContext
	private EntityManager em;
	
	
	
	private Logger log = LogManager.getLogger(PersonalAccidentServiceImpl.class);

	@Override
	public List<Error> validatepersonalaccident(PersonalAccidentSaveReq req) {
		List<Error> error = new ArrayList<Error>();
		List<PersonalAccidentDetailsListReq> data = req.getPersonaldetails();
		try {
			List<PersonalAccidentDetailsListReq> reqList=req.getPersonaldetails();
			Long row = 0L ;
			BigDecimal sumInsured = BigDecimal.ZERO ;
			String quoteNo = "" ;
			String requestno=req.getRequestReferenceNo();
			String companyId="";
			List<EserviceBuildingDetails> buidingData = new ArrayList<>();
		
			EserviceCommonDetails accidentData = new EserviceCommonDetails(); 
			EserviceCommonDetails liabilityData = new EserviceCommonDetails();
			List<CommonDataDetails> commonDatas = new ArrayList<CommonDataDetails>();
			
			if (StringUtils.isBlank(req.getRequestReferenceNo())) {
				error.add(new Error("03", "RequestReferenceNo", "Please Select RequestReferenceNo"));
			} 
			if(!StringUtils.isBlank(req.getRequestReferenceNo()))
			{
				List<EserviceSectionDetails> commonRecords = secrepo.findByRequestReferenceNoAndSectionId(req.getRequestReferenceNo(),req.getSectionId());
				companyId=commonRecords.get(0).getCompanyId();
			}
			else if (StringUtils.isBlank(req.getSectionId())) {
				error.add(new Error("01", "SectionId", "Please Select Section Id"));
			} else {
				
				
				 //quoteNo = req.getQuoteNo() ;
				//buidingData =  eserBuildingRepo.findByRequestReferenceNoAndSectionIdAndRiskId(requestno,req.getSectionId(),Integer.valueOf(request.getRiskId()));
				
				//List<EserviceCommonDetails> accidentDatas = commonRepo.findByRequestReferenceNoAndSectionId(requestno,req.getSectionId() );
				//accidentData = accidentDatas.size() > 0 ? accidentDatas.get(0) : new EserviceCommonDetails() ;
				//companyId = accidentDatas.size() > 0 ? accidentDatas.get(0).getCompanyId() : "";
			//	List<EserviceCommonDetails> liabilityDatas = commonRepo.findByRequestReferenceNoAndSectionId(requestno ,"36" );
				//liabilityData = liabilityDatas.size() > 0 ? liabilityDatas.get(0) : new EserviceCommonDetails() ;
				//companyId = liabilityDatas.size() > 0 ? liabilityDatas.get(0).getCompanyId() : "";
				}
			
			if (reqList != null && reqList.size() > 0) {
				commonDatas = commonDataRepo.findByQuoteNoAndSectionId(req.getQuoteNo(), req.getSectionId());
				commonDatas = commonDatas.stream().filter(o -> !o.getStatus().equalsIgnoreCase("D"))
						.collect(Collectors.toList());
				quoteNo = req.getQuoteNo();
			} else {
//				error.add("1196");
				error.add(new Error("01", "QuoteNo", "Please Enter Atleat one Details "));
			}
			
			BigDecimal zero = BigDecimal.ZERO;
		/*	if(StringUtils.isNotBlank(req.getType())&&req.getType().equalsIgnoreCase("PA")) {
				for(PersonalAccidentDetailsListReq req1:  req.getPersonaldetails()) {				
				if (StringUtils.isBlank(req1.getPaDeathSuminsured())) {
					error.add(new Error("03", "PaDeathSuminsured", "Please Enter PaDeathSuminsured"));
				} else if (!req1.getPaDeathSuminsured().matches("[0-9.]+")) {
					error.add(new Error("03", "PaDeathSuminsured", "Please Enter Valid Number In PaDeathSuminsured"));
				}
				else if (req1.getPaDeathSuminsured().equalsIgnoreCase("0")) {
					error.add(new Error("03", "PaDeathSuminsured", "Please Enter PaDeathSuminsured above 0"));
				}
				if (StringUtils.isBlank(req1.getPaMedicalSuminsured())) {
					error.add(new Error("04", "PaMedicalSuminsured", "Please Enter PaMedicalSuminsured"));
				} else if (!req1.getPaMedicalSuminsured().matches("[0-9.]+")) {
					error.add(new Error("04", "PaMedicalSuminsured", "Please Enter Valid Number In PaMedicalSuminsured"));
				}
				else if (req1.getPaMedicalSuminsured().equalsIgnoreCase("0") ) {
					error.add(new Error("04", "PaMedicalSuminsured", "Please Enter PaMedicalSuminsured above 0"));
				}
	
				if (StringUtils.isBlank(req1.getPaPermanentdisablementSuminsured())) {
					error.add(new Error("05", "PaPermanentdisablementSuminsured", "Please Enter PaPermanentdisablementSuminsured"));
				} else if (!req1.getPaMedicalSuminsured().matches("[0-9.]+")) {
					error.add(new Error("05", "PaPermanentdisablementSuminsured", "Please Enter Valid Number In PaPermanentdisablementSuminsured"));
				}
				else if (req1.getPaPermanentdisablementSuminsured().equalsIgnoreCase("0") ) {
					error.add(new Error("05", "PaPermanentdisablementSuminsured", "Please Enter PaPermanentdisablementSuminsured above 0"));
				}
				
				if (StringUtils.isBlank(req1.getPaTotaldisabilitySuminsured())) {
					error.add(new Error("06", "PaTotaldisabilitySuminsured", "Please Enter PaTotaldisabilitySuminsured"));
				} else if (!req1.getPaMedicalSuminsured().matches("[0-9.]+")) {
					error.add(new Error("06", "PaTotaldisabilitySuminsured", "Please Enter Valid Number In PaTotaldisabilitySuminsured"));
				}
				else if (req1.getPaTotaldisabilitySuminsured().equalsIgnoreCase("0") ) {
					error.add(new Error("06", "PaTotaldisabilitySuminsured", "Please Enter PaTotaldisabilitySuminsured above 0"));
				}

				}
				}
			*/
			List<EmployeeIdDuplicaionReq> nationalityIds = new ArrayList<EmployeeIdDuplicaionReq>();
			
			
			
			
				
			for(PersonalAccidentDetailsListReq req2:  req.getPersonaldetails()) {				
			
				row = row + 1 ;
				if (StringUtils.isBlank(req2.getPersonName())) {
					error.add(new Error("08", "PersonName", "Please Enter PersonName In Row No:" + row ));
				}
				
				else if (req2.getPersonName().length()>20) {
					error.add(new Error("08", "PersonName", "Please Enter PersonName within 20 Characters In Row No:" + row ));
				}
				else if ((StringUtils.isNotBlank(req2.getPersonName()))&&!req2.getPersonName().matches("[A-Z a-z]+")) {
					error.add(new Error("08", "PersonName", "Please Enter Valid Name In Person Name In Row No:" + row ));
				}
				if (!("100020".equalsIgnoreCase(companyId))) {
					if (StringUtils.isNotBlank(req2.getNationalityId())) {
					//List<String> filterIds = nationalityIds.stream().filter( o -> (o.equalsIgnoreCase(req.getNationalityId())) && () ).collect(Collectors.toList());
					List<EmployeeIdDuplicaionReq> filterIds = nationalityIds.stream().filter( o -> (o.getNationalityId().equalsIgnoreCase(req2.getNationalityId())) && (o.getLocationId().equalsIgnoreCase(req2.getRiskId())) ).collect(Collectors.toList());
					
					/*
					 * if(filterIds.size() >0 ) { error.add(new Error("01", "NationalityId",
					 * "Duplicate NationalityId in Row : " + row)); }
					 *//* else {*/
						EmployeeIdDuplicaionReq nation = new EmployeeIdDuplicaionReq();
						nation.setLocationId(req2.getRiskId());
						nation.setNationalityId(req2.getNationalityId());
						nationalityIds.add(nation);
					//}
					
				}
				if(StringUtils.isBlank(req2.getNationalityId()))
				{
					error.add(new Error("20", "NationalityId", "Please Enter NationalityId In Row No:" + row));
						
				}
				if (StringUtils.isBlank(req2.getOccupationId()) && req.getSectionId().equals("35")) {
					error.add(new Error("14", "OccupationId", "Please Enter OccupationId In Row No:" + row));
				}
			}
//				if (StringUtils.isNotBlank(req2.getHeight())) {
//					if ((StringUtils.isNotBlank(req2.getHeight()))&&!req2.getHeight().matches("[0-9.]+")) {
//						error.add(new Error("09", "Height", "Please Enter Valid Number In Height In Row No:" + row ));
//					}
//					else if ((StringUtils.isNotBlank(req2.getHeight()))&&req2.getHeight().equalsIgnoreCase("0") ) {
//						error.add(new Error("09", "Height", "Please Enter Height above 0 In Row No:" + row ));
//					}
//					else if ((StringUtils.isNotBlank(req2.getHeight()))&&(Integer.valueOf(req2.getHeight()))<120) {
//						error.add(new Error("09", "Height", "Please Enter Height above 120 CM In Row No:" + row ));
//					}
//				}
//				
//				if (StringUtils.isNotBlank(req2.getWeight())) {
//					if ((StringUtils.isNotBlank(req2.getWeight()))&&!req2.getWeight().matches("[0-9.]+")) {
//						error.add(new Error("10", "Weight", "Please Enter Valid Number In Weight In Row No:" + row ));
//					}
//					else if ((StringUtils.isNotBlank(req2.getWeight()))&&req2.getWeight().equalsIgnoreCase("0") ) {
//						error.add(new Error("10", "Weight", "Please Enter Weight above 0 In Row No:" + row ));
//					}
//					else if ((StringUtils.isNotBlank(req2.getWeight()))&&(Integer.valueOf(req2.getWeight()))<40) {
//						error.add(new Error("10", "Weight", "Please Enter Weight above 40 KG In Row No:" + row ));
//					}
//				}
				
				if (StringUtils.isBlank(req2.getSalary())) {
					error.add(new Error("12", "Salary", "Please Enter Salary"));
				}
				else if ((StringUtils.isNotBlank(req2.getSalary()))&&!req2.getSalary().matches("[0-9.]+")) {
					error.add(new Error("12", "Salary", "Please Enter Valid Number In Salary In Row No:" + row ));
				}
				else if ((StringUtils.isNotBlank(req2.getSalary())) && Double.valueOf(req2.getSalary()) <=0) {
					error.add(new Error("12", "Salary", "Please Enter Salary above zero In Row No:" + row ));
				}
				else {
					sumInsured = sumInsured.add(new BigDecimal(req2.getSalary()));
				}
			
				if (StringUtils.isBlank(req2.getRiskId())) {
					error.add(new Error("15", "RiskId", "Please Enter RiskId In Row No:" + row ));
				}
				Date today = new Date();

				if (req2.getDob() == null) {
					error.add(new Error("11", "Dob", "Please Enter Dob In Row No:" + row ));

				} else if (req2.getDob().after(today)) {
					error.add(new Error("11", "Dob", "Please Enter Dob as Past Date In Row No:" + row ));
					
				}

				if (req2.getDob() != null) {
					LocalDate localDate1 = req2.getDob().toInstant().atZone(ZoneId.systemDefault())
							.toLocalDate();
					LocalDate localDate2 = today.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	
					Integer years = Period.between(localDate1, localDate2).getYears();
					if (years > 100) {
						error.add(new Error("11", "Dob", "Dob Not Accepted More than 100 Years In Row No:" + row ));
	
					}
				}
				
				
			/*	if (StringUtils.isBlank(req2.getSumInsured())) {
					error.add(new Error("13", "SumInsured", "Please Enter SumInsured"));
				}
				else if (!req2.getSumInsured().matches("[0-9.]+")) {
					error.add(new Error("13", "SumInsured", "Please Enter Valid Number In SumInsured"));
				}
				else if (req2.getSumInsured().equalsIgnoreCase("0") ) {
					error.add(new Error("13", "SumInsured", "Please Enter SumInsured above 0"));
				}
			*/	
				
			}
			if((StringUtils.isNotBlank(req.getType()))&&req.getType().equalsIgnoreCase("WC")) {
				if(StringUtils.isNotBlank(requestno)  ) {
//					if(buidingData.getWorkmenCompSuminsured()!=null && sumInsured.compareTo(buidingData.getWorkmenCompSuminsured()) > 0 ) {
//						error.add(new Error("03", "SumINsured", " Total SumInsured Greater Workmen Compensation SumInsured In Row No:" + row  ));
//					}
				}
				}
				

			if((StringUtils.isNotBlank(req.getType()))&&req.getType().equalsIgnoreCase("PI")) {
				if(StringUtils.isNotBlank(requestno)  ) {
					if(liabilityData.getSumInsured()!=null && sumInsured.compareTo(liabilityData.getSumInsured()) > 0 ) {
						error.add(new Error("03", "SumINsured", " Total SumInsured Greater Personal Intermnity SumInsured In Row No:" + row  ));
					}
				}
				}


				/*
				 * if((StringUtils.isNotBlank(req.getType()))&&req.getType().equalsIgnoreCase(
				 * "PA")) { if(StringUtils.isNotBlank(requestno) ) {
				 * if(accidentData.getSumInsured()!=null &&
				 * sumInsured.compareTo(accidentData.getSumInsured()) > 0 ) { error.add(new
				 * Error("03", "SumINsured",
				 * " Total SumInsured Greater Personal Accident SumInsured In Row No:" + row ));
				 * } } }
				 */
			int checkCount = 0;
			int empCount = 0;
			double totalSi = 0.0;
			double empSi = 0.0;
			boolean temp1 = true;
			int indivcount = 0;
			if (error.size() < 1) {

				empCount = commonDatas.stream().mapToInt(o -> o.getCount().intValue()).sum();

				// count
				if(StringUtils.isNotBlank(req.getSaveOrSubmit()) && "Save".equalsIgnoreCase(req.getSaveOrSubmit())) {
					if (reqList.size() > empCount) {
						error.add(new Error("333", "Employees Count", "Employee's Details Count Should be "+empCount));
					}
				}else {
				if (reqList.size() > empCount || reqList.size() < empCount) {
//					error.add("1214" + "," + empCount);
					error.add(new Error("333", "Employees Count", "Employee's Details Count Should be "+empCount));
				}
				}
			}
			if (commonDatas != null && commonDatas.size()>0) {
			Set<Integer> findlocationid = commonDatas.stream().map(CommonDataDetails::getLocationId).distinct()
					.collect(Collectors.toSet());
			for (Integer l : findlocationid) {
				if (error.size() < 1) {
					// Total si & Individual occupation count
					commonDatas = commonDatas.stream().filter(o -> o.getLocationId().equals(l))
							.collect(Collectors.toList());
					for (CommonDataDetails cdata : commonDatas) {
						indivcount = 0;
						checkCount = 0;
						empSi = 0.0;
						indivcount = indivcount + cdata.getCount().intValue();

						totalSi = cdata.getSumInsured() == null ? 0.0 : cdata.getSumInsured().doubleValue();

						checkCount = (int) reqList.stream().filter(o -> o.getOccupationId() != null
								&& o.getOccupationId().equalsIgnoreCase(cdata.getOccupationType())&& o.getLocationId().equalsIgnoreCase(cdata.getLocationId().toString())).count();
						empSi = reqList.stream()
								.filter(o -> o.getOccupationId() != null
										&& o.getOccupationId().equalsIgnoreCase(cdata.getOccupationType())
										&& o.getLocationId().equalsIgnoreCase(cdata.getLocationId().toString()))
								.mapToDouble(o -> Double.valueOf(o.getSalary())).sum();

						if (indivcount != checkCount) {
//							error.add("1215" + "," + indivcount);
					error.add(new Error("111", "Occupation Count", "Employee Details count should be "+indivcount+" for Occupation "+"'"+cdata.getOccupationDesc()+"'"));
							temp1 = false;
						}
						if (error.size() < 1) {
//							if (totalSi != empSi) {
							if(StringUtils.isNotBlank(req.getSaveOrSubmit()) && "Save".equalsIgnoreCase(req.getSaveOrSubmit())) {
							if (totalSi < empSi) {
//								error.add("1216" + "," + cdata.getOccupationDesc());
								error.add(new Error("222", "Sum Insured", "Total SumInsured not equal to the Actual SumInsured for occupation "+"'"+cdata.getOccupationDesc()+"'" ));
								temp1 = false;
							}
							}else {
								if (totalSi < empSi || totalSi > empSi ) {
									error.add(new Error("222", "Sum Insured", "Total SumInsured not equal to the Actual SumInsured for occupation "+"'"+cdata.getOccupationDesc()+"'" ));
									temp1 = false;
								}
							}
						}
						if (!temp1)
							break;
					}

				}
			}
		}

			
		}
		 catch (Exception e) {

				log.error(e);
				e.printStackTrace();
				error.add(new Error("03", "Common Error", e.getMessage()));
			}
			return error;
		}

	public SuccessRes savepersonalaccidentInfo(PersonalAccidentSaveReq req) {
		SuccessRes res = new SuccessRes();
		String requestNo = req.getRequestReferenceNo();
		try {
		    
		    Long employeeId = (long) 1;
		    Date entryDate = new Date();
		    String quoteNo = req.getQuoteNo();
			Integer sectionId = Integer.valueOf(req.getSectionId());
		   List<PersonalAccidentDetailsListReq> req1 = req.getPersonaldetails();
		   List<EserviceBuildingDetails> eserBuildingData = eserBuildingRepo.findByRequestReferenceNoAndSectionIdAndRiskId(requestNo, "1",1);
		    List<ProductEmployeeDetails> save = new ArrayList<>();

		    
		    List<CommonDataDetails> groupList = commonDataRepo.findByQuoteNoAndSectionIdAndStatusNot(quoteNo,
					sectionId.toString(), "D");
		    // Delete existing records (uncomment if intended)
		    List<ProductEmployeeDetails> existingRecords = repo.findByRequestReferenceNoAndSectionId(requestNo, req.getSectionId());
		    if (existingRecords.size() > 0) {
		        repo.deleteAll(existingRecords);
		    }
		    ProductEmployeeDetails newRecords = new ProductEmployeeDetails();
		    // Save new records/update records
		    
		 // Grouping By Location Id
			Map<String, List<PersonalAccidentDetailsListReq>> groupByLocationId = req1.stream()
					.filter(o -> o.getLocationId() != null)
					.collect(Collectors.groupingBy(PersonalAccidentDetailsListReq::getLocationId));
			for (String location : groupByLocationId.keySet()) {
				List<PersonalAccidentDetailsListReq> filterReq = groupByLocationId.get(location);
				Map<String, List<PersonalAccidentDetailsListReq>> groupByGroupId = filterReq.stream()
						.filter(o -> o.getRiskId() != null)
						.collect(Collectors.groupingBy(PersonalAccidentDetailsListReq::getRiskId));
				for (String group : groupByGroupId.keySet()) {
					List<PersonalAccidentDetailsListReq> filterReqPass = groupByGroupId.get(group);
					List<CommonDataDetails> filterGroupList = groupList.stream()
							.filter(o -> o.getRiskId().equals(Integer.valueOf(group))
									&& o.getLocationId().equals(Integer.valueOf(location)))
							.collect(Collectors.toList());
					CommonDataDetails groupData = new CommonDataDetails();
					if (filterGroupList.size() > 0) {
						groupData = filterGroupList.get(0);
					}
				    for (PersonalAccidentDetailsListReq request : filterReqPass) {
					       
				    	if(request.getOccupationId()!=null) {
				        List<OccupationMaster> occ = occupationrepo.findByOccupationId(Integer.valueOf(request.getOccupationId()));
				    	 newRecords.setOccupationId(request.getOccupationId());
				    	 newRecords.setOccupationDesc(occ.get(0).getOccupationName());
				    	}
				    	else {
				    		 newRecords.setOccupationId(null);	
				    		 newRecords.setOccupationDesc(null);
				    	}
				    	List<EserviceSectionDetails> commonRecords = secrepo.findByRequestReferenceNoAndSectionId(requestNo,req.getSectionId());
						   
				        newRecords.setProductId(StringUtils.isBlank(commonRecords.get(0).getProductId()) ? 0 : Integer.valueOf(commonRecords.get(0).getProductId()));
				        newRecords.setProductDesc(StringUtils.isBlank(commonRecords.get(0).getProductDesc()) ? null : commonRecords.get(0).getProductDesc());
				        newRecords.setRiskId(StringUtils.isBlank(request.getRiskId()) ? 0 : Integer.valueOf(request.getRiskId()));
				        newRecords.setCompanyId(commonRecords.get(0).getCompanyId());
				        newRecords.setEmployeeId(employeeId);
				        newRecords.setNationalityId(request.getNationalityId());
				        newRecords.setEmployeeName(request.getPersonName());
				       
				        newRecords.setLocationId(Integer.valueOf(request.getLocationId()));
				        newRecords.setLocationName(StringUtils.isBlank(request.getLocationName()) ? "" : request.getLocationName());
				        newRecords.setEntryDate(entryDate);
				        newRecords.setCreatedBy(req.getCreatedBy());
				        newRecords.setStatus("Y");
				        newRecords.setDateOfBirth(request.getDob());
				        newRecords.setSectionDesc(commonRecords.get(0).getSectionName());
				        newRecords.setSectionId(req.getSectionId());
				        newRecords.setRequestReferenceNo(requestNo);
				       newRecords.setSalary(new BigDecimal(request.getSalary()));

				        if (eserBuildingData != null && !eserBuildingData.isEmpty() && StringUtils.isNotBlank(eserBuildingData.get(0).getExchangeRate().toString())) {
				            BigDecimal exRate = eserBuildingData.get(0).getExchangeRate();
				            if (StringUtils.isNotBlank(request.getSalary())) {
				                newRecords.setSalaryLc(new BigDecimal(request.getSalary()).multiply(exRate));
				            } else {
				                newRecords.setSalaryLc(BigDecimal.ZERO);
				            }
				        }

				        repo.saveAndFlush(newRecords);
				        employeeId++; 
				    }
				}
			}
		 			

		    res.setSuccessId(requestNo);
			res.setResponse("Saved Successful");
		    
		  
		} catch (Exception ex) {
		    System.out.println("*************Exception in personal accident additional info api *********************");
		    ex.printStackTrace(); // Print stack trace for debugging
		    res.setSuccessId(requestNo);
			res.setResponse("Oops Something Went Wrong!.....");
		}
		return res;
	}
	
	@Override
	public SuccessRes savepersonalaccident(PersonalAccidentSaveReq req) {
		SuccessRes res = new SuccessRes();
		DozerBeanMapper dozermapper = new DozerBeanMapper();
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		
		try {
			Date entryDate = new Date();
			req.getSectionId();
			String sectionId = req.getSectionId() ;
			HomePositionMaster homeData = homeRepo.findByQuoteNo(req.getQuoteNo()); 
		
			
			List<ProductEmployeeDetails> find = repo.findByQuoteNoAndSectionId(req.getQuoteNo(),req.getSectionId());
			if(find.size()>0) {
				String archId = "AI-" +  genSeqNoService.generateArchId();
				List<ProductEmployeeDetailsArch> savearchList = new ArrayList<ProductEmployeeDetailsArch>();
				for(ProductEmployeeDetails data : find) {
					ProductEmployeeDetailsArch savearch = new ProductEmployeeDetailsArch();
					savearch.setArchId(archId); 
					dozermapper.map(data, savearch);
					savearchList.add(savearch);
				}
				empArchRepo.saveAllAndFlush(savearchList);
			
				repo.deleteAll(find);
			}
			
			
			
				
			
			List<SectionDataDetails> sections = sectionrepo.findByQuoteNoOrderByRiskIdAsc(req.getQuoteNo());
			List<SectionDataDetails> filtersection =  sections.stream().filter( o -> o.getSectionId().equals(req.getSectionId()) ).collect(Collectors.toList() );
		//	ListItemValue type = listrepo.findByItemTypeAndItemCode("PERSONAL",req.getType());
			res.setSuccessId(req.getRequestReferenceNo());
			res.setResponse("Saved Successful");

			
			Integer personId=1;
			List<CommonDataDetails> groupList = commonDataRepo.findByQuoteNoAndSectionIdAndStatusNot(req.getQuoteNo() ,sectionId , "D" );  
			List<PolicyCoverData>  coverList = coverRepo.findByQuoteNoAndSectionId(req.getQuoteNo() ,Integer.valueOf(sectionId) );
			List<PremiumGroupDevideRes> covers = new ArrayList<PremiumGroupDevideRes>();
			Type listType = new TypeToken<List<PremiumGroupDevideRes>>(){}.getType();
			covers = modelMapper.map(coverList,listType);
			 
			Map<Integer, List<PremiumGroupDevideRes>> coverGroupByGroupId = covers.stream().filter( o -> o.getVehicleId() !=null  ).collect( Collectors.groupingBy(PremiumGroupDevideRes :: getVehicleId )) ;		
			List<PersonalAccidentDetailsListReq> reqList =  req.getPersonaldetails()  ;
			List<PolicyCoverDataIndividuals> totalIndiCovers = new ArrayList<PolicyCoverDataIndividuals>(); 
			
			Map<String,	List<PersonalAccidentDetailsListReq>> groupByRiskId = reqList.stream().collect(Collectors.groupingBy(PersonalAccidentDetailsListReq :: getRiskId))  ;
		
			List<ProductEmployeeDetails> saveDataList = new ArrayList<ProductEmployeeDetails>();
			for(String riskId : groupByRiskId.keySet()) {
				
			List<PersonalAccidentDetailsListReq> filterData =  groupByRiskId.get(riskId) ;

				
			BigDecimal sno = BigDecimal.ZERO ;
			for(PersonalAccidentDetailsListReq reqdata :filterData) {
			List<OccupationMaster> occupation = occupationrepo.findByOccupationId(Integer.valueOf(reqdata.getOccupationId()));
			sno = sno.add(BigDecimal.ONE);

			List<PersonalAccidentDetailsListReq> filterReqPass = groupByRiskId.get(riskId);
			String riskId2 = filterReqPass.get(0).getOccupationId();
			List<PremiumGroupDevideRes> groupCovers = coverGroupByGroupId.get(Integer.valueOf(filterReqPass.get(0).getOccupationId()));
			
			List<CommonDataDetails> filterGroupList = groupList.stream().filter( o -> o.getRiskId().equals(Integer.valueOf(riskId2))).collect(Collectors.toList());
			CommonDataDetails groupData = new CommonDataDetails();
			EserviceBuildingDetails eserBuildingData = eserBuildingRepo.findByQuoteNoAndSectionId(req.getQuoteNo(),"0");
			if(filterGroupList.size() > 0) {
				groupData = filterGroupList.get(0);  
				
			}
			//	saveData.setSumInsured(new BigDecimal(reqdata.getSumInsured()));
			//	saveData.setPaDeathSuminsured(new BigDecimal(reqdata.getPaDeathSuminsured()));
			//	saveData.setPaPermanentdisablementSuminsured(new BigDecimal(reqdata.getPaPermanentdisablementSuminsured()));
			//	saveData.setPaMedicalSuminsured(new BigDecimal(reqdata.getPaMedicalSuminsured()));
			//	saveData.setPaTotaldisabilitySuminsured(new BigDecimal(reqdata.getPaTotaldisabilitySuminsured()));
				ProductEmployeeDetails saveData = new ProductEmployeeDetails();
				saveData=dozermapper.map(req, ProductEmployeeDetails.class);
				saveData.setEntryDate(entryDate);
				saveData.setCreatedBy(req.getCreatedBy());
				saveData.setStatus("Y");
				saveData.setRequestReferenceNo(req.getRequestReferenceNo());
				saveData.setQuoteNo(req.getQuoteNo());	
				saveData.setSectionDesc(filtersection.get(0).getSectionDesc());
				saveData.setSectionId(sectionId);
				saveData.setProductId(Integer.valueOf(filtersection.get(0).getProductId()));
				saveData.setProductDesc(filtersection.get(0).getProductDesc());
		//		saveData.setDescription(req.getDescription());
		//		saveData.setTypeDesc(type.getItemValue());
				
		//		saveData.setSerialNo(sno);
				
				saveData.setDateOfBirth(reqdata.getDob());
		//		saveData.setHeight(reqdata.getHeight()==null ? new BigDecimal(0) : new BigDecimal(reqdata.getHeight()));				
		//		saveData.setWeight(reqdata.getWeight()==null ? new BigDecimal(0) : new BigDecimal(reqdata.getWeight()));
				saveData.setSalary(new BigDecimal(reqdata.getSalary()));
				if (eserBuildingData != null) {
					if (StringUtils.isNotBlank(eserBuildingData.getExchangeRate().toString())) {
						BigDecimal exRate = eserBuildingData.getExchangeRate();
						if (StringUtils.isNotBlank(reqdata.getSalary())) {
							saveData.setSalaryLc(new BigDecimal(reqdata.getSalary()).multiply(exRate));
						} else {
							saveData.setSalaryLc(BigDecimal.ZERO);
						}
					}
				}

				
				saveData.setOccupationDesc(occupation.get(0).getOccupationName());
				saveData.setOccupationId(reqdata.getOccupationId());
				
				saveData.setNationalityId(StringUtils.isNotBlank(reqdata.getNationalityId())? reqdata.getNationalityId() :reqdata.getPersonName());
		//		saveData.setCategoryId(occupation.get(0).getCategoryId());
				saveData.setEmployeeId(Long.valueOf(personId));
				saveData.setEmployeeName(reqdata.getPersonName());
 				saveData.setRiskId(Integer.valueOf(reqdata.getRiskId()));
 				saveData.setCompanyId(filtersection.get(0).getCompanyId() );
 				saveData.setLocationId(Integer.valueOf(reqdata.getRiskId()));
 				saveData.setLocationName(StringUtils.isBlank(reqdata.getLocationName())?"":reqdata.getLocationName()) 	;			
 				
 				BigDecimal groupSuminsured = groupData.getSumInsured() ;
				BigDecimal individualSuminsured = new BigDecimal(reqdata.getSalary());
				BigDecimal sharePercent = groupSuminsured.divide(individualSuminsured, 2, RoundingMode.HALF_UP);
				
				List<PremiumGroupDevideRes> getDividedCovers = getDevidedCovers(saveData.getQuoteNo() , sharePercent, groupCovers ) ;
				
 				 // Covers
				 List<PolicyCoverDataIndividuals> indiCovers = new ArrayList<PolicyCoverDataIndividuals>(); 
					Type listType2 = new TypeToken<List<PolicyCoverDataIndividuals>>(){}.getType();
				 indiCovers = modelMapper.map(getDividedCovers,listType2);
				 for( PolicyCoverDataIndividuals o : indiCovers ) { 
					 o.setGroupId(groupData.getRiskId());
					 o.setGroupCount(groupData.getCount());
					 o.setVehicleId(saveData.getEmployeeId().intValue());
					 o.setIndividualId(saveData.getEmployeeId().intValue());
				 }					
				 totalIndiCovers.addAll(indiCovers);
				 
				personId++;
				//Age Calculator
				Date dob= (reqdata.getDob());
				Calendar cal = Calendar.getInstance();
				Date today = new Date();
				long years = today.getYear()-dob.getYear();
		        int age = (int)years;
			//	saveData.setAge(age);
				
		        saveDataList.add(saveData);
		       }
				
				
			}
			
			List<Integer> passengerIds = new ArrayList<Integer>();
			List<Long> passengerId2 = new ArrayList<Long>();
			
			Long empCount = repo.countByQuoteNoAndSectionIdAndEmployeeIdInAndStatusNot(homeData.getQuoteNo(), sectionId.toString(),passengerId2,"D");
			if(empCount > 0 ) {
				repo.deleteByQuoteNoAndSectionIdAndEmployeeIdInAndStatusNot(homeData.getQuoteNo() ,sectionId.toString(), passengerId2,"D");
			}
			repo.saveAllAndFlush(saveDataList);
			
			saveDataList.forEach( o -> {
				passengerIds.add(Integer.valueOf(o.getEmployeeId().toString()))	;	
				passengerId2.add(o.getEmployeeId())	;	
			});
			Long count = indiCoverRepo.countByQuoteNoAndSectionIdAndVehicleIdIn(homeData.getQuoteNo() ,Integer.valueOf(sectionId), passengerIds);
			if(count > 0 ) {
				indiCoverRepo.deleteByQuoteNoAndSectionIdAndVehicleIdIn(homeData.getQuoteNo(),Integer.valueOf(sectionId) ,passengerIds);
			}
			indiCoverRepo.saveAllAndFlush(totalIndiCovers);
			
			//uploaded Documents delete
			
			
			// Copy Old Doc
			OldDocumentsCopyReq oldDocCopyReq = new OldDocumentsCopyReq();
			oldDocCopyReq.setInsuranceId(homeData.getCompanyId());
			oldDocCopyReq.setProductId(homeData.getProductId().toString());
			oldDocCopyReq.setSectionId(sectionId.toString());
			oldDocCopyReq.setQuoteNo(homeData.getQuoteNo());
			docService.copyOldDocumentsToNewQuote(oldDocCopyReq);
			
			
			List<ProductEmployeeDetails> pass = repo.findByQuoteNoAndSectionIdAndStatusNot(homeData.getQuoteNo(),sectionId.toString(),"D");
			List<DocumentTransactionDetails> doc1 = docTransRepo.findByQuoteNoAndSectionId(homeData.getQuoteNo(), Integer.valueOf(sectionId));
			
			if(pass.size()>0) {
				if(doc1.size()>0) {
					
					List<DocumentTransactionDetails> filterDoc = doc1.stream()
							.filter( d ->  pass.stream().filter( o -> ! o.getStatus().equalsIgnoreCase("D")) .map( ProductEmployeeDetails :: getSectionId).anyMatch(  
									e ->    e.equals( d.getSectionId().toString() )))
							.filter( d -> pass.stream().filter( o -> ! o.getStatus().equalsIgnoreCase("D")).map( ProductEmployeeDetails :: getNationalityId).anyMatch(  
							e ->  e.equals( d.getId() ))).collect(Collectors.toList());
					if(filterDoc.size()>0) {
						List<DocumentTransactionDetails> del = doc1;
						del.removeAll(filterDoc);
						docTransRepo.deleteAll(del);	
					} else
						docTransRepo.deleteAll(doc1);
				}
				
				
			}else {
				
				docTransRepo.deleteAll(doc1);
			}
			
		}
			
		 catch (Exception e) {
			e.printStackTrace();
			log.info("Log Details" + e.getMessage());
			return null;
		}

		return res;
	}

	public List<PremiumGroupDevideRes> getDevidedCovers(String quoteNo , BigDecimal groupCount , List<PremiumGroupDevideRes> groupCovers ) {
		List<PremiumGroupDevideRes> devidedCovers = new ArrayList<PremiumGroupDevideRes>();
		
		try {
		for ( PremiumGroupDevideRes cover  : groupCovers) {
			cover.setActualRate(cover.getActualRate()==null?null : getDevidedValue(  cover.getActualRate() ,groupCount));
			cover.setMaxLodingAmount(cover.getMaxLodingAmount()==null?null : getDevidedValue(  cover.getMaxLodingAmount() ,groupCount));
			cover.setMinimumPremium(cover.getMinimumPremium()==null?null : getDevidedValue(  cover.getMinimumPremium() ,groupCount));
			cover.setPremiumAfterDiscountFc(cover.getPremiumAfterDiscountFc()==null?null : getDevidedValue(  cover.getPremiumAfterDiscountFc() ,groupCount));
			cover.setPremiumAfterDiscountLc(cover.getPremiumAfterDiscountLc()==null?null : getDevidedValue(  cover.getPremiumAfterDiscountLc() ,groupCount));
			cover.setPremiumBeforeDiscountFc(cover.getPremiumBeforeDiscountFc()==null?null : getDevidedValue(  cover.getPremiumBeforeDiscountFc() ,groupCount));
			cover.setPremiumBeforeDiscountLc(cover.getPremiumBeforeDiscountLc()==null?null : getDevidedValue(  cover.getPremiumBeforeDiscountLc() ,groupCount));
			cover.setPremiumExcludedTaxFc(cover.getPremiumExcludedTaxFc()==null?null : getDevidedValue(  cover.getPremiumExcludedTaxFc() ,groupCount));
			cover.setPremiumExcludedTaxLc(cover.getPremiumExcludedTaxLc()==null?null : getDevidedValue(  cover.getPremiumExcludedTaxLc() ,groupCount));
			cover.setPremiumIncludedTaxFc(cover.getPremiumIncludedTaxFc()==null?null : getDevidedValue(  cover.getPremiumIncludedTaxFc() ,groupCount));
			cover.setPremiumIncludedTaxLc(cover.getPremiumIncludedTaxLc()==null?null : getDevidedValue(  cover.getPremiumIncludedTaxLc() ,groupCount));
			cover.setRate(cover.getRate()==null?null : getDevidedValue(  cover.getRate() ,groupCount));
			cover.setRegulSumInsured(cover.getRegulSumInsured()==null?null : getDevidedValue(  cover.getRegulSumInsured() ,groupCount));
			cover.setSumInsured(cover.getSumInsured()==null?null : getDevidedValue(  cover.getSumInsured() ,groupCount));
			cover.setTaxAmount(cover.getTaxAmount()==null?null : getDevidedValue(  cover.getTaxAmount() ,groupCount));
			cover.setTaxRate(cover.getTaxRate()==null?null : getDevidedValue(  cover.getTaxRate() ,groupCount));  
			devidedCovers.add(cover);
		}
		
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Log Details"+e.getMessage());
			return null;
		}
		return devidedCovers;
	}
	
	
	private synchronized BigDecimal getDevidedValue(BigDecimal inputValue ,BigDecimal groupCount ) {
		BigDecimal devidedValue = BigDecimal.ZERO ;
		try {
			devidedValue = inputValue.divide(groupCount,2, RoundingMode.HALF_UP) ;
	
		}catch (Exception e) {
			e.printStackTrace();
			log.error("Exception is ---> " + e.getMessage());
			return null ;
		}
	
		return devidedValue;
	}
	

	public synchronized Integer currencyDecimalFormat(String insuranceId  ,String currencyId ) {
		Integer decimalFormat = 0 ;
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
			CriteriaQuery<CurrencyMaster> query = cb.createQuery(CurrencyMaster.class);
			List<CurrencyMaster> list = new ArrayList<CurrencyMaster>();
			
			// Find All
			Root<CurrencyMaster>    c = query.from(CurrencyMaster.class);		
			
			// Select
			query.select(c);
			
		
			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(c.get("currencyName")));
			
			// Effective Date Max Filter
			Subquery<Date> effectiveDate = query.subquery(Date.class);
			Root<CurrencyMaster> ocpm1 = effectiveDate.from(CurrencyMaster.class);
			effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart").as(Date.class)));
			Predicate a11 = cb.equal(c.get("currencyId"),ocpm1.get("currencyId") );
			Predicate a12 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
			Predicate a18 = cb.equal(c.get("status"),ocpm1.get("status") );
			Predicate a22 = cb.equal(c.get("companyId"), ocpm1.get("companyId"));
			
			effectiveDate.where(a11,a12,a18,a22);
			
			// Effective Date Max Filter
			Subquery<Date> effectiveDate2 = query.subquery(Date.class);
			Root<CurrencyMaster> ocpm2 = effectiveDate2.from(CurrencyMaster.class);
			effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd").as(Date.class)));
			Predicate a13 = cb.equal(c.get("currencyId"),ocpm2.get("currencyId") );
			Predicate a14 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
			Predicate a19 = cb.equal(c.get("status"),ocpm2.get("status") );
			Predicate a23 = cb.equal(c.get("companyId"), ocpm2.get("companyId"));
			
			effectiveDate2.where(a13,a14,a19,a23);
			
		    // Where	
			Predicate n1 = cb.equal(c.get("status"), "Y");
			Predicate n2 = cb.equal(c.get("effectiveDateStart"), effectiveDate);
			Predicate n3 = cb.equal(c.get("effectiveDateEnd"), effectiveDate2);
			Predicate n4 = cb.equal(c.get("companyId"),insuranceId);
			Predicate n5 = cb.equal(c.get("companyId"),"99999");
			Predicate n6 = cb.or(n4,n5);
			Predicate n7 = cb.equal(c.get("currencyId"),currencyId);
			query.where(n1,n2,n3,n6,n7).orderBy(orderList);
			
			// Get Result
			TypedQuery<CurrencyMaster> result = em.createQuery(query);			
			list =  result.getResultList(); 
			
			decimalFormat = list.size() > 0 ? (list.get(0).getDecimalDigit()==null?0 :list.get(0).getDecimalDigit()) :0; 		
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			return null;
		}
		return decimalFormat;
	}

	@Override
	public PersonalAccidentGetRes getpersonalaccident(PersonalAccidentGetReq req) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		PersonalAccidentGetRes res = new PersonalAccidentGetRes();
		DozerBeanMapper dozermapper = new DozerBeanMapper();
		try {
		ProductEmployeeDetails data = repo.findByQuoteNoAndRiskIdAndEmployeeIdAndSectionId(req.getQuoteNo(),
					Integer.valueOf(req.getRiskId()),Long.valueOf(req.getPersonId()),req.getSectionId());
		if(data!=null) {
			res = dozermapper.map(data, PersonalAccidentGetRes.class);
		//	res.setPaDeathSuminsured(data.getPaDeathSuminsured().toString());
		//	res.setPaMedicalSuminsured(data.getPaMedicalSuminsured().toString());
		//	res.setPaPermanentdisablementSuminsured(data.getPaMedicalSuminsured().toString());
		//	res.setPaTotaldisabilitySuminsured(data.getPaTotaldisabilitySuminsured().toString());
		//	res.setSumInsured(data.getSumInsured().toString());
			res.setRiskId(data.getRiskId().toString());
		//	res.setSerialNo(data.getSerialNo().toString());
			res.setDob(data.getDateOfBirth());
		//	res.setAge(data.getAge().toString());
		//	res.setHeight(data.getHeight().toString());
		//	res.setWeight(data.getWeight().toString());
			res.setEntryDate(data.getEntryDate());	
			res.setUpdatedDate(data.getEntryDate());	
			res.setSalary(data.getSalary().toString());
			res.setOccupationId(data.getOccupationId());
			res.setNationalityId(data.getNationalityId());
			
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
	public PersonalAccidentGetAllRes getallpersonalaccident(PersonalAccidentGetAllReq req) {
		PersonalAccidentGetAllRes res = new PersonalAccidentGetAllRes();
		DozerBeanMapper dozermapper  = new DozerBeanMapper();
		try {
			//List<ProductEmployeeDetails> datas = repo.findByQuoteNoAndSectionId(req.getQuoteNo(),req.getSectionId());
			List<ProductEmployeeDetails> datas = repo.findByRequestReferenceNoAndSectionId(req.getRequestReferenceNo(),req.getSectionId());

			if(datas!=null  && datas.size()>0 ) {
			
			res = dozermapper.map(datas.get(0),PersonalAccidentGetAllRes.class);			
			res.setQuoteNo(datas.get(0).getQuoteNo());
			res.setRequestReferenceNo(datas.get(0).getRequestReferenceNo());
			res.setSectionId(datas.get(0).getSectionId());				
			res.setSectionDesc(datas.get(0).getSectionDesc());
		//	res.setSerialNo(datas.get(0).getSerialNo().toString());
		//	res.setType(datas.get(0).getType());
		//	res.setTypeDesc(datas.get(0).getTypeDesc());
			List<PersonalDetailsGetallRes> resList1 = new ArrayList<PersonalDetailsGetallRes>();;
			for(ProductEmployeeDetails data : datas) {
				PersonalDetailsGetallRes res1 = new PersonalDetailsGetallRes();
				res1=dozermapper.map(data, PersonalDetailsGetallRes.class);
			//	res1.setPaDeathSuminsured(data.getPaDeathSuminsured().toString());
			//	res1.setPaMedicalSuminsured(data.getPaMedicalSuminsured().toString());
			//	res1.setPaPermanentdisablementSuminsured(data.getPaPermanentdisablementSuminsured().toString());
			//	res1.setPaTotaldisabilitySuminsured(data.getPaTotaldisabilitySuminsured().toString());
			//	res1.setSumInsured(data.getSumInsured().toString());
				res1.setDob(data.getDateOfBirth());
				res1.setPersonName(data.getEmployeeName());
				res1.setPersonId(data.getEmployeeId().toString() );
			//	res1.setAge(data.getAge().toString());
			//	res1.setHeight(data.getHeight().toString());
			//	res1.setWeight(data.getWeight().toString());
				res1.setSalary(data.getSalary()==null ? "" :new DecimalFormat("#").format(data.getSalary()));			
				res1.setOccupationId(data.getOccupationId());
				res1.setRiskId(data.getRiskId().toString());
				res1.setNationalityId(data.getNationalityId());
				resList1.add(res1);
			}
			res.setPersonalDeatils(resList1);
		
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
		
	
}
