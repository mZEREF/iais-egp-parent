package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskWeightageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskWeightageShowDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: jiahao
 * @Date: 2019/12/26 15:41
 */
public class HcsaWeightageRiskValidate implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errMap = new HashMap<>();
        HcsaRiskWeightageShowDto wightageDto = (HcsaRiskWeightageShowDto) ParamUtil.getSessionAttr(request, "wightageDto");
        List<HcsaRiskWeightageDto> editList =  wightageDto.getWeightageDtoList();
        List<HcsaRiskWeightageDto> wDtoList =  wightageDto.getWeightageDtoList();
        for(HcsaRiskWeightageDto temp:wDtoList){
            if(temp.isEdit()){
                editList.add(temp);
            }
        }
        if(editList!=null && !editList.isEmpty()){
            for(HcsaRiskWeightageDto temp:editList){
                doWeightageVad(temp,errMap);
            }
        }
        return null;
    }

    private void doWeightageVad(HcsaRiskWeightageDto temp, Map<String, String> errMap) {
        boolean isnullFlag = notEmptyVad(temp,errMap);
        boolean numberForamatterFlag = formatVad(temp,errMap);
        if(isnullFlag){
            calwWeightageVad(temp,errMap);
        }
    }

    private boolean formatVad(HcsaRiskWeightageDto temp, Map<String, String> errMap) {
        try {
            if(!StringUtil.isEmpty(temp.getDoLastInp())){
                Integer.parseInt(temp.getDoLastInp());
            }
        }catch (Exception e){
            errMap.put(temp.getServiceCode()+"lastInp","Invalid Number");
            e.printStackTrace();
        }
        return false;
    }

    private void calwWeightageVad(HcsaRiskWeightageDto temp, Map<String, String> errMap) {
        
    }


    private boolean notEmptyVad(HcsaRiskWeightageDto temp, Map<String, String> errMap) {
        int notEmptyNum = 0;

        if(StringUtil.isEmpty(temp.getDoLastInp())){
            errMap.put(temp.getServiceCode()+"lastInp","ERR0009");
            notEmptyNum++;
        }
        if(StringUtil.isEmpty(temp.getDoSecLastInp())){
            errMap.put(temp.getServiceCode()+"secLastInp","ERR0009");
            notEmptyNum++;
        }
        if(StringUtil.isEmpty(temp.getDoFinancial())){
            errMap.put(temp.getServiceCode()+"fin","ERR0009");
            notEmptyNum++;
        }
        if(StringUtil.isEmpty(temp.getDoLeadship())){
            errMap.put(temp.getServiceCode()+"lea","ERR0009");
            notEmptyNum++;
        }
        if(StringUtil.isEmpty(temp.getDoLegislative())){
            errMap.put(temp.getServiceCode()+"leg","ERR0009");
            notEmptyNum++;
        }
        if(StringUtil.isEmpty(temp.getDoEffectiveDate())){
            notEmptyNum++;
            errMap.put(temp.getServiceCode()+"effDate","ERR0009");
        }
        if(StringUtil.isEmpty(temp.getDoEndDate())){
            notEmptyNum++;
            errMap.put(temp.getServiceCode()+"endDate","ERR0009");
        }
        if(notEmptyNum==0){
            return true;
        }else{
            return false;
        }
    }
}
