package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.constant.risk.RiskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskLicenceTenureDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.LicenceTenShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.SubLicenceTenureDto;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: jiahao
 * @Date: 2020/1/7 14:38
 */
@Slf4j
public class HcsaLicTenVadlidate implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        LicenceTenShowDto showDto = (LicenceTenShowDto)ParamUtil.getSessionAttr(request,"tenShowDto");
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        if(showDto.isAddFlag()){
            addMaxVad(showDto,errMap);
        }else{
            List<HcsaRiskLicenceTenureDto> ltDtoList =  showDto.getLicenceTenureDtoList();
            List<HcsaRiskLicenceTenureDto> updateList = IaisCommonUtils.genNewArrayList();
            if(ltDtoList!=null){
                for(HcsaRiskLicenceTenureDto temp:ltDtoList){
                    if(temp.isEdit()){
                        updateList.add(temp);
                    }
                }
            }
            if(updateList!=null&&!updateList.isEmpty()){
                for(HcsaRiskLicenceTenureDto fdto:updateList){
                    dateVad(errMap, fdto);
                    subVad(errMap, fdto);
                }
            }else{
                errMap.put("All","RSM_ERR003");
            }
        }
        WebValidationHelper.saveAuditTrailForNoUseResult(errMap);
        return errMap;
    }

    private void addMaxVad(LicenceTenShowDto showDto, Map<String, String> errMap) {
        String svcCode = showDto.getAddSvcCode();
        if(showDto.getLicenceTenureDtoList()!=null&&!showDto.getLicenceTenureDtoList().isEmpty()){
            for(HcsaRiskLicenceTenureDto temp:showDto.getLicenceTenureDtoList()){
                if(temp.getSvcCode().equals(svcCode)){
                    if(temp.getSubDtoList()!=null&&!temp.getSubDtoList().isEmpty()){
                        if(temp.getSubDtoList().size()>=6){
                            errMap.put(svcCode+"maxSubList","The number of cloum can only add up to six.");
                        }
                    }
                }
            }
        }
    }

    private void subVad(Map<String, String> errMap, HcsaRiskLicenceTenureDto fdto) {
        List<SubLicenceTenureDto> subList = fdto.getSubDtoList();
        int itFlagNum = 0;
        int maxAndMinFlagNum = 0;
        if( subList== null || subList.size() ==0){
            errMap.put(fdto.getSvcCode()+"maxAddList","Please add Licence Tenure Configuration for " + fdto.getServiceName());
            return;
        }
        for(SubLicenceTenureDto temp:subList){
            boolean ltFlag = ltVad(temp,errMap,fdto.getSvcCode());
            if(!ltFlag){
                itFlagNum++;
            }
            boolean f= maxAndMinVad(temp,errMap,fdto.getSvcCode());
            if(!f){
                maxAndMinFlagNum++;
            }
        }
        if(itFlagNum==0&&maxAndMinFlagNum==0){
            sortVad(subList,errMap,fdto.getSvcCode());
        }
    }

    private void sortVad(List<SubLicenceTenureDto> subList, Map<String, String> errMap, String svcCode) {
        boolean ltSortFlag = doLtsort(subList);
        /*if(!ltSortFlag){
          errMap.put(svcCode+"ltsort","Please sort in chronological order.");
        }*/
        boolean maxSortFlag = doMaxSortList(subList);
        /*if(!maxSortFlag){
           errMap.put(svcCode+"maxsort","Please follow the order from small to large");
        }*/
        boolean minSortFlag = doMinSortList(subList);
        if(ltSortFlag&&maxSortFlag&&minSortFlag){
            doMaxAndMinSort(subList,errMap,svcCode);
        }
    }

    private void doMaxAndMinSort(List<SubLicenceTenureDto> subList, Map<String, String> errMap,String svcCode) {
        for (int i=0;i<subList.size();i++){
            if(i>=1&&i<subList.size()-2){
                if(!subList.get(i).getColumLeft().equals(subList.get(i-1).getColumRight())){
                    errMap.put(svcCode+"maxminsort","Min should be equal before Max.");
                }
            }
        }
    }

    private boolean doMinSortList(List<SubLicenceTenureDto> subList) {
        List<SubLicenceTenureDto> minList = IaisCommonUtils.genNewArrayList();
        CopyUtil.copyMutableObjectList(subList,minList);
        minList.sort((SubLicenceTenureDto s1,SubLicenceTenureDto s2)->s1.getColumRight().compareTo(s2.getColumRight()));
        for(int i=0;i<subList.size();i++){
            if(!subList.get(i).getColumRight().equals(minList.get(i).getColumRight())){
                return false;
            }
        }
        return true;
    }

    private boolean doMaxSortList( List<SubLicenceTenureDto> subList) {
        List<SubLicenceTenureDto> maxList = IaisCommonUtils.genNewArrayList();
        CopyUtil.copyMutableObjectList(subList,maxList);
        maxList.sort((SubLicenceTenureDto s1,SubLicenceTenureDto s2)->s1.getColumRight().compareTo(s2.getColumRight()));
        for(int i=0;i<subList.size();i++){
            if(!subList.get(i).getColumRight().equals(maxList.get(i).getColumRight())){
                return false;
            }
        }
        return true;
    }

    private boolean doLtsort(List<SubLicenceTenureDto> subList) {
        List<SubLicenceTenureDto> monthList = IaisCommonUtils.genNewArrayList();
        List<SubLicenceTenureDto> yearList = IaisCommonUtils.genNewArrayList();
        List<SubLicenceTenureDto> ltSortList = IaisCommonUtils.genNewArrayList();
        for(SubLicenceTenureDto temp:subList){
            if(RiskConsts.MONTH.equals(temp.getDateType())){
                monthList.add(temp);
            }else if(RiskConsts.YEAR.equals(temp.getDateType())){
                yearList.add(temp);
            }
        }
        for(SubLicenceTenureDto temp:monthList){
            temp.setLicenceTenureNum(Integer.valueOf(temp.getLicenceTenure()));
        }
        if(!monthList.isEmpty()){
            monthList.sort((SubLicenceTenureDto s1,SubLicenceTenureDto s2)->s1.getLicenceTenureNum().compareTo(s2.getLicenceTenureNum()));
            ltSortList.addAll(monthList);
        }
        for(SubLicenceTenureDto temp:yearList){
            temp.setLicenceTenureNum(Integer.valueOf(temp.getLicenceTenure()));
        }
        if(!yearList.isEmpty()){
            yearList.sort((SubLicenceTenureDto s1,SubLicenceTenureDto s2)->s1.getLicenceTenureNum().compareTo(s2.getLicenceTenureNum()));
            ltSortList.addAll(yearList);
        }
        for(int i=0;i<subList.size();i++){
            if(!subList.get(i).getLicenceTenure().equals(ltSortList.get(i).getLicenceTenure())){
                return false;
            }
        }
        return true;
    }


    private boolean maxAndMinVad(SubLicenceTenureDto temp, Map<String, String> errMap, String svcCode) {
        //mandatory
        boolean numfalg = true;
            if(StringUtil.isEmpty(temp.getColumRight())){
                errMap.put(svcCode+"righterr",MessageUtil.replaceMessage("ERR0009","Maximum Risk Score","The field"));
                numfalg = false;
            }else{
                if(!StringUtil.stringIsFewDecimal(temp.getColumRight(),2)){
                    errMap.put(svcCode+"righterr",MessageUtil.getMessageDesc("ERR0013") + "for Maximum Risk Score");
                    numfalg = false;
                }
            }

            if(StringUtil.isEmpty(temp.getColumLeft())){
                errMap.put(svcCode+"lefterr",MessageUtil.replaceMessage("ERR0009","Minimum Risk Score","The field"));
                numfalg = false;
            }else{
                if(!StringUtil.stringIsFewDecimal(temp.getColumLeft(),2)){
                    errMap.put(svcCode+"lefterr",MessageUtil.replaceMessage("ERR0013","Minimum Risk Score" ,"The field"));
                    numfalg = false;
                }
            }

        if(numfalg){
            Double rightnum =  Double.valueOf(temp.getColumRight());
            Double leftnum =  Double.valueOf(temp.getColumLeft());
            if(rightnum<leftnum){
                errMap.put(svcCode+"righterr","Maximum should be granter than Minimum in the same row");
                numfalg = false;
            }
        }
        return numfalg;
    }


    private boolean ltVad(SubLicenceTenureDto temp,Map<String, String> errMap,String svcCode) {
        boolean flag = true;
        Integer numyear =0;
        Integer numMonth =0;
        try {
            if(StringUtil.isEmpty(temp.getYearNum())){
                errMap.put(svcCode+"lterr",MessageUtil.replaceMessage("ERR0009","Licence Tenure Year(s)","The field"));
                flag = false;
            }
            else{
                numyear = Integer.valueOf(temp.getYearNum());
                    if(numyear<0||numyear>5){
                        errMap.put(svcCode+"lterr","Licence Tenure should be less than 5 years");
                        flag = false;
                    }
                  }
            if(StringUtil.isEmpty(temp.getMonthNum())){
                errMap.put(svcCode+"timeerr",MessageUtil.replaceMessage("ERR0009","Licence Tenure Month(s)","The field"));
                flag = false;
            }
            else {
                numMonth = Integer.valueOf(temp.getMonthNum());
                if( numMonth <0|| numMonth>13){
                    errMap.put(svcCode+"timeerr","Licence Tenure should be less than 12 months");
                    flag = false;
                }
            }
            if(flag){
                temp.setLicenceTenure(String.valueOf(numyear*12+numMonth));
            }
        }catch (Exception e){
            errMap.put(svcCode+"lterr","ERR0013");
            flag = false;
            log.error(e.getMessage(), e);
        }
        return flag;
    }

    public void dateVad(Map<String, String> errMap, HcsaRiskLicenceTenureDto fdto){
        String inEffDate = fdto.getDoEffectiveDate();
        String inEndDate = fdto.getDoEndDate();
        boolean inDateFormatVad = doDateFormatVad(errMap,inEffDate,inEndDate,fdto.getSvcCode());
        if(inDateFormatVad&&fdto.isEdit()){
            doDateLogicVad(errMap,fdto,true);
        }
    }

    public boolean doDateFormatVad(Map<String, String> errMap,String strEffDate,String strEndDate,String serviceCode){
        boolean vadFlag = true;
        if(StringUtil.isEmpty(strEffDate)){
            vadFlag = false;
            errMap.put(serviceCode+"inEffDate",MessageUtil.replaceMessage("ERR0009","Effective Start Date","The field"));
        }else{
            try {
                Formatter.parseDate(strEffDate);
            }catch (Exception e){
                errMap.put(serviceCode+"inEffDate",MessageUtil.replaceMessage("ERR0017","Effective Start Date","replaceArea"));
                vadFlag = false;
            }
        }
        if(StringUtil.isEmpty(strEndDate)){
            vadFlag = false;
            errMap.put(serviceCode+"inEndDate",MessageUtil.replaceMessage("ERR0009","Effective End Date","The field"));
        }else{
            try {
                Formatter.parseDate(strEndDate);
            }catch (Exception e){
                vadFlag = false;
                errMap.put(serviceCode+"inEndDate",MessageUtil.replaceMessage("ERR0017","Effective End Date","replaceArea"));
                return false;
            }
        }
        return vadFlag;
    }

    public void doDateLogicVad(Map<String, String> errMap, HcsaRiskLicenceTenureDto fdto, boolean isIn){
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
                errMap.put(serviceCode + "inEndDate", "ERR0016");
            }
        }
        return flag;
    }

    public void doSpecialDateFlag(Map<String, String> errMap,HcsaRiskLicenceTenureDto fdto){
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


}
