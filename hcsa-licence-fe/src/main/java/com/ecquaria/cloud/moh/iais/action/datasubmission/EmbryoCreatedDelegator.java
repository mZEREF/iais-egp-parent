package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArChangeInventoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.EmbryoCreatedStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.client.ArFeClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
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

    @Autowired
    private ArFeClient arFeClient;

    @Override
    public void start(BaseProcessClass bpc) {
        AuditTrailHelper.auditFunction("Assisted Reproduction", "Embryo Created Cycle Stage");
        ParamUtil.setSessionAttr(bpc.request, "totalFreshMax",0);
        ParamUtil.setSessionAttr(bpc.request, "totalThawedMax",0);
        ArSuperDataSubmissionDto arSuperDataSubmissionDto= DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
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

        int totalThawedMax =0;
        int totalFreshMax =0;
        List<DataSubmissionDto> dataSubmissionDtoList=arFeClient.getAllDataSubmissionByCycleId(arSuperDataSubmissionDto.getDataSubmissionDto().getCycleId()).getEntity();

        for (DataSubmissionDto dataSubmissionDto:dataSubmissionDtoList
        ) {
            if(dataSubmissionDto.getCycleStage().equals(DataSubmissionConsts.AR_STAGE_FERTILISATION)){
                ArSuperDataSubmissionDto arSuperFertilisationDto =arFeClient.getArSuperDataSubmissionDto(dataSubmissionDto.getSubmissionNo()).getEntity();

                if(arSuperFertilisationDto.getFertilisationDto().isGiftUsed()){
                    totalFreshMax+=Integer.parseInt(arSuperFertilisationDto.getFertilisationDto().getFreshOocytesGiftNum());
                    totalThawedMax+=Integer.parseInt(arSuperFertilisationDto.getFertilisationDto().getThawedOocytesGiftNum());
                }
                if(arSuperFertilisationDto.getFertilisationDto().isIcsiUsed()){
                    totalFreshMax+=Integer.parseInt(arSuperFertilisationDto.getFertilisationDto().getFreshOocytesMicroInjectedNum());
                    totalThawedMax+=Integer.parseInt(arSuperFertilisationDto.getFertilisationDto().getThawedOocytesMicroinjectedNum());
                }
                if(arSuperFertilisationDto.getFertilisationDto().isIvfUsed()){
                    totalFreshMax+=Integer.parseInt(arSuperFertilisationDto.getFertilisationDto().getFreshOocytesInseminatedNum());
                    totalThawedMax+=Integer.parseInt(arSuperFertilisationDto.getFertilisationDto().getThawedOocytesInseminatedNum());
                }
                if(arSuperFertilisationDto.getFertilisationDto().isZiftUsed()){
                    totalFreshMax+=Integer.parseInt(arSuperFertilisationDto.getFertilisationDto().getFreshOocytesZiftNum());
                    totalThawedMax+=Integer.parseInt(arSuperFertilisationDto.getFertilisationDto().getThawedOocytesZiftNum());
                }
              break;
            }
        }

        ParamUtil.setSessionAttr(bpc.request, "totalFreshMax",totalFreshMax);
        ParamUtil.setSessionAttr(bpc.request, "totalThawedMax",totalThawedMax);
        EmbryoCreatedStageDto embryoCreatedStageDto=arSuperDataSubmissionDto.getEmbryoCreatedStageDto();
        HttpServletRequest request=bpc.request;
        int totalNum =0;
        boolean isInt=true;

        Integer transEmbrFreshOccNum =  null;
        try {
            String transEmbrFreshOccNumString=ParamUtil.getString(request, "transEmbrFreshOccNum");
            embryoCreatedStageDto.setTransEmbrFreshOccNumStr(transEmbrFreshOccNumString);
            transEmbrFreshOccNum =  ParamUtil.getInt(request, "transEmbrFreshOccNum");
            totalNum+=transEmbrFreshOccNum;
        }catch (Exception e){
            log.error("no int");
                        isInt=false;
        }
        Integer poorDevFreshOccNum = null;
        try {
            String poorDevFreshOccNumString=ParamUtil.getString(request, "poorDevFreshOccNum");
            embryoCreatedStageDto.setPoorDevFreshOccNumStr(poorDevFreshOccNumString);
            poorDevFreshOccNum = ParamUtil.getInt(request, "poorDevFreshOccNum");
            totalNum+=poorDevFreshOccNum;
        }catch (Exception e){
            log.error("no int");
                        isInt=false;
        }
        Integer transEmbrThawOccNum = null;
        try {
            String transEmbrThawOccNumString=ParamUtil.getString(request, "transEmbrThawOccNum");
            embryoCreatedStageDto.setTransEmbrThawOccNumStr(transEmbrThawOccNumString);
            transEmbrThawOccNum =  ParamUtil.getInt(request, "transEmbrThawOccNum");
            totalNum+=transEmbrThawOccNum;
        }catch (Exception e){
            log.error("no int");
                        isInt=false;

        }
        Integer poorDevThawOccNum = null;
        try {
            String poorDevThawOccNumString=ParamUtil.getString(request, "poorDevThawOccNum");
            embryoCreatedStageDto.setPoorDevThawOccNumStr(poorDevThawOccNumString);
            poorDevThawOccNum =  ParamUtil.getInt(request, "poorDevThawOccNum");
            totalNum+=poorDevThawOccNum;
        }catch (Exception e){
            log.error("no int");
                        isInt=false;

        }
        embryoCreatedStageDto.setTransEmbrFreshOccNum(transEmbrFreshOccNum);
        embryoCreatedStageDto.setTransEmbrThawOccNum(transEmbrThawOccNum);
        embryoCreatedStageDto.setPoorDevFreshOccNum(poorDevFreshOccNum);
        embryoCreatedStageDto.setPoorDevThawOccNum(poorDevThawOccNum);
        if(isInt){
            embryoCreatedStageDto.setTotalNum(totalNum);
        }else {
            embryoCreatedStageDto.setTotalNum(null);
        }
        embryoCreatedStageDto.setTotalNum(totalNum);


        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto,bpc.request);
        String actionType=ParamUtil.getRequestString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE);
        if("confirm".equals(actionType)){
            ValidationResult validationResult = WebValidationHelper.validateProperty(embryoCreatedStageDto, "save");
            Map<String, String> errorMap = validationResult.retrieveAll();
            verifyRfcCommon(request, errorMap);

            if (!errorMap.isEmpty() || validationResult.isHasErrors()) {
                WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "page");
            }else valRFC(bpc.request,embryoCreatedStageDto);
        }
    }

    @Override
    public void prepareConfim(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto= DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        EmbryoCreatedStageDto embryoCreatedStageDto=arSuperDataSubmissionDto.getEmbryoCreatedStageDto();
        ArChangeInventoryDto arChangeInventoryDto = new ArChangeInventoryDto();

        arChangeInventoryDto.setFreshEmbryoNum(embryoCreatedStageDto.getTotalNum());
        arSuperDataSubmissionDto.setArChangeInventoryDto(arChangeInventoryDto);
        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto,bpc.request);


    }

    protected void valRFC(HttpServletRequest request, EmbryoCreatedStageDto cycleStageDto){
        if(isRfc(request)){
            ArSuperDataSubmissionDto arOldSuperDataSubmissionDto = DataSubmissionHelper.getOldArDataSubmission(request);
            if(arOldSuperDataSubmissionDto != null && arOldSuperDataSubmissionDto.getEmbryoCreatedStageDto()!= null && cycleStageDto.equals(arOldSuperDataSubmissionDto.getEmbryoCreatedStageDto())){
                ParamUtil.setRequestAttr(request, DataSubmissionConstant.RFC_NO_CHANGE_ERROR, AppConsts.YES);
                ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE,ACTION_TYPE_PAGE);
            }
        }
    }
}
