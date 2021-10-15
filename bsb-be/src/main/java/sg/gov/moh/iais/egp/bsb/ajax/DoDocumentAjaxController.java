package sg.gov.moh.iais.egp.bsb.ajax;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.utils.*;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sg.gov.moh.iais.egp.bsb.client.DocClient;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.dto.audit.AuditDocDto;
import sg.gov.moh.iais.egp.bsb.entity.Facility;
import sg.gov.moh.iais.egp.bsb.entity.FacilityDoc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author tangtang
 */
@Controller
@Slf4j
public class DoDocumentAjaxController {

    private static final String FACILITY = "facility";
    private static final String VERIFY = "verify";
    private static final String AUDIT_DOC_DTO = "auditDocDto";

    @Autowired
    private FileRepoClient fileRepoClient;

    @Autowired
    private DocClient docClient;

    //upload file
    @PostMapping(value = "/uploadInternalFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public String uploadInternalFile(HttpServletRequest request, @RequestParam("selectedFile") MultipartFile selectedFile, @RequestParam("fileRemark") String remark) {
        String data = "";
        request.setAttribute("selectedFile", selectedFile);
        String csrf = ParamUtil.getString(request, "OWASP_CSRFTOKEN");
        FacilityDoc doc = new FacilityDoc();
        if (selectedFile != null && !StringUtils.isEmpty(selectedFile.getOriginalFilename())) {
            //size
            long size = selectedFile.getSize();
            doc.setSize(size / 1024);
            log.info(StringUtil.changeForLog("DoDocumentAjaxController uploadInternalFile OriginalFilename ==== " + selectedFile.getOriginalFilename()));
            //type
            String[] fileSplit = selectedFile.getOriginalFilename().split("\\.");
            //name
            String fileName = IaisCommonUtils.getDocNameByStrings(fileSplit);
            doc.setName(selectedFile.getOriginalFilename());

            doc.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            Facility facility = (Facility) ParamUtil.getSessionAttr(request, FACILITY);
            doc.setFacility(facility);

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

            doc.setSubmitAt(new Date());
            doc.setSubmitAtStr(formatter.format(doc.getSubmitAt()));
            doc.setSubmitBy(IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
            doc.setSubmitByName(doc.getAuditTrailDto().getMohUserId());

            FileRepoDto fileRepoDto = new FileRepoDto();
            fileRepoDto.setFileName(fileName);
            fileRepoDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            fileRepoDto.setRelativePath(AppConsts.FALSE);

            //save file to file DB
            String repoId = fileRepoClient.saveFiles(selectedFile, JsonUtil.parseToJson(fileRepoDto)).getEntity();
            doc.setFileRepoId(repoId);
            String id = docClient.saveFacilityDoc(doc).getEntity();
            doc.setId(id);

            // set auditDocDto to seesion
            AuditDocDto auditDocDto = (AuditDocDto) ParamUtil.getSessionAttr(request, AUDIT_DOC_DTO);
            if (auditDocDto == null) {
                auditDocDto = new AuditDocDto();
            }
            List<FacilityDoc> facilityDocs;
            if (auditDocDto != null && auditDocDto.getFacilityDocs() != null) {
                facilityDocs = auditDocDto.getFacilityDocs();
            } else {
                facilityDocs = new ArrayList<>(5);
            }
            Integer index = (Integer) ParamUtil.getSessionAttr(request, "AppIntranetDocDtoIndex");
            int fileSizes = facilityDocs.size();
            if (index == null) {
                index = fileSizes;
            } else {
                index++;
            }
            ParamUtil.setSessionAttr(request, "AppIntranetDocDtoIndex", index);
            String mask = MaskUtil.maskValue("fileRo" + index, doc.getFileRepoId());
            String url = "<a href=\"pageContext.request.contextPath/file-repo?filerepo=fileRostatus.index&fileRostatus.index=maskDec&fileRepoName=interalFile.docName&OWASP_CSRFTOKEN=csrf\" title=\"Download\" class=\"downloadFile\">";
            try {
                String docName = selectedFile.getOriginalFilename() == null ? "" : URLEncoder.encode(selectedFile.getOriginalFilename(), StandardCharsets.UTF_8.toString());
                url = url.replaceAll("pageContext.request.contextPath", "/bsb-be").replaceAll("status.index", String.valueOf(index)).
                        replaceAll("interalFile.docName", docName).replace("maskDec", mask).replace("csrf", csrf);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            doc.setUrl(url);
            doc.setFileSn(0);
            doc.setIsUpload(Boolean.TRUE);
            facilityDocs.add(doc);
            if (auditDocDto != null) {
                auditDocDto.setFacilityDocs(facilityDocs);
                auditDocDto.setIsUpload(Boolean.TRUE);
            }
            ParamUtil.setSessionAttr(request, AUDIT_DOC_DTO, auditDocDto);
            //call back upload file succeeded
            if (!StringUtils.isEmpty(doc.getFileRepoId())) {
                doc.setMaskId(MaskUtil.maskValue("fileRepoId", doc.getFileRepoId()));
            }
            String appIntranetDocDtoJsonStr = JsonUtil.parseToJson(doc);
            data = appIntranetDocDtoJsonStr;
            ParamUtil.setRequestAttr(request, "doDocument", "Y");
        }
        return data;
    }

    @PostMapping(value = "/deleteInternalFile")
    @ResponseBody
    public Map<String, Object> deleteInternalFile(HttpServletRequest request) {
        String guid = request.getParameter("appDocId");
        String fileId = MaskUtil.unMaskValue("fileRepoId", guid);
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        AuditDocDto auditDocDto = (AuditDocDto) ParamUtil.getSessionAttr(request, AUDIT_DOC_DTO);
        if (auditDocDto != null && auditDocDto.getFacilityDocs() != null) {
            List<FacilityDoc> facilityDocs = auditDocDto.getFacilityDocs();
            FacilityDoc facilityDoc = null;
            for (FacilityDoc doc : facilityDocs) {
                if (doc.getFileRepoId().equalsIgnoreCase(fileId)) {
                    docClient.deleteByFileRepoId(doc.getFileRepoId());
                    FileRepoDto fileRepoDto = new FileRepoDto();
                    fileRepoDto.setId(doc.getFileRepoId());
                    fileRepoDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    fileRepoClient.removeFileById(fileRepoDto);
                    facilityDoc = doc;
                }
            }
            if (facilityDoc != null)
                facilityDocs.remove(facilityDoc);
            boolean isUpload = false;
            for (FacilityDoc facilityDoc1 : facilityDocs) {
                if (facilityDoc1.getIsUpload() != null && facilityDoc1.getIsUpload()) {
                    isUpload = true;
                    break;
                }
            }
            auditDocDto.setIsUpload(isUpload);
            ParamUtil.setSessionAttr(request, AUDIT_DOC_DTO, auditDocDto);
            map.put("fileSn", 0);
            if (StringUtils.isEmpty(facilityDocs.size())) {
                map.put("noFilesMessage", MessageUtil.getMessageDesc("GENERAL_ACK018"));
            }
        }
        return map;
    }


    @PostMapping(value = "/verifyFileExist")
    @ResponseBody
    public Map<String, Object> verifyFileExist(HttpServletRequest request) {
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        String reportId = ParamUtil.getString(request, "repoId");
        if (StringUtils.isEmpty(reportId)) {
            map.put(VERIFY, "N");
            return map;
        }
        byte[] data = fileRepoClient.getFileFormDataBase(reportId).getEntity();
        if (data == null) {
            map.put(VERIFY, "N");
            return map;
        }
        map.put(VERIFY, "Y");
        return map;
    }

    @GetMapping(value = "/file-repo")
    public @ResponseBody
    void fileDownload(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug(StringUtil.changeForLog("file-repo start ...."));
        String fileRepoName = ParamUtil.getRequestString(request, "fileRepoName");
        String maskFileRepoIdName = ParamUtil.getRequestString(request, "filerepo");
        String fileRepoId = ParamUtil.getMaskedString(request, maskFileRepoIdName);
        if (StringUtils.isEmpty(fileRepoId)) {
            log.debug(StringUtil.changeForLog("file-repo id is empty"));
            return;
        }
        byte[] fileData = fileRepoClient.getFileFormDataBase(fileRepoId).getEntity();
        response.addHeader("Content-Disposition", "attachment;filename=\"" + fileRepoName + "\"");
        response.addHeader("Content-Length", "" + fileData.length);
        response.setContentType("application/x-octet-stream");
        OutputStream ops = new BufferedOutputStream(response.getOutputStream());
        ops.write(fileData);
        ops.close();
        ops.flush();
        log.debug(StringUtil.changeForLog("file-repo end ...."));
    }
}
