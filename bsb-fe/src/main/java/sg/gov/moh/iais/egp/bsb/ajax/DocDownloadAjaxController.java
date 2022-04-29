package sg.gov.moh.iais.egp.bsb.ajax;

import com.ecquaria.cloud.moh.iais.common.mask.MaskAttackException;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.common.multipart.ByteArrayMultipartFile;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.common.node.simple.SimpleNode;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.constant.FacCertifierRegisterConstants;
import sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants;
import sg.gov.moh.iais.egp.bsb.dto.audit.FacilitySubmitSelfAuditDto;
import sg.gov.moh.iais.egp.bsb.dto.file.CommonDocDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo;
import sg.gov.moh.iais.egp.bsb.dto.inspection.CommentInsReportDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.RectifyInsReportDto;
import sg.gov.moh.iais.egp.bsb.dto.register.afc.CertTeamSavedDoc;
import sg.gov.moh.iais.egp.bsb.dto.register.afc.CertifyingTeamDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAuthoriserDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityCommitteeDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityProfileDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.PrimaryDocDto;
import sg.gov.moh.iais.egp.bsb.dto.submission.ConsumeNotificationDto;
import sg.gov.moh.iais.egp.bsb.dto.submission.DisposalNotificationDto;
import sg.gov.moh.iais.egp.bsb.dto.submission.ExportNotificationDto;
import sg.gov.moh.iais.egp.bsb.dto.submission.ReceiptNotificationDto;
import sg.gov.moh.iais.egp.bsb.dto.submission.TransferNotificationDto;
import sg.gov.moh.iais.egp.bsb.dto.withdrawn.AppSubmitWithdrawnDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

import static sg.gov.moh.iais.egp.bsb.action.WithdrawnAppDelegator.WITHDRAWN_APP_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.SELF_AUDIT_DATA;
import static sg.gov.moh.iais.egp.bsb.constant.DataSubmissionConstants.KEY_CONSUME_NOTIFICATION_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.DataSubmissionConstants.KEY_DISPOSAL_NOTIFICATION_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.DataSubmissionConstants.KEY_EXPORT_NOTIFICATION_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.DataSubmissionConstants.KEY_RECEIPT_NOTIFICATION_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.FacCertifierRegisterConstants.NODE_NAME_APPLICATION_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.FacCertifierRegisterConstants.NODE_NAME_CERTIFYING_TEAM_DETAIL;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.NODE_NAME_FAC_AUTH;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.NODE_NAME_FAC_COMMITTEE;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.NODE_NAME_FAC_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.NODE_NAME_FAC_PROFILE;
import static sg.gov.moh.iais.egp.bsb.constant.ReportableEventConstants.KEY_ROOT_NODE_GROUP_INCIDENT_NOT;
import static sg.gov.moh.iais.egp.bsb.constant.ReportableEventConstants.KEY_ROOT_NODE_GROUP_INVEST_REPORT;
import static sg.gov.moh.iais.egp.bsb.constant.ReportableEventConstants.NODE_NAME_DOCUMENTS;
import static sg.gov.moh.iais.egp.bsb.constant.ReportableEventConstants.PARAM_REPO_ID_FILE_MAP;


@RestController
@RequestMapping(path = "/ajax/doc/download")
@Slf4j
public class DocDownloadAjaxController {
    private static final String ERROR_MESSAGE_RECORD_INFO_NULL = "Can not get the record for the repo id";
    private static final String ERROR_MESSAGE_INVALID_ID = "ID is invalid";
    private final FileRepoClient fileRepoClient;

    @Autowired
    public DocDownloadAjaxController(FileRepoClient fileRepoClient) {
        this.fileRepoClient = fileRepoClient;
    }


    /**
     * Common method to download the file
     * @param maskedId masked id received from request
     * @param unmaskFunc used to decrypt the masked id, this function must throw a exception if the decryption fails
     * @param retrieveFileFunc used to retrieve the MultipartFile, this function can return null if not found
     */
    public void downloadFile(HttpServletRequest request, HttpServletResponse response, String maskedId, UnaryOperator<String> unmaskFunc, BiFunction<HttpServletRequest, String, MultipartFile> retrieveFileFunc) {
        String filename = "error";
        long length = 0;
        byte[] data = new byte[0];

        try {
            if (log.isInfoEnabled()) {
                log.info("Masked id is:{}", LogUtil.escapeCrlf(maskedId));
            }

            String id = unmaskFunc.apply(maskedId);
            log.info("Unmasked id is {}", id);

            MultipartFile file = retrieveFileFunc.apply(request, id);
            if (file == null) {
                throw new IllegalStateException("Can not find the MultipartFile for the id");
            }

            filename = file.getOriginalFilename();
            if (filename == null) {
                filename = "File";
            }
            length = file.getSize();
            data = file.getBytes();
        } catch (Exception e) {
            log.error("Fail to download file", e);
        } finally {
            OutputStream ops = null;
            try {
                response.addHeader("Content-Disposition", "attachment;filename=\"" + URLEncoder.encode(filename, StandardCharsets.UTF_8.name()) + "\"");
                response.addHeader("Content-Length", "" + length);
                response.setContentType("application/x-octet-stream");
                ops = response.getOutputStream();
                ops.write(data);
                ops.flush();
                ops.close();
            } catch (IOException e) {
                log.error("Fail to write file to response", e);
            } finally {
                if (ops != null) {
                    try {
                        ops.close();
                    } catch (IOException e) {
                        log.error("Fail to close response output stream", e);
                    }
                }
            }
        }
    }

