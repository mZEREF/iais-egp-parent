package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskLeadershipMatrixDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskLeaderShipShowDto;
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
 * @Date: 2019/12/24 13:27
 */
public class HcsaLeadershipValidate implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errMap = new HashMap<>();
        RiskLeaderShipShowDto findto = (RiskLeaderShipShowDto) ParamUtil.getSessionAttr(request, "leaderShowDto");
        List<HcsaRiskLeadershipMatrixDto> financeList = findto.getLeaderShipDtoList();
        List<HcsaRiskLeadershipMatrixDto> editList = new ArrayList<>();
        for(HcsaRiskLeadershipMatrixDto dto :financeList){
            if(dto.isAdIsEdit()||dto.isDpIsEdit()){
                editList.add(dto);
            }
        }
        if(editList!=null &&!editList.isEmpty()){
            for(HcsaRiskLeadershipMatrixDto fdto :editList){
                if(fdto.isAdIsEdit()&&fdto.isDpIsEdit()||!StringUtil.isEmpty(fdto.getId())){
                    therholdVad(errMap,fdto);
                    caseCounthVad(errMap,fdto);
                    dateVad(errMap,fdto);
                }else{
                    //one serviceCode need both Edit pr and in
                    errMap.put(fdto.getSvcCode()+"both","The category of Institution and Practitioner should be update at the same time");
                }

            }
        }
        return errMap;
    }

    public void therholdVad(Map<String, String> errMap,HcsaRiskLeadershipMatrixDto fdto){
        if(StringUtil.isEmpty(fdto.getAdThershold())){
            errMap.put(fdto.getSvcCode()+"inThershold","Thershold is mandatory");
        }else{
            try {
                Integer thold = Integer.parseInt(fdto.getAdThershold());
                if(thold<0 || thold>999){
                    errMap.put(fdto.getSvcCode()+"inThershold","Invalid Number");
                }
            }catch (Exception e){
                errMap.put(fdto.getSvcCode()+"inThershold","Invalid Number");
            }
        }
        if(StringUtil.isEmpty(fdto.getDpThershold())){
            errMap.put(fdto.getSvcCode()+"prThershold","Thershold is mandatory");
        }else{
            try {
                Integer thold = Integer.parseInt(fdto.getDpThershold());
                if(thold<0 || thold>999){
                    errMap.put(fdto.getSvcCode()+"prThershold","Invalid Number");
                }
            }catch (Exception e){
                errMap.put(fdto.getSvcCode()+"prThershold","Invalid Number");
            }
        }
    }

    public void caseCounthVad(Map<String, String> errMap,HcsaRiskLeadershipMatrixDto fdto){
        mandatoryCaseCounthVad(errMap,fdto);
        numberCaseCounthVad(errMap,fdto);
    }
    public void numberCaseCounthVad(Map<String, String> errMap,HcsaRiskLeadershipMatrixDto fdto){
        inLeftModVadAndinRightModVad(errMap,fdto.getAdLeftModCaseCounth(),fdto.getAdRightModCaseCounth(),fdto.getSvcCode(),true);
        inLeftModVadAndinRightModVad(errMap,fdto.getDpLeftModCaseCounth(),fdto.getDpRightModCaseCounth(),fdto.getSvcCode(),false);
        inRightLowVad(errMap,fdto.getAdRightLowCaseCounth(),fdto.getAdLeftModCaseCounth(),fdto.getSvcCode(),true);
        inRightLowVad(errMap,fdto.getDpRightLowCaseCounth(),fdto.getDpLeftModCaseCounth(),fdto.getSvcCode(),false);
        inLeftHighVad(errMap,fdto.getAdLeftHighCaseCounth(),fdto.getDpRightModCaseCounth(),fdto.getSvcCode(),true);
        inLeftHighVad(errMap,fdto.getDpLeftHighCaseCounth(),fdto.getDpRightModCaseCounth(),fdto.getSvcCode(),false);

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
    public void inLeftHighVad(Map<String, String> errMap,String inLeftHigh,String inRightMod, String serviceCode,boolean isIn){
        Integer inLeftHighNum = 0;
        Integer inRightModNum = 0;
        try {
            if(!StringUtil.isEmpty(inLeftHigh)){
                inLeftHighNum = Integer.parseInt(inLeftHigh);
                if (inLeftHighNum > 999 || inLeftHighNum < 0) {
                    if(isIn){
                        errMap.put(serviceCode + "inRightLowCaseCounth", "Invalid Number");
                    }else{
                        errMap.put(serviceCode + "prRightLowCaseCounth", "Invalid Number");
                    }
                }
            }
        } catch (Exception e) {
            if(isIn){
                errMap.put(serviceCode + "inRightLowCaseCounth", "Invalid Number");
            }else{
                errMap.put(serviceCode + "prRightLowCaseCounth", "Invalid Number");
            }
            e.printStackTrace();
        }
        try {
            if(!StringUtil.isEmpty(inRightMod)){
                inRightModNum = Integer.parseInt(inRightMod);
                if(inRightModNum +1 != inLeftHighNum){
                    if(isIn){
                        errMap.put(serviceCode + "inRightLowCaseCounth", "High Maximun cases and Moderate Minimun can only differ by 1");
                    }else{
                        errMap.put(serviceCode + "prRightLowCaseCounth", "High Maximun cases and Moderate Minimun can only differ by 1");
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void inRightLowVad(Map<String, String> errMap,String inRightLow,String inLeftMod,String serviceCode,boolean isIn){
        Integer inRightLowNum = 0;
        Integer inLeftModNum = 0;
        try {
            if(!StringUtil.isEmpty(inRightLow)){
                inRightLowNum = Integer.parseInt(inRightLow);
                if (inRightLowNum > 999 || inRightLowNum < 0) {
                    if(isIn){
                        errMap.put(serviceCode + "inRightLowCaseCounth", "Invalid Number");
                    }else{
                        errMap.put(serviceCode + "prRightLowCaseCounth", "Invalid Number");
                    }
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            if(isIn){
                errMap.put(serviceCode + "inRightLowCaseCounth", "Invalid Number");
            }else{
                errMap.put(serviceCode + "prRightLowCaseCounth", "Invalid Number");
            }

            e.printStackTrace();
        }
        try {
            if(!StringUtil.isEmpty(inLeftMod)){
                inLeftModNum = Integer.parseInt(inLeftMod);
                if(inLeftModNum -1 != inRightLowNum){
                    if(isIn){
                        errMap.put(serviceCode + "inRightLowCaseCounth", "Low Maximun cases and Moderate Minimun can only differ by 1");
                    }else{
                        errMap.put(serviceCode + "prRightLowCaseCounth", "Low Maximun cases and Moderate Minimun can only differ by 1");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void mandatoryCaseCounthVad(Map<String, String> errMap,HcsaRiskLeadershipMatrixDto fdto){
        //in
        if(StringUtil.isEmpty(fdto.getAdLeftModCaseCounth())){
            errMap.put(fdto.getSvcCode()+"inLeftModCaseCounth","CaseCounth is mandatory");
        }
        if(StringUtil.isEmpty(fdto.getAdRightModCaseCounth())){
            errMap.put(fdto.getSvcCode()+"inRightModCaseCounth()","CaseCounth is mandatory");
        }
        if(StringUtil.isEmpty(fdto.getAdRightLowCaseCounth())){
            errMap.put(fdto.getSvcCode()+"inRightLowCaseCounth","CaseCounth is mandatory");
        }
        if(StringUtil.isEmpty(fdto.getAdLeftHighCaseCounth())){
            errMap.put(fdto.getSvcCode()+"inLeftHighCaseCounth","CaseCounth is mandatory");
        }
        //pr
        if(StringUtil.isEmpty(fdto.getDpLeftModCaseCounth())){
            errMap.put(fdto.getSvcCode()+"prLeftModCaseCounth","CaseCounth is mandatory");
        }
        if(StringUtil.isEmpty(fdto.getDpRightModCaseCounth())){
            errMap.put(fdto.getSvcCode()+"prRightModCaseCounth()","CaseCounth is mandatory");
        }
        if(StringUtil.isEmpty(fdto.getDpRightLowCaseCounth())){
            errMap.put(fdto.getSvcCode()+"prRightLowCaseCounth","CaseCounth is mandatory");
        }
        if(StringUtil.isEmpty(fdto.getDpRightLowCaseCounth())){
            errMap.put(fdto.getSvcCode()+"prLeftHighCaseCounth","CaseCounth is mandatory");
        }
    }

    public void dateVad(Map<String, String> errMap,HcsaRiskLeadershipMatrixDto fdto){
        String inEffDate = fdto.getAdEffectiveStartDate();
        String inEndDate = fdto.getAdEffectiveEndDate();
        String prEffDate = fdto.getDpEffectiveStartDate();
        String prEndDate = fdto.getDpEffectiveEndDate();
        boolean inDateFormatVad = doDateFormatVad(errMap,inEffDate,inEndDate,fdto.getSvcCode(),true);
        boolean prDateFormatVad = doDateFormatVad(errMap,prEffDate,prEndDate,fdto.getSvcCode(),false);
        if(inDateFormatVad&&fdto.isAdIsEdit()){
            doDateLogicVad(errMap,fdto,true);
        }
        if(prDateFormatVad&&fdto.isDpIsEdit()){
            doDateLogicVad(errMap,fdto,false);
        }

    }
    public void doDateLogicVad(Map<String, String> errMap,HcsaRiskLeadershipMatrixDto fdto,boolean isIn){
        //effdate least version <
        try {
            Date inEffDate = Formatter.parseDate(fdto.getAdEffectiveStartDate());
            Date inEndDate = Formatter.parseDate(fdto.getAdEffectiveEndDate());
            Date prEffDate = Formatter.parseDate(fdto.getDpEffectiveStartDate());
            Date prEndDate = Formatter.parseDate(fdto.getDpEffectiveEndDate());
            int inEditNumFlag = 0;
            int prEditNumFlag = 0;
            if(fdto.isAdIsEdit()){
                inEditNumFlag = 1;
            }
            if(fdto.isDpIsEdit()){
                prEditNumFlag = 1;
            }
            if (StringUtil.isEmpty(fdto.getId())) {
                doUsualDateVad(inEffDate,inEndDate,fdto.getSvcCode(),errMap,true,inEditNumFlag,prEditNumFlag);
                doUsualDateVad(prEffDate,prEndDate,fdto.getSvcCode(),errMap,false,inEditNumFlag,prEditNumFlag);
            } else {
                boolean inDateFlag;
                inDateFlag = doUsualDateVad(inEffDate,inEndDate,fdto.getSvcCode(),errMap,true,inEditNumFlag,prEditNumFlag);
                if(inDateFlag){
                    doSpecialDateFlag(errMap,fdto,true);
                }
                inDateFlag = doUsualDateVad(prEffDate,prEndDate,fdto.getSvcCode(),errMap,false,inEditNumFlag,prEditNumFlag);
                if(inDateFlag){
                    doSpecialDateFlag(errMap,fdto,true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void doSpecialDateFlag(Map<String, String> errMap,HcsaRiskLeadershipMatrixDto fdto,boolean isIn){
        try {
            Date inEffDate = Formatter.parseDate(fdto.getAdEffectiveStartDate());
            Date inEndDate = Formatter.parseDate(fdto.getAdEffectiveEndDate());
            Date prEffDate = Formatter.parseDate(fdto.getDpEffectiveStartDate());
            Date prEndDate = Formatter.parseDate(fdto.getDpEffectiveEndDate());
            Date baseInEffDate = Formatter.parseDate(fdto.getBaseAdEffectiveStartDate());
            Date baseInEndDate = Formatter.parseDate(fdto.getBaseAdEffectiveEndDate());
            Date basePrEffDate = Formatter.parseDate(fdto.getBaseDpEffectiveStartDate());
            Date basePrEndDate = Formatter.parseDate(fdto.getBaseDpEffectiveEndDate());
            if(inEffDate.getTime()<baseInEffDate.getTime()){
                errMap.put(fdto.getSvcCode() + "inEffDate", "EffectiveDate should later than Previous version");
            }
            if(prEffDate.getTime()<basePrEffDate.getTime()){
                errMap.put(fdto.getSvcCode() + "prEffDate", "EffectiveDate should later than Previous version");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean doUsualDateVad(Date effDate,Date endDate,String serviceCode,Map<String, String> errMap,boolean isIn,int inEdit,int prEdit){
        boolean flag = true;
        if (effDate.getTime() < System.currentTimeMillis()) {
            flag = false;
            if(isIn){
                if(inEdit == 1){
                    errMap.put(serviceCode + "inEffDate", "EffectiveDate should be furture time");
                }
            }else {
                if(prEdit == 1){
                    errMap.put(serviceCode + "prEffDate", "EffectiveDate should be furture time");
                }
            }
        } else if (endDate.getTime() < effDate.getTime()) {
            flag = false;
            if(isIn){
                if(inEdit == 1){
                    errMap.put(serviceCode + "inEndDate", "EffectiveDate should be ealier than EndDate");
                }
            }else{
                if(prEdit == 1){
                    errMap.put(serviceCode + "prEndDate", "EffectiveDate should be ealier than EndDate");
                }
            }
        }
        return flag;
    }
    public boolean doDateFormatVad(Map<String, String> errMap,String strEffDate,String strEndDate,String serviceCode,boolean isIn){
        boolean vadFlag = true;
        if(StringUtil.isEmpty(strEffDate)){
            vadFlag = false;
            if(isIn){
                errMap.put(serviceCode+"inEffDate","EffectiveDate is mandatory");
            }else{
                errMap.put(serviceCode+"prEffDate","EffectiveDate is mandatory");
            }
        }else{
            try {
                Formatter.parseDate(strEffDate);
            }catch (Exception e){
                if(isIn){
                    errMap.put(serviceCode+"inEffDate","Date Format Error");
                }else{
                    errMap.put(serviceCode+"prEffDate","Date Format Error");
                }
                vadFlag = false;
            }
        }
        if(StringUtil.isEmpty(strEndDate)){
            vadFlag = false;
            if(isIn){
                errMap.put(serviceCode+"inEndDate","EndDate is mandatory");
            }else{
                errMap.put(serviceCode+"prEndDate","EndDate is mandatory");
            }
        }else{
            try {
                Formatter.parseDate(strEndDate);
            }catch (Exception e){
                vadFlag = false;
                if(isIn){
                    errMap.put(serviceCode+"inEndDate","Date Format Error");
                }else{
                    errMap.put(serviceCode+"prEndDate","Date Format Error");
                }
                return false;
            }
        }
        return vadFlag;
    }

}
