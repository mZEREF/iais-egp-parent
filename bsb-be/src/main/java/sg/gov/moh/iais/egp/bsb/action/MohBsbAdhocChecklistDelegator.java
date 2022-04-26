package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.checklist.HcsaChecklistConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.CheckItemQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SqlHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.HcsaChecklistClient;
import sg.gov.moh.iais.egp.bsb.client.InspectionClient;
import sg.gov.moh.iais.egp.bsb.dto.entity.AdhocChecklistConfigDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.AdhocChecklistItemDto;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_ADHOC_CHECKLIST_ACTION_FLAG;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_ADHOC_CHECKLIST_LIST_ATTR;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_INSPECTION_CONFIG;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_TASK_ID;

@Slf4j
@Delegator(value = "MohBsbAdhocChecklistDelegator")
public class MohBsbAdhocChecklistDelegator {
    private final InspectionClient inspectionClient;
    private final HcsaChecklistClient hcsaChecklistClient;

    private final FilterParameter filterParameter = new FilterParameter.Builder()
            .clz(CheckItemQueryDto.class)
            .searchAttr(HcsaChecklistConstants.PARAM_CHECKLIST_ITEM_SEARCH)
            .resultAttr(HcsaChecklistConstants.PARAM_CHECKLIST_ITEM_RESULT)
            .sortField("item_id").build();

