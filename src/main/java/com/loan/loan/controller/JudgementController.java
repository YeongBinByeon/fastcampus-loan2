package com.loan.loan.controller;


import com.loan.loan.dto.JudgementDTO;
import com.loan.loan.dto.ResponseDTO;
import com.loan.loan.service.JudgementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/judgements")
public class JudgementController extends AbstractController{

    private final JudgementService judgementService;

    @PostMapping
    public ResponseDTO<JudgementDTO.Response> create(@RequestBody JudgementDTO.Request request){
        return ok(judgementService.create(request));
    }

    @GetMapping("/{judgementId}")
    public ResponseDTO<JudgementDTO.Response> get(@PathVariable Long judgementId){
        return ok(judgementService.get(judgementId));
    }

    @GetMapping("/applications/{applicationId}")
    public ResponseDTO<JudgementDTO.Response> getJudgementOfApplication(@PathVariable Long applicationId){
        return ok(judgementService.getJudgementOfApplication(applicationId));
    }
//
//    @PutMapping("/{judgementId}")
//    public ResponseDTO<JudgementDTO.Response> update(@PathVariable Long judgementId, @RequestBody JudgementDTO.Request request){
//        return ok(judgementService.update(judgementId, request));
//    }
//
//    @DeleteMapping("/{judgementId}")
//    public ResponseDTO<Void> delete(@PathVariable Long judgementId){
//        judgementService.delete(judgementId);
//        return ok();
//    }
//
//    @PatchMapping("/{judgementId}/grants")
//    public ResponseDTO<ApplicationDTO.GrantAmount> grant(@PathVariable Long judgementId){
//        return ok(judgementService.grant(judgementId));
//    }

}