package com.maan.eway.common.controller;

import java.math.BigDecimal;
import java.math.MathContext;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.maan.eway.bean.FactorRateRequestDetails;
import com.maan.eway.bean.MsCustomerDetails;
import com.maan.eway.bean.MsLifeDetails;
import com.maan.eway.bean.SurrenderFactorMaster;
import com.maan.eway.bean.SurvivalBenefitMaster;
import com.maan.eway.common.req.BenefitIllustration;
import com.maan.eway.common.req.DeathBenefit;
import com.maan.eway.common.req.Illustration;
import com.maan.eway.repository.FactorRateRequestDetailsRepository;
import com.maan.eway.repository.MsCustomerDetailsRepository;
import com.maan.eway.repository.MsLifeDetailsRepository;
import com.maan.eway.upgrade.criteria.CriteriaService;
import com.maan.eway.upgrade.criteria.SpecCriteria;

import jakarta.persistence.Tuple;

@Service
public class IllustrationService {

	@Autowired
	private MsLifeDetailsRepository msLifeRepo;
	
	@Autowired
	private MsCustomerDetailsRepository msCustomerRepo;
	
	@Autowired
	private FactorRateRequestDetailsRepository factorRateRepo;
	
	
	@Autowired
	protected CriteriaService crservice;
	protected SimpleDateFormat DD_MM_YYYY = new SimpleDateFormat("dd/MM/yyyy")  ;
	
