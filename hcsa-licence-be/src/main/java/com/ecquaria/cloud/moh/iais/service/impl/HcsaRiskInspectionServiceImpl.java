package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.risk.RiskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskFeSupportDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskInspectionMatrixDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.InspectionShowDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.HcsaRiskInspectionService;
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
 * @Date: 2020/1/2 13:46
 */
@Service
@Slf4j
public class HcsaRiskInspectionServiceImpl implements HcsaRiskInspectionService {
    @Autowired
    private HcsaConfigClient hcsaConfigClient;
    @Autowired
    private HcsaRiskSupportBeService hcsaRiskSupportBeService;

    @Override
    public InspectionShowDto getInspectionShowDto() {
        return hcsaConfigClient.getInspectionshow(hcsaRiskSupportBeService.getNameSortHcsaServiceDtos()).getEntity();
    }

    @Override
    public void getOneFinDto(HcsaRiskInspectionMatrixDto fin, String caleftmod, String calefthigh, String carightlow, String carightmod, String caStartDate, String caEndDate,
                             String mileftmod, String milefthigh, String mirightlow, String mirightmod, String miStartDate, String miEndDate,
                             String mjleftmod, String mjlefthigh, String mjrightlow, String mjrightmod, String mjStartDate, String mjEndDate) {

        if(fin.getId()!=null){
            int caeditNum = getCaColumCa(fin,caleftmod,calefthigh,carightlow,carightmod,caStartDate,caEndDate);
            if(caeditNum>0){
                fin.setCaEdit(true);
            }else{
                fin.setCaEdit(false);
            }
            int mieditNum = getCaColumMi(fin,mileftmod,milefthigh,mirightlow,mirightmod,miStartDate,miEndDate);
            if(mieditNum>0){
                fin.setMiEdit(true);
            }else{
                fin.setMiEdit(false);
            }
            int mjeditNum = getCaColumMj(fin,mjleftmod,mjlefthigh,mjrightlow,mjrightmod,mjStartDate,mjEndDate);
            if(mjeditNum>0){
                fin.setMjEdit(true);
            }else{
                fin.setMjEdit(false);
            }
        }else{
            boolean caEditFlag = getNoIdEditFlag(caleftmod,calefthigh,carightlow,carightmod,caStartDate,caEndDate);
            if(caEditFlag){
                fin.setDoCaLeftHighCounth(calefthigh);
                fin.setDoCaRightModCounth(carightmod);
                fin.setDoCaLeftModCounth(caleftmod);
                fin.setDoCaRightLowCounth(carightlow);
                fin.setDoCaEffectiveDate(caStartDate);
                fin.setDoCaEndDate(caEndDate);
                fin.setCaEdit(true);
            }
            boolean miEditFlag = getNoIdEditFlag(mileftmod,milefthigh,mirightlow,mirightmod,miStartDate,miEndDate);
            if(miEditFlag){
                fin.setDoMiLeftHighCounth(milefthigh);
                fin.setDoMiRightModCounth(mirightmod);
                fin.setDoMiLeftModCounth(mileftmod);
                fin.setDoMiRightLowCounth(mirightlow);
                fin.setDoMiEffectiveDate(miStartDate);
                fin.setDoMiEndDate(miEndDate);
                fin.setMiEdit(true);
            }
            boolean mjEditFlag = getNoIdEditFlag(mjleftmod,mjlefthigh,mjrightlow,mjrightmod,mjStartDate,mjEndDate);
            if(mjEditFlag){
                fin.setDoMjLeftHighCounth(mjlefthigh);
                fin.setDoMjRightModCounth(mjrightmod);
                fin.setDoMjLeftModCounth(mjleftmod);
                fin.setDoMjRightLowCounth(mjrightlow);
                fin.setDoMjEffectiveDate(mjStartDate);
                fin.setDoMjEndDate(mjEndDate);
                fin.setMjEdit(true);
            }

        }

    }

    private boolean getNoIdEditFlag(String caleftmod, String calefthigh, String carightlow, String carightmod, String caStartDate, String caEndDate) {
        if(StringUtil.isEmpty(caleftmod)&&StringUtil.isEmpty(calefthigh)&&StringUtil.isEmpty(carightlow)&&StringUtil.isEmpty(carightmod)&&StringUtil.isEmpty(caStartDate)&&StringUtil.isEmpty(caEndDate)){
            return false;
        }else{
            return true;
        }
    }

