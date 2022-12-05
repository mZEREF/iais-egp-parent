package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArCurrentInventoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.FertilisationDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
        FertilisationDto fertilisationDto = arSuperDataSubmissionDto.getFertilisationDto() == null ? new FertilisationDto() : arSuperDataSubmissionDto.getFertilisationDto();
        List<String> atuList = fertilisationDto.getAtuList();
        List<String> sosList = fertilisationDto.getSosList();
        String sourceOfOocyte = fertilisationDto.getSourceOfOocyte();
        String oocyteUsed = fertilisationDto.getOocyteUsed();
        String spermUsed = fertilisationDto.getSpermUsed();
        String usedOocytes = fertilisationDto.getUsedOocytesNum();
        int patientFrozen = 100;
        int thawedMaxNum = 100;
        int freshMaxNum = 100;
        ArCurrentInventoryDto arCurrentInventoryDto = arSuperDataSubmissionDto.getArCurrentInventoryDto();
        if (arCurrentInventoryDto != null) {
            thawedMaxNum = arCurrentInventoryDto.getThawedOocyteNum();
            freshMaxNum = arCurrentInventoryDto.getFreshOocyteNum();
            patientFrozen = arCurrentInventoryDto.getFrozenSpermNum();
        }
        int totalThawedSum = 0;
        int totalFreshSum = 0;
                if(IaisCommonUtils.isNotEmpty(atuList)){
                    if (atuList.contains(DataSubmissionConsts.AR_TECHNIQUES_USED_IVF)){
                        if (StringUtil.isEmpty(fertilisationDto.getFreshOocytesInseminatedNum())){
                            errorMap.put("freshOocytesInseminatedNum","GENERAL_ERR0006");
                        }else if(!StringUtil.isEmpty(fertilisationDto.getFreshOocytesInseminatedNum())&& StringUtil.isNumber(fertilisationDto.getFreshOocytesInseminatedNum())){
                            totalFreshSum+=Integer.parseInt(fertilisationDto.getFreshOocytesInseminatedNum());
                        }
                        if (StringUtil.isEmpty(fertilisationDto.getThawedOocytesInseminatedNum())){
                            errorMap.put("thawedOocytesInseminatedNum","GENERAL_ERR0006");
                        }else if(!StringUtil.isEmpty(fertilisationDto.getThawedOocytesInseminatedNum())&&StringUtil.isNumber(fertilisationDto.getThawedOocytesInseminatedNum())){
                            totalThawedSum+=Integer.parseInt(fertilisationDto.getThawedOocytesInseminatedNum());
                        }
                    }
                    if (atuList.contains(DataSubmissionConsts.AR_TECHNIQUES_USED_ICSI)){
                        if (StringUtil.isEmpty(fertilisationDto.getFreshOocytesMicroInjectedNum())){
                            errorMap.put("freshOocytesMicroInjectedNum","GENERAL_ERR0006");
                        }else if(!StringUtil.isEmpty(fertilisationDto.getFreshOocytesMicroInjectedNum())&& StringUtil.isNumber(fertilisationDto.getFreshOocytesMicroInjectedNum())){
                            totalFreshSum+=Integer.parseInt(fertilisationDto.getFreshOocytesMicroInjectedNum());
                        }
                        if (StringUtil.isEmpty(fertilisationDto.getThawedOocytesMicroinjectedNum())){
                            errorMap.put("thawedOocytesMicroinjectedNum","GENERAL_ERR0006");
                        }else if(!StringUtil.isEmpty(fertilisationDto.getThawedOocytesMicroinjectedNum())&&StringUtil.isNumber(fertilisationDto.getThawedOocytesMicroinjectedNum())){
                            totalThawedSum+=Integer.parseInt(fertilisationDto.getThawedOocytesMicroinjectedNum());
                        }
                    }
                    if (atuList.contains(DataSubmissionConsts.AR_TECHNIQUES_USED_GIFT)){
                        if (StringUtil.isEmpty(fertilisationDto.getFreshOocytesGiftNum())){
                            errorMap.put("freshOocytesGiftNum","GENERAL_ERR0006");
                        }else if(!StringUtil.isEmpty(fertilisationDto.getFreshOocytesGiftNum()) && StringUtil.isNumber(fertilisationDto.getFreshOocytesGiftNum())){
                            totalFreshSum+=Integer.parseInt(fertilisationDto.getFreshOocytesGiftNum());
                        }
                        if (StringUtil.isEmpty(fertilisationDto.getThawedOocytesGiftNum())){
                            errorMap.put("thawedOocytesGiftNum","GENERAL_ERR0006");
                        }else if(!StringUtil.isEmpty(fertilisationDto.getThawedOocytesGiftNum()) && StringUtil.isNumber(fertilisationDto.getThawedOocytesGiftNum())){
                            totalThawedSum+=Integer.parseInt(fertilisationDto.getThawedOocytesGiftNum());
                        }
                    }
                    if (atuList.contains(DataSubmissionConsts.AR_TECHNIQUES_USED_ZIFT)){
                        if (StringUtil.isEmpty(fertilisationDto.getFreshOocytesZiftNum())){
                            errorMap.put("freshOocytesZiftNum","GENERAL_ERR0006");
                        }else if(!StringUtil.isEmpty(fertilisationDto.getFreshOocytesZiftNum())&&StringUtil.isNumber(fertilisationDto.getFreshOocytesZiftNum())){
                            totalFreshSum+=Integer.parseInt(fertilisationDto.getFreshOocytesZiftNum());
                        }
                        if (StringUtil.isEmpty(fertilisationDto.getThawedOocytesZiftNum())){
                            errorMap.put("thawedOocytesZiftNum","GENERAL_ERR0006");
                        }else if(!StringUtil.isEmpty(fertilisationDto.getThawedOocytesZiftNum())&&StringUtil.isNumber(fertilisationDto.getThawedOocytesZiftNum())){
                            totalThawedSum+=Integer.parseInt(fertilisationDto.getThawedOocytesZiftNum());
                        }
                    }

                }else {
                    errorMap.put("arTechniquesUsed","GENERAL_ERR0006");
                }
                if(IaisCommonUtils.isEmpty(sosList)){
                    errorMap.put("sourceOfSemen","GENERAL_ERR0006");
                }
                if (StringUtil.isEmpty(sourceOfOocyte)) {
                    errorMap.put("sourceOfOocyteOp","GENERAL_ERR0006");
                }
                if (StringUtil.isEmpty(oocyteUsed)) {
                    errorMap.put("oocyteUsedOp", "GENERAL_ERR0006");
                }
                if (StringUtil.isEmpty(spermUsed)) {
                    errorMap.put("spermUsedOp","GENERAL_ERR0006");
                }
        if(totalThawedSum>thawedMaxNum){
            Map<String, String> errMsgThawed =IaisCommonUtils.genNewHashMap();
            errMsgThawed.put("item","No. of Thawed Oocytes Inseminated, No. of Thawed Oocytes Microinjected, No. of Thawed Oocytes Used for GIFT and No. of Thawed Oocytes Used for ZIFT");
            errMsgThawed.put("inventory","thawed oocytes");
            String errMsg = MessageUtil.getMessageDesc("DS_ERR060",errMsgThawed);
            errorMap.put("thawedOocytesZiftNum", errMsg);

        }
        if(totalFreshSum>freshMaxNum){
            Map<String, String> errMsgFresh =IaisCommonUtils.genNewHashMap();
            errMsgFresh.put("item","No. of Fresh Oocytes Inseminated, No. of Fresh Oocytes Microinjected, No. of Fresh Oocytes Used for GIFT and No. of Fresh Oocytes Used for ZIFT");
            errMsgFresh.put("inventory","fresh oocytes");
            String errMsg = MessageUtil.getMessageDesc("DS_ERR060",errMsgFresh);
            errorMap.put("freshOocytesZiftNum", errMsg);
        }
        if (StringUtil.isNotEmpty(fertilisationDto.getExtractedSpermVialsNum()) &&StringUtil.isNotEmpty(fertilisationDto.getUsedSpermVialsNum())){
            if(StringUtil.isNumber(fertilisationDto.getExtractedSpermVialsNum())&&StringUtil.isNumber(fertilisationDto.getUsedSpermVialsNum())){
                Integer extractedSpermVialsNum = Integer.valueOf(fertilisationDto.getExtractedSpermVialsNum());
                Integer usedSpermVialsNum =Integer.valueOf(fertilisationDto.getUsedSpermVialsNum());
                if(usedSpermVialsNum != null && usedSpermVialsNum >= 0) {
                    if(extractedSpermVialsNum != null) {
                        int FrozenSumNum = patientFrozen + extractedSpermVialsNum;
                        if(usedSpermVialsNum > FrozenSumNum) {
                            Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                            repMap.put("field1","How many vials of sperm were used in this cycle?");
                            repMap.put("field2","'How many vials of sperm were extracted?'");
                            repMap.put("field3","frozen sperm tagged to patient");
                            String errMsg = MessageUtil.getMessageDesc("DS_ERR011",repMap);
                            errorMap.put("usedSpermVialsNum", errMsg);
                        }
                    }
                }
            }
        }

        if(StringUtil.isNotEmpty(usedOocytes) && StringUtil.isNumber(usedOocytes) && StringUtil.isNotEmpty(oocyteUsed)) {
            int usedOocytesNum = Integer.parseInt(usedOocytes);
            if ((oocyteUsed.equals("Fresh") && (usedOocytesNum != totalFreshSum)) || (oocyteUsed.equals("Frozen") && (usedOocytesNum != totalThawedSum)) || (usedOocytesNum != totalFreshSum + totalThawedSum)) {
                errorMap.put("usedOocytesNum", "Please check the number of oocytes");
            }
        }

        return  errorMap;
    }
}
