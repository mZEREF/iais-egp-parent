package sg.gov.moh.iais.egp.bsb.ajax;

import com.ecquaria.cloud.moh.iais.common.mask.MaskAttackException;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.common.multipart.ByteArrayMultipartFile;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.PrimaryDocDto;
import sg.gov.moh.iais.egp.bsb.dto.incident.FollowupViewDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.afc.AFCCommonDocDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.afc.CertificationDocDisPlayDto;
import sg.gov.moh.iais.egp.bsb.dto.revocation.SubmitRevokeDto;
import sg.gov.moh.iais.egp.bsb.dto.suspension.SuspensionReinstatementDto;
import sg.gov.moh.iais.egp.bsb.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

import static sg.gov.moh.iais.egp.bsb.constant.DocConstants.PARAM_REPO_ID_DOC_MAP;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_DOC_DISPLAY_DTO_REPO_ID_NAME_MAP;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST;


@RestController
@RequestMapping(path = "/ajax/doc/download")
@Slf4j
public class DocDownloadAjaxController {
    private static final String ERROR_MESSAGE_RECORD_INFO_NULL = "Can not get the record for the repo id";
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

    @GetMapping("/facility/repo/{id}")
    public void downloadSavedFile(@PathVariable("id") String maskedRepoId, HttpServletRequest request, HttpServletResponse response) {
        downloadFile(request, response, maskedRepoId, this::unmaskFileId, this::getSavedFile);
    }

    @GetMapping("/revocation/repo/{id}")
    public void downloadSavedRevocationFile(@PathVariable("id") String maskedRepoId, HttpServletRequest request, HttpServletResponse response) {
        downloadFile(request, response, maskedRepoId, this::unmaskFileId, this::getSavedRevocationFile);
    }

    @GetMapping("/revocation/new/{id}")
    public void downloadNewRevocationFile(@PathVariable("id") String maskedRepoId, HttpServletRequest request, HttpServletResponse response) {
        downloadFile(request, response, maskedRepoId, this::unmaskFileId, this::getNewRevocationFile);
    }

    @GetMapping("/incident/repo/{id}")
    public void downloadIncidentSavedFile(@PathVariable("id") String maskedRepoId, HttpServletRequest request, HttpServletResponse response) {
        downloadFile(request, response, maskedRepoId, this::unmaskFileId, this::incidentGetSavedFile);
    }

    @GetMapping("/incidentView/repo/{id}")
    public void downloadIncidentViewSavedFile(@PathVariable("id") String maskedRepoId, HttpServletRequest request, HttpServletResponse response) {
        downloadFile(request, response, maskedRepoId, this::unmaskFileId, this::incidentViewGetSavedFile);
    }

    @GetMapping("/suspension/repo/{id}")
    public void downloadSavedSuspensionFile(@PathVariable("id") String maskedRepoId, HttpServletRequest request, HttpServletResponse response) {
        downloadFile(request, response, maskedRepoId, this::unmaskFileId, this::getSavedSuspensionFile);
    }

    @GetMapping("/suspension/new/{id}")
    public void downloadNewSuspensionFile(@PathVariable("id") String maskedRepoId, HttpServletRequest request, HttpServletResponse response) {
        downloadFile(request, response, maskedRepoId, this::unmaskFileId, this::getNewSuspensionFile);
    }

    @GetMapping("/applicationDoc/{id}")
    public void downloadApplicationFile(@PathVariable("id") String maskedRepoId, HttpServletRequest request, HttpServletResponse response) {
        downloadFile(request, response, maskedRepoId, this::unmaskFileId, this::getApplicationFile);
    }

    @GetMapping("/appointment/fac/repo/{id}")
    public void downloadAppointmentFacFile(@PathVariable("id") String maskedRepoId, HttpServletRequest request, HttpServletResponse response) {
        downloadFile(request, response, maskedRepoId, this::unmaskFileId, this::appointmentGetSavedFile);
    }

    @GetMapping("/insAFC/new/{id}")
    public void downloadNewInsAFCFile(@PathVariable("id") String maskedTmpId, HttpServletRequest request, HttpServletResponse response) {
        downloadFile(request, response, maskedTmpId, this::unmaskFileId, this::getInsAFCNewFile);
    }

    @GetMapping("/insAFC/repo/{id}")
    public void downloadSavedInsAFCFile(@PathVariable("id") String maskedTmpId, HttpServletRequest request, HttpServletResponse response) {
        downloadFile(request, response, maskedTmpId, this::unmaskFileId, this::getInsAFCSavedFile);
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

    private MultipartFile getSavedFile(HttpServletRequest request, String id) {
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) ParamUtil.getSessionAttr(request, "primaryDocDto");
        DocRecordInfo info = primaryDocDto.getSavedDocMap().get(id);
        if (info == null) {
            throw new IllegalStateException(ERROR_MESSAGE_RECORD_INFO_NULL);
        }
        byte[] content = fileRepoClient.getFileFormDataBase(id).getEntity();
        return new ByteArrayMultipartFile(null, info.getFilename(), null, content);
    }

    public MultipartFile incidentViewGetSavedFile(HttpServletRequest request, String id){
        Map<String,DocRecordInfo> repoIdDocMap = (Map<String, DocRecordInfo>) ParamUtil.getSessionAttr(request,"repoIdDocMap");
        DocRecordInfo info = repoIdDocMap.get(id);
        if (info == null) {
            throw new IllegalStateException(ERROR_MESSAGE_RECORD_INFO_NULL);
        }
        byte[] content = fileRepoClient.getFileFormDataBase(id).getEntity();
        return new ByteArrayMultipartFile(null, info.getFilename(), null, content);
    }

