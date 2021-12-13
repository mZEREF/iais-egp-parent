package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.risk.RiskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskFeSupportDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskLegislativeMatrixDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskLegislativeShowDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.HcsaRiskLegislativeService;
import com.ecquaria.cloud.moh.iais.service.HcsaRiskSupportBeService;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import java.util.Date;
import java.util.List;

import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: jiahao
 * @Date: 2019/12/24 19:12
 */
@Service
@Slf4j
public class HcsaRiskLegislativeServiceImpl implements HcsaRiskLegislativeService {
    @Autowired
    HcsaConfigClient hcsaConfigClient;
    @Autowired
    private HcsaRiskSupportBeService hcsaRiskSupportBeService;
    @Override
    public RiskLegislativeShowDto getLegShowDto() {
        return hcsaConfigClient.getLegislativeShow(hcsaRiskSupportBeService.getNameSortHcsaServiceDtos()).getEntity();
    }

    @Override
    public void getOneFinDto(HcsaRiskLegislativeMatrixDto lea, String inthershold, String inleftmod, String inlefthigh, String inrightlow, String inrightmod, String inStartDate, String inEndDate) {
        Integer isEditNum = 0;
        if(!StringUtil.isEmpty(lea.getBaseThershold())) {
            if (!(lea.getBaseThershold() + "").equals(inthershold)) {
                isEditNum++;
            }
            lea.setDoThershold(inthershold);
            String baseRightLowCase = getRightLowCase(lea.getBaseLowCaseCounth());
            if (!baseRightLowCase.equals(inrightlow)) {
                isEditNum++;
            }
            lea.setDoRightLowCaseCounth(inrightlow);

            if (!lea.getBaseLowCaseCounth().equals(inleftmod)) {
                isEditNum++;
            }
            lea.setDoLeftModCaseCounth(inleftmod);
            if (!lea.getBaseHighCaseCounth().equals(inrightmod)) {
                isEditNum++;
            }
            lea.setDoRightModCaseCounth(inrightmod);
            String baseLeftHighCase = getLeftHighCase(lea.getBaseHighCaseCounth());
            if (!baseLeftHighCase.equals(inlefthigh)) {
                isEditNum++;
            }
            lea.setDoLeftHighCaseCounth(inlefthigh);
            if (!lea.getBaseEffectiveDate().equals(inStartDate) && !(StringUtil.isEmpty(lea.getBaseEffectiveDate()) && StringUtil.isEmpty(inStartDate))) {
                isEditNum++;
            }
            lea.setDoEffectiveDate(inStartDate);
            if (!lea.getBaseEndDate().equals(inEndDate) && !(StringUtil.isEmpty(lea.getBaseEndDate())&& StringUtil.isEmpty(inEndDate))) {
                isEditNum++;
            }
            lea.setDoEndDate(inEndDate);
            if (isEditNum >= 1) {
                lea.setEdit(true);
            } else {
                lea.setEdit(false);
            }
        }else {
            boolean isInEdit = isNoBaseSourceIsEdit(lea,inthershold, inrightlow, inleftmod, inlefthigh, inrightmod,inStartDate,inEndDate);
            if(isInEdit){
                lea.setEdit(isInEdit);
                lea.setDoThershold(inthershold);
                lea.setDoRightLowCaseCounth(inrightlow);
                lea.setDoLeftModCaseCounth(inleftmod);
                lea.setDoLeftHighCaseCounth(inlefthigh);
                lea.setDoRightModCaseCounth(inrightmod);
                lea.setDoEffectiveDate(inStartDate);
                lea.setDoEndDate(inEndDate);
            }
        }

    }
    public boolean isNoBaseSourceIsEdit(HcsaRiskLegislativeMatrixDto fin, String thershold, String rightlow, String leftmod, String lefthigh, String rightmod, String StartDate, String endDate){
        if(StringUtil.isEmpty(thershold)&&StringUtil.isEmpty(rightlow)&&StringUtil.isEmpty(leftmod)&&StringUtil.isEmpty(lefthigh)&&StringUtil.isEmpty(rightmod)) {
            return false;
        }
        return true;
    }
    public String getRightLowCase(String lowCaseCount){
        Integer num = 0;
        try {
            num = Integer.valueOf(lowCaseCount);
            num = num-1;
        } catch (Exception e) {
            return "";
        }
        return num+"";
    }
    public String getLeftHighCase(String highCaseCount){
        Integer num = 0;
        try {
            num = Integer.valueOf(highCaseCount);
            num = num+1;
        } catch (Exception e) {
            return "";
        }
        return num+"";
    }

    @Override
    public void saveDto(RiskLegislativeShowDto dto) {
        List<HcsaRiskLegislativeMatrixDto> dtoList = dto.getLegislativeList();
        List<HcsaRiskLegislativeMatrixDto> saveList = IaisCommonUtils.genNewArrayList();
        for(HcsaRiskLegislativeMatrixDto temp : dtoList){
            if(temp.isEdit()){
                saveList.add(getFinDto(temp,true));
                saveList.add(getFinDto(temp,false));
            }
        }
        FeignResponseEntity<List<HcsaRiskLegislativeMatrixDto>> responseEntity = doUpdate(saveList,dtoList);
        HcsaRiskFeSupportDto supportDto = new HcsaRiskFeSupportDto();
        supportDto.setRiskLegislativeShowDto(dto);
        supportDto.setLegFlag(true);
        supportDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        hcsaRiskSupportBeService.sysnRiskSaveEic(responseEntity.getStatusCode(),supportDto);
    }

