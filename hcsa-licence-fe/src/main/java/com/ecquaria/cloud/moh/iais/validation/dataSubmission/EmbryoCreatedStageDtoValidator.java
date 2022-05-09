package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.EmbryoCreatedStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * EmbryoCreatedStageDtoValidator
 *
 * @author junyu
 * @date 2021/10/26
 */
@Component
@Slf4j
public class EmbryoCreatedStageDtoValidator implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest httpServletRequest) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto= DataSubmissionHelper.getCurrentArDataSubmission(httpServletRequest);
        EmbryoCreatedStageDto embryoCreatedStageDto=arSuperDataSubmissionDto.getEmbryoCreatedStageDto();
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        int totalThawedMax = (int) ParamUtil.getSessionAttr(httpServletRequest,"totalThawedMax");
        int totalFreshMax =(int) ParamUtil.getSessionAttr(httpServletRequest,"totalFreshMax");
        String errMsg002 = MessageUtil.getMessageDesc("DS_ERR002");

        int totalThawedNum =0;
        int totalFreshNum =0;
        String errMsgErr006 = MessageUtil.getMessageDesc("GENERAL_ERR0006");
        String errMsgErr002 = MessageUtil.getMessageDesc("GENERAL_ERR0002");

        if (embryoCreatedStageDto.getTransEmbrFreshOccNum()==null) {
            errorMap.put("transEmbrFreshOccNum", errMsgErr006);
            if(StringUtil.isNotEmpty(embryoCreatedStageDto.getTransEmbrFreshOccNumStr())){
                errorMap.put("transEmbrFreshOccNum", errMsgErr002);
            }
        }else if(embryoCreatedStageDto.getTransEmbrFreshOccNum()>99||embryoCreatedStageDto.getTransEmbrFreshOccNum()<0){

            Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
            repMap.put("minNum","0");
            repMap.put("maxNum","99");
            repMap.put("field","This field");
            String errMsg = MessageUtil.getMessageDesc("DS_ERR003",repMap);
            errorMap.put("transEmbrFreshOccNum", errMsg);
        }else {
            if(embryoCreatedStageDto.getTransEmbrFreshOccNumStr().length()>2){
                String general_err0041= NewApplicationHelper.repLength("This field","2");
                errorMap.put("transEmbrFreshOccNum", general_err0041);
            }
            totalFreshNum+=embryoCreatedStageDto.getTransEmbrFreshOccNum();
        }

        if (embryoCreatedStageDto.getPoorDevFreshOccNum()==null) {
            errorMap.put("poorDevFreshOccNum", errMsgErr006);
            if(StringUtil.isNotEmpty(embryoCreatedStageDto.getPoorDevFreshOccNumStr())){
                errorMap.put("poorDevFreshOccNum", errMsgErr002);
            }
        }else if(embryoCreatedStageDto.getPoorDevFreshOccNum()>99||embryoCreatedStageDto.getPoorDevFreshOccNum()<0){

            Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
            repMap.put("minNum","0");
            repMap.put("maxNum","99");
            repMap.put("field","This field");
            String errMsg = MessageUtil.getMessageDesc("DS_ERR003",repMap);
            errorMap.put("poorDevFreshOccNum", errMsg);
        }else {
            if(embryoCreatedStageDto.getPoorDevFreshOccNumStr().length()>2){
                String general_err0041= NewApplicationHelper.repLength("This field","2");
                errorMap.put("poorDevFreshOccNum", general_err0041);
            }
            totalFreshNum+=embryoCreatedStageDto.getPoorDevFreshOccNum();
        }

        if (embryoCreatedStageDto.getTransEmbrThawOccNum() == null) {
            errorMap.put("transEmbrThawOccNum", errMsgErr006);
            if(StringUtil.isNotEmpty(embryoCreatedStageDto.getTransEmbrThawOccNumStr())){
                errorMap.put("transEmbrThawOccNum", errMsgErr002);
            }
        }else if(embryoCreatedStageDto.getTransEmbrThawOccNum()>99||embryoCreatedStageDto.getTransEmbrThawOccNum()<0){
            Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
            repMap.put("minNum","0");
            repMap.put("maxNum","99");
            repMap.put("field","This field");
            String errMsg = MessageUtil.getMessageDesc("DS_ERR003",repMap);
            errorMap.put("transEmbrThawOccNum", errMsg);
        }else {
            if(embryoCreatedStageDto.getTransEmbrThawOccNumStr().length()>2){
                String general_err0041= NewApplicationHelper.repLength("This field","2");
                errorMap.put("transEmbrThawOccNum", general_err0041);
            }
            totalThawedNum+=embryoCreatedStageDto.getTransEmbrThawOccNum();
        }

        if (embryoCreatedStageDto.getPoorDevThawOccNum() == null) {
            errorMap.put("poorDevThawOccNum", errMsgErr006);
            if(StringUtil.isNotEmpty(embryoCreatedStageDto.getPoorDevThawOccNumStr())){
                errorMap.put("poorDevThawOccNum", errMsgErr002);
            }
        }else if(embryoCreatedStageDto.getPoorDevThawOccNum()>99||embryoCreatedStageDto.getPoorDevThawOccNum()<0){

            Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
            repMap.put("minNum","0");
            repMap.put("maxNum","99");
            repMap.put("field","This field");
            String errMsg = MessageUtil.getMessageDesc("DS_ERR003",repMap);
            errorMap.put("poorDevThawOccNum", errMsg);
        }else {
            if(embryoCreatedStageDto.getPoorDevThawOccNumStr().length()>2){
                String general_err0041= NewApplicationHelper.repLength("This field","2");
                errorMap.put("poorDevThawOccNum", general_err0041);
            }
            totalThawedNum+=embryoCreatedStageDto.getPoorDevThawOccNum();
        }


        if(totalThawedNum>totalThawedMax&&!errorMap.containsKey("poorDevThawOccNum")){
            errorMap.put("poorDevThawOccNum", errMsg002);
        }
        if(totalFreshNum>totalFreshMax&&!errorMap.containsKey("poorDevFreshOccNum")){
            errorMap.put("poorDevFreshOccNum", errMsg002);
        }
        return errorMap;
    }
}