    @GetMapping("/repo/{repoId}")
    public void downloadFileFromRepo(@PathVariable("repoId") String maskedRepoId, HttpServletRequest request, HttpServletResponse response) {
        downloadFile(request, response, maskedRepoId, this::unmaskFileId, this::getStandaloneSavedFile);
    }

    @GetMapping("/facReg/new/{id}")
    public void downloadNotSavedFile(@PathVariable("id") String maskedTmpId, HttpServletRequest request, HttpServletResponse response) {
        downloadFile(request, response, maskedTmpId, this::unmaskFileId, this::facRegGetNewFile);
    }

    @GetMapping("/facReg/repo/{id}")
    public void downloadSavedFile(@PathVariable("id") String maskedRepoId, HttpServletRequest request, HttpServletResponse response) {
        downloadFile(request, response, maskedRepoId, this::unmaskFileId, this::facRegGetSavedFile);
    }

    @GetMapping("/facReg/profile/new/{id}")
    public void downloadProfileNewFile(@PathVariable("id") String maskedTmpId, HttpServletRequest request, HttpServletResponse response) {
        downloadFile(request, response, maskedTmpId, this::unmaskFileId, this::facRegProfileGetNewFile);
    }

    @GetMapping("/facReg/profile/repo/{id}")
    public void downloadProfileSavedFile(@PathVariable("id") String maskedRepoId, HttpServletRequest request, HttpServletResponse response) {
        downloadFile(request, response, maskedRepoId, this::unmaskFileId, this::facRegProfileGetSavedFile);
    }

    @GetMapping("/facReg/committee/new/{id}")
    public void downloadFacCommitteeNewFile(@PathVariable("id") String maskedTmpId, HttpServletRequest request, HttpServletResponse response) {
        downloadFile(request, response, maskedTmpId, this::unmaskFileId, this::facRegCommitteeNewFile);
    }

    @GetMapping("/facReg/committee/repo/{id}")
    public void downloadFacCommitteeSavedFile(@PathVariable("id") String maskedRepoId, HttpServletRequest request, HttpServletResponse response) {
        downloadFile(request, response, maskedRepoId, this::unmaskFileId, this::facRegCommitteeSavedFile);
    }

    @GetMapping("/facCertReg/certTeamDto/new/{id}")
    public void downloadFacCertTeamDtoNewFile(@PathVariable("id") String maskedTmpId, HttpServletRequest request, HttpServletResponse response) {
        downloadFile(request, response, maskedTmpId, this::unmaskFileId, this::facCertRegCertTeamDtoNewFile);
    }

    @GetMapping("/facCertReg/certTeamDto/repo/{id}")
    public void downloadFacCertTeamDtoSavedFile(@PathVariable("id") String maskedRepoId, HttpServletRequest request, HttpServletResponse response) {
        downloadFile(request, response, maskedRepoId, this::unmaskFileId, this::facCertRegCertTeamDtoSavedFile);
    }

    @GetMapping("/facReg/authoriser/new/{id}")
    public void downloadFacAuthoriserNewFile(@PathVariable("id") String maskedTmpId, HttpServletRequest request, HttpServletResponse response) {
        downloadFile(request, response, maskedTmpId, this::unmaskFileId, this::facRegAuthoriserNewFile);
    }

    @GetMapping("/facReg/authoriser/repo/{id}")
    public void downloadFacAuthoriserSavedFile(@PathVariable("id") String maskedRepoId, HttpServletRequest request, HttpServletResponse response) {
        downloadFile(request, response, maskedRepoId, this::unmaskFileId, this::facRegAuthoriserSavedFile);
    }

    @GetMapping("/facCertifierReg/certTeam/new/{id}")
    public void downloadCertNotSavedTeamFile(@PathVariable("id") String maskedTmpId, HttpServletRequest request, HttpServletResponse response) {
        downloadFile(request, response, maskedTmpId, this::unmaskFileId, this::facRegCertTeamGetNewFile);
    }

    @GetMapping("/facCertifierReg/certTeam/repo/{id}")
    public void downloadCertSavedTeamFile(@PathVariable("id") String maskedRepoId, HttpServletRequest request, HttpServletResponse response) {
        downloadFile(request, response, maskedRepoId, this::unmaskFileId, this::facRegCertTeamGetSavedFile);
    }

    @GetMapping("/facCertifierReg/new/{id}")
    public void downloadCertNotSavedFile(@PathVariable("id") String maskedTmpId, HttpServletRequest request, HttpServletResponse response) {
        downloadFile(request, response, maskedTmpId, this::unmaskFileId, this::facRegCertGetNewFile);
    }

    @GetMapping("/facCertifierReg/repo/{id}")
    public void downloadCertSavedFile(@PathVariable("id") String maskedRepoId, HttpServletRequest request, HttpServletResponse response) {
        downloadFile(request, response, maskedRepoId, this::unmaskFileId, this::facRegCertGetSavedFile);
    }

