package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.EmbryoCreatedStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInventoryDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * EmbryoCreatedDelegator
 *
 * @author junyu
 * @date 2021/10/26
 */
@Delegator("embryoCreatedDelegator")
@Slf4j
public class EmbryoCreatedDelegator extends CommonDelegator{
    @Override
    public void start(BaseProcessClass bpc) {
        AuditTrailHelper.auditFunction("Assisted Reproduction", "Embryo Created Cycle Stage");

        ArSuperDataSubmissionDto arSuperDataSubmissionDto= DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        if(arSuperDataSubmissionDto==null){
            arSuperDataSubmissionDto=new ArSuperDataSubmissionDto();
        }
        if(arSuperDataSubmissionDto.getEmbryoCreatedStageDto()==null){
            arSuperDataSubmissionDto.setEmbryoCreatedStageDto(new EmbryoCreatedStageDto());
        }


        ParamUtil.setSessionAttr(bpc.request,DataSubmissionConstant.AR_DATA_SUBMISSION,arSuperDataSubmissionDto);
    }

    @Override
    public void prepareSwitch(BaseProcessClass bpc) {
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>Cycle Stages</strong>");

    }



    @Override
    public void pageAction(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto= DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        EmbryoCreatedStageDto embryoCreatedStageDto=arSuperDataSubmissionDto.getEmbryoCreatedStageDto();
        HttpServletRequest request=bpc.request;
        int totalNum =0;
        Integer transEmbrFreshOccNum =  null;
        try {
            transEmbrFreshOccNum =  ParamUtil.getInt(request, "transEmbrFreshOccNum");
            totalNum+=transEmbrFreshOccNum;
        }catch (Exception e){
            log.error("no int");
        }
        Integer poorDevFreshOccNum = null;
        try {
            poorDevFreshOccNum = ParamUtil.getInt(request, "poorDevFreshOccNum");
            totalNum+=poorDevFreshOccNum;
        }catch (Exception e){
            log.error("no int");
        }
        Integer transEmbrThawOccNum = null;
        try {
            transEmbrThawOccNum =  ParamUtil.getInt(request, "transEmbrThawOccNum");
            totalNum+=transEmbrThawOccNum;
        }catch (Exception e){
            log.error("no int");
        }
        Integer poorDevThawOccNum = null;
        try {
            poorDevThawOccNum =  ParamUtil.getInt(request, "poorDevThawOccNum");
            totalNum+=poorDevThawOccNum;
        }catch (Exception e){
            log.error("no int");
        }
        embryoCreatedStageDto.setTransEmbrFreshOccNum(transEmbrFreshOccNum);
        embryoCreatedStageDto.setTransEmbrThawOccNum(transEmbrThawOccNum);
        embryoCreatedStageDto.setPoorDevFreshOccNum(poorDevFreshOccNum);
        embryoCreatedStageDto.setPoorDevThawOccNum(poorDevThawOccNum);

        embryoCreatedStageDto.setTotalNum(totalNum);


        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto,bpc.request);
        String actionType=ParamUtil.getRequestString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE);
        if("confirm".equals(actionType)){
            ValidationResult validationResult = WebValidationHelper.validateProperty(embryoCreatedStageDto, "save");
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
        ArSuperDataSubmissionDto arSuperDataSubmissionDto= DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        EmbryoCreatedStageDto embryoCreatedStageDto=arSuperDataSubmissionDto.getEmbryoCreatedStageDto();
        PatientInventoryDto patientInventoryDto = new PatientInventoryDto();
        if(arSuperDataSubmissionDto.getPatientInventoryDto()!=null){
            patientInventoryDto=arSuperDataSubmissionDto.getPatientInventoryDto();
        }
//        patientInventoryDto.setChangeFreshOocytes(-embryoCreatedStageDto.getPoorDevFreshOccNum()-embryoCreatedStageDto.getTransEmbrFreshOccNum());
//        patientInventoryDto.setChangeThawedOocytes(-embryoCreatedStageDto.getPoorDevThawOccNum()-embryoCreatedStageDto.getTransEmbrThawOccNum());

        patientInventoryDto.setChangeFreshEmbryos(embryoCreatedStageDto.getTotalNum());

        ParamUtil.setRequestAttr(bpc.request, "patientInventoryDto", patientInventoryDto);
    }
}
