package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.NullArgumentException;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.FacilityClient;
import sg.gov.moh.iais.egp.bsb.client.InspectionClient;
import sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.adhocInspection.AdhocInspectionDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.adhocInspection.AdhocInspectionSearchDto;
import sg.gov.moh.iais.egp.bsb.dto.adhocInspection.AdhocInspectionSearchResultDto;
import sg.gov.moh.iais.egp.bsb.service.UserRoleService;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_ADHOC_INSPECTION;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_INTRANET_DASHBOARD;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.ADHOC_INSPECTION_SEARCH_ACTIVITY_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.ADHOC_INSPECTION_SEARCH_FACILITY_STATUS;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.ADHOC_INSPECTION_SEARCH_FAC_CLASSIFICATION;
import static sg.gov.moh.iais.egp.bsb.constant.ResponseConstants.ERROR_CODE_VALIDATION_FAIL;
import static sg.gov.moh.iais.egp.bsb.constant.ResponseConstants.ERROR_INFO_ERROR_MSG;
import static sg.gov.moh.iais.egp.bsb.constant.module.FacilityManagementModuleConstants.KEY_DATA_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.FacilityManagementModuleConstants.KEY_PAGE_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.FacilityManagementModuleConstants.KEY_SEARCH_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_ADDITIONAL;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_VALUE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_PAGE_NO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_PAGE_SIZE;

@Slf4j
@RequiredArgsConstructor
@Delegator(value = "adhocInspectionDelegator")
public class AdhocInspectionDelegator {
    private final UserRoleService userRoleService;
    private final FacilityClient facilityClient;
    private final InspectionClient inspectionClient;

