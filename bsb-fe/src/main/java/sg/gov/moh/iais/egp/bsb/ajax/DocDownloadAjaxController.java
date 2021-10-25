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
import sg.gov.moh.iais.egp.bsb.common.multipart.ByteArrayMultipartFile;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.common.node.simple.SimpleNode;
import sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.PrimaryDocDto;
import sg.gov.moh.iais.egp.bsb.util.LogUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;


@RestController
@RequestMapping(path = "/ajax/doc/download")
@Slf4j
public class DocDownloadAjaxController {
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


    @GetMapping("/facReg/new/{id}")
    public void downloadNotSavedFile(@PathVariable("id") String maskedTmpId, HttpServletRequest request, HttpServletResponse response) {
        downloadFile(request, response, maskedTmpId, this::unmaskFileId, this::facRegGetNewFile);
    }

    @GetMapping("/facReg/repo/{id}")
    public void downloadSavedFile(@PathVariable("id") String maskedRepoId, HttpServletRequest request, HttpServletResponse response) {
        downloadFile(request, response, maskedRepoId, this::unmaskFileId, this::facRegGetSavedFile);
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

    /**
     * Facility registration get the new doc file object
     * @param id key of the newDocMap in the PrimaryDocDto
     */
    private MultipartFile facRegGetNewFile(HttpServletRequest request, String id) {
        NodeGroup facRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, FacilityRegistrationDelegator.KEY_ROOT_NODE_GROUP);
        SimpleNode primaryDocNode = (SimpleNode) facRegRoot.at(FacRegisterConstants.NODE_NAME_PRIMARY_DOC);
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) primaryDocNode.getValue();
        return primaryDocDto.getNewDocMap().get(id).getMultipartFile();
    }

    /**
     * Facility registration get the saved doc file data
     * @param id key of the savedDocMap in the PrimaryDocDto
     */
    private MultipartFile facRegGetSavedFile(HttpServletRequest request, String id) {
        NodeGroup facRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, FacilityRegistrationDelegator.KEY_ROOT_NODE_GROUP);
        SimpleNode primaryDocNode = (SimpleNode) facRegRoot.at(FacRegisterConstants.NODE_NAME_PRIMARY_DOC);
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) primaryDocNode.getValue();
        PrimaryDocDto.DocRecordInfo info = primaryDocDto.getSavedDocMap().get(id);
        if (info == null) {
            throw new IllegalStateException("Can not get the record for the repo id");
        }
        byte[] content = fileRepoClient.getFileFormDataBase(id).getEntity();
        return new ByteArrayMultipartFile(null, info.getFilename(), null, content);
    }
}
