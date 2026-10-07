package com.maan.eway.common.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.maan.eway.bean.BranchMaster;
import com.maan.eway.bean.BrokerCommissionDetails;
import com.maan.eway.bean.CompanyProductMaster;
import com.maan.eway.bean.CountryMaster;
import com.maan.eway.bean.EserviceCommonDetails;
import com.maan.eway.bean.EserviceCustomerDetails;
import com.maan.eway.bean.EserviceSectionDetails;
import com.maan.eway.bean.EserviceTravelDetails;
import com.maan.eway.bean.EserviceTravelGroupDetails;
import com.maan.eway.bean.InsuranceCompanyMaster;
import com.maan.eway.bean.ListItemValue;
import com.maan.eway.bean.LoginBranchMaster;
import com.maan.eway.bean.LoginMaster;
import com.maan.eway.bean.LoginUserInfo;
import com.maan.eway.bean.ProductGroupMaster;
import com.maan.eway.bean.ProductSectionMaster;
import com.maan.eway.bean.TravelPassengerDetails;
import com.maan.eway.common.req.EserviceSaveRes;
import com.maan.eway.common.req.EserviceTravelDeleteReq;
import com.maan.eway.common.req.EserviceTravelGetAllReq;
import com.maan.eway.common.req.EserviceTravelGetReq;
import com.maan.eway.common.req.EserviceTravelSaveReq;
import com.maan.eway.common.req.SequenceGenerateReq;
import com.maan.eway.common.req.TravelGroupGetRes;
import com.maan.eway.common.req.TravelGroupInsertReq;
import com.maan.eway.common.res.EserviceTravelGetRes;
import com.maan.eway.common.res.PremiaTiraReq;
import com.maan.eway.common.res.PremiaTiraRes;
import com.maan.eway.common.res.SuccessRes;
import com.maan.eway.common.service.PhoenixTravelDetailsService;
import com.maan.eway.repository.EServiceSectionDetailsRepository;
import com.maan.eway.repository.EserviceCommonDetailsRepository;
import com.maan.eway.repository.EserviceCustomerDetailsRepository;
import com.maan.eway.repository.EserviceTravelDetailsRepository;
import com.maan.eway.repository.EserviceTravelGroupDetailsRepository;
import com.maan.eway.repository.InsuranceCompanyMasterRepository;
import com.maan.eway.repository.LoginBranchMasterRepository;
import com.maan.eway.repository.LoginMasterRepository;
import com.maan.eway.repository.LoginUserInfoRepository;
import com.maan.eway.repository.ProductMasterRepository;
import com.maan.eway.repository.SectionMasterRepository;
import com.maan.eway.repository.TravelPassengerDetailsRepository;
import com.maan.eway.req.OneTimeTableReq;
import com.maan.eway.res.OneTimeTableRes;
import com.maan.eway.service.OneTimeService;

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
public class PhoenixTravelDetailServiceImpl implements PhoenixTravelDetailsService {
	

	@Autowired
	private EserviceCustomerDetailsRepository custRepo;
	
	@Autowired
	private EserviceTravelDetailsRepository repository;

	@Autowired
	private EserviceTravelGroupDetailsRepository groupRepo;

	@Autowired
	private LoginMasterRepository loginRepo ;

	@Autowired
	private LoginBranchMasterRepository lbranchRepo; 

	@Autowired
	private ProductMasterRepository productRepo;

	@Autowired
	private SectionMasterRepository sectionRepo;

	@Autowired
	private InsuranceCompanyMasterRepository companyRepo;

	@Autowired
	private EServiceSectionDetailsRepository eserSecRepo; 

	@Autowired
	private GenerateSeqNoServiceImpl genSeqNoService; 

	@Autowired
	private EserviceMotorDetailsServiceImpl motorService; 

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private OneTimeService otService;

	@Autowired
	private TravelPassengerDetailsRepository travelRepo ;

	@Autowired
	private TrackingDetailsServiceImpl  trackService ;

	@Autowired
	private LoginUserInfoRepository loginUserRepo ;

	@Autowired
	private PremiaBrokerServiceImpl premiaBrokerService;

	@Autowired
	private FetchErrorDescServiceImpl errorDescService ;
	
	@Autowired
	private EserviceCommonDetailsRepository humanRepo;

	@Autowired
	private EServiceSectionDetailsRepository secRepo;

	@Autowired
	private EserviceTravelGroupDetailsRepository grouprepo;


	private Logger log=LogManager.getLogger(EserviceTravelDetailsServiceImpl.class);

	Gson json = new Gson();
	/*
	public EserviceTravelDetailsServiceImpl(EserviceTravelDetailsRepository repo) {
	this.repository = repo;
	}

	  */
	 @Override
	    public EserviceTravelDetails create(EserviceTravelDetails d) {

	       EserviceTravelDetails entity;

	        try {
	            entity = repository.save(d);

	        } catch (Exception ex) {
				log.error(ex);
	            return null;
	        }
	        return entity;
	    }

	    
	    @Override
	    public EserviceTravelDetails update(EserviceTravelDetails d) {
	        EserviceTravelDetails c;

	        try {
	            c = repository.saveAndFlush(d);

	        } catch (Exception ex) {
				log.error(ex);
	            return null;
	        }
	        return c;
	    }

	/*
	    @Override
	    public EserviceTravelDetails getOne(long id) {
	        EserviceTravelDetails t;

	        try {
	            t = repository.findById(id).orElse(null);

	        } catch (Exception ex) {
				log.error(ex);
	            return null;
	        }
	        return t;
	    }

	*/
	    @Override
	    public List<EserviceTravelDetails> getAll() {
	        List<EserviceTravelDetails> lst;

	        try {
	            lst = repository.findAll();

	        } catch (Exception ex) {
				log.error(ex);
	            return Collections.emptyList();
	        }
	        return lst;
	    }


	    @Override
	    public long getTotal() {
	        long total;

	        try {
	            total = repository.count();
	        } catch (Exception ex) {
	            log.error(ex);
				return 0;
	        }
	        return total;
	    }


