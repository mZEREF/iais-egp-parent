package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.EfoCycleStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * EfoDtoValidate
 *
 * @author junyu
 * @date 2021/10/21
 */
@Component
@Slf4j
public class EfoDtoValidator implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest httpServletRequest) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        ArSuperDataSubmissionDto arSuperDataSubmissionDto= DataSubmissionHelper.getCurrentArDataSubmission(httpServletRequest);
        if (arSuperDataSubmissionDto == null){
            return errorMap;
        }
        if (arSuperDataSubmissionDto.getSelectionDto() == null){
            return errorMap;
        }
        String cycle = arSuperDataSubmissionDto.getSelectionDto().getCycle();
        if (StringUtil.isNotEmpty(cycle)){
            isEfoCycle(cycle, arSuperDataSubmissionDto, errorMap);
        }
        return errorMap;
    }

    private static void isEfoCycle(String cycle, ArSuperDataSubmissionDto arSuperDataSubmissionDto,Map<String, String> errorMap){
        if (DataSubmissionConsts.AR_CYCLE_EFO.equals(cycle)
                || DataSubmissionConsts.DS_CYCLE_EFO.equals(cycle)){
            EfoCycleStageDto efoCycleStageDto = arSuperDataSubmissionDto.getOfoCycleStageDto();
            doValidateEfoCycleStageDto(errorMap, arSuperDataSubmissionDto, efoCycleStageDto);
        } else if (DataSubmissionConsts.AR_CYCLE_SFO.equals(cycle)
                || DataSubmissionConsts.DS_CYCLE_SFO.equals(cycle)){
            EfoCycleStageDto efoCycleStageDto = arSuperDataSubmissionDto.getEfoCycleStageDto();
            doValidateEfoCycleStageDto(errorMap, arSuperDataSubmissionDto, efoCycleStageDto);
        }
    }

    private static void doValidateEfoCycleStageDto(Map<String, String> errorMap, ArSuperDataSubmissionDto arSuperDataSubmissionDto,
                                                   EfoCycleStageDto efoCycleStageDto) {
        if (efoCycleStageDto == null){
            return;
        }
        Date sDate = efoCycleStageDto.getStartDate();
        String reason = efoCycleStageDto.getReason();
        String othersReason = efoCycleStageDto.getOtherReason();
        String cryopresNumStr = efoCycleStageDto.getCryopresNumStr();
        String others = efoCycleStageDto.getOthers();
        if (!StringUtil.isEmpty(sDate) ) {
            Date today = new Date();
            if( sDate.after(today)) {
                errorMap.put("startDate", MessageUtil.replaceMessage("DS_ERR001","Date Started", "field"));
            }
        }

        if (efoCycleStageDto.getCryopresNum() == null && StringUtil.isEmpty(cryopresNumStr)) {
            String errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006","No.Cryopreserved", "field");
            errorMap.put("cryopresNum", errMsg);
        } else if (StringUtil.isNotEmpty(cryopresNumStr)) {
            String errMsg = MessageUtil.replaceMessage("GENERAL_ERR0002","No.Cryopreserved", "field");
            errorMap.put("cryopresNum", errMsg);
        } else if (DataSubmissionConsts.DS_CYCLE_EFO.equals(arSuperDataSubmissionDto.getSelectionDto().getCycle()) && efoCycleStageDto.getCryopresNum() < 0) {
            Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
            repMap.put("minNum","0");
            repMap.put("maxNum","99");
            repMap.put("field","This field");
            String errMsg = MessageUtil.getMessageDesc("DS_ERR003",repMap);
            errorMap.put("cryopresNum", errMsg);
        } else if (DataSubmissionConsts.DS_CYCLE_SFO.equals(arSuperDataSubmissionDto.getSelectionDto().getCycle()) && efoCycleStageDto.getCryopresNum() <= 0) {
            Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
            repMap.put("minNum","1");
            repMap.put("maxNum","99");
            repMap.put("field","This field");
            String errMsg = MessageUtil.getMessageDesc("DS_ERR003",repMap);
            errorMap.put("cryopresNum", errMsg);
        }

        if (DataSubmissionConsts.DS_CYCLE_EFO.equals(arSuperDataSubmissionDto.getSelectionDto().getCycle())
                && efoCycleStageDto.getCryopresNum() != null && efoCycleStageDto.getCryopresNum() == 0
                && StringUtil.isEmpty(others)) {
            String errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006","others", "field");
            errorMap.put("others", errMsg);
        }

        if(efoCycleStageDto.getIsMedicallyIndicated()==1){
            if (!StringUtil.isEmpty(reason)&& DataSubmissionConsts.EFO_REASON_OTHERS.equals(reason)) {
                if (StringUtil.isEmpty(othersReason)) {
                    String errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006","Others Reason", "field");
                    errorMap.put("othersReason", errMsg);
                }else if(othersReason.length()>100){
                    Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                    repMap.put("number","100");
                    repMap.put("fieldNo","Others Reason");
                    String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                    errorMap.put("othersReason", errMsg);
                }
            }
        }
        if(efoCycleStageDto.getIsMedicallyIndicated()==0){
            if(!StringUtil.isEmpty(reason)&&reason.length()>100){
                Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                repMap.put("number","100");
                repMap.put("fieldNo","Reason");
                String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                errorMap.put("reason", errMsg);
            }
        }
    }

}
