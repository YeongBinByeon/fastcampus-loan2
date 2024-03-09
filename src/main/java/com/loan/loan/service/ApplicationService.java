package com.loan.loan.service;

import com.loan.loan.dto.ApplicationDTO.AcceptTerms;
import com.loan.loan.dto.ApplicationDTO.Response;
import com.loan.loan.dto.ApplicationDTO.Request;

public interface ApplicationService {

    Response create(Request request);

    Response get(Long applicationId);

    Response update(Long applicationId, Request request);

    void delete(Long applicationId);

    boolean acceptTerms(Long applicationId, AcceptTerms request);

    Response contract (Long applicationId);
}
