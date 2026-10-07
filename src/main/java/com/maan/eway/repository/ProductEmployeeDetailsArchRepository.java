package com.maan.eway.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.eway.bean.ProductEmployeeDetailsArch;
import com.maan.eway.bean.ProductEmployeeDetailsArchId;

public interface ProductEmployeeDetailsArchRepository  extends JpaRepository<ProductEmployeeDetailsArch, ProductEmployeeDetailsArchId>, JpaSpecificationExecutor<ProductEmployeeDetailsArch> {



}