    @GetMapping("/adhocRfi/repo/{id}/{docName}")
    public void downloadAdhocRfiFile(@PathVariable("id") String maskedRepoId,@PathVariable("docName") String fileRepoName, HttpServletRequest request, HttpServletResponse response) {
        log.debug(StringUtil.changeForLog("file-repo start ...."));

        String fileRepoId = MaskUtil.unMaskValue("file", maskedRepoId);

        if (StringUtils.isEmpty(fileRepoId)) {
            log.debug(StringUtil.changeForLog("file-repo id is empty"));
            return;
        }
        byte[] fileData = fileRepoClient.getFileFormDataBase(fileRepoId).getEntity();


        long length = 0;
        byte[] data = fileRepoClient.getFileFormDataBase(fileRepoId).getEntity();

        try {


            length = fileData.length;

        } catch (Exception e) {
            log.error("Fail to download file", e);
        } finally {
            OutputStream ops = null;
            try {
                response.addHeader("Content-Disposition", "attachment;filename=\"" + URLEncoder.encode(fileRepoName, StandardCharsets.UTF_8.name()) + "\"");
                response.addHeader("Content-Length", "" + length);
                response.setContentType("application/x-octet-stream");
                ops = response.getOutputStream();
                ops.write(data);
                ops.flush();
                ops.close();
            } catch (IOException e) {
                log.error("Fail to write file to response", e);
            } finally {
                if (ops != null) {
                    try {
                        ops.close();
                    } catch (IOException e) {
                        log.error("Fail to close response output stream", e);
                    }
                }
            }
        }
    }

    @GetMapping("/dataSub/new/{id}")
    public void downloadDataSubNotSavedFile(@PathVariable("id") String maskedTmpId, HttpServletRequest request, HttpServletResponse response) {
        downloadFile(request, response, maskedTmpId, this::unmaskFileId, this::dataSubGetNewFile);
    }

    @GetMapping("/dataSub/repo/{id}")
    public void downloadDataSubSavedFile(@PathVariable("id") String maskedTmpId, HttpServletRequest request, HttpServletResponse response) {
        downloadFile(request, response, maskedTmpId, this::unmaskFileId, this::dataSubGetSavedFile);
    }

    @GetMapping("/audit/repo/{id}")
    public void downloadAuditSavedFile(@PathVariable("id") String maskedRepoId, HttpServletRequest request, HttpServletResponse response) {
        downloadFile(request, response, maskedRepoId, this::unmaskFileId, this::auditGetSavedFile);
    }

    @GetMapping("/audit/new/{id}")
    public void downloadAuditNewFile(@PathVariable("id") String maskedRepoId, HttpServletRequest request, HttpServletResponse response) {
        downloadFile(request, response, maskedRepoId, this::unmaskFileId, this::auditGetNewFile);
    }

    @GetMapping("/withdrawn/repo/{id}")
    public void downloadWithdrawnFile(@PathVariable("id") String maskedRepoId, HttpServletRequest request, HttpServletResponse response) {
        downloadFile(request, response, maskedRepoId, this::unmaskFileId, this::withdrawnGetNewFile);
    }

    @GetMapping("/reportableEvent/followup/new/{id}")
    public void downloadNotSavedFollowupFile(@PathVariable("id") String maskedTmpId, HttpServletRequest request, HttpServletResponse response) {
        downloadFile(request, response, maskedTmpId, this::unmaskFileId, this::followupGetNewFile);
    }

    @GetMapping("/reportableEvent/followup/repo/{id}")
    public void downloadSavedFollowupFile(@PathVariable("id") String maskedRepoId, HttpServletRequest request, HttpServletResponse response) {
        downloadFile(request, response, maskedRepoId, this::unmaskFileId, this::followupGetSavedFile);
    }

    @GetMapping("/commonDoc/new/{id}")
    public void downloadNotSavedCommonDocFile(@PathVariable("id") String maskedTmpId, HttpServletRequest request, HttpServletResponse response) {
        downloadFile(request, response, maskedTmpId, this::unmaskFileId, this::commonDocGetNewFile);
    }

    @GetMapping("/commonDoc/repo/{id}")
    public void downloadSavedCommonDocFile(@PathVariable("id") String maskedRepoId, HttpServletRequest request, HttpServletResponse response) {
        downloadFile(request, response, maskedRepoId, this::unmaskFileId, this::commonDocGetSavedFile);
    }

    @GetMapping("/commentInsReport/report/{id}")
    public void downloadInspectionReport(@PathVariable("id") String maskedRepoId, HttpServletRequest request, HttpServletResponse response) {
        downloadFile(request, response, maskedRepoId, this::unmaskFileId, this::getInspectionReportPdf);
    }

    @GetMapping("/commentInsReport/comment/{id}")
    public void downloadNewCommentReport(@PathVariable("id") String maskedTmpId, HttpServletRequest request, HttpServletResponse response) {
        downloadFile(request, response, maskedTmpId, this::unmaskFileId, this::getCommentInspectionReport);
    }

    @GetMapping("/insNonCompliance/new/{id}")
    public void downloadNewNonComplianceReport(@PathVariable("id") String maskedTmpId, HttpServletRequest request, HttpServletResponse response) {
        downloadFile(request, response, maskedTmpId, this::unmaskFileId, this::getNonComplianceInspectionNewReport);
    }

    @GetMapping("/insNonCompliance/repo/{id}")
    public void downloadSavedNonComplianceReport(@PathVariable("id") String maskedTmpId, HttpServletRequest request, HttpServletResponse response) {
        downloadFile(request, response, maskedTmpId, this::unmaskFileId, this::getNonComplianceInspectionSavedReport);
    }

