package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskLeadershipMatrixDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskLeaderShipShowDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceBeConstant;
import com.ecquaria.cloud.moh.iais.dto.HcsaRiskLeaderShipVadlidateDto;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.HcsaRiskLeaderShipService;
import com.ecquaria.cloud.moh.iais.util.LicenceUtil;
import com.ecquaria.cloud.moh.iais.validation.HcsaLeadershipValidate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author: jiahao
 * @Date: 2019/11/20 9:56
 */
@Delegator(value = "hcsaRiskLeadershipConfigDelegator")
@Slf4j
public class HcsaRiskLeadershipConfigDelegator {
    private static final String LEADER_SHOW_DTO = "leaderShowDto";
    @Autowired
    private HcsaRiskLeaderShipService hcsaRiskLeaderShipService;

    public void start(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_SYSTEM_CONFIG, AuditTrailConsts.FUNCTION_LEADERSHIP_RISK_CONFIG);

    }

    public void init(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the init start ...."));
        HttpServletRequest request = bpc.request;
        RiskLeaderShipShowDto leaderShowDto = hcsaRiskLeaderShipService.getLeaderShowDto();
        ParamUtil.setSessionAttr(request, LEADER_SHOW_DTO, leaderShowDto);
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
        RiskLeaderShipShowDto leaderShowDto = (RiskLeaderShipShowDto) ParamUtil.getSessionAttr(request, LEADER_SHOW_DTO);
        ParamUtil.setSessionAttr(request, LEADER_SHOW_DTO, leaderShowDto);
        ParamUtil.setSessionAttr(request,"yearSelectOptions",(Serializable)LicenceUtil.getRiskYearsForGlobalRisk());

    }

    public void confirm(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the PreConfirm start ...."));
        HttpServletRequest request = bpc.request;
        RiskLeaderShipShowDto leaderShowDto = (RiskLeaderShipShowDto) ParamUtil.getSessionAttr(request, LEADER_SHOW_DTO);
        ParamUtil.setSessionAttr(request, LEADER_SHOW_DTO, leaderShowDto);
    }

    public void doNext(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doNext start ...."));
        HttpServletRequest request = bpc.request;
        RiskLeaderShipShowDto leaderShowDto = (RiskLeaderShipShowDto) ParamUtil.getSessionAttr(request, LEADER_SHOW_DTO);
        getDataFrompage(request, leaderShowDto);
        HcsaLeadershipValidate leadershipValidate = new HcsaLeadershipValidate();
        Map<String, String> errMap = leadershipValidate.validate(request);
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
        RiskLeaderShipShowDto leaderShowDto = (RiskLeaderShipShowDto) ParamUtil.getSessionAttr(request, LEADER_SHOW_DTO);
        if(hcsaRiskLeaderShipService.compareVersionsForRiskLeaderShip(leaderShowDto,hcsaRiskLeaderShipService.getLeaderShowDto())){
            hcsaRiskLeaderShipService.saveDto(leaderShowDto);
        }else {
            ParamUtil.setRequestAttr(request, HcsaLicenceBeConstant.REQUEST_FOR_ACK_CODE,HcsaLicenceBeConstant.REQUEST_FOR_ACK_CODE);
        }
    }

    public void backToMenu(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the backToMenu start ...."));
    }

    public RiskLeaderShipShowDto getDataFrompage(HttpServletRequest request, RiskLeaderShipShowDto financialShowDto) {
        List<HcsaRiskLeadershipMatrixDto> finList = financialShowDto.getLeaderShipDtoList();
        for (HcsaRiskLeadershipMatrixDto fin : finList) {
            String prsource = ParamUtil.getString(request, fin.getSvcCode() + "prsource");
            String prthershold = ParamUtil.getString(request, fin.getSvcCode() + "prthershold");
            String prleftmod = ParamUtil.getString(request, fin.getSvcCode() + "prleftmod");
            String prlefthigh = ParamUtil.getString(request, fin.getSvcCode() + "prlefthigh");
            String prrightlow = ParamUtil.getString(request, fin.getSvcCode() + "prrightlow");
            String prrightmod = ParamUtil.getString(request, fin.getSvcCode() + "prrightmod");
            String insource = ParamUtil.getString(request, fin.getSvcCode() + "insource");
            String inthershold = ParamUtil.getString(request, fin.getSvcCode() + "inthershold");
            String inleftmod = ParamUtil.getString(request, fin.getSvcCode() + "inleftmod");
            String inlefthigh = ParamUtil.getString(request, fin.getSvcCode() + "inlefthigh");
            String inrightlow = ParamUtil.getString(request, fin.getSvcCode() + "inrightlow");
            String inrightmod = ParamUtil.getString(request, fin.getSvcCode() + "inrightmod");
            String inStartDate = ParamUtil.getString(request, fin.getSvcCode() + "instartdate");
            String inEndDate = ParamUtil.getString(request, fin.getSvcCode() + "inenddate");
            String prStartDate = ParamUtil.getString(request, fin.getSvcCode() + "prstartdate");
            String prEndDate = ParamUtil.getString(request, fin.getSvcCode() + "prenddate");
            hcsaRiskLeaderShipService.getOneFinDto(fin,prsource,prthershold,prleftmod,prlefthigh,prrightlow,prrightmod,insource,inthershold
                    ,inleftmod,inlefthigh,inrightlow,inrightmod,inStartDate,inEndDate,prStartDate,prEndDate);
            clearErrFlag(fin);
        }
        financialShowDto.setLeaderShipDtoList(finList);
        ParamUtil.setSessionAttr(request,LEADER_SHOW_DTO,financialShowDto);
        return financialShowDto;
    }

    public HcsaRiskLeaderShipVadlidateDto getValueFromPage(HttpServletRequest request) {
        HcsaRiskLeaderShipVadlidateDto dto = new HcsaRiskLeaderShipVadlidateDto();
        RiskLeaderShipShowDto financialShowDto = (RiskLeaderShipShowDto) ParamUtil.getSessionAttr(request, LEADER_SHOW_DTO);
        getDataFrompage(request, financialShowDto);
        return dto;
    }
    public void clearErrFlag(HcsaRiskLeadershipMatrixDto fin) {
        fin.setAdEffectiveEndDateerr(false);
        fin.setAdEffectiveStartDateerr(false);
        fin.setAdThersholderr(false);
        fin.setAdRightLowCaseCountherr(false);
        fin.setAdLeftModCaseCountherr(false);
        fin.setAdRightModCaseCountherr(false);
        fin.setAdLeftHighCaseCountherr(false);
        fin.setDpEffectiveEndDateerr(false);
        fin.setDpEffectiveStartDateerr(false);
        fin.setDpThersholderr(false);
        fin.setDpRightLowCaseCountherr(false);
        fin.setDpLeftModCaseCountherr(false);
        fin.setDpRightModCaseCountherr(false);
    }
}
