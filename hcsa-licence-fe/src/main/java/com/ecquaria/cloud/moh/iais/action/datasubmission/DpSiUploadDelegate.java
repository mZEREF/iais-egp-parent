package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.action.HcsaFileAjaxController;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DpSovenorInventoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DpSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.dto.ExcelPropertyDto;
import com.ecquaria.cloud.moh.iais.dto.FileErrorMsg;
import com.ecquaria.cloud.moh.iais.dto.PageShowFileDto;
import com.ecquaria.cloud.moh.iais.dto.SovenorInventoryExcelDto;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelValidatorHelper;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.DpDataSubmissionService;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Process: MohDpSIUpload
 *
 * @Description DpSiUploadDelegate
 * @Auther suocheng on 01/17/2020.
 */
@Delegator("dpSiUploadDelegate")
@Slf4j
public class DpSiUploadDelegate {

    protected static final String ACTION_TYPE_PREVIEW = "preview";

    private static final String FILE_ITEM_SIZE = "fileItemSize";

    private static final String SOVENOR_INVENTORY_LIST = "SOVENOR_INVENTORY_LIST";
    private static final String PAGE_SHOW_FILE = "showPatientFile";
    private static final String FILE_APPEND = "uploadFile";
    private static final String SEESION_FILES_MAP_AJAX = HcsaFileAjaxController.SEESION_FILES_MAP_AJAX + FILE_APPEND;

    @Autowired
    private SystemParamConfig systemParamConfig;

    @Autowired
    private NotificationHelper notificationHelper;

    @Autowired
    private ArDataSubmissionService arDataSubmissionService;

    @Autowired
    private DpDataSubmissionService dpDataSubmissionService;

