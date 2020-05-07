package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskInspectionMatrixDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.InspectionShowDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author: jiahao
 * @Date: 2020/1/2 18:22
 */
public class HcsaInspectionValidate implements CustomizeValidator {

    private static  Integer MAX_VALUE_LENGTH = 3;
    private static  Integer MIN_VALUE_LENGTH = 2;
    private static  String  MANDATORY        = "CaseCounth is mandatory";
    private static  String  LENGTH_MORE_MES  = "CaseCounth length max is ";
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        InspectionShowDto showDto = (InspectionShowDto) ParamUtil.getSessionAttr(request,"inShowDto");
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        List<HcsaRiskInspectionMatrixDto> iDtoList = showDto.getInspectionDtoList();
        List<HcsaRiskInspectionMatrixDto> editList = IaisCommonUtils.genNewArrayList();
        for(HcsaRiskInspectionMatrixDto temp:iDtoList){
            if(temp.isCaEdit()||temp.isMjEdit()||temp.isMiEdit()){
                editList.add(temp);
            }
        }
        if(editList!=null&&!editList.isEmpty()){
            for(HcsaRiskInspectionMatrixDto temp:editList){
                caseCounthVad(errMap,temp);
                dateVad(errMap,temp);
            }
        }else{
            errMap.put("All","Please do some change");
        }

