package com.ecquaria.cloud.moh.iais.action;


import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LaboratoryDevelopTestDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.client.EicGatewayFeMainClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceInboxClient;
import com.netflix.discovery.converters.Auto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Delegator("submissionDataDelegator")
@Slf4j
public class IaisSubmissionDataDelegator {

    @Autowired
    private LicenceInboxClient inboxClient;

    @Autowired
    private EicGatewayFeMainClient eicGatewayFeMainClient;

    public void startLDT(BaseProcessClass bpc){
        LoginContext loginContext= (LoginContext)ParamUtil.getSessionAttr(bpc.request,AppConsts.SESSION_ATTR_LOGIN_USER);
        if (loginContext != null){
            String licenseeId = loginContext.getLicenseeId();
            List<AppGrpPremisesDto> entity = inboxClient.getDistinctPremisesByLicenseeId(licenseeId).getEntity();
            List<SelectOption> selectOptions = IaisCommonUtils.genNewArrayList();
            if (entity != null){
                ArrayList<AppGrpPremisesDto> collect = entity.stream().collect(
                        Collectors.collectingAndThen(
                                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(AppGrpPremisesDto::getPremisesSelect))), ArrayList::new));
                for (AppGrpPremisesDto appGrpPremisesDto:collect
                     ) {
                    String hciName = appGrpPremisesDto.getAddress();
                    String hciCode = appGrpPremisesDto.getHciCode();
                    if (!StringUtil.isEmpty(hciName)){
                        SelectOption selectOption = new SelectOption(hciCode,hciName);
                        selectOptions.add(selectOption);
                    }
                }
            }
            ParamUtil.setSessionAttr(bpc.request, "personnelOptions", (Serializable) selectOptions);
        }
        log.info(StringUtil.changeForLog("Step ---> startLDT"));
    }

    public void prepareData(BaseProcessClass bpc){

    }

    public void saveDataLDT(BaseProcessClass bpc) throws ParseException {
        LaboratoryDevelopTestDto laboratoryDevelopTestDto = transformPageData(bpc.request);
        ValidationResult validationResult = WebValidationHelper.validateProperty(laboratoryDevelopTestDto,"save");
        if(validationResult != null && validationResult.isHasErrors()){
            Map<String, String> err = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(bpc.request, SystemAdminBaseConstants.ERROR_MSG, WebValidationHelper.generateJsonStr(err));
            ParamUtil.setRequestAttr(bpc.request, SystemAdminBaseConstants.ISVALID, AppConsts.FALSE);
            return;
        }
        LaboratoryDevelopTestDto entity = inboxClient.saveLaboratoryDevelopTest(laboratoryDevelopTestDto).getEntity();
        try{
            eicGatewayFeMainClient.syncLaboratoryDevelopTestFormFe(entity);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        ParamUtil.setRequestAttr(bpc.request, SystemAdminBaseConstants.ISVALID, SystemAdminBaseConstants.YES);
    }

    private LaboratoryDevelopTestDto transformPageData(HttpServletRequest request) throws ParseException {
        LaboratoryDevelopTestDto laboratoryDevelopTestDto = new LaboratoryDevelopTestDto();
        String hciCode = ParamUtil.getString(request,"hciCode");
        String ldtTestName = ParamUtil.getString(request,"ldtTestName");
        String intendedPurpose = ParamUtil.getString(request,"intendedPurpose");
        Date ldtDate = Formatter.parseDate(ParamUtil.getString(request,"ldtDate"));
        String responsePerson = ParamUtil.getString(request,"responsePerson");
        String testStatus = ParamUtil.getString(request,"testStatus");
        String designation = ParamUtil.getString(request,"designation");
        String remarks = ParamUtil.getString(request,"remarks");
        laboratoryDevelopTestDto.setDesignation(designation);
        laboratoryDevelopTestDto.setHciCode(hciCode);
        laboratoryDevelopTestDto.setRemarks(remarks);
        laboratoryDevelopTestDto.setIntendedPurpose(intendedPurpose);
        laboratoryDevelopTestDto.setLdtTestName(ldtTestName);
        laboratoryDevelopTestDto.setResponsePerson(responsePerson);
        laboratoryDevelopTestDto.setTestStatus(testStatus);
        laboratoryDevelopTestDto.setLdtDate(ldtDate);
        return laboratoryDevelopTestDto;
    }

}
