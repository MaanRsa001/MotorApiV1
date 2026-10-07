package com.maan.eway.common.req;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.eway.common.res.AdditionalInformationResponse;
import com.maan.eway.common.res.ContentItemResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdditionalInformationRequest {
	
	@JsonProperty("QuoteNo")
	private String quoteNo;
    
	@JsonProperty("ContentItems")
    private List<ContentItem> contentItems;
}
