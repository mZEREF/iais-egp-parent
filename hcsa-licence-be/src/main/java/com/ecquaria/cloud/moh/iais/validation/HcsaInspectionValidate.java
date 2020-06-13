package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskInspectionMatrixDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.InspectionShowDto;
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
 * @Date: 2020/1/2 18:22
 */
@Slf4j
public class HcsaInspectionValidate implements CustomizeValidator {

    private static  Integer MAX_VALUE_LENGTH = 3;
    private static  Integer MIN_VALUE_LENGTH = 2;
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
        WebValidationHelper.saveAuditTrailForNoUseResult(errMap);
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
          //  Date caEndDate = Formatter.parseDate(fdto.getDoCaEndDate());
            Date miEffDate = Formatter.parseDate(fdto.getDoMiEffectiveDate());
        //    Date miEndDate = Formatter.parseDate(fdto.getDoMiEndDate());
            Date mjEffDate = Formatter.parseDate(fdto.getDoMjEffectiveDate());
         //   Date mjEndDate = Formatter.parseDate(fdto.getDoMjEndDate());
            Date basecaEffDate = Formatter.parseDate(fdto.getBaseCEffectiveDate());
         //   Date basecaEndDate = Formatter.parseDate(fdto.getBaseCEndDate());
            Date basemiEffDate = Formatter.parseDate(fdto.getBaseMiEffectiveDate());
         //   Date basemiEndDate = Formatter.parseDate(fdto.getBaseMiEndDate());
            Date basemjEffDate = Formatter.parseDate(fdto.getBaseMjEffectiveDate());
         //   Date basemjEndDate = Formatter.parseDate(fdto.getBaseMjEndDate());
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
            }else if(mjEditNumFlag == 1 && "A".equals(level)){
                errMap.put(serviceCode + "mjEffDate", "ERR0012");
            }
        } else if (endDate.getTime() < effDate.getTime()) {
            flag = false;
            if("C".equals(level)){
                if(caEditNumFlag == 1){
                    errMap.put(serviceCode + "caEndDate", "ERR0016");
                }
            }else if("I".equals(level)){
                if(miEditNumFlag == 1){
                    errMap.put(serviceCode + "miEndDate", "ERR0016");
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
                errMap.put(serviceCode+"caEffDate",MessageUtil.replaceMessage("ERR0009","Effective Start Date","The field"));
            }else if("I".equals(level)){
                errMap.put(serviceCode+"miEffDate",MessageUtil.replaceMessage("ERR0009","Effective Start Date","The field"));
            }else if("A".equals(level)){
                errMap.put(serviceCode+"mjEffDate",MessageUtil.replaceMessage("ERR0009","Effective Start Date","The field"));
            }
        }else{
            try {
                Formatter.parseDate(strEffDate);
            }catch (Exception e){
                if("C".equals(level)){
                    errMap.put(serviceCode+"caEffDate",MessageUtil.replaceMessage("ERR0017","Effective Start Date","replaceArea"));
                }else if("I".equals(level)){
                    errMap.put(serviceCode+"miEffDate",MessageUtil.replaceMessage("ERR0017","Effective Start Date","replaceArea"));
                }else if("A".equals(level)){
                    errMap.put(serviceCode+"mjEffDate",MessageUtil.replaceMessage("ERR0017","Effective Start Date","replaceArea"));
                }
                vadFlag = false;
            }
        }
        if(StringUtil.isEmpty(strEndDate)){
            vadFlag = false;
            if("C".equals(level)){
                errMap.put(serviceCode+"caEndDate",MessageUtil.replaceMessage("ERR0009","Effective End Date","The field"));
            }else if("I".equals(level)){
                errMap.put(serviceCode+"miEndDate",MessageUtil.replaceMessage("ERR0009","Effective End Date","The field"));
            }else if("A".equals(level)){
                errMap.put(serviceCode+"mjEndDate",MessageUtil.replaceMessage("ERR0009","Effective End Date","The field"));
            }
        }else{
            try {
                Formatter.parseDate(strEndDate);
            }catch (Exception e){
                if("C".equals(level)){
                    errMap.put(serviceCode+"caEndDate",MessageUtil.replaceMessage("ERR0017","Effective End Date","replaceArea"));
                }else if("I".equals(level)){
                    errMap.put(serviceCode+"miEndDate",MessageUtil.replaceMessage("ERR0017","Effective End Date","replaceArea"));
                }else if("A".equals(level)){
                    errMap.put(serviceCode+"mjEndDate",MessageUtil.replaceMessage("ERR0017","Effective End Date","replaceArea"));
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
                inLeftHighNum = Integer.valueOf(inLeftHigh);
                if (inLeftHighNum > 999 || inLeftHighNum < 0) {
                    if("C".equals(level)){
                        caLeftHighNumFlag = false;
                        errMap.put(serviceCode + "caRightLowCaseCounth",MessageUtil.getMessageDesc("ERR0013") + HcsaLicenceBeConstant.ERROR_MESSAGE_MAX_NC);
                        fdto.setDoCaLeftHighCountherr(true);
                    }else if("I".equals(level)){
                        miLeftHighNumFlag = false;
                        errMap.put(serviceCode + "miRightLowCaseCounth", MessageUtil.getMessageDesc("ERR0013") + HcsaLicenceBeConstant.ERROR_MESSAGE_MAX_NC);
                        fdto.setDoMiLeftHighCountherr(false);
                    }else if("A".equals(level)){
                        mjLeftHighNumFlag = false;
                        errMap.put(serviceCode + "mjRightLowCaseCounth",MessageUtil.getMessageDesc("ERR0013") + HcsaLicenceBeConstant.ERROR_MESSAGE_MAX_NC);
                        fdto.setDoMjLeftHighCountherr(false);
                    }
                }
            }
        } catch (Exception e) {
            if("C".equals(level)){
                caLeftHighNumFlag = false;
                errMap.put(serviceCode + "caRightLowCaseCounth", MessageUtil.getMessageDesc("ERR0013") + HcsaLicenceBeConstant.ERROR_MESSAGE_MAX_NC);
                fdto.setDoCaLeftHighCountherr(true);
            }else if("I".equals(level)){
                miLeftHighNumFlag = false;
                errMap.put(serviceCode + "miRightLowCaseCounth", MessageUtil.getMessageDesc("ERR0013") + HcsaLicenceBeConstant.ERROR_MESSAGE_MAX_NC);
                fdto.setDoMiLeftHighCountherr(true);
             }else if("A".equals(level)){
                mjLeftHighNumFlag = false;
                errMap.put(serviceCode + "mjRightLowCaseCounth", MessageUtil.getMessageDesc("ERR0013") + HcsaLicenceBeConstant.ERROR_MESSAGE_MAX_NC);
                fdto.setDoMjLeftHighCountherr(true);
            }
            log.error(e.getMessage(), e);
        }
        try {
            if(!StringUtil.isEmpty(inRightMod)){
                inRightModNum = Integer.valueOf(inRightMod);
                if(inRightModNum +1 != inLeftHighNum){
                    if(caLeftHighNumFlag&&"C".equals(level)){
                        errMap.put(serviceCode + "caRightLowCaseCounth", "ERR0014");
                        fdto.setDoCaLeftHighCountherr(true);
                    }else if(miLeftHighNumFlag&&"I".equals(level)){
                        errMap.put(serviceCode + "miRightLowCaseCounth", "ERR0014");
                        fdto.setDoMiLeftHighCountherr(true);
                    }else if(mjLeftHighNumFlag&&"A".equals(level)){
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
        try {
            if(!StringUtil.isEmpty(inRightLow)){
                inRightLowNum = Integer.valueOf(inRightLow);
                if (inRightLowNum > 999 || inRightLowNum < 0) {
                    if("C".equals(level)){
                        caRightLowNumFlag = false;
                        errMap.put(serviceCode + "caRightLowCaseCounth",  MessageUtil.getMessageDesc("ERR0013") + HcsaLicenceBeConstant.ERROR_MESSAGE_MAX_NC);
                        fdto.setDoCaRightLowCountherr(true);
                    }else if("I".equals(level)){
                        miRightLowNumFlag = false;
                        errMap.put(serviceCode + "miRightLowCaseCounth",  MessageUtil.getMessageDesc("ERR0013") + HcsaLicenceBeConstant.ERROR_MESSAGE_MAX_NC);
                        fdto.setDoMiRightLowCountherr(true);
                    }else if("A".equals(level)){
                        mjRightLowNumFlag = false;
                        errMap.put(serviceCode + "mjRightLowCaseCounth",  MessageUtil.getMessageDesc("ERR0013") + HcsaLicenceBeConstant.ERROR_MESSAGE_MAX_NC);
                        fdto.setDoMjRightLowCountherr(true);
                    }
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            if("C".equals(level)){
                caRightLowNumFlag = false;
                errMap.put(serviceCode + "caRightLowCaseCounth",  MessageUtil.getMessageDesc("ERR0013") + HcsaLicenceBeConstant.ERROR_MESSAGE_MAX_NC);
                fdto.setDoCaRightLowCountherr(true);
            }else if("I".equals(level)){
                miRightLowNumFlag = false;
                errMap.put(serviceCode + "miRightLowCaseCounth",  MessageUtil.getMessageDesc("ERR0013") + HcsaLicenceBeConstant.ERROR_MESSAGE_MAX_NC);
                fdto.setDoMiRightLowCountherr(true);
            }else if("A".equals(level)){
                mjRightLowNumFlag = false;
                errMap.put(serviceCode + "mjRightLowCaseCounth", MessageUtil.getMessageDesc("ERR0013") + HcsaLicenceBeConstant.ERROR_MESSAGE_MAX_NC);
                fdto.setDoMiRightLowCountherr(true);
            }
            log.error(e.getMessage(), e);
        }
        try {
            if(!StringUtil.isEmpty(inLeftMod)){
                int inLeftModNum = Integer.parseInt(inLeftMod);
                if(inLeftModNum -1 != inRightLowNum  && inLeftModNum != (inRightLowNum)){
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
                inLeftModNum = Integer.valueOf(inLeftMod);
                if(inLeftModNum<0||inLeftModNum>999){
                    if("C".equals(level)){
                        errMap.put(serviceCode+"caLeftModCaseCounth", MessageUtil.getMessageDesc("ERR0013") + HcsaLicenceBeConstant.ERROR_MESSAGE_MIN_NC);
                        fdto.setDoCaLeftModCountherr(true);
                    }else if("I".equals(level)){
                        errMap.put(serviceCode+"miLeftModCaseCounth", MessageUtil.getMessageDesc("ERR0013") + HcsaLicenceBeConstant.ERROR_MESSAGE_MIN_NC);
                        fdto.setDoMiLeftModCountherr(true);
                    }else if("A".equals(level)){
                        errMap.put(serviceCode+"mjLeftModCaseCounth", MessageUtil.getMessageDesc("ERR0013") + HcsaLicenceBeConstant.ERROR_MESSAGE_MIN_NC);
                        fdto.setDoMjLeftModCountherr(true);
                    }
                }
                numberFlag++;
            }catch (Exception e){
                if("C".equals(level)){
                    errMap.put(serviceCode+"caLeftModCaseCounth", MessageUtil.getMessageDesc("ERR0013") + HcsaLicenceBeConstant.ERROR_MESSAGE_MIN_NC);
                    fdto.setDoCaLeftModCountherr(true);
                }else if("I".equals(level)){
                    errMap.put(serviceCode+"miLeftModCaseCounth", MessageUtil.getMessageDesc("ERR0013") + HcsaLicenceBeConstant.ERROR_MESSAGE_MIN_NC);
                    fdto.setDoMiLeftModCountherr(true);
                }else if("A".equals(level)){
                    errMap.put(serviceCode+"mjLeftModCaseCounth", MessageUtil.getMessageDesc("ERR0013") + HcsaLicenceBeConstant.ERROR_MESSAGE_MIN_NC);
                    fdto.setDoMjLeftModCountherr(true);
                }
                log.error(e.getMessage(), e);
            }
        }
        if(!StringUtil.isEmpty(inRightMod)){
            try {
                inRightModNum = Integer.valueOf(inRightMod);
                if(inRightModNum<0 || inRightModNum >999){
                    if("C".equals(level)){
                        errMap.put(serviceCode+"caRightModCaseCounth", MessageUtil.getMessageDesc("ERR0013") + HcsaLicenceBeConstant.ERROR_MESSAGE_MAX_NC);
                        fdto.setDoCaRightModCountherr(true);
                    }else if("I".equals(level)){
                        errMap.put(serviceCode+"miRightModCaseCounth", MessageUtil.getMessageDesc("ERR0013") + HcsaLicenceBeConstant.ERROR_MESSAGE_MAX_NC);
                        fdto.setDoMiRightModCountherr(true);
                    }else if("A".equals(level)){
                        errMap.put(serviceCode+"mjRightModCaseCounth", MessageUtil.getMessageDesc("ERR0013") + HcsaLicenceBeConstant.ERROR_MESSAGE_MAX_NC);
                        fdto.setDoMjRightModCountherr(true);
                    }
                }
                numberFlag++;
            }catch (Exception e){
                    if("C".equals(level)){
                        errMap.put(serviceCode+"caRightModCaseCounth", MessageUtil.getMessageDesc("ERR0013") + HcsaLicenceBeConstant.ERROR_MESSAGE_MAX_NC);
                        fdto.setDoCaRightModCountherr(true);
                    }else if("I".equals(level)){
                        errMap.put(serviceCode+"miRightModCaseCounth", MessageUtil.getMessageDesc("ERR0013") + HcsaLicenceBeConstant.ERROR_MESSAGE_MAX_NC);
                        fdto.setDoMiRightModCountherr(true);
                    }else if("A".equals(level)){
                        errMap.put(serviceCode+"mjRightModCaseCounth", MessageUtil.getMessageDesc("ERR0013") + HcsaLicenceBeConstant.ERROR_MESSAGE_MAX_NC);
                        fdto.setDoMjRightModCountherr(true);
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
            errMap.put(serviceCode+"mjRightModCaseCounth","ERR0011");
            fdto.setDoMjRightModCountherr(true);
            fdto.setDoMjLeftModCountherr(true);
        }
    }
    public void mandatoryCaseCounthVad(Map<String, String> errMap, HcsaRiskInspectionMatrixDto fdto){
        //ca
        if(StringUtil.isEmpty(fdto.getDoCaLeftModCounth())){
            errMap.put(fdto.getSvcCode()+"caLeftModCaseCounth",MessageUtil.replaceMessage("ERR0009",HcsaLicenceBeConstant.ERROR_MESSAGE_MIN_NC_NO_SPACE,"The field"));
            fdto.setDoCaLeftModCountherr(true);
        }else if(fdto.getDoCaLeftModCounth().length()> MIN_VALUE_LENGTH){
            errMap.put(fdto.getSvcCode()+"caLeftModCaseCounth","ERR0018");
            fdto.setDoCaLeftModCountherr(true);
        }

        if(StringUtil.isEmpty(fdto.getDoCaLeftHighCounth())){
            errMap.put(fdto.getSvcCode()+"caLeftHighCaseCounth",MessageUtil.replaceMessage("ERR0009",HcsaLicenceBeConstant.ERROR_MESSAGE_MIN_NC_NO_SPACE,"The field"));
            fdto.setDoCaLeftHighCountherr(true);
        }else if(fdto.getDoCaLeftHighCounth().length() > MIN_VALUE_LENGTH){
            errMap.put(fdto.getSvcCode()+"caLeftHighCaseCounth","ERR0018");
            fdto.setDoCaLeftHighCountherr(true);
        }

        if(StringUtil.isEmpty(fdto.getDoCaRightLowCounth())){
            errMap.put(fdto.getSvcCode()+"caRightLowCaseCounth",MessageUtil.replaceMessage("ERR0009",HcsaLicenceBeConstant.ERROR_MESSAGE_MAX_NC_NO_SPACE,"The field"));
            fdto.setDoCaRightLowCountherr(true);
        }else if (fdto.getDoCaRightLowCounth().length() > MAX_VALUE_LENGTH ){
            errMap.put(fdto.getSvcCode()+"caRightLowCaseCounth","ERR0019");
            fdto.setDoCaRightLowCountherr(true);
        }

        if(StringUtil.isEmpty(fdto.getDoCaRightModCounth())){
            errMap.put(fdto.getSvcCode()+"caRightModCaseCounth",MessageUtil.replaceMessage("ERR0009",HcsaLicenceBeConstant.ERROR_MESSAGE_MAX_NC_NO_SPACE,"The field"));
            fdto.setDoCaRightModCountherr(true);
        }else if (fdto.getDoCaRightModCounth().length() >  MAX_VALUE_LENGTH ){
            errMap.put(fdto.getSvcCode()+"caRightModCaseCounth","ERR0019");
            fdto.setDoCaRightModCountherr(true);
        }

        //mi

        if(StringUtil.isEmpty(fdto.getDoMiRightModCounth())){
            errMap.put(fdto.getSvcCode()+"miRightModCaseCounth",MessageUtil.replaceMessage("ERR0009",HcsaLicenceBeConstant.ERROR_MESSAGE_MAX_NC_NO_SPACE,"The field"));
            fdto.setDoMiRightModCountherr(true);
        }else if(fdto.getDoMiRightModCounth().length() > MIN_VALUE_LENGTH){
            errMap.put(fdto.getSvcCode()+"miRightModCaseCounth","ERR0018");
            fdto.setDoMiRightModCountherr(true);
        }

        if(StringUtil.isEmpty(fdto.getDoMiRightLowCounth())){
            errMap.put(fdto.getSvcCode()+"miRightLowCaseCounth",MessageUtil.replaceMessage("ERR0009",HcsaLicenceBeConstant.ERROR_MESSAGE_MAX_NC_NO_SPACE,"The field"));
            fdto.setDoMiRightLowCountherr(true);
        }else if(fdto.getDoMiRightLowCounth().length() >  MIN_VALUE_LENGTH){
            errMap.put(fdto.getSvcCode()+"miRightLowCaseCounth","ERR0018");
            fdto.setDoMiRightLowCountherr(true);
        }

        if(StringUtil.isEmpty(fdto.getDoMiLeftHighCounth())){
            errMap.put(fdto.getSvcCode()+"miLeftHighCaseCounth",MessageUtil.replaceMessage("ERR0009",HcsaLicenceBeConstant.ERROR_MESSAGE_MIN_NC_NO_SPACE,"The field"));
            fdto.setDoMiLeftHighCountherr(true);
        }else if(fdto.getDoMiLeftHighCounth().length() > MAX_VALUE_LENGTH){
            errMap.put(fdto.getSvcCode()+"miLeftHighCaseCounth","ERR0019");
            fdto.setDoMiLeftHighCountherr(true);
        }

        if(StringUtil.isEmpty(fdto.getDoMiLeftModCounth())){
            errMap.put(fdto.getSvcCode()+"miLeftModCaseCounth",MessageUtil.replaceMessage("ERR0009",HcsaLicenceBeConstant.ERROR_MESSAGE_MIN_NC_NO_SPACE,"The field"));
            fdto.setDoMiLeftModCountherr(true);
        }else if(fdto.getDoMiLeftModCounth().length()> MAX_VALUE_LENGTH){
            errMap.put(fdto.getSvcCode()+"miLeftModCaseCounth","ERR0019");
            fdto.setDoMiLeftModCountherr(true);
        }

        //mj
        if(StringUtil.isEmpty(fdto.getDoMjRightModCounth())){
            errMap.put(fdto.getSvcCode()+"mjRightModCaseCounth",MessageUtil.replaceMessage("ERR0009",HcsaLicenceBeConstant.ERROR_MESSAGE_MAX_NC_NO_SPACE,"The field"));
            fdto.setDoMjRightModCountherr(true);
        }else if(fdto.getDoMjRightModCounth().length() >MIN_VALUE_LENGTH){
            errMap.put(fdto.getSvcCode()+"mjRightModCaseCounth","ERR0018");
            fdto.setDoMjRightModCountherr(true);
        }

        if(StringUtil.isEmpty(fdto.getDoMjRightLowCounth())){
            errMap.put(fdto.getSvcCode()+"mjRightLowCaseCounth",MessageUtil.replaceMessage("ERR0009",HcsaLicenceBeConstant.ERROR_MESSAGE_MAX_NC_NO_SPACE,"The field"));
            fdto.setDoMjRightLowCountherr(true);
        }else if(fdto.getDoMjRightLowCounth().length() > MIN_VALUE_LENGTH){
            errMap.put(fdto.getSvcCode()+"mjRightLowCaseCounth","ERR0018");
            fdto.setDoMjRightLowCountherr(true);
        }

        if(StringUtil.isEmpty(fdto.getDoMjLeftHighCounth())){
            errMap.put(fdto.getSvcCode()+"mjLeftHighCaseCounth",MessageUtil.replaceMessage("ERR0009",HcsaLicenceBeConstant.ERROR_MESSAGE_MAX_NC_NO_SPACE,"The field"));
            fdto.setDoMjLeftHighCountherr(true);
        }else if(fdto.getDoMjLeftHighCounth().length()>MAX_VALUE_LENGTH){
            errMap.put(fdto.getSvcCode()+"mjLeftHighCaseCounth","ERR0019");
            fdto.setDoMjLeftHighCountherr(true);
        }

        if(StringUtil.isEmpty(fdto.getDoMjLeftModCounth())){
            errMap.put(fdto.getSvcCode()+"mjLeftModCaseCounth",MessageUtil.replaceMessage("ERR0009",HcsaLicenceBeConstant.ERROR_MESSAGE_MIN_NC_NO_SPACE,"The field"));
            fdto.setDoMjLeftModCountherr(true);
        }else if(fdto.getDoMjLeftModCounth().length() >MAX_VALUE_LENGTH ){
            errMap.put(fdto.getSvcCode()+"mjLeftModCaseCounth","ERR0019");
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
