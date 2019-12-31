package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.GobalRiskTotalDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.GolbalRiskShowDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: jiahao
 * @Date: 2019/12/30 16:28
 */
public class HcsaGolbalValidate implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        GolbalRiskShowDto golbalShowDto = (GolbalRiskShowDto) ParamUtil.getSessionAttr(request, "golbalShowDto");
        Map<String, String> errMap = new HashMap<>();
        List<GobalRiskTotalDto> tolList =  golbalShowDto.getGoalbalTotalList();
        List<GobalRiskTotalDto> updatetolList =  new ArrayList<>();
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
        }
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
            e.printStackTrace();
        }
    }
    public void doSpecialDateFlag(Map<String, String> errMap,GobalRiskTotalDto fdto){
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

    private void doComVad(GobalRiskTotalDto temp, Map<String, String> errMap) {
        if(StringUtil.isEmpty(temp.getDoMaxLic())){
            errMap.put(temp.getServiceCode()+"maxl","ERR0009");
        }else{
            numberVad(temp.getDoMaxLic(),errMap,temp.getServiceCode(),"maxl");
        }
        if(StringUtil.isEmpty(temp.getDoLastInspection())){
            errMap.put(temp.getServiceCode()+"last","ERR0009");
        }else{
            numberVad(temp.getDoLastInspection(),errMap,temp.getServiceCode(),"last");
        }
        if(StringUtil.isEmpty(temp.getDoAutoRenew())){
            errMap.put(temp.getServiceCode()+"auto","ERR0009");
        }
        if(StringUtil.isEmpty(temp.getDonewInspectType())){
            errMap.put(temp.getServiceCode()+"newit","ERR0009");
        }
        if(StringUtil.isEmpty(temp.getDonewIsPreInspect())){
            errMap.put(temp.getServiceCode()+"newpp","ERR0009");
        }
        if(StringUtil.isEmpty(temp.getDorenewInspectType())){
            errMap.put(temp.getServiceCode()+"renewit","ERR0009");
        }
        if(StringUtil.isEmpty(temp.getDorenewIsPreInspect())){
            errMap.put(temp.getServiceCode()+"renewpp","ERR0009");
        }
    }

    private void numberVad(String strNum, Map<String, String> errMap, String serviceCode,String key) {
        try {
            int num = Integer.parseInt(strNum);
            if(num<0||num>99){
                errMap.put(serviceCode+key,"Invalid Number");
            }
        }catch (Exception e){
            errMap.put(serviceCode+key,"Invalid Number");
        }

    }

}
