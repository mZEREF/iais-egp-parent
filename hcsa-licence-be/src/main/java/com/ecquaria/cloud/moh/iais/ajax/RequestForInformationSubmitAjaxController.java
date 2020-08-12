package com.ecquaria.cloud.moh.iais.ajax;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.*;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFDtosDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.*;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.LicenceViewService;
import com.ecquaria.cloud.moh.iais.service.client.*;
import com.ecquaria.cloud.moh.iais.validation.HcsaApplicationUploadFileValidate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.*;

/**
 * @author zhilin
 * @date 2020/03/30
 */
@Controller
@Slf4j
public class RequestForInformationSubmitAjaxController {
    private static final String APPSUBMISSIONDTO = "appSubmissionDto";

    @Autowired
    private LicenceViewService licenceViewService;
    @Autowired
    private ApplicationClient applicationClient;
    @Autowired
    private ApplicationViewService applicationViewService;
    @Autowired
    private OrganizationClient organizationClient;
    @Autowired
    private HcsaConfigClient hcsaConfigClient;
    @Autowired
    private AppointmentClient appointmentClient;
    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;
    @Autowired
    private CessationClient cessationClient;

    @Autowired
    private ApplicationService applicationService;

    @RequestMapping(value = "/callRfiSubmit",method = RequestMethod.POST)
    public @ResponseBody String callRfiSubmit(HttpServletRequest request){
        String data = "";
        log.debug(StringUtil.changeForLog("the do LicenceViewServiceDelegator doSaveSelect start ..."));
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(request, "applicationViewDto");
        String isSuccess = "Y";
        String parentMsg = null;
        String successMsg = null;
        String errorMsg = null;
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(request, APPSUBMISSIONDTO);
        if (applicationViewDto != null) {
            AppPremisesCorrelationDto newAppPremisesCorrelationDto = applicationViewDto.getNewAppPremisesCorrelationDto();
            if (newAppPremisesCorrelationDto != null) {
                String[] selects = ParamUtil.getStrings(request, "editCheckbox");
                if (selects != null && selects.length > 0) {
                    List<String> selectsList = Arrays.asList(selects);
                    AppEditSelectDto appEditSelectDto = setAppEditSelectDto(newAppPremisesCorrelationDto, selectsList,appSubmissionDto);
                    //pre-inspection show, The following method(licenceViewService.saveAppEditSelect) will clear the field. Please do not move it
                    ParamUtil.setSessionAttr(request, "rfiUpWindowsCheck", (Serializable) appEditSelectDto.getRfiUpWindowsCheck());
                    parentMsg = "<ul>";
                    parentMsg = parentMsg + appEditSelectDto.getParentMsg();
                    parentMsg = parentMsg + "</ul>";
                    appEditSelectDto = licenceViewService.saveAppEditSelect(appEditSelectDto);
                    applicationViewDto.setAppEditSelectDto(appEditSelectDto);
                    licenceViewService.saveAppEditSelectToFe(appEditSelectDto);
                    successMsg = "save success";
                    ParamUtil.setSessionAttr(request, "isSaveRfiSelect", AppConsts.YES);
                } else {
                    errorMsg = "Please select at least a section";
                }
            } else {
                errorMsg = "Data Error!!!";
            }
        } else {
            errorMsg = "Session Time out !!!";
        }
        if (!StringUtil.isEmpty(errorMsg)) {
            isSuccess = "N";
            request.setAttribute("rfi","rfi");
        }
        ParamUtil.setRequestAttr(request, "successMsg", successMsg);
        ParamUtil.setRequestAttr(request, "isSuccess", isSuccess);
        ParamUtil.setRequestAttr(request, "errorMsg", errorMsg);
        ParamUtil.setSessionAttr(request, "parentMsg", parentMsg);
        data = parentMsg;
        ParamUtil.setSessionAttr(request, "applicationViewDto", applicationViewDto);
        log.debug(StringUtil.changeForLog("the do LicenceViewServiceDelegator doSaveSelect end ..."));
        return data;
    }

    private AppEditSelectDto setAppEditSelectDto(AppPremisesCorrelationDto newAppPremisesCorrelationDto, List<String> selectsList, AppSubmissionDto appSubmissionDto) {
        AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
        appEditSelectDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appEditSelectDto.setApplicationId(newAppPremisesCorrelationDto.getApplicationId());
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        String serviceName = "";
        if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)) {
            String serviceId = appSvcRelatedInfoDtos.get(0).getServiceId();
            HcsaServiceDto hcsaServiceDto = applicationViewService.getHcsaServiceDtoById(serviceId);
            serviceName = hcsaServiceDto.getSvcName();
        }
        String parentMsg = "";
        List<String> rfiUpWindowsCheck = IaisCommonUtils.genNewArrayList();
        if (selectsList.contains("premises")) {
            appEditSelectDto.setPremisesEdit(true);
            parentMsg = parentMsg + "<li style=\"padding-left: 0px;\">Premises</li>";
            rfiUpWindowsCheck.add("Premises");
        }
        if (selectsList.contains("primary")) {
            appEditSelectDto.setDocEdit(true);
            parentMsg = parentMsg + "<li style=\"padding-left: 0px;\">Primary Documents</li>";
            rfiUpWindowsCheck.add("Primary Documents");
        }
        if (selectsList.contains("service")) {
            appEditSelectDto.setServiceEdit(true);
            parentMsg = parentMsg + "<li style=\"padding-left: 0px;\">Service Related Information - " + serviceName+ "</li>";
            rfiUpWindowsCheck.add("Service Related Information - " + serviceName);
        }
        if (selectsList.contains("po")) {
            appEditSelectDto.setPoEdit(true);
            parentMsg = parentMsg + "<li style=\"padding-left: 0px;\">PO</li>";
            rfiUpWindowsCheck.add("PO");
        }
        if (selectsList.contains("dpo")) {
            appEditSelectDto.setDpoEdit(true);
            parentMsg = parentMsg + "<li style=\"padding-left: 0px;\">DPO</li>";
            rfiUpWindowsCheck.add("DPO");
        }
        if (selectsList.contains("medAlert")) {
            appEditSelectDto.setMedAlertEdit(true);
            parentMsg = parentMsg + "<li style=\"padding-left: 0px;\">medAlert</li>";
            rfiUpWindowsCheck.add("medAlert");
        }
        appEditSelectDto.setParentMsg(parentMsg);
        appEditSelectDto.setEditType(ApplicationConsts.APPLICATION_EDIT_TYPE_RFI);
        appEditSelectDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        if(IaisCommonUtils.isEmpty(rfiUpWindowsCheck)){
            appEditSelectDto.setRfiUpWindowsCheck(null);
        } else {
            appEditSelectDto.setRfiUpWindowsCheck(rfiUpWindowsCheck);
        }
        return appEditSelectDto;
    }
}
