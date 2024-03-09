package com.loan.loan.service;


import com.loan.loan.dto.BalanceDTO;

public interface BalanceService {

    BalanceDTO.Response create(Long applicationId, BalanceDTO.Request request);

//    BalanceDTO.Response update(Long applicationId, BalanceDTO.UpdateRequest request);
//
//    BalanceDTO.Response repaymentUpdate(Long applicationId, BalanceDTO.RepaymentRequest request);
}
