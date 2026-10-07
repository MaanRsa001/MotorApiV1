package com.maan.eway.common.service;

import java.util.List;

import com.maan.eway.common.req.ProductEmployeeDeleteAllReq;
import com.maan.eway.common.req.ProductEmployeeDeleteReq;
import com.maan.eway.common.req.ProductEmployeesGetReq;
import com.maan.eway.common.req.SaveProductDetailsReq;
import com.maan.eway.common.res.ProductEmployeeGetRes;
import com.maan.eway.error.Error;
import com.maan.eway.res.SuccessRes;

public interface ProductEmployeesDetailsService {
	
	List<Error> validateProductEmployeesDetails(SaveProductDetailsReq req);

	SuccessRes saveProductEmployeesDetails(SaveProductDetailsReq req);

	List<ProductEmployeeGetRes> getallProductEmployeesDetails(ProductEmployeesGetReq req);

	SuccessRes deleteProductEmployeesDetails(ProductEmployeeDeleteReq req);

	List<Error> validateProductEmployeesDetailsExcel(SaveProductDetailsReq req);

	List<String> validateSaveEmployeesDetails(SaveProductDetailsReq req);

	SuccessRes saveEmployeesDetails(SaveProductDetailsReq req);

	List<String> validateProceedEmployeesDetails(SaveProductDetailsReq req);

	SuccessRes proceedEmployeesDetails(SaveProductDetailsReq req);

	List<ProductEmployeeGetRes> getallActiveEmployeesDetails(ProductEmployeesGetReq req);

	List<ProductEmployeeGetRes> getallRemovedEmployeesDetails(ProductEmployeesGetReq req);

	SuccessRes deleteAllFidelityEmployees(ProductEmployeesGetReq req);

//	SuccessRes deleteAllEmployees(ProductEmployeeDeleteAllReq req);

}
