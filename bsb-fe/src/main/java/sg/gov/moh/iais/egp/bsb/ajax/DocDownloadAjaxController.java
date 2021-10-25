package sg.gov.moh.iais.egp.bsb.ajax;

import com.ecquaria.cloud.moh.iais.common.mask.MaskAttackException;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sg.gov.moh.iais.egp.bsb.action.FacilityRegistrationDelegator;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.common.node.simple.SimpleNode;
import sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.PrimaryDocDto;
import sg.gov.moh.iais.egp.bsb.util.LogUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;


@RestController
@RequestMapping(path = "/ajax/doc/download")
@Slf4j
public class DocDownloadAjaxController {
    private final FileRepoClient fileRepoClient;

    @Autowired
    public DocDownloadAjaxController(FileRepoClient fileRepoClient) {
        this.fileRepoClient = fileRepoClient;
    }

    @GetMapping("/new/{id}")
    public void downloadNotSavedFile(@PathVariable("id") String maskedTmpId, HttpServletRequest request, HttpServletResponse response) {
        String filename = "error";
        long length = 0;
        byte[] data = new byte[0];

        try {
            if (log.isInfoEnabled()) {
                log.info("Masked id is:{}", LogUtil.escapeCrlf(maskedTmpId));
            }

            String tmpId = MaskUtil.unMaskValue("file", maskedTmpId);
            if (tmpId == null || maskedTmpId == null || tmpId.equals(maskedTmpId)) {
                throw new MaskAttackException("Masked id is invalid");
            }

            log.info("tmpId is {}", tmpId);

            NodeGroup facRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, FacilityRegistrationDelegator.KEY_ROOT_NODE_GROUP);
            SimpleNode primaryDocNode = (SimpleNode) facRegRoot.at(FacRegisterConstants.NODE_NAME_PRIMARY_DOC);
            PrimaryDocDto primaryDocDto = (PrimaryDocDto) primaryDocNode.getValue();
            MultipartFile file = primaryDocDto.getNewDocMap().get(tmpId).getMultipartFile();
            if (file == null) {
                throw new IllegalStateException("Can not find the MultipartFile for the tmpId");
            }

            filename = file.getOriginalFilename();
            length = file.getSize();
            data = file.getBytes();
        } catch (Exception e) {
            log.error("Fail to download file", e);
        } finally {
            try {
                response.addHeader("Content-Disposition", "attachment;filename=\"" + filename + "\"");
                response.addHeader("Content-Length", "" + length);
                response.setContentType("application/x-octet-stream");
                OutputStream ops = response.getOutputStream();
                ops.write(data);
                ops.close();
                ops.flush();
            } catch (IOException e) {
                log.error("Fail to write file to response", e);
            }
        }
    }

    @GetMapping("/repo/{id}")
    public void downloadSavedFile(@PathVariable("id") String maskedRepoId, HttpServletRequest request, HttpServletResponse response) {
        if (log.isInfoEnabled()) {
            log.info("Masked id is:{}", LogUtil.escapeCrlf(maskedRepoId));
        }

        String repoId = MaskUtil.unMaskValue("file", maskedRepoId);
        if (repoId == null || maskedRepoId == null || repoId.equals(maskedRepoId)) {
            log.warn("Masked id is invalid");
            return;
        }
        log.info("repoId is {}", repoId);

        NodeGroup facRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, FacilityRegistrationDelegator.KEY_ROOT_NODE_GROUP);
        SimpleNode primaryDocNode = (SimpleNode) facRegRoot.at(FacRegisterConstants.NODE_NAME_PRIMARY_DOC);
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) primaryDocNode.getValue();
        PrimaryDocDto.DocRecordInfo info = primaryDocDto.getSavedDocMap().get(repoId);
        if (info == null) {
            log.error("Can not get the record for the repo id");
            return;
        }

        byte[] content = fileRepoClient.getFileFormDataBase(repoId).getEntity();

        try {
            response.addHeader("Content-Disposition", "attachment;filename=\"" + info.getFilename() + "\"");
            response.addHeader("Content-Length", "" + info.getSize());
            response.setContentType("application/x-octet-stream");
            OutputStream ops = response.getOutputStream();
            ops.write(content);
            ops.close();
            ops.flush();
        } catch (IOException e) {
            log.error("Fail to write file to response", e);
        }
    }
}
