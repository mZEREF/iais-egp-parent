package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.InspectionClient;
import sg.gov.moh.iais.egp.bsb.client.RfiClient;
import sg.gov.moh.iais.egp.bsb.dto.inspection.ReportDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.service.RfiService;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_INSPECTION_REPORT;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_REQUEST_FOR_INFORMATION;
import static sg.gov.moh.iais.egp.bsb.constant.DocConstants.KEY_COMMON_DOC_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_INSPECTION_REPORT_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_ROUTE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_VALIDATION_ERRORS;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_RFT_DATA_ID;


@Slf4j
@RequiredArgsConstructor
@Delegator("bsbRfiCommentInspectionReport")
public class BsbRfiCommentInspectionReportDelegator {
    private final InspectionClient inspectionClient;
    private final RfiService rfiService;
    private final RfiClient rfiClient;

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        HttpSession session = request.getSession();
        session.removeAttribute(KEY_INSPECTION_REPORT_DTO);
        session.removeAttribute(KEY_COMMON_DOC_DTO);
        rfiService.clearAndSetAppIdInSession(request);
        AuditTrailHelper.auditFunction(MODULE_REQUEST_FOR_INFORMATION, FUNCTION_INSPECTION_REPORT);
    }

    public void init(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        ReportDto reportDto = (ReportDto) ParamUtil.getSessionAttr(request, KEY_INSPECTION_REPORT_DTO);
        if (reportDto == null) {
            reportDto = inspectionClient.getInsReportDto(appId);
        }
        ParamUtil.setSessionAttr(request, KEY_INSPECTION_REPORT_DTO, reportDto);
    }

    public void pre(BaseProcessClass bpc) {
        // do nothing now
    }

    public void validateSubmit(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ReportDto reportDto = (ReportDto) ParamUtil.getSessionAttr(request, KEY_INSPECTION_REPORT_DTO);
        reportDto.reqObjMapping(request);
        ParamUtil.setSessionAttr(request, KEY_INSPECTION_REPORT_DTO, reportDto);

        ValidationResultDto validationResultDto = inspectionClient.validateReportDto(reportDto);
        if (validationResultDto.isPass()){
            ParamUtil.setRequestAttr(request, KEY_ROUTE, "save");
        } else {
            ParamUtil.setRequestAttr(request, KEY_ROUTE, "back");
            String errorMsg = validationResultDto.toErrorMsg();
            log.info("Error msg is [{}]", errorMsg);
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, errorMsg);
        }
    }

    public void save(BaseProcessClass bpc) {
        // rfi sub-module only save data don't update application and create task (this is done in RFI)
        HttpServletRequest request = bpc.request;
        ReportDto reportDto = (ReportDto) ParamUtil.getSessionAttr(request, KEY_INSPECTION_REPORT_DTO);
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        reportDto.setAppId(appId);
        // save data
        String rfiDataId = (String) ParamUtil.getSessionAttr(request, KEY_RFT_DATA_ID);
        rfiClient.saveInspectionReport(reportDto, rfiDataId);
        // acknowledge page need appId
        ParamUtil.setRequestAttr(request, KEY_APP_ID, appId);
    }
}
