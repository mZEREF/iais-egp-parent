package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.checklist.HcsaChecklistConstants;
import com.ecquaria.cloud.moh.iais.common.constant.risk.RiskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskFinanceMatrixDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskFinancialShowDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.service.HcsaRiskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: jiahao
 * @Date: 2019/11/20 9:56
 */
@Delegator(value = "hcsaRiskLeadershipConfigDelegator")
@Slf4j
public class HcsaRiskLeadershipConfigDelegator {

    private HcsaRiskService hcsaRiskService;
    @Autowired
    public HcsaRiskLeadershipConfigDelegator(HcsaRiskService hcsaRiskService){
        this.hcsaRiskService = hcsaRiskService;
    }

    public void start(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;

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
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        String common = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CONFIG_MODULE);
        String type = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CONFIG_TYPE);
        String svcName = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CONFIG_SERVICE);
        String svcSubType = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CONFIG_SERVICE_SUB_TYPE);

    }

    public void confirm(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the PreConfirm start ...."));
        HttpServletRequest request = bpc.request;
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        String common = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CONFIG_MODULE);
        String type = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CONFIG_TYPE);
        String svcName = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CONFIG_SERVICE);
        String svcSubType = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CONFIG_SERVICE_SUB_TYPE);
        RiskFinancialShowDto financialShowDto = hcsaRiskService.getfinancialShowDto();
        ParamUtil.setSessionAttr(request, RiskConsts.FINANCIALSHOWDTO, financialShowDto);
        request.setAttribute(IaisEGPConstant.CRUD_ACTION_TYPE, "next");
    }

    public void doNext(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doNext start ...."));
        HttpServletRequest request = bpc.request;
        RiskFinancialShowDto financialShowDto = (RiskFinancialShowDto) ParamUtil.getSessionAttr(request, RiskConsts.FINANCIALSHOWDTO);
        getDataFrompage(request, financialShowDto);
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        String common = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CONFIG_MODULE);
        String type = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CONFIG_TYPE);
        String svcName = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CONFIG_SERVICE);
        String svcSubType = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CONFIG_SERVICE_SUB_TYPE);

        //do validation


    }

    public void submit(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doSubmit start ...."));
        HttpServletRequest request = bpc.request;
    }

    public void backToMenu(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the backToMenu start ...."));
        HttpServletRequest request = bpc.request;
    }

    public RiskFinancialShowDto getDataFrompage(HttpServletRequest request, RiskFinancialShowDto financialShowDto) {
        List<HcsaRiskFinanceMatrixDto> finList = financialShowDto.getFinanceList();

        boolean isInEdit = false;
        boolean isPrEdit = false;
        for (HcsaRiskFinanceMatrixDto fin : finList) {
            HcsaRiskFinanceMatrixDto newFinDto = new HcsaRiskFinanceMatrixDto();
            String prsource = ParamUtil.getString(request, fin.getServiceCode() + "prsource");
            String prthershold = ParamUtil.getString(request, fin.getServiceCode() + "prthershold");
            String prleftlow = ParamUtil.getString(request, fin.getServiceCode() + "prleftlow");
            String prleftmod = ParamUtil.getString(request, fin.getServiceCode() + "prleftmod");
            String prlefthigh = ParamUtil.getString(request, fin.getServiceCode() + "prlefthigh");
            String prrightlow = ParamUtil.getString(request, fin.getServiceCode() + "prrightlow");
            String prrightmod = ParamUtil.getString(request, fin.getServiceCode() + "prrightmod");
            String prrighthigh = ParamUtil.getString(request, fin.getServiceCode() + "prrighthigh");
            String insource = ParamUtil.getString(request, fin.getServiceCode() + "insource");
            String inthershold = ParamUtil.getString(request, fin.getServiceCode() + "inthershold");
            String inleftlow = ParamUtil.getString(request, fin.getServiceCode() + "inleftlow");
            String inleftmod = ParamUtil.getString(request, fin.getServiceCode() + "inleftmod");
            String inlefthigh = ParamUtil.getString(request, fin.getServiceCode() + "inlefthigh");
            String inrightlow = ParamUtil.getString(request, fin.getServiceCode() + "inrightlow");
            String inrightmod = ParamUtil.getString(request, fin.getServiceCode() + "inrightmod");
            String inrighthigh = ParamUtil.getString(request, fin.getServiceCode() + "inrighthigh");

        }
        return null;
    }



}
