package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.constant.risk.RiskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskFinanceMatrixDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskFinancialShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.HcsaRiskService;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: jiahao
 * @Date: 2019/11/13 16:42
 */
@Service
@Slf4j
public class HcsaRiskServiceImpl implements HcsaRiskService {
    @Autowired
    HcsaConfigClient hcsaConfigClient;


    @Override
    public RiskFinancialShowDto getfinancialShowDto(){
        String url = RestApiUrlConsts.HCSA_CONFIG + RestApiUrlConsts.HCSA_CONFIG_SERVICE+"/all-service";
        Map<String,Object> map = new HashMap();
        map.put("test","add");
        //List<HcsaServiceDto> serviceDtoList =  RestApiUtil.getListByReqParam(url,map,HcsaServiceDto.class);
        List<HcsaServiceDto> serviceDtoList = hcsaConfigClient.getActiveServices().getEntity();
        Map<String,Object> mapTwo = new HashMap();
        RiskFinancialShowDto showDto  = hcsaConfigClient.getRiskFinShow(serviceDtoList).getEntity();
        //RiskFinancialShowDto showDto = RestApiUtil.postGetObject("hcsa-config:8878/iais-hcsa-risk/FinancialShow",serviceDtoList,RiskFinancialShowDto.class);
        //RiskFinancialShowDto showDto = new RiskFinancialShowDto();
        for(HcsaRiskFinanceMatrixDto temp:showDto.getFinanceList()){
            if(!StringUtil.isEmpty(temp.getId())){
                temp.setInEffectiveEndDate(temp.getBaseInEffectiveEndDate());
                temp.setInEffectiveStartDate(temp.getBaseInEffectiveStartDate());
                temp.setPrEffectiveEndDate(temp.getBasePrEffectiveEndDate());
                temp.setPrEffectiveStartDate(temp.getBasePrEffectiveStartDate());
            }
        }
        return showDto;
    }

