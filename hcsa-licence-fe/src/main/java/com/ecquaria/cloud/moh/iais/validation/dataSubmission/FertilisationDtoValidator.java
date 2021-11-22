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
        List<String> sosList = fertilisationDto.getSosList();
        int patientFrozen = 100;
        int thawedMaxNum = 100;
        int freshMaxNum = 100;
        if(arSuperDataSubmissionDto.getPatientInventoryDto() != null){
            thawedMaxNum=arSuperDataSubmissionDto.getPatientInventoryDto().getCurrentThawedOocytes();
            freshMaxNum=arSuperDataSubmissionDto.getPatientInventoryDto().getCurrentFreshOocytes();
           patientFrozen=arSuperDataSubmissionDto.getPatientInventoryDto().getCurrentFrozenSperms();
        }
        int totalThawedSum = 0;
        int totalFreshSum = 0;
                if(IaisCommonUtils.isNotEmpty(atuList)){
                    if (atuList.contains(DataSubmissionConsts.AR_TECHNIQUES_USED_IVF)){
                        if (StringUtil.isEmpty(fertilisationDto.getFreshOocytesInseminatedNum())){
                            errorMap.put("freshOocytesInseminatedNum","GENERAL_ERR0006");
                        }else if(!StringUtil.isEmpty(fertilisationDto.getFreshOocytesInseminatedNum())){
                            totalFreshSum+=Integer.valueOf(fertilisationDto.getFreshOocytesInseminatedNum());
                        }
                        if (StringUtil.isEmpty(fertilisationDto.getThawedOocytesInseminatedNum())){
                            errorMap.put("thawedOocytesInseminatedNum","GENERAL_ERR0006");
                        }else if(!StringUtil.isEmpty(fertilisationDto.getThawedOocytesInseminatedNum())){
                            totalThawedSum+=Integer.valueOf(fertilisationDto.getThawedOocytesInseminatedNum());
                        }
                    }
                    if (atuList.contains(DataSubmissionConsts.AR_TECHNIQUES_USED_ICSI)){
                        if (StringUtil.isEmpty(fertilisationDto.getFreshOocytesMicroInjectedNum())){
                            errorMap.put("freshOocytesMicroinjectedNum","GENERAL_ERR0006");
                        }else if(!StringUtil.isEmpty(fertilisationDto.getFreshOocytesMicroInjectedNum())){
                            totalFreshSum+=Integer.valueOf(fertilisationDto.getFreshOocytesMicroInjectedNum());
                        }
                        if (StringUtil.isEmpty(fertilisationDto.getThawedOocytesMicroinjectedNum())){
                            errorMap.put("thawedOocytesMicroinjectedNum","GENERAL_ERR0006");
                        }else if(!StringUtil.isEmpty(fertilisationDto.getThawedOocytesMicroinjectedNum())){
                            totalThawedSum+=Integer.valueOf(fertilisationDto.getThawedOocytesMicroinjectedNum());
                        }
                    }
                    if (atuList.contains(DataSubmissionConsts.AR_TECHNIQUES_USED_GIFT)){
                        if (StringUtil.isEmpty(fertilisationDto.getFreshOocytesGiftNum())){
                            errorMap.put("freshOocytesGiftNum","GENERAL_ERR0006");
                        }else if(!StringUtil.isEmpty(fertilisationDto.getFreshOocytesGiftNum())){
                            totalFreshSum+=Integer.valueOf(fertilisationDto.getFreshOocytesGiftNum());
                        }
                        if (StringUtil.isEmpty(fertilisationDto.getThawedOocytesGiftNum())){
                            errorMap.put("thawedOocytesGiftNum","GENERAL_ERR0006");
                        }else if(!StringUtil.isEmpty(fertilisationDto.getThawedOocytesGiftNum())){
                            totalThawedSum+=Integer.valueOf(fertilisationDto.getThawedOocytesGiftNum());
                        }
                    }
                    if (atuList.contains(DataSubmissionConsts.AR_TECHNIQUES_USED_ZIFT)){
                        if (StringUtil.isEmpty(fertilisationDto.getFreshOocytesZiftNum())){
                            errorMap.put("freshOocytesZiftNum","GENERAL_ERR0006");
                        }else if(!StringUtil.isEmpty(fertilisationDto.getFreshOocytesZiftNum())){
                            totalFreshSum+=Integer.valueOf(fertilisationDto.getFreshOocytesZiftNum());
                        }
                        if (StringUtil.isEmpty(fertilisationDto.getThawedOocytesZiftNum())){
                            errorMap.put("thawedOocytesZiftNum","GENERAL_ERR0006");
                        }else if(!StringUtil.isEmpty(fertilisationDto.getThawedOocytesZiftNum())){
                            totalThawedSum+=Integer.valueOf(fertilisationDto.getThawedOocytesZiftNum());
                        }
                    }

                }else {
                    errorMap.put("arTechniquesUsed","GENERAL_ERR0006");
                }
                if(IaisCommonUtils.isEmpty(sosList)){
                    errorMap.put("sourceOfSemen","GENERAL_ERR0006");
                }
        String errMsgFresh = "Total sum of data item 5, 6, 7 and 8 cannot be greater than number of fresh oocytes tagged to patien";
        String errMsgThawed = "Total sum of data item 9, 10, 11 and 12 cannot be greater than number of thawed oocytes tagged to patient";
        if(totalThawedSum>thawedMaxNum){
            errorMap.put("thawedOocytesZiftNum", errMsgThawed);
        }
        if(totalFreshSum>freshMaxNum){
            errorMap.put("freshOocytesZiftNum", errMsgFresh);
        }
        if (StringUtil.isNotEmpty(fertilisationDto.getExtractedSpermVialsNum()) ||StringUtil.isNotEmpty(fertilisationDto.getUsedSpermVialsNum())){
            Integer extractedSpermVialsNum = Integer.valueOf(fertilisationDto.getExtractedSpermVialsNum());
            Integer usedSpermVialsNum =Integer.valueOf(fertilisationDto.getUsedSpermVialsNum());

            if(usedSpermVialsNum != null && usedSpermVialsNum >= 0) {
                if(extractedSpermVialsNum != null) {
                    int FrozenSumNum = patientFrozen + extractedSpermVialsNum;
                    if(usedSpermVialsNum > FrozenSumNum) {
                        errorMap.put("usedSpermVialsNum", "Cannot be greater than 'How many vials of sperm were extracted?' + frozen sperm tagged to patient");
                    }
                }
            }
        }

        return  errorMap;
    }
}