    @GetMapping("/incidentNotification/new/{id}")
    public void downloadNewIncidentNotificationReport(@PathVariable("id") String maskedTmpId, HttpServletRequest request, HttpServletResponse response) {
        downloadFile(request, response, maskedTmpId, this::unmaskFileId, this::getIncidentNotificationNewFile);
    }

    @GetMapping("/incidentNotification/repo/{id}")
    public void downloadSavedIncidentNotificationReport(@PathVariable("id") String maskedTmpId, HttpServletRequest request, HttpServletResponse response) {
        downloadFile(request, response, maskedTmpId, this::unmaskFileId, this::getIncidentNotificationSavedFile);
    }

    @GetMapping("/investigationReport/new/{id}")
    public void downloadNewInvestigationReport(@PathVariable("id") String maskedTmpId, HttpServletRequest request, HttpServletResponse response) {
        downloadFile(request, response, maskedTmpId, this::unmaskFileId, this::getInvestigationReportNewFile);
    }

    @GetMapping("/investigationReport/repo/{id}")
    public void downloadSavedInvestigationReport(@PathVariable("id") String maskedTmpId, HttpServletRequest request, HttpServletResponse response) {
        downloadFile(request, response, maskedTmpId, this::unmaskFileId, this::getInvestigationReportSavedFile);
    }

    @GetMapping("/reportableEvent/view/{id}")
    public void downloadViewReportableEventReport(@PathVariable("id") String maskedTmpId, HttpServletRequest request, HttpServletResponse response) {
        downloadFile(request, response, maskedTmpId, this::unmaskFileId, this::getViewReportableEventSavedFile);
    }


    /**
     * Use the param 'file' to unmask the id
     * @return unmasked id
     * @throws MaskAttackException if fail to unmask the parameter
     */
    private String unmaskFileId(String maskedId) {
        String tmpId = MaskUtil.unMaskValue("file", maskedId);
        if (tmpId == null || maskedId == null || tmpId.equals(maskedId)) {
            throw new MaskAttackException("Masked id is invalid");
        }
        return tmpId;
    }

    /** Download saved files directly from file-repo
     * @param repoId file repo ID
     */
    private MultipartFile getStandaloneSavedFile(HttpServletRequest request, String repoId) {
        byte[] content = fileRepoClient.getFileFormDataBase(repoId).getEntity();
        String filename = request.getParameter("filename");
        return new ByteArrayMultipartFile(null, filename, null, content);
    }

