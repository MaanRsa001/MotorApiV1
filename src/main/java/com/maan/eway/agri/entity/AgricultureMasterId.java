package com.maan.eway.agri.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AgricultureMasterId implements Serializable {

	private static final long serialVersionUID = 1L;
	
    private Integer sno;
    private Integer companyId;
    private Integer productId;
    private Integer amendId;
}
