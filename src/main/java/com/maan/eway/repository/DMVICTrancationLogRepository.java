package com.maan.eway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.eway.bean.DMVICTrancationLog;

public interface DMVICTrancationLogRepository extends JpaRepository<DMVICTrancationLog,Integer > , JpaSpecificationExecutor<DMVICTrancationLog> {

}
