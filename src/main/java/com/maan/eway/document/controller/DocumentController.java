package com.maan.eway.document.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maan.eway.common.res.CommonRes;
import com.maan.eway.document.req.CommonValidationException;
import com.maan.eway.document.req.DocumentUploadReq;
import com.maan.eway.document.req.GetDocListReq;
import com.maan.eway.document.res.AIDocListRes;
import com.maan.eway.document.res.DocumentImageRes;
import com.maan.eway.document.service.DocumentService;
import com.maan.eway.error.Error;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/api/document")
@Api(tags = "DOCUMENT : Document ", description = "API's")
@RestController
public class DocumentController {

	private Logger log = LogManager.getLogger(DocumentController.class);

	@Autowired
	private DocumentService documentservice;

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_APPROVER','ROLE_USER')")
	@PostMapping("/upload")
	@ApiOperation(value = "This method is to Upload Document")
	public ResponseEntity<CommonRes> uploadFile(@RequestParam("File") MultipartFile file,
			@RequestParam("Req") String jsonString)
			throws CommonValidationException, JsonMappingException, JsonProcessingException {

		log.info(jsonString);
		DocumentImageRes imgRes =  documentservice.fileValidation(file);
		DocumentUploadReq req = new ObjectMapper().readValue(jsonString, DocumentUploadReq.class);
		List<Error> error = new ArrayList<Error>();
		error = documentservice.docvalidation(req, file, imgRes);
		if (error != null && error.size() > 0) {

			CommonRes res = new CommonRes();
			res.setCommonResponse(null);
			res.setIsError(true);
			res.setErrorMessage(error);
			res.setMessage("Success");
			return ResponseEntity.status(HttpStatus.OK).body(res);
		} else {
			CommonRes res = documentservice.fileupload(req, file, imgRes);
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_APPROVER','ROLE_USER')")
	@PostMapping("/getdoclist")
	@ApiOperation(value = "This method is to Get Document List")
	public ResponseEntity<CommonRes> getdoclist(@RequestBody GetDocListReq req) {

		CommonRes data = new CommonRes();

		// Total Doc List
		AIDocListRes res = documentservice.getTotalDocList(req);

		data.setCommonResponse(res);
		data.setIsError(false);
		data.setErrorMessage(Collections.emptyList());
		data.setMessage("Success");

		if (res != null) {
			 return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}

}
