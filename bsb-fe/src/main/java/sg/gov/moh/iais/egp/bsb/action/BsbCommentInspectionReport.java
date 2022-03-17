package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import sg.gov.moh.iais.egp.bsb.client.BsbFileClient;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.client.InspectionClient;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.file.FileRepoSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.CommentInsReportDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.CommentInsReportSaveDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsCommentReportDataDto;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_VALIDATION_ERRORS;


@Slf4j
@Delegator("bsbCommentInspectionReport")
public class BsbCommentInspectionReport {
    private final FileRepoClient fileRepoClient;
    private final BsbFileClient bsbFileClient;
    private final InspectionClient inspectionClient;

    @Autowired
    public BsbCommentInspectionReport(FileRepoClient fileRepoClient, BsbFileClient bsbFileClient,
                                      InspectionClient inspectionClient) {
        this.fileRepoClient = fileRepoClient;
        this.bsbFileClient = bsbFileClient;
        this.inspectionClient = inspectionClient;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        HttpSession session = request.getSession();
        session.removeAttribute(KEY_APP_ID);
        session.removeAttribute(KEY_COMMENT_REPORT_DATA);

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
        // retrieve inspection report file-repository ID
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        InsCommentReportDataDto dto = inspectionClient.retrieveInspectionReport(appId);
        ParamUtil.setSessionAttr(request, KEY_REPORT_REPO_ID, dto.getRepoId());
    }

    public void pre(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        CommentInsReportDto dto = (CommentInsReportDto) ParamUtil.getSessionAttr(request, KEY_COMMENT_REPORT_DATA);
        if (dto == null) {
            dto = new CommentInsReportDto();
            ParamUtil.setSessionAttr(request, KEY_COMMENT_REPORT_DATA, dto);
        }
    }

    public void validateSubmit(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        CommentInsReportDto dto = (CommentInsReportDto) ParamUtil.getSessionAttr(request, KEY_COMMENT_REPORT_DATA);
        dto.reqObjMapping(request);
        ParamUtil.setSessionAttr(request, KEY_COMMENT_REPORT_DATA, dto);

        CommentInsReportDto.CommentInsReportValidateDto validateDto = dto.toValidateDto();
        ValidationResultDto validationResultDto = inspectionClient.validateCommentReportForm(validateDto);
        if (validationResultDto.isPass()) {
            log.info("Validation pass for submission");
            ParamUtil.setRequestAttr(request, KEY_ROUTE, "save");
        } else {
            log.info("Validation fail for submission");
            ParamUtil.setRequestAttr(request, KEY_ROUTE, "back");
            String errorMsg = validationResultDto.toErrorMsg();
            log.info("Error msg is [{}]", errorMsg);
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, errorMsg);
        }
    }

    public void save(BaseProcessClass bpc) {
        log.info("Start to save submission of inspection report comment");
        HttpServletRequest request = bpc.request;
        CommentInsReportDto dto = (CommentInsReportDto) ParamUtil.getSessionAttr(request, KEY_COMMENT_REPORT_DATA);

        if (MasterCodeConstants.YES.equals(dto.getUpload()) && !dto.getNewDocMap().isEmpty()) {
            // save new uploaded files at local and sync
            // save at local
            log.info("Save attachment into file-repo");
            MultipartFile[] files = dto.getNewDocMap().values().stream().map(NewDocInfo::getMultipartFile).toArray(MultipartFile[]::new);
            List<String> repoIds = fileRepoClient.saveFiles(files).getEntity();
            List<NewFileSyncDto> newFilesToSync = dto.newFileSaved(repoIds);
            if (!newFilesToSync.isEmpty()) {
                // sync files to BE
                log.info("Sync attachment to BE");
                FileRepoSyncDto syncDto = new FileRepoSyncDto();
                syncDto.setNewFiles(newFilesToSync);
                bsbFileClient.saveFiles(syncDto);
            }

            // save app doc if any
            log.info("Save application, routing history and bsb_application_doc if any");
            CommentInsReportSaveDto saveDto = new CommentInsReportSaveDto();
            String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
            saveDto.setAppId(appId);
            saveDto.setUpload(dto.getUpload());
            saveDto.setAttachmentList(dto.getSavedDocList());
            inspectionClient.saveCommentReportForm(saveDto);
        }
    }
}