		@Override
		public List<String> validateTravelDetails(EserviceTravelSaveReq req) {
			List<String> error = new ArrayList<String>();
			
			try {

				if (StringUtils.isBlank(req.getBranchCode())) {
					
					error.add("1144");
					//error.add(new Error("01", "BranchCode", "Please Enter BranchCode "));
					
				} else if (req.getBranchCode().length() > 20) {
					error.add("1145");
					//error.add(new Error("01", "Branch Code", "Please Enter Branch Code within 20 Characters"));
				}
				if (StringUtils.isBlank(req.getProductId())) {
					error.add("1146");
					//error.add(new Error("03", "Product Id", "Please Enter ProductId "));
				} else if (req.getProductId().length() > 20) {
					error.add("1147");
					//error.add(new Error("03", "Product Id", "Please Enter Product Id within 20 Characters"));
				}
//				if (StringUtils.isBlank(req.getSectionId())) {
//					error.add("1148");
//					//error.add(new Error("04", "SectionId", "Please Enter SectionId"));
//				} else if (req.getSectionId().length() > 20) {
//					error.add("1149");
//					//error.add(new Error("04", "SectionId", "Please Enter SectionId within 20 Characters"));
//				}
				if (StringUtils.isBlank(req.getCompanyId())) {
					error.add("1150");
					//error.add(new Error("05", "CompanyId", "Please Enter CompanyId "));
				} else if (req.getCompanyId().length() > 20) {
					error.add("1151");
					//error.add(new Error("05", "CompanyId", "Please Enter CompanyId within 20 Characters"));
				}
				
				if (StringUtils.isBlank(req.getCurrency())) {
					error.add("1152");
					//error.add(new Error("10", "Currency", "Please Select Currency"));
				}
				
//				// Active / Deactive
//				if (StringUtils.isBlank(req.getCovidCoverYn())) {
//					error.add("1153");
//					//error.add(new Error("10", "CovidCover", "Please Select CovidCover Yes/No"));
//				} else if ( !(req.getCovidCoverYn().equalsIgnoreCase("Y") || req.getCovidCoverYn().equalsIgnoreCase("N") )) {
//					error.add("1154");
//					//error.add(new Error("10", "CovidCover", "Please Select Valid CovidCover Yes/No"));
//				}
//				if (StringUtils.isBlank(req.getSportsCoverYn())) {
//					error.add("1155");
//					//error.add(new Error("10", "SportsCover", "Please Select SportsCover Yes/No"));
//				} else if ( !(req.getSportsCoverYn().equalsIgnoreCase("Y") || req.getSportsCoverYn().equalsIgnoreCase("N") )) {
//					error.add("1155");
//					//error.add(new Error("10", "SportsCover", "Please Select SportsCover Yes/No"));
//				}
//				if (StringUtils.isBlank(req.getTerrorismCoverYn())) {
//					error.add("1156");
//				} else if ( !(req.getTerrorismCoverYn().equalsIgnoreCase("Y") || req.getTerrorismCoverYn().equalsIgnoreCase("N") )) {
//					error.add("1156");
//					//error.add(new Error("10", "TerrorismCover", "Please Select TerrorismCover Yes/No"));
//				}
//				if (StringUtils.isBlank(req.getHavepromocode())) {
//					error.add("1157");
//					//error.add(new Error("10", "Havepromocode", "Please Select Havepromocode Yes/No"));
//				} else if ( !(req.getHavepromocode().equalsIgnoreCase("Y") || req.getHavepromocode().equalsIgnoreCase("N") )) {
//					error.add("1157");
//				//	error.add(new Error("10", "Havepromocode", "Please Select Havepromocode Yes/No"));
//				}else if (req.getHavepromocode().equalsIgnoreCase("Y") ) {
//					if (StringUtils.isBlank(req.getPromocode())) {
//						error.add("1158");
//						//error.add(new Error("10", "Promocode", "Please Enter Promocode"));
//					}else if (req.getPromocode().length() > 20 ) {
//						error.add("1159");
//					}
//				}
				
				if (StringUtils.isBlank(req.getDestinationCountry())) {
					error.add("1160");
					//error.add(new Error("10", "DestinationCountry", "Please Select DestinationCountry"));
				}
//				if (StringUtils.isBlank(req.getPlanTypeId())) {
//					error.add("1161");
//				}
//				
				
//				if (StringUtils.isNotBlank(req.getPlanTypeId()) && StringUtils.isNotBlank(req.getTotalPassengers()) && req.getPlanTypeId().equalsIgnoreCase("4") ){
//					
//					if(req.getPlanTypeId().equalsIgnoreCase("4") &&  Integer.parseInt(req.getTotalPassengers())<2 ) { //group
//						error.add("1554");
//					}
//				}
				
				
				if (StringUtils.isBlank(req.getSourceCountry())) {
					error.add("1162");
				}
//				if (StringUtils.isBlank(req.getTravelCoverDuration())) {
//					error.add("1163");
//					//error.add(new Error("10", "TravelCoverDuration", "Please Select TravelCoverDuration"));
//				}
//				if (StringUtils.isBlank(req.getTravelCoverId())) {
//					error.add("1164");
//					//error.add(new Error("10", "TravelCoverId", "Please Select TravelCoverId"));
//				}
//				if (StringUtils.isBlank(req.getTravelId())) {
//					error.add("1165");
//					//error.add(new Error("10", "TravelId", "Please Select TravelId"));
//				}
				
				// Age Band Validation
				
				if (StringUtils.isBlank(req.getTotalPassengers())) {
					error.add("1166");
				//	error.add(new Error("10", "TotalPassengers", "Please Enter TotalPassengers"));
				} else 	if (! req.getTotalPassengers().matches("[0-9]+") ) {
					error.add("1167");
					//error.add(new Error("10", "TotalPassengers", "Please Enter Valid Number In TotalPassengers"));
				}
//				
//				if (StringUtils.isBlank(req.getTravelCoverDuration())) {
//					error.add("1168");
//					//error.add(new Error("10", "TravelCoverDuration", "Please Enter TravelCoverDuration"));
//				} else 	if (! req.getTotalPassengers().matches("[0-9]+") ) {
//					error.add("1169");
//					//error.add(new Error("10", "TravelCoverDuration", "Please Enter Valid Number In TravelCoverDuration"));
//				} 
				
				// Source Type Search Condition
				List<String> directSource = new ArrayList<String>();
				directSource.add("1");
				directSource.add("2");
				directSource.add("3");
				
				if( req.getUserType().equalsIgnoreCase("Issuer") && StringUtils.isBlank(req.getSourceTypeId()) )  {
					error.add("1170");
					//error.add(new Error("10", "BdmCode", "Please Select Source Type"));
					
				} else if  (StringUtils.isNotBlank(req.getSourceTypeId()) && directSource.contains(req.getSourceTypeId()) ){
					if (StringUtils.isBlank(req.getBdmCode())) {
						error.add("1171");
						//error.add(new Error("10", "BdmCode", "Please Select Source Code"));
					}
					if (StringUtils.isBlank(req.getCustomerName())) {
						error.add("1172");
						//error.add(new Error("10", "CustomerName", "Please Select Customer Name"));
					}
					
				} else {
					if(StringUtils.isBlank(req.getLoginId()) ) {
						error.add("1173");
						//error.add(new Error("10", "Login ID", "Please Select login Id"));
					} else {
						LoginMaster loginData =  	loginRepo.findByLoginId(req.getCreatedBy());
						if( loginData.getSubUserType().equalsIgnoreCase("bank")  ) {
							if( StringUtils.isBlank(req.getAcExecutiveId())) {
								error.add("1174");
								//error.add(new Error("01", "AcExecutiveId", "Please Select AcExecutiveId"));
							}
//													
						}
					}
					
					if (StringUtils.isBlank(req.getBrokerBranchCode())) {
						error.add("1175");
						//error.add(new Error("10", "BrokerBranchCode", "Please Enter BrokerBranchCode"));
					}
				}
				String status = StringUtils.isBlank(req.getStatus()) ? "Y" : req.getStatus() ;
				if (req.getTravelStartDate()==null) {
					error.add("1176");
					//error.add(new Error("13", "TravelStartDate", "Please Enter TravelStartDate"));
				} else if( (req.getEndorsementType()==null || req.getEndorsementType().equals(0))  && ! "RQ".equalsIgnoreCase(status) ){
					long MILLS_IN_A_DAY = 1000*60*60*24;
					long days90 = MILLS_IN_A_DAY * 90 ;
					Date today = new Date() ;
					
					int before = getBackDays(req.getCompanyId() , req.getProductId() , req.getLoginId() ) ;
					int days = before ==0 ? -1 : - before ;
					long backDays = MILLS_IN_A_DAY * days ;
					Date beforedays = new Date(today.getTime() + backDays);
					Date resticDate = new Date(today.getTime() + days90);
					if( req.getTravelStartDate().before(beforedays) ) {
						error.add("1196");
					} else if( req.getTravelStartDate().after(resticDate) ) {
						error.add("1177");
						//error.add(new Error("14", "TravelStartDate", "TravelStartDate  even after 90 days Not Allowed"));
					}
				}
				if (req.getTravelEndDate()==null) {
					error.add("1178");
					//error.add(new Error("14", "TravelEndDate", "Please Enter PolicyEndDate"));
				}else if (req.getTravelStartDate()!=null && req.getTravelEndDate()!=null && req.getEndorsementType()==null ) {
					if( req.getTravelStartDate().after(req.getTravelEndDate())) {
						error.add("1179");	
						//error.add(new Error("14", "TravelEndDate", "TravelStartDate After TravelEndDate Not Allwoed"));	
					}
					
				} 
				if (req.getTravelStartDate()!=null &&  req.getTravelEndDate()!=null  ) {
					// Date Differents
					Date periodStart =  req.getTravelStartDate();
					Date periodEnd = req.getTravelEndDate() ;
					String diff = "0";
					if(periodStart!=null && periodEnd!=null ) {
						Long diffInMillies = Math.abs(periodEnd.getTime() - periodStart.getTime());
						Long daysBetween =  TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS) ;
						
						// Check Leap Year
						if(daysBetween > 365  ) {
							error.add("1180");	
							//error.add(new Error("14", "TravelEndDate", "Travelling Date More Than 365 Days Not Allwoed"));
						}
						
					}
					
					
				}
				
				if (StringUtils.isBlank(req.getUserType())) {
					error.add("1181");
					//error.add(new Error("25", "UserType", "Please Enter UserType"));
				}
				
				 
				if(req.getGroupDetails() ==null || req.getGroupDetails().size()<=0 ) {
					error.add("1182");
					//error.add(new Error("01", "TravelGroupDetails", "Please Enter Alteast 1 TravelGroupDetails"));
				} else {
					List<TravelGroupInsertReq> notKids = req.getGroupDetails().stream().filter( o -> ! o.getGroupId().equalsIgnoreCase("1") ).collect(Collectors.toList());
					if(notKids.size()<=0 ) {
						//Kids validation is stoped because kids can travel eg.for studies
//						error.add("1183");
						//error.add(new Error("10", "Kids", "Kids Only Cannot Travel .Please Enter Parent/Guardians Count"));
					} else {
						for (TravelGroupInsertReq data :  req.getGroupDetails() ) {
							if (StringUtils.isBlank(data.getGroupId())) {
								error.add("1184");
								//error.add(new Error("10", "GroupId", "Please Enter GroupId"));
							} else 	if (! data.getGroupId().matches("[0-9]+") ) {
								error.add("1185");
								//error.add(new Error("10", "GroupId", "Please Enter Valid Number In GroupId"));
							} else 	if ( data.getGroupId().equalsIgnoreCase("0") ) {
								error.add("1185");
								//error.add(new Error("10", "GroupId", "Please Enter Valid Number In GroupId"));
							} else 	if ( data.getGroupId().equalsIgnoreCase("5") ) {
								
								if(!(req.getTravelCoverId().equalsIgnoreCase("25"))){
									error.add("1186");
									//error.add(new Error("10", "GroupId", "For Grand Senior, Only Europe/Schengen Plan Available"));
								}
								
							}
							
							if (StringUtils.isBlank(data.getGroupMembers())) {
								error.add("1187");
								//error.add(new Error("10", "GroupMembers", "Please Enter GroupMembers"));
							} else 	if (! data.getGroupMembers().matches("[0-9]+") ) {
								error.add("1188");
								//error.add(new Error("10", "GroupMembers", "Please Enter Valid Number In Members"));
							} else 	if ( data.getGroupMembers().equalsIgnoreCase("0") ) {
								error.add("1188");
								//error.add(new Error("10", "GroupMembers", "Please Enter Valid Number In Members"));
							}
							
							
							// Family Validation
//							if(StringUtils.isNotBlank(req.getPlanTypeId())  && req.getPlanTypeId().equalsIgnoreCase("3")) {
//								
//								if (StringUtils.isNotBlank(data.getGroupId()) &&  data.getGroupId().equalsIgnoreCase("1") ) {
//									if (StringUtils.isNotBlank(data.getGroupMembers()) &&  data.getGroupMembers().matches("[0-9]+") && Integer.valueOf(data.getGroupMembers()) >3  ) {
//										error.add("1189");
//										//error.add(new Error("10", "GroupMembers", "More Then 3 Kids Not Allowed "));
//									}
//								}
//								if (StringUtils.isNotBlank(data.getGroupId()) &&  data.getGroupId().equalsIgnoreCase("2") ) {
//									if (StringUtils.isNotBlank(data.getGroupMembers()) &&  data.getGroupMembers().matches("[0-9]+") && Integer.valueOf(data.getGroupMembers()) >2  ) {
//										error.add("1190");
//										//error.add(new Error("10", "GroupMembers", "More Then 2 Adults Not Allowed "));
//									}
//								}
//								
//								if (StringUtils.isNotBlank(data.getGroupId()) && !( data.getGroupId().equalsIgnoreCase("1") || data.getGroupId().equalsIgnoreCase("2")) ) {
//									if (StringUtils.isNotBlank(data.getGroupMembers()) ) {
//										error.add("1191");
//										//error.add(new Error("10", "GroupMembers", "Senior , Super Senior , Grand Senior Not Allowed in Family Plan"));
//									}
//								}
//							}
						}
					}
							
					
				}
				

				List<EserviceTravelDetails> list = new ArrayList<EserviceTravelDetails>();
				
				if ((StringUtils.isBlank(req.getRequestReferenceNo()))
						&& (StringUtils.isNotBlank(req.getBranchCode())) && (StringUtils.isNotBlank(req.getCompanyId()))
						&&(StringUtils.isNotBlank(req.getCreatedBy())) && (StringUtils.isNotBlank(req.getCovidCoverYn()))
						&& (StringUtils.isNotBlank(req.getCurrency())) && (StringUtils.isNotBlank(req.getCustomerReferenceNo()))
						&& (StringUtils.isNotBlank(req.getDestinationCountry())) && (StringUtils.isNotBlank(req.getExchangeRate()))
						&& (StringUtils.isNotBlank(req.getHavepromocode())) && (StringUtils.isNotBlank(req.getPlanTypeId()))
						&& (StringUtils.isNotBlank(req.getProductId())) && (StringUtils.isNotBlank(req.getPromocode()))
						//&&(StringUtils.isNotBlank(req.getCustomerReferenceNo())) 
						&& (StringUtils.isNotBlank(req.getSectionId()))
						&& (StringUtils.isNotBlank(req.getSourceCountry())) && (StringUtils.isNotBlank(req.getSportsCoverYn()))
						&& (StringUtils.isNotBlank(req.getTerrorismCoverYn())) && (StringUtils.isNotBlank(req.getTotalPassengers()))
						&& (StringUtils.isNotBlank(req.getTravelCoverDuration())) && (StringUtils.isNotBlank(req.getTravelCoverId()))
						&& (StringUtils.isNotBlank(req.getTravelId())) 
						&&( req.getTravelEndDate() !=null ) && (req.getTravelStartDate()!=null )
						) {
					
					CriteriaBuilder cb = em.getCriteriaBuilder();
					CriteriaQuery<EserviceTravelDetails> query = cb.createQuery(EserviceTravelDetails.class);
					// Find all
					Root<EserviceTravelDetails> b = query.from(EserviceTravelDetails.class);
					// Select
					query.select(b);
					// Where

					Predicate n5 = cb.equal(b.get("branchCode"), req.getBranchCode());
					Predicate n6 = cb.equal(b.get("companyId"), req.getCompanyId());
					Predicate n7 = cb.equal(cb.lower(b.get("createdBy")), req.getCreatedBy().toString().toLowerCase());
					Predicate n8 = cb.equal(cb.lower(b.get("covidCoverYn")), req.getCovidCoverYn().toString().toLowerCase());
					Predicate n9 = cb.equal(cb.lower(b.get("currency")), req.getCurrency().toString().toLowerCase());
					Predicate n10 = cb.equal(cb.lower(b.get("customerReferenceNo")), req.getCustomerReferenceNo().toString().toLowerCase());
					Predicate n11 = cb.equal(cb.lower(b.get("destinationCountry")), req.getDestinationCountry().toString().toLowerCase());
					Predicate n12 = cb.equal(b.get("exchangeRate"), req.getExchangeRate());
					Predicate n13 = cb.equal(cb.lower(b.get("havepromocode")), req.getHavepromocode().toString().toLowerCase());
					Predicate n14 = cb.equal(b.get("planTypeId"), req.getPlanTypeId());
					Predicate n15 = cb.equal(b.get("productId"), req.getProductId());
					Predicate n16 = cb.equal(cb.lower(b.get("promocode")), req.getPromocode().toString().toLowerCase());
					Predicate n17 = cb.equal(b.get("sectionId"), req.getSectionId());
					Predicate n18 = cb.equal(cb.lower(b.get("sourceCountry")), req.getSourceCountry().toString().toLowerCase());
					Predicate n19 = cb.equal(cb.lower(b.get("sportsCoverYn")), req.getSportsCoverYn().toString().toLowerCase());
					Predicate n20 = cb.equal(cb.lower(b.get("terrorismCoverYn")), req.getTerrorismCoverYn().toString().toLowerCase());
					Predicate n21 = cb.equal(b.get("totalPassengers"), req.getTotalPassengers());
					Predicate n22 = cb.equal(b.get("travelCoverDuration"), req.getTravelCoverDuration());
					Predicate n23 = cb.equal(b.get("travelCoverId"), req.getTravelCoverId());
					Predicate n24 = cb.equal(b.get("riskId"), req.getTravelId());
					
					Predicate n25 = cb.equal(b.get("travelEndDate"), req.getTravelEndDate());
					Predicate n26 = cb.equal(b.get("travelStartDate"), req.getTravelStartDate());
					
					query.where(n5,n6,n7,n8,n9,n10,n11,n12,n13,n14,n15,n16,
							n17,n18,n19,n20,n21,n22,n23,n23,n24,n25,n26) ;				
					// Get Result
					TypedQuery<EserviceTravelDetails> result = em.createQuery(query);
					list = result.getResultList();
					
					if (list.size() > 0) {
						error.add("1197");
					}
				} 
				
				/*
				if( (StringUtils.isNotBlank(req.getLoginId())) && (StringUtils.isNotBlank(req.getCompanyId()))	
						&& (StringUtils.isNotBlank(req.getProductId())) && (StringUtils.isNotBlank(req.getSectionId()))&& (StringUtils.isNotBlank(req.getBrokerCode()))){	
					String policy  =	policyRestriction( req.getLoginId(),req.getBrokerCode(),req.getCompanyId(),req.getProductId(),req.getSectionId());		
					if(!policy.equalsIgnoreCase("Success")) {
						error.add(new Error("43", "Policy Type", "This loginid is not eligible for this type of policy"));

					}
				
				}
	*/
				
			} catch (Exception e) {
				log.error(e);
				e.printStackTrace();
				error.add("1195");
			}
			return error;
		}
		
		public Integer getBackDays(String companyId , String productId , String loginId ) {
			Integer backDays = 0 ;
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

				List<BrokerCommissionDetails> list = new ArrayList<BrokerCommissionDetails>();
			
				// Find Latest Record
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<BrokerCommissionDetails> query = cb.createQuery(BrokerCommissionDetails.class);

				// Find All
				Root<BrokerCommissionDetails> b = query.from(BrokerCommissionDetails.class);

				// Select
				query.select(b);

				// Effective Date Max Filter
				Subquery<Date> effectiveDate = query.subquery(Date.class);
				Root<BrokerCommissionDetails> ocpm1 = effectiveDate.from(BrokerCommissionDetails.class);
				effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart").as(Date.class)));
				jakarta.persistence.criteria.Predicate a1 = cb.equal(b.get("companyId"), ocpm1.get("companyId"));
				jakarta.persistence.criteria.Predicate a2 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
				jakarta.persistence.criteria.Predicate a3 = cb.equal(b.get("loginId"), ocpm1.get("loginId"));
				jakarta.persistence.criteria.Predicate a4 = cb.equal(b.get("productId"), ocpm1.get("productId"));
				jakarta.persistence.criteria.Predicate a11 = cb.equal(b.get("policyType"), ocpm1.get("policyType"));
				jakarta.persistence.criteria.Predicate a12 = cb.equal(b.get("id"), ocpm1.get("id"));
				effectiveDate.where(a1, a2, a3,a4,a11,a12);
				
				// Effective Date End Max Filter
				Subquery<Date> effectiveDate2 = query.subquery(Date.class);
				Root<BrokerCommissionDetails> ocpm2 = effectiveDate2.from(BrokerCommissionDetails.class);
				effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd").as(Date.class)));
				jakarta.persistence.criteria.Predicate a6 = cb.equal(b.get("companyId"), ocpm2.get("companyId"));
				jakarta.persistence.criteria.Predicate a8 = cb.equal(b.get("productId"), ocpm2.get("productId"));
				jakarta.persistence.criteria.Predicate a9 = cb.equal(b.get("loginId"), ocpm2.get("loginId"));
				jakarta.persistence.criteria.Predicate a10 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
				jakarta.persistence.criteria.Predicate a13 = cb.equal(b.get("policyType"), ocpm2.get("policyType"));
				jakarta.persistence.criteria.Predicate a14 = cb.equal(b.get("id"), ocpm2.get("id"));
				effectiveDate2.where(a6,  a8, a9, a10,a13,a14);

				// Order By
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.asc(b.get("policyType")));

				// Where
				Predicate n1 = cb.equal(b.get("effectiveDateStart"), effectiveDate);
				Predicate n2 = cb.equal(b.get("effectiveDateEnd"), effectiveDate2);
				Predicate n3 = cb.equal(b.get("companyId"), companyId);
				Predicate n4 = cb.equal(b.get("productId"),productId);
				Predicate n5 = cb.equal(b.get("loginId"), loginId);
				Predicate n6 = cb.equal(b.get("policyType"),"99999");
				Predicate n7 = cb.equal(b.get("id"),"99999");
				query.where(n1,n2,n3,n4,n5,n6,n7).orderBy(orderList);
				
				// Get Result
				TypedQuery<BrokerCommissionDetails> result = em.createQuery(query);

				list = result.getResultList();
				backDays = list.size() > 0 ? (list.get(0).getBackDays() !=null ? list.get(0).getBackDays() : 0)  : 0 ;
				
			} catch (Exception e) {
				e.printStackTrace();
				log.info("Exception is ---> " + e.getMessage());
				return null;
			}
			return backDays;
		}

		private String policyRestriction(String loginId, String bdmCode, String companyId,
				String productId, String policyType) {
			// TODO Auto-generated method stub
			String a ="";
			DozerBeanMapper mapper = new DozerBeanMapper();
			try {
				Date today = new Date();
				Calendar cal = new GregorianCalendar();
				cal.setTime(today);
				cal.set(Calendar.HOUR_OF_DAY, 23);
				cal.set(Calendar.MINUTE, 1);
				today = cal.getTime();

				List<BrokerCommissionDetails> list = new ArrayList<BrokerCommissionDetails>();
			
				// Find Latest Record
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<BrokerCommissionDetails> query = cb.createQuery(BrokerCommissionDetails.class);

				// Find All
				Root<BrokerCommissionDetails> b = query.from(BrokerCommissionDetails.class);

				// Select
				query.select(b);

				// Amend ID Max Filter
				Subquery<Long> amendId = query.subquery(Long.class);
				Root<BrokerCommissionDetails> ocpm1 = amendId.from(BrokerCommissionDetails.class);
				amendId.select(cb.max(ocpm1.get("amendId")));
				//Predicate a1 = cb.equal(ocpm1.get("agencyCode"), b.get("agencyCode"));
				Predicate a2 = cb.equal(ocpm1.get("companyId"), b.get("companyId"));
				Predicate a3 = cb.equal(ocpm1.get("productId"), b.get("productId"));
				Predicate a4 = cb.equal(ocpm1.get("oaCode"), b.get("oaCode"));
				Predicate a5 = cb.equal(ocpm1.get("loginId"), b.get("loginId"));
				Predicate a6 = cb.equal(ocpm1.get("policyType"), b.get("policyType"));
				//Predicate a7 = cb.equal(ocpm1.get("id"), b.get("id"));
				Predicate a8 = cb.equal(ocpm1.get("status"), b.get("status"));

				amendId.where( a2,a3,a4,a5,a6,a8);

				// Order By
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.asc(b.get("policyType")));

				// Where
				Predicate n1 = cb.equal(b.get("amendId"), amendId);
				Predicate n2 = cb.equal(b.get("companyId"), companyId);
				Predicate n3 = cb.equal(b.get("productId"),productId);
				Predicate n4 = cb.equal(b.get("oaCode"),bdmCode);
				Predicate n5 = cb.equal(b.get("loginId"),loginId);
				Predicate n6 = cb.equal(b.get("policyType"),policyType);
				Predicate n7 = cb.equal(b.get("status"),"Y");
				//Predicate n8 = cb.equal(b.get("agencyCode"),agencyCode);
				Predicate n9 = cb.equal(b.get("status"),"R");
				Predicate n10 = cb.or(n7,n9);

				query.where(n1,n2,n3,n4,n5,n6,n10).orderBy(orderList);
				
				// Get Result
				TypedQuery<BrokerCommissionDetails> result = em.createQuery(query);

				list = result.getResultList();
				List<BrokerCommissionDetails> policy = list
						  .stream()
						  .filter(c -> c.getPolicyTypeDesc().equalsIgnoreCase("ALL"))
						  .collect(Collectors.toList());
				if(policy.size()>0 || list.size()>0) {
					a = "Success";
					}
				} catch (Exception e) {

				log.error(e);
				e.printStackTrace();
			}
			return a;
		}

		@Override
		public EserviceSaveRes saveTravelDetails(EserviceTravelSaveReq req) {
			EserviceSaveRes res = new EserviceSaveRes();
			DozerBeanMapper mapper = new DozerBeanMapper();
		//	SimpleDateFormat idf = new SimpleDateFormat("yyMMddmmssSSS");
			EserviceTravelDetails savedata = new EserviceTravelDetails();
			List<EserviceTravelDetails>findData = new ArrayList<EserviceTravelDetails>();
			List<EserviceCommonDetails> findHumans = new ArrayList<EserviceCommonDetails>();
			List<EserviceSectionDetails> findsectionDetails = new ArrayList<EserviceSectionDetails>();
			List<EserviceTravelGroupDetails> findgroupdetails=new ArrayList<EserviceTravelGroupDetails>();
			String oaCode = "" ;
			try {
				
				String productName =   getCompanyProductMasterDropdown(req.getCompanyId() , req.getProductId()); //productRepo.findByProductIdOrderByAmendIdDesc(Integer.valueOf(req.getProductId()));
				ProductSectionMaster section =  getProductSectionDropdown(req.getCompanyId() , req.getProductId(), req.getSectionId());// sectionRepo.findBySectionIdOrderByAmendIdDesc(Integer.valueOf(req.getSectionId())); 
				String companyName =  getInscompanyMasterDropdown(req.getCompanyId()) ; // companyRepo.findByCompanyIdOrderByAmendIdDesc(req.getCompanyId());
				
				Integer traId = 0 ;
				String refNo = "" ;
				Date entryDate = null ;
				String createdBy = "" ;
				
				if(StringUtils.isBlank(req.getRequestReferenceNo())) {
					// Save
					entryDate = new Date();
					createdBy = req.getCreatedBy();
				//	Random rand = new Random();
		        //  int random=rand.nextInt(90)+10; 
		            
					//String refShortCode = getListItem (req.getCompanyId() , req.getBranchCode() ,"PRODUCT_SHORT_CODE",req.getProductId() );  
		     		// refNo = refShortCode +"-"  + genSeqNoService.generateRefNo() ; // idf.format(new Date()) + random ; 
		     		// Generate Seq
		 			SequenceGenerateReq generateSeqReq = new SequenceGenerateReq();
		 		 	generateSeqReq.setInsuranceId(req.getCompanyId());  
		 		 	generateSeqReq.setProductId(req.getProductId());
		 		 	generateSeqReq.setType("2");
		 		 	generateSeqReq.setTypeDesc("REQUEST_REFERENCE_NO");
		 		 	refNo =  genSeqNoService.generateSeqCall(generateSeqReq);
		     		 
		     		 res.setResponse("Saved Successfully");
		 			 res.setRequestReferenceNo(refNo);
		 			 
		 			 res.setTravelId(req.getTravelId()) ;			 
				} 
				else {
					// Update
					
					try {
						// Find old
						findHumans = humanRepo.findByRequestReferenceNo(req.getRequestReferenceNo());
						findsectionDetails = secRepo.findByRequestReferenceNo(req.getRequestReferenceNo());
						findData = repository.findByRequestReferenceNo(req.getRequestReferenceNo());
						findgroupdetails=grouprepo.findByRequestReferenceNo(req.getRequestReferenceNo());

						// Delete Old Records
						if (findgroupdetails != null && findgroupdetails.size() > 0) {
							grouprepo.deleteAll(findgroupdetails);
							List<EserviceTravelGroupDetails> afterDeleteGroups =
							    grouprepo.findByRequestReferenceNo(req.getRequestReferenceNo());
							System.out.println("Group details count after delete: " + afterDeleteGroups.size());
						}
						if (findData != null && findData.size() > 0) {
							repository.deleteAll(findData);
							List<EserviceTravelDetails> afterDeleteTravel =
									repository.findByRequestReferenceNo(req.getRequestReferenceNo());
								System.out.println("Group details count after delete: " + afterDeleteTravel.size());
						}
						
						if (findHumans != null && findHumans.size() > 0) {
							humanRepo.deleteAll(findHumans);
							List<EserviceCommonDetails> afterDeleteHuman =
									humanRepo.findByRequestReferenceNo(req.getRequestReferenceNo());
								System.out.println("Group details count after delete: " + afterDeleteHuman.size());
						}
						if (!findsectionDetails.isEmpty()) {
							secRepo.deleteAll(findsectionDetails);
							List<EserviceSectionDetails> afterDeleteSection =
									secRepo.findByRequestReferenceNo(req.getRequestReferenceNo());
								System.out.println("Group details count after delete: " + afterDeleteSection.size());
						}
						System.out.println("Deleted Successfully");
						System.out.println("Group details count: " + findgroupdetails.size());
						System.out.println("Travel details count: " + findData.size());
						System.out.println("Human details count: " + findHumans.size());
						System.out.println("Section details count: " + findsectionDetails.size());
					} catch (Exception e) {
						e.printStackTrace();
						log.info("Exception Is ---> " + e.getMessage());

					}
					refNo = req.getRequestReferenceNo() ;
									
					if(findData !=null ) {
					entryDate = new Date();
					createdBy = req.getCreatedBy();
					res.setResponse("Updated Successfully");
					res.setRequestReferenceNo(refNo);
					res.setCustomerReferenceNo(req.getCustomerReferenceNo());
					res.setTravelId(req.getTravelId()) ;
					
					} else {
						entryDate = new Date();
						createdBy = req.getCreatedBy();
						res.setResponse("Updated Successfully");
						res.setRequestReferenceNo(refNo);
						res.setCustomerReferenceNo(req.getCustomerReferenceNo());
						res.setTravelId(req.getTravelId()) ;
					}
				}
				
				savedata=mapper.map(req,EserviceTravelDetails.class);
				traId = Integer.valueOf(req.getTravelId()) ;
				savedata.setRiskId(traId);	
				savedata.setLocationId(1);
				savedata.setCreatedBy(createdBy);
				savedata.setEntryDate(entryDate);
				savedata.setRequestReferenceNo(refNo);
				savedata.setBranchCode(  req.getBranchCode()) ;
				savedata.setAcExecutiveId(StringUtils.isBlank(req.getAcExecutiveId())?null :Integer.valueOf(req.getAcExecutiveId()));
				savedata.setApplicationId(req.getApplicationId());
				savedata.setUpdatedBy(req.getCreatedBy());
				savedata.setUpdatedDate(new Date());
				
				if ("SQ".equals(req.getSavedFrom())) {
//					savedata.setIdNumber("11111");
//					savedata.setFleetOwnerYn("N");
					
					
					if(StringUtils.isBlank(req.getCustomerReferenceNo())) {
						String customerRefNo = createCustomerForShortQuote(req);
						req.setCustomerReferenceNo(customerRefNo);
						savedata.setCustomerReferenceNo(customerRefNo);
						res.setCustomerReferenceNo(req.getCustomerReferenceNo());
					}

				}
				
				// Source Type Search Condition
				List<String> directSource = new ArrayList<String>();
				directSource.add("1");
				directSource.add("2");
				directSource.add("3");
				Double commissionPercent = 0.0;
				// Source Type Search Condition
				List<ListItemValue> sourcerTypes = premiaBrokerService.getSourceTypeDropdown(req.getCompanyId() , req.getBranchCode() ,"SOURCE_TYPE"); 
				List<ListItemValue> filterSource =  sourcerTypes.stream().filter( o -> StringUtils.isNotBlank(req.getSourceTypeId()) && 
						( o.getItemCode().equalsIgnoreCase(req.getSourceTypeId()) || o.getItemValue().equalsIgnoreCase(req.getSourceTypeId()) )  ).collect(Collectors.toList());				
				
				// Broker Commission
				if(filterSource.size() > 0 && directSource.contains(filterSource.get(0).getItemCode())  ) {
					//commissionPercent=12.5;
					String sourceType = filterSource.get(0).getItemValue();
					String subUserType = sourceType.contains("Broker") ? "Broker" : sourceType.contains("Agent") ? "Agent" : "" ;
					LoginMaster premiaLogin =  getPremiaBroker(req.getBdmCode() , subUserType , req.getCompanyId()  );
					String premiaLoginId = premiaLogin != null ? premiaLogin.getLoginId() : ""  ;
					
					String  commission =  getListItem (req.getCompanyId() , req.getBranchCode() ,"COMMISSION_PERCENT",filterSource.get(0).getItemValue() );
					if(StringUtils.isNotBlank(premiaLoginId) ) {
						List<BrokerCommissionDetails> commissionList = getPolicyName(req.getCompanyId(), req.getProductId(), premiaLoginId, req.getBrokerCode(), req.getSectionId());
						commission = commissionList.size() > 0 && commissionList.get(0).getCommissionPercentage() !=null ? commissionList.get(0).getCommissionPercentage().toString() : commission ;					
					}
					commissionPercent = StringUtils.isNotBlank(commission) ? Double.valueOf(commission ) : 0D;
					savedata.setCommissionPercentage(commissionPercent ==null ? new BigDecimal("0") : new BigDecimal(commissionPercent) );
					
				} else {
					String loginId = StringUtils.isNotBlank(req.getSourceType()) &&  req.getSourceType().toLowerCase().contains("b2c") ? "guest" : req.getLoginId() ;
					
					List<BrokerCommissionDetails> commissionList = getPolicyName(req.getCompanyId(), req.getProductId(), loginId, req.getBrokerCode(), req.getSectionId());
					if( commissionList!=null && commissionList.size()>0 ) {
						BrokerCommissionDetails comm = commissionList.get(0) ;
						savedata.setCommissionPercentage(comm.getCommissionPercentage() ==null ? new BigDecimal("0") : new BigDecimal(comm.getCommissionPercentage()) );
						savedata.setVatCommission(comm.getCommissionVatPercent() ==null ? new BigDecimal("0") : new BigDecimal(comm.getCommissionVatPercent()) );
					} else {
						savedata.setCommissionPercentage(new BigDecimal("0") );
						savedata.setVatCommission( new BigDecimal("0") );
					}
				}
				
				
				
				// Admin Details
				if( findData !=null ) {
					
					savedata.setAdminLoginId(req.getAdminLoginId());
					savedata.setAdminRemarks(req.getAdminRemarks());
					savedata.setReferalRemarks(req.getReferalRemarks());
					savedata.setRejectReason(req.getRejectReason());
					savedata.setQuoteNo(req.getEndtPrevQuoteNo());
					savedata.setCustomerId(req.getCustomerName());
					savedata.setOldReqRefNo(req.getRequestReferenceNo());
					savedata.setCreatedBy(req.getReferalRemarks());
					savedata.setEntryDate(new Date());
					savedata.setManualReferalYn(req.getReferalRemarks());
					
				}
				if( StringUtils.isNotBlank(req.getCommissionType())) {
					String commistionDesc =getListItem (req.getCompanyId() , req.getBranchCode() ,"COMMISSION_TYPE",req.getCommissionType());
					savedata.setCommissionTypeDesc(commistionDesc);
				}
				
				String planType = getListItem (req.getCompanyId() , req.getBranchCode() ,"PLAN_TYPE",req.getPlanTypeId() ); 
				String coverDesc =  getCoverName(req.getCompanyId() , req.getProductId() , req.getSectionId()) ;
				
				savedata.setPlanTypeDesc(planType);
				savedata.setTravelCoverDesc(coverDesc);
				savedata.setProductName(productName);
				savedata.setSectionName(section.getSectionName());
				savedata.setCompanyName(companyName);
				
				String exRateStr = req.getExchangeRate();

				BigDecimal exRate = (exRateStr != null && !exRateStr.trim().isEmpty())? new BigDecimal(exRateStr.trim()) : BigDecimal.ZERO; 
				savedata.setLcExchangeRate(exRate);
				savedata.setExchangeRate(new BigDecimal(1));
				savedata.setCurrency(req.getCurrency());
				String sourceCountry =  getCountryName(req.getSourceCountry());
				String DestinationCountry =  getCountryName(req.getDestinationCountry());
				savedata.setSourceCountryDesc(sourceCountry);
				savedata.setDestinationCountryDesc(DestinationCountry);
				savedata.setPromocode(req.getHavepromocode().equalsIgnoreCase("N")?null: req.getPromocode());
				savedata.setFinalizeYn(StringUtils.isNotBlank(req.getFinalizeYn()) ? req.getFinalizeYn() :"N") ;
				savedata.setSectionId((StringUtils.isBlank(req.getSectionId())?null:Integer.valueOf(req.getSectionId())));
				// Admin Details
				
				// Endoresment Chagnes
				if(!(req.getEndorsementType()==null || req.getEndorsementType()==0))
					
				 {
				  
					 savedata.setOriginalPolicyNo(req.getOriginalPolicyNo());
					 savedata.setEndorsementDate(req.getEndorsementDate());
					 savedata.setEndorsementRemarks(req.getEndorsementRemarks());
					 savedata.setEndorsementEffdate(req.getEndorsementEffdate());
					 savedata.setEndtPrevPolicyNo(req.getEndtPrevPolicyNo());
					 savedata.setEndtPrevQuoteNo(req.getEndtPrevQuoteNo());
					 savedata.setEndtCount(req.getEndtCount());
					 savedata.setEndtStatus(req.getEndtStatus());
					 savedata.setIsFinyn(req.getIsFinaceYn());
					 savedata.setEndtCategDesc(req.getEndtCategDesc());
					 savedata.setEndorsementType(req.getEndorsementType());
					 savedata.setEndorsementTypeDesc(req.getEndorsementTypeDesc());  
					 savedata.setPolicyNo(req.getPolicyNo());
					 
					 //Endt Commission
					
					List<TravelPassengerDetails> mainMotList = travelRepo.findByQuoteNoOrderByPassengerIdAsc(req.getEndtPrevQuoteNo() );
					mainMotList = mainMotList.stream().filter( o -> o.getSectionId().equals(Integer.valueOf(req.getSectionId()) ) ).collect(Collectors.toList() );
					if( mainMotList!=null && mainMotList.size() > 0) {
						 TravelPassengerDetails mainMot = mainMotList.get(0);
						 savedata.setCommissionPercentage(mainMot.getCommissionPercentage() ==null ? savedata.getCommissionPercentage() : mainMot.getCommissionPercentage() );
						 savedata.setVatCommission(mainMot.getVatCommission() ==null ? savedata.getVatCommission() : mainMot.getVatCommission() );
					}
					
				}
				// status
				savedata.setStatus(StringUtils.isBlank(req.getStatus()) ? "Y" : req.getStatus()) ;
				if(savedata.getStatus().equalsIgnoreCase("D") ) {
					//	BigDecimal.ZERO
						
				}
				// Date Differents
				Date periodStart =  req.getTravelStartDate();
				Date periodEnd = req.getTravelEndDate() ;
				String diff = "0";
				
				if(periodStart!=null && periodEnd!=null ) {
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
					String st = sdf.format(req.getTravelStartDate()) ;
					String ed = sdf.format(req.getTravelEndDate()) ;
					if( st.equalsIgnoreCase(ed) && ( req.getEndorsementType()==null || req.getEndorsementType()==0)) {
						diff = "1" ;
					} else if( st.equalsIgnoreCase(ed) ) {
						diff = "0" ;
					} else {
						Long diffInMillies = Math.abs(periodEnd.getTime() - periodStart.getTime());
						Long daysBetween =  TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS) + 1 ;
						
						// Check Leap Year
						//SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd"); 
						//boolean leapYear = LocalDate.parse(sdf2.format(periodEnd) ).isLeapYear();
//						diff = String.valueOf(daysBetween==364 &&  leapYear==false ? daysBetween+1 : 
//							  daysBetween==364 &&  leapYear==true ? daysBetween+2 : 
//							  daysBetween==365 &&  leapYear==true ? daysBetween+1 : daysBetween );
						diff = String.valueOf(daysBetween);
					}
					
				}
				savedata.setTravelCoverDuration(Integer.valueOf(diff));
				
				// Source Type Details 
				BranchMaster branchData = getBranchMasterRes(req.getCompanyId()   ,req.getBranchCode() );
				savedata.setBranchCode(  req.getBranchCode()) ;
				savedata.setBranchName(branchData.getBranchName());
				savedata.setApplicationId(StringUtils.isBlank(req.getApplicationId())?"1": req.getApplicationId());
				
						
				if(filterSource.size() > 0 && directSource.contains(filterSource.get(0).getItemCode())  ) {
					
					String sourceType = filterSource.get(0).getItemValue();
					String subUserType = sourceType.contains("Broker") ? "Broker" : sourceType.contains("Agent") ? "Agent" : "" ;
					LoginMaster premiaLogin =  getPremiaBroker(req.getBdmCode() , subUserType , req.getCompanyId()  );
					String premiaLoginId = premiaLogin != null ? premiaLogin.getLoginId() : ""  ;
					
					LoginUserInfo premiaUser = loginUserRepo.findByLoginId(premiaLoginId);
					LoginUserInfo	loginUserData = premiaUser!=null ? premiaUser : loginUserRepo.findByLoginId(branchData.getDirectBrokerId());
					
					LoginBranchMaster premiaBranch = lbranchRepo.findByLoginIdAndBranchCodeAndCompanyId(premiaLoginId , req.getBranchCode() ,req.getCompanyId());
					LoginBranchMaster brokerBranch = premiaBranch!=null ? premiaBranch : lbranchRepo.findByLoginIdAndBranchCodeAndCompanyId(branchData.getDirectBrokerId() , req.getBranchCode() ,req.getCompanyId());
					
					
					savedata.setBrokerCode(loginUserData.getOaCode());
					savedata.setAgencyCode(loginUserData.getAgencyCode());
					savedata.setLoginId(loginUserData.getLoginId());
					savedata.setCustomerCode(req.getBdmCode());
					savedata.setCustomerName(req.getCustomerName());
					savedata.setBdmCode(req.getBdmCode());
					savedata.setBrokerBranchCode(brokerBranch.getBrokerBranchCode() );
					savedata.setBrokerBranchName(brokerBranch.getBrokerBranchName() );
					savedata.setSourceTypeId(filterSource.get(0).getItemCode());
					savedata.setSourceType(filterSource.get(0).getItemValue());
					
					// Direct Source Type 
					if(filterSource.get(0).getItemValue().contains("Direct") || (premiaLogin!=null && premiaUser!=null && premiaBranch!=null)) {
						savedata.setSalePointCode(brokerBranch.getSalePointCode());
						savedata.setBrokerTiraCode(loginUserData.getRegulatoryCode());		
					} else {
						try {
							// Broker Tira Code
							PremiaTiraReq brokerTiraCodeReq = new PremiaTiraReq(); 
							brokerTiraCodeReq.setInsuranceId(req.getCompanyId());
							brokerTiraCodeReq.setPremiaCode(req.getCustomerCode());
							List<PremiaTiraRes> brokerTira =  premiaBrokerService.searchPremiaBrokerTiraCode(brokerTiraCodeReq);
							String brokerTiraCode = "" ;
							if(brokerTira.size() > 0 ) {
								brokerTiraCode = brokerTira.get(0).getTiraCode();
							}
							
							// Sale Point Code
							PremiaTiraReq brokerSpCodeReq = new PremiaTiraReq(); 
							brokerSpCodeReq.setInsuranceId(req.getCompanyId());
							brokerSpCodeReq.setPremiaCode(brokerTiraCode);
							List<PremiaTiraRes> brokerSp =  premiaBrokerService.searchPremiaBrokerSpCode(brokerSpCodeReq);
							String brokerSpCode = "" ;
							if(brokerSp.size() > 0 ) {
								brokerSpCode = brokerSp.get(0).getTiraCode();
							}
							
							savedata.setSalePointCode(brokerSpCode);
							savedata.setBrokerTiraCode(brokerTiraCode);	
						} catch(Exception e){
							e.printStackTrace();
							log.info("Log Details" + e.getMessage());
							
						}
					}
					
				} else  {
					LoginUserInfo loginUserData = loginUserRepo.findByLoginId(req.getLoginId());
					LoginBranchMaster brokerBranch =lbranchRepo.findByLoginIdAndBrokerBranchCodeAndCompanyId(req.getLoginId()  , req.getBrokerBranchCode() ,req.getCompanyId());
					LoginMaster loginData = loginRepo.findByLoginId(req.getLoginId());
					
					savedata.setBrokerCode(loginData.getOaCode());
					savedata.setAgencyCode(loginData.getAgencyCode());
					savedata.setCustomerCode(loginUserData.getCustomerCode());
					savedata.setLoginId(req.getLoginId());
					savedata.setCustomerName(loginUserData.getCustomerName());
					savedata.setBdmCode(null);
					savedata.setBrokerBranchCode(brokerBranch.getBrokerBranchCode() );
					savedata.setBrokerBranchName(brokerBranch.getBrokerBranchName() );
					savedata.setSalePointCode(brokerBranch.getSalePointCode());
					savedata.setBrokerTiraCode(loginUserData.getRegulatoryCode());	
					
					List<ListItemValue> filterBrokerSource =  sourcerTypes.stream().filter( o -> o.getItemValue().equalsIgnoreCase(loginData.getSubUserType()) ).collect(Collectors.toList());
					savedata.setSourceTypeId(filterBrokerSource.size() > 0 ? filterBrokerSource.get(0).getItemCode() : "" );
					savedata.setSourceType(filterBrokerSource.size() > 0 ? filterBrokerSource.get(0).getItemValue() : loginData.getSubUserType() );

				}
				savedata.setAcExecutiveId(StringUtils.isBlank(req.getAcExecutiveId()) ? null  : Integer.valueOf(req.getAcExecutiveId()));
				if( "1".equalsIgnoreCase(req.getApplicationId()  )) {
					savedata.setSubUserType(savedata.getSourceType());
				} else {
					LoginMaster issuerData = loginRepo.findByLoginId(req.getApplicationId());
					savedata.setSubUserType(issuerData!=null ? issuerData.getSubUserType() : savedata.getSourceType() ) ;
				}
		        
			
				createdBy = req.getCreatedBy();
				savedata.setCreatedBy(createdBy);
				repository.saveAndFlush(savedata);
				log.info( "Saved Details Is ---> " + json.toJson(savedata));
				
				// Section Insert
				EserviceSectionDetails secData = new EserviceSectionDetails(); 
				List<EserviceSectionDetails> sectionList =  eserSecRepo.findByRequestReferenceNoAndRiskIdAndProductIdOrderBySectionIdAsc(refNo, savedata.getRiskId(), savedata.getProductId().toString() );
				if (sectionList!=null && sectionList.size()>0 ) {
					eserSecRepo.deleteAll(sectionList);
				}
				mapper.map(savedata, secData);
				secData.setExchangeRate(savedata.getExchangeRate());
				secData.setCurrencyId(savedata.getCurrency());
				secData.setSectionName(section.getSectionName());
				secData.setRiskId(savedata.getRiskId());
				secData.setRequestReferenceNo(refNo);
				secData.setUserOpt("N");
				secData.setProductType(section.getMotorYn());
				secData.setRiskId(savedata.getRiskId());
				secData.setLocationId(Integer.valueOf(req.getLocationId()));
				secData.setCoverId(Integer.valueOf(req.getTravelCoverId()));

				String productTypeDesc = getListItem(req.getCompanyId(), req.getBranchCode(), "PRODUCT_CATEGORY", secData.getProductType());
				secData.setProductTypeDesc(productTypeDesc  );
				// Endoresment Chagnes
				if (!(req.getEndorsementType() == null || req.getEndorsementType() == 0))

				{

					secData.setLocationId(Integer.valueOf(req.getLocationId()));
					secData.setOriginalPolicyNo(req.getOriginalPolicyNo());
					secData.setEndorsementDate(req.getEndorsementDate());
					secData.setEndorsementRemarks(req.getEndorsementRemarks());
					secData.setEndorsementEffdate(req.getEndorsementEffdate());
					secData.setEndtPrevPolicyNo(req.getEndtPrevPolicyNo());
					secData.setEndtPrevQuoteNo(req.getEndtPrevQuoteNo());
					secData.setEndtCount(req.getEndtCount());
					secData.setEndtStatus(req.getEndtStatus());
					secData.setIsFinyn(req.getIsFinaceYn());
					secData.setEndtCategDesc(req.getEndtCategDesc());
					secData.setEndorsementType(req.getEndorsementType());
					secData.setEndorsementTypeDesc(req.getEndorsementTypeDesc());
					secData.setPolicyNo(req.getPolicyNo());
					
				}
				eserSecRepo.saveAndFlush(secData);

		
				// Delete Tavel Group Details
				long groupCount =  groupRepo.countByRequestReferenceNo(savedata.getRequestReferenceNo());
				if(groupCount > 0 ) {
					groupRepo.deleteByRequestReferenceNo(savedata.getRequestReferenceNo());
				}
				
				List<ProductGroupMaster> groupMaster = getProductGroupMasterDropdown(savedata.getProductId().toString(), savedata.getCompanyId() , savedata.getBranchCode());
				
				// Insert Tavel Group Details
				List<TravelGroupGetRes> travelGroupList = new ArrayList<TravelGroupGetRes>();
				
				for (TravelGroupInsertReq data : req.getGroupDetails()) {
					
						// No Cover For Family Kids Condition
					    
						EserviceTravelGroupDetails saveGroup = new EserviceTravelGroupDetails();
						// Save
						mapper.map(req, saveGroup);
						saveGroup.setTravelId( Integer.valueOf(data.getGroupId()));
						saveGroup.setRiskId( Integer.valueOf(data.getGroupId()));
						saveGroup.setRequestReferenceNo(savedata.getRequestReferenceNo());	
						saveGroup.setEntryDate(new Date());
						saveGroup.setSectionId(savedata.getSectionId()==null? null :Integer.valueOf(savedata.getSectionId()) );
						saveGroup.setSectionDesc(savedata.getSectionName());
						//saveGroup.setStatus("Y");
						saveGroup.setStatus(StringUtils.isBlank(req.getStatus()) ? "Y" : req.getStatus()) ;
						saveGroup.setStartt(groupMaster.size() > 0 ? groupMaster.stream().filter( o -> o.getGroupId().equals(Integer.valueOf(data.getGroupId())) ).collect(Collectors.toList()).get(0).getGroupFrom() : null ) ;
						saveGroup.setEnd(groupMaster.size() > 0 ? groupMaster.stream().filter( o -> o.getGroupId().equals(Integer.valueOf(data.getGroupId())) ).collect(Collectors.toList()).get(0).getGroupTo() : null);
						saveGroup.setGroupId(Integer.valueOf(data.getGroupId()));
						saveGroup.setGroupMembers(Integer.valueOf(data.getGroupMembers()));
						saveGroup.setGroupDesc(groupMaster.size() > 0 ? groupMaster.stream().filter( o -> o.getGroupId().equals(Integer.valueOf(data.getGroupId())) ).collect(Collectors.toList()).get(0).getBandDesc() : "" );
						data.setRiskId(saveGroup.getRiskId());
						
						if(!(req.getEndorsementType()==null || req.getEndorsementType()==0))
							
						 {
						  
							 saveGroup.setOriginalPolicyNo(req.getOriginalPolicyNo());
							 saveGroup.setEndorsementDate(req.getEndorsementDate());
							 saveGroup.setEndorsementRemarks(req.getEndorsementRemarks());
							 saveGroup.setEndorsementEffdate(req.getEndorsementEffdate());
							 saveGroup.setEndtPrevPolicyNo(req.getEndtPrevPolicyNo());
							 saveGroup.setEndtPrevQuoteNo(req.getEndtPrevQuoteNo());
							 saveGroup.setEndtCount(req.getEndtCount());
							 saveGroup.setEndtStatus(req.getEndtStatus());
							 saveGroup.setIsFinyn(req.getIsFinaceYn());
							 saveGroup.setEndtCategDesc(req.getEndtCategDesc());
							 saveGroup.setEndorsementType(req.getEndorsementType());
							 saveGroup.setEndorsementTypeDesc(req.getEndorsementTypeDesc());  
							 saveGroup.setPolicyNo(req.getPolicyNo());
						 }
						groupRepo.saveAndFlush(saveGroup);
						
						// Group Res
						if (  ! (req.getPlanTypeId().equalsIgnoreCase("3") &&  data.getGroupId().equalsIgnoreCase("1") ) ) {
							TravelGroupGetRes groupRes = new TravelGroupGetRes(); 
								groupRes.setGroupId(saveGroup.getGroupId().toString());
								groupRes.setGroupMembers(saveGroup.getGroupMembers().toString());
								groupRes.setTravelId(saveGroup.getTravelId().toString());
						
							
							travelGroupList.add(groupRes);
						}
					
					
				}
				
				res.setGroupDetails(travelGroupList);
				res.setSourceCountryDesc(sourceCountry);
				res.setDesctinationCountryDesc(DestinationCountry);
				/*
				// Insert Tracking
				{
					TrackingDetailsSaveReq trackingReq=new TrackingDetailsSaveReq();
					trackingReq.setProductId(req.getProductId());
					trackingReq.setStatus(req.getStatus());
					trackingReq.setBranchCode(req.getBranchCode());
					if( findData !=null ) {
						trackingReq.setQuoteNo(findData.getQuoteNo());
						}else {
							trackingReq.setQuoteNo("");
						}
					trackingReq.setRiskId(traId.toString());
					trackingReq.setCompanyId(req.getCompanyId());
					trackingReq.setPolicyNo(req.getPolicyNo());
					trackingReq.setOriginalPolicyNo(req.getOriginalPolicyNo());
					trackingReq.setCreatedby(req.getCreatedBy());
					trackingReq.setRequestReferenceNo(refNo);
					trackingReq.setRemarks(req.getEndorsementRemarks());
					trackService.insertTrackingDetails(trackingReq);
				}
				*/
			} catch (Exception e) {
				e.printStackTrace();
				log.info("Log Details" + e.getMessage());
				return null;
			}
			OneTimeTableRes otRes=null;
			try {
				// One Time Table Thread Call 
				OneTimeTableReq otReq = new OneTimeTableReq();
				EserviceTravelDetails traData = repository.findByRequestReferenceNoAndRiskId(savedata.getRequestReferenceNo() , savedata.getRiskId());
				otReq.setRequestReferenceNo(savedata.getRequestReferenceNo());
				otReq.setId(traData.getRiskId());
				otReq.setAgencyCode(StringUtils.isBlank(oaCode) ? traData.getBrokerCode() : oaCode );
				otReq.setBranchCode(traData.getBranchCode());
				otReq.setInsuranceId(traData.getCompanyId());
				otReq.setProductId((traData.getProductId()));
				otReq.setSectionId((traData.getSectionId()));
				otReq.setGroupDetails(req.getGroupDetails());
				otReq.setLocationId(Integer.valueOf(req.getLocationId()));
				
				otRes = otService.call_OT_Insert(otReq).get(0);
				res.setVdRefNo(otRes.getVdRefNo());
				res.setCdRefNo(otRes.getCdRefNo());
				res.setMsrefno(otRes.getMsRefNo());
				res.setInsuranceId(traData.getCompanyId());
				res.setSectionId(traData.getSectionId().toString());
				res.setProductId(traData.getProductId().toString());
				res.setCreatedBy(traData.getCreatedBy());
				res.setLocationId(req.getLocationId());	
			} catch (Exception e) {
				e.printStackTrace();
				e.printStackTrace();
				log.info("Log Details" + e.getMessage());
				return null;
			}
			return res;
		}
		
		public String createCustomerForShortQuote(EserviceTravelSaveReq req) {
			try {
				List<EserviceCustomerDetails> customers = custRepo
						.findByMobileCode1AndMobileNo1AndCreatedByAndCompanyIdAndProductIdOrderByUpdatedDateDesc(
								req.getMobileCode(), req.getMobileNo(), req.getCreatedBy(), req.getCompanyId(),
								Integer.parseInt(req.getProductId()));
				if (customers == null || customers.isEmpty()) {
					Date entryDate = new Date();
					String createdBy = req.getCreatedBy();
					// Random rand = new Random();
					// int random = rand.nextInt(90) + 10;
					Integer productId = Integer.valueOf(req.getProductId());
					// custRefNo = "Cust-" + generateCustRefNo() ; // idf.format(new Date()) +
					// random ;
					// Generate Seq
					SequenceGenerateReq generateSeqReq = new SequenceGenerateReq();
					generateSeqReq.setInsuranceId(req.getCompanyId());
					generateSeqReq.setProductId(req.getProductId());
					generateSeqReq.setType("1");
					generateSeqReq.setTypeDesc("CUSTOMER_REFERENCE_NO");
					String custRefNo = genSeqNoService.generateSeqCall(generateSeqReq);
					EserviceCustomerDetails saveData = new EserviceCustomerDetails();
					saveData.setProductId(productId);
					saveData.setEntryDate(entryDate);
					saveData.setCreatedBy(createdBy);
					saveData.setUpdatedDate(new Date());
					saveData.setUpdatedBy(req.getCreatedBy());
					saveData.setCustomerReferenceNo(custRefNo);
					saveData.setStatus("P");
					saveData.setLicenseIssuedDate(new Date());
					saveData.setLicenseDuration(20);

					saveData.setWhatsappCode(req.getMobileCode());
					saveData.setMobileCode1(req.getMobileCode());
					saveData.setMobileNo1(req.getMobileNo());
					saveData.setWhatsappNo(req.getMobileNo());
					saveData.setCompanyId(req.getCompanyId());
					saveData.setClientName(req.getCustomerName());
					saveData.setPolicyHolderTypeid("1");
					saveData.setPolicyHolderType("1");
					saveData.setIdType("1");
					saveData.setAge(20);
					saveData.setGender("M");
					saveData.setOccupation(StringUtils.isBlank(req.getOccupation())?"99999":req.getOccupation());
					saveData.setIsTaxExempted("N");
					saveData.setIdNumber("NA");
					saveData.setBranchCode(req.getBranchCode());
					saveData.setBrokerBranchCode(req.getBrokerBranchCode());
					ListItemValue policyHolderTypeIdL = getListItemTravel(req.getCompanyId(), req.getBranchCode(),
							"POLICY_HOLDER_ID_TYPE", saveData.getPolicyHolderTypeid());// listRepo.findByItemTypeAndItemCode("POLICY_HOLDER_ID_TYPE",
																						// req.getPolicyHolderTypeid());
					String policyHolderTypeId_en = policyHolderTypeIdL.getItemValue();
					String policyHolderTypeId_other = policyHolderTypeIdL.getItemValueLocal();
					saveData.setPolicyHolderTypeIdDesc(policyHolderTypeId_en);
					ListItemValue policyHolderTypeL = getListItemTravel("99999", req.getBranchCode(), "POLICY_HOLDER_TYPE",
							saveData.getPolicyHolderType());// listRepo.findByItemTypeAndItemCode("POLICY_HOLDER_TYPE",
															// req.getPolicyHolderType());
					String policyHolderType_en = policyHolderTypeL.getItemValue();
					String policyHolderType_other = policyHolderTypeL.getItemValueLocal();
					saveData.setPolicyHolderTypeDesc(policyHolderType_en);
					custRepo.save(saveData);
					return custRefNo;

				} else {
					return customers.get(0).getCustomerReferenceNo();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;

		}
		
		public synchronized ListItemValue getListItemTravel(String insuranceId, String branchCode, String itemType,
				String itemCode) {
			String itemDesc = "";
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
				Predicate b3 = cb.equal(c.get("branchCode"), ocpm1.get("branchCode"));
				Predicate b4 = cb.equal(c.get("companyId"), ocpm1.get("companyId"));
				Predicate a2 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
				effectiveDate.where(a1, a2, b3, b4);
				// Effective Date End Max Filter
				Subquery<Date> effectiveDate2 = query.subquery(Date.class);
				Root<ListItemValue> ocpm2 = effectiveDate2.from(ListItemValue.class);
				effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd").as(Date.class)));
				Predicate a3 = cb.equal(c.get("itemId"), ocpm2.get("itemId"));
				Predicate b1 = cb.equal(c.get("branchCode"), ocpm2.get("branchCode"));
				Predicate b2 = cb.equal(c.get("companyId"), ocpm2.get("companyId"));
				Predicate a4 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
				effectiveDate2.where(a3, a4, b1, b2);

				// Where
				Predicate n1 = cb.equal(c.get("status"), "Y");
				Predicate n12 = cb.equal(c.get("status"), "R");
				Predicate n13 = cb.or(n1, n12);
				Predicate n2 = cb.equal(c.get("effectiveDateStart"), effectiveDate);
				Predicate n3 = cb.equal(c.get("effectiveDateEnd"), effectiveDate2);
				Predicate n4 = cb.equal(c.get("companyId"), insuranceId);
				Predicate n5 = cb.equal(c.get("companyId"), "99999");
				Predicate n6 = cb.equal(c.get("branchCode"), branchCode);
				Predicate n7 = cb.equal(c.get("branchCode"), "99999");
				Predicate n8 = cb.or(n4, n5);
				Predicate n9 = cb.or(n6, n7);
				Predicate n10 = cb.equal(c.get("itemType"), itemType);
				Predicate n11 = cb.equal(c.get("itemCode"), itemCode);

				if (itemType.equalsIgnoreCase("PRODUCT_SHORT_CODE") || itemType.equalsIgnoreCase("PRODUCT_CATEGORY")) // not
																														// company
																														// based
					query.where(n13, n2, n3, n8, n9, n10, n11).orderBy(orderList);
				else
					query.where(n13, n2, n3, n4, n9, n10, n11).orderBy(orderList);

				// Get Result
				TypedQuery<ListItemValue> result = em.createQuery(query);
				list = result.getResultList();

				// itemDesc = list.size() > 0 ? list.get(0).getItemValue() : "" ;

				if (list != null && list.size() > 0) {
					return list.get(0);
				} else {
					return new ListItemValue();
				}

				// return new ListItemValue();
			} catch (Exception e) {
				e.printStackTrace();
				log.info("Exception is ---> " + e.getMessage());
				return null;
			}
			// return itemDesc ;
		}

	public BranchMaster getBranchMasterRes(String companyId , String branchCode ) {
		BranchMaster branchRes = new BranchMaster(); 
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
			
			// Criteria
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<BranchMaster> query = cb.createQuery(BranchMaster.class);
			List<BranchMaster> list = new ArrayList<BranchMaster>();
			
			// Find All
			Root<BranchMaster>    c = query.from(BranchMaster.class);		
			
			// Select
			query.select(c );
			
		
			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(c.get("branchName")));
			
			// Effective Date Max Filter
			Subquery<Date> effectiveDate = query.subquery(Date.class);
			Root<BranchMaster> ocpm1 = effectiveDate.from(BranchMaster.class);
			effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart").as(Date.class)));
			Predicate a1 = cb.equal(c.get("branchCode"),ocpm1.get("branchCode") );
			Predicate a2 = cb.equal(c.get("companyId"),ocpm1.get("companyId") );
			Predicate a3 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
			effectiveDate.where(a1,a2,a3);
			
			// Effective Date Max Filter
			Subquery<Date> effectiveDate2 = query.subquery(Date.class);
			Root<BranchMaster> ocpm2 = effectiveDate2.from(BranchMaster.class);
			effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd").as(Date.class)));
			Predicate a4 = cb.equal(c.get("branchCode"),ocpm2.get("branchCode") );
			Predicate a5 = cb.equal(c.get("companyId"),ocpm2.get("companyId") );
			Predicate a6 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
			effectiveDate2.where(a4,a5,a6);
			
		    // Where	
			Predicate n1 = cb.equal(c.get("status"), "Y");
			Predicate n2 = cb.equal(c.get("effectiveDateStart"), effectiveDate);
			Predicate n3 = cb.equal(c.get("effectiveDateEnd"), effectiveDate2);
			Predicate n4 = cb.equal(c.get("companyId"), companyId);
			Predicate n5 = cb.equal(c.get("branchCode"), branchCode);
			
			query.where(n1,n2,n3,n4,n5).orderBy(orderList);
			
			// Get Result
			TypedQuery<BranchMaster> result = em.createQuery(query);			
			list =  result.getResultList();  
			branchRes =  list.get(0);
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			return null;
		}
		return branchRes;
	}
		
		public BranchMaster getCompanyBranch( String insuranceId , String branchCode) {
			BranchMaster branchData = new BranchMaster();
			DozerBeanMapper dozerMapper = new DozerBeanMapper();
			SimpleDateFormat idf = new SimpleDateFormat("yyMMddhhssmmss");
			try {
				Calendar cal = new GregorianCalendar();
				Date today = new Date();
				cal.setTime(new Date() );  
				cal.setTime(today);
				cal.set(Calendar.HOUR_OF_DAY, 23);
				cal.set(Calendar.MINUTE, 1);
				today   = cal.getTime();
				cal.set(Calendar.HOUR_OF_DAY, 1);
				cal.set(Calendar.MINUTE, 1);
				Date todayEnd   = cal.getTime();
				
				// Login Data
				// Criteria
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<BranchMaster> query = cb.createQuery(BranchMaster.class);
				List<BranchMaster> branchlist = new ArrayList<BranchMaster>();
				
				// Find All
				Root<BranchMaster>    c = query.from(BranchMaster.class);		
				
				// Select
				query.select(c );
				
			
				// Order By
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.desc(c.get("branchCode")));
				
				// Effective Date Max Filter
				Subquery<Date> effectiveDate = query.subquery(Date.class);
				Root<BranchMaster> ocpm1 = effectiveDate.from(BranchMaster.class);
				effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart").as(Date.class)));
				Predicate a1 = cb.equal(c.get("branchCode"),ocpm1.get("branchCode") );
				Predicate a2 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
				Predicate a3 = cb.equal(c.get("companyId"),ocpm1.get("companyId") );
				effectiveDate.where(a1,a2,a3);
				
				// Effective Date Max Filter
				Subquery<Date> effectiveDate2 = query.subquery(Date.class);
				Root<BranchMaster> ocpm2 = effectiveDate2.from(BranchMaster.class);
				effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd").as(Date.class)));
				Predicate a4 = cb.equal(c.get("branchCode"),ocpm2.get("branchCode") );
				Predicate a5 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
				Predicate a6 = cb.equal(c.get("companyId"),ocpm2.get("companyId") );
				effectiveDate2.where(a4,a5,a6);
				
				 // Where	
				Predicate n1 = cb.equal(c.get("status"), "Y");
				Predicate n2 = cb.equal(c.get("effectiveDateStart"), effectiveDate);
				Predicate n3 = cb.equal(c.get("effectiveDateEnd"), effectiveDate2);
				Predicate n5 =cb.equal(c.get("companyId"), insuranceId );
				Predicate n6 =cb.equal(c.get("branchCode"), branchCode);
				query.where(n1,n2,n3,n5,n6).orderBy(orderList);	
				
				// Get Result
				TypedQuery<BranchMaster> result = em.createQuery(query);			
				branchlist =  result.getResultList();
				
				branchData = branchlist.get(0);
				
			} catch (Exception e) {
				e.printStackTrace();
				log.info("Exception is --->" + e.getMessage());
				return null;
			}
			return branchData;
		}
		
		public String getInscompanyMasterDropdown(String companyId ) {
			String companyName = "" ;
			try {
				Date today  = new Date();
				Calendar cal = new GregorianCalendar(); 
				cal.setTime(today);
				cal.set(Calendar.HOUR_OF_DAY, 23);
				cal.set(Calendar.MINUTE, 1);
				today   = cal.getTime();
				cal.set(Calendar.HOUR_OF_DAY, 1);
				cal.set(Calendar.MINUTE, 1);
				Date todayEnd = cal.getTime();
				
				// Criteria
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<InsuranceCompanyMaster> query = cb.createQuery(InsuranceCompanyMaster.class);
				List<InsuranceCompanyMaster> list = new ArrayList<InsuranceCompanyMaster>();
				
				// Find All
				Root<InsuranceCompanyMaster>    c = query.from(InsuranceCompanyMaster.class);		
				
				// Select
				query.select(c );
				
			
				// Order By
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.asc(c.get("companyName")));
				
				// Effective Date Max Filter
				Subquery<Date> effectiveDate = query.subquery(Date.class);
				Root<InsuranceCompanyMaster> ocpm1 = effectiveDate.from(InsuranceCompanyMaster.class);
				effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart").as(Date.class)));
				jakarta.persistence.criteria.Predicate a1 = cb.equal(c.get("companyId"),ocpm1.get("companyId") );
				jakarta.persistence.criteria.Predicate a2 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
				effectiveDate.where(a1,a2);
				
				// Effective Date End
				Subquery<Date> effectiveDate2 = query.subquery(Date.class);
				Root<InsuranceCompanyMaster> ocpm2 = effectiveDate2.from(InsuranceCompanyMaster.class);
				effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd").as(Date.class)));
				jakarta.persistence.criteria.Predicate a3 = cb.equal(c.get("companyId"),ocpm2.get("companyId") );
				jakarta.persistence.criteria.Predicate a4 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
				effectiveDate2.where(a3,a4);
				
			    // Where	
				jakarta.persistence.criteria.Predicate n1 = cb.equal(c.get("status"), "Y");
				jakarta.persistence.criteria.Predicate n2 = cb.equal(c.get("effectiveDateStart"), effectiveDate);
				jakarta.persistence.criteria.Predicate n3 = cb.equal(c.get("effectiveDateEnd"), effectiveDate2);
				Predicate n4 = cb.equal(c.get("companyId"), companyId);
				
				query.where(n1,n2,n3,n4).orderBy(orderList);
		
				// Get Result
				TypedQuery<InsuranceCompanyMaster> result = em.createQuery(query);
				list = result.getResultList();
				companyName  = list.size()> 0 ? list.get(0).getCompanyName() : "";	
					
			} catch (Exception e) {
				e.printStackTrace();
				log.info("Exception is ---> " + e.getMessage());
				return null;
			}
			return companyName;
		}
		
		
		public String getCompanyProductMasterDropdown(String companyId , String productId) {
			String productName = "";
			try {
				Date today = new Date();
				Calendar cal = new GregorianCalendar();
				cal.setTime(today);
				cal.set(Calendar.HOUR_OF_DAY, 23);;
				cal.set(Calendar.MINUTE, 1);
				today = cal.getTime();
				cal.set(Calendar.HOUR_OF_DAY, 1);
				cal.set(Calendar.MINUTE, 1);
				Date todayEnd = cal.getTime();
				
				// Criteria
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<CompanyProductMaster> query=  cb.createQuery(CompanyProductMaster.class);
				List<CompanyProductMaster> list = new ArrayList<CompanyProductMaster>();
				// Find All
				Root<CompanyProductMaster> c = query.from(CompanyProductMaster.class);
				//Select
				query.select(c);
				// Order By
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.asc(c.get("productName")));
				
				// Effective Date Start Max Filter
				Subquery<Date> effectiveDate = query.subquery(Date.class);
				Root<CompanyProductMaster> ocpm1 = effectiveDate.from(CompanyProductMaster.class);
				effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart").as(Date.class)));
				Predicate a1 = cb.equal(c.get("productId"),ocpm1.get("productId"));
				Predicate a2 = cb.equal(c.get("companyId"),ocpm1.get("companyId"));
				Predicate a3 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
				effectiveDate.where(a1,a2,a3);
				// Effective Date End Max Filter
				Subquery<Date> effectiveDate2 = query.subquery(Date.class);
				Root<CompanyProductMaster> ocpm2 = effectiveDate2.from(CompanyProductMaster.class);
				effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd").as(Date.class)));
				Predicate a4 = cb.equal(c.get("productId"),ocpm2.get("productId"));
				Predicate a5 = cb.equal(c.get("companyId"),ocpm2.get("companyId"));
				Predicate a6 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
				effectiveDate2.where(a4,a5,a6);
				
				// Where
				Predicate n1 = cb.equal(c.get("status"),"Y");
				Predicate n2 = cb.equal(c.get("effectiveDateStart"),effectiveDate);
				Predicate n3 = cb.equal(c.get("effectiveDateEnd"),effectiveDate2);	
				Predicate n4 = cb.equal(c.get("companyId"),companyId);
				Predicate n5 = cb.equal(c.get("productId"),productId);
				query.where(n1,n2,n3,n4,n5).orderBy(orderList);
				// Get Result
				TypedQuery<CompanyProductMaster> result = em.createQuery(query);
				list = result.getResultList();
				productName  = list.size()> 0 ? list.get(0).getProductName() : "";	
			}
				catch(Exception e) {
					e.printStackTrace();
					log.info("Exception is --->"+e.getMessage());
					return null;
					}
				return productName;
			}
		
		
		public ProductSectionMaster getProductSectionDropdown(String companyId , String productId , String sectionId) {
			ProductSectionMaster section = new ProductSectionMaster();
			try {
				Date today  = new Date();
				Calendar cal = new GregorianCalendar(); 
				cal.setTime(today);cal.set(Calendar.HOUR_OF_DAY, 23);cal.set(Calendar.MINUTE, 1);
				today   = cal.getTime();
				cal.set(Calendar.HOUR_OF_DAY, 1);cal.set(Calendar.MINUTE, 1);
				Date todayEnd = cal.getTime();
				
				// Criteria
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<ProductSectionMaster> query = cb.createQuery(ProductSectionMaster.class);
				List<ProductSectionMaster> list = new ArrayList<ProductSectionMaster>();
				
				// Find All
				Root<ProductSectionMaster>    c = query.from(ProductSectionMaster.class);		
				
				// Select
				query.select(c );
				
			
				// Order By
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.asc(c.get("sectionName")));
				
				// Effective Date Max Filter
				Subquery<Date> effectiveDate = query.subquery(Date.class);
				Root<ProductSectionMaster> ocpm1 = effectiveDate.from(ProductSectionMaster.class);
				effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart").as(Date.class)));
				Predicate a1 = cb.equal(c.get("sectionId"),ocpm1.get("sectionId"));
				Predicate a2 = cb.equal(c.get("companyId"), ocpm1.get("companyId"));
				Predicate a3 = cb.equal(c.get("productId"), ocpm1.get("productId"));
				Predicate a4 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
				effectiveDate.where(a1,a2,a3,a4);
				
				// Effective Date End
				Subquery<Date> effectiveDate2 = query.subquery(Date.class);
				Root<ProductSectionMaster> ocpm2 = effectiveDate2.from(ProductSectionMaster.class);
				effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd").as(Date.class)));
				jakarta.persistence.criteria.Predicate a5 = cb.equal(c.get("sectionId"), ocpm2.get("sectionId"));
				Predicate a7 = cb.equal(c.get("companyId"), ocpm2.get("companyId") );
				Predicate a8 = cb.equal(c.get("productId"), ocpm2.get("productId") );
				
				jakarta.persistence.criteria.Predicate a6 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
				effectiveDate2.where(a5, a6,a7,a8);

			    // Where	
				jakarta.persistence.criteria.Predicate n1 = cb.equal(c.get("status"), "Y");
				jakarta.persistence.criteria.Predicate n2 = cb.equal(c.get("effectiveDateStart"), effectiveDate);
				jakarta.persistence.criteria.Predicate n3 = cb.equal(c.get("effectiveDateEnd"), effectiveDate2);
				jakarta.persistence.criteria.Predicate n4 = cb.equal(c.get("companyId"),companyId);
				jakarta.persistence.criteria.Predicate n5 = cb.equal(c.get("productId"), productId);
				Predicate n6 = cb.equal(c.get("sectionId"), sectionId);
				query.where(n1,n2,n3,n4,n5,n6).orderBy(orderList);
				
				// Get Result
				TypedQuery<ProductSectionMaster> result = em.createQuery(query);			
				list =  result.getResultList();  
				section = list.size()> 0 ? list.get(0) : null;	
			} catch (Exception e) {
				e.printStackTrace();
				log.info("Exception is ---> " + e.getMessage());
				return null;
			}
			return section;
		}
		
		public List<ProductGroupMaster> getProductGroupMasterDropdown(String productId , String companyId , String branchCode ) {
			List<ProductGroupMaster> list = new ArrayList<ProductGroupMaster>();
			try {
				Date today = new Date();
				Calendar cal = new GregorianCalendar();
				cal.setTime(today);
				today = cal.getTime();
				Date todayEnd = cal.getTime();
				
				// Criteria
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<ProductGroupMaster> query = cb.createQuery(ProductGroupMaster.class);
				
				// Find All
				Root<ProductGroupMaster> c = query.from(ProductGroupMaster.class);
				// Select
				query.select(c);
				// Order By
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.asc(c.get("branchCode")));

				// Effective Date Start Max Filter
				Subquery<Date> effectiveDate = query.subquery(Date.class);
				Root<ProductGroupMaster> ocpm1 = effectiveDate.from(ProductGroupMaster.class);
				effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart").as(Date.class)));
				Predicate a1 = cb.equal(c.get("groupId"), ocpm1.get("groupId"));
				Predicate a2 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
				Predicate a3 = cb.equal(c.get("companyId"), ocpm1.get("companyId"));
				Predicate a4 = cb.equal(c.get("branchCode"), ocpm1.get("branchCode"));
				Predicate a5 = cb.equal(c.get("productId"), ocpm1.get("productId"));
				effectiveDate.where(a1, a2, a3, a4, a5);
				// Effective Date End Max Filter
				Subquery<Date> effectiveDate2 = query.subquery(Date.class);
				Root<ProductGroupMaster> ocpm2 = effectiveDate2.from(ProductGroupMaster.class);
				effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd").as(Date.class)));
				Predicate a6 = cb.equal(c.get("groupId"), ocpm2.get("groupId"));
				Predicate a7 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
				Predicate a8 = cb.equal(c.get("companyId"), ocpm2.get("companyId"));
				Predicate a9 = cb.equal(c.get("branchCode"), ocpm2.get("branchCode"));
				Predicate a10 = cb.equal(c.get("productId"), ocpm2.get("productId"));

				effectiveDate2.where(a6, a7, a8, a9, a10);
				// Where
				Predicate n1 = cb.equal(c.get("status"), "Y");
				Predicate n2 = cb.equal(c.get("effectiveDateStart"), effectiveDate);
				Predicate n3 = cb.equal(c.get("effectiveDateEnd"), effectiveDate2);
				Predicate n4 = cb.equal(c.get("productId"), productId);
				Predicate n8 = cb.equal(c.get("companyId"), companyId);
				Predicate n5 = cb.equal(c.get("branchCode"),branchCode);
				Predicate n6 = cb.equal(c.get("branchCode"), "99999");
				Predicate n7 = cb.or(n5, n6);
				query.where(n1, n2, n3, n4, n7, n8).orderBy(orderList);
				// Get Result
				TypedQuery<ProductGroupMaster> result = em.createQuery(query);
				list = result.getResultList();
				list = list.stream().filter(distinctByKey(o -> Arrays.asList(o.getGroupId()))).collect(Collectors.toList());
				list.sort(Comparator.comparing(ProductGroupMaster::getGroupDesc));
				
			}catch(Exception e) {
					e.printStackTrace();
					log.info("Exception is --->"+e.getMessage());
					return null;
					}
				return list;
			}
		
		private static <T> java.util.function.Predicate<T> distinctByKey(java.util.function.Function<? super T, ?> keyExtractor) {
		    Map<Object, Boolean> seen = new ConcurrentHashMap<>();
		    return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
		}

		private List<BrokerCommissionDetails> getPolicyName(String companyId, String productId, String loginId,
				String agencyCode, String policyType) {
			// TODO Auto-generated method stub
			List<BrokerCommissionDetails> list = new ArrayList<BrokerCommissionDetails>();
			try {
				Date today = new Date();
				// Find Latest Record
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<BrokerCommissionDetails> query = cb.createQuery(BrokerCommissionDetails.class);

				// Find All
				Root<BrokerCommissionDetails> b = query.from(BrokerCommissionDetails.class);

				// Select
				query.select(b);

				// Effective Date Max Filter
				Subquery<Long> amendId = query.subquery(Long.class);
				Root<BrokerCommissionDetails> ocpm1 = amendId.from(BrokerCommissionDetails.class);
				amendId.select(cb.max(ocpm1.get("amendId")));
				Predicate a1 = cb.equal(ocpm1.get("id"), b.get("id"));
				Predicate a2 = cb.equal(ocpm1.get("companyId"), b.get("companyId"));
				Predicate a3 = cb.equal(ocpm1.get("productId"), b.get("productId"));
				Predicate a4 = cb.equal(ocpm1.get("policyType"), b.get("policyType"));
				Predicate a5 = cb.equal(ocpm1.get("loginId"), b.get("loginId"));
				Predicate a6 = cb.equal(ocpm1.get("agencyCode"), b.get("agencyCode"));

				amendId.where(a1, a2, a3, a4, a5, a6);

				Predicate n1 = cb.equal(b.get("amendId"), amendId);
				Predicate n2 = cb.equal(b.get("policyType"), policyType);
				Predicate n7 = cb.equal(b.get("policyType"), "99999");
				Predicate n8 = cb.or(n2, n7);
				Predicate n3 = cb.equal(b.get("companyId"), companyId);
				Predicate n4 = cb.equal(b.get("productId"), productId);
				Predicate n5 = cb.equal(b.get("loginId"), loginId);
				Predicate n6 = cb.equal(b.get("agencyCode"), agencyCode);

				query.where(n1, n8, n3, n4, n5, n6);

				// Get Result
				TypedQuery<BrokerCommissionDetails> result = em.createQuery(query);
				list = result.getResultList();

			} catch (Exception e) {
				e.printStackTrace();

			}
			return list;
		}
		
	public synchronized String getListItem(String insuranceId , String branchCode, String itemType, String itemCode) {
		String itemDesc = "" ;
		List<ListItemValue> list = new ArrayList<ListItemValue>();
		try {
			Date today = new Date();
			Calendar cal = new GregorianCalendar();
			cal.setTime(today);
			today = cal.getTime();
			Date todayEnd = cal.getTime();
			
			// Criteria
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<ListItemValue> query=  cb.createQuery(ListItemValue.class);
			// Find All
			Root<ListItemValue> c = query.from(ListItemValue.class);
			
			//Select
			query.select(c);
			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(c.get("branchCode")));
			
			
			// Effective Date Start Max Filter
			Subquery<Date> effectiveDate = query.subquery(Date.class);
			Root<ListItemValue> ocpm1 = effectiveDate.from(ListItemValue.class);
			effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart").as(Date.class)));
			Predicate a1 = cb.equal(c.get("itemId"),ocpm1.get("itemId"));
			Predicate a2 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
			Predicate b1= cb.equal(c.get("branchCode"),ocpm1.get("branchCode"));
			Predicate b2 = cb.equal(c.get("companyId"),ocpm1.get("companyId"));
			effectiveDate.where(a1,a2,b1,b2);
			
			// Effective Date End Max Filter
			Subquery<Date> effectiveDate2 = query.subquery(Date.class);
			Root<ListItemValue> ocpm2 = effectiveDate2.from(ListItemValue.class);
			effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd").as(Date.class)));
			Predicate a3 = cb.equal(c.get("itemId"),ocpm2.get("itemId"));
			Predicate a4 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
			Predicate b3= cb.equal(c.get("companyId"),ocpm2.get("companyId"));
			Predicate b4= cb.equal(c.get("branchCode"),ocpm2.get("branchCode"));
			effectiveDate2.where(a3,a4,b3,b4);
						
			// Where
			Predicate n1 = cb.equal(c.get("status"),"Y");
			Predicate n12 = cb.equal(c.get("status"),"R");
			Predicate n13 = cb.or(n1,n12);
			Predicate n2 = cb.equal(c.get("effectiveDateStart"),effectiveDate);
			Predicate n3 = cb.equal(c.get("effectiveDateEnd"),effectiveDate2);	
			Predicate n4 = cb.equal(c.get("companyId"), insuranceId);
			Predicate n5 = cb.equal(c.get("companyId"), "99999");
			Predicate n6 = cb.equal(c.get("branchCode"), branchCode);
			Predicate n7 = cb.equal(c.get("branchCode"), "99999");
			Predicate n8 = cb.or(n4,n5);
			Predicate n9 = cb.or(n6,n7);
			Predicate n10 = cb.equal(c.get("itemType"),itemType );
			Predicate n11 = cb.equal(c.get("itemCode"), itemCode);
			
			if(itemType.equalsIgnoreCase("PRODUCT_SHORT_CODE")|| itemType.equalsIgnoreCase("PRODUCT_CATEGORY") )          //not company based
				query.where(n13,n2,n3,n8,n9,n10,n11).orderBy(orderList);
			else
				query.where(n13,n2,n3,n4,n9,n10,n11).orderBy(orderList);
			
			
			// Get Result
			TypedQuery<ListItemValue> result = em.createQuery(query);
			list = result.getResultList();
			
			itemDesc = list.size() > 0 ? list.get(0).getItemValue() : "" ; 
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			return null;
		}
		return itemDesc ;
	}

	public String getCoverName(String companyId , String productId , String sectionId ) {
		String sectionName = "" ;
		try {
			Date today  = new Date();
			Calendar cal = new GregorianCalendar(); 
			cal.setTime(today);cal.set(Calendar.HOUR_OF_DAY, 23);cal.set(Calendar.MINUTE, 1);
			today   = cal.getTime();
			cal.set(Calendar.HOUR_OF_DAY, 1);cal.set(Calendar.MINUTE, 1);
			Date todayEnd = cal.getTime();
			
			// Criteria
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<ProductSectionMaster> query = cb.createQuery(ProductSectionMaster.class);
			List<ProductSectionMaster> list = new ArrayList<ProductSectionMaster>();
			
			// Find All
			Root<ProductSectionMaster>    c = query.from(ProductSectionMaster.class);		
			
			// Select
			query.select(c );
			
		
			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(c.get("sectionName")));
			
			// Effective Date Max Filter
			Subquery<Date> effectiveDate = query.subquery(Date.class);
			Root<ProductSectionMaster> ocpm1 = effectiveDate.from(ProductSectionMaster.class);
			effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart").as(Date.class)));
			Predicate a1 = cb.equal(c.get("sectionId"),ocpm1.get("sectionId") );
			Predicate a2 = cb.equal(c.get("companyId"), ocpm1.get("companyId") );
			Predicate a3 = cb.equal(c.get("productId"), ocpm1.get("productId") );
			Predicate a4 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
			effectiveDate.where(a1,a2,a3,a4);
			
			// Effective Date End
			Subquery<Date> effectiveDate2 = query.subquery(Date.class);
			Root<ProductSectionMaster> ocpm2 = effectiveDate2.from(ProductSectionMaster.class);
			effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd").as(Date.class)));
			jakarta.persistence.criteria.Predicate a5 = cb.equal(c.get("sectionId"), ocpm2.get("sectionId"));
			Predicate a7 = cb.equal(c.get("companyId"), ocpm2.get("companyId") );
			Predicate a8 = cb.equal(c.get("productId"), ocpm2.get("productId") );
			
			jakarta.persistence.criteria.Predicate a6 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
			effectiveDate2.where(a5, a6,a7,a8);

		    // Where	
			jakarta.persistence.criteria.Predicate n1 = cb.equal(c.get("status"), "Y");
			jakarta.persistence.criteria.Predicate n2 = cb.equal(c.get("effectiveDateStart"), effectiveDate);
			jakarta.persistence.criteria.Predicate n3 = cb.equal(c.get("effectiveDateEnd"), effectiveDate2);
			jakarta.persistence.criteria.Predicate n4 = cb.equal(c.get("companyId"),companyId);
			jakarta.persistence.criteria.Predicate n5 = cb.equal(c.get("productId"), productId);
			jakarta.persistence.criteria.Predicate n6 = cb.equal(c.get("sectionId"), sectionId);
			query.where(n1,n2,n3,n4,n5,n6).orderBy(orderList);
			
			// Get Result
			TypedQuery<ProductSectionMaster> result = em.createQuery(query);			
			list =  result.getResultList();  
			
			sectionName = list.size()> 0 ? list.get(0).getSectionName() : "" ; 
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			return null;
		}
		return sectionName;
	}
	/*
	    @Override
	    public boolean delete(long id) {
	        try {
	            repository.deleteById(id);
	            return true;

	        } catch (Exception ex) {
				log.error(ex);
	            return false;
	        }
	    }

	 */


		@Override
		public EserviceTravelGetRes getTravelDetails(EserviceTravelGetReq req) {
			EserviceTravelGetRes res = new EserviceTravelGetRes();
			DozerBeanMapper mapper = new DozerBeanMapper();
			try {
				EserviceTravelDetails data = repository.findByRequestReferenceNoAndRiskId( req.getRequestReferenceNo(), Integer.valueOf(req.getTravelId()));
				res = mapper.map(data, EserviceTravelGetRes.class);
				res.setQuoteNo(data.getQuoteNo()!=null?data.getQuoteNo() :"" );
				res.setCustomerId(data.getCustomerId()!=null ? data.getCustomerId() : "" );
				res.setTravelId(data.getRiskId().toString());
				res.setPlanTypeDesc(data.getPlanTypeDesc());
				res.setDesctinationCountryDesc(data.getDestinationCountryDesc());
				// Group Details
				List<TravelGroupGetRes> travelGroupList = new ArrayList<TravelGroupGetRes>();
				List<EserviceTravelGroupDetails> groupDatas = groupRepo.findByRequestReferenceNoOrderByGroupId(req.getRequestReferenceNo());
				for (EserviceTravelGroupDetails groupData :  groupDatas ) {
					TravelGroupGetRes  travelGroup = new TravelGroupGetRes();
					travelGroup.setGroupId(groupData.getGroupId().toString());
					travelGroup.setGroupMembers(groupData.getGroupMembers().toString());
					travelGroup.setTravelId(groupData.getRiskId().toString());
					travelGroup.setRiskId(groupData.getRiskId());
					travelGroupList.add(travelGroup);		
				}
				travelGroupList.sort(Comparator.comparing(TravelGroupGetRes :: getRiskId));
				res.setGroupDetails(travelGroupList);
			} catch (Exception e) {
				e.printStackTrace();
				log.info("Log Details" + e.getMessage());
				return null;
			}
			return res;
		}
		
		
		@Override
		public SuccessRes deleteTravelDetails(EserviceTravelDeleteReq req) {
			SuccessRes res = new SuccessRes();
			try {
				EserviceTravelDetails findData = repository.findByRequestReferenceNoAndRiskId(req.getRequestReferenceNo() , Integer.valueOf(req.getTravelId()));
				if(findData !=null ) {
					repository.delete(findData);
					res.setResponse("Successfully Updated");
					res.setSuccessId(req.getRequestReferenceNo());
					
				} else {
					res.setResponse("Data Not Found");
					res.setSuccessId(req.getRequestReferenceNo());
				}
				
				
			} catch (Exception e) {
				e.printStackTrace();
				log.info("Log Details" + e.getMessage());
				return null;
			}
			return res;
		}
		
		
		@Override
		public List<EserviceTravelGetRes> getallTravelDetails(EserviceTravelGetAllReq req) {
			List<EserviceTravelGetRes> reslist = new ArrayList<EserviceTravelGetRes>();
			DozerBeanMapper mapper = new DozerBeanMapper();
			try {
				List<EserviceTravelDetails> datas = repository.findByRequestReferenceNoOrderByRiskIdAsc(req.getRequestReferenceNo());
				for (EserviceTravelDetails data : datas) {
					EserviceTravelGetRes res = new EserviceTravelGetRes();
					res = mapper.map(data, EserviceTravelGetRes.class);
					res.setQuoteNo(data.getQuoteNo()!=null?data.getQuoteNo() :"" );
					res.setCustomerId(data.getCustomerId()!=null ? data.getCustomerId() : "" );
					res.setTravelId(data.getRiskId().toString());
					reslist.add(res);
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.info("Log Details" + e.getMessage());
				return null;
			}
			return reslist;
		}

		
		public String getCountryName(String countryId) {
			String countryName = "";
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
				CriteriaQuery<CountryMaster> query = cb.createQuery(CountryMaster.class);
				List<CountryMaster> list = new ArrayList<CountryMaster>();

				// Find All
				Root<CountryMaster> c = query.from(CountryMaster.class);

				// Select
				query.select(c);

				// Order By
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.asc(c.get("countryName")));

				// Effective Date Start Max Filter
				Subquery<Date> effectiveDate = query.subquery(Date.class);
				Root<CountryMaster> ocpm1 = effectiveDate.from(CountryMaster.class);
				effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart").as(Date.class)));
				Predicate a1 = cb.equal(c.get("countryId"), ocpm1.get("countryId"));
				Predicate a2 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
				effectiveDate.where(a1, a2);
				
				// Effective Date End Max Filter
				Subquery<Date> effectiveDate2 = query.subquery(Date.class);
				Root<CountryMaster> ocpm2 = effectiveDate2.from(CountryMaster.class);
				effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd").as(Date.class)));
				Predicate a3 = cb.equal(c.get("countryId"), ocpm2.get("countryId"));
				Predicate a4 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
				effectiveDate2.where(a3,a4);

				// Where
				Predicate n1 = cb.equal(c.get("status"), "Y");
				Predicate n2 = cb.lessThanOrEqualTo(c.get("effectiveDateStart"), today); // any start date <= today
				Predicate n3 = cb.greaterThanOrEqualTo(c.get("effectiveDateEnd"), todayEnd); // any end date >= today
				Predicate n4 = cb.equal(c.get("countryId"), countryId);
				
				query.where(n1, n2,n3,n4).orderBy(orderList);

				// Get Result
				TypedQuery<CountryMaster> result = em.createQuery(query);
				list = result.getResultList();

				countryName = list.get(0).getCountryShortCode();
				
			} catch (Exception e) {
				e.printStackTrace();
				log.info("Exception is ---> " + e.getMessage());
				return null;
			}
			return countryName;
		}
		
		public synchronized LoginMaster getPremiaBroker(String customerCode ,String subUserType , String insuranceId) {
			LoginMaster lg = new LoginMaster();
			try {
				
				// Criteria
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<LoginMaster> query=  cb.createQuery(LoginMaster.class);
				// Find All
				Root<LoginMaster> l = query.from(LoginMaster.class);
				Root<LoginUserInfo> lu = query.from(LoginUserInfo.class);
				
				//Select
				query.select(l);
				// Order By
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.desc(l.get("entryDate")));
			
				// Where
			//	Predicate n1 = cb.equal(l.get("status"),"Y");
				Predicate n2 = cb.equal(l.get("companyId"), insuranceId);
				Predicate n3 = cb.equal(l.get("userType"), "Broker");
				Predicate n4 = cb.equal(l.get("userType"), "User");
				Predicate n5 = cb.or(n3,n4);
				Predicate n6 = cb.equal(lu.get("customerCode"),customerCode );
				Predicate n7 = cb.equal(l.get("loginId"),lu.get("loginId") );
				Predicate n8 = cb.equal(cb.lower(l.get("subUserType")),subUserType.toLowerCase());
				query.where(n2,n5,n6,n7,n8).orderBy(orderList);
				
			
				// Get Result
				TypedQuery<LoginMaster> result = em.createQuery(query);
				List<LoginMaster> list = result.getResultList();
				
				lg = list.size() > 0 ? list.get(0) : null ; 
			} catch (Exception e) {
				e.printStackTrace();
				log.info("Exception is ---> " + e.getMessage());
				return null;
			}
			return lg ;
		}

}
