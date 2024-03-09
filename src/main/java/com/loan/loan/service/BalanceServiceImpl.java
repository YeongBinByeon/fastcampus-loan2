package com.loan.loan.service;


import com.loan.loan.domain.Balance;
import com.loan.loan.dto.BalanceDTO;
import com.loan.loan.repository.BalanceRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BalanceServiceImpl implements BalanceService {

    private final BalanceRepository balanceRepository;

    private final ModelMapper modelMapper;

    @Override
    public BalanceDTO.Response create(Long applicationId, BalanceDTO.Request request) {

        Balance balance = modelMapper.map(request, Balance.class);

        BigDecimal entryAmount = request.getEntryAmount();
        balance.setApplicationId(applicationId);
        balance.setBalance(entryAmount);

        balanceRepository.findByApplicationId(applicationId).ifPresent(b -> {
            balance.setBalanceId(b.getBalanceId());
            balance.setIsDeleted(b.getIsDeleted());
            balance.setCreatedAt(b.getCreatedAt());
            balance.setUpdatedAt(b.getUpdatedAt());
        });

        Balance saved = balanceRepository.save(balance);

        return modelMapper.map(saved, BalanceDTO.Response.class);
    }

//    @Override
//    public BalanceDTO.Response update(Long applicationId, BalanceDTO.UpdateRequest request) {
//
//        // balance, 대출 잔액 가져오기
//        Balance balance = balanceRepository.findByApplicationId(applicationId).orElseThrow(()->{
//            throw new BaseException(ResultType.SYSTEM_ERROR);
//        });
//
//        BigDecimal beforeEntryAmount = request.getBeforeEntryAmount();
//        BigDecimal afterEntryAmount = request.getAfterEntryAmount();
//        BigDecimal updatedBalance = balance.getBalance();
//
//        // 잘못 요청된 대출 집행 금액은 빼주고, 수정된 대출 집행 금액은 더해서 수정
//        // as-is -> to-be
//        updatedBalance = updatedBalance.subtract(beforeEntryAmount).add(afterEntryAmount);
//        balance.setBalance(updatedBalance);
//
//        Balance updated = balanceRepository.save(balance);
//
//        return modelMapper.map(updated, BalanceDTO.Response.class);
//    }
//
//    @Override
//    public BalanceDTO.Response repaymentUpdate(Long applicationId, BalanceDTO.RepaymentRequest request) {
//        Balance balance = balanceRepository.findByApplicationId(applicationId).orElseThrow(() -> {
//            throw new BaseException(ResultType.SYSTEM_ERROR);
//        });
//
//        BigDecimal updatedBalance = balance.getBalance();
//        BigDecimal repaymentAmount = request.getRepaymentAmount();
//
//        // 상환 정상 : balance - repaymentAmount
//        // 상환금 금액 : balance + repaymentAmount
//        if(request.getType().equals(BalanceDTO.RepaymentRequest.RepaymentType.ADD)){
//            updatedBalance = updatedBalance.add(repaymentAmount);
//        } else {
//            updatedBalance = updatedBalance.subtract(repaymentAmount);
//        }
//
//        balance.setBalance(updatedBalance);
//
//        Balance updated = balanceRepository.save(balance);
//
//        return modelMapper.map(updated, BalanceDTO.Response.class);
//    }
}
