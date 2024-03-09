package com.loan.loan.service;

import com.loan.loan.domain.Application;
import com.loan.loan.domain.Judgement;
import com.loan.loan.dto.ApplicationDTO;
import com.loan.loan.dto.JudgementDTO;
import com.loan.loan.repository.ApplicationRepository;
import com.loan.loan.repository.JudgementRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JudgementServiceTest {

    @InjectMocks
    private JudgementServiceImpl judgementService;

    @Mock
    private JudgementRepository judgementRepository;

    @Mock
    private ApplicationRepository applicationRepository;

    @Spy
    private ModelMapper modelMapper;

    @Test
    void Should_ReturnResponseOfNewJudgementEntity_When_RequestNewJudgement(){

        Judgement judgement = Judgement.builder()
                .applicationId(1L)
                .name("Member Kim")
                .approvalAmount(BigDecimal.valueOf(5000000))
                .build();

        JudgementDTO.Request request = JudgementDTO.Request.builder()
                .applicationId(1L)
                .name("Member Kim")
                .approvalAmount(BigDecimal.valueOf(5000000))
                .build();

        //application find
        when(applicationRepository.findById(1L)).thenReturn(Optional.ofNullable(Application.builder().build()));
        //judgement save
        when(judgementRepository.save(ArgumentMatchers.any(Judgement.class))).thenReturn(judgement);

        JudgementDTO.Response actual = judgementService.create(request);

        Assertions.assertThat(actual.getName()).isSameAs(judgement.getName());
        Assertions.assertThat(actual.getApplicationId()).isSameAs(judgement.getApplicationId());
        Assertions.assertThat(actual.getApprovalAmount()).isSameAs(judgement.getApprovalAmount());

    }

    @Test
    void Should_ReturnResponseOfExistJudgementEntity_When_RequestExistJudgementId(){
        Judgement entity = Judgement.builder()
                .judgementId(1L)
                .build();

        when(judgementRepository.findById(1L)).thenReturn(Optional.ofNullable(entity));

        JudgementDTO.Response actual = judgementService.get(1L);

        Assertions.assertThat(actual.getJudgementId()).isSameAs(1L);
    }

    @Test
    void Should_ReturnResponseOfExistJudgementEntity_When_RequestExistApplicationId(){
        Judgement judgementEntity = Judgement.builder()
                .judgementId(1L)
                .build();

        Application applicationEntity = Application.builder()
                .applicationId(1L)
                .build();

        when(applicationRepository.findById(1L)).thenReturn(Optional.ofNullable(applicationEntity));
        when(judgementRepository.findByApplicationId(1L)).thenReturn(Optional.ofNullable(judgementEntity));

        JudgementDTO.Response actual = judgementService.getJudgementOfApplication(1L);

        Assertions.assertThat(actual.getJudgementId()).isSameAs(1L);
    }

    @Test
    void Should_ReturnUpdatedResponseOfExistJudgementEntity_When_RequestUpdateExistJudgementInfo(){
        Judgement entity = Judgement.builder()
                .judgementId(1L)
                .name("Member Kim")
                .approvalAmount(BigDecimal.valueOf(5000000))
                .build();

        JudgementDTO.Request request = JudgementDTO.Request.builder()
                .name("Member Lee")
                .approvalAmount(BigDecimal.valueOf(10000000))
                .build();

        when(judgementRepository.findById(1L)).thenReturn(Optional.ofNullable(entity));
        // 아래 save 메서드 모킹은 없어도 테스트 성공함. 일단 아래 Assertions 검증 판별 로직에 영향을 주지 않기 때문이긴 한데.. 없어도 실패 안 하는 원리는 모르겠음
        //when(judgementRepository.save(ArgumentMatchers.any(Judgement.class))).thenReturn(entity);

        JudgementDTO.Response actual = judgementService.update(1L, request);

        Assertions.assertThat(actual.getJudgementId()).isSameAs(1L);
        Assertions.assertThat(actual.getName()).isSameAs(request.getName());
        Assertions.assertThat(actual.getApprovalAmount()).isSameAs(request.getApprovalAmount());
    }

    @Test
    void Should_DeletedJudgementEntity_When_RequestDeleteExistJudgementInfo(){
        Judgement entity = Judgement.builder()
                .judgementId(1L)
                .build();

        when(judgementRepository.findById(1L)).thenReturn(Optional.ofNullable(entity));
        // 아래 save 메서드 모킹은 없어도 테스트 성공함.
        //when(judgementRepository.save(ArgumentMatchers.any(Judgement.class))).thenReturn(entity);

        judgementService.delete(1L);

        Assertions.assertThat(entity.getIsDeleted()).isTrue();
    }

    @Test
    void Should_ReturnUpdateResponseOfExistApplicationEntity_When_RequestGrantApprovalAmountOfJudgementInfo(){
        Judgement judgementEntity = Judgement.builder()
                .name("Member Kim")
                .applicationId(1L)
                .approvalAmount(BigDecimal.valueOf(5000000))
                .build();

        Application applicationEntity = Application.builder()
                .applicationId(1L)
                .build();

        when(judgementRepository.findById(1L)).thenReturn(Optional.ofNullable(judgementEntity));
        when(applicationRepository.findById(1L)).thenReturn(Optional.ofNullable(applicationEntity));
        when(applicationRepository.save(ArgumentMatchers.any(Application.class))).thenReturn(applicationEntity);
        ApplicationDTO.GrantAmount actual = judgementService.grant(1L);

        Assertions.assertThat(actual.getApplicationId()).isSameAs(1L);
        Assertions.assertThat(actual.getApprovalAmount()).isSameAs(judgementEntity.getApprovalAmount());
    }
}