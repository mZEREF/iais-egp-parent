package sg.gov.moh.iais.egp.bsb.action;


import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.BsbInboxClient;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.inbox.InboxMsgContentDto;
import sg.gov.moh.iais.egp.bsb.dto.inbox.InboxMsgSearchDto;
import sg.gov.moh.iais.egp.bsb.dto.inbox.InboxMsgSearchResultDto;
import sg.gov.moh.iais.egp.bsb.entity.MsgMaskParam;
import sg.gov.moh.iais.egp.bsb.service.BsbInboxService;
import sg.gov.moh.iais.egp.bsb.util.CollectionUtils;
import sg.gov.moh.iais.egp.bsb.util.mastercode.MasterCodeHolder;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_ADDITIONAL;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_VALUE;


@Slf4j
@Delegator("bsbInboxMsgDelegator")
public class BsbInboxMsgDelegator {
    private static final String KEY_INBOX_MSG_PAGE_INFO = "pageInfo";
    private static final String KEY_MESSAGE_STATUS = "msgStatus";

    private static final String KEY_SEARCH_MSG_TYPE = "searchMsgType";
    private static final String KEY_SEARCH_SUBMISSION_TYPE = "searchSubType";
    private static final String KEY_SEARCH_SUBJECT = "searchSubject";

    private static final String KEY_PAGE_SIZE = "pageJumpNoPageSize";
    private static final String KEY_PAGE_NO = "pageJumpNoTextchangePage";
    private static final String KEY_SIGN_EQUAL = "=";
    private static final String KEY_MESSAGE_PAGE = "msgPage";
    public static final String KEY_INBOX = "inbox";
    public static final String KEY_ARCHIVE = "archive";

    private final BsbInboxClient inboxClient;
    private final BsbInboxService inboxService;

