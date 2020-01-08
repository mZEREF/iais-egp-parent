package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.risk.RiskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskFinanceMatrixDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskLicenceTenureDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.LicenceTenShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskFinancialShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.SubLicenceTenureDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.HcsaRiskLicTenValidateDto;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.HcsaRiskLicenceTenureSerice;
import com.ecquaria.cloud.moh.iais.validation.HcsaLicTenVadlidate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author: jiahao
 * @Date: 2020/1/3 16:50
 */
@Delegator(value = "hcsaRiskLicenceTenureConfigDelegator")
@Slf4j
public class HcsaRiskLicenceTenureConfigDelegator {
    private HcsaRiskLicenceTenureSerice hcsaRiskLicenceTenureSerice;

    @Autowired
    public HcsaRiskLicenceTenureConfigDelegator(HcsaRiskLicenceTenureSerice hcsaRiskLicenceTenureSerice) {
        this.hcsaRiskLicenceTenureSerice = hcsaRiskLicenceTenureSerice;
    }

    public void start(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;

    }

    public void init(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the init start ...."));
        HttpServletRequest request = bpc.request;
        LicenceTenShowDto showDto = hcsaRiskLicenceTenureSerice.getTenShowDto();
        List<SelectOption> ops = hcsaRiskLicenceTenureSerice.getDateTypeOps();
        ParamUtil.setSessionAttr(request,"timeType",(Serializable) ops);
        ParamUtil.setSessionAttr(request,"tenShowDto", showDto);
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
        RiskFinancialShowDto financialShowDto = (RiskFinancialShowDto) ParamUtil.getSessionAttr(request, RiskConsts.FINANCIALSHOWDTO);
        ParamUtil.setSessionAttr(request, RiskConsts.FINANCIALSHOWDTO, financialShowDto);
        ParamUtil.setSessionAttr(request, RiskConsts.FINANCIALSHOWDTO, financialShowDto);
    }

    public void doNext(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doNext start ...."));
        HttpServletRequest request = bpc.request;
        LicenceTenShowDto showDto = (LicenceTenShowDto)ParamUtil.getSessionAttr(request,"tenShowDto");
        String removeVal = ParamUtil.getString(request,"removeValue");
        String addValue = ParamUtil.getString(request,"addValue");
        if(!StringUtil.isEmpty(removeVal)){
            hcsaRiskLicenceTenureSerice.remove(removeVal,showDto);
            ParamUtil.setRequestAttr(request, "isValid", "Y");
            ParamUtil.setSessionAttr(request,"tenShowDto", showDto);
        }else if(!StringUtil.isEmpty(addValue)){
            hcsaRiskLicenceTenureSerice.add(addValue,showDto);
            showDto.setAddFlag(true);
            ParamUtil.setRequestAttr(request, "isValid", "Y");
            ParamUtil.setSessionAttr(request,"tenShowDto", showDto);
            HcsaLicTenVadlidate hcsaLicTenVadlidate = new HcsaLicTenVadlidate();
            Map<String, String> errMap = hcsaLicTenVadlidate.validate(request);
            if (!errMap.isEmpty()) {
                ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errMap));
            }
        }else {
            getDataFrompage(request,showDto);
            HcsaLicTenVadlidate hcsaLicTenVadlidate = new HcsaLicTenVadlidate();
            Map<String, String> errMap = hcsaLicTenVadlidate.validate(request);
            if (errMap.isEmpty()) {
                ParamUtil.setRequestAttr(request, "isValid", "N");
            } else {
                ParamUtil.setRequestAttr(request, "isValid", "Y");
                ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errMap));
            }
        }

    }

    public void submit(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doSubmit start ...."));
        HttpServletRequest request = bpc.request;
        LicenceTenShowDto showDto = (LicenceTenShowDto)ParamUtil.getSessionAttr(request,"tenShowDto");
        hcsaRiskLicenceTenureSerice.saveDto(showDto);

    }

    public void backToMenu(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the backToMenu start ...."));
        HttpServletRequest request = bpc.request;
    }

    public LicenceTenShowDto getDataFrompage(HttpServletRequest request, LicenceTenShowDto showDto) {
        LicenceTenShowDto ltShowDto = new LicenceTenShowDto();
        List<HcsaRiskLicenceTenureDto> ltDtoList = showDto.getLicenceTenureDtoList();
        if(ltDtoList!=null &&!ltDtoList.isEmpty()){
            for(HcsaRiskLicenceTenureDto temp:ltDtoList){
                String doeffDate = ParamUtil.getString(request,temp.getSvcCode()+"instartdate");
                String doEndDate = ParamUtil.getString(request,temp.getSvcCode()+"inenddate");
                temp.setDoEffectiveDate(doeffDate);
                temp.setDoEndDate(doEndDate);
                getSubListFrompage(request,temp);
                boolean editFlag  = hcsaRiskLicenceTenureSerice.doIsEditLogic(temp);
                temp.setEdit(!editFlag);
            }
        }
        ltShowDto.setLicenceTenureDtoList(ltDtoList);
        return ltShowDto;
    }





    private void getSubListFrompage(HttpServletRequest request, HcsaRiskLicenceTenureDto temp) {
        List<SubLicenceTenureDto> subDtoList = temp.getSubDtoList();
        if(subDtoList!=null && !subDtoList.isEmpty()){
            for(SubLicenceTenureDto sbDto:subDtoList){
                String max =  ParamUtil.getString(request,temp.getSvcCode()+sbDto.getOrderNum()+"right");
                String min = ParamUtil.getString(request,temp.getSvcCode()+sbDto.getOrderNum()+"left");
                String timetype = ParamUtil.getString(request,temp.getSvcCode()+sbDto.getOrderNum()+"type");
                String lt = ParamUtil.getString(request,temp.getSvcCode()+sbDto.getOrderNum()+"lt");
                sbDto.setColumRight(max);
                sbDto.setColumLeft(min);
                sbDto.setDateType(timetype);
                sbDto.setLicenceTenure(lt);
            }
        }
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
    public HcsaRiskLicTenValidateDto getValueFromPage(HttpServletRequest request) {
        HcsaRiskLicTenValidateDto dto = new HcsaRiskLicTenValidateDto();
        LicenceTenShowDto showDto = (LicenceTenShowDto)ParamUtil.getSessionAttr(request,"tenShowDto");
        getDataFrompage(request, showDto);
        return dto;
    }
}
