package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.client.InspectionAFCClient;
import sg.gov.moh.iais.egp.bsb.client.InspectionClient;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.ReportDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.afc.ReviewAFCReportDto;
import sg.gov.moh.iais.egp.bsb.service.ViewAppService;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.Map;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_VIEW_APPLICATION;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_INSPECTION_REPORT_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_REVIEW_AFC_REPORT_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ViewApplicationConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.ViewApplicationConstants.KEY_INS_CER_FAC_REL_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.ViewApplicationConstants.KEY_TYPE;

@Delegator(value = "bsbViewInspectionCertification")
@Slf4j
@RequiredArgsConstructor
public class ViewInspectionCertificationDelegator {
    private static final String RETRIEVE_DATA_FAILED = "Fail to retrieve app data";

    private final InspectionClient inspectionClient;
    private final InspectionAFCClient inspectionAFCClient;

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        HttpSession session = request.getSession();
        session.removeAttribute(KEY_APP_ID);
        AuditTrailHelper.auditFunction(MODULE_VIEW_APPLICATION, "Inspection/Certification");
        ViewAppService.init(bpc);
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String type = request.getParameter(KEY_TYPE);
        if (MasterCodeConstants.DATA_TYPE_INS.equals(type)) {
            getInspectionReportDataByInsAppId(request,appId);
        } else if (MasterCodeConstants.DATA_TYPE_CER.equals(type)) {
            getCertificationReportDataByCerAppId(request,appId);
        } else if (MasterCodeConstants.DATA_TYPE_ALL.equals(type)) {
            getInspectionAndCertificationDataByMainAppId(request,appId);
        } else {
            throw new IaisRuntimeException("No Such Type");
        }
    }

    public void getInspectionReportDataByInsAppId(HttpServletRequest request, String insAppId) {
        ResponseDto<ReportDto> responseDto = inspectionClient.getInspectionReportFinalDataByInspectionAppId(insAppId);
        if (!responseDto.ok()) {
            throw new IaisRuntimeException(RETRIEVE_DATA_FAILED);
        }
        ParamUtil.setRequestAttr(request,KEY_INSPECTION_REPORT_DTO, responseDto.getEntity());
    }

    public void getCertificationReportDataByCerAppId(HttpServletRequest request, String cerAppId) {
        ResponseDto<ReviewAFCReportDto> responseDto = inspectionAFCClient.getCertificationReportByCertificationAppId(cerAppId);
        if (!responseDto.ok()) {
            throw new IaisRuntimeException(RETRIEVE_DATA_FAILED);
        }
        ParamUtil.setRequestAttr(request,KEY_REVIEW_AFC_REPORT_DTO, responseDto.getEntity());
    }

    public void getInspectionAndCertificationDataByMainAppId(HttpServletRequest request, String mainAppId) {
        String maskedInsCerFacRelId = request.getParameter(KEY_INS_CER_FAC_REL_ID);
        String insCerFacRelId = MaskHelper.unmask(KEY_INS_CER_FAC_REL_ID, maskedInsCerFacRelId);
        Map<String, Boolean> stringBooleanMap = inspectionAFCClient.judgeWhetherHaveInspectionAndCertificationByInsCerFacRelId(insCerFacRelId);
        if (stringBooleanMap.get(MasterCodeConstants.DATA_TYPE_INS) == Boolean.TRUE) {
            ResponseDto<ReportDto> responseDto = inspectionClient.getCertificationReportByMainAppId(mainAppId);
            if (!responseDto.ok()) {
                throw new IaisRuntimeException(RETRIEVE_DATA_FAILED);
            }
            ParamUtil.setRequestAttr(request,KEY_INSPECTION_REPORT_DTO, responseDto.getEntity());
        }
        if (stringBooleanMap.get(MasterCodeConstants.DATA_TYPE_CER) == Boolean.TRUE) {
            ResponseDto<ReviewAFCReportDto> responseDto = inspectionAFCClient.getCertificationReportByMainAppId(mainAppId);
            if (!responseDto.ok()) {
                throw new IaisRuntimeException(RETRIEVE_DATA_FAILED);
            }
            ParamUtil.setRequestAttr(request,KEY_REVIEW_AFC_REPORT_DTO, responseDto.getEntity());
        }
    }
}
