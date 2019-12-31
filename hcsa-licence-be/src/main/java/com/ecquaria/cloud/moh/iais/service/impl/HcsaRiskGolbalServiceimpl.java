package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.GobalRiskTotalDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.GolbalRiskShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskGlobalDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskGolbalExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.HcsaRiskGolbalService;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: jiahao
 * @Date: 2019/12/30 9:44
 */
@Service
@Slf4j
public class HcsaRiskGolbalServiceimpl implements HcsaRiskGolbalService {
    @Autowired
    private HcsaConfigClient hcsaConfigClient;
    @Override
    public GolbalRiskShowDto getGolbalRiskShowDto() {
        List<HcsaServiceDto> serviceDtoList = hcsaConfigClient.getActiveServices().getEntity();
        GolbalRiskShowDto showDto = hcsaConfigClient.getgolbalshow(serviceDtoList).getEntity();
        List<GobalRiskTotalDto> gobalRiskTotalDtoList = showDto.getGoalbalTotalList();
        return showDto;
    }

    @Override
    public List<SelectOption> getAutoOp() {
        List<SelectOption> autoRenew = new ArrayList<>();
        SelectOption op = new SelectOption();
        op.setValue("Y");
        op.setText("Yes");
        SelectOption op2 = new SelectOption();
        op2.setValue("N");
        op2.setText("No");
        autoRenew.add(op);
        autoRenew.add(op2);
        return autoRenew;
    }

    @Override
    public List<SelectOption> inpTypeOp() {
        String category[] = {"TOIR001","TOIR002","TOIR003"};
        List<SelectOption> inpTypeOp =   MasterCodeUtil.retrieveOptionsByCodes(category);
        return inpTypeOp;
    }

    @Override
    public List<SelectOption> PreOrPostOp() {
        List<SelectOption> autoRenew = new ArrayList<>();
        SelectOption op = new SelectOption();
        op.setValue("post");
        op.setText("Post");
        SelectOption op2 = new SelectOption();
        op2.setValue("pre");
        op2.setText("Pre");
        autoRenew.add(op);
        autoRenew.add(op2);
        return autoRenew;
    }

    @Override
    public void setGolShowDto(GobalRiskTotalDto fin, String maxLic, String doLast, String autoreop,
                              String newinpTypeOps, String newPreOrPostOps, String renewinpTypeOps,
                              String renewPreOrPostOps, String instartdate, String inEndDate) {
        int editNum = 0;
        if(fin.getGalbalId()!=null){
            if(!fin.getBaseMaxLic().equals(maxLic)){
                editNum++;
            }
            fin.setDoMaxLic(maxLic);
            if(!fin.getBaseLastInspection().equals(doLast)){
                editNum++;
            }
            fin.setDoLastInspection(doLast);
            if(!fin.getBaseAutoRenew().equals(autoreop)){
                editNum++;
            }
            fin.setDoAutoRenew(autoreop);
            if(!fin.getBasenewInspectType().equals(newinpTypeOps)){
                editNum++;
            }
            fin.setDonewInspectType(newinpTypeOps);
            if(!fin.getBasenewIsPreInspect().equals(newPreOrPostOps)){
                editNum++;
            }
            fin.setDonewIsPreInspect(newPreOrPostOps);

            if(!fin.getBaserenewInspectType().equals(renewinpTypeOps)){
                editNum++;
            }
            fin.setDorenewInspectType(renewinpTypeOps);
            if(!fin.getBaserenewIsPreInspect().equals(renewPreOrPostOps)){
                editNum++;
            }
            fin.setDorenewIsPreInspect(renewPreOrPostOps);

            if(!fin.getBaseEffectiveDate().equals(instartdate)){
                editNum++;
            }
            fin.setDoEffectiveDate(instartdate);

            if(!fin.getBaseEndDate().equals(inEndDate)){
                editNum++;
            }
            fin.setDoEndDate(inEndDate);
            if(editNum>=1){
                fin.setEdit(true);
            }
        }else{
            getNullGolIdCloum(fin,maxLic,doLast,autoreop,newinpTypeOps,newPreOrPostOps,renewinpTypeOps,renewPreOrPostOps,instartdate,inEndDate);
        }

    }

    private void getNullGolIdCloum(GobalRiskTotalDto fin, String maxLic, String doLast, String autoreop,
                                   String newinpTypeOps, String newPreOrPostOps, String renewinpTypeOps,
                                   String renewPreOrPostOps, String instartdate, String inEndDate) {
        if(StringUtil.isEmpty(maxLic)&&StringUtil.isEmpty(doLast)&&StringUtil.isEmpty(autoreop)&&StringUtil.isEmpty(newinpTypeOps)&&StringUtil.isEmpty(newPreOrPostOps)&&StringUtil.isEmpty(renewinpTypeOps)&&StringUtil.isEmpty(renewPreOrPostOps)
        &&StringUtil.isEmpty(instartdate)&&StringUtil.isEmpty(inEndDate)){
            fin.setEdit(false);
        }else{
            fin.setEdit(true);
        }
        fin.setDoMaxLic(maxLic);
        fin.setDoLastInspection(doLast);
        fin.setDoAutoRenew(autoreop);
        fin.setDonewInspectType(newinpTypeOps);
        fin.setDonewIsPreInspect(newPreOrPostOps);
        fin.setDonewIsPreInspect(newPreOrPostOps);
        fin.setDorenewInspectType(renewinpTypeOps);
        fin.setDorenewIsPreInspect(renewPreOrPostOps);
        fin.setDoEffectiveDate(instartdate);
        fin.setDoEndDate(inEndDate);
    }

