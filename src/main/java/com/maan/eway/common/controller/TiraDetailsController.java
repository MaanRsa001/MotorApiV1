package com.maan.eway.common.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.eway.common.req.TravelPassValidateReq;
import com.maan.eway.tira.service.CollectInfomation;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/tira")
@Api(tags = "Tira Integration",description="API's")
public class TiraDetailsController {

	 @Autowired
	 private CollectInfomation collect;
	 
	@PostMapping(value="/push", produces=MediaType.APPLICATION_XML_VALUE)
	@ApiOperation(value="This method is to Save Content Risk")
	public ResponseEntity<Object> pushTira(@RequestBody TravelPassValidateReq req){ //@PathVariable("quoteNo") String quoteNo ){
		Object collectInfo = collect.collectInfo(req.getQuoteNo(),StringUtils.isBlank(req.getRiskId())?"1":req.getRiskId()
				,StringUtils.isBlank(req.getLocationId())?"1":req.getLocationId()
				,req.getSectionId());
		/*CommonRes c=new CommonRes();
		c.setCommonResponse(collectInfo);*/
		return new ResponseEntity<>(collectInfo,HttpStatus.OK);
	}
	
	@GetMapping(value="/get/{QuoteNo}", produces=MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value="This method is to Save Content Risk")
	public ResponseEntity<Object> collectTira(@PathVariable("QuoteNo") String quoteNo ){
		Object collectInfo = collect.postedInfo(quoteNo);
		return new ResponseEntity<>(collectInfo,HttpStatus.OK);
	}
	@PostMapping(value="/push/fleet", produces=MediaType.APPLICATION_XML_VALUE)
	@ApiOperation(value="This method is to Post to Fleet")
	public ResponseEntity<Object> pushFleetTira(@RequestBody TravelPassValidateReq req){ //@PathVariable("quoteNo") String quoteNo ){
		Object collectInfo = collect.collectInfoForFleet(req.getQuoteNo());
		/*CommonRes c=new CommonRes();
		c.setCommonResponse(collectInfo);*/
		return new ResponseEntity<>(collectInfo,HttpStatus.OK);
	}
}