    @Override
    public void saveDto(RiskFinancialShowDto dto) {
        List<HcsaRiskFinanceMatrixDto> dtoList = dto.getFinanceList();
        List<HcsaRiskFinanceMatrixDto> saveList = new ArrayList<>();
        List<HcsaRiskFinanceMatrixDto> updateList = new ArrayList<>();
        for(HcsaRiskFinanceMatrixDto temp : dtoList){
            if(temp.isInIsEdit()){
                saveList.add(getFinDto(temp,true,true));
                saveList.add(getFinDto(temp,false,true));
            }
            if(temp.isPrIsEdit()){
                saveList.add(getFinDto(temp,true,false));
                saveList.add(getFinDto(temp,false,false));
            }
        }
        for(HcsaRiskFinanceMatrixDto temp : dtoList){
            if(StringUtil.isEmpty(temp.getId())){
                if(isNeedUpdatePreviouds(temp,true)){
                    updateList.add(temp);
                }
                if(isNeedUpdatePreviouds(temp,false)){
                    updateList.add(temp);
                }
            }
        }
        //call api to save
        if(saveList!= null && !saveList.isEmpty()){
            //RestApiUtil.postGetObject("hcsa-config:8878/iais-hcsa-risk/FinanceMatrixMemoryStorage",saveList,HcsaRiskFinanceMatrixDto.class);

        }
        if(updateList!=null&&!updateList.isEmpty()){
            //RestApiUtil.update("hcsa-config:8878/iais-hcsa-risk/FinanceMatrixMemoryUpdating",updateList,HcsaRiskFinanceMatrixDto.class);

        }
    }
    public boolean isNeedUpdatePreviouds(HcsaRiskFinanceMatrixDto dto,boolean isIn){
        try {
            if(isIn){
                Date inEffDate = Formatter.parseDate(dto.getInEffectiveStartDate());
                Date inEndDate = Formatter.parseDate(dto.getInEffectiveEndDate());
                Date BaseInEffDate = Formatter.parseDate(dto.getBaseInEffectiveStartDate());
                Date BaseInEndDate = Formatter.parseDate(dto.getBaseInEffectiveEndDate());
                if(inEndDate.getTime()<BaseInEndDate.getTime()){
                    dto.setBaseInEffectiveEndDate(dto.getInEffectiveStartDate());
                    return true;
                }else{
                    return false;
                }
            }else{
                Date prEffDate = Formatter.parseDate(dto.getPrEffectiveStartDate());
                Date prEndDate = Formatter.parseDate(dto.getPrEffectiveEndDate());
                Date BasePrEffDate = Formatter.parseDate(dto.getBasePrEffectiveStartDate());
                Date BasePrEndDate = Formatter.parseDate(dto.getBasePrEffectiveEndDate());
                if(prEndDate.getTime()<BasePrEndDate.getTime()){
                    dto.setBasePrEffectiveEndDate(dto.getPrEffectiveStartDate());
                    return true;
                }else{
                    return false;
                }
            }


        }catch (Exception e){

            return false;
        }
    }
    public HcsaRiskFinanceMatrixDto getFinDto(HcsaRiskFinanceMatrixDto dto,boolean isLow,boolean isIn){
        HcsaRiskFinanceMatrixDto finDto = new HcsaRiskFinanceMatrixDto();
        finDto.setServiceCode(dto.getServiceCode());
        finDto.setStatus("CMSTAT001");
        if(StringUtil.isEmpty(dto.getVersion())){
            finDto.setVersion(1);
        }else{
            finDto.setVersion(dto.getVersion()+1);
        }
        Date effDate = null;
        Date endDate = null;
        if(isIn){
            try {
                effDate = Formatter.parseDate(dto.getInEffectiveStartDate());
                endDate = Formatter.parseDate(dto.getInEffectiveEndDate());
            }catch (Exception e){
                e.printStackTrace();
            }
            finDto.setEffectiveDate(effDate);
            finDto.setEndDate(endDate);
            if(isLow){
                finDto.setFinSource(RiskConsts.INSTITUTION);
                finDto.setThershold(Integer.parseInt(dto.getInThershold()));
                finDto.setCaseCountTh(Integer.parseInt(dto.getInLeftModCaseCounth()));
                finDto.setRiskRating(RiskConsts.LOW);
            }else{
                finDto.setFinSource(RiskConsts.INSTITUTION);
                finDto.setThershold(Integer.parseInt(dto.getInThershold()));
                finDto.setCaseCountTh(Integer.parseInt(dto.getInRightModCaseCounth()));
                finDto.setRiskRating(RiskConsts.HIGH);
            }
        }else{
            try {
                effDate = Formatter.parseDate(dto.getPrEffectiveStartDate());
                endDate = Formatter.parseDate(dto.getPrEffectiveEndDate());
            }catch (Exception e){
                e.printStackTrace();
            }
            finDto.setEffectiveDate(effDate);
            finDto.setEndDate(endDate);
            if(isLow){
                finDto.setFinSource(RiskConsts.PRACTITIONER);
                finDto.setThershold(Integer.parseInt(dto.getPrThershold()));
                finDto.setCaseCountTh(Integer.parseInt(dto.getPrLeftModCaseCounth()));
                finDto.setRiskRating(RiskConsts.LOW);
            }else{
                finDto.setFinSource(RiskConsts.PRACTITIONER);
                finDto.setThershold(Integer.parseInt(dto.getPrThershold()));
                finDto.setCaseCountTh(Integer.parseInt(dto.getPrRightModCaseCounth()));
                finDto.setRiskRating(RiskConsts.HIGH);
            }
        }
        return finDto;
    }
    @Override
    public void getOneFinDto(HcsaRiskFinanceMatrixDto fin,String prsource,String prthershold,
                             String prleftmod,String prlefthigh,String prrightlow,String prrightmod,
                             String insource,String inthershold,String inleftmod,String inlefthigh,String inrightlow,String inrightmod,
                             String inStartDate,String inEndDate,String prStartDate,String prEndDate){
        Integer isInEditNum = 0;
        Integer prInEditNum = 0;
        if(!StringUtil.isEmpty(fin.getFinSource())) {
            if ("SOURCE001".equals(fin.getInSource())) {
                if (!(fin.getInThershold() + "").equals(inthershold)) {
                    fin.setInThershold(inthershold);
                    isInEditNum++;
                }
                String baseRightLowCase = getRightLowCase(fin.getBaseInLowCaseCountth());
                if (!baseRightLowCase.equals(inrightlow)) {
                    fin.setInRightLowCaseCounth(inrightlow);
                    isInEditNum++;
                }

                if (!fin.getBaseInLowCaseCountth().equals(inleftmod)) {
                    fin.setInLeftModCaseCounth(inleftmod);
                    isInEditNum++;
                }
                if (!fin.getBaseInHighCaseCountth().equals(inrightmod)) {
                    fin.setInRightModCaseCounth(inrightmod);
                    isInEditNum++;
                }
                String baseLeftHighCase = getLeftHighCase(fin.getBaseInHighCaseCountth());
                if (!baseLeftHighCase.equals(inlefthigh)) {
                    fin.setInLeftHighCaseCount(inlefthigh);
                    isInEditNum++;
                }
                if(!fin.getBaseInEffectiveStartDate().equals(inStartDate)){
                    fin.setInEffectiveStartDate(inStartDate);
                    isInEditNum++;
                }
                if(!fin.getBaseInEffectiveEndDate().equals(inEndDate)){
                    fin.setInEffectiveEndDate(inEndDate);
                    isInEditNum++;
                }
                if (isInEditNum >=1) {
                    fin.setInIsEdit(true);
                } else {
                    fin.setInIsEdit(false);
                }
            }
            if ("SOURCE002".equals(fin.getPrSource())) {
                if (!(fin.getPrThershold() + "").equals(prthershold)) {
                    fin.setPrThershold(prthershold);
                    prInEditNum++;
                }
                String baseRightLowCase = getRightLowCase(fin.getBasePrLowCaseCountth());
                if (!baseRightLowCase.equals(prrightlow)) {
                    fin.setPrRightLowCaseCounth(prrightlow);
                    prInEditNum++;
                }
                if (!fin.getBasePrLowCaseCountth().equals(prleftmod)) {
                    fin.setPrLeftModCaseCounth(prleftmod);
                    prInEditNum++;
                }
                if (!fin.getBasePrHighCaseCountth().equals(prrightmod)) {
                    fin.setPrRightModCaseCounth(prrightmod);
                    prInEditNum++;
                }
                String baseLeftHighCase = getLeftHighCase(fin.getBasePrHighCaseCountth());
                if (!baseLeftHighCase.equals(prlefthigh)) {
                    fin.setPrLeftHighCaseCount(prlefthigh);
                    prInEditNum++;
                }
                if(!fin.getBasePrEffectiveStartDate().equals(prStartDate)){
                    fin.setPrEffectiveStartDate(prStartDate);
                    prInEditNum++;
                }
                if(!fin.getBasePrEffectiveEndDate().equals(prEndDate)){
                    fin.setPrEffectiveEndDate(prEndDate);
                    prInEditNum++;
                }
                if (prInEditNum >= 1) {
                    fin.setPrIsEdit(true);
                } else {
                    fin.setPrIsEdit(false);
                }
            }
        }else {
            boolean isInEdit = isNoBaseSourceIsEdit(fin, insource, inthershold, inrightlow, inleftmod, inlefthigh, inrightmod,inStartDate,inEndDate);
            if(isInEdit){
                fin.setInIsEdit(isInEdit);
                fin.setInSource(insource);
                fin.setInThershold(inthershold);
                fin.setInRightLowCaseCounth(inrightlow);
                fin.setInLeftModCaseCounth(inleftmod);
                fin.setInLeftHighCaseCount(inlefthigh);
                fin.setInRightModCaseCounth(inrightmod);
                fin.setInEffectiveStartDate(inStartDate);
                fin.setInEffectiveEndDate(inEndDate);
            }

            boolean isPrEdit = isNoBaseSourceIsEdit(fin, prsource, prthershold, prrightlow, prleftmod, prlefthigh, prrightmod,prStartDate,prEndDate);
            if(isPrEdit){
                fin.setPrIsEdit(isPrEdit);
                fin.setPrSource(prsource);
                fin.setPrThershold(prthershold);
                fin.setPrRightLowCaseCounth(prrightlow);
                fin.setPrLeftModCaseCounth(prleftmod);
                fin.setPrLeftHighCaseCount(prlefthigh);
                fin.setPrRightModCaseCounth(prrightmod);
                fin.setPrEffectiveStartDate(prStartDate);
                fin.setPrEffectiveEndDate(prEndDate);
            }
        }

    }

    public boolean isNoBaseSourceIsEdit(HcsaRiskFinanceMatrixDto fin,String source,String thershold,String rightlow,String leftmod,String lefthigh,String rightmod,String StartDate,String endDate){
        if(StringUtil.isEmpty(thershold)&&StringUtil.isEmpty(rightlow)&&StringUtil.isEmpty(leftmod)&&StringUtil.isEmpty(lefthigh)&&StringUtil.isEmpty(rightmod)) {
            return false;
        }
        return true;
    }
    public String getRightLowCase(String lowCaseCount){
        Integer num = 0;
        try {
            num = Integer.parseInt(lowCaseCount);
            num = num-1;
        } catch (Exception e) {
            return "";
        }
        return num+"";
    }
    public String getLeftHighCase(String highCaseCount){
        Integer num = 0;
        try {
            num = Integer.parseInt(highCaseCount);
            num = num+1;
        } catch (Exception e) {
            return "";
        }
        return num+"";
    }
}
