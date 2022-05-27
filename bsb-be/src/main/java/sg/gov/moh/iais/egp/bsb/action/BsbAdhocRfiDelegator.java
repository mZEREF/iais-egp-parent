package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.reqForInfo.RequestForInformationConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.CommonValidator;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.AdhocRfiClient;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.adhocrfi.AdhocRfiQueryDto;
import sg.gov.moh.iais.egp.bsb.dto.adhocrfi.AdhocRfiQueryResultDto;
import sg.gov.moh.iais.egp.bsb.dto.adhocrfi.NewAdhocRfiDto;
import sg.gov.moh.iais.egp.bsb.dto.adhocrfi.ViewAdhocRfiDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.AdhocRfiDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.KEY_PAGE_NO;
import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.KEY_PAGE_SIZE;


@Slf4j
@Delegator("bsbAdhocRfiDelegator")
public class BsbAdhocRfiDelegator {
    private static final String ACTION_TYPE = "action_type";
    private static final String IS_VALIDATE = "isValidate";
    private static final String ACTION_VALUE = "action_value";
    private static final String ACTION_ADDT = "action_additional";
    private static final String KEY_ADHOC_RFI_LIST = "infoList";
    private static final String KEY_ADHOC_LIST_SEARCH_DTO = "adhocSearchDto";
    private static final String KEY_ADHOC_PAGE_INFO = "pageInfo";
    private static final String KEY_NEW_REQ_INFO = "newReqInfo";
    private static final String KEY_VIEW_REQ_INFO = "viewReqInfo";
    private static final String KEY_APPROVAL_ID = "approvalId";

    private final AdhocRfiClient adhocRfiClient;

    public BsbAdhocRfiDelegator(AdhocRfiClient adhocRfiClient) {
        this.adhocRfiClient = adhocRfiClient;
    }

    public void doStart(BaseProcessClass bpc){
        log.info("=======>>>>>start>>>>>>>>>>>>>>>>bsbAdhocRfiDelegator");
        HttpServletRequest request = bpc.request;
        HttpSession session = request.getSession();
        session.removeAttribute(KEY_ADHOC_LIST_SEARCH_DTO);
        session.removeAttribute(KEY_NEW_REQ_INFO);
        session.removeAttribute(KEY_VIEW_REQ_INFO);
        String approvalId = ParamUtil.getMaskedString(request,KEY_APPROVAL_ID);
        ParamUtil.setSessionAttr(request, KEY_APPROVAL_ID, approvalId);
    }
    /**
     * preAdhocRfi
     */
    public void preAdhocRfi(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, KEY_ADHOC_RFI_LIST, null);

        String approvalId = (String) ParamUtil.getSessionAttr(request, KEY_APPROVAL_ID);
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
            viewAdhocRfiDto = (ViewAdhocRfiDto) ParamUtil.getSessionAttr(request,KEY_VIEW_REQ_INFO);
        }
        if(!StringUtils.isEmpty(viewAdhocRfiDto.getDueDate())){
            viewAdhocRfiDto.setDueDateShow(viewAdhocRfiDto.getDueDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
        ParamUtil.setSessionAttr(request, KEY_VIEW_REQ_INFO, viewAdhocRfiDto);
        String[] status=new String[]{viewAdhocRfiDto.getStatus()};
        if(viewAdhocRfiDto.getStatus().equals(RequestForInformationConstants.RFI_CLOSE)){
            status=new String[]{RequestForInformationConstants.RFI_CLOSE_OFFICER,RequestForInformationConstants.RFI_RETRIGGER};
        }
        if(viewAdhocRfiDto.getStatus().equals(RequestForInformationConstants.RFI_CLOSE_OFFICER)){
            status=new String[]{RequestForInformationConstants.RFI_CLOSE_OFFICER};
        }
        List<SelectOption> statusLists= MasterCodeUtil.retrieveOptionsByCodes(status);
        ParamUtil.setSessionAttr(bpc.request, "statusLists", (Serializable) statusLists);
    }
    /**
     * doUpdate
     */
    public void doUpdate(BaseProcessClass bpc) {
        log.info("=======>>>>>start>>>>>>>>>>>>>>>>doUpdate");
        HttpServletRequest request = bpc.request;
        ViewAdhocRfiDto viewAdhocRfiDto = (ViewAdhocRfiDto) ParamUtil.getSessionAttr(request,KEY_VIEW_REQ_INFO);
        String date = ParamUtil.getString(request,"dueDate");
        String status = ParamUtil.getString(request,"status");
        if(!StringUtils.isEmpty(date)&&CommonValidator.isDate(date)){
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
        ParamUtil.setSessionAttr(request, KEY_VIEW_REQ_INFO, viewAdhocRfiDto);
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
        String approvalId = (String) ParamUtil.getSessionAttr(request, KEY_APPROVAL_ID);
        NewAdhocRfiDto newAdhocRfiDto = adhocRfiClient.queryPreNewData(approvalId).getEntity();
        String isValidate = (String) ParamUtil.getRequestAttr(request,IS_VALIDATE);
        if(isValidate !=null && !isValidate.equals("true")){
            newAdhocRfiDto = (NewAdhocRfiDto) ParamUtil.getRequestAttr(request,KEY_NEW_REQ_INFO);
        }
        if(!StringUtils.isEmpty(newAdhocRfiDto.getDueDate())){
            newAdhocRfiDto.setDueDateShow(newAdhocRfiDto.getDueDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
        String[] status=new String[]{"RFIST001"};
        List<SelectOption> statusList= MasterCodeUtil.retrieveOptionsByCodes(status);
        ParamUtil.setSessionAttr(bpc.request, "statusList", (Serializable) statusList);
        ParamUtil.setSessionAttr(request, KEY_NEW_REQ_INFO, newAdhocRfiDto);
    }
    public void doValidate(BaseProcessClass bpc) {
        //validate adhocRfi submitted data
        HttpServletRequest request = bpc.request;
        NewAdhocRfiDto newAdhocRfiDto = (NewAdhocRfiDto) ParamUtil.getSessionAttr(request,KEY_NEW_REQ_INFO);
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
        if(!StringUtils.isEmpty(date)&&CommonValidator.isDate(date)){
            newAdhocRfiDto.setDueDate(LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            newAdhocRfiDto.setDueDateShow(date);
        }
        newAdhocRfiDto.setStatus(status);
        newAdhocRfiDto.setSupportingDocRequired("documents".equals(doc));
        newAdhocRfiDto.setInformationRequired("information".equals(info));
        if(!StringUtils.isEmpty(info)&&"information".equals(info)){
            newAdhocRfiDto.setTitleOfInformationRequired(information);
        }else {
            newAdhocRfiDto.setTitleOfInformationRequired(null);
        }
        if(!StringUtils.isEmpty(doc)&&"documents".equals(doc)){
            newAdhocRfiDto.setTitleOfSupportingDocRequired(documentsTitle);
        }else {
            newAdhocRfiDto.setTitleOfSupportingDocRequired(null);
        }
        ParamUtil.setRequestAttr(request, KEY_NEW_REQ_INFO, newAdhocRfiDto);
        validateData(newAdhocRfiDto,request);

    }

    public void doGreateRfi(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NewAdhocRfiDto newAdhocRfiDto = (NewAdhocRfiDto) ParamUtil.getSessionAttr(request,KEY_NEW_REQ_INFO);
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