    @Override
    public void saveDto(GolbalRiskShowDto golbalShowDto) {
        List<GobalRiskTotalDto> totalDtoList  = golbalShowDto.getGoalbalTotalList();
        List<GobalRiskTotalDto> updateList  = new ArrayList<>();
        for(GobalRiskTotalDto temp:totalDtoList){
            if(temp.isEdit()){
                updateList.add(temp);
            }
        }
        for(GobalRiskTotalDto temp:updateList){
            dosave(temp);
        }
    }

    private void dosave(GobalRiskTotalDto temp) {
        HcsaRiskGlobalDto golDto = new HcsaRiskGlobalDto();
        HcsaRiskGolbalExtDto golExtDto = new HcsaRiskGolbalExtDto();
        List<HcsaRiskGolbalExtDto> extDtoList = new ArrayList<>();
        if(temp.getId()!=null){
            golDto = transferTogolDto(temp);
            extDtoList = transferToextDtoList(temp);
            updateLastVersion(golDto);
            create(golDto,extDtoList);
        }else{

        }
    }

    private void create(HcsaRiskGlobalDto golDto, List<HcsaRiskGolbalExtDto> extDtoList) {
        golDto.setVersion(golDto.getVersion()+1);
        golDto.setId(null);
        golDto = hcsaConfigClient.saveGoalbalMatrix(golDto).getEntity();
        String golId = golDto.getId();
        for(HcsaRiskGolbalExtDto temp:extDtoList){
            temp.setRsGolbalId(golId);
            temp.setId(null);
        }
        hcsaConfigClient.saveGoalbalExtMatrixList(extDtoList);
    }

    private void updateLastVersion(HcsaRiskGlobalDto golDto) {
        HcsaRiskGlobalDto lastVersionDto  = hcsaConfigClient.getRiskGolbalRiskMatraixBySvcCode(golDto.getServiceCode()).getEntity();
        String status = null;
        Date doeffDate = null;
        try {
            doeffDate = golDto.getEffectiveDate();
            Date lastVersionEndDate = lastVersionDto.getEndDate();
            if(lastVersionEndDate.getTime()<System.currentTimeMillis()){
                status = "CMSTAT003";
            }else{
                status = "CMSTAT001";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        lastVersionDto.setEndDate(doeffDate);
        lastVersionDto.setStatus(status);
        List<HcsaRiskGlobalDto> golDtoList= new ArrayList<>();
        golDtoList.add(lastVersionDto);
        hcsaConfigClient.udpateGoalbalMatrixList(golDtoList);

    }

    private List<HcsaRiskGolbalExtDto> transferToextDtoList(GobalRiskTotalDto temp) {
        List<HcsaRiskGolbalExtDto> extList = new ArrayList<>();
        HcsaRiskGolbalExtDto newExt = new HcsaRiskGolbalExtDto();
        newExt.setAppType(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
        newExt.setRsGolbalId(temp.getGalbalId());
        newExt.setInspectType(temp.getDonewInspectType());
        if("Y".equals(temp.getDonewIsPreInspect())){
            newExt.setPreInspect(true);
        }else{
            newExt.setPreInspect(false);
        }

        HcsaRiskGolbalExtDto renewExt = new HcsaRiskGolbalExtDto();
        renewExt.setAppType(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
        renewExt.setRsGolbalId(temp.getGalbalId());
        renewExt.setInspectType(temp.getDorenewInspectType());
        if("Y".equals(temp.getDorenewIsPreInspect())){
            renewExt.setPreInspect(true);
        }else{
            renewExt.setPreInspect(false);
        }
        extList.add(newExt);
        extList.add(renewExt);
        return extList;
    }

    private HcsaRiskGlobalDto transferTogolDto(GobalRiskTotalDto temp) {
        HcsaRiskGlobalDto dto = new HcsaRiskGlobalDto();
        if("Y".equals(temp.getDoAutoRenew())){
            dto.setAutoRenewal(true);
        }else{
            dto.setAutoRenewal(false);
        }
        try {
            dto.setEffectiveDate(Formatter.parseDate(temp.getDoEffectiveDate()));
            dto.setEndDate(Formatter.parseDate(temp.getDoEndDate()));
            dto.setLastInpectTh(Integer.parseInt(temp.getDoLastInspection()));
            dto.setMaxLicTenu(Integer.parseInt(temp.getDoMaxLic()));
            dto.setServiceCode(temp.getServiceCode());
        }catch (Exception e){
            e.printStackTrace();
        }
        return dto;
    }
}