    @Autowired
    public BsbInboxMsgDelegator(BsbInboxClient bsbInboxClient, BsbInboxService inboxService) {
        this.inboxClient = bsbInboxClient;
        this.inboxService = inboxService;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_INBOX_MSG_SEARCH_DTO);
        request.getSession().removeAttribute(KEY_MESSAGE_PAGE);

        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_INTERNAL_INBOX, AuditTrailConsts.FUNCTION_INBOX);
    }

    public void init(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String msgStatus = request.getParameter(KEY_MESSAGE_STATUS);
        InboxMsgSearchDto searchDto = getSearchDto(request);
        searchDto.setMsgStatus(msgStatus);
        ParamUtil.setSessionAttr(request,KEY_INBOX_MSG_SEARCH_DTO,searchDto);
    }

    @SneakyThrows
    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;

        // get search DTO
        InboxMsgSearchDto searchDto = getSearchDto(request);
        ParamUtil.setSessionAttr(request, KEY_INBOX_MSG_SEARCH_DTO, searchDto);

        // call API to get dashboard data
        inboxService.retrieveDashboardData(request);
        // call API to get searched data
        ResponseDto<InboxMsgSearchResultDto> resultDto = inboxClient.getInboxMsg(searchDto);

        if (resultDto.ok()) {
            ParamUtil.setRequestAttr(request, KEY_INBOX_MSG_PAGE_INFO, resultDto.getEntity().getPageInfo());
            ParamUtil.setRequestAttr(request, KEY_INBOX_DATA_LIST, resultDto.getEntity().getBsbInboxes());
        } else {
            log.warn("get inbox message API doesn't return ok, the response is {}", resultDto);
            ParamUtil.setRequestAttr(request, KEY_INBOX_MSG_PAGE_INFO, PageInfo.emptyPageInfo(searchDto));
            ParamUtil.setRequestAttr(request, KEY_INBOX_DATA_LIST, new ArrayList<>());
        }
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request,ValidationConstants.KEY_SHOW_ERROR_SWITCH);
        if (Boolean.TRUE.equals(needShowError)) {
            Map<String,String> errorMap = Maps.newHashMapWithExpectedSize(1);
            errorMap.put("archiveInfo","Please select a message");
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValueAsString(errorMap);
            ParamUtil.setRequestAttr(request, ValidationConstants.KEY_VALIDATION_ERRORS, mapper);
        }
        // get select options
        ParamUtil.setRequestAttr(request, "msgTypeOps", MasterCodeHolder.MSG_TYPE.allOptions());
        List<SelectOption> inboxSubTypeOps = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_SUBMISSION_TYPE);
        ParamUtil.setRequestAttr(request, "msgSubTypeOps", inboxSubTypeOps);

        String msgPage = getMsgPage(request);
        ParamUtil.setRequestAttr(request,KEY_MESSAGE_PAGE,msgPage);
    }

    public void search(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        InboxMsgSearchDto searchDto = getSearchDto(request);
        String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);
        switch (actionValue) {
            case "msgType":
                String searchMsgType = ParamUtil.getString(request, KEY_SEARCH_MSG_TYPE);
                searchDto.setSearchMsgType(searchMsgType);
                searchDto.setSearchSubject("");
                searchDto.setPage(0);
                break;
            case "subType":
                String searchSubType = ParamUtil.getString(request, KEY_SEARCH_SUBMISSION_TYPE);
                searchDto.setSearchSubType(searchSubType);
                searchDto.setSearchSubject("");
                searchDto.setPage(0);
                break;
            case "subject":
                String searchSubject = ParamUtil.getString(request, KEY_SEARCH_SUBJECT);
                searchDto.setSearchSubject(searchSubject);
                searchDto.setPage(0);
                break;
            case KEY_ARCHIVE:
                searchDto.setSearchSubject("");
                searchDto.setMsgStatus("MSGRS005");
                searchDto.setPage(0);
                ParamUtil.setSessionAttr(request,KEY_MESSAGE_PAGE,actionValue);
                break;
            case KEY_INBOX:
                searchDto.setSearchSubject("");
                searchDto.setMsgStatus("");
                searchDto.setPage(0);
                ParamUtil.setSessionAttr(request,KEY_MESSAGE_PAGE,actionValue);
                break;
            default:
                log.warn("search, action_value is invalid: {}", actionValue);
                break;
        }
        ParamUtil.setSessionAttr(request, KEY_INBOX_MSG_SEARCH_DTO, searchDto);
    }

    public void sort(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        InboxMsgSearchDto searchDto = getSearchDto(request);
        String field = ParamUtil.getString(request, KEY_ACTION_VALUE);
        String sortType = ParamUtil.getString(request, KEY_ACTION_ADDITIONAL);
        searchDto.changeSort(field, sortType);
        ParamUtil.setSessionAttr(request, KEY_INBOX_MSG_SEARCH_DTO, searchDto);
    }

    public void page(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        InboxMsgSearchDto searchDto = getSearchDto(request);
        String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);
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
        ParamUtil.setSessionAttr(request, KEY_INBOX_MSG_SEARCH_DTO, searchDto);
    }

    public void viewMsg(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        //get mask msg id
        String maskedMsgId = ParamUtil.getString(request,KEY_ACTION_VALUE);
        //unMasked msg id
        if(StringUtils.hasLength(maskedMsgId)){
           String msgId = MaskUtil.unMaskValue(KEY_ACTION_VALUE,maskedMsgId);
           InboxMsgContentDto msgContentDto = inboxClient.searchInboxContentByMsgId(msgId).getEntity();
           String finalContent = msgContentDto.getContent();
           List<MsgMaskParam> msgMaskParams = msgContentDto.getInboxMaskParamDtoList();
           if(msgMaskParams != null && !msgMaskParams.isEmpty()){
               for (MsgMaskParam maskParam : msgMaskParams) {
                   finalContent = finalContent.replaceAll(KEY_SIGN_EQUAL+maskParam.getParamValue(),
                           KEY_SIGN_EQUAL+MaskUtil.maskValue(maskParam.getParamName(),maskParam.getParamValue()));
               }
           }
            //get msg content
            //set content into request
            ParamUtil.setRequestAttr(request, FeInboxConstants.KEY_BSB_FE_MSG_CONTENT,finalContent);
           inboxClient.updateInboxMsgStatusRead(msgId);
        }
    }

    public void doArchive(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);
        String[] maskedMsgIds = ParamUtil.getStrings(request,"chkChild");
        //validate
        if(maskedMsgIds != null && maskedMsgIds.length > 0){
            //update archive
            List<String> msgIds = CollectionUtils.listMapping(new ArrayList<>(Arrays.asList(maskedMsgIds)),i->MaskUtil.unMaskValue(KEY_ACTION_VALUE,i));
            if(KEY_ARCHIVE.equals(actionValue)){
                inboxClient.updateInboxMsgStatusRead(msgIds);
            }else if(KEY_INBOX.equals(actionValue)){
                inboxClient.updateInboxMsgStatusArchive(msgIds);
            }
        }else{
            ParamUtil.setRequestAttr(request, ValidationConstants.IS_VALID, ValidationConstants.NO);
            ParamUtil.setRequestAttr(request, ValidationConstants.KEY_SHOW_ERROR_SWITCH, Boolean.TRUE);
        }
    }



    public void bindAction(BaseProcessClass bpc) {
        // empty now
    }




    private InboxMsgSearchDto getSearchDto(HttpServletRequest request) {
        InboxMsgSearchDto searchDto = (InboxMsgSearchDto) ParamUtil.getSessionAttr(request, KEY_INBOX_MSG_SEARCH_DTO);
        return searchDto == null ? getDefaultSearchDto() : searchDto;
    }

    private InboxMsgSearchDto getDefaultSearchDto() {
        InboxMsgSearchDto dto = new InboxMsgSearchDto();
        dto.clearAllFields();
        dto.defaultPaging("subject,desc");
        return dto;
    }

    public String getMsgPage(HttpServletRequest request){
       String msgPage = (String) ParamUtil.getSessionAttr(request,KEY_MESSAGE_PAGE);
       return msgPage != null?msgPage:KEY_INBOX;
    }
}
