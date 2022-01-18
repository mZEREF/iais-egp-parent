package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.action.HcsaFileAjaxController;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.HusbandDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeView;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.ExcelPropertyDto;
import com.ecquaria.cloud.moh.iais.dto.FileErrorMsg;
import com.ecquaria.cloud.moh.iais.dto.PageShowFileDto;
import com.ecquaria.cloud.moh.iais.dto.PatientInfoExcelDto;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.DsRfcHelper;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.helper.excel.IrregularExcelWriterUtil;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.PatientService;
import com.ecquaria.cloud.moh.iais.utils.SingeFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Process: MohARPatientInfoUpload
 *
 * @Description PatientUploadDelegate
 * @Auther chenlei on 11/23/2021.
 */
@Delegator("patientUploadDelegate")
@Slf4j
public class PatientUploadDelegate {

    protected static final String ACTION_TYPE_PAGE = "page";
    protected static final String ACTION_TYPE_PREVIEW = "preview";

    private static final String FILE_ITEM_SIZE = "fileItemSize";

    private static final String PATIENT_INFO_LIST = "PATIENT_INFO_LIST";
    private static final String PAGE_SHOW_FILE = "showPatientFile";
    private static final String FILE_APPEND = "uploadFile";
    private static final String SEESION_FILES_MAP_AJAX = HcsaFileAjaxController.SEESION_FILES_MAP_AJAX + FILE_APPEND;

    @Autowired
    private PatientService patientService;

    @Autowired
    private ArDataSubmissionService arDataSubmissionService;

