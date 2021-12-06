package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.risk.RiskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskFeSupportDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskWeightageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskWeightageShowDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.HcsaRiskSupportBeService;
import com.ecquaria.cloud.moh.iais.service.HcsaRiskWeightageService;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import java.util.Date;
import java.util.List;

import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sop.util.CopyUtil;

/**
 * @Author: jiahao
 * @Date: 2019/12/26 13:29
 */
@Service
@Slf4j
public class HcsaRiskWeightageServiceImpl implements HcsaRiskWeightageService {
    @Autowired
    HcsaConfigClient hcsaConfigClient;
    @Autowired
    private HcsaRiskSupportBeService hcsaRiskSupportBeService;

    @Override
    public HcsaRiskWeightageShowDto getWeightage() {
        return hcsaConfigClient.getWeightageShow(hcsaRiskSupportBeService.getNameSortHcsaServiceDtos()).getEntity();
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
            if(!wDto.getBaseEffectiveDate().equals(inStartDate) && !(StringUtil.isEmpty(wDto.getBaseEffectiveDate()) && StringUtil.isEmpty(inStartDate))){
                editNum++;
            }
            wDto.setDoEffectiveDate(inStartDate);
            if(!wDto.getBaseEndDate().equals(inEndDate)  && !(StringUtil.isEmpty(wDto.getBaseEndDate()) && StringUtil.isEmpty(inEndDate))){
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
                wDto.setEdit(true);
                wDto.setDoLastInp(lastInp);
                wDto.setDoSecLastInp(secLastInp);
                wDto.setDoFinancial(finan);
                wDto.setDoLeadship(leadership);
                wDto.setDoLegislative(legislative);
                wDto.setDoEffectiveDate(inStartDate);
                wDto.setDoEndDate(inEndDate);
            }else{
                wDto.setEdit(false);
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
        HcsaRiskWeightageShowDto saveShowDto = null;
        try {
            saveShowDto = (HcsaRiskWeightageShowDto)CopyUtil.copyMutableObject(wShowDto);
        }catch (CloneNotSupportedException C){
            log.error(C.getMessage(),C);
        }
        List<HcsaRiskWeightageDto> saveList = IaisCommonUtils.genNewArrayList();
        HcsaRiskWeightageDto sWeiDto;
        for(HcsaRiskWeightageDto temp:weightageDtoList){
            if(temp.isEdit()){
                sWeiDto = getWeiDto(temp,RiskConsts.LAST_INSPECTION);
                saveList.add(sWeiDto);
                sWeiDto = getWeiDto(temp,RiskConsts.SEC_LASTINSPECTION);
                saveList.add(sWeiDto);
                sWeiDto = getWeiDto(temp,RiskConsts.FINANCIAL_SCHEME_AUDIT);
                saveList.add(sWeiDto);
                sWeiDto = getWeiDto(temp,RiskConsts.LEADERSHIP_AND_GOVERNANCE);
                saveList.add(sWeiDto);
                sWeiDto = getWeiDto(temp,RiskConsts.LEGISLATIVE_BREACHES);
                saveList.add(sWeiDto);
            }
        }
        if(saveShowDto!=null&&saveShowDto.getWeightageDtoList()!=null){
            FeignResponseEntity<List<HcsaRiskWeightageDto>> responseEntity = doUpdate(saveList,saveShowDto.getWeightageDtoList());
            HcsaRiskFeSupportDto supportDto = new HcsaRiskFeSupportDto();
            supportDto.setHcsaRiskWeightageShowDto(wShowDto);
            supportDto.setWeightageFlag(true);
            supportDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            hcsaRiskSupportBeService.sysnRiskSaveEic(responseEntity.getStatusCode(),supportDto);
        }
    }

    private HcsaRiskWeightageDto getWeiDto(HcsaRiskWeightageDto temp,String type) {
        HcsaRiskWeightageDto dto = null;
        try {
            dto = (HcsaRiskWeightageDto)CopyUtil.copyMutableObject(temp);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        try {
            if(dto!=null){
                if(RiskConsts.LAST_INSPECTION.equals(type)){
                    dto.setRiskWeightage(Double.parseDouble(temp.getDoLastInp()));
                    dto.setRiskComponent(RiskConsts.LAST_INSPECTION);
                }else if(RiskConsts.SEC_LASTINSPECTION.equals(type)){
                    dto.setRiskWeightage(Double.parseDouble(temp.getDoSecLastInp()));
                    dto.setRiskComponent(RiskConsts.SEC_LASTINSPECTION);
                }else if(RiskConsts.FINANCIAL_SCHEME_AUDIT.equals(type)){
                    dto.setRiskWeightage(Double.parseDouble(temp.getDoFinancial()));
                    dto.setRiskComponent(RiskConsts.FINANCIAL_SCHEME_AUDIT);
                }else if(RiskConsts.LEADERSHIP_AND_GOVERNANCE.equals(type)){
                    dto.setRiskWeightage(Double.parseDouble(temp.getDoLeadship()));
                    dto.setRiskComponent(RiskConsts.LEADERSHIP_AND_GOVERNANCE);
                }else if(RiskConsts.LEGISLATIVE_BREACHES.equals(type)){
                    dto.setRiskWeightage(Double.parseDouble(temp.getDoLegislative()));
                    dto.setRiskComponent(RiskConsts.LEGISLATIVE_BREACHES);
                }
                dto.setEndDate(Formatter.parseDate(temp.getDoEndDate()));
                dto.setEffectiveDate(Formatter.parseDate(temp.getDoEffectiveDate()));
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return dto;
    }

    public FeignResponseEntity<List<HcsaRiskWeightageDto>> doUpdate(List<HcsaRiskWeightageDto> updateList, List<HcsaRiskWeightageDto> weightageDtoList){
        for(HcsaRiskWeightageDto temp:weightageDtoList){
            List<HcsaRiskWeightageDto> weightageLeastVersionList = hcsaConfigClient.getWeightageRiskBySvcCode(temp.getServiceCode()).getEntity();
            if(temp.isEdit()){
                weightageLeastVersionList = updateLastVersion(weightageLeastVersionList,temp);
                if( weightageLeastVersionList != null &&  weightageLeastVersionList.size() > 0)
                hcsaConfigClient.updateWeightageMatrixList(weightageLeastVersionList);
            }
        }
        for(HcsaRiskWeightageDto temp:updateList){
            if(StringUtil.isEmpty(temp.getId())){
                temp.setVersion(1);
            }else{
                temp.setVersion(temp.getVersion()+1);
            }
            temp.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            temp.setId(null);
        }
        return hcsaConfigClient.saveWeightageMatrixList(updateList);
    }

    private List<HcsaRiskWeightageDto> updateLastVersion(List<HcsaRiskWeightageDto> weightageLeastVersionList, HcsaRiskWeightageDto temp) {
       if( weightageLeastVersionList == null || weightageLeastVersionList .size() == 0) return  weightageLeastVersionList;
        HcsaRiskWeightageDto wei = weightageLeastVersionList.get(0);
        String status = null;
        try {
            Date doeffDate = Formatter.parseDate(temp.getDoEffectiveDate());
            if(wei.getEndDate() != null && wei.getEndDate().getTime()<System.currentTimeMillis()){
                status = "CMSTAT003";
            }else{
                status = "CMSTAT001";
            }

            for(HcsaRiskWeightageDto weightage :weightageLeastVersionList){
                weightage.setEndDate(doeffDate);
                weightage.setStatus(status);
                weightage.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return weightageLeastVersionList;
    }

    @Override
    public boolean compareVersionsForRiskWeightage(HcsaRiskWeightageShowDto needSaveDto, HcsaRiskWeightageShowDto dbSearchDto) {
        if(needSaveDto == null || IaisCommonUtils.isEmpty(needSaveDto.getWeightageDtoList())){
            return false;
        }else {
            if(dbSearchDto == null || IaisCommonUtils.isEmpty(dbSearchDto.getWeightageDtoList())){
                return true;
            }
            List<HcsaRiskWeightageDto> weightageDtoList = needSaveDto.getWeightageDtoList();
            List<HcsaRiskWeightageDto> weightageDtoListDb = dbSearchDto.getWeightageDtoList();
            for(HcsaRiskWeightageDto hcsaRiskWeightageDto : weightageDtoList){
                for(HcsaRiskWeightageDto hcsaRiskWeightageDtoDb : weightageDtoListDb){
                    if(hcsaRiskWeightageDto.getServiceCode().equalsIgnoreCase(hcsaRiskWeightageDtoDb.getServiceCode())){
                        if( !hcsaRiskSupportBeService.versionSameForRisk(hcsaRiskWeightageDto.getVersion(),hcsaRiskWeightageDtoDb.getVersion())){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
}