        mergeList(editList,iDtoList);
        return errMap;
    }



    public void dateVad(Map<String, String> errMap,HcsaRiskInspectionMatrixDto fdto){
        String caEffDate = fdto.getDoCaEffectiveDate();
        String caEndDate = fdto.getDoCaEndDate();
        String miEffDate = fdto.getDoMiEffectiveDate();
        String miEndDate = fdto.getDoMiEndDate();
        String mjEffDate = fdto.getDoMjEffectiveDate();
        String mjEndDate = fdto.getDoMjEndDate();
        boolean caDateFormatVad = doDateFormatVad(errMap,caEffDate,caEndDate,fdto.getSvcCode(),"C",fdto);
        boolean miDateFormatVad = doDateFormatVad(errMap,miEffDate,miEndDate,fdto.getSvcCode(),"I",fdto);
        boolean mjDateFormatVad = doDateFormatVad(errMap,mjEffDate,mjEndDate,fdto.getSvcCode(),"A",fdto);
        if(caDateFormatVad&&fdto.isCaEdit()){
            doDateLogicVad(errMap,fdto,"C");
        }
        if(miDateFormatVad&&fdto.isMiEdit()){
            doDateLogicVad(errMap,fdto,"I");
        }
        if(mjDateFormatVad&&fdto.isMjEdit()){
            doDateLogicVad(errMap,fdto,"A");
        }

    }
    public void doDateLogicVad(Map<String, String> errMap,HcsaRiskInspectionMatrixDto fdto,String level){
        //effdate least version <
        try {
            Date caEffDate = Formatter.parseDate(fdto.getDoCaEffectiveDate());
            Date caEndDate = Formatter.parseDate(fdto.getDoCaEndDate());
            Date miEffDate = Formatter.parseDate(fdto.getDoMiEffectiveDate());
            Date miEndDate = Formatter.parseDate(fdto.getDoMiEndDate());
            Date mjEffDate = Formatter.parseDate(fdto.getDoMjEffectiveDate());
            Date mjEndDate = Formatter.parseDate(fdto.getDoMjEndDate());
            int caEditNumFlag = 0;
            int miEditNumFlag = 0;
            int mjEditNumFlag = 0;
            if(fdto.isCaEdit()){
                caEditNumFlag = 1;
            }
            if(fdto.isMiEdit()){
                miEditNumFlag = 1;
            }
            if(fdto.isMjEdit()){
                mjEditNumFlag = 1;
            }
            if (StringUtil.isEmpty(fdto.getId())) {
                doUsualDateVad(caEffDate,caEndDate,fdto.getSvcCode(),errMap,level,caEditNumFlag,miEditNumFlag,mjEditNumFlag,fdto);
                doUsualDateVad(miEffDate,miEndDate,fdto.getSvcCode(),errMap,level,caEditNumFlag,miEditNumFlag,mjEditNumFlag,fdto);
                doUsualDateVad(mjEffDate,mjEndDate,fdto.getSvcCode(),errMap,level,caEditNumFlag,miEditNumFlag,mjEditNumFlag,fdto);
            } else {
                boolean caDateFlag;
                caDateFlag = doUsualDateVad(caEffDate,caEndDate,fdto.getSvcCode(),errMap,"C",caEditNumFlag,miEditNumFlag,mjEditNumFlag,fdto);
                if(caDateFlag){
                    doSpecialDateFlag(errMap,fdto,"C");
                }
                boolean miDateFlag ;
                miDateFlag = doUsualDateVad(miEffDate,miEndDate,fdto.getSvcCode(),errMap,"I",caEditNumFlag,miEditNumFlag,mjEditNumFlag,fdto);
                if(miDateFlag){
                    doSpecialDateFlag(errMap,fdto,"I");
                }

                boolean mjDateFlag ;
                mjDateFlag = doUsualDateVad(miEffDate,miEndDate,fdto.getSvcCode(),errMap,"A",caEditNumFlag,miEditNumFlag,mjEditNumFlag,fdto);
                if(mjDateFlag){
                    doSpecialDateFlag(errMap,fdto,"A");
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void doSpecialDateFlag(Map<String, String> errMap,HcsaRiskInspectionMatrixDto fdto,String level){
        try {
            Date caEffDate = Formatter.parseDate(fdto.getDoCaEffectiveDate());
            Date caEndDate = Formatter.parseDate(fdto.getDoCaEndDate());
            Date miEffDate = Formatter.parseDate(fdto.getDoMiEffectiveDate());
            Date miEndDate = Formatter.parseDate(fdto.getDoMiEndDate());
            Date mjEffDate = Formatter.parseDate(fdto.getDoMjEffectiveDate());
            Date mjEndDate = Formatter.parseDate(fdto.getDoMjEndDate());
            Date basecaEffDate = Formatter.parseDate(fdto.getBaseCEffectiveDate());
            Date basecaEndDate = Formatter.parseDate(fdto.getBaseCEndDate());
            Date basemiEffDate = Formatter.parseDate(fdto.getBaseMiEffectiveDate());
            Date basemiEndDate = Formatter.parseDate(fdto.getBaseMiEndDate());
            Date basemjEffDate = Formatter.parseDate(fdto.getBaseMjEffectiveDate());
            Date basemjEndDate = Formatter.parseDate(fdto.getBaseMjEndDate());
            if(caEffDate.getTime()<basecaEffDate.getTime()&&"C".equals(level)){
                errMap.put(fdto.getSvcCode() + "caEffDate", "Effective Date should later than Previous version");
            }
            if(miEffDate.getTime()<basemiEffDate.getTime()&&"I".equals(level)){
                errMap.put(fdto.getSvcCode() + "miEffDate", "Effective Date should later than Previous version");
            }
            if(mjEffDate.getTime()<basemjEffDate.getTime()&&"A".equals(level)){
                errMap.put(fdto.getSvcCode() + "mjEffDate", "Effective Date should later than Previous version");
            }

        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }


    public boolean doUsualDateVad(Date effDate,Date endDate,String serviceCode,Map<String, String> errMap,String level,int caEditNumFlag,int miEditNumFlag,int mjEditNumFlag,HcsaRiskInspectionMatrixDto fdto){
        boolean flag = true;
        if (effDate.getTime() < System.currentTimeMillis()) {
            flag = false;
            if("C".equals(level)){
                if(caEditNumFlag == 1){
                    errMap.put(serviceCode + "caEffDate", "ERR0012");
                }
            }else if("I".equals(level)){
                if(miEditNumFlag == 1){
                    errMap.put(serviceCode + "miEffDate", "ERR0012");
                }
            }else if("A".equals(level)){
                if(mjEditNumFlag == 1){
                    errMap.put(serviceCode + "mjEffDate", "ERR0012");
                }
            }
        } else if (endDate.getTime() < effDate.getTime()) {
            flag = false;
            if("C".equals(level)){
                if(caEditNumFlag == 1){
                    errMap.put(serviceCode + "caEndDate", "Effective Date should be ealier than EndDate");
                }
            }else if("I".equals(level)){
                if(miEditNumFlag == 1){
                    errMap.put(serviceCode + "miEndDate", "Effective Date should be ealier than EndDate");
                }
            }else if("A".equals(level)){
                if(mjEditNumFlag == 1){
                    errMap.put(serviceCode + "mjEndDate", "ERR0012");
                }
            }
        }
        return flag;
    }
    public boolean doDateFormatVad(Map<String, String> errMap,String strEffDate,String strEndDate,String serviceCode,String level,HcsaRiskInspectionMatrixDto fdto){
        boolean vadFlag = true;
        if(StringUtil.isEmpty(strEffDate)){
            vadFlag = false;
            if("C".equals(level)){
                errMap.put(serviceCode+"caEffDate","Effective Date is mandatory");
            }else if("I".equals(level)){
                errMap.put(serviceCode+"caEffDate","Effective Date is mandatory");
            }else if("A".equals(level)){
                errMap.put(serviceCode+"miEffDate","Effective Date is mandatory");
            }
        }else{
            try {
                Formatter.parseDate(strEffDate);
            }catch (Exception e){
                if("C".equals(level)){
                    errMap.put(serviceCode+"caEffDate","Date Format Error");
                }else if("I".equals(level)){
                    errMap.put(serviceCode+"miEffDate","Date Format Error");
                }else if("A".equals(level)){
                    errMap.put(serviceCode+"mjEffDate","Date Format Error");
                }
                vadFlag = false;
            }
        }
        if(StringUtil.isEmpty(strEndDate)){
            vadFlag = false;
            if("C".equals(level)){
                errMap.put(serviceCode+"caEndDate","EndDate is mandatory");
            }else if("I".equals(level)){
                errMap.put(serviceCode+"miEndDate","EndDate is mandatory");
            }else if("A".equals(level)){
                errMap.put(serviceCode+"mjEndDate","EndDate is mandatory");
            }
        }else{
            try {
                Formatter.parseDate(strEndDate);
            }catch (Exception e){
                vadFlag = false;
                if("C".equals(level)){
                    errMap.put(serviceCode+"caEndDate","Date Format Error");
                }else if("I".equals(level)){
                    errMap.put(serviceCode+"miEndDate","Date Format Error");
                }else if("A".equals(level)){
                    errMap.put(serviceCode+"mjEndDate","Date Format Error");
                }
                return false;
            }
        }
        return vadFlag;
    }
    public void caseCounthVad(Map<String, String> errMap, HcsaRiskInspectionMatrixDto fdto){
        mandatoryCaseCounthVad(errMap,fdto);
        numberCaseCounthVad(errMap,fdto);
    }
    public void numberCaseCounthVad(Map<String, String> errMap,HcsaRiskInspectionMatrixDto fdto){
        inLeftModVadAndinRightModVad(errMap,fdto.getDoCaLeftModCounth(),fdto.getDoCaRightModCounth(),fdto.getSvcCode(),"C",fdto);
        inLeftModVadAndinRightModVad(errMap,fdto.getDoMiLeftModCounth(),fdto.getDoMiRightModCounth(),fdto.getSvcCode(),"I",fdto);
        inLeftModVadAndinRightModVad(errMap,fdto.getDoMjLeftModCounth(),fdto.getDoMjRightModCounth(),fdto.getSvcCode(),"A",fdto);
        inRightLowVad(errMap,fdto.getDoCaRightLowCounth(),fdto.getDoCaLeftModCounth(),fdto.getSvcCode(),"C",fdto);
        inRightLowVad(errMap,fdto.getDoMiRightLowCounth(),fdto.getDoMiLeftModCounth(),fdto.getSvcCode(),"I",fdto);
        inRightLowVad(errMap,fdto.getDoMjRightLowCounth(),fdto.getDoMjLeftModCounth(),fdto.getSvcCode(),"A",fdto);
        inLeftHighVad(errMap,fdto.getDoCaLeftHighCounth(),fdto.getDoCaRightModCounth(),fdto.getSvcCode(),"C",fdto);
        inLeftHighVad(errMap,fdto.getDoMiLeftHighCounth(),fdto.getDoMiRightModCounth(),fdto.getSvcCode(),"I",fdto);
        inLeftHighVad(errMap,fdto.getDoMjLeftHighCounth(),fdto.getDoMjRightModCounth(),fdto.getSvcCode(),"A",fdto);
    }
    public void inLeftHighVad(Map<String, String> errMap,String inLeftHigh,String inRightMod, String serviceCode,String level,HcsaRiskInspectionMatrixDto fdto){
        Integer inLeftHighNum = 0;
        boolean caLeftHighNumFlag = true;
        boolean miLeftHighNumFlag = true;
        boolean mjLeftHighNumFlag = true;
        Integer inRightModNum = 0;
        try {
            if(!StringUtil.isEmpty(inLeftHigh)){
                inLeftHighNum = Integer.parseInt(inLeftHigh);
                if (inLeftHighNum > 999 || inLeftHighNum < 0) {
                    if("C".equals(level)){
                        caLeftHighNumFlag = false;
                        errMap.put(serviceCode + "caRightLowCaseCounth", "ERR0013");
                        fdto.setDoCaLeftHighCountherr(true);
                    }else if("I".equals(level)){
                        miLeftHighNumFlag = false;
                        errMap.put(serviceCode + "miRightLowCaseCounth", "ERR0013");
                        fdto.setDoMiLeftHighCountherr(false);
                    }else if("A".equals(level)){
                        mjLeftHighNumFlag = false;
                        errMap.put(serviceCode + "mjRightLowCaseCounth", "ERR0013");
                        fdto.setDoMjLeftHighCountherr(false);
                    }
                }
            }
        } catch (Exception e) {
            if("C".equals(level)){
                caLeftHighNumFlag = false;
                errMap.put(serviceCode + "caRightLowCaseCounth", "ERR0013");
                fdto.setDoCaLeftHighCountherr(true);
            }else if("I".equals(level)){
                miLeftHighNumFlag = false;
                errMap.put(serviceCode + "miRightLowCaseCounth", "ERR0013");
                fdto.setDoMiLeftHighCountherr(true);
             }else if("A".equals(level)){
                mjLeftHighNumFlag = false;
                errMap.put(serviceCode + "mjRightLowCaseCounth", "ERR0013");
                fdto.setDoMjLeftHighCountherr(true);
            }
            log.error(e.getMessage(), e);
        }
        try {
            if(!StringUtil.isEmpty(inRightMod)){
                inRightModNum = Integer.parseInt(inRightMod);
                if(inRightModNum +1 != inLeftHighNum){
                    if("C".equals(level)&&caLeftHighNumFlag){
                        errMap.put(serviceCode + "caRightLowCaseCounth", "ERR0014");
                        fdto.setDoCaLeftHighCountherr(true);
                    }else if("I".equals(level)&&miLeftHighNumFlag){
                        errMap.put(serviceCode + "miRightLowCaseCounth", "ERR0014");
                        fdto.setDoMiLeftHighCountherr(true);
                    }else if("A".equals(level)&&mjLeftHighNumFlag){
                        errMap.put(serviceCode + "mjRightLowCaseCounth", "ERR0014");
                        fdto.setDoMjLeftHighCountherr(true);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
    public void inRightLowVad(Map<String, String> errMap,String inRightLow,String inLeftMod,String serviceCode,String level,HcsaRiskInspectionMatrixDto fdto){
        Integer inRightLowNum = 0;
        boolean caRightLowNumFlag = true;
        boolean miRightLowNumFlag = true;
        boolean mjRightLowNumFlag = true;
        Integer inLeftModNum = 0;
        try {
            if(!StringUtil.isEmpty(inRightLow)){
                inRightLowNum = Integer.parseInt(inRightLow);
                if (inRightLowNum > 999 || inRightLowNum < 0) {
                    if("C".equals(level)){
                        caRightLowNumFlag = false;
                        errMap.put(serviceCode + "caRightLowCaseCounth", "ERR0013");
                        fdto.setDoCaRightLowCountherr(true);
                    }else if("I".equals(level)){
                        miRightLowNumFlag = false;
                        errMap.put(serviceCode + "miRightLowCaseCounth", "ERR0013");
                        fdto.setDoMiRightLowCountherr(true);
                    }else if("A".equals(level)){
                        mjRightLowNumFlag = false;
                        errMap.put(serviceCode + "mjRightLowCaseCounth", "ERR0013");
                        fdto.setDoMjRightLowCountherr(true);
                    }
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            if("C".equals(level)){
                caRightLowNumFlag = false;
                errMap.put(serviceCode + "caRightLowCaseCounth", "ERR0013");
                fdto.setDoCaRightLowCountherr(true);
            }else if("I".equals(level)){
                miRightLowNumFlag = false;
                errMap.put(serviceCode + "miRightLowCaseCounth", "ERR0013");
                fdto.setDoMiRightLowCountherr(true);
            }else if("A".equals(level)){
                mjRightLowNumFlag = false;
                errMap.put(serviceCode + "miRightLowCaseCounth", "ERR0013");
                fdto.setDoMiRightLowCountherr(true);
            }
            log.error(e.getMessage(), e);
        }
        try {
            if(!StringUtil.isEmpty(inLeftMod)){
                inLeftModNum = Integer.parseInt(inLeftMod);
                if(inLeftModNum -1 != inRightLowNum){
                    if("C".equals(level)){
                        if(caRightLowNumFlag){
                            errMap.put(serviceCode + "caRightLowCaseCounth", "ERR0015");
                            fdto.setDoCaRightLowCountherr(true);
                        }
                    }else if("I".equals(level)){
                        if(miRightLowNumFlag){
                            errMap.put(serviceCode + "miRightLowCaseCounth", "ERR0015");
                            fdto.setDoMiRightLowCountherr(true);
                        }
                    }else if("A".equals(level)){
                        if(mjRightLowNumFlag){
                            errMap.put(serviceCode + "mjRightLowCaseCounth", "ERR0015");
                            fdto.setDoMjRightLowCountherr(true);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    public void inLeftModVadAndinRightModVad(Map<String, String> errMap,String inLeftMod,String inRightMod,String serviceCode,String level,HcsaRiskInspectionMatrixDto fdto){
        Integer inLeftModNum = 0;
        Integer inRightModNum = 0;
        int numberFlag = 0;
        if(!StringUtil.isEmpty(inLeftMod)){
            try {
                inLeftModNum = Integer.parseInt(inLeftMod);
                if(inLeftModNum<0||inLeftModNum>999){
                    if("C".equals(level)){
                        errMap.put(serviceCode+"caLeftModCaseCounth","ERR0013");
                        fdto.setDoCaLeftModCountherr(true);
                    }else if("I".equals(level)){
                        errMap.put(serviceCode+"miLeftModCaseCounth","ERR0013");
                        fdto.setDoMiLeftModCountherr(true);
                    }else if("A".equals(level)){
                        errMap.put(serviceCode+"mjLeftModCaseCounth","ERR0013");
                        fdto.setDoMjLeftModCountherr(true);
                    }
                }
                numberFlag++;
            }catch (Exception e){
                if("C".equals(level)){
                    errMap.put(serviceCode+"caLeftModCaseCounth","ERR0013");
                    fdto.setDoCaLeftModCountherr(true);
                }else if("I".equals(level)){
                    errMap.put(serviceCode+"miLeftModCaseCounth","ERR0013");
                    fdto.setDoMiLeftModCountherr(true);
                }else if("A".equals(level)){
                    errMap.put(serviceCode+"mjLeftModCaseCounth","ERR0013");
                    fdto.setDoMjLeftModCountherr(true);
                }
                log.error(e.getMessage(), e);
            }
        }
        if(!StringUtil.isEmpty(inRightMod)){
            try {
                inRightModNum = Integer.parseInt(inRightMod);
                if(inRightModNum<0 || inRightModNum >999){
                    if("C".equals(level)){
                        errMap.put(serviceCode+"caRightModCaseCounth","ERR0013");
                        fdto.setDoCaRightModCountherr(true);
                    }else if("I".equals(level)){
                        errMap.put(serviceCode+"miRightModCaseCounth","ERR0013");
                        fdto.setDoMiRightModCountherr(true);
                    }else if("A".equals(level)){
                        errMap.put(serviceCode+"mjRightModCaseCounth","ERR0013");
                        fdto.setDoMjRightModCountherr(true);
                    }
                }
                numberFlag++;
            }catch (Exception e){
                if(inRightModNum<0 || inRightModNum >999){
                    if("C".equals(level)){
                        errMap.put(serviceCode+"caRightModCaseCounth","ERR0013");
                        fdto.setDoCaRightModCountherr(true);
                    }else if("I".equals(level)){
                        errMap.put(serviceCode+"miRightModCaseCounth","ERR0013");
                        fdto.setDoMiRightModCountherr(true);
                    }else if("A".equals(level)){
                        errMap.put(serviceCode+"mjRightModCaseCounth","ERR0013");
                        fdto.setDoMjRightModCountherr(true);
                    }
                }
                log.error(e.getMessage(), e);
            }
        }
        if(numberFlag == 2){
            numberOrderVad(errMap,inLeftModNum,inRightModNum,serviceCode,level,fdto);
        }
    }
    public void numberOrderVad(Map<String, String> errMap,Integer lm,Integer rm,String serviceCode,String level,HcsaRiskInspectionMatrixDto fdto){
        if(lm>rm&&"C".equals(level)){
            errMap.put(serviceCode+"caRightModCaseCounth","ERR0011");
            fdto.setDoCaRightModCountherr(true);
            fdto.setDoCaLeftModCountherr(true);
        }else if(lm>rm&&"I".equals(level)){
            errMap.put(serviceCode+"miRightModCaseCounth","ERR0011");
            fdto.setDoMiRightModCountherr(true);
            fdto.setDoMiLeftModCountherr(true);
        }else if(lm>rm&&"A".equals(level)){
            errMap.put(serviceCode+"miRightModCaseCounth","ERR0011");
            fdto.setDoMjRightModCountherr(true);
            fdto.setDoMjLeftModCountherr(true);
        }
    }
    public void mandatoryCaseCounthVad(Map<String, String> errMap, HcsaRiskInspectionMatrixDto fdto){
        //ca
        if(StringUtil.isEmpty(fdto.getDoCaLeftModCounth())){
            errMap.put(fdto.getSvcCode()+"caLeftModCaseCounth",MANDATORY);
            fdto.setDoCaLeftModCountherr(true);
        }else if(fdto.getDoCaLeftModCounth().length()> MIN_VALUE_LENGTH){
            errMap.put(fdto.getSvcCode()+"caLeftModCaseCounth",LENGTH_MORE_MES +MIN_VALUE_LENGTH);
            fdto.setDoCaLeftModCountherr(true);
        }

        if(StringUtil.isEmpty(fdto.getDoCaLeftHighCounth())){
            errMap.put(fdto.getSvcCode()+"caLeftHighCaseCounth",MANDATORY);
            fdto.setDoCaLeftHighCountherr(true);
        }else if(fdto.getDoCaLeftHighCounth().length() > MIN_VALUE_LENGTH){
            errMap.put(fdto.getSvcCode()+"caLeftHighCaseCounth",LENGTH_MORE_MES  +MIN_VALUE_LENGTH);
            fdto.setDoCaLeftHighCountherr(true);
        }

        if(StringUtil.isEmpty(fdto.getDoCaRightLowCounth())){
            errMap.put(fdto.getSvcCode()+"caRightLowCaseCounth",MANDATORY);
            fdto.setDoCaRightLowCountherr(true);
        }else if (fdto.getDoCaRightLowCounth().length() > MAX_VALUE_LENGTH ){
            errMap.put(fdto.getSvcCode()+"caRightLowCaseCounth",LENGTH_MORE_MES +MAX_VALUE_LENGTH );
            fdto.setDoCaRightLowCountherr(true);
        }

        if(StringUtil.isEmpty(fdto.getDoCaRightModCounth())){
            errMap.put(fdto.getSvcCode()+"caRightModCaseCounth()",MANDATORY);
            fdto.setDoCaRightModCountherr(true);
        }else if (fdto.getDoCaRightModCounth().length() >  MAX_VALUE_LENGTH ){
            errMap.put(fdto.getSvcCode()+"caRightModCaseCounth()",LENGTH_MORE_MES + MAX_VALUE_LENGTH );
            fdto.setDoCaRightModCountherr(true);
        }

        //mi

        if(StringUtil.isEmpty(fdto.getDoMiRightModCounth())){
            errMap.put(fdto.getSvcCode()+"miRightModCaseCounth()",MANDATORY);
            fdto.setDoMiRightModCountherr(true);
        }else if(fdto.getDoMiRightModCounth().length() > MIN_VALUE_LENGTH){
            errMap.put(fdto.getSvcCode()+"miRightModCaseCounth()",LENGTH_MORE_MES + MIN_VALUE_LENGTH);
            fdto.setDoMiRightModCountherr(true);
        }

        if(StringUtil.isEmpty(fdto.getDoMiRightLowCounth())){
            errMap.put(fdto.getSvcCode()+"miRightLowCaseCounth",MANDATORY);
            fdto.setDoMiRightLowCountherr(true);
        }else if(fdto.getDoMiRightLowCounth().length() >  MIN_VALUE_LENGTH){
            errMap.put(fdto.getSvcCode()+"miRightLowCaseCounth",LENGTH_MORE_MES + MIN_VALUE_LENGTH);
            fdto.setDoMiRightLowCountherr(true);
        }

        if(StringUtil.isEmpty(fdto.getDoMiLeftHighCounth())){
            errMap.put(fdto.getSvcCode()+"miLeftHighCaseCounth",MANDATORY);
            fdto.setDoMiLeftHighCountherr(true);
        }else if(fdto.getDoMiLeftHighCounth().length() > MAX_VALUE_LENGTH){
            errMap.put(fdto.getSvcCode()+"miLeftHighCaseCounth",LENGTH_MORE_MES + MAX_VALUE_LENGTH);
            fdto.setDoMiLeftHighCountherr(true);
        }

        if(StringUtil.isEmpty(fdto.getDoMiLeftModCounth())){
            errMap.put(fdto.getSvcCode()+"miLeftModCaseCounth",MANDATORY);
            fdto.setDoMiLeftModCountherr(true);
        }else if(fdto.getDoMiLeftModCounth().length()> MAX_VALUE_LENGTH){
            errMap.put(fdto.getSvcCode()+"miLeftModCaseCounth",LENGTH_MORE_MES + MAX_VALUE_LENGTH);
            fdto.setDoMiLeftModCountherr(true);
        }

        //mj
        if(StringUtil.isEmpty(fdto.getDoMjRightModCounth())){
            errMap.put(fdto.getSvcCode()+"mjRightModCaseCounth()",MANDATORY);
            fdto.setDoMjRightModCountherr(true);
        }else if(fdto.getDoMjRightModCounth().length() >MIN_VALUE_LENGTH){
            errMap.put(fdto.getSvcCode()+"mjRightModCaseCounth()",LENGTH_MORE_MES + MIN_VALUE_LENGTH);
            fdto.setDoMjRightModCountherr(true);
        }

        if(StringUtil.isEmpty(fdto.getDoMjRightLowCounth())){
            errMap.put(fdto.getSvcCode()+"mjRightLowCaseCounth",MANDATORY);
            fdto.setDoMjRightLowCountherr(true);
        }else if(fdto.getDoMjRightLowCounth().length() > MIN_VALUE_LENGTH){
            errMap.put(fdto.getSvcCode()+"mjRightLowCaseCounth",LENGTH_MORE_MES + MIN_VALUE_LENGTH);
            fdto.setDoMjRightLowCountherr(true);
        }

        if(StringUtil.isEmpty(fdto.getDoMjLeftHighCounth())){
            errMap.put(fdto.getSvcCode()+"mjLeftHighCaseCounth",MANDATORY);
            fdto.setDoMjLeftHighCountherr(true);
        }else if(fdto.getDoMjLeftHighCounth().length()>MAX_VALUE_LENGTH){
            errMap.put(fdto.getSvcCode()+"mjLeftHighCaseCounth",LENGTH_MORE_MES + MAX_VALUE_LENGTH);
            fdto.setDoMjLeftHighCountherr(true);
        }

        if(StringUtil.isEmpty(fdto.getDoMjLeftModCounth())){
            errMap.put(fdto.getSvcCode()+"mjLeftModCaseCounth",MANDATORY);
            fdto.setDoMjLeftModCountherr(true);
        }else if(fdto.getDoMjLeftModCounth().length() >MAX_VALUE_LENGTH ){
            errMap.put(fdto.getSvcCode()+"mjLeftModCaseCounth",LENGTH_MORE_MES + MAX_VALUE_LENGTH);
            fdto.setDoMjLeftModCountherr(true);
        }

    }
    public void mergeList(List<HcsaRiskInspectionMatrixDto> editList, List<HcsaRiskInspectionMatrixDto> financeList){
        if(editList!=null &&financeList!=null){
            for(HcsaRiskInspectionMatrixDto fin:financeList){
                for(HcsaRiskInspectionMatrixDto ed:editList){
                    if(ed.getSvcCode().equals(fin.getSvcCode())){
                        fin = ed;
                    }
                }
            }
        }
    }
}
