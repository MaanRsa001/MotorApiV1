package com.maan.eway.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maan.eway.bean.RtsaMakeBodyError;

public interface RtsaMakeBodyErrorRepository extends JpaRepository<RtsaMakeBodyError, String>{

	Optional<RtsaMakeBodyError> findByRegNumberAndStatus(String resRegNumber, String string);

}