    /**
     * Step: Start
     *
     * @param bpc
     */
    public void doStart(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("-----DpSiUploadDelegate Start -----"));
        clearSession(bpc.request);
    }

    private void clearSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute(SEESION_FILES_MAP_AJAX);
        session.removeAttribute(SOVENOR_INVENTORY_LIST);
        session.removeAttribute(PAGE_SHOW_FILE);
        session.removeAttribute(DataSubmissionConstant.DP_DATA_LIST);
        int configFileSize = systemParamConfig.getUploadFileLimit();

        ParamUtil.setSessionAttr(request,"configFileSize",configFileSize);
    }

    /**
     * Step: PrepareSwitch
     *
     * @param bpc
     */
    public void prepareSwitch(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("----- PrepareSwitch -----"));
        ParamUtil.setRequestAttr(bpc.request, "title", DataSubmissionConstant.DS_TITLE_NEW);
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>Drug Practices</strong>");
        String actionType = (String) ParamUtil.getRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
        log.info(StringUtil.changeForLog("----- Action Type: " + actionType + " -----"));
        if (StringUtil.isEmpty(actionType)) {
            actionType = ACTION_TYPE_PREVIEW;
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

    }

    private void preapreDate(String pageStage, HttpServletRequest request) {
        Map<String, String> maxCountMap = IaisCommonUtils.genNewHashMap(1);
        maxCountMap.put("maxCount", Formatter.formatNumber(DataSubmissionHelper.getFileRecordMaxNumber(), "#,##0"));
        ParamUtil.setRequestAttr(request, "maxCountMap", maxCountMap);
        ParamUtil.setRequestAttr(request, DataSubmissionConstant.CURRENT_PAGE_STAGE, pageStage);
        Integer fileItemSize = (Integer) request.getAttribute(FILE_ITEM_SIZE);
        if (fileItemSize == null) {
            List<DpSovenorInventoryDto> sovenorInventoryDtos = (List<DpSovenorInventoryDto>) request.getSession().getAttribute(SOVENOR_INVENTORY_LIST);
            fileItemSize = sovenorInventoryDtos != null ? sovenorInventoryDtos.size() : 0;
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

    }

    private void validSovenorInventory(List<FileErrorMsg> errorMsgs,DpSovenorInventoryDto siDto,Map<String, ExcelPropertyDto> fieldCellMap,int i){
        String errMsgErr002 = MessageUtil.getMessageDesc("GENERAL_ERR0002");
        String errMsgErr006 = MessageUtil.getMessageDesc("GENERAL_ERR0006");

        if(StringUtil.isNotEmpty(siDto.getHciName())){
            if(siDto.getHciName().length()>256){
                Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                repMap.put("number","256");
                repMap.put("HCI Name","This field");
                String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("hciName"), errMsg));
            }
        }else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("hciName"), errMsgErr006));
        }

        if(StringUtil.isNotEmpty(siDto.getDrugName())){
            if(siDto.getDrugName().length()>50){
                Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                repMap.put("number","50");
                repMap.put("Drug Name","This field");
                String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("drugName"), errMsg));
            }
        }else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("drugName"), errMsgErr006));
        }
        if(StringUtil.isNotEmpty(siDto.getBatchNumber())){
            try {
                Double.parseDouble(siDto.getBatchNumber());
                if(siDto.getBatchNumber().length()>50){
                    Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                    repMap.put("number","50");
                    repMap.put("Batch Number","This field");
                    String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                    errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("batchNumber"), errMsg));
                }
            }catch (Exception e){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("batchNumber"), errMsgErr002));
            }

        }else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("batchNumber"), errMsgErr006));
        }
        if(StringUtil.isNotEmpty(siDto.getDrugStrength())){
            try {
                Double.parseDouble(siDto.getDrugStrength());
                if(siDto.getDrugStrength().length()>50){
                    Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                    repMap.put("number","50");
                    repMap.put("Drug Strength","This field");
                    String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                    errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("drugStrength"), errMsg));
                }
            }catch (Exception e){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("drugStrength"), errMsgErr002));
            }
        }else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("drugStrength"), errMsgErr006));
        }
        if(StringUtil.isNotEmpty(siDto.getQuantityDrugPurchased())){
            if(siDto.getQuantityDrugPurchased().length()>50){
                Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                repMap.put("number","50");
                repMap.put("Quantity of Drug Purchased","This field");
                String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("quantityDrugPurchased"), errMsg));
            }
        }else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("quantityDrugPurchased"), errMsgErr006));
        }

        if(StringUtil.isNotEmpty(siDto.getPurchaseDate())){
            try {
                Formatter.parseDate(siDto.getPurchaseDate());
            }catch (Exception e){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("purchaseDate"), "GENERAL_ERR0033"));
            }
        }else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("purchaseDate"), errMsgErr006));
        }

        if(StringUtil.isNotEmpty(siDto.getDeliveryDate())){
            try {
                Formatter.parseDate(siDto.getDeliveryDate());
            }catch (Exception e){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("deliveryDate"), "GENERAL_ERR0033"));
            }
        }else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("deliveryDate"), errMsgErr006));
        }

        if(StringUtil.isNotEmpty(siDto.getExpiryDate())){
            try {
                Formatter.parseDate(siDto.getExpiryDate());
            }catch (Exception e){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("expiryDate"), "GENERAL_ERR0033"));
            }
        }else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("expiryDate"), errMsgErr006));
        }

        if(StringUtil.isNotEmpty(siDto.getQuantityBalanceStock())){
            try {
                Double.parseDouble(siDto.getQuantityBalanceStock());
                if(siDto.getQuantityBalanceStock().length()>50){
                    Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                    repMap.put("number","50");
                    repMap.put("Quantity of balance stock as at 31 Dec 2017","This field");
                    String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                    errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("quantityBalanceStock"), errMsg));
                }
            }catch (Exception e){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("quantityBalanceStock"), errMsgErr002));
            }
        }else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("quantityBalanceStock"), errMsgErr006));
        }
        if(StringUtil.isNotEmpty(siDto.getQuantityExpiredStock())){
            try {
                Double.parseDouble(siDto.getQuantityExpiredStock());
                if(siDto.getQuantityExpiredStock().length()>50){
                    Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                    repMap.put("number","50");
                    repMap.put("Quantity of expired stock as at 31 Dec 2017","This field");
                    String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                    errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("quantityExpiredStock"), errMsg));
                }
            }catch (Exception e){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("quantityExpiredStock"), errMsgErr002));
            }
        }else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("quantityExpiredStock"), errMsgErr006));
        }
        if(StringUtil.isNotEmpty(siDto.getRemarks())){
            if(siDto.getRemarks().length()>50){
                Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                repMap.put("number","50");
                repMap.put("Remarks","This field");
                String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("remarks"), errMsg));
            }
        }
    }




    /**
     * Transfer to patient info dto from patient info excel dto
     * And map value to code for some fields (drowndrop)
     *
     * @param sovenorInventoryExcelDtos
     * @return
     */
    private List<DpSovenorInventoryDto> getSovenorInventoryList(List<SovenorInventoryExcelDto> sovenorInventoryExcelDtos) {
        if (sovenorInventoryExcelDtos == null) {
            return null;
        }

        List<DpSovenorInventoryDto> result = IaisCommonUtils.genNewArrayList(sovenorInventoryExcelDtos.size());
        for (SovenorInventoryExcelDto excelDto : sovenorInventoryExcelDtos) {
            DpSovenorInventoryDto dto = new DpSovenorInventoryDto();
            dto.setBatchNumber(excelDto.getBatchNumber());
            dto.setDeliveryDate(IaisCommonUtils.handleDate(excelDto.getDeliveryDate()));
            dto.setDrugName(excelDto.getDrugName());
            dto.setDrugStrength(excelDto.getDrugStrength());
            dto.setExpiryDate(IaisCommonUtils.handleDate(excelDto.getExpiryDate()));
            dto.setHciName(excelDto.getHciName());
            dto.setPurchaseDate(IaisCommonUtils.handleDate(excelDto.getPurchaseDate()));
            dto.setQuantityBalanceStock(excelDto.getQuantityBalanceStock());
            dto.setQuantityDrugPurchased(excelDto.getQuantityDrugPurchased());
            dto.setQuantityExpiredStock(excelDto.getQuantityExpiredStock());
            dto.setRemarks(excelDto.getRemarks());
            result.add(dto);
        }
        return result;
    }

    private List<SovenorInventoryExcelDto> getSovenorInventoryExcelDtoList(Entry<String, File> fileEntry) {
        if (fileEntry == null) {
            return IaisCommonUtils.genNewArrayList(0);
        }
        try {
            File file = fileEntry.getValue();
            if (FileUtils.isExcel(file.getName())) {
                return FileUtils.transformToJavaBean(fileEntry.getValue(), SovenorInventoryExcelDto.class, true);
            } else if (FileUtils.isCsv(file.getName())) {
                return FileUtils.transformCsvToJavaBean(fileEntry.getValue(), SovenorInventoryExcelDto.class, true);
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
        DpSuperDataSubmissionDto superDataSubmissionDto = DataSubmissionHelper.getCurrentDpDataSubmission(bpc.request);
        DataSubmissionDto dataSubmissionDto = superDataSubmissionDto.getDataSubmissionDto();
        dataSubmissionDto.setDeclaration(declaration);
        DataSubmissionHelper.setCurrentDpDataSubmission(superDataSubmissionDto, bpc.request);
        String crudype = ParamUtil.getString(bpc.request, DataSubmissionConstant.CRUD_TYPE);
        if (StringUtil.isIn(crudype, new String[]{"return", "back"})) {
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "return");
            clearSession(bpc.request);
            return;
        }
        int fileItemSize = 0;
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        boolean hasItems = AppConsts.YES.equals(ParamUtil.getString(bpc.request, "hasItems"));
        log.info(StringUtil.changeForLog("---- Has Items: " + hasItems + " ----"));
        List<DpSovenorInventoryDto> dpSovenorInventoryDtos = (List<DpSovenorInventoryDto>) bpc.request.getSession().getAttribute(SOVENOR_INVENTORY_LIST);
        if (dpSovenorInventoryDtos == null || !hasItems) {
            // upload file (first time / error)
            Entry<String, File> fileEntry = getFileEntry(bpc.request);
            PageShowFileDto pageShowFileDto = getPageShowFileDto(fileEntry);
            ParamUtil.setSessionAttr(bpc.request, PAGE_SHOW_FILE, pageShowFileDto);
            errorMap = DataSubmissionHelper.validateFile(SEESION_FILES_MAP_AJAX, bpc.request);
            if (errorMap.isEmpty()) {
                String fileName=fileEntry.getValue().getName();
                if(!fileName.equals("Sovenor_Inventory_List.xlsx")&&!fileName.equals("Sovenor_Inventory_List.csv")){
                    errorMap.put("uploadFileError", "DS_ERR068");
                }
                List<SovenorInventoryExcelDto> sovenorInventoryExcelDtos = getSovenorInventoryExcelDtoList(fileEntry);
                fileItemSize = sovenorInventoryExcelDtos.size();
                if (fileItemSize == 0) {
                    errorMap.put("uploadFileError", "PRF_ERR006");
                } else if (fileItemSize > DataSubmissionHelper.getFileRecordMaxNumber()) {
                    errorMap.put("uploadFileError", MessageUtil.replaceMessage("GENERAL_ERR0052",
                            Formatter.formatNumber(DataSubmissionHelper.getFileRecordMaxNumber(), "#,##0"), "maxCount"));
                } else {
                    dpSovenorInventoryDtos = getSovenorInventoryList(sovenorInventoryExcelDtos);
                    Map<String, ExcelPropertyDto> fieldCellMap = ExcelValidatorHelper.getFieldCellMap(SovenorInventoryExcelDto.class);
                    List<FileErrorMsg> errorMsgs = DataSubmissionHelper.validateExcelList(dpSovenorInventoryDtos, "file", fieldCellMap);

                    for (int i = 1; i <= fileItemSize; i++) {
                        DpSovenorInventoryDto siDto=dpSovenorInventoryDtos.get(i-1);
                        validSovenorInventory(errorMsgs, siDto, fieldCellMap, i);
                    }
                    if (!errorMsgs.isEmpty()) {
                        Collections.sort(errorMsgs, Comparator.comparing(FileErrorMsg::getRow).thenComparing(FileErrorMsg::getCol));
                        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.FILE_ITEM_ERROR_MSGS, errorMsgs);
                        errorMap.put("itemError", "itemError");
                        errorMap.put("uploadFileError", "DS_ERR068");
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
            ParamUtil.setRequestAttr(bpc.request, "hasError",true);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            clearSession(bpc.request);
            fileItemSize = 0;
            crudype = ACTION_TYPE_PREVIEW;
        } else {
            if (dpSovenorInventoryDtos != null) {
                fileItemSize = dpSovenorInventoryDtos.size();
            }
            bpc.request.getSession().setAttribute(SOVENOR_INVENTORY_LIST, dpSovenorInventoryDtos);
        }
        ParamUtil.setRequestAttr(bpc.request, FILE_ITEM_SIZE, fileItemSize);
        log.info(StringUtil.changeForLog("---- Action Type: " + crudype + " ----"));
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, crudype);
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
        List<DpSovenorInventoryDto> sovenorInventoryDtos = (List<DpSovenorInventoryDto>) bpc.request.getSession().getAttribute(SOVENOR_INVENTORY_LIST);
        if (sovenorInventoryDtos == null || sovenorInventoryDtos.isEmpty()) {
            log.warn(StringUtil.changeForLog("----- No Data to be submitted -----"));
            return;
        }
        boolean useParallel = sovenorInventoryDtos.size() >= AppConsts.DFT_MIN_PARALLEL_SIZE;
        DpSuperDataSubmissionDto dpSuperDto = DataSubmissionHelper.getCurrentDpDataSubmission(bpc.request);
        String declaration = dpSuperDto.getDataSubmissionDto().getDeclaration();
        List<DpSuperDataSubmissionDto> dpSuperList = StreamSupport.stream(sovenorInventoryDtos.spliterator(), useParallel)
                .map(dto -> {
                    DpSuperDataSubmissionDto newDto = DataSubmissionHelper.dpReNew(dpSuperDto);
                    newDto.setFe(true);
                    DataSubmissionDto dataSubmissionDto = newDto.getDataSubmissionDto();
                    String submissionNo = dpDataSubmissionService.getSubmissionNo(DataSubmissionConsts.DS_DRP);
                    dataSubmissionDto.setSubmitBy(DataSubmissionHelper.getLoginContext(bpc.request).getUserId());
                    dataSubmissionDto.setSubmitDt(new Date());
                    dataSubmissionDto.setSubmissionNo(submissionNo);
                    dataSubmissionDto.setDeclaration(declaration);
                    newDto.setDataSubmissionDto(dataSubmissionDto);
                    newDto.setDpSovenorInventoryDto(dto);
                    return newDto;
                })
                .collect(Collectors.toList());
        if (useParallel) {
            Collections.sort(dpSuperList, Comparator.comparing(dto -> dto.getDataSubmissionDto().getSubmissionNo()));
        }
        List<DpSuperDataSubmissionDto> dpSuperLists=IaisCommonUtils.genNewArrayList();
        for (DpSuperDataSubmissionDto dp:dpSuperList
             ) {
            DpSuperDataSubmissionDto dpNew=dpDataSubmissionService.saveDpSuperDataSubmissionDto(dp);
            dpSuperLists.add(dpNew);
        }
        List<DpSuperDataSubmissionDto> dpSuperListBe=IaisCommonUtils.genNewArrayList();
        try {
            for (DpSuperDataSubmissionDto dp:dpSuperLists
            ) {
                DpSuperDataSubmissionDto dpNew=dpDataSubmissionService.saveDpSuperDataSubmissionDtoToBE(dp);
                dpSuperListBe.add(dpNew);
            }
        } catch (Exception e) {
            log.error(StringUtil.changeForLog("The Eic saveArSuperDataSubmissionDtoToBE failed ===>" + e.getMessage()), e);
        }
        sendSovenorFileMsg(bpc.request);
        ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.DP_DATA_LIST, (Serializable) dpSuperListBe);
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.EMAIL_ADDRESS,
                DataSubmissionHelper.getLicenseeEmailAddrs(bpc.request));
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.SUBMITTED_BY,
                DataSubmissionHelper.getLoginContext(bpc.request).getUserName());
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CURRENT_PAGE_STAGE, DataSubmissionConstant.PAGE_STAGE_ACK);
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.PRINT_FLAG, DataSubmissionConstant.PRINT_FLAG_ACKDRP);
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
    @GetMapping(value = "/ds/dp/si-file")
    @ResponseBody
    public void exportTemplate(HttpServletRequest request, HttpServletResponse response) {
        log.info(StringUtil.changeForLog("----- Export Patient Info File -----"));
        try {
            String fileName = "Sovenor_Inventory_List";
            File inputFile = ResourceUtils.getFile("classpath:template/" + fileName + ".xlsx");
            if (!inputFile.exists() || !inputFile.isFile()) {
                log.error("No File Template Found!");
                return;
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

    public void sendSovenorFileMsg(HttpServletRequest request)  {

        Map<String, Object> emailMap = IaisCommonUtils.genNewHashMap();

        String subNo=System.currentTimeMillis()+"";
        emailMap.put("SubmitterName", DataSubmissionHelper.getLoginContext(request).getUserName());

        emailMap.put("DDMMYYYYtime", Formatter.formatDateTime(new Date()));

        emailMap.put("MOH_AGENCY_NAM_GROUP","<b>"+AppConsts.MOH_AGENCY_NAM_GROUP+"</b>");
        emailMap.put("MOH_AGENCY_NAME", "<b>"+AppConsts.MOH_AGENCY_NAME+"</b>");

        //msg
        try {
            EmailParam msgParam = new EmailParam();
            msgParam.setQueryCode(subNo);
            msgParam.setReqRefNum(subNo);
            msgParam.setTemplateContent(emailMap);
            msgParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_SOVENOR_MSG);

            List<String> svcCode=IaisCommonUtils.genNewArrayList();
            svcCode.add(DataSubmissionConsts.DS_DRP);
            msgParam.setSvcCodeList(svcCode);
            msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
            msgParam.setRefId(DataSubmissionHelper.getLoginContext(request).getLicenseeId());
            notificationHelper.sendNotification(msgParam);
        }catch (Exception e){
            log.info(e.getMessage(),e);
        }

    }

}
