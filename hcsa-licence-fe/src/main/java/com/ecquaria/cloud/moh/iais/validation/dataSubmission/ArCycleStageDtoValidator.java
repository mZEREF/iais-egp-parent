package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArCycleStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArDonorDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
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
        if( (IaisCommonUtils.getIntByNum(arCycleStageDto.getNoChildrenCurrentMarriage(),0)
                + IaisCommonUtils.getIntByNum(arCycleStageDto. getNoChildrenPreviousMarriage(),0) ) < IaisCommonUtils.getIntByNum(arCycleStageDto.getNoChildrenConceivedAR(),0) ){
            Map<String,String> stringStringMap = IaisCommonUtils.genNewHashMap(3);
            stringStringMap.put("field1","");
            stringStringMap.put("field2","No. of Children with Current Marriage");
            stringStringMap.put("field3","No. of Children with Previous Marriage");
            errorMap.put("noChildrenConceivedAR", MessageUtil.getMessageDesc("DS_ERR011",stringStringMap).trim());
        }
        List<ArDonorDto> arDonorDtos = arCycleStageDto.getArDonorDtos();
        arDonorDtos.forEach( arDonorDto -> {
                    if(arCycleStageDto.isUsedDonorOocyte() && arDonorDto.getAge() == null){
                        errorMap.put("age"+ arDonorDto.getArDonorIndex() ,"GENERAL_ERR0006");
                    }
                    if(!arDonorDto.isDirectedDonation() && StringUtil.isEmpty(arDonorDto.getDonorSampleCode())){
                        errorMap.put("donorSampleCode"+ arDonorDto.getArDonorIndex() ,"GENERAL_ERR0006");
                    }
                  }
                );
        return errorMap;
    }

}
