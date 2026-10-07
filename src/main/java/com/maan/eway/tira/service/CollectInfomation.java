package com.maan.eway.tira.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.eway.bean.CompanyProductMaster;
import com.maan.eway.bean.EndtTypeMaster;
import com.maan.eway.bean.HomePositionMaster;
import com.maan.eway.bean.InsuranceCompanyMaster;
import com.maan.eway.bean.ListItemValue;
import com.maan.eway.bean.LoginUserInfo;
import com.maan.eway.bean.MotorDataDetails;
import com.maan.eway.bean.MotorVehicleInfo;
import com.maan.eway.bean.PaymentDetail;
import com.maan.eway.bean.PersonalInfo;
import com.maan.eway.bean.PolicyCoverData;
import com.maan.eway.bean.ProductSectionMaster;
import com.maan.eway.bean.SectionDataDetails;
import com.maan.eway.bean.TiraTrackingDetails;
import com.maan.eway.common.res.TiraAcknowledgement;
import com.maan.eway.common.res.TiraHistory;
import com.maan.eway.common.res.TiraPushedData;
import com.maan.eway.config.thread.MyTaskList;
import com.maan.eway.req.fleet.FleetDtl;
import com.maan.eway.req.fleet.FleetHdr;
import com.maan.eway.req.fleet.TiraMsgVehiclePushFleet;
import com.maan.eway.req.push.TiraMsgCoverPush;
import com.maan.eway.req.push.TiraMsgVehiclePush;
import com.maan.eway.tira.bean.MaansarovarToTira;
@Service
public class CollectInfomation {

	@Autowired
	private QuoteInfoUtil quote;
	
	List<Callable<Object>> queue = new ArrayList<Callable<Object>>();
	
	@Autowired
	private ConvertTiraRequest covernt;
	
