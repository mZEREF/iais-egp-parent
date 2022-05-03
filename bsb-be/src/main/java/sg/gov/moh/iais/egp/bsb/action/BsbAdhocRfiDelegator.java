package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.CommonValidator;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.client.AdhocRfiClient;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.adhocRfi.AdhocRfiQueryDto;
import sg.gov.moh.iais.egp.bsb.dto.adhocRfi.AdhocRfiQueryResultDto;
import sg.gov.moh.iais.egp.bsb.dto.adhocRfi.NewAdhocRfiDto;
import sg.gov.moh.iais.egp.bsb.dto.adhocRfi.ViewAdhocRfiDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.*;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.DateUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.rmi.CORBA.Util;
import javax.servlet.http.HttpServletRequest;

import java.io.Serializable;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.RevocationConstants.PARAM_APPROVAL_ID;

@Slf4j
@Delegator("bsbAdhocRfiDelegator")
public class BsbAdhocRfiDelegator {
    private static final String ACTION_TYPE = "action_type";
    private static final String IS_VALIDATE = "isValidate";
    public static final String ACTION_VALUE = "action_value";
    public static final String ACTION_ADDT = "action_additional";
    private static final String KEY_ADHOC_RFI_LIST = "infoList";
    public static final String KEY_ADHOC_LIST_SEARCH_DTO = "adhocSearchDto";
    public static final String KEY_ADHOC_PAGE_INFO = "pageInfo";
    private final AdhocRfiClient adhocRfiClient;


    public BsbAdhocRfiDelegator(AdhocRfiClient adhocRfiClient) {
        this.adhocRfiClient = adhocRfiClient;
    }

