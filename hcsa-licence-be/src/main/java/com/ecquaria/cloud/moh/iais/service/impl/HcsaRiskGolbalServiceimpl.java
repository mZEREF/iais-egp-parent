package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.GobalRiskTotalDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.GolbalRiskShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskFeSupportDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskGlobalDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskGolbalExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.HcsaRiskGolbalService;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;

import java.util.Date;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @Author: jiahao
 * @Date: 2019/12/30 9:44
 */
@Service
@Slf4j
public class HcsaRiskGolbalServiceimpl implements HcsaRiskGolbalService {
    @Autowired
    private HcsaConfigClient hcsaConfigClient;
    @Autowired
    private BeEicGatewayClient beEicGatewayClient;
    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    @Override
    public GolbalRiskShowDto getGolbalRiskShowDto() {
        List<HcsaServiceDto> serviceDtoList = hcsaConfigClient.getActiveServices().getEntity();
        GolbalRiskShowDto showDto = hcsaConfigClient.getgolbalshow(serviceDtoList).getEntity();
        List<GobalRiskTotalDto> gobalRiskTotalDtoList = showDto.getGoalbalTotalList();
        return showDto;
    }

    @Override
    public List<SelectOption> getAutoOp() {
        List<SelectOption> autoRenew = IaisCommonUtils.genNewArrayList();
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
        String category[] = {"TOIR001", "TOIR002", "TOIR003"};
        List<SelectOption> inpTypeOp = MasterCodeUtil.retrieveOptionsByCodes(category);
        return inpTypeOp;
    }

    @Override
    public List<SelectOption> PreOrPostOp() {
        List<SelectOption> autoRenew = IaisCommonUtils.genNewArrayList();
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

        if (fin.getGalbalId() != null) {
            if (!StringUtil.stringEqual(fin.getBaseMaxLic(), maxLic) || !StringUtil.stringEqual(fin.getBaseLastInspection(), doLast) ||
                    !StringUtil.stringEqual(fin.getBaseAutoRenew(), autoreop) || !StringUtil.stringEqual(fin.getBasenewInspectType(), newinpTypeOps) ||
                    !StringUtil.stringEqual(fin.getBasenewIsPreInspect(), newPreOrPostOps) || !StringUtil.stringEqual(fin.getBaserenewInspectType(), renewinpTypeOps) ||
                    !StringUtil.stringEqual(fin.getBaserenewIsPreInspect(), renewPreOrPostOps) || !StringUtil.stringEqual(fin.getBaseEffectiveDate(), instartdate) ||
                    !StringUtil.stringEqual(fin.getBaseEndDate(), inEndDate)
                    ) {
                fin.setEdit(true);
            }
            fin.setDoMaxLic(maxLic);
            fin.setDoLastInspection(doLast);
            fin.setDoAutoRenew(autoreop);
            fin.setDonewInspectType(newinpTypeOps);
            fin.setDonewIsPreInspect(newPreOrPostOps);
            fin.setDorenewInspectType(renewinpTypeOps);
            fin.setDorenewIsPreInspect(renewPreOrPostOps);
            fin.setDoEffectiveDate(instartdate);
            fin.setDoEndDate(inEndDate);
        } else {
            getNullGolIdCloum(fin, maxLic, doLast, autoreop, newinpTypeOps, newPreOrPostOps, renewinpTypeOps, renewPreOrPostOps, instartdate, inEndDate);
        }

    }

