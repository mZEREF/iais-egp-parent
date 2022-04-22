package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.InspectionClient;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.dto.inspection.ReportDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_VALIDATION_ERRORS;


@Slf4j
@Delegator("bsbCommentInspectionReport")
public class BsbCommentInspectionReport {
    private final InspectionClient inspectionClient;

    @Autowired
    public BsbCommentInspectionReport(InspectionClient inspectionClient) {
        this.inspectionClient = inspectionClient;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        HttpSession session = request.getSession();
        session.removeAttribute(KEY_APP_ID);
        session.removeAttribute(KEY_INSPECTION_REPORT_DTO);
        session.removeAttribute(DocConstants.KEY_COMMON_DOC_DTO);

        // get app ID from request parameter
        String maskedAppId = ParamUtil.getString(request, KEY_APP_ID);
        String appId = MaskUtil.unMaskValue(MASK_PARAM_COMMENT_REPORT, maskedAppId);
        if (appId == null || appId.equals(maskedAppId)) {
            throw new IllegalArgumentException("Invalid masked app ID:" + LogUtil.escapeCrlf(maskedAppId));
        }
        ParamUtil.setSessionAttr(request, KEY_APP_ID, appId);
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_INSPECTION, AuditTrailConsts.FUNCTION_INSPECTION_REPORT);
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
        // do nothinf now
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
        HttpServletRequest request = bpc.request;
        ReportDto reportDto = (ReportDto) ParamUtil.getSessionAttr(request, KEY_INSPECTION_REPORT_DTO);
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        //save data
        inspectionClient.saveInspectionReport(reportDto, appId);
    }
}