    public MultipartFile incidentGetSavedFile(HttpServletRequest request, String id){
        FollowupViewDto followupViewDto = (FollowupViewDto) ParamUtil.getSessionAttr(request,"processDto");
        List<DocDisplayDto> docDisplayDtoList = followupViewDto.getSupportDocDisplayDtoList();
        DocDisplayDto dto = CollectionUtils.uniqueIndexMap(docDisplayDtoList,DocDisplayDto::getFileRepoId).get(id);
        if (dto == null) {
            throw new IllegalStateException(ERROR_MESSAGE_RECORD_INFO_NULL);
        }
        byte[] content = fileRepoClient.getFileFormDataBase(id).getEntity();
        return new ByteArrayMultipartFile(null, dto.getDocName(), null, content);
    }

    private MultipartFile getSavedSuspensionFile(HttpServletRequest request, String id) {
        SuspensionReinstatementDto dto = (SuspensionReinstatementDto) ParamUtil.getSessionAttr(request, "suspensionReinstatementDto");
        DocRecordInfo info = dto.getPrimaryDocDto().getSavedDocMap().get(id);
        if (info == null) {
            throw new IllegalStateException(ERROR_MESSAGE_RECORD_INFO_NULL);
        }
        byte[] content = fileRepoClient.getFileFormDataBase(id).getEntity();
        return new ByteArrayMultipartFile(null, info.getFilename(), null, content);
    }

    private MultipartFile getNewSuspensionFile(HttpServletRequest request, String id) {
        SuspensionReinstatementDto dto = (SuspensionReinstatementDto) ParamUtil.getSessionAttr(request,"suspensionReinstatementDto");
        sg.gov.moh.iais.egp.bsb.dto.suspension.PrimaryDocDto primaryDocDto = dto.getPrimaryDocDto();
        if(primaryDocDto==null){
            throw new IllegalStateException(ERROR_MESSAGE_RECORD_INFO_NULL);
        }
        return primaryDocDto.getNewDocMap().get(id).getMultipartFile();
    }

    private MultipartFile getSavedRevocationFile(HttpServletRequest request, String id) {
        SubmitRevokeDto dto = (SubmitRevokeDto) ParamUtil.getSessionAttr(request, "revokeDto");
        DocRecordInfo info = dto.getPrimaryDocDto().getSavedDocMap().get(id);
        if (info == null) {
            throw new IllegalStateException(ERROR_MESSAGE_RECORD_INFO_NULL);
        }
        byte[] content = fileRepoClient.getFileFormDataBase(id).getEntity();
        return new ByteArrayMultipartFile(null, info.getFilename(), null, content);
    }

    private MultipartFile getNewRevocationFile(HttpServletRequest request, String id) {
        SubmitRevokeDto dto = (SubmitRevokeDto) ParamUtil.getSessionAttr(request,"revokeDto");
        sg.gov.moh.iais.egp.bsb.dto.suspension.PrimaryDocDto primaryDocDto = dto.getPrimaryDocDto();
        if(primaryDocDto==null){
            throw new IllegalStateException(ERROR_MESSAGE_RECORD_INFO_NULL);
        }
        return primaryDocDto.getNewDocMap().get(id).getMultipartFile();
    }

    private MultipartFile getApplicationFile(HttpServletRequest request, String id) {
        Map<String, String> map = (Map<String, String>) ParamUtil.getSessionAttr(request, KEY_DOC_DISPLAY_DTO_REPO_ID_NAME_MAP);
        String fileName = map.get(id);
        if (fileName == null) {
            throw new IllegalStateException(ERROR_MESSAGE_RECORD_INFO_NULL);
        }
        byte[] content = fileRepoClient.getFileFormDataBase(id).getEntity();
        return new ByteArrayMultipartFile(null, fileName, null, content);
    }

    public MultipartFile appointmentGetSavedFile(HttpServletRequest request, String id){
        List<DocDisplayDto> docDisplayDtoList = (List<DocDisplayDto>) ParamUtil.getSessionAttr(request, KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST);
        DocDisplayDto dto = CollectionUtils.uniqueIndexMap(docDisplayDtoList,DocDisplayDto::getFileRepoId).get(id);
        if (dto == null) {
            throw new IllegalStateException(ERROR_MESSAGE_RECORD_INFO_NULL);
        }
        byte[] content = fileRepoClient.getFileFormDataBase(id).getEntity();
        return new ByteArrayMultipartFile(null, dto.getDocName(), null, content);
    }

    private MultipartFile getInsAFCNewFile(HttpServletRequest request, String id) {
        AFCCommonDocDto dto = (AFCCommonDocDto) ParamUtil.getSessionAttr(request, DocConstants.KEY_COMMON_DOC_DTO);
        return dto.getNewDocMap().get(id).getMultipartFile();
    }

    private MultipartFile getInsAFCSavedFile(HttpServletRequest request, String id){
        Map<String, CertificationDocDisPlayDto> repoIdDocDtoMap = (Map<String, CertificationDocDisPlayDto>) ParamUtil.getSessionAttr(request,PARAM_REPO_ID_DOC_MAP);
        CertificationDocDisPlayDto info = repoIdDocDtoMap.get(id);
        if (info == null) {
            throw new IllegalStateException(ERROR_MESSAGE_RECORD_INFO_NULL);
        }
        byte[] content = fileRepoClient.getFileFormDataBase(id).getEntity();
        return new ByteArrayMultipartFile(null, info.getDocName(), null, content);
    }
}
