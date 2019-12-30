package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.risk.RiskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskWeightageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskWeightageShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.HcsaRiskWeightageService;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: jiahao
 * @Date: 2019/12/26 13:29
 */
@Service
@Slf4j
public class HcsaRiskWeightageServiceImpl implements HcsaRiskWeightageService {
    @Autowired
    HcsaConfigClient hcsaConfigClient;

    @Override
    public HcsaRiskWeightageShowDto getWeightage() {
        List<HcsaServiceDto> serviceDtoList =  hcsaConfigClient.getActiveServices().getEntity();
        HcsaRiskWeightageShowDto showDto = hcsaConfigClient.getWeightageShow(serviceDtoList).getEntity();
        return showDto;
    }

    @Override
    public void getOneWdto(HcsaRiskWeightageDto wDto, String lastInp, String secLastInp, String finan, String leadership, String legislative, String inStartDate, String inEndDate) {
        int editNum = 0;
        if(!StringUtil.isEmpty(wDto.getId())){
            if(!wDto.getBaseLastInp().equals(lastInp)){
                editNum++;
            }
            wDto.setDoLastInp(lastInp);
            if(!wDto.getBaseSecLastInp().equals(secLastInp)){
                editNum++;
            }
            wDto.setDoSecLastInp(secLastInp);
            if(!wDto.getBaseFinancial().equals(finan)){
                editNum++;
            }
            wDto.setDoFinancial(finan);
            if(!wDto.getBaseLeadship().equals(leadership)){
                editNum++;
            }
            wDto.setDoLeadship(leadership);
            if(!wDto.getBaseLegislative().equals(legislative)){
                editNum++;
            }
            wDto.setDoLegislative(legislative);
            if(!wDto.getBaseEffectiveDate().equals(inStartDate)){
                editNum++;
            }
            wDto.setDoEffectiveDate(inStartDate);
            if(!wDto.getBaseEndDate().equals(inEndDate)){
                editNum++;
            }
            wDto.setDoEndDate(inEndDate);
            if(editNum>0){
                wDto.setEdit(true);
            }else{
                wDto.setEdit(false);
            }
        }else{
            boolean editfalg =isEdit(lastInp,secLastInp,finan,leadership,legislative,inStartDate,inEndDate);
            if(editfalg){
                wDto.setDoLastInp(lastInp);
                wDto.setDoSecLastInp(secLastInp);
                wDto.setDoFinancial(finan);
                wDto.setDoLeadship(leadership);
                wDto.setDoLegislative(legislative);
                wDto.setDoEffectiveDate(inStartDate);
                wDto.setDoEndDate(inEndDate);
            }
        }
    }
    boolean isEdit(String lastInp, String secLastInp, String finan, String leadership, String legislative, String inStartDate, String inEndDate){
        if(StringUtil.isEmpty(lastInp)&&StringUtil.isEmpty(secLastInp)&&StringUtil.isEmpty(finan)&&StringUtil.isEmpty(leadership)&&StringUtil.isEmpty(legislative)&&StringUtil.isEmpty(inStartDate)
                &&StringUtil.isEmpty(inEndDate)){
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void saveDto(HcsaRiskWeightageShowDto wShowDto) {
        List<HcsaRiskWeightageDto> weightageDtoList = wShowDto.getWeightageDtoList();
        List<HcsaRiskWeightageDto> saveList = new ArrayList<>();
        for(HcsaRiskWeightageDto temp:weightageDtoList){
            if(temp.isEdit()){
                getWeiDto(temp,RiskConsts.LAST_INSPECTION);
                saveList.add(temp);
                getWeiDto(temp,RiskConsts.SEC_LASTINSPECTION);
                saveList.add(temp);
                getWeiDto(temp,RiskConsts.FINANCIAL_SCHEME_AUDIT);
                saveList.add(temp);
                getWeiDto(temp,RiskConsts.LEADERSHIP_AND_GOVERNANCE);
                saveList.add(temp);
                getWeiDto(temp,RiskConsts.LEGISLATIVE_BREACHES);
                saveList.add(temp);
            }
        }
        doUpdate(saveList);
    }

    private void getWeiDto(HcsaRiskWeightageDto temp,String type) {
        try {
            if(RiskConsts.LAST_INSPECTION.equals(type)){
                temp.setRiskWeightage(Double.parseDouble(temp.getDoLastInp()));
            }else if(RiskConsts.SEC_LASTINSPECTION.equals(type)){
                temp.setRiskWeightage(Double.parseDouble(temp.getDoSecLastInp()));
            }else if(RiskConsts.FINANCIAL_SCHEME_AUDIT.equals(type)){
                temp.setRiskWeightage(Double.parseDouble(temp.getDoFinancial()));
            }else if(RiskConsts.LEADERSHIP_AND_GOVERNANCE.equals(type)){
                temp.setRiskWeightage(Double.parseDouble(temp.getDoLeadship()));
            }else if(RiskConsts.LEGISLATIVE_BREACHES.equals(type)){
                temp.setRiskWeightage(Double.parseDouble(temp.getDoLegislative()));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void doUpdate(List<HcsaRiskWeightageDto> updateList){
        for(HcsaRiskWeightageDto temp:updateList){
            List<HcsaRiskWeightageDto> weightageLeastVersionList = hcsaConfigClient.getWeightageRiskBySvcCode(temp.getServiceCode()).getEntity();
            updateLastVersion(weightageLeastVersionList,temp);
            hcsaConfigClient.updateWeightageMatrixList(weightageLeastVersionList);
        }
        for(HcsaRiskWeightageDto temp:updateList){
            temp.setId(null);
        }
        hcsaConfigClient.saveWeightageMatrixList(updateList);
    }

    private void updateLastVersion(List<HcsaRiskWeightageDto> weightageLeastVersionList, HcsaRiskWeightageDto temp) {
        HcsaRiskWeightageDto wei = weightageLeastVersionList.get(0);
        Date lastversionEndDate = null;
        String status = null;
        try {
            Date doeffDate = Formatter.parseDate(temp.getDoEffectiveDate());
            lastversionEndDate = doeffDate;
            if(wei.getEndDate().getTime()<System.currentTimeMillis()){
                status = "CMSTAT003";
            }else{
                status = "CMSTAT001";
            }

            for(HcsaRiskWeightageDto weightage :weightageLeastVersionList){
                weightage.setEndDate(lastversionEndDate);
                weightage.setStatus(status);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
