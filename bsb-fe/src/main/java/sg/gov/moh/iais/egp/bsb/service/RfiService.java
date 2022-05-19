package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import sg.gov.moh.iais.egp.bsb.client.RfiClient;
import sg.gov.moh.iais.egp.bsb.dto.entity.SelfAssessmtChklDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.ReportDto;
import sg.gov.moh.iais.egp.bsb.dto.rfi.ApplicationRfiIndicatorDto;
import sg.gov.moh.iais.egp.bsb.dto.rfi.RfiDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.rfi.save.SaveInspectionReportDto;
import sg.gov.moh.iais.egp.bsb.dto.rfi.save.SaveSelfAssessmentDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_RFI_DISPLAY_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.MODULE_NAME_INSPECTION_REPORT;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.MODULE_NAME_INSPECTION_SELF_ASSESSMENT;

@Service
@Slf4j
public class RfiService {
    private final RfiClient rfiClient;

    public RfiService(RfiClient rfiClient) {
        this.rfiClient = rfiClient;
    }

    public void saveInspectionReport(HttpServletRequest request, ReportDto reportDto, String appId) {
        RfiDisplayDto rfiDisplayDto = updateRfiDisplayDto(request, MODULE_NAME_INSPECTION_REPORT);
        SaveInspectionReportDto saveInspectionReportDto = new SaveInspectionReportDto();
        saveInspectionReportDto.setReportDto(reportDto);
        saveInspectionReportDto.setRfiDisplayDto(rfiDisplayDto);
        saveInspectionReportDto.setAppId(appId);
        rfiClient.saveInspectionReport(saveInspectionReportDto);
    }

    public void saveInspectionSelfAssessment(HttpServletRequest request, SelfAssessmtChklDto selfAssessmtChklDto) {
        RfiDisplayDto rfiDisplayDto = updateRfiDisplayDto(request, MODULE_NAME_INSPECTION_SELF_ASSESSMENT);
        SaveSelfAssessmentDto saveSelfAssessmentDto = new SaveSelfAssessmentDto();
        saveSelfAssessmentDto.setSelfAssessmtChklDto(selfAssessmtChklDto);
        saveSelfAssessmentDto.setRfiDisplayDto(rfiDisplayDto);
        rfiClient.saveInspectionSelfAssessment(saveSelfAssessmentDto);
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
