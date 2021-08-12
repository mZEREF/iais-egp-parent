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
import sg.gov.moh.iais.egp.bsb.dto.approval.BsbFacilityQueryDto;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

/**
 * @author : LiRan
 * @date : 2021/8/11
 */
@Delegator
@Slf4j
public class NewApprovalDelegator {

    public static final String APPROVAL_STATUS_1 = "PROTYPE002";
    public static final String APPROVAL_STATUS_2 = "PROTYPE008";
    public static final String APPROVAL_STATUS_3 = "PROTYPE014";
    public static final String TASK_LIST = "taskList";
    public static final String TASK_LIST_TYPE_1 = "Approval To Possess";
    public static final String TASK_LIST_TYPE_2 = "Approval To LargeScaleProduce";
    public static final String TASK_LIST_TYPE_3 = "Approval To Special";

    private final ApprovalApplicationClient approvalApplicationClient;

    @Autowired
    public NewApprovalDelegator(ApprovalApplicationClient approvalApplicationClient) {
        this.approvalApplicationClient = approvalApplicationClient;
    }

    @Autowired
    private SystemParamConfig systemParamConfig;

    public void doStart(BaseProcessClass bpc) throws IllegalAccessException {
        log.info(StringUtil.changeForLog("the doStart start ...."));
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_SYSTEM_CONFIG,
                AuditTrailConsts.FUNCTION_ERROR_MESSAGES_MANAGEMENT);
        HttpServletRequest request = bpc.request;
        IaisEGPHelper.clearSessionAttr(request, ApprovalApplicationConstants.class);
    }

    public void prepare(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the prepare start ...."));
        HttpServletRequest request = bpc.request;
        String task = "";
        String action = (String) ParamUtil.getRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if (StringUtil.isEmpty(action)) {
            action = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
            if (StringUtil.isEmpty(action) || "validation".equals(action)) {
                task = ParamUtil.getString(request,TASK_LIST);
                action = "PrepareForms";
            }else{
                task = (String)ParamUtil.getSessionAttr(request,TASK_LIST);
            }
        }
        ParamUtil.setSessionAttr(request, TASK_LIST, task);
        ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE, action);
        log.info(StringUtil.changeForLog("prepare(action):"+action));
    }

    public void prepareDocuments(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the prepareDocuments start ...."));
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
        log.info(StringUtil.changeForLog("the preparePreview start ...."));
    }

    public void prepareForms(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the prepareForms start ...."));
        HttpServletRequest request = bpc.request;
        List<BsbFacilityQueryDto> facilityByApprovalStatus = approvalApplicationClient.getFacilityByApprovalStatus(APPROVAL_STATUS_1).getEntity();
        List<SelectOption> facilityNameList =  IaisCommonUtils.genNewArrayList();
        for (BsbFacilityQueryDto dto : facilityByApprovalStatus) {
            facilityNameList.add(new SelectOption(dto.getId(),dto.getFacilityName()));
        }
        ParamUtil.setRequestAttr(request, "facilityNameSelect", facilityNameList);
    }

    public void prepareJump(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the prepareJump start ...."));
    }

    public void doDocuments(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the doDocuments start ...."));
    }

    public void doPreview(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the doPreview start ...."));
    }

    public void doForms(BaseProcessClass bpc) throws ParseException {
        log.info(StringUtil.changeForLog("the doForms start ...."));
        HttpServletRequest request = bpc.request;
        String task = (String)ParamUtil.getSessionAttr(request,TASK_LIST);
        String facilityId = ParamUtil.getString(request, ApprovalApplicationConstants.FACILITY_ID);
        String facilityName = ParamUtil.getString(request, ApprovalApplicationConstants.FACILITY_NAME);
        String schedule = ParamUtil.getString(request, ApprovalApplicationConstants.SCHEDULE);
        String estimatedMaximumVolume = ParamUtil.getString(request, ApprovalApplicationConstants.ESTIMATED_MAXIMUM_VOLUME);
        String methodOrSystemUsedForLargeScaleProduction = ParamUtil.getString(request, ApprovalApplicationConstants.METHOD_OR_SYSTEM_USER_FOR_LARGE_SCALE_PRODUCTION);
        String listOfAgentsOrToxins = ParamUtil.getString(request, ApprovalApplicationConstants.LIST_OF_AGENTS_OR_TOXINS);
        String[] biologicalIdArray = ParamUtil.getStrings(request, ApprovalApplicationConstants.BIOLOGICAL_ID);
        List<String> biologicalIdList = null;
        StringBuilder biologicalIdBuilder = new StringBuilder();
        if (!StringUtil.isEmpty(biologicalIdArray)){
            biologicalIdList = Arrays.asList(biologicalIdArray);
            for (int i = 0; i < biologicalIdArray.length; i++) {
                biologicalIdBuilder.append(biologicalIdArray[i]);
                if (i != (biologicalIdArray.length-1)){
                    biologicalIdBuilder.append(",");
                }
            }
        }
        String biologicalId = biologicalIdBuilder.toString();
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
        ApprovalApplicationDto approvalApplicationDto = new ApprovalApplicationDto();
        approvalApplicationDto.setFacilityId(facilityId);
        approvalApplicationDto.setBiologicalId(biologicalId);
        if(task.equals(TASK_LIST_TYPE_1)){
            approvalApplicationDto.setSampleNature(natureOfTheSample);
            approvalApplicationDto.setSampleNatureOth(others);
        }else if(task.equals(TASK_LIST_TYPE_2)){
            approvalApplicationDto.setProductionMaximumVolumeLitres(estimatedMaximumVolume);
            approvalApplicationDto.setLargeScaleProductionMethod(methodOrSystemUsedForLargeScaleProduction);
        }else if(task.equals(TASK_LIST_TYPE_3)){
            approvalApplicationDto.setProjectName(nameOfProject);
            approvalApplicationDto.setPrincipalInvestigatorName(nameOfPrincipalInvestigator);
            approvalApplicationDto.setWorkActivityIntended(intendedWorkActivity);
            approvalApplicationDto.setStartDt(Formatter.parseDate(startDate));
            approvalApplicationDto.setEndDt(Formatter.parseDate(endDate));
        }
        approvalApplicationDto.setFacilityName(facilityName);
        approvalApplicationDto.setSchedule(schedule);
        approvalApplicationDto.setListOfAgentsOrToxins(listOfAgentsOrToxins);
        approvalApplicationDto.setProcurementMode(modeOfProcurement);
        if (modeOfProcurement != null){
            if (modeOfProcurement.equals("BMOP001")){
                approvalApplicationDto.setFacilityTransferFrom(transferFromFacilityName);
                approvalApplicationDto.setTransferExpectedDt(Formatter.parseDate(expectedDateOfTransfer));
                approvalApplicationDto.setImportContactPersonName(contactPersonFromTransferringFacility);
                approvalApplicationDto.setImportContactPersonNo(contactNoOfContactPersonFromTransferringFacility);
                approvalApplicationDto.setImportContactPersonEmail(emailAddressOfContactPersonFromTransferringFacility);
            }else if(modeOfProcurement.equals("BMOP002")){
                approvalApplicationDto.setFacilityTransferFrom(overseasFacilityName);
                approvalApplicationDto.setTransferExpectedDt(Formatter.parseDate(expectedDateOfImport));
                approvalApplicationDto.setImportContactPersonName(contactPersonFromSourceFacility);
                approvalApplicationDto.setImportContactPersonEmail(emailAddressOfContactPersonFromSourceFacility);
            }
        }
        approvalApplicationDto.setTransferFacilityAddr1(facilityAddress1);
        approvalApplicationDto.setTransferFacilityAddr2(facilityAddress2);
        approvalApplicationDto.setTransferFacilityAddr3(facilityAddress3);
        approvalApplicationDto.setTransferCountry(country);
        approvalApplicationDto.setTransferCity(city);
        approvalApplicationDto.setTransferState(state);
        approvalApplicationDto.setTransferPostalCode(postalCode);
        approvalApplicationDto.setCourierServiceProviderName(nameOfCourierServiceProvider);
        approvalApplicationDto.setRemarks(remarks);
        ParamUtil.setSessionAttr(request,ApprovalApplicationConstants.APPROVAL_APPLICATION_DTO_ATTR, approvalApplicationDto);
        ParamUtil.setSessionAttr(request,ApprovalApplicationConstants.AGENTS_OR_TOXINS_LIST_ATTR, (Serializable) biologicalIdList);
        ParamUtil.setSessionAttr(request,ApprovalApplicationConstants.NATURE_LIST_ATTR, (Serializable) natureList);
        ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"Y");
        approvalApplicationClient.saveApproval(approvalApplicationDto);
    }

    public void controlSwitch(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the controlSwitch start ...."));
        String switch2 = "loading";
        ParamUtil.setRequestAttr(bpc.request, "Switch2", switch2);
    }

    public void doSaveDraft(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the doSaveDraft start ...."));
    }

    public void doSubmit(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the doSubmit start ...."));
        HttpServletRequest request = bpc.request;
        Object approvalApplicationDto = ParamUtil.getSessionAttr(request, ApprovalApplicationConstants.APPROVAL_APPLICATION_DTO_ATTR);

    }

}

