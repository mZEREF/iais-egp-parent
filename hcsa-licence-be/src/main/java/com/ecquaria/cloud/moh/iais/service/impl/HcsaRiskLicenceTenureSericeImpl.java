package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskFeSupportDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskLicenceTenureDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.LicenceTenShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.SubLicenceTenureDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.HcsaRiskLicenceTenureSerice;
import com.ecquaria.cloud.moh.iais.service.HcsaRiskSupportBeService;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;

import java.util.ArrayList;
import java.util.List;

import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: jiahao
 * @Date: 2020/1/3 17:00
 */
@Service
@Slf4j
public class HcsaRiskLicenceTenureSericeImpl implements HcsaRiskLicenceTenureSerice {
    @Autowired
    private HcsaConfigClient hcsaConfigClient;
    @Autowired
    private HcsaRiskSupportBeService hcsaRiskSupportBeService;

    @Override
    public LicenceTenShowDto getTenShowDto() {
        return hcsaConfigClient.getLicenceTenureShow(hcsaRiskSupportBeService.getNameSortHcsaServiceDtos()).getEntity();
    }

    @Override
    public List<SelectOption> getDateTypeOps() {
        String dataType[] = {"DTPE001","DTPE002"};
        List<SelectOption> ops = MasterCodeUtil.retrieveOptionsByCodes(dataType);
        return ops;
    }

    @Override
    public void remove(String removeVal, LicenceTenShowDto showDto) {
        char num = removeVal.charAt(removeVal.length()-1);
        String svcCode = removeVal.substring(0,removeVal.length()-1);
        List<SubLicenceTenureDto> subList = IaisCommonUtils.genNewArrayList();
        List<HcsaRiskLicenceTenureDto> tdDtoList = showDto.getLicenceTenureDtoList();
        for(HcsaRiskLicenceTenureDto temp:tdDtoList){
            if(temp.getSvcCode().equals(svcCode)){
                if(temp.getMaxSubOrderNum()>0){
                    temp.setMaxSubOrderNum(temp.getMaxSubOrderNum()-1);
                }
                subList = removeCol(num,temp.getSubDtoList());
                temp.setSubDtoList(subList);
            }
        }
        showDto.setLicenceTenureDtoList(tdDtoList);
    }

    private List<SubLicenceTenureDto> removeCol(char num, List<SubLicenceTenureDto> subDtoList) {
        List<SubLicenceTenureDto> removeList = IaisCommonUtils.genNewArrayList();
        int i=0;
        for(SubLicenceTenureDto temp:subDtoList){
            if(!(temp.getOrderNum()+"").equals(num+"")){
                temp.setOrderNum(i);
                removeList.add(temp);
                i++;
            }
        }
        return removeList;
    }

    @Override
    public void add(String svcCode, LicenceTenShowDto showDto) {
        List<HcsaRiskLicenceTenureDto> tdDtoList = showDto.getLicenceTenureDtoList();
        for(HcsaRiskLicenceTenureDto temp:tdDtoList){
            if(temp.getSvcCode().equals(svcCode)){
                temp.setMaxSubOrderNum(temp.getMaxSubOrderNum()+1);
                addValue(temp);
            }
        }
    }

    private void addValue(HcsaRiskLicenceTenureDto temp) {
        List<SubLicenceTenureDto> subList= temp.getSubDtoList();
        SubLicenceTenureDto subDto = new SubLicenceTenureDto();
        subDto.setEndDate(temp.getBaseEndDate());
        subDto.setEffDate(temp.getBaseEffectiveDate());
        if(subList!=null&&!subList.isEmpty()){
            subDto.setOrderNum(subList.get(subList.size()-1).getOrderNum()+1);
            subList.add(subDto);
        }else {
            subList = new ArrayList<>(1);
            subList.add(subDto);
            temp.setSubDtoList(subList);
        }
    }

