package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.GobalRiskTotalDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.GolbalRiskShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskWeightageShowDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.HcsaRiskFinianceVadlidateDto;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.HcsaRiskGolbalService;
import com.ecquaria.cloud.moh.iais.validation.HcsaWeightageRiskValidate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
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
        //HcsaRiskWeightageShowDto wightageDto = hcsaRiskWeightageService.getWeightage();
        GolbalRiskShowDto showDto = hcsaRiskGolbalService.getGolbalRiskShowDto();
        List<SelectOption> autoRenewOp = hcsaRiskGolbalService.getAutoOp();
        ParamUtil.setSessionAttr(request, "golbalShowDto", showDto);

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
        //golbalShowDto = getDataFrompage(request, golbalShowDto);
        HcsaWeightageRiskValidate weightageRiskValidate = new HcsaWeightageRiskValidate();
        Map<String, String> errMap = weightageRiskValidate.validate(request);
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


    }

    public void backToMenu(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the backToMenu start ...."));
        HttpServletRequest request = bpc.request;
    }

    public HcsaRiskWeightageShowDto getDataFrompage(HttpServletRequest request, GolbalRiskShowDto golbalRiskShowDto) {
        List<GobalRiskTotalDto> finList = golbalRiskShowDto.getGoalbalTotalList();
        for (GobalRiskTotalDto fin : finList) {
            String lastInp = ParamUtil.getString(request, fin.getServiceCode() + "last");
            String secLastInp = ParamUtil.getString(request, fin.getServiceCode() + "secLast");
            String finan = ParamUtil.getString(request, fin.getServiceCode() + "fin");
            String leadership = ParamUtil.getString(request, fin.getServiceCode() + "lea");
            String legislative = ParamUtil.getString(request, fin.getServiceCode() + "leg");
            String inStartDate = ParamUtil.getDate(request, fin.getServiceCode() + "instartdate");
            String inEndDate = ParamUtil.getDate(request, fin.getServiceCode() + "inenddate");
            //hcsaRiskWeightageService.getOneWdto(fin,lastInp,secLastInp,finan,leadership,legislative,inStartDate,inEndDate);
        }
        //weightageShowDto.setWeightageDtoList(finList);
        //ParamUtil.setSessionAttr(request, "wightageDto", weightageShowDto);
        return null;
    }
    public HcsaRiskFinianceVadlidateDto getValueFromPage(HttpServletRequest request) {
        HcsaRiskFinianceVadlidateDto dto = new HcsaRiskFinianceVadlidateDto();
        HcsaRiskWeightageShowDto wightageDto = (HcsaRiskWeightageShowDto)ParamUtil.getSessionAttr(request, "wightageDto");
        //getDataFrompage(request, wightageDto);
        return dto;
    }
}
