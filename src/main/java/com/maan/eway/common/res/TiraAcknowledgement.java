package com.maan.eway.common.res;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TiraAcknowledgement {
	@JsonProperty("ResponseId") 
    public String responseId;
    @JsonProperty("Status") 
    public String status;
    @JsonProperty("ResponseStatusCode") 
    public String responseStatusCode;
    @JsonProperty("RequestStatusDesc") 
    public String responseStatusDesc;
    @JsonProperty("RequestFilePath") 
    public String requestFilePath;
    @JsonProperty("ResponseFilePath") 
    public String responseFilePath;
}
