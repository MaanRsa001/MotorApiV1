
package com.maan.eway.bean;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.*;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedMalLiabilityMasterId implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	private Integer sNo;
	private Integer aooId;
	private Integer amendId;
	private String companyId;


}