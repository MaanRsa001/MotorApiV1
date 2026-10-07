package com.maan.eway.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.eway.bean.EwayStateMaster;
import com.maan.eway.bean.EwayStateMasterId;

public interface EwayStateMasterRepo extends JpaRepository<EwayStateMaster,EwayStateMasterId > , JpaSpecificationExecutor<EwayStateMaster> {

	

}
