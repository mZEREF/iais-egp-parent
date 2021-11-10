package sg.gov.moh.iais.egp.bsb.dto.submission;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sg.gov.moh.iais.egp.bsb.common.multipart.ByteArrayMultipartFile;
import sg.gov.moh.iais.egp.bsb.common.node.simple.ValidatableNodeValue;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.CollectionUtils;
import sg.gov.moh.iais.egp.bsb.util.LogUtil;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;
import sop.servlet.webflow.HttpHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportInventoryDto{

    @Data
    @NoArgsConstructor
    public static class DocRecordInfo implements Serializable {
        private String docEntityId;
        private String docType;
        private String filename;
        private long size;
        private String repoId;
        private Date submitDate;
        private String submitBy;
    }

    @Data
    @NoArgsConstructor
    public static class NewDocInfo implements Serializable {
        private String tmpId;
        private String docType;
        private String filename;
        private long size;
        private Date submitDate;
        private String submitBy;
        private ByteArrayMultipartFile multipartFile;
    }

    @Data
    @NoArgsConstructor
    public static class DocMeta implements Serializable {
        private String id;
        private String docType;
        private String filename;
        private long size;
        private String module;

        public DocMeta(String docType, String filename, long size) {
            this.docType = docType;
            this.filename = filename;
            this.size = size;
        }

        public DocMeta(String id, String docType, String filename, long size, String module) {
            this.id = id;
            this.docType = docType;
            this.filename = filename;
            this.size = size;
            this.module = module;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DocsMetaDto implements Serializable {
        private Map<String, List<ReportInventoryDto.DocMeta>> metaDtoMap;
    }

    /* docs already saved in DB, key is repoId */
    private Map<String, ReportInventoryDto.DocRecordInfo> savedDocMap;
    /* docs new uploaded, key is tmpId */
    private final Map<String, ReportInventoryDto.NewDocInfo> newDocMap;
    /* to be deleted files (which already saved), the string is repoId, used to delete file in repo */
    private final Set<String> toBeDeletedRepoIds;

    //a field record the type of report
    private String reportType;



    @JsonIgnore
    private ValidationResultDto validationResultDto;


    public ReportInventoryDto() {
        savedDocMap = new LinkedHashMap<>();
        newDocMap = new LinkedHashMap<>();
        toBeDeletedRepoIds = new HashSet<>();
    }

    public boolean doValidation() {
        List<ReportInventoryDto.DocMeta> metaDtoList = new ArrayList<>(this.savedDocMap.size() + this.newDocMap.size());
        this.savedDocMap.values().forEach(i -> {
            ReportInventoryDto.DocMeta docMeta = new ReportInventoryDto.DocMeta(i.getRepoId(), i.getDocType(), i.getFilename(), i.getSize(), "reportInventory");
            metaDtoList.add(docMeta);
        });
        this.newDocMap.values().forEach(i -> {
            ReportInventoryDto.DocMeta docMeta = new ReportInventoryDto.DocMeta(i.getTmpId(), i.getDocType(), i.getFilename(), i.getSize(), "reportInventory");
            metaDtoList.add(docMeta);
        });

        Map<String, List<ReportInventoryDto.DocMeta>> metaDtoMap = CollectionUtils.groupCollectionToMap(metaDtoList, ReportInventoryDto.DocMeta::getDocType);
        ReportInventoryDto.DocsMetaDto docsMetaDto = new ReportInventoryDto.DocsMetaDto(metaDtoMap);

        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("cerRegFeignClient", "validateCerPrimaryDocs", new Object[]{docsMetaDto});
        return validationResultDto.isPass();
    }

    public String retrieveValidationResult() {
        if (this.validationResultDto == null) {
            throw new IllegalStateException("This DTO is not validated");
        }
        return this.validationResultDto.toErrorMsg();
    }

    public void clearValidationResult() {
        this.validationResultDto = null;
    }


    /**
     * get a structure used to display the already saved docs
     * we have not retrieve data of these docs yet, if user wants to download it, we call API to retrieve the data
     * @return a map, the key is the doc type, the value is the exist doc info list
     */
    public Map<String, List<ReportInventoryDto.DocRecordInfo>> getExistDocTypeMap() {
        return CollectionUtils.groupCollectionToMap(this.savedDocMap.values(), ReportInventoryDto.DocRecordInfo::getDocType);
    }

    /**
     * get a structure used to display new selected docs
     * these docs have not been saved into DB, if user wants to download it, we send the data from current data structure
     * @return a map, the key is the doc type, the value is the new doc info list
     */
    public Map<String, List<ReportInventoryDto.NewDocInfo>> getNewDocTypeMap() {
        return CollectionUtils.groupCollectionToMap(this.newDocMap.values(), ReportInventoryDto.NewDocInfo::getDocType);
    }


    public Map<String, List<ReportInventoryDto.DocMeta>> getAllDocTypeMap() {
        Map<String, List<ReportInventoryDto.DocMeta>> data = Maps.newLinkedHashMapWithExpectedSize(DocConstants.FAC_REG_CERTIFIER_DOC_TYPE_ORDER.size());

        Map<String, List<ReportInventoryDto.DocRecordInfo>> savedMap = getExistDocTypeMap();
        Map<String, List<ReportInventoryDto.NewDocInfo>> newMap = getNewDocTypeMap();

        for (String docType : DocConstants.FAC_REG_CERTIFIER_DOC_TYPE_ORDER) {
            List<ReportInventoryDto.DocMeta> metaList = new ArrayList<>();
            List<ReportInventoryDto.DocRecordInfo> savedFiles = savedMap.get(docType);
            if (savedFiles != null) {
                savedFiles.forEach(i -> {
                    ReportInventoryDto.DocMeta docMeta = new ReportInventoryDto.DocMeta(i.getDocType(), i.getFilename(), i.getSize());
                    metaList.add(docMeta);
                });
            }
            List<ReportInventoryDto.NewDocInfo> newFiles = newMap.get(docType);
            if (newFiles != null) {
                newFiles.forEach(i -> {
                    ReportInventoryDto.DocMeta docMeta = new ReportInventoryDto.DocMeta(i.getDocType(), i.getFilename(), i.getSize());
                    metaList.add(docMeta);
                });
            }
            if (!metaList.isEmpty()) {
                data.put(docType, metaList);
            }
        }

        return data;
    }


    /**
     * This file is called when new uploaded files are saved and we get the repo Ids
     * ATTENTION!!!
     * This method is dangerous! The relationship between the ids and the files in this dto is fragile!
     * We rely on the order is not changed! So we use a LinkedHashMap to save our data.
     */
    public void newFileSaved(List<String> repoIds) {
        Iterator<String> repoIdIt = repoIds.iterator();
        Iterator<ReportInventoryDto.NewDocInfo> newDocIt = newDocMap.values().iterator();
        while (repoIdIt.hasNext() && newDocIt.hasNext()) {
            String repoId = repoIdIt.next();
            ReportInventoryDto.NewDocInfo newDocInfo = newDocIt.next();
            ReportInventoryDto.DocRecordInfo docRecordInfo = new ReportInventoryDto.DocRecordInfo();
            docRecordInfo.setDocType(newDocInfo.getDocType());
            docRecordInfo.setFilename(newDocInfo.getFilename());
            docRecordInfo.setSize(newDocInfo.getSize());
            docRecordInfo.setRepoId(repoId);
            docRecordInfo.setSubmitBy(newDocInfo.getSubmitBy());
            docRecordInfo.setSubmitDate(newDocInfo.getSubmitDate());
            savedDocMap.put(repoId, docRecordInfo);
        }
    }

    public Map<String, ReportInventoryDto.DocRecordInfo> getSavedDocMap() {
        return savedDocMap;
    }

    public void setSavedDocMap(Map<String, ReportInventoryDto.DocRecordInfo> savedDocMap) {
        this.savedDocMap = savedDocMap;
    }

    public Map<String, ReportInventoryDto.NewDocInfo> getNewDocMap() {
        return newDocMap;
    }

    public Set<String> getToBeDeletedRepoIds() {
        return toBeDeletedRepoIds;
    }




//    ---------------------------- request -> object ----------------------------------------------

    private static final String MASK_PARAM = "file";

    private static final String KEY_DELETED_SAVED_FILES = "deleteExistFiles";
    private static final String KEY_DELETED_NEW_FILES = "deleteNewFiles";

    public void reqObjMapping(HttpServletRequest request) {
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);

        String deleteSavedFilesString = ParamUtil.getString(mulReq, KEY_DELETED_SAVED_FILES);
        if (log.isInfoEnabled()) {
            log.info("deleteSavedFilesString: {}", LogUtil.escapeCrlf(deleteSavedFilesString));
        }
        if (StringUtils.hasLength(deleteSavedFilesString)) {
            List<String> deleteFileRepoIds = Arrays.stream(deleteSavedFilesString.split(","))
                    .map(f -> MaskUtil.unMaskValue(MASK_PARAM, f))
                    .collect(Collectors.toList());
            deleteFileRepoIds.forEach(it -> {this.savedDocMap.remove(it); toBeDeletedRepoIds.add(it);});
        }

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
        Iterator<String> inputNameIt = mulReq.getFileNames();
        Date currentDate = new Date();
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        while (inputNameIt.hasNext()) {
            String inputName = inputNameIt.next();
            String docType = MaskUtil.unMaskValue(MASK_PARAM, inputName);
            if (docType != null && !docType.equals(inputName)) {
                List<MultipartFile> files = mulReq.getFiles(inputName);
                for (MultipartFile f : files) {
                    if (log.isInfoEnabled()) {
                        log.info("input name: {}; filename: {}", LogUtil.escapeCrlf(inputName), LogUtil.escapeCrlf(f.getOriginalFilename()));
                    }
                    if (f.isEmpty()) {
                        log.warn("File is empty, ignore it");
                    } else {
                        ReportInventoryDto.NewDocInfo newDocInfo = new ReportInventoryDto.NewDocInfo();
                        String tmpId = inputName + f.getSize() + System.nanoTime();
                        newDocInfo.setTmpId(tmpId);
                        newDocInfo.setDocType(docType);
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
}
