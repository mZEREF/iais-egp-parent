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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author: jiahao
 * @Date: 2020/1/7 14:38
 */
public class HcsaLicTenVadlidate implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        LicenceTenShowDto showDto = (LicenceTenShowDto)ParamUtil.getSessionAttr(request,"tenShowDto");
        Map<String, String> errMap = new HashMap<>();
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
                errMap.put("notEidt","please do some change");
            }
        }
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
        int timeFlagNum = 0;
        int itFlagNum = 0;
        int maxAndMinFlagNum = 0;
        for(SubLicenceTenureDto temp:subList){
            boolean timeFlag = timeTypeVad(temp,errMap,fdto.getSvcCode());
            if(!timeFlag){
                timeFlagNum++;
            }
            boolean ltFlag = ltVad(temp,errMap,fdto.getSvcCode());
            if(!ltFlag){
                itFlagNum++;
            }
            boolean f= maxAndMinVad(temp,errMap,fdto.getSvcCode());
            if(f){
                maxAndMinFlagNum++;
            }
        }
        if(timeFlagNum==0&&itFlagNum==0&&maxAndMinFlagNum==0){
            sortVad(subList,errMap,fdto.getSvcCode());
        }
    }

    private void sortVad(List<SubLicenceTenureDto> subList, Map<String, String> errMap, String svcCode) {
        boolean ltSortFlag = doLtsort(subList);
        if(!ltSortFlag){
            errMap.put(svcCode+"ltsort","Please sort in chronological order.");
        }
        boolean maxSortFlag = doMaxSortList(subList);
        if(!maxSortFlag){
            errMap.put(svcCode+"maxsort","Please follow the order from small to large");
        }
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
            temp.setLicenceTenureNum(Integer.parseInt(temp.getLicenceTenure()));
        }
        if(monthList!=null&&!monthList.isEmpty()){
            monthList.sort((SubLicenceTenureDto s1,SubLicenceTenureDto s2)->s1.getLicenceTenureNum().compareTo(s2.getLicenceTenureNum()));
            for(SubLicenceTenureDto temp:monthList){
                ltSortList.add(temp);
            }
        }
        for(SubLicenceTenureDto temp:yearList){
            temp.setLicenceTenureNum(Integer.parseInt(temp.getLicenceTenure()));
        }
        if(yearList!=null&&!yearList.isEmpty()){
            yearList.sort((SubLicenceTenureDto s1,SubLicenceTenureDto s2)->s1.getLicenceTenureNum().compareTo(s2.getLicenceTenureNum()));
            for(SubLicenceTenureDto temp:yearList){
                ltSortList.add(temp);
            }
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
        try {
            if(StringUtil.isEmpty(temp.getColumRight())){
                errMap.put(svcCode+temp.getOrderNum()+"righterr","The Filed is mandatory.");
                numfalg = false;
            }else{
               Double rightnum =  Double.parseDouble(temp.getColumRight());
               if(rightnum<0||rightnum>999){
                   errMap.put(svcCode+temp.getOrderNum()+"righterr","Invalid Number.");
               }
            }
            if(StringUtil.isEmpty(temp.getColumRight())){
                errMap.put(svcCode+temp.getOrderNum()+"lefterr","The Filed is mandatory.");
                numfalg = false;
            }else{
                Double leftnum =  Double.parseDouble(temp.getColumLeft());
                numfalg = false;
                if(leftnum<0||leftnum>999){
                    errMap.put(svcCode+temp.getOrderNum()+"lefterr","Invalid Number.");
                }
            }
        }catch (Exception e){
            numfalg = false;
        }
        if(numfalg){
            Double rightnum =  Double.parseDouble(temp.getColumRight());
            Double leftnum =  Double.parseDouble(temp.getColumLeft());
            if(rightnum<leftnum){
                errMap.put(svcCode+temp.getOrderNum()+"righterr","Manimun should be granter than Minimun");
                numfalg = false;
            }
        }
        return numfalg;
    }

    private boolean timeTypeVad(SubLicenceTenureDto temp, Map<String, String> errMap, String svcCode) {
        if(StringUtil.isEmpty(temp.getDateType())){
            errMap.put(svcCode+temp.getOrderNum()+"timeerr","The Filed is mandatory.");
            return false;
        }
        return true;
    }

    private boolean ltVad(SubLicenceTenureDto temp,Map<String, String> errMap,String svcCode) {
        boolean flag = true;
        try {
            if(StringUtil.isEmpty(temp.getLicenceTenure())){
                errMap.put(svcCode+temp.getOrderNum()+"lterr","The Filed is mandatory.");
                flag = false;
            }else{
                Integer num = Integer.parseInt(temp.getLicenceTenure());
                if(RiskConsts.MONTH.equals(temp.getDateType())){
                    if(num<0||num>13){
                        errMap.put(svcCode+temp.getOrderNum()+"lterr","Invalid Number.");
                        flag = false;
                    }
                }else{
                    if(num<0||num>99){
                        errMap.put(svcCode+temp.getOrderNum()+"lterr","Invalid Number.");
                        flag = false;
                    }
                }
            }
        }catch (Exception e){
            errMap.put(svcCode+temp.getOrderNum()+"lterr","Invalid Number.");
            flag = false;
            e.printStackTrace();
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
            errMap.put(serviceCode+"inEffDate","EffectiveDate is mandatory");
        }else{
            try {
                Formatter.parseDate(strEffDate);
            }catch (Exception e){
                errMap.put(serviceCode+"inEffDate","Date Format Error");
                vadFlag = false;
            }
        }
        if(StringUtil.isEmpty(strEndDate)){
            vadFlag = false;
            errMap.put(serviceCode+"inEndDate","EndDate is mandatory");
        }else{
            try {
                Formatter.parseDate(strEndDate);
            }catch (Exception e){
                vadFlag = false;
                errMap.put(serviceCode+"inEndDate","Date Format Error");
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
            e.printStackTrace();
        }
    }

    public boolean doUsualDateVad(Date effDate,Date endDate,String serviceCode,Map<String, String> errMap,int inEdit,int prEdit){
        boolean flag = true;
        if (effDate.getTime() < System.currentTimeMillis()) {
            flag = false;
            if (inEdit == 1) {
                errMap.put(serviceCode + "inEffDate", "EffectiveDate should be furture time");
            }
        } else if (endDate.getTime() < effDate.getTime()) {
            flag = false;
            if (inEdit == 1) {
                errMap.put(serviceCode + "inEndDate", "EffectiveDate should be ealier than EndDate");
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
                errMap.put(fdto.getSvcCode() + "inEffDate", "EffectiveDate should later than Previous version");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
