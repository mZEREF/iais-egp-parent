package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskLegislativeMatrixDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskLegislativeShowDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceBeConstant;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: jiahao
 * @Date: 2019/12/25 13:27
 */
@Slf4j
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
            errMap.put("All","RSM_ERR001");
        }
        mergeList(editList,financeList);
        WebValidationHelper.saveAuditTrailForNoUseResult(errMap);
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
            log.error(e.getMessage(), e);
        }
    }

    public void doSpecialDateFlag(Map<String, String> errMap,HcsaRiskLegislativeMatrixDto fdto){
        try {
            Date inEffDate = Formatter.parseDate(fdto.getDoEffectiveDate());
            Date inEndDate = Formatter.parseDate(fdto.getDoEndDate());
            Date baseInEffDate = Formatter.parseDate(fdto.getBaseEffectiveDate());
            Date baseInEndDate = Formatter.parseDate(fdto.getBaseEndDate());
            if(inEffDate.getTime()<baseInEffDate.getTime()){
                errMap.put(fdto.getSvcCode() + "inEffDate", "RSM_ERR018");
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

    public void therholdVad(Map<String, String> errMap,HcsaRiskLegislativeMatrixDto fdto){
        String serviceCode = fdto.getSvcCode();
        if(StringUtil.isEmpty(fdto.getDoThershold())){
            errMap.put(serviceCode+"inThershold",MessageUtil.replaceMessage("GENERAL_ERR0006","Thershold","field"));
        }else{
            try {
                Integer thold = Integer.valueOf(fdto.getDoThershold());
                if(thold<0 || thold> HcsaLicenceBeConstant.RISK_Validate_HIGH_MAX){
                    errMap.put(serviceCode+"inThershold","GENERAL_ERR0027");
                }
            }catch (Exception e){
                errMap.put(serviceCode+"inThershold","GENERAL_ERR0027");
            }
        }
    }

    public void caseCounthVad(Map<String, String> errMap,HcsaRiskLegislativeMatrixDto fdto){
        mandatoryCaseCounthVad(errMap,fdto);
        numberCaseCounthVad(errMap,fdto);
    }
    public void numberCaseCounthVad(Map<String, String> errMap,HcsaRiskLegislativeMatrixDto fdto){
        String serviceCode = fdto.getSvcCode();
        inLeftModVadAndinRightModVad(errMap,fdto.getDoLeftModCaseCounth(),fdto.getDoRightModCaseCounth(),serviceCode,fdto);
        inRightLowVad(errMap,fdto.getDoRightLowCaseCounth(),fdto.getDoLeftModCaseCounth(),serviceCode,fdto);
        inLeftHighVad(errMap,fdto.getDoLeftHighCaseCounth(),fdto.getDoRightModCaseCounth(),serviceCode,fdto);

    }
    public void inLeftHighVad(Map<String, String> errMap,String inLeftHigh,String inRightMod, String serviceCode,HcsaRiskLegislativeMatrixDto fdto){
        String GENERAL_ERR0027Min = MessageUtil.getMessageDesc("GENERAL_ERR0027") + HcsaLicenceBeConstant.ERROR_MESSAGE_MIN_CASES;
        Integer inLeftHighNum = 0;
        boolean inLeftHighNumFlag = true;
        Integer inRightModNum = 0;
        try {
            if(!StringUtil.isEmpty(inLeftHigh)){
                inLeftHighNum = Integer.valueOf(inLeftHigh);
                if (inLeftHighNum > HcsaLicenceBeConstant.RISK_Validate_HIGH_MAX || inLeftHighNum < 0) {
                    inLeftHighNumFlag = false;
                    errMap.put(serviceCode + "inLeftHighCaseCounth", GENERAL_ERR0027Min);
                    fdto.setDoLeftHighCaseCountherr(true);
                }
            }
        } catch (Exception e) {
            inLeftHighNumFlag = false;
            errMap.put(serviceCode + "inLeftHighCaseCounth", GENERAL_ERR0027Min);
            fdto.setDoLeftHighCaseCountherr(true);
            log.error(e.getMessage(), e);
        }
        try {
            if(!StringUtil.isEmpty(inRightMod)){
                inRightModNum = Integer.valueOf(inRightMod);
                if((inRightModNum +1 != inLeftHighNum)&&inLeftHighNumFlag){
                    errMap.put(serviceCode + "inLeftHighCaseCounth", "RSM_ERR014");
                    fdto.setDoLeftHighCaseCountherr(true);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }
    public void inRightLowVad(Map<String, String> errMap,String inRightLow,String inLeftMod,String serviceCode,HcsaRiskLegislativeMatrixDto fdto){
        String GENERAL_ERR0027Max = MessageUtil.getMessageDesc("GENERAL_ERR0027") + HcsaLicenceBeConstant.ERROR_MESSAGE_MAX_CASES;
        Integer inRightLowNum = 0;
        boolean inrightflag = true;
        Integer inLeftModNum = 0;
        try {
            if(!StringUtil.isEmpty(inRightLow)){
                inRightLowNum = Integer.valueOf(inRightLow);
                if (inRightLowNum > HcsaLicenceBeConstant.RISK_Validate_HIGH_MAX || inRightLowNum < 0) {
                    inrightflag = false;
                    errMap.put(serviceCode + "inRightLowCaseCounth", GENERAL_ERR0027Max);
                    fdto.setDoRightModCaseCountherr(true);
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
                inrightflag = false;
                errMap.put(serviceCode + "inRightLowCaseCounth", GENERAL_ERR0027Max);
                fdto.setDoRightLowCaseCountherr(true);
            log.error(e.getMessage(), e);
        }
        try {
            if(!StringUtil.isEmpty(inLeftMod)&&inrightflag){
                inLeftModNum = Integer.valueOf(inLeftMod);
                if(inLeftModNum -1 != inRightLowNum && !inLeftModNum.equals(inRightLowNum)){
                    errMap.put(serviceCode + "inRightLowCaseCounth", "RSM_ERR015");
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    public void inLeftModVadAndinRightModVad(Map<String, String> errMap,String inLeftMod,String inRightMod,String serviceCode,HcsaRiskLegislativeMatrixDto fdto){
        String mess = MessageUtil.getMessageDesc("GENERAL_ERR0027");
        String GENERAL_ERR0027Max =  mess + HcsaLicenceBeConstant.ERROR_MESSAGE_MAX_CASES;
        String GENERAL_ERR0027Min =  mess + HcsaLicenceBeConstant.ERROR_MESSAGE_MIN_CASES;
        Integer inLeftModNum = 0;
        Integer inRightModNum = 0;
        int numberFlag = 0;
        if(!StringUtil.isEmpty(inLeftMod)){
            try {
                inLeftModNum = Integer.valueOf(inLeftMod);
                if(inLeftModNum<= 0||inLeftModNum>HcsaLicenceBeConstant.RISK_Validate_HIGH_MAX){
                        errMap.put(serviceCode+"inLeftModCaseCounth",GENERAL_ERR0027Min);
                        fdto.setDoLeftModCaseCountherr(true);
                }
                numberFlag++;
            }catch (Exception e){
                    errMap.put(serviceCode+"inLeftModCaseCounth",GENERAL_ERR0027Min);
                    fdto.setDoLeftModCaseCountherr(true);
                log.error(e.getMessage(), e);
            }
        }
        if(!StringUtil.isEmpty(inRightMod)){
            try {
                inRightModNum = Integer.valueOf(inRightMod);
                if(inRightModNum<=0 || inRightModNum >HcsaLicenceBeConstant.RISK_Validate_HIGH_MAX){
                    errMap.put(serviceCode+"inRightModCaseCounth",GENERAL_ERR0027Max);
                    fdto.setDoRightModCaseCountherr(true);
                }
                numberFlag++;
            }catch (Exception e){
                errMap.put(serviceCode+"inRightModCaseCounth",GENERAL_ERR0027Max);
                fdto.setDoRightModCaseCountherr(true);
                log.error(e.getMessage(), e);
            }
        }
        if(numberFlag == 2){
            numberOrderVad(errMap,inLeftModNum,inRightModNum,serviceCode,fdto);
        }
    }
    public void numberOrderVad(Map<String, String> errMap,Integer lm,Integer rm,String serviceCode,HcsaRiskLegislativeMatrixDto fdto){
        if(lm>rm){
            errMap.put(serviceCode+"inRightModCaseCounth","RSM_ERR011");
            fdto.setDoLeftModCaseCountherr(true);
            fdto.setDoRightModCaseCountherr(true);
        }
    }
    public void mandatoryCaseCounthVad(Map<String, String> errMap,HcsaRiskLegislativeMatrixDto fdto){
        String serviceCode = fdto.getSvcCode();
        String GENERAL_ERR0006MaxCase = MessageUtil.replaceMessage("GENERAL_ERR0006",HcsaLicenceBeConstant.ERROR_MESSAGE_MAX_CASES_NO_SPACE,"field");
        String GENERAL_ERR0006MinCase = MessageUtil.replaceMessage("GENERAL_ERR0006",HcsaLicenceBeConstant.ERROR_MESSAGE_MIN_CASES_NO_SPACE,"field");
        //in
        if(StringUtil.isEmpty(fdto.getDoLeftModCaseCounth())){
            errMap.put(serviceCode+"inLeftModCaseCounth",GENERAL_ERR0006MinCase);
            fdto.setDoLeftModCaseCountherr(true);
        }
        if(StringUtil.isEmpty(fdto.getDoRightModCaseCounth())){
            errMap.put(serviceCode+"inRightModCaseCounth",GENERAL_ERR0006MaxCase);
            fdto.setDoRightModCaseCountherr(true);
        }
        if(StringUtil.isEmpty(fdto.getDoRightLowCaseCounth())){
            errMap.put(serviceCode+"inRightLowCaseCounth",GENERAL_ERR0006MaxCase);
            fdto.setDoRightLowCaseCountherr(true);
        }
        if(StringUtil.isEmpty(fdto.getDoLeftHighCaseCounth())){
            errMap.put(serviceCode+"inLeftHighCaseCounth",GENERAL_ERR0006MinCase);
            fdto.setDoLeftModCaseCountherr(true);
        }
    }
}
