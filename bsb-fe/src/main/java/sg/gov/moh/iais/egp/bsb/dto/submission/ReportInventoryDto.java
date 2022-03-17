package sg.gov.moh.iais.egp.bsb.dto.submission;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
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
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocMeta;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.util.CollectionUtils;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;
import sop.servlet.webflow.HttpHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportInventoryDto implements Serializable{

    @Data
    @NoArgsConstructor
    public static class DocRecordInfo implements Serializable {
        private String docEntityId;
        private String docType;
        private String reportType;
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
    @AllArgsConstructor
    public static class DocsMetaDto implements Serializable {
        private Map<String, List<DocMeta>> metaDtoMap;
        private String reportType;
    }


    // this dto is pass data that needed saved to db
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SaveDocDto implements Serializable{
        private String facId;
        private List<ReportInventoryDto.DocRecordInfo> savedDocInfo;
        private String reportType;
    }

    /* docs already saved in DB, key is repoId */
    private Map<String, ReportInventoryDto.DocRecordInfo> savedDocMap;
    /* docs new uploaded, key is tmpId */
    private final Map<String, ReportInventoryDto.NewDocInfo> newDocMap;
    /* to be deleted files (which already saved), the string is repoId, used to delete file in repo */
    private final Set<String> toBeDeletedRepoIds;

    private String reportType;

    private String declare;




    @JsonIgnore
    private ValidationResultDto validationResultDto;


    public ReportInventoryDto() {
        savedDocMap = new LinkedHashMap<>();
        newDocMap = new LinkedHashMap<>();
        toBeDeletedRepoIds = new HashSet<>();
    }

    //should change
    public boolean doValidation() {
        List<DocMeta> metaDtoList = new ArrayList<>(this.newDocMap.size());
        this.newDocMap.values().forEach(i -> {
            DocMeta docMeta = new DocMeta(i.getTmpId(), i.getDocType(), i.getFilename(), i.getSize());
            metaDtoList.add(docMeta);
        });

        Map<String, List<DocMeta>> metaDtoMap = CollectionUtils.groupCollectionToMap(metaDtoList, DocMeta::getDocType);
        DocsMetaDto docsMetaDto = new DocsMetaDto(metaDtoMap,this.reportType);

        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("dataSubmissionFeignClient", "validateReportAndInventory", new Object[]{docsMetaDto});
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
    public Map<String, List<DocRecordInfo>> getExistDocTypeMap() {
        return CollectionUtils.groupCollectionToMap(this.savedDocMap.values(), DocRecordInfo::getDocType);
    }

    /**
     * get a structure used to display new selected docs
     * these docs have not been saved into DB, if user wants to download it, we send the data from current data structure
     * @return a map, the key is the doc type, the value is the new doc info list
     */
    public Map<String, List<NewDocInfo>> getNewDocTypeMap() {
        return CollectionUtils.groupCollectionToMap(this.newDocMap.values(), NewDocInfo::getDocType);
    }

    /**
     * get a structure used to display preview
     * the returned map contains two file types
     * the map will keep the pre-setting order of doc types
     * @return a map, the key is the doc type, the value is the docs
     */
    public Map<String, List<DocMeta>> getAllDocTypeMap() {
        Map<String, List<DocMeta>> data = Maps.newLinkedHashMapWithExpectedSize(DocConstants.FAC_REG_DOC_TYPE_ORDER.size());

        Map<String, List<DocRecordInfo>> savedMap = getExistDocTypeMap();
        Map<String, List<NewDocInfo>> newMap = getNewDocTypeMap();

        for (String docType : DocConstants.DATA_SUBMISSION_REPORT_AND_INVENTORY) {
            List<DocMeta> metaList = new ArrayList<>();
            List<DocRecordInfo> savedFiles = savedMap.get(docType);
            if (savedFiles != null) {
                savedFiles.forEach(i -> {
                    DocMeta docMeta = new DocMeta(i.getDocType(), i.getFilename(), i.getSize());
                    metaList.add(docMeta);
                });
            }
            List<NewDocInfo> newFiles = newMap.get(docType);
            if (newFiles != null) {
                newFiles.forEach(i -> {
                    DocMeta docMeta = new DocMeta(i.getDocType(), i.getFilename(), i.getSize());
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
     * This method will put new added files to the important data structure which is used to update the FacilityDoc.
     * This file is called when new uploaded files are saved and we get the repo Ids.
     * ATTENTION!!!
     * This method is dangerous! The relationship between the ids and the files in this dto is fragile!
     * We rely on the order is not changed! So we use a LinkedHashMap to save our data.
     * <p>
     * This method will generate id-bytes pairs at the same time, the result will be used to sync files to BE.
     * @return a list of file data to be synchronized to BE
     */
    public List<NewFileSyncDto> newFileSaved(List<String> repoIds) {
        Iterator<String> repoIdIt = repoIds.iterator();
        Iterator<NewDocInfo> newDocIt = newDocMap.values().iterator();

        List<NewFileSyncDto> newFileSyncDtoList = new ArrayList<>(repoIds.size());
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
            savedDocMap.put(repoId, docRecordInfo);

            NewFileSyncDto newFileSyncDto = new NewFileSyncDto();
            newFileSyncDto.setId(repoId);
            newFileSyncDto.setData(newDocInfo.getMultipartFile().getBytes());
            newFileSyncDtoList.add(newFileSyncDto);
        }
        return newFileSyncDtoList;
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

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getDeclare() {
        return declare;
    }

    public void setDeclare(String declare) {
        this.declare = declare;
    }

    //    ---------------------------- request -> object ----------------------------------------------

    private static final String MASK_PARAM = "file";
    private static final String KEY_REPORT = "report";
    private static final String KEY_OTHERS = "others";

    private static final String KEY_DELETED_NEW_FILES = "deleteNewFiles";

    public void reqObjMapping(HttpServletRequest request) {
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);

        //get reportType from request
        String repType =  ParamUtil.getString(request,"reportType");
        this.reportType = repType;

       // when do delete
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
            if (docType != null && !docType.equals(inputName) && KEY_REPORT.equals(docType)){
                reqDocMapping(inputName,mulReq,repType,loginContext,currentDate);
            }else if(docType != null && !docType.equals(inputName) && KEY_OTHERS.equals(docType)){
                reqDocMapping(inputName,mulReq,docType,loginContext,currentDate);
            }
        }
    }

    public void reqDocMapping(String inputName,MultipartHttpServletRequest mulReq,String docType,LoginContext loginContext,Date currentDate){
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
