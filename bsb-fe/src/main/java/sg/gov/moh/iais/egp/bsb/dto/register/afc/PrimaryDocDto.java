package sg.gov.moh.iais.egp.bsb.dto.register.afc;

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
import sg.gov.moh.iais.egp.bsb.common.node.simple.ValidatableNodeValue;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocMeta;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo;
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
public class PrimaryDocDto extends ValidatableNodeValue {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DocsMetaDto implements Serializable {
        private Map<String, List<DocMeta>> metaDtoMap;
    }

    /* docs already saved in DB, key is repoId */
    private Map<String, DocRecordInfo> savedDocMap;
    private Map<String,CertTeamSavedDoc> certTeamSavedDocMap;
    /* docs new uploaded, key is tmpId */
    private final Map<String,CertTeamNewDoc> certTeamNewDocMap;
    private final Map<String, NewDocInfo> newDocMap;
    /* to be deleted files (which already saved), the string is repoId, used to delete file in repo */
    private final Set<String> toBeDeletedRepoIds;
    private final Set<String> certTeamToBeDeletedRepoIds;





    @JsonIgnore
    private ValidationResultDto validationResultDto;


    public PrimaryDocDto() {
        savedDocMap = new LinkedHashMap<>();
        certTeamSavedDocMap = new LinkedHashMap<>();
        newDocMap = new LinkedHashMap<>();
        certTeamNewDocMap = new LinkedHashMap<>();
        toBeDeletedRepoIds = new HashSet<>();
        certTeamToBeDeletedRepoIds = new HashSet<>();
    }

    @Override
    public boolean doValidation() {
        List<DocMeta> metaDtoList = new ArrayList<>(this.savedDocMap.size() + this.newDocMap.size()+this.certTeamNewDocMap.size()+this.certTeamSavedDocMap.size());
        this.savedDocMap.values().forEach(i -> {
            DocMeta docMeta = new DocMeta(i.getRepoId(), i.getDocType(), i.getFilename(), i.getSize());
            metaDtoList.add(docMeta);
        });
        this.newDocMap.values().forEach(i -> {
            DocMeta docMeta = new DocMeta(i.getTmpId(), i.getDocType(), i.getFilename(), i.getSize());
            metaDtoList.add(docMeta);
        });
        this.certTeamNewDocMap.values().forEach(i -> {
            DocMeta docMeta = new DocMeta(i.getTmpId(),i.getMemberDocKey().split(KEY_HYPHEN)[1], i.getFilename(), i.getSize());
            metaDtoList.add(docMeta);
        });
        this.certTeamSavedDocMap.values().forEach(i -> {
            DocMeta docMeta = new DocMeta(i.getRepoId(),i.getDocType(), i.getFilename(), i.getSize());
            metaDtoList.add(docMeta);
        });


        Map<String, List<DocMeta>> metaDtoMap = CollectionUtils.groupCollectionToMap(metaDtoList, DocMeta::getDocType);
        DocsMetaDto docsMetaDto = new DocsMetaDto(metaDtoMap);
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("cerRegFeignClient", "validateCerPrimaryDocs", new Object[]{docsMetaDto});
        return validationResultDto.isPass();
    }

    @Override
    public String retrieveValidationResult() {
        if (this.validationResultDto == null) {
            throw new IllegalStateException("This DTO is not validated");
        }
        return this.validationResultDto.toErrorMsg();
    }

    @Override
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

    public Map<String,CertTeamSavedDoc> getExistCertTeamKeyMap(){
        Map<String,CertTeamSavedDoc> certTeamSavedKeyMap = new HashMap<>(this.certTeamSavedDocMap.size());
        for (CertTeamSavedDoc doc : this.certTeamSavedDocMap.values()) {
            certTeamSavedKeyMap.put(doc.getMemberIdNo()+KEY_HYPHEN+doc.getDocType(),doc);
        }
        return certTeamSavedKeyMap;
    }

    /**
     * get a structure used to display new selected docs
     * these docs have not been saved into DB, if user wants to download it, we send the data from current data structure
     * @return a map, the key is the doc type, the value is the new doc info list
     */
    public Map<String, List<NewDocInfo>> getNewDocTypeMap() {
        return CollectionUtils.groupCollectionToMap(this.newDocMap.values(), NewDocInfo::getDocType);
    }


