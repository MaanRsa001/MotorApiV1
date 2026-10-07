package com.maan.eway.common.service.impl;



import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maan.eway.bean.CountryMaster;
import com.maan.eway.bean.CurrencyMaster;
import com.maan.eway.bean.DocumentTransactionDetails;
import com.maan.eway.bean.EserviceTravelDetails;
import com.maan.eway.bean.EserviceTravelGroupDetails;
import com.maan.eway.bean.HomePositionMaster;
import com.maan.eway.bean.ListItemValue;
import com.maan.eway.bean.PolicyCoverData;
import com.maan.eway.bean.PolicyCoverDataIndividuals;
import com.maan.eway.bean.ProductGroupMaster;
import com.maan.eway.bean.StateMaster;
import com.maan.eway.bean.TravelPassengerDetails;
import com.maan.eway.bean.TravelPassengerHistory;
import com.maan.eway.common.req.AddRemovedPassengerReq;
import com.maan.eway.common.req.GroupFilterReq;
import com.maan.eway.common.req.OldDocumentsCopyReq;
import com.maan.eway.common.req.PassengerListSaveReq;
import com.maan.eway.common.req.PassengerSaveReq;
import com.maan.eway.common.req.TravelPassDetailsGetAllReq;
import com.maan.eway.common.req.TravelPassDetailsGetReq;
import com.maan.eway.common.req.TravelPassDetailsSaveListReq;
import com.maan.eway.common.req.TravelPassValidateReq;
import com.maan.eway.common.res.PremiumGroupDevideRes;
import com.maan.eway.common.res.TravelPassDetailsRes;
import com.maan.eway.common.res.TravelPassHistoryRes;
import com.maan.eway.common.service.DocumentCopyService;
import com.maan.eway.common.service.TravelPassengerDetailsService;
import com.maan.eway.error.Error;
import com.maan.eway.repository.DocumentTransactionDetailsRepository;
import com.maan.eway.repository.EserviceTravelDetailsRepository;
import com.maan.eway.repository.EserviceTravelGroupDetailsRepository;
import com.maan.eway.repository.HomePositionMasterRepository;
import com.maan.eway.repository.ListItemValueRepository;
import com.maan.eway.repository.PolicyCoverDataIndividualsRepository;
import com.maan.eway.repository.PolicyCoverDataRepository;
import com.maan.eway.repository.TravelPassengerDetailsRepository;
import com.maan.eway.repository.TravelPassengerHistoryRepository;
import com.maan.eway.res.SuccessRes;
import com.maan.eway.res.TravelIndividualPassRes;
import com.maan.eway.res.TravelPassCommonRes;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;


@Service 
@Transactional
public class TravelPassengerDetailsServiceImpl  implements TravelPassengerDetailsService{
	
	@Autowired
	private TravelPassengerDetailsRepository repo;
	
	 
	@Autowired
	private DocumentTransactionDetailsRepository docTransRepo;
	@Autowired
	private TravelPassengerHistoryRepository historyRepo;
	@Autowired
	private TravelPassengerHistoryRepository traPassHisRepo  ;
	
	private Logger log=LogManager.getLogger(TravelPassengerDetailsServiceImpl.class);

	@Autowired
	private ListItemValueRepository listRepo;
	
	@Autowired
	private HomePositionMasterRepository homeRepo;
	
	@Autowired
	private DocumentCopyService  docService ;
	
	@Autowired
	private PolicyCoverDataRepository coverRepo ;
	
	@Autowired
	private PolicyCoverDataIndividualsRepository indiCoverRepo ;
	
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private EserviceTravelGroupDetailsRepository groupRepo ; 

	@Autowired
	private EserviceTravelDetailsRepository eserTraRepo ;
	
