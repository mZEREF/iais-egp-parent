package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppCessDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.service.CessationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author weilu
 * @date 2020/2/7 13:12
 */
@Delegator("CessationApplication")
@Slf4j
public class CessationApplicationDelegator {

    @Autowired
    private CessationService cessationService;

    public void start(BaseProcessClass bpc){
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>CessationApplicationDelegator");
    }

    public void init(BaseProcessClass bpc){
        List<String> licIds = new ArrayList<>();
        licIds.add("7ECAE165-534A-EA11-BE7F-000C29F371DC");
        List<AppCessDto> appCessDtosByLicIds = cessationService.getAppCessDtosByLicIds(licIds);
        List<SelectOption> reasonOption = getReasonOption();
        List<SelectOption> patientsOption = getPatientsOption();
        String text1 = "(1). The licensee must notify the Director of Medical Services in writing at least 30 days before the cessation of operation, letting, sale or disposal of his private hospital, medical clinic or clinical laboratory.";
        String text2 = "(2). Any licensee of a licensed healthcare institution (For e.g a medical clinic) who intends to cease operating the medical clinic shall take all measures as are reasonable and necessary to ensure that the medical records of every patient are properly transferred to the medical clinic or other healthcare institution to which such patient is to be transferred.";

        ParamUtil.setSessionAttr(bpc.request, "appCessDtosByLicIds", (Serializable)appCessDtosByLicIds);
        ParamUtil.setSessionAttr(bpc.request, "reasonOption", (Serializable) reasonOption);
        ParamUtil.setSessionAttr(bpc.request, "patientsOption", (Serializable) patientsOption);
        ParamUtil.setSessionAttr(bpc.request, "text1", text1);
        ParamUtil.setSessionAttr(bpc.request, "text2", text2);

    }

    public void prepareData(BaseProcessClass bpc){

    }

    public void valiant(BaseProcessClass bpc){

    }

    public void action(BaseProcessClass bpc){

    }

    private List<SelectOption> getReasonOption() {
        List<SelectOption> riskLevelResult = new ArrayList<>();
        SelectOption so1 = new SelectOption("Low", "Not Profitable");
        SelectOption so2 = new SelectOption("Moderate", "Reduce Workloa");
        SelectOption so3 = new SelectOption("Others", "Others");
        riskLevelResult.add(so1);
        riskLevelResult.add(so2);
        riskLevelResult.add(so3);
        return riskLevelResult;
    }

    private List<SelectOption> getPatientsOption() {
        List<SelectOption> riskLevelResult = new ArrayList<>();
        SelectOption so1 = new SelectOption("hciName", "HCI Name");
        SelectOption so2 = new SelectOption("regNo", "Professional Regn No.");
        SelectOption so3 = new SelectOption("Others", "Others");
        riskLevelResult.add(so1);
        riskLevelResult.add(so2);
        riskLevelResult.add(so3);
        return riskLevelResult;
    }

}
