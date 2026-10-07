package com.maan.eway.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.eway.bean.DMVICToken;

public interface DMVICTokenRepository extends JpaRepository<DMVICToken,Integer > , JpaSpecificationExecutor<DMVICToken>{

	List<DMVICToken> findByEntryDateBeforeAndExpireDateAfter(Date currentDate1, Date currentDate2);
}
