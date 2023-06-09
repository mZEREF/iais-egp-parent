package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.risk.RiskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskFinanceMatrixDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskFinancialShowDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceBeConstant;
import com.ecquaria.cloud.moh.iais.dto.HcsaRiskFinianceVadlidateDto;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.HcsaRiskService;
import com.ecquaria.cloud.moh.iais.util.LicenceUtil;
import com.ecquaria.cloud.moh.iais.validation.HcsaFinancialRiskValidate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author: jiahao
 * @Date: 2019/11/13 18:10
 */
@Delegator(value = "hcsaRiskFinancialConfigDelegator")
@Slf4j
public class HcsaRiskFinancialConfigDelegator {
    @Autowired
    private HcsaRiskService hcsaRiskService;

    public void start(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_SYSTEM_CONFIG, AuditTrailConsts.FUNCTION_FINANCIAL_RISK_CONFIG);
    }

    public void init(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the init start ...."));
        HttpServletRequest request = bpc.request;
        RiskFinancialShowDto financialShowDto = hcsaRiskService.getfinancialShowDto();
        ParamUtil.setSessionAttr(request, RiskConsts.FINANCIALSHOWDTO, financialShowDto);
        ;
    }

    public void prepare(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the PreConfig start ...."));
        HttpServletRequest request = bpc.request;
        String  url = request.getRequestURI();
        if(!StringUtil.isEmpty(url) && url.contains(HcsaLicenceBeConstant.MOH_RISK_CONIG_MENU)){
            ParamUtil.setSessionAttr(request,HcsaLicenceBeConstant.RISK_NEED_BACK_BUTTON,HcsaLicenceBeConstant.RISK_NEED_BACK_BUTTON_YES);
        }else {
            ParamUtil.setSessionAttr(request,HcsaLicenceBeConstant.RISK_NEED_BACK_BUTTON,HcsaLicenceBeConstant.RISK_NEED_BACK_BUTTON_NO);
        }
        RiskFinancialShowDto financialShowDto = (RiskFinancialShowDto) ParamUtil.getSessionAttr(request, RiskConsts.FINANCIALSHOWDTO);
        ParamUtil.setSessionAttr(request, RiskConsts.FINANCIALSHOWDTO, financialShowDto);
        ParamUtil.setSessionAttr(request,"yearSelectOptions",(Serializable)LicenceUtil.getRiskYearsForGlobalRisk());
    }

    public void confirm(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the PreConfirm start ...."));
        HttpServletRequest request = bpc.request;
        RiskFinancialShowDto financialShowDto = (RiskFinancialShowDto) ParamUtil.getSessionAttr(request, RiskConsts.FINANCIALSHOWDTO);
        ParamUtil.setSessionAttr(request, RiskConsts.FINANCIALSHOWDTO, financialShowDto);
        ParamUtil.setSessionAttr(request, RiskConsts.FINANCIALSHOWDTO, financialShowDto);
    }

    public void doNext(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doNext start ...."));
        HttpServletRequest request = bpc.request;
        RiskFinancialShowDto financialShowDto = (RiskFinancialShowDto) ParamUtil.getSessionAttr(request, RiskConsts.FINANCIALSHOWDTO);
        financialShowDto = getDataFrompage(request, financialShowDto);
        ParamUtil.setSessionAttr(request, RiskConsts.FINANCIALSHOWDTO, financialShowDto);
        HcsaFinancialRiskValidate financialRiskValidate = new HcsaFinancialRiskValidate();
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
        RiskFinancialShowDto financialShowDto = (RiskFinancialShowDto) ParamUtil.getSessionAttr(request, RiskConsts.FINANCIALSHOWDTO);
        if(hcsaRiskService.compareVersionsForRiskFinancial(financialShowDto,hcsaRiskService.getfinancialShowDto())){
            hcsaRiskService.saveDto(financialShowDto);
        }else {
            ParamUtil.setRequestAttr(request, HcsaLicenceBeConstant.REQUEST_FOR_ACK_CODE,HcsaLicenceBeConstant.REQUEST_FOR_ACK_CODE);
        }
    }

    public void backToMenu(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the backToMenu start ...."));
        HttpServletRequest request = bpc.request;
    }

    public RiskFinancialShowDto getDataFrompage(HttpServletRequest request, RiskFinancialShowDto financialShowDto) {
        List<HcsaRiskFinanceMatrixDto> finList = financialShowDto.getFinanceList();
        for (HcsaRiskFinanceMatrixDto fin : finList) {
            String prsource = ParamUtil.getString(request, fin.getServiceCode() + "prsource");
            String prthershold = ParamUtil.getString(request, fin.getServiceCode() + "prthershold");
            String prleftmod = ParamUtil.getString(request, fin.getServiceCode() + "prleftmod");
            String prlefthigh = ParamUtil.getString(request, fin.getServiceCode() + "prlefthigh");
            String prrightlow = ParamUtil.getString(request, fin.getServiceCode() + "prrightlow");
            String prrightmod = ParamUtil.getString(request, fin.getServiceCode() + "prrightmod");
            String insource = ParamUtil.getString(request, fin.getServiceCode() + "insource");
            String inthershold = ParamUtil.getString(request, fin.getServiceCode() + "inthershold");
            String inleftmod = ParamUtil.getString(request, fin.getServiceCode() + "inleftmod");
            String inlefthigh = ParamUtil.getString(request, fin.getServiceCode() + "inlefthigh");
            String inrightlow = ParamUtil.getString(request, fin.getServiceCode() + "inrightlow");
            String inrightmod = ParamUtil.getString(request, fin.getServiceCode() + "inrightmod");
            String inStartDate = ParamUtil.getString(request, fin.getServiceCode() + "instartdate");
            String inEndDate = ParamUtil.getString(request, fin.getServiceCode() + "inenddate");
            String prStartDate = ParamUtil.getString(request, fin.getServiceCode() + "prstartdate");
            String prEndDate = ParamUtil.getString(request, fin.getServiceCode() + "prenddate");
            hcsaRiskService.getOneFinDto(fin,prsource,prthershold,prleftmod,prlefthigh,prrightlow,prrightmod,insource,inthershold
            ,inleftmod,inlefthigh,inrightlow,inrightmod,inStartDate,inEndDate,prStartDate,prEndDate);
            clearErrFlag(fin);
        }
        financialShowDto.setFinanceList(finList);
        ParamUtil.setSessionAttr(request,RiskConsts.FINANCIALSHOWDTO,financialShowDto);
        return financialShowDto;
    }

    public void clearErrFlag(HcsaRiskFinanceMatrixDto fin){
        fin.setInEffectiveEndDateerr(false);
        fin.setInEffectiveStartDateerr(false);
        fin.setInThersholderr(false);
        fin.setInRightLowCaseCountherr(false);
        fin.setInLeftModCaseCountherr(false);
        fin.setInRightModCaseCountherr(false);
        fin.setInLeftHighCaseCounterr(false);
        fin.setPrEffectiveEndDateerr(false);
        fin.setPrEffectiveStartDateerr(false);
        fin.setPrThersholderr(false);
        fin.setPrRightLowCaseCountherr(false);
        fin.setPrLeftModCaseCountherr(false);
        fin.setPrRightModCaseCountherr(false);
        fin.setPrLeftHighCaseCounterr(false);
    }
    public HcsaRiskFinianceVadlidateDto getValueFromPage(HttpServletRequest request) {
        HcsaRiskFinianceVadlidateDto dto = new HcsaRiskFinianceVadlidateDto();
        RiskFinancialShowDto financialShowDto = (RiskFinancialShowDto) ParamUtil.getSessionAttr(request, RiskConsts.FINANCIALSHOWDTO);
        getDataFrompage(request, financialShowDto);
        return dto;
    }

}