    public void doStart(BaseProcessClass bpc){
        log.info("=======>>>>>start>>>>>>>>>>>>>>>>bsbAdhocRfiDelegator");
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, KEY_ADHOC_LIST_SEARCH_DTO, null);
        ParamUtil.setSessionAttr(request, "newReqInfo", null);
        ParamUtil.setSessionAttr(request, "viewReqInfo", null);
        String approvalId=ParamUtil.getMaskedString(request,"approvalId");
        ParamUtil.setSessionAttr(request, "approvalId", approvalId);

    }
    /**
     * preAdhocRfi
     */
    public void preAdhocRfi(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, KEY_ADHOC_RFI_LIST, null);

        String approvalId = (String) ParamUtil.getSessionAttr(request, "approvalId");
        AdhocRfiQueryDto searchDto = getSearchDto(request);
        searchDto.setApprovalId(approvalId);
        ParamUtil.setSessionAttr(request, KEY_ADHOC_LIST_SEARCH_DTO, searchDto);
        ResponseDto<AdhocRfiQueryResultDto> resultDto = adhocRfiClient.queryAdhocRfi(searchDto);
        if (resultDto.ok()) {
            ParamUtil.setRequestAttr(request, KEY_ADHOC_PAGE_INFO, resultDto.getEntity().getPageInfo());
            List<AdhocRfiDto> reqInfos = resultDto.getEntity().getRfiList();
            ParamUtil.setSessionAttr(request, KEY_ADHOC_RFI_LIST, (Serializable) reqInfos);
        } else {
            log.warn("get adhocRfi API doesn't return ok, the response is {}", resultDto);
            ParamUtil.setRequestAttr(request, KEY_ADHOC_PAGE_INFO, PageInfo.emptyPageInfo(searchDto));
            ParamUtil.setRequestAttr(request, KEY_ADHOC_RFI_LIST, new ArrayList<>());
        }
    }
    /**
     * doAdhocRfi
     */
    public void doAdhocRfi(BaseProcessClass bpc){
        log.info("=======>>>>>start>>>>>>>>>>>>>>>>bsbAdhocRfiDelegator");
        String actionType =  ParamUtil.getRequestString(bpc.request, ACTION_TYPE);
        log.info(StringUtil.changeForLog("----- Action Type: " + actionType + " -----"));


    }
    /**
     * doAdhocRfi
     */
    public void preViewAdhocRfi(BaseProcessClass bpc){
        log.info("=======>>>>>start>>>>>>>>>>>>>>>>preViewAdhocRfi");
        HttpServletRequest request = bpc.request;
        String id =  ParamUtil.getRequestString(bpc.request, ACTION_VALUE);
        log.info(StringUtil.changeForLog("----- Action value: " + id + " -----"));
        ViewAdhocRfiDto viewAdhocRfiDto = adhocRfiClient.findAdhocRfiById(id).getEntity();
        String isValidate = (String) ParamUtil.getRequestAttr(request,IS_VALIDATE);
        if(isValidate !=null && !isValidate.equals("true")){
            viewAdhocRfiDto = (ViewAdhocRfiDto) ParamUtil.getSessionAttr(request,"viewReqInfo");
        }
        if(!StringUtil.isEmpty(viewAdhocRfiDto.getDueDate())){
            viewAdhocRfiDto.setDueDateShow(viewAdhocRfiDto.getDueDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
        ParamUtil.setSessionAttr(request, "viewReqInfo", viewAdhocRfiDto);
        String[] status=new String[]{viewAdhocRfiDto.getStatus()};
        if(viewAdhocRfiDto.getStatus().equals("RFIST002")){
            status=new String[]{"RFIST004","RFIST003"};
        }
        if(viewAdhocRfiDto.getStatus().equals("RFIST004")){
            status=new String[]{"RFIST004"};
        }
        List<SelectOption> statusLists= MasterCodeUtil.retrieveOptionsByCodes(status);
        ParamUtil.setSessionAttr(bpc.request, "statusLists", (Serializable) statusLists);
    }
    /**
     * doUpdate
     */
    public void doUpdate(BaseProcessClass bpc) throws ParseException {
        log.info("=======>>>>>start>>>>>>>>>>>>>>>>doUpdate");
        HttpServletRequest request = bpc.request;
        ViewAdhocRfiDto viewAdhocRfiDto = (ViewAdhocRfiDto) ParamUtil.getSessionAttr(request,"viewReqInfo");
        String date = ParamUtil.getString(request,"dueDate");
        String status = ParamUtil.getString(request,"status");
        if(!StringUtil.isEmpty(date)&&CommonValidator.isDate(date)){
            viewAdhocRfiDto.setDueDate(LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            viewAdhocRfiDto.setDueDateShow(date);
        }
        viewAdhocRfiDto.setStatus(status);
        ValidationResultDto validationResultDto = adhocRfiClient.validateManualAdhocRfiUpdate(viewAdhocRfiDto);
        String isValidate;
        if (!validationResultDto.isPass()){
            ParamUtil.setRequestAttr(request, ValidationConstants.KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
            isValidate = "N";
        }else {
            isValidate = "Y";
        }
        ParamUtil.setRequestAttr(request, IS_VALIDATE, isValidate);
        ParamUtil.setSessionAttr(request, "viewReqInfo", viewAdhocRfiDto);
        if(isValidate.equals("N")){
            return;
        }
        adhocRfiClient.saveAdhocRfiView(viewAdhocRfiDto);
    }
    /**
     * doPaging
     */
    public void doPaging(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        AdhocRfiQueryDto searchDto = getSearchDto(request);
        String actionValue = ParamUtil.getString(request, ACTION_VALUE);
        switch (actionValue) {
            case "changeSize":
                int pageSize = ParamUtil.getInt(request, KEY_PAGE_SIZE);
                searchDto.setPage(0);
                searchDto.setSize(pageSize);
                break;
            case "changePage":
                int pageNo = ParamUtil.getInt(request, KEY_PAGE_NO);
                searchDto.setPage(pageNo - 1);
                break;
            default:
                log.warn("page, action_value is invalid: {}", actionValue);
                break;
        }
        ParamUtil.setSessionAttr(request, KEY_ADHOC_LIST_SEARCH_DTO, searchDto);
    }
    /**
     * doSort
     */
    public void doSort(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        AdhocRfiQueryDto searchDto = getSearchDto(request);
        String field = ParamUtil.getString(request, ACTION_VALUE);
        String sortType = ParamUtil.getString(request, ACTION_ADDT);
        searchDto.changeSort(field, sortType);
        ParamUtil.setSessionAttr(request, KEY_ADHOC_LIST_SEARCH_DTO, searchDto);
    }
    /**
     * preNewAdhocRfi
     */
    public void preNewAdhocRfi(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String approvalId = (String) ParamUtil.getSessionAttr(request, "approvalId");
        NewAdhocRfiDto newAdhocRfiDto = adhocRfiClient.queryPreNewData(approvalId).getEntity();
        String isValidate = (String) ParamUtil.getRequestAttr(request,IS_VALIDATE);
        if(isValidate !=null && !isValidate.equals("true")){
            newAdhocRfiDto = (NewAdhocRfiDto) ParamUtil.getRequestAttr(request,"newReqInfo");
        }
        if(!StringUtil.isEmpty(newAdhocRfiDto.getDueDate())){
            newAdhocRfiDto.setDueDateShow(newAdhocRfiDto.getDueDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
        String[] status=new String[]{"RFIST001"};
        List<SelectOption> statusList= MasterCodeUtil.retrieveOptionsByCodes(status);
        ParamUtil.setSessionAttr(bpc.request, "statusList", (Serializable) statusList);
        ParamUtil.setSessionAttr(request, "newReqInfo", newAdhocRfiDto);
    }
    public void doValidate(BaseProcessClass bpc) throws ParseException {
        //validate adhocRfi submitted data
        HttpServletRequest request = bpc.request;
        NewAdhocRfiDto newAdhocRfiDto = (NewAdhocRfiDto) ParamUtil.getSessionAttr(request,"newReqInfo");
        if(newAdhocRfiDto ==null){
            newAdhocRfiDto = new NewAdhocRfiDto();
        }
        String rfiTitle = ParamUtil.getString(request,"rfiTitle");
        String date = ParamUtil.getString(request,"dueDate");
        String status = ParamUtil.getString(request,"status");
        String info = ParamUtil.getString(request,"info");
        String doc = ParamUtil.getString(request,"doc");
        String information = ParamUtil.getString(request,"informationTitle");
        String documentsTitle = ParamUtil.getString(request,"documentsTitle");
        newAdhocRfiDto.setTitle(rfiTitle);
        if(!StringUtil.isEmpty(date)&&CommonValidator.isDate(date)){
            newAdhocRfiDto.setDueDate(LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            newAdhocRfiDto.setDueDateShow(date);
        }
        newAdhocRfiDto.setStatus(status);
        newAdhocRfiDto.setSupportingDocRequired("documents".equals(doc));
        newAdhocRfiDto.setInformationRequired("information".equals(info));
        if(!StringUtil.isEmpty(info)&&"information".equals(info)){
            newAdhocRfiDto.setTitleOfInformationRequired(information);
        }else {
            newAdhocRfiDto.setTitleOfInformationRequired(null);
        }
        if(!StringUtil.isEmpty(doc)&&"documents".equals(doc)){
            newAdhocRfiDto.setTitleOfSupportingDocRequired(documentsTitle);
        }else {
            newAdhocRfiDto.setTitleOfSupportingDocRequired(null);
        }
        ParamUtil.setRequestAttr(request, "newReqInfo", newAdhocRfiDto);
        validateData(newAdhocRfiDto,request);

    }

    public void doGreateRfi(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NewAdhocRfiDto newAdhocRfiDto = (NewAdhocRfiDto) ParamUtil.getSessionAttr(request,"newReqInfo");
        adhocRfiClient.saveAdhocRfi(newAdhocRfiDto);
        ParamUtil.setRequestAttr(request,"ackMsg", MessageUtil.dateIntoMessage("RFI_ACK001"));
    }
    /**
     * doCancel
     */
    public void doCancel(BaseProcessClass bpc){
        log.info("=======>>>>>start>>>>>>>>>>>>>>>>doCancel");
        HttpServletRequest request = bpc.request;
        ParamUtil.setRequestAttr(request, IS_VALIDATE, "Y");
    }

    private void validateData(NewAdhocRfiDto dto, HttpServletRequest request){
        //validation
        String isValidate;
        ValidationResultDto validationResultDto = adhocRfiClient.validateManualAdhocRfi(dto);
        if (!validationResultDto.isPass()){
            ParamUtil.setRequestAttr(request, ValidationConstants.KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
            isValidate = "false";
        }else {
            isValidate = "true";
        }

        ParamUtil.setRequestAttr(request, IS_VALIDATE, isValidate);
    }
    private AdhocRfiQueryDto getSearchDto(HttpServletRequest request) {
        AdhocRfiQueryDto searchDto = (AdhocRfiQueryDto) ParamUtil.getSessionAttr(request, KEY_ADHOC_LIST_SEARCH_DTO);
        return searchDto == null ? getDefaultSearchDto() : searchDto;
    }

    private AdhocRfiQueryDto getDefaultSearchDto() {
        AdhocRfiQueryDto dto = new AdhocRfiQueryDto();
        dto.defaultPaging();
        return dto;
    }
}
