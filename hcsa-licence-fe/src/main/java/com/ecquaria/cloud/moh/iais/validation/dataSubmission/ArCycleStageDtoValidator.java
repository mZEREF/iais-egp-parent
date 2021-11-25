package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArCycleStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DonorDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.SgNoValidator;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * ArCycleStageDtoValidator
 *
 * @author wangyu
 * @date 2021/10/27
 */
@Component
@Slf4j
public class ArCycleStageDtoValidator implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
        ArCycleStageDto arCycleStageDto = arSuperDataSubmissionDto.getArCycleStageDto();

        if(arCycleStageDto.getTotalPreviouslyPreviously() != null && arCycleStageDto.getTotalPreviouslyPreviously() == 21 && StringUtil.isEmpty(arCycleStageDto.getCyclesUndergoneOverseas())){
            errorMap.put("cyclesUndergoneOverseas" ,"GENERAL_ERR0006");
        }

        if(arCycleStageDto.validateOtherIndicationOthersMandatory()
                && StringUtil.isEmpty(arCycleStageDto.getOtherIndicationOthers())){
            errorMap.put("otherIndicationOthers" ,"GENERAL_ERR0006");
        }

        if(arCycleStageDto.validateMainIndicationOtherMandatory() && StringUtil.isEmpty(arCycleStageDto.getMainIndicationOthers())){
            errorMap.put("mainIndicationOthers" ,"GENERAL_ERR0006");
        }

        if( !arCycleStageDto.validateEnhancedCounselling(arCycleStageDto.getEnhancedCounselling())){
            errorMap.put("enhancedCounselling" ,"GENERAL_ERR0006");
        }

        if( (IaisCommonUtils.getIntByNum(arCycleStageDto.getCurrentMarriageChildren(),0)
                + IaisCommonUtils.getIntByNum(arCycleStageDto. getPreviousMarriageChildren(),0) ) < IaisCommonUtils.getIntByNum(arCycleStageDto.getDeliveredThroughChildren(),0) ){
            Map<String,String> stringStringMap = IaisCommonUtils.genNewHashMap(3);
            stringStringMap.put("field1","");
            stringStringMap.put("field2","No. of Children with Current Marriage");
            stringStringMap.put("field3","No. of Children with Previous Marriage");
            errorMap.put("deliveredThroughChildren", MessageUtil.getMessageDesc("DS_ERR011",stringStringMap).trim());
        }
        DonorValidator.validateDonors(arCycleStageDto.getDonorDtos(),errorMap,arCycleStageDto.isUsedDonorOocyte());
        return errorMap;
    }

}
