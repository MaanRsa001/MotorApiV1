package com.maan.eway.common.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.eway.common.req.NonMotorReqForDD;
import com.maan.eway.common.res.CodeDescRes;
import com.maan.eway.common.service.NonMotorSectionService;

@RestController
@RequestMapping("/common")
public class NonMotorSectionController {

    @Autowired
    private NonMotorSectionService nonMotorSectionService;

    @GetMapping("/nonMotorSectionFields")
    public List<CodeDescRes> getNonMotorSectionFields() {

        return nonMotorSectionService.getNonMotorSectionFields();
    }
    
    @PostMapping("/nonMotorRatingDD")
    public List<CodeDescRes> nonMotorRatingDD(@RequestBody NonMotorReqForDD req) {

        return nonMotorSectionService.getNonMotorRatingDD(req);
    }
}
