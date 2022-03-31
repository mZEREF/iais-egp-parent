package sg.gov.moh.iais.egp.bsb.action;


import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.BsbInboxClient;
import sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.inbox.InboxMsgContentDto;
import sg.gov.moh.iais.egp.bsb.dto.inbox.InboxMsgSearchDto;
import sg.gov.moh.iais.egp.bsb.dto.inbox.InboxMsgSearchResultDto;
import sg.gov.moh.iais.egp.bsb.entity.MsgMaskParam;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Delegator("bsbInboxMsgDelegator")
public class BsbInboxMsgDelegator {
    private static final String KEY_INBOX_MSG_SEARCH_DTO = "inboxMsgSearchDto";
    private static final String KEY_INBOX_MSG_PAGE_INFO = "pageInfo";
    private static final String KEY_INBOX_MSG_DATA_LIST = "dataList";
    private static final String KEY_INBOX_MSG_UNREAD_AMT = "unreadMsgAmt";

    private static final String KEY_ACTION_TYPE = "action_type";
    private static final String KEY_ACTION_VALUE = "action_value";
    private static final String KEY_ACTION_ADDT = "action_additional";

    private static final String KEY_SEARCH_MSG_TYPE = "searchMsgType";
    private static final String KEY_SEARCH_APP_TYPE = "searchAppType";
    private static final String KEY_SEARCH_SUBJECT = "searchSubject";

    private static final String KEY_PAGE_SIZE = "pageJumpNoPageSize";
    private static final String KEY_PAGE_NO = "pageJumpNoTextchangePage";
    private static final String KEY_SIGN_EQUAL = "=";

    private final BsbInboxClient inboxClient;

    @Autowired
    public BsbInboxMsgDelegator(BsbInboxClient bsbInboxClient) {
        this.inboxClient = bsbInboxClient;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_INBOX_MSG_SEARCH_DTO);

        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_INTERNAL_INBOX, AuditTrailConsts.FUNCTION_INBOX);
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;

        // get search DTO
        InboxMsgSearchDto searchDto = getSearchDto(request);
        ParamUtil.setSessionAttr(request, KEY_INBOX_MSG_SEARCH_DTO, searchDto);

        // call API to get searched data
        ResponseDto<InboxMsgSearchResultDto> resultDto = inboxClient.getInboxMsg(searchDto);

        if (resultDto.ok()) {
            ParamUtil.setRequestAttr(request, KEY_INBOX_MSG_PAGE_INFO, resultDto.getEntity().getPageInfo());
            ParamUtil.setRequestAttr(request, KEY_INBOX_MSG_DATA_LIST, resultDto.getEntity().getBsbInboxes());
            ParamUtil.setRequestAttr(request, KEY_INBOX_MSG_UNREAD_AMT, resultDto.getEntity().getUnreadMsgAmt());
        } else {
            log.warn("get inbox message API doesn't return ok, the response is {}", resultDto);
            ParamUtil.setRequestAttr(request, KEY_INBOX_MSG_PAGE_INFO, PageInfo.emptyPageInfo(searchDto));
            ParamUtil.setRequestAttr(request, KEY_INBOX_MSG_DATA_LIST, new ArrayList<>());
            ParamUtil.setRequestAttr(request, KEY_INBOX_MSG_UNREAD_AMT, 0);
        }

        // get select options
        List<SelectOption> inboxMsgTypeOps = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_INBOX_MESSAGE_TYPE);
        ParamUtil.setRequestAttr(request, "msgTypeOps", inboxMsgTypeOps);
        List<SelectOption> inboxAppTypeOps = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_APP_TYPE);
        ParamUtil.setRequestAttr(request, "msgAppTypeOps", inboxAppTypeOps);
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
            case "appType":
                String searchAppType = ParamUtil.getString(request, KEY_SEARCH_APP_TYPE);
                searchDto.setSearchAppType(searchAppType);
                searchDto.setSearchSubject("");
                searchDto.setPage(0);
                break;
            case "subject":
                String searchSubject = ParamUtil.getString(request, KEY_SEARCH_SUBJECT);
                searchDto.setSearchSubject(searchSubject);
                searchDto.setPage(0);
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
        String sortType = ParamUtil.getString(request, KEY_ACTION_ADDT);
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
        dto.defaultPaging();
        return dto;
    }
}
