package sg.gov.moh.iais.egp.bsb.dto.register.facility;

import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.googlecode.jmapper.JMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sg.gov.moh.iais.egp.bsb.common.node.simple.ValidatableNodeValue;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.RefreshableDocDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.RequestObjectMappingUtil;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;
import sop.servlet.webflow.HttpHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FacilityProfileDto extends ValidatableNodeValue implements RefreshableDocDto {
    @Getter @Setter
    private List<FacilityProfileInfo> infoList;

    /* to be deleted files (which already saved), the string is repoId, used to delete file in repo.
     * This set contains all deleted files in all profile sections */
    @Getter @JsonIgnore
    private final Set<String> toBeDeletedRepoIds;

    /* Value set during service selection page */
    @Getter @Setter @JsonIgnore
    private boolean fifthRf;

    /* Value set during service selection page */
    @Getter @Setter @JsonIgnore
    private boolean pvRf;


    public FacilityProfileDto() {
        infoList = new ArrayList<>();
        FacilityProfileInfo info = new FacilityProfileInfo();
        info.setFacName("");
        infoList.add(info);

        toBeDeletedRepoIds = new HashSet<>();
    }

    @JsonIgnore
    private ValidationResultDto validationResultDto;



    @Override
    public boolean doValidation() {
        JMapper<FacilityProfileInfoValidateDto, FacilityProfileInfo> mapper = new JMapper<>(FacilityProfileInfoValidateDto.class, FacilityProfileInfo.class);
        List<FacilityProfileInfoValidateDto> validateDtoList = new ArrayList<>(infoList.size());
        for (FacilityProfileInfo info : infoList) {
            FacilityProfileInfoValidateDto validateDto = mapper.getDestination(info);
            validateDto.setMetaList(info.getAllFilesMeta());
            validateDtoList.add(validateDto);
        }
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("facRegFeignClient", "validateFacilityProfile", new Object[]{new FacilityProfileValidateDto(validateDtoList)});
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

    @Override
    public Map<String, byte[]> prepare4Saving() {
        Map<String, byte[]> all = new HashMap<>();
        for (FacilityProfileInfo info : infoList) {
            all.putAll(info.prepare4Saving());
        }
        return all;
    }

    @Override
    public void refreshAfterSave(Map<String, String> idMap) {
        infoList.forEach(i -> i.refreshAfterSave(idMap));
    }

    /**
     * Finds the new uploaded file by tmp ID.
     * This method will try to find it in any of the sections.
     * @return found NewDocInfo instance or null if not found
     */
    public NewDocInfo findNewDocByTmpId(String tmpId) {
        NewDocInfo fileInfo = null;
        for (FacilityProfileInfo info : infoList) {
            if ((fileInfo = info.getNewDocMap().get(tmpId)) != null) {
                break;
            }
        }
        return fileInfo;
    }

    /**
     * Finds the saved file by repo ID.
     * This method will try to find it in any of the sections.
     * @return found DocRecordInfo instance or null if not found
     */
    public DocRecordInfo findSavedDocByRepoId(String repoId) {
        DocRecordInfo fileInfo = null;
        for (FacilityProfileInfo info : infoList) {
            if ((fileInfo = info.getSavedDocMap().get(repoId)) != null) {
                break;
            }
        }
        return fileInfo;
    }

    public FacilityProfileInfo firstProfile() {
        return this.infoList.get(0);
    }

    public void clearProfileInfoList() {
        this.infoList.clear();
    }

    public void addProfileInfo(FacilityProfileInfo info) {
        this.infoList.add(info);
    }


    //    ---------------------------- request -> object ----------------------------------------------
    private static final String KEY_SECTION_IDXES = "sectionIdx";
    private static final String KEY_DELETED_SECTION_IDXES = "deletedSectionIdx";

    private static final String KEY_DELETED_SAVED_FILES = "deleteExistFiles";
    private static final String KEY_DELETED_NEW_FILES = "deleteNewFiles";
    private static final String MASK_PARAM = "file";


    @SneakyThrows({InstantiationException.class, IllegalAccessException.class})
    public void reqObjMapping(HttpServletRequest request) {
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String idxes = mulReq.getParameter(KEY_SECTION_IDXES);
        Set<Integer> idxSet = StringUtils.hasLength(idxes) ? Arrays.stream(idxes.trim().split(" +")).map(Integer::valueOf).collect(Collectors.toSet()) : Collections.emptySet();
        String deletedIdxes = mulReq.getParameter(KEY_DELETED_SECTION_IDXES);
        Set<Integer> deletedIdxSet = StringUtils.hasLength(deletedIdxes) ? Arrays.stream(deletedIdxes.trim().split(" +")).map(Integer::valueOf).collect(Collectors.toSet()) : Collections.emptySet();

        // record deleted saved files in any deleted sections
        recordDeletedSavedFilesInDeletedSections(deletedIdxSet);
        // record and remove user deleted files
        recordRemovedSavedFiles(mulReq);
        recordRemovedNewFiles(mulReq);

        if (idxSet.isEmpty()) {
            clearProfileInfoList();
            infoList.add(new FacilityProfileInfo());
        } else {
            Map<Integer, FacilityProfileInfo> map = RequestObjectMappingUtil.readAndReuseSectionDto(FacilityProfileInfo.class, infoList, idxSet, deletedIdxSet);
            map.forEach((k, v) -> v.reqObjMapping(request, k.toString(), isFifthRf(), isPvRf()));
            this.setInfoList(new ArrayList<>(map.values()));
        }
    }



    /**
     * If user delete a section which is already saved into DB (for example, in a RFI module).
     * Then there may be some files already saved in DB, this method will add the repo IDs to the
     * to be deleted repo IDs set.
     * <p>
     * Attention, this method will not delete the DTO for that section!
     */
    private void recordDeletedSavedFilesInDeletedSections(Collection<Integer> deletedIdxes) {
        for (int idx : deletedIdxes) {
            if (infoList.size() > idx) {
                this.toBeDeletedRepoIds.addAll(infoList.get(idx).getSavedDocMap().keySet());
            }
        }
    }

    /**
     * Removes saved files from map in specific DTO.
     * Records the removed file repo IDs.
     */
    private void recordRemovedSavedFiles(MultipartHttpServletRequest mulReq) {
        String deleteSavedFilesString = ParamUtil.getString(mulReq, KEY_DELETED_SAVED_FILES);
        if (StringUtils.hasLength(deleteSavedFilesString)) {
            Arrays.stream(deleteSavedFilesString.split(","))
                .map(f -> MaskUtil.unMaskValue(MASK_PARAM, f))
                .forEach(repoId -> {
                    for (FacilityProfileInfo info : infoList) {
                        if (info.getSavedDocMap().remove(repoId) != null) {
                            toBeDeletedRepoIds.add(repoId);
                            break;
                        }
                    }
                });
        }
    }

    /**
     * Removes new files from map in specific DTO.
     */
    private void recordRemovedNewFiles(MultipartHttpServletRequest mulReq) {
        String deleteNewFilesString = ParamUtil.getString(mulReq, KEY_DELETED_NEW_FILES);
        if (StringUtils.hasLength(deleteNewFilesString)) {
            Arrays.stream(deleteNewFilesString.split(","))
                .map(f -> MaskUtil.unMaskValue(MASK_PARAM, f))
                .forEach(tmpId -> {
                    for (FacilityProfileInfo info : infoList) {
                        if (info.getNewDocMap().remove(tmpId) != null) {
                            break;
                        }
                    }
                });
        }
    }
}