    private void getNullGolIdCloum(GobalRiskTotalDto fin, String maxLic, String doLast, String autoreop,
                                   String newinpTypeOps, String newPreOrPostOps, String renewinpTypeOps,
                                   String renewPreOrPostOps, String instartdate, String inEndDate) {
        if (StringUtil.isEmpty(maxLic) && StringUtil.isEmpty(doLast) && StringUtil.isEmpty(autoreop) && StringUtil.isEmpty(newinpTypeOps) && StringUtil.isEmpty(newPreOrPostOps) && StringUtil.isEmpty(renewinpTypeOps) && StringUtil.isEmpty(renewPreOrPostOps)
                && StringUtil.isEmpty(instartdate) && StringUtil.isEmpty(inEndDate)) {
            fin.setEdit(false);
        } else {
            fin.setEdit(true);
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
    }

    @Override
    public void saveDto(GolbalRiskShowDto golbalShowDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        List<GobalRiskTotalDto> totalDtoList = golbalShowDto.getGoalbalTotalList();
        List<GobalRiskTotalDto> updateList = IaisCommonUtils.genNewArrayList();
        for (GobalRiskTotalDto temp : totalDtoList) {
            if (temp.isEdit()) {
                updateList.add(temp);
            }
        }
        for (GobalRiskTotalDto temp : updateList) {
            dosave(temp);
        }
        HcsaRiskFeSupportDto supportDto = new HcsaRiskFeSupportDto();
        supportDto.setGolbalRiskShowDto(golbalShowDto);
        supportDto.setGolbalFlag(true);
        beEicGatewayClient.feCreateRiskData(supportDto, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization());
    }

    private void dosave(GobalRiskTotalDto temp) {
        HcsaRiskGlobalDto golDto = new HcsaRiskGlobalDto();
        HcsaRiskGolbalExtDto golExtDto = new HcsaRiskGolbalExtDto();
        List<HcsaRiskGolbalExtDto> extDtoList = IaisCommonUtils.genNewArrayList();
        if (temp.getId() != null) {
            golDto = transferTogolDto(temp);
            extDtoList = transferToextDtoList(temp);
            updateLastVersion(golDto);
            create(golDto, extDtoList);
        } else {
            golDto = transferTogolDto(temp);
            extDtoList = transferToextDtoList(temp);
            create(golDto, extDtoList);
        }
    }

    private void create(HcsaRiskGlobalDto golDto, List<HcsaRiskGolbalExtDto> extDtoList) {
        if (golDto.getVersion() != null) {
            golDto.setVersion(golDto.getVersion() + 1);
        } else {
            golDto.setVersion(1);
        }
        golDto.setId(null);
        golDto.setStatus("CMSTAT001");
        golDto = hcsaConfigClient.saveGoalbalMatrix(golDto).getEntity();
        String golId = golDto.getId();
        for (HcsaRiskGolbalExtDto temp : extDtoList) {
            temp.setRsGolbalId(golId);
            temp.setId(null);
        }
        hcsaConfigClient.saveGoalbalExtMatrixList(extDtoList);
    }

    private void updateLastVersion(HcsaRiskGlobalDto golDto) {
        HcsaRiskGlobalDto lastVersionDto = hcsaConfigClient.getRiskGolbalRiskMatraixBySvcCode(golDto.getServiceCode()).getEntity();
        String status = null;
        Date doeffDate = null;
        try {
            doeffDate = golDto.getEffectiveDate();
            Date lastVersionEndDate = lastVersionDto.getEndDate();
            if (lastVersionEndDate.getTime() < System.currentTimeMillis()) {
                status = "CMSTAT003";
            } else {
                status = "CMSTAT001";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        lastVersionDto.setEndDate(doeffDate);
        lastVersionDto.setStatus(status);
        List<HcsaRiskGlobalDto> golDtoList = IaisCommonUtils.genNewArrayList();
        golDtoList.add(lastVersionDto);
        hcsaConfigClient.udpateGoalbalMatrixList(golDtoList);

    }

    private List<HcsaRiskGolbalExtDto> transferToextDtoList(GobalRiskTotalDto temp) {
        List<HcsaRiskGolbalExtDto> extList = IaisCommonUtils.genNewArrayList();
        HcsaRiskGolbalExtDto newExt = new HcsaRiskGolbalExtDto();
        newExt.setAppType(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
        newExt.setRsGolbalId(temp.getGalbalId());
        newExt.setInspectType(temp.getDonewInspectType());
        if ("Y".equals(temp.getDonewIsPreInspect())) {
            newExt.setPreInspect(true);
        } else {
            newExt.setPreInspect(false);
        }
        newExt.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        newExt.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        HcsaRiskGolbalExtDto renewExt = new HcsaRiskGolbalExtDto();
        renewExt.setAppType(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
        renewExt.setRsGolbalId(temp.getGalbalId());
        renewExt.setInspectType(temp.getDorenewInspectType());
        renewExt.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        renewExt.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        if ("Y".equals(temp.getDorenewIsPreInspect())) {
            renewExt.setPreInspect(true);
        } else {
            renewExt.setPreInspect(false);
        }
        extList.add(newExt);
        extList.add(renewExt);
        return extList;
    }

    private HcsaRiskGlobalDto transferTogolDto(GobalRiskTotalDto temp) {
        HcsaRiskGlobalDto dto = new HcsaRiskGlobalDto();
        if ("Y".equals(temp.getDoAutoRenew())) {
            dto.setAutoRenewal(true);
        } else {
            dto.setAutoRenewal(false);
        }
        try {
            dto.setEffectiveDate(Formatter.parseDate(temp.getDoEffectiveDate()));
            dto.setId(temp.getId());
            dto.setVersion(Integer.parseInt(temp.getVersion()));
            dto.setEndDate(Formatter.parseDate(temp.getDoEndDate()));
            dto.setLastInpectTh(Integer.parseInt(temp.getDoLastInspection()));
            dto.setMaxLicTenu(Integer.parseInt(temp.getDoMaxLic()));
            dto.setServiceCode(temp.getServiceCode());
            dto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dto;
    }
}