    /**
     * Step: Start
     *
     * @param bpc
     */
    public void doStart(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("-----PatientUploadDelegate Start -----"));
        clearSession(bpc.request);
    }

    private void clearSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute(SEESION_FILES_MAP_AJAX);
        session.removeAttribute(PATIENT_INFO_LIST);
        session.removeAttribute(PAGE_SHOW_FILE);
        session.removeAttribute(DataSubmissionConstant.AR_DATA_LIST);
    }

    /**
     * Step: PrepareSwitch
     *
     * @param bpc
     */
    public void prepareSwitch(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("----- PrepareSwitch -----"));
        ParamUtil.setRequestAttr(bpc.request, "title", DataSubmissionHelper.getMainTitle(DataSubmissionConstant.DS_TITLE_NEW));
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", DataSubmissionHelper.getSmallTitle(DataSubmissionConsts.DS_AR));
        String actionType = (String) ParamUtil.getRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
        log.info(StringUtil.changeForLog("----- Action Type: " + actionType + " -----"));
        if (StringUtil.isEmpty(actionType)) {
            actionType = ACTION_TYPE_PAGE;
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, actionType);
        }
    }

    /**
     * Step: PreparePage
     *
     * @param bpc
     */
    public void preparePage(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("----- PreparePage -----"));
        preapreDate(DataSubmissionConstant.PAGE_STAGE_PAGE, bpc.request);

    }

    private void preapreDate(String pageStage, HttpServletRequest request) {
        Map<String, String> maxCountMap = IaisCommonUtils.genNewHashMap(1);
        maxCountMap.put("maxCount", Formatter.formatNumber(DataSubmissionHelper.getFileRecordMaxNumber(), "#,##0"));
        ParamUtil.setRequestAttr(request, "maxCountMap", maxCountMap);
        ParamUtil.setRequestAttr(request, DataSubmissionConstant.CURRENT_PAGE_STAGE, pageStage);
        Integer fileItemSize = (Integer) request.getAttribute(FILE_ITEM_SIZE);
        if (fileItemSize == null) {
            List<PatientInfoDto> patientInfoList = (List<PatientInfoDto>) request.getSession().getAttribute(PATIENT_INFO_LIST);
            fileItemSize = patientInfoList != null ? patientInfoList.size() : 0;
            ParamUtil.setRequestAttr(request, FILE_ITEM_SIZE, fileItemSize);
        }
    }

    /**
     * Step: PageAction
     *
     * @param bpc
     */
    public void doPageAction(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("----- PageAction -----"));
        String crudype = ParamUtil.getString(bpc.request, DataSubmissionConstant.CRUD_TYPE);
        if (StringUtil.isIn(crudype, new String[]{"return", "back"})) {
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "return");
            clearSession(bpc.request);
            return;
        }
        int fileItemSize = 0;
        Map<String, String> errorMap = null;
        boolean hasItems = AppConsts.YES.equals(ParamUtil.getString(bpc.request, "hasItems"));
        log.info(StringUtil.changeForLog("---- Has Items: " + hasItems + " ----"));
        List<PatientInfoDto> patientInfoList = (List<PatientInfoDto>) bpc.request.getSession().getAttribute(PATIENT_INFO_LIST);
        if (patientInfoList == null || !hasItems) {
            // upload file (first time / error)
            Entry<String, File> fileEntry = getFileEntry(bpc.request);
            PageShowFileDto pageShowFileDto = getPageShowFileDto(fileEntry);
            ParamUtil.setSessionAttr(bpc.request, PAGE_SHOW_FILE, pageShowFileDto);
            errorMap = DataSubmissionHelper.validateFile(SEESION_FILES_MAP_AJAX, bpc.request);
            if (errorMap.isEmpty()) {
                List<PatientInfoExcelDto> patientInfoExcelDtoList = getPatientInfoExcelDtoList(fileEntry);
                fileItemSize = patientInfoExcelDtoList.size();
                if (fileItemSize == 0) {
                    errorMap.put("uploadFileError", "PRF_ERR006");
                } else if (fileItemSize > DataSubmissionHelper.getFileRecordMaxNumber()) {
                    errorMap.put("uploadFileError", MessageUtil.replaceMessage("GENERAL_ERR0052",
                            Formatter.formatNumber(DataSubmissionHelper.getFileRecordMaxNumber(), "#,##0"), "maxCount"));
                } else {
                    String orgId = DataSubmissionHelper.getLoginContext(bpc.request).getOrgId();
                    patientInfoList = getPatientInfoList(patientInfoExcelDtoList, orgId);
                    Map<String, ExcelPropertyDto> fieldCellMap = DataSubmissionHelper.getFieldCellMap(PatientInfoExcelDto.class);
                    List<FileErrorMsg> errorMsgs = DataSubmissionHelper.validateExcelList(patientInfoList, "file", fieldCellMap);
                    List<PatientDto> patientDtos = patientInfoList.stream()
                            .map(PatientInfoDto::getPatient)
                            .collect(Collectors.toList());
                    for (int i = 1; i < fileItemSize; i++) {
                        if (duplicate(patientDtos.get(i), patientDtos.subList(0, i))) {
                            errorMsgs.add(new FileErrorMsg(DataSubmissionHelper.getRow(i), fieldCellMap.get("idNumber"),
                                    "GENERAL_ERR0053"));
                        }
                    }
                    if (!errorMsgs.isEmpty()) {
                        Collections.sort(errorMsgs, Comparator.comparing(FileErrorMsg::getRow).thenComparing(FileErrorMsg::getCol));
                        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.FILE_ITEM_ERROR_MSGS, errorMsgs);
                        errorMap.put("itemError", "itemError");
                    }
                }
            }
            //crudype = ACTION_TYPE_PAGE;
        } else {
            // To submission
            // crudype = "submission";
        }
        log.info(StringUtil.changeForLog("---- File Item Size: " + fileItemSize + " ----"));
        if (errorMap != null && !errorMap.isEmpty()) {
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            clearSession(bpc.request);
            fileItemSize = 0;
            crudype = ACTION_TYPE_PAGE;
        } else {
            if (patientInfoList != null) {
                fileItemSize = patientInfoList.size();
            }
            bpc.request.getSession().setAttribute(PATIENT_INFO_LIST, patientInfoList);
            crudype = ACTION_TYPE_PREVIEW;
        }
        ParamUtil.setRequestAttr(bpc.request, FILE_ITEM_SIZE, fileItemSize);
        log.info(StringUtil.changeForLog("---- Action Type: " + crudype + " ----"));
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, crudype);
    }

    /**
     * Check duplication for all records in file
     *
     * @param patient
     * @param patientDtos
     * @return
     */
    private boolean duplicate(PatientDto patient, List<PatientDto> patientDtos) {
        if (StringUtil.isEmpty(patient.getIdType()) || StringUtil.isEmpty(patient.getIdNumber()) || StringUtil.isEmpty(
                patient.getNationality())) {
            return false;
        }
        return patientDtos.stream().anyMatch(dto -> Objects.equals(patient.getIdNumber(), dto.getIdNumber())
                && Objects.equals(patient.getIdType(), dto.getIdType())
                && Objects.equals(patient.getNationality(), dto.getNationality()));
    }

    /**
     * Transfer to patient info dto from patient info excel dto
     * And map value to code for some fields (drowndrop)
     *
     * @param patientInfoExcelDtoList
     * @param orgId
     * @return
     */
    private List<PatientInfoDto> getPatientInfoList(List<PatientInfoExcelDto> patientInfoExcelDtoList, String orgId) {
        if (patientInfoExcelDtoList == null) {
            return null;
        }
        List<MasterCodeView> idTypes = MasterCodeUtil.retrieveByCategory(MasterCodeUtil.CATE_ID_DS_ID_TYPE);
        List<MasterCodeView> nationalities = MasterCodeUtil.retrieveByCategory(MasterCodeUtil.CATE_ID_NATIONALITY);
        List<MasterCodeView> groups = MasterCodeUtil.retrieveByCategory(MasterCodeUtil.CATE_ID_ETHNIC_GROUP);
        List<PatientInfoDto> result = IaisCommonUtils.genNewArrayList(patientInfoExcelDtoList.size());
        for (PatientInfoExcelDto patientInfoExcelDto : patientInfoExcelDtoList) {
            PatientInfoDto dto = new PatientInfoDto();
            PatientDto patient = MiscUtil.transferEntityDto(patientInfoExcelDto, PatientDto.class);
            patient.setBirthDate(IaisCommonUtils.handleDate(patient.getBirthDate()));
            patient.setIdType(DataSubmissionHelper.getCode(patientInfoExcelDto.getIdType(), idTypes));
            patient.setNationality(DataSubmissionHelper.getCode(patientInfoExcelDto.getNationality(), nationalities));
            patient.setEthnicGroup(DataSubmissionHelper.getCode(patientInfoExcelDto.getEthnicGroup(), groups));
            patient.setPreviousIdentification("YES".equals(patientInfoExcelDto.getIsPreviousIdentification()));
            patient.setOrgId(orgId);
            DsRfcHelper.handlePatient(patient);
            dto.setPatient(patient);
            dto.setIsPreviousIdentification(patientInfoExcelDto.getIsPreviousIdentification());
            if (patient.isPreviousIdentification()) {
                String preIdType = DataSubmissionHelper.getCode(patientInfoExcelDto.getPreIdType(), idTypes);
                String preIdNumber = patientInfoExcelDto.getPreIdNumber();
                String preNationality = DataSubmissionHelper.getCode(patientInfoExcelDto.getPreNationality(), nationalities);
                PatientDto previous = new PatientDto();
                previous.setIdType(preIdType);
                previous.setIdNumber(preIdNumber);
                previous.setNationality(preNationality);
                PatientDto db = patientService.getActiveArPatientByConds(preIdType, preIdNumber, preNationality, orgId);
                if (db != null) {
                    previous = db;
                }
                dto.setPrevious(previous);
            }
            HusbandDto husbandDto = new HusbandDto();
            husbandDto.setName(patientInfoExcelDto.getNameHbd());
            husbandDto.setIdType(DataSubmissionHelper.getCode(patientInfoExcelDto.getIdTypeHbd(), idTypes));
            husbandDto.setIdNumber(patientInfoExcelDto.getIdNumberHbd());
            husbandDto.setNationality(DataSubmissionHelper.getCode(patientInfoExcelDto.getNationalityHbd(), nationalities));
            husbandDto.setBirthDate(IaisCommonUtils.handleDate(patientInfoExcelDto.getBirthDateHbd()));
            husbandDto.setEthnicGroup(DataSubmissionHelper.getCode(patientInfoExcelDto.getEthnicGroupHbd(), groups));
            DsRfcHelper.handleHusband(husbandDto);
            dto.setHusband(husbandDto);
            result.add(dto);
        }
        return result;
    }

    private List<PatientInfoExcelDto> getPatientInfoExcelDtoList(Entry<String, File> fileEntry) {
        if (fileEntry == null) {
            return IaisCommonUtils.genNewArrayList(0);
        }
        try {
            File file = fileEntry.getValue();
            if (FileUtils.isExcel(file.getName())) {
                return FileUtils.transformToJavaBean(fileEntry.getValue(), PatientInfoExcelDto.class, true);
            } else if (FileUtils.isCsv(file.getName())) {
                return FileUtils.transformCsvToJavaBean(fileEntry.getValue(), PatientInfoExcelDto.class, true);
            }
        } catch (Exception e) {
            log.error(StringUtil.changeForLog(e.getMessage()), e);
        }
        return IaisCommonUtils.genNewArrayList(0);
    }

    private PageShowFileDto getPageShowFileDto(Entry<String, File> fileEntry) {
        if (fileEntry == null) {
            return null;
        }
        File file = fileEntry.getValue();
        PageShowFileDto pageShowFileDto = new PageShowFileDto();
        String index = fileEntry.getKey().substring(FILE_APPEND.length());
        SingeFileUtil singeFileUtil = SingeFileUtil.getInstance();
        String fileMd5 = singeFileUtil.getFileMd5(file);
        pageShowFileDto.setIndex(index);
        pageShowFileDto.setFileName(file.getName());
        pageShowFileDto.setFileMapId(FILE_APPEND + "Div" + index);
        pageShowFileDto.setSize((int) (file.length() / 1024));
        pageShowFileDto.setMd5Code(fileMd5);
        List<String> list = arDataSubmissionService.saveFileRepo(Collections.singletonList(file));
        if (!list.isEmpty()) {
            pageShowFileDto.setFileUploadUrl(list.get(0));
        }
        return pageShowFileDto;
    }

    private Entry<String, File> getFileEntry(HttpServletRequest request) {
        Map<String, File> fileMap = (Map<String, File>) ParamUtil.getSessionAttr(request, SEESION_FILES_MAP_AJAX);
        if (fileMap == null || fileMap.isEmpty()) {
            return null;
        }
        // only one
        Iterator<Entry<String, File>> iterator = fileMap.entrySet().iterator();
        if (!iterator.hasNext()) {
            return null;
        }
        Entry<String, File> next = iterator.next();
        File file = next.getValue();
        long length = file.length();
        if (length == 0) {
            return null;
        }
        return next;
    }

    /**
     * Step: PreparePreview
     *
     * @param bpc
     */
    public void preparePreview(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("----- PreparePreview -----"));
        preapreDate(DataSubmissionConstant.PAGE_STAGE_PREVIEW, bpc.request);
    }

    /**
     * Step: DoPreview
     *
     * @param bpc
     */
    public void doPreview(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("----- DoPreview -----"));
        // declaration
        String declaration = ParamUtil.getString(bpc.request, "declaration");
        ArSuperDataSubmissionDto arSuperDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        DataSubmissionDto dataSubmissionDto = arSuperDataSubmission.getDataSubmissionDto();
        dataSubmissionDto.setDeclaration(declaration);
        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmission, bpc.request);
        String crudype = ParamUtil.getString(bpc.request, DataSubmissionConstant.CRUD_TYPE);
        if (StringUtil.isIn(crudype, new String[]{ACTION_TYPE_PAGE, "back"})) {
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, ACTION_TYPE_PAGE);
            return;
        }
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap(1);
        if (StringUtil.isEmpty(declaration)) {
            errorMap.put("declaration", "GENERAL_ERR0006");
        }
        if (!errorMap.isEmpty()) {
            log.error("------No checked for declaration-----");
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, ACTION_TYPE_PREVIEW);
        }
    }

    /**
     * Step: Submission
     *
     * @param bpc
     */
    public void doSubmission(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("----- Submission -----"));
        List<PatientInfoDto> patientInfoList = (List<PatientInfoDto>) bpc.request.getSession().getAttribute(PATIENT_INFO_LIST);
        if (patientInfoList == null || patientInfoList.isEmpty()) {
            log.warn(StringUtil.changeForLog("----- No Data to be submitted -----"));
            return;
        }
        boolean useParallel = patientInfoList.size() >= AppConsts.DFT_MIN_PARALLEL_SIZE;
        ArSuperDataSubmissionDto arSuperDto = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        String declaration = arSuperDto.getDataSubmissionDto().getDeclaration();
        List<ArSuperDataSubmissionDto> arSuperList = StreamSupport.stream(patientInfoList.spliterator(), useParallel)
                .map(dto -> {
                    ArSuperDataSubmissionDto newDto = DataSubmissionHelper.reNew(arSuperDto);
                    newDto.setFe(true);
                    DataSubmissionDto dataSubmissionDto = newDto.getDataSubmissionDto();
                    String submissionNo = arDataSubmissionService.getSubmissionNo(newDto.getSelectionDto(),
                            DataSubmissionConsts.DS_AR);
                    dataSubmissionDto.setSubmitBy(DataSubmissionHelper.getLoginContext(bpc.request).getUserId());
                    dataSubmissionDto.setSubmitDt(new Date());
                    dataSubmissionDto.setSubmissionNo(submissionNo);
                    dataSubmissionDto.setDeclaration(declaration);
                    newDto.setDataSubmissionDto(dataSubmissionDto);
                    PatientDto patient = dto.getPatient();
                    String patientCode = patient.getPatientCode();
                    if (dto.getPrevious() != null && !StringUtil.isEmpty(dto.getPatient().getPatientCode())) {
                        patientCode = dto.getPatient().getPatientCode();
                    }
                    patient.setPatientCode(patientService.getPatientCode(patientCode));
                    patient.setPatientType(DataSubmissionConsts.DS_PATIENT_ART);
                    dto.setPatient(patient);
                    newDto.setPatientInfoDto(dto);
                    return newDto;
                })
                .collect(Collectors.toList());
        if (useParallel) {
            Collections.sort(arSuperList, Comparator.comparing(dto -> dto.getDataSubmissionDto().getSubmissionNo()));
        }
        arSuperList = arDataSubmissionService.saveArSuperDataSubmissionDtoList(arSuperList);
        try {
            arSuperList = arDataSubmissionService.saveArSuperDataSubmissionDtoListToBE(arSuperList);
        } catch (Exception e) {
            log.error(StringUtil.changeForLog("The Eic saveArSuperDataSubmissionDtoToBE failed ===>" + e.getMessage()), e);
        }
        ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.AR_DATA_LIST, (Serializable) arSuperList);
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.EMAIL_ADDRESS,
                DataSubmissionHelper.getLicenseeEmailAddrs(bpc.request));
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.SUBMITTED_BY,
                DataSubmissionHelper.getLoginContext(bpc.request).getUserName());
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CURRENT_PAGE_STAGE, DataSubmissionConstant.PAGE_STAGE_ACK);
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.PRINT_FLAG, DataSubmissionConstant.PRINT_FLAG_ACKART);
    }

    /**
     * Step: PrepareReturn
     *
     * @param bpc
     */
    public void prepareReturn(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("----- PrepareReturn -----"));
    }

    /**
     * Ajax: Download File Template
     *
     * @param request
     * @param response
     */
    @GetMapping(value = "/ds/ar/patient-info-file")
    @ResponseBody
    public void exportTemplate(HttpServletRequest request, HttpServletResponse response) {
        log.info(StringUtil.changeForLog("----- Export Patient Info File -----"));
        try {
            String fileName = "Data_Submission_ART_Patient";
            File inputFile = ResourceUtils.getFile("classpath:template/" + fileName + ".xlsx");
            if (!inputFile.exists() || !inputFile.isFile()) {
                log.error("No File Template Found!");
                return;
            }
            // write Id type
            List<MasterCodeView> masterCodes = MasterCodeUtil.retrieveByCategory(MasterCodeUtil.CATE_ID_DS_ID_TYPE);
            if (IaisCommonUtils.isNotEmpty(masterCodes)) {
                int size = masterCodes.size();
                List<String> values = IaisCommonUtils.genNewArrayList(size);
                Map<Integer, List<Integer>> excelConfigIndex = IaisCommonUtils.genNewLinkedHashMap(size);
                int i = 1;
                for (MasterCodeView view : masterCodes) {
                    values.add(view.getCodeValue());
                    excelConfigIndex.put(i++, Collections.singletonList(1));
                }
                inputFile = IrregularExcelWriterUtil.writerToExcelByIndex(inputFile, 1, values.toArray(new String[size]),
                        excelConfigIndex);
            }
            // wite nationality
            masterCodes = MasterCodeUtil.retrieveByCategory(MasterCodeUtil.CATE_ID_NATIONALITY);
            if (IaisCommonUtils.isNotEmpty(masterCodes)) {
                int size = masterCodes.size();
                List<String> values = IaisCommonUtils.genNewArrayList(size);
                Map<Integer, List<Integer>> excelConfigIndex = IaisCommonUtils.genNewLinkedHashMap(size);
                int i = 1;
                for (MasterCodeView view : masterCodes) {
                    values.add(view.getCodeValue());
                    excelConfigIndex.put(i++, Collections.singletonList(3));
                }
                inputFile = IrregularExcelWriterUtil.writerToExcelByIndex(inputFile, 1, values.toArray(new String[size]),
                        excelConfigIndex);
            }
            // wite ethnic group
            masterCodes = MasterCodeUtil.retrieveByCategory(MasterCodeUtil.CATE_ID_ETHNIC_GROUP);
            if (IaisCommonUtils.isNotEmpty(masterCodes)) {
                int size = masterCodes.size();
                List<String> values = IaisCommonUtils.genNewArrayList(size);
                Map<Integer, List<Integer>> excelConfigIndex = IaisCommonUtils.genNewLinkedHashMap(size);
                int i = 1;
                for (MasterCodeView view : masterCodes) {
                    values.add(view.getCodeValue());
                    excelConfigIndex.put(i++, Collections.singletonList(5));
                }
                inputFile = IrregularExcelWriterUtil.writerToExcelByIndex(inputFile, 1, values.toArray(new String[size]),
                        excelConfigIndex);
            }
            final String postFileName = FileUtils.generationFileName(fileName, FileUtils.EXCEL_TYPE_XSSF);
            File outFile = MiscUtil.generateFile(postFileName);
            boolean b = inputFile.renameTo(outFile);
            if (b) {
                FileUtils.writeFileResponseContent(response, outFile);
                FileUtils.deleteTempFile(outFile);
            } else {
                FileUtils.writeFileResponseContent(response, inputFile);
            }
            FileUtils.deleteTempFile(inputFile);
        } catch (Exception e) {
            log.error(StringUtil.changeForLog("Export Template has error - " + e.getMessage()), e);
        }
    }

}
