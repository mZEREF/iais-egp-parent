package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskLegislativeMatrixDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskLegislativeShowDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.HcsaRiskLegislativeValidateDto;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.HcsaRiskLegislativeService;
import com.ecquaria.cloud.moh.iais.service.HcsaRiskService;
import com.ecquaria.cloud.moh.iais.validation.HcsaLegislativeValidate;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Author: jiahao
 * @Date: 2019/12/24 15:57
 */
@Delegator(value = "hcsaRiskLegislativeConfigDelegator")
@Slf4j
public class HcsaRiskLegislativeConfigDelegator {
    private HcsaRiskLegislativeService hcsaRiskLegislativeService;
    private HcsaRiskService hcsaRiskService;
    public HcsaRiskLegislativeConfigDelegator(HcsaRiskLegislativeService hcsaRiskLegislativeService){
        this.hcsaRiskLegislativeService = hcsaRiskLegislativeService;

    }

    public void start(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;
        AuditTrailHelper.auditFunction("hcsa-application", "hcsa application");

    }

    public void init(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the init start ...."));
        HttpServletRequest request = bpc.request;
        RiskLegislativeShowDto legislativeShowDto = hcsaRiskLegislativeService.getLegShowDto();
        ParamUtil.setSessionAttr(request, "legShowDto", legislativeShowDto);
        ;
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
        RiskLegislativeShowDto legislativeShowDto = (RiskLegislativeShowDto)ParamUtil.getSessionAttr(request, "legShowDto");
        legislativeShowDto = getDataFrompage(request, legislativeShowDto);
        HcsaLegislativeValidate financialRiskValidate = new HcsaLegislativeValidate();
        Map<String, String> errMap = financialRiskValidate.validate(request);
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
        RiskLegislativeShowDto legislativeShowDto = (RiskLegislativeShowDto)ParamUtil.getSessionAttr(request, "legShowDto");
        hcsaRiskLegislativeService.saveDto(legislativeShowDto);


    }

    public void backToMenu(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the backToMenu start ...."));
        HttpServletRequest request = bpc.request;
    }

    public RiskLegislativeShowDto getDataFrompage(HttpServletRequest request, RiskLegislativeShowDto financialShowDto) {
        List<HcsaRiskLegislativeMatrixDto> finList = financialShowDto.getLegislativeList();
        for (HcsaRiskLegislativeMatrixDto fin : finList) {
            String inthershold = ParamUtil.getString(request, fin.getSvcCode() + "inthershold");
            String inleftmod = ParamUtil.getString(request, fin.getSvcCode() + "inleftmod");
            String inlefthigh = ParamUtil.getString(request, fin.getSvcCode() + "inlefthigh");
            String inrightlow = ParamUtil.getString(request, fin.getSvcCode() + "inrightlow");
            String inrightmod = ParamUtil.getString(request, fin.getSvcCode() + "inrightmod");
            String inStartDate = ParamUtil.getString(request, fin.getSvcCode() + "instartdate");
            String inEndDate = ParamUtil.getString(request, fin.getSvcCode() + "inenddate");
            hcsaRiskLegislativeService.getOneFinDto(fin,inthershold
                    ,inleftmod,inlefthigh,inrightlow,inrightmod,inStartDate,inEndDate);
            clearErrFlag(fin);
        }
        financialShowDto.setLegislativeList(finList);
        ParamUtil.setSessionAttr(request, "legShowDto",financialShowDto);
        return financialShowDto;
    }
    public HcsaRiskLegislativeValidateDto getValueFromPage(HttpServletRequest request) {
        HcsaRiskLegislativeValidateDto dto = new HcsaRiskLegislativeValidateDto();
        RiskLegislativeShowDto legislativeShowDto = (RiskLegislativeShowDto) ParamUtil.getSessionAttr(request, "legShowDto");
        getDataFrompage(request, legislativeShowDto);
        return dto;
    }
    public void clearErrFlag(HcsaRiskLegislativeMatrixDto fin){
        fin.setDoLeftHighCaseCountherr(false);
        fin.setDoRightLowCaseCountherr(false);
        fin.setDoLeftModCaseCountherr(false);
        fin.setDoRightModCaseCountherr(false);
    }

}
