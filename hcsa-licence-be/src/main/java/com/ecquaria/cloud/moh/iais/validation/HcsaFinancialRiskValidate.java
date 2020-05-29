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
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import lombok.extern.slf4j.Slf4j;

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
            errMap.put("All","Please do some change");
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
        if(StringUtil.isEmpty(fdto.getInThershold())){
            errMap.put(fdto.getServiceCode()+"inThershold",MessageUtil.replaceMessage("ERR0009","Threshold","The field"));
            fdto.setInThersholderr(true);
        }else{
            try {
                Integer thold = Integer.valueOf(fdto.getInThershold());
                if(thold<0 || thold>999){
                    errMap.put(fdto.getServiceCode()+"inThershold","ERR0013");
                    fdto.setInThersholderr(true);
                }
            }catch (Exception e){
                errMap.put(fdto.getServiceCode()+"inThershold","ERR0013");
                fdto.setInThersholderr(true);
            }
        }
        if(StringUtil.isEmpty(fdto.getPrThershold())){
            errMap.put(fdto.getServiceCode()+"prThershold",MessageUtil.replaceMessage("ERR0009","Threshold","The field"));
            fdto.setPrThersholderr(true);
        }else{
            try {
                Integer thold = Integer.valueOf(fdto.getPrThershold());
                if(thold<0 || thold>999){
                    errMap.put(fdto.getServiceCode()+"prThershold","ERR0013");
                    fdto.setPrThersholderr(true);
                }
            }catch (Exception e){
                errMap.put(fdto.getServiceCode()+"prThershold","ERR0013");
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
        int numberFlag = 0;
        if(!StringUtil.isEmpty(inLeftMod)){
            try {
                inLeftModNum = Integer.valueOf(inLeftMod);
                if(inLeftModNum<0||inLeftModNum>999){
                    if(isIn){
                        errMap.put(serviceCode+"inLeftModCaseCounth", MessageUtil.getMessageDesc("ERR0013") + "for Minimum Number of NCs");
                        fdto.setInLeftModCaseCountherr(true);
                    }else{
                        errMap.put(serviceCode+"prLeftModCaseCounth",MessageUtil.getMessageDesc("ERR0013") + "for Minimum Number of NCs");
                        fdto.setPrLeftModCaseCountherr(true);
                    }
                }
                numberFlag++;
            }catch (Exception e){
                if(isIn){
                    errMap.put(serviceCode+"inLeftModCaseCounth",MessageUtil.getMessageDesc("ERR0013") + "for Minimum Number of NCs");
                    fdto.setInLeftModCaseCountherr(true);
                }else{
                    errMap.put(serviceCode+"prLeftModCaseCounth",MessageUtil.getMessageDesc("ERR0013") + "for Minimum Number of NCs");
                    fdto.setPrLeftModCaseCountherr(true);
                }
                log.error(e.getMessage(), e);
            }
        }
        if(!StringUtil.isEmpty(inRightMod)){
            try {
                inRightModNum = Integer.valueOf(inRightMod);
                if(inRightModNum<0 || inRightModNum >99){
                    if(isIn){
                        errMap.put(serviceCode+"inRightModCaseCounth",MessageUtil.getMessageDesc("ERR0013") + "for Maximum Number of NCs");
                        fdto.setInRightModCaseCountherr(true);
                    }else{
                        errMap.put(serviceCode+"prRightModCaseCounth",MessageUtil.getMessageDesc("ERR0013") + "for Maximum Number of NCs");
                        fdto.setPrRightModCaseCountherr(true);
                    }
                }
                numberFlag++;
            }catch (Exception e){
                if(isIn){
                    errMap.put(serviceCode+"inRightModCaseCounth",MessageUtil.getMessageDesc("ERR0013") + "for Maximum Number of NCs");
                    fdto.setInRightModCaseCountherr(true);
                }else{
                    errMap.put(serviceCode+"prRightModCaseCounth",MessageUtil.getMessageDesc("ERR0013") + "for Maximum Number of NCs");
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
            errMap.put(serviceCode+"inRightModCaseCounth","ERR0011");
            fdto.setInRightModCaseCountherr(true);
            fdto.setInLeftModCaseCountherr(true);
        }else if(lm>rm&&!isIn){
            errMap.put(serviceCode+"prRightModCaseCounth","ERR0011");
            fdto.setPrRightModCaseCountherr(true);
            fdto.setPrLeftModCaseCountherr(true);
        }
    }
    public void inLeftHighVad(Map<String, String> errMap,String inLeftHigh,String inRightMod, String serviceCode,boolean isIn,HcsaRiskFinanceMatrixDto fdto){
        Integer inLeftHighNum = 0;
        boolean inLeftHighNumFlag = true;
        boolean prLeftHighNumFlag = true;
        Integer inRightModNum = 0;
        try {
            if(!StringUtil.isEmpty(inLeftHigh)){
                inLeftHighNum = Integer.valueOf(inLeftHigh);
                if (inLeftHighNum > 999 || inLeftHighNum < 0) {
                    if(isIn){
                        inLeftHighNumFlag = false;
                        errMap.put(serviceCode + "inLeftHighCaseCounth", MessageUtil.getMessageDesc("ERR0013") + "for Minimum Number of NCs");
                        fdto.setInLeftHighCaseCounterr(true);
                    }else{
                        prLeftHighNumFlag = false;
                        errMap.put(serviceCode + "prLeftHighCaseCounth", MessageUtil.getMessageDesc("ERR0013") + "for Minimum Number of NCs");
                        fdto.setPrLeftHighCaseCounterr(true);
                    }
                }
            }
        } catch (Exception e) {
            if(isIn){
                inLeftHighNumFlag = false;
                errMap.put(serviceCode + "inLeftHighCaseCounth", MessageUtil.getMessageDesc("ERR0013") + "for Minimum Number of NCs");
                fdto.setInLeftHighCaseCounterr(true);
            }else{
                prLeftHighNumFlag = false;
                errMap.put(serviceCode + "prLeftHighCaseCounth", MessageUtil.getMessageDesc("ERR0013") + "for Minimum Number of NCs");
                fdto.setPrLeftHighCaseCounterr(true);
            }
            log.error(e.getMessage(), e);
        }
        try {
            if(!StringUtil.isEmpty(inRightMod)){
                inRightModNum = Integer.valueOf(inRightMod);
                if(inRightModNum +1 != inLeftHighNum){
                    if(isIn&&inLeftHighNumFlag){
                        errMap.put(serviceCode + "inLeftHighCaseCounth", "ERR0014");
                        fdto.setInLeftHighCaseCounterr(true);
                    }else if(!isIn&&prLeftHighNumFlag){
                        errMap.put(serviceCode + "prLeftHighCaseCounth", "ERR0014");
                        fdto.setPrLeftHighCaseCounterr(true);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }
    public void inRightLowVad(Map<String, String> errMap,String inRightLow,String inLeftMod,String serviceCode,boolean isIn,HcsaRiskFinanceMatrixDto fdto){
        Integer inRightLowNum = 0;
        boolean inRightLowNumFlag = true;
        boolean prRightLowNumFlag = true;
        Integer inLeftModNum = 0;
        try {
            if(!StringUtil.isEmpty(inRightLow)){
                inRightLowNum = Integer.valueOf(inRightLow);
                if (inRightLowNum > 999 || inRightLowNum < 0) {
                    if(isIn){
                        inRightLowNumFlag = false;
                        errMap.put(serviceCode + "inRightLowCaseCounth", MessageUtil.getMessageDesc("ERR0013") + "for Maximum Number of NCs");
                        fdto.setInRightLowCaseCountherr(true);
                    }else{
                        prRightLowNumFlag = false;
                        errMap.put(serviceCode + "prRightLowCaseCounth", MessageUtil.getMessageDesc("ERR0013") + "for Maximum Number of NCs");
                        fdto.setPrRightLowCaseCountherr(true);
                    }
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            if(isIn){
                inRightLowNumFlag = false;
                errMap.put(serviceCode + "inRightLowCaseCounth",  MessageUtil.getMessageDesc("ERR0013") + "for Maximum Number of NCs");
                fdto.setInRightLowCaseCountherr(true);
            }else{
                prRightLowNumFlag = false;
                errMap.put(serviceCode + "prRightLowCaseCounth",  MessageUtil.getMessageDesc("ERR0013") + "for Maximum Number of NCs");
                fdto.setPrRightLowCaseCountherr(true);
            }

            log.error(e.getMessage(), e);
        }
        try {
            if(!StringUtil.isEmpty(inLeftMod)){
                inLeftModNum = Integer.valueOf(inLeftMod);
                if(inLeftModNum -1 != inRightLowNum && inLeftModNum != inRightLowNum){
                    if(isIn){
                        if(inRightLowNumFlag){
                            errMap.put(serviceCode + "inRightLowCaseCounth", "ERR0015");
                            fdto.setInRightLowCaseCountherr(true);
                        }
                    }else{
                        if(prRightLowNumFlag){
                            errMap.put(serviceCode + "prRightLowCaseCounth", "ERR0015");
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
        if(StringUtil.isEmpty(fdto.getInLeftModCaseCounth())){
            errMap.put(fdto.getServiceCode()+"inLeftModCaseCounth",MessageUtil.replaceMessage("ERR0009","Minimum Number of NCs","The field"));
            fdto.setInLeftModCaseCountherr(true);
        }
        if(StringUtil.isEmpty(fdto.getInRightModCaseCounth())){
            errMap.put(fdto.getServiceCode()+"inRightModCaseCounth",MessageUtil.replaceMessage("ERR0009","Maximum Number of NCs","The field"));
            fdto.setInRightModCaseCountherr(true);
        }
        if(StringUtil.isEmpty(fdto.getInRightLowCaseCounth())){
            errMap.put(fdto.getServiceCode()+"inRightLowCaseCounth",MessageUtil.replaceMessage("ERR0009","Maximum Number of NCs","The field"));
            fdto.setInRightLowCaseCountherr(true);
        }
        if(StringUtil.isEmpty(fdto.getInLeftHighCaseCount())){
            errMap.put(fdto.getServiceCode()+"inLeftHighCaseCounth",MessageUtil.replaceMessage("ERR0009","Minimum Number of NCs","The field"));
            fdto.setInLeftHighCaseCounterr(true);
        }
        //pr
        if(StringUtil.isEmpty(fdto.getPrLeftModCaseCounth())){
            errMap.put(fdto.getServiceCode()+"prLeftModCaseCounth",MessageUtil.replaceMessage("ERR0009","Minimum Number of NCs","The field"));
            fdto.setPrLeftModCaseCountherr(true);
        }
        if(StringUtil.isEmpty(fdto.getPrRightModCaseCounth())){
            errMap.put(fdto.getServiceCode()+"prRightModCaseCounth",MessageUtil.replaceMessage("ERR0009","Maximum Number of NCs","The field"));
            fdto.setPrRightModCaseCountherr(true);
        }
        if(StringUtil.isEmpty(fdto.getPrRightLowCaseCounth())){
            errMap.put(fdto.getServiceCode()+"prRightLowCaseCounth",MessageUtil.replaceMessage("ERR0009","Maximum Number of NCs","The field"));
            fdto.setPrRightLowCaseCountherr(true);
        }
        if(StringUtil.isEmpty(fdto.getPrLeftHighCaseCount())){
            errMap.put(fdto.getServiceCode()+"prLeftHighCaseCounth",MessageUtil.replaceMessage("ERR0009","Minimum Number of NCs","The field"));
            fdto.setPrLeftHighCaseCounterr(true);
        }
    }

    public void dateVad(Map<String, String> errMap,HcsaRiskFinanceMatrixDto fdto){
        String inEffDate = fdto.getInEffectiveStartDate();
        String inEndDate = fdto.getInEffectiveEndDate();
        String prEffDate = fdto.getPrEffectiveStartDate();
        String prEndDate = fdto.getPrEffectiveEndDate();
        boolean inDateFormatVad = doDateFormatVad(errMap,inEffDate,inEndDate,fdto.getServiceCode(),true,fdto);
        boolean prDateFormatVad = doDateFormatVad(errMap,prEffDate,prEndDate,fdto.getServiceCode(),false,fdto);
        if(inDateFormatVad&&fdto.isInIsEdit()){
            doDateLogicVad(errMap,fdto,true);
        }
        if(prDateFormatVad&&fdto.isPrIsEdit()){
            doDateLogicVad(errMap,fdto,false);
        }

    }
    public void doDateLogicVad(Map<String, String> errMap,HcsaRiskFinanceMatrixDto fdto,boolean isIn){
        //effdate least version <
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
                doUsualDateVad(inEffDate,inEndDate,fdto.getServiceCode(),errMap,true,inEditNumFlag,prEditNumFlag,fdto);
                doUsualDateVad(prEffDate,prEndDate,fdto.getServiceCode(),errMap,false,inEditNumFlag,prEditNumFlag,fdto);
            } else {
                boolean inDateFlag;
                inDateFlag = doUsualDateVad(inEffDate,inEndDate,fdto.getServiceCode(),errMap,true,inEditNumFlag,prEditNumFlag,fdto);
                if(inDateFlag){
                    doSpecialDateFlag(errMap,fdto,true);
                }
                inDateFlag = doUsualDateVad(prEffDate,prEndDate,fdto.getServiceCode(),errMap,false,inEditNumFlag,prEditNumFlag,fdto);
                if(inDateFlag){
                    doSpecialDateFlag(errMap,fdto,false);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
    public void doSpecialDateFlag(Map<String, String> errMap,HcsaRiskFinanceMatrixDto fdto,boolean isIn){
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
                errMap.put(fdto.getServiceCode() + "inEffDate", "Effective Date should later than Previous version");
                fdto.setInEffectiveStartDateerr(true);
            }
            if(prEffDate.getTime()<basePrEffDate.getTime()&&!isIn){
                errMap.put(fdto.getServiceCode() + "prEffDate", "Effective Date should later than Previous version");
                fdto.setPrEffectiveStartDateerr(true);
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }

    public boolean doUsualDateVad(Date effDate,Date endDate,String serviceCode,Map<String, String> errMap,boolean isIn,int inEdit,int prEdit,HcsaRiskFinanceMatrixDto fdto){
        boolean flag = true;
        if (effDate.getTime() < System.currentTimeMillis()) {
            flag = false;
            if(isIn){
                if(inEdit == 1){
                    errMap.put(serviceCode + "inEffDate",MessageUtil.replaceMessage("ERR0012","Effective Start Date","Effective Date"));
                    fdto.setInEffectiveStartDateerr(true);
                }
            }else {
                if(prEdit == 1){
                    errMap.put(serviceCode + "prEffDate", MessageUtil.replaceMessage("ERR0012","Effective Start Date","Effective Date"));
                    fdto.setPrEffectiveStartDateerr(true);
                }
            }
        } else if (endDate.getTime() < effDate.getTime()) {
            flag = false;
            if(isIn){
                if(inEdit == 1){
                    errMap.put(serviceCode + "inEndDate", "ERR0016");
                    fdto.setInEffectiveEndDateerr(true);
                }
            }else{
                if(prEdit == 1){
                    errMap.put(serviceCode + "prEndDate", "ERR0016");
                    fdto.setPrEffectiveEndDateerr(true);
                }
            }
        }
        return flag;
    }
    public boolean doDateFormatVad(Map<String, String> errMap,String strEffDate,String strEndDate,String serviceCode,boolean isIn,HcsaRiskFinanceMatrixDto fdto){
        boolean vadFlag = true;
        if(StringUtil.isEmpty(strEffDate)){
            vadFlag = false;
            if(isIn){
                errMap.put(serviceCode+"inEffDate",MessageUtil.replaceMessage("ERR0009","Effective Start Date","The field"));
                fdto.setInEffectiveStartDateerr(true);
            }else{
                errMap.put(serviceCode+"prEffDate",MessageUtil.replaceMessage("ERR0009","Effective Start Date","The field"));
                fdto.setPrEffectiveStartDateerr(true);
            }
        }else{
           try {
               Formatter.parseDate(strEffDate);
           }catch (Exception e){
               if(isIn){
                   errMap.put(serviceCode+"inEffDate",MessageUtil.replaceMessage("ERR0017","Effective Start Date","replaceArea"));
                   fdto.setInEffectiveStartDateerr(true);
               }else{
                   errMap.put(serviceCode+"prEffDate",MessageUtil.replaceMessage("ERR0017","Effective Start Date","replaceArea"));
                   fdto.setPrEffectiveStartDateerr(true);
               }
               vadFlag = false;
           }
        }
        if(StringUtil.isEmpty(strEndDate)){
            vadFlag = false;
            if(isIn){
                errMap.put(serviceCode+"inEndDate",MessageUtil.replaceMessage("ERR0009","Effective End Date","The field"));
                fdto.setInEffectiveEndDateerr(true);
            }else{
                errMap.put(serviceCode+"prEndDate",MessageUtil.replaceMessage("ERR0009","Effective End Date","The field"));
                fdto.setPrEffectiveEndDateerr(true);
            }
        }else{
            try {
                Formatter.parseDate(strEndDate);
            }catch (Exception e){
                if(isIn){
                    errMap.put(serviceCode+"inEndDate",MessageUtil.replaceMessage("ERR0017","Effective End Date","replaceArea"));
                    fdto.setInEffectiveEndDateerr(true);
                }else{
                    errMap.put(serviceCode+"prEndDate",MessageUtil.replaceMessage("ERR0017","Effective End Date","replaceArea"));
                    fdto.setPrEffectiveEndDateerr(true);
                }
                return false;
            }
        }
        return vadFlag;
    }

}
