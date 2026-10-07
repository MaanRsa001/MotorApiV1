package com.maan.eway.res.verification;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.eway.req.verification.CoverNoteDtl;

import lombok.Data;
@Data
public class CoverNoteVerificationRes {
	@JsonProperty("CoverNoteHdr") 
   private CoverNoteHdrResponse coverNoteHdrList ;
	@JsonProperty("CoverNoteDtl") 
    private CoverNoteDtl coverNoteDtlList ;

}
