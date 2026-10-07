package com.maan.eway.document.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.maan.eway.common.res.CommonRes;
import com.maan.eway.document.req.DocumentUploadReq;
import com.maan.eway.document.req.GetDocListReq;
import com.maan.eway.document.res.AIDocListRes;
import com.maan.eway.document.res.DocumentImageRes;
import com.maan.eway.error.Error;

public interface DocumentService {
	
	public DocumentImageRes fileValidation(MultipartFile file);
	
	public List<Error> docvalidation(DocumentUploadReq req, MultipartFile file, DocumentImageRes documentImageRes);
	
	public CommonRes fileupload(DocumentUploadReq req, MultipartFile file, DocumentImageRes documentImageRes);
	
	public AIDocListRes getTotalDocList(GetDocListReq req);

}