    public Map<String,CertTeamNewDoc> getNewCertTeamKeyMap(){
        return CollectionUtils.uniqueIndexMap(this.certTeamNewDocMap.values(),CertTeamNewDoc::getMemberDocKey);
    }


    public Map<String, List<DocMeta>> getAllDocTypeMap() {
        Map<String, List<DocMeta>> data = Maps.newLinkedHashMapWithExpectedSize(DocConstants.FAC_REG_CERTIFIER_DOC_TYPE_ORDER.size());

        Map<String, List<DocRecordInfo>> savedMap = getExistDocTypeMap();
        Map<String, List<NewDocInfo>> newMap = getNewDocTypeMap();

        for (String docType : DocConstants.FAC_REG_CERTIFIER_DOC_TYPE_ORDER) {
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

    public List<NewFileSyncDto> newCertTeamFileSaved(List<String> repoIds) {
        Iterator<String> repoIdIt = repoIds.iterator();
        Iterator<CertTeamNewDoc> newDocIt = certTeamNewDocMap.values().iterator();

        List<NewFileSyncDto> newFileSyncDtoList = new ArrayList<>(repoIds.size());
        while (repoIdIt.hasNext() && newDocIt.hasNext()) {
            String repoId = repoIdIt.next();
            CertTeamNewDoc certTeamNewDoc = newDocIt.next();
            CertTeamSavedDoc certTeamSavedDoc = new CertTeamSavedDoc();
            String[] keys = certTeamNewDoc.getMemberDocKey().split(KEY_HYPHEN);
            certTeamSavedDoc.setDocType(keys[1]);
            certTeamSavedDoc.setSize(certTeamNewDoc.getSize());
            certTeamSavedDoc.setFilename(certTeamNewDoc.getFilename());
            certTeamSavedDoc.setMemberIdNo(keys[0]);
            certTeamSavedDoc.setSubmitBy(certTeamNewDoc.getSubmitBy());
            certTeamSavedDoc.setSubmitDate(certTeamNewDoc.getSubmitDate());
            certTeamSavedDoc.setRepoId(repoId);
            certTeamSavedDoc.setSize(certTeamNewDoc.getSize());
            certTeamSavedDocMap.put(repoId, certTeamSavedDoc);

            NewFileSyncDto newFileSyncDto = new NewFileSyncDto();
            newFileSyncDto.setId(repoId);
            newFileSyncDto.setData(certTeamNewDoc.getMultipartFile().getBytes());
            newFileSyncDtoList.add(newFileSyncDto);
        }
        return newFileSyncDtoList;
    }

    public Map<String, DocRecordInfo> getSavedDocMap() {
        return savedDocMap;
    }

    public void setSavedDocMap(Map<String, DocRecordInfo> savedDocMap) {
        this.savedDocMap = savedDocMap;
    }

    public Map<String,NewDocInfo> getNewDocMap() {
        return newDocMap;
    }

    public Set<String> getToBeDeletedRepoIds() {
        return toBeDeletedRepoIds;
    }

    public Map<String, CertTeamSavedDoc> getCertTeamSavedDocMap() {
        return certTeamSavedDocMap;
    }

    public void setCertTeamSavedDocMap(Map<String, CertTeamSavedDoc> certTeamSavedDocMap) {
        this.certTeamSavedDocMap = certTeamSavedDocMap;
    }

    public Map<String, CertTeamNewDoc> getCertTeamNewDocMap() {
        return certTeamNewDocMap;
    }

    public Set<String> getCertTeamToBeDeletedRepoIds() {
        return certTeamToBeDeletedRepoIds;
    }

    //    ---------------------------- request -> object ----------------------------------------------

    public static final String KEY_HYPHEN = "--v--";
    private static final String MASK_PARAM = "file";
    public static final String SUFFIX_TESTIMONIAL = "Testimonial";
    public static final String SUFFIX_CURRICULUM_VITAE = "CurriculumVitae";

    private static final String KEY_DELETED_SAVED_FILES = "deleteExistFiles";
    private static final String KEY_DELETED_NEW_FILES = "deleteNewFiles";
    private static final String KEY_DELETED_SAVED_CERTIFY_TEAM_FILES = "deleteExistCertTeamFiles";
    private static final String KEY_DELETED_NEW_CERTIFY_TEAM_FILES = "deleteNewCertTeamFiles";

    public void reqObjMapping(HttpServletRequest request) {
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);

        doCommonFileDelete(mulReq);
        doCertifyTeamFileDelete(mulReq);

        // read new uploaded files
        Iterator<String> inputNameIt = mulReq.getFileNames();
        Date currentDate = new Date();
        LoginContext loginContext = (LoginContext) com.ecquaria.cloud.moh.iais.common.utils.ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        while (inputNameIt.hasNext()) {
            String inputName = inputNameIt.next();
            String itemKey = MaskUtil.unMaskValue(MASK_PARAM, inputName);
            if (itemKey != null && !itemKey.equals(inputName)) {
                List<MultipartFile> files = mulReq.getFiles(inputName);
                for (MultipartFile f : files) {

                    if (log.isInfoEnabled()) {
                        log.info("input name: {}; filename: {}", LogUtil.escapeCrlf(inputName), LogUtil.escapeCrlf(f.getOriginalFilename()));
                    }
                    if (f.isEmpty()) {
                        log.warn("File is empty, ignore it");
                    } else {
                        if(itemKey.contains(SUFFIX_TESTIMONIAL) || itemKey.contains(SUFFIX_CURRICULUM_VITAE)){
                            CertTeamNewDoc certTeamNewDoc = new CertTeamNewDoc();
                            String tmpId = inputName + f.getSize() + System.nanoTime();
                            certTeamNewDoc.setTmpId(tmpId);
                            certTeamNewDoc.setFilename(f.getOriginalFilename());
                            certTeamNewDoc.setMemberDocKey(itemKey);
                            certTeamNewDoc.setSize(f.getSize());
                            certTeamNewDoc.setSubmitDate(currentDate);
                            certTeamNewDoc.setSubmitBy(loginContext.getUserId());
                            byte[] bytes = new byte[0];
                            try {
                                bytes = f.getBytes();
                            } catch (IOException e) {
                                log.warn("Fail to read bytes for file {}, tmpId {}", f.getOriginalFilename(), tmpId);
                            }
                            ByteArrayMultipartFile multipartFile = new ByteArrayMultipartFile(f.getName(), f.getOriginalFilename(), f.getContentType(), bytes);
                            certTeamNewDoc.setMultipartFile(multipartFile);
                            this.certTeamNewDocMap.put(tmpId, certTeamNewDoc);
                        }else{
                            NewDocInfo newDocInfo = new NewDocInfo();
                            String tmpId = inputName + f.getSize() + System.nanoTime();
                            newDocInfo.setTmpId(tmpId);
                            newDocInfo.setDocType(itemKey);
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


    private void doCommonFileDelete(MultipartHttpServletRequest mulReq){
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
    }

    private void doCertifyTeamFileDelete(MultipartHttpServletRequest mulReq){
        String deleteSavedFilesString = ParamUtil.getString(mulReq, KEY_DELETED_SAVED_CERTIFY_TEAM_FILES);
        if (log.isInfoEnabled()) {
            log.info("deleteSavedFilesString: {}", LogUtil.escapeCrlf(deleteSavedFilesString));
        }
        if (StringUtils.hasLength(deleteSavedFilesString)) {
            List<String> deleteFileRepoIds = Arrays.stream(deleteSavedFilesString.split(","))
                    .map(f -> MaskUtil.unMaskValue(MASK_PARAM, f))
                    .collect(Collectors.toList());
            deleteFileRepoIds.forEach(it -> {this.certTeamSavedDocMap.remove(it); certTeamToBeDeletedRepoIds.add(it);});
        }

        String deleteNewFilesString = ParamUtil.getString(mulReq, KEY_DELETED_NEW_CERTIFY_TEAM_FILES);
        if (log.isInfoEnabled()) {
            log.info("deleteNewFilesString: {}", LogUtil.escapeCrlf(deleteNewFilesString));
        }
        if (StringUtils.hasLength(deleteNewFilesString)) {
            List<String> deleteFileTmpIds = Arrays.stream(deleteNewFilesString.split(","))
                    .map(f -> MaskUtil.unMaskValue(MASK_PARAM, f))
                    .collect(Collectors.toList());
            deleteFileTmpIds.forEach(this.certTeamNewDocMap::remove);
        }
    }
}
