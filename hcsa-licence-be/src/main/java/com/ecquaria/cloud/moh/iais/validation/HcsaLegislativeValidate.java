package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskLegislativeMatrixDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskLegislativeShowDto;
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
 * @Date: 2019/12/25 13:27
 */
public class HcsaLegislativeValidate implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errMap = new HashMap<>();
        RiskLegislativeShowDto legShowDto = (RiskLegislativeShowDto)ParamUtil.getSessionAttr(request, "legShowDto");
        List<HcsaRiskLegislativeMatrixDto> financeList = legShowDto.getLegislativeList();
        List<HcsaRiskLegislativeMatrixDto> editList = new ArrayList<>();
        for(HcsaRiskLegislativeMatrixDto dto :financeList){
            if(dto.isEdit()){
                editList.add(dto);
            }
        }
        if(editList!=null &&!editList.isEmpty()){
            for(HcsaRiskLegislativeMatrixDto fdto :editList){
                therholdVad(errMap, fdto);
                caseCounthVad(errMap, fdto);
                dateVad(errMap, fdto);
            }
        }else{
            errMap.put("All","Please do some change");
        }
        return errMap;
    }
    public void dateVad(Map<String, String> errMap,HcsaRiskLegislativeMatrixDto fdto){
        String inEffDate = fdto.getDoEffectiveDate();
        String inEndDate = fdto.getDoEndDate();
        boolean inDateFormatVad = doDateFormatVad(errMap,inEffDate,inEndDate,fdto.getSvcCode());
        if(inDateFormatVad&&fdto.isEdit()){
            doDateLogicVad(errMap,fdto,true);
        }


    }

    public void doDateLogicVad(Map<String, String> errMap,HcsaRiskLegislativeMatrixDto fdto,boolean isIn){
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
                doUsualDateVad(inEffDate,inEndDate,fdto.getSvcCode(),errMap,inEditNumFlag,prEditNumFlag);
            } else {
                boolean inDateFlag;
                inDateFlag = doUsualDateVad(inEffDate,inEndDate,fdto.getSvcCode(),errMap,inEditNumFlag,prEditNumFlag);
                if(inDateFlag){
                    doSpecialDateFlag(errMap,fdto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doSpecialDateFlag(Map<String, String> errMap,HcsaRiskLegislativeMatrixDto fdto){
        try {
            Date inEffDate = Formatter.parseDate(fdto.getDoEffectiveDate());
            Date inEndDate = Formatter.parseDate(fdto.getDoEndDate());
            Date baseInEffDate = Formatter.parseDate(fdto.getBaseEffectiveDate());
            Date baseInEndDate = Formatter.parseDate(fdto.getBaseEndDate());
            if(inEffDate.getTime()<baseInEffDate.getTime()){
                errMap.put(fdto.getSvcCode() + "inEffDate", "EffectiveDate should later than Previous version");
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

    public void therholdVad(Map<String, String> errMap,HcsaRiskLegislativeMatrixDto fdto){
        if(StringUtil.isEmpty(fdto.getDoThershold())){
            errMap.put(fdto.getSvcCode()+"inThershold","Thershold is mandatory");
        }else{
            try {
                Integer thold = Integer.parseInt(fdto.getDoThershold());
                if(thold<0 || thold>999){
                    errMap.put(fdto.getSvcCode()+"inThershold","Invalid Number");
                }
            }catch (Exception e){
                errMap.put(fdto.getSvcCode()+"inThershold","Invalid Number");
            }
        }
    }

    public void caseCounthVad(Map<String, String> errMap,HcsaRiskLegislativeMatrixDto fdto){
        mandatoryCaseCounthVad(errMap,fdto);
        numberCaseCounthVad(errMap,fdto);
    }
    public void numberCaseCounthVad(Map<String, String> errMap,HcsaRiskLegislativeMatrixDto fdto){
        inLeftModVadAndinRightModVad(errMap,fdto.getDoLeftModCaseCounth(),fdto.getDoRightModCaseCounth(),fdto.getSvcCode(),true);
        inRightLowVad(errMap,fdto.getDoRightLowCaseCounth(),fdto.getDoLeftModCaseCounth(),fdto.getSvcCode());
        inLeftHighVad(errMap,fdto.getDoLeftHighCaseCounth(),fdto.getDoRightModCaseCounth(),fdto.getSvcCode());

    }
    public void inLeftHighVad(Map<String, String> errMap,String inLeftHigh,String inRightMod, String serviceCode){
        Integer inLeftHighNum = 0;
        Integer inRightModNum = 0;
        try {
            if(!StringUtil.isEmpty(inLeftHigh)){
                inLeftHighNum = Integer.parseInt(inLeftHigh);
                if (inLeftHighNum > 999 || inLeftHighNum < 0) {
                    errMap.put(serviceCode + "inRightLowCaseCounth", "Invalid Number");
                }
            }
        } catch (Exception e) {
            errMap.put(serviceCode + "inRightLowCaseCounth", "Invalid Number");
            e.printStackTrace();
        }
        try {
            if(!StringUtil.isEmpty(inRightMod)){
                inRightModNum = Integer.parseInt(inRightMod);
                if(inRightModNum +1 != inLeftHighNum){
                    errMap.put(serviceCode + "inRightLowCaseCounth", "High Maximun cases and Moderate Minimun can only differ by 1");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void inRightLowVad(Map<String, String> errMap,String inRightLow,String inLeftMod,String serviceCode){
        Integer inRightLowNum = 0;
        Integer inLeftModNum = 0;
        try {
            if(!StringUtil.isEmpty(inRightLow)){
                inRightLowNum = Integer.parseInt(inRightLow);
                if (inRightLowNum > 999 || inRightLowNum < 0) {
                    errMap.put(serviceCode + "inRightLowCaseCounth", "Invalid Number");
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
                errMap.put(serviceCode + "inRightLowCaseCounth", "Invalid Number");
            e.printStackTrace();
        }
        try {
            if(!StringUtil.isEmpty(inLeftMod)){
                inLeftModNum = Integer.parseInt(inLeftMod);
                if(inLeftModNum -1 != inRightLowNum){
                    errMap.put(serviceCode + "inRightLowCaseCounth", "Low Maximun cases and Moderate Minimun can only differ by 1");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void inLeftModVadAndinRightModVad(Map<String, String> errMap,String inLeftMod,String inRightMod,String serviceCode,boolean isIn){
        Integer inLeftModNum = 0;
        Integer inRightModNum = 0;
        int numberFlag = 0;
        if(!StringUtil.isEmpty(inLeftMod)){
            try {
                inLeftModNum = Integer.parseInt(inLeftMod);
                if(inLeftModNum<0||inLeftModNum>999){
                    if(isIn){
                        errMap.put(serviceCode+"inLeftModCaseCounth","Invalid Number");
                    }else{
                        errMap.put(serviceCode+"prLeftModCaseCounth","Invalid Number");
                    }
                }
                numberFlag++;
            }catch (Exception e){
                if(isIn){
                    errMap.put(serviceCode+"inLeftModCaseCounth","Invalid Number");
                }else{
                    errMap.put(serviceCode+"prLeftModCaseCounth","Invalid Number");
                }
                e.printStackTrace();
            }
        }
        if(!StringUtil.isEmpty(inRightMod)){
            try {
                inRightModNum = Integer.parseInt(inRightMod);
                if(inRightModNum<0 || inRightModNum >999){
                    errMap.put(serviceCode+"inRightModCaseCounth","Invalid Number");
                }
                numberFlag++;
            }catch (Exception e){
                errMap.put(serviceCode+"inRightModCaseCounth","Invalid Number");
                e.printStackTrace();
            }
        }
        if(numberFlag == 2){
            numberOrderVad(errMap,inLeftModNum,inRightModNum,serviceCode);
        }
    }
    public void numberOrderVad(Map<String, String> errMap,Integer lm,Integer rm,String serviceCode){
        if(lm>rm){
            errMap.put(serviceCode+"inRightModCaseCounth","Minimun cases should be smaller than Maximun cases");
        }
    }
    public void mandatoryCaseCounthVad(Map<String, String> errMap,HcsaRiskLegislativeMatrixDto fdto){
        //in
        if(StringUtil.isEmpty(fdto.getDoLeftModCaseCounth())){
            errMap.put(fdto.getSvcCode()+"inLeftModCaseCounth","CaseCounth is mandatory");
        }
        if(StringUtil.isEmpty(fdto.getDoRightModCaseCounth())){
            errMap.put(fdto.getSvcCode()+"inRightModCaseCounth()","CaseCounth is mandatory");
        }
        if(StringUtil.isEmpty(fdto.getDoRightLowCaseCounth())){
            errMap.put(fdto.getSvcCode()+"inRightLowCaseCounth","CaseCounth is mandatory");
        }
        if(StringUtil.isEmpty(fdto.getDoLeftHighCaseCounth())){
            errMap.put(fdto.getSvcCode()+"inLeftHighCaseCounth","CaseCounth is mandatory");
        }
    }
}
