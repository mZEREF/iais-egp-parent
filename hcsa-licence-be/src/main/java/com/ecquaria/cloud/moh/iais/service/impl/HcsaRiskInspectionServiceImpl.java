package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskInspectionMatrixDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.InspectionShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.HcsaRiskInspectionService;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
