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
import sg.gov.moh.iais.egp.bsb.client.RfiClient;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants;
import sg.gov.moh.iais.egp.bsb.dto.inspection.ReportDto;
import sg.gov.moh.iais.egp.bsb.dto.rfi.ApplicationRfiIndicatorDto;
import sg.gov.moh.iais.egp.bsb.dto.rfi.RfiDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.rfi.save.SaveInspectionReportDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_VALIDATION_ERRORS;


@Slf4j
@Delegator("bsbCommentInspectionReport")
public class BsbCommentInspectionReport {
    private final InspectionClient inspectionClient;
    private final RfiClient rfiClient;

    @Autowired
    public BsbCommentInspectionReport(InspectionClient inspectionClient, RfiClient rfiClient) {
        this.inspectionClient = inspectionClient;
        this.rfiClient = rfiClient;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        HttpSession session = request.getSession();
        session.removeAttribute(RfiConstants.KEY_APP_ID);
        session.removeAttribute(KEY_INSPECTION_REPORT_DTO);
        session.removeAttribute(DocConstants.KEY_COMMON_DOC_DTO);

        // get app ID from request parameter
        String maskedAppId = ParamUtil.getString(request, RfiConstants.KEY_APP_ID);
        String appId = MaskUtil.unMaskValue(RfiConstants.KEY_RFI_APP_ID, maskedAppId);
        if (appId == null || appId.equals(maskedAppId)) {
            throw new IllegalArgumentException("Invalid masked app ID:" + LogUtil.escapeCrlf(maskedAppId));
        }
        ParamUtil.setSessionAttr(request, RfiConstants.KEY_APP_ID, appId);
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_INSPECTION, AuditTrailConsts.FUNCTION_INSPECTION_REPORT);
    }

    public void init(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, RfiConstants.KEY_APP_ID);
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
        String appId = (String) ParamUtil.getSessionAttr(request, RfiConstants.KEY_APP_ID);
        // update this module rfi status
        RfiDisplayDto rfiDisplayDto = (RfiDisplayDto) ParamUtil.getSessionAttr(request, RfiConstants.KEY_RFI_DISPLAY_DTO);
        List<ApplicationRfiIndicatorDto> applicationRfiIndicatorDtoList = rfiDisplayDto.getApplicationRfiIndicatorDtoList();
        for (ApplicationRfiIndicatorDto applicationRfiIndicatorDto : applicationRfiIndicatorDtoList) {
            if (applicationRfiIndicatorDto.getModuleName().equals(RfiConstants.MODULE_NAME_INSPECTION_REPORT)) {
                applicationRfiIndicatorDto.setStatus(Boolean.TRUE);
            }
        }
        SaveInspectionReportDto saveInspectionReportDto = new SaveInspectionReportDto();
        saveInspectionReportDto.setReportDto(reportDto);
        saveInspectionReportDto.setRfiDisplayDto(rfiDisplayDto);
        saveInspectionReportDto.setAppId(appId);
        // save data
        rfiClient.saveInspectionReport(saveInspectionReportDto);
        // acknowledge page need appId
        ParamUtil.setRequestAttr(request, RfiConstants.KEY_APP_ID, appId);
    }
}
