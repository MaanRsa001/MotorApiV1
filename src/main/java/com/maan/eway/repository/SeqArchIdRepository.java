package com.maan.eway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.eway.bean.SeqArchId;

public interface SeqArchIdRepository extends JpaRepository<SeqArchId,Long > , JpaSpecificationExecutor<SeqArchId> {

}