	@Autowired
	private ConvertTiraFleetRequest convertF;
	
	
	public Object collectInfo(String quoteNo,String riskId,String locationId, String sectionId) {
		try {
			HomePositionMaster hp=quote.getFromHomePositionMaster(quoteNo);
			//="1";	
			
			PaymentDetail pd = quote.getpaymentDetail(quoteNo);
			
			String paymode = pd != null ? pd.getPaymentType() : hp.getPaymentMode();
			
			List<SectionDataDetails> data = quote.getFromSectionDataDetails(quoteNo,riskId,locationId,sectionId);
			hp.setSectionId(Integer.parseInt(data.get(0).getSectionId()));

			
			List<Callable<Object>> queue = new ArrayList<Callable<Object>>();


			Callable<Object> q1 = new Callable<Object>(){
				@Override
				public Object call() throws Exception {

					return quote.getFromCompanyProductMaster(hp.getCompanyId(),hp.getProductId().toString());
				}
			};
			queue.add(q1);

			Callable<Object> q2 = new Callable<Object>(){
				@Override
				public Object call() throws Exception {

					return quote.getFromInsuranceCompanyMaster(hp.getCompanyId());
				}
			};
			queue.add(q2);

			Callable<Object> q3 = new Callable<Object>(){
				@Override
				public Object call() throws Exception {

					return quote.getLogInUserDetails(hp.getLoginId(), hp.getCompanyId());
				}
			};
			queue.add(q3);
			if(hp.getApplicationId()!=null && !(hp.getApplicationId().equals("1")||  hp.getApplicationId().equals("01") ) ) {
				Callable<Object> q4 = new Callable<Object>(){
					@Override
					public Object call() throws Exception {

						return quote.getLogInUserDetails(hp.getApplicationId(), hp.getCompanyId());
					}
				};
				queue.add(q4);
			}
			Callable<Object> q5 = new Callable<Object>(){
				@Override
				public Object call() throws Exception {

					return quote.getFromPolicyCoverData(quoteNo,Integer.parseInt(riskId),locationId,sectionId);
				}
			};
			queue.add(q5);
			System.out.println("Collect Info ListItemValue "+"PAYMENT_MODE"+ paymode  + hp.getCompanyId());
			Callable<Object> q6 = new Callable<Object>(){
				@Override
				public Object call() throws Exception {
					
					return quote.getFromListItemValue("PAYMENT_MODE", paymode, hp.getCompanyId());
				}
			};
			queue.add(q6);

			Callable<Object> q7 = new Callable<Object>(){
				@Override
				public Object call() throws Exception {

					return quote.getFromPresonalInfo(hp.getCompanyId(),hp.getCustomerId());
				}
			};
			queue.add(q7);

			Callable<Object> q8 = new Callable<Object>(){
				@Override
				public Object call() throws Exception {

					return quote.getFromMotorVehicleInfo(hp.getQuoteNo(),riskId, hp.getCompanyId());
				}
			};
			queue.add(q8);

			Callable<Object> q9 = new Callable<Object>(){
				@Override
				public Object call() throws Exception {
					return quote.getFromSectionMaster(hp.getCompanyId(),hp.getProductId(),hp.getSectionId());
				}
			};
			queue.add(q9);
			//getFromListItemValue

			Callable<Object> q10 = new Callable<Object>(){
				@Override
				public Object call() throws Exception {

					return quote.getFromEndtMaster(hp.getCompanyId(),hp.getProductId(),hp.getEffectiveDate(),hp.getEndtTypeId());
				}
			};
			queue.add(q10);
			if(hp.getProductId().doubleValue()==46D || hp.getProductId().doubleValue()==5D) {
			
			Callable<Object> q11 = new Callable<Object>(){
				@Override
				public Object call() throws Exception {

					return quote.getFromMotorDataDetails(hp.getQuoteNo(),riskId);
				}
			};
			queue.add(q11);
		}

			InsuranceCompanyMaster companyId=null;
			CompanyProductMaster product=null;
			LoginUserInfo broker=null;
			LoginUserInfo uw=null;
			List<PolicyCoverData> covers=null;
			PersonalInfo customerInfo=null;
			MotorVehicleInfo vehicleInfo=null;
			ListItemValue l=null;
			ProductSectionMaster p=null;
			EndtTypeMaster endt=null;
			 MotorDataDetails mdd=null;
			if(!queue.isEmpty()) {
				MyTaskList taskList = new MyTaskList(queue);		
				ForkJoinPool forkjoin = new ForkJoinPool((queue.size()>1 ? (queue.size()>10)?10:(int )(queue.size()/2) : 1)); 
				ConcurrentLinkedQueue<Future<Object>> invoke  = (ConcurrentLinkedQueue<Future<Object>>) forkjoin.invoke(taskList) ;
				int success=0;
				for (Future<Object> callable : invoke) {
					System.out.println(callable.getClass() + "," + callable.isDone());
					if (callable.isDone()) {
						try {

							Object object = callable.get();
							if(object instanceof InsuranceCompanyMaster  ) {
								companyId= (InsuranceCompanyMaster) object;
							}else if(object instanceof CompanyProductMaster  ) {
								product= (CompanyProductMaster) object;
							}else if(object instanceof LoginUserInfo  ) {
								LoginUserInfo o = (LoginUserInfo) object;
								if(o !=null && o.getLoginId().equals(hp.getLoginId())) {
									broker=o;
								}else if(o !=null && o.getLoginId().equals(hp.getApplicationId())) {
									uw=o;
								} 
							}else if(object instanceof ListItemValue){
								l=(ListItemValue) object;
							}else if (object instanceof PersonalInfo){
								customerInfo=(PersonalInfo) object;
							}else if( object instanceof MotorVehicleInfo) {
								vehicleInfo = (MotorVehicleInfo) object;
							}else if(object instanceof ProductSectionMaster){
								p=(ProductSectionMaster) object;
							}else if(object instanceof EndtTypeMaster){
								endt=(EndtTypeMaster) object;
							}else if(object instanceof MotorDataDetails){
								mdd =(MotorDataDetails) object;
							}else {
								covers=(List<PolicyCoverData>) object;
							}

						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ExecutionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

			}

			MaansarovarToTira tira=MaansarovarToTira.builder()
					.company(companyId)
					.product(product)
					.covers(covers)
					.broker(broker)
					.uw(uw)
					.vehicleInfo(vehicleInfo)
					.customerInfo(customerInfo)	
					.ltPayment(l)
					.policy(hp)
					.section(p)
					.endt(endt)
					.mdd(mdd)
					.sectionData(data)
					.build();

			if("M".equals(p.getMotorYn())) {
				System.out.println("tira collectInfo Req:" + tira.getPolicy());
				TiraMsgVehiclePush	convert=covernt.convert(tira);
				System.out.println("tira collectInfo Res:" + convert.getMotorCoverNoteRefReq());
				return convert;
			}else {
				System.out.println("tira collectInfo Req Non motor:" + tira.getPolicy());
				TiraMsgCoverPush convert=covernt.convertDomestic(tira);
				System.out.println("tira collectInfo Req Non motor:" + tira.getPolicy());
				return convert;
			}
		}catch (Exception e) {
			e.printStackTrace();
			
		}
		return null;
	}

	public  <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
		Map<Object, Boolean> uniqueMap = new ConcurrentHashMap<>();
		return t -> uniqueMap.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}
	public Object postedInfo(String quoteNo) {
		try {
			List<Callable<Object>> queue = new ArrayList<Callable<Object>>();
			
			Callable<Object> q1 = new Callable<Object>(){
				@Override
				public Object call() throws Exception {

					return quote.getFromHomePositionMaster(quoteNo);
				}
			};
			queue.add(q1);
			
			Callable<Object> q2 = new Callable<Object>(){
				@Override
				public Object call() throws Exception {

					return quote.getFromTiraTrackingDetails(quoteNo);
				}
			};
			queue.add(q2);
			
			HomePositionMaster hp=null;
			List<TiraTrackingDetails> datas=null;
			if(!queue.isEmpty()) {
				MyTaskList taskList = new MyTaskList(queue);		
				ForkJoinPool forkjoin = new ForkJoinPool((queue.size()>1 ? (queue.size()>10)?10:(int )(queue.size()/2) : 1)); 
				ConcurrentLinkedQueue<Future<Object>> invoke  = (ConcurrentLinkedQueue<Future<Object>>) forkjoin.invoke(taskList) ;
				int success=0;
				for (Future<Object> callable : invoke) {
					System.out.println(callable.getClass() + "," + callable.isDone());
					if (callable.isDone()) {
						try {
							Object object = callable.get();
							if(object instanceof HomePositionMaster  ) {
								hp= (HomePositionMaster) object;
							}else {
								datas=(List<TiraTrackingDetails>) object;
							}
						}catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
				
			if(hp!=null) {
				List<TiraHistory> historys=new ArrayList<TiraHistory>();
				if(datas!=null && datas.size()>0) {
					List<TiraTrackingDetails> distinctData = datas.stream().filter(distinctByKey(d -> d.getRequestId())).collect(Collectors.toList());

					for (TiraTrackingDetails t : distinctData) {

						List<TiraTrackingDetails> request = datas.stream().filter(p -> p.getRequestId().equals(t.getRequestId()) && p.getResponseId().equals("NOT FOUND") ).collect(Collectors.toList());
						for(TiraTrackingDetails a :request) {

							List<TiraTrackingDetails> acknow = datas.stream().filter(p -> p.getRequestId().equals(t.getRequestId()) &&p.getAcknowledgementId().equals(a.getAcknowledgementId())
									&& p.getMethodName().contains("/acknowledge") ).collect(Collectors.toList());

							TiraAcknowledgement tak=null;
							for(TiraTrackingDetails ax :acknow) {
								tak=TiraAcknowledgement.builder()
										.requestFilePath(ax.getRequestFilePath())
										.responseFilePath(ax.getResponseFilePath())
										.responseId(ax.getResponseId())
										.responseStatusCode(ax.getStatusCode())
										.responseStatusDesc(ax.getStatusDesc())
										.status(ax.getStatus())								
										.build();
							}

							TiraHistory histor=TiraHistory.builder()
									.requestFilePath(a.getRequestFilePath())
									.responseFilePath(a.getResponseFilePath())
									.requestId(a.getRequestId())
									.requestStatusCode(a.getStatusCode())
									.requestStatusDesc(a.getStatusDesc())
									.status(a.getStatus())				
									.acknowledgementId(a.getAcknowledgementId())
									.acknowledgement(tak)
									.build();
							historys.add(histor);
						}

					}
				}
				Boolean status=("TIRA001".equals(hp.getResponseStatusCode())
						||
						("TIRA214".equals(hp.getResponseStatusCode()) 
								&& "Transaction successfully cancelled".equalsIgnoreCase(hp.getResponseStatusDesc())))?true:false;	
				TiraPushedData d=TiraPushedData.builder()
						.quoteNo(hp.getQuoteNo())
						.policyNo(hp.getPolicyNo())
						.coverNoteNo(hp.getCoverNoteReferenceNo())
						.isPosted(status)
						.prevCoverNoteNo(hp.getPrevCovernoteRefno())						
						.recentStatusCode(hp.getResponseStatusCode())
						.recentStatusDesc(hp.getResponseStatusDesc())
						.request_Id_Successful(hp.getTiraRequestId())
						.response_Id_Successful(hp.getTiraResponseId())
						.stickerNumber(hp.getStickerNumber())						
						.history(historys)
						.build();
				return d;
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Object collectInfoForFleet(String quoteNo) {
		try {	

			HomePositionMaster hp=quote.getFromHomePositionMaster(quoteNo);
	 	
			

			List<Callable<Object>> queue = new ArrayList<Callable<Object>>();
			Callable<Object> q1 = new Callable<Object>(){
				@Override
				public Object call() throws Exception {

					return quote.getFromCompanyProductMaster(hp.getCompanyId(),hp.getProductId().toString());
				}
			};
			queue.add(q1);

			Callable<Object> q2 = new Callable<Object>(){
				@Override
				public Object call() throws Exception {

					return quote.getFromInsuranceCompanyMaster(hp.getCompanyId());
				}
			};
			queue.add(q2);

			Callable<Object> q3 = new Callable<Object>(){
				@Override
				public Object call() throws Exception {

					return quote.getLogInUserDetails(hp.getLoginId(), hp.getCompanyId());
				}
			};
			queue.add(q3);
			if(hp.getApplicationId()!=null && !(hp.getApplicationId().equals("1")||  hp.getApplicationId().equals("01") ) ) {
				Callable<Object> q4 = new Callable<Object>(){
					@Override
					public Object call() throws Exception {

						return quote.getLogInUserDetails(hp.getApplicationId(), hp.getCompanyId());
					}
				};
				queue.add(q4);
			}
			Callable<Object> q5 = new Callable<Object>(){
				@Override
				public Object call() throws Exception {

					return quote.getFromListItemValue("PAYMENT_MODE", hp.getPaymentMode(), hp.getCompanyId());
				}
			};
			queue.add(q5);

			Callable<Object> q6 = new Callable<Object>(){
				@Override
				public Object call() throws Exception {

					return quote.getFromPresonalInfo(hp.getCompanyId(),hp.getCustomerId());
				}
			};
			queue.add(q6);
			
			Callable<Object> q7 = new Callable<Object>(){
				@Override
				public Object call() throws Exception {

					return quote.getFromSectionDataDetails(quoteNo);
				}
			};
			queue.add(q7);
			
			Callable<Object> q11 = new Callable<Object>(){
				@Override
				public Object call() throws Exception {

					return quote.getFromEndtMaster(hp.getCompanyId(),hp.getProductId(),hp.getEffectiveDate(),hp.getEndtTypeId());
				}
			};
			queue.add(q11);
			

			InsuranceCompanyMaster companyId=null;
			CompanyProductMaster product=null;
			LoginUserInfo broker=null;
			LoginUserInfo uw=null;
			ListItemValue l=null;
			PersonalInfo customerInfo=null;			
			List<SectionDataDetails> sections=null;
			EndtTypeMaster endt=null;
			if(!queue.isEmpty()) {
				MyTaskList taskList = new MyTaskList(queue);		
				ForkJoinPool forkjoin = new ForkJoinPool((queue.size()>1 ? (queue.size()>10)?10:(int )(queue.size()/2) : 1)); 
				ConcurrentLinkedQueue<Future<Object>> invoke  = (ConcurrentLinkedQueue<Future<Object>>) forkjoin.invoke(taskList) ;
				int success=0;
				for (Future<Object> callable : invoke) {
					System.out.println(callable.getClass() + "," + callable.isDone());
					if (callable.isDone()) {
						try {

							Object object = callable.get();
							if(object instanceof InsuranceCompanyMaster  ) {
								companyId= (InsuranceCompanyMaster) object;
							}else if(object instanceof CompanyProductMaster  ) {
								product= (CompanyProductMaster) object;
							}else if(object instanceof LoginUserInfo  ) {
								LoginUserInfo o = (LoginUserInfo) object;
								if(o !=null && o.getLoginId().equals(hp.getLoginId())) {
									broker=o;
								}else if(o !=null && o.getLoginId().equals(hp.getApplicationId())) {
									uw=o;
								} 
							}else if(object instanceof ListItemValue){
								l=(ListItemValue) object;
							}else if (object instanceof PersonalInfo){
								customerInfo=(PersonalInfo) object;
							}else if(object instanceof EndtTypeMaster){
								endt=(EndtTypeMaster) object;
							}else {
								sections=(List<SectionDataDetails>) object;
							}
							
							
							

						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ExecutionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

			}
			
			
			
			
			List<FleetDtl> riskDatas=new ArrayList<FleetDtl>();
			Integer noofComp=0;
			queue.clear();
			for(int i=0;sections!=null &&  i<sections.size();i++) {
						
				SectionDataDetails section = sections.get(i);
				Callable<Object> q8 = new Callable<Object>(){
					@Override
					public Object call() throws Exception {

						return quote.getFromPolicyCoverData(quoteNo,section.getRiskId(),String.valueOf(section.getLocationId()),section.getSectionId() );
					}
				};
				queue.add(q8);
				

				Callable<Object> q9 = new Callable<Object>(){
					@Override
					public Object call() throws Exception {

						return quote.getFromMotorVehicleInfo(hp.getQuoteNo(),String.valueOf(section.getRiskId()),hp.getCompanyId());
					}
				};
				queue.add(q9);

				Callable<Object> q10 = new Callable<Object>(){
					@Override
					public Object call() throws Exception {
						return quote.getFromSectionMaster(hp.getCompanyId(),hp.getProductId(),Integer.parseInt(section.getSectionId()));
					}
				};
				queue.add(q10); 
				
				
				
				Callable<Object> q12 = new Callable<Object>(){
					@Override
					public Object call() throws Exception {

						return quote.getFromMotorDataDetails(hp.getQuoteNo(),String.valueOf(section.getRiskId()));
					}
				};
				queue.add(q12);
				
				
				List<PolicyCoverData> covers=null;
 				MotorVehicleInfo vehicleInfo=null;				
				ProductSectionMaster p=null;
				MotorDataDetails mdd=null;
				
				if(!queue.isEmpty()) {
					MyTaskList taskList = new MyTaskList(queue);		
					ForkJoinPool forkjoin = new ForkJoinPool((queue.size()>1 ? (queue.size()>10)?10:(int )(queue.size()/2) : 1)); 
					ConcurrentLinkedQueue<Future<Object>> invoke  = (ConcurrentLinkedQueue<Future<Object>>) forkjoin.invoke(taskList) ;
					int success=0;
					for (Future<Object> callable : invoke) {
						System.out.println(callable.getClass() + "," + callable.isDone());
						if (callable.isDone()) {
							try {

								Object object = callable.get();
								 if( object instanceof MotorVehicleInfo) {
									vehicleInfo = (MotorVehicleInfo) object;
								}else if(object instanceof ProductSectionMaster){
									p=(ProductSectionMaster) object;
								}else if(object instanceof MotorDataDetails){
									mdd =(MotorDataDetails) object;
								}else {
									covers=(List<PolicyCoverData>) object;
								}

							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (ExecutionException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}

				}
				
				if("1".equals(mdd.getPolicyType()))
					noofComp++;
				MaansarovarToTira tira=MaansarovarToTira.builder()
						.company(companyId)
						.product(product)					
						.broker(broker)
						.uw(uw)
						.covers(covers)
						.vehicleInfo(vehicleInfo)
						.customerInfo(customerInfo)	
						.ltPayment(l)
						.policy(hp)
						.section(p)
						.endt(endt)
						.mdd(mdd)
						.build();
				FleetDtl riskData = convertF.riskData(tira, section.getRiskId());
				riskDatas.add(riskData);
			      
			}
			
			MaansarovarToTira tira=MaansarovarToTira.builder()
					.company(companyId)
					.product(product)					
					.broker(broker)
					.uw(uw)
					
					.customerInfo(customerInfo)	
					.ltPayment(l)
					.policy(hp)					
					.endt(endt)
					/*.mdd(mdd)
					.section(p)
					.covers(covers)
					.vehicleInfo(vehicleInfo)*/
					.build();
			
			FleetHdr fleetHdr = convertF.fleetHdr(tira,riskDatas.size(),noofComp);
			TiraMsgVehiclePushFleet createCover = convertF.createCover(fleetHdr,riskDatas,tira);
			return createCover;
/*
			MaansarovarToTira tira=MaansarovarToTira.builder()
					.company(companyId)
					.product(product)					
					.broker(broker)
					.uw(uw)
					.covers(covers)
					.vehicleInfo(vehicleInfo)
					.customerInfo(customerInfo)	
					.ltPayment(l)
					.policy(hp)
					.section(p)
					.endt(endt)
					.mdd(mdd)
					.build();

			if("M".equals(p.getMotorYn())) {
				TiraMsgVehiclePush	convert=covernt.convert(tira);
				return convert;
			} 
	*/	
		}catch (Exception e) {
			e.printStackTrace();

		}
		return null;
	}
 
}
