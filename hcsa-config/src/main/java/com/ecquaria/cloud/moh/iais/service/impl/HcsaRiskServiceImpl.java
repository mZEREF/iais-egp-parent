package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.constant.risk.RiskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskFinanceMatrixDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskFinancialShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.HcsaRiskService;
import lombok.extern.slf4j.Slf4j;
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
    @Override
    public RiskFinancialShowDto getfinancialShowDto(){
        String url = RestApiUrlConsts.HCSA_CONFIG + RestApiUrlConsts.HCSA_CONFIG_SERVICE+"/all-service";
        Map<String,Object> map = new HashMap();
        map.put("test","add");
        List<HcsaServiceDto> serviceDtoList =  RestApiUtil.getListByReqParam(url,map,HcsaServiceDto.class);
        Map<String,Object> mapTwo = new HashMap();
        RiskFinancialShowDto showDto = RestApiUtil.postGetObject("hcsa-config:8878/iais-hcsa-risk/FinancialShow",serviceDtoList,RiskFinancialShowDto.class);

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
            RestApiUtil.postGetObject("hcsa-config:8878/iais-hcsa-risk/FinanceMatrixMemoryStorage",saveList,HcsaRiskFinanceMatrixDto.class);
        }
        if(updateList!=null&&!updateList.isEmpty()){
            RestApiUtil.update("hcsa-config:8878/iais-hcsa-risk/FinanceMatrixMemoryUpdating",updateList,HcsaRiskFinanceMatrixDto.class);
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
        finDto.setEffectiveDate(new Date());
        finDto.setEffectiveDate(new Date());
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
}
