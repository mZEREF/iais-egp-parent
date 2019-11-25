package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.checklist.HcsaChecklistConstants;
import com.ecquaria.cloud.moh.iais.common.constant.risk.RiskConsts;
import com.ecquaria.cloud.moh.iais.common.constant.sample.DemoConstants;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskFinanceMatrixDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskFinancialShowDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.service.HcsaRiskService;
import com.ecquaria.cloud.moh.iais.valiation.HcsaFinancialRiskValidate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: jiahao
 * @Date: 2019/11/13 18:10
 */
@Delegator(value = "hcsaRiskFinancialConfigDelegator")
@Slf4j
public class HcsaRiskFinancialConfigDelegator {
    private HcsaRiskService hcsaRiskService;

    @Autowired
    public HcsaRiskFinancialConfigDelegator(HcsaRiskService hcsaRiskService) {
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
        RiskFinancialShowDto financialShowDto = (RiskFinancialShowDto) ParamUtil.getSessionAttr(request, RiskConsts.FINANCIALSHOWDTO);
        ParamUtil.setSessionAttr(request, RiskConsts.FINANCIALSHOWDTO, financialShowDto);
    }

    public void confirm(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the PreConfirm start ...."));
        HttpServletRequest request = bpc.request;
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        String common = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CONFIG_MODULE);
        String type = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CONFIG_TYPE);
        String svcName = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CONFIG_SERVICE);
        String svcSubType = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CONFIG_SERVICE_SUB_TYPE);
        RiskFinancialShowDto financialShowDto = (RiskFinancialShowDto) ParamUtil.getSessionAttr(request, RiskConsts.FINANCIALSHOWDTO);
        ParamUtil.setSessionAttr(request, RiskConsts.FINANCIALSHOWDTO, financialShowDto);
        ParamUtil.setSessionAttr(request, RiskConsts.FINANCIALSHOWDTO, financialShowDto);
        request.setAttribute(IaisEGPConstant.CRUD_ACTION_TYPE, "next");
    }

    public void doNext(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doNext start ...."));
        HttpServletRequest request = bpc.request;
        RiskFinancialShowDto financialShowDto = (RiskFinancialShowDto) ParamUtil.getSessionAttr(request, RiskConsts.FINANCIALSHOWDTO);
        financialShowDto = getDataFrompage(request, financialShowDto);
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        String common = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CONFIG_MODULE);
        String type = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CONFIG_TYPE);
        String svcName = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CONFIG_SERVICE);
        String svcSubType = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CONFIG_SERVICE_SUB_TYPE);
        ParamUtil.setSessionAttr(request, RiskConsts.FINANCIALSHOWDTO, financialShowDto);
        HcsaFinancialRiskValidate financialRiskValidate = new HcsaFinancialRiskValidate();
        Map<String, String> errMap = financialRiskValidate.validate(request);
        if(errMap.isEmpty()){
            ParamUtil.setRequestAttr(request, "isValid", "N");
        }else{
            ParamUtil.setRequestAttr(request, "isValid", "Y");
            ParamUtil.setRequestAttr(request, DemoConstants.ERRORMAP,errMap);
        }

    }

    public void submit(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doSubmit start ...."));
        HttpServletRequest request = bpc.request;
        RiskFinancialShowDto financialShowDto = (RiskFinancialShowDto) ParamUtil.getSessionAttr(request, RiskConsts.FINANCIALSHOWDTO);
        hcsaRiskService.saveDto(financialShowDto);

    }

    public void backToMenu(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the backToMenu start ...."));
        HttpServletRequest request = bpc.request;
    }

    public RiskFinancialShowDto getDataFrompage(HttpServletRequest request, RiskFinancialShowDto financialShowDto) {
        List<HcsaRiskFinanceMatrixDto> finList = financialShowDto.getFinanceList();
        for (HcsaRiskFinanceMatrixDto fin : finList) {
            HcsaRiskFinanceMatrixDto newFinDto = new HcsaRiskFinanceMatrixDto();
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
            String inStartDate = ParamUtil.getDate(request, fin.getServiceCode() + "instartdate");
            String inEndDate = ParamUtil.getDate(request, fin.getServiceCode() + "inenddate");
            String prStartDate = ParamUtil.getDate(request, fin.getServiceCode() + "prstartdate");
            String prEndDate = ParamUtil.getDate(request, fin.getServiceCode() + "prenddate");
            getOneFinDto(fin,prsource,prthershold,prleftmod,prlefthigh,prrightlow,prrightmod,insource,inthershold
            ,inleftmod,inlefthigh,inrightlow,inrightmod,inStartDate,inEndDate,prStartDate,prEndDate);
        }
        financialShowDto.setFinanceList(finList);
        ParamUtil.setSessionAttr(request,RiskConsts.FINANCIALSHOWDTO,financialShowDto);
        return financialShowDto;
    }

    public void getOneFinDto(HcsaRiskFinanceMatrixDto fin,String prsource,String prthershold,
                    String prleftmod,String prlefthigh,String prrightlow,String prrightmod,
                    String insource,String inthershold,String inleftmod,String inlefthigh,String inrightlow,String inrightmod,
                             String inStartDate,String inEndDate,String prStartDate,String prEndDate){
        Integer isInEditNum = 0;
        Integer prInEditNum = 0;
        if(!StringUtil.isEmpty(fin.getFinSource())) {
            if ("SOURCE001".equals(fin.getInSource())) {
                if (!(fin.getInThershold() + "").equals(inthershold)) {
                    fin.setInThershold(inthershold);
                    isInEditNum++;
                }
                String baseRightLowCase = getRightLowCase(fin.getBaseInLowCaseCountth());
                if (!baseRightLowCase.equals(inrightlow)) {
                    fin.setInRightLowCaseCounth(inrightlow);
                    isInEditNum++;
                }

                if (!fin.getBaseInLowCaseCountth().equals(inleftmod)) {
                    fin.setInLeftModCaseCounth(inleftmod);
                    isInEditNum++;
                }
                if (!fin.getBaseInHighCaseCountth().equals(inrightmod)) {
                    fin.setInRightModCaseCounth(inrightmod);
                    isInEditNum++;
                }
                String baseLeftHighCase = getLeftHighCase(fin.getBaseInHighCaseCountth());
                if (!baseLeftHighCase.equals(inlefthigh)) {
                    fin.setInLeftHighCaseCount(inlefthigh);
                    isInEditNum++;
                }
                if(!fin.getBaseInEffectiveStartDate().equals(inStartDate)){
                    fin.setInEffectiveStartDate(inStartDate);
                    isInEditNum++;
                }
                if(!fin.getBaseInEffectiveEndDate().equals(inEndDate)){
                    fin.setInEffectiveEndDate(inEndDate);
                    isInEditNum++;
                }
                if (isInEditNum >=1) {
                    fin.setInIsEdit(true);
                } else {
                    fin.setInIsEdit(false);
                }
            }
            if ("SOURCE002".equals(fin.getPrSource())) {
                if (!(fin.getPrThershold() + "").equals(prthershold)) {
                    fin.setPrThershold(prthershold);
                    prInEditNum++;
                }
                String baseRightLowCase = getRightLowCase(fin.getBasePrLowCaseCountth());
                if (!baseRightLowCase.equals(prrightlow)) {
                    fin.setPrRightLowCaseCounth(prrightlow);
                    prInEditNum++;
                }
                if (!fin.getBasePrLowCaseCountth().equals(prleftmod)) {
                    fin.setPrLeftModCaseCounth(prleftmod);
                    prInEditNum++;
                }
                if (!fin.getBasePrHighCaseCountth().equals(prrightmod)) {
                    fin.setPrRightModCaseCounth(prrightmod);
                    prInEditNum++;
                }
                String baseLeftHighCase = getLeftHighCase(fin.getBasePrHighCaseCountth());
                if (!baseLeftHighCase.equals(prlefthigh)) {
                    fin.setPrLeftHighCaseCount(prlefthigh);
                    prInEditNum++;
                }
                if(!fin.getBasePrEffectiveStartDate().equals(prStartDate)){
                    fin.setPrEffectiveStartDate(prStartDate);
                    prInEditNum++;
                }
                if(!fin.getBasePrEffectiveEndDate().equals(prEndDate)){
                    fin.setPrEffectiveEndDate(prEndDate);
                    prInEditNum++;
                }
                if (prInEditNum >= 1) {
                    fin.setPrIsEdit(true);
                } else {
                    fin.setPrIsEdit(false);
                }
            }
        }else {
                boolean isInEdit = isNoBaseSourceIsEdit(fin, insource, inthershold, inrightlow, inleftmod, inlefthigh, inrightmod,inStartDate,inEndDate);
                if(isInEdit){
                    fin.setInIsEdit(isInEdit);
                    fin.setInSource(insource);
                    fin.setInThershold(inthershold);
                    fin.setInRightLowCaseCounth(inrightlow);
                    fin.setInLeftModCaseCounth(inleftmod);
                    fin.setInLeftHighCaseCount(inlefthigh);
                    fin.setInRightModCaseCounth(inrightmod);
                    fin.setInEffectiveStartDate(inStartDate);
                    fin.setInEffectiveEndDate(inEndDate);
                }

                boolean isPrEdit = isNoBaseSourceIsEdit(fin, prsource, prthershold, prrightlow, prleftmod, prlefthigh, prrightmod,prStartDate,prEndDate);
                if(isPrEdit){
                    fin.setPrIsEdit(isPrEdit);
                    fin.setPrSource(prsource);
                    fin.setPrThershold(prthershold);
                    fin.setPrRightLowCaseCounth(prrightlow);
                    fin.setPrLeftModCaseCounth(prleftmod);
                    fin.setPrLeftHighCaseCount(prlefthigh);
                    fin.setPrRightModCaseCounth(prrightmod);
                    fin.setPrEffectiveStartDate(prStartDate);
                    fin.setPrEffectiveEndDate(prEndDate);
                }
        }

    }

    public boolean isNoBaseSourceIsEdit(HcsaRiskFinanceMatrixDto fin,String source,String thershold,String rightlow,String leftmod,String lefthigh,String rightmod,String StartDate,String endDate){
        if(StringUtil.isEmpty(thershold)&&StringUtil.isEmpty(rightlow)&&StringUtil.isEmpty(leftmod)&&StringUtil.isEmpty(lefthigh)&&StringUtil.isEmpty(rightmod)) {
            return false;
        }
        return true;
    }
    public String getRightLowCase(String lowCaseCount){
        Integer num = 0;
        try {
            num = Integer.parseInt(lowCaseCount);
            num = num-1;
        } catch (Exception e) {
            return "";
        }
        return num+"";
    }
    public String getLeftHighCase(String highCaseCount){
        Integer num = 0;
        try {
            num = Integer.parseInt(highCaseCount);
            num = num+1;
        } catch (Exception e) {
            return "";
        }
        return num+"";
    }


}
