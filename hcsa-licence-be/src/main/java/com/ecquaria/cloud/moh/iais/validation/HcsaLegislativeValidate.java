package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskLegislativeMatrixDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskLegislativeShowDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author: jiahao
 * @Date: 2019/12/25 13:27
 */
public class HcsaLegislativeValidate implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        RiskLegislativeShowDto legShowDto = (RiskLegislativeShowDto)ParamUtil.getSessionAttr(request, "legShowDto");
        List<HcsaRiskLegislativeMatrixDto> financeList = legShowDto.getLegislativeList();
        List<HcsaRiskLegislativeMatrixDto> editList = IaisCommonUtils.genNewArrayList();
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
        mergeList(editList,financeList);
        return errMap;

    }


    public void mergeList(List<HcsaRiskLegislativeMatrixDto> editList, List<HcsaRiskLegislativeMatrixDto> financeList){
        if(editList!=null &&financeList!=null){
            for(HcsaRiskLegislativeMatrixDto fin:financeList){
                for(HcsaRiskLegislativeMatrixDto ed:editList){
                    if(ed.getSvcCode().equals(fin.getSvcCode())){
                        fin = ed;
                    }
                }
            }
        }
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
        inLeftModVadAndinRightModVad(errMap,fdto.getDoLeftModCaseCounth(),fdto.getDoRightModCaseCounth(),fdto.getSvcCode(),fdto);
        inRightLowVad(errMap,fdto.getDoRightLowCaseCounth(),fdto.getDoLeftModCaseCounth(),fdto.getSvcCode(),fdto);
        inLeftHighVad(errMap,fdto.getDoLeftHighCaseCounth(),fdto.getDoRightModCaseCounth(),fdto.getSvcCode(),fdto);

    }
    public void inLeftHighVad(Map<String, String> errMap,String inLeftHigh,String inRightMod, String serviceCode,HcsaRiskLegislativeMatrixDto fdto){
        Integer inLeftHighNum = 0;
        boolean inLeftHighNumFlag = true;
        Integer inRightModNum = 0;
        try {
            if(!StringUtil.isEmpty(inLeftHigh)){
                inLeftHighNum = Integer.parseInt(inLeftHigh);
                if (inLeftHighNum > 999 || inLeftHighNum < 0) {
                    inLeftHighNumFlag = false;
                    errMap.put(serviceCode + "inLeftHighCaseCounth", "Invalid Number");
                    fdto.setDoLeftHighCaseCountherr(true);
                }
            }
        } catch (Exception e) {
            inLeftHighNumFlag = false;
            errMap.put(serviceCode + "inLeftHighCaseCounth", "Invalid Number");
            fdto.setDoLeftHighCaseCountherr(true);
            e.printStackTrace();
        }
        try {
            if(!StringUtil.isEmpty(inRightMod)){
                inRightModNum = Integer.parseInt(inRightMod);
                if((inRightModNum +1 != inLeftHighNum)&&inLeftHighNumFlag){
                    errMap.put(serviceCode + "inLeftHighCaseCounth", "High Maximun cases and Moderate Minimun can only differ by 1");
                    fdto.setDoLeftHighCaseCountherr(true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void inRightLowVad(Map<String, String> errMap,String inRightLow,String inLeftMod,String serviceCode,HcsaRiskLegislativeMatrixDto fdto){
        Integer inRightLowNum = 0;
        boolean inrightflag = true;
        Integer inLeftModNum = 0;
        try {
            if(!StringUtil.isEmpty(inRightLow)){
                inRightLowNum = Integer.parseInt(inRightLow);
                if (inRightLowNum > 999 || inRightLowNum < 0) {
                    inrightflag = false;
                    errMap.put(serviceCode + "inRightLowCaseCounth", "Invalid Number");
                    fdto.setDoRightModCaseCountherr(true);
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
                inrightflag = false;
                errMap.put(serviceCode + "inRightLowCaseCounth", "Invalid Number");
                fdto.setDoRightLowCaseCountherr(true);
            e.printStackTrace();
        }
        try {
            if(!StringUtil.isEmpty(inLeftMod)&&inrightflag){
                inLeftModNum = Integer.parseInt(inLeftMod);
                if(inLeftModNum -1 != inRightLowNum){
                    errMap.put(serviceCode + "inRightLowCaseCounth", "Low Maximun cases and Moderate Minimun can only differ by 1");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void inLeftModVadAndinRightModVad(Map<String, String> errMap,String inLeftMod,String inRightMod,String serviceCode,HcsaRiskLegislativeMatrixDto fdto){
        Integer inLeftModNum = 0;
        Integer inRightModNum = 0;
        int numberFlag = 0;
        if(!StringUtil.isEmpty(inLeftMod)){
            try {
                inLeftModNum = Integer.parseInt(inLeftMod);
                if(inLeftModNum<0||inLeftModNum>999){
                        errMap.put(serviceCode+"inLeftModCaseCounth","Invalid Number");
                        fdto.setDoLeftModCaseCountherr(true);
                }
                numberFlag++;
            }catch (Exception e){
                    errMap.put(serviceCode+"inLeftModCaseCounth","Invalid Number");
                    fdto.setDoLeftModCaseCountherr(true);
                e.printStackTrace();
            }
        }
        if(!StringUtil.isEmpty(inRightMod)){
            try {
                inRightModNum = Integer.parseInt(inRightMod);
                if(inRightModNum<0 || inRightModNum >999){
                    errMap.put(serviceCode+"inRightModCaseCounth","Invalid Number");
                    fdto.setDoRightModCaseCountherr(true);
                }
                numberFlag++;
            }catch (Exception e){
                errMap.put(serviceCode+"inRightModCaseCounth","Invalid Number");
                fdto.setDoRightModCaseCountherr(true);
                e.printStackTrace();
            }
        }
        if(numberFlag == 2){
            numberOrderVad(errMap,inLeftModNum,inRightModNum,serviceCode,fdto);
        }
    }
    public void numberOrderVad(Map<String, String> errMap,Integer lm,Integer rm,String serviceCode,HcsaRiskLegislativeMatrixDto fdto){
        if(lm>rm){
            errMap.put(serviceCode+"inRightModCaseCounth","Minimun cases should be smaller than Maximun cases");
            fdto.setDoLeftModCaseCountherr(true);
            fdto.setDoRightModCaseCountherr(true);
        }
    }
    public void mandatoryCaseCounthVad(Map<String, String> errMap,HcsaRiskLegislativeMatrixDto fdto){
        //in
        if(StringUtil.isEmpty(fdto.getDoLeftModCaseCounth())){
            errMap.put(fdto.getSvcCode()+"inLeftModCaseCounth","CaseCounth is mandatory");
            fdto.setDoLeftModCaseCountherr(true);
        }
        if(StringUtil.isEmpty(fdto.getDoRightModCaseCounth())){
            errMap.put(fdto.getSvcCode()+"inRightModCaseCounth()","CaseCounth is mandatory");
            fdto.setDoRightModCaseCountherr(true);
        }
        if(StringUtil.isEmpty(fdto.getDoRightLowCaseCounth())){
            errMap.put(fdto.getSvcCode()+"inRightLowCaseCounth","CaseCounth is mandatory");
            fdto.setDoRightLowCaseCountherr(true);
        }
        if(StringUtil.isEmpty(fdto.getDoLeftHighCaseCounth())){
            errMap.put(fdto.getSvcCode()+"inLeftHighCaseCounth","CaseCounth is mandatory");
            fdto.setDoLeftModCaseCountherr(true);
        }
    }
}
