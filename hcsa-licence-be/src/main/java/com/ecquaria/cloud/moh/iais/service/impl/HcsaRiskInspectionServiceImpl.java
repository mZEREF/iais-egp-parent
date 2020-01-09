package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.risk.RiskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskInspectionMatrixDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.InspectionShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.HcsaRiskInspectionService;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: jiahao
 * @Date: 2020/1/2 13:46
 */
@Service
@Slf4j
public class HcsaRiskInspectionServiceImpl implements HcsaRiskInspectionService {
    @Autowired
    private HcsaConfigClient hcsaConfigClient;
    @Override
    public InspectionShowDto getInspectionShowDto() {
        List<HcsaServiceDto> serviceDtoList = hcsaConfigClient.getActiveServices().getEntity();
        InspectionShowDto showDto = hcsaConfigClient.getInspectionshow(serviceDtoList).getEntity();
        return showDto;
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
            }
            boolean miEditFlag = getNoIdEditFlag(mileftmod,milefthigh,mirightlow,mirightmod,miStartDate,miEndDate);
            if(miEditFlag){
                fin.setDoMiLeftHighCounth(milefthigh);
                fin.setDoMiRightModCounth(mirightmod);
                fin.setDoMiLeftModCounth(mileftmod);
                fin.setDoMiRightLowCounth(mirightlow);
                fin.setDoMiEffectiveDate(miStartDate);
                fin.setDoMiEndDate(miEndDate);
            }
            boolean mjEditFlag = getNoIdEditFlag(mjleftmod,mjlefthigh,mjrightlow,mjrightmod,mjStartDate,mjEndDate);
            if(mjEditFlag){
                fin.setDoMjLeftHighCounth(mjlefthigh);
                fin.setDoMjRightModCounth(mjrightmod);
                fin.setDoMjLeftModCounth(mjleftmod);
                fin.setDoMjRightLowCounth(mjrightlow);
                fin.setDoMjEffectiveDate(mjStartDate);
                fin.setDoMjEndDate(mjEndDate);
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
        if(!fin.getBaseMjEffectiveDate().equals(mjStartDate)){
            editNum++;
        }
        fin.setDoMjEffectiveDate(mjStartDate);
        if(!fin.getBaseMjEndDate().equals(mjEndDate)){
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
        if(!fin.getBaseMiEffectiveDate().equals(miStartDate)){
            editNum++;
        }
        fin.setDoMiEffectiveDate(miStartDate);
        if(!fin.getBaseMiEndDate().equals(miEndDate)){
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
        if(!fin.getBaseCEffectiveDate().equals(caStartDate)){
            editNum++;
        }
        fin.setDoCaEffectiveDate(caStartDate);
        if(!fin.getBaseCEndDate().equals(caEndDate)){
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
        List<HcsaRiskInspectionMatrixDto> saveList = new ArrayList<>();
        List<HcsaRiskInspectionMatrixDto> updateList = new ArrayList<>();
        for(HcsaRiskInspectionMatrixDto temp : dtoList){
            if(temp.isCaEdit()||temp.isMjEdit()||temp.isMiEdit()){
                saveList.add(getFinDto(temp,true,"C"));
                saveList.add(getFinDto(temp,false,"C"));
                saveList.add(getFinDto(temp,true,"I"));
                saveList.add(getFinDto(temp,false,"I"));
                saveList.add(getFinDto(temp,true,"A"));
                saveList.add(getFinDto(temp,true,"A"));
            }
        }
        doUpdate(saveList,dtoList);
    }




    public void doUpdate(List<HcsaRiskInspectionMatrixDto> updateList,List<HcsaRiskInspectionMatrixDto> dtoList){
        //get last version form db
        for(HcsaRiskInspectionMatrixDto temp:dtoList){
            //List<HcsaRiskFinanceMatrixDto> lastversionList= hcsaConfigClient.getFinianceRiskBySvcCode(temp.getServiceCode()).getEntity();
            List<HcsaRiskInspectionMatrixDto> lastversionList = getLastversionList(temp);
            if(lastversionList!=null && !lastversionList.isEmpty()){
                for(HcsaRiskInspectionMatrixDto lastversion:lastversionList){
                    lastversion.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    if("C".equals(lastversion.getRiskLevel())&&lastversion.isCaEdit()){
                        updateLastVersion(temp,lastversion);
                    }else if("I".equals(lastversion.getRiskLevel())&&lastversion.isMiEdit()){
                        updateLastVersion(temp,lastversion);
                    }else if("A".equals(lastversion.getRiskLevel())&&lastversion.isMjEdit()){
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
        hcsaConfigClient.saveInspectionMatrix(updateList);
    }
    public void updateLastVersion(HcsaRiskInspectionMatrixDto newFin,HcsaRiskInspectionMatrixDto dbFin){
        try {
            if("C".equals(dbFin.getRiskLevel())){
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
            }else if("I".equals(dbFin.getRiskLevel())){
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
                }else if("J".equals(dbFin.getRiskLevel())){
                    dbFin.setEndDate(Formatter.parseDate(newFin.getDoMjEffectiveDate()));
                    if(dbFin.getEndDate().getTime()<System.currentTimeMillis()){
                        dbFin.setStatus("CMSTAT003");
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public List<HcsaRiskInspectionMatrixDto> getLastversionList(HcsaRiskInspectionMatrixDto temp){
        List<HcsaRiskInspectionMatrixDto> lastversionList= hcsaConfigClient.getInspectionBySvcCode(temp.getSvcCode()).getEntity();
        List<HcsaRiskInspectionMatrixDto> returnList = new ArrayList<>();
        if(lastversionList!=null && !lastversionList.isEmpty()){
            for(HcsaRiskInspectionMatrixDto fin:lastversionList){
                if(temp.isCaEdit()&&"C".equals(fin.getRiskLevel())){
                    fin.setCaEdit(true);
                }
                if(temp.isMiEdit()&&"I".equals(fin.getRiskLevel())){
                    fin.setMiEdit(true);
                }
                if(temp.isMjEdit()&&"A".equals(fin.getRiskLevel())){
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
        if("C".equals(level)) {
            finDto.setRiskLevel("C");
            try {
                effDate = Formatter.parseDate(dto.getDoCaEffectiveDate());
                endDate = Formatter.parseDate(dto.getDoCaEndDate());
            } catch (Exception e) {
                e.printStackTrace();
            }
            finDto.setEffectiveDate(effDate);
            finDto.setEndDate(endDate);
            if (isLow) {
                finDto.setNcCountTh(Integer.parseInt(dto.getDoCaLeftModCounth()));
                finDto.setRiskRating(RiskConsts.LOW);
            } else {
                finDto.setNcCountTh(Integer.parseInt(dto.getDoCaRightModCounth()));
                finDto.setRiskRating(RiskConsts.HIGH);
            }
        }else if("I".equals(level)){
            finDto.setRiskLevel("I");
            try {
                effDate = Formatter.parseDate(dto.getDoMiEffectiveDate());
                endDate = Formatter.parseDate(dto.getDoMiEndDate());
            }catch (Exception e){
                e.printStackTrace();
            }
            finDto.setEffectiveDate(effDate);
            finDto.setEndDate(endDate);
            if(isLow){
                finDto.setNcCountTh(Integer.parseInt(dto.getDoMiLeftModCounth()));
                finDto.setRiskRating(RiskConsts.LOW);
            }else{
                finDto.setNcCountTh(Integer.parseInt(dto.getDoMiRightModCounth()));
                finDto.setRiskRating(RiskConsts.HIGH);
            }
        }else if("A".equals(level)){
            finDto.setRiskLevel("A");
            try {
                effDate = Formatter.parseDate(dto.getDoMjEffectiveDate());
                endDate = Formatter.parseDate(dto.getDoMjEndDate());
            }catch (Exception e){
                e.printStackTrace();
            }
            finDto.setEffectiveDate(effDate);
            finDto.setEndDate(endDate);
            if(isLow){
                finDto.setNcCountTh(Integer.parseInt(dto.getDoMjLeftModCounth()));
                finDto.setRiskRating(RiskConsts.LOW);
            }else{
                finDto.setNcCountTh(Integer.parseInt(dto.getDoMjRightModCounth()));
                finDto.setRiskRating(RiskConsts.HIGH);
            }
        }
        return finDto;
    }

}
