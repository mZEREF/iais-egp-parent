package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskWeightageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskWeightageShowDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: jiahao
 * @Date: 2019/12/26 15:41
 */
@Slf4j
public class HcsaWeightageRiskValidate implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        HcsaRiskWeightageShowDto wightageDto = (HcsaRiskWeightageShowDto) ParamUtil.getSessionAttr(request, "wightageDto");
        List<HcsaRiskWeightageDto> editList =  IaisCommonUtils.genNewArrayList();
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
        }else {
            errMap.put("All","RSM_ERR001");
        }
        WebValidationHelper.saveAuditTrailForNoUseResult(errMap);
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
            log.error(e.getMessage(), e);
        }
    }
    public void doSpecialDateFlag(Map<String, String> errMap,HcsaRiskWeightageDto fdto){
        try {
            Date inEffDate = Formatter.parseDate(fdto.getDoEffectiveDate());
            Date inEndDate = Formatter.parseDate(fdto.getDoEndDate());
            Date baseInEffDate = Formatter.parseDate(fdto.getBaseEffectiveDate());
            Date baseInEndDate = Formatter.parseDate(fdto.getBaseEndDate());
            if(inEffDate.getTime()<baseInEffDate.getTime()){
                errMap.put(fdto.getServiceCode() + "inEffDate", "RSM_ERR018");
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }
    public boolean doUsualDateVad(Date effDate,Date endDate,String serviceCode,Map<String, String> errMap,int inEdit,int prEdit){
        boolean flag = true;
        if (effDate.getTime() < System.currentTimeMillis()) {
            flag = false;
            if (inEdit == 1) {
                errMap.put(serviceCode + "inEffDate", MessageUtil.replaceMessage("RSM_ERR012","Effective Start Date","Effective Date"));
            }
        } else if (endDate.getTime() < effDate.getTime()) {
            flag = false;
            if (inEdit == 1) {
                errMap.put(serviceCode + "inEndDate", "RSM_ERR017");
            }
        }
        return flag;
    }
    public boolean doDateFormatVad(Map<String, String> errMap,String strEffDate,String strEndDate,String serviceCode){
        boolean vadFlag = true;
        if(StringUtil.isEmpty(strEffDate)){
            vadFlag = false;
            errMap.put(serviceCode+"inEffDate",MessageUtil.replaceMessage("GENERAL_ERR0006","Effective Start Date","field"));
        }else{
            try {
                Formatter.parseDate(strEffDate);
            }catch (Exception e){
                errMap.put(serviceCode+"inEffDate",MessageUtil.replaceMessage("RSM_ERR016","Effective Start Date","replaceArea"));
                vadFlag = false;
            }
        }
        if(StringUtil.isEmpty(strEndDate)){
            vadFlag = false;
            errMap.put(serviceCode+"inEndDate",MessageUtil.replaceMessage("GENERAL_ERR0006","Effective End Date","field"));
        }else{
            try {
                Formatter.parseDate(strEndDate);
            }catch (Exception e){
                vadFlag = false;
                errMap.put(serviceCode+"inEndDate",MessageUtil.replaceMessage("RSM_ERR016","Effective End Date","replaceArea"));
            }
        }
        return vadFlag;
    }
    private void doWeightageVad(HcsaRiskWeightageDto temp, Map<String, String> errMap) {
        boolean isnullFlag = notEmptyVad(temp,errMap);
        boolean lasInpNumFlag = formatVad(temp,errMap,temp.getDoLastInp(),"lastInp",MessageUtil.getMessageDesc("GENERAL_ERR0027") + "for Last Inspection");
        boolean lasSecInpNumFlag = formatVad(temp,errMap,temp.getDoSecLastInp(),"secLastInp",MessageUtil.getMessageDesc("GENERAL_ERR0027") + "for Second Last Inspection");
        boolean lasFinNumFlag = formatVad(temp,errMap,temp.getDoFinancial(),"fin",MessageUtil.getMessageDesc("GENERAL_ERR0027") + "for Financial Scheme Audit");
        boolean lasLeaNumFlag = formatVad(temp,errMap,temp.getDoLeadship(),"lea",MessageUtil.getMessageDesc("GENERAL_ERR0027") + "for Leadership And Governance");
        boolean lasLegNumFlag = formatVad(temp,errMap,temp.getDoLegislative(),"leg",MessageUtil.getMessageDesc("GENERAL_ERR0027") + "for Legislative Breaches");
        if(isnullFlag&&lasInpNumFlag&&lasSecInpNumFlag&&lasFinNumFlag&&lasLeaNumFlag&&lasLegNumFlag){
            calwWeightageVad(temp,errMap);
        }
    }

    private boolean formatVad(HcsaRiskWeightageDto temp, Map<String, String> errMap,String numStr,String mapkey,String mapv) {
        try {
            if(!StringUtil.isEmpty(numStr)){
                if(!StringUtil.stringIsFewDecimal(numStr,2)){
                    errMap.put(temp.getServiceCode()+mapkey,mapv);
                    return false;
                }
                Double num = Double.valueOf(numStr);
                if(num<=0||num>=1){
                    errMap.put(temp.getServiceCode()+mapkey,mapv);
                }else{
                    return true;
                }
            }
        }catch (Exception e){
            errMap.put(temp.getServiceCode()+mapkey,mapv);
            log.error(e.getMessage(), e);
            return false;
        }
        return false;
    }

    private void calwWeightageVad(HcsaRiskWeightageDto temp, Map<String, String> errMap) {
        Double lastInp = Double.valueOf(temp.getDoLastInp());
        Double seclastInp = Double.valueOf(temp.getDoSecLastInp());
        Double finInp = Double.valueOf(temp.getDoFinancial());
        Double leaInp = Double.valueOf(temp.getDoLeadship());
        Double legInp = Double.valueOf(temp.getDoLegislative());
        BigDecimal lastBig = new BigDecimal(lastInp.toString());
        BigDecimal seclastInpBig = new BigDecimal(seclastInp.toString());
        BigDecimal finInpBig = new BigDecimal(finInp.toString());
        BigDecimal leaInpBig = new BigDecimal(leaInp.toString());
        BigDecimal legInpBig = new BigDecimal(legInp.toString());
        BigDecimal result = lastBig.add(seclastInpBig).add(finInpBig).add(leaInpBig).add(legInpBig);
        if(result.doubleValue()!=1.0D){
            errMap.put(temp.getServiceCode()+"totalw", "The sum of weights for service should be 1.");
        }
    }


    private boolean notEmptyVad(HcsaRiskWeightageDto temp, Map<String, String> errMap) {
        int notEmptyNum = 0;

        if(StringUtil.isEmpty(temp.getDoLastInp())){
            errMap.put(temp.getServiceCode()+"lastInp",MessageUtil.replaceMessage("GENERAL_ERR0006","Last Inspection","field"));
            notEmptyNum++;
        }
        if(StringUtil.isEmpty(temp.getDoSecLastInp())){
            errMap.put(temp.getServiceCode()+"secLastInp",MessageUtil.replaceMessage("GENERAL_ERR0006","Second Last Inspection","field"));
            notEmptyNum++;
        }
        if(StringUtil.isEmpty(temp.getDoFinancial())){
            errMap.put(temp.getServiceCode()+"fin",MessageUtil.replaceMessage("GENERAL_ERR0006","Financial Scheme Audit","field"));
            notEmptyNum++;
        }
        if(StringUtil.isEmpty(temp.getDoLeadship())){
            errMap.put(temp.getServiceCode()+"lea",MessageUtil.replaceMessage("GENERAL_ERR0006","Leadership And Governance","field"));
            notEmptyNum++;
        }
        if(StringUtil.isEmpty(temp.getDoLegislative())){
            errMap.put(temp.getServiceCode()+"leg",MessageUtil.replaceMessage("GENERAL_ERR0006","Legislative Breaches","field"));
            notEmptyNum++;
        }
        if(StringUtil.isEmpty(temp.getDoEffectiveDate())){
            notEmptyNum++;
            errMap.put(temp.getServiceCode()+"inEffDate",MessageUtil.replaceMessage("GENERAL_ERR0006","Effective Start Date","field"));
        }
        if(StringUtil.isEmpty(temp.getDoEndDate())){
            notEmptyNum++;
            errMap.put(temp.getServiceCode()+"inEndDate",MessageUtil.replaceMessage("GENERAL_ERR0006","Effective End Date","field"));
        }
        if(notEmptyNum==0){
            return true;
        }else{
            return false;
        }
    }
}
