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
    private static  Integer MIN_VALUE_LENGTH = 3;
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
            errMap.put("All","RSM_ERR001");
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
                errMap.put(fdto.getSvcCode() + "caEffDate", "RSM_ERR018");
            }
            if(miEffDate.getTime()<basemiEffDate.getTime()&&"I".equals(level)){
                errMap.put(fdto.getSvcCode() + "miEffDate", "RSM_ERR018");
            }
            if(mjEffDate.getTime()<basemjEffDate.getTime()&&"A".equals(level)){
                errMap.put(fdto.getSvcCode() + "mjEffDate", "RSM_ERR018");
            }

        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }


    public boolean doUsualDateVad(Date effDate,Date endDate,String serviceCode,Map<String, String> errMap,String level,int caEditNumFlag,int miEditNumFlag,int mjEditNumFlag,HcsaRiskInspectionMatrixDto fdto){
        boolean flag = true;
        String RSM_ERR012Message = MessageUtil.replaceMessage("RSM_ERR012","Effective Start Date","Effective Date");
        if (effDate.getTime() < System.currentTimeMillis()) {
            flag = false;
            if("C".equals(level)){
                if(caEditNumFlag == 1){
                    errMap.put(serviceCode + "caEffDate", RSM_ERR012Message);
                }
            }else if("I".equals(level)){
                if(miEditNumFlag == 1){
                    errMap.put(serviceCode + "miEffDate", RSM_ERR012Message);
                }
            }else if(mjEditNumFlag == 1 && "A".equals(level)){
                errMap.put(serviceCode + "mjEffDate",RSM_ERR012Message);
            }
        } else if (endDate.getTime() < effDate.getTime()) {
            flag = false;
            if("C".equals(level)){
                if(caEditNumFlag == 1){
                    errMap.put(serviceCode + "caEndDate", "RSM_ERR017");
                }
            }else if("I".equals(level)){
                if(miEditNumFlag == 1){
                    errMap.put(serviceCode + "miEndDate", "RSM_ERR017");
                }
            }else if(mjEditNumFlag == 1&&"A".equals(level)){
                errMap.put(serviceCode + "mjEndDate", "RSM_ERR017");
            }
        }
        return flag;
    }
    public boolean doDateFormatVad(Map<String, String> errMap,String strEffDate,String strEndDate,String serviceCode,String level,HcsaRiskInspectionMatrixDto fdto){
        boolean vadFlag = true;
        String GENERAL_ERR0006Message = MessageUtil.replaceMessage("GENERAL_ERR0006","Effective Start Date","field");
        String RSM_ERR016Message = MessageUtil.replaceMessage("RSM_ERR016","Effective Start Date","replaceArea");
        if(StringUtil.isEmpty(strEffDate)){
            vadFlag = false;
            if("C".equals(level)){
                errMap.put(serviceCode+"caEffDate",GENERAL_ERR0006Message);
            }else if("I".equals(level)){
                errMap.put(serviceCode+"miEffDate",GENERAL_ERR0006Message);
            }else if("A".equals(level)){
                errMap.put(serviceCode+"mjEffDate",GENERAL_ERR0006Message);
            }
        }else{
            try {
                Formatter.parseDate(strEffDate);
            }catch (Exception e){
                if("C".equals(level)){
                    errMap.put(serviceCode+"caEffDate", RSM_ERR016Message);
                }else if("I".equals(level)){
                    errMap.put(serviceCode+"miEffDate", RSM_ERR016Message);
                }else if("A".equals(level)){
                    errMap.put(serviceCode+"mjEffDate", RSM_ERR016Message);
                }
                vadFlag = false;
            }
        }
        String GENERAL_ERR0006EMessage = MessageUtil.replaceMessage("GENERAL_ERR0006","Effective End Date","field");
        String RSM_ERR016EMessage = MessageUtil.replaceMessage("RSM_ERR016","Effective End Date","replaceArea");
        if(StringUtil.isEmpty(strEndDate)){
            vadFlag = false;
            if("C".equals(level)){
                errMap.put(serviceCode+"caEndDate",GENERAL_ERR0006EMessage);
            }else if("I".equals(level)){
                errMap.put(serviceCode+"miEndDate",GENERAL_ERR0006EMessage);
            }else if("A".equals(level)){
                errMap.put(serviceCode+"mjEndDate",GENERAL_ERR0006EMessage);
            }
        }else{
            try {
                Formatter.parseDate(strEndDate);
            }catch (Exception e){
                if("C".equals(level)){
                    errMap.put(serviceCode+"caEndDate",RSM_ERR016EMessage);
                }else if("I".equals(level)){
                    errMap.put(serviceCode+"miEndDate",RSM_ERR016EMessage);
                }else if("A".equals(level)){
                    errMap.put(serviceCode+"mjEndDate",RSM_ERR016EMessage);
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
        String maxMesMes = MessageUtil.getMessageDesc("GENERAL_ERR0027") + HcsaLicenceBeConstant.ERROR_MESSAGE_MAX_NC;
        Integer inLeftHighNum = 0;
        boolean caLeftHighNumFlag = true;
        boolean miLeftHighNumFlag = true;
        boolean mjLeftHighNumFlag = true;
        Integer inRightModNum = 0;
        try {
            if(!StringUtil.isEmpty(inLeftHigh)){
                inLeftHighNum = Integer.valueOf(inLeftHigh);
                if (inLeftHighNum > HcsaLicenceBeConstant.RISK_Validate_HIGH_MAX || inLeftHighNum < 0) {
                    if("C".equals(level)){
                        caLeftHighNumFlag = false;
                        errMap.put(serviceCode + "caRightLowCaseCounth",maxMesMes);
                        fdto.setDoCaLeftHighCountherr(true);
                    }else if("I".equals(level)){
                        miLeftHighNumFlag = false;
                        errMap.put(serviceCode + "miRightLowCaseCounth", maxMesMes);
                        fdto.setDoMiLeftHighCountherr(false);
                    }else if("A".equals(level)){
                        mjLeftHighNumFlag = false;
                        errMap.put(serviceCode + "mjRightLowCaseCounth",maxMesMes);
                        fdto.setDoMjLeftHighCountherr(false);
                    }
                }
            }
        } catch (Exception e) {
            if("C".equals(level)){
                caLeftHighNumFlag = false;
                errMap.put(serviceCode + "caRightLowCaseCounth", maxMesMes);
                fdto.setDoCaLeftHighCountherr(true);
            }else if("I".equals(level)){
                miLeftHighNumFlag = false;
                errMap.put(serviceCode + "miRightLowCaseCounth", maxMesMes);
                fdto.setDoMiLeftHighCountherr(true);
             }else if("A".equals(level)){
                mjLeftHighNumFlag = false;
                errMap.put(serviceCode + "mjRightLowCaseCounth", maxMesMes);
                fdto.setDoMjLeftHighCountherr(true);
            }
            log.error(e.getMessage(), e);
        }
        try {
            if(!StringUtil.isEmpty(inRightMod)){
                inRightModNum = Integer.valueOf(inRightMod);
                if(inRightModNum +1 != inLeftHighNum){
                    if(caLeftHighNumFlag&&"C".equals(level)){
                        errMap.put(serviceCode + "caRightLowCaseCounth", "RSM_ERR014");
                        fdto.setDoCaLeftHighCountherr(true);
                    }else if(miLeftHighNumFlag&&"I".equals(level)){
                        errMap.put(serviceCode + "miRightLowCaseCounth", "RSM_ERR014");
                        fdto.setDoMiLeftHighCountherr(true);
                    }else if(mjLeftHighNumFlag&&"A".equals(level)){
                        errMap.put(serviceCode + "mjRightLowCaseCounth", "RSM_ERR014");
                        fdto.setDoMjLeftHighCountherr(true);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
    public void inRightLowVad(Map<String, String> errMap,String inRightLow,String inLeftMod,String serviceCode,String level,HcsaRiskInspectionMatrixDto fdto){
        String maxMesMes = MessageUtil.getMessageDesc("GENERAL_ERR0027") + HcsaLicenceBeConstant.ERROR_MESSAGE_MAX_NC;
        Integer inRightLowNum = 0;
        boolean caRightLowNumFlag = true;
        boolean miRightLowNumFlag = true;
        boolean mjRightLowNumFlag = true;
        try {
            if(!StringUtil.isEmpty(inRightLow)){
                inRightLowNum = Integer.valueOf(inRightLow);
                if (inRightLowNum > HcsaLicenceBeConstant.RISK_Validate_HIGH_MAX || inRightLowNum < 0) {
                    if("C".equals(level)){
                        caRightLowNumFlag = false;
                        errMap.put(serviceCode + "caRightLowCaseCounth",  maxMesMes);
                        fdto.setDoCaRightLowCountherr(true);
                    }else if("I".equals(level)){
                        miRightLowNumFlag = false;
                        errMap.put(serviceCode + "miRightLowCaseCounth",  maxMesMes);
                        fdto.setDoMiRightLowCountherr(true);
                    }else if("A".equals(level)){
                        mjRightLowNumFlag = false;
                        errMap.put(serviceCode + "mjRightLowCaseCounth",  maxMesMes);
                        fdto.setDoMjRightLowCountherr(true);
                    }
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            if("C".equals(level)){
                caRightLowNumFlag = false;
                errMap.put(serviceCode + "caRightLowCaseCounth",  maxMesMes);
                fdto.setDoCaRightLowCountherr(true);
            }else if("I".equals(level)){
                miRightLowNumFlag = false;
                errMap.put(serviceCode + "miRightLowCaseCounth",  maxMesMes);
                fdto.setDoMiRightLowCountherr(true);
            }else if("A".equals(level)){
                mjRightLowNumFlag = false;
                errMap.put(serviceCode + "mjRightLowCaseCounth", maxMesMes);
                fdto.setDoMiRightLowCountherr(true);
            }
            log.error(e.getMessage(), e);
        }
        try {
            if(!StringUtil.isEmpty(inLeftMod)){
                int inLeftModNum = Integer.parseInt(inLeftMod);
                if( inLeftModNum != (inRightLowNum)  && inLeftModNum -1 != inRightLowNum ){
                    if("C".equals(level)){
                        if(caRightLowNumFlag){
                            errMap.put(serviceCode + "caRightLowCaseCounth", "RSM_ERR015");
                            fdto.setDoCaRightLowCountherr(true);
                        }
                    }else if("I".equals(level)){
                        if(miRightLowNumFlag){
                            errMap.put(serviceCode + "miRightLowCaseCounth", "RSM_ERR015");
                            fdto.setDoMiRightLowCountherr(true);
                        }
                    }else if(mjRightLowNumFlag&&"A".equals(level)){
                        errMap.put(serviceCode + "mjRightLowCaseCounth", "RSM_ERR015");
                        fdto.setDoMjRightLowCountherr(true);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    public void inLeftModVadAndinRightModVad(Map<String, String> errMap,String inLeftMod,String inRightMod,String serviceCode,String level,HcsaRiskInspectionMatrixDto fdto){
        String mess = MessageUtil.getMessageDesc("GENERAL_ERR0027");
        String maxMesMes = mess + HcsaLicenceBeConstant.ERROR_MESSAGE_MAX_NC;
        String minMesMes =  mess + HcsaLicenceBeConstant.ERROR_MESSAGE_MIN_NC;
        Integer inLeftModNum = 0;
        Integer inRightModNum = 0;
        int numberFlag = 0;
        if(!StringUtil.isEmpty(inLeftMod)){
            try {
                inLeftModNum = Integer.valueOf(inLeftMod);
                if(inLeftModNum<=0||inLeftModNum>HcsaLicenceBeConstant.RISK_Validate_HIGH_MAX){
                    if("C".equals(level)){
                        errMap.put(serviceCode+"caLeftModCaseCounth", minMesMes);
                        fdto.setDoCaLeftModCountherr(true);
                    }else if("I".equals(level)){
                        errMap.put(serviceCode+"miLeftModCaseCounth", minMesMes);
                        fdto.setDoMiLeftModCountherr(true);
                    }else if("A".equals(level)){
                        errMap.put(serviceCode+"mjLeftModCaseCounth", minMesMes);
                        fdto.setDoMjLeftModCountherr(true);
                    }
                }
                numberFlag++;
            }catch (Exception e){
                if("C".equals(level)){
                    errMap.put(serviceCode+"caLeftModCaseCounth", minMesMes);
                    fdto.setDoCaLeftModCountherr(true);
                }else if("I".equals(level)){
                    errMap.put(serviceCode+"miLeftModCaseCounth", minMesMes);
                    fdto.setDoMiLeftModCountherr(true);
                }else if("A".equals(level)){
                    errMap.put(serviceCode+"mjLeftModCaseCounth", minMesMes);
                    fdto.setDoMjLeftModCountherr(true);
                }
                log.error(e.getMessage(), e);
            }
        }
        if(!StringUtil.isEmpty(inRightMod)){
            try {
                inRightModNum = Integer.valueOf(inRightMod);
                if(inRightModNum<=0 || inRightModNum >HcsaLicenceBeConstant.RISK_Validate_HIGH_MAX){
                    if("C".equals(level)){
                        errMap.put(serviceCode+"caRightModCaseCounth", maxMesMes);
                        fdto.setDoCaRightModCountherr(true);
                    }else if("I".equals(level)){
                        errMap.put(serviceCode+"miRightModCaseCounth", maxMesMes);
                        fdto.setDoMiRightModCountherr(true);
                    }else if("A".equals(level)){
                        errMap.put(serviceCode+"mjRightModCaseCounth", maxMesMes);
                        fdto.setDoMjRightModCountherr(true);
                    }
                }
                numberFlag++;
            }catch (Exception e){
                    if("C".equals(level)){
                        errMap.put(serviceCode+"caRightModCaseCounth", maxMesMes);
                        fdto.setDoCaRightModCountherr(true);
                    }else if("I".equals(level)){
                        errMap.put(serviceCode+"miRightModCaseCounth", maxMesMes);
                        fdto.setDoMiRightModCountherr(true);
                    }else if("A".equals(level)){
                        errMap.put(serviceCode+"mjRightModCaseCounth", maxMesMes);
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
            errMap.put(serviceCode+"caRightModCaseCounth","RSM_ERR011");
            fdto.setDoCaRightModCountherr(true);
            fdto.setDoCaLeftModCountherr(true);
        }else if(lm>rm&&"I".equals(level)){
            errMap.put(serviceCode+"miRightModCaseCounth","RSM_ERR011");
            fdto.setDoMiRightModCountherr(true);
            fdto.setDoMiLeftModCountherr(true);
        }else if(lm>rm&&"A".equals(level)){
            errMap.put(serviceCode+"mjRightModCaseCounth","RSM_ERR011");
            fdto.setDoMjRightModCountherr(true);
            fdto.setDoMjLeftModCountherr(true);
        }
    }
    public void mandatoryCaseCounthVad(Map<String, String> errMap, HcsaRiskInspectionMatrixDto fdto){
        //ca
        String ncMinNoMes = MessageUtil.replaceMessage("GENERAL_ERR0006",HcsaLicenceBeConstant.ERROR_MESSAGE_MIN_NC_NO_SPACE,"field");
        String ncMaxNoMes =  MessageUtil.replaceMessage("GENERAL_ERR0006",HcsaLicenceBeConstant.ERROR_MESSAGE_MAX_NC_NO_SPACE,"field");
        Map<String,String> params = IaisCommonUtils.genNewHashMap(2);
        params.put("field","Minimum Number of NCs");
        params.put("maxlength",String.valueOf(MIN_VALUE_LENGTH));
        String ERR0041Min =  MessageUtil.getMessageDesc("GENERAL_ERR0041",params);

        Map<String,String> params2 = IaisCommonUtils.genNewHashMap(2);
        params.put("field","Maximum Number of NCs");
        params.put("maxlength",String.valueOf(MAX_VALUE_LENGTH));
        String ERR0041Max =  MessageUtil.getMessageDesc("GENERAL_ERR0041",params2);

        if(StringUtil.isEmpty(fdto.getDoCaLeftModCounth())){
            errMap.put(fdto.getSvcCode()+"caLeftModCaseCounth",ncMinNoMes);
            fdto.setDoCaLeftModCountherr(true);
        }else if(fdto.getDoCaLeftModCounth().length()> MIN_VALUE_LENGTH){
            errMap.put(fdto.getSvcCode()+"caLeftModCaseCounth", ERR0041Min);
            fdto.setDoCaLeftModCountherr(true);
        }

        if(StringUtil.isEmpty(fdto.getDoCaLeftHighCounth())){
            errMap.put(fdto.getSvcCode()+"caLeftHighCaseCounth",ncMinNoMes);
            fdto.setDoCaLeftHighCountherr(true);
        }else if(fdto.getDoCaLeftHighCounth().length() > MIN_VALUE_LENGTH){
            errMap.put(fdto.getSvcCode()+"caLeftHighCaseCounth", ERR0041Min);
            fdto.setDoCaLeftHighCountherr(true);
        }

        if(StringUtil.isEmpty(fdto.getDoCaRightLowCounth())){
            errMap.put(fdto.getSvcCode()+"caRightLowCaseCounth",ncMaxNoMes);
            fdto.setDoCaRightLowCountherr(true);
        }else if (fdto.getDoCaRightLowCounth().length() > MAX_VALUE_LENGTH ){
            errMap.put(fdto.getSvcCode()+"caRightLowCaseCounth", ERR0041Max);
            fdto.setDoCaRightLowCountherr(true);
        }

        if(StringUtil.isEmpty(fdto.getDoCaRightModCounth())){
            errMap.put(fdto.getSvcCode()+"caRightModCaseCounth",ncMaxNoMes);
            fdto.setDoCaRightModCountherr(true);
        }else if (fdto.getDoCaRightModCounth().length() >  MAX_VALUE_LENGTH ){
            errMap.put(fdto.getSvcCode()+"caRightModCaseCounth", ERR0041Max);
            fdto.setDoCaRightModCountherr(true);
        }

        //mi

        if(StringUtil.isEmpty(fdto.getDoMiRightModCounth())){
            errMap.put(fdto.getSvcCode()+"miRightModCaseCounth",ncMaxNoMes);
            fdto.setDoMiRightModCountherr(true);
        }else if(fdto.getDoMiRightModCounth().length() > MIN_VALUE_LENGTH){
            errMap.put(fdto.getSvcCode()+"miRightModCaseCounth", ERR0041Min);
            fdto.setDoMiRightModCountherr(true);
        }

        if(StringUtil.isEmpty(fdto.getDoMiRightLowCounth())){
            errMap.put(fdto.getSvcCode()+"miRightLowCaseCounth",ncMaxNoMes);
            fdto.setDoMiRightLowCountherr(true);
        }else if(fdto.getDoMiRightLowCounth().length() >  MIN_VALUE_LENGTH){
            errMap.put(fdto.getSvcCode()+"miRightLowCaseCounth", ERR0041Min);
            fdto.setDoMiRightLowCountherr(true);
        }

        if(StringUtil.isEmpty(fdto.getDoMiLeftHighCounth())){
            errMap.put(fdto.getSvcCode()+"miLeftHighCaseCounth",ncMinNoMes);
            fdto.setDoMiLeftHighCountherr(true);
        }else if(fdto.getDoMiLeftHighCounth().length() > MAX_VALUE_LENGTH){
            errMap.put(fdto.getSvcCode()+"miLeftHighCaseCounth", ERR0041Max);
            fdto.setDoMiLeftHighCountherr(true);
        }

        if(StringUtil.isEmpty(fdto.getDoMiLeftModCounth())){
            errMap.put(fdto.getSvcCode()+"miLeftModCaseCounth",ncMinNoMes);
            fdto.setDoMiLeftModCountherr(true);
        }else if(fdto.getDoMiLeftModCounth().length()> MAX_VALUE_LENGTH){
            errMap.put(fdto.getSvcCode()+"miLeftModCaseCounth", ERR0041Max);
            fdto.setDoMiLeftModCountherr(true);
        }

        //mj
        if(StringUtil.isEmpty(fdto.getDoMjRightModCounth())){
            errMap.put(fdto.getSvcCode()+"mjRightModCaseCounth",ncMaxNoMes);
            fdto.setDoMjRightModCountherr(true);
        }else if(fdto.getDoMjRightModCounth().length() >MIN_VALUE_LENGTH){
            errMap.put(fdto.getSvcCode()+"mjRightModCaseCounth", ERR0041Min);
            fdto.setDoMjRightModCountherr(true);
        }

        if(StringUtil.isEmpty(fdto.getDoMjRightLowCounth())){
            errMap.put(fdto.getSvcCode()+"mjRightLowCaseCounth",ncMaxNoMes);
            fdto.setDoMjRightLowCountherr(true);
        }else if(fdto.getDoMjRightLowCounth().length() > MIN_VALUE_LENGTH){
            errMap.put(fdto.getSvcCode()+"mjRightLowCaseCounth", ERR0041Min);
            fdto.setDoMjRightLowCountherr(true);
        }

        if(StringUtil.isEmpty(fdto.getDoMjLeftHighCounth())){
            errMap.put(fdto.getSvcCode()+"mjLeftHighCaseCounth",ncMaxNoMes);
            fdto.setDoMjLeftHighCountherr(true);
        }else if(fdto.getDoMjLeftHighCounth().length()>MAX_VALUE_LENGTH){
            errMap.put(fdto.getSvcCode()+"mjLeftHighCaseCounth", ERR0041Max);
            fdto.setDoMjLeftHighCountherr(true);
        }

        if(StringUtil.isEmpty(fdto.getDoMjLeftModCounth())){
            errMap.put(fdto.getSvcCode()+"mjLeftModCaseCounth",ncMinNoMes);
            fdto.setDoMjLeftModCountherr(true);
        }else if(fdto.getDoMjLeftModCounth().length() >MAX_VALUE_LENGTH ){
            errMap.put(fdto.getSvcCode()+"mjLeftModCaseCounth", ERR0041Max);
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
