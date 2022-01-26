package sg.gov.moh.iais.egp.bsb.dto.inspection;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sg.gov.moh.iais.egp.bsb.common.multipart.ByteArrayMultipartFile;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.dto.file.DocMeta;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sop.servlet.webflow.HttpHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Data
public class CommentInsReportDto implements Serializable {
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CommentInsReportValidateDto {
        private String upload;
        private List<DocMeta> metaList;
    }




    private String upload;

    /* docs new uploaded, key is tmpId */
    private final Map<String, NewDocInfo> newDocMap;

    /* docs already saved in DB
     * This process never read saved docs, so this field is just used to sync data. */
    private transient List<DocRecordInfo> savedDocList;

    public CommentInsReportDto() {
        newDocMap = new LinkedHashMap<>();
    }


    public List<DocMeta> getNewFileMeta() {
        List<DocMeta> metaList = new ArrayList<>(newDocMap.size());
        for (NewDocInfo i : newDocMap.values()) {
            DocMeta docMeta = new DocMeta(i.getTmpId(), i.getDocType(), i.getFilename(), i.getSize(), "commentReport");
            metaList.add(docMeta);
        }
        return metaList;
    }

    public CommentInsReportValidateDto toValidateDto() {
        CommentInsReportValidateDto validateDto = new CommentInsReportValidateDto();
        validateDto.setUpload(this.upload);
        validateDto.setMetaList(getNewFileMeta());
        return validateDto;
    }

    public List<NewFileSyncDto> newFileSaved(List<String> repoIds) {
        Iterator<String> repoIdIt = repoIds.iterator();
        Iterator<NewDocInfo> newDocIt = newDocMap.values().iterator();

        List<NewFileSyncDto> newFileSyncDtoList = new ArrayList<>(repoIds.size());
        this.savedDocList = new ArrayList<>(repoIds.size());
        while (repoIdIt.hasNext() && newDocIt.hasNext()) {
            String repoId = repoIdIt.next();
            NewDocInfo newDocInfo = newDocIt.next();

            DocRecordInfo docRecordInfo = new DocRecordInfo();
            docRecordInfo.setDocType(newDocInfo.getDocType());
            docRecordInfo.setFilename(newDocInfo.getFilename());
            docRecordInfo.setSize(newDocInfo.getSize());
            docRecordInfo.setRepoId(repoId);
            docRecordInfo.setSubmitBy(newDocInfo.getSubmitBy());
            docRecordInfo.setSubmitDate(newDocInfo.getSubmitDate());
            this.savedDocList.add(docRecordInfo);

            NewFileSyncDto newFileSyncDto = new NewFileSyncDto();
            newFileSyncDto.setId(repoId);
            newFileSyncDto.setData(newDocInfo.getMultipartFile().getBytes());
            newFileSyncDtoList.add(newFileSyncDto);
        }
        newDocMap.clear();
        return newFileSyncDtoList;
    }


    //    ---------------------------- request -> object ----------------------------------------------
    private static final String MASK_PARAM = "file";
    private static final String KEY_DELETED_NEW_FILES = "deleteNewFiles";
    private static final String KEY_WHETHER_UPLOAD = "upload";
    private static final String KEY_ATTACHMENT = "attachment";

    public void reqObjMapping(HttpServletRequest request) {
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);

        setUpload(ParamUtil.getString(mulReq, KEY_WHETHER_UPLOAD));


        String deleteNewFilesString = ParamUtil.getString(mulReq, KEY_DELETED_NEW_FILES);
        if (log.isInfoEnabled()) {
            log.info("deleteNewFilesString: {}", LogUtil.escapeCrlf(deleteNewFilesString));
        }
        if (StringUtils.hasLength(deleteNewFilesString)) {
            List<String> deleteFileTmpIds = Arrays.stream(deleteNewFilesString.split(","))
                    .map(f -> MaskUtil.unMaskValue(MASK_PARAM, f))
                    .collect(Collectors.toList());
            deleteFileTmpIds.forEach(this.newDocMap::remove);
        }

        // read new uploaded files
        List<MultipartFile> files = mulReq.getFiles(KEY_ATTACHMENT);
        if (files.isEmpty()) {
            log.info("No new file uploaded");
        } else {
            Date currentDate = new Date();
            LoginContext loginContext = (LoginContext) com.ecquaria.cloud.moh.iais.common.utils.ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
            for (MultipartFile f : files) {
                if (f.isEmpty()) {
                    log.warn("File is empty, ignore it");
                } else {
                    NewDocInfo newDocInfo = new NewDocInfo();
                    String tmpId = DocConstants.DOC_TYPE_APPLICANT_COMMENT_REPORT + f.getSize() + System.nanoTime();
                    newDocInfo.setTmpId(tmpId);
                    newDocInfo.setDocType(DocConstants.DOC_TYPE_APPLICANT_COMMENT_REPORT);
                    newDocInfo.setFilename(f.getOriginalFilename());
                    newDocInfo.setSize(f.getSize());
                    newDocInfo.setSubmitDate(currentDate);
                    newDocInfo.setSubmitBy(loginContext.getUserId());
                    byte[] bytes = new byte[0];
                    try {
                        bytes = f.getBytes();
                    } catch (IOException e) {
                        log.warn("Fail to read bytes for file {}, tmpId {}", f.getOriginalFilename(), tmpId);
                    }
                    ByteArrayMultipartFile multipartFile = new ByteArrayMultipartFile(f.getName(), f.getOriginalFilename(), f.getContentType(), bytes);
                    newDocInfo.setMultipartFile(multipartFile);
                    this.newDocMap.put(tmpId, newDocInfo);
                }
            }
        }
    }
}
