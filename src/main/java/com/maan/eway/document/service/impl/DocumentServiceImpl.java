package com.maan.eway.document.service.impl;

import java.math.BigDecimal;
import java.net.ConnectException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maan.eway.bean.CompanyProductMaster;
import com.maan.eway.bean.CoverDocumentMaster;
import com.maan.eway.bean.DamagePartsDetails;
import com.maan.eway.bean.DocumentTransactionDetails;
import com.maan.eway.bean.DocumentUniqueDetails;
import com.maan.eway.bean.EndtTypeMaster;
import com.maan.eway.bean.HomePositionMaster;
import com.maan.eway.bean.ProductSectionMaster;
import com.maan.eway.bean.SeqDocuniqueid;
import com.maan.eway.common.res.CommonRes;
import com.maan.eway.document.req.DocumentUploadReq;
import com.maan.eway.document.req.GetDocListReq;
import com.maan.eway.document.res.AIDocListRes;
import com.maan.eway.document.res.DamagedPartRes;
import com.maan.eway.document.res.DocSection;
import com.maan.eway.document.res.DocumentImageRes;
import com.maan.eway.document.service.DocumentService;
import com.maan.eway.error.Error;
import com.maan.eway.repository.DamagePartsDetailsRepository;
import com.maan.eway.repository.DocumentTransactionDetailsRepository;
import com.maan.eway.repository.DocumentUniqueDetailsRepository;
import com.maan.eway.repository.EndtTypeMasterRepository;
import com.maan.eway.repository.HomePositionMasterRepository;
import com.maan.eway.repository.SeqDocuniqueidRepository;

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
@Transactional
public class DocumentServiceImpl implements DocumentService {

	private Logger log = LogManager.getLogger(DocumentServiceImpl.class);

	@Value("${file.directoryPath}")
	private String directoryPath;

	@Value("${file.compressedImg}")
	private String compressedImg;

	@Value("${DocumentImageValidation}")
	private String documentImageValidation;

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private HomePositionMasterRepository homeRepo;

	@Autowired
	private SeqDocuniqueidRepository seqDocUniqueRepo;

	@Autowired
	private DocumentUniqueDetailsRepository docUniqueRepo;

	@Autowired
	private DocumentTransactionDetailsRepository docTranRepo;

	@Autowired
	private EndtTypeMasterRepository endtTypeRepo;
	
	@Autowired
	private DamagePartsDetailsRepository damagePartsDetailsRepo;

