package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskLeadershipMatrixDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskLeaderShipShowDto;
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
 * @Date: 2019/12/24 13:27
 */
public class HcsaLeadershipValidate implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        RiskLeaderShipShowDto findto = (RiskLeaderShipShowDto) ParamUtil.getSessionAttr(request, "leaderShowDto");
        List<HcsaRiskLeadershipMatrixDto> financeList = findto.getLeaderShipDtoList();
        List<HcsaRiskLeadershipMatrixDto> editList = IaisCommonUtils.genNewArrayList();
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
        }else{
            errMap.put("All","Please do some change");
        }
        mergeList(editList,financeList);
        return errMap;
    }

    public void mergeList(List<HcsaRiskLeadershipMatrixDto> editList, List<HcsaRiskLeadershipMatrixDto> financeList){
        if(editList!=null &&financeList!=null){
            for(HcsaRiskLeadershipMatrixDto fin:financeList){
                for(HcsaRiskLeadershipMatrixDto ed:editList){
                    if(ed.getSvcCode().equals(fin.getSvcCode())){
                        fin = ed;
                    }
                }
            }
        }
    }

    public void therholdVad(Map<String, String> errMap,HcsaRiskLeadershipMatrixDto fdto){
        if(StringUtil.isEmpty(fdto.getAdThershold())){
            errMap.put(fdto.getSvcCode()+"inThershold","Thershold is mandatory");
            fdto.setAdThersholderr(true);
        }else{
            try {
                Integer thold = Integer.parseInt(fdto.getAdThershold());
                if(thold<0 || thold>999){
                    errMap.put(fdto.getSvcCode()+"inThershold","Invalid Number");
                    fdto.setAdThersholderr(true);
                }
            }catch (Exception e){
                errMap.put(fdto.getSvcCode()+"inThershold","Invalid Number");
                fdto.setAdThersholderr(true);
            }
        }
        if(StringUtil.isEmpty(fdto.getDpThershold())){
            errMap.put(fdto.getSvcCode()+"prThershold","Thershold is mandatory");
            fdto.setDpThersholderr(true);
        }else{
            try {
                Integer thold = Integer.parseInt(fdto.getDpThershold());
                if(thold<0 || thold>999){
                    errMap.put(fdto.getSvcCode()+"prThershold","Invalid Number");
                    fdto.setDpThersholderr(true);
                }
            }catch (Exception e){
                errMap.put(fdto.getSvcCode()+"prThershold","Invalid Number");
                fdto.setDpThersholderr(true);
            }
        }
    }

    public void caseCounthVad(Map<String, String> errMap,HcsaRiskLeadershipMatrixDto fdto){
        mandatoryCaseCounthVad(errMap,fdto);
        numberCaseCounthVad(errMap,fdto);
    }
    public void numberCaseCounthVad(Map<String, String> errMap,HcsaRiskLeadershipMatrixDto fdto){
        inLeftModVadAndinRightModVad(errMap,fdto.getAdLeftModCaseCounth(),fdto.getAdRightModCaseCounth(),fdto.getSvcCode(),true,fdto);
        inLeftModVadAndinRightModVad(errMap,fdto.getDpLeftModCaseCounth(),fdto.getDpRightModCaseCounth(),fdto.getSvcCode(),false,fdto);
        inRightLowVad(errMap,fdto.getAdRightLowCaseCounth(),fdto.getAdLeftModCaseCounth(),fdto.getSvcCode(),true,fdto);
        inRightLowVad(errMap,fdto.getDpRightLowCaseCounth(),fdto.getDpLeftModCaseCounth(),fdto.getSvcCode(),false,fdto);
        inLeftHighVad(errMap,fdto.getAdLeftHighCaseCounth(),fdto.getAdRightModCaseCounth(),fdto.getSvcCode(),true,fdto);
        inLeftHighVad(errMap,fdto.getDpLeftHighCaseCounth(),fdto.getDpRightModCaseCounth(),fdto.getSvcCode(),false,fdto);

    }
    public void inLeftModVadAndinRightModVad(Map<String, String> errMap,String inLeftMod,String inRightMod,String serviceCode,boolean isIn,HcsaRiskLeadershipMatrixDto fdto){
        Integer inLeftModNum = 0;
        Integer inRightModNum = 0;
        int numberFlag = 0;
        if(!StringUtil.isEmpty(inLeftMod)){
            try {
                inLeftModNum = Integer.parseInt(inLeftMod);
                if(inLeftModNum<0||inLeftModNum>999){
                    if(isIn){
                        errMap.put(serviceCode+"inLeftModCaseCounth","Invalid Number");
                        fdto.setAdLeftModCaseCountherr(true);
                    }else{
                        errMap.put(serviceCode+"prLeftModCaseCounth","Invalid Number");
                        fdto.setDpLeftModCaseCountherr(true);
                    }
                }
                numberFlag++;
            }catch (Exception e){
                if(isIn){
                    errMap.put(serviceCode+"inLeftModCaseCounth","Invalid Number");
                    fdto.setAdLeftModCaseCountherr(true);
                }else{
                    errMap.put(serviceCode+"prLeftModCaseCounth","Invalid Number");
                    fdto.setDpLeftModCaseCountherr(true);
                }
                e.printStackTrace();
            }
        }
        if(!StringUtil.isEmpty(inRightMod)){
            try {
                inRightModNum = Integer.parseInt(inRightMod);
                if(inRightModNum<0 || inRightModNum >999){
                    if(isIn){
                        errMap.put(serviceCode+"inRightModCaseCounth","Invalid Number");
                        fdto.setAdRightModCaseCountherr(true);
                    }else{
                        errMap.put(serviceCode+"prRightModCaseCounth","Invalid Number");
                        fdto.setDpRightModCaseCountherr(true);
                    }
            }
                numberFlag++;
            }catch (Exception e){
                if(isIn){
                    errMap.put(serviceCode+"inRightModCaseCounth","Invalid Number");
                    fdto.setAdRightModCaseCountherr(true);
                }else{
                    errMap.put(serviceCode+"prRightModCaseCounth","Invalid Number");
                    fdto.setDpRightModCaseCountherr(true);
                }
                e.printStackTrace();
            }
        }
        if(numberFlag == 2){
            numberOrderVad(errMap,inLeftModNum,inRightModNum,serviceCode,isIn,fdto);
        }
    }
    public void numberOrderVad(Map<String, String> errMap,Integer lm,Integer rm,String serviceCode,boolean isIn,HcsaRiskLeadershipMatrixDto fdto){
        if(lm>rm&&isIn){
            errMap.put(serviceCode+"inRightModCaseCounth","Minimun cases should be smaller than Maximun cases");
            fdto.setAdRightModCaseCountherr(true);
        }else if(lm>rm&&!isIn){
            errMap.put(serviceCode+"prRightModCaseCounth","Minimun cases should be smaller than Maximun cases");
            fdto.setDpRightModCaseCountherr(true);
        }
    }
    public void inLeftHighVad(Map<String, String> errMap,String inLeftHigh,String inRightMod, String serviceCode,boolean isIn,HcsaRiskLeadershipMatrixDto fdto){
        Integer inLeftHighNum = 0;
        boolean inLeftHighNumFlag = true;
        boolean prLeftHighNumFlag = true;
        Integer inRightModNum = 0;
        try {
            if(!StringUtil.isEmpty(inLeftHigh)){
                inLeftHighNum = Integer.parseInt(inLeftHigh);
                if (inLeftHighNum > 999 || inLeftHighNum < 0) {
                    if(isIn){
                        inLeftHighNumFlag = false;
                        errMap.put(serviceCode + "inLeftHighCaseCounth", "Invalid Number");
                        fdto.setAdLeftHighCaseCountherr(true);
                    }else{
                        prLeftHighNumFlag = false;
                        errMap.put(serviceCode + "prLeftHighCaseCounth", "Invalid Number");
                        fdto.setDpLeftHighCaseCountherr(true);
                    }
                }
            }
        } catch (Exception e) {
            if(isIn){
                inLeftHighNumFlag = false;
                errMap.put(serviceCode + "inLeftHighCaseCounth", "Invalid Number");
                fdto.setAdLeftHighCaseCountherr(true);
            }else{
                prLeftHighNumFlag = false;
                errMap.put(serviceCode + "prLeftHighCaseCounth", "Invalid Number");
                fdto.setDpLeftHighCaseCountherr(true);
            }
            e.printStackTrace();
        }
        try {
            if(!StringUtil.isEmpty(inRightMod)){
                inRightModNum = Integer.parseInt(inRightMod);
                if(inRightModNum +1 != inLeftHighNum){
                    if(isIn&&inLeftHighNumFlag){
                        errMap.put(serviceCode + "inLeftHighCaseCounth", "High Maximun cases and Moderate Minimax can only differ by 1");
                        fdto.setAdLeftHighCaseCountherr(true);
                    }else if(!isIn&&prLeftHighNumFlag){
                        errMap.put(serviceCode + "prLeftHighCaseCounth", "High Maximun cases and Moderate Minimax can only differ by 1");
                        fdto.setDpLeftHighCaseCountherr(true);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void inRightLowVad(Map<String, String> errMap,String inRightLow,String inLeftMod,String serviceCode,boolean isIn,HcsaRiskLeadershipMatrixDto fdto){
        Integer inRightLowNum = 0;
        Integer inLeftModNum = 0;
        boolean inRightLowNumFlag = true;
        boolean prRightLowNumFlag = true;
        try {
            if(!StringUtil.isEmpty(inRightLow)){
                inRightLowNum = Integer.parseInt(inRightLow);
                if (inRightLowNum > 999 || inRightLowNum < 0) {
                    if(isIn){
                        inRightLowNumFlag = false;
                        errMap.put(serviceCode + "inRightLowCaseCounth", "Invalid Number");
                        fdto.setAdRightLowCaseCountherr(true);
                    }else{
                        prRightLowNumFlag = false;
                        errMap.put(serviceCode + "prRightLowCaseCounth", "Invalid Number");
                        fdto.setDpRightLowCaseCountherr(true);
                    }
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            if(isIn){
                if(inRightLowNumFlag){
                    errMap.put(serviceCode + "inRightLowCaseCounth", "Invalid Number");
                    fdto.setAdRightModCaseCountherr(true);
                }
            }else{
                if(prRightLowNumFlag){
                    errMap.put(serviceCode + "prRightLowCaseCounth", "Invalid Number");
                    fdto.setDpRightLowCaseCountherr(true);
                }
            }

            e.printStackTrace();
        }
        try {
            if(!StringUtil.isEmpty(inLeftMod)){
                inLeftModNum = Integer.parseInt(inLeftMod);
                if(inLeftModNum -1 != inRightLowNum){
                    if(isIn){
                        errMap.put(serviceCode + "inRightLowCaseCounth", "Low Maximun cases and Moderate Minimun can only differ by 1");
                        fdto.setAdRightLowCaseCountherr(true);
                    }else{
                        errMap.put(serviceCode + "prRightLowCaseCounth", "Low Maximun cases and Moderate Minimun can only differ by 1");
                        fdto.setDpRightLowCaseCountherr(true);
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
            fdto.setAdLeftModCaseCountherr(true);
        }
        if(StringUtil.isEmpty(fdto.getAdRightModCaseCounth())){
            errMap.put(fdto.getSvcCode()+"inRightModCaseCounth()","CaseCounth is mandatory");
            fdto.setAdRightModCaseCountherr(true);
        }
        if(StringUtil.isEmpty(fdto.getAdRightLowCaseCounth())){
            fdto.setAdRightLowCaseCountherr(true);
            errMap.put(fdto.getSvcCode()+"inRightLowCaseCounth","CaseCounth is mandatory");
        }
        if(StringUtil.isEmpty(fdto.getAdLeftHighCaseCounth())){
            fdto.setAdLeftHighCaseCountherr(true);
            errMap.put(fdto.getSvcCode()+"inLeftHighCaseCounth","CaseCounth is mandatory");
        }
        //pr
        if(StringUtil.isEmpty(fdto.getDpLeftModCaseCounth())){
            fdto.setDpLeftModCaseCountherr(true);
            errMap.put(fdto.getSvcCode()+"prLeftModCaseCounth","CaseCounth is mandatory");
        }
        if(StringUtil.isEmpty(fdto.getDpRightModCaseCounth())){
            fdto.setDpRightModCaseCountherr(true);
            errMap.put(fdto.getSvcCode()+"prRightModCaseCounth()","CaseCounth is mandatory");
        }
        if(StringUtil.isEmpty(fdto.getDpRightLowCaseCounth())){
            fdto.setDpRightLowCaseCountherr(true);
            errMap.put(fdto.getSvcCode()+"prRightLowCaseCounth","CaseCounth is mandatory");
        }
        if(StringUtil.isEmpty(fdto.getDpLeftHighCaseCounth())){
            fdto.setDpLeftHighCaseCountherr(true);
            errMap.put(fdto.getSvcCode()+"prLeftHighCaseCounth","CaseCounth is mandatory");
        }
    }

    public void dateVad(Map<String, String> errMap,HcsaRiskLeadershipMatrixDto fdto){
        String inEffDate = fdto.getAdEffectiveStartDate();
        String inEndDate = fdto.getAdEffectiveEndDate();
        String prEffDate = fdto.getDpEffectiveStartDate();
        String prEndDate = fdto.getDpEffectiveEndDate();
        boolean inDateFormatVad = doDateFormatVad(errMap,inEffDate,inEndDate,fdto.getSvcCode(),true,fdto);
        boolean prDateFormatVad = doDateFormatVad(errMap,prEffDate,prEndDate,fdto.getSvcCode(),false,fdto);
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
                doUsualDateVad(inEffDate,inEndDate,fdto.getSvcCode(),errMap,true,inEditNumFlag,prEditNumFlag,fdto);
                doUsualDateVad(prEffDate,prEndDate,fdto.getSvcCode(),errMap,false,inEditNumFlag,prEditNumFlag,fdto);
            } else {
                boolean inDateFlag;
                inDateFlag = doUsualDateVad(inEffDate,inEndDate,fdto.getSvcCode(),errMap,true,inEditNumFlag,prEditNumFlag,fdto);
                if(inDateFlag){
                    doSpecialDateFlag(errMap,fdto,true);
                }
                inDateFlag = doUsualDateVad(prEffDate,prEndDate,fdto.getSvcCode(),errMap,false,inEditNumFlag,prEditNumFlag,fdto);
                if(inDateFlag){
                    doSpecialDateFlag(errMap,fdto,false);
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
            if(inEffDate.getTime()<baseInEffDate.getTime()&&isIn){
                errMap.put(fdto.getSvcCode() + "inEffDate", "EffectiveDate should later than Previous version");
                fdto.setAdEffectiveStartDateerr(true);
            }
            if(prEffDate.getTime()<basePrEffDate.getTime()&&!isIn){
                errMap.put(fdto.getSvcCode() + "prEffDate", "EffectiveDate should later than Previous version");
                fdto.setDpEffectiveStartDateerr(true);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean doUsualDateVad(Date effDate,Date endDate,String serviceCode,Map<String, String> errMap,boolean isIn,int inEdit,int prEdit,HcsaRiskLeadershipMatrixDto fdto){
        boolean flag = true;
        if (effDate.getTime() < System.currentTimeMillis()) {
            flag = false;
            if(isIn){
                if(inEdit == 1){
                    errMap.put(serviceCode + "inEffDate", "EffectiveDate should be furture time");
                    fdto.setAdEffectiveStartDateerr(true);
                }
            }else {
                if(prEdit == 1){
                    errMap.put(serviceCode + "prEffDate", "EffectiveDate should be furture time");
                    fdto.setDpEffectiveEndDateerr(true);
                }
            }
        } else if (endDate.getTime() < effDate.getTime()) {
            flag = false;
            if(isIn){
                if(inEdit == 1){
                    errMap.put(serviceCode + "inEndDate", "EffectiveDate should be ealier than EndDate");
                    fdto.setAdEffectiveEndDateerr(true);
                }
            }else{
                if(prEdit == 1){
                    errMap.put(serviceCode + "prEndDate", "EffectiveDate should be ealier than EndDate");
                    fdto.setDpEffectiveEndDateerr(true);
                }
            }
        }
        return flag;
    }
    public boolean doDateFormatVad(Map<String, String> errMap,String strEffDate,String strEndDate,String serviceCode,boolean isIn,HcsaRiskLeadershipMatrixDto fdto){
        boolean vadFlag = true;
        if(StringUtil.isEmpty(strEffDate)){
            vadFlag = false;
            if(isIn){
                errMap.put(serviceCode+"inEffDate","EffectiveDate is mandatory");
                fdto.setAdEffectiveStartDateerr(true);
            }else{
                errMap.put(serviceCode+"prEffDate","EffectiveDate is mandatory");
                fdto.setDpEffectiveStartDateerr(true);
            }
        }else{
            try {
                Formatter.parseDate(strEffDate);
            }catch (Exception e){
                if(isIn){
                    errMap.put(serviceCode+"inEffDate","Date Format Error");
                    fdto.setAdEffectiveStartDateerr(true);
                }else{
                    errMap.put(serviceCode+"prEffDate","Date Format Error");
                    fdto.setDpEffectiveStartDateerr(true);
                }
                vadFlag = false;
            }
        }
        if(StringUtil.isEmpty(strEndDate)){
            vadFlag = false;
            if(isIn){
                errMap.put(serviceCode+"inEndDate","EndDate is mandatory");
                fdto.setAdEffectiveEndDateerr(true);
            }else{
                errMap.put(serviceCode+"prEndDate","EndDate is mandatory");
                fdto.setDpEffectiveEndDateerr(true);
            }
        }else{
            try {
                Formatter.parseDate(strEndDate);
            }catch (Exception e){
                vadFlag = false;
                if(isIn){
                    errMap.put(serviceCode+"inEndDate","Date Format Error");
                    fdto.setAdEffectiveEndDateerr(true);
                }else{
                    errMap.put(serviceCode+"prEndDate","Date Format Error");
                    fdto.setDpEffectiveEndDateerr(true);
                }
                return false;
            }
        }
        return vadFlag;
    }

}
