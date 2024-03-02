package com.loan.loan.service;

import com.loan.loan.dto.ApplicationDTO;

public interface ApplicationService {

    ApplicationDTO.Response create(ApplicationDTO.Request request);
}