	@Override
	public List<Error> docvalidation(DocumentUploadReq req, MultipartFile file, DocumentImageRes documentImageRes) {
		List<Error> errorList = new ArrayList<Error>();
		try {
			log.info(req);
			long fileSizeInBytes = file.getSize();
			double size_kb = fileSizeInBytes / 1024;
			double size_mb = size_kb / 1024;

			if (documentImageRes != null) {
				if (documentImageRes.getCarDetails().getMake().equalsIgnoreCase("unknown"))
					errorList.add(new Error("01", "File", "Please select a Image related to a motor"));
			} else {
				errorList.add(new Error("01", "File", "Please select a Image related to a motor"));
			}

			if (size_mb > 25) {
				errorList.add(new Error("01", "FileSize", "File Size Must be 25Mb Current file value is" + size_mb
						+ "MB for " + req.getOriginalFileName()));
			}

			if (StringUtils.isBlank(req.getLocationId())) {
				errorList.add(new Error("01", "LocationId", "Please Select Location Id"));
			}
			if (StringUtils.isBlank(req.getLocationName())) {
				errorList.add(new Error("01", "LocationName", "Please Select Location Name"));
			}
			if (StringUtils.isBlank(req.getProductId())) {
				errorList.add(new Error("01", "ProductId", "Please Select Product"));
			}
			if (StringUtils.isBlank(req.getSectionId())) {
				errorList.add(new Error("01", "SectionId", "Please Select Section"));
			}
			if (StringUtils.isBlank(req.getSectionId())) {
				errorList.add(new Error("01", "SectionId", "Please Select Section"));
			}

			if (StringUtils.isBlank(req.getRiskId())) {
				errorList.add(new Error("01", "RiskId", "Please Select RiskId"));
			}

			if (StringUtils.isBlank(req.getId())) {
				errorList.add(new Error("01", "Id", "Please Select Id"));
			}

			if (StringUtils.isBlank(req.getIdType())) {
				errorList.add(new Error("01", "IdType", "Please Select IdType"));
			}

			if (StringUtils.isBlank(req.getDocumentId())) {
				errorList.add(new Error("01", "DocumentId", "Please Select Document Type"));
			}

			if (StringUtils.isBlank(req.getUploadedBy())) {
				errorList.add(new Error("01", "UploadedBy", "Please Select Uploaded By"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			return null;
		}
		return errorList;
	}

	public DocumentImageRes fileValidation(MultipartFile file) {
		try {
			ResponseEntity<DocumentImageRes> response = null;
			RestTemplate temp = new RestTemplateBuilder().setConnectTimeout(Duration.ofSeconds(5))
					.setReadTimeout(Duration.ofMinutes(2)).build();
			ByteArrayResource byteArrayResource = new ByteArrayResource(file.getBytes()) {
				@Override
				public String getFilename() {
					return file.getOriginalFilename();
				}
			};
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("files", byteArrayResource);
			HttpEntity<?> requestEntity = new HttpEntity<>(body, headers);
			System.out.println("calling Documant validation Api:  " + requestEntity);
			try {
				response = temp.exchange(documentImageValidation, HttpMethod.POST, requestEntity,
						DocumentImageRes.class);
				System.out.println("report response Api:  " + response.getBody());

			} catch (RestClientException e) {
				if (e.getCause() instanceof ConnectException) {
					System.out.println("Connection refused: Unable to connect to the server at " + response);

				} else {
					System.out.println("An error occurred while making the REST call: " + e.getMessage());
				}
			}
			if (response != null && response.getBody() != null) {
				return response.getBody();
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			return null;
		}
		return null;
	}

	@Override
	public CommonRes fileupload(DocumentUploadReq req, MultipartFile file, DocumentImageRes documentImageRes) {
		CommonRes res = new CommonRes();

		try {
			HomePositionMaster homeData = homeRepo.findByQuoteNo(req.getQuoteNo());
			CompanyProductMaster product = getCompanyProductMasterDropdown(homeData.getCompanyId(),
					homeData.getProductId().toString());
			ProductSectionMaster secData = getProductSectionDropdown(req.getInsuranceId(), req.getProductId(),
					req.getSectionId());

			CoverDocumentMaster docDetails = new CoverDocumentMaster();
			if (StringUtils.isNotBlank(req.getTermsAndCondtionYn())
					&& req.getTermsAndCondtionYn().equalsIgnoreCase("Y")) {
				docDetails = getByDocumentId(homeData.getCompanyId(), homeData.getProductId(), "99999",
						req.getDocumentId());
			} else {
				docDetails = getByDocumentId(homeData.getCompanyId(), homeData.getProductId(), req.getSectionId(),
						req.getDocumentId());
			}

			// Copy File
			Random random = new Random();
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());

			String newfilename = "";
			String newfilename1 = "";
			// OrginalFile
			Path destination = Paths.get(directoryPath);
			newfilename = random.nextInt(100)
					+ timestamp.toString().replace(":", "T").replace(" ", "S").replace("-", "H").replace(".", "D") + "."
					+ FilenameUtils.getExtension(file.getOriginalFilename());
			Files.copy(file.getInputStream(), destination.resolve(newfilename));

			Timestamp timestamp1 = new Timestamp(System.currentTimeMillis());
			// BackupFile
			Path destination1 = Paths.get(compressedImg);
			newfilename1 = random.nextInt(100)
					+ timestamp1.toString().replace(":", "T").replace(" ", "S").replace("-", "H").replace(".", "D")
					+ "." + FilenameUtils.getExtension(file.getOriginalFilename());
			Files.copy(file.getInputStream(), destination1.resolve(newfilename1));

			saveDocument(req, file, documentImageRes, homeData, product, secData, docDetails, newfilename, newfilename1,
					timestamp1);
			
			if (documentImageRes != null) {
				res.setCommonResponse(documentImageRes.getDamagedPartRes());
			} else {
				res.setCommonResponse("File Upload Sucessfully");
			}
			res.setIsError(false);
		} catch (Exception e) {
			e.printStackTrace();
			res.setCommonResponse(null);
			List<Error> error = new ArrayList<Error>();
			error.add(new Error("01", "Upload Error", e.getMessage()));
			res.setErrorMessage(error);
			res.setIsError(true);
		}
		return res;
	}

	private synchronized void saveDocument(DocumentUploadReq req, MultipartFile file, DocumentImageRes documentImageRes,
			HomePositionMaster homeData, CompanyProductMaster product, ProductSectionMaster secData,
			CoverDocumentMaster docDetails, String newfilename, String newfilename1, Timestamp timestamp1)
			throws JsonProcessingException {
		try {
			// Save Document Unique Details
			String uniqueId = saveDocumentUniqueDetails(req, file, product, secData, docDetails, newfilename,
					newfilename1);

			// Save Document Transaction Details
			saveDocumentTransactionDetails(req, documentImageRes, homeData, product, secData, timestamp1, uniqueId);

			// Save Damage Part Details
			saveDamagePartsDetails(req, documentImageRes, homeData, product, secData, timestamp1, uniqueId);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
		}
	}
	
	private void saveDamagePartsDetails(DocumentUploadReq req, DocumentImageRes documentImageRes,
			HomePositionMaster homeData, CompanyProductMaster product, ProductSectionMaster secData,
			Timestamp timestamp1, String uniqueId) throws JsonProcessingException {
		try {
			Date currentDate = new Date();
			List<DamagePartsDetails> damagePartsDetailsList = new ArrayList<>();
			Long damageId = damagePartsDetailsRepo.findTopByOrderByDamageIdDesc() != null
					? damagePartsDetailsRepo.findTopByOrderByDamageIdDesc().getDamageId() + 1
					: 1;
			List<DamagedPartRes> damagedPartRes = documentImageRes.getDamagedPartRes();
			for (DamagedPartRes damagedPart : damagedPartRes) {
				DamagePartsDetails damagePartsDetails = new DamagePartsDetails();
				damagePartsDetails.setDamageId(damageId++);
				damagePartsDetails.setDamagePercentage(damagedPart.getDamagePercentage());
				damagePartsDetails.setDamageType(damagedPart.getDamageType());
				damagePartsDetails.setDocumentId(Integer.valueOf(req.getDocumentId()));
				damagePartsDetails.setDocumentRef(null);
				damagePartsDetails.setENTRY_DATE(currentDate);
				damagePartsDetails.setMaterialType(damagedPart.getMaterialType());
				damagePartsDetails.setName(damagedPart.getName());
				damagePartsDetails.setQuoteNo(req.getQuoteNo());
				damagePartsDetails.setRecommendation(damagedPart.getRecommendation());
				damagePartsDetails.setRemark(null);
				damagePartsDetails
						.setRepairCostInrMAX(Long.valueOf(String.valueOf(documentImageRes.getTotalRepairCostInrMax())));
				damagePartsDetails
						.setRepairCostInrMIN(Long.valueOf(String.valueOf(documentImageRes.getTotalRepairCostInrMin())));
				damagePartsDetails.setRepairCostUSD(null);
				damagePartsDetails.setUniqueId(uniqueId);
				damagePartsDetailsList.add(damagePartsDetails);

			}
			damagePartsDetailsRepo.saveAllAndFlush(damagePartsDetailsList);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
		}
	}

	private void saveDocumentTransactionDetails(DocumentUploadReq req, DocumentImageRes documentImageRes,
			HomePositionMaster homeData, CompanyProductMaster product, ProductSectionMaster secData,
			Timestamp timestamp1, String uniqueId) throws JsonProcessingException {
		try {
			DocumentTransactionDetails docTran = new DocumentTransactionDetails();
			docTran.setUniqueId(Integer.valueOf(uniqueId));
			docTran.setId(req.getId());
			docTran.setIdType(req.getIdType());
			docTran.setRequestReferenceNo(homeData.getRequestReferenceNo());
			docTran.setQuoteNo(req.getQuoteNo());
			docTran.setCompanyId(homeData.getCompanyId());
			docTran.setCompanyName(homeData.getCompanyName());
			docTran.setProductId(homeData.getProductId());
			docTran.setProductName(homeData.getProductName());
			docTran.setSectionId(Integer.valueOf(req.getSectionId()));
			docTran.setSectionName(secData == null ? "All" : secData.getSectionName());
			docTran.setProductType(secData == null ? product.getMotorYn() : secData.getMotorYn());
			docTran.setLocationId(Integer.valueOf(req.getLocationId()));
			docTran.setLocationName(req.getLocationName());
			docTran.setRiskId(Integer.valueOf(req.getRiskId()));
			docTran.setCreatedBy(req.getUploadedBy());
			docTran.setEntryDate(timestamp1);
			docTran.setStatus("Y");
			docTran.setEntryDate(new Date());
			if (documentImageRes != null) {
				ObjectMapper objectMapper = new ObjectMapper();
				String json = objectMapper.writeValueAsString(documentImageRes);
				docTran.setDocumentJson(json);
			}
			if (StringUtils.isNotBlank(req.getEndorsementType())) {
				EndtTypeMaster entMaster = endtTypeRepo.findByCompanyIdAndProductIdAndStatusAndEndtTypeId(
						req.getInsuranceId(), Integer.parseInt(req.getProductId()), "Y",
						Integer.valueOf(req.getEndorsementType()));
				if (entMaster != null) {
					docTran.setEndorsementDate(req.getEndorsementDate() == null ? null : new Date());
					docTran.setEndorsementEffdate(
							req.getEndorsementEffdate() == null ? null : req.getEndorsementEffdate());
					docTran.setEndorsementRemarks(
							req.getEndorsementRemarks() == null ? "" : req.getEndorsementRemarks());
					docTran.setEndorsementTypeDesc(entMaster.getEndtTypeDesc());
					docTran.setIsFinaceYn(entMaster.getEndtTypeCategoryId() == 2 ? "Y" : "N");
					docTran.setEndtCategDesc(entMaster.getEndtTypeCategory());
					docTran.setEndtStatus("P");
					docTran.setEndtCount(new BigDecimal(req.getEndtCount()));
					docTran.setEndtPrevPolicyNo(req.getEndtPrevPolicyNo());
					docTran.setEndtPrevQuoteNo(req.getEndtPrevQuoteNo());

					// insert local description
					docTran.setEndorsementTypeDescLocal(entMaster.getEndtTypeCategory());
				}
			}
			if ("Y".equalsIgnoreCase(req.getEmiYn())) {
				docTran.setEmiYn(req.getEmiYn() == null ? null : req.getEmiYn());
				docTran.setInstallmentPeriod(req.getInstallmentPeriod() == null ? null : req.getInstallmentPeriod());
				docTran.setNoOfInstallment(req.getNoOfInstallment() == null ? null : req.getNoOfInstallment());
			}
			// insert local description
			docTran.setProductNameLocal(homeData.getProductName());

			docTran.setSectionNameLocal(secData == null ? "All" : secData.getSectionName());

			docTranRepo.saveAndFlush(docTran);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
		}
	}

	private String saveDocumentUniqueDetails(DocumentUploadReq req, MultipartFile file, CompanyProductMaster product,
			ProductSectionMaster secData, CoverDocumentMaster docDetails, String newfilename, String newfilename1) {
		DocumentUniqueDetails uniqDoc = new DocumentUniqueDetails();
		String uniqueId = genDocUniqueId();
		try {
			uniqDoc.setUniqueId(Integer.valueOf(uniqueId));
			uniqDoc.setUploadedBy(req.getUploadedBy());
			uniqDoc.setDocApplicable(docDetails.getDocApplicable());
			uniqDoc.setDocApplicableId(
					docDetails.getDocApplicableId() == null ? null : docDetails.getDocApplicableId().toString());
			uniqDoc.setFileName(newfilename);
			uniqDoc.setFilePathOrginal(directoryPath + newfilename);
			uniqDoc.setFilePathBackup(compressedImg + newfilename1);
			uniqDoc.setOrginalFileName(file.getOriginalFilename());
			uniqDoc.setUploadedTime(new Date());
			uniqDoc.setDocumentId(Integer.valueOf(req.getDocumentId()));
			uniqDoc.setDocumentType(docDetails.getDocumentType());
			uniqDoc.setDocumentTypeDesc(docDetails.getDocumentTypeDesc());
			uniqDoc.setDocumentDesc(docDetails.getDocumentDesc());
			uniqDoc.setDocumentName(docDetails.getDocumentName());
			uniqDoc.setEntryDate(new Date());
			uniqDoc.setUploadedTime(new Date());
			uniqDoc.setStatus("Y");
			uniqDoc.setId(req.getId());
			uniqDoc.setIdType(req.getIdType());
			uniqDoc.setProductType(secData == null ? product.getMotorYn() : secData.getMotorYn());
			uniqDoc.setVerifiedYn(StringUtils.isBlank(req.getVerifiedYn()) ? "N" : req.getVerifiedYn());

			if ("Y".equalsIgnoreCase(req.getEmiYn())) {
				uniqDoc.setEmiYn(req.getEmiYn() == null ? null : req.getEmiYn());
				uniqDoc.setInstallmentPeriod(req.getInstallmentPeriod() == null ? null : req.getInstallmentPeriod());
				uniqDoc.setNoOfInstallment(req.getNoOfInstallment() == null ? null : req.getNoOfInstallment());
			}

			// adding local description feilds
			uniqDoc.setIdTypeLocal(req.getIdType());
			// uniqDoc.setDocumentApplicableLocal(docDetails.getDocApplicableLocal());
			uniqDoc.setDocumentNameLocal(docDetails.getDocumentNameLocal());
			uniqDoc.setDocumentDescLocal(docDetails.getDocumentDescLocal());
			uniqDoc.setDocumentTypeDescLocal(docDetails.getDocumentTypeDescLocal());
			docUniqueRepo.saveAndFlush(uniqDoc);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
		}
		return uniqueId;
	}

	public ProductSectionMaster getProductSectionDropdown(String companyId, String productId, String sectionId) {
		ProductSectionMaster section = new ProductSectionMaster();
		try {
			List<ProductSectionMaster> sectionList = new ArrayList<ProductSectionMaster>();

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
			Predicate a5 = cb.equal(c.get("sectionId"), ocpm2.get("sectionId"));
			Predicate a7 = cb.equal(c.get("companyId"), ocpm2.get("companyId"));
			Predicate a8 = cb.equal(c.get("productId"), ocpm2.get("productId"));

			Predicate a6 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
			effectiveDate2.where(a5, a6, a7, a8);

			// Where
			Predicate n1 = cb.equal(c.get("status"), "Y");
			Predicate n2 = cb.equal(c.get("effectiveDateStart"), effectiveDate);
			Predicate n3 = cb.equal(c.get("effectiveDateEnd"), effectiveDate2);
			Predicate n4 = cb.equal(c.get("companyId"), companyId);
			Predicate n5 = cb.equal(c.get("productId"), productId);
			Predicate n6 = cb.equal(c.get("sectionId"), sectionId);
			query.where(n1, n2, n3, n4, n5, n6).orderBy(orderList);
			// Get Result
			TypedQuery<ProductSectionMaster> result = em.createQuery(query);
			sectionList = result.getResultList();
			section = sectionList.size() > 0 ? sectionList.get(0) : null;

		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			return null;
		}
		return section;
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
			product = list.size() > 0 ? list.get(0) : null;
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is --->" + e.getMessage());
			return null;
		}
		return product;
	}

	public CoverDocumentMaster getByDocumentId(String insId, Integer productId, String sectionId, String documentId) {
		CoverDocumentMaster res = new CoverDocumentMaster();

		try {
			Date today = new Date();
			Calendar cal = new GregorianCalendar();
			cal.setTime(today);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 1);
			today = cal.getTime();
			cal.set(Calendar.HOUR_OF_DAY, 1);
			cal.set(Calendar.MINUTE, 1);
			// Criteria
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<CoverDocumentMaster> query = cb.createQuery(CoverDocumentMaster.class);
			List<CoverDocumentMaster> list = new ArrayList<CoverDocumentMaster>();

			// Find All
			Root<CoverDocumentMaster> c = query.from(CoverDocumentMaster.class);

			// Select
			query.select(c);

			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.desc(c.get("effectiveDateStart")));

			// Where
			jakarta.persistence.criteria.Predicate n1 = cb.equal(c.get("status"), "Y");
			jakarta.persistence.criteria.Predicate n11 = cb.equal(c.get("status"), "R");
			Predicate n12 = cb.or(n1, n11);
			jakarta.persistence.criteria.Predicate n3 = cb.equal(c.get("companyId"), insId);
			jakarta.persistence.criteria.Predicate n4 = cb.equal(c.get("productId"), productId);
			jakarta.persistence.criteria.Predicate n5 = cb.equal(c.get("sectionId"), sectionId);
			jakarta.persistence.criteria.Predicate n7 = cb.equal(c.get("documentId"), documentId);
			query.where(n12, n3, n4, n5, n7).orderBy(orderList);

			// Get Result
			TypedQuery<CoverDocumentMaster> result = em.createQuery(query);
			list = result.getResultList();
			res = list.get(0);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			return null;
		}
		return res;
	}

