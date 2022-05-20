package com.ecquaria.cloud.moh.iais.action;


import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LaboratoryDevelopTestDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterInboxUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.LaboratoryDevelopTestService;
import com.ecquaria.cloud.moh.iais.service.client.EicGatewayFeMainClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceInboxClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminMainFeClient;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

@Delegator("submissionDataDelegator")
@Slf4j
public class IaisSubmissionDataDelegator {

    @Autowired
    private LicenceInboxClient inboxClient;

    @Autowired
    private LaboratoryDevelopTestService laboratoryDevelopTestService;

    @Autowired
    private EicGatewayFeMainClient eicGatewayFeMainClient;
    @Autowired
    private SystemAdminMainFeClient systemAdminMainFeClient;


    private final String LABORATORY_DEVELOP_TEST_DTO = "laboratoryDevelopTestDto";

    public void startLDT(BaseProcessClass bpc){
        LoginContext loginContext= (LoginContext)ParamUtil.getSessionAttr(bpc.request,AppConsts.SESSION_ATTR_LOGIN_USER);
        ParamUtil.setSessionAttr(bpc.request, LABORATORY_DEVELOP_TEST_DTO, null);
        ParamUtil.setSessionAttr(bpc.request,"noContainCLB",null);
        if (loginContext != null){
            String licenseeId = loginContext.getLicenseeId();
            List<LicenceDto> licenceDtos = inboxClient.getLicenceDtosByLicenseeId(licenseeId).getEntity();
            boolean containCLB = containCLB(licenceDtos);
            if(containCLB) {
                List<AppGrpPremisesDto> entity = inboxClient.getDistinctPremisesByLicenseeId(licenseeId, AppServicesConsts.SERVICE_NAME_CLINICAL_LABORATORY).getEntity();
                List<SelectOption> selectOptions = IaisCommonUtils.genNewArrayList();
                if (entity != null) {
                    ArrayList<AppGrpPremisesDto> collect = entity.stream().collect(
                            Collectors.collectingAndThen(
                                    Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(AppGrpPremisesDto::getHciCode))), ArrayList::new));
                    for (AppGrpPremisesDto appGrpPremisesDto : collect
                            ) {
                        String hciName = appGrpPremisesDto.getAddress();
                        if (!StringUtil.isEmpty(appGrpPremisesDto.getHciName())) {
                            hciName = appGrpPremisesDto.getHciName() + "," + hciName;
                        }
                        String hciCode = appGrpPremisesDto.getHciCode();
                        if (!StringUtil.isEmpty(hciName)) {
                            SelectOption selectOption = new SelectOption(hciCode, hciName);
                            selectOptions.add(selectOption);
                        }
                    }
                }
                ParamUtil.setSessionAttr(bpc.request, "personnelOptions", (Serializable) selectOptions);
                ParamUtil.setRequestAttr(bpc.request,"canSubmit","Y");
            }else{
                ParamUtil.setRequestAttr(bpc.request,"canSubmit","N");
                ParamUtil.setRequestAttr(bpc.request,"noContainCLB",MessageUtil.getMessageDesc("CANNOT_SUBMIT"));
            }
        }

        String backUrl = "/main-web";
        String isSelf = ParamUtil.getString(bpc.request,"selfAssessmentGuide");
        if("true".equals(isSelf)){
            backUrl = "/main-web/eservice/INTERNET/MohAccessmentGuide";
        }
        ParamUtil.setSessionAttr(bpc.request, "backUrl", backUrl);
        log.info(StringUtil.changeForLog("Step ---> startLDT"));
    }

    public void prepareData(BaseProcessClass bpc){
        // nothing to do
    }

    public void confirmStep(BaseProcessClass bpc){
        String licenceId = "";
        InterInboxUserDto interInboxUserDto = (InterInboxUserDto) ParamUtil.getSessionAttr(bpc.request,InboxConst.INTER_INBOX_USER_INFO);
        String licenseeId = interInboxUserDto.getLicenseeId();
        String orgId = interInboxUserDto.getOrgId();
        LaboratoryDevelopTestDto laboratoryDevelopTestDto = (LaboratoryDevelopTestDto)ParamUtil.getSessionAttr(bpc.request, LABORATORY_DEVELOP_TEST_DTO);
        String hciCode = Optional.ofNullable(laboratoryDevelopTestDto).map(LaboratoryDevelopTestDto::getHciCode).orElseGet(() -> "");
        List<LicenceDto> licenceDtoList = inboxClient.getLicenceDtoByHciCode(hciCode, licenseeId).getEntity();

        if (!IaisCommonUtils.isEmpty(licenceDtoList)){
            LicenceDto licenceDto = licenceDtoList.get(0);
            licenceId = licenceDto.getId();
        }
        if (laboratoryDevelopTestDto != null) {
            String ldtNo = systemAdminMainFeClient.ldTNumber().getEntity();
            if (StringUtil.isEmpty(ldtNo)){
                ldtNo = "LDT0000000000001";
            }
            laboratoryDevelopTestDto.setLdtNo(ldtNo);
            LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
            if (loginContext != null) {
                laboratoryDevelopTestDto.setSubmitBy(loginContext.getUserId());
                laboratoryDevelopTestDto.setSubmitDt(new Date());
            }
            LaboratoryDevelopTestDto entity = inboxClient.saveLaboratoryDevelopTest(laboratoryDevelopTestDto).getEntity();
            try {
                eicGatewayFeMainClient.callEicWithTrackForApp(entity, eicGatewayFeMainClient::syncLaboratoryDevelopTestFormFe,
                        "syncLaboratoryDevelopTestFormFe");
                laboratoryDevelopTestService.sendLDTTestEmailAndSMS(entity,orgId,licenceId);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    public void PreparePrintSwitch(BaseProcessClass bpc){
        switchPage(bpc);
    }
    public void PreparePrintLdtData(BaseProcessClass bpc){
        switchPage(bpc);
    }
    public void PrepareMsg(BaseProcessClass bpc){
       // switchPage(bpc);
    }

    private void switchPage(BaseProcessClass bpc){
        String crud_action_type = bpc.request.getParameter("whichPage");
        bpc.request.setAttribute("crud_action_type",crud_action_type);
    }
    public void saveDataLDT(BaseProcessClass bpc) throws ParseException {
        LaboratoryDevelopTestDto laboratoryDevelopTestDto = transformPageData(bpc.request);
        ValidationResult validationResult = WebValidationHelper.validateProperty(laboratoryDevelopTestDto,"save");
        ParamUtil.setSessionAttr(bpc.request, LABORATORY_DEVELOP_TEST_DTO, laboratoryDevelopTestDto);
        if(validationResult != null && validationResult.isHasErrors()){
            Map<String, String> err = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(bpc.request, SystemAdminBaseConstants.ERROR_MSG, WebValidationHelper.generateJsonStr(err));
            ParamUtil.setRequestAttr(bpc.request, SystemAdminBaseConstants.ISVALID, AppConsts.FALSE);
            return;
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
    private boolean containCLB(List<LicenceDto> licenceDtos){
        log.info(StringUtil.changeForLog("The containCLB  start ..."));
        boolean result = false;
        if(!IaisCommonUtils.isEmpty(licenceDtos)){
            for(LicenceDto licenceDto : licenceDtos){
                if(AppServicesConsts.SERVICE_NAME_CLINICAL_LABORATORY.equals(licenceDto.getSvcName())
                        &&ApplicationConsts.LICENCE_STATUS_ACTIVE.equals(licenceDto.getStatus())){
                    result = true;
                    break;
                }
            }
        }else{
            log.info(StringUtil.changeForLog("The containCLB  licenceDtos is empty"));
        }
        log.info(StringUtil.changeForLog("The containCLB  result is -->:"+result));
        log.info(StringUtil.changeForLog("The containCLB  end ..."));
        return result;
    }
}
