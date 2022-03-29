package sg.gov.moh.iais.egp.bsb.dto.register.facility;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.shaded.com.google.common.collect.Maps;
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
import sg.gov.moh.iais.egp.bsb.dto.validation.FileDataValidationResultDto;
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
public class FacilityCommitteeDto extends ValidatableNodeValue {
    @Data
    @NoArgsConstructor
    public static class BioSafetyCommitteePersonnel implements Serializable {
        private String committeeEntityId;

        private String salutation;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.facilityCommittee.name")
        private String name;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.facilityCommittee.nationality")
        private String nationality;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.facilityCommittee.idType")
        private String idType;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.facilityCommittee.idNumber")
        private String idNumber;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.facilityCommittee.designation")
        private String designation;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.facilityCommittee.contactNo")
        private String contactNo;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.facilityCommittee.email")
        private String email;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.facilityCommittee.employmentStartDate")
        private String employmentStartDt;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.facilityCommittee.expertiseArea")
        private String expertiseArea;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.facilityCommittee.role")
        private String role;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.facilityCommittee.employee")
        private String employee;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.facilityCommittee.externalCompName")
        private String externalCompName;
    }

    @RfcAttributeDesc(aliasName = "iais.bsbfe.facilityCommittee.addOrDelete")
    private List<BioSafetyCommitteePersonnel> facCommitteePersonnelList;


    @JsonIgnore
    private DocRecordInfo savedFile;
    @JsonIgnore
    private NewDocInfo newFile;
    @JsonIgnore
    private String toBeDeletedRepoId;
    @JsonIgnore
    private boolean dataErrorExists;


    @JsonIgnore
    private FileDataValidationResultDto<FacilityCommitteeFileDto> validationResultDto;


    public FacilityCommitteeDto() {
        facCommitteePersonnelList = new ArrayList<>();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean doValidation() {
        this.validationResultDto = (FileDataValidationResultDto<FacilityCommitteeFileDto>) SpringReflectionUtils.invokeBeanMethod("facRegFeignClient", "validateFacilityCommittee", new Object[]{this});
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
            Map<String, String> errorMap = Maps.newHashMapWithExpectedSize(1);
            errorMap.put(DocConstants.DOC_TYPE_DATA_COMMITTEE, "This document is mandatory");
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
        List<FacilityCommitteeFileDto> data;
        try {
            if ("csv".equalsIgnoreCase(suffix)) {
                data = CsvConvertUtil.csv2List(new String(this.newFile.getMultipartFile().getBytes(), StandardCharsets.UTF_8), FacilityCommitteeFileDto.class);
            } else {
                data = ExcelConverter.DEFAULT.excel2List(this.newFile.getMultipartFile().getBytes(), FacilityCommitteeFileDto.class);
            }
            setFacCommitteePersonnelList(FacilityCommitteeFileDto.toProcessingDtoList(data));
            return true;
        } catch (IOException e) {
            log.error("Fail to convert EXCEL/CSV to DTOs", e);
            Map<String, String> errorMap = Maps.newHashMapWithExpectedSize(1);
            errorMap.put(DocConstants.DOC_TYPE_DATA_COMMITTEE, "Could not parse file content.");
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
        clearCommitteePersonnel();
    }

    /** Get a list of committee data for display.
     * All fields are not master codes */
    public List<FacilityCommitteeFileDto> getDataListForDisplay() {
        return FacilityCommitteeFileDto.toDisplayDtoList(this.facCommitteePersonnelList);
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


    public List<BioSafetyCommitteePersonnel> getFacCommitteePersonnelList() {
        return new ArrayList<>(facCommitteePersonnelList);
    }

    public void clearCommitteePersonnel() {
        this.facCommitteePersonnelList.clear();
    }

    public void addCommitteePersonnel(BioSafetyCommitteePersonnel personnel) {
        this.facCommitteePersonnelList.add(personnel);
    }

    public void setFacCommitteePersonnelList(List<BioSafetyCommitteePersonnel> facCommitteePersonnelList) {
        this.facCommitteePersonnelList = new ArrayList<>(facCommitteePersonnelList);
    }

    public int getAmount() {
        return this.facCommitteePersonnelList.size();
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



    public FileDataValidationResultDto<FacilityCommitteeFileDto> getValidationResultDto() {
        return validationResultDto;
    }

    //    ---------------------------- request -> object ----------------------------------------------
    public void reqObjMapping(HttpServletRequest request) {
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);

        MultipartFile file = mulReq.getFile(DocConstants.DOC_TYPE_DATA_COMMITTEE);
        if (file != null && !file.isEmpty()) {
            // add new file
            LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
            NewDocInfo newDocInfo = new NewDocInfo();
            String tmpId = DocConstants.DOC_TYPE_DATA_COMMITTEE + file.getSize() + System.nanoTime();
            newDocInfo.setTmpId(tmpId);
            newDocInfo.setDocType(DocConstants.DOC_TYPE_DATA_COMMITTEE);
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
            clearCommitteePersonnel();
        }
    }
}
