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
            errMap.put("All","RSM_ERR001");
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
                errMap.put(serviceCode+"inEndDate",MessageUtil.replaceMessage("RSM_ERR016","Effective End Date","replaceArea"));
                return false;
            }
        }
        return vadFlag;
    }

    private void doComVad(GobalRiskTotalDto temp, Map<String, String> errMap) {
        String serviceCode = temp.getSvcCode();
        if(StringUtil.isEmpty(temp.getDoMaxLic())){
            errMap.put(serviceCode+"maxl",MessageUtil.replaceMessage("GENERAL_ERR0006","Maximum Licence Tenure","field"));
        }else{
            numberVad(temp.getDoMaxLic(),errMap,serviceCode,"maxl");
        }
        if(StringUtil.isEmpty(temp.getDoLastInspection())){
            errMap.put(serviceCode+"last",MessageUtil.replaceMessage("GENERAL_ERR0006","Last Inspection was more than","field"));
        }else{
            numberVad(temp.getDoLastInspection(),errMap,serviceCode,"last");
        }
        if(StringUtil.isEmpty(temp.getDoAutoRenew())){
            errMap.put(serviceCode+"auto",MessageUtil.replaceMessage("GENERAL_ERR0006","Eligible for auto renewal","field"));
        }
        if(StringUtil.isEmpty(temp.getDonewInspectType())){
            errMap.put(serviceCode+"newit",MessageUtil.replaceMessage("GENERAL_ERR0006","Type of Inspection Required for New Application","field"));
        }
        if(StringUtil.isEmpty(temp.getDonewIsPreInspect())){
            errMap.put(serviceCode+"newpp",MessageUtil.replaceMessage("GENERAL_ERR0006","Pre/Post Licensing Inspection for New Application","field"));
        }
        if(StringUtil.isEmpty(temp.getDorenewInspectType())){
            errMap.put(serviceCode+"renewit",MessageUtil.replaceMessage("GENERAL_ERR0006","Type of Inspection Required for Renewal","field"));
        }
        if(StringUtil.isEmpty(temp.getDorenewIsPreInspect())){
            errMap.put(serviceCode+"renewpp",MessageUtil.replaceMessage("GENERAL_ERR0006","Pre/Post Licensing Inspection for Renewal","field"));
        }
    }

    private void numberVad(String strNum, Map<String, String> errMap, String serviceCode,String key) {
        try {
            int num = Integer.parseInt(strNum);
            if(num<0||num>99){
                errMap.put(serviceCode+key,"GENERAL_ERR0027");
            }
        }catch (Exception e){
            errMap.put(serviceCode+key,"GENERAL_ERR0027");
        }

    }

}
