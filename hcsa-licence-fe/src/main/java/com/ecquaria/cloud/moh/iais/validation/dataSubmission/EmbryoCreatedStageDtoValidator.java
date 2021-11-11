package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.EmbryoCreatedStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
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
        int totalThawedMax =100;
        int totalFreshMax =100;
        if(arSuperDataSubmissionDto.getPatientInventoryDto()!=null){
            totalThawedMax=arSuperDataSubmissionDto.getPatientInventoryDto().getCurrentThawedOocytes();
            totalFreshMax=arSuperDataSubmissionDto.getPatientInventoryDto().getCurrentFreshOocytes();
        }
        int totalThawedNum =0;
        int totalFreshNum =0;


        if (embryoCreatedStageDto.getTransEmbrFreshOccNum()==null) {
            String errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006","This field", "field");
            errorMap.put("othersReason", errMsg);
        }else if(embryoCreatedStageDto.getTransEmbrFreshOccNum()>99){
            totalFreshNum+=embryoCreatedStageDto.getTransEmbrFreshOccNum();
            Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
            repMap.put("number","2");
            repMap.put("fieldNo","This field");
            String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
            errorMap.put("transEmbrFreshOccNum", errMsg);
        }

        if (embryoCreatedStageDto.getPoorDevFreshOccNum()==null) {
            String errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006","This field", "field");
            errorMap.put("poorDevFreshOccNum", errMsg);
        }else if(embryoCreatedStageDto.getPoorDevFreshOccNum()>99){
            totalFreshNum+=embryoCreatedStageDto.getPoorDevFreshOccNum();
            Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
            repMap.put("number","2");
            repMap.put("fieldNo","This field");
            String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
            errorMap.put("poorDevFreshOccNum", errMsg);
        }

        if (embryoCreatedStageDto.getTransEmbrThawOccNum() == null) {
            String errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006","This field", "field");
            errorMap.put("transEmbrThawOccNum", errMsg);
        }else if(embryoCreatedStageDto.getTransEmbrThawOccNum()>99){
            totalThawedNum+=embryoCreatedStageDto.getTransEmbrThawOccNum();
            Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
            repMap.put("number","2");
            repMap.put("fieldNo","This field");
            String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
            errorMap.put("transEmbrThawOccNum", errMsg);
        }

        if (embryoCreatedStageDto.getPoorDevThawOccNum() == null) {
            String errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006","This field", "field");
            errorMap.put("poorDevThawOccNum", errMsg);
        }else if(embryoCreatedStageDto.getPoorDevThawOccNum()>99){
            totalThawedNum+=embryoCreatedStageDto.getPoorDevThawOccNum();
            Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
            repMap.put("number","2");
            repMap.put("fieldNo","This field");
            String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
            errorMap.put("poorDevThawOccNum", errMsg);
        }

        String errMsgFresh = "Total sum of data item 1, 2 cannot be greater than number of fresh oocytes tagged to patient";
        String errMsgThawed = "Total sum of data item 3, 4 cannot be greater than number of thawed oocytes tagged to patient";

        if(totalThawedNum>totalThawedMax){
            errorMap.put("poorDevThawOccNum", errMsgThawed);

        }
        if(totalFreshNum>totalFreshMax){
            errorMap.put("poorDevFreshOccNum", errMsgFresh);

        }
        return errorMap;
    }
}
