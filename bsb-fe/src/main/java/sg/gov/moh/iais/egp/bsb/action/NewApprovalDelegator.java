package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.*;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.*;
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
import java.util.*;

/**
 * @author : LiRan
 * @date : 2021/8/11
 */
@Delegator
@Slf4j
public class NewApprovalDelegator {

    public static final String TASK_LIST = "taskList";

    @Autowired
    private  ApprovalApplicationClient approvalApplicationClient;

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

    public void prepareCompanyInfo(BaseProcessClass bpc) {

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
        ParamUtil.setSessionAttr(request, ApprovalApplicationConstants.BIOLOGICAL_SCHEDULE_1, (Serializable) biologicalSchedule1);
        ParamUtil.setSessionAttr(request, ApprovalApplicationConstants.BIOLOGICAL_SCHEDULE_2, (Serializable) biologicalSchedule2);
        ParamUtil.setSessionAttr(request, ApprovalApplicationConstants.BIOLOGICAL_SCHEDULE_3, (Serializable) biologicalSchedule3);
        ParamUtil.setSessionAttr(request, ApprovalApplicationConstants.BIOLOGICAL_SCHEDULE_4, (Serializable) biologicalSchedule4);
        ParamUtil.setSessionAttr(request, ApprovalApplicationConstants.BIOLOGICAL_SCHEDULE_5, (Serializable) biologicalSchedule5);
        ParamUtil.setSessionAttr(request, ApprovalApplicationConstants.BIOLOGICAL_SCHEDULE_6, (Serializable) biologicalSchedule6);
        ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE, action);
        log.info(StringUtil.changeForLog("prepare(action):"+action));
    }

    public void prepareDocuments(BaseProcessClass bpc) {
        int sysFileSize = systemParamConfig.getUploadFileLimit();
        ParamUtil.setRequestAttr(bpc.request, "sysFileSize", sysFileSize);
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
        ParamUtil.setRequestAttr(bpc.request, "sysFileType", fileTypeStr.toString());
        log.info(StringUtil.changeForLog("the do prepareDocuments end ...."));
    }

    public void preparePreview(BaseProcessClass bpc) {
    }
    public void prepareJump(BaseProcessClass bpc) {
    }
    public void doDocuments(BaseProcessClass bpc) {
    }
    public void doPreview(BaseProcessClass bpc) {
    }

    public void prepareForms(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String task = (String)ParamUtil.getSessionAttr(request,TASK_LIST);
        List<FacilityQueryDto> facilityByApprovalStatus = approvalApplicationClient.getFacilityByApprovalType(task).getEntity();
        List<SelectOption> facilityNameList =  new ArrayList<>();
        for (FacilityQueryDto dto : facilityByApprovalStatus) {
            facilityNameList.add(new SelectOption(dto.getId(),dto.getFacilityName()));
        }
        ParamUtil.setRequestAttr(request, ApprovalApplicationConstants.FACILITY_NAME_SELECT, facilityNameList);
    }

    public void doForms(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request = bpc.request;
        ApprovalApplicationDto approvalApplicationDto = getDtoByForm(bpc);
        //validate by different application status and type
        String validateStatus = "";

        ValidationResult vResult = WebValidationHelper.validateProperty(approvalApplicationDto,validateStatus);
        if(vResult != null && vResult.isHasErrors()){
            Map<String,String> errorMap = vResult.retrieveAll();
//            ParamUtil.setRequestAttr(request, ProcessContants.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
//            crudActionType = ProcessContants.CRUD_ACTION_TYPE_2;
        }
        ParamUtil.setSessionAttr(request,ApprovalApplicationConstants.APPROVAL_APPLICATION_DTO_ATTR, approvalApplicationDto);
        ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"Y");
    }

    public void controlSwitch(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String crud_action_type_form_page = ParamUtil.getString(request,ApprovalApplicationConstants.CRUD_ACTION_TYPE_FROM_PAGE);
        String crud_action_type = ParamUtil.getString(request,ApprovalApplicationConstants.CRUD_ACTION_TYPE);
        ParamUtil.setRequestAttr(request,ApprovalApplicationConstants.CRUD_ACTION_TYPE_FROM_PAGE,crud_action_type_form_page);
        ParamUtil.setRequestAttr(request,ApprovalApplicationConstants.CRUD_ACTION_TYPE,crud_action_type);
    }

    public void doSaveDraft(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ApprovalApplicationDto approvalApplicationDto = (ApprovalApplicationDto)ParamUtil.getSessionAttr(request, ApprovalApplicationConstants.APPROVAL_APPLICATION_DTO_ATTR);
        String status = ApprovalApplicationConstants.APP_STATUS_11;
        approvalApplicationDto.setStatus(status);
        approvalApplicationClient.saveApproval(approvalApplicationDto);
    }

    public void doSubmit(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ApprovalApplicationDto approvalApplicationDto = (ApprovalApplicationDto)ParamUtil.getSessionAttr(request, ApprovalApplicationConstants.APPROVAL_APPLICATION_DTO_ATTR);
        String status = ApprovalApplicationConstants.APP_STATUS_1;
        approvalApplicationDto.setStatus(status);
        approvalApplicationClient.saveApproval(approvalApplicationDto);
    }

    private ApprovalApplicationDto getDtoByForm(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request = bpc.request;
        String task = (String)ParamUtil.getSessionAttr(request,TASK_LIST);
        String facilityId = ParamUtil.getString(request, ApprovalApplicationConstants.FACILITY_ID);
        String facilityName = ParamUtil.getString(request, ApprovalApplicationConstants.FACILITY_NAME);
        String schedule = ParamUtil.getString(request, ApprovalApplicationConstants.SCHEDULE);
        String estimatedMaximumVolume = ParamUtil.getString(request, ApprovalApplicationConstants.ESTIMATED_MAXIMUM_VOLUME);
        String methodOrSystemUsedForLargeScaleProduction = ParamUtil.getString(request, ApprovalApplicationConstants.METHOD_OR_SYSTEM_USER_FOR_LARGE_SCALE_PRODUCTION);
        List<String> listOfAgentsOrToxinsList = stringArrayToList(ParamUtil.getStrings(request, ApprovalApplicationConstants.LIST_OF_AGENTS_OR_TOXINS));
        //preview biological info
        List<String> biologicalNameList = new ArrayList<>();
        if (listOfAgentsOrToxinsList != null){
            for (int i = 0; i < listOfAgentsOrToxinsList.size(); i++) {
                biologicalNameList.add(approvalApplicationClient.getBiologicalById(listOfAgentsOrToxinsList.get(i)).getEntity().getName());
            }
        }
        String biologicalName = listToString(biologicalNameList);
        List<String> natureOfTheSampleList = stringArrayToList(ParamUtil.getStrings(request, ApprovalApplicationConstants.NATURE_OF_THE_SAMPLE));
        String sampleNature = listToString(natureOfTheSampleList);
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
        String appType = ApprovalApplicationConstants.APP_TYPE_1;
        Date applicationDt = new Date();
        String processType = "";
        if (task.equals(ApprovalApplicationConstants.APPROVAL_TYPE_1)){
            processType = ApprovalApplicationConstants.PROCESS_TYPE_2;
        }else if (task.equals(ApprovalApplicationConstants.APPROVAL_TYPE_2)){
            processType = ApprovalApplicationConstants.PROCESS_TYPE_3;
        }else if (task.equals(ApprovalApplicationConstants.APPROVAL_TYPE_3)){
            processType = ApprovalApplicationConstants.PROCESS_TYPE_4;
        }
        ApprovalApplicationDto approvalApplicationDto = new ApprovalApplicationDto();
        Facility facility = new Facility();
        facility.setId(facilityId);
        facility.setFacilityName(facilityName);
        approvalApplicationDto.setFacility(facility);
        approvalApplicationDto.setNatureOfTheSampleList(natureOfTheSampleList);
        approvalApplicationDto.setSampleNature(sampleNature);
        approvalApplicationDto.setSampleNatureOth(others);
        approvalApplicationDto.setProdMaxVolumeLitres(estimatedMaximumVolume);
        approvalApplicationDto.setLspMethod(methodOrSystemUsedForLargeScaleProduction);
        approvalApplicationDto.setPrjName(nameOfProject);
        approvalApplicationDto.setPrincipalInvestigatorName(nameOfPrincipalInvestigator);
        approvalApplicationDto.setWorkActivityIntended(intendedWorkActivity);
        approvalApplicationDto.setStartDate(Formatter.parseDate(startDate));
        approvalApplicationDto.setEndDate(Formatter.parseDate(endDate));
        approvalApplicationDto.setSchedule(schedule);
        approvalApplicationDto.setBiologicalIdList(listOfAgentsOrToxinsList);
        approvalApplicationDto.setProcurementMode(modeOfProcurement);
        approvalApplicationDto.setBiologicalName(biologicalName);
        if (modeOfProcurement != null){
            if (modeOfProcurement.equals(ApprovalApplicationConstants.MODE_OF_PROCUREMENT_1)){
                approvalApplicationDto.setFacTransferForm(transferFromFacilityName);
                approvalApplicationDto.setTransferExpectedDate(Formatter.parseDate(expectedDateOfTransfer));
                approvalApplicationDto.setImpCtcPersonName(contactPersonFromTransferringFacility);
                approvalApplicationDto.setImpCtcPersonNo(contactNoOfContactPersonFromTransferringFacility);
                approvalApplicationDto.setImpCtcPersonEmail(emailAddressOfContactPersonFromTransferringFacility);
            }else if(modeOfProcurement.equals(ApprovalApplicationConstants.MODE_OF_PROCUREMENT_2)){
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
        approvalApplicationDto.setAppType(appType);
        approvalApplicationDto.setProcessType(processType);
        approvalApplicationDto.setApplicationDt(applicationDt);
        return approvalApplicationDto;
    }

    private List<String> stringArrayToList(String[] strings){
        List<String> list = null;
        if (!StringUtil.isEmpty(strings)){
            list = Arrays.asList(strings);
        }
        return list;
    }

    private String listToString(List<String> list){
        StringBuilder stringBuilder = new StringBuilder();
        if (list != null && list.size() > 0){
            for (int i = 0; i < list.size(); i++) {
                stringBuilder.append(list.get(i));
                if (i < list.size()-1){
                    stringBuilder.append(",");
                }
            }
        }
        return stringBuilder.toString();
    }

    private List<SelectOption> getSelectOptionList(String schedule){
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

