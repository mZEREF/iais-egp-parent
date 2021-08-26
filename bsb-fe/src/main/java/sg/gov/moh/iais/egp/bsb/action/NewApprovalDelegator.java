package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.ApprovalApplicationClient;
import sg.gov.moh.iais.egp.bsb.constant.ApprovalApplicationConstants;
import sg.gov.moh.iais.egp.bsb.dto.approval.ApprovalApplicationDto;
import sg.gov.moh.iais.egp.bsb.dto.approval.BiologicalQueryDto;
import sg.gov.moh.iais.egp.bsb.dto.approval.FacilityQueryDto;
import sg.gov.moh.iais.egp.bsb.entity.Facility;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author : LiRan
 * @date : 2021/8/11
 */
@Delegator
@Slf4j
public class NewApprovalDelegator {

    public static final String TASK_LIST = "taskList";

    private final ApprovalApplicationClient approvalApplicationClient;

    @Autowired
    public NewApprovalDelegator(ApprovalApplicationClient approvalApplicationClient) {
        this.approvalApplicationClient = approvalApplicationClient;
    }

    @Autowired
    private SystemParamConfig systemParamConfig;

    public void doStart(BaseProcessClass bpc) throws IllegalAccessException {
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_SYSTEM_CONFIG,
                AuditTrailConsts.FUNCTION_ERROR_MESSAGES_MANAGEMENT);
        HttpServletRequest request = bpc.request;
        String task = ParamUtil.getString(request,TASK_LIST);
        ParamUtil.setSessionAttr(request, TASK_LIST, task);
        log.info("task is {}", task);
        IaisEGPHelper.clearSessionAttr(request, ApprovalApplicationConstants.class);
    }

    public void prepare(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String action = (String) ParamUtil.getRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_PAGE);
        if (StringUtil.isEmpty(action)) {
            action = "PrepareForms";
        }
        List<SelectOption> biologicalSchedule1 = getSelectOptionList(ApprovalApplicationConstants.SCHEDULE_1);
        List<SelectOption> biologicalSchedule2 = getSelectOptionList(ApprovalApplicationConstants.SCHEDULE_2);
        List<SelectOption> biologicalSchedule3 = getSelectOptionList(ApprovalApplicationConstants.SCHEDULE_3);
        List<SelectOption> biologicalSchedule4 = getSelectOptionList(ApprovalApplicationConstants.SCHEDULE_4);
        List<SelectOption> biologicalSchedule5 = getSelectOptionList(ApprovalApplicationConstants.SCHEDULE_5);
        List<SelectOption> biologicalSchedule6 = getSelectOptionList(ApprovalApplicationConstants.SCHEDULE_6);
        ParamUtil.setSessionAttr(request, "biologicalSchedule1", (Serializable) biologicalSchedule1);
        ParamUtil.setSessionAttr(request, "biologicalSchedule2", (Serializable) biologicalSchedule2);
        ParamUtil.setSessionAttr(request, "biologicalSchedule3", (Serializable) biologicalSchedule3);
        ParamUtil.setSessionAttr(request, "biologicalSchedule4", (Serializable) biologicalSchedule4);
        ParamUtil.setSessionAttr(request, "biologicalSchedule5", (Serializable) biologicalSchedule5);
        ParamUtil.setSessionAttr(request, "biologicalSchedule6", (Serializable) biologicalSchedule6);
        ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE, action);
        log.info(StringUtil.changeForLog("prepare(action):"+action));
    }

    public void prepareDocuments(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        int sysFileSize = systemParamConfig.getUploadFileLimit();
        ParamUtil.setRequestAttr(request, "sysFileSize", sysFileSize);
        String sysFileType = systemParamConfig.getUploadFileType();
        String[] sysFileTypeArr = FileUtils.fileTypeToArray(sysFileType);
        StringBuilder fileTypeStr = new StringBuilder();
        if (sysFileTypeArr != null) {
            int i = 0;
            int fileTypeLength = sysFileTypeArr.length;
            for (String fileType : sysFileTypeArr) {
                fileTypeStr.append(' ').append(fileType);
                if (fileTypeLength > 1 && i < fileTypeLength - 2) {
                    fileTypeStr.append(',');
                }
                if (fileTypeLength > 1 && i == sysFileTypeArr.length - 2) {
                    fileTypeStr.append(" and");
                }
                if (i == fileTypeLength - 1) {
                    fileTypeStr.append('.');
                }
                i++;
            }
        }
        ParamUtil.setRequestAttr(request, "sysFileType", fileTypeStr.toString());
    }

    public void preparePreview(BaseProcessClass bpc) {
    }

    public void prepareForms(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String task = (String)ParamUtil.getSessionAttr(request,TASK_LIST);
        List<FacilityQueryDto> facilityByApprovalStatus = approvalApplicationClient.getFacilityByApprovalType(task).getEntity();
        List<SelectOption> facilityNameList =  IaisCommonUtils.genNewArrayList();
        for (FacilityQueryDto dto : facilityByApprovalStatus) {
            facilityNameList.add(new SelectOption(dto.getId(),dto.getFacilityName()));
        }
        ParamUtil.setRequestAttr(request, "facilityNameSelect", facilityNameList);
    }

    public void prepareJump(BaseProcessClass bpc) {
    }

    public void doDocuments(BaseProcessClass bpc) {
    }

    public void doPreview(BaseProcessClass bpc) {
    }

    public List<String> stringArrayToList(String[] strings){
        List<String> list = null;
        if (!StringUtil.isEmpty(strings)){
            list = Arrays.asList(strings);
        }
        return list;
    }

    public void doForms(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request = bpc.request;
        String task = (String)ParamUtil.getSessionAttr(request,TASK_LIST);
        String facilityId = ParamUtil.getString(request, ApprovalApplicationConstants.FACILITY_ID);
        String facilityName = ParamUtil.getString(request, ApprovalApplicationConstants.FACILITY_NAME);
        String schedule = ParamUtil.getString(request, ApprovalApplicationConstants.SCHEDULE);
        String estimatedMaximumVolume = ParamUtil.getString(request, ApprovalApplicationConstants.ESTIMATED_MAXIMUM_VOLUME);
        String methodOrSystemUsedForLargeScaleProduction = ParamUtil.getString(request, ApprovalApplicationConstants.METHOD_OR_SYSTEM_USER_FOR_LARGE_SCALE_PRODUCTION);
        String[] listOfAgentsOrToxinsArray = ParamUtil.getStrings(request, ApprovalApplicationConstants.LIST_OF_AGENTS_OR_TOXINS);
        List<String> listOfAgentsOrToxins = stringArrayToList(listOfAgentsOrToxinsArray);
        String[] natureArray = ParamUtil.getStrings(request, ApprovalApplicationConstants.NATURE_OF_THE_SAMPLE);
        List<String> natureList = null;
        StringBuilder natureBuilder = new StringBuilder();
        if (!StringUtil.isEmpty(natureArray)){
            natureList = Arrays.asList(natureArray);
            for (int i = 0; i < natureArray.length; i++) {
                natureBuilder.append(natureArray[i]);
                if (i != (natureArray.length-1)){
                    natureBuilder.append(",");
                }
            }
        }
        String natureOfTheSample = natureBuilder.toString();
        String others = ParamUtil.getString(request, ApprovalApplicationConstants.OTHERS);
        String modeOfProcurement = ParamUtil.getString(request, ApprovalApplicationConstants.MODE_OF_PROCUREMENT);
        String transferFromFacilityName = ParamUtil.getString(request, ApprovalApplicationConstants.TRANSFER_FROM_FACILITY_NAME);
        String expectedDateOfTransfer = ParamUtil.getString(request, ApprovalApplicationConstants.EXPECTED_DATE_OF_TRANSFER);
        String contactPersonFromTransferringFacility = ParamUtil.getString(request, ApprovalApplicationConstants.CONTACT_PERSON_FROM_TRANSFERRING_FACILITY);
        String contactNoOfContactPersonFromTransferringFacility = ParamUtil.getString(request, ApprovalApplicationConstants.CONTACT_NO_OF_CONTACT_PERSON_FROM_TRANSFERRING_FACILITY);
        String emailAddressOfContactPersonFromTransferringFacility = ParamUtil.getString(request, ApprovalApplicationConstants.EMAIL_ADDRESS_OF_CONTACT_PERSON_FROM_TRANSFERRING_FACILITY);
        String overseasFacilityName = ParamUtil.getString(request, ApprovalApplicationConstants.OVERSEAS_FACILITY_NAME);
        String expectedDateOfImport = ParamUtil.getString(request, ApprovalApplicationConstants.EXPECTED_DATE_OF_IMPORT);
        String contactPersonFromSourceFacility = ParamUtil.getString(request, ApprovalApplicationConstants.CONTACT_PERSON_FROM_SOURCE_FACILITY);
        String emailAddressOfContactPersonFromSourceFacility = ParamUtil.getString(request, ApprovalApplicationConstants.EMAIL_ADDRESS_OF_CONTACT_PERSON_FROM_SOURCE_FACILITY);
        String facilityAddress1 = ParamUtil.getString(request, ApprovalApplicationConstants.FACILITY_ADDRESS_1);
        String facilityAddress2 = ParamUtil.getString(request, ApprovalApplicationConstants.FACILITY_ADDRESS_2);
        String facilityAddress3 = ParamUtil.getString(request, ApprovalApplicationConstants.FACILITY_ADDRESS_3);
        String country = ParamUtil.getString(request, ApprovalApplicationConstants.COUNTRY);
        String city = ParamUtil.getString(request, ApprovalApplicationConstants.CITY);
        String state = ParamUtil.getString(request, ApprovalApplicationConstants.STATE);
        String postalCode = ParamUtil.getString(request, ApprovalApplicationConstants.POSTAL_CODE);
        String nameOfCourierServiceProvider = ParamUtil.getString(request, ApprovalApplicationConstants.NAME_OF_COURIER_SERVICE_PROVIDER);
        String nameOfProject = ParamUtil.getString(request, ApprovalApplicationConstants.NAME_OF_PROJECT);
        String nameOfPrincipalInvestigator = ParamUtil.getString(request, ApprovalApplicationConstants.NAME_OF_PRINCIPAL_INVESTIGATOR);
        String intendedWorkActivity = ParamUtil.getString(request, ApprovalApplicationConstants.INTENDED_WORK_ACTIVITY);
        String startDate = ParamUtil.getString(request, ApprovalApplicationConstants.START_DATE);
        String endDate = ParamUtil.getString(request, ApprovalApplicationConstants.END_DATE);
        String remarks = ParamUtil.getString(request, ApprovalApplicationConstants.REMARKS);
        String checkbox1 = ParamUtil.getString(request,ApprovalApplicationConstants.CHECKBOX_1);
        String checkbox2 = ParamUtil.getString(request,ApprovalApplicationConstants.CHECKBOX_2);
        ApprovalApplicationDto approvalApplicationDto = new ApprovalApplicationDto();
        Facility facility = new Facility();
        facility.setId(facilityId);
        facility.setFacilityName(facilityName);
        approvalApplicationDto.setFacility(facility);
        if(task.equals("APPRTY001")){
            approvalApplicationDto.setSampleNature(natureOfTheSample);
            approvalApplicationDto.setSampleNatureOth(others);
        }else if(task.equals("APPRTY002")){
            approvalApplicationDto.setProdMaxVolumeLitres(estimatedMaximumVolume);
            approvalApplicationDto.setLspMethod(methodOrSystemUsedForLargeScaleProduction);
        }else if(task.equals("APPRTY003")){
            approvalApplicationDto.setPrjName(nameOfProject);
            approvalApplicationDto.setPrincipalInvestigatorName(nameOfPrincipalInvestigator);
            approvalApplicationDto.setWorkActivityIntended(intendedWorkActivity);
            approvalApplicationDto.setStartDate(Formatter.parseDate(startDate));
            approvalApplicationDto.setEndDate(Formatter.parseDate(endDate));
        }
        approvalApplicationDto.setSchedule(schedule);
//        approvalApplicationDto.setListOfAgentsOrToxins(listOfAgentsOrToxins);
        approvalApplicationDto.setProcurementMode(modeOfProcurement);
        if (modeOfProcurement != null){
            if (modeOfProcurement.equals("BMOP001")){
                approvalApplicationDto.setFacTransferForm(transferFromFacilityName);
                approvalApplicationDto.setTransferExpectedDate(Formatter.parseDate(expectedDateOfTransfer));
                approvalApplicationDto.setImpCtcPersonName(contactPersonFromTransferringFacility);
                approvalApplicationDto.setImpCtcPersonNo(contactNoOfContactPersonFromTransferringFacility);
                approvalApplicationDto.setImpCtcPersonEmail(emailAddressOfContactPersonFromTransferringFacility);
            }else if(modeOfProcurement.equals("BMOP002")){
                approvalApplicationDto.setFacTransferForm(overseasFacilityName);
                approvalApplicationDto.setTransferExpectedDate(Formatter.parseDate(expectedDateOfImport));
                approvalApplicationDto.setImpCtcPersonName(contactPersonFromSourceFacility);
                approvalApplicationDto.setImpCtcPersonEmail(emailAddressOfContactPersonFromSourceFacility);
            }
        }
        approvalApplicationDto.setTransferFacAddr1(facilityAddress1);
        approvalApplicationDto.setTransferFacAddr2(facilityAddress2);
        approvalApplicationDto.setTransferFacAddr3(facilityAddress3);
        approvalApplicationDto.setTransferCountry(country);
        approvalApplicationDto.setTransferCity(city);
        approvalApplicationDto.setTransferState(state);
        approvalApplicationDto.setTransferPostalCode(postalCode);
        approvalApplicationDto.setCourierServiceProviderName(nameOfCourierServiceProvider);
        approvalApplicationDto.setRemarks(remarks);
        approvalApplicationDto.setCheckbox1(checkbox1);
        approvalApplicationDto.setCheckbox2(checkbox2);
        ParamUtil.setSessionAttr(request,ApprovalApplicationConstants.APPROVAL_APPLICATION_DTO_ATTR, approvalApplicationDto);
        ParamUtil.setSessionAttr(request,ApprovalApplicationConstants.NATURE_LIST_ATTR, (Serializable) natureList);
//        String propertyName = "";
//        if (modeOfProcurement == null) {
//            propertyName = "nullMode";
//        }else{
//            if (task == "APPRTY001" && modeOfProcurement == "BMOP001" && others == null){
//                propertyName = "possessLocal";
//            }else if (task == "APPRTY001" && modeOfProcurement == "BMOP001" && others != null){
//                propertyName = "possessLocalOthers";
//            }else if (task == "APPRTY001" && modeOfProcurement == "BMOP002" && others == null){
//                propertyName = "possessImport";
//            }else if (task == "APPRTY001" && modeOfProcurement == "BMOP002" && others != null){
//                propertyName = "possessImportOthers";
//            }else if (task == "APPRTY002" && modeOfProcurement == "BMOP001"){
//                propertyName = "largeLocal";
//            }else if (task == "APPRTY002" && modeOfProcurement == "BMOP002"){
//                propertyName = "largeImport";
//            }else if (task == "APPRTY003"){
//                propertyName = "special";
//            }
//        }
//        ValidationResult vResult = WebValidationHelper.validateProperty(approvalApplicationDto, propertyName);
//        if(vResult != null && vResult.isHasErrors()){
//            Map<String,String> errorMap = vResult.retrieveAll();
//            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
//            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"N");
//        }else {
//            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"Y");
//        }
        ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"Y");
    }

    public void controlSwitch(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String crud_action_type_form_page=ParamUtil.getString(request,"crud_action_type_form_page");
        ParamUtil.setRequestAttr(request,"crud_action_type_form_page",crud_action_type_form_page);
    }

    public void doSaveDraft(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ApprovalApplicationDto approvalApplicationDto = (ApprovalApplicationDto)ParamUtil.getSessionAttr(request, ApprovalApplicationConstants.APPROVAL_APPLICATION_DTO_ATTR);
        String task = (String)ParamUtil.getSessionAttr(request,TASK_LIST);
        String processType = "";
        if (task.equals("APPRTY001")){
            processType = "PROTYPE002";
        }else if (task.equals("APPRTY002")){
            processType = "PROTYPE003";
        }else if (task.equals("APPRTY003")){
            processType = "PROTYPE004";
        }
        String appType = "BSBAPTY001";
        String status = "BSBAPST011";
        Date applicationDt = new Date();
        approvalApplicationDto.setProcessType(processType);
        approvalApplicationDto.setAppType(appType);
        approvalApplicationDto.setStatus(status);
        approvalApplicationDto.setApplicationDt(applicationDt);
        approvalApplicationClient.saveApproval(approvalApplicationDto);
    }

    public void doSubmit(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ApprovalApplicationDto approvalApplicationDto = (ApprovalApplicationDto)ParamUtil.getSessionAttr(request, ApprovalApplicationConstants.APPROVAL_APPLICATION_DTO_ATTR);
        String task = (String)ParamUtil.getSessionAttr(request,TASK_LIST);
        String processType = "";
        if (task.equals("APPRTY001")){
            processType = "PROTYPE002";
        }else if (task.equals("APPRTY002")){
            processType = "PROTYPE003";
        }else if (task.equals("APPRTY003")){
            processType = "PROTYPE004";
        }
        String appType = "BSBAPTY001";
        String status = "BSBAPST001";
        Date applicationDt = new Date();
        approvalApplicationDto.setProcessType(processType);
        approvalApplicationDto.setAppType(appType);
        approvalApplicationDto.setStatus(status);
        approvalApplicationDto.setApplicationDt(applicationDt);
        approvalApplicationClient.saveApproval(approvalApplicationDto);
    }

    public List<SelectOption> getSelectOptionList(String schedule){
        List<BiologicalQueryDto> biological = approvalApplicationClient.getBiologicalBySchedule(schedule).getEntity();
        List<SelectOption> biologicalSchedule =  IaisCommonUtils.genNewArrayList();
        if (biological != null && biological.size() > 0){
            for (BiologicalQueryDto dto : biological) {
                biologicalSchedule.add(new SelectOption(dto.getId(),dto.getName()));
            }
        }
        return biologicalSchedule;
    }
}

