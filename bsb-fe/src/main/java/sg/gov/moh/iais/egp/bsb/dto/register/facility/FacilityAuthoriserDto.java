package sg.gov.moh.iais.egp.bsb.dto.register.facility;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sg.gov.moh.iais.egp.bsb.common.multipart.ByteArrayMultipartFile;
import sg.gov.moh.iais.egp.bsb.common.node.simple.ValidatableNodeValue;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.dto.file.DocMeta;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.FileDataValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;
import sg.gov.moh.iais.egp.bsb.util.excel.CsvConvertUtil;
import sg.gov.moh.iais.egp.bsb.util.excel.ExcelConverter;
import sg.gov.moh.iais.egp.common.annotation.RfcAttributeDesc;
import sop.servlet.webflow.HttpHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FacilityAuthoriserDto extends ValidatableNodeValue {
    @Data
    @NoArgsConstructor
    public static class FacilityAuthorisedPersonnel implements Serializable {
        private String authEntityId;

        private String salutation;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.facAuthoriser.name")
        private String name;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.facAuthoriser.nationality")
        private String nationality;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.facAuthoriser.idType")
        private String idType;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.facAuthoriser.idNumber")
        private String idNumber;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.facAuthoriser.designation")
        private String designation;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.facAuthoriser.contactNo")
        private String contactNo;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.facAuthoriser.email")
        private String email;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.facAuthoriser.employmentStartDate")
        private String employmentStartDt;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.facAuthoriser.employmentPeriod")
        private String employmentPeriod;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.facAuthoriser.workArea")
        private String workArea;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.facAuthoriser.securityClearanceDate")
        private String securityClearanceDt;
    }

    @RfcAttributeDesc(aliasName = "iais.bsbfe.facAuthoriser.addOrDelete")
    private List<FacilityAuthorisedPersonnel> facAuthPersonnelList;

    private String protectedPlace;

    @JsonIgnore
    private DocRecordInfo savedFile;
    @JsonIgnore
    private NewDocInfo newFile;
    @JsonIgnore
    private String toBeDeletedRepoId;
    @JsonIgnore
    private boolean dataErrorExists;


    @JsonIgnore
    private FileDataValidationResultDto<FacilityAuthoriserFileDto> validationResultDto;

    public FacilityAuthoriserDto() {
        facAuthPersonnelList = new ArrayList<>();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean doValidation() {
        this.validationResultDto = (FileDataValidationResultDto<FacilityAuthoriserFileDto>) SpringReflectionUtils.invokeBeanMethod("facRegFeignClient", "validateFacilityAuthoriser", new Object[]{this});
        if (!this.validationResultDto.isPass()) {
            this.newFile = null;
        }
        this.dataErrorExists = this.validationResultDto.getDataErrorMap() != null && !this.validationResultDto.getDataErrorMap().isEmpty();
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



    /** Call this method if user upload a data file.
     * The sequence of these methods are:
     * 1, {@link #reqObjMapping(HttpServletRequest)}, read input's submission.
     * 2, this method, if user upload a data file.
     * 3, {@link #loadFileData()}, read data in the file to DTO list
     * 4, {@link #doValidation()}, validate DTO list
     * @return true if the file metadata is valid
     */
    public boolean validateDataFile() {
        if (this.newFile == null) {
            Map<String, String> errorMap = IaisCommonUtils.genNewHashMap(1);
            errorMap.put(DocConstants.DOC_TYPE_DATA_AUTHORISER, "This document is mandatory");
            this.validationResultDto = FileDataValidationResultDto.of(false, errorMap, null);
            return false;
        }
        DocMeta meta = new DocMeta(this.newFile.getTmpId(), this.newFile.getDocType(), this.newFile.getFilename(), this.newFile.getSize());
        ValidationResultDto fileMetaValidationResultDto =  (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("facRegFeignClient", "validateUploadedDataFileMeta", new Object[]{meta});
        this.validationResultDto = FileDataValidationResultDto.of(fileMetaValidationResultDto.isPass(), fileMetaValidationResultDto.getErrorMap(), null);
        if (!this.validationResultDto.isPass()) {
            this.newFile = null;
        }
        return this.validationResultDto.isPass();
    }

    /**
     * @see #validateDataFile() to find the usage sequence of this method
     * @return true if data of the file is read; false if fail to read the data
     */
    public boolean loadFileData() {
        if (this.newFile == null || this.validationResultDto == null || !this.validationResultDto.isPass()) {
            throw new IllegalStateException("Can not load file data");
        }
        clearValidationResult();
        String filename = this.newFile.getFilename();
        String suffix = filename.substring(filename.lastIndexOf('.') + 1);
        List<FacilityAuthoriserFileDto> data;
        try {
            if ("csv".equalsIgnoreCase(suffix)) {
                data = CsvConvertUtil.csv2List(new String(this.newFile.getMultipartFile().getBytes(), StandardCharsets.UTF_8), FacilityAuthoriserFileDto.class);
            } else {
                data = ExcelConverter.DEFAULT.excel2List(this.newFile.getMultipartFile().getBytes(), FacilityAuthoriserFileDto.class);
            }
            setFacAuthPersonnelList(FacilityAuthoriserFileDto.toProcessingDtoList(data));
            return true;
        } catch (IOException e) {
            log.error("Fail to convert EXCEL/CSV to DTOs", e);
            Map<String, String> errorMap = IaisCommonUtils.genNewHashMap(1);
            errorMap.put(DocConstants.DOC_TYPE_DATA_AUTHORISER, "Could not parse file content.");
            this.newFile = null;
            this.validationResultDto = FileDataValidationResultDto.of(false, errorMap, null);
            return false;
        }
    }

    /** Delete data file action lead to delete all data here */
    public void deleteDataFile() {
        if (this.savedFile != null) {
            this.toBeDeletedRepoId = this.savedFile.getRepoId();
            this.savedFile = null;
        }
        this.newFile = null;
        clearAuthPersonnel();
    }

    /** Get a list of committee data for display.
     * All fields are not master codes */
    public List<FacilityAuthoriserFileDto> getDataListForDisplay() {
        return FacilityAuthoriserFileDto.toDisplayDtoList(this.facAuthPersonnelList);
    }

    public NewFileSyncDto newFileSaved(String repoId) {
        DocRecordInfo docRecordInfo = new DocRecordInfo();
        docRecordInfo.setDocType(this.newFile.getDocType());
        docRecordInfo.setFilename(this.newFile.getFilename());
        docRecordInfo.setSize(this.newFile.getSize());
        docRecordInfo.setRepoId(repoId);
        docRecordInfo.setSubmitBy(this.newFile.getSubmitBy());
        docRecordInfo.setSubmitDate(this.newFile.getSubmitDate());
        this.savedFile = docRecordInfo;

        NewFileSyncDto newFileSyncDto = new NewFileSyncDto();
        newFileSyncDto.setId(repoId);
        newFileSyncDto.setData(this.newFile.getMultipartFile().getBytes());

        this.newFile = null;
        return newFileSyncDto;
    }



    public List<FacilityAuthorisedPersonnel> getFacAuthPersonnelList() {
        return new ArrayList<>(facAuthPersonnelList);
    }

    public void setFacAuthPersonnelList(List<FacilityAuthorisedPersonnel> facAuthPersonnelList) {
        this.facAuthPersonnelList = new ArrayList<>(facAuthPersonnelList);
    }

    public void clearAuthPersonnel() {
        this.facAuthPersonnelList.clear();
    }

    public void addAuthPersonnel(FacilityAuthorisedPersonnel personnel) {
        this.facAuthPersonnelList.add(personnel);
    }

    public int getAmount() {
        return this.facAuthPersonnelList.size();
    }

    public String getProtectedPlace() {
        return protectedPlace;
    }

    public void setProtectedPlace(String protectedPlace) {
        this.protectedPlace = protectedPlace;
    }

    public DocRecordInfo getSavedFile() {
        return savedFile;
    }

    public void setSavedFile(DocRecordInfo savedFile) {
        this.savedFile = savedFile;
    }

    public NewDocInfo getNewFile() {
        return newFile;
    }

    public void setNewFile(NewDocInfo newFile) {
        this.newFile = newFile;
    }

    public String getToBeDeletedRepoId() {
        return toBeDeletedRepoId;
    }

    public void setToBeDeletedRepoId(String toBeDeletedRepoId) {
        this.toBeDeletedRepoId = toBeDeletedRepoId;
    }

    public boolean isDataErrorExists() {
        return dataErrorExists;
    }

    public FileDataValidationResultDto<FacilityAuthoriserFileDto> getValidationResultDto() {
        return validationResultDto;
    }

    //    ---------------------------- request -> object ----------------------------------------------
    public void reqObjMapping(HttpServletRequest request) {
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);

        MultipartFile file = mulReq.getFile(DocConstants.DOC_TYPE_DATA_AUTHORISER);
        if (file != null && !file.isEmpty()) {
            // add new file
            LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
            NewDocInfo newDocInfo = new NewDocInfo();
            String tmpId = DocConstants.DOC_TYPE_DATA_AUTHORISER + file.getSize() + System.nanoTime();
            newDocInfo.setTmpId(tmpId);
            newDocInfo.setDocType(DocConstants.DOC_TYPE_DATA_AUTHORISER);
            newDocInfo.setFilename(file.getOriginalFilename());
            newDocInfo.setSize(file.getSize());
            newDocInfo.setSubmitDate(new Date());
            newDocInfo.setSubmitBy(loginContext.getUserId());
            byte[] bytes = new byte[0];
            try {
                bytes = file.getBytes();
            } catch (IOException e) {
                log.warn("Fail to read bytes for file {}, tmpId {}", file.getOriginalFilename(), tmpId);
            }
            ByteArrayMultipartFile multipartFile = new ByteArrayMultipartFile(file.getName(), file.getOriginalFilename(), file.getContentType(), bytes);
            newDocInfo.setMultipartFile(multipartFile);
            this.newFile = newDocInfo;

            // remove existing file if any
            if (this.savedFile != null) {
                this.toBeDeletedRepoId = this.savedFile.getRepoId();
                this.savedFile = null;
            }

            // remove current committee data
            clearAuthPersonnel();
        }
    }
}
