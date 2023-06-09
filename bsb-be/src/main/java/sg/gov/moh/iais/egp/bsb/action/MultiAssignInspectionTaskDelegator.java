package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.BsbTaskClient;
import sg.gov.moh.iais.egp.bsb.client.OrganizationClient;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.multiassign.MultiAssignInsDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.CollectionUtils;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_NAME_MULTI_ASSIGN_INSPECTION;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_INSPECTION;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.KEY_ASSIGN_RESULT;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.PARAM_NAME_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.PARAM_NAME_TASK_ID;

@Slf4j
@Delegator("multiAssignInspectionTaskDelegator")
public class MultiAssignInspectionTaskDelegator {
    private static final String MULTI_ASSIGN_INSPECTION_DTO = "multiAssignInsDto";
    private static final String MASK_USER_ID = "maskUserId";
    private static final String USER_OPTION = "userOption";
    private static final String ACTION_TYPE = "action_type";
    private static final String ACTION_TYPE_NEXT = "next";
    private static final String ACTION_TYPE_BACK = "back";

    private final BsbTaskClient bsbTaskClient;
    private final OrganizationClient organizationClient;

    public MultiAssignInspectionTaskDelegator(BsbTaskClient bsbTaskClient, OrganizationClient organizationClient) {
        this.bsbTaskClient = bsbTaskClient;
        this.organizationClient = organizationClient;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        AuditTrailHelper.auditFunction(MODULE_INSPECTION, FUNCTION_NAME_MULTI_ASSIGN_INSPECTION);
        MaskHelper.taskProcessUnmask(request, PARAM_NAME_APP_ID, PARAM_NAME_TASK_ID);
        request.getSession().removeAttribute(MULTI_ASSIGN_INSPECTION_DTO);
        request.getSession().removeAttribute(USER_OPTION);
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        MultiAssignInsDto dto = getMultiAssignInsDto(request);
        if (StringUtils.isEmpty(dto.getApplicationId())) {
            String appId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_APP_ID);
            String taskId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_TASK_ID);
            log.info("taskId is {}",taskId);
            ResponseDto<MultiAssignInsDto> responseDto = bsbTaskClient.getMultiAssignDataByAppId(appId);
            if (responseDto.ok()){
                dto = responseDto.getEntity();
                dto.setTaskId(taskId);
                Map<String, SelectOption> optionMap = retrieveUserInfo(request);
                dto.setOptionMap(optionMap);
                dto.setCanMultiAssign(true);
            }else {
                dto = new MultiAssignInsDto();
                dto.setCanMultiAssign(false);
            }
            ParamUtil.setSessionAttr(request, MULTI_ASSIGN_INSPECTION_DTO, dto);
        }
    }

    private Map<String, SelectOption> retrieveUserInfo(HttpServletRequest request) {
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String curRoleId = loginContext.getCurRoleId();
        List<OrgUserDto> dtoList = organizationClient.retrieveOrgUserAccountByRoleId(curRoleId).getEntity();
        List<OrgUserDto> userDtoList = new ArrayList<>();
        for (OrgUserDto orgUserDto : dtoList) {
            List<String> roles = organizationClient.retrieveUserRoles(orgUserDto.getId()).getEntity();
            // System will display all MOH BSB Officers with inspector role
            if (!org.springframework.util.CollectionUtils.isEmpty(roles) && roles.contains(RoleConsts.USER_ROLE_BSB_INSPECTOR)){
                userDtoList.add(orgUserDto);
            }
        }

        List<SelectOption> opts = new ArrayList<>();
        userDtoList.forEach(orgUserDto -> opts.add(new SelectOption(MaskUtil.maskValue(MASK_USER_ID, orgUserDto.getId()), orgUserDto.getUserId())));
        Map<String, SelectOption> optionMap = CollectionUtils.uniqueIndexMap(SelectOption::getValue, opts);
        ParamUtil.setSessionAttr(request, USER_OPTION, (Serializable) opts);
        return optionMap;
    }

    public void valFormData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        MultiAssignInsDto dto = getMultiAssignInsDto(request);

        String[] inspectors = ParamUtil.getStrings(request, "inspectorCheck");
        if (inspectors != null) {
            List<String> displayList = new ArrayList<>(inspectors.length);
            dto.setInspectors(Arrays.asList(inspectors));
            Map<String, SelectOption> optionMap = dto.getOptionMap();
            for (String inspector : inspectors) {
                displayList.add(optionMap.get(inspector).getText());
            }
            dto.setInspectorsDisplayName(displayList);
        }
        dto.setModule("multiAssignIns");
        String actionType;
        ValidationResultDto validationResultDto = bsbTaskClient.validateMultiAssignInsDto(dto);
        if (!validationResultDto.isPass()){
            ParamUtil.setRequestAttr(request, ValidationConstants.KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
            actionType = ACTION_TYPE_BACK;
        }else {
            actionType = ACTION_TYPE_NEXT;
        }
        ParamUtil.setRequestAttr(request, ACTION_TYPE, actionType);
        ParamUtil.setSessionAttr(request, MULTI_ASSIGN_INSPECTION_DTO, dto);
    }

    public void submitData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        MultiAssignInsDto dto = getMultiAssignInsDto(request);

        List<String> inspectors = dto.getInspectors();
        List<String> userIdList = new ArrayList<>(inspectors.size());
        for (String inspector : inspectors) {
            userIdList.add(MaskUtil.unMaskValue(MASK_USER_ID,inspector));
        }
        dto.setInspectors(userIdList);

        ResponseDto<String> responseDto = bsbTaskClient.multiAssignTask(dto);
        if (responseDto.ok()) {
            ParamUtil.setRequestAttr(request, KEY_ASSIGN_RESULT, "Task has been assigned successfully!");
        } else {
            ParamUtil.setRequestAttr(request, KEY_ASSIGN_RESULT, "Fail to assign this task!");
        }
    }

    private MultiAssignInsDto getMultiAssignInsDto(HttpServletRequest request){
        MultiAssignInsDto dto = (MultiAssignInsDto) ParamUtil.getSessionAttr(request,MULTI_ASSIGN_INSPECTION_DTO);
        return dto == null ? getDefaultDto() : dto;
    }

    private MultiAssignInsDto getDefaultDto(){
        return new MultiAssignInsDto();
    }
}
