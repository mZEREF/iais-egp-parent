package sg.gov.moh.iais.egp.bsb.dto.inspection.afc;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sg.gov.moh.iais.egp.bsb.common.multipart.ByteArrayMultipartFile;
import sg.gov.moh.iais.egp.bsb.common.node.simple.ValidatableNodeValue;
import sg.gov.moh.iais.egp.bsb.dto.file.DocMeta;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sop.servlet.webflow.HttpHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_REVIEW_AFC_REPORT_DTO;


@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AFCCommonDocDto extends ValidatableNodeValue {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DocsMetaDto implements Serializable {
        private Map<String, List<DocMeta>> metaDtoMap;
    }

    /* docs already saved in DB, key is repoId */
    private Map<String, CertificationDocDisPlayDto> savedDocMap;
    /* docs new uploaded, key is tmpId */
    private final Map<String, CertificationDocDto> newDocMap;
    /* to be deleted files (which already saved), the string is repoId, used to delete file in repo */
    private final Set<String> toBeDeletedRepoIds;



    @JsonIgnore
    private ValidationResultDto validationResultDto;


    public AFCCommonDocDto() {
        savedDocMap = new LinkedHashMap<>();
        newDocMap = new LinkedHashMap<>();
        toBeDeletedRepoIds = new HashSet<>();
    }

    @Override
    public boolean doValidation() {
        return true;
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

    public List<DocMeta> convertToDocMetaList() {
        List<DocMeta> metaDtoList = new ArrayList<>(this.savedDocMap.size() + this.newDocMap.size());
        this.savedDocMap.values().forEach(i -> {
            DocMeta docMeta = new DocMeta(i.getRepoId(), i.getDocType(), i.getDocName(), i.getSize());
            metaDtoList.add(docMeta);
        });
        this.newDocMap.values().forEach(i -> {
            DocMeta docMeta = new DocMeta(i.getDisPlayDto().getRepoId(), i.getDisPlayDto().getDocType(), i.getDisPlayDto().getDocName(), i.getDisPlayDto().getSize());
            metaDtoList.add(docMeta);
        });
        return metaDtoList;
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
        Iterator<CertificationDocDto> newDocIt = newDocMap.values().iterator();

        List<NewFileSyncDto> newFileSyncDtoList = new ArrayList<>(repoIds.size());
        while (repoIdIt.hasNext() && newDocIt.hasNext()) {
            String repoId = repoIdIt.next();
            CertificationDocDto newDocInfo = newDocIt.next();

            CertificationDocDisPlayDto disPlayDto = new CertificationDocDisPlayDto();
            disPlayDto.setRepoId(repoId);
            disPlayDto.setDocName(newDocInfo.getDisPlayDto().getDocName());
            disPlayDto.setDocType(newDocInfo.getDisPlayDto().getDocType());
            disPlayDto.setUploadBy(newDocInfo.getDisPlayDto().getUploadBy());
            disPlayDto.setUploadDate(newDocInfo.getDisPlayDto().getUploadDate());
            disPlayDto.setSize(newDocInfo.getDisPlayDto().getSize());
            disPlayDto.setRoundOfReview(newDocInfo.getDisPlayDto().getRoundOfReview());
            disPlayDto.setAfcMarkFinal(newDocInfo.getDisPlayDto().getAfcMarkFinal());
            disPlayDto.setApplicantMarkFinal(newDocInfo.getDisPlayDto().getApplicantMarkFinal());
            disPlayDto.setMohMarkFinal(newDocInfo.getDisPlayDto().getMohMarkFinal());
            disPlayDto.setUserDisplayName(newDocInfo.getDisPlayDto().getUserDisplayName());

            savedDocMap.put(repoId, disPlayDto);
            NewFileSyncDto newFileSyncDto = new NewFileSyncDto();
            newFileSyncDto.setId(repoId);
            newFileSyncDto.setData(newDocInfo.getMultipartFile().getBytes());
            newFileSyncDtoList.add(newFileSyncDto);
        }
        newDocMap.clear();
        return newFileSyncDtoList;
    }

    public Map<String, CertificationDocDisPlayDto> getSavedDocMap() {
        return savedDocMap;
    }

    public void setSavedDocMap(Map<String, CertificationDocDisPlayDto> savedDocMap) {
        this.savedDocMap = savedDocMap;
    }

    public Map<String, CertificationDocDto> getNewDocMap() {
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
        ReviewAFCReportDto dto = (ReviewAFCReportDto) ParamUtil.getSessionAttr(request, KEY_REVIEW_AFC_REPORT_DTO);
        if (dto == null) {
            dto = new ReviewAFCReportDto();
        }
        if(StringUtils.isEmpty(dto.getMaxRound())){
            dto.setMaxRound(0);
        }
        while (inputNameIt.hasNext()) {
            String inputName = inputNameIt.next();
            String docType = ParamUtil.getString(request,"docType");
            List<MultipartFile> files = mulReq.getFiles(inputName);
            for (MultipartFile f : files) {
                if (log.isInfoEnabled()) {
                    log.info("input name: {}; filename: {}", LogUtil.escapeCrlf(inputName), LogUtil.escapeCrlf(f.getOriginalFilename()));
                }
                if (f.isEmpty()) {
                    log.warn("File is empty, ignore it");
                } else {
                    CertificationDocDto newDocInfo = new CertificationDocDto();
                    CertificationDocDisPlayDto docDisPlayDto = new CertificationDocDisPlayDto();
                    String tmpId = inputName + f.getSize() + System.nanoTime();
                    docDisPlayDto.setRepoId(tmpId);
                    docDisPlayDto.setMaskedRepoId(MaskUtil.maskValue("file",tmpId));
                    docDisPlayDto.setDocType(docType);
                    docDisPlayDto.setDocName(f.getOriginalFilename());
                    docDisPlayDto.setSize(f.getSize());
                    docDisPlayDto.setUploadDate(currentDate);
                    docDisPlayDto.setUploadBy(loginContext.getUserId());
                    docDisPlayDto.setUserDisplayName(loginContext.getUserName());
                    docDisPlayDto.setRoundOfReview(dto.getMaxRound()+1);
                    newDocInfo.setDisPlayDto(docDisPlayDto);
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
