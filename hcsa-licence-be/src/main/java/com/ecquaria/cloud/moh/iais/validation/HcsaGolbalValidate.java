package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.GobalRiskTotalDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.GolbalRiskShowDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: jiahao
 * @Date: 2019/12/30 16:28
 */
@Slf4j
public class HcsaGolbalValidate implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        GolbalRiskShowDto golbalShowDto = (GolbalRiskShowDto) ParamUtil.getSessionAttr(request, "golbalShowDto");
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        List<GobalRiskTotalDto> tolList =  golbalShowDto.getGoalbalTotalList();
        List<GobalRiskTotalDto> updatetolList =  IaisCommonUtils.genNewArrayList();
        for(GobalRiskTotalDto temp:tolList){
            if(temp.isEdit()){
                updatetolList.add(temp);
            }
        }
        if(updatetolList!=null && !updatetolList.isEmpty()){
            for(GobalRiskTotalDto temp:updatetolList){
                doComVad(temp,errMap);
                dateVad(errMap, temp);
            }
        }else{
            errMap.put("All","please do some change");
        }
        WebValidationHelper.saveAuditTrailForNoUseResult(errMap);
        return errMap;
    }
    public void dateVad(Map<String, String> errMap, GobalRiskTotalDto fdto){
        String inEffDate = fdto.getDoEffectiveDate();
        String inEndDate = fdto.getDoEndDate();
        boolean inDateFormatVad = doDateFormatVad(errMap,inEffDate,inEndDate,fdto.getServiceCode());
        if(inDateFormatVad&&fdto.isEdit()){
            doDateLogicVad(errMap,fdto,true);
        }
    }

    public void doDateLogicVad(Map<String, String> errMap, GobalRiskTotalDto fdto, boolean isIn){
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
    public void doSpecialDateFlag(Map<String, String> errMap,GobalRiskTotalDto fdto){
        try {
            Date inEffDate = Formatter.parseDate(fdto.getDoEffectiveDate());
            Date baseInEffDate = Formatter.parseDate(fdto.getBaseEffectiveDate());
            if(inEffDate.getTime()<baseInEffDate.getTime()){
                errMap.put(fdto.getServiceCode() + "inEffDate", "Effective Date should later than Previous version");
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
                errMap.put(serviceCode + "inEffDate", MessageUtil.replaceMessage("ERR0012","Effective Start Date","Effective Date"));
            }
        } else if (endDate.getTime() < effDate.getTime()) {
            flag = false;
            if (inEdit == 1) {
                errMap.put(serviceCode + "inEndDate", "ERR0016");
            }
        }
        return flag;
    }
    public boolean doDateFormatVad(Map<String, String> errMap,String strEffDate,String strEndDate,String serviceCode){
        boolean vadFlag = true;
        if(StringUtil.isEmpty(strEffDate)){
            vadFlag = false;
            errMap.put(serviceCode+"inEffDate",MessageUtil.replaceMessage("ERR0009","Effective Start Date","The field"));
        }else{
            try {
                Formatter.parseDate(strEffDate);
            }catch (Exception e){
                errMap.put(serviceCode+"inEffDate",MessageUtil.replaceMessage("ERR0017","Effective Start Date","replaceArea"));
                vadFlag = false;
            }
        }
        if(StringUtil.isEmpty(strEndDate)){
            vadFlag = false;
            errMap.put(serviceCode+"inEndDate",MessageUtil.replaceMessage("ERR0009","Effective End Date","The field"));
        }else{
            try {
                Formatter.parseDate(strEndDate);
            }catch (Exception e){
                errMap.put(serviceCode+"inEndDate",MessageUtil.replaceMessage("ERR0017","Effective End Date","replaceArea"));
                return false;
            }
        }
        return vadFlag;
    }

    private void doComVad(GobalRiskTotalDto temp, Map<String, String> errMap) {
        if(StringUtil.isEmpty(temp.getDoMaxLic())){
            errMap.put(temp.getServiceCode()+"maxl",MessageUtil.replaceMessage("ERR0009","Maximum Licence Tenure","The field"));
        }else{
            numberVad(temp.getDoMaxLic(),errMap,temp.getServiceCode(),"maxl");
        }
        if(StringUtil.isEmpty(temp.getDoLastInspection())){
            errMap.put(temp.getServiceCode()+"last",MessageUtil.replaceMessage("ERR0009","Last Inspection was more than","The field"));
        }else{
            numberVad(temp.getDoLastInspection(),errMap,temp.getServiceCode(),"last");
        }
        if(StringUtil.isEmpty(temp.getDoAutoRenew())){
            errMap.put(temp.getServiceCode()+"auto",MessageUtil.replaceMessage("ERR0009","Eligible for auto renewal","The field"));
        }
        if(StringUtil.isEmpty(temp.getDonewInspectType())){
            errMap.put(temp.getServiceCode()+"newit",MessageUtil.replaceMessage("ERR0009","Type of Inspection Required for New Application","The field"));
        }
        if(StringUtil.isEmpty(temp.getDonewIsPreInspect())){
            errMap.put(temp.getServiceCode()+"newpp",MessageUtil.replaceMessage("ERR0009","Pre/Post Licensing Inspection for New Application","The field"));
        }
        if(StringUtil.isEmpty(temp.getDorenewInspectType())){
            errMap.put(temp.getServiceCode()+"renewit",MessageUtil.replaceMessage("ERR0009","Type of Inspection Required for Renewal","The field"));
        }
        if(StringUtil.isEmpty(temp.getDorenewIsPreInspect())){
            errMap.put(temp.getServiceCode()+"renewpp",MessageUtil.replaceMessage("ERR0009","Pre/Post Licensing Inspection for Renewal","The field"));
        }
    }

    private void numberVad(String strNum, Map<String, String> errMap, String serviceCode,String key) {
        try {
            int num = Integer.parseInt(strNum);
            if(num<0||num>99){
                errMap.put(serviceCode+key,"ERR0013");
            }
        }catch (Exception e){
            errMap.put(serviceCode+key,"ERR0013");
        }

    }

}
