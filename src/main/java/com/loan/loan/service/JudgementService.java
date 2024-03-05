package com.loan.loan.service;


import com.loan.loan.dto.ApplicationDTO;
import com.loan.loan.dto.JudgementDTO.Request;
import com.loan.loan.dto.JudgementDTO.Response;

public interface JudgementService {

    Response create(Request request);

    Response get(Long judgementId);

    Response getJudgementOfApplication(Long applicationId);
//
//    Response update(Long judgementId, Request request);
//
//    void delete(Long judgementId);
//
//    ApplicationDTO.GrantAmount grant(Long judgementId);
}
