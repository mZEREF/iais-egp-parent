package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DisposalStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInventoryDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * DisposalStageDelegator
 *
 * @author junyu
 * @date 2021/11/5
 */
@Delegator("disposalDelegator")
@Slf4j
public class DisposalStageDelegator extends CommonDelegator{
    @Override
    public void start(BaseProcessClass bpc) {
        AuditTrailHelper.auditFunction("Assisted Reproduction", "Disposal");

        ArSuperDataSubmissionDto arSuperDataSubmissionDto= DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        if(arSuperDataSubmissionDto==null){
            arSuperDataSubmissionDto=new ArSuperDataSubmissionDto();
        }
        if(arSuperDataSubmissionDto.getDisposalStageDto()==null){
            arSuperDataSubmissionDto.setDisposalStageDto(new DisposalStageDto());
        }

        ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.AR_DATA_SUBMISSION,arSuperDataSubmissionDto);
    }
    @Override
    public void prepareSwitch(BaseProcessClass bpc) {
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>Cycle Stages</strong>");

        List<SelectOption> disposalTypeSelectOption= MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_DISPOSAL_TYPE);
        ParamUtil.setRequestAttr(bpc.request,"disposalTypeSelectOption",disposalTypeSelectOption);
    }

    @Override
    public void pageAction(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto= DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        DisposalStageDto disposalStageDto=arSuperDataSubmissionDto.getDisposalStageDto();
        HttpServletRequest request=bpc.request;
        String disposedType=ParamUtil.getString(request,"disposedType");
        disposalStageDto.setDisposedType(disposedType);
        int totalNum =0;

        if(disposedType!=null){
            switch (disposedType){
                case "DISPTY001":
                case "DISPTY002":
                case "DISPTY003":
                    disposalStageDto.setDisposedTypeDisplay(1);
                    Integer immature =  null;
                    try {
                        immature =  ParamUtil.getInt(request, "immature");
                        totalNum+=immature;
                        disposalStageDto.setImmature(immature);
                    }catch (Exception e){
                        log.error("no int");
                    }
                    Integer abnormallyFertilised = null;
                    try {
                        abnormallyFertilised = ParamUtil.getInt(request, "abnormallyFertilised");
                        totalNum+=abnormallyFertilised;
                        disposalStageDto.setAbnormallyFertilised(abnormallyFertilised);
                    }catch (Exception e){
                        log.error("no int");
                    }
                    Integer unfertilised = null;
                    try {
                        unfertilised =  ParamUtil.getInt(request, "unfertilised");
                        totalNum+=unfertilised;
                        disposalStageDto.setUnfertilised(unfertilised);
                    }catch (Exception e){
                        log.error("no int");
                    }
                    Integer atretic = null;
                    try {
                        atretic =  ParamUtil.getInt(request, "atretic");
                        totalNum+=atretic;
                        disposalStageDto.setAtretic(atretic);
                    }catch (Exception e){
                        log.error("no int");
                    }
                    Integer damaged = null;
                    try {
                        damaged =  ParamUtil.getInt(request, "damaged");
                        totalNum+=damaged;
                        disposalStageDto.setDamaged(damaged);
                    }catch (Exception e){
                        log.error("no int");
                    }
                    Integer lysedOrDegenerated = null;
                    try {
                        lysedOrDegenerated =  ParamUtil.getInt(request, "lysedOrDegenerated");
                        totalNum+=lysedOrDegenerated;
                        disposalStageDto.setLysedOrDegenerated(lysedOrDegenerated);
                    }catch (Exception e){
                        log.error("no int");
                    }
                    break;
                case "DISPTY004":
                case "DISPTY005":
                case "DISPTY006":
                    disposalStageDto.setDisposedTypeDisplay(2);
                    Integer unhealthyNum = null;
                    try {
                        unhealthyNum =  ParamUtil.getInt(request, "unhealthyNum");
                        totalNum+=unhealthyNum;
                        disposalStageDto.setUnhealthyNum(unhealthyNum);
                    }catch (Exception e){
                        log.error("no int");
                    }
                    break;
                case "DISPTY007":
                    disposalStageDto.setDisposedTypeDisplay(3);
                    break;
                default:
            }
        }

        Integer otherDiscardedNum = null;
        try {
            otherDiscardedNum =  ParamUtil.getInt(request, "otherDiscardedNum");
            totalNum+=otherDiscardedNum;
            disposalStageDto.setOtherDiscardedNum(otherDiscardedNum);
        }catch (Exception e){
            log.error("no int");
        }
        String otherDiscardedReason=ParamUtil.getString(request,"otherDiscardedReason");
        disposalStageDto.setOtherDiscardedReason(otherDiscardedReason);
        disposalStageDto.setTotalNum(totalNum);
        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto,bpc.request);
        String actionType=ParamUtil.getRequestString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if("confirm".equals(actionType)){
            ValidationResult validationResult = WebValidationHelper.validateProperty(disposalStageDto, "save");
            Map<String, String> errorMap = validationResult.retrieveAll();

            if (!errorMap.isEmpty() || validationResult.isHasErrors()) {
                WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "page");
            }
        }
    }

    @Override
    public void prepareConfim(BaseProcessClass bpc) {
        PatientInventoryDto patientInventoryDto = new PatientInventoryDto();
        ParamUtil.setRequestAttr(bpc.request, "patientInventoryDto", patientInventoryDto);
    }
}
