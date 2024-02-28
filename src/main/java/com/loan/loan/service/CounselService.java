package com.loan.loan.service;

import com.loan.loan.dto.CounselDTO;

public interface CounselService {
    CounselDTO.Response create(CounselDTO.Request request);

    CounselDTO.Response get(Long counselId);
}