    public FeignResponseEntity<List<HcsaRiskLegislativeMatrixDto>> doUpdate(List<HcsaRiskLegislativeMatrixDto> updateList, List<HcsaRiskLegislativeMatrixDto> dtoList){
        //get last version form db
        for(HcsaRiskLegislativeMatrixDto temp:dtoList){
            List<HcsaRiskLegislativeMatrixDto> lastversionList = getLastversionList(temp);
            if(lastversionList!=null && !lastversionList.isEmpty()){
                for(HcsaRiskLegislativeMatrixDto lastversion:lastversionList){
                    updateLastVersion(temp,lastversion);
                }
                hcsaConfigClient.updateLegislativeRiskMatrix(lastversionList);
            }
        }
        for(HcsaRiskLegislativeMatrixDto temp:updateList){
            temp.setId(null);
        }
      return   hcsaConfigClient.saveLegislativeRiskMatrix(updateList);
    }

    public void updateLastVersion(HcsaRiskLegislativeMatrixDto newFin,HcsaRiskLegislativeMatrixDto dbFin){
        try {
            if ("CRRR003".equals(dbFin.getRiskRating())) {
                dbFin.setEndDate(Formatter.parseDate(newFin.getDoEffectiveDate()));
                if (dbFin.getEndDate().getTime() < System.currentTimeMillis()) {
                    dbFin.setStatus("CMSTAT003");
                }
            } else if ("CRRR001".equals(dbFin.getRiskRating())) {
                dbFin.setEndDate(Formatter.parseDate(newFin.getDoEffectiveDate()));
                if (dbFin.getEndDate().getTime() < System.currentTimeMillis()) {
                    dbFin.setStatus("CMSTAT003");
                }
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }

    }

    public List<HcsaRiskLegislativeMatrixDto> getLastversionList(HcsaRiskLegislativeMatrixDto temp){
        List<HcsaRiskLegislativeMatrixDto> lastversionList= hcsaConfigClient.getLegislativeRiskBySvcCode(temp.getSvcCode()).getEntity();
        List<HcsaRiskLegislativeMatrixDto> returnList = IaisCommonUtils.genNewArrayList();
        if(lastversionList!=null && !lastversionList.isEmpty()){
            for(HcsaRiskLegislativeMatrixDto fin:lastversionList){
                if(temp.isEdit()){
                    returnList.add(fin);
                }
            }
        }
        return returnList;
    }

    public HcsaRiskLegislativeMatrixDto getFinDto(HcsaRiskLegislativeMatrixDto dto,boolean isLow){
        HcsaRiskLegislativeMatrixDto finDto = new HcsaRiskLegislativeMatrixDto();
        finDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        finDto.setSvcCode(dto.getSvcCode());
        finDto.setStatus("CMSTAT001");
        finDto.setEdit(dto.isEdit());
        if(StringUtil.isEmpty(dto.getVersion())){
            finDto.setVersion(1);
        }else{
            finDto.setVersion(dto.getVersion()+1);
        }
        Date effDate = null;
        Date endDate = null;
        try {
            effDate = Formatter.parseDate(dto.getDoEffectiveDate());
            endDate = Formatter.parseDate(dto.getDoEndDate());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        finDto.setEffectiveDate(effDate);
        finDto.setEndDate(endDate);
        if (isLow) {
            finDto.setThershold(Integer.valueOf(dto.getDoThershold()));
            finDto.setCaseCounth(Integer.valueOf(dto.getDoLeftModCaseCounth()));
            finDto.setRiskRating(RiskConsts.LOW);
        } else {
            finDto.setThershold(Integer.valueOf(dto.getDoThershold()));
            finDto.setCaseCounth(Integer.valueOf(dto.getDoRightModCaseCounth()));
            finDto.setRiskRating(RiskConsts.HIGH);
        }

        return finDto;
    }

    @Override
    public boolean compareVersionsForRiskLegislative(RiskLegislativeShowDto needSaveDto, RiskLegislativeShowDto dbSearchDto) {
         if(needSaveDto == null || IaisCommonUtils.isEmpty(needSaveDto.getLegislativeList())){
             return false;
         }else {
             if( dbSearchDto == null || IaisCommonUtils.isEmpty(dbSearchDto.getLegislativeList())){
                 return true;
             }
             List<HcsaRiskLegislativeMatrixDto> legislativeList = needSaveDto.getLegislativeList();
             List<HcsaRiskLegislativeMatrixDto> legislativeListDb = dbSearchDto.getLegislativeList();
             for(HcsaRiskLegislativeMatrixDto hcsaRiskLegislativeMatrixDto :legislativeList){
                 for(HcsaRiskLegislativeMatrixDto hcsaRiskLegislativeMatrixDtoDb : legislativeListDb){
                     if(hcsaRiskLegislativeMatrixDto.getSvcCode().equalsIgnoreCase(hcsaRiskLegislativeMatrixDtoDb.getSvcCode())){
                         if( !hcsaRiskSupportBeService.versionSameForRisk(hcsaRiskLegislativeMatrixDto.getVersion(),hcsaRiskLegislativeMatrixDtoDb.getVersion())){
                          return false;
                         }
                     }
                 }
             }
         }
        return true;
    }
}
