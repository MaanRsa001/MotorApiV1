package com.maan.eway.common.res;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TiraPushedData {
	@JsonProperty("QuoteNo") 
    public String quoteNo;
    @JsonProperty("PolicyNo") 
    public String policyNo;
    @JsonProperty("isPosted")
    public boolean isPosted;
    @JsonProperty("Request_Id_Successful") 
    public String request_Id_Successful;
    @JsonProperty("Response_Id_Successful") 
    public String response_Id_Successful;
    @JsonProperty("CoverNoteNo") 
    public String coverNoteNo;
    @JsonProperty("PrevCoverNoteNo") 
    public String prevCoverNoteNo;
    @JsonProperty("StickerNumber") 
    public String stickerNumber;
    @JsonProperty("RecentStatusCode") 
    public String recentStatusCode;
    @JsonProperty("RecentStatusDesc") 
    public String recentStatusDesc;
    @JsonProperty("History") 
    public List<TiraHistory> history;
}
