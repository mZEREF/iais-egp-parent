package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.IuiCycleStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Shicheng
 * @date 2021/11/12 15:42
 **/
public class IuiCycleStageDtoValidator implements CustomizeValidator {

    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        ArSuperDataSubmissionDto arSuperDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(request);
        if(arSuperDataSubmission != null) {
            IuiCycleStageDto iuiCycleStageDto = arSuperDataSubmission.getIuiCycleStageDto();
            if(iuiCycleStageDto != null) {
                //date
                Date iuiCycleStartDate = iuiCycleStageDto.getStartDate();
                if(iuiCycleStartDate != null) {
                    if(iuiCycleStartDate.after(new Date())) {
                        errMap.put("startDate", MessageUtil.replaceMessage("DS_ERR010", "Date Started", "field"));
                    }
                }
                //check box
                List<String> sourceOfSemenList = iuiCycleStageDto.getSemenSources();
                if(IaisCommonUtils.isEmpty(sourceOfSemenList)) {
                    errMap.put("semenSource", "GENERAL_ERR0006");
                }
                //number
                Integer curMarrChildNum = iuiCycleStageDto.getCurMarrChildNum();
                Integer prevMarrChildNum = iuiCycleStageDto.getPrevMarrChildNum();
                String iuiDeliverChildNum = iuiCycleStageDto.getIuiDeliverChildNum();
                String extractVialsOfSpermNum = iuiCycleStageDto.getExtractVialsOfSperm();
                String usedVialsOfSpermNum = iuiCycleStageDto.getUsedVialsOfSperm();
                errMap = validateNumberLength(errMap, curMarrChildNum, "No. of Children from Current Marriage", "curMarrChildNum", 2);
                errMap = validateNumberLength(errMap, prevMarrChildNum, "No. of Children from Previous Marriage", "prevMarrChildNum", 2);
                Integer iuiDeliverChild =  validateStringIsNumberAndValidateMaxlength(errMap,iuiDeliverChildNum,"No. of Children Delivered under IUI", "iuiDeliverChildNum", 2,false);
                Integer extractVialsOfSperm= validateStringIsNumberAndValidateMaxlength(errMap,extractVialsOfSpermNum, "How many vials of sperm were extracted", "extractVialsOfSperm", 2,true);
                Integer usedVialsOfSperm =validateStringIsNumberAndValidateMaxlength(errMap,usedVialsOfSpermNum, "How many vials of sperm were used in this cycle", "usedVialsOfSperm", 2,true);

                //Data association validation
                if(iuiDeliverChildNum != null) {
                    if((curMarrChildNum != null && curMarrChildNum >= 0) && (prevMarrChildNum != null && prevMarrChildNum >= 0)) {
                        int allChildren = curMarrChildNum + prevMarrChildNum;
                        if(iuiDeliverChild > allChildren) {
                            Map<String,String> stringStringMap = IaisCommonUtils.genNewHashMap(3);
                            stringStringMap.put("field1","");
                            stringStringMap.put("field2","No. of Children from Current Marriage");
                            stringStringMap.put("field3","No. of Children from Previous Marriage");
                            errMap.put("iuiDeliverChildNum", MessageUtil.getMessageDesc("DS_ERR011",stringStringMap).trim());
                        }
                    }
                }

                int patientFrozen = arSuperDataSubmission.getArCurrentInventoryDto() == null ? 0 : arSuperDataSubmission.getArCurrentInventoryDto().getFrozenSpermNum();
                int allFrozen = patientFrozen + extractVialsOfSperm;
                if(usedVialsOfSperm > allFrozen && usedVialsOfSperm < 100) {
                    Map<String,String> stringStringMap = IaisCommonUtils.genNewHashMap(3);
                    stringStringMap.put("field1","");
                    stringStringMap.put("field2","How many vials of sperm were extracted?");
                    stringStringMap.put("field3","frozen sperm tagged to patient");
                    errMap.put("usedVialsOfSperm", MessageUtil.getMessageDesc("DS_ERR011",stringStringMap).trim());
                }


                if(!iuiCycleStageDto.validateOtherPremises(iuiCycleStageDto.getOtherPremises())){
                    errMap.put("otherPremises", "GENERAL_ERR0006");
                }
            }
            DonorValidator.validateDonors(iuiCycleStageDto.getDonorDtos(),errMap,iuiCycleStageDto.isFromDonorFlag());
        }

        return errMap;
    }

    private Map<String, String> validateNumberLength(Map<String, String> errMap, Integer number, String fieldName, String msgName, int length) {
        if(number != null) {
            if(number < 0) {
                errMap.put(msgName, "GENERAL_ERR0027");
            }
            if(number.toString().length() > length) {
                errMap.put(msgName, MessageUtil.replaceMessage("DS_ERR009", fieldName, "field"));
            }
        }
        return errMap;
    }

    //if string is no number,return 0
    private Integer validateStringIsNumberAndValidateMaxlength(Map<String, String> errMap,String numberString,String fieldName, String msgName, int length,boolean needIsNumberMsg){
        if(StringUtil.isNumber(numberString)){
            Integer number = Integer.valueOf(numberString);
            validateNumberLength(errMap,  number, fieldName, msgName, length);
            return number;
        }else {
            if(needIsNumberMsg && StringUtil.isNotEmpty(numberString)){
                errMap.put(msgName,"GENERAL_ERR0002");
            }
            return 0;
        }
    }

}
