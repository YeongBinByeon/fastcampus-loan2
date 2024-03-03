package com.loan.loan.service;

import com.loan.loan.domain.Application;
import com.loan.loan.domain.Terms;
import com.loan.loan.dto.ApplicationDTO;
import com.loan.loan.dto.ApplicationDTO.Response;
import com.loan.loan.dto.ApplicationDTO.Request;
import com.loan.loan.dto.ApplicationDTO.AcceptTerms;
import com.loan.loan.exception.BaseException;
import com.loan.loan.exception.ResultType;
import com.loan.loan.repository.AcceptTermsRepository;
import com.loan.loan.repository.ApplicationRepository;
import com.loan.loan.repository.TermsRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService{

    private final ApplicationRepository applicationRepository;

    private final TermsRepository termsRepository;

    private final AcceptTermsRepository acceptTermsRepository;

    private final ModelMapper modelMapper;

    @Override
    public Response create(Request request) {
        Application application = modelMapper.map(request, Application.class);
        application.setAppliedAt(LocalDateTime.now());

        Application applied = applicationRepository.save(application);

        return modelMapper.map(applied, Response.class);
    }

    @Override
    public Response get(Long applicationId) {
        Application application = applicationRepository.findById(applicationId).orElseThrow(()->{
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        return modelMapper.map(application, Response.class);
    }

    @Override
    public Response update(Long applicationId, Request request) {
        Application application = applicationRepository.findById(applicationId).orElseThrow(()->{
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        application.setName(request.getName());
        application.setCellPhone(request.getCellPhone());
        application.setEmail(request.getEmail());
        application.setHopeAmount(request.getHopeAmount());

        applicationRepository.save(application);

        return modelMapper.map(application, Response.class);
    }

    @Override
    public void delete(Long applicationId) {
        Application application = applicationRepository.findById(applicationId).orElseThrow(()->{
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        application.setIsDeleted(true);

        applicationRepository.save(application);
    }

    @Override
    public boolean acceptTerms(Long applicationId, AcceptTerms request) {
        // 대출 신청 정보가 존재해야 한다.
        applicationRepository.findById(applicationId).orElseThrow(()->{
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        // 약관이 하나라도 있어야 한다.
        List<Terms> termsList = termsRepository.findAll(Sort.by(Sort.Direction.ASC, "termsId"));
        if(termsList.isEmpty()){
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        // 개시해 놓은 약관 수와 고객이 신청할 때 동의한 약관의 수가 동일해야 한다.
        // 아래 validation 체크는 딱 현재 예제 상황에서만 유효한 체크로 보임
        List<Long> acceptTermsIds = request.getAcceptTermsIds();
        if(termsList.size() != acceptTermsIds.size()){
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        List<Long> termsIds = termsList.stream().map(Terms::getTermsId).collect(Collectors.toList());
        Collections.sort(acceptTermsIds);

        // 고객이 요청한 약관 id 중에 존재하지 않는 약관 ID가 존재하면 예외 처리
        if(!termsIds.containsAll(acceptTermsIds)){
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        for(Long termsId : acceptTermsIds){
            com.loan.loan.domain.AcceptTerms accepted = com.loan.loan.domain.AcceptTerms.builder()
                    .termsId(termsId)
                    .applicationId(applicationId)
                    .build();

            acceptTermsRepository.save(accepted);
        }

        return true;
    }

}
