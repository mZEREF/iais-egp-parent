package sg.gov.moh.iais.egp.bsb.action;


import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
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
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.entity.MsgMaskParam;
import sg.gov.moh.iais.egp.bsb.service.BsbInboxService;
import sg.gov.moh.iais.egp.bsb.util.CollectionUtils;
import sg.gov.moh.iais.egp.bsb.util.mastercode.MasterCodeHolder;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_INBOX_MESSAGE;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_INTERNAL_INBOX;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_DASHBOARD_UNREAD_MSG_AMT;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_INBOX_DATA_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_INBOX_MSG_SEARCH_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.FeInboxConstants.KEY_IS_FAC_ADMIN;
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
    private static final String KEY_SEARCH_MSG_DATE_FROM = "searchMsgDateFrom";
    private static final String KEY_SEARCH_MSG_DATE_TO = "searchMsgDateTo";

    private static final String KEY_SIGN_EQUAL = "=";
    private static final String KEY_MESSAGE_PAGE = "msgPage";
    public static final String KEY_INBOX = "inbox";
    public static final String KEY_ARCHIVE = "archive";
    private static final String KEY_AFTER_ARCHIVE = "AFTER_ARCHIVE";
    public static final String KEY_ACTION_REQUIRED_MAP = "actionRequiredMap";

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
        request.getSession().removeAttribute(KEY_DASHBOARD_UNREAD_MSG_AMT);

        AuditTrailHelper.auditFunction(MODULE_INTERNAL_INBOX, FUNCTION_INBOX_MESSAGE);
    }

    public void init(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String msgStatus = request.getParameter(KEY_MESSAGE_STATUS);
        InboxMsgSearchDto searchDto = getSearchDto(request);
        searchDto.setMsgStatus(msgStatus);
        ParamUtil.setSessionAttr(request, KEY_INBOX_MSG_SEARCH_DTO, searchDto);

        // judge the role of the logging user
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        ParamUtil.setSessionAttr(request, KEY_IS_FAC_ADMIN, loginContext.getCurRoleId().equals(RoleConsts.USER_ROLE_BSB_FACILITY_USER));
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
            ParamUtil.setRequestAttr(request, KEY_ACTION_REQUIRED_MAP, new HashMap<>(resultDto.getEntity().getInboxActionRequiredMsgIdMap()));
        } else {
            log.warn("get inbox message API doesn't return ok, the response is {}", resultDto);
            ParamUtil.setRequestAttr(request, KEY_INBOX_MSG_PAGE_INFO, PageInfo.emptyPageInfo(searchDto));
            ParamUtil.setRequestAttr(request, KEY_INBOX_DATA_LIST, new ArrayList<>());
            ParamUtil.setRequestAttr(request, KEY_ACTION_REQUIRED_MAP ,new HashMap<>());
        }
        // get select options
        ParamUtil.setRequestAttr(request, "msgTypeOps", MasterCodeHolder.MSG_TYPE.allOptions());
        List<SelectOption> inboxSubTypeOps = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_SUBMISSION_TYPE);
        ParamUtil.setRequestAttr(request, "msgSubTypeOps", inboxSubTypeOps);

        String msgPage = getMsgPage(request);
        ParamUtil.setRequestAttr(request, KEY_MESSAGE_PAGE, msgPage);
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
            case "msgDtFrom":
                String searchMsgDtFrom = ParamUtil.getString(request, KEY_SEARCH_MSG_DATE_FROM);
                searchDto.setSearchMsgDateFrom(searchMsgDtFrom);
                searchDto.setPage(0);
                break;
            case "msgDtTo":
                String searchMsgDtTo = ParamUtil.getString(request, KEY_SEARCH_MSG_DATE_TO);
                searchDto.setSearchMsgDateTo(searchMsgDtTo);
                searchDto.setPage(0);
                break;
            case KEY_ARCHIVE:
                searchDto.setSearchSubject("");
                searchDto.setMsgStatus("MSGRS005");
                searchDto.setPage(0);
                ParamUtil.setSessionAttr(request, KEY_MESSAGE_PAGE, actionValue);
                break;
            case KEY_INBOX:
                searchDto.setSearchSubject("");
                searchDto.setMsgStatus("");
                searchDto.setPage(0);
                ParamUtil.setSessionAttr(request, KEY_MESSAGE_PAGE, actionValue);
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
        BsbInboxService.page(request, searchDto);
        ParamUtil.setSessionAttr(request, KEY_INBOX_MSG_SEARCH_DTO, searchDto);
    }

    public void viewMsg(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        //get mask msg id
        inboxService.retrieveDashboardData(request);
        String maskedMsgId = ParamUtil.getString(request, KEY_ACTION_VALUE);
        //unMasked msg id
        if (StringUtils.hasLength(maskedMsgId)) {
            String msgId = MaskUtil.unMaskValue(KEY_ACTION_VALUE, maskedMsgId);
            InboxMsgContentDto msgContentDto = inboxClient.searchInboxContentByMsgId(msgId).getEntity();
            String finalContent = msgContentDto.getContent();
            List<MsgMaskParam> msgMaskParams = msgContentDto.getInboxMaskParamDtoList();
            if (msgMaskParams != null && !msgMaskParams.isEmpty()) {
                for (MsgMaskParam maskParam : msgMaskParams) {
                    finalContent = finalContent.replaceAll(KEY_SIGN_EQUAL + maskParam.getParamValue(),
                            KEY_SIGN_EQUAL + MaskUtil.maskValue(maskParam.getParamName(), maskParam.getParamValue()));
                }
            }
            //get msg content
            //set content into request
            ParamUtil.setRequestAttr(request, FeInboxConstants.KEY_BSB_FE_MSG_CONTENT, finalContent);
            inboxClient.updateInboxMsgStatusRead(msgId);
        }
    }

    public void doArchive(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);
        String[] maskedMsgIds = ParamUtil.getStrings(request, "chkChild");
        //validate
        if (maskedMsgIds != null && maskedMsgIds.length > 0) {
            //update archive
            List<String> msgIds = CollectionUtils.listMapping(i -> MaskUtil.unMaskValue(KEY_ACTION_VALUE, i), new ArrayList<>(Arrays.asList(maskedMsgIds)));
            ValidationResultDto validationResultDto = inboxClient.validateInboxMsgArchive(msgIds);
            if (validationResultDto.isPass()) {
                if (KEY_ARCHIVE.equals(actionValue)) {
                    inboxClient.updateInboxMsgStatusRead(msgIds);
                } else if (KEY_INBOX.equals(actionValue)) {
                    inboxClient.updateInboxMsgStatusArchive(msgIds);
                    ParamUtil.setRequestAttr(request, KEY_AFTER_ARCHIVE, "true");
                }
            } else {
//                ParamUtil.setRequestAttr(request, ValidationConstants.IS_VALID, ValidationConstants.NO);
//                ParamUtil.setRequestAttr(request, ValidationConstants.KEY_SHOW_ERROR_SWITCH, Boolean.TRUE);
                ParamUtil.setRequestAttr(request, ValidationConstants.KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
            }
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

    public String getMsgPage(HttpServletRequest request) {
        String msgPage = (String) ParamUtil.getSessionAttr(request, KEY_MESSAGE_PAGE);
        return msgPage != null ? msgPage : KEY_INBOX;
    }
}