    /**
     * Facility registration get the new doc file object
     * @param id key of the newDocMap in the PrimaryDocDto
     */
    private MultipartFile facRegGetNewFile(HttpServletRequest request, String id) {
        NodeGroup facRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, FacRegisterConstants.KEY_ROOT_NODE_GROUP);
        SimpleNode primaryDocNode = (SimpleNode) facRegRoot.at(FacRegisterConstants.NODE_NAME_PRIMARY_DOC);
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) primaryDocNode.getValue();
        return primaryDocDto.getNewDocMap().get(id).getMultipartFile();
    }

    /**
     * Facility registration get the saved doc file data
     * @param id key of the savedDocMap in the PrimaryDocDto
     */
    private MultipartFile facRegGetSavedFile(HttpServletRequest request, String id) {
        NodeGroup facRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, FacRegisterConstants.KEY_ROOT_NODE_GROUP);
        SimpleNode primaryDocNode = (SimpleNode) facRegRoot.at(FacRegisterConstants.NODE_NAME_PRIMARY_DOC);
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) primaryDocNode.getValue();
        DocRecordInfo info = primaryDocDto.getSavedDocMap().get(id);
        if (info == null) {
            throw new IllegalStateException(ERROR_MESSAGE_RECORD_INFO_NULL);
        }
        byte[] content = fileRepoClient.getFileFormDataBase(id).getEntity();
        return new ByteArrayMultipartFile(null, info.getFilename(), null, content);
    }

    /** Facility registration get new uploaded file for profile (exactly the gazette order) */
    private MultipartFile facRegProfileGetNewFile(HttpServletRequest request, String id) {
        NodeGroup facRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, FacRegisterConstants.KEY_ROOT_NODE_GROUP);
        SimpleNode profileNode = (SimpleNode) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_PROFILE);
        FacilityProfileDto profileDto = (FacilityProfileDto) profileNode.getValue();
        return profileDto.getNewDocMap().get(id).getMultipartFile();
    }

    /** Facility registration get saved uploaded file for profile (exactly the gazette order) */
    private MultipartFile facRegProfileGetSavedFile(HttpServletRequest request, String id) {
        NodeGroup facRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, FacRegisterConstants.KEY_ROOT_NODE_GROUP);
        SimpleNode profileNode = (SimpleNode) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_PROFILE);
        FacilityProfileDto profileDto = (FacilityProfileDto) profileNode.getValue();
        DocRecordInfo info = profileDto.getSavedDocMap().get(id);
        if (info == null) {
            throw new IllegalStateException(ERROR_MESSAGE_RECORD_INFO_NULL);
        }
        byte[] content = fileRepoClient.getFileFormDataBase(id).getEntity();
        return new ByteArrayMultipartFile(null, info.getFilename(), null, content);
    }

    /** Facility registration get new uploaded data file for committee */
    private MultipartFile facRegCommitteeNewFile(HttpServletRequest request, String id) {
        NodeGroup facRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, FacRegisterConstants.KEY_ROOT_NODE_GROUP);
        FacilityCommitteeDto committeeDto = (FacilityCommitteeDto) ((SimpleNode) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_COMMITTEE)).getValue();
        NewDocInfo newDocInfo = committeeDto.getNewFile();
        if (!newDocInfo.getTmpId().equals(id)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_INVALID_ID);
        }
        return newDocInfo.getMultipartFile();
    }


    /** Facility registration get new uploaded data file for committee */
    private MultipartFile facCertRegCertTeamDtoNewFile(HttpServletRequest request, String id) {
        NodeGroup facRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, FacCertifierRegisterConstants.KEY_ROOT_NODE_GROUP);
        CertifyingTeamDto  certifyingTeamDto = (CertifyingTeamDto) ((SimpleNode) facRegRoot.at(NODE_NAME_APPLICATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_CERTIFYING_TEAM_DETAIL)).getValue();
        NewDocInfo newDocInfo = certifyingTeamDto.getNewFile();
        if (!newDocInfo.getTmpId().equals(id)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_INVALID_ID);
        }
        return newDocInfo.getMultipartFile();
    }

    /** Facility registration get saved data file for committee */
    private MultipartFile facCertRegCertTeamDtoSavedFile(HttpServletRequest request, String id) {
        NodeGroup facRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, FacRegisterConstants.KEY_ROOT_NODE_GROUP);
        CertifyingTeamDto  certifyingTeamDto = (CertifyingTeamDto) ((SimpleNode) facRegRoot.at( NODE_NAME_APPLICATION_INFO+ facRegRoot.getPathSeparator() + NODE_NAME_CERTIFYING_TEAM_DETAIL)).getValue();
        DocRecordInfo docRecordInfo = certifyingTeamDto.getSavedFile();
        if (!docRecordInfo.getRepoId().equals(id)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_INVALID_ID);
        }
        byte[] content = fileRepoClient.getFileFormDataBase(id).getEntity();
        return new ByteArrayMultipartFile(null, docRecordInfo.getFilename(), null, content);
    }

    /** Facility registration get saved data file for committee */
    private MultipartFile facRegCommitteeSavedFile(HttpServletRequest request, String id) {
        NodeGroup facRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, FacRegisterConstants.KEY_ROOT_NODE_GROUP);
        FacilityCommitteeDto committeeDto = (FacilityCommitteeDto) ((SimpleNode) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_COMMITTEE)).getValue();
        DocRecordInfo docRecordInfo = committeeDto.getSavedFile();
        if (!docRecordInfo.getRepoId().equals(id)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_INVALID_ID);
        }
        byte[] content = fileRepoClient.getFileFormDataBase(id).getEntity();
        return new ByteArrayMultipartFile(null, docRecordInfo.getFilename(), null, content);
    }

    /** Facility registration get new uploaded data file for authoriser */
    private MultipartFile facRegAuthoriserNewFile(HttpServletRequest request, String id) {
        NodeGroup facRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, FacRegisterConstants.KEY_ROOT_NODE_GROUP);
        FacilityAuthoriserDto authDto = (FacilityAuthoriserDto) ((SimpleNode) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_AUTH)).getValue();
        NewDocInfo newDocInfo = authDto.getNewFile();
        if (!newDocInfo.getTmpId().equals(id)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_INVALID_ID);
        }
        return newDocInfo.getMultipartFile();
    }

    /** Facility registration get saved data file for authoriser */
    private MultipartFile facRegAuthoriserSavedFile(HttpServletRequest request, String id) {
        NodeGroup facRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, FacRegisterConstants.KEY_ROOT_NODE_GROUP);
        FacilityAuthoriserDto authDto = (FacilityAuthoriserDto) ((SimpleNode) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_AUTH)).getValue();
        DocRecordInfo docRecordInfo = authDto.getSavedFile();
        if (!docRecordInfo.getRepoId().equals(id)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_INVALID_ID);
        }
        byte[] content = fileRepoClient.getFileFormDataBase(id).getEntity();
        return new ByteArrayMultipartFile(null, docRecordInfo.getFilename(), null, content);
    }

    /**
     * Facility Certifier registration get the new doc file object
     * @param id key of the newDocMap in the PrimaryDocDto
     */
    private MultipartFile facRegCertGetNewFile(HttpServletRequest request, String id) {
        SimpleNode primaryDocNode = getSimpleNode(request, FacCertifierRegisterConstants.NODE_NAME_SUPPORTING_DOCUMENT, FacCertifierRegisterConstants.KEY_ROOT_NODE_GROUP);
        sg.gov.moh.iais.egp.bsb.dto.register.afc.PrimaryDocDto primaryDocDto = (sg.gov.moh.iais.egp.bsb.dto.register.afc.PrimaryDocDto) primaryDocNode.getValue();
        return primaryDocDto.getNewDocMap().get(id).getMultipartFile();
    }
    private MultipartFile facRegCertTeamGetNewFile(HttpServletRequest request, String id) {
        SimpleNode primaryDocNode = getSimpleNode(request, FacCertifierRegisterConstants.NODE_NAME_SUPPORTING_DOCUMENT, FacCertifierRegisterConstants.KEY_ROOT_NODE_GROUP);
        sg.gov.moh.iais.egp.bsb.dto.register.afc.PrimaryDocDto primaryDocDto = (sg.gov.moh.iais.egp.bsb.dto.register.afc.PrimaryDocDto) primaryDocNode.getValue();
        return primaryDocDto.getCertTeamNewDocMap().get(id).getMultipartFile();
    }




    private MultipartFile dataSubGetNewFile(HttpServletRequest request, String id) {
        TransferNotificationDto transferNotificationDto = (TransferNotificationDto) ParamUtil.getSessionAttr(request,"transferNotDto");
        ConsumeNotificationDto consumeNotificationDto = (ConsumeNotificationDto) ParamUtil.getSessionAttr(request,KEY_CONSUME_NOTIFICATION_DTO);
        DisposalNotificationDto disposalNotificationDto = (DisposalNotificationDto) ParamUtil.getSessionAttr(request,KEY_DISPOSAL_NOTIFICATION_DTO);
        ExportNotificationDto exportNotificationDto = (ExportNotificationDto) ParamUtil.getSessionAttr(request,KEY_EXPORT_NOTIFICATION_DTO);
        ReceiptNotificationDto receiptNotificationDto = (ReceiptNotificationDto) ParamUtil.getSessionAttr(request,KEY_RECEIPT_NOTIFICATION_DTO);

        if(transferNotificationDto != null){
            return transferNotificationDto.getAllNewDocInfos().get(id).getMultipartFile();
        } else if(consumeNotificationDto != null){
            return consumeNotificationDto.getAllNewDocInfos().get(id).getMultipartFile();
        } else if(disposalNotificationDto != null){
            return disposalNotificationDto.getAllNewDocInfos().get(id).getMultipartFile();
        } else if(exportNotificationDto != null){
            return exportNotificationDto.getAllNewDocInfos().get(id).getMultipartFile();
        } else if(receiptNotificationDto != null){
            return receiptNotificationDto.getAllNewDocInfos().get(id).getMultipartFile();
        } else{
            return null;
        }
    }

    /**
     * Follow-up report get the new doc file object
     * @param id key of the newDocMap in the PrimaryDocDto
     */
    private MultipartFile dataSubGetSavedFile(HttpServletRequest request, String id) {
        TransferNotificationDto transferNotificationDto = (TransferNotificationDto) ParamUtil.getSessionAttr(request,"transferNotDto");
        ConsumeNotificationDto consumeNotificationDto = (ConsumeNotificationDto) ParamUtil.getSessionAttr(request,KEY_CONSUME_NOTIFICATION_DTO);
        DisposalNotificationDto disposalNotificationDto = (DisposalNotificationDto) ParamUtil.getSessionAttr(request,KEY_DISPOSAL_NOTIFICATION_DTO);
        ExportNotificationDto exportNotificationDto = (ExportNotificationDto) ParamUtil.getSessionAttr(request,KEY_EXPORT_NOTIFICATION_DTO);
        ReceiptNotificationDto receiptNotificationDto = (ReceiptNotificationDto) ParamUtil.getSessionAttr(request,KEY_RECEIPT_NOTIFICATION_DTO);

        sg.gov.moh.iais.egp.bsb.dto.submission.PrimaryDocDto.DocRecordInfo docRecordInfo;
        if(transferNotificationDto != null){
            docRecordInfo = transferNotificationDto.getSavedDocInfos().get(id);
        } else if(consumeNotificationDto != null){
            docRecordInfo = consumeNotificationDto.getSavedDocInfos().get(id);
        } else if(disposalNotificationDto != null){
            docRecordInfo = disposalNotificationDto.getSavedDocInfos().get(id);
        } else if(exportNotificationDto != null){
            docRecordInfo = exportNotificationDto.getSavedDocInfos().get(id);
        } else if(receiptNotificationDto != null){
            docRecordInfo = receiptNotificationDto.getSavedDocInfos().get(id);
        } else{
            return null;
        }
        if (docRecordInfo == null) {
            throw new IllegalStateException(ERROR_MESSAGE_RECORD_INFO_NULL);
        }
        byte[] content = fileRepoClient.getFileFormDataBase(id).getEntity();
        return new ByteArrayMultipartFile(null, docRecordInfo.getFilename(), null, content);
    }

    /**
     * Facility Certifier registration get the saved doc file data
     * @param id key of the savedDocMap in the PrimaryDocDto
     */
    private MultipartFile facRegCertGetSavedFile(HttpServletRequest request, String id) {
        SimpleNode primaryDocNode = getSimpleNode(request, FacCertifierRegisterConstants.NODE_NAME_SUPPORTING_DOCUMENT, FacCertifierRegisterConstants.KEY_ROOT_NODE_GROUP);
        sg.gov.moh.iais.egp.bsb.dto.register.afc.PrimaryDocDto primaryDocDto = (sg.gov.moh.iais.egp.bsb.dto.register.afc.PrimaryDocDto) primaryDocNode.getValue();
        DocRecordInfo info = primaryDocDto.getSavedDocMap().get(id);
        if (info == null) {
            throw new IllegalStateException(ERROR_MESSAGE_RECORD_INFO_NULL);
        }
        byte[] content = fileRepoClient.getFileFormDataBase(id).getEntity();
        return new ByteArrayMultipartFile(null, info.getFilename(), null, content);
    }

    private MultipartFile facRegCertTeamGetSavedFile(HttpServletRequest request, String id) {
        SimpleNode primaryDocNode = getSimpleNode(request, FacCertifierRegisterConstants.NODE_NAME_SUPPORTING_DOCUMENT, FacCertifierRegisterConstants.KEY_ROOT_NODE_GROUP);
        sg.gov.moh.iais.egp.bsb.dto.register.afc.PrimaryDocDto primaryDocDto = (sg.gov.moh.iais.egp.bsb.dto.register.afc.PrimaryDocDto) primaryDocNode.getValue();
        CertTeamSavedDoc info = primaryDocDto.getCertTeamSavedDocMap().get(id);
        if (info == null) {
            throw new IllegalStateException(ERROR_MESSAGE_RECORD_INFO_NULL);
        }
        byte[] content = fileRepoClient.getFileFormDataBase(id).getEntity();
        return new ByteArrayMultipartFile(null, info.getFilename(), null, content);
    }

    public SimpleNode getSimpleNode(HttpServletRequest request,String nodeName,String rootName){
        NodeGroup facCertRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, rootName);
        return  (SimpleNode) facCertRegRoot.at(nodeName);
    }

    private MultipartFile withdrawnGetNewFile(HttpServletRequest request, String id) {
        AppSubmitWithdrawnDto dto = (AppSubmitWithdrawnDto)ParamUtil.getSessionAttr(request, WITHDRAWN_APP_DTO);
        MultipartFile multipartFile = null;
        for (sg.gov.moh.iais.egp.bsb.dto.withdrawn.PrimaryDocDto.NewDocInfo newDocInfo : dto.getNewDocInfos()) {
            if (newDocInfo.getTmpId().equals(id)) {
                multipartFile = newDocInfo.getMultipartFile();
                break;
            }
        }
        return multipartFile;
    }

    private MultipartFile auditGetNewFile(HttpServletRequest request, String id) {
        FacilitySubmitSelfAuditDto dto = (FacilitySubmitSelfAuditDto)ParamUtil.getSessionAttr(request, SELF_AUDIT_DATA);
        MultipartFile multipartFile = null;
        for (sg.gov.moh.iais.egp.bsb.dto.audit.PrimaryDocDto.NewDocInfo newDocInfo : dto.getNewDocInfos()) {
            if (newDocInfo.getTmpId().equals(id)) {
                multipartFile = newDocInfo.getMultipartFile();
                break;
            }
        }
        return multipartFile;
    }

    private MultipartFile auditGetSavedFile(HttpServletRequest request, String id) {
        FacilitySubmitSelfAuditDto dto = (FacilitySubmitSelfAuditDto)ParamUtil.getSessionAttr(request, SELF_AUDIT_DATA);
        sg.gov.moh.iais.egp.bsb.dto.audit.PrimaryDocDto.DocRecordInfo info = null;
        for (sg.gov.moh.iais.egp.bsb.dto.audit.PrimaryDocDto.DocRecordInfo docRecordInfo : dto.getDocRecordInfos()) {
            if (docRecordInfo.getRepoId().equals(id)) {
                info = docRecordInfo;
                break;
            }
        }
        if (info == null) {
            throw new IllegalStateException(ERROR_MESSAGE_RECORD_INFO_NULL);
        }
        byte[] content = fileRepoClient.getFileFormDataBase(id).getEntity();
        return new ByteArrayMultipartFile(null, info.getFilename(), null, content);
    }

    /**
     * Follow-up report get the new doc file object
     * @param id key of the newDocMap in the PrimaryDocDto
     */
    private MultipartFile followupGetNewFile(HttpServletRequest request, String id) {
        CommonDocDto followupDoc = (CommonDocDto) ParamUtil.getSessionAttr(request,"followupDoc");
        return followupDoc.getNewDocMap().get(id).getMultipartFile();
    }

    /**
     * Follow-up report get the new doc file object
     * @param id key of the newDocMap in the PrimaryDocDto
     */
    private MultipartFile followupGetSavedFile(HttpServletRequest request, String id) {
        CommonDocDto followupDoc = (CommonDocDto) ParamUtil.getSessionAttr(request,"followupDoc");
        DocRecordInfo info = followupDoc.getSavedDocMap().get(id);
        if (info == null) {
            throw new IllegalStateException(ERROR_MESSAGE_RECORD_INFO_NULL);
        }
        byte[] content = fileRepoClient.getFileFormDataBase(id).getEntity();
        return new ByteArrayMultipartFile(null, info.getFilename(), null, content);
    }

    private MultipartFile commonDocGetNewFile(HttpServletRequest request, String id) {
        CommonDocDto followupDoc = (CommonDocDto) ParamUtil.getSessionAttr(request, DocConstants.KEY_COMMON_DOC_DTO);
        return followupDoc.getNewDocMap().get(id).getMultipartFile();
    }

    private MultipartFile commonDocGetSavedFile(HttpServletRequest request, String id) {
        CommonDocDto followupDoc = (CommonDocDto) ParamUtil.getSessionAttr(request,DocConstants.KEY_COMMON_DOC_DTO);
        DocRecordInfo info = followupDoc.getSavedDocMap().get(id);
        if (info == null) {
            throw new IllegalStateException(ERROR_MESSAGE_RECORD_INFO_NULL);
        }
        byte[] content = fileRepoClient.getFileFormDataBase(id).getEntity();
        return new ByteArrayMultipartFile(null, info.getFilename(), null, content);
    }

    private MultipartFile getInspectionReportPdf(HttpServletRequest request, String id) {
        byte[] content = fileRepoClient.getFileFormDataBase(id).getEntity();
        return new ByteArrayMultipartFile(null, "Inspection report.pdf", null, content);
    }

    private MultipartFile getCommentInspectionReport(HttpServletRequest request, String id) {
        CommentInsReportDto dto = (CommentInsReportDto) ParamUtil.getSessionAttr(request, InspectionConstants.KEY_COMMENT_REPORT_DATA);
        return dto.getNewDocMap().get(id).getMultipartFile();
    }

    private MultipartFile getNonComplianceInspectionNewReport(HttpServletRequest request, String id) {
        RectifyInsReportDto dto = (RectifyInsReportDto) ParamUtil.getSessionAttr(request, InspectionConstants.KEY_RECTIFY_SAVED_DOC_DTO);
        return dto.getNewDocMap().get(id).getMultipartFile();
    }

    private MultipartFile getNonComplianceInspectionSavedReport(HttpServletRequest request, String id) {
        RectifyInsReportDto dto = (RectifyInsReportDto) ParamUtil.getSessionAttr(request, InspectionConstants.KEY_RECTIFY_SAVED_DOC_DTO);
        DocRecordInfo info = dto.getSavedDocMap().get(id);
        if (info == null) {
            throw new IllegalStateException(ERROR_MESSAGE_RECORD_INFO_NULL);
        }
        byte[] content = fileRepoClient.getFileFormDataBase(id).getEntity();
        return new ByteArrayMultipartFile(null, info.getFilename(), null, content);
    }

    private MultipartFile getIncidentNotificationNewFile(HttpServletRequest request, String id) {
        NodeGroup incidentNotificationRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP_INCIDENT_NOT);
        SimpleNode documentNode = (SimpleNode) incidentNotificationRoot.at(NODE_NAME_DOCUMENTS);
        sg.gov.moh.iais.egp.bsb.dto.report.PrimaryDocDto dto = (sg.gov.moh.iais.egp.bsb.dto.report.PrimaryDocDto) documentNode.getValue();
        return dto.getNewDocMap().get(id).getMultipartFile();
    }

    private MultipartFile getIncidentNotificationSavedFile(HttpServletRequest request, String id) {
        NodeGroup incidentNotificationRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP_INCIDENT_NOT);
        SimpleNode documentNode = (SimpleNode) incidentNotificationRoot.at(NODE_NAME_DOCUMENTS);
        sg.gov.moh.iais.egp.bsb.dto.report.PrimaryDocDto dto = (sg.gov.moh.iais.egp.bsb.dto.report.PrimaryDocDto) documentNode.getValue();
        DocRecordInfo info = dto.getSavedDocMap().get(id);
        if (info == null) {
            throw new IllegalStateException(ERROR_MESSAGE_RECORD_INFO_NULL);
        }
        byte[] content = fileRepoClient.getFileFormDataBase(id).getEntity();
        return new ByteArrayMultipartFile(null, info.getFilename(), null, content);
    }

    private MultipartFile getInvestigationReportNewFile(HttpServletRequest request, String id) {
        NodeGroup root = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP_INVEST_REPORT);
        SimpleNode documentNode = (SimpleNode) root.at(NODE_NAME_DOCUMENTS);
        sg.gov.moh.iais.egp.bsb.dto.report.PrimaryDocDto dto = (sg.gov.moh.iais.egp.bsb.dto.report.PrimaryDocDto) documentNode.getValue();
        return dto.getNewDocMap().get(id).getMultipartFile();
    }

    private MultipartFile getInvestigationReportSavedFile(HttpServletRequest request, String id) {
        NodeGroup root = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP_INVEST_REPORT);
        SimpleNode documentNode = (SimpleNode) root.at(NODE_NAME_DOCUMENTS);
        sg.gov.moh.iais.egp.bsb.dto.report.PrimaryDocDto dto = (sg.gov.moh.iais.egp.bsb.dto.report.PrimaryDocDto) documentNode.getValue();
        DocRecordInfo info = dto.getSavedDocMap().get(id);
        if (info == null) {
            throw new IllegalStateException(ERROR_MESSAGE_RECORD_INFO_NULL);
        }
        byte[] content = fileRepoClient.getFileFormDataBase(id).getEntity();
        return new ByteArrayMultipartFile(null, info.getFilename(), null, content);
    }

    private MultipartFile getViewReportableEventSavedFile(HttpServletRequest request, String id){
        Map<String,DocRecordInfo> repoIdDocMap = (Map<String, DocRecordInfo>) ParamUtil.getSessionAttr(request,PARAM_REPO_ID_FILE_MAP);
        DocRecordInfo info = repoIdDocMap.get(id);
        if (info == null) {
            throw new IllegalStateException(ERROR_MESSAGE_RECORD_INFO_NULL);
        }
        byte[] content = fileRepoClient.getFileFormDataBase(id).getEntity();
        return new ByteArrayMultipartFile(null, info.getFilename(), null, content);

    }
}