	public Object generate(Illustration onetimeId) {
		try {
			MsLifeDetails life = msLifeRepo.findByVdRefno(onetimeId.getVdRefNo());
			MsCustomerDetails customer = msCustomerRepo.findByCdRefno(onetimeId.getCdRefNo());

			List<FactorRateRequestDetails> data = factorRateRepo.findByRequestReferenceNoOrderByVehicleIdAsc(life.getRequestReferenceNo());
			BigDecimal premium=data.stream().filter(i -> i.getCoverageType().equals("B"))
					.map(x -> x.getPremiumExcludedTaxFc())    
					.reduce(BigDecimal.ZERO, BigDecimal::add);
			
			Map<String, FactorRateRequestDetails> optionalCover = data.stream().filter(i -> i.getCoverageType().equals("O"))
			.collect(Collectors.toMap(p-> p.getCoverName(), Function.identity()));
		  Map<String,BigDecimal> additionalBenefit =new HashMap<String, BigDecimal>();
			if(!optionalCover.isEmpty()) {				
				for (Entry<String, FactorRateRequestDetails> entry : optionalCover.entrySet()) {
					if(onetimeId.getOptedCovers() !=null && onetimeId.getOptedCovers().size()>0 && onetimeId.getOptedCovers().contains(entry.getValue().getCoverId()))
						additionalBenefit.put(entry.getKey(),entry.getValue().getPremiumExcludedTaxFc());
				}
			}
			
			Double policyYear= Double.parseDouble(life.getPeriodOfInsurance());
			Integer age = customer.getAge();
			BigDecimal sumInsured = life.getSumInsured();
			Double deathBenefit=4D;
			String todayInString = DD_MM_YYYY.format(new Date());

			String search="policyTerms:"+ life.getPeriodOfInsurance()
			+";productId:"+life.getProductId()
			+";sectionId:"+"99999"
			+";companyId:"+life.getCompanyId()
			+";status:"+"Y;"
			+todayInString+"~effectiveDateStart&effectiveDateEnd;";    

			SpecCriteria criteria = crservice.createCriteria(SurvivalBenefitMaster.class,search,"endOfYear");
			List<Tuple> survivalenefitRules = crservice.getResult(criteria, 0, 50);
			Map<Integer, Tuple> survivalenefitRule = survivalenefitRules.stream().collect(Collectors.toMap(p-> Integer.parseInt(p.get("endOfYear").toString()), Function.identity()));

			List<BenefitIllustration> objects = IntStream.rangeClosed(1, policyYear.intValue())
					.mapToObj(i -> BenefitIllustration.builder()
							.age(age + i - 1)
							.policyYear(i)
							.premium(premium)
							.additionalBenefit(additionalBenefit)
							.deathBenefit(DeathBenefit.builder().sumAssured(sumInsured).build())
							.build())
					.collect(Collectors.toList());

			if(survivalenefitRule!=null && !survivalenefitRule.isEmpty()) {
				objects.stream().forEach(i ->

				{
					if (survivalenefitRule.containsKey(i.getPolicyYear())) {
						Tuple tuple = survivalenefitRule.get(i.getPolicyYear());
						i.setSurvivalBenefit(new BigDecimal(tuple.get("amount").toString())
								.multiply(sumInsured,MathContext.DECIMAL32)
								.divide(new BigDecimal("100"),MathContext.DECIMAL32));
					} else {
						i.setSurvivalBenefit(null);
					}
				});
			}
			
			
			ListIterator<?> it = objects.listIterator();

			BigDecimal revisionaryBonusRule =BigDecimal.ZERO,totalRule =BigDecimal.ZERO;

			while (it.hasNext()) {

				BenefitIllustration current = (BenefitIllustration)it.next();

				revisionaryBonusRule = sumInsured.add(revisionaryBonusRule)
						.multiply(new BigDecimal(deathBenefit/100,MathContext.DECIMAL32),MathContext.DECIMAL32)
						.add(revisionaryBonusRule);
				try {
					totalRule =	current.getSurvivalBenefit() == null ? totalRule : 
						totalRule.add(current.getSurvivalBenefit());
				}catch (Exception e) {
					// TODO: handle exception
				}
				current.getDeathBenefit().setRevisionaryBonus(revisionaryBonusRule);

				BigDecimal total=current.getDeathBenefit().getSumAssured().add(revisionaryBonusRule).subtract(totalRule,MathContext.DECIMAL32); 
				current.getDeathBenefit().setTotal(total);

			}

			search="policyTerms:"+ life.getPeriodOfInsurance()
			+";productId:"+life.getProductId()
			+";sectionId:"+"99999"
			+";companyId:"+life.getCompanyId()
			+";status:"+"Y;"
			+todayInString+"~effectiveDateStart&effectiveDateEnd;";    

			criteria = crservice.createCriteria(SurrenderFactorMaster.class,search,"policyYear");
			List<Tuple> surrenderRules = crservice.getResult(criteria, 0, 50);
			Map<Integer, Tuple> surrenderRule = surrenderRules.stream().collect(Collectors.toMap(p-> Integer.parseInt(p.get("policyYear").toString()), Function.identity()));

			if(surrenderRule!=null && !surrenderRule.isEmpty()) {
				objects.stream().forEach(i ->
				{
					if(surrenderRule.containsKey(i.getPolicyYear())){
						Double result= i.getPolicyYear()/policyYear;

						Tuple t=surrenderRule.get(i.getPolicyYear());
						BigDecimal surrender = sumInsured.multiply(new BigDecimal(result ) ,MathContext.DECIMAL32)
								.multiply(
										new BigDecimal(t.get("amount").toString())
										.divide(new BigDecimal("100"),MathContext.DECIMAL32) 
										,MathContext.DECIMAL32 );
						i.setSurrender(surrender);
					}

				}
						);
			}

			BenefitIllustration lastRecord = objects.get(objects.size()-1);

			BigDecimal maturityRule = lastRecord.getSurvivalBenefit().add(lastRecord.getDeathBenefit().getTotal());

			lastRecord.setMaturityBenefit(maturityRule);
			
			
			
			return objects;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Object downloadPdf(Illustration onetimeId, String tokens) {
		try {
			HashMap<String, Object> onetime=null;
			Object generate = generate(onetimeId);
			long epochMilli = Instant.now().toEpochMilli();
			String fileSavePath = "\\\\192.168.1.99\\Users\\CommonPath\\EwayPortal\\IllustrationFile\\";
			if(generate!=null) {
			    Path path = Paths.get(fileSavePath+epochMilli+".json");
			    Gson g=new Gson();
			    String json = g.toJson(generate);			    
			    Files.write(path,json.getBytes());			    		
			}
			try {
				
				RestTemplate restTemplate = new RestTemplate();
				HttpHeaders headers = new HttpHeaders();
				headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
				headers.setContentType(MediaType.APPLICATION_JSON);
				headers.set("Authorization", tokens.split(",")[0]);
				HttpEntity<Object> entityReq = new HttpEntity<>("", headers);
				System.out.println(entityReq.getBody());
			   ResponseEntity<Object> response = restTemplate.postForEntity("http://localhost:8086/pdf/illustration/"+epochMilli+".json", entityReq, Object.class);
			   return response.getBody();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
