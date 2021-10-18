package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.DistributionListDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.DistributionListWebDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.ValidationUtils;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.DistributionListService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/*
 *File Name: MessageDelegator
 *Creator: guyin
 *Creation time:2019/12/26 19:08
 *Describe:
 */

@Delegator(value = "MassEmailDelegator")
@Slf4j
public class MassEmailDelegator {

    private static final String BASE_SERVICE = "SVTP001";
    private static final String SPECIFIED_SERVICE = "SVTP003";
    private static final String EMAIL = "Email";
    private static final String SMS = "SMS";
    private static final String SEARCHPARAM = "massEmailSearchParam";
    private static final String FILE_UPLOAD_ERROR = "fileUploadError";

    @Autowired
    DistributionListService distributionListService;

    public void start(BaseProcessClass bpc){
        SearchParam searchParam = new SearchParam(DistributionListDto.class.getName());
        searchParam.setPageSize(SystemParamUtil.getDefaultPageSize());
        searchParam.setPageNo(1);
        searchParam.setSort("CREATED_DT", SearchParam.DESCENDING);
        searchParam.addFilter("status", AppConsts.COMMON_STATUS_ACTIVE,true);
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_BLAST_NOTIFICATIONS, AuditTrailConsts.FUNCTION_MASS_EMAIL);
        setSearchparam(bpc,searchParam);
    }
    /**
     * doPrepare
     * @param bpc
     */
    public void prepare(BaseProcessClass bpc){
        SearchParam searchParam = getSearchParam(bpc);
        CrudHelper.doPaging(searchParam,bpc.request);
        QueryHelp.setMainSql("systemAdmin", "queryMassDistributionList",searchParam);
        SearchResult<DistributionListDto> searchResult = distributionListService.distributionList(searchParam);
        setServiceSelect(bpc);
        searchRole(bpc);
        setModeSelection(bpc);
        for (DistributionListDto item:searchResult.getRows()
             ) {
            item.setRole(roleName(item.getRole()));
        }
        setSearchparam(bpc,searchParam);
        ParamUtil.setRequestAttr(bpc.request,"distributionSearchResult",searchResult);
        ParamUtil.setRequestAttr(bpc.request,"distributionSearchParam",searchParam);
    }

    public void searchPage(BaseProcessClass bpc){
        SearchParam searchParamGroup = getSearchParam(bpc);
        CrudHelper.doPaging(searchParamGroup,bpc.request);
    }

    /**
     * create
     * @param bpc
     */
    public void create(BaseProcessClass bpc){
        ParamUtil.setSessionAttr(bpc.request,"distributionCanEdit","1");
        setServiceSelect(bpc);
        ParamUtil.setSessionAttr(bpc.request,"distribution",null);
        setModeSelection(bpc);
        String service =  ParamUtil.getString(bpc.request, "service");
        if(service == null){
            setRoleSelection(bpc,"");
        }else{
            setRoleSelection(bpc,HcsaServiceCacheHelper.getServiceByCode(service).getId());
        }
        ParamUtil.setRequestAttr(bpc.request,"title","New");
    }

    /**
     * doPrepare
     * @param bpc
     */
    public void save(BaseProcessClass bpc){
        DistributionListWebDto distributionListDto = getDistribution(bpc);
        ParamUtil.setSessionAttr(bpc.request,"distribution",distributionListDto);
        distributionListDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        ValidationResult validationResult = WebValidationHelper.validateProperty(distributionListDto, "save");
        if(validationResult != null && validationResult.isHasErrors()) {
            Map<String, String> errorMap = validationResult.retrieveAll();
            String emailAddress = StringUtils.join(distributionListDto.getEmailAddress(),"\r\n");
            ParamUtil.setRequestAttr(bpc.request, "emailAddress", emailAddress);
            ParamUtil.setRequestAttr(bpc.request, SystemAdminBaseConstants.ERROR_MSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, SystemAdminBaseConstants.ISVALID, AppConsts.FALSE);
            ParamUtil.setRequestAttr(bpc.request,"distribution",distributionListDto);
        }else{
            distributionListDto = distributionListService.saveDistributionList(distributionListDto);
            distributionListService.saveDistributionRole(distributionListDto);
            ParamUtil.setRequestAttr(bpc.request, SystemAdminBaseConstants.ISVALID, AppConsts.TRUE);
        }

    }

    /**
     * delete
     * @param bpc
     */
    public void delete(BaseProcessClass bpc){
        String[] checkboxlist =  ParamUtil.getMaskedStrings(bpc.request, "checkboxlist");
        if(checkboxlist != null && checkboxlist.length > 0){
            List<String> list = Arrays.asList(checkboxlist);
            distributionListService.deleteDistributionList(list);
        }

    }

    /**
     * search
     * @param bpc
     */
    public void search(BaseProcessClass bpc){
        SearchParam searchParam = getSearchParam(bpc);
        String distributionName = ParamUtil.getRequestString(bpc.request,"distributionName");
        String fieldName = ParamUtil.getRequestString(bpc.request,"fieldName");
        String sortType = ParamUtil.getRequestString(bpc.request,"sortType");
        String[] roleString = ParamUtil.getStrings(bpc.request,"role");

        String roleshow  = "";
        String service = ParamUtil.getString(bpc.request, "service");
        String mode = ParamUtil.getRequestString(bpc.request,"modeDelivery");
        searchParam.getParams().clear();
        searchParam.getFilters().clear();
//        searchParam.setPageNo(1);
//        searchParam.setPageSize(SystemParamUtil.getDefaultPageSize());
        if(!StringUtil.isEmpty(fieldName) && !StringUtil.isEmpty(sortType)){
            searchParam.setSort(fieldName, sortType);
        }else{
            if(IaisCommonUtils.isEmpty(searchParam.getSortMap())){
                searchParam.setSort("CREATED_DT", SearchParam.DESCENDING);
            }
        }
        searchParam.addFilter("status", AppConsts.COMMON_STATUS_ACTIVE,true);
        if(!StringUtil.isEmpty(distributionName)){
            searchParam.addFilter("description",  distributionName ,true);
        }else{
            searchParam.removeFilter("description");
            distributionName = null;
        }

        if(roleString != null){
            List<String> role = Arrays.asList(roleString);
            if(role.size() > 0){
                StringBuilder sb = new StringBuilder("(");
                int i =0;
                for (String item: role) {
                    sb.append(":itemKey").append(i).append(',');
                    i++;
                }
                String inSql = sb.substring(0, sb.length() - 1) + ")";
                searchParam.addParam("remises_corr_id_in", inSql);
                i = 0;
                for (String item: role) {
                    searchParam.addFilter("itemKey" + i,
                            item);
                    i ++;
                }
                roleshow = String.join("#", role);
            }else{
                searchParam.removeFilter("remises_corr_id_in");
                roleshow = null;
            }
        }
        if(!StringUtil.isEmpty(service)){
            searchParam.addFilter("service",  service,true);
        }else{
            searchParam.removeFilter("service");
            service = null;
        }
        if(!StringUtil.isEmpty(mode)){
            searchParam.addFilter("mode",  mode,true);
        }else{
            mode = null;
            searchParam.removeFilter("mode");
        }
        setServiceSelect(bpc);
        setModeSelection(bpc);
        setSearchparam(bpc,searchParam);
        ParamUtil.setRequestAttr(bpc.request,"distributionName",distributionName);
        ParamUtil.setRequestAttr(bpc.request,"role",roleshow);
        ParamUtil.setRequestAttr(bpc.request,"service",service);
        ParamUtil.setRequestAttr(bpc.request,"modeDelivery",mode);
    }

    /**
     * edit
     * @param bpc
     */
    public void edit(BaseProcessClass bpc){
        ParamUtil.setSessionAttr(bpc.request,"distributionCanEdit","0");
        String id =  ParamUtil.getMaskedString(bpc.request, "editDistribution");
        DistributionListWebDto distributionListDto = distributionListService.getDistributionListById(id);
        setServiceSelect(bpc);
        setModeSelection(bpc);
        setRoleSelection(bpc, HcsaServiceCacheHelper.getServiceByCode(distributionListDto.getService()).getId());
        String emailAddress = StringUtils.join(distributionListDto.getEmailAddress(),"\r\n");
        ParamUtil.setRequestAttr(bpc.request, "emailAddress", emailAddress);
        ParamUtil.setRequestAttr(bpc.request,"distribution",distributionListDto);
        ParamUtil.setRequestAttr(bpc.request, "firstOption", distributionListDto.getService());
        ParamUtil.setRequestAttr(bpc.request,"title","Edit");
    }

    public void insertFile(BaseProcessClass bpc) throws IOException{
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        DistributionListWebDto distributionListWebDto = getDistribution(bpc);
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        MultipartFile file = mulReq.getFile("selectedFile");
        if(file!=null&&file.getOriginalFilename()!=null){
            String originalFileName = file.getOriginalFilename();
            if (!FileUtils.isExcel(originalFileName)){
                errMap.put("file", MessageUtil.getMessageDesc("CHKL_ERR041"));
            }else if (FileUtils.outFileSize(file.getSize())){
                int maxSize = SystemParamUtil.getFileMaxLimit();
                String replaceMsg = MessageUtil.replaceMessage("GENERAL_ERR0019", String.valueOf(maxSize),"sizeMax");
                errMap.put("file", replaceMsg);
            }else {
                String mode = mulReq.getParameter("mode");
                List<String> filelist = IaisCommonUtils.genNewArrayList();
                filelist = getAllData(file,mode);
                ParamUtil.setSessionAttr(bpc.request,"massEmailFilelist",(Serializable) getAllData(file,mode));
                if(SMS.equals(mode)){
                    List<String> address = getEmail(bpc,"mobile");
                    if(!filelist.isEmpty() && repeatList(filelist)){
                        errMap.put("file", MessageUtil.replaceMessage("EMM_ERR009","mobiles","mode"));
                    }
                    if(!filelist.isEmpty() && !isMobileEmail(filelist)){
                        errMap.put("file", MessageUtil.getMessageDesc("GENERAL_ERR0007"));
                    }
                    if(address != null){
                        if(!filelist.isEmpty() ) {
                            filelist.addAll(address);
                        }else{
                            filelist = address;
                        }
                    }
                    if(!filelist.isEmpty() ){
                        if(repeatList(filelist) && StringUtil.isEmpty(errMap.get("file"))){
                            errMap.put("file", MessageUtil.replaceMessage("EMM_ERR008","mobiles","mode"));
                        }
                        String fileData = StringUtils.join(filelist, "\r\n");
                        ParamUtil.setRequestAttr(bpc.request,"emailAddress",fileData);
                    }
                }else{
                    List<String> address = getEmail(bpc,"email");
                    boolean isRepeat = false;
                    if(!filelist.isEmpty() ){
                        isRepeat = repeatList(filelist);
                    }

                    if(isRepeat){
                        errMap.put("file", MessageUtil.replaceMessage("EMM_ERR009","email address(es)","mode"));
                    }
                    if(!filelist.isEmpty() && !isEmail(filelist)){
                        errMap.put("file", MessageUtil.getMessageDesc("GENERAL_ERR0014"));
                    }
                    if(address != null){
                        if(!filelist.isEmpty() ) {
                            filelist.addAll(address);
                        }else{
                            filelist = address;
                        }
                    }
                    if(!filelist.isEmpty() ){
                        if(repeatList(filelist) && StringUtil.isEmpty(errMap.get("file"))){
                            errMap.put("file", MessageUtil.replaceMessage("EMM_ERR008","email address(es)","mode"));
                        }
                        String fileData = StringUtils.join(filelist, "\r\n");
                        ParamUtil.setRequestAttr(bpc.request,"emailAddress",fileData);
                    }

                }
            }
        }


        ParamUtil.setRequestAttr(bpc.request, SystemAdminBaseConstants.ERROR_MSG, WebValidationHelper.generateJsonStr(errMap));
        ParamUtil.setRequestAttr(bpc.request,"distribution",distributionListWebDto);
        ParamUtil.setRequestAttr(bpc.request,"fileName",file != null ? file.getOriginalFilename() : null);
        setServiceSelect(bpc);
        setModeSelection(bpc);
        if(!StringUtil.isEmpty(distributionListWebDto.getService())){
            setRoleSelection(bpc, HcsaServiceCacheHelper.getServiceByCode(distributionListWebDto.getService()).getId());
        }else{
            setRoleSelection(bpc,null);
        }


    }

    private boolean isEmail(List<String> list){
        for (String item:list
        ) {
            if(!ValidationUtils.isEmail(item)){
                return false;
            }
        }
        return true;
    }
    private boolean isMobileEmail(List<String> list){
        for (String item:list
        ) {
            if(!item.matches("^[8|9][0-9]{7}$")){
                return false;
            }
        }
        return true;
    }
    private boolean repeatList(List<String> list){
        Map<String,String> repeatMap = IaisCommonUtils.genNewHashMap();
        for (String item:list
             ) {
            if(StringUtil.isEmpty(repeatMap.get(item))){
                repeatMap.put(item,item);
                continue;
            }else{
                return true;
            }
        }
        return false;
    }
    private Map<String, String> validationFile(MultipartFile file){
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        String size = Long.toString(file.getSize());
        String ext = file.getContentType();
        if (Double.parseDouble(size) <= 0){
            errMap.put(FILE_UPLOAD_ERROR, "GENERAL_ERR0004");
        }
        return errMap;
    }

    private List<String> getEmail(BaseProcessClass bpc,String name){
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String email = mulReq.getParameter(name);
        List<String> emailAddress = IaisCommonUtils.genNewArrayList();
        if(!StringUtil.isEmpty(email)){
            List<String> rnemaillist = Arrays.asList(email.split("\r\n"));
            List<String> commaemaillist = Arrays.asList(email.split(","));
            if(rnemaillist.size() > commaemaillist.size() ){
                emailAddress = rnemaillist;
            }else{
                emailAddress = commaemaillist;
            }
        }
        return emailAddress;
    }

    private DistributionListWebDto getDistribution(BaseProcessClass bpc){
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String mode = mulReq.getParameter("mode");
        String id = mulReq.getParameter("distributionId");
        String name = mulReq.getParameter("name");
        String service = mulReq.getParameter("service");
        String role = mulReq.getParameter("role");

        String mobile = mulReq.getParameter("mobile");

        DistributionListWebDto distributionListDto = new DistributionListWebDto();
        String textarea = "email";
        if(SMS.equals(mode)){
            textarea ="mobile";
        }
        distributionListDto.setEmailAddress(getEmail(bpc,textarea));

        if(!StringUtil.isEmpty(id))
        {
            distributionListDto.setId(id);
        }else{
            distributionListDto.setId(null);
        }
        distributionListDto.setService(service);
        distributionListDto.setDisname(name);
        distributionListDto.setMode(mode);
        distributionListDto.setRole(role);
        distributionListDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return distributionListDto;
    }

    public void switchStep(BaseProcessClass bpc){
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String type = mulReq.getParameter("crud_action_type");
        ParamUtil.setRequestAttr(bpc.request,"crud_action_type",type);
    }

    private List<String> getAllData(MultipartFile mulfile,String mode) throws IOException {
        List<String> list = IaisCommonUtils.genNewArrayList();
        try{
            File file = FileUtils.multipartFileToFile(mulfile, "mass");
            Sheet sheet= parseFile(file,0);
            int firstrow=    sheet.getFirstRowNum() + 1;
            int lastrow=    sheet.getLastRowNum();
            for (int i = firstrow; i < lastrow+1; i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    int firstcell = row.getFirstCellNum();
                    int lastcell = row.getLastCellNum();

                    for (int j = firstcell; j < lastcell; j++) {

                        Cell cell = row.getCell(j);
                        if (cell != null) {
                            String cellString;
                            cellString = cell.toString();
                            if(isScienceNum(cellString)){
                                DecimalFormat df = new DecimalFormat("0");
                                cellString = df.format(cell.getNumericCellValue());
                            }
                            if(!StringUtil.isEmpty(cellString)){
                                list.add(cellString);
                            }
                        }
                    }
                }
            }

        }catch (Exception e){
            log.error(e.getMessage(), e);
        }finally {

        }
        return list;
    }

    private boolean isScienceNum(String str){
        String regEx = "^[0-9]{1,5}.[0-9]{1,5}E[0-9]{1,5}$";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    @SuppressWarnings("resource")
    private static Sheet parseFile(final File file, int sheetAt) throws Exception {
        Workbook workBook = null;
        try (InputStream in = Files.newInputStream(file.toPath())){
            char indexChar = ".".charAt(0);
            String suffix = file.getName().substring(file.getName().indexOf(indexChar) + 1);
            workBook = suffix.equals(FileUtils.EXCEL_TYPE_XSSF) ? new XSSFWorkbook(in) : new HSSFWorkbook(in);
            return workBook.getSheetAt(sheetAt);
        } catch (Exception e) {
            throw new Exception(e.getMessage(),e);
        }finally {
            try {
                if (workBook != null){
                    workBook.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private void setServiceSelect(BaseProcessClass bpc){
        List<HcsaServiceDto> hcsaServiceDtoList = distributionListService.getServicesInActive();
        if (IaisCommonUtils.isEmpty(hcsaServiceDtoList)){
            log.debug("can not find hcsa service list in service menu delegator!");
            return;
        }

        List<HcsaServiceDto> baseService = hcsaServiceDtoList.stream()
                .filter(hcsaServiceDto -> BASE_SERVICE.equals(hcsaServiceDto.getSvcType())).collect(Collectors.toList());

        List<HcsaServiceDto> specifiedService = hcsaServiceDtoList.stream()
                .filter(hcsaServiceDto -> SPECIFIED_SERVICE.equals(hcsaServiceDto.getSvcType())).collect(Collectors.toList());

        baseService.addAll(specifiedService);
        List<SelectOption> selectOptionArrayList = IaisCommonUtils.genNewArrayList();
        for (HcsaServiceDto item : baseService) {
            selectOptionArrayList.add(new SelectOption(item.getSvcCode(),item.getSvcName()));
        }
        ParamUtil.setRequestAttr(bpc.request, "serviceSelection", (Serializable) selectOptionArrayList);
    }

    private void setModeSelection(BaseProcessClass bpc){
        List<SelectOption> selectOptions = IaisCommonUtils.genNewArrayList();
        selectOptions.add(new SelectOption(EMAIL,EMAIL));
        selectOptions.add(new SelectOption(SMS,SMS));
        ParamUtil.setRequestAttr(bpc.request, "mode",  (Serializable) selectOptions);
    }

    private void setRoleSelection(BaseProcessClass bpc, String service){
        List<SelectOption> selectOptions = IaisCommonUtils.genNewArrayList();
        if(!StringUtils.isEmpty(service)){
            List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtoList = distributionListService.roleByServiceId(service,AppConsts.COMMON_STATUS_ACTIVE);
            for (HcsaSvcPersonnelDto item:hcsaSvcPersonnelDtoList
            ) {
                selectOptions.add(new SelectOption(item.getPsnType(),roleName(item.getPsnType())));
            }
            selectOptions.add(new SelectOption(ApplicationConsts.PERSONNEL_PSN_TYPE_LICENSEE,"Licensee"));
            selectOptions.add(new SelectOption(ApplicationConsts.PERSONNEL_PSN_TYPE_AP,"Authorised Person"));
        }else{
            selectOptions.add(new SelectOption("Licensee","Licensee"));
            selectOptions.add(new SelectOption("Authorised Person","Authorised Person"));
            selectOptions.add(new SelectOption("Principal Officer","Principal Officer"));
            selectOptions.add(new SelectOption("Nominee","Nominee"));
            selectOptions.add(new SelectOption("Clinical Governance Officer","Clinical Governance Officer"));
            selectOptions.add(new SelectOption("Service Personnel","Service Personnel"));
            selectOptions.add(new SelectOption("MedAlert","MedAlert"));
            selectOptions.add(new SelectOption("Clinical Director","Clinical Director"));
        }
        doSortSelOption(selectOptions);
        ParamUtil.setRequestAttr(bpc.request, "roleSelection",  (Serializable) selectOptions);
    }
    public static void doSortSelOption(List<SelectOption> selectOptions){
        Collections.sort(selectOptions,(s1, s2)->(s1.getText().compareTo(s2.getText())));
    }

    private void searchRole(BaseProcessClass bpc){
        SearchParam searchParam = getSearchParam(bpc);
        String serviceCode = (String)searchParam.getFilters().get("service");
        List<SelectOption> selectOptions = IaisCommonUtils.genNewArrayList();
        if(serviceCode != null){
            if(!serviceCode.isEmpty()){
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByCode(serviceCode);
                List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtoList = distributionListService.roleByServiceId(hcsaServiceDto.getId(),AppConsts.COMMON_STATUS_ACTIVE);
                for (HcsaSvcPersonnelDto item:hcsaSvcPersonnelDtoList
                ) {
                    selectOptions.add(new SelectOption(item.getPsnType(),roleName(item.getPsnType())));
                }
                selectOptions.add(new SelectOption(ApplicationConsts.PERSONNEL_PSN_TYPE_LICENSEE,"Licensee"));
                selectOptions.add(new SelectOption(ApplicationConsts.PERSONNEL_PSN_TYPE_AP,"Authorised Person"));
            }
        }else{
            selectOptions.add(new SelectOption(ApplicationConsts.PERSONNEL_PSN_TYPE_LICENSEE,"Licensee"));
            selectOptions.add(new SelectOption(ApplicationConsts.PERSONNEL_PSN_TYPE_AP,"Authorised Person"));
            selectOptions.add(new SelectOption(ApplicationConsts.PERSONNEL_PSN_TYPE_PO,"Principal Officer"));
            selectOptions.add(new SelectOption(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO,"Nominee"));
            selectOptions.add(new SelectOption(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO,"Clinical Governance Officer"));
            selectOptions.add(new SelectOption(ApplicationConsts.PERSONNEL_PSN_TYPE_SVC_PERSONNEL,"Service Personnel"));
            selectOptions.add(new SelectOption(ApplicationConsts.PERSONNEL_PSN_TYPE_MEDALERT,"MedAlert"));
            selectOptions.add(new SelectOption(ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR,"Clinical Director"));

        }
        doSortSelOption(selectOptions);
       ParamUtil.setRequestAttr(bpc.request, "roleSelection",  (Serializable) selectOptions);
    }


    private String roleName(String roleAbbreviation){
        String roleName = "";
        switch (roleAbbreviation){
            case ApplicationConsts.PERSONNEL_PSN_TYPE_CGO:
                roleName = ApplicationConsts.PERSONNEL_PSN_TYPE_CLINICAL_GOVERNANCE_OFFICER;
                break;
            case ApplicationConsts.PERSONNEL_PSN_TYPE_PO:
                roleName = ApplicationConsts.PERSONNEL_PSN_TYPE_PRINCIPAL_OFFICER;
                break;
            case ApplicationConsts.PERSONNEL_PSN_TYPE_DPO:
                roleName = ApplicationConsts.PERSONNEL_PSN_TYPE_DEPUTY_PRINCIPAL_OFFICER;
                break;
            case ApplicationConsts.PERSONNEL_PSN_TYPE_MAP:
                roleName = ApplicationConsts.PERSONNEL_PSN_TYPE_MEDALERT;
                break;
            case ApplicationConsts.PERSONNEL_PSN_TYPE_SVC_PERSONNEL:
                roleName = ApplicationConsts.PERSONNEL_PSN_TYPE_SVC;
                break;
            case ApplicationConsts.PERSONNEL_PSN_TYPE_LICENSEE:
                roleName = "Licensee";
                break;
            case ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR:
                roleName = "Clinical Director";
                break;
                default:
                    roleName = roleAbbreviation;
                    break;
        }
        return roleName;
    }

    private SearchParam getSearchParam(BaseProcessClass bpc){
        return (SearchParam) ParamUtil.getSessionAttr(bpc.request,SEARCHPARAM);
    }

    private void setSearchparam(BaseProcessClass bpc,SearchParam searchParam){
        ParamUtil.setSessionAttr(bpc.request,SEARCHPARAM,searchParam);
    }
}