    private int getCaColumMj(HcsaRiskInspectionMatrixDto fin, String mjleftmod, String mjlefthigh, String mjrightlow, String mjrightmod, String mjStartDate, String mjEndDate) {
        int editNum = 0;
        if(!fin.getBaseMjEffectiveDate().equals(mjStartDate) && !(StringUtil.isEmpty(fin.getBaseMjEffectiveDate()) &&StringUtil.isEmpty(mjStartDate))){
            editNum++;
        }
        fin.setDoMjEffectiveDate(mjStartDate);
        if(!fin.getBaseMjEndDate().equals(mjEndDate) && !(StringUtil.isEmpty(fin.getBaseMjEndDate()) &&StringUtil.isEmpty(mjEndDate))){
            editNum++;
        }
        fin.setDoMjEndDate(mjEndDate);
        if(!fin.getBaseMjRightLowCounth().equals(mjrightlow)){
            editNum++;
        }
        fin.setDoMjRightLowCounth(mjrightlow);
        if(!fin.getBaseMjLeftModCounth().equals(mjleftmod)){
            editNum++;
        }
        fin.setDoMjLeftModCounth(mjleftmod);
        if(!fin.getBaseMjRightModCounth().equals(mjrightmod)){
            editNum++;
        }
        fin.setDoMjRightModCounth(mjrightmod);
        if(!fin.getBaseMjLeftHighCounth().equals(mjlefthigh)){
            editNum++;
        }
        fin.setDoMjLeftHighCounth(mjlefthigh);
        return editNum;
    }

    private int getCaColumMi(HcsaRiskInspectionMatrixDto fin, String mileftmod, String milefthigh, String mirightlow, String mirightmod, String miStartDate, String miEndDate) {
        int editNum = 0;
        if(!fin.getBaseMiEffectiveDate().equals(miStartDate)  && !(StringUtil.isEmpty(fin.getBaseMiEffectiveDate()) &&StringUtil.isEmpty(miStartDate))){
            editNum++;
        }
        fin.setDoMiEffectiveDate(miStartDate);
        if(!fin.getBaseMiEndDate().equals(miEndDate) &&  !(StringUtil.isEmpty(fin.getBaseMiEndDate()) &&StringUtil.isEmpty(miEndDate))){
            editNum++;
        }
        fin.setDoMiEndDate(miEndDate);
        if(!fin.getBaseMiRightLowCounth().equals(mirightlow)){
            editNum++;
        }
        fin.setDoMiRightLowCounth(mirightlow);
        if(!fin.getBaseMiLeftModCounth().equals(mileftmod)){
            editNum++;
        }
        fin.setDoMiLeftModCounth(mileftmod);
        if(!fin.getBaseMiRightModCounth().equals(mirightmod)){
            editNum++;
        }
        fin.setDoMiRightModCounth(mirightmod);
        if(!fin.getBaseMiLeftHighCounth().equals(milefthigh)){
            editNum++;
        }
        fin.setDoMiLeftHighCounth(milefthigh);
        return editNum;
    }

    private int getCaColumCa(HcsaRiskInspectionMatrixDto fin, String caleftmod, String calefthigh, String carightlow, String carightmod, String caStartDate, String caEndDate) {
        int editNum = 0;
        if(!fin.getBaseCEffectiveDate().equals(caStartDate) && !(StringUtil.isEmpty(fin.getBaseCEffectiveDate()) &&StringUtil.isEmpty(caStartDate))){
            editNum++;
        }
        fin.setDoCaEffectiveDate(caStartDate);
        if(!fin.getBaseCEndDate().equals(caEndDate) && !(StringUtil.isEmpty(fin.getBaseCEndDate()) &&StringUtil.isEmpty(caEndDate))){
            editNum++;
        }
        fin.setDoCaEndDate(caEndDate);
        if(!fin.getBaseCaRightLowCounth().equals(carightlow)){
            editNum++;
        }
        fin.setDoCaRightLowCounth(carightlow);
        if(!fin.getBaseCaLeftModCounth().equals(caleftmod)){
            editNum++;
        }
        fin.setDoCaLeftModCounth(caleftmod);
        if(!fin.getBaseCaRightModCounth().equals(carightmod)){
            editNum++;
        }
        fin.setDoCaRightModCounth(carightmod);
        if(!fin.getBaseCaLeftHighCounth().equals(calefthigh)){
            editNum++;
        }
        fin.setDoCaLeftHighCounth(calefthigh);
        return editNum;
    }

    @Override
    public void saveDto(InspectionShowDto showDto) {
        List<HcsaRiskInspectionMatrixDto> dtoList = showDto.getInspectionDtoList();
        List<HcsaRiskInspectionMatrixDto> saveList = IaisCommonUtils.genNewArrayList();
        for(HcsaRiskInspectionMatrixDto temp : dtoList){
            if(temp.isCaEdit()||temp.isMjEdit()||temp.isMiEdit()){
                saveList.add(getFinDto(temp,true,"RSKL0001"));
                saveList.add(getFinDto(temp,false,"RSKL0001"));
                saveList.add(getFinDto(temp,true,"RSKL0003"));
                saveList.add(getFinDto(temp,false,"RSKL0003"));
                saveList.add(getFinDto(temp,true,"RSKL0002"));
                saveList.add(getFinDto(temp,false,"RSKL0002"));
            }
        }
        FeignResponseEntity<List<HcsaRiskInspectionMatrixDto>> responseEntity = doUpdate(saveList,dtoList);
        HcsaRiskFeSupportDto supportDto = new HcsaRiskFeSupportDto();
        supportDto.setInspectionShowDto(showDto);
        supportDto.setInspectionRiskFlag(true);
        supportDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        hcsaRiskSupportBeService.sysnRiskSaveEic(responseEntity.getStatusCode(),supportDto);
    }