	public synchronized String genDocUniqueId() {
		try {
			SeqDocuniqueid entity;
			entity = seqDocUniqueRepo.save(new SeqDocuniqueid());
			return String.format("%05d", entity.getDocUniqueId());
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			return null;
		}

	}
	
	@Override
	public AIDocListRes getTotalDocList(GetDocListReq req) {
		try {
			AIDocListRes aiDocListRes = new AIDocListRes();

			List<DocumentTransactionDetails> docList = docTranRepo.findByQuoteNoAndStatus(req.getQuoteNo(),"Y");
			if (docList.isEmpty()) {
				return null; 
			}
			DocumentTransactionDetails firstDoc = docList.get(0);
			aiDocListRes.setCompanyId(firstDoc.getCompanyId());
			aiDocListRes.setProductId(firstDoc.getProductId().toString());
			aiDocListRes.setRequestReferenceNo(firstDoc.getRequestReferenceNo());

			List<DocSection> docSectionList = docList.stream().map(doc -> {
				DocumentUniqueDetails docUniqueDetails = docUniqueRepo.findByUniqueIdAndStatus(doc.getUniqueId(), "Y");
				List<DamagePartsDetails> damagePartsDetailsList = damagePartsDetailsRepo
						.findByQuoteNoAndUniqueId(req.getQuoteNo(), doc.getUniqueId().toString());

				DocSection docSection = new DocSection();
				docSection.setSectionId(doc.getSectionId().toString());
				docSection.setDocumentId(docUniqueDetails.getDocumentId().toString());
				docSection.setDocumentName(docUniqueDetails.getOrginalFileName());
				docSection.setDocumentIdName(docUniqueDetails.getDocumentName());
				docSection.setDocumentType(docUniqueDetails.getDocumentType());
				docSection.setDocumentTypeName(docUniqueDetails.getDocumentTypeDesc());
				docSection.setDamagePartsDetailsList(damagePartsDetailsList);
				return docSection;
			}).collect(Collectors.toList());

			aiDocListRes.setSectionList(docSectionList);
			return aiDocListRes;

		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			return null;
		}

	}
}