    @Autowired
    public MohBsbAdhocChecklistDelegator(InspectionClient inspectionClient, HcsaChecklistClient hcsaChecklistClient) {
        this.inspectionClient = inspectionClient;
        this.hcsaChecklistClient = hcsaChecklistClient;
    }


    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
//        MaskHelper.taskProcessUnmask(request, KEY_APP_ID, KEY_TASK_ID);
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_INSPECTION, AuditTrailConsts.FUNCTION_ADHOC_CHECKLIST);

        ParamUtil.setSessionAttr(request, KEY_ADHOC_CHECKLIST_ACTION_FLAG, IaisEGPConstant.YES);

        HttpSession session = request.getSession();
        //TODO need remove
        session.removeAttribute(KEY_INSPECTION_CONFIG);
    }

    public void prepareDisplay(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(bpc.request, KEY_APP_ID);

        ChecklistConfigDto checklistConfigDto = (ChecklistConfigDto) ParamUtil.getSessionAttr(request, KEY_INSPECTION_CONFIG);
        if (checklistConfigDto == null) {
            checklistConfigDto = inspectionClient.getMaxVersionChecklistConfig(appId, HcsaChecklistConstants.INSPECTION);
            ParamUtil.setSessionAttr(request, KEY_INSPECTION_CONFIG, checklistConfigDto);
        }
    }

    public void displayAction(BaseProcessClass bpc) {
    }

    public void prepareItemPool(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        log.debug("receive item by item pool ==>>> start");

        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        AdhocChecklistConfigDto config = getAdhocChecklistConfigInSession(request);
        filterExistAdhocItem(searchParam, config);
        QueryHelp.setMainSql("hcsaconfig", "queryChecklistItem", searchParam);
        SearchResult<CheckItemQueryDto> searchResult = hcsaChecklistClient.listChklItem(searchParam).getEntity();
        ParamUtil.setRequestAttr(request, HcsaChecklistConstants.PARAM_CHECKLIST_ITEM_RESULT, searchResult);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_CHECKLIST_ITEM_SEARCH, searchParam);
        log.debug("receive item by item pool ==>>> start");
    }

    public void removeAdhocItem(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String value = ParamUtil.getMaskedString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
        AdhocChecklistConfigDto config = getAdhocChecklistConfigInSession(request);
        List<AdhocChecklistItemDto> allAdhocItem = config.getAdhocChecklistItemList();
        int idx = Integer.parseInt(value);
        for (int i = 0; i < allAdhocItem.size(); i++) {
            if (i == idx) {
                allAdhocItem.remove(i);
            }
        }
        if (allAdhocItem.isEmpty()) {
            ParamUtil.setSessionAttr(request, KEY_ADHOC_CHECKLIST_LIST_ATTR, null);
        } else {
            ParamUtil.setSessionAttr(request, KEY_ADHOC_CHECKLIST_LIST_ATTR, config);
        }
    }

    public void cancel(BaseProcessClass bpc) {
        ParamUtil.setSessionAttr(bpc.request, KEY_ADHOC_CHECKLIST_ACTION_FLAG, IaisEGPConstant.NO);
    }

    public void saveAdhocItem(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        AdhocChecklistConfigDto adhocConfig = getAdhocChecklistConfigInSession(request);
        boolean hasSampleItem = hasDuplicateChecklistItem(adhocConfig);
        if (hasSampleItem) {
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr("checklistItem", MessageUtil.getMessageDesc("CHKL_ERR047")));
        } else {
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
            String appId = (String) ParamUtil.getSessionAttr(bpc.request, KEY_APP_ID);
            adhocConfig.setApplicationId(appId);
            // should move to Pre
            inspectionClient.saveAdhocChecklistConfig(adhocConfig);
            ParamUtil.setSessionAttr(request, KEY_ADHOC_CHECKLIST_LIST_ATTR, adhocConfig);
            ParamUtil.setSessionAttr(bpc.request, KEY_ADHOC_CHECKLIST_ACTION_FLAG, IaisEGPConstant.NO);
        }
    }

    public void changePage(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        CrudHelper.doPaging(searchParam, bpc.request);
    }

    public void sort(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        CrudHelper.doSorting(searchParam, bpc.request);
    }

    public void appendItem(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String[] checkBoxItemId = ParamUtil.getStrings(request, HcsaChecklistConstants.PARAM_CHKL_ITEM_CHECKBOX);
        if (checkBoxItemId == null || checkBoxItemId.length == 0) {
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr("checklistItem", "please select item!"));
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            return;
        }
        List<ChecklistItemDto> itemList = hcsaChecklistClient.listChklItemByItemId(Arrays.asList(checkBoxItemId)).getEntity();
        AdhocChecklistConfigDto adhocConfig = getAdhocChecklistConfigInSession(request);
        appendChecklistItem(itemList, adhocConfig);
        ParamUtil.setSessionAttr(request, KEY_ADHOC_CHECKLIST_LIST_ATTR, adhocConfig);
        ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
    }

    public void back(BaseProcessClass bpc) {
    }

    public void customAction(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;

        String action = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if (!"customItem".equals(action)) {
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
            return;
        }

        String question = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CHECKLIST_ITEM);
        AdhocChecklistItemDto adhocItem = new AdhocChecklistItemDto();
        adhocItem.setQuestion(question);

        ValidationResult validationResult = WebValidationHelper.validateProperty(adhocItem, "customAdd");
        if (validationResult != null && validationResult.isHasErrors()) {
            Map<String, String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            return;
        }

        AdhocChecklistConfigDto config = getAdhocChecklistConfigInSession(request);
        List<AdhocChecklistItemDto> adhocItemList = config.getAdhocChecklistItemList();
        Optional<AdhocChecklistItemDto> optional =
                adhocItemList.stream().
                        filter(i -> i.getQuestion().equals(question))
                        .findFirst();

        if (!optional.isPresent()) {
            adhocItemList.add(adhocItem);
            config.setAdhocChecklistItemList(adhocItemList);
            ParamUtil.setSessionAttr(request, KEY_ADHOC_CHECKLIST_LIST_ATTR, config);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
        } else {
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr("customItemError", MessageUtil.getMessageDesc("CHKL_ERR047")));
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
        }
    }

    private AdhocChecklistConfigDto getAdhocChecklistConfigInSession(HttpServletRequest request) {
        AdhocChecklistConfigDto obj = (AdhocChecklistConfigDto) ParamUtil.getSessionAttr(request, KEY_ADHOC_CHECKLIST_LIST_ATTR);
        if (obj == null) {
            obj = new AdhocChecklistConfigDto();
            List<AdhocChecklistItemDto> allAdhocItem = IaisCommonUtils.genNewArrayList();
            obj.setAdhocChecklistItemList(allAdhocItem);
            return obj;
        } else {
            return obj;
        }
    }

    private boolean hasDuplicateChecklistItem(AdhocChecklistConfigDto adhocConfig) {
        Set<String> hashSet = IaisCommonUtils.genNewHashSet();
        List<AdhocChecklistItemDto> itemList = adhocConfig.getAdhocChecklistItemList();
        for (AdhocChecklistItemDto item : itemList) {
            String question = item.getQuestion();
            if (!hashSet.add(question)) {
                return true;
            }
        }
        return false;
    }

    private void filterExistAdhocItem(SearchParam searchParam, AdhocChecklistConfigDto config) {
        if (Optional.ofNullable(config).isPresent()) {
            List<AdhocChecklistItemDto> itemList = config.getAdhocChecklistItemList();
            log.debug("indicates that a record has been selected ");
            if (IaisCommonUtils.isNotEmpty(itemList)) {
                itemList.removeIf(i -> StringUtil.isEmpty(i.getItemId()));
                SqlHelper.builderNotInSql(searchParam, "item.id", "adhocItemId",
                        itemList.stream().map(AdhocChecklistItemDto::getItemId).collect(Collectors.toList()));
            }
        }
    }

    private AdhocChecklistItemDto checklistItemDtoConvertAdhocChecklistItemDto(ChecklistItemDto checklistItemDto) {
        AdhocChecklistItemDto adhocChecklistItemDto = new AdhocChecklistItemDto();
        adhocChecklistItemDto.setItemId(checklistItemDto.getItemId());
        adhocChecklistItemDto.setQuestion(checklistItemDto.getChecklistItem());
        return adhocChecklistItemDto;
    }

    private void appendChecklistItem(List<ChecklistItemDto> itemList, AdhocChecklistConfigDto adhocConfig) {
        adhocConfig.getAdhocChecklistItemList().addAll(itemList.stream().map(this::checklistItemDtoConvertAdhocChecklistItemDto).collect(Collectors.toList()));
    }
}
