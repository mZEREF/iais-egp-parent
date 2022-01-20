package sg.gov.moh.iais.egp.bsb.ajax;


import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.sz.commons.util.DateUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.client.InternalDocClient;
import sg.gov.moh.iais.egp.bsb.dto.entity.InternalDocDto;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/ajax/doc")
public class DocumentAjaxController {
    private final FileRepoClient fileRepoClient;
    private final InternalDocClient internalDocClient;

    @Autowired
    public DocumentAjaxController(FileRepoClient fileRepoClient, InternalDocClient internalDocClient) {
        this.fileRepoClient = fileRepoClient;
        this.internalDocClient = internalDocClient;
    }

    @GetMapping(value = "/internal-doc")
    public void downloadInternalDoc(HttpServletResponse response,
                                    @RequestParam("repoId") String maskedRepoId,
                                    @RequestParam("filename") String filename) throws IOException {
        String repoId = MaskUtil.unMaskValue("file", maskedRepoId);
        if (repoId == null || repoId.equals(maskedRepoId)) {
            log.error("Invalid repo ID:{}", LogUtil.escapeCrlf(maskedRepoId));
            return;
        }
        byte[] fileData = fileRepoClient.getFileFormDataBase(repoId).getEntity();
        response.addHeader("Content-Disposition", "attachment;filename=\"" + filename + "\"");
        response.addHeader("Content-Length", String.valueOf(fileData.length));
        response.setContentType("application/x-octet-stream");
        OutputStream ops = new BufferedOutputStream(response.getOutputStream());
        ops.write(fileData);
        ops.flush();
        ops.close();
    }

    @PostMapping(value = "/internal-doc", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadInternalDoc(@RequestParam("internalFileSelectedFile") MultipartFile selectedFile,
                                    @RequestParam("internalFileType") String docType,
                                    @RequestParam("curAppId") String maskedAppId) {
        log.info("Start to upload internal document");
        String data = "";
        boolean go = true;
        String appId = MaskUtil.unMaskValue("uploadInternalDoc", maskedAppId);
        if (appId == null || appId.equals(maskedAppId)) {
            log.error("Invalid masked app ID: {}", LogUtil.escapeCrlf(maskedAppId));
            data = "{\"success\":false, \"error_msg\":\"Bad request\"}";
            go = false;
        }
        if (go && selectedFile == null || selectedFile.isEmpty() || selectedFile.getOriginalFilename() == null) {
            log.error("No file selected");
            data = "{\"success\":false, \"error_msg\":\"No file selected\"}";
            go = false;
        }

        if (go) {
            try {
                AuditTrailDto auditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
                Assert.notNull(auditTrailDto, "AuditTrailDto must exists");

                // save file into file-repo
                List<String> repoIdList = fileRepoClient.saveFiles(new MultipartFile[]{selectedFile}).getEntity();
                String repoId = repoIdList.get(0);
                Assert.hasLength(repoId, "No repo ID received");

                Date current = new Date();
                InternalDocDto internalDocDto = new InternalDocDto();
                internalDocDto.setApplicationId(appId);
                String filename = Paths.get(selectedFile.getOriginalFilename()).getFileName().toString();
                internalDocDto.setDocName(filename);
                internalDocDto.setDocSize(selectedFile.getSize());
                internalDocDto.setFileRepoId(repoId);
                internalDocDto.setDocType(docType);
                internalDocDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                internalDocDto.setSubmitDt(current);
                internalDocDto.setSubmitBy(auditTrailDto.getMohUserGuid());
                internalDocClient.saveInternalDoc4CurUser(internalDocDto);

                // this masked appId and repo ID will be used to delete this file
                String maskedAppId4Delete = MaskUtil.maskValue("file", internalDocDto.getApplicationId());
                internalDocDto.setApplicationId(maskedAppId4Delete);
                String maskedRepoId = MaskUtil.maskValue("file", repoId);
                internalDocDto.setFileRepoId(maskedRepoId);

                // remove sensitive data
                internalDocDto.setSubmitBy(null);

                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.valueToTree(internalDocDto);
                ObjectNode rootObjectNode = (ObjectNode) rootNode;
                rootObjectNode.put("success", true);
                rootObjectNode.put("submitByName", auditTrailDto.getMohUserId());
                rootObjectNode.put("submitDtStr", DateUtil.formatDateTime(current, "dd/MM/yyyy HH:mm"));
                data = mapper.writeValueAsString(rootObjectNode);
            } catch (Exception e) {
                log.error("Fail to save internal document");
                data = "{\"success\":false, \"error_msg\":\"Fail to upload document\"}";
            }
        }
        return data;
    }

    @DeleteMapping(value = "/internal-doc")
    public String deleteInternalDoc(@RequestParam("appId") String maskedAppId, @RequestParam("repoId") String maskedRepoId) {
        log.info("Start to delete internal document");
        String data = "";
        boolean go = true;
        String appId = MaskUtil.unMaskValue("file", maskedAppId);
        String repoId = MaskUtil.unMaskValue("file", maskedRepoId);
        if (appId == null || repoId == null || appId.equals(maskedAppId) || repoId.equals(maskedRepoId)) {
            log.error("Invalid masked app ID {}, repo ID:{}", LogUtil.escapeCrlf(maskedAppId), LogUtil.escapeCrlf(maskedRepoId));
            data = "{\"success\":false, \"error_msg\":\"Bad request\"}";
            go = false;
        }
        if (go) {
            try {
                internalDocClient.deleteInternalDoc(appId, repoId);
            } catch (Exception e) {
                log.error("Fail to delete internal document");
                data = "{\"success\":false, \"error_msg\":\"Fail to delete document\"}";
            }
        }
        return data;
    }
}