    @Override
    public boolean doIsEditLogic(HcsaRiskLicenceTenureDto temp) {
        boolean flag = true;
        int flagNum = 0;
        if(!StringUtil.isEmpty(temp.getId())){
            if(!temp.getBaseEffectiveDate().equals(temp.getDoEffectiveDate()) && !(StringUtil.isEmpty(temp.getBaseEffectiveDate()) && StringUtil.isEmpty(temp.getDoEffectiveDate()))){
                return false;
            }else if(!temp.getBaseEndDate().equals(temp.getDoEndDate()) && !(StringUtil.isEmpty(temp.getBaseEndDate()) && StringUtil.isEmpty(temp.getDoEndDate()))){
                return false;
            }
            List<SubLicenceTenureDto> subDtoList = temp.getSubDtoList();
            List<SubLicenceTenureDto> baseSubDtoList = temp.getBaseSubDtoList();
            if(subDtoList!=null&&!baseSubDtoList.isEmpty()){
                if(baseSubDtoList.size()!=subDtoList.size()){
                    return false;
                }
                for(int i=0;i<subDtoList.size();i++){
                    SubLicenceTenureDto subDto = subDtoList.get(i);
                    SubLicenceTenureDto baseSubDto = baseSubDtoList.get(i);
                    if(!doSubEdit(subDto,baseSubDto)){
                        flagNum++;
                    }
                }
            }
            if(flagNum>0){
                flag = false;
            }
            return flag;
        }else{
            return  notIdEdit(temp);
        }
    }

    private boolean notIdEdit(HcsaRiskLicenceTenureDto temp) {
       boolean flag  = false;
       if(StringUtil.isEmpty(temp.getDoEndDate())&&StringUtil.isEmpty(temp.getEffectiveDate())){
           if(temp.getSubDtoList()!=null){
               if(temp.getSubDtoList().size()==0){
                   return true;
               }
           }
       }
       return flag;
    }

    private boolean doSubEdit(SubLicenceTenureDto subDto, SubLicenceTenureDto baseSubDto) {
        if(!baseSubDto.getColumLeft().equalsIgnoreCase(subDto.getColumLeft())){
            return false;
        }else if(!baseSubDto.getColumRight().equalsIgnoreCase(subDto.getColumRight())){
            return false;
        }else if(!baseSubDto.getDateType().equalsIgnoreCase(subDto.getDateType())){
            return false;
        }else if(!baseSubDto.getYearNum().equalsIgnoreCase(subDto.getYearNum())){
            return false;
        }else if(!baseSubDto.getMonthNum().equalsIgnoreCase(subDto.getMonthNum())){
            return false;
        }
        return true;
    }

    @Override
    public void saveDto(LicenceTenShowDto showDto) {
        List<HcsaRiskLicenceTenureDto> ltDtoList = showDto.getLicenceTenureDtoList();
        List<HcsaRiskLicenceTenureDto> saveDtoList = IaisCommonUtils.genNewArrayList();
        for(HcsaRiskLicenceTenureDto temp:ltDtoList){
            if(temp.getSubDtoList()!=null&&!temp.getSubDtoList().isEmpty()&&temp.isEdit()){
                for(SubLicenceTenureDto sub:temp.getSubDtoList()){
                    saveDtoList.add(getSaveDto(temp,sub));
                }
            }
        }
        FeignResponseEntity<List<HcsaRiskLicenceTenureDto>> responseEntity = doUpdate(saveDtoList,ltDtoList);
        HcsaRiskFeSupportDto supportDto = new HcsaRiskFeSupportDto();
        supportDto.setLicenceTenShowDto(showDto);
        supportDto.setLictenureFLag(true);
        supportDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        hcsaRiskSupportBeService.sysnRiskSaveEic(responseEntity.getStatusCode(),supportDto);
    }

