package com.maan.eway.service;

import com.maan.eway.req.DMVICRequest;
import com.maan.eway.req.GetCertificateReq;
import com.maan.eway.req.ValidateKMPDCRequest;
import com.maan.eway.res.GetCertificateRes;
import com.maan.eway.res.ValidateDoubleInsuranceRes;

public interface DMVICService {

	ValidateDoubleInsuranceRes checkDMVIC(DMVICRequest req);

	ValidateDoubleInsuranceRes kmpdcValidation(ValidateKMPDCRequest req);

	ValidateDoubleInsuranceRes issuePDCert(String policyNo);

	GetCertificateRes getCertificate(GetCertificateReq req);

	ValidateDoubleInsuranceRes vehicleSearchDMVIC(DMVICRequest req);
	
	ValidateDoubleInsuranceRes vehicleCertificate(ValidateKMPDCRequest req);

}
