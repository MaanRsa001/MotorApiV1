package com.maan.eway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.eway.bean.SeqDocuniqueid;

public interface SeqDocuniqueidRepository  extends JpaRepository<SeqDocuniqueid,Long > , JpaSpecificationExecutor<SeqDocuniqueid> {

}
