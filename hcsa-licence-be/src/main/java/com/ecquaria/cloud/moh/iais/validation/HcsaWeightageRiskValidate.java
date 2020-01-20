package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskWeightageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskWeightageShowDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
        List<HcsaRiskWeightageDto> editList =  new ArrayList<>();
        List<HcsaRiskWeightageDto> wDtoList =  wightageDto.getWeightageDtoList();
        for(HcsaRiskWeightageDto temp:wDtoList){
            if(temp.isEdit()){
                editList.add(temp);
            }
        }
        if(editList!=null && !editList.isEmpty()){
            for(HcsaRiskWeightageDto temp:editList){
                doWeightageVad(temp,errMap);
                dateVad(errMap, temp);
            }
        }
        return errMap;
    }
    public void dateVad(Map<String, String> errMap, HcsaRiskWeightageDto fdto){
        String inEffDate = fdto.getDoEffectiveDate();
        String inEndDate = fdto.getDoEndDate();
        boolean inDateFormatVad = doDateFormatVad(errMap,inEffDate,inEndDate,fdto.getServiceCode());
        if(inDateFormatVad&&fdto.isEdit()){
            doDateLogicVad(errMap,fdto,true);
        }
    }

    public void doDateLogicVad(Map<String, String> errMap, HcsaRiskWeightageDto fdto, boolean isIn){
        //effdate least version <
        try {
            Date inEffDate = Formatter.parseDate(fdto.getDoEffectiveDate());
            Date inEndDate = Formatter.parseDate(fdto.getDoEndDate());
            int inEditNumFlag = 0;
            int prEditNumFlag = 0;
            if(fdto.isEdit()){
                inEditNumFlag = 1;
            }
            if (StringUtil.isEmpty(fdto.getId())) {
                doUsualDateVad(inEffDate,inEndDate,fdto.getServiceCode(),errMap,inEditNumFlag,prEditNumFlag);
            } else {
                boolean inDateFlag;
                inDateFlag = doUsualDateVad(inEffDate,inEndDate,fdto.getServiceCode(),errMap,inEditNumFlag,prEditNumFlag);
                if(inDateFlag){
                    doSpecialDateFlag(errMap,fdto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void doSpecialDateFlag(Map<String, String> errMap,HcsaRiskWeightageDto fdto){
        try {
            Date inEffDate = Formatter.parseDate(fdto.getDoEffectiveDate());
            Date inEndDate = Formatter.parseDate(fdto.getDoEndDate());
            Date baseInEffDate = Formatter.parseDate(fdto.getBaseEffectiveDate());
            Date baseInEndDate = Formatter.parseDate(fdto.getBaseEndDate());
            if(inEffDate.getTime()<baseInEffDate.getTime()){
                errMap.put(fdto.getServiceCode() + "inEffDate", "EffectiveDate should later than Previous version");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public boolean doUsualDateVad(Date effDate,Date endDate,String serviceCode,Map<String, String> errMap,int inEdit,int prEdit){
        boolean flag = true;
        if (effDate.getTime() < System.currentTimeMillis()) {
            flag = false;
            if (inEdit == 1) {
                errMap.put(serviceCode + "inEffDate", "EffectiveDate should be furture time");
            }
        } else if (endDate.getTime() < effDate.getTime()) {
            flag = false;
            if (inEdit == 1) {
                errMap.put(serviceCode + "inEndDate", "EffectiveDate should be ealier than EndDate");
            }
        }
        return flag;
    }
    public boolean doDateFormatVad(Map<String, String> errMap,String strEffDate,String strEndDate,String serviceCode){
        boolean vadFlag = true;
        if(StringUtil.isEmpty(strEffDate)){
            vadFlag = false;
            errMap.put(serviceCode+"inEffDate","EffectiveDate is mandatory");
        }else{
            try {
                Formatter.parseDate(strEffDate);
            }catch (Exception e){
                errMap.put(serviceCode+"inEffDate","Date Format Error");
                vadFlag = false;
            }
        }
        if(StringUtil.isEmpty(strEndDate)){
            vadFlag = false;
            errMap.put(serviceCode+"inEndDate","EndDate is mandatory");
        }else{
            try {
                Formatter.parseDate(strEndDate);
            }catch (Exception e){
                vadFlag = false;
                errMap.put(serviceCode+"inEndDate","Date Format Error");
                return false;
            }
        }
        return vadFlag;
    }
    private void doWeightageVad(HcsaRiskWeightageDto temp, Map<String, String> errMap) {
        boolean isnullFlag = notEmptyVad(temp,errMap);
        boolean lasInpNumFlag = formatVad(temp,errMap,temp.getDoLastInp(),"lastInp","Invalid Number");
        boolean lasSecInpNumFlag = formatVad(temp,errMap,temp.getDoSecLastInp(),"secLastInp","Invalid Number");
        boolean lasFinNumFlag = formatVad(temp,errMap,temp.getDoFinancial(),"fin","Invalid Number");
        boolean lasLeaNumFlag = formatVad(temp,errMap,temp.getDoLeadship(),"lea","Invalid Number");
        boolean lasLegNumFlag = formatVad(temp,errMap,temp.getDoLegislative(),"leg","Invalid Number");
        if(isnullFlag&&lasInpNumFlag&&lasSecInpNumFlag&&lasFinNumFlag&&lasLeaNumFlag&&lasLegNumFlag){
            calwWeightageVad(temp,errMap);
        }
    }

    private boolean formatVad(HcsaRiskWeightageDto temp, Map<String, String> errMap,String numStr,String mapkey,String mapv) {
        try {
            if(!StringUtil.isEmpty(numStr)){
                Double num = Double.parseDouble(numStr);
                if(num<=0||num>=1){
                    errMap.put(temp.getServiceCode()+mapkey,mapv);
                }else{
                    return true;
                }
            }
        }catch (Exception e){
            errMap.put(temp.getServiceCode()+mapkey,mapv);
            e.printStackTrace();
            return false;
        }
        return false;
    }

    private void calwWeightageVad(HcsaRiskWeightageDto temp, Map<String, String> errMap) {
        Double lastInp = Double.parseDouble(temp.getDoLastInp());
        Double seclastInp = Double.parseDouble(temp.getDoSecLastInp());
        Double finInp = Double.parseDouble(temp.getDoFinancial());
        Double leaInp = Double.parseDouble(temp.getDoLeadship());
        Double legInp = Double.parseDouble(temp.getDoLegislative());
        BigDecimal lastBig = new BigDecimal(lastInp.toString());
        BigDecimal seclastInpBig = new BigDecimal(seclastInp.toString());
        BigDecimal finInpBig = new BigDecimal(finInp.toString());
        BigDecimal leaInpBig = new BigDecimal(leaInp.toString());
        BigDecimal legInpBig = new BigDecimal(legInp.toString());
        BigDecimal result = lastBig.add(seclastInpBig).add(finInpBig).add(leaInpBig).add(legInpBig).add(lastBig);
        if(result.doubleValue()!=1.0D){
            errMap.put(temp.getServiceCode()+"totalw", "The total weight should be 1");
        }
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
            errMap.put(temp.getServiceCode()+"inEffDate","ERR0009");
        }
        if(StringUtil.isEmpty(temp.getDoEndDate())){
            notEmptyNum++;
            errMap.put(temp.getServiceCode()+"inEndDate","ERR0009");
        }
        if(notEmptyNum==0){
            return true;
        }else{
            return false;
        }
    }
}
