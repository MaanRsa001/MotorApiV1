package com.maan.eway.common.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TiraHistory {
	@JsonProperty("RequestId") 
    public String requestId;
    @JsonProperty("Status") 
    public String status;
    @JsonProperty("AcknowledgementId") 
    public String acknowledgementId;
    @JsonProperty("RequestStatusCode") 
    public String requestStatusCode;
    @JsonProperty("RequestStatusDesc") 
    public String requestStatusDesc;
    @JsonProperty("RequestFilePath") 
    public String requestFilePath;
    @JsonProperty("ResponseFilePath") 
    public String responseFilePath;
    @JsonProperty("Acknowledgement") 
    public TiraAcknowledgement acknowledgement;
}
