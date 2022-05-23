package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import sg.gov.moh.iais.egp.bsb.client.RfiClient;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.SelfAssessmtChklDto;
import sg.gov.moh.iais.egp.bsb.dto.info.common.AppMainInfo;
import sg.gov.moh.iais.egp.bsb.dto.inspection.ReportDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityRegisterDto;
import sg.gov.moh.iais.egp.bsb.dto.rfi.ApplicationRfiIndicatorDto;
import sg.gov.moh.iais.egp.bsb.dto.rfi.RfiDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.rfi.save.SaveFacilityRegistrationDto;
import sg.gov.moh.iais.egp.bsb.dto.rfi.save.SaveInspectionReportDto;
import sg.gov.moh.iais.egp.bsb.dto.rfi.save.SaveSelfAssessmentDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_CONFIRM_RFI;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_CONFIRM_RFI_Y;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_RFI_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_RFI_DISPLAY_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.MODULE_NAME_FACILITY_REGISTRATION;
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
        // clear app id
        request.getSession().removeAttribute(KEY_APP_ID);
        // rfi inspection self-assessment need
        request.getSession().removeAttribute(KEY_CONFIRM_RFI);
        // get app id
        String maskedRfiAppId = ParamUtil.getString(request, KEY_RFI_APP_ID);
        if (maskedRfiAppId != null) {
            String appId = MaskUtil.unMaskValue(KEY_RFI_APP_ID, maskedRfiAppId);
            if (appId == null || appId.equals(maskedRfiAppId)) {
                throw new IllegalArgumentException("Invalid masked rfi app ID:" + LogUtil.escapeCrlf(maskedRfiAppId));
            }
            ParamUtil.setSessionAttr(request, KEY_APP_ID, appId);
            // rfi inspection self-assessment need
            ParamUtil.setSessionAttr(request, KEY_CONFIRM_RFI, KEY_CONFIRM_RFI_Y);
        }
    }

    public ResponseDto<AppMainInfo> saveFacilityRegistration(HttpServletRequest request, FacilityRegisterDto facilityRegisterDto) {
        RfiDisplayDto rfiDisplayDto = updateRfiDisplayDto(request, MODULE_NAME_FACILITY_REGISTRATION);
        SaveFacilityRegistrationDto dto = new SaveFacilityRegistrationDto();
        dto.setRfiDisplayDto(rfiDisplayDto);
        dto.setFacilityRegisterDto(facilityRegisterDto);
        return rfiClient.saveFacilityRegistration(dto);
    }

    public void saveInspectionReport(HttpServletRequest request, ReportDto reportDto, String appId) {
        RfiDisplayDto rfiDisplayDto = updateRfiDisplayDto(request, MODULE_NAME_INSPECTION_REPORT);
        SaveInspectionReportDto dto = new SaveInspectionReportDto();
        dto.setReportDto(reportDto);
        dto.setRfiDisplayDto(rfiDisplayDto);
        dto.setAppId(appId);
        rfiClient.saveInspectionReport(dto);
    }

    public void saveInspectionSelfAssessment(HttpServletRequest request, SelfAssessmtChklDto selfAssessmtChklDto) {
        RfiDisplayDto rfiDisplayDto = updateRfiDisplayDto(request, MODULE_NAME_INSPECTION_SELF_ASSESSMENT);
        SaveSelfAssessmentDto dto = new SaveSelfAssessmentDto();
        dto.setSelfAssessmtChklDto(selfAssessmtChklDto);
        dto.setRfiDisplayDto(rfiDisplayDto);
        rfiClient.saveInspectionSelfAssessment(dto);
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
