package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.FertilisationDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * FertilisationDtoValidator
 *
 * @author fanghao
 * @date 2021/10/28
 */

@Component
@Slf4j
public class FertilisationDtoValidator implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = (ArSuperDataSubmissionDto) ParamUtil.getSessionAttr(request, DataSubmissionConstant.AR_DATA_SUBMISSION);
        FertilisationDto fertilisationDto = arSuperDataSubmissionDto.getFertilisationDto();
        List<String> atuList = fertilisationDto.getAtuList();
                if(IaisCommonUtils.isNotEmpty(atuList)){
                    if (atuList.contains(DataSubmissionConsts.AR_TECHNIQUES_USED_IVF)){
                        if (StringUtil.isEmpty(fertilisationDto.getFreshOocytesInseminatedNum())){
                            errorMap.put("freshOocytesInseminatedNum","GENERAL_ERR0006");
                        }
                        if (StringUtil.isEmpty(fertilisationDto.getThawedOocytesInseminatedNum())){
                            errorMap.put("thawedOocytesInseminatedNum","GENERAL_ERR0006");
                        }
                    }
                    if (atuList.contains(DataSubmissionConsts.AR_TECHNIQUES_USED_ICSI)){
                        if (StringUtil.isEmpty(fertilisationDto.getFreshOocytesMicroInjectedNum())){
                            errorMap.put("freshOocytesMicroinjectedNum","GENERAL_ERR0006");
                        }
                        if (StringUtil.isEmpty(fertilisationDto.getThawedOocytesMicroinjectedNum())){
                            errorMap.put("thawedOocytesMicroinjectedNum","GENERAL_ERR0006");
                        }
                    }
                    if (atuList.contains(DataSubmissionConsts.AR_TECHNIQUES_USED_GIFT)){
                        if (StringUtil.isEmpty(fertilisationDto.getFreshOocytesGiftNum())){
                            errorMap.put("freshOocytesGiftNum","GENERAL_ERR0006");
                        }
                        if (StringUtil.isEmpty(fertilisationDto.getThawedOocytesGiftNum())){
                            errorMap.put("thawedOocytesGiftNum","GENERAL_ERR0006");
                        }
                    }
                    if (atuList.contains(DataSubmissionConsts.AR_TECHNIQUES_USED_ZIFT)){
                        if (StringUtil.isEmpty(fertilisationDto.getFreshOocytesZiftNum())){
                            errorMap.put("freshOocytesZiftNum","GENERAL_ERR0006");
                        }
                        if (StringUtil.isEmpty(fertilisationDto.getThawedOocytesZiftNum())){
                            errorMap.put("thawedOocytesZiftNum","GENERAL_ERR0006");
                        }
                    }

                }
        return  errorMap;
    }
}
