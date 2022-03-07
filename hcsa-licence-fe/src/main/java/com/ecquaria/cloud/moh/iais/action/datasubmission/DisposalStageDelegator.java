package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArChangeInventoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DisposalStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.client.ArFeClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private ArFeClient arFeClient;

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
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>Disposal</strong>");

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
        boolean isInt=true;
        if(disposedType!=null){
            switch (disposedType){
                case DataSubmissionConsts.DISPOSAL_TYPE_FRESH_OOCYTE:
                case DataSubmissionConsts.DISPOSAL_TYPE_FROZEN_OOCYTE:
                case DataSubmissionConsts.DISPOSAL_TYPE_THAWED_OOCYTE:
                    disposalStageDto.setDisposedTypeDisplay(1);
                    Integer immature =  null;
                    try {
                        String immatureString=ParamUtil.getString(request, "immature");
                        disposalStageDto.setImmatureString(immatureString);
                        if(StringUtil.isEmpty(immatureString)){
                            disposalStageDto.setImmature(null);
                        }else {
                            immature =  ParamUtil.getInt(request, "immature");
                            totalNum+=immature;
                            disposalStageDto.setImmature(immature);
                        }

                    }catch (Exception e){
                        log.error("no int");
                        isInt=false;
                        disposalStageDto.setImmature(null);
                    }
                    Integer abnormallyFertilised = null;
                    try {
                        String abnormallyFertilisedString=ParamUtil.getString(request, "abnormallyFertilised");
                        disposalStageDto.setAbnormallyFertilisedString(abnormallyFertilisedString);
                        if(StringUtil.isEmpty(abnormallyFertilisedString)){
                            disposalStageDto.setAbnormallyFertilised(null);
                        }else {
                            abnormallyFertilised = ParamUtil.getInt(request, "abnormallyFertilised");
                            totalNum+=abnormallyFertilised;
                            disposalStageDto.setAbnormallyFertilised(abnormallyFertilised);
                        }

                    }catch (Exception e){
                        log.error("no int");
                        isInt=false;
                        disposalStageDto.setAbnormallyFertilised(null);
                    }
                    Integer unfertilised = null;
                    try {
                        String unfertilisedString=ParamUtil.getString(request, "unfertilised");
                        disposalStageDto.setUnfertilisedString(unfertilisedString);
                        if(StringUtil.isEmpty(unfertilisedString)){
                            disposalStageDto.setUnfertilised(null);
                        }else {
                            unfertilised =  ParamUtil.getInt(request, "unfertilised");
                            totalNum+=unfertilised;
                            disposalStageDto.setUnfertilised(unfertilised);
                        }

                    }catch (Exception e){
                        isInt=false;
                        disposalStageDto.setUnfertilised(null);
                        log.error("no int");
                    }
                    Integer atretic = null;
                    try {
                        String atreticString=ParamUtil.getString(request, "atretic");
                        disposalStageDto.setAtreticString(atreticString);
                        if(StringUtil.isEmpty(atreticString)){
                            disposalStageDto.setAtretic(null);
                        }else {
                            atretic =  ParamUtil.getInt(request, "atretic");
                            totalNum+=atretic;
                            disposalStageDto.setAtretic(atretic);
                        }

                    }catch (Exception e){
                        isInt=false;
                        disposalStageDto.setAtretic(null);
                        log.error("no int");
                    }
                    Integer damaged = null;
                    try {
                        String damagedString=ParamUtil.getString(request, "damaged");
                        disposalStageDto.setDamagedString(damagedString);
                        if(StringUtil.isEmpty(damagedString)){
                            disposalStageDto.setDamaged(null);
                        }else {
                            damaged =  ParamUtil.getInt(request, "damaged");
                            totalNum+=damaged;
                            disposalStageDto.setDamaged(damaged);
                        }

                    }catch (Exception e){
                        isInt=false;
                        disposalStageDto.setDamaged(null);
                        log.error("no int");
                    }
                    Integer lysedOrDegenerated = null;
                    try {
                        String lysedOrDegeneratedString=ParamUtil.getString(request, "lysedOrDegenerated");
                        disposalStageDto.setLysedOrDegeneratedString(lysedOrDegeneratedString);
                        if(StringUtil.isEmpty(lysedOrDegeneratedString)){
                            disposalStageDto.setLysedOrDegenerated(null);
                        }else {
                            lysedOrDegenerated =  ParamUtil.getInt(request, "lysedOrDegenerated");
                            totalNum+=lysedOrDegenerated;
                            disposalStageDto.setLysedOrDegenerated(lysedOrDegenerated);
                        }

                    }catch (Exception e){
                        isInt=false;
                        disposalStageDto.setLysedOrDegenerated(null);
                        log.error("no int");
                    }
                    break;
                case DataSubmissionConsts.DISPOSAL_TYPE_FRESH_EMBRYO:
                case DataSubmissionConsts.DISPOSAL_TYPE_FROZEN_EMBRYO:
                case DataSubmissionConsts.DISPOSAL_TYPE_THAWED_EMBRYO:
                    disposalStageDto.setDisposedTypeDisplay(2);
                    Integer unhealthyNum = null;
                    try {
                        String unhealthyNumString=ParamUtil.getString(request, "unhealthyNum");
                        disposalStageDto.setUnhealthyNumString(unhealthyNumString);
                        if(StringUtil.isEmpty(unhealthyNumString)){
                            disposalStageDto.setUnhealthyNum(null);
                        }else {
                            unhealthyNum =  ParamUtil.getInt(request, "unhealthyNum");
                            totalNum+=unhealthyNum;
                            disposalStageDto.setUnhealthyNum(unhealthyNum);
                        }

                    }catch (Exception e){
                        disposalStageDto.setUnhealthyNum(null);
                        isInt=false;
                        log.error("no int");
                    }
                    break;
                case DataSubmissionConsts.DISPOSAL_TYPE_FROZEN_SPERM:
                    disposalStageDto.setDisposedTypeDisplay(3);
                    break;
                default:
            }
        }

        Integer otherDiscardedNum = null;
        try {
            String otherDiscardedNumString=ParamUtil.getString(request, "otherDiscardedNum");
            disposalStageDto.setOtherDiscardedNumString(otherDiscardedNumString);
            if(StringUtil.isEmpty(otherDiscardedNumString)){
                disposalStageDto.setOtherDiscardedNum(null);
            }else {
                otherDiscardedNum =  ParamUtil.getInt(request, "otherDiscardedNum");
                totalNum+=otherDiscardedNum;
                disposalStageDto.setOtherDiscardedNum(otherDiscardedNum);
            }

        }catch (Exception e){
            disposalStageDto.setOtherDiscardedNum(null);
            isInt=false;
            log.error("no int");
        }
        String otherDiscardedReason=ParamUtil.getString(request,"otherDiscardedReason");
        disposalStageDto.setOtherDiscardedReason(otherDiscardedReason);
        if(isInt){
            disposalStageDto.setTotalNum(totalNum);
        }else {
            disposalStageDto.setTotalNum(null);
        }
        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto,bpc.request);
        String actionType=ParamUtil.getRequestString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if("confirm".equals(actionType)){
            ValidationResult validationResult = WebValidationHelper.validateProperty(disposalStageDto, "save");
            Map<String, String> errorMap = validationResult.retrieveAll();
            verifyRfcCommon(request, errorMap);
            if (!errorMap.isEmpty() || validationResult.isHasErrors()) {
                WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "page");
            }
            valRFC(bpc.request,disposalStageDto);

        }
    }

    @Override
    public void prepareConfim(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto= DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        DisposalStageDto disposalStageDto=arSuperDataSubmissionDto.getDisposalStageDto();
        ArChangeInventoryDto arChangeInventoryDto = new ArChangeInventoryDto();

        switch (disposalStageDto.getDisposedType()){
            case DataSubmissionConsts.DISPOSAL_TYPE_FRESH_OOCYTE:
                arChangeInventoryDto.setFreshOocyteNum(-disposalStageDto.getTotalNum());
                break;
            case DataSubmissionConsts.DISPOSAL_TYPE_FROZEN_OOCYTE:
                arChangeInventoryDto.setFrozenOocyteNum(-disposalStageDto.getTotalNum());
                break;
            case DataSubmissionConsts.DISPOSAL_TYPE_THAWED_OOCYTE:
                arChangeInventoryDto.setThawedOocyteNum(-disposalStageDto.getTotalNum());
                break;
            case DataSubmissionConsts.DISPOSAL_TYPE_FRESH_EMBRYO:
                arChangeInventoryDto.setFreshEmbryoNum(-disposalStageDto.getTotalNum());
                break;
            case DataSubmissionConsts.DISPOSAL_TYPE_FROZEN_EMBRYO:
                arChangeInventoryDto.setFrozenEmbryoNum(-disposalStageDto.getTotalNum());
                break;
            case DataSubmissionConsts.DISPOSAL_TYPE_THAWED_EMBRYO:
                arChangeInventoryDto.setThawedEmbryoNum(-disposalStageDto.getTotalNum());
                break;
            case DataSubmissionConsts.DISPOSAL_TYPE_FROZEN_SPERM:
                arChangeInventoryDto.setFrozenSpermNum(-disposalStageDto.getTotalNum());
                break;
            default:
        }
        arSuperDataSubmissionDto.setArChangeInventoryDto(arChangeInventoryDto);
        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto,bpc.request);
    }

    protected void valRFC(HttpServletRequest request, DisposalStageDto disposalStageDto){
        if(isRfc(request)){
            ArSuperDataSubmissionDto arOldSuperDataSubmissionDto = DataSubmissionHelper.getOldArDataSubmission(request);
            if(arOldSuperDataSubmissionDto != null && arOldSuperDataSubmissionDto.getDisposalStageDto()!= null && disposalStageDto.equals(arOldSuperDataSubmissionDto.getDisposalStageDto())){
                ParamUtil.setRequestAttr(request, DataSubmissionConstant.RFC_NO_CHANGE_ERROR, AppConsts.YES);
                ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE,ACTION_TYPE_PAGE);
            }
        }
    }
}