	@Override
	public List<Error> validatepassdetails(List<TravelPassDetailsSaveListReq> reqList) {
		List<Error> errors = new ArrayList<Error>();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			List<String> passNo = new ArrayList<String>();
			List<String> relationId = new ArrayList<String>();
			Long row = 0L ; 
			int count=0;
			String quoteNo = ""; 
			if ( reqList.size() <= 0 ) {
				errors.add(new Error("05", "PassngerList", "Please Enter Atleast One Passenger Details "));
				
			} else if (StringUtils.isBlank(reqList.get(0).getQuoteNo())   ) {
				errors.add(new Error("05", "PassngerList", "Please Enter QuoteNo"));
			} else {
				quoteNo = reqList.get(0).getQuoteNo() ;
			}
			List<TravelPassengerDetails> passDetails = repo.findByQuoteNoOrderByPassengerIdAsc(quoteNo);
			String insuranceId = passDetails.get(0).getCompanyId() ;
			String branchCode = passDetails.get(0).getBranchCode() ;
			Integer productId = passDetails.get(0).getProductId() ;
			
			List<ProductGroupMaster>  groupMaster =  getProductGroupMasterDropdown(insuranceId , branchCode , productId );
			List<ListItemValue> relations  = getListItem(insuranceId , branchCode , "RELATION_TYPE");
			
			for(TravelPassDetailsSaveListReq data: reqList ) {
				row = row + 1 ;
//				TravelPassengerDetails filterPassengerDetails =  passDetails.stream().filter( o -> o.getPassengerId().equals(Integer.valueOf(data.getPassengerId()))).collect(Collectors.toList()).get(0);
				
//			
//				if (StringUtils.isBlank(data.getPassengerId())) {
//					errors.add(new Error("02", "PassengerId", "Please Select PassengerId "+ row));
//				}else if (data.getPassengerId().length() > 20) {
//					errors.add(new Error("02", "PassengerId", "Please Enter PassengerId within 20 Characters "+ row));
//				}

				if (StringUtils.isBlank(data.getPassengerFirstName())) {
					errors.add(new Error("05", "PassengerFirstName", "Please Enter Passenger Firs tName "+ row));
				} else if (data.getPassengerFirstName().length() > 120) {
					errors.add(new Error("05", "PassengerFirstName", "Please Enter Passenger First Name within 120 Characters "+ row));
				}
			
				if (StringUtils.isBlank(data.getPassengerLastName())) {
					errors.add(new Error("06", "PassengerLastName", "Please Enter Passenger Last Name "+ row));
				} else if (data.getPassengerLastName().length() > 120) {
					errors.add(new Error("06", "PassengerLastName", "Please Enter Passenger Last Name within 120 Characters "+ row));
				}
				Calendar cal = new GregorianCalendar();
				Date today = new Date();
				
				//Age Validation
				
				if (data.getDob()==null) {
					errors.add(new Error("07", "Dob", "Please Enter Dob "+ row));
				} else if ((data.getDob().after(today))) {
					errors.add(new Error("07", "Dob", "Please Enter Dob as Past Date "+ row));
				} 
				ProductGroupMaster filterGroupMaster =  groupMaster.stream().filter( o -> o.getGroupId().equals(Integer.valueOf(data.getGroupId()))).collect(Collectors.toList()).get(0);
				Long fromAge = Long.valueOf(filterGroupMaster.getGroupFrom()) ;
			    Long toAge = Long.valueOf(filterGroupMaster.getGroupTo()) ;
			    String bandDesc = filterGroupMaster.getBandDesc();
			    
				// Age Band Validation
				String date =  sdf.format(data.getDob()) ; 
				Date birthDate = sdf.parse(date);
			    Long ageInMillis = System.currentTimeMillis() - birthDate.getTime();
			    Long years = ageInMillis /(365 * 24*60*60*1000l);
			    Long leftover = ageInMillis %(365 * 24*60*60*1000l);
			    Long days = leftover/(24*60*60*1000l);
			    System.out.println(years);
			    System.out.println(days);
			    
				if(filterGroupMaster.getGroupId().equals(1) && StringUtils.isNotBlank(data.getRelationId()  ) ) {
					String relation = relations.stream().filter(o -> o.getItemCode().equalsIgnoreCase(data.getRelationId()  )).collect(Collectors.toList()).get(0).getItemValue();
					 if("1".equalsIgnoreCase(data.getRelationId()) || "2".equalsIgnoreCase(data.getRelationId()) || "3".equalsIgnoreCase(data.getRelationId())
							 || "4".equalsIgnoreCase(data.getRelationId()) || "9".equalsIgnoreCase(data.getRelationId()) || "10".equalsIgnoreCase(data.getRelationId())) {
						 errors.add(new Error("01", "Relation", relation + " Relation Type " + " Not Allowed For Kids Section In Row No: " +  row )); 
					 }
					
				}
				
				if(filterGroupMaster.getGroupId().equals(1) ) {
					// Age 3 - 18 Restrict
					 if( 90 > days && years > toAge    ) {
						 errors.add(new Error("01", "Dob", "Date Of Birth Should be " + bandDesc +" Years Allowed in Passenger No : " +  row )); 
					 }
				 
				} else {
					// Other Age Restrict	
					 if( fromAge > years && years < toAge ) {
						 errors.add(new Error("01", "Dob", "Date Of Birth Should be  " + bandDesc + "  Years Only Allowed in Passenger No : " + row)); 
					 }
				} 
				
		
				
				//Gender Validation
				if (StringUtils.isBlank(data.getGenderId())) {
					errors.add(new Error("08", "GenderId", "Please Select GenderId "+ row));
				}

				//RelationShip validation
				
				if (StringUtils.isBlank(data.getRelationId())) {
					errors.add(new Error("10", "Relation Id", "Please Select Relation "+ row));
				}else {
					
					String relation = relations.stream().filter(o -> o.getItemCode().equalsIgnoreCase(data.getRelationId()  )).collect(Collectors.toList()).get(0).getItemValue();
					if("SELF".equalsIgnoreCase(relation)) {
						count++;
					} 
					if(count>1 && ! relation.equalsIgnoreCase("Others") ) {
						errors.add(new Error("10", "Relation Id", "Duplicate Relation  Available in Row No : " + row ));
					} 
				}
				if (StringUtils.isBlank(data.getNationality())) {
					errors.add(new Error("11", "Nationality", "Please Enter Nationality "+ row));
				}
				//Passport No validation
				
				if (StringUtils.isBlank(data.getPassportNo())) {
					errors.add(new Error("12", "PassportNo", "Please Enter PassportNo "+ row));
				} else if (data.getPassportNo().length() > 20) {
					errors.add(new Error("12", "PassportNo", "Please Enter PassportNo within 20 Characters "+ row));
				} else {
					List<String> passPortNo =  passNo.stream().filter( o -> o.equalsIgnoreCase(data.getPassportNo()) ).collect(Collectors.toList()); 
					if(passPortNo.size()>0  ) {
						errors.add(new Error("12", "PassportNo", "Duplicate PassportNo Available in Row No : " + row ));
					} else {
						passNo.add(data.getPassportNo());
					}
				}
				

			}
			
			
			List<EserviceTravelGroupDetails> groupDetails = groupRepo.findByQuoteNoOrderByGroupIdAsc(quoteNo);
			// Group Validation 
			for ( EserviceTravelGroupDetails group : groupDetails ) {
				List<TravelPassDetailsSaveListReq> filterList = reqList.stream().filter( o -> o.getGroupId()!=null && Integer.valueOf(o.getGroupId()).equals(group.getGroupId())  )
						.collect(Collectors.toList()) ;
				if( group.getGroupMembers() < filterList.size() ) {
					errors.add(new Error("12", "Group", "Group : " + group.getGroupDesc() + " Number Of Passengers Greater Than " + group.getGroupMembers() + " Passengers Not Allowed" ));
				} else if( group.getGroupMembers() > filterList.size() ) {
					errors.add(new Error("12", "Group", "Group : " + group.getGroupDesc() + " Number Of Passengers Lesser Than  " + group.getGroupMembers() + " Passengers Not Allowed" ));
				}
				
			}
			
			List<TravelPassDetailsSaveListReq> filterSelf1 = reqList.stream().filter( o -> o.getRelationId()!=null && Integer.valueOf(o.getRelationId()).equals(9)  )
					.collect(Collectors.toList()) ;
			List<TravelPassDetailsSaveListReq> filterSelf2 = reqList.stream().filter( o -> o.getRelationId()!=null && Integer.valueOf(o.getRelationId()).equals(10)  )
					.collect(Collectors.toList()) ;
			if(filterSelf1.size()<=0 && filterSelf2.size()<=0 ) {
				errors.add(new Error("12", "SelfRelation", " Self Relation is missing in Passenger Details" ));
			} 
			
		} catch(Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			errors.add(new Error("01","Common Error",e.getMessage() ));
		}
		return errors;
	}

	@Override
	public SuccessRes savepassdetails(List<TravelPassDetailsSaveListReq> reqList) {
		SuccessRes res = new SuccessRes();
		DozerBeanMapper dozerMapper = new DozerBeanMapper();
		try {
			
			String quoteNo = reqList.get(0).getQuoteNo();
			List<TravelPassengerDetails> passDetails = repo.findByQuoteNoOrderByPassengerIdAsc(quoteNo);
			String insuranceId = passDetails.get(0).getCompanyId() ;
			String productId = passDetails.get(0).getProductId().toString();
			String sectionId = passDetails.get(0).getSectionId().toString();
			String branchCode = passDetails.get(0).getBranchCode() ;	
			List<ListItemValue> genders  = getListItem(insuranceId , branchCode , "GENDER");
			List<ListItemValue> relations  = getListItem(insuranceId , branchCode , "RELATION_TYPE");
	//		List<ListItemValue> nametitles  = getListItem(insuranceId , branchCode , "TITLE");
			
			Map<Integer, List<TravelPassDetailsSaveListReq>> groupByGroupId = reqList.stream().filter( o -> o.getGroupId() !=null  ).collect( Collectors.groupingBy(TravelPassDetailsSaveListReq :: getGroupId )) ;
					
			List<TravelPassengerDetails>  updateDatas = new ArrayList<TravelPassengerDetails>();
			List<TravelPassengerHistory>  passnegerHistories = new ArrayList<TravelPassengerHistory>();
			for (Integer group :  groupByGroupId.keySet() ) {
				
				 List<TravelPassDetailsSaveListReq> filterReqPass = groupByGroupId.get(group);
				 List<TravelPassengerDetails> filterPolicyPass = passDetails.stream().filter( o -> o.getGroupId().equals(group ) ).collect(Collectors.toList());
				 
				 Integer passengerCount  = 0;
				 for(TravelPassengerDetails updateData : filterPolicyPass) {
					 
					 
					TravelPassDetailsSaveListReq data = filterReqPass.get(passengerCount);
					passengerCount = passengerCount + 1;
					String gender = genders.stream().filter(o -> o.getItemCode().equalsIgnoreCase(data.getGenderId()  )).collect(Collectors.toList()).get(0).getItemValue();
					String relation = relations.stream().filter(o -> o.getItemCode().equalsIgnoreCase(data.getRelationId()  )).collect(Collectors.toList()).get(0).getItemValue();
				
					updateData.setPassengerFirstName(data.getPassengerFirstName());
					updateData.setPassengerLastName(data.getPassengerLastName());
					updateData.setPassengerName(data.getPassengerFirstName() + " " + data.getPassengerLastName() );
					updateData.setDob(data.getDob());
					updateData.setGenderId(data.getGenderId());
					updateData.setGenderDesc(gender );
					updateData.setAge(Integer.valueOf(data.getAge()));
					updateData.setRelationId(Integer.valueOf(data.getRelationId()));
					updateData.setRelationDesc(relation);
					updateData.setNationality(data.getNationality());
					updateData.setPassportNo(data.getPassportNo());
					updateData.setCivilId(data.getCivilId());
					updateData.setUpdatedBy(data.getCreatedBy());
					updateData.setUpdatedDate(new Date());
					List<Tuple> countryName = getCountryName(data.getNationality());
					
					if(countryName!=null && countryName.size()>0  ) {
						updateData.setNationalityDesc(countryName.get(0).get("countryName") == null ? "" : countryName.get(0).get("countryName").toString());
					}
					updateDatas.add(updateData);
						
					// Save New 
					TravelPassengerHistory traHistorySave = new TravelPassengerHistory(); 
					dozerMapper.map(updateData, traHistorySave);
					traHistorySave.setEntryDate(updateData.getEntryDate());
					traHistorySave.setPassengerFirstName(data.getPassengerFirstName());
					traHistorySave.setPassengerLastName(data.getPassengerLastName());
					traHistorySave.setPassengerName(data.getPassengerFirstName() + " " + data.getPassengerLastName() );
					traHistorySave.setDob(data.getDob());
					traHistorySave.setGenderId(data.getGenderId());
					traHistorySave.setGenderDesc(gender );
					traHistorySave.setAge(Integer.valueOf(data.getAge()));
					traHistorySave.setRelationId(Integer.valueOf(data.getRelationId()));
					traHistorySave.setRelationDesc(relation);;
					traHistorySave.setNationality(data.getNationality());
					traHistorySave.setPassportNo(data.getPassportNo());
					traHistorySave.setCivilId(data.getCivilId());
					traHistorySave.setUpdatedBy(data.getCreatedBy());
					traHistorySave.setUpdatedDate(new Date());
					
					if(countryName!=null && countryName.size()>0  ) {
						traHistorySave.setNationalityDesc(countryName.get(0).get("countryName") == null ? "" : countryName.get(0).get("countryName").toString());
					}	
					passnegerHistories.add(traHistorySave);
				 }
			}
			
			// Save All Passengers
			repo.saveAllAndFlush(updateDatas);
			
			// Save All Passenger History
			traPassHisRepo.deleteByQuoteNo(quoteNo);
			traPassHisRepo.saveAllAndFlush(passnegerHistories);
			
			// Copy Old Doc
			OldDocumentsCopyReq oldDocCopyReq = new OldDocumentsCopyReq();
			oldDocCopyReq.setInsuranceId(insuranceId);
			oldDocCopyReq.setProductId(productId);
			oldDocCopyReq.setSectionId(sectionId);
			oldDocCopyReq.setQuoteNo(quoteNo);
			docService.copyOldDocumentsToNewQuote(oldDocCopyReq);
			
			
			//uploaded Documents delete
			
			List<TravelPassengerDetails> pass = repo.findByQuoteNoAndProductIdAndCompanyId(quoteNo,Integer.valueOf(productId),insuranceId);
			List<DocumentTransactionDetails> doc1 = docTransRepo.findByQuoteNoAndProductIdAndCompanyId(quoteNo, Integer.valueOf(productId),insuranceId);
			
			if(pass.size()>0) {
				if(doc1.size()>0) {
					
					List<DocumentTransactionDetails> filterDoc = doc1.stream()
							.filter( d -> pass.stream().map( TravelPassengerDetails :: getSectionId).anyMatch(  
									e ->  e.equals( d.getSectionId() )))
							.filter( d -> pass.stream().map( TravelPassengerDetails :: getPassportNo).anyMatch(  
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
			
		
			
			res.setSuccessId("1");
			res.setResponse("Updated Successfully");
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Log Details"+e.getMessage());
			return null;
		}
		return res;
	}

	public List<Tuple> getCountryName(String countryId) {
		List<Tuple> list = new ArrayList<Tuple>();
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
			// Find Latest Record
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class);

			Root<CountryMaster> c = query.from(CountryMaster.class);
			
			// State Effective Date Max Filter
			Subquery<Date> effectiveDate1 = query.subquery(Date.class);
			Root<CountryMaster> ocpm1 = effectiveDate1.from(CountryMaster.class);
			effectiveDate1.select(cb.greatest(ocpm1.get("effectiveDateStart").as(Date.class)));
			Predicate seff2 = cb.equal(ocpm1.get("countryId"), c.get("countryId"));
			Predicate seff4 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
			effectiveDate1.where( seff2, seff4);

			// State Name Max Filter
			// State Effective Date Max Filter
			Subquery<Date> effectiveDate2 = query.subquery(Date.class);
			Root<StateMaster> ocpm2 = effectiveDate2.from(StateMaster.class);
			effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd").as(Date.class)));
			Predicate seff6 = cb.equal(ocpm2.get("countryId"), c.get("countryId"));
			Predicate seff7 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
			effectiveDate2.where( seff6, seff7);

			// Select
			query.multiselect( c.get("countryName").alias("countryName") );
			
			Predicate s2 = cb.equal(c.get("countryId"), countryId);
			Predicate s3 = cb.equal(c.get("status"), "Y");
			Predicate s4 = cb.equal(c.get("effectiveDateStart"), effectiveDate1);
			Predicate s5 = cb.equal(c.get("effectiveDateEnd"), effectiveDate2);
			query.where( s2, s3, s4,s5);
			
			// Get Result
			TypedQuery<Tuple> result = em.createQuery(query);
			list = result.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			return null;
		}
		return list;
	}
	
	public List<CountryMaster> getCountryList(String companyId) {
		List<CountryMaster> list = new ArrayList<CountryMaster>();
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
			// Find Latest Record
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<CountryMaster> query = cb.createQuery(CountryMaster.class);

			Root<CountryMaster> c = query.from(CountryMaster.class);
			
			// State Effective Date Max Filter
			Subquery<Date> effectiveDate1 = query.subquery(Date.class);
			Root<CountryMaster> ocpm1 = effectiveDate1.from(CountryMaster.class);
			effectiveDate1.select(cb.greatest(ocpm1.get("effectiveDateStart").as(Date.class)));
			Predicate seff2 = cb.equal(ocpm1.get("countryId"), c.get("countryId"));
			Predicate seff3 = cb.equal(ocpm1.get("companyId"), c.get("companyId"));
			Predicate seff4 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
			effectiveDate1.where( seff2, seff3 , seff4);

			// State Name Max Filter
			// State Effective Date Max Filter
			Subquery<Date> effectiveDate2 = query.subquery(Date.class);
			Root<CountryMaster> ocpm2 = effectiveDate2.from(CountryMaster.class);
			effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd").as(Date.class)));
			Predicate seff6 = cb.equal(ocpm2.get("countryId"), c.get("countryId"));
			Predicate seff5 = cb.equal(ocpm2.get("companyId"), c.get("companyId"));
			Predicate seff7 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
			effectiveDate2.where(seff5, seff6, seff7);

			// Select
			query.select( c );
			
			Predicate s3 = cb.equal(c.get("status"), "Y");
			Predicate s4 = cb.equal(c.get("effectiveDateStart"), effectiveDate1);
			Predicate s5 = cb.equal(c.get("effectiveDateEnd"), effectiveDate2);
			Predicate s6 = cb.equal(c.get("companyId"), companyId);
			Predicate s7 = cb.equal(c.get("companyId"), "99999");
			Predicate s8 = cb.or(s6,s7);
			query.where(  s3, s4,s5, s8 );
			
			// Get Result
			TypedQuery<CountryMaster> result = em.createQuery(query);
			list = result.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			return null;
		}
		return list;
	}
	
	public synchronized List<ListItemValue> getListItem(String insuranceId , String branchCode , String itemType) {
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
			Predicate n2 = cb.equal(c.get("effectiveDateStart"),effectiveDate);
			Predicate n3 = cb.equal(c.get("effectiveDateEnd"),effectiveDate2);	
			Predicate n4 = cb.equal(c.get("companyId"), insuranceId);
		//	Predicate n5 = cb.equal(c.get("companyId"), "99999");
			Predicate n6 = cb.equal(c.get("branchCode"), branchCode);
			Predicate n7 = cb.equal(c.get("branchCode"), "99999");
		//	Predicate n8 = cb.or(n4,n5);
			Predicate n9 = cb.or(n6,n7);
			Predicate n10 = cb.equal(c.get("itemType"),itemType);
			query.where(n1,n2,n3,n4,n9,n10).orderBy(orderList);
			// Get Result
			TypedQuery<ListItemValue> result = em.createQuery(query);
			list = result.getResultList();
			
			list = list.stream().filter(distinctByKey(o -> Arrays.asList(o.getItemCode()))).collect(Collectors.toList());
			list.sort(Comparator.comparing(ListItemValue :: getItemValue));
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			return null;
		}
		return list ;
	}
	
	private static <T> java.util.function.Predicate<T> distinctByKey(java.util.function.Function<? super T, ?> keyExtractor) {
	    Map<Object, Boolean> seen = new ConcurrentHashMap<>();
	    return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}
	
	@Override
	public TravelPassDetailsRes getpassdetails(TravelPassDetailsGetReq req) {
	TravelPassDetailsRes res = new TravelPassDetailsRes();
	DozerBeanMapper mapper = new DozerBeanMapper();
	try {

		List<TravelPassengerDetails> data = repo.findByQuoteNoAndPassengerId(req.getQuoteNo(),Integer.valueOf( req.getPassengerId())); 
		if(data!=null) {
			res =mapper.map(data.get(0),TravelPassDetailsRes.class);
		}
	}
	catch(Exception e) {
		e.printStackTrace();
		log.info("Log Details"+e.getMessage());
		return null;
	}
	return res;
	}

	@Override
	public SuccessRes deletepassdetails(TravelPassDetailsGetReq req) {
		SuccessRes res = new SuccessRes();
	try {

			List<TravelPassengerDetails> datas = repo.findByQuoteNoAndPassengerId(req.getQuoteNo(),Integer.valueOf(req.getPassengerId())); 
			if(datas!=null && datas.size() > 0 ) {
				datas.forEach( data -> {
					data.setStatus("D");
				});					
			}
			repo.saveAll(datas);
			res.setResponse("Deleted Successfully");
			res.setSuccessId(req.getPassengerId());
				
		}
		catch(Exception e) {
			e.printStackTrace();
			log.info("Log Details"+e.getMessage());
			return null;
		}
		return res;
		}

	@Override
	public List<TravelPassDetailsRes> getallpassdetails(TravelPassDetailsGetAllReq req) {
		List<TravelPassDetailsRes> resList = new ArrayList<TravelPassDetailsRes>();
		DozerBeanMapper mapper = new DozerBeanMapper();
		try {
			List<TravelPassengerDetails> datas = repo.findByQuoteNoOrderByPassengerIdAsc(req.getQuoteNo());
			
			List<TravelPassengerDetails> adultList = datas.stream().filter( o -> o.getGroupId().equals(2) ).collect(Collectors.toList());
			for(TravelPassengerDetails data : adultList) {
				TravelPassDetailsRes res = new TravelPassDetailsRes();
				res = mapper.map(data, TravelPassDetailsRes.class);
				resList.add(res);
			}
			List<TravelPassengerDetails> otherList =  datas.stream().filter( o -> ! o.getGroupId().equals(2) ).collect(Collectors.toList());
			for(TravelPassengerDetails data : otherList) {
				TravelPassDetailsRes res = new TravelPassDetailsRes();
				res = mapper.map(data, TravelPassDetailsRes.class);
				resList.add(res);
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
	public List<Error> validatepassListDetails(TravelPassValidateReq quoteReq) {
		List<Error> errors = new ArrayList<Error>();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			List<TravelPassengerDetails> passDetails = repo.findByQuoteNoOrderByPassengerIdAsc(quoteReq.getQuoteNo());
			List<ProductGroupMaster>  groupMaster =  getProductGroupMasterDropdown(passDetails.get(0).getCompanyId() , passDetails.get(0).getBranchCode() , passDetails.get(0).getProductId() );
			
			
			// Indivudal Validation
			for (TravelPassengerDetails pass :  passDetails ) {
				// Group Master
				ProductGroupMaster filterGroupMaster =  groupMaster.stream().filter( o -> o.getGroupId().equals(pass.getGroupId())    ).collect(Collectors.toList()).get(0);
				Long fromAge = Long.valueOf(filterGroupMaster.getGroupFrom()) ;
			    Long toAge = Long.valueOf(filterGroupMaster.getGroupTo()) ;
			    String bandDesc = filterGroupMaster.getBandDesc();
			    
				// Age Band Validation
				String date =  sdf.format(pass.getDob()) ; 
				Date birthDate = sdf.parse(date);
			    Long ageInMillis = System.currentTimeMillis() - birthDate.getTime();
			    Long years = ageInMillis /(365 * 24*60*60*1000l);
			    Long leftover = ageInMillis %(365 * 24*60*60*1000l);
			    Long days = leftover/(24*60*60*1000l);
			    System.out.println(years);
			    System.out.println(days);
				
				if(filterGroupMaster.getGroupId().equals(1) ) {
					// Age 3 - 18 Restrict
					 if( 90 > days && years > toAge    ) {
						 errors.add(new Error("01", "Dob", "Date Of Birth Should be " + bandDesc +" Years Allowed in Passenger No : " +  pass.getPassengerId() )); 
					 }
				 
				} else {
					// Other Age Restrict	
					 if( fromAge > years && years < toAge ) {
						 errors.add(new Error("01", "Dob", "Date Of Birth Should be  " + bandDesc + "  Years Only Allowed in Passenger No : " +  pass.getPassengerId() )); 
					 }
				} 
				
			}
				
			// Group Validation
			Map<Integer , List<TravelPassengerDetails>> groupById = 	passDetails.stream().collect( Collectors.groupingBy(TravelPassengerDetails :: getGroupId));
			for ( Integer id :  groupById.keySet()) {
				List<TravelPassengerDetails> groupDatas = groupById.get(id);
				ProductGroupMaster filterGroupMaster =  groupMaster.stream().filter( o -> o.getGroupId().equals(groupDatas.get(0).getGroupId())    ).collect(Collectors.toList()).get(0);
				String bandDesc = filterGroupMaster.getBandDesc();
				if(groupDatas.size() !=  groupDatas.get(0).getGroupCount()) {
					errors.add(new Error("01", "Group Count", "Group Count :" + groupDatas.get(0).getGroupCount() +  " Only Allowed In "+ bandDesc + " Age Group Band"  ));
				}
				
			}
					
					
			
		} catch(Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			errors.add(new Error("01","Common Error",e.getMessage() ));
		}
		return errors;
	}

	public List<ProductGroupMaster> getProductGroupMasterDropdown(String insId , String branchCode , Integer productId) {
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
			Predicate n4 = cb.equal(c.get("productId"),productId);
			Predicate n8 = cb.equal(c.get("companyId"), insId);
			Predicate n5 = cb.equal(c.get("branchCode"), branchCode);
			Predicate n6 = cb.equal(c.get("branchCode"), "99999");
			Predicate n7 = cb.or(n5, n6);
			query.where(n1, n2, n3, n4, n7, n8).orderBy(orderList);
			// Get Result
			TypedQuery<ProductGroupMaster> result = em.createQuery(query);
			list = result.getResultList();
			list = list.stream().filter(distinctByKey(o -> Arrays.asList(o.getGroupId()))).collect(Collectors.toList());
			list.sort(Comparator.comparing(ProductGroupMaster::getGroupDesc));
			
		}
			catch(Exception e) {
				e.printStackTrace();
				log.info("Exception is --->"+e.getMessage());
				return null;
				}
			return list;
		}
	
	@Override
	public TravelPassCommonRes getpassdetails(TravelPassValidateReq req) {
		TravelPassCommonRes travelRes = new TravelPassCommonRes();
		DozerBeanMapper dozerMapper = new DozerBeanMapper(); 
		try {
			List<TravelPassengerDetails> passList = repo.findByQuoteNoOrderByPassengerIdAsc(req.getQuoteNo()) ;
			dozerMapper.map(passList.get(0), travelRes ) ;
			
			List<TravelIndividualPassRes> travelPassList = new ArrayList<TravelIndividualPassRes>();
			
			for (TravelPassengerDetails pass :  passList ) {
				TravelIndividualPassRes  traPass = new TravelIndividualPassRes();
				dozerMapper.map(pass, traPass ) ;
				travelPassList.add(traPass);
			}
			
			// Total Premium
			HomePositionMaster homeData = homeRepo.findByQuoteNo(req.getQuoteNo());  
			travelRes.setActualPremiumFc(homeData.getPremiumFc()==null?"":homeData.getPremiumFc().toString());
			travelRes.setActualPremiumLc(homeData.getPremiumLc()==null?"":homeData.getPremiumLc().toString());
			travelRes.setOverallPremiumFc(homeData.getOverallPremiumFc()==null?"":homeData.getOverallPremiumFc().toString());
			travelRes.setOverallPremiumLc(homeData.getOverallPremiumLc()==null?"":homeData.getOverallPremiumLc().toString());
			
		} catch(Exception e) {
			e.printStackTrace();
			log.info("Exception is --->"+e.getMessage());
			return null;
		}
			return travelRes;
	}

	@Override
	public List<TravelPassHistoryRes> getallpasshistorydetails(TravelPassDetailsGetAllReq req) {
		List<TravelPassHistoryRes> resList = new ArrayList<TravelPassHistoryRes>();
		DozerBeanMapper mapper = new DozerBeanMapper();
		try {
			List<TravelPassengerHistory> datas = historyRepo.findByQuoteNoOrderByPassengerIdAsc(req.getQuoteNo());
			
			List<TravelPassengerHistory> adultList = datas.stream().filter( o -> o.getGroupId().equals(2) ).collect(Collectors.toList());
			for(TravelPassengerHistory data : adultList) {
				TravelPassHistoryRes res = new TravelPassHistoryRes();
				res = mapper.map(data, TravelPassHistoryRes.class);
				resList.add(res);
			}
			
			List<TravelPassengerHistory> otherList =  datas.stream().filter( o -> ! o.getGroupId().equals(2) ).collect(Collectors.toList());
			for(TravelPassengerHistory data : otherList) {
				TravelPassHistoryRes res = new TravelPassHistoryRes();
				res = mapper.map(data, TravelPassHistoryRes.class);
				resList.add(res);
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
	public List<Error> validatepassergerList(PassengerSaveReq req) {
		List<Error> errors = new ArrayList<Error>();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			
			if (StringUtils.isBlank(req.getQuoteNo())   ) {
				errors.add(new Error("05", "PassngerList", "Please Enter QuoteNo"));
			} else  {
//				if(merge == true  ) {
//					errors.addAll(mergepassergerValidation(PassengerSaveReq req));
//				} else {
//					errors.addAll(addnewpassergerListValidate(req));
//				}
			}
			
		
		} catch(Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			errors.add(new Error("01","Common Error",e.getMessage() ));
		}
		return errors;
	}

	public List<String> addnewpassergerListValidate(PassengerSaveReq req) {
		List<String> errors = new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			List<String> passNo = new ArrayList<String>();
			List<String> relationId = new ArrayList<String>();
			Long row = 0L ;
			int count=0;
			String quoteNo = ""; 
			String insuranceId = "";
			String branchCode = "" ;
			Integer productId = null ;
			List<GroupFilterReq> groupReqList = new ArrayList<GroupFilterReq>(); 
				
			if (req.getPassengerList()==null ||  req.getPassengerList().size() <= 0 ) {
				errors.add("1237");
//				errors.add(new Error("05", "PassngerList", "Please Enter Atleast One Passenger Details "));
				
			} else if (StringUtils.isBlank(req.getQuoteNo())   ) {
				errors.add("1218");
//				errors.add(new Error("05", "PassngerList", "Please Enter QuoteNo"));
			} else {
				quoteNo = req.getQuoteNo() ;
				HomePositionMaster homeData = homeRepo.findByQuoteNo(quoteNo);
				insuranceId = homeData.getCompanyId() ;
				branchCode = homeData.getBranchCode() ;
				productId = homeData.getProductId() ;
				
			}
			List<ProductGroupMaster>  groupMaster =  getProductGroupMasterDropdown(insuranceId , branchCode , productId );
			List<EserviceTravelGroupDetails> groupDetails = groupRepo.findByQuoteNoOrderByGroupIdAsc(quoteNo);
			List<ListItemValue> relations  = getListItem(insuranceId , branchCode , "RELATION_TYPE");
			
			for(PassengerListSaveReq data: req.getPassengerList()) {
				row = row + 1 ;
				if (StringUtils.isBlank(data.getPassengerFirstName())) {
					errors.add("1219"+","+row);
//					errors.add(new Error("05", "PassengerFirstName", "Please Enter Passenger First Name in Row No : "+ row));
				} else if (data.getPassengerFirstName().length() > 120) {
					errors.add("1220"+","+row);
//					errors.add(new Error("05", "PassengerFirstName", "Please Enter Passenger First Name within 120 Characters in Row No : "+ row));
				}
			
				if (StringUtils.isBlank(data.getPassengerLastName())) {
					errors.add("1221"+","+row);
//					errors.add(new Error("06", "PassengerLastName", "Please Enter Passenger Last Name in Row No : "+ row));
				} else if (data.getPassengerLastName().length() > 120) {
					errors.add("1222"+","+row);
//					errors.add(new Error("06", "PassengerLastName", "Please Enter Passenger Last Name within 120 Characters in Row No : "+ row));
				}
				Calendar cal = new GregorianCalendar();
				Date today = new Date();
				
				//Age Validation
				
				//Age Validation
				if (data.getDob()==null) {
					errors.add("1223"+","+row);
//					errors.add(new Error("07", "Dob", "Please Enter Dob in Row No : "+ row));
				} else if ((data.getDob().after(today))) {
					errors.add("1224"+","+row);
//					errors.add(new Error("07", "Dob", "Please Enter Dob as Past Date in Row No : "+ row));
				} else {
					// Age Band Validation
					String date =  sdf.format(data.getDob()) ; 
					Date birthDate = sdf.parse(date);
				    Long ageInMillis = System.currentTimeMillis() - birthDate.getTime();
				    Long years1 = ageInMillis /(365 * 24*60*60*1000l);
				    Long leftover = ageInMillis %(365 * 24*60*60*1000l);
				    Long days = leftover/(24*60*60*1000l);
				    Long years = years1 <=3 ? 3 : years1 ;
				    System.out.println(years);
				    System.out.println(days);
				   
				    
				    List<ProductGroupMaster> filterGroupMaster =  groupMaster.stream().filter( o -> o.getGroupFrom() <=years && o.getGroupFrom() >= years ).collect(Collectors.toList());
				    List<EserviceTravelGroupDetails> filterGroupData =  groupDetails.stream().filter( o -> o.getStartt() <=years && o.getEnd() >= years ).collect(Collectors.toList());
				    if( filterGroupMaster.size() > 0 && filterGroupData.size() <= 0 ) {
				    	errors.add("1225"+","+row);
//				    	errors.add(new Error("07", "Group","Row No : "+ row + " - Dob is based on " + filterGroupMaster.get(0).getGroupDesc() +  "Group .  Which is not opted Any Cover " ));
				    } else if ( filterGroupData.size() > 0) {
				    	EserviceTravelGroupDetails groupData =  filterGroupData.get(0);
				    	Long fromAge = Long.valueOf(groupData.getStartt()) ;
					    Long toAge = Long.valueOf(groupData.getEnd()) ;
					    String bandDesc = groupData.getGroupDesc();
					    
					    GroupFilterReq setGroup = new GroupFilterReq();
					    setGroup.setDob(data.getDob());
					    setGroup.setPassportNo(data.getPassportNo());
					    setGroup.setFrom(fromAge);
					    setGroup.setTo(toAge);
					    setGroup.setGroupDesc(bandDesc);
					    setGroup.setGroupId(groupData.getGroupId());
					    groupReqList.add(setGroup);
					    
					    if(groupData.getGroupId().equals(1) && StringUtils.isNotBlank(data.getRelationId()  ) ) {
							String relation = relations.stream().filter(o -> o.getItemCode().equalsIgnoreCase(data.getRelationId()  )).collect(Collectors.toList()).get(0).getItemValue();
							 if("1".equalsIgnoreCase(data.getRelationId()) || "2".equalsIgnoreCase(data.getRelationId()) || "3".equalsIgnoreCase(data.getRelationId())
									 || "4".equalsIgnoreCase(data.getRelationId()) || "9".equalsIgnoreCase(data.getRelationId()) || "10".equalsIgnoreCase(data.getRelationId())) {
//								 errors.add("1226"+","+row);
//								 errors.add(new Error("01", "Relation", relation + " Relation Type " + " Not Allowed For Kids Section In Row No: " +  row )); 
							 }
							
						}
						
						if(groupData.getGroupId().equals(1) ) {
							// Age 3 - 18 Restrict
							 if( 90 > days && years > toAge    ) {
								 errors.add("1227"+","+row);
//								 errors.add(new Error("01", "Dob", "Date Of Birth Should be " + bandDesc +" Years Allowed in Row No : " +  row )); 
							 }
						 
						} else {
							// Other Age Restrict	
							 if( fromAge > years && years < toAge ) {
								 errors.add("1228"+","+row);
//								 errors.add(new Error("01", "Dob", "Date Of Birth Should be  " + bandDesc + "  Years Only Allowed in Row No : " + row)); 
							 }
						}  
				    	
				    }
				    
				}
		
				
				//Gender Validation
				if (StringUtils.isBlank(data.getGenderId())) {
					errors.add("1229"+","+row);
//					errors.add(new Error("08", "GenderId", "Please Select GenderId in Row No : "+ row));
				}

				//RelationShip validation
				
				if (StringUtils.isBlank(data.getRelationId())) {
					errors.add("1230"+","+row);
//					errors.add(new Error("10", "Relation Id", "Please Select Relation in Row No : "+ row));
				}else {
					
					String relation = relations.stream().filter(o -> o.getItemCode().equalsIgnoreCase(data.getRelationId()  )).collect(Collectors.toList()).get(0).getItemValue();
					if("SELF".equalsIgnoreCase(relation)) {
						count++;
					} 
					if(count>1 && ! relation.equalsIgnoreCase("Others") ) {
						errors.add("1238"+","+row);
//						errors.add(new Error("10", "Relation Id", "Duplicate Relation  Available in Row No : " + row ));
					} 
				}
				if (StringUtils.isBlank(data.getNationality())) {
					errors.add("1231"+","+row);
//					errors.add(new Error("11", "Nationality", "Please Enter Nationality in Row No : "+ row));
				}
				//Passport No validation
				if (StringUtils.isBlank(data.getPassportNo())) {
					
				} else {
					List<String> passPortNo =  passNo.stream().filter( o -> o.equalsIgnoreCase(data.getPassportNo()) ).collect(Collectors.toList()); 
					if(passPortNo.size()>0  ) {
						errors.add("2357"+","+row);
//						errors.add(new Error("12", "PassportNo", "Duplicate PassportNo Available in Row No : " + row ));
					} else {
						passNo.add(data.getPassportNo());
					}
				}
				

			}
			
			//boolean checkGroupValidation = StringUtils.isNotBlank(req.getGroupValidationYesOrNo()) && "Yes".equalsIgnoreCase(req.getGroupValidationYesOrNo()) ? true : false ; 
					
		//	if(checkGroupValidation == true ) {
				// Group Validation 
				for ( EserviceTravelGroupDetails group : groupDetails ) {
					List<GroupFilterReq> filterList = groupReqList.stream().filter( o -> o.getGroupId()!=null && Integer.valueOf(o.getGroupId()).equals(group.getGroupId())  )
							.collect(Collectors.toList()) ;
					
					if( group.getGroupMembers() < filterList.size() ) {
						errors.add("1239"+","+group.getGroupDesc());
//						errors.add(new Error("12", "Group", "Group : " + group.getGroupDesc() + " Number Of Passengers Greater Than " + group.getGrouppMembers() + " Passengers Not Allowed" ));
					} else if( group.getGroupMembers() > filterList.size() ) {
						errors.add("1240"+","+group.getGroupDesc());
//						errors.add(new Error("12", "Group", "Group : " + group.getGroupDesc() + " Number Of Passengers Lesser Than  " + group.getGrouppMembers() + " Passengers Not Allowed" ));
					}
					
				}
			
				List<PassengerListSaveReq> filterSelf1 = req.getPassengerList().stream().filter( o -> o.getRelationId()!=null && Integer.valueOf(o.getRelationId()).equals(9)  )
						.collect(Collectors.toList()) ;
				List<PassengerListSaveReq> filterSelf2 = req.getPassengerList().stream().filter( o -> o.getRelationId()!=null && Integer.valueOf(o.getRelationId()).equals(10)  )
						.collect(Collectors.toList()) ;
				if(filterSelf1.size()<=0 && filterSelf2.size()<=0 ) {
					errors.add("1241");
//					errors.add(new Error("12", "SelfRelation", " Self Relation is missing in Passenger Details" ));
				}
				List<Integer> filterGroupIds = groupReqList.stream().map(GroupFilterReq :: getGroupId ).collect(Collectors.toList());
				filterGroupIds.forEach( o -> {
					List<EserviceTravelGroupDetails> filterGroup = groupDetails.stream().filter( e -> e.getGroupId()!=null && Integer.valueOf(e.getGroupId()).equals(o)  ).collect(Collectors.toList());
					String groupDesc = groupMaster.size() > 0 ? groupMaster.stream().filter( f -> f.getGroupId().equals(o) ).collect(Collectors.toList()).get(0).getBandDesc() : "" ;
					
					if(filterGroup.size() <= 0 ) {
						errors.add("1242"+","+groupDesc);
//						errors.add(new Error("12", "filterGroup", groupDesc + " Is not a Valid Group In Passenger Details " ));
					}
				});		
				
			
		//	}
			
		} catch(Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			errors.add("1236");
//			errors.add(new Error("01","Common Error",e.getMessage() ));
		}
		return errors;
	}
	
	public List<String> mergepassergerValidation(PassengerSaveReq req) {
		List<String> errors = new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			if (StringUtils.isBlank(req.getCreatedBy())   ) {
				errors.add("1217");
//				errors.add(new Error("05", "CreatedBy", "Please Enter CreatedBy"));
			}
			if (StringUtils.isBlank(req.getQuoteNo())   ) {
				errors.add("1218");
//				errors.add(new Error("05", "QuoteNo", "Please Enter QuoteNo"));
			} else {
				String quoteNo = req.getQuoteNo() ;
				
				HomePositionMaster homeData = homeRepo.findByQuoteNo(quoteNo);
				String insuranceId = homeData.getCompanyId() ;
				String branchCode = homeData.getBranchCode() ;
				Integer productId = homeData.getProductId() ;
				List<PassengerListSaveReq> passengerList  = req.getPassengerList();
				// Previuos Passenger List 
				List<TravelPassengerDetails> previousPassengers = repo.findByQuoteNoAndStatusNotOrderByPassengerIdAsc(quoteNo , "D" ) ;
				List<String> newPassPortNo = new ArrayList<String>();
				// DropDown Descriptions 
				List<ListItemValue> genders  = getListItem(insuranceId , branchCode , "GENDER");
				List<ListItemValue> relations  = getListItem(insuranceId , branchCode , "RELATION_TYPE");
				List<ProductGroupMaster>  groupMaster =  getProductGroupMasterDropdown(insuranceId , branchCode , productId );
				List<EserviceTravelGroupDetails> groupDetails = groupRepo.findByQuoteNoOrderByGroupIdAsc(quoteNo);
				
				if (StringUtils.isBlank(req.getQuoteNo())   ) {
					errors.add("1218");
//					errors.add(new Error("05", "PassngerList", "Please Enter QuoteNo"));
				}
				
				int selfRealtionCount=0;
				Long row = 0L ;
				for(PassengerListSaveReq data: passengerList) {
					row = row + 1 ;
					if (StringUtils.isBlank(data.getPassengerFirstName())) {
						errors.add("1219"+","+row);
//						errors.add(new Error("05", "PassengerFirstName", "Please Enter Passenger First Name in Row No : "+ row));
					} else if (data.getPassengerFirstName().length() > 120) {
						errors.add("1220"+","+row);
//						errors.add(new Error("05", "PassengerFirstName", "Please Enter Passenger First Name within 120 Characters in Row No : "+ row));
					}
				
					if (StringUtils.isBlank(data.getPassengerLastName())) {
						errors.add("1221"+","+row);
//						errors.add(new Error("06", "PassengerLastName", "Please Enter Passenger Last Name in Row No : "+ row));
					} else if (data.getPassengerLastName().length() > 120) {
						errors.add("1222"+","+row);
//						errors.add(new Error("06", "PassengerLastName", "Please Enter Passenger Last Name within 120 Characters in Row No : "+ row));
					}
					Calendar cal = new GregorianCalendar();
					Date today = new Date();
					
					//Age Validation
					if (data.getDob()==null) {
						errors.add("1223"+","+row);
//						errors.add(new Error("07", "Dob", "Please Enter Dob in Row No : "+ row));
					} else if ((data.getDob().after(today))) {
						errors.add("1224"+","+row);
//						errors.add(new Error("07", "Dob", "Please Enter Dob as Past Date in Row No : "+ row));
					} else {
						// Age Band Validation
						String date =  sdf.format(data.getDob()) ; 
						Date birthDate = sdf.parse(date);
					    Long ageInMillis = System.currentTimeMillis() - birthDate.getTime();
					    Long years1 = ageInMillis /(365 * 24*60*60*1000l);
					    Long leftover = ageInMillis %(365 * 24*60*60*1000l);
					    Long days = leftover/(24*60*60*1000l);
					    Long years = years1 <=3 ? 3 : years1 ;
					    System.out.println(years);
					    System.out.println(days);
					    List<ProductGroupMaster> filterGroupMaster =  groupMaster.stream().filter( o -> o.getGroupFrom() <=years && o.getGroupFrom() >= years ).collect(Collectors.toList());
					    List<EserviceTravelGroupDetails> filterGroupData =  groupDetails.stream().filter( o -> o.getStartt() <=years && o.getEnd() >= years ).collect(Collectors.toList());
					    if( filterGroupData.size() <= 0 ) {
					    	errors.add("1225"+","+row);
//					    	errors.add(new Error("07", "Group","Row No : "+ row + " - Dob is based on " + filterGroupMaster.get(0).getGroupDesc() +  "Group .  Which is not opted Any Cover " ));
					    } else if ( filterGroupData.size() > 0) {
					    	EserviceTravelGroupDetails groupData =  filterGroupData.get(0);
					    	Long fromAge = Long.valueOf(groupData.getStartt()) ;
						    Long toAge = Long.valueOf(groupData.getEnd()) ;
						    String bandDesc = groupData.getGroupDesc();
						    if(groupData.getGroupId().equals(1) && StringUtils.isNotBlank(data.getRelationId()  ) ) {
								String relation = relations.stream().filter(o -> o.getItemCode().equalsIgnoreCase(data.getRelationId()  )).collect(Collectors.toList()).get(0).getItemValue();
								 if("1".equalsIgnoreCase(data.getRelationId()) || "2".equalsIgnoreCase(data.getRelationId()) || "3".equalsIgnoreCase(data.getRelationId())
										 || "4".equalsIgnoreCase(data.getRelationId()) || "9".equalsIgnoreCase(data.getRelationId()) || "10".equalsIgnoreCase(data.getRelationId())) {
//									 errors.add("1226"+","+row);
//									 errors.add(new Error("01", "Relation", relation + " Relation Type " + " Not Allowed For Kids Section In Row No: " +  row )); 
								 }
								
							}
							
							if(groupData.getGroupId().equals(1) ) {
								// Age 3 - 18 Restrict
								 if( 90 > days && years > toAge    ) {
									 errors.add("1227"+","+row);
//									 errors.add(new Error("01", "Dob", "Date Of Birth Should be " + bandDesc +" Years Allowed in Row No : " +  row )); 
								 }
							 
							} else {
								// Other Age Restrict	
								 if( fromAge > years && years < toAge ) {
									 errors.add("1228"+","+row);
//									 errors.add(new Error("01", "Dob", "Date Of Birth Should be  " + bandDesc + "  Years Only Allowed in Row No : " + row)); 
								 }
							}  
					    	
					    }
					    
					}
							
					//Gender Validation
					if (StringUtils.isBlank(data.getGenderId())) {
						errors.add("1229"+","+row);
//						errors.add(new Error("08", "GenderId", "Please Select GenderId in Row No : "+ row));
					}

					//RelationShip validation
					
					if (StringUtils.isBlank(data.getRelationId())) {
						errors.add("1230"+","+row);
//						errors.add(new Error("10", "Relation Id", "Please Select Relation in Row No : "+ row));
					}else {
						
//						String relation = relations.stream().filter(o -> o.getItemCode().equalsIgnoreCase(data.getRelationId()  )).collect(Collectors.toList()).get(0).getItemValue();
//						if("SELF".equalsIgnoreCase(relation)) {
//							selfRealtionCount++;
//						} 
//						if( filterSelf.size() > 0  && selfRealtionCount>=1 ) {
//							errors.add(new Error("10", "Relation Id", "Self Relation is Already Active in Previous Record in Row No : "));
//							break ;
//						} else if(  selfRealtionCount>1 ) {
//							errors.add(new Error("10", "Relation Id", "Duplicate Relation  Available in Row No : " + row ));
//							break ;
//						}  
					}
					if (StringUtils.isBlank(data.getNationality())) {
						errors.add("1231"+","+row);
//						errors.add(new Error("11", "Nationality", "Please Enter Nationality in Row No : "+ row));
					}
					//Passport No validation
					
					if (StringUtils.isBlank(data.getPassportNo())) {
						errors.add("1232"+","+row);
//						errors.add(new Error("12", "PassportNo", "Please Enter PassportNo in Row No : "+ row));
					} else if (data.getPassportNo().length() > 20) {
						errors.add("1233"+","+row);
//						errors.add(new Error("12", "PassportNo", "Please Enter PassportNo within 20 Characters in Row No : "+ row));
					} else {
						List<TravelPassengerDetails> oldpassPortNo =  previousPassengers.stream().filter( o ->  o.getPassportNo().equalsIgnoreCase(data.getPassportNo()) ).collect(Collectors.toList());
						if(oldpassPortNo.size()>0 && StringUtils.isBlank(data.getPassengerId()) ) {
							errors.add("1234"+","+row);
//							errors.add(new Error("12", "PassportNo", "Row No : "+ row + " PassportNo is Already Active in Previous Record "));
						} else if(oldpassPortNo.size()>0 && StringUtils.isNotBlank(data.getPassengerId()) && ! oldpassPortNo.get(0).getPassengerId().equals(Integer.valueOf(data.getPassengerId()))  ) {							
							errors.add("1234"+","+row);	
//							errors.add(new Error("12", "PassportNo", "Row No : "+ row + " PassportNo is Already Active in Previous Record "));
						} else {
							List<String> passPortNo =  newPassPortNo.stream().filter( o -> o.equalsIgnoreCase(data.getPassportNo()) ).collect(Collectors.toList()); 
							if(passPortNo.size()>0  ) {
								errors.add("1235"+","+row);
//								errors.add(new Error("12", "PassportNo", "Duplicate PassportNo Available in Row No : " + row ));
							} else {
								newPassPortNo.add(data.getPassportNo());
							}
						}
					
					}
					

				}
			}
			
			
			
			
		} catch(Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			errors.add("1236");
//			errors.add(new Error("01","Common Error",e.getMessage() ));
		}
		return errors;
	}
	@Override
	public SuccessRes savepassengerlist(PassengerSaveReq req) {
		SuccessRes res = new SuccessRes();
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		try {
			
//			if( merge==true ) {
//				res = mergepassengerlist(req);
//			} else {
//				res = addpassengerlist(req);
//			}
//			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Log Details"+e.getMessage());
			return null;
		}
		return res;
	}
	
	@Override
	public SuccessRes mergepassengerlist(PassengerSaveReq req) {
		SuccessRes res = new SuccessRes();
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		try {
			List<PassengerListSaveReq> reqList = req.getPassengerList();
			// Primary Tables & Details 
			String quoteNo = req.getQuoteNo();
			Integer lastPassCount = Integer.valueOf( repo.countByQuoteNo(quoteNo).toString());
			List<PolicyCoverData>  coverList = coverRepo.findByQuoteNo(quoteNo);
			List<PremiumGroupDevideRes> covers = new ArrayList<PremiumGroupDevideRes>();
			 Type listType = new TypeToken<List<PremiumGroupDevideRes>>(){}.getType();
			 covers = modelMapper.map(coverList,listType);
			List<EserviceTravelGroupDetails> groupList = groupRepo.findByQuoteNoOrderByGroupIdAsc(quoteNo);  
			int totalGroupMembers = groupList.stream()
			        .map(EserviceTravelGroupDetails::getGroupMembers)
			        .filter(Objects::nonNull)
			        .mapToInt(Integer::intValue)
			        .sum();
			EserviceTravelDetails travelData = eserTraRepo.findByQuoteNo(quoteNo);
			HomePositionMaster homeData = homeRepo.findByQuoteNo(quoteNo);
			BigDecimal lcExchangeRate = travelData.getLcExchangeRate();
			Double exchangeRate=lcExchangeRate != null ? lcExchangeRate.doubleValue() : 1.0;
			String insuranceId = homeData.getCompanyId() ;
			String productId = homeData.getProductId().toString();
			String sectionId = homeData.getSectionId().toString();
			String branchCode = homeData.getBranchCode() ;
			
			// DropDown Descriptions 
			List<ListItemValue> genders  = getListItem(insuranceId , branchCode , "GENDER");
			List<ListItemValue> relations  = getListItem(insuranceId , branchCode , "RELATION_TYPE");
			List<CountryMaster> countryList = getCountryList(insuranceId);
				
			// Currency Decimal Digit
			String decimalDigits = currencyDecimalFormat(homeData.getCompanyId() , homeData.getCurrency() ).toString();
			String stringFormat = "%0"+decimalDigits+"d" ;
			String decimalLength = decimalDigits.equals("0") ?"" : String.format(stringFormat ,0L)  ;
			String pattern = StringUtils.isBlank(decimalLength) ?  "#####0" :   "#####0." + decimalLength;
			DecimalFormat df = new DecimalFormat(pattern);
			
			// Grouping
			List<EserviceTravelGroupDetails> groupDetails = groupRepo.findByQuoteNoOrderByGroupIdAsc(quoteNo);
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
			List<GroupFilterReq> groupReqList = new ArrayList<GroupFilterReq>();
			reqList.forEach( data ->  {
				GroupFilterReq setGroup = new GroupFilterReq();
				String date =  sdf.format(data.getDob()) ; 
				Date birthDate = null;
				try {
					birthDate = sdf.parse(date);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    Long ageInMillis = System.currentTimeMillis() - birthDate.getTime();
			    Long years1 = ageInMillis /(365 * 24*60*60*1000l);
			    Long leftover = ageInMillis %(365 * 24*60*60*1000l);
			    Long days = leftover/(24*60*60*1000l);
			    Long years = years1 <=3 ? 3 : years1 ;
			    System.out.println(years);
			    System.out.println(days);
			    List<EserviceTravelGroupDetails> filterGroupData =  groupDetails.stream().filter( o -> o.getStartt() <=years && o.getEnd() >= years ).collect(Collectors.toList());
				EserviceTravelGroupDetails groupData =  filterGroupData.get(0);
		    	Long fromAge = Long.valueOf(groupData.getStartt()) ;
			    Long toAge = Long.valueOf(groupData.getEnd()) ;
			    String bandDesc = groupData.getGroupDesc();
			    setGroup.setDob(data.getDob());
			    setGroup.setPassportNo(data.getPassportNo());
			    setGroup.setFrom(fromAge);
			    setGroup.setTo(toAge);
			    setGroup.setGroupDesc(bandDesc);
			    setGroup.setGroupId(groupData.getGroupId());
			    groupReqList.add(setGroup);
			    
			} ) ;;
			 
			    
			Map<Integer, List<GroupFilterReq>> groupByGroupId = groupReqList.stream().filter( o -> o.getGroupId() !=null  ).collect( Collectors.groupingBy(GroupFilterReq :: getGroupId )) ;
			Map<Integer, List<PremiumGroupDevideRes>> coverGroupByGroupId = covers.stream().filter( o -> o.getVehicleId() !=null  ).collect( Collectors.groupingBy(PremiumGroupDevideRes :: getVehicleId )) ;		
			Map<Integer, List<EserviceTravelGroupDetails>> groupListGroupByGroupId = groupList.stream().filter( o -> o.getGroupId() !=null  ).collect( Collectors.groupingBy(EserviceTravelGroupDetails :: getGroupId )) ;
			
			// Framing List 
			List<TravelPassengerDetails>  saveDatas = new ArrayList<TravelPassengerDetails>();
			List<PolicyCoverDataIndividuals> totalIndiCovers = new ArrayList<PolicyCoverDataIndividuals>(); 
			List<EserviceTravelGroupDetails> updateGroups = new ArrayList<EserviceTravelGroupDetails>(); 
			Double totalPremium = covers.stream().filter( o ->  o.getTaxId().equals(0) && o.getDiscLoadId().equals(0) && o.getPremiumIncludedTaxLc()!=null && o.getPremiumIncludedTaxLc().doubleValue() > 0D ).mapToDouble( o ->   o.getPremiumIncludedTaxLc().doubleValue()  ).sum(); ; 
			
			for (Integer group :  groupByGroupId.keySet() ) {
				
				List<GroupFilterReq>  filterSameGroup  =  groupByGroupId.get(group);
				List<String>  filerPassportnos = filterSameGroup.stream().map(GroupFilterReq :: getPassportNo ) .collect(Collectors.toList());
				
				List<PassengerListSaveReq> filterReqPass = reqList.stream().filter( o -> filerPassportnos.contains(o.getPassportNo()) ).collect(Collectors.toList());
				List<PremiumGroupDevideRes> groupCovers = coverGroupByGroupId.get(group);
				EserviceTravelGroupDetails groupData =  groupListGroupByGroupId.get(group).get(0)  ;
				//String groupDesc =  groupData.getGroupDesc() ;
				Double groupPremiumFc = groupCovers.stream().filter( o -> o.getVehicleId().equals(groupData.getGroupId()) && o.getTaxId().equals(0) && o.getDiscLoadId().equals(0) && o.getPremiumExcludedTaxFc()!=null && o.getPremiumExcludedTaxFc().doubleValue() > 0D ).mapToDouble( o ->   o.getPremiumExcludedTaxFc().doubleValue()  ).sum();					
				Double groupOverAllPremiumFc = groupCovers.stream().filter( o -> o.getVehicleId().equals(groupData.getGroupId()) && o.getTaxId().equals(0) && o.getDiscLoadId().equals(0) && o.getPremiumIncludedTaxFc()!=null && o.getPremiumIncludedTaxFc().doubleValue() > 0D ).mapToDouble( o ->   o.getPremiumIncludedTaxFc().doubleValue()  ).sum();
				Double groupPremiumLc = groupCovers.stream().filter( o -> o.getVehicleId().equals(groupData.getGroupId()) && o.getTaxId().equals(0) && o.getDiscLoadId().equals(0) && o.getPremiumExcludedTaxLc()!=null && o.getPremiumExcludedTaxLc().doubleValue() > 0D ).mapToDouble( o ->   o.getPremiumExcludedTaxLc().doubleValue()  ).sum();					
				Double groupOverAllPremiumLc = groupCovers.stream().filter( o -> o.getVehicleId().equals(groupData.getGroupId()) && o.getTaxId().equals(0) && o.getDiscLoadId().equals(0) && o.getPremiumIncludedTaxLc()!=null && o.getPremiumIncludedTaxLc().doubleValue() > 0D ).mapToDouble( o ->   o.getPremiumIncludedTaxLc().doubleValue()  ).sum();
				totalPremium = totalPremium +  groupOverAllPremiumFc; 
				groupData.setActualPremiumFc(groupPremiumFc  == 0D ? new BigDecimal(0) : new BigDecimal(df.format(groupPremiumFc)));
				groupData.setActualPremiumLc(groupPremiumLc  == 0D ? new BigDecimal(0) : new BigDecimal(df.format(groupPremiumLc)));
				groupData.setOverallPremiumFc(groupOverAllPremiumFc  == 0D ? new BigDecimal(0) : new BigDecimal(df.format(groupOverAllPremiumFc)));
				groupData.setOverallPremiumLc(groupOverAllPremiumLc  == 0D ? new BigDecimal(0) : new BigDecimal(df.format(groupOverAllPremiumLc)));
				updateGroups.add(groupData);
				
				List<PremiumGroupDevideRes> getDividedCovers = getDevidedCovers(quoteNo ,  groupData.getGroupMembers(), groupCovers ) ;
				PremiumGroupDevideRes coverData = getDividedCovers.get(0);
				
				Double premiumFc = getDividedCovers.stream().filter( o -> o.getTaxId().equals(0) && o.getDiscLoadId().equals(0) && o.getPremiumExcludedTaxFc()!=null && o.getPremiumExcludedTaxFc().doubleValue() > 0D ).mapToDouble( o ->   o.getPremiumExcludedTaxFc().doubleValue()  ).sum();					
				Double overAllPremiumFc = getDividedCovers.stream().filter( o ->  o.getTaxId().equals(0) && o.getDiscLoadId().equals(0) && o.getPremiumIncludedTaxFc()!=null && o.getPremiumIncludedTaxFc().doubleValue() > 0D ).mapToDouble( o ->   o.getPremiumIncludedTaxFc().doubleValue()  ).sum();
				Double premiumLc = getDividedCovers.stream().filter( o -> o.getTaxId().equals(0) && o.getTaxId().equals(0) &&o.getDiscLoadId().equals(0) && o.getPremiumExcludedTaxLc()!=null && o.getPremiumExcludedTaxLc().doubleValue() > 0D ).mapToDouble( o ->   o.getPremiumExcludedTaxLc().doubleValue() *exchangeRate ).sum();					
				Double overAllPremiumLc = getDividedCovers.stream().filter( o -> o.getTaxId().equals(0) && o.getDiscLoadId().equals(0) && o.getPremiumIncludedTaxLc()!=null && o.getPremiumIncludedTaxLc().doubleValue() > 0D ).mapToDouble( o ->   o.getPremiumIncludedTaxLc().doubleValue() *exchangeRate ).sum();
				Double taxPremium = getDividedCovers.stream().filter( o -> !o.getTaxId().equals(0)  && o.getCoverageType().equalsIgnoreCase("T") && o.getTaxAmount()!=null  &&o.getTaxAmount().doubleValue() > 0D ).mapToDouble( o ->   o.getTaxAmount().doubleValue()  ).sum();
				
				 for(PassengerListSaveReq data : filterReqPass ) {
					 TravelPassengerDetails saveData = new TravelPassengerDetails();
					 
					 String gender = genders.stream().filter(o -> o.getItemCode().equalsIgnoreCase(data.getGenderId()  )).collect(Collectors.toList()).get(0).getItemValue();
					 String relation = relations.stream().filter(o -> o.getItemCode().equalsIgnoreCase(data.getRelationId()  )).collect(Collectors.toList()).get(0).getItemValue();
					 Integer passengerId = 0 ;
						
					 if (StringUtils.isNotBlank(data.getPassengerId())) {
						 passengerId = Integer.valueOf(data.getPassengerId()) ;
					 }else {
						 lastPassCount =  lastPassCount + 1 ;
						 passengerId = lastPassCount ;
					 }
					 Integer passCount = passengerId ;
					 // Riks Details 
					 TravelPassengerDetails riskDetails = setTravelRiskDetails(travelData);
					 modelMapper.map(riskDetails ,saveData ); 
					 
					 // Passenger Details 
					 saveData.setQuoteNo(quoteNo);
					 saveData.setPassengerId(passengerId);
					 saveData.setPassengerFirstName(data.getPassengerFirstName());
					 saveData.setPassengerLastName(data.getPassengerLastName());
					 saveData.setPassengerName(data.getPassengerFirstName() + " " + data.getPassengerLastName() );
					 saveData.setDob(data.getDob());
					 saveData.setGenderId(data.getGenderId());
					 saveData.setGenderDesc(gender );
					 LocalDate birthDate = data.getDob().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					 LocalDate currentDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();;
					 int age =  Period.between(birthDate, currentDate).getYears();
					 saveData.setAge(age);
					 saveData.setLcExchangeRate(lcExchangeRate);
					
					 //saveData.setAddress1("");
					 //saveData.setAddress2("");
					 saveData.setRelationId(Integer.valueOf(data.getRelationId()));
					 saveData.setRelationDesc(relation);
					 saveData.setGroupId(group);
					 saveData.setLocationId(1);
					 saveData.setGroupCount(groupData.getGroupMembers());
					 saveData.setPassportNo(data.getPassportNo());
			//		 saveData.setCivilId(data.getCivilId());
					 saveData.setUpdatedBy(req.getCreatedBy());
					 saveData.setUpdatedDate(new Date());
					// Find by country name (not countryId)
					 List<CountryMaster> filterCountry = countryList.stream()
					         .filter(o -> o.getCountryName().equalsIgnoreCase(data.getNationality()))
					         .collect(Collectors.toList());

					 // Set NationalityDesc = name from input
					 saveData.setNationalityDesc(data.getNationality());

					 // Set Nationality = ID from country master (if found)
					 saveData.setNationality(filterCountry.size() > 0 ? filterCountry.get(0).getCountryId() : "");
					 saveData.setStatus("Y");
					 // Cover Details 
					 saveData.setCdRefno(coverData.getCdRefno());
					 saveData.setVdRefno(coverData.getVdRefno());
					 saveData.setMsRefno(coverData.getMsRefno());
					 saveData.setBasePremium(overAllPremiumFc  == 0D ? new BigDecimal(0) :new BigDecimal(df.format(overAllPremiumFc)));
					 saveData.setCommissionPercentage(homeData.getCommissionPercentage());
					 saveData.setCommissionType(homeData.getCommissionType());
					 saveData.setCommissionTypeDesc(homeData.getCommissionTypeDesc());
					 saveData.setActualPremiumFc(premiumFc  == 0D ? new BigDecimal(0) : new BigDecimal(df.format(premiumFc)) );
					 saveData.setActualPremiumLc(premiumLc  == 0D ? new BigDecimal(0) :new BigDecimal(df.format(premiumLc)));
					 saveData.setOverallPremiumFc(overAllPremiumFc  == 0D ? new BigDecimal(0) :new BigDecimal(df.format(overAllPremiumFc)));
					 saveData.setOverallPremiumLc(overAllPremiumLc  == 0D ? new BigDecimal(0) : new BigDecimal(df.format(overAllPremiumLc)));
					 saveData.setVatPremium(taxPremium  == 0D ? new BigDecimal(0) : new BigDecimal(df.format(taxPremium)));
					 saveData.setVatCommission(homeData.getVatCommission());
					 saveData.setTotalPremium(totalPremium  == 0D ? new BigDecimal(0) :new BigDecimal(df.format(totalPremium)));
					 saveData.setPassportType(data.getPassportType() == null ? null : data.getPassportType());
					 saveData.setPlaceOfBirth(data.getPlaceOfBirth() == null ? null : data.getPlaceOfBirth());
					 saveData.setPlaceOfIssue(data.getPlaceOfIssue() == null ? null : data.getPlaceOfIssue());
					 saveData.setPassportIssueDate(data.getPassportIssueDate() == null ? null : data.getPassportIssueDate());
					 saveData.setPassportExpiryDate(data.getPassportExpiryDate() == null ? null : data.getPassportExpiryDate());
					 
					 //passportExpiryCheck
					 
					 Date expiry = data.getPassportExpiryDate() == null ? null : data.getPassportExpiryDate();
					 Date sysDate = new Date();
					
					 if (totalGroupMembers > 5) {
					
					if (expiry.before(sysDate)) {
						saveData.setAlertYN("Y");
						saveData.setRemarks("Your Passport Already Expired");
						System.out.println("Your Passport Already Expired");
					}else {
						Calendar cal = Calendar.getInstance();
			            cal.setTime(sysDate);
			            cal.add(Calendar.MONTH, 6);
			            
			            Date sixMonthsLater = cal.getTime();
			            
			            if (expiry.before(sixMonthsLater)) {
			            	saveData.setAlertYN("Y");
							saveData.setRemarks("Your Passport will Expire Soon");
							System.out.println("Your Passport will Expire Soon");
			            } else {
			            	saveData.setAlertYN("N");
			                System.out.println("Passport is valid");
			            }

					}
					 }
					 saveDatas.add(saveData);
					 
					 // Covers
					 List<PolicyCoverDataIndividuals> indiCovers = new ArrayList<PolicyCoverDataIndividuals>(); 
					 Type listType2 = new TypeToken<List<PolicyCoverDataIndividuals>>(){}.getType();
					 indiCovers = modelMapper.map(getDividedCovers,listType2);
					 indiCovers.forEach( o ->  {
						 o.setGroupId(group);
						 o.setGroupCount(groupData.getGroupMembers());
						 o.setVehicleId(passCount);
						 o.setIndividualId(passCount);
						 o.setLocationId(1);
						 });					
					 totalIndiCovers.addAll(indiCovers);
				}
				 
			}
			
			List<Integer> passengerIds = saveDatas.stream().map(TravelPassengerDetails :: getPassengerId ).collect(Collectors.toList()) ;
			// Save All Passengers
			Long oldPass = repo.countByQuoteNoAndPassengerIdInAndStatusNot(quoteNo,passengerIds,"D");
			if(oldPass > 0 ) {
				repo.deleteByQuoteNoAndPassengerIdInAndStatusNot(quoteNo, passengerIds, "D");
			}
			repo.saveAllAndFlush(saveDatas);
			
			
			
			// Save Divided Indi Covers 
			
			Long count = indiCoverRepo.countByQuoteNoAndVehicleIdIn(quoteNo ,passengerIds);
			if(count > 0 ) {
				indiCoverRepo.deleteByQuoteNoAndVehicleIdIn(quoteNo ,passengerIds);
			}
			indiCoverRepo.saveAllAndFlush(totalIndiCovers);
			
		   // Copy Old Doc
			OldDocumentsCopyReq oldDocCopyReq = new OldDocumentsCopyReq();
			oldDocCopyReq.setInsuranceId(insuranceId);
			oldDocCopyReq.setProductId(productId);
			oldDocCopyReq.setSectionId(sectionId);
			oldDocCopyReq.setQuoteNo(quoteNo);
			docService.copyOldDocumentsToNewQuote(oldDocCopyReq);
			
			//uploaded Documents delete
			
			List<TravelPassengerDetails> pass = repo.findByQuoteNoAndProductIdAndCompanyId(quoteNo,Integer.valueOf(productId),insuranceId);
			List<DocumentTransactionDetails> doc1 = docTransRepo.findByQuoteNoAndProductIdAndCompanyId(quoteNo, Integer.valueOf(productId),insuranceId);
			
			if(pass.size()>0) {
				if(doc1.size()>0) {
					
					List<DocumentTransactionDetails> filterDoc = doc1.stream()
							.filter( d ->  pass.stream().filter( o -> ! o.getStatus().equalsIgnoreCase("D")) .map( TravelPassengerDetails :: getSectionId).anyMatch(  
									e ->    e.equals( d.getSectionId() )))
							.filter( d -> pass.stream().filter( o -> ! o.getStatus().equalsIgnoreCase("D")).map( TravelPassengerDetails :: getPassportNo).anyMatch(  
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
			
		
			
			res.setSuccessId("1");
			res.setResponse("Updated Successfully");
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Log Details"+e.getMessage());
			return null;
		}
		return res;
	}
	
	@Override
	public SuccessRes addpassengerlist(PassengerSaveReq req) {
		SuccessRes res = new SuccessRes();
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		try {
			
			List<PassengerListSaveReq> reqList = req.getPassengerList();
			// Primary Tables & Details 
			String quoteNo = req.getQuoteNo();
			Integer lastPassCount = Integer.valueOf( repo.countByQuoteNo(quoteNo).toString());
			List<PolicyCoverData>  coverList = coverRepo.findByQuoteNo(quoteNo);
			List<PremiumGroupDevideRes> covers = new ArrayList<PremiumGroupDevideRes>();
			 Type listType = new TypeToken<List<PremiumGroupDevideRes>>(){}.getType();
			 covers = modelMapper.map(coverList,listType);
			List<EserviceTravelGroupDetails> groupList = groupRepo.findByQuoteNoOrderByGroupIdAsc(quoteNo);  
			EserviceTravelDetails travelData = eserTraRepo.findByQuoteNo(quoteNo);
			HomePositionMaster homeData = homeRepo.findByQuoteNo(quoteNo); 
			BigDecimal lcExchangeRate = travelData.getLcExchangeRate();
			double exchangeRate = lcExchangeRate != null ? lcExchangeRate.doubleValue() : 1.0;
			String insuranceId = homeData.getCompanyId() ;
			String productId = homeData.getProductId().toString();
			String sectionId = homeData.getSectionId().toString();
			String branchCode = homeData.getBranchCode() ;
			
			// DropDown Descriptions 
			List<ListItemValue> genders  = getListItem(insuranceId , branchCode , "GENDER");
			List<ListItemValue> relations  = getListItem(insuranceId , branchCode , "RELATION_TYPE");
			List<CountryMaster> countryList = getCountryList(insuranceId);
			
			// Currency Decimal Digit
			String decimalDigits = currencyDecimalFormat(homeData.getCompanyId() , homeData.getCurrency() ).toString();
			String stringFormat = "%0"+decimalDigits+"d" ;
			String decimalLength = decimalDigits.equals("0") ?"" : String.format(stringFormat ,0L)  ;
			String pattern = StringUtils.isBlank(decimalLength) ?  "#####0" :   "#####0." + decimalLength;
			DecimalFormat df = new DecimalFormat(pattern);
			
			// Grouping
			List<EserviceTravelGroupDetails> groupDetails = groupRepo.findByQuoteNoOrderByGroupIdAsc(quoteNo);
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
			List<GroupFilterReq> groupReqList = new ArrayList<>();

			reqList.forEach(data -> {
			    GroupFilterReq setGroup = new GroupFilterReq();
			    String date = sdf.format(data.getDob());
			    Date birthDate = null;
			    try {
			        birthDate = sdf.parse(date);
			    } catch (ParseException e) {
			        e.printStackTrace();
			        return; 
			    }

			    long ageInMillis = System.currentTimeMillis() - birthDate.getTime();
			    long years1 = ageInMillis / (365L * 24 * 60 * 60 * 1000);
			    long leftover = ageInMillis % (365L * 24 * 60 * 60 * 1000);
			    long days = leftover / (24 * 60 * 60 * 1000);
			    long years = years1 <= 3 ? 3 : years1;

			    System.out.println(years);
			    System.out.println(days);

			    List<EserviceTravelGroupDetails> filterGroupData = groupDetails.stream()
			            .filter(o -> o.getStartt() <= years && o.getEnd() >= years)
			            .collect(Collectors.toList());

			    if (filterGroupData.isEmpty()) {
			        System.out.println("No matching group for years: " + years);
			        return; 
			    }

			    EserviceTravelGroupDetails groupData = filterGroupData.get(0);
			    long fromAge = groupData.getStartt();
			    long toAge = groupData.getEnd();
			    String bandDesc = groupData.getGroupDesc();

			    setGroup.setDob(data.getDob());
			    setGroup.setPassportNo(data.getPassportNo());
			    setGroup.setFrom(fromAge);
			    setGroup.setTo(toAge);
			    setGroup.setGroupDesc(bandDesc);
			    setGroup.setGroupId(groupData.getGroupId());

			    groupReqList.add(setGroup);
			});

			 
			    
			Map<Integer, List<GroupFilterReq>> groupByGroupId = groupReqList.stream().filter( o -> o.getGroupId() !=null  ).collect( Collectors.groupingBy(GroupFilterReq :: getGroupId )) ;
			Map<Integer, List<PremiumGroupDevideRes>> coverGroupByGroupId = covers.stream().filter( o -> o.getVehicleId() !=null  ).collect( Collectors.groupingBy(PremiumGroupDevideRes :: getVehicleId )) ;		
			Map<Integer, List<EserviceTravelGroupDetails>> groupListGroupByGroupId = groupList.stream().filter( o -> o.getGroupId() !=null  ).collect( Collectors.groupingBy(EserviceTravelGroupDetails :: getGroupId )) ;
			
			// Framing List 
			List<TravelPassengerDetails>  saveDatas = new ArrayList<TravelPassengerDetails>();
			List<PolicyCoverDataIndividuals> totalIndiCovers = new ArrayList<PolicyCoverDataIndividuals>(); 
			List<EserviceTravelGroupDetails> updateGroups = new ArrayList<EserviceTravelGroupDetails>(); 
			Double totalPremium = covers.stream().filter( o ->  o.getTaxId().equals(0) && o.getDiscLoadId().equals(0) && o.getPremiumIncludedTaxLc()!=null && o.getPremiumIncludedTaxLc().doubleValue() > 0D ).mapToDouble( o ->   o.getPremiumIncludedTaxLc().doubleValue()  ).sum(); ; 
			
			for (Integer group :  groupByGroupId.keySet() ) {
				
				List<GroupFilterReq>  filterSameGroup  =  groupByGroupId.get(group);
				List<String>  filerPassportnos = filterSameGroup.stream().map(GroupFilterReq :: getPassportNo ) .collect(Collectors.toList());
				
				List<PassengerListSaveReq> filterReqPass = reqList.stream().filter( o -> filerPassportnos.contains(o.getPassportNo()) ).collect(Collectors.toList());
				List<PremiumGroupDevideRes> groupCovers =null;
				groupCovers = coverGroupByGroupId.get(group);
				if (groupCovers == null) {
					if ("3".equalsIgnoreCase(travelData.getPlanTypeId().toString()) && group == 1) {
						groupCovers = coverGroupByGroupId.get(2);
					}
				}
				EserviceTravelGroupDetails groupData =  groupListGroupByGroupId.get(group).get(0)  ;
				//String groupDesc =  groupData.getGroupDesc() ;
				Double groupPremiumFc = groupCovers.stream().filter( o -> o.getVehicleId().equals(groupData.getGroupId()) && o.getTaxId().equals(0) && o.getDiscLoadId().equals(0) && o.getPremiumExcludedTaxFc()!=null && o.getPremiumExcludedTaxFc().doubleValue() > 0D ).mapToDouble( o ->   o.getPremiumExcludedTaxFc().doubleValue()  ).sum();					
				Double groupOverAllPremiumFc = groupCovers.stream().filter( o -> o.getVehicleId().equals(groupData.getGroupId()) && o.getTaxId().equals(0) && o.getDiscLoadId().equals(0) && o.getPremiumIncludedTaxFc()!=null && o.getPremiumIncludedTaxFc().doubleValue() > 0D ).mapToDouble( o ->   o.getPremiumIncludedTaxFc().doubleValue()   ).sum();
				Double groupPremiumLc = groupCovers.stream().filter( o -> o.getVehicleId().equals(groupData.getGroupId()) && o.getTaxId().equals(0) && o.getDiscLoadId().equals(0) && o.getPremiumExcludedTaxLc()!=null && o.getPremiumExcludedTaxLc().doubleValue() > 0D ).mapToDouble( o ->   o.getPremiumExcludedTaxLc().doubleValue() *exchangeRate  ).sum();					
				Double groupOverAllPremiumLc = groupCovers.stream().filter( o -> o.getVehicleId().equals(groupData.getGroupId()) && o.getTaxId().equals(0) && o.getDiscLoadId().equals(0) && o.getPremiumIncludedTaxLc()!=null && o.getPremiumIncludedTaxLc().doubleValue() > 0D ).mapToDouble( o ->   o.getPremiumIncludedTaxLc().doubleValue() *exchangeRate  ).sum();
				totalPremium = totalPremium +  groupOverAllPremiumFc; 
				groupData.setActualPremiumFc(groupPremiumFc  == 0D ? new BigDecimal(0) : new BigDecimal(df.format(groupPremiumFc)));
				groupData.setActualPremiumLc(groupPremiumLc  == 0D ? new BigDecimal(0) : new BigDecimal(df.format(groupPremiumLc)));
				groupData.setOverallPremiumFc(groupOverAllPremiumFc  == 0D ? new BigDecimal(0) : new BigDecimal(df.format(groupOverAllPremiumFc)));
				groupData.setOverallPremiumLc(groupOverAllPremiumLc  == 0D ? new BigDecimal(0) : new BigDecimal(df.format(groupOverAllPremiumLc)));
				updateGroups.add(groupData);
				
				List<PremiumGroupDevideRes> getDividedCovers = getDevidedCovers(quoteNo ,  groupData.getGroupMembers(), groupCovers ) ;
				PremiumGroupDevideRes coverData = getDividedCovers.get(0);
				
				Double premiumFc = getDividedCovers.stream().filter( o -> o.getTaxId().equals(0) && o.getDiscLoadId().equals(0) && o.getPremiumExcludedTaxFc()!=null && o.getPremiumExcludedTaxFc().doubleValue() > 0D ).mapToDouble( o ->   o.getPremiumExcludedTaxFc().doubleValue()  ).sum();					
				Double overAllPremiumFc = getDividedCovers.stream().filter( o ->  o.getTaxId().equals(0) && o.getDiscLoadId().equals(0) && o.getPremiumIncludedTaxFc()!=null && o.getPremiumIncludedTaxFc().doubleValue() > 0D ).mapToDouble( o ->   o.getPremiumIncludedTaxFc().doubleValue()  ).sum();
				Double premiumLc = getDividedCovers.stream().filter( o -> o.getTaxId().equals(0) && o.getTaxId().equals(0) &&o.getDiscLoadId().equals(0) && o.getPremiumExcludedTaxLc()!=null && o.getPremiumExcludedTaxLc().doubleValue() > 0D ).mapToDouble( o ->   o.getPremiumExcludedTaxLc().doubleValue() *exchangeRate  ).sum();					
				Double overAllPremiumLc = getDividedCovers.stream().filter( o -> o.getTaxId().equals(0) && o.getDiscLoadId().equals(0) && o.getPremiumIncludedTaxLc()!=null && o.getPremiumIncludedTaxLc().doubleValue() > 0D ).mapToDouble( o ->   o.getPremiumIncludedTaxLc().doubleValue() *exchangeRate ).sum();
				Double taxPremium = getDividedCovers.stream().filter( o -> !o.getTaxId().equals(0)  && o.getCoverageType().equalsIgnoreCase("T") && o.getTaxAmount()!=null  &&o.getTaxAmount().doubleValue() > 0D ).mapToDouble( o ->   o.getTaxAmount().doubleValue()  ).sum();
				
				for (PassengerListSaveReq data : filterReqPass) {

				   
				    List<EserviceTravelGroupDetails> matchedGroups = groupList.stream()
				        .filter(g -> g.getGroupId() != null && g.getGroupId().equals(group)) 
				        .collect(Collectors.toList());
				    
				    final int membersToInsert = matchedGroups.isEmpty() || matchedGroups.get(0).getGroupMembers() == null
				            ? 1
				            : matchedGroups.get(0).getGroupMembers();	  
				    

				    for (int i = 0; i < membersToInsert; i++) {
				        TravelPassengerDetails saveData = new TravelPassengerDetails();

				        String gender = genders.stream()
				                .filter(o -> o.getItemCode().equalsIgnoreCase(data.getGenderId()))
				                .collect(Collectors.toList()).get(0).getItemValue();
				        String relation = relations.stream()
				                .filter(o -> o.getItemCode().equalsIgnoreCase(data.getRelationId()))
				                .collect(Collectors.toList()).get(0).getItemValue();

				        Integer passengerId = 0;
				        if (StringUtils.isNotBlank(data.getPassengerId())) {
				            passengerId = Integer.valueOf(data.getPassengerId());
				        } else {
				            lastPassCount = lastPassCount + 1;
				            passengerId = lastPassCount;
				        }
				        Integer passCount = passengerId;

				        // Risk Details
				        TravelPassengerDetails riskDetails = setTravelRiskDetails(travelData);
				        modelMapper.map(riskDetails, saveData);

				        // Passenger Details
				        saveData.setQuoteNo(quoteNo);
				        saveData.setPassengerId(passengerId);
				        saveData.setPassengerFirstName(data.getPassengerFirstName());
				        saveData.setPassengerLastName(data.getPassengerLastName());
				        saveData.setPassengerName(data.getPassengerFirstName() + " " + data.getPassengerLastName());
				        saveData.setDob(data.getDob());
				        saveData.setGenderId(data.getGenderId());
				        saveData.setGenderDesc(gender);
				        LocalDate birthDate = data.getDob().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				        LocalDate currentDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				        int age = Period.between(birthDate, currentDate).getYears();
				        saveData.setAge(age);
				        saveData.setRelationId(Integer.valueOf(data.getRelationId()));
				        saveData.setRelationDesc(relation);
				        saveData.setGroupId(group);
				        saveData.setGroupCount(membersToInsert);
//				        saveData.setNationality(data.getNationality());
//				        saveData.setNationalityDesc(data.getNationality());
				        saveData.setPassportNo(data.getPassportNo());
				        saveData.setUpdatedBy(req.getCreatedBy());
				        saveData.setUpdatedDate(new Date());
				        Optional<CountryMaster> match = countryList.stream()
				                .filter(o ->o.getCompanyId().contains(homeData.getCompanyId()) && o.getCountryName().equalsIgnoreCase(data.getNationality()))
				                .findFirst();

				        saveData.setNationalityDesc(data.getNationality()); 
				        saveData.setNationality(match.map(CountryMaster::getCountryId).orElse(data.getNationality())); 
				        saveData.setStatus("Y");

				        // Cover Details
				        saveData.setCdRefno(coverData.getCdRefno());
				        saveData.setVdRefno(coverData.getVdRefno());
				        saveData.setMsRefno(coverData.getMsRefno());
				        saveData.setBasePremium(overAllPremiumFc == 0D ? new BigDecimal(0) : new BigDecimal(df.format(overAllPremiumFc)));
				        saveData.setCommissionPercentage(homeData.getCommissionPercentage());
				        saveData.setCommissionType(homeData.getCommissionType());
				        saveData.setCommissionTypeDesc(homeData.getCommissionTypeDesc());
				        saveData.setActualPremiumFc(premiumFc == 0D ? new BigDecimal(0) : new BigDecimal(df.format(premiumFc)));
				        saveData.setActualPremiumLc(premiumLc == 0D ? new BigDecimal(0) : new BigDecimal(df.format(premiumLc)));
				        saveData.setOverallPremiumFc(overAllPremiumFc == 0D ? new BigDecimal(0) : new BigDecimal(df.format(overAllPremiumFc)));
				        saveData.setOverallPremiumLc(overAllPremiumLc == 0D ? new BigDecimal(0) : new BigDecimal(df.format(overAllPremiumLc)));
				        saveData.setVatCommission(homeData.getVatCommission());
				        saveData.setTotalPremium(totalPremium == 0D ? new BigDecimal(0) : new BigDecimal(df.format(totalPremium)));
				        saveData.setVatPremium(taxPremium == 0D ? new BigDecimal(0) : new BigDecimal(df.format(taxPremium)));
				        saveData.setLocationId(1);
				        saveData.setLcExchangeRate(lcExchangeRate);
				        
				        boolean alreadyExists = saveDatas.stream().anyMatch(p -> 
				        p.getPassportNo().equalsIgnoreCase(data.getPassportNo()) &&
				        p.getGroupId().equals(group)
				        		);

				    	if (!alreadyExists) {
				    	    saveDatas.add(saveData);
				    	}

				        // Covers
				        List<PolicyCoverDataIndividuals> indiCovers = new ArrayList<>();
				        Type listType2 = new TypeToken<List<PolicyCoverDataIndividuals>>() {}.getType();
				        indiCovers = modelMapper.map(getDividedCovers, listType2);
				        indiCovers.forEach(o -> {
				            o.setGroupId(group);
				            o.setGroupCount(membersToInsert);
				            o.setVehicleId(passCount);
				            o.setIndividualId(passCount);
				            o.setLocationId(1);
				        });
				        totalIndiCovers.addAll(indiCovers);
				    }
				}

				 
			}
			
			// Save All Passengers
			List<Integer> passengerIds = saveDatas.stream().map(TravelPassengerDetails :: getPassengerId ).collect(Collectors.toList()) ;
			// Save All Passengers
			Long oldPass = repo.countByQuoteNoAndPassengerIdInAndStatusNot(quoteNo,passengerIds,"D");
			if(oldPass > 0 ) {
				repo.deleteByQuoteNoAndPassengerIdInAndStatusNot(quoteNo, passengerIds, "D");
			}
			repo.saveAllAndFlush(saveDatas);
			
			// Deactivate Other passengers and Remvoe Covers
			updateRemovedPassengers(quoteNo , passengerIds , travelData.getTravelStartDate());
			
			// Save Divided Indi Covers 
			indiCoverRepo.saveAllAndFlush(totalIndiCovers);
			
			
			
		   // Copy Old Doc
			OldDocumentsCopyReq oldDocCopyReq = new OldDocumentsCopyReq();
			oldDocCopyReq.setInsuranceId(insuranceId);
			oldDocCopyReq.setProductId(productId);
			oldDocCopyReq.setSectionId(sectionId);
			oldDocCopyReq.setQuoteNo(quoteNo);
			docService.copyOldDocumentsToNewQuote(oldDocCopyReq);
			
			//uploaded Documents delete
			
			List<TravelPassengerDetails> pass = repo.findByQuoteNoAndProductIdAndCompanyId(quoteNo,Integer.valueOf(productId),insuranceId);
			List<DocumentTransactionDetails> doc1 = docTransRepo.findByQuoteNoAndProductIdAndCompanyId(quoteNo, Integer.valueOf(productId),insuranceId);
			
			if(pass.size()>0) {
				if(doc1.size()>0) {
					
					List<DocumentTransactionDetails> filterDoc = doc1.stream()
							.filter( d ->  pass.stream().filter( o -> ! o.getStatus().equalsIgnoreCase("D")) .map( TravelPassengerDetails :: getSectionId).anyMatch(  
									e ->    e.equals( d.getSectionId() )))
							.filter( d -> pass.stream().filter( o -> ! o.getStatus().equalsIgnoreCase("D")).map( TravelPassengerDetails :: getPassportNo).anyMatch(  
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
			
		
			
			res.setSuccessId("1");
			res.setResponse("Updated Successfully");
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Log Details"+e.getMessage());
			return null;
		}
		return res;
	}
	public List<PremiumGroupDevideRes> getDevidedCovers(String quoteNo , Integer groupCount , List<PremiumGroupDevideRes> groupCovers ) {
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
	
	public String updateRemovedPassengers(String quoteNo  ,List<Integer> passengerIds , Date startDate ) {
		String res = "" ;
		try {
			{
				// Update Deactivated Passengers
				CriteriaBuilder cb = em.getCriteriaBuilder();
				// create update
				CriteriaUpdate<TravelPassengerDetails> update = cb.createCriteriaUpdate(TravelPassengerDetails.class);
				// set the root class
				Root<TravelPassengerDetails> m = update.from(TravelPassengerDetails.class);
				update.set("status","D");
				//In 
				Expression<String>e0= m.get("passengerId");
				Predicate n1 = cb.equal(m.get("quoteNo"),quoteNo );
				Predicate n2 = e0.in(passengerIds).not();
				update.where(n1,n2);
				em.createQuery(update).executeUpdate();
			
			}
			
			// Remove Deactivated Covers
			Long count = indiCoverRepo.countByQuoteNoAndVehicleIdIn(quoteNo ,passengerIds);
			if(count > 0 ) {
				indiCoverRepo.deleteByQuoteNoAndVehicleIdIn(quoteNo ,passengerIds);
			}
			{
				// Date Diffrence
				Date periodStart = startDate;
				Date endDate =  new Date();
				Long daysBetween = 0L ;
				String diff = "" ;
				
				Long diffInMillies = Math.abs(endDate.getTime() - periodStart.getTime());
				daysBetween =  TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS) + 1 ;
					
				diff = String.valueOf( daysBetween);
				
				// Update Deactivated Passengers
				CriteriaBuilder cb = em.getCriteriaBuilder();
				// create update
				CriteriaUpdate<PolicyCoverDataIndividuals> update = cb.createCriteriaUpdate(PolicyCoverDataIndividuals.class);
				// set the root class
				Root<PolicyCoverDataIndividuals> m = update.from(PolicyCoverDataIndividuals.class);
				update.set("status","D");
				update.set("noOfDays",daysBetween);
				update.set("coverPeriodTo",endDate);
				//In 
				Expression<String>e0= m.get("vehicleId");
				Predicate n1 = cb.equal(m.get("quoteNo"),quoteNo );
				Predicate n2 = e0.in(passengerIds).not();
				update.where(n1,n2);
				em.createQuery(update).executeUpdate();
				
			}
	
			res = "Success" ;
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Log Details"+e.getMessage());
			return null;
		}
		return res;
	}
	
	public TravelPassengerDetails setTravelRiskDetails(EserviceTravelDetails travelData ) {
		TravelPassengerDetails saveData = new TravelPassengerDetails()  ;
		try {
			 // Risk Details 
			 saveData.setAdminLoginId(travelData.getAdminLoginId());
			 saveData.setAdminRemarks(travelData.getAdminRemarks());
			 saveData.setApplicationId(travelData.getApplicationId());
			 saveData.setBankCode(travelData.getBankCode());
			 saveData.setBranchCode(travelData.getBranchCode());
			 saveData.setBrokerBranchCode(travelData.getBrokerBranchCode());
			 saveData.setBrokerBranchName(travelData.getBrokerBranchName());
			 saveData.setBrokerCode(travelData.getBrokerCode());
			 saveData.setCompanyId(travelData.getCompanyId());
			 saveData.setCompanyName(travelData.getCompanyName());
			 saveData.setCurrency(travelData.getCurrency());
			 saveData.setCustomerCode(travelData.getCurrency());
			 saveData.setCustomerId(travelData.getCustomerId());
			 saveData.setCustomerReferenceNo(travelData.getCustomerReferenceNo());
			 saveData.setDestinationCountry(travelData.getDestinationCountry());
			 saveData.setDestinationCountryDesc(travelData.getDestinationCountryDesc());
			 saveData.setEffectiveDate(travelData.getTravelStartDate());
			 saveData.setExchangeRate(travelData.getExchangeRate()==null ? null : travelData.getExchangeRate());
			 saveData.setHavepromocode(travelData.getHavepromocode());
			 saveData.setLoginId(travelData.getLoginId());
			 saveData.setPlanTypeDesc(travelData.getPlanTypeDesc());
			 saveData.setPlanTypeId(travelData.getPlanTypeId());
			 saveData.setPolicyNo(travelData.getPolicyNo());
			 saveData.setProductId(Integer.valueOf(travelData.getProductId()));
			 saveData.setProductName(travelData.getProductName());
			 saveData.setPromocode(travelData.getPromocode());
			 saveData.setReferalRemarks(travelData.getReferalRemarks());
			 saveData.setRejectReason(travelData.getRejectReason());
			 saveData.setRemarks(travelData.getRemarks());
			 saveData.setRequestReferenceNo(travelData.getRequestReferenceNo());
			 saveData.setSectionId(Integer.valueOf(travelData.getSectionId()));
			 saveData.setSectionName(travelData.getSectionName());
			 saveData.setSourceCountry(travelData.getSourceCountry());
			 saveData.setSourceCountryDesc(travelData.getSourceCountryDesc());
			 saveData.setSourceType(travelData.getSourceType());
			 saveData.setStatus(travelData.getStatus());
			 saveData.setSubUserType(travelData.getSubUserType());
			 saveData.setTiraCoverNoteNo(travelData.getTiraCoverNoteNo());
			 saveData.setTotalPassengers(travelData.getTotalPassengers());
			 saveData.setTravelCoverDesc(travelData.getTravelCoverDesc());
			 saveData.setTravelCoverDuration(travelData.getTravelCoverDuration());
			 saveData.setTravelCoverId(travelData.getTravelCoverId());
			 saveData.setTravelEndDate(travelData.getTravelEndDate());
			 saveData.setTravelStartDate(travelData.getTravelStartDate());
			 saveData.setVatCommission(travelData.getVatCommission());
			 saveData.setTravelId(travelData.getRiskId());
			 
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Log Details"+e.getMessage());
			return null;
		}
		return saveData;
	}
	
	private  List<PolicyCoverDataIndividuals>  InsertCoverDetails(List<PolicyCoverData> covers  , TravelPassengerDetails passengerData ) {
		List<PolicyCoverDataIndividuals>  saveIndiCovers = new ArrayList<PolicyCoverDataIndividuals>();
		DozerBeanMapper dozerMapper = new DozerBeanMapper();
		try {
			// Save Cover Details
			
			for ( PolicyCoverData cov : covers) {
				PolicyCoverDataIndividuals indiCoverData  = new PolicyCoverDataIndividuals();
				dozerMapper.map(cov, indiCoverData);
				indiCoverData.setEntryDate(new Date());

				indiCoverData.setQuoteNo(passengerData.getQuoteNo());
				indiCoverData.setIsSelected(cov.getIsSelected().equalsIgnoreCase("N") ? "Y" :cov.getIsSelected());
				indiCoverData.setCreatedBy(passengerData.getCreatedBy());
				indiCoverData.setVehicleId(passengerData.getPassengerId());
				indiCoverData.setDiscountCoverId(cov.getDiscountCoverId()==null?0 :cov.getDiscountCoverId());
				indiCoverData.setIndividualId(passengerData.getPassengerId());
				indiCoverData.setGroupId(passengerData.getGroupId());
				indiCoverData.setGroupCount(passengerData.getGroupCount());
				
				saveIndiCovers.add(indiCoverData);
				
			}
				
		}catch (Exception e) {
			e.printStackTrace();
			log.error("Exception is ---> " + e.getMessage());
			
		}
	
		return saveIndiCovers;
	}
	
	private synchronized BigDecimal getDevidedValue(BigDecimal inputValue ,Integer groupCount ) {
		BigDecimal devidedValue = BigDecimal.ZERO ;
		try {
			devidedValue = inputValue.divide(new BigDecimal(groupCount),2, RoundingMode.HALF_UP) ;
	
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
	public List<TravelPassDetailsRes> getActivePassengers(TravelPassDetailsGetAllReq req) {
		List<TravelPassDetailsRes> resList = new ArrayList<TravelPassDetailsRes>();
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		try {
			List<TravelPassengerDetails> datas = repo.findByQuoteNoAndStatusNotOrderByPassengerIdAsc(req.getQuoteNo() , "D");
			Type listType = new TypeToken<List<TravelPassDetailsRes>>(){}.getType();
			//resList = modelMapper.map(datas,listType);
			 
			List<TravelPassengerDetails> adultList = datas.stream().filter( o -> o.getGroupId().equals(2) ).collect(Collectors.toList());
			resList.addAll(modelMapper.map(adultList,listType));
			 
			 
			List<TravelPassengerDetails> otherList =  datas.stream().filter( o -> ! o.getGroupId().equals(2) ).collect(Collectors.toList());
			resList.addAll(modelMapper.map(otherList,listType));
		}
		catch(Exception e) {
			e.printStackTrace();
			log.info("Log Details"+e.getMessage());
			return null;
		}
		return resList;
	}

	@Override
	public List<TravelPassDetailsRes> getRemovedPassengers(TravelPassDetailsGetAllReq req) {
		List<TravelPassDetailsRes> resList = new ArrayList<TravelPassDetailsRes>();
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		try {
			List<TravelPassengerDetails> datas = repo.findByQuoteNoAndStatusOrderByPassengerIdAsc(req.getQuoteNo() , "D");
			Type listType = new TypeToken<List<TravelPassDetailsRes>>(){}.getType();
			//resList = modelMapper.map(datas,listType);
			 
			List<TravelPassengerDetails> adultList = datas.stream().filter( o -> o.getGroupId().equals(2) ).collect(Collectors.toList());
			resList.addAll(modelMapper.map(adultList,listType));
			 
			 
			List<TravelPassengerDetails> otherList =  datas.stream().filter( o -> ! o.getGroupId().equals(2) ).collect(Collectors.toList());
			resList.addAll(modelMapper.map(otherList,listType));
			
		}
		catch(Exception e) {
			e.printStackTrace();
			log.info("Log Details"+e.getMessage());
			return null;
		}
		return resList;
	}

	@Override
	public List<Error> validateaddpassergerList(AddRemovedPassengerReq req) {
		List<Error> errors = new ArrayList<Error>();
		try {
			if (req.getPassengerIds()==null ||  req.getPassengerIds().size() <= 0 ) {
				errors.add(new Error("05", "PassengerList", "Please Enter Atleast One Passenger to Add "));
				
			}
		} catch(Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			errors.add(new Error("01","Common Error",e.getMessage() ));
		}
		return errors;
	}

	@Override
	public SuccessRes addRemovedPassengersAgain(AddRemovedPassengerReq req) {
		SuccessRes res = new SuccessRes();
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		try {
			
			// Primary Tables & Details 
			String quoteNo = req.getQuoteNo();
			Integer lastPassCount = Integer.valueOf( repo.countByQuoteNo(quoteNo).toString());
			List<PolicyCoverData>  coverList = coverRepo.findByQuoteNo(quoteNo);
			List<PremiumGroupDevideRes> covers = new ArrayList<PremiumGroupDevideRes>();
			 Type listType = new TypeToken<List<PremiumGroupDevideRes>>(){}.getType();
			 covers = modelMapper.map(coverList,listType);
			List<EserviceTravelGroupDetails> groupList = groupRepo.findByQuoteNoOrderByGroupIdAsc(quoteNo);  
			EserviceTravelDetails travelData = eserTraRepo.findByQuoteNo(quoteNo);
			TravelPassengerDetails riskDetails = setTravelRiskDetails(travelData);
			HomePositionMaster homeData = homeRepo.findByQuoteNo(quoteNo); 
			String insuranceId = homeData.getCompanyId() ;
			String productId = homeData.getProductId().toString();
			String sectionId = homeData.getSectionId().toString();
			String branchCode = homeData.getBranchCode() ;
			
			List<TravelPassengerDetails> passengers =  repo.findByQuoteNoAndPassengerIdInOrderByPassengerIdAsc(req.getQuoteNo() , req.getPassengerIds());
			
			// Currency Decimal Digit
			String decimalDigits = currencyDecimalFormat(homeData.getCompanyId() , homeData.getCurrency() ).toString();
			String stringFormat = "%0"+decimalDigits+"d" ;
			String decimalLength = decimalDigits.equals("0") ?"" : String.format(stringFormat ,0L)  ;
			String pattern = StringUtils.isBlank(decimalLength) ?  "#####0" :   "#####0." + decimalLength;
			DecimalFormat df = new DecimalFormat(pattern);
			
			// Grouping 
			Map<Integer, List<TravelPassengerDetails>> groupByGroupId = passengers.stream().filter( o -> o.getGroupId() !=null  ).collect( Collectors.groupingBy(TravelPassengerDetails :: getGroupId )) ;
			Map<Integer, List<PremiumGroupDevideRes>> coverGroupByGroupId = covers.stream().filter( o -> o.getVehicleId() !=null  ).collect( Collectors.groupingBy(PremiumGroupDevideRes :: getVehicleId )) ;		
			Map<Integer, List<EserviceTravelGroupDetails>> groupListGroupByGroupId = groupList.stream().filter( o -> o.getGroupId() !=null  ).collect( Collectors.groupingBy(EserviceTravelGroupDetails :: getGroupId )) ;
			
			// Framing List 
			List<TravelPassengerDetails>  saveDatas = new ArrayList<TravelPassengerDetails>();
			List<PolicyCoverDataIndividuals> totalIndiCovers = new ArrayList<PolicyCoverDataIndividuals>(); 
			List<EserviceTravelGroupDetails> updateGroups = new ArrayList<EserviceTravelGroupDetails>(); 
			Double totalPremium = covers.stream().filter( o ->  o.getTaxId().equals(0) && o.getDiscLoadId().equals(0) && o.getPremiumIncludedTaxLc()!=null && o.getPremiumIncludedTaxLc().doubleValue() > 0D ).mapToDouble( o ->   o.getPremiumIncludedTaxLc().doubleValue()  ).sum(); ; 
			
			for (Integer group :  groupByGroupId.keySet() ) {
				
				 
				List<TravelPassengerDetails> filterPassList = groupByGroupId.get(group);
				List<PremiumGroupDevideRes> groupCovers = coverGroupByGroupId.get(group);
				EserviceTravelGroupDetails groupData =  groupListGroupByGroupId.get(group).get(0)  ;
				Double groupPremiumFc = groupCovers.stream().filter( o -> o.getVehicleId().equals(groupData.getGroupId()) && o.getTaxId().equals(0) && o.getDiscLoadId().equals(0) && o.getPremiumExcludedTaxFc()!=null && o.getPremiumExcludedTaxFc().doubleValue() > 0D ).mapToDouble( o ->   o.getPremiumExcludedTaxFc().doubleValue()  ).sum();					
				Double groupOverAllPremiumFc = groupCovers.stream().filter( o -> o.getVehicleId().equals(groupData.getGroupId()) && o.getTaxId().equals(0) && o.getDiscLoadId().equals(0) && o.getPremiumIncludedTaxFc()!=null && o.getPremiumIncludedTaxFc().doubleValue() > 0D ).mapToDouble( o ->   o.getPremiumIncludedTaxFc().doubleValue()  ).sum();
				Double groupPremiumLc = groupCovers.stream().filter( o -> o.getVehicleId().equals(groupData.getGroupId()) && o.getTaxId().equals(0) && o.getDiscLoadId().equals(0) && o.getPremiumExcludedTaxLc()!=null && o.getPremiumExcludedTaxLc().doubleValue() > 0D ).mapToDouble( o ->   o.getPremiumExcludedTaxLc().doubleValue()  ).sum();					
				Double groupOverAllPremiumLc = groupCovers.stream().filter( o -> o.getVehicleId().equals(groupData.getGroupId()) && o.getTaxId().equals(0) && o.getDiscLoadId().equals(0) && o.getPremiumIncludedTaxLc()!=null && o.getPremiumIncludedTaxLc().doubleValue() > 0D ).mapToDouble( o ->   o.getPremiumIncludedTaxLc().doubleValue()  ).sum();
				totalPremium = totalPremium +  groupOverAllPremiumFc; 
				groupData.setActualPremiumFc(groupPremiumFc  == 0D ? new BigDecimal(0) : new BigDecimal(df.format(groupPremiumFc)));
				groupData.setActualPremiumLc(groupPremiumLc  == 0D ? new BigDecimal(0) : new BigDecimal(df.format(groupPremiumLc)));
				groupData.setOverallPremiumFc(groupOverAllPremiumFc  == 0D ? new BigDecimal(0) : new BigDecimal(df.format(groupOverAllPremiumFc)));
				groupData.setOverallPremiumLc(groupOverAllPremiumLc  == 0D ? new BigDecimal(0) : new BigDecimal(df.format(groupOverAllPremiumLc)));
				updateGroups.add(groupData);
				
				List<PremiumGroupDevideRes> getDividedCovers = getDevidedCovers(quoteNo ,  groupData.getGroupMembers(), groupCovers ) ;
				PremiumGroupDevideRes coverData = getDividedCovers.get(0);
				
				Double premiumFc = getDividedCovers.stream().filter( o -> o.getTaxId().equals(0) && o.getDiscLoadId().equals(0) && o.getPremiumExcludedTaxFc()!=null && o.getPremiumExcludedTaxFc().doubleValue() > 0D ).mapToDouble( o ->   o.getPremiumExcludedTaxFc().doubleValue()  ).sum();					
				Double overAllPremiumFc = getDividedCovers.stream().filter( o ->  o.getTaxId().equals(0) && o.getDiscLoadId().equals(0) && o.getPremiumIncludedTaxFc()!=null && o.getPremiumIncludedTaxFc().doubleValue() > 0D ).mapToDouble( o ->   o.getPremiumIncludedTaxFc().doubleValue()  ).sum();
				Double premiumLc = getDividedCovers.stream().filter( o -> o.getTaxId().equals(0) && o.getTaxId().equals(0) &&o.getDiscLoadId().equals(0) && o.getPremiumExcludedTaxLc()!=null && o.getPremiumExcludedTaxLc().doubleValue() > 0D ).mapToDouble( o ->   o.getPremiumExcludedTaxLc().doubleValue()  ).sum();					
				Double overAllPremiumLc = getDividedCovers.stream().filter( o -> o.getTaxId().equals(0) && o.getDiscLoadId().equals(0) && o.getPremiumIncludedTaxLc()!=null && o.getPremiumIncludedTaxLc().doubleValue() > 0D ).mapToDouble( o ->   o.getPremiumIncludedTaxLc().doubleValue()  ).sum();
				
				 for(TravelPassengerDetails data : filterPassList ) {
					 TravelPassengerDetails saveData = new TravelPassengerDetails();
					 lastPassCount  =  lastPassCount + 1 ; 
					 Integer passengerId =  lastPassCount ; 
					 
					 // Riks Details 
					 saveData = riskDetails ; 
					 
					 // Passenger Details 
					 saveData.setQuoteNo(quoteNo);
					 saveData.setPassengerId(passengerId);
					 saveData.setPassengerFirstName(data.getPassengerFirstName());
					 saveData.setPassengerLastName(data.getPassengerLastName());
					 saveData.setPassengerName(data.getPassengerFirstName() + " " + data.getPassengerLastName() );
					 saveData.setDob(data.getDob());
					 saveData.setGenderId(data.getGenderId());
					 saveData.setGenderDesc(data.getGenderDesc());
					 saveData.setAge(Integer.valueOf(data.getAge()));
					 //saveData.setAddress1("");
					 //saveData.setAddress2("");
					 saveData.setRelationId(Integer.valueOf(data.getRelationId()));
					 saveData.setRelationDesc(data.getRelationDesc());
					 saveData.setGroupId(groupData.getGroupMembers());
					 saveData.setGroupCount(group);
					 saveData.setNationality(data.getNationality());
					 saveData.setPassportNo(data.getPassportNo());
					 saveData.setCivilId(data.getCivilId());
					 saveData.setUpdatedBy(data.getCreatedBy());
					 saveData.setUpdatedDate(new Date());
					 saveData.setNationalityDesc(data.getNationalityDesc());
					  
					 // Cover Details 
					 saveData.setCdRefno(coverData.getCdRefno());
					 saveData.setVdRefno(coverData.getVdRefno());
					 saveData.setMsRefno(coverData.getMsRefno());
					 saveData.setBasePremium(overAllPremiumFc  == 0D ? new BigDecimal(0) :new BigDecimal(df.format(overAllPremiumFc)));
					 saveData.setCommissionPercentage(homeData.getCommissionPercentage());
					 saveData.setCommissionType(homeData.getCommissionType());
					 saveData.setCommissionTypeDesc(homeData.getCommissionTypeDesc());
					 saveData.setActualPremiumFc(premiumFc  == 0D ? new BigDecimal(0) : new BigDecimal(df.format(premiumFc)) );
					 saveData.setActualPremiumLc(premiumLc  == 0D ? new BigDecimal(0) :new BigDecimal(df.format(premiumLc)));
					 saveData.setOverallPremiumFc(overAllPremiumFc  == 0D ? new BigDecimal(0) :new BigDecimal(df.format(overAllPremiumFc)));
					 saveData.setOverallPremiumLc(overAllPremiumLc  == 0D ? new BigDecimal(0) : new BigDecimal(df.format(overAllPremiumLc)));
					 saveData.setVatCommission(homeData.getVatCommission());
					 saveData.setTotalPremium(totalPremium  == 0D ? new BigDecimal(0) :new BigDecimal(df.format(totalPremium)));
					 
					 saveDatas.add(saveData);
					 
					 // Covers
					 List<PolicyCoverDataIndividuals> indiCovers = new ArrayList<PolicyCoverDataIndividuals>(); 
					 Type listType2 = new TypeToken<List<PolicyCoverDataIndividuals>>(){}.getType();
					 indiCovers = modelMapper.map(getDividedCovers,listType2);
					 indiCovers.forEach( o ->  {
						 o.setGroupId(group);
						 o.setGroupCount(groupData.getGroupMembers());
						 o.setVehicleId(passengerId);
						 o.setIndividualId(passengerId);
					 });					
					 totalIndiCovers.addAll(indiCovers);
				}
				 
			}
			
			// Save All Passengers
			repo.saveAllAndFlush(saveDatas);
			
			// Save Divided Indi Covers 
			indiCoverRepo.saveAllAndFlush(totalIndiCovers);
			
			// Deactivate Other passengers and Remvoe Covers
		//	List<Integer> passengerIds = saveDatas.stream().map(TravelPassengerDetails :: getPassengerId ).collect(Collectors.toList()) ;
			//updateRemovedPassengers(quoteNo , passengerIds );
			
		   // Copy Old Doc
			OldDocumentsCopyReq oldDocCopyReq = new OldDocumentsCopyReq();
			oldDocCopyReq.setInsuranceId(insuranceId);
			oldDocCopyReq.setProductId(productId);
			oldDocCopyReq.setSectionId(sectionId);
			oldDocCopyReq.setQuoteNo(quoteNo);
			docService.copyOldDocumentsToNewQuote(oldDocCopyReq);
			
			//uploaded Documents delete
			
			List<TravelPassengerDetails> pass = repo.findByQuoteNoAndProductIdAndCompanyId(quoteNo,Integer.valueOf(productId),insuranceId);
			List<DocumentTransactionDetails> doc1 = docTransRepo.findByQuoteNoAndProductIdAndCompanyId(quoteNo, Integer.valueOf(productId),insuranceId);
			
			if(pass.size()>0) {
				if(doc1.size()>0) {
					
					List<DocumentTransactionDetails> filterDoc = doc1.stream()
							.filter( d ->  pass.stream().filter( o -> ! o.getStatus().equalsIgnoreCase("D")) .map( TravelPassengerDetails :: getSectionId).anyMatch(  
									e ->    e.equals( d.getSectionId() )))
							.filter( d -> pass.stream().filter( o -> ! o.getStatus().equalsIgnoreCase("D")).map( TravelPassengerDetails :: getPassportNo).anyMatch(  
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
			
		
			
			res.setSuccessId("1");
			res.setResponse("Updated Successfully");
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Log Details"+e.getMessage());
			return null;
		}
		return res;
	}

	@Override
	public SuccessRes deleteallpassdetails(TravelPassDetailsGetAllReq req) {
		SuccessRes res = new SuccessRes();
		try {

				List<TravelPassengerDetails> datas = repo.findByQuoteNoAndStatusNotOrderByPassengerIdAsc(req.getQuoteNo(),"D"); 
				if(datas!=null && datas.size() > 0 ) {
					datas.forEach( data -> {
						data.setStatus("D");
					});					
				}
				repo.saveAll(datas);
				res.setResponse("Deleted Successfully");
				res.setSuccessId("");
				
		} catch(Exception e) {
		e.printStackTrace();
		log.info("Log Details"+e.getMessage());
		return null;
	}
	return res;
	}
}
