package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import sg.gov.moh.iais.egp.bsb.client.RfiClient;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.SelfAssessmtChklDto;
import sg.gov.moh.iais.egp.bsb.dto.info.common.AppMainInfo;
import sg.gov.moh.iais.egp.bsb.dto.inspection.RectifyInsReportSaveDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.ReportDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.insfollowup.FollowUpSaveDto;
import sg.gov.moh.iais.egp.bsb.dto.register.approval.ApprovalBatAndActivityDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityRegisterDto;
import sg.gov.moh.iais.egp.bsb.dto.rfi.ApplicationRfiIndicatorDto;
import sg.gov.moh.iais.egp.bsb.dto.rfi.RfiDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.rfi.SaveRfiDto;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_CONFIRM_RFI;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_CONFIRM_RFI_Y;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_RFI_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_RFI_DISPLAY_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.MODULE_NAME_APPROVAL_APPLICATION;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.MODULE_NAME_FACILITY_REGISTRATION;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.MODULE_NAME_INSPECTION_FOLLOW_UP;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.MODULE_NAME_INSPECTION_NC;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.MODULE_NAME_INSPECTION_REPORT;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.MODULE_NAME_INSPECTION_SELF_ASSESSMENT;

@Service
@Slf4j
public class RfiService {
    private final RfiClient rfiClient;

    public RfiService(RfiClient rfiClient) {
        this.rfiClient = rfiClient;
    }

    /**
     * rfi start method clear app id, and un mask app id
     */
    public void clearAndSetAppIdInSession(HttpServletRequest request) {
        // rfi inspection self-assessment, nc, follow-up need
        request.getSession().removeAttribute(KEY_CONFIRM_RFI);
        // get app id
        String maskedRfiAppId = ParamUtil.getString(request, KEY_RFI_APP_ID);
        if (maskedRfiAppId != null) {
            String appId = MaskHelper.unmask(KEY_RFI_APP_ID, maskedRfiAppId);
            ParamUtil.setSessionAttr(request, KEY_APP_ID, appId);
            // rfi inspection self-assessment, nc, follow-up need
            ParamUtil.setSessionAttr(request, KEY_CONFIRM_RFI, KEY_CONFIRM_RFI_Y);
        }
    }

    public ResponseDto<AppMainInfo> saveFacilityRegistration(HttpServletRequest request, FacilityRegisterDto facilityRegisterDto) {
        RfiDisplayDto rfiDisplayDto = updateRfiDisplayDto(request, MODULE_NAME_FACILITY_REGISTRATION);
        return rfiClient.saveFacilityRegistration(new SaveRfiDto<>(rfiDisplayDto, facilityRegisterDto));
    }

    public void saveInspectionSelfAssessment(HttpServletRequest request, SelfAssessmtChklDto selfAssessmtChklDto) {
        RfiDisplayDto rfiDisplayDto = updateRfiDisplayDto(request, MODULE_NAME_INSPECTION_SELF_ASSESSMENT);
        rfiClient.saveInspectionSelfAssessment(new SaveRfiDto<>(rfiDisplayDto, selfAssessmtChklDto));
    }

    public void saveInspectionReport(HttpServletRequest request, ReportDto reportDto) {
        RfiDisplayDto rfiDisplayDto = updateRfiDisplayDto(request, MODULE_NAME_INSPECTION_REPORT);
        rfiClient.saveInspectionReport(new SaveRfiDto<>(rfiDisplayDto, reportDto));
    }

    public void saveInspectionNC(HttpServletRequest request, RectifyInsReportSaveDto rectifyInsReportSaveDto) {
        RfiDisplayDto rfiDisplayDto = updateRfiDisplayDto(request, MODULE_NAME_INSPECTION_NC);
        rfiClient.saveInspectionNC(new SaveRfiDto<>(rfiDisplayDto, rectifyInsReportSaveDto));
    }

    public ResponseDto<String> saveInspectionFollowUp(HttpServletRequest request, FollowUpSaveDto followUpSaveDto) {
        RfiDisplayDto rfiDisplayDto = updateRfiDisplayDto(request, MODULE_NAME_INSPECTION_FOLLOW_UP);
        return rfiClient.saveInspectionFollowUp(new SaveRfiDto<>(rfiDisplayDto, followUpSaveDto));
    }

    public ResponseDto<AppMainInfo> saveApprovalBatAndActivity(HttpServletRequest request, ApprovalBatAndActivityDto approvalBatAndActivityDto) {
        RfiDisplayDto rfiDisplayDto = updateRfiDisplayDto(request, MODULE_NAME_APPROVAL_APPLICATION);
        return rfiClient.saveNewApplicationToApproval(new SaveRfiDto<>(rfiDisplayDto, approvalBatAndActivityDto));
    }

    /**
     * modify the applicationRfiIndicator status of RFI to true by Module name
     */
    public RfiDisplayDto updateRfiDisplayDto(HttpServletRequest request, String moduleName) {
        RfiDisplayDto rfiDisplayDto = (RfiDisplayDto) ParamUtil.getSessionAttr(request, KEY_RFI_DISPLAY_DTO);
        List<ApplicationRfiIndicatorDto> applicationRfiIndicatorDtoList = rfiDisplayDto.getApplicationRfiIndicatorDtoList();
        for (ApplicationRfiIndicatorDto applicationRfiIndicatorDto : applicationRfiIndicatorDtoList) {
            if (applicationRfiIndicatorDto.getModuleName().equals(moduleName)) {
                applicationRfiIndicatorDto.setStatus(true);
                return rfiDisplayDto;
            }
        }
        log.info("no module name matches, module name is {}", StringUtils.normalizeSpace(moduleName));
        return null;
    }
}
