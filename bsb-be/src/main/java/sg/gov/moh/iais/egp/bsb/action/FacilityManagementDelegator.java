package sg.gov.moh.iais.egp.bsb.action;


import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.client.FacilityClient;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.facilitymanagement.FacilityManagementSearchDto;
import sg.gov.moh.iais.egp.bsb.dto.facilitymanagement.FacilityManagementSearchResultDto;
import sg.gov.moh.iais.egp.bsb.service.UserRoleService;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_FACILITY_MANAGEMENT;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_INTRANET_DASHBOARD;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.FACILITY_MANAGEMENT_SEARCH_ACTIVITY_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.FACILITY_MANAGEMENT_SEARCH_FACILITY_STATUS;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.FACILITY_MANAGEMENT_SEARCH_FAC_CLASSIFICATION;
import static sg.gov.moh.iais.egp.bsb.constant.ResponseConstants.ERROR_CODE_VALIDATION_FAIL;
import static sg.gov.moh.iais.egp.bsb.constant.ResponseConstants.ERROR_INFO_ERROR_MSG;
import static sg.gov.moh.iais.egp.bsb.constant.module.FacilityManagementModuleConstants.KEY_DATA_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.FacilityManagementModuleConstants.KEY_PAGE_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.FacilityManagementModuleConstants.KEY_SEARCH_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_ADDITIONAL;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_VALUE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_PAGE_NO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_PAGE_SIZE;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.KEY_CUR_ROLE;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.KEY_ROLE_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.KEY_ROLE_OPTIONS;


@Slf4j
@Delegator("facilityManagementDelegator")
@RequiredArgsConstructor
public class FacilityManagementDelegator {
    private final UserRoleService userRoleService;
    private final FacilityClient facilityClient;

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_SEARCH_DTO);

        AuditTrailHelper.auditFunction(MODULE_INTRANET_DASHBOARD, FUNCTION_FACILITY_MANAGEMENT);
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        FacilityManagementSearchDto searchDto = getSearchDto(request);
        ParamUtil.setSessionAttr(request, KEY_SEARCH_DTO, searchDto);

        ResponseDto<FacilityManagementSearchResultDto> resultDto = facilityClient.searchFacilityList(searchDto);

        if (resultDto.ok()) {
            ParamUtil.setRequestAttr(request, KEY_PAGE_INFO, resultDto.getEntity().getPageInfo());
            ParamUtil.setRequestAttr(request, KEY_DATA_LIST, resultDto.getEntity().getDisplayDtos());
        } else {
            log.info("Search Task List fail");
            ParamUtil.setRequestAttr(request, KEY_PAGE_INFO, PageInfo.emptyPageInfo(searchDto));
            ParamUtil.setRequestAttr(request, KEY_DATA_LIST, new ArrayList<>());
            if(ERROR_CODE_VALIDATION_FAIL.equals(resultDto.getErrorCode())) {
                log.warn("Fail reason: {}", resultDto.getErrorInfos().get(ERROR_INFO_ERROR_MSG));
                ParamUtil.setRequestAttr(request,"errorMsg", JsonUtil.parseToJson(resultDto.getErrorInfos().get(ERROR_INFO_ERROR_MSG)));
            }
        }

        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        List<String> userRoles = new ArrayList<>(loginContext.getRoleIds());
        ArrayList<SelectOption> roleOps = new ArrayList<>(userRoleService.getRoleSelectOptions(AppConsts.HALP_EGP_DOMAIN, userRoles));
        ParamUtil.setSessionAttr(bpc.request, KEY_ROLE_OPTIONS, roleOps);

        ParamUtil.setRequestAttr(request, KEY_CUR_ROLE, loginContext.getCurRoleId());
        ParamUtil.setRequestAttr(request, "facilityClassificationOps", FACILITY_MANAGEMENT_SEARCH_FAC_CLASSIFICATION);
        ParamUtil.setRequestAttr(request, "facilityActivityTypeOps", FACILITY_MANAGEMENT_SEARCH_ACTIVITY_TYPE);
        ParamUtil.setRequestAttr(request, "facilityStatusOps", FACILITY_MANAGEMENT_SEARCH_FACILITY_STATUS);
    }

    public void search(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        FacilityManagementSearchDto searchDto = bindModel(request, getSearchDto(request));
        searchDto.setPage(0);
        ParamUtil.setSessionAttr(request, KEY_SEARCH_DTO, searchDto);
    }

    public void sort(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        FacilityManagementSearchDto searchDto = getSearchDto(request);
        String field = ParamUtil.getString(request, KEY_ACTION_VALUE);
        String sortType = ParamUtil.getString(request, KEY_ACTION_ADDITIONAL);
        searchDto.changeSort(field, sortType);
        ParamUtil.setSessionAttr(request, KEY_SEARCH_DTO, searchDto);
    }

    public void page(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        FacilityManagementSearchDto searchDto = getSearchDto(request);
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
    }


    public void changeRole(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String roleId = ParamUtil.getString(request, KEY_ROLE_ID);

        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        /* if the roleId in request is not a member of user's roleIds, the valus is invalid */
        if (roleId == null || !loginContext.getRoleIds().contains(roleId)) {
            throw new IaisRuntimeException("Invalid role Id");
        }

        loginContext.setCurRoleId(roleId);
        ParamUtil.setSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER, loginContext);

        /* Call bindModel, so the new search condition will be in effect.
         * It's also ok to use the old search condition, then just remove the bindModel */
        FacilityManagementSearchDto searchDto = bindModel(request, getSearchDto(request));
        searchDto.setRoleIds(Collections.singleton(roleId));
        ParamUtil.setSessionAttr(request, KEY_SEARCH_DTO, searchDto);
    }

    private FacilityManagementSearchDto getSearchDto(HttpServletRequest request) {
        FacilityManagementSearchDto dto = (FacilityManagementSearchDto) ParamUtil.getSessionAttr(request, KEY_SEARCH_DTO);
        if (dto == null) {
            dto = new FacilityManagementSearchDto();
            dto.defaultPaging();

            LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
            dto.setRoleIds(Collections.singleton(loginContext.getCurRoleId()));
        }
        return dto;
    }

    private FacilityManagementSearchDto bindModel(HttpServletRequest request, FacilityManagementSearchDto dto) {
        if (dto == null) {
            dto = (FacilityManagementSearchDto) ParamUtil.getSessionAttr(request, KEY_SEARCH_DTO);
        }
        if (dto == null) {
            dto = new FacilityManagementSearchDto();
        }
        dto.setSearchKeyword(ParamUtil.getString(request, "searchKeyword"));
        dto.setSearchKeywordType(ParamUtil.getString(request,"searchKeywordType"));
        dto.setSearchFacilityClassification(ParamUtil.getString(request,"searchFacilityClassification"));
        dto.setSearchFacilityActivityType(ParamUtil.getString(request,"searchFacilityActivityType"));
        dto.setSearchFacilityStatus(ParamUtil.getString(request,"searchFacilityStatus"));
        return dto;
    }
}
