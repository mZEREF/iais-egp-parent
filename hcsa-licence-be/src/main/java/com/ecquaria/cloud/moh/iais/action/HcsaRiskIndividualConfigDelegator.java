package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskInspectionMatrixDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.InspectionShowDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceBeConstant;
import com.ecquaria.cloud.moh.iais.dto.HcsaInspectionValidateDto;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.HcsaRiskInspectionService;
import com.ecquaria.cloud.moh.iais.util.LicenceUtil;
import com.ecquaria.cloud.moh.iais.validation.HcsaInspectionValidate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author: jiahao
 * @Date: 2019/11/21 14:54
 */
@Delegator(value = "hcsaRiskIndividualConfigDelegator")
@Slf4j
public class HcsaRiskIndividualConfigDelegator {
    @Autowired
    private HcsaRiskInspectionService hcsaRiskInspectionService;
    public void start(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;
        AuditTrailHelper.auditFunction("hcsa-application", "hcsa application");

    }

    public void init(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the init start ...."));
        HttpServletRequest request = bpc.request;
        InspectionShowDto showDto = hcsaRiskInspectionService.getInspectionShowDto();
        ParamUtil.setSessionAttr(request,"inShowDto",showDto);
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
        ParamUtil.setSessionAttr(request,"yearSelectOptions",(Serializable)LicenceUtil.getRiskYearsForGlobalRisk());

    }


    public void doNext(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doNext start ...."));
        HttpServletRequest request = bpc.request;
        InspectionShowDto showDto = (InspectionShowDto)ParamUtil.getSessionAttr(request,"inShowDto");
        getDataFrompage(request, showDto);
        //do validation
        ParamUtil.setSessionAttr(request,"inShowDto",showDto);
        HcsaInspectionValidate inspectionValidate = new HcsaInspectionValidate();
        Map<String, String> errMap = inspectionValidate.validate(request);
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
        InspectionShowDto showDto = (InspectionShowDto)ParamUtil.getSessionAttr(request,"inShowDto");
        hcsaRiskInspectionService.saveDto(showDto);
    }

    public void backToMenu(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the backToMenu start ...."));
        HttpServletRequest request = bpc.request;
    }

    public InspectionShowDto getDataFrompage(HttpServletRequest request, InspectionShowDto financialShowDto) {
        List<HcsaRiskInspectionMatrixDto> finList = financialShowDto.getInspectionDtoList();
        for (HcsaRiskInspectionMatrixDto fin : finList) {
            String mjleftmod = ParamUtil.getString(request, fin.getSvcCode() + "mjleftmod");
            String mjlefthigh = ParamUtil.getString(request, fin.getSvcCode() + "mjlefthigh");
            String mjrightlow = ParamUtil.getString(request, fin.getSvcCode() + "mjrightlow");
            String mjrightmod = ParamUtil.getString(request, fin.getSvcCode() + "mjrightmod");
            String mjStartDate = ParamUtil.getString(request, fin.getSvcCode() + "mjstartdate");
            String mjEndDate = ParamUtil.getString(request, fin.getSvcCode() + "mjenddate");
            String caleftmod = ParamUtil.getString(request, fin.getSvcCode() + "caleftmod");
            String calefthigh = ParamUtil.getString(request, fin.getSvcCode() + "calefthigh");
            String carightlow = ParamUtil.getString(request, fin.getSvcCode() + "carightlow");
            String carightmod = ParamUtil.getString(request, fin.getSvcCode() + "carightmod");
            String caStartDate = ParamUtil.getString(request, fin.getSvcCode() + "castartdate");
            String caEndDate = ParamUtil.getString(request, fin.getSvcCode() + "caenddate");
            String mileftmod = ParamUtil.getString(request, fin.getSvcCode() + "mileftmod");
            String milefthigh = ParamUtil.getString(request, fin.getSvcCode() + "milefthigh");
            String mirightlow = ParamUtil.getString(request, fin.getSvcCode() + "mirightlow");
            String mirightmod = ParamUtil.getString(request, fin.getSvcCode() + "mirightmod");
            String miStartDate = ParamUtil.getString(request, fin.getSvcCode() + "mistartdate");
            String miEndDate = ParamUtil.getString(request, fin.getSvcCode() + "mienddate");
            hcsaRiskInspectionService.getOneFinDto(fin,caleftmod,calefthigh,carightlow,carightmod,caStartDate,caEndDate
                    ,mileftmod,milefthigh,mirightlow,mirightmod,miStartDate,miEndDate
                    ,mjleftmod,mjlefthigh,mjrightlow,mjrightmod,mjStartDate,mjEndDate);
            clearErrFlag(fin);
        }
        financialShowDto.setInspectionDtoList(finList);
        ParamUtil.setSessionAttr(request,"inShowDto",financialShowDto);
        return financialShowDto;
    }

    public void clearErrFlag(HcsaRiskInspectionMatrixDto fin){
        fin.setDoCaRightLowCountherr(false);
        fin.setDoCaLeftModCountherr(false);
        fin.setDoCaRightModCountherr(false);
        fin.setDoCaLeftHighCountherr(false);
        fin.setDoMiRightLowCountherr(false);
        fin.setDoMiLeftModCountherr(false);
        fin.setDoMiRightModCountherr(false);
        fin.setDoMiLeftHighCountherr(false);
        fin.setDoMjRightLowCountherr(false);
        fin.setDoMjLeftModCountherr(false);
        fin.setDoMjRightModCountherr(false);
        fin.setDoMjLeftHighCountherr(false);
    }
    public HcsaInspectionValidateDto getValueFromPage(HttpServletRequest request) {
        HcsaInspectionValidateDto dto = new HcsaInspectionValidateDto();
        InspectionShowDto showDto = (InspectionShowDto)ParamUtil.getSessionAttr(request,"inShowDto");
        getDataFrompage(request, showDto);
        return dto;
    }
}
