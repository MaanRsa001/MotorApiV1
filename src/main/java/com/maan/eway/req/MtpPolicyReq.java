package com.maan.eway.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MtpPolicyReq {

	@NotBlank(message = "vrn is required")
	private String vrn;
}
