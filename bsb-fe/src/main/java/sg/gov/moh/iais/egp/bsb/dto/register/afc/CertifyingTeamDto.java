package sg.gov.moh.iais.egp.bsb.dto.register.afc;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.shaded.com.google.common.collect.Maps;
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

/**
 *@author YiMing
 * @version 2021/10/15 14:16
 **/
@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"name", "available", "validated", "dependNodes", "validationResultDto"})
public class CertifyingTeamDto extends ValidatableNodeValue {
    @Data
    @NoArgsConstructor
    public static class CertifierTeamMember implements Serializable {
        private String memberEntityId;

        private String salutation;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.certifierTeamMember.name")
        private String name;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.certifierTeamMember.idType")
        private String idType;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.certifierTeamMember.idNo")
        private String idNumber;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.certifierTeamMember.birthDate")
        private String entityBirthDate;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.certifierTeamMember.nationality")
        private String nationality;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.certifierTeamMember.telNo")
        private String contactNo;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.certifierTeamMember.jobDesignation")
        private String jobDesignation;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.certifierTeamMember.leadCertifier")
        private String leadCertifier;

        private String role;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.certifierTeamMember.certBSL3Exp")
        private String certBSL3Exp;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.certifierTeamMember.commBSL34Exp")
        private String commBSL34Exp;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.certifierTeamMember.otherBSL34Exp")
        private String otherBSL34Exp;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.certifierTeamMember.eduBackground")
        private String highestEdu;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.certifierTeamMember.proRegAndCert")
        private String proCertifications;

        private String otherRelatedAchievement;
    }

    @RfcAttributeDesc(aliasName = "iais.bsbfe.certifierTeamMember.addOrDelete")
    private  List<CertifierTeamMember> certifierTeamMemberList;

    @JsonIgnore
    private DocRecordInfo savedFile;
    @JsonIgnore
    private NewDocInfo newFile;
    @JsonIgnore
    private String toBeDeletedRepoId;
    @JsonIgnore
    private boolean dataErrorExists;

    @JsonIgnore
    private FileDataValidationResultDto<CertifyingTeamFileDto> validationResultDto;

    public CertifyingTeamDto() {
        certifierTeamMemberList = new ArrayList<>();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean doValidation() {
        this.validationResultDto = (FileDataValidationResultDto<CertifyingTeamFileDto>) SpringReflectionUtils.invokeBeanMethod("cerRegFeignClient", "validateCertifierTeam", new Object[]{this});
        if (!this.validationResultDto.isPass()) {
            this.newFile = null;
        }
        this.dataErrorExists = !this.validationResultDto.isPass();
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

    public List<CertifierTeamMember> getCertifierTeamMemberList() {
        return certifierTeamMemberList;
    }

    public void clearCertifierTeamMemberList() {
        this.certifierTeamMemberList.clear();
    }

    public void setCertifierTeamMemberList(List<CertifyingTeamDto.CertifierTeamMember> certifierTeamMemberList) {
        this.certifierTeamMemberList = new ArrayList<>(certifierTeamMemberList);
    }

    public int getAmount() {
        return this.certifierTeamMemberList.size();
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

    public void setDataErrorExists(boolean dataErrorExists) {
        this.dataErrorExists = dataErrorExists;
    }

    public FileDataValidationResultDto<CertifyingTeamFileDto> getValidationResultDto() {
        return validationResultDto;
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
        List<CertifyingTeamFileDto> data;
        try {
            if ("csv".equalsIgnoreCase(suffix)) {
                data = CsvConvertUtil.csv2List(new String(this.newFile.getMultipartFile().getBytes(), StandardCharsets.UTF_8), CertifyingTeamFileDto.class);
            } else {
                data = ExcelConverter.DEFAULT.excel2List(this.newFile.getMultipartFile().getBytes(), CertifyingTeamFileDto.class);
            }
            setCertifierTeamMemberList(CertifyingTeamFileDto.toProcessingDtoList(data));
            return true;
        } catch (IOException e) {
            log.error("Fail to convert EXCEL/CSV to DTOs", e);
            Map<String, String> errorMap = IaisCommonUtils.genNewHashMap(1);
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
        clearCertifierTeamMemberList();
    }


    /** Get a list of committee data for display.
     * All fields are not master codes */
    public List<CertifyingTeamFileDto> getDataListForDisplay() {
        return CertifyingTeamFileDto.toDisplayDtoList(this.certifierTeamMemberList);
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



    //    ---------------------------- request -> object ----------------------------------------------


    public void reqObjMapping(HttpServletRequest request) {
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);

        MultipartFile file = mulReq.getFile(DocConstants.DOC_TYPE_DATA_CERTIFYING_TEAM);
        if (file != null && !file.isEmpty()) {
            // if no deleteFileString specified, user doesn't delete any files or upload any file, so we do nothing
            // add new file
            LoginContext loginContext = (LoginContext) com.ecquaria.cloud.moh.iais.common.utils.ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
            NewDocInfo newDocInfo = new NewDocInfo();
            String tmpId = DocConstants.DOC_TYPE_DATA_CERTIFYING_TEAM + file.getSize() + System.nanoTime();
            newDocInfo.setTmpId(tmpId);
            newDocInfo.setDocType(DocConstants.DOC_TYPE_DATA_CERTIFYING_TEAM);
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
            clearCertifierTeamMemberList();
        }
    }
}
