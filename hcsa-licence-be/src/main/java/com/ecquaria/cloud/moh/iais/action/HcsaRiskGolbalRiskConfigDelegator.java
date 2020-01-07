package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.GobalRiskTotalDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.GolbalRiskShowDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.HcsaRiskGolbalVadlidateDto;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.HcsaRiskGolbalService;
import com.ecquaria.cloud.moh.iais.validation.HcsaGolbalValidate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author: jiahao
 * @Date: 2019/12/28 15:20
 */
@Delegator(value = "hcsaRiskGolbalRiskConfigDelegator")
@Slf4j
public class HcsaRiskGolbalRiskConfigDelegator {
    @Autowired
    HcsaRiskGolbalService hcsaRiskGolbalService;
    public HcsaRiskGolbalRiskConfigDelegator(HcsaRiskGolbalService hcsaRiskGolbalService){
        this.hcsaRiskGolbalService = hcsaRiskGolbalService;

    }

    public void start(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;

    }

    public void init(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the init start ...."));
        HttpServletRequest request = bpc.request;
        GolbalRiskShowDto showDto = hcsaRiskGolbalService.getGolbalRiskShowDto();
        List<SelectOption> autoRenewOp = hcsaRiskGolbalService.getAutoOp();
        List<SelectOption> inpTypeOp = hcsaRiskGolbalService.inpTypeOp();
        List<SelectOption> PreOrPostOp = hcsaRiskGolbalService.PreOrPostOp();
        ParamUtil.setSessionAttr(request, "autoRenewOp", (Serializable) autoRenewOp);
        ParamUtil.setSessionAttr(request, "golbalShowDto", showDto);
        ParamUtil.setSessionAttr(request, "inpTypeOp", (Serializable)inpTypeOp);
        ParamUtil.setSessionAttr(request, "PreOrPostOp", (Serializable)PreOrPostOp);
    }

    public void prepare(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the PreConfig start ...."));
        HttpServletRequest request = bpc.request;
    }

    public void confirm(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the PreConfirm start ...."));
        HttpServletRequest request = bpc.request;
    }

    public void doNext(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doNext start ...."));
        HttpServletRequest request = bpc.request;
        GolbalRiskShowDto golbalShowDto = (GolbalRiskShowDto)ParamUtil.getSessionAttr(request, "golbalShowDto");
        golbalShowDto = getDataFrompage(request, golbalShowDto);
        HcsaGolbalValidate golBalvad = new HcsaGolbalValidate();
        Map<String, String> errMap = golBalvad.validate(request);
        if(errMap.isEmpty()){
            ParamUtil.setRequestAttr(request, "isValid", "N");
        }else{
            ParamUtil.setRequestAttr(request, "isValid", "Y");
            ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errMap));
        }

    }

    public void submit(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doSubmit start ...."));
        HttpServletRequest request = bpc.request;
        GolbalRiskShowDto golbalShowDto = (GolbalRiskShowDto)ParamUtil.getSessionAttr(request, "golbalShowDto");
       // hcsaRiskWeightageService.saveDto(wightageDto);
        hcsaRiskGolbalService.saveDto(golbalShowDto);

    }

    public void backToMenu(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the backToMenu start ...."));
        HttpServletRequest request = bpc.request;
    }

    public GolbalRiskShowDto getDataFrompage(HttpServletRequest request, GolbalRiskShowDto golbalRiskShowDto) {
        List<GobalRiskTotalDto> finList = golbalRiskShowDto.getGoalbalTotalList();
        for (GobalRiskTotalDto fin : finList) {
            String maxLic = ParamUtil.getString(request, fin.getServiceCode() + "maxLic");
            String doLast = ParamUtil.getString(request, fin.getServiceCode() + "doLast");
            String autoreop = ParamUtil.getString(request, fin.getServiceCode() + "autoreop");
            String newinpTypeOps = ParamUtil.getString(request, fin.getServiceCode() + "newinpTypeOps");
            String newPreOrPostOps = ParamUtil.getString(request, fin.getServiceCode() + "newPreOrPostOps");
            String renewinpTypeOps = ParamUtil.getString(request, fin.getServiceCode() + "renewinpTypeOps");
            String renewPreOrPostOps = ParamUtil.getString(request, fin.getServiceCode() + "renewPreOrPostOps");
            String instartdate = ParamUtil.getString(request, fin.getServiceCode() + "instartdate");
            String inEndDate = ParamUtil.getString(request, fin.getServiceCode() + "inenddate");
            hcsaRiskGolbalService.setGolShowDto(fin,maxLic,doLast,autoreop,newinpTypeOps,newPreOrPostOps,renewinpTypeOps,renewPreOrPostOps,instartdate,inEndDate);
        }
        ParamUtil.setSessionAttr(request, "golbalShowDto", golbalRiskShowDto);
        return golbalRiskShowDto;
    }

    public HcsaRiskGolbalVadlidateDto getValueFromPage(HttpServletRequest request) {
        HcsaRiskGolbalVadlidateDto dto = new HcsaRiskGolbalVadlidateDto();
        GolbalRiskShowDto golbalShowDto = (GolbalRiskShowDto)ParamUtil.getSessionAttr(request, "golbalShowDto");
        getDataFrompage(request, golbalShowDto);
        return dto;
    }
}