    private FeignResponseEntity<List<HcsaRiskLicenceTenureDto>> doUpdate(List<HcsaRiskLicenceTenureDto> saveDtoList, List<HcsaRiskLicenceTenureDto> ltDtoList) {
        for(HcsaRiskLicenceTenureDto temp:ltDtoList){
            List<HcsaRiskLicenceTenureDto> lastversionList = getLastversionList(temp);
            if(lastversionList!=null &&!lastversionList.isEmpty()){
                for(HcsaRiskLicenceTenureDto lastversion:lastversionList){
                    lastversion.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    updateLastVersion(temp,lastversion);
                }
                hcsaConfigClient.updatehcsaRiskLicenceTenure(lastversionList).getEntity();
            }
        }
        for(HcsaRiskLicenceTenureDto temp:saveDtoList){
            temp.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            temp.setId(null);
            temp.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        }
       return hcsaConfigClient.savehcsaRiskLicenceTenure(saveDtoList);
    }

    public void updateLastVersion(HcsaRiskLicenceTenureDto newFin, HcsaRiskLicenceTenureDto dbFin){
        try {
            dbFin.setEndDate(Formatter.parseDate(newFin.getDoEffectiveDate()));
            if (dbFin.getEndDate().getTime() < System.currentTimeMillis()) {
                dbFin.setStatus("CMSTAT003");
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }

    }

    private List<HcsaRiskLicenceTenureDto> getLastversionList(HcsaRiskLicenceTenureDto temp) {
        List<HcsaRiskLicenceTenureDto> lastVersionList = hcsaConfigClient.getgetLictenureByCode(temp.getSvcCode()).getEntity();
        List<HcsaRiskLicenceTenureDto> returnList = IaisCommonUtils.genNewArrayList();
        for(HcsaRiskLicenceTenureDto lt:lastVersionList){
            if(temp.isEdit()){
                returnList.add(lt);
            }
        }
        return returnList;
    }

    private HcsaRiskLicenceTenureDto getSaveDto(HcsaRiskLicenceTenureDto temp, SubLicenceTenureDto sub) {
        HcsaRiskLicenceTenureDto ltDto = new HcsaRiskLicenceTenureDto();
        ltDto.setId(temp.getId());
        try {
            ltDto.setEffectiveDate(Formatter.parseDate(temp.getDoEffectiveDate()));
            ltDto.setEndDate(Formatter.parseDate(temp.getDoEndDate()));
            if(temp.getVersion()!=null){
                ltDto.setVersion(temp.getVersion()+1);
            }else{
                ltDto.setVersion(1);
            }
            ltDto.setSvcCode(temp.getSvcCode());
            ltDto.setLicTenure(Integer.valueOf(sub.getLicenceTenure()));
            ltDto.setRiskScoreTh(Double.valueOf(sub.getColumRight()));
            ltDto.setChronoUnit(sub.getDateType());
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
       return ltDto;
    }

    @Override
    public boolean compareVersionsForRiskLicenceTenure(LicenceTenShowDto needSaveDto, LicenceTenShowDto dbSearchDto) {
        if(needSaveDto == null || IaisCommonUtils.isEmpty(needSaveDto.getLicenceTenureDtoList())){
            return false;
        }else {
            if(dbSearchDto == null || IaisCommonUtils.isEmpty(dbSearchDto.getLicenceTenureDtoList())){
                return true;
            }
            List<HcsaRiskLicenceTenureDto> licenceTenureDtoList = needSaveDto.getLicenceTenureDtoList();
            List<HcsaRiskLicenceTenureDto> licenceTenureDtoListDb = dbSearchDto.getLicenceTenureDtoList();
            for(HcsaRiskLicenceTenureDto hcsaRiskLicenceTenureDto : licenceTenureDtoList){
                for(HcsaRiskLicenceTenureDto hcsaRiskLicenceTenureDtoDb : licenceTenureDtoListDb){
                    if(hcsaRiskLicenceTenureDto.getSvcCode().equalsIgnoreCase(hcsaRiskLicenceTenureDtoDb.getSvcCode())){
                        if(!hcsaRiskSupportBeService.versionSameForRisk(hcsaRiskLicenceTenureDto.getVersion(),hcsaRiskLicenceTenureDtoDb.getVersion())){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
}
