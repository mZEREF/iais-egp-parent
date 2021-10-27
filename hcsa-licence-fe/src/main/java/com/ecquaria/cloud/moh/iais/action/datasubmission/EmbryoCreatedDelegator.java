package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.EmbryoCreatedStageDto;
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
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "page");
        ParamUtil.setSessionAttr(bpc.request, "smallTitle", "You are submitting for <strong>Cycle Stages</strong>");
        ArSuperDataSubmissionDto arSuperDataSubmissionDto= DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        if(arSuperDataSubmissionDto==null){
            arSuperDataSubmissionDto=new ArSuperDataSubmissionDto();
        }
        arSuperDataSubmissionDto.setEmbryoCreatedStageDto(new EmbryoCreatedStageDto());


        ParamUtil.setSessionAttr(bpc.request,DataSubmissionConstant.AR_DATA_SUBMISSION,arSuperDataSubmissionDto);
    }

    @Override
    public void prepareSwitch(BaseProcessClass bpc) {

    }

    @Override
    public void returnStep(BaseProcessClass bpc) {

    }

    @Override
    public void preparePage(BaseProcessClass bpc) {

    }

    @Override
    public void prepareConfim(BaseProcessClass bpc) {

    }

    @Override
    public void draft(BaseProcessClass bpc) {

    }

    @Override
    public void submission(BaseProcessClass bpc) {

    }

    @Override
    public void pageAction(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto= DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        EmbryoCreatedStageDto embryoCreatedStageDto=arSuperDataSubmissionDto.getEmbryoCreatedStageDto();
        HttpServletRequest request=bpc.request;
        Integer transEmbrFreshOccNum =  null;
        try {
            transEmbrFreshOccNum =  ParamUtil.getInt(request, "transEmbrFreshOccNum");
        }catch (Exception e){
            log.error("no int");
        }
        Integer poorDevFreshOccNum = null;
        try {
            poorDevFreshOccNum = ParamUtil.getInt(request, "poorDevFreshOccNum");
        }catch (Exception e){
            log.error("no int");
        }
        Integer transEmbrThawOccNum = null;
        try {
            transEmbrThawOccNum =  ParamUtil.getInt(request, "transEmbrThawOccNum");
        }catch (Exception e){
            log.error("no int");
        }
        Integer poorDevThawOccNum = null;
        try {
            poorDevThawOccNum =  ParamUtil.getInt(request, "poorDevThawOccNum");
        }catch (Exception e){
            log.error("no int");
        }
        int totalNum =0;
        if(transEmbrFreshOccNum!=null){
            totalNum+=transEmbrFreshOccNum;
        }
        if(poorDevFreshOccNum!=null){
            totalNum+=poorDevFreshOccNum;
        }
        if(transEmbrThawOccNum!=null){
            totalNum+=transEmbrThawOccNum;
        }
        if(poorDevThawOccNum!=null){
            totalNum+=poorDevThawOccNum;
        }
        embryoCreatedStageDto.setTransEmbrFreshOccNum(transEmbrFreshOccNum);
        embryoCreatedStageDto.setTransEmbrThawOccNum(transEmbrThawOccNum);
        embryoCreatedStageDto.setPoorDevFreshOccNum(poorDevFreshOccNum);
        embryoCreatedStageDto.setPoorDevThawOccNum(poorDevThawOccNum);

        embryoCreatedStageDto.setTotalNum(totalNum);


        embryoCreatedStageDto.setSubmissionId(MasterCodeUtil.CATE_ID_EFO_REASON);

        ValidationResult validationResult = WebValidationHelper.validateProperty(embryoCreatedStageDto, "save");
        Map<String, String> errorMap = validationResult.retrieveAll();
        ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.AR_DATA_SUBMISSION, arSuperDataSubmissionDto);
        if (!errorMap.isEmpty() || validationResult.isHasErrors()) {
            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "page");
            return;
        }


        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "confirm");
    }

    @Override
    public void pageConfirmAction(BaseProcessClass bpc) {

    }
}