    public FeignResponseEntity<List<HcsaRiskInspectionMatrixDto>> doUpdate(List<HcsaRiskInspectionMatrixDto> updateList, List<HcsaRiskInspectionMatrixDto> dtoList){
        //get last version form db
        for(HcsaRiskInspectionMatrixDto temp:dtoList){
            //List<HcsaRiskFinanceMatrixDto> lastversionList= hcsaConfigClient.getFinianceRiskBySvcCode(temp.getServiceCode()).getEntity();
            List<HcsaRiskInspectionMatrixDto> lastversionList = getLastversionList(temp);
            if(lastversionList!=null && !lastversionList.isEmpty()){
                for(HcsaRiskInspectionMatrixDto lastversion:lastversionList){
                    lastversion.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    if("RSKL0001".equals(lastversion.getRiskLevel())&&lastversion.isCaEdit()){
                        updateLastVersion(temp,lastversion);
                    }else if("RSKL0003".equals(lastversion.getRiskLevel())&&lastversion.isMiEdit()){
                        updateLastVersion(temp,lastversion);
                    }else if("RSKL0002".equals(lastversion.getRiskLevel())&&lastversion.isMjEdit()){
                        updateLastVersion(temp,lastversion);
                    }
                }
                hcsaConfigClient.udpateInspectionMatrix(lastversionList);
            }
        }
        for(HcsaRiskInspectionMatrixDto temp:updateList){
            temp.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            temp.setId(null);
        }
       return hcsaConfigClient.saveInspectionMatrix(updateList);
    }
    public void updateLastVersion(HcsaRiskInspectionMatrixDto newFin,HcsaRiskInspectionMatrixDto dbFin){
        try {
            if("RSKL0001".equals(dbFin.getRiskLevel())){
                if("CRRR003".equals(dbFin.getRiskRating())){
                    dbFin.setEndDate(Formatter.parseDate(newFin.getDoCaEffectiveDate()));
                    if(dbFin.getEndDate().getTime()<System.currentTimeMillis()){
                        dbFin.setStatus("CMSTAT003");
                    }
                }else if("CRRR001".equals(dbFin.getRiskRating())){
                    dbFin.setEndDate(Formatter.parseDate(newFin.getDoCaEffectiveDate()));
                    if(dbFin.getEndDate().getTime()<System.currentTimeMillis()){
                        dbFin.setStatus("CMSTAT003");
                    }
                }
            }else if("RSKL0003".equals(dbFin.getRiskLevel())){
                if("CRRR003".equals(dbFin.getRiskRating())){
                    dbFin.setEndDate(Formatter.parseDate(newFin.getDoMiEffectiveDate()));
                    if(dbFin.getEndDate().getTime()<System.currentTimeMillis()){
                        dbFin.setStatus("CMSTAT003");
                    }
                }else if("CRRR001".equals(dbFin.getRiskRating())){
                    dbFin.setEndDate(Formatter.parseDate(newFin.getDoMiEffectiveDate()));
                    if(dbFin.getEndDate().getTime()<System.currentTimeMillis()){
                        dbFin.setStatus("CMSTAT003");
                    }
                }
            }else if("RSKL0002".equals(dbFin.getRiskLevel())){
                dbFin.setEndDate(Formatter.parseDate(newFin.getDoMjEffectiveDate()));
                if(dbFin.getEndDate().getTime()<System.currentTimeMillis()){
                    dbFin.setStatus("CMSTAT003");
                }
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }

    }

    public List<HcsaRiskInspectionMatrixDto> getLastversionList(HcsaRiskInspectionMatrixDto temp){
        List<HcsaRiskInspectionMatrixDto> lastversionList= hcsaConfigClient.getInspectionBySvcCode(temp.getSvcCode()).getEntity();
        List<HcsaRiskInspectionMatrixDto> returnList = IaisCommonUtils.genNewArrayList();
        if(lastversionList!=null && !lastversionList.isEmpty()){
            for(HcsaRiskInspectionMatrixDto fin:lastversionList){
                if(temp.isCaEdit()&&"RSKL0001".equals(fin.getRiskLevel())){
                    fin.setCaEdit(true);
                }
                if(temp.isMiEdit()&&"RSKL0003".equals(fin.getRiskLevel())){
                    fin.setMiEdit(true);
                }
                if(temp.isMjEdit()&&"RSKL0002".equals(fin.getRiskLevel())){
                    fin.setMjEdit(true);
                }
                returnList.add(fin);
            }
        }
        return returnList;
    }

    public HcsaRiskInspectionMatrixDto getFinDto(HcsaRiskInspectionMatrixDto dto,boolean isLow,String level){
        HcsaRiskInspectionMatrixDto finDto = new HcsaRiskInspectionMatrixDto();
        finDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        finDto.setSvcCode(dto.getSvcCode());
        finDto.setStatus("CMSTAT001");
        finDto.setOrdinalNo(1);
        finDto.setCaEdit(dto.isCaEdit());
        finDto.setMiEdit(dto.isMiEdit());
        finDto.setMjEdit(dto.isMjEdit());
        if(StringUtil.isEmpty(dto.getVersion())){
            finDto.setVersion(1);
        }else{
            finDto.setVersion(dto.getVersion()+1);
        }
        Date effDate = null;
        Date endDate = null;
        if("RSKL0001".equals(level)) {
            finDto.setRiskLevel("RSKL0001");
            try {
                effDate = Formatter.parseDate(dto.getDoCaEffectiveDate());
                endDate = Formatter.parseDate(dto.getDoCaEndDate());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            finDto.setEffectiveDate(effDate);
            finDto.setEndDate(endDate);
            if (isLow) {
                finDto.setNcCountTh(Integer.valueOf(dto.getDoCaLeftModCounth()));
                finDto.setRiskRating(RiskConsts.LOW);
            } else {
                finDto.setNcCountTh(Integer.valueOf(dto.getDoCaRightModCounth()));
                finDto.setRiskRating(RiskConsts.HIGH);
            }
        }else if("RSKL0003".equals(level)){
            finDto.setRiskLevel("RSKL0003");
            try {
                effDate = Formatter.parseDate(dto.getDoMiEffectiveDate());
                endDate = Formatter.parseDate(dto.getDoMiEndDate());
            }catch (Exception e){
                log.error(e.getMessage(), e);
            }
            finDto.setEffectiveDate(effDate);
            finDto.setEndDate(endDate);
            if(isLow){
                finDto.setNcCountTh(Integer.valueOf(dto.getDoMiLeftModCounth()));
                finDto.setRiskRating(RiskConsts.LOW);
            }else{
                finDto.setNcCountTh(Integer.valueOf(dto.getDoMiRightModCounth()));
                finDto.setRiskRating(RiskConsts.HIGH);
            }
        }else if("RSKL0002".equals(level)){
            finDto.setRiskLevel("RSKL0002");
            try {
                effDate = Formatter.parseDate(dto.getDoMjEffectiveDate());
                endDate = Formatter.parseDate(dto.getDoMjEndDate());
            }catch (Exception e){
                log.error(e.getMessage(), e);
            }
            finDto.setEffectiveDate(effDate);
            finDto.setEndDate(endDate);
            if(isLow){
                finDto.setNcCountTh(Integer.valueOf(dto.getDoMjLeftModCounth()));
                finDto.setRiskRating(RiskConsts.LOW);
            }else{
                finDto.setNcCountTh(Integer.valueOf(dto.getDoMjRightModCounth()));
                finDto.setRiskRating(RiskConsts.HIGH);
            }
        }
        return finDto;
    }

    @Override
    public boolean compareVersionsForRiskInspection(InspectionShowDto needSaveDto, InspectionShowDto dbSearchDto) {
        if(needSaveDto == null || IaisCommonUtils.isEmpty( needSaveDto.getInspectionDtoList())){
            return false;
        }else {
            if(dbSearchDto == null || IaisCommonUtils.isEmpty(dbSearchDto.getInspectionDtoList())){
                return true;
            }
            List<HcsaRiskInspectionMatrixDto> inspectionDtoList = needSaveDto.getInspectionDtoList();
            List<HcsaRiskInspectionMatrixDto> inspectionDtoListDb = dbSearchDto.getInspectionDtoList();
            for(HcsaRiskInspectionMatrixDto hcsaRiskInspectionMatrixDto : inspectionDtoList){
                for(HcsaRiskInspectionMatrixDto hcsaRiskInspectionMatrixDtoDb : inspectionDtoListDb){
                    if(hcsaRiskInspectionMatrixDto.getSvcCode().equalsIgnoreCase( hcsaRiskInspectionMatrixDtoDb.getSvcCode())){
                        if(!hcsaRiskSupportBeService.versionSameForRisk(hcsaRiskInspectionMatrixDto.getVersion(),hcsaRiskInspectionMatrixDtoDb.getVersion())){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
}
