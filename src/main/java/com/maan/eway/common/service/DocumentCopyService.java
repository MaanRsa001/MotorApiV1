package com.maan.eway.common.service;

import com.maan.eway.common.req.OldDocumentsCopyReq;
import com.maan.eway.res.SuccessRes;

public interface DocumentCopyService {

	SuccessRes copyOldDocumentsToNewQuote(OldDocumentsCopyReq req);

}
