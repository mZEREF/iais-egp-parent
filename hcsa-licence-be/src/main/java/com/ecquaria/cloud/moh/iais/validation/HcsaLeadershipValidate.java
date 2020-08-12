package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskLeadershipMatrixDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskLeaderShipShowDto;
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
 * @Date: 2019/12/24 13:27
 */
@Slf4j
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
            errMap.put("All","RSM_ERR001");
        }
        mergeList(editList,financeList);
        WebValidationHelper.saveAuditTrailForNoUseResult(errMap);
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
        String serviceCode = fdto.getSvcCode();
        String GENERAL_ERR0006Threshold = MessageUtil.replaceMessage("GENERAL_ERR0006","Threshold","field");
        if(StringUtil.isEmpty(fdto.getAdThershold())){
            errMap.put(serviceCode+"inThershold",GENERAL_ERR0006Threshold);
            fdto.setAdThersholderr(true);
        }else{
            try {
                Integer thold = Integer.valueOf(fdto.getAdThershold());
                if(thold<0 || thold>999){
                    errMap.put(serviceCode+"inThershold","GENERAL_ERR0027");
                    fdto.setAdThersholderr(true);
                }
            }catch (Exception e){
                errMap.put(serviceCode+"inThershold","GENERAL_ERR0027");
                fdto.setAdThersholderr(true);
            }
        }
        if(StringUtil.isEmpty(fdto.getDpThershold())){
            errMap.put(serviceCode+"prThershold",GENERAL_ERR0006Threshold);
            fdto.setDpThersholderr(true);
        }else{
            try {
                Integer thold = Integer.valueOf(fdto.getDpThershold());
                if(thold<0 || thold>999){
                    errMap.put(serviceCode+"prThershold","GENERAL_ERR0027");
                    fdto.setDpThersholderr(true);
                }
            }catch (Exception e){
                errMap.put(serviceCode+"prThershold","GENERAL_ERR0027");
                fdto.setDpThersholderr(true);
            }
        }
    }

    public void caseCounthVad(Map<String, String> errMap,HcsaRiskLeadershipMatrixDto fdto){
        mandatoryCaseCounthVad(errMap,fdto);
        numberCaseCounthVad(errMap,fdto);
    }
    public void numberCaseCounthVad(Map<String, String> errMap,HcsaRiskLeadershipMatrixDto fdto){
        String serviceCode = fdto.getSvcCode();
        inLeftModVadAndinRightModVad(errMap,fdto.getAdLeftModCaseCounth(),fdto.getAdRightModCaseCounth(),serviceCode,true,fdto);
        inLeftModVadAndinRightModVad(errMap,fdto.getDpLeftModCaseCounth(),fdto.getDpRightModCaseCounth(),serviceCode,false,fdto);
        inRightLowVad(errMap,fdto.getAdRightLowCaseCounth(),fdto.getAdLeftModCaseCounth(),serviceCode,true,fdto);
        inRightLowVad(errMap,fdto.getDpRightLowCaseCounth(),fdto.getDpLeftModCaseCounth(),serviceCode,false,fdto);
        inLeftHighVad(errMap,fdto.getAdLeftHighCaseCounth(),fdto.getAdRightModCaseCounth(),serviceCode,true,fdto);
        inLeftHighVad(errMap,fdto.getDpLeftHighCaseCounth(),fdto.getDpRightModCaseCounth(),serviceCode,false,fdto);

    }
    public void inLeftModVadAndinRightModVad(Map<String, String> errMap,String inLeftMod,String inRightMod,String serviceCode,boolean isIn,HcsaRiskLeadershipMatrixDto fdto){
        String maxCaseMes = MessageUtil.getMessageDesc("GENERAL_ERR0027") + HcsaLicenceBeConstant.ERROR_MESSAGE_MAX_CASES;
        String minCaseMes = MessageUtil.getMessageDesc("GENERAL_ERR0027") + HcsaLicenceBeConstant.ERROR_MESSAGE_MIN_CASES;
        Integer inLeftModNum = 0;
        Integer inRightModNum = 0;
        int numberFlag = 0;
        if(!StringUtil.isEmpty(inLeftMod)){
            try {
                inLeftModNum = Integer.valueOf(inLeftMod);
                if(inLeftModNum<= 0||inLeftModNum>999){
                    if(isIn){
                        errMap.put(serviceCode+"inLeftModCaseCounth",minCaseMes);
                        fdto.setAdLeftModCaseCountherr(true);
                    }else{
                        errMap.put(serviceCode+"prLeftModCaseCounth",minCaseMes);
                        fdto.setDpLeftModCaseCountherr(true);
                    }
                }
                numberFlag++;
            }catch (Exception e){
                if(isIn){
                    errMap.put(serviceCode+"inLeftModCaseCounth",minCaseMes);
                    fdto.setAdLeftModCaseCountherr(true);
                }else{
                    errMap.put(serviceCode+"prLeftModCaseCounth",minCaseMes);
                    fdto.setDpLeftModCaseCountherr(true);
                }
                log.error(e.getMessage(), e);
            }
        }
        if(!StringUtil.isEmpty(inRightMod)){
            try {
                inRightModNum = Integer.valueOf(inRightMod);
                if(inRightModNum<=0 || inRightModNum >999){
                    if(isIn){
                        errMap.put(serviceCode+"inRightModCaseCounth",maxCaseMes);
                        fdto.setAdRightModCaseCountherr(true);
                    }else{
                        errMap.put(serviceCode+"prRightModCaseCounth",maxCaseMes);
                        fdto.setDpRightModCaseCountherr(true);
                    }
            }
                numberFlag++;
            }catch (Exception e){
                if(isIn){
                    errMap.put(serviceCode+"inRightModCaseCounth",maxCaseMes);
                    fdto.setAdRightModCaseCountherr(true);
                }else{
                    errMap.put(serviceCode+"prRightModCaseCounth",maxCaseMes);
                    fdto.setDpRightModCaseCountherr(true);
                }
                log.error(e.getMessage(), e);
            }
        }
        if(numberFlag == 2){
            numberOrderVad(errMap,inLeftModNum,inRightModNum,serviceCode,isIn,fdto);
        }
    }
    public void numberOrderVad(Map<String, String> errMap,Integer lm,Integer rm,String serviceCode,boolean isIn,HcsaRiskLeadershipMatrixDto fdto){
        if(lm>rm&&isIn){
            errMap.put(serviceCode+"inRightModCaseCounth","RSM_ERR011");
            fdto.setAdRightModCaseCountherr(true);
        }else if(lm>rm&&!isIn){
            errMap.put(serviceCode+"prRightModCaseCounth","RSM_ERR011");
            fdto.setDpRightModCaseCountherr(true);
        }
    }
    public void inLeftHighVad(Map<String, String> errMap,String inLeftHigh,String inRightMod, String serviceCode,boolean isIn,HcsaRiskLeadershipMatrixDto fdto){
        String minCaseMes = MessageUtil.getMessageDesc("GENERAL_ERR0027") + HcsaLicenceBeConstant.ERROR_MESSAGE_MIN_CASES;
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
                        errMap.put(serviceCode + "inLeftHighCaseCounth", minCaseMes);
                        fdto.setAdLeftHighCaseCountherr(true);
                    }else{
                        prLeftHighNumFlag = false;
                        errMap.put(serviceCode + "prLeftHighCaseCounth",minCaseMes);
                        fdto.setDpLeftHighCaseCountherr(true);
                    }
                }
            }
        } catch (Exception e) {
            if(isIn){
                inLeftHighNumFlag = false;
                errMap.put(serviceCode + "inLeftHighCaseCounth", minCaseMes);
                fdto.setAdLeftHighCaseCountherr(true);
            }else{
                prLeftHighNumFlag = false;
                errMap.put(serviceCode + "prLeftHighCaseCounth", minCaseMes);
                fdto.setDpLeftHighCaseCountherr(true);
            }
            log.error(e.getMessage(), e);
        }
        try {
            if(!StringUtil.isEmpty(inRightMod)){
                inRightModNum = Integer.valueOf(inRightMod);
                if(inRightModNum +1 != inLeftHighNum){
                    if(isIn&&inLeftHighNumFlag){
                        errMap.put(serviceCode + "inLeftHighCaseCounth", "High Maximum cases and Moderate Minimax can only differ by 1");
                        fdto.setAdLeftHighCaseCountherr(true);
                    }else if(!isIn&&prLeftHighNumFlag){
                        errMap.put(serviceCode + "prLeftHighCaseCounth", "High Maximum cases and Moderate Minimax can only differ by 1");
                        fdto.setDpLeftHighCaseCountherr(true);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
    public void inRightLowVad(Map<String, String> errMap,String inRightLow,String inLeftMod,String serviceCode,boolean isIn,HcsaRiskLeadershipMatrixDto fdto){
        String maxCaseMes = MessageUtil.getMessageDesc("GENERAL_ERR0027") + HcsaLicenceBeConstant.ERROR_MESSAGE_MAX_CASES;
        Integer inRightLowNum = 0;
        Integer inLeftModNum = 0;
        boolean inRightLowNumFlag = true;
        boolean prRightLowNumFlag = true;
        try {
            if(!StringUtil.isEmpty(inRightLow)){
                inRightLowNum = Integer.valueOf(inRightLow);
                if (inRightLowNum > 999 || inRightLowNum < 0) {
                    if(isIn){
                        inRightLowNumFlag = false;
                        errMap.put(serviceCode + "inRightLowCaseCounth", maxCaseMes);
                        fdto.setAdRightLowCaseCountherr(true);
                    }else{
                        prRightLowNumFlag = false;
                        errMap.put(serviceCode + "prRightLowCaseCounth", maxCaseMes);
                        fdto.setDpRightLowCaseCountherr(true);
                    }
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            if(isIn){
                if(inRightLowNumFlag){
                    errMap.put(serviceCode + "inRightLowCaseCounth", maxCaseMes);
                    fdto.setAdRightModCaseCountherr(true);
                }
            }else{
                if(prRightLowNumFlag){
                    errMap.put(serviceCode + "prRightLowCaseCounth", maxCaseMes);
                    fdto.setDpRightLowCaseCountherr(true);
                }
            }

            log.error(e.getMessage(), e);
        }
        try {
            if(!StringUtil.isEmpty(inLeftMod)){
                inLeftModNum = Integer.valueOf(inLeftMod);
                if(inLeftModNum -1 != inRightLowNum && !inLeftModNum.equals(inRightLowNum)){
                    if(isIn){
                        errMap.put(serviceCode + "inRightLowCaseCounth", "RSM_ERR015");
                        fdto.setAdRightLowCaseCountherr(true);
                    }else{
                        errMap.put(serviceCode + "prRightLowCaseCounth", "RSM_ERR015");
                        fdto.setDpRightLowCaseCountherr(true);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    public void mandatoryCaseCounthVad(Map<String, String> errMap,HcsaRiskLeadershipMatrixDto fdto){
        String serviceCode = fdto.getSvcCode();
        String minNCNoEmpty = MessageUtil.replaceMessage("GENERAL_ERR0006",HcsaLicenceBeConstant.ERROR_MESSAGE_MIN_CASES_NO_SPACE,"field");
        String maxNCNoEmpty = MessageUtil.replaceMessage("GENERAL_ERR0006",HcsaLicenceBeConstant.ERROR_MESSAGE_MAX_CASES_NO_SPACE,"field");
        //in
        if(StringUtil.isEmpty(fdto.getAdLeftModCaseCounth())){
            errMap.put(serviceCode+"inLeftModCaseCounth",minNCNoEmpty);
            fdto.setAdLeftModCaseCountherr(true);
        }
        if(StringUtil.isEmpty(fdto.getAdRightModCaseCounth())){
            errMap.put(serviceCode+"inRightModCaseCounth",maxNCNoEmpty);
            fdto.setAdRightModCaseCountherr(true);
        }
        if(StringUtil.isEmpty(fdto.getAdRightLowCaseCounth())){
            fdto.setAdRightLowCaseCountherr(true);
            errMap.put(serviceCode+"inRightLowCaseCounth",maxNCNoEmpty);
        }
        if(StringUtil.isEmpty(fdto.getAdLeftHighCaseCounth())){
            fdto.setAdLeftHighCaseCountherr(true);
            errMap.put(serviceCode+"inLeftHighCaseCounth",minNCNoEmpty);
        }
        //pr
        if(StringUtil.isEmpty(fdto.getDpLeftModCaseCounth())){
            fdto.setDpLeftModCaseCountherr(true);
            errMap.put(serviceCode+"prLeftModCaseCounth",minNCNoEmpty);
        }
        if(StringUtil.isEmpty(fdto.getDpRightModCaseCounth())){
            fdto.setDpRightModCaseCountherr(true);
            errMap.put(serviceCode+"prRightModCaseCounth",maxNCNoEmpty);
        }
        if(StringUtil.isEmpty(fdto.getDpRightLowCaseCounth())){
            fdto.setDpRightLowCaseCountherr(true);
            errMap.put(serviceCode+"prRightLowCaseCounth",maxNCNoEmpty);
        }
        if(StringUtil.isEmpty(fdto.getDpLeftHighCaseCounth())){
            fdto.setDpLeftHighCaseCountherr(true);
            errMap.put(serviceCode+"prLeftHighCaseCounth",minNCNoEmpty);
        }
    }

    public void dateVad(Map<String, String> errMap,HcsaRiskLeadershipMatrixDto fdto){
        String serviceCode = fdto.getSvcCode();
        String inEffDate = fdto.getAdEffectiveStartDate();
        String inEndDate = fdto.getAdEffectiveEndDate();
        String prEffDate = fdto.getDpEffectiveStartDate();
        String prEndDate = fdto.getDpEffectiveEndDate();
        boolean inDateFormatVad = doDateFormatVad(errMap,inEffDate,inEndDate,serviceCode,true,fdto);
        boolean prDateFormatVad = doDateFormatVad(errMap,prEffDate,prEndDate,serviceCode,false,fdto);
        if(inDateFormatVad&&fdto.isAdIsEdit()){
            doDateLogicVad(errMap,fdto,true);
        }
        if(prDateFormatVad&&fdto.isDpIsEdit()){
            doDateLogicVad(errMap,fdto,false);
        }

    }
    public void doDateLogicVad(Map<String, String> errMap,HcsaRiskLeadershipMatrixDto fdto,boolean isIn){
        //effdate least version <
        String serviceCode = fdto.getSvcCode();
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
    public void doSpecialDateFlag(Map<String, String> errMap,HcsaRiskLeadershipMatrixDto fdto,boolean isIn){
        String serviceCode = fdto.getSvcCode();
        try {
            Date inEffDate = Formatter.parseDate(fdto.getAdEffectiveStartDate());
         //   Date inEndDate = Formatter.parseDate(fdto.getAdEffectiveEndDate());
            Date prEffDate = Formatter.parseDate(fdto.getDpEffectiveStartDate());
          //  Date prEndDate = Formatter.parseDate(fdto.getDpEffectiveEndDate());
            Date baseInEffDate = Formatter.parseDate(fdto.getBaseAdEffectiveStartDate());
          //  Date baseInEndDate = Formatter.parseDate(fdto.getBaseAdEffectiveEndDate());
            Date basePrEffDate = Formatter.parseDate(fdto.getBaseDpEffectiveStartDate());
          //  Date basePrEndDate = Formatter.parseDate(fdto.getBaseDpEffectiveEndDate());
            if(inEffDate.getTime()<baseInEffDate.getTime()&&isIn){
                errMap.put(serviceCode + "inEffDate", "RSM_ERR018");
                fdto.setAdEffectiveStartDateerr(true);
            }
            if(prEffDate.getTime()<basePrEffDate.getTime()&&!isIn){
                errMap.put(serviceCode + "prEffDate", "RSM_ERR018");
                fdto.setDpEffectiveStartDateerr(true);
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }

    public boolean doUsualDateVad(Date effDate,Date endDate,String serviceCode,Map<String, String> errMap,boolean isIn,int inEdit,int prEdit,HcsaRiskLeadershipMatrixDto fdto){
        String err0012 = MessageUtil.replaceMessage("RSM_ERR012","Effective Start Date","Effective Date");
        boolean flag = true;
        if (effDate.getTime() < System.currentTimeMillis()) {
            flag = false;
            if(isIn){
                if(inEdit == 1){
                    errMap.put(serviceCode + "inEffDate",err0012);
                    fdto.setAdEffectiveStartDateerr(true);
                }
            }else {
                if(prEdit == 1){
                    errMap.put(serviceCode + "prEffDate", err0012);
                    fdto.setDpEffectiveEndDateerr(true);
                }
            }
        } else if (endDate.getTime() < effDate.getTime()) {
            flag = false;
            if(isIn){
                if(inEdit == 1){
                    errMap.put(serviceCode + "inEndDate", "RSM_ERR017");
                    fdto.setAdEffectiveEndDateerr(true);
                }
            }else{
                if(prEdit == 1){
                    errMap.put(serviceCode + "prEndDate", "RSM_ERR017");
                    fdto.setDpEffectiveEndDateerr(true);
                }
            }
        }
        return flag;
    }
    public boolean doDateFormatVad(Map<String, String> errMap,String strEffDate,String strEndDate,String serviceCode,boolean isIn,HcsaRiskLeadershipMatrixDto fdto){
        String mesStartEmpty  = MessageUtil.replaceMessage("GENERAL_ERR0006","Effective Start Date","field");
        String mesStartRepalce  = MessageUtil.replaceMessage("RSM_ERR016","Effective Start Date","replaceArea");
        String mesEndEmpty  = MessageUtil.replaceMessage("GENERAL_ERR0006","Effective End Date","field");
        String mesEndRepalce  = MessageUtil.replaceMessage("RSM_ERR016","Effective End Date","replaceArea");
        boolean vadFlag = true;
        if(StringUtil.isEmpty(strEffDate)){
            vadFlag = false;
            if(isIn){
                errMap.put(serviceCode+"inEffDate",mesStartEmpty);
                fdto.setAdEffectiveStartDateerr(true);
            }else{
                errMap.put(serviceCode+"prEffDate",mesStartEmpty);
                fdto.setDpEffectiveStartDateerr(true);
            }
        }else{
            try {
                Formatter.parseDate(strEffDate);
            }catch (Exception e){
                if(isIn){
                    errMap.put(serviceCode+"inEffDate",mesStartRepalce);
                    fdto.setAdEffectiveStartDateerr(true);
                }else{
                    errMap.put(serviceCode+"prEffDate",mesStartRepalce);
                    fdto.setDpEffectiveStartDateerr(true);
                }
                vadFlag = false;
            }
        }
        if(StringUtil.isEmpty(strEndDate)){
            vadFlag = false;
            if(isIn){
                errMap.put(serviceCode+"inEndDate",mesEndEmpty);
                fdto.setAdEffectiveEndDateerr(true);
            }else{
                errMap.put(serviceCode+"prEndDate",mesEndEmpty);
                fdto.setDpEffectiveEndDateerr(true);
            }
        }else{
            try {
                Formatter.parseDate(strEndDate);
            }catch (Exception e){
                if(isIn){
                    errMap.put(serviceCode+"inEndDate",mesEndRepalce);
                    fdto.setAdEffectiveEndDateerr(true);
                }else{
                    errMap.put(serviceCode+"prEndDate",mesEndRepalce);
                    fdto.setDpEffectiveEndDateerr(true);
                }
                return false;
            }
        }
        return vadFlag;
    }

}
