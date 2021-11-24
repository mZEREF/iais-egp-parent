package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.IuiCycleStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
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
                Integer iuiDeliverChildNum = iuiCycleStageDto.getIuiDeliverChildNum();
                Integer extractVialsOfSpermNum = iuiCycleStageDto.getExtractVialsOfSperm();
                Integer usedVialsOfSpermNum = iuiCycleStageDto.getUsedVialsOfSperm();

                errMap = validateNumberLength(errMap, curMarrChildNum, "No. of Children with Current Marriage", "curMarrChildNum", 2);
                errMap = validateNumberLength(errMap, prevMarrChildNum, "No. of Children with Previous Marriage", "prevMarrChildNum", 2);
                errMap = validateNumberLength(errMap, iuiDeliverChildNum, "Total No. of Children Delivered under IUI", "iuiDeliverChildNum", 2);
                errMap = validateNumberLength(errMap, extractVialsOfSpermNum, "How many vials of sperm were extracted", "extractVialsOfSperm", 2);
                errMap = validateNumberLength(errMap, usedVialsOfSpermNum, "How many vials of sperm were used in this cycle", "usedVialsOfSperm", 2);

                //Data association validation
                if(iuiDeliverChildNum != null) {
                    if((curMarrChildNum != null && curMarrChildNum >= 0) && (prevMarrChildNum != null && prevMarrChildNum >= 0)) {
                        int allChildren = curMarrChildNum + prevMarrChildNum;
                        if(0 <= iuiDeliverChildNum && iuiDeliverChildNum > allChildren) {
                            Map<String,String> stringStringMap = IaisCommonUtils.genNewHashMap(3);
                            stringStringMap.put("field1","");
                            stringStringMap.put("field2","No. of Children with Current Marriage");
                            stringStringMap.put("field3","No. of Children with Previous Marriage");
                            errMap.put("iuiDeliverChildNum", MessageUtil.getMessageDesc("DS_ERR011",stringStringMap).trim());
                        }
                    }
                }
                //todo get Patient Inventory
                int patientFrozen = 5;
                if(usedVialsOfSpermNum != null && usedVialsOfSpermNum >= 0) {
                    if(extractVialsOfSpermNum != null) {
                        int allFrozen = patientFrozen + extractVialsOfSpermNum;
                        if(usedVialsOfSpermNum > allFrozen) {
                            Map<String,String> stringStringMap = IaisCommonUtils.genNewHashMap(3);
                            stringStringMap.put("field1","");
                            stringStringMap.put("field2","Cannot be greater than 'How many vials of sperm were extracted?");
                            stringStringMap.put("field3","frozen sperm tagged to patient");
                            errMap.put("usedVialsOfSperm", MessageUtil.getMessageDesc("DS_ERR011",stringStringMap).trim());
                        }
                    }
                }
            }
            DonorValidator.validateDonors(iuiCycleStageDto.getDonorDtos(),errMap,iuiCycleStageDto.isFromDonorFlag());
        }
        if(errMap.isEmpty()) {
            return null;
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
}