    private static final String ACTION_LIST = "list";
    private static final String ACTION_INSPECTION = "inspection";
    private static final String ACTION_ACK = "ack";
    private static final String ACTION_PAGE = "page";
    private static final String ACTION_SORT = "sort";
    private static final String ACTION_SEARCH = "search";

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_SEARCH_DTO);
        AuditTrailHelper.auditFunction(MODULE_INTRANET_DASHBOARD, FUNCTION_ADHOC_INSPECTION);
    }

    public void prepareSwitch(BaseProcessClass bpc) {
        String action = ParamUtil.getRequestString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if (StringUtils.isEmpty(action)) {
            action = ACTION_LIST;
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, action);
        }
    }

    public void prepareList(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        AdhocInspectionSearchDto searchDto = getSearchDto(request);
        ParamUtil.setSessionAttr(request, KEY_SEARCH_DTO, searchDto);

        ResponseDto<AdhocInspectionSearchResultDto> resultDto = facilityClient.searchFacilityList(searchDto);

        if (resultDto.ok()) {
            ParamUtil.setRequestAttr(request, KEY_PAGE_INFO, resultDto.getEntity().getPageInfo());
            ParamUtil.setRequestAttr(request, KEY_DATA_LIST, resultDto.getEntity().getDisplayDtos());
        } else {
            log.info("Search Task List fail");
            ParamUtil.setRequestAttr(request, KEY_PAGE_INFO, PageInfo.emptyPageInfo(searchDto));
            ParamUtil.setRequestAttr(request, KEY_DATA_LIST, new ArrayList<>());
            if (ERROR_CODE_VALIDATION_FAIL.equals(resultDto.getErrorCode())) {
                log.warn("Fail reason: {}", resultDto.getErrorInfos().get(ERROR_INFO_ERROR_MSG));
                ParamUtil.setRequestAttr(request, "errorMsg", JsonUtil.parseToJson(resultDto.getErrorInfos().get(ERROR_INFO_ERROR_MSG)));
            }
        }

        ParamUtil.setRequestAttr(request, "facilityClassificationOps", ADHOC_INSPECTION_SEARCH_FAC_CLASSIFICATION);
        ParamUtil.setRequestAttr(request, "facilityActivityTypeOps", ADHOC_INSPECTION_SEARCH_ACTIVITY_TYPE);
        ParamUtil.setRequestAttr(request, "facilityStatusOps", ADHOC_INSPECTION_SEARCH_FACILITY_STATUS);
    }

    public void handleList(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String action = ParamUtil.getRequestString(request, KEY_ACTION_TYPE);
        if (ACTION_INSPECTION.equals(action)) {
            String facId = ParamUtil.getString(request, TaskModuleConstants.PARAM_NAME_FAC_ID);
            if (StringUtils.hasLength(facId)) {
                facId = MaskUtil.maskValue(TaskModuleConstants.PARAM_NAME_FAC_ID, facId);
            } else {
                throw new NullArgumentException("no facility id.");
            }
            ParamUtil.setRequestAttr(request, TaskModuleConstants.PARAM_NAME_FAC_ID, facId);
        }
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, action);
    }

    public void search(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        AdhocInspectionSearchDto searchDto = bindModel(request, getSearchDto(request));
        searchDto.setPage(0);
        ParamUtil.setSessionAttr(request, KEY_SEARCH_DTO, searchDto);
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, ACTION_LIST);
    }

    public void sort(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        AdhocInspectionSearchDto searchDto = getSearchDto(request);
        String field = ParamUtil.getString(request, KEY_ACTION_VALUE);
        String sortType = ParamUtil.getString(request, KEY_ACTION_ADDITIONAL);
        searchDto.changeSort(field, sortType);
        ParamUtil.setSessionAttr(request, KEY_SEARCH_DTO, searchDto);
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, ACTION_LIST);
    }

    public void page(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        AdhocInspectionSearchDto searchDto = getSearchDto(request);
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
        ParamUtil.setSessionAttr(request, KEY_SEARCH_DTO, searchDto);
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, ACTION_LIST);
    }

    public void prepareAdhocInspection(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String facId = ParamUtil.getString(request, TaskModuleConstants.PARAM_NAME_FAC_ID);
        if (StringUtils.hasLength(facId)) {
            facId = MaskUtil.unMaskValue(TaskModuleConstants.PARAM_NAME_FAC_ID, facId);
            AdhocInspectionSearchDto dto = new AdhocInspectionSearchDto();
            dto.defaultPaging();
            dto.setSearchFacilityId(facId);
            ResponseDto<AdhocInspectionSearchResultDto> resultDto = facilityClient.searchFacilityList(dto);
            if (resultDto.ok()) {
                AdhocInspectionDisplayDto displayDto = resultDto.getEntity().getDisplayDtos().stream().findFirst().orElseThrow(IllegalArgumentException::new);
                ParamUtil.setRequestAttr(request, "facility", displayDto);
            } else {
                throw new IllegalArgumentException("search failed.");
            }
        } else {
            throw new NullArgumentException("no facility id.");
        }
    }

    public void handleAdhocInspection(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String action = ParamUtil.getRequestString(request, KEY_ACTION_TYPE);
        if ("submit".equals(action)) {
            String facId = ParamUtil.getString(request, TaskModuleConstants.PARAM_NAME_FAC_ID);
            if (StringUtils.hasLength(facId)) {
                facId = MaskUtil.unMaskValue(TaskModuleConstants.PARAM_NAME_FAC_ID, facId);
                String appNo = inspectionClient.createAdhocInspection(facId).getEntity();
                ParamUtil.setRequestAttr(request, "appNo", appNo);
                action = ACTION_ACK;
            } else {
                throw new NullArgumentException("no facility id.");
            }
        }
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, action);
    }

    public void prepareAck(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String applicationNo = ParamUtil.getRequestString(request, "appNo");
        String msg = MessageUtil.replaceMessage("BISACKINS005", applicationNo, "1");
        ParamUtil.setRequestAttr(request, ModuleCommonConstants.RESULT_MSG, msg);
    }

    public void handleAck(BaseProcessClass bpc) {
    }

    private AdhocInspectionSearchDto getSearchDto(HttpServletRequest request) {
        AdhocInspectionSearchDto dto = (AdhocInspectionSearchDto) ParamUtil.getSessionAttr(request, KEY_SEARCH_DTO);
        if (dto == null) {
            dto = new AdhocInspectionSearchDto();
            dto.defaultPaging();
        }
        return dto;
    }

    private AdhocInspectionSearchDto bindModel(HttpServletRequest request, AdhocInspectionSearchDto dto) {
        if (dto == null) {
            dto = (AdhocInspectionSearchDto) ParamUtil.getSessionAttr(request, KEY_SEARCH_DTO);
        }
        if (dto == null) {
            dto = new AdhocInspectionSearchDto();
        }
        dto.setSearchKeyword(ParamUtil.getString(request, "searchKeyword"));
        dto.setSearchKeywordType(ParamUtil.getString(request, "searchKeywordType"));
        dto.setSearchFacilityClassification(ParamUtil.getString(request, "searchFacilityClassification"));
        dto.setSearchFacilityActivityType(ParamUtil.getString(request, "searchFacilityActivityType"));
        dto.setSearchFacilityStatus(ParamUtil.getString(request, "searchFacilityStatus"));
        return dto;
    }
}
