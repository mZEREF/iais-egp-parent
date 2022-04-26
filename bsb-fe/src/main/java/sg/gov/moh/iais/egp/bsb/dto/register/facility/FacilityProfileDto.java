package sg.gov.moh.iais.egp.bsb.dto.register.facility;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.googlecode.jmapper.JMapper;
import com.googlecode.jmapper.annotations.JGlobalMap;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sg.gov.moh.iais.egp.bsb.common.multipart.ByteArrayMultipartFile;
import sg.gov.moh.iais.egp.bsb.common.node.simple.ValidatableNodeValue;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.dto.info.common.OrgAddressInfo;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocMeta;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.common.annotation.RfcAttributeDesc;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;
import sop.servlet.webflow.HttpHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_ORG_ADDRESS;


@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FacilityProfileDto extends ValidatableNodeValue {
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JGlobalMap(excluded = {"metaList"})
    public static class FacilityProfileValidateDto {
        private String facName;
        private String facType;
        private String sameAddress;
        private String postalCode;
        private String addressType;
        private String block;
        private String floor;
        private String unitNo;
        private String streetName;
        private String building;
        private String facilityProtected;
        private List<DocMeta> metaList;
    }


    private String facilityEntityId;

    @RfcAttributeDesc(aliasName = "iais.bsbfe.facProfile.name")
    private String facName;

    private String facType;

    private String sameAddress;

    @RfcAttributeDesc(aliasName = "iais.bsbfe.facProfile.blkNo")
    private String block;

    private String addressType;

    @RfcAttributeDesc(aliasName = "iais.bsbfe.facProfile.streetName")
    private String streetName;

    @RfcAttributeDesc(aliasName = "iais.bsbfe.facProfile.floorNo")
    private String floor;

    @RfcAttributeDesc(aliasName = "iais.bsbfe.facProfile.unitNo")
    private String unitNo;

    @RfcAttributeDesc(aliasName = "iais.bsbfe.facProfile.postalCode")
    private String postalCode;

    private String building;

    @RfcAttributeDesc(aliasName = "iais.bsbfe.facProfile.isProtected")
    private String facilityProtected;


    /* docs already saved in DB, key is repoId */
    @JsonIgnore
    private Map<String, DocRecordInfo> savedDocMap;
    /* docs new uploaded, key is tmpId */
    @JsonIgnore
    private final Map<String, NewDocInfo> newDocMap;
    /* to be deleted files (which already saved), the string is repoId, used to delete file in repo */
    @JsonIgnore
    private final Set<String> toBeDeletedRepoIds;


    public FacilityProfileDto() {
        savedDocMap = new LinkedHashMap<>();
        newDocMap = new LinkedHashMap<>();
        toBeDeletedRepoIds = new HashSet<>();
    }

    @JsonIgnore
    private ValidationResultDto validationResultDto;

    public List<DocMeta> getAllFilesMeta() {
        List<DocMeta> metaDtoList = new ArrayList<>(this.savedDocMap.size() + this.newDocMap.size());
        this.savedDocMap.values().forEach(i -> {
            DocMeta docMeta = new DocMeta(i.getRepoId(), i.getDocType(), i.getFilename(), i.getSize());
            metaDtoList.add(docMeta);
        });
        this.newDocMap.values().forEach(i -> {
            DocMeta docMeta = new DocMeta(i.getTmpId(), i.getDocType(), i.getFilename(), i.getSize());
            metaDtoList.add(docMeta);
        });
        return metaDtoList;
    }

    public FacilityProfileValidateDto toValidateDto() {
        JMapper<FacilityProfileValidateDto, FacilityProfileDto> jMapper = new JMapper<>(FacilityProfileValidateDto.class, FacilityProfileDto.class);
        FacilityProfileValidateDto validateDto = jMapper.getDestinationWithoutControl(this);
        validateDto.setMetaList(getAllFilesMeta());
        return validateDto;
    }

    @Override
    public boolean doValidation() {
        FacilityProfileValidateDto validateDto = toValidateDto();
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("facRegFeignClient", "validateFacilityProfile", new Object[]{validateDto});
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
        newDocMap.clear();
        return newFileSyncDtoList;
    }


    public String getFacilityEntityId() {
        return facilityEntityId;
    }

    public void setFacilityEntityId(String facilityEntityId) {
        this.facilityEntityId = facilityEntityId;
    }

    public String getFacName() {
        return facName;
    }

    public void setFacName(String facName) {
        this.facName = facName;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getUnitNo() {
        return unitNo;
    }

    public void setUnitNo(String unitNo) {
        this.unitNo = unitNo;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getFacilityProtected() {
        return facilityProtected;
    }

    public void setFacilityProtected(String facilityProtected) {
        this.facilityProtected = facilityProtected;
    }

    public Map<String, DocRecordInfo> getSavedDocMap() {
        return savedDocMap;
    }

    public void setSavedDocMap(Map<String, DocRecordInfo> savedDocMap) {
        this.savedDocMap = savedDocMap;
    }

    public Map<String, NewDocInfo> getNewDocMap() {
        return newDocMap;
    }

    public Set<String> getToBeDeletedRepoIds() {
        return toBeDeletedRepoIds;
    }

    public String getFacType() {
        return facType;
    }

    public void setFacType(String facType) {
        this.facType = facType;
    }

    public String getSameAddress() {
        return sameAddress;
    }

    public void setSameAddress(String sameAddress) {
        this.sameAddress = sameAddress;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    //    ---------------------------- request -> object ----------------------------------------------
    private static final String KEY_FAC_NAME = "facName";
    private static final String KEY_FACILITY_TYPE = "facType";
    private static final String KEY_IS_SAME_ADDRESS_AS_COMPANY = "isSameAddress";
    private static final String KEY_BLOCK = "block";
    private static final String KEY_ADDRESS_TYPE = "addressType";
    private static final String KEY_STREET_NAME = "streetName";
    private static final String KEY_FLOOR = "floor";
    private static final String KEY_UNIT_NO = "unitNo";
    private static final String KEY_POSTAL_CODE = "postalCode";
    private static final String KEY_BUILDING_NAME = "buildingName";
    private static final String KEY_IS_PROTECTED_PLACE = "protectedPlace";

    private static final String MASK_PARAM = "file";
    private static final String KEY_DELETED_SAVED_FILES = "deleteExistFiles";
    private static final String KEY_DELETED_NEW_FILES = "deleteNewFiles";
    private static final String KEY_GAZETTE = "gazetteOrder";

    public void reqObjMapping(HttpServletRequest request) {
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);

        this.setFacName(ParamUtil.getString(mulReq, KEY_FAC_NAME));
        this.setFacType(ParamUtil.getString(mulReq,KEY_FACILITY_TYPE));

        String sameAddressAsCompany = ParamUtil.getString(mulReq,KEY_IS_SAME_ADDRESS_AS_COMPANY);
        if (MasterCodeConstants.YES.equals(sameAddressAsCompany)) {
            if (!sameAddressAsCompany.equals(this.sameAddress)) {
                // load company address and set to this DTO
                OrgAddressInfo orgAddressInfo = (OrgAddressInfo) request.getSession().getAttribute(KEY_ORG_ADDRESS);
                this.setPostalCode(orgAddressInfo.getPostalCode());
                this.setAddressType(orgAddressInfo.getAddressType());
                this.setBlock(orgAddressInfo.getBlockNo());
                this.setFloor(orgAddressInfo.getFloor());
                this.setUnitNo(orgAddressInfo.getUnitNo());
                this.setStreetName(orgAddressInfo.getStreet());
                this.setBuilding(orgAddressInfo.getBuilding());
            }
            // do nothing, if current address is already the same as company
        } else if (MasterCodeConstants.NO.equals(sameAddressAsCompany)) {
            this.setAddressType(ParamUtil.getString(mulReq,KEY_ADDRESS_TYPE));
            this.setBlock(ParamUtil.getString(mulReq, KEY_BLOCK));
            this.setStreetName(ParamUtil.getString(mulReq, KEY_STREET_NAME));
            this.setFloor(ParamUtil.getString(mulReq, KEY_FLOOR));
            this.setUnitNo(ParamUtil.getString(mulReq, KEY_UNIT_NO));
            this.setPostalCode(ParamUtil.getString(mulReq, KEY_POSTAL_CODE));
            this.setBuilding(ParamUtil.getString(mulReq,KEY_BUILDING_NAME));
        }
        this.setSameAddress(sameAddressAsCompany);

        this.setFacilityProtected(ParamUtil.getString(mulReq, KEY_IS_PROTECTED_PLACE));

        String deleteSavedFilesString = ParamUtil.getString(mulReq, KEY_DELETED_SAVED_FILES);
        String deleteNewFilesString = ParamUtil.getString(mulReq, KEY_DELETED_NEW_FILES);
        if (log.isInfoEnabled()) {
            log.info("deleteSavedFilesString: {}", LogUtil.escapeCrlf(deleteSavedFilesString));
            log.info("deleteNewFilesString: {}", LogUtil.escapeCrlf(deleteNewFilesString));
        }
        if (StringUtils.hasLength(deleteSavedFilesString)) {
            List<String> deleteFileRepoIds = Arrays.stream(deleteSavedFilesString.split(","))
                    .map(f -> MaskUtil.unMaskValue(MASK_PARAM, f))
                    .collect(Collectors.toList());
            deleteFileRepoIds.forEach(it -> {this.savedDocMap.remove(it); toBeDeletedRepoIds.add(it);});
        }
        if (StringUtils.hasLength(deleteNewFilesString)) {
            List<String> deleteFileTmpIds = Arrays.stream(deleteNewFilesString.split(","))
                    .map(f -> MaskUtil.unMaskValue(MASK_PARAM, f))
                    .collect(Collectors.toList());
            deleteFileTmpIds.forEach(this.newDocMap::remove);
        }

        // read new uploaded files
        if (MasterCodeConstants.YES.equals(this.facilityProtected)) {
            List<MultipartFile> files = mulReq.getFiles(KEY_GAZETTE);
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
                        String tmpId = DocConstants.DOC_TYPE_GAZETTE_ORDER + f.getSize() + System.nanoTime();
                        newDocInfo.setTmpId(tmpId);
                        newDocInfo.setDocType(DocConstants.DOC_TYPE_GAZETTE_ORDER);
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
        } else {
            log.info("This place is not protected, won't read attachments");
        }
    }
}
