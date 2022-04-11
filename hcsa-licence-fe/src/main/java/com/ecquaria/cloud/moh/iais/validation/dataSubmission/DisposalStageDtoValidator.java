package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DisposalStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * DisposalStageDtoValidator
 *
 * @author junyu
 * @date 2021/11/5
 */
public class DisposalStageDtoValidator implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        ArSuperDataSubmissionDto arSuperDataSubmissionDto= DataSubmissionHelper.getCurrentArDataSubmission(request);
        DisposalStageDto disposalStageDto=arSuperDataSubmissionDto.getDisposalStageDto();
        String errMsgErr006 = MessageUtil.getMessageDesc("GENERAL_ERR0006");
        String errMsgErr002 = MessageUtil.getMessageDesc("GENERAL_ERR0002");
        String errMsg023 = MessageUtil.getMessageDesc("DS_ERR023");
        if(disposalStageDto.getDisposedTypeDisplay()!=null&&(disposalStageDto.getTotalNum()==null||disposalStageDto.getTotalNum()==0)){
            errorMap.put("totalNum", "One data item in the list must be entered");

        }
        int maxSamplesNum=100;

        if(arSuperDataSubmissionDto.getArCurrentInventoryDto()!=null){
            if(disposalStageDto.getDisposedType()!=null){
                switch (disposalStageDto.getDisposedType()){
                    case DataSubmissionConsts.DISPOSAL_TYPE_FRESH_OOCYTE:
                        maxSamplesNum=arSuperDataSubmissionDto.getArCurrentInventoryDto().getFreshOocyteNum();
                        break;
                    case DataSubmissionConsts.DISPOSAL_TYPE_FROZEN_OOCYTE:
                        maxSamplesNum=arSuperDataSubmissionDto.getArCurrentInventoryDto().getFrozenOocyteNum();
                        break;
                    case DataSubmissionConsts.DISPOSAL_TYPE_THAWED_OOCYTE:
                        maxSamplesNum=arSuperDataSubmissionDto.getArCurrentInventoryDto().getThawedOocyteNum();
                        break;
                    case DataSubmissionConsts.DISPOSAL_TYPE_FRESH_EMBRYO:
                        maxSamplesNum=arSuperDataSubmissionDto.getArCurrentInventoryDto().getFreshEmbryoNum();
                        break;
                    case DataSubmissionConsts.DISPOSAL_TYPE_FROZEN_EMBRYO:
                        maxSamplesNum=arSuperDataSubmissionDto.getArCurrentInventoryDto().getFrozenEmbryoNum();
                        break;
                    case DataSubmissionConsts.DISPOSAL_TYPE_THAWED_EMBRYO:
                        maxSamplesNum=arSuperDataSubmissionDto.getArCurrentInventoryDto().getThawedEmbryoNum();
                        break;
                    case DataSubmissionConsts.DISPOSAL_TYPE_FROZEN_SPERM:
                        maxSamplesNum=arSuperDataSubmissionDto.getArCurrentInventoryDto().getFrozenSpermNum();
                        break;
                    default:
                }
            }else {
                errorMap.put("disposedType", errMsgErr006);
            }
        }



        if(disposalStageDto.getImmature()!=null){
            if(disposalStageDto.getImmature()>99||disposalStageDto.getImmature()<0){
                Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                repMap.put("minNum","0");
                repMap.put("maxNum","99");
            repMap.put("field","This field");
                String errMsg = MessageUtil.getMessageDesc("DS_ERR003",repMap);
                errorMap.put("immature", errMsg);
            }
            if(disposalStageDto.getImmature()>maxSamplesNum){
                errorMap.put("totalNum", errMsg023);
            }
            if(disposalStageDto.getImmatureString().length()>2){
                String general_err0041= NewApplicationHelper.repLength("This field","2");
                errorMap.put("immature", general_err0041);
            }
        }else {
            if(StringUtil.isNotEmpty(disposalStageDto.getImmatureString())){
                errorMap.put("immature", errMsgErr002);
            }
        }

        if(disposalStageDto.getAbnormallyFertilised()!=null){
            if(disposalStageDto.getAbnormallyFertilised()>99||disposalStageDto.getAbnormallyFertilised()<0){
                Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                repMap.put("minNum","0");
                repMap.put("maxNum","99");
            repMap.put("field","This field");
                String errMsg = MessageUtil.getMessageDesc("DS_ERR003",repMap);
                errorMap.put("abnormallyFertilised", errMsg);
            }
            if(disposalStageDto.getAbnormallyFertilised()>maxSamplesNum){
                errorMap.put("totalNum", errMsg023);
            }
            if(disposalStageDto.getAbnormallyFertilisedString().length()>2){
                String general_err0041= NewApplicationHelper.repLength("This field","2");
                errorMap.put("abnormallyFertilised", general_err0041);
            }
        }else {
            if(StringUtil.isNotEmpty(disposalStageDto.getAbnormallyFertilisedString())){
                errorMap.put("abnormallyFertilised", errMsgErr002);
            }
        }

        if(disposalStageDto.getUnfertilised()!=null){
            if(disposalStageDto.getUnfertilised()>99||disposalStageDto.getUnfertilised()<0){
                Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                repMap.put("minNum","0");
                repMap.put("maxNum","99");
            repMap.put("field","This field");
                String errMsg = MessageUtil.getMessageDesc("DS_ERR003",repMap);
                errorMap.put("unfertilised", errMsg);
            }
            if(disposalStageDto.getUnfertilised()>maxSamplesNum){
                errorMap.put("totalNum", errMsg023);
            }
            if(disposalStageDto.getUnfertilisedString().length()>2){
                String general_err0041= NewApplicationHelper.repLength("This field","2");
                errorMap.put("unfertilised", general_err0041);
            }
        }else {
            if(StringUtil.isNotEmpty(disposalStageDto.getUnfertilisedString())){
                errorMap.put("unfertilised", errMsgErr002);
            }
        }


        if(disposalStageDto.getAtretic()!=null){
            if(disposalStageDto.getAtretic()>99||disposalStageDto.getAtretic()<0){
                Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                repMap.put("minNum","0");
                repMap.put("maxNum","99");
            repMap.put("field","This field");
                String errMsg = MessageUtil.getMessageDesc("DS_ERR003",repMap);
                errorMap.put("atretic", errMsg);
            }
            if(disposalStageDto.getAtretic()>maxSamplesNum){
                errorMap.put("totalNum", errMsg023);
            }
            if(disposalStageDto.getAtreticString().length()>2){
                String general_err0041= NewApplicationHelper.repLength("This field","2");
                errorMap.put("atretic", general_err0041);
            }
        }else {
            if(StringUtil.isNotEmpty(disposalStageDto.getAtreticString())){
                errorMap.put("atretic", errMsgErr002);
            }
        }

        if(disposalStageDto.getDamaged()!=null){
            if(disposalStageDto.getDamaged()>99||disposalStageDto.getDamaged()<0){
                Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                repMap.put("minNum","0");
                repMap.put("maxNum","99");
            repMap.put("field","This field");
                String errMsg = MessageUtil.getMessageDesc("DS_ERR003",repMap);
                errorMap.put("damaged", errMsg);
            }
            if(disposalStageDto.getDamaged()>maxSamplesNum){
                errorMap.put("totalNum", errMsg023);
            }
            if(disposalStageDto.getDamagedString().length()>2){
                String general_err0041= NewApplicationHelper.repLength("This field","2");
                errorMap.put("damaged", general_err0041);
            }
        }else {
            if(StringUtil.isNotEmpty(disposalStageDto.getDamagedString())){
                errorMap.put("damaged", errMsgErr002);
            }
        }

        if(disposalStageDto.getLysedOrDegenerated()!=null){
            if(disposalStageDto.getLysedOrDegenerated()>99||disposalStageDto.getLysedOrDegenerated()<0){
                Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                repMap.put("minNum","0");
                repMap.put("maxNum","99");
            repMap.put("field","This field");
                String errMsg = MessageUtil.getMessageDesc("DS_ERR003",repMap);
                errorMap.put("lysedOrDegenerated", errMsg);
            }
            if(disposalStageDto.getLysedOrDegenerated()>maxSamplesNum){
                errorMap.put("totalNum", errMsg023);
            }
            if(disposalStageDto.getLysedOrDegeneratedString().length()>2){
                String general_err0041= NewApplicationHelper.repLength("This field","2");
                errorMap.put("lysedOrDegenerated", general_err0041);
            }
        }else {
            if(StringUtil.isNotEmpty(disposalStageDto.getLysedOrDegeneratedString())){
                errorMap.put("lysedOrDegenerated", errMsgErr002);
            }
        }

        if(disposalStageDto.getUnhealthyNum()!=null){
            if(disposalStageDto.getUnhealthyNum()>99||disposalStageDto.getUnhealthyNum()<0){
                Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                repMap.put("minNum","0");
                repMap.put("maxNum","99");
            repMap.put("field","This field");
                String errMsg = MessageUtil.getMessageDesc("DS_ERR003",repMap);
                errorMap.put("unhealthyNum", errMsg);
            }
            if(disposalStageDto.getUnhealthyNum()>maxSamplesNum){
                errorMap.put("totalNum", errMsg023);
            }
            if(disposalStageDto.getUnhealthyNumString().length()>2){
                String general_err0041= NewApplicationHelper.repLength("This field","2");
                errorMap.put("unhealthyNum", general_err0041);
            }
        }else {
            if(StringUtil.isNotEmpty(disposalStageDto.getUnhealthyNumString())){
                errorMap.put("unhealthyNum", errMsgErr002);
            }
        }

        if(disposalStageDto.getOtherDiscardedNum()!=null){
            if(disposalStageDto.getOtherDiscardedNum()>99||disposalStageDto.getOtherDiscardedNum()<0){
                Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                repMap.put("minNum","0");
                repMap.put("maxNum","99");
            repMap.put("field","This field");
                String errMsg = MessageUtil.getMessageDesc("DS_ERR003",repMap);
                errorMap.put("otherDiscardedNum", errMsg);
            }
            if(disposalStageDto.getOtherDiscardedNum()>maxSamplesNum){
                errorMap.put("totalNum", errMsg023);
            }
            if(disposalStageDto.getOtherDiscardedNum()>0){

                if(StringUtil.isEmpty(disposalStageDto.getOtherDiscardedReason())){
                    errorMap.put("otherDiscardedReason", errMsgErr006);
                }else if(disposalStageDto.getOtherDiscardedReason().length()>20){
                    Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                    repMap.put("number","20");
                    repMap.put("fieldNo","This field");
                    String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                    errorMap.put("otherDiscardedReason", errMsg);
                }
                if(disposalStageDto.getOtherDiscardedNumString().length()>2){
                    String general_err0041= NewApplicationHelper.repLength("This field","2");
                    errorMap.put("otherDiscardedNum", general_err0041);
                }
            }
        }else {
            if(disposalStageDto.getDisposedType()!=null&&disposalStageDto.getDisposedType().equals(DataSubmissionConsts.DISPOSAL_TYPE_FROZEN_SPERM)){
                errorMap.put("otherDiscardedNum", errMsgErr006);
            }
            if(StringUtil.isNotEmpty(disposalStageDto.getOtherDiscardedNumString())){
                errorMap.put("otherDiscardedNum", errMsgErr002);
            }
        }
        if(disposalStageDto.getTotalNum()>maxSamplesNum){
            errorMap.put("totalNum", errMsg023);
        }
        return errorMap;
    }
}
