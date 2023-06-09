/*
 *   This file is generated by ECQ project skeleton automatically.
 *
 *   Copyright 2019-2049, Ecquaria Technologies Pte Ltd. All rights reserved.
 *
 *   No part of this material may be copied, reproduced, transmitted,
 *   stored in a retrieval system, reverse engineered, decompiled,
 *   disassembled, localised, ported, adapted, varied, modified, reused,
 *   customised or translated into any language in any form or by any means,
 *   electronic, mechanical, photocopying, recording or otherwise,
 *   without the prior written permission of Ecquaria Technologies Pte Ltd.
 */

package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.constant.risk.RiskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskFinanceMatrixDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskFinancialShowDto;
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
 * HcsaFinancialRiskValidate
 *
 * @author jiahao
 * @date 20/11/2019
 */
@Slf4j
public class HcsaFinancialRiskValidate implements CustomizeValidator {

    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        RiskFinancialShowDto findto = (RiskFinancialShowDto) ParamUtil.getSessionAttr(request, RiskConsts.FINANCIALSHOWDTO);
        List<HcsaRiskFinanceMatrixDto> financeList = findto.getFinanceList();
        List<HcsaRiskFinanceMatrixDto> editList = IaisCommonUtils.genNewArrayList();
        for(HcsaRiskFinanceMatrixDto dto :financeList){
            if(dto.isPrIsEdit()||dto.isInIsEdit()){
                editList.add(dto);
            }
        }
        if(editList!=null &&!editList.isEmpty()){
            for(HcsaRiskFinanceMatrixDto fdto :editList){
                if(fdto.isInIsEdit()&&fdto.isPrIsEdit()||!StringUtil.isEmpty(fdto.getId())){
                    therholdVad(errMap,fdto);
                    caseCounthVad(errMap,fdto);
                    dateVad(errMap,fdto);
                }else{
                    //one serviceCode need both Edit pr and in
                    errMap.put(fdto.getServiceCode()+"both","The category of Institution and Practitioner should be update at the same time");
                }

            }
        }else{
            errMap.put("All","RSM_ERR001");
        }
        mergeList(editList,financeList);
        WebValidationHelper.saveAuditTrailForNoUseResult(errMap);
        return errMap;
    }
    public void mergeList( List<HcsaRiskFinanceMatrixDto> editList,List<HcsaRiskFinanceMatrixDto> financeList){
        if(editList!=null &&financeList!=null){
            for(HcsaRiskFinanceMatrixDto fin:financeList){
                for(HcsaRiskFinanceMatrixDto ed:editList){
                    if(ed.getServiceCode().equals(fin.getServiceCode())){
                        fin = ed;
                    }
                }
            }
        }
    }
    public void therholdVad(Map<String, String> errMap,HcsaRiskFinanceMatrixDto fdto){
        String serviceCode = fdto.getServiceCode();
        String messForEmpty = MessageUtil.replaceMessage("GENERAL_ERR0006","Threshold","field");
        if(StringUtil.isEmpty(fdto.getInThershold())){
            errMap.put(serviceCode+"inThershold",messForEmpty);
            fdto.setInThersholderr(true);
        }else{
            try {
                Integer thold = Integer.valueOf(fdto.getInThershold());
                if(thold<0 || thold>HcsaLicenceBeConstant.RISK_Validate_HIGH_MAX){
                    errMap.put(serviceCode+"inThershold","GENERAL_ERR0027");
                    fdto.setInThersholderr(true);
                }
            }catch (Exception e){
                errMap.put(serviceCode +"inThershold","GENERAL_ERR0027");
                fdto.setInThersholderr(true);
            }
        }
        if(StringUtil.isEmpty(fdto.getPrThershold())){
            errMap.put(serviceCode+"prThershold",messForEmpty);
            fdto.setPrThersholderr(true);
        }else{
            try {
                Integer thold = Integer.valueOf(fdto.getPrThershold());
                if(thold<0 || thold>HcsaLicenceBeConstant.RISK_Validate_HIGH_MAX){
                    errMap.put(serviceCode+"prThershold","GENERAL_ERR0027");
                    fdto.setPrThersholderr(true);
                }
            }catch (Exception e){
                errMap.put(serviceCode+"prThershold","GENERAL_ERR0027");
                fdto.setPrThersholderr(true);
            }
        }
    }

    public void caseCounthVad(Map<String, String> errMap,HcsaRiskFinanceMatrixDto fdto){
        mandatoryCaseCounthVad(errMap,fdto);
        numberCaseCounthVad(errMap,fdto);
    }
    public void numberCaseCounthVad(Map<String, String> errMap,HcsaRiskFinanceMatrixDto fdto){
        inLeftModVadAndinRightModVad(errMap,fdto.getInLeftModCaseCounth(),fdto.getInRightModCaseCounth(),fdto.getServiceCode(),true,fdto);
        inLeftModVadAndinRightModVad(errMap,fdto.getPrLeftModCaseCounth(),fdto.getPrRightModCaseCounth(),fdto.getServiceCode(),false,fdto);
        inRightLowVad(errMap,fdto.getInRightLowCaseCounth(),fdto.getInLeftModCaseCounth(),fdto.getServiceCode(),true,fdto);
        inRightLowVad(errMap,fdto.getPrRightLowCaseCounth(),fdto.getPrLeftModCaseCounth(),fdto.getServiceCode(),false,fdto);
        inLeftHighVad(errMap,fdto.getInLeftHighCaseCount(),fdto.getInRightModCaseCounth(),fdto.getServiceCode(),true,fdto);
        inLeftHighVad(errMap,fdto.getPrLeftHighCaseCount(),fdto.getPrRightModCaseCounth(),fdto.getServiceCode(),false,fdto);

    }
    public void inLeftModVadAndinRightModVad(Map<String, String> errMap,String inLeftMod,String inRightMod,String serviceCode,boolean isIn,HcsaRiskFinanceMatrixDto fdto){
        Integer inLeftModNum = 0;
        Integer inRightModNum = 0;
        String mess = MessageUtil.getMessageDesc("GENERAL_ERR0027");
        String maxMes =  mess+ HcsaLicenceBeConstant.ERROR_MESSAGE_MAX_CASES;
        String minMes = mess + HcsaLicenceBeConstant.ERROR_MESSAGE_MIN_CASES;
        int numberFlag = 0;
        if(!StringUtil.isEmpty(inLeftMod)){
            try {
                inLeftModNum = Integer.valueOf(inLeftMod);
                if(inLeftModNum<=0||inLeftModNum>HcsaLicenceBeConstant.RISK_Validate_HIGH_MAX){
                    if(isIn){
                        errMap.put(serviceCode+"inLeftModCaseCounth", minMes);
                        fdto.setInLeftModCaseCountherr(true);
                    }else{
                        errMap.put(serviceCode+"prLeftModCaseCounth",minMes);
                        fdto.setPrLeftModCaseCountherr(true);
                    }
                }
                numberFlag++;
            }catch (Exception e){
                if(isIn){
                    errMap.put(serviceCode+"inLeftModCaseCounth",minMes);
                    fdto.setInLeftModCaseCountherr(true);
                }else{
                    errMap.put(serviceCode+"prLeftModCaseCounth",minMes);
                    fdto.setPrLeftModCaseCountherr(true);
                }
                log.error(e.getMessage(), e);
            }
        }
        if(!StringUtil.isEmpty(inRightMod)){
            try {
                inRightModNum = Integer.valueOf(inRightMod);
                if(inRightModNum<=0 || inRightModNum >HcsaLicenceBeConstant.RISK_Validate_HIGH_MAX){
                    if(isIn){
                        errMap.put(serviceCode+"inRightModCaseCounth",maxMes);
                        fdto.setInRightModCaseCountherr(true);
                    }else{
                        errMap.put(serviceCode+"prRightModCaseCounth",maxMes);
                        fdto.setPrRightModCaseCountherr(true);
                    }
                }
                numberFlag++;
            }catch (Exception e){
                if(isIn){
                    errMap.put(serviceCode+"inRightModCaseCounth",maxMes);
                    fdto.setInRightModCaseCountherr(true);
                }else{
                    errMap.put(serviceCode+"prRightModCaseCounth",maxMes);
                    fdto.setPrRightModCaseCountherr(true);
                }
                log.error(e.getMessage(), e);
            }
        }
        if(numberFlag == 2){
            numberOrderVad(errMap,inLeftModNum,inRightModNum,serviceCode,isIn,fdto);
        }
    }
    public void numberOrderVad(Map<String, String> errMap,Integer lm,Integer rm,String serviceCode,boolean isIn,HcsaRiskFinanceMatrixDto fdto){
        if(lm>rm&&isIn){
            errMap.put(serviceCode+"inRightModCaseCounth","RSM_ERR011");
            fdto.setInRightModCaseCountherr(true);
            fdto.setInLeftModCaseCountherr(true);
        }else if(lm>rm&&!isIn){
            errMap.put(serviceCode+"prRightModCaseCounth","RSM_ERR011");
            fdto.setPrRightModCaseCountherr(true);
            fdto.setPrLeftModCaseCountherr(true);
        }
    }
    public void inLeftHighVad(Map<String, String> errMap,String inLeftHigh,String inRightMod, String serviceCode,boolean isIn,HcsaRiskFinanceMatrixDto fdto){
        String minMes = MessageUtil.getMessageDesc("GENERAL_ERR0027") + HcsaLicenceBeConstant.ERROR_MESSAGE_MIN_CASES;
        Integer inLeftHighNum = 0;
        boolean inLeftHighNumFlag = true;
        boolean prLeftHighNumFlag = true;
        Integer inRightModNum = 0;
        try {
            if(!StringUtil.isEmpty(inLeftHigh)){
                inLeftHighNum = Integer.valueOf(inLeftHigh);
                if (inLeftHighNum > HcsaLicenceBeConstant.RISK_Validate_HIGH_MAX || inLeftHighNum < 0) {
                    if(isIn){
                        inLeftHighNumFlag = false;
                        errMap.put(serviceCode + "inLeftHighCaseCounth", minMes);
                        fdto.setInLeftHighCaseCounterr(true);
                    }else{
                        prLeftHighNumFlag = false;
                        errMap.put(serviceCode + "prLeftHighCaseCounth", minMes);
                        fdto.setPrLeftHighCaseCounterr(true);
                    }
                }
            }
        } catch (Exception e) {
            if(isIn){
                inLeftHighNumFlag = false;
                errMap.put(serviceCode + "inLeftHighCaseCounth", minMes);
                fdto.setInLeftHighCaseCounterr(true);
            }else{
                prLeftHighNumFlag = false;
                errMap.put(serviceCode + "prLeftHighCaseCounth", minMes);
                fdto.setPrLeftHighCaseCounterr(true);
            }
            log.error(e.getMessage(), e);
        }
        try {
            if(!StringUtil.isEmpty(inRightMod)){
                inRightModNum = Integer.valueOf(inRightMod);
                if(inRightModNum +1 != inLeftHighNum){
                    if(isIn&&inLeftHighNumFlag){
                        errMap.put(serviceCode + "inLeftHighCaseCounth", "RSM_ERR014");
                        fdto.setInLeftHighCaseCounterr(true);
                    }else if(!isIn&&prLeftHighNumFlag){
                        errMap.put(serviceCode + "prLeftHighCaseCounth", "RSM_ERR014");
                        fdto.setPrLeftHighCaseCounterr(true);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }
    public void inRightLowVad(Map<String, String> errMap,String inRightLow,String inLeftMod,String serviceCode,boolean isIn,HcsaRiskFinanceMatrixDto fdto){
        String maxMes = MessageUtil.getMessageDesc("GENERAL_ERR0027") + HcsaLicenceBeConstant.ERROR_MESSAGE_MAX_CASES;
        Integer inRightLowNum = 0;
        boolean inRightLowNumFlag = true;
        boolean prRightLowNumFlag = true;
        try {
            if(!StringUtil.isEmpty(inRightLow)){
                inRightLowNum = Integer.valueOf(inRightLow);
                if (inRightLowNum > HcsaLicenceBeConstant.RISK_Validate_HIGH_MAX || inRightLowNum < 0) {
                    if(isIn){
                        inRightLowNumFlag = false;
                        errMap.put(serviceCode + "inRightLowCaseCounth", maxMes);
                        fdto.setInRightLowCaseCountherr(true);
                    }else{
                        prRightLowNumFlag = false;
                        errMap.put(serviceCode + "prRightLowCaseCounth", maxMes);
                        fdto.setPrRightLowCaseCountherr(true);
                    }
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            if(isIn){
                inRightLowNumFlag = false;
                errMap.put(serviceCode + "inRightLowCaseCounth",  maxMes);
                fdto.setInRightLowCaseCountherr(true);
            }else{
                prRightLowNumFlag = false;
                errMap.put(serviceCode + "prRightLowCaseCounth",  maxMes);
                fdto.setPrRightLowCaseCountherr(true);
            }

            log.error(e.getMessage(), e);
        }
        try {
            if(!StringUtil.isEmpty(inLeftMod)){
                int inLeftModNum = Integer.parseInt(inLeftMod);
                if(inLeftModNum -1 != inRightLowNum && inLeftModNum != inRightLowNum){
                    if(isIn){
                        if(inRightLowNumFlag){
                            errMap.put(serviceCode + "inRightLowCaseCounth", "RSM_ERR015");
                            fdto.setInRightLowCaseCountherr(true);
                        }
                    }else{
                        if(prRightLowNumFlag){
                            errMap.put(serviceCode + "prRightLowCaseCounth", "RSM_ERR015");
                            fdto.setPrRightLowCaseCountherr(true);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    public void mandatoryCaseCounthVad(Map<String, String> errMap,HcsaRiskFinanceMatrixDto fdto){
        //in
        String serviceCode = fdto.getServiceCode();
        String minNCNoEmpty = MessageUtil.replaceMessage("GENERAL_ERR0006",HcsaLicenceBeConstant.ERROR_MESSAGE_MIN_CASES_NO_SPACE,"field");
        String maxNCNoEmpty = MessageUtil.replaceMessage("GENERAL_ERR0006",HcsaLicenceBeConstant.ERROR_MESSAGE_MAX_CASES_NO_SPACE,"field");
        if(StringUtil.isEmpty(fdto.getInLeftModCaseCounth())){
            errMap.put(serviceCode+"inLeftModCaseCounth",minNCNoEmpty);
            fdto.setInLeftModCaseCountherr(true);
        }
        if(StringUtil.isEmpty(fdto.getInRightModCaseCounth())){
            errMap.put(serviceCode+"inRightModCaseCounth",maxNCNoEmpty);
            fdto.setInRightModCaseCountherr(true);
        }
        if(StringUtil.isEmpty(fdto.getInRightLowCaseCounth())){
            errMap.put(serviceCode+"inRightLowCaseCounth",maxNCNoEmpty);
            fdto.setInRightLowCaseCountherr(true);
        }
        if(StringUtil.isEmpty(fdto.getInLeftHighCaseCount())){
            errMap.put(serviceCode+"inLeftHighCaseCounth",minNCNoEmpty);
            fdto.setInLeftHighCaseCounterr(true);
        }
        //pr
        if(StringUtil.isEmpty(fdto.getPrLeftModCaseCounth())){
            errMap.put(serviceCode+"prLeftModCaseCounth",minNCNoEmpty);
            fdto.setPrLeftModCaseCountherr(true);
        }
        if(StringUtil.isEmpty(fdto.getPrRightModCaseCounth())){
            errMap.put(serviceCode+"prRightModCaseCounth",maxNCNoEmpty);
            fdto.setPrRightModCaseCountherr(true);
        }
        if(StringUtil.isEmpty(fdto.getPrRightLowCaseCounth())){
            errMap.put(serviceCode+"prRightLowCaseCounth",maxNCNoEmpty);
            fdto.setPrRightLowCaseCountherr(true);
        }
        if(StringUtil.isEmpty(fdto.getPrLeftHighCaseCount())){
            errMap.put(serviceCode+"prLeftHighCaseCounth",minNCNoEmpty);
            fdto.setPrLeftHighCaseCounterr(true);
        }
    }

    public void dateVad(Map<String, String> errMap,HcsaRiskFinanceMatrixDto fdto){
        String serviceCode = fdto.getServiceCode();
        String inEffDate = fdto.getInEffectiveStartDate();
        String inEndDate = fdto.getInEffectiveEndDate();
        String prEffDate = fdto.getPrEffectiveStartDate();
        String prEndDate = fdto.getPrEffectiveEndDate();
        boolean inDateFormatVad = doDateFormatVad(errMap,inEffDate,inEndDate,serviceCode,true,fdto);
        boolean prDateFormatVad = doDateFormatVad(errMap,prEffDate,prEndDate,serviceCode,false,fdto);
        if(inDateFormatVad&&fdto.isInIsEdit()){
            doDateLogicVad(errMap,fdto,true);
        }
        if(prDateFormatVad&&fdto.isPrIsEdit()){
            doDateLogicVad(errMap,fdto,false);
        }

    }
    public void doDateLogicVad(Map<String, String> errMap,HcsaRiskFinanceMatrixDto fdto,boolean isIn){
        //effdate least version <
        String serviceCode = fdto.getServiceCode();
        try {
            Date inEffDate = Formatter.parseDate(fdto.getInEffectiveStartDate());
            Date inEndDate = Formatter.parseDate(fdto.getInEffectiveEndDate());
            Date prEffDate = Formatter.parseDate(fdto.getPrEffectiveStartDate());
            Date prEndDate = Formatter.parseDate(fdto.getPrEffectiveEndDate());
            int inEditNumFlag = 0;
            int prEditNumFlag = 0;
            if(fdto.isInIsEdit()){
                inEditNumFlag = 1;
            }
            if(fdto.isPrIsEdit()){
                prEditNumFlag = 1;
            }
            if (StringUtil.isEmpty(fdto.getId())) {
                doUsualDateVad(inEffDate,inEndDate,serviceCode,errMap,true,inEditNumFlag,prEditNumFlag,fdto);
                doUsualDateVad(prEffDate,prEndDate,serviceCode,errMap,false,inEditNumFlag,prEditNumFlag,fdto);
            } else {
                boolean inDateFlag;
                inDateFlag = doUsualDateVad(inEffDate,inEndDate,serviceCode,errMap,true,inEditNumFlag,prEditNumFlag,fdto);
                if(inDateFlag){
                    doSpecialDateFlag(errMap,fdto,true);
                }
                inDateFlag = doUsualDateVad(prEffDate,prEndDate,serviceCode,errMap,false,inEditNumFlag,prEditNumFlag,fdto);
                if(inDateFlag){
                    doSpecialDateFlag(errMap,fdto,false);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
    public void doSpecialDateFlag(Map<String, String> errMap,HcsaRiskFinanceMatrixDto fdto,boolean isIn){
        String serviceCode = fdto.getServiceCode();
        try {
            Date inEffDate = Formatter.parseDate(fdto.getInEffectiveStartDate());
           // Date inEndDate = Formatter.parseDate(fdto.getInEffectiveEndDate());
            Date prEffDate = Formatter.parseDate(fdto.getPrEffectiveStartDate());
          //  Date prEndDate = Formatter.parseDate(fdto.getPrEffectiveEndDate());
            Date baseInEffDate = Formatter.parseDate(fdto.getBaseInEffectiveStartDate());
          //  Date baseInEndDate = Formatter.parseDate(fdto.getBaseInEffectiveEndDate());
            Date basePrEffDate = Formatter.parseDate(fdto.getBasePrEffectiveStartDate());
          //  Date basePrEndDate = Formatter.parseDate(fdto.getBasePrEffectiveEndDate());
            if(inEffDate.getTime()<baseInEffDate.getTime()&&isIn){
                errMap.put(serviceCode + "inEffDate", "RSM_ERR018");
                fdto.setInEffectiveStartDateerr(true);
            }
            if(prEffDate.getTime()<basePrEffDate.getTime()&&!isIn){
                errMap.put(serviceCode + "prEffDate", "RSM_ERR018");
                fdto.setPrEffectiveStartDateerr(true);
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }

    public boolean doUsualDateVad(Date effDate,Date endDate,String serviceCode,Map<String, String> errMap,boolean isIn,int inEdit,int prEdit,HcsaRiskFinanceMatrixDto fdto){
        boolean flag = true;
        String mesStartMes = MessageUtil.replaceMessage("RSM_ERR012","Effective Start Date","Effective Date");
        if (effDate.getTime() < System.currentTimeMillis()) {
            flag = false;
            if(isIn){
                if(inEdit == 1){
                    errMap.put(serviceCode + "inEffDate",mesStartMes);
                    fdto.setInEffectiveStartDateerr(true);
                }
            }else {
                if(prEdit == 1){
                    errMap.put(serviceCode + "prEffDate", mesStartMes);
                    fdto.setPrEffectiveStartDateerr(true);
                }
            }
        } else if (endDate.getTime() < effDate.getTime()) {
            flag = false;
            if(isIn){
                if(inEdit == 1){
                    errMap.put(serviceCode + "inEndDate", "RSM_ERR017");
                    fdto.setInEffectiveEndDateerr(true);
                }
            }else{
                if(prEdit == 1){
                    errMap.put(serviceCode + "prEndDate", "RSM_ERR017");
                    fdto.setPrEffectiveEndDateerr(true);
                }
            }
        }
        return flag;
    }
    public boolean doDateFormatVad(Map<String, String> errMap,String strEffDate,String strEndDate,String serviceCode,boolean isIn,HcsaRiskFinanceMatrixDto fdto){
        boolean vadFlag = true;
        String mesStartEmpty  = MessageUtil.replaceMessage("GENERAL_ERR0006","Effective Start Date","field");
        String mesStartRepalce  = MessageUtil.replaceMessage("RSM_ERR016","Effective Start Date","replaceArea");
        String mesEndEmpty  = MessageUtil.replaceMessage("GENERAL_ERR0006","Effective End Date","field");
        String mesEndRepalce  = MessageUtil.replaceMessage("RSM_ERR016","Effective End Date","replaceArea");
        if(StringUtil.isEmpty(strEffDate)){
            vadFlag = false;
            if(isIn){
                errMap.put(serviceCode+"inEffDate",mesStartEmpty);
                fdto.setInEffectiveStartDateerr(true);
            }else{
                errMap.put(serviceCode+"prEffDate",mesStartEmpty);
                fdto.setPrEffectiveStartDateerr(true);
            }
        }else{
           try {
               Formatter.parseDate(strEffDate);
           }catch (Exception e){
               if(isIn){
                   errMap.put(serviceCode+"inEffDate",mesStartRepalce);
                   fdto.setInEffectiveStartDateerr(true);
               }else{
                   errMap.put(serviceCode+"prEffDate",mesStartRepalce);
                   fdto.setPrEffectiveStartDateerr(true);
               }
               vadFlag = false;
           }
        }
        if(StringUtil.isEmpty(strEndDate)){
            vadFlag = false;
            if(isIn){
                errMap.put(serviceCode+"inEndDate",mesEndEmpty);
                fdto.setInEffectiveEndDateerr(true);
            }else{
                errMap.put(serviceCode+"prEndDate",mesEndEmpty);
                fdto.setPrEffectiveEndDateerr(true);
            }
        }else{
            try {
                Formatter.parseDate(strEndDate);
            }catch (Exception e){
                if(isIn){
                    errMap.put(serviceCode+"inEndDate",mesEndRepalce);
                    fdto.setInEffectiveEndDateerr(true);
                }else{
                    errMap.put(serviceCode+"prEndDate",mesEndRepalce);
                    fdto.setPrEffectiveEndDateerr(true);
                }
                return false;
            }
        }
        return vadFlag;
    }

}
