package com.maan.eway.agri.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class aezResponse {

	 private Double perHaCost;
	 private String cropDesc; 
}
